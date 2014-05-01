/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.communicator.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.accessor.ConnectNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * DataCollector���N���C�A���g����ڑ����ꂽ�Ƃ��ɋN�������N���C�A���g�X���b�h�B
 * <p>�{�X���b�h���N������ƁA�ڑ����ʒm�d���̎�M�҂���ԂƂȂ�B�N���C�A���g
 * ����̍ŏ��̓d���͐ڑ����ʒm�łȂ���΂Ȃ炸�A����ȊO�̓d������M���Ă�
 * ���ׂĔj������B�܂��A�ڑ����ʒm�d������M������́A
 * {@link DataCollectorClient#setEnabled(boolean)}���R�[������Đڑ���Ԃ�
 * �L���ƂȂ�܂ŁA�d������M�����ɑؗ�������B</p>
 * 
 * @author matsuoka
 *
 */
public class DataCollectorClient implements Runnable, TelegramSender
{
    private static final long WAIT_FOR_CONNECT_NOTIFY_SLEEP_TIME = 100;

    private JavelinClientConnection clientConnection_;

    private boolean isRunning_;

    private boolean isWaitForConnectNotify_ = false;

    private boolean isEnabled_ = false;

    private ConnectNotifyData notifyData_;

    private String ipAddr_;

    private String hostName_;

    private int port_;

    private String clientId_;

    /** DB���P�ʂŃ��j�[�N��DB�ԍ� */
    private int dbNo_ = 0;
    
    /** DB�� */
    private String agentName_ = null;

    /** �d�������N���X�̃��X�g */
    private final List<TelegramListener> telegramListenerList_ = new ArrayList<TelegramListener>();

    /** JavelinClientThread�̏�ԕω���ʒm���郊�X�i */
    private DataCollectorClientListener clientListener_;

    /**
     * {@link DataCollectorClient}�̏�ԕω���ʒm���郊�X�i�B
     * 
     * <p>�ėp���̂Ȃ� {@link DataCollectorClient} ��p�̃R�[���o�b�N�C���^�[�t�F�C�X
     * �ł��邽�߁A�����C���^�[�t�F�C�X�Ƃ��Ē�`����B</p>
     * 
     * @author matsuoka
     */
    interface DataCollectorClientListener
    {
        /**
         * �ڑ����ʒm����M�������ɃR�[�������B
         * <p>�������\�b�h�ł́A�ڑ����ʒm�̏ڍׂ��m�F����
         * {@link DataCollectorClient#setEnabled(boolean)}��
         * {@link DataCollectorClient#stop()}���R�[�����Ȃ���΂Ȃ�Ȃ��B</p>
         * 
         * @param client �N���C�A���g�C���X�^���X
         */
        void receiveConnectNotify(DataCollectorClient client);

        /**
         * �ʐM�ؒf���ɃR�[�������B
         * @param client �N���C�A���g�C���X�^���X
         * @param forceDisconnected �����ؒf���ꂽ�ꍇ�� <code>true</code>
         */
        void disconnected(DataCollectorClient client, boolean forceDisconnected);
    }

    /**
     * JavelinClient�R�l�N�V�����̊J�n�Ɠd���N���X�̓o�^���s���܂��B<br />
     * 
     * @param objSocket �\�P�b�g
     * @param discard discard
     * @param clientId �N���C�A���gID
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    public DataCollectorClient(final Socket objSocket, final boolean discard, String clientId)
        throws IOException
    {
        InetAddress addr = objSocket.getInetAddress();
        ipAddr_ = addr.getHostAddress();
        hostName_ = addr.getHostName();
        port_ = objSocket.getPort();
        clientId_ = clientId;

        this.clientConnection_ = new JavelinClientConnection(objSocket, discard);
    }

    /**
     * 
     */
    public void run()
    {
        try
        {
            // ���M�X���b�h���J�n����B
            startSendThread();

            isRunning_ = true;
            while (isRunning_)
            {
                try
                {
                    while (isWaitForConnectNotify_ && clientConnection_.isConnected())
                    {
                        // �ڑ����ʒm�d������M������́AsetEnabled()��
                        // �R�[�������܂�sleep����B
                        Thread.sleep(WAIT_FOR_CONNECT_NOTIFY_SLEEP_TIME);
                    }

                    // �v������M����B
                    byte[] byteInputArr = null;
                    byteInputArr = clientConnection_.recvRequest();

                    // byte���Telegram�ɕϊ�����B
                    Telegram request = TelegramUtil.recoveryTelegram(byteInputArr);

                    if (request == null)
                    {
                        continue;
                    }
                    if (SystemLogger.getInstance().isDebugEnabled())
                    {
                        logReceiveTelegram(request, byteInputArr);
                    }

                    receiveTelegram(request);
                }
                catch (SocketTimeoutException ste)
                {
                    SystemLogger.getInstance().debug(ste);
                }
            }
        }
        catch (Exception exception)
        {
            String key = "javelin.communicate.commonMessage.receiveTelegramError";
            SystemLogger.getInstance().warn(CommunicatorMessages.getMessage(key), exception);
        }
        finally
        {
            boolean forceDisconnected = false;
            if (this.isRunning_)
            {
                forceDisconnected = true;
            }

            this.isRunning_ = false;
            this.clientConnection_.close();

            if (this.clientListener_ != null)
            {
                clientListener_.disconnected(this, forceDisconnected);
            }
        }
    }

    private void startSendThread()
    {
        JavelinClientSendRunnable clientSendRunnable =
                new JavelinClientSendRunnable(this.clientConnection_);
        String threadName = Thread.currentThread().getName() + "-Send";
        Thread clientSendThread = new Thread(clientSendRunnable, threadName);
        clientSendThread.setDaemon(true);
        clientSendThread.start();
    }

    /**
     * �d�������ɗ��p����TelegramListener��o�^����
     * 
     * @param listenerList �d�������ɗ��p����TelegramListener�̃��X�g
     */
    public void setTelegramListener(final List<TelegramListener> listenerList)
    {
        for (TelegramListener listener : listenerList)
        {
            addTelegramListener(listener);
        }
    }

    /**
     * �d�������ɗ��p����TelegramListener��o�^����
     * 
     * @param listener �d�������ɗ��p����TelegramListener
     */
    public void addTelegramListener(final TelegramListener listener)
    {
        synchronized (this.telegramListenerList_)
        {
            this.telegramListenerList_.add(listener);
        }
    }

    /**
     * �d������M���A�����d��������Ƃ��̂ݓd���𑗐M���܂��B<br />
     * 
     * @param request �擾�d��
     */
    protected void receiveTelegram(final Telegram request)
    {
        if (!isEnabled_)
        {
            // �R�l�N�V�������L���ƂȂ�O�̏���
            boolean result = processConnectNotify(request);
            if (result)
            {
                isWaitForConnectNotify_ = true;
                clientListener_.receiveConnectNotify(this);
            }
            return;
        }

        // �eTelegramListener�ŏ������s��
        for (TelegramListener listener : this.telegramListenerList_)
        {
            try
            {
                Telegram response = listener.receiveTelegram(request);

                // �����d��������ꍇ�̂݁A������Ԃ�
                if (response == null)
                {
                    continue;
                }
                List<byte[]> byteList = TelegramUtil.createTelegram(response);
                for (byte[] byteOutputArr : byteList)
                {
                    this.clientConnection_.sendAlarm(byteOutputArr);
                    if (SystemLogger.getInstance().isDebugEnabled())
                    {
                        logTelegram(response, byteOutputArr);
                    }
                }
            }
            catch (Throwable th)
            {
                SystemLogger.getInstance().warn(th);
            }
        }
    }

    /**
     * �ڑ����ʒm����������B
     * <p>��M�����d�����ڑ����ʒm�ł���΁A
     * {@link DataCollectorClientListener#receiveConnectNotify(ConnectNotifyData)}��
     * �R�[�����A<code>true</code>��Ԃ��B����ȊO�̓d���ł����
     * <code>false</code>��Ԃ��B</p>
     * 
     * @param request ��M�����d��
     * @return ��M�����d���������ł�����<code>true</code>��Ԃ��B
     */
    private boolean processConnectNotify(Telegram request)
    {
        Header header = request.getObjHeader();
        byte reqKind = header.getByteRequestKind();
        byte telKind = header.getByteTelegramKind();
        if (reqKind == TelegramConstants.BYTE_REQUEST_KIND_NOTIFY
                && telKind == TelegramConstants.BYTE_TELEGRAM_KIND_CONNECT_NOTIFY)
        {
            notifyData_ = ConnectNotifyAccessor.getConnectNotifyData(request);
        }
        else
        {
            return false;
        }
        return true;
    }

    /**
     * �X���b�h���~����B
     */
    public void stop()
    {
        this.isRunning_ = false;
    }

    /**
     * �ʐM���N���[�Y���Ă��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return �ʐM���N���[�Y���Ă���ꍇ�A<code>true</code>
     */
    public boolean isClosed()
    {
        return this.clientConnection_.isClosed();
    }

    /**
     * �A���[���𑗐M���܂��B<br />
     * 
     * @param bytes �d���̃o�C�g��
     */
    public void sendAlarm(final byte[] bytes)
    {
        this.clientConnection_.sendAlarm(bytes);
    }

    /**
     * �d���̃��O���f�o�b�O���x���ŕ\�����܂��B<br />
     * 
     * @param telegram �d��
     * @param bytes �o�C�g��
     */
    public void logTelegram(final Telegram telegram, final byte[] bytes)
    {
        String key = "javelin.communicate.commonMessage.sendTelegram";
        SystemLogger.getInstance().debug(CommunicatorMessages.getMessage(key, telegram, bytes));
    }

    /**
     * ��M�d���̃��O���f�o�b�O���x���ŕ\�����܂��B<br />
     * 
     * @param telegram �d��
     * @param bytes �o�C�g��
     */
    public void logReceiveTelegram(final Telegram telegram, final byte[] bytes)
    {
        String key = "javelin.communicate.commonMessage.receiveTelegram";
        SystemLogger.getInstance().debug(CommunicatorMessages.getMessage(key, telegram, bytes));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isConnected()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void sendTelegram(Telegram telegram)
    {
        if (telegram == null)
        {
            return;
        }

        List<byte[]> byteList = TelegramUtil.createTelegram(telegram);
        for (byte[] bytes : byteList)
        {
            sendAlarm(bytes);
        }                    
        
    }

    /**
     * �ڑ��̏�Ԃ���Ԃ��B
     * @return �ڑ����L���ł����<code>true</code>��Ԃ��B
     */
    public boolean isEnabled()
    {
        return isEnabled_;
    }

    /**
     * �ڑ��̏�Ԃ�ݒ肷��B
     * @param enabled �ڑ��̏��
     */
    public void setEnabled(boolean enabled)
    {
        isEnabled_ = enabled;
        isWaitForConnectNotify_ = false;
    }

    /**
     * {@link DataCollectorClient}�̏�ԕω���ʒm���邽�߂̃��X�i��o�^����B
     * @param listener �o�^���郊�X�i
     */
    public void setClientListener(DataCollectorClientListener listener)
    {
        clientListener_ = listener;
    }

    /**
     * �ڑ��ʒm�����擾����B
     * @return �ڑ��ʒm����Ԃ��B
     */
    public ConnectNotifyData getConnectNotifyData()
    {
        return notifyData_;
    }


    /**
     * DB�ԍ����擾����B
     * @return dbNo DB�ԍ�
     */
    public int getDbNo()
    {
        return dbNo_;
    }

    /**
     * DB�ԍ���ݒ肷��A
     * @param dbNo DB�ԍ�
     */
    public void setDbNo(int dbNo)
    {
        dbNo_ = dbNo;
    }

    /**
     * DB�����擾����B
     * @return DB��
     */
    public String getAgentName()
    {
        return this.agentName_;
    }

    /**
     * IP�A�h���X���擾����B
     * @return ipAddr IP�A�h���X
     */
    public String getIpAddr()
    {
        return ipAddr_;
    }

    /**
     * �z�X�g�����擾����B
     * @return hostName �z�X�g��
     */
    public String getHostName()
    {
        return hostName_;
    }

    /**
     * �|�[�g�ԍ����擾����B
     * @return port �|�[�g�ԍ�
     */
    public int getPort()
    {
        return port_;
    }

    /**
     * �N���C�A���gID���擾����B
     * @return clientId �N���C�A���gID
     */
    public String getClientId()
    {
        return clientId_;
    }

    /**
     * DB����ݒ肷��B
     * 
     * @param dbName DB��
     */
    public void setAgentName(String dbName)
    {
        this.agentName_ = dbName;
    }
}
