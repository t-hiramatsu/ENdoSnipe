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
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * Javelin�̃N���C�A���g�X���b�h�ł�<br />
 * 
 * @author eriguchi
 *
 */
public class JavelinClientThread implements Runnable
{
    private JavelinClientConnection clientConnection_;

    private boolean isRunning_;

    /** �d�������N���X�̃��X�g */
    private final List<TelegramListener> telegramListenerList_ = new ArrayList<TelegramListener>();
    
    /** JavelinClientThread�̏�ԕω���ʒm���郊�X�i */
    private JavelinClientThreadListener clientListener_;

    /**
     * JavelinClientThread�̏�ԕω���ʒm���郊�X�i�B
     * 
     * <p>�ėp���̂Ȃ� {@link JavelinClientThread} ��p�̃R�[���o�b�N�C���^�[�t�F�C�X
     * �ł��邽�߁A�����C���^�[�t�F�C�X�Ƃ��Ē�`����B</p>
     * 
     * @author matsuoka
     */
    interface JavelinClientThreadListener 
    {
        /**
         * �ʐM�ؒf���ɃR�[�������B
         *
         * @param forceDisconnected �����ؒf���ꂽ�ꍇ�� <code>true</code>
         */
        void disconnected(boolean forceDisconnected);
    }

    /**
     * JavelinClient�R�l�N�V�����̊J�n�Ɠd���N���X�̓o�^���s���܂��B<br />
     * 
     * @param objSocket �\�P�b�g
     * @param discard �A���[�����M�Ԋu���ɔ������������A���[����j�����邩�ǂ���
     * @param listeners ���p����TelegramListener��
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    public JavelinClientThread(final Socket objSocket, final boolean discard,
            final String[] listeners)
        throws IOException
    {
        this.clientConnection_ = new JavelinClientConnection(objSocket, discard);

        // �d�������N���X��o�^����
        registerTelegramListeners(listeners);
    }
    
    /**
     * JavelinClient�R�l�N�V�����̊J�n�Ɠd���N���X�̓o�^���s���܂��B<br />
     * 
     * @param objSocket �\�P�b�g
     * @param discard �A���[�����M�Ԋu���ɔ������������A���[����j�����邩�ǂ���
     * @param listeners ���p����TelegramListener��
     * @param clientLisener JavelinClientThread�̏�Ԃ�ʒm���邽�߂̃��X�i
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    public JavelinClientThread(final Socket objSocket, final boolean discard,
            final String[] listeners, JavelinClientThreadListener clientLisener)
        throws IOException
    {
        this(objSocket, discard, listeners);
        this.clientListener_ = clientLisener;
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

            this.isRunning_ = true;
            while (this.isRunning_)
            {
                try
                {
                    // �v������M����B
                    byte[] byteInputArr = null;
                    byteInputArr = this.clientConnection_.recvRequest();

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

                    this.receiveTelegram(request);
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
                clientListener_.disconnected(forceDisconnected);
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
     * @param listener �d�������ɗ��p����TelegramListener
     */
    public void addListener(final TelegramListener listener)
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
        // �eTelegramListener�ŏ������s��
        for (TelegramListener listener : this.telegramListenerList_)
        {
            try
            {
                Telegram response = listener.receiveTelegram(request);

                // �����d��������ꍇ�̂݁A������Ԃ�
                if (response != null)
                {
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
            }
            catch (Throwable th)
            {
                SystemLogger.getInstance().warn(th);
            }
        }
    }

    /**
     * TelegramListener�̃N���X��Javelin�ݒ肩��ǂݍ��݁A�o�^����B �N���X�̃��[�h�́A�ȉ��̏��ŃN���X���[�_�ł̃��[�h�����݂�B
     * <ol> <li>JavelinClientThread�����[�h�����N���X���[�_</li> <li>�R���e�L�X�g�N���X���[�_</li>
     * </ol>
     * 
     * @param listeners ���p����TelegramListener��
     */
    private void registerTelegramListeners(final String[] listeners)
    {
        String key = "";
        String message = "";
        for (String listenerName : listeners)
        {
            try
            {
                if ("".equals(listenerName))
                {
                    continue;
                }

                Class<?> listenerClass = loadClass(listenerName);
                Object listener = listenerClass.newInstance();
                if (listener instanceof TelegramListener)
                {
                    addListener((TelegramListener)listener);

                    key = "javelin.communicate.JavelinClientThread.regist";
                    message = CommunicatorMessages.getMessage(key, listenerName);
                    SystemLogger.getInstance().info(message);
                }
                else
                {
                    key = "javelin.communicate.JavelinClientThread.notImplement";
                    message = CommunicatorMessages.getMessage(key, listenerName);
                    SystemLogger.getInstance().info(message);
                }
            }
            catch (Exception ex)
            {
                key = "javelin.communicate.JavelinClientThread.registError";
                message = CommunicatorMessages.getMessage(key, listenerName);
                SystemLogger.getInstance().warn(message, ex);
            }
        }
    }

    /**
     * �N���X�����[�h����B �ȉ��̏��ŃN���X���[�_�ł̃��[�h�����݂�B <ol> <li>JavelinClientThread�����[�h�����N���X���[�_</li>
     * <li>�R���e�L�X�g�N���X���[�_</li> </ol>
     * 
     * @param className ���[�h����N���X�̖��O�B
     * @return ���[�h�����N���X�B
     * @throws ClassNotFoundException �S�ẴN���X���[�_�ŃN���X��������Ȃ��ꍇ
     */
    private Class<?> loadClass(final String className)
        throws ClassNotFoundException
    {

        Class<?> clazz;
        try
        {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException cnfe)
        {
            String key = "javelin.communicate.JavelinClientThread.loadError";
            String message = CommunicatorMessages.getMessage(key, className);
            SystemLogger.getInstance().info(message);
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        }

        return clazz;
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
}
