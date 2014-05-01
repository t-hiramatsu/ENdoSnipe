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
package jp.co.acroquest.endosnipe.collector;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.data.JavelinConnectionData;
import jp.co.acroquest.endosnipe.collector.listener.AllNotifyListener;
import jp.co.acroquest.endosnipe.collector.listener.CommonResponseListener;
import jp.co.acroquest.endosnipe.collector.listener.JvnFileNotifyListener;
import jp.co.acroquest.endosnipe.collector.listener.SignalChangeListener;
import jp.co.acroquest.endosnipe.collector.listener.SignalStateListener;
import jp.co.acroquest.endosnipe.collector.listener.SystemResourceListener;
import jp.co.acroquest.endosnipe.collector.listener.TelegramNotifyListener;
import jp.co.acroquest.endosnipe.collector.transfer.JavelinTransferServerThread;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.NetworkUtil;
import jp.co.acroquest.endosnipe.communicator.AbstractCommunicator;
import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.CommunicationFactory;
import jp.co.acroquest.endosnipe.communicator.CommunicatorListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramReceiver;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.service.HostInfoManager;

/**
 * Javelin ����f�[�^����M���邽�߂̃N���C�A���g�ł��B<br />
 * 
 * @author y-komori
 */
public class JavelinClient implements CommunicatorListener, LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(JavelinClient.class);

    private String databaseName_;

    /** Javelin �����삵�Ă���z�X�g���܂��� IP �A�h���X */
    private String javelinHost_;

    /** Javelin �ւ̐ڑ��|�[�g�ԍ� */
    private int javelinPort_;

    /** BottleneckEye �҂��󂯃|�[�g�ԍ� */
    private int acceptPort_;

    private CommunicationClient client_;

    private List<TelegramNotifyListener> telegramNotifyListenerList_;

    private String clientId_;

    private final JavelinTransferServerThread transferThread_ = new JavelinTransferServerThread();

    /** �f�[�^��~�ς��邽�߂̃L���[ */
    private JavelinDataQueue queue_;

    /**
     * �R���X�g���N�^�B
     */
    public JavelinClient()
    {
        this.telegramNotifyListenerList_ = Collections.emptyList();
    }

    /**
     * �N���C�A���g�̐ڑ��ݒ���s���܂��B<br />
     *
     * @param databaseName �f�[�^�x�[�X��
     * @param javelinHost �ڑ��� Javelin �̃z�X�g���܂��� IP �A�h���X
     * @param javelinPort �ڑ��� Javelin �̃|�[�g�ԍ�
     * @param acceptPort BottleneckEye ����̐ڑ��҂��󂯃|�[�g�ԍ�
     */
    public void init(final String databaseName, final String javelinHost, final int javelinPort,
            final int acceptPort)
    {
        this.databaseName_ = databaseName;
        this.javelinHost_ = javelinHost;
        this.javelinPort_ = javelinPort;
        this.acceptPort_ = acceptPort;
        this.clientId_ = JavelinClient.createClientIdFromHost(javelinHost, javelinPort);
    }

    /**
     * �N���C�A���g�ɒʒm���邽�߂̃��X�i��o�^���܂��B
     * �o�^�Ɠ����ɁA���X�i�ɑ΂��ēd�����M�I�u�W�F�N�g���Z�b�g���܂��B
     *
     * @param notifyListenerList �N���C�A���g�ɒʒm���邽�߂̃��X�i
     */
    public void setTelegramNotifyListener(final List<TelegramNotifyListener> notifyListenerList)
    {
        if (notifyListenerList != null)
        {
            this.telegramNotifyListenerList_ = notifyListenerList;
            setTelegramSenders();
        }
        else
        {
            this.telegramNotifyListenerList_ = Collections.emptyList();
        }
    }

    /**
     * �N���C�A���g���J�n���܂��B<br />
     *
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param connectNotify �ڑ�������ɑ��M����ڑ��ʒm���
     */
    public synchronized void connect(final JavelinDataQueue queue,
            final ConnectNotifyData connectNotify)
    {
        connect(queue, BehaviorMode.CONNECT_MODE, connectNotify);
    }

    /**
     * �N���C�A���g���J�n���܂��B<br />
     *
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param behaviorMode �T�[�r�X���[�h
     * @param connectNotify �ڑ�������ɑ��M����ڑ��ʒm���
     */
    public synchronized void connect(final JavelinDataQueue queue, final BehaviorMode behaviorMode,
            final ConnectNotifyData connectNotify)
    {
        this.queue_ = queue;

        if (isConnected() == true)
        {
            // ���ɐڑ����̏ꍇ
            LOGGER.log(JAVELIN_ALREADY_CONNECTED, this.javelinHost_, this.javelinPort_);
            return;
        }

        String hostName = null;
        if (NetworkUtil.isIpAddress(this.javelinHost_) == false)
        {
            hostName = this.javelinHost_;
        }

        initializeClient(queue, behaviorMode, hostName, connectNotify);
    }

    /**
     * �T�[�o����ؒf���܂��B<br />
     */
    public synchronized void disconnect()
    {
        if (this.client_ != null)
        {
            // �ؒf��\���f�[�^���L���[�ɒǉ�����
            Date currentDate = new Date();
            long currentTime = currentDate.getTime();
            JavelinConnectionData disconnectionData =
                                                      new JavelinConnectionData(
                                                                                JavelinConnectionData.TYPE_DISCONNECTION);
            disconnectionData.measurementTime = currentTime;
            disconnectionData.setDatabaseName(JavelinClient.this.databaseName_);
            if (this.queue_ != null)
            {
                this.queue_.offer(disconnectionData);
            }
            transferThread_.stop();
        }

        if (this.client_ != null)
        {
            this.client_.disconnect();
            this.client_.shutdown();
        }
    }

    /**
     * Javelin �֐ڑ������ǂ�����Ԃ��܂��B<br />
     * 
     * @return �ڑ����̏ꍇ�� <code>true</code>
     */
    public synchronized boolean isConnected()
    {
        if (this.client_ != null)
        {
            return this.client_.isConnected();
        }
        return false;
    }

    /**
     * �f�[�^�x�[�X����Ԃ��܂��B<br />
     *
     * @return �f�[�^�x�[�X��
     */
    public String getDatabaseName()
    {
        return this.databaseName_;
    }

    /**
     * �N���C�A���g�����ʂ��� ID ��Ԃ��܂��B<br />
     * 
     * @return �N���C�A���gID
     */
    public String getClientId()
    {
        return this.clientId_;
    }

    /**
     * �z�X�g���ƃ|�[�g�ԍ�����N���C�A���g ID �𐶐����܂��B<br />
     * 
     * @param host �z�X�g��
     * @param port �|�[�g�ԍ�
     * @return �N���C�A���g ID
     */
    public static String createClientIdFromHost(final String host, final int port)
    {
        String ipAddress = host;
        try
        {
            InetAddress address = InetAddress.getByName(host);
            ipAddress = address.getHostAddress();
        }
        catch (UnknownHostException ex)
        {
            LOGGER.warn(CommonLogMessageCodes.UNEXPECTED_ERROR, ex);
        }

        return createClientId(ipAddress, port);
    }

    /**
     * IP�A�h���X�ƃ|�[�g�ԍ�����N���C�A���g ID �𐶐����܂��B<br />
     * 
     * @param ipAddr IP�A�h���X
     * @param port �|�[�g�ԍ�
     * @return �N���C�A���g ID
     */
    public static String createClientId(final String ipAddr, final int port)
    {
        return ipAddr + ":" + port;
    }

    /**
     * {@inheritDoc}
     */
    public void clientConnected(final String hostName, final String ipAddress, final int port)
    {
        LOGGER.log(JAVELIN_CONNECTED, this.javelinHost_, this.javelinPort_);
    }

    /**
     * {@inheritDoc}
     */
    public void clientDisconnected(final boolean forceDisconnected)
    {
        LOGGER.log(JAVELIN_DISCONNECTED, this.javelinHost_, this.javelinPort_);
    }

    private void setTelegramSenders()
    {
        for (TelegramNotifyListener notifyListener : this.telegramNotifyListenerList_)
        {
            notifyListener.setTelegramSender(getTelegramSender());
        }
    }

    /**
     * �d�����M�I�u�W�F�N�g���擾���܂��B
     * @return �d�����M�I�u�W�F�N�g
     */
    public TelegramSender getTelegramSender()
    {
        return getCommunicator();
    }

    /**
     * �d����M�I�u�W�F�N�g���擾���܂��B
     * @return �d����M�I�u�W�F�N�g
     */
    private TelegramReceiver getTelegramReceiver()
    {
        return getCommunicator();
    }

    private void addResponseTelegramListener(final byte telegramKind)
    {
        CommonResponseListener listener = new CommonResponseListener(this.queue_, telegramKind);
        getTelegramReceiver().addTelegramListener(listener);
    }

    /**
     * BottleneckEye �ɁA DataCollector ���ϊ������f�[�^��ʒm���܂��B
     *
     * @param telegram �d��
     */
    public void sendTelegramToClient(final Telegram telegram)
    {
        // �v���O�C�����[�h�̏ꍇ�͒ʒm���A�����łȂ��ꍇ�̓N���C�A���g�ɓd�����M����B
        if (this.acceptPort_ == -1)
        {
            for (TelegramNotifyListener notifyListener : this.telegramNotifyListenerList_)
            {
                if (notifyListener.isRawTelegramNeeded() == false)
                {
                    notifyListener.receiveTelegram(telegram);
                }
            }
        }
        else
        {
            this.transferThread_.sendTelegram(telegram);
        }
    }

    /**
     * �N���C�A���g���쎞�̏����������B
     * 
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param alarmRepository �A���[��
     * @param behaviorMode �T�[�r�X���[�h
     * @param hostName �z�X�g��
     * @param connectNotify �ڑ�������ɑ��M����ڑ��ʒm���
     */
    private synchronized void initializeClient(final JavelinDataQueue queue,
            final BehaviorMode behaviorMode, final String hostName,
            final ConnectNotifyData connectNotify)
    {
        this.client_ =
                       CommunicationFactory.getCommunicationClient("DataCollector-ClientThread-"
                               + this.clientId_);
        this.client_.init(this.javelinHost_, this.javelinPort_);

        String agentName = connectNotify.getAgentName();
        initializeCommon(queue, behaviorMode, hostName, agentName);

        // �T�[�o�֐ڑ�����(�ڑ��ɐ�������܂Ń��g���C�𑱂���)
        this.client_.connect(connectNotify);
    }

    /**
     * �T�[�o/�N���C�A���g����̋��ʂ̏����������B
     * 
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param alarmRepository �A���[��
     * @param behaviorMode �T�[�r�X���[�h
     * @param hostName �z�X�g��
     */
    private synchronized void initializeCommon(final JavelinDataQueue queue,
            final BehaviorMode behaviorMode, final String hostName, final String agentName)
    {
        setTelegramSenders();

        TelegramReceiver receiver = getTelegramReceiver();

        AllNotifyListener allNotifyListener = new AllNotifyListener();
        allNotifyListener.setTelegramNotifyListener(this.telegramNotifyListenerList_);
        receiver.addTelegramListener(allNotifyListener);

        final JvnFileNotifyListener JVN_FILE_NOTIFY_LISTENER =
                                                               createJvnFileNotifyListener(queue,
                                                                                           hostName);
        final SystemResourceListener SYSTEM_RESOURCE_LISTENER =
                                                                createSystemResourceListener(queue,
                                                                                             hostName,
                                                                                             agentName);

        final SignalStateListener SIGNAL_STATE_LISTENER = new SignalStateListener();
        final SignalChangeListener SIGNAL_CHANGE_LISTENER = new SignalChangeListener();
        if (queue != null)
        {
            receiver.addTelegramListener(JVN_FILE_NOTIFY_LISTENER);
            receiver.addTelegramListener(SYSTEM_RESOURCE_LISTENER);
            receiver.addTelegramListener(SIGNAL_STATE_LISTENER);
            receiver.addTelegramListener(SIGNAL_CHANGE_LISTENER);
            addResponseTelegramListener(TelegramConstants.BYTE_TELEGRAM_KIND_GET_DUMP);
            addResponseTelegramListener(TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_PROPERTY);
        }

        // �N���C�A���g�E�T�[�o�̏�ԕω���ʒm���邽�߂̃��X�i��o�^
        CommunicatorListener listener =
                                        createCommunicatorListener(queue, JVN_FILE_NOTIFY_LISTENER,
                                                                   SYSTEM_RESOURCE_LISTENER);
        getCommunicator().addCommunicatorListener(listener);
        getCommunicator().addCommunicatorListener(this);
    }

    /**
     * �T�[�r�X���[�h���̏����������B
     * @param alarmRepository �A���[��
     */
    private synchronized void initializeForServiceMode()
    {

        // BottleneckEye->DataCollector->Javelin
        this.transferThread_.addTelegramListener(new TelegramListener() {
            public Telegram receiveTelegram(final Telegram telegram)
            {
                getTelegramSender().sendTelegram(telegram);
                return null;
            }
        });

        // Javelin->DataCollector->BottleneckEye
        this.transferThread_.start(this.acceptPort_);
        getTelegramReceiver().addTelegramListener(new TelegramListener() {
            public Telegram receiveTelegram(final Telegram telegram)
            {
                Header header = telegram.getObjHeader();
                if (header.getByteTelegramKind() == TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY
                        && header.getByteRequestKind() == TelegramConstants.BYTE_REQUEST_KIND_RESPONSE)
                {
                    return null;
                }

                transferThread_.sendTelegram(telegram);
                return null;
            }
        });
    }

    /**
     * �N���C�A���g�E�T�[�o�̏�ԕω����󂯎�郊�X�i�𐶐����܂��B
     * 
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param jvnFileNotifyListener Javelin���O����M���邽�߂̃��X�i
     * @param systemResourceListener �V�X�e�����\�[�X�ʒm����M���邽�߂̃��X�i
     * @return �����������X�i
     */
    private CommunicatorListener createCommunicatorListener(final JavelinDataQueue queue,
            final JvnFileNotifyListener jvnFileNotifyListener,
            final SystemResourceListener systemResourceListener)
    {
        // �ڑ��ɐ��������SocketChannel����IP�A�h���X���擾�ł��邽�߁A
        // ���̂Ƃ���JvnFileNotifyListener��IP�A�h���X��o�^����
        CommunicatorListener listener = new CommunicatorListener() {
            public void clientConnected(final String hostName, final String ipAddress,
                    final int port)
            {
                if (queue != null)
                {
                    jvnFileNotifyListener.setIpAddress(ipAddress);
                    systemResourceListener.setIpAddress(ipAddress);
                    HostInfoManager.registerHostInfo(JavelinClient.this.databaseName_, hostName,
                                                     ipAddress, JavelinClient.this.javelinPort_,
                                                     null);
                }

                for (TelegramNotifyListener telegramNotifyListener : JavelinClient.this.telegramNotifyListenerList_)
                {
                    telegramNotifyListener.clientConnected(hostName, ipAddress, port);
                }

                // �ڑ���\���f�[�^���L���[�ɒǉ�����
                Date currentDate = new Date();
                long currentTime = currentDate.getTime();
                JavelinConnectionData connectionData =
                                                       new JavelinConnectionData(
                                                                                 JavelinConnectionData.TYPE_CONNECTION);
                connectionData.measurementTime = currentTime;
                connectionData.setDatabaseName(JavelinClient.this.databaseName_);
                if (queue != null)
                {
                    queue.offer(connectionData);
                }
            }

            public void clientDisconnected(final boolean forceDisconnected)
            {
                if (forceDisconnected)
                {
                    // �ؒf��\���f�[�^���L���[�ɒǉ�����i�����ؒf���ꂽ�ꍇ�j
                    Date currentDate = new Date();
                    long currentTime = currentDate.getTime();
                    JavelinConnectionData disconnectionData =
                                                              new JavelinConnectionData(
                                                                                        JavelinConnectionData.TYPE_DISCONNECTION);
                    disconnectionData.measurementTime = currentTime;
                    disconnectionData.setDatabaseName(JavelinClient.this.databaseName_);
                    if (queue != null)
                    {
                        queue.offer(disconnectionData);
                    }
                }

                for (TelegramNotifyListener telegramNotifyListener : JavelinClient.this.telegramNotifyListenerList_)
                {
                    telegramNotifyListener.clientDisconnected(forceDisconnected);
                }
            }
        };
        return listener;
    }

    /**
     * JvnFileNotifyListener���쐬���܂��B
     * 
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param hostName �ڑ���̃z�X�g��
     * @return �쐬����JvnFileNotifyListener
     */
    private JvnFileNotifyListener createJvnFileNotifyListener(final JavelinDataQueue queue,
            final String hostName)
    {
        JvnFileNotifyListener notifyListener = null;
        if (queue != null)
        {
            notifyListener = new JvnFileNotifyListener(queue);
            notifyListener.setDatabaseName(this.databaseName_);
            notifyListener.setHostName(hostName);
            notifyListener.setPort(this.javelinPort_);
        }
        return notifyListener;
    }

    /**
     * SystemResourceListener���쐬���܂��B
     * 
     * @param queue �f�[�^��~�ς��邽�߂̃L���[
     * @param hostName �ڑ���̃z�X�g��
     * @return �쐬����SystemResourceListener
     */
    private SystemResourceListener createSystemResourceListener(final JavelinDataQueue queue,
            final String hostName, final String agentName)
    {
        SystemResourceListener notifyListener = null;
        if (queue != null)
        {
            notifyListener = new SystemResourceListener(queue);
            notifyListener.setDatabaseName(this.databaseName_);
            notifyListener.setHostName(hostName);
            notifyListener.setPort(this.javelinPort_);
            notifyListener.setAgentName(agentName);
        }
        return notifyListener;
    }

    /**
     * Javelin�ڑ����[�h���猻�ݗL���ȃR�~���j�P�[�^��Ԃ��܂��B
     * @return ���ݗL���ȃR�~���j�P�[�^
     */
    private synchronized AbstractCommunicator getCommunicator()
    {
        AbstractCommunicator communicator;
        communicator = this.client_;
        return communicator;
    }
}
