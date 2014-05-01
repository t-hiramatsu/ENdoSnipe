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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.collector.data.JavelinConnectionData;
import jp.co.acroquest.endosnipe.collector.listener.AllNotifyListener;
import jp.co.acroquest.endosnipe.collector.listener.CommonResponseListener;
import jp.co.acroquest.endosnipe.collector.listener.JvnFileNotifyListener;
import jp.co.acroquest.endosnipe.collector.listener.SignalChangeListener;
import jp.co.acroquest.endosnipe.collector.listener.SignalStateListener;
import jp.co.acroquest.endosnipe.collector.listener.SystemResourceListener;
import jp.co.acroquest.endosnipe.collector.listener.TelegramNotifyListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.accessor.ConnectNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.SystemResourceGetter;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.communicator.impl.DataCollectorClient;
import jp.co.acroquest.endosnipe.communicator.impl.DataCollectorServer;
import jp.co.acroquest.endosnipe.communicator.impl.DataCollectorServer.ClientNotifyListener;
import jp.co.acroquest.endosnipe.data.service.HostInfoManager;

/**
 * Javelin ����f�[�^����M���邽�߂̃T�[�o�ł��B<br />
 * 
 * @author matsuoka
 */
public class JavelinServer implements TelegramSender
{
    /** �T�[�o�C���X�^���X */
    private final DataCollectorServer server_ = new DataCollectorServer();

    /** DB�����L�[�Ƃ���Javelin�N���C�A���g�̃��X�g */
    private final Map<String, DataCollectorClient> javelinClientList_ =
                                                                        new HashMap<String, DataCollectorClient>();

    /** DB�����L�[�Ƃ�������N���C�A���g�̃��X�g */
    private final Map<String, Set<DataCollectorClient>> controlClientList_ =
                                                                             new HashMap<String, Set<DataCollectorClient>>();

    /** DB���̑�����ʒm����N���C�A���g */
    private DataCollectorClient databaseAdminClient_ = null;

    /** �L���[ */
    private JavelinDataQueue queue_;

    /** DB�����L�[�Ƃ����ʒm���X�i�̃}�b�v */
    private Map<String, List<TelegramNotifyListener>> notifyListenerMap_ =
                                                                           new HashMap<String, List<TelegramNotifyListener>>();

    /** ���\�[�X�擾 */
    private SystemResourceGetter resourceGetter_;

    /** ���샂�[�h */
    private BehaviorMode behaviorMode_;

    /** Javelin�N���C�A���g�Ƃ̐ڑ����m������܂œd����ؗ������Ă������߂̃��X�g */
    private final List<Telegram> waitingTelegramList_ = new ArrayList<Telegram>();

    /** �ڑ�����f�[�^�x�[�X���B */
    private String dbName_;

    /**
     * �T�[�o���J�n����B
     * 
     * @param port �|�[�g�ԍ�
     * @param queue �f�[�^�L���[
     * @param dbName �ڑ�����f�[�^�x�[�X���B
     * @param resourceGetter �V�X�e�����\�[�X�擾
     * @param behaviorMode DataCollector�̓��샂�[�h
     */
    public void start(final int port, final JavelinDataQueue queue, final String dbName,
            final SystemResourceGetter resourceGetter, final BehaviorMode behaviorMode)
    {
        dbName_ = dbName;
        queue_ = queue;
        resourceGetter_ = resourceGetter;
        behaviorMode_ = behaviorMode;

        server_.addClientNotifyListener(new ClientNotifyListener() {

            public void clientDisconnected(final DataCollectorClient client,
                    final boolean forceDisconnected)
            {
                ConnectNotifyData notifyData = client.getConnectNotifyData();
                if (notifyData == null)
                {
                    return;
                }

                switch (notifyData.getKind())
                {
                case ConnectNotifyData.KIND_JAVELIN:
                    notifyJavelinDisconnected(dbName_, forceDisconnected);
                    removeJavelinClient(dbName_);
                    notifyDelJavelin(dbName_);
                    break;

                case ConnectNotifyData.KIND_CONTROLLER:
                    removeControlClient(dbName_);
                    break;

                default:
                    break;
                }

            }

            public void clientConnected(final DataCollectorClient client)
            {
                ConnectNotifyData notifyData = client.getConnectNotifyData();
                if (notifyData == null)
                {
                    return;
                }

                switch (notifyData.getKind())
                {
                case ConnectNotifyData.KIND_JAVELIN:
                    addJavelinClient(client);
                    initializeJavelinClient(client);
                    sendWaitingTelegram(client);
                    notifyAddJavelin(dbName_);
                    break;

                case ConnectNotifyData.KIND_CONTROLLER:
                    switch (notifyData.getPurpose())
                    {
                    case ConnectNotifyData.PURPOSE_GET_RESOURCE:
                        addControlClient(client);
                        initializeControlClient(client);
                        break;
                    case ConnectNotifyData.PURPOSE_GET_DATABASE:
                        setDatabaseAdminClient(client);
                        sendDatabaseName();
                        break;
                    }
                    break;

                default:
                    break;
                }
            }
        });

        server_.start(port);
    }

    /**
     * �T�[�o���~����B
     */
    public void stop()
    {
        server_.stop();
    }

    /**
     * �N���C�A���g�ɒʒm���邽�߂̃��X�i��o�^���܂��B
     *
     * @param notifyListenerList �N���C�A���g�ɒʒm���邽�߂̃��X�i
     */
    public void setTelegramNotifyListener(
            final Map<String, List<TelegramNotifyListener>> notifyListenerList)
    {
        if (notifyListenerList != null)
        {
            notifyListenerMap_ = notifyListenerList;

            // �{���\�b�h�̃R�[�����ɂ́A�܂��N���C�A���g�Ɛڑ�����Ă��Ȃ��B
            // ���̂��߁A�ʒm���X�i�̑��M��Ƀ_�~�[��ݒ肷��B
            setDummyTelegramSenders();
        }
        else
        {
            notifyListenerMap_ = new HashMap<String, List<TelegramNotifyListener>>();
        }
    }

    /**
     * �w�肳�ꂽ�N���C�A���gID�̃N���C�A���g���擾����B
     * @param clientId �L�[�ƂȂ�N���C�A���gID
     * @return �w�肳�ꂽ�N���C�A���gID�̃N���C�A���g��Ԃ��B
     */
    public DataCollectorClient getClient(final String clientId)
    {
        return server_.getClient(clientId);
    }

    /**
     * Javelin�N���C�A���g�ɕR�t������N���C�A���g�ɓd���𑗐M����B
     * @param clientId Javelin�N���C�A���g�̃N���C�A���gID
     * @param telegram ���M����d��
     */
    public void sendTelegramToControlClient(final String clientId, final Telegram telegram)
    {
        DataCollectorClient javelinClient = getClient(clientId);
        if (javelinClient == null)
        {
            return;
        }
        ConnectNotifyData notifyData = javelinClient.getConnectNotifyData();
        if (notifyData == null)
        {
            return;
        }
        if (notifyData.getKind() != ConnectNotifyData.KIND_JAVELIN)
        {
            return;
        }

        if (behaviorMode_.equals(BehaviorMode.PLUGIN_MODE))
        {
            // �v���O�C�����[�h�œ��삵�Ă���Ȃ�A���X�i�ɒʒm����B
            String dbName = dbName_;
            List<TelegramNotifyListener> notifyListenerList = notifyListenerMap_.get(dbName);
            if (notifyListenerList != null)
            {
                for (TelegramNotifyListener notifyListener : notifyListenerList)
                {
                    if (notifyListener.isRawTelegramNeeded() == false)
                    {
                        notifyListener.receiveTelegram(telegram);
                    }
                }
            }
        }
        else
        {
            // �T�[�r�X���[�h�œ��삵�Ă���Ȃ�A�ڑ�����Ă��鐧��N���C�A���g��
            // �d���𑗐M����B
            Set<DataCollectorClient> controlClientSet = getControlClient(dbName_);
            if (controlClientSet != null)
            {
                for (DataCollectorClient controlClient : controlClientSet)
                {
                    controlClient.sendTelegram(telegram);
                }
            }
        }

        return;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isConnected()
    {
        return false;
    }

    /**
     * Javelin���ڑ������܂ł̃_�~�[�̓d�����M�����B
     * @param telegram ���M�d��
     */
    public void sendTelegram(final Telegram telegram)
    {
        // �d����ؗ�������B
        waitingTelegramList_.add(telegram);
    }

    /**
     * javelin�̑�����ʒm����
     * @param databaseName DB��
     */
    private void notifyAddJavelin(final String databaseName)
    {
        if (this.databaseAdminClient_ != null)
        {
            Set<String> databaseNameList = new HashSet<String>();
            databaseNameList.add(databaseName);
            databaseAdminClient_.sendTelegram(ConnectNotifyAccessor.createAddDatabaseNameTelegram(databaseNameList));
        }
    }

    /**
     * javelin�̌�����ʒm����
     * @param databaseName DB��
     */
    private void notifyDelJavelin(final String databaseName)
    {
        if (this.databaseAdminClient_ != null)
        {
            Set<String> databaseNameList = new HashSet<String>();
            databaseNameList.add(databaseName);
            databaseAdminClient_.sendTelegram(ConnectNotifyAccessor.createDelDatabaseNameTelegram(databaseNameList));
        }
    }

    /**
     * �ڑ����ꂽJavelin�N���C�A���g������������B
     * @param client Javelin�N���C�A���g
     */
    private void initializeJavelinClient(final DataCollectorClient client)
    {
        setTelegramSenders();

        String dbName = dbName_;
        String hostName = client.getHostName();
        String ipAddress = client.getIpAddr();
        int port = client.getPort();
        String clientId = client.getClientId();
        String agentName = client.getAgentName();

        List<TelegramNotifyListener> listenerList = notifyListenerMap_.get(dbName);
        AllNotifyListener allNotifyListener = new AllNotifyListener();
        allNotifyListener.setTelegramNotifyListener(listenerList);
        client.addTelegramListener(allNotifyListener);

        JvnFileNotifyListener jvnFileNotifyListener =
                                                      createJvnFileNotifyListener(dbName, hostName,
                                                                                  ipAddress, port,
                                                                                  clientId,
                                                                                  agentName);
        SystemResourceListener systemResourceListener =
                                                        createSystemResourceListener(dbName,
                                                                                     hostName,
                                                                                     ipAddress,
                                                                                     port,
                                                                                     clientId,
                                                                                     agentName);

        if (queue_ != null)
        {
            client.addTelegramListener(jvnFileNotifyListener);
            client.addTelegramListener(systemResourceListener);

            client.addTelegramListener(createResponseTelegramListener(TelegramConstants.BYTE_TELEGRAM_KIND_GET_DUMP));
            client.addTelegramListener(createResponseTelegramListener(TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_PROPERTY));

            HostInfoManager.registerHostInfo(dbName, hostName, ipAddress, port, null);

            notifyJavelinConnected(dbName, hostName, ipAddress, port);
        }

        SignalStateListener signalStateListener = new SignalStateListener();
        SignalChangeListener signalChangeListener = new SignalChangeListener();
        client.addTelegramListener(signalStateListener);
        client.addTelegramListener(signalChangeListener);

        // ����N���C�A���g�����݂���Ȃ�AJavelin�N���C�A���g�ƕR�t����B
        Set<DataCollectorClient> controlClientSet = getControlClient(dbName);
        if (controlClientSet != null)
        {
            for (DataCollectorClient controlClient : controlClientSet)
            {
                bindJavelinAndControlClient(client, controlClient);
            }
        }

        resourceGetter_.addTelegramSenderList(client);
    }

    /**
     * ����N���C�A���g��Javelin���ڑ����ꂽ���Ƃ�ʒm����B
     * @param dbName �f�[�^�x�[�X��
     * @param hostName Javelin�̃z�X�g��
     * @param ipAddress Javelin��IP�A�h���X
     * @param port Javelin�̃|�[�g�ԍ�
     */
    private void notifyJavelinConnected(final String dbName, final String hostName,
            final String ipAddress, final int port)
    {
        List<TelegramNotifyListener> listenerList = notifyListenerMap_.get(dbName);
        if (listenerList != null)
        {
            for (TelegramNotifyListener telegramNotifyListener : listenerList)
            {
                telegramNotifyListener.clientConnected(hostName, ipAddress, port);
            }
        }

        // �ڑ���\���f�[�^���L���[�ɒǉ�����
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        JavelinConnectionData connectionData =
                                               new JavelinConnectionData(
                                                                         JavelinConnectionData.TYPE_CONNECTION);
        connectionData.measurementTime = currentTime;
        connectionData.setDatabaseName(dbName);
        if (queue_ != null)
        {
            queue_.offer(connectionData);
        }
    }

    /**
     * ����N���C�A���g��Javelin���ؒf���ꂽ���Ƃ�ʒm����B
     * @param dbName �f�[�^�x�[�X��
     * @param forceDisconnected �����ؒf�Ȃ�<code>true</code>
     */
    private void notifyJavelinDisconnected(final String dbName, final boolean forceDisconnected)
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
            disconnectionData.setDatabaseName(dbName);
            if (queue_ != null)
            {
                queue_.offer(disconnectionData);
            }
        }

        List<TelegramNotifyListener> listenerList = notifyListenerMap_.get(dbName);
        if (listenerList != null)
        {
            for (TelegramNotifyListener telegramNotifyListener : listenerList)
            {
                telegramNotifyListener.clientDisconnected(forceDisconnected);
            }
        }
    }

    /**
     * �T�[�r�X���[�h���̏����������B
     * @param alarmRepository �A���[��
     */
    private synchronized void initializeControlClient(final DataCollectorClient client)
    {
        DataCollectorClient javelinClient = getJavelinClient(dbName_);
        if (javelinClient != null)
        {
            bindJavelinAndControlClient(javelinClient, client);
        }
    }

    /**
     * Javelin�Ɛ���N���C�A���g��R�t����B
     * @param javelinClient Javelin�N���C�A���g
     * @param controlClient ����N���C�A���g
     */
    private void bindJavelinAndControlClient(final DataCollectorClient javelinClient,
            final DataCollectorClient controlClient)
    {
        // BottleneckEye->DataCollector->Javelin
        controlClient.addTelegramListener(new TelegramListener() {
            public Telegram receiveTelegram(final Telegram telegram)
            {
                javelinClient.sendTelegram(telegram);
                return null;
            }
        });

        controlClient.addTelegramListener(new SignalStateListener());
        controlClient.addTelegramListener(new SignalChangeListener());

        // Javelin->DataCollector->BottleneckEye
        javelinClient.addTelegramListener(new TelegramListener() {
            public Telegram receiveTelegram(final Telegram telegram)
            {
                Header header = telegram.getObjHeader();
                byte telKind = header.getByteTelegramKind();
                byte reqKind = header.getByteRequestKind();

                if (telKind == TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY
                        && reqKind == TelegramConstants.BYTE_REQUEST_KIND_RESPONSE)
                {
                    return null;
                }

                controlClient.sendTelegram(telegram);
                return null;
            }
        });
    }

    /**
     * JVN���O�ʒm���������郊�X�i�쐬����B
     * 
     * @param dbName �f�[�^�x�[�X��
     * @param hostName �ڑ���̃z�X�g��
     * @param ipAddress �ڑ����IP�A�h���X
     * @param port �ڑ���̃|�[�g�ԍ�
     * @param clientId �N���C�A���gID
     * @return �쐬����JvnFileNotifyListener
     */
    private JvnFileNotifyListener createJvnFileNotifyListener(final String dbName,
            final String hostName, final String ipAddress, final int port, final String clientId,
            final String agentName)
    {
        JvnFileNotifyListener notifyListener = null;
        if (queue_ != null)
        {
            notifyListener = new JvnFileNotifyListener(queue_);
            notifyListener.setDatabaseName(dbName);
            notifyListener.setHostName(hostName);
            notifyListener.setIpAddress(ipAddress);
            notifyListener.setPort(port);
            notifyListener.setClientId(clientId);
            notifyListener.setAgentName(agentName);
        }
        return notifyListener;
    }

    /**
     * ���\�[�X�ʒm���������郊�X�i���쐬����B
     * 
     * @param dbName �f�[�^�x�[�X��
     * @param hostName �ڑ���̃z�X�g��
     * @param ipAddress �ڑ����IP�A�h���X
     * @param port �ڑ���̃|�[�g�ԍ�
     * @param clientId �N���C�A���gID
     * @return �쐬����SystemResourceListener
     */
    private SystemResourceListener createSystemResourceListener(final String dbName,
            final String hostName, final String ipAddress, final int port, final String clientId,
            final String agentName)
    {
        SystemResourceListener notifyListener = null;
        if (queue_ != null)
        {
            notifyListener = new SystemResourceListener(queue_);
            notifyListener.setDatabaseName(dbName);
            notifyListener.setHostName(hostName);
            notifyListener.setIpAddress(ipAddress);
            notifyListener.setPort(port);
            notifyListener.setClientId(clientId);
            notifyListener.setAgentName(agentName);
        }
        return notifyListener;
    }

    /**
     * �����d�����������郊�X�i���쐬����B
     * @param telegramKind �d�����
     * @return �쐬�������X�i��Ԃ��B
     */
    private CommonResponseListener createResponseTelegramListener(final byte telegramKind)
    {
        return new CommonResponseListener(queue_, telegramKind);
    }

    /**
     * Javelin�Ƃ̐ڑ����m������܂ł̃_�~�[���M���ݒ肷��B
     */
    private void setDummyTelegramSenders()
    {
        Set<String> keySet = notifyListenerMap_.keySet();

        for (String key : keySet)
        {
            List<TelegramNotifyListener> listenerList = notifyListenerMap_.get(key);
            for (TelegramNotifyListener notifyListener : listenerList)
            {
                notifyListener.setTelegramSender(this);
            }
        }
    }

    /**
     * �d�����M���ݒ肷��B
     */
    private void setTelegramSenders()
    {
        Set<String> keySet = notifyListenerMap_.keySet();

        for (String key : keySet)
        {
            DataCollectorClient client = getJavelinClient(key);
            if (client != null)
            {
                List<TelegramNotifyListener> listenerList = notifyListenerMap_.get(key);
                for (TelegramNotifyListener notifyListener : listenerList)
                {
                    notifyListener.setTelegramSender(client);
                }
            }
        }
    }

    /**
     * Javelin�N���C�A���g�����X�g�ɓo�^����B
     * @param client �o�^����Javelin�N���C�A���g
     */
    private void addJavelinClient(final DataCollectorClient client)
    {
        synchronized (javelinClientList_)
        {
            javelinClientList_.put(dbName_, client);
        }
    }

    /**
     * Javelin�N���C�A���g�����X�g����폜����B
     * @param dbName �L�[�ƂȂ�DB��
     */
    private void removeJavelinClient(final String dbName)
    {
        synchronized (javelinClientList_)
        {
            javelinClientList_.remove(dbName);
        }
    }

    /**
     * DB�����L�[�Ƃ���Javelin�N���C�A���g���擾����B
     * @param dbName �L�[�ƂȂ�DB��
     * @return Javelin�N���C�A���g��Ԃ��B
     */
    private DataCollectorClient getJavelinClient(final String dbName)
    {
        synchronized (javelinClientList_)
        {
            return javelinClientList_.get(dbName);
        }
    }

    /**
     * ����N���C�A���g��ǉ�����B
     * @param client ����N���C�A���g
     */
    private void addControlClient(final DataCollectorClient client)
    {
        synchronized (controlClientList_)
        {
            Set<DataCollectorClient> clientList = controlClientList_.get(dbName_);
            if (clientList == null)
            {
                clientList = new HashSet<DataCollectorClient>();
                clientList.add(client);
            }
            else
            {
                clientList.add(client);
            }
            controlClientList_.put(dbName_, clientList);
        }
    }

    /**
     * Javelin�N���C�A���g�����X�g����폜����B
     * @param dbName �L�[�ƂȂ�DB��
     */
    private void removeControlClient(final String dbName)
    {
        synchronized (controlClientList_)
        {
            controlClientList_.remove(dbName);
        }
    }

    /**
     * DB�����L�[�Ƃ��Đ���N���C�A���g���擾����B
     * @param dbName �L�[�ƂȂ�DB��
     * @return ����N���C�A���g
     */
    private Set<DataCollectorClient> getControlClient(final String dbName)
    {
        synchronized (controlClientList_)
        {
            return controlClientList_.get(dbName);
        }
    }

    /**
     * �ؗ������Ă����d�������ׂđ��M����B
     */
    private void sendWaitingTelegram(final DataCollectorClient client)
    {
        for (Telegram telegram : waitingTelegramList_)
        {
            client.sendTelegram(telegram);
        }
        waitingTelegramList_.clear();
    }

    /**
     * �ڑ����Ă���DB���Ǘ��N���C�A���g�ɒǉ�����Javelin��DB���𑗐M���܂��B
     */
    private void sendDatabaseName()
    {
        if (this.databaseAdminClient_ != null)
        {
            Set<String> databaseNameList = javelinClientList_.keySet();
            databaseAdminClient_.sendTelegram(ConnectNotifyAccessor.createAddDatabaseNameTelegram(databaseNameList));
        }
    }

    /**
     * DB���Ǘ��N���C�A���g���擾���܂��B
     * @return DB���Ǘ��N���C�A���g
     */
    public DataCollectorClient getDatabaseAdminClient()
    {
        return databaseAdminClient_;
    }

    /**
     * DB���Ǘ��N���C�A���g��ݒ肵�܂��B
     * @param databaseAdminClient DB���Ǘ��N���C�A���g
     */
    public void setDatabaseAdminClient(final DataCollectorClient databaseAdminClient)
    {
        databaseAdminClient_ = databaseAdminClient;
    }
}
