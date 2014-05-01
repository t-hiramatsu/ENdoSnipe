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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.config.DisplayNameManager;
import jp.co.acroquest.endosnipe.collector.config.RotateConfig;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.collector.listener.TelegramNotifyListener;
import jp.co.acroquest.endosnipe.collector.request.CommunicationClientRepository;
import jp.co.acroquest.endosnipe.collector.rotate.LogRotator;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.SystemResourceGetter;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.impl.DataCollectorClient;
import jp.co.acroquest.endosnipe.data.dao.SignalDefinitionDao;
import jp.co.acroquest.endosnipe.data.db.ConnectionManager;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.db.DatabaseType;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SignalDefinition;

/**
 * ENdoSnipe DataCollector �̃��C���N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class ENdoSnipeDataCollector implements CommunicationClientRepository, LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(ENdoSnipeDataCollector.class);

    /** JavelinDataLogger �̃X���b�h���� */
    private static final String LOGGER_THREAD_NAME = "JavelinDataLoggerThread";

    /** ���[�e�[�g�p�X���b�h�̖��� */
    private static final String ROTATE_THREAD_NAME = "JavelinDataCollectorRotateThread";

    private static final String RESOURCE_GETTER_THREAD_NAME = "ResourceGetterThread";

    /** �~����\���P�� */
    private static final int MILLI_UNIT = 1000;

    private DataCollectorConfig config_;

    /** ���[�e�[�g�p�ݒ胊�X�g */
    private final List<RotateConfig> rotateConfigList_ = new ArrayList<RotateConfig>();

    /** �f�t�H���g�̃��[�e�[�g�ݒ� */
    private RotateConfig defaultRotateConfig_;

    /** ���O���[�e�[�g�^�X�N */
    private LogRotator logRotator_;

    /** ���O���[�e�[�g�p�X���b�h */
    private Thread rotateThread_;

    private volatile Thread javelinDataLoggerThread_;

    private JavelinDataLogger javelinDataLogger_;

    /** DB�����L�[�ɂ����A�N���C�A���g�ɒʒm���邽�߂̃��X�i�̃��X�g */
    private final Map<String, List<TelegramNotifyListener>> telegramNotifyListenersMap_ =
                                                                                          new HashMap<String, List<TelegramNotifyListener>>();

    private final List<JavelinClient> clientList_ = new ArrayList<JavelinClient>();

    private volatile boolean isRunning_;

    /** ���\�[�X���擾����X���b�h */
    private Timer timer_;

    /** ���\�[�X���擾����^�C�}�[�^�X�N */
    private SystemResourceGetter resourceGetterTask_;

    /** �T�[�r�X���[�h���ǂ��� */
    private BehaviorMode behaviorMode_ = BehaviorMode.SERVICE_MODE;

    /** Javelin����̐ڑ���҂��󂯂�T�[�o�C���X�^���X */
    private JavelinServer server_;

    /**
     * {@link ENdoSnipeDataCollector} �̐ݒ���s���܂��B<br />
     * 
     * @param config �ݒ�I�u�W�F�N�g
     */
    public void init(final DataCollectorConfig config)
    {
        this.config_ = config;
    }

    /**
     * �N���C�A���g�ɒʒm���邽�߂̃��X�i��o�^���܂��B
     * 
     * @param dbName DB��
     * @param notifyListener �N���C�A���g�ɒʒm���邽�߂̃��X�i
     */
    public void addTelegramNotifyListener(final String dbName,
            final TelegramNotifyListener notifyListener)
    {
        List<TelegramNotifyListener> telegramNotifyListeners =
                                                               this.telegramNotifyListenersMap_.get(dbName);
        if (telegramNotifyListeners == null)
        {
            telegramNotifyListeners = new ArrayList<TelegramNotifyListener>();
            this.telegramNotifyListenersMap_.put(dbName, telegramNotifyListeners);
        }
        telegramNotifyListeners.add(notifyListener);
    }

    /**
     * �v���O�C�����[�h�� DataCollector �����s���܂��B
     */
    public void startPluginMode()
    {
        start(BehaviorMode.PLUGIN_MODE);
    }

    /**
     * �T�[�r�X���[�h�� DataCollector �����s���܂��B
     */
    public void startService()
    {
        start(BehaviorMode.SERVICE_MODE);
    }

    /**
     * {@link ENdoSnipeDataCollector} ���J�n���܂��B<br />
     * ���O���[�e�[�g���s���ꍇ�́A{@link #addRotateConfig(RotateConfig)}��p����
     * ���[�e�[�g�p�̐ݒ��ǉ�������ɖ{���\�b�h���Ăяo���Ă��������B
     * 
     * @param behaviorMode �T�[�r�X���[�h�̏ꍇ�� <code>true</code> �A�v���O�C�����[�h�̏ꍇ��
     *            <code>false</code>
     */
    public synchronized void start(final BehaviorMode behaviorMode)
    {

        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STARTING, behaviorMode);

        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setItemNamePrefix("");
        javelinConfig.setClusterName("");

        Map<Long, SignalDefinitionDto> signalDefinitionMap = null;

        this.behaviorMode_ = behaviorMode;
        if (config_ != null)
        {
            String baseDir = config_.getBaseDir();
            boolean useDefaultDatabase = true;
            if (config_.getDatabaseType() != DatabaseType.H2)
            {
                useDefaultDatabase = false;
            }
            String databaseHost = config_.getDatabaseHost();
            String databasePort = config_.getDatabasePort();
            String databaseName = config_.getDatabaseName();
            String databaseUserName = config_.getDatabaseUserName();
            String databasePassword = config_.getDatabasePassword();
            DBManager.updateSettings(useDefaultDatabase, baseDir, databaseHost, databasePort,
                                     databaseName, databaseUserName, databasePassword);
            ConnectionManager.getInstance().setBaseDir(baseDir);
            LOGGER.log(DATABASE_BASE_DIR, baseDir);
            if (useDefaultDatabase == false)
            {
                LOGGER.log(DATABASE_PARAMETER, databaseHost, databasePort, databaseUserName);
            }

            signalDefinitionMap = createSignalDefinition(databaseName);
        }

        // JavelinDataLogger �̊J�n
        if (javelinDataLogger_ != null)
        {
            javelinDataLogger_.stop();
        }
        javelinDataLogger_ = new JavelinDataLogger(config_, this, signalDefinitionMap);
        javelinDataLogger_.init(this.rotateConfigList_);
        if (defaultRotateConfig_ != null)
        {
            javelinDataLogger_.setDefaultRotateConfig(defaultRotateConfig_);
        }
        javelinDataLoggerThread_ =
                                   new Thread(javelinDataLogger_, LOGGER_THREAD_NAME + "_"
                                           + Integer.toHexString(System.identityHashCode(this)));

        if (behaviorMode == BehaviorMode.SERVICE_MODE)
        {
            javelinDataLoggerThread_.setDaemon(true);

            // BottleneckEye�֒ʒm����\�����̕ϊ��}�b�v��o�^
            ResourceNotifyAccessor.setConvMap(DisplayNameManager.getManager().getConvMap());
        }

        javelinDataLoggerThread_.start();

        long resourceRequestInterval = config_.getResourceInterval();
        startTimer(resourceRequestInterval, behaviorMode);

        if (behaviorMode == BehaviorMode.PLUGIN_MODE)
        {
            startLogRotate(false);
        }
        isRunning_ = true;
        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STARTED);
    }

    /**
     * 臒l�����`���̃}�b�v���쐬����B
     * @param databaseName �f�[�^�x�[�X�� 
     * @return 臒l�����`���̃}�b�v
     */
    private Map<Long, SignalDefinitionDto> createSignalDefinition(final String databaseName)
    {
        Map<Long, SignalDefinitionDto> signalDefinitionMap =
                                                             new ConcurrentHashMap<Long, SignalDefinitionDto>();
        try
        {
            List<SignalDefinition> signalDefinitionList =
                                                          SignalDefinitionDao.selectAll(databaseName);
            for (SignalDefinition signalDefinition : signalDefinitionList)
            {
                SignalDefinitionDto signalDefinitionDto = new SignalDefinitionDto(signalDefinition);
                signalDefinitionMap.put(signalDefinition.signalId, signalDefinitionDto);
            }
        }
        catch (SQLException ex)
        {
            LOGGER.log(FAIL_READ_SIGNAL_DEFINITION, ex, ex.getMessage());
        }
        return signalDefinitionMap;
    }

    /**
     * DataCollector���I������܂ŁA�X���b�h���u���b�N���܂��B<br />
     */
    public void blockTillStop()
    {
        if (javelinDataLoggerThread_ != null)
        {
            try
            {
                javelinDataLoggerThread_.join();
            }
            catch (InterruptedException ex)
            {
                // Do nothing.
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /**
     * �V�X�e�����\�[�X���擾�̃^�C�}�[�^�X�N���Ainterval(�P��:ms)���ƂɎ��s����B
     * ���Ƀ^�C�}�[�^�X�N������΁A�L�����Z��������V���Ƀ^�C�}�[�^�X�N���쐬����B
     * 
     * @param interval �V�X�e�����\�[�X���擾�̊Ԋu(�P��:ms)
     * @param behaviorMode {@link BehaviorMode.SERVICE_MODE} �̏ꍇ�A�X���b�h���f�[����������B
     */
    private void startTimer(final long interval, final BehaviorMode behaviorMode)
    {
        if (this.resourceGetterTask_ != null)
        {
            this.resourceGetterTask_.cancel();
        }

        if (this.timer_ != null)
        {
            this.timer_.purge();
        }

        boolean daemon = (behaviorMode == BehaviorMode.SERVICE_MODE);
        this.timer_ = new Timer(RESOURCE_GETTER_THREAD_NAME, daemon);
        this.resourceGetterTask_ = new SystemResourceGetter();
        this.resourceGetterTask_.setMinimumInterval(interval / 2);
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        Date firstDate = new Date(currentTime - (currentTime % MILLI_UNIT) + interval);
        this.timer_.scheduleAtFixedRate(this.resourceGetterTask_, firstDate, interval);
    }

    private synchronized void stopTimer()
    {
        // �V�X�e�����\�[�X�擾�X���b�h�̒�~
        if (this.resourceGetterTask_ != null)
        {
            this.resourceGetterTask_.cancel();
        }

        // �V�X�e�����\�[�X�擾�p�^�C�}�[�̒�~
        this.timer_.cancel();
    }

    /**
     * ���O���[�e�[�g�p�X���b�h���J�n����B
     * 
     * @param daemon <code>true</code> �̏ꍇ�A�X���b�h���f�[����������B
     */
    private void startLogRotate(final boolean daemon)
    {
        if (DBManager.isDefaultDb() == false)
        {
            return;
        }

        // H2���g�p���Ă���Ƃ��̂݁A���[�e�[�g�p�X���b�h���J�n����
        this.logRotator_ = new LogRotator();
        this.logRotator_.setConfig(this.rotateConfigList_);

        this.rotateThread_ =
                             new Thread(this.logRotator_, ROTATE_THREAD_NAME + "_"
                                     + Integer.toHexString(System.identityHashCode(this)));
        if (daemon == true)
        {
            this.rotateThread_.setDaemon(true);
        }
        this.rotateThread_.start();
    }

    /**
     * {@link ENdoSnipeDataCollector} ���I�����܂��B<br />
     */
    public synchronized void stop()
    {
        if (isRunning() == false)
        {
            return;
        }
        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STOPPING);

        // ���O���[�e�[�g�@�\�̏I��
        if (this.logRotator_ != null)
        {
            this.logRotator_.stop();
            this.rotateThread_.interrupt();
        }
        this.rotateConfigList_.clear();

        // ���\�[�X�擾�^�C�}�̏I��
        stopTimer();

        // JavelinClient �̏I��
        disconnectAll();

        // JavelinDataLogger �̏I��
        javelinDataLogger_.stop();

        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STOPPED);
    }

    /**
     * �N���C�A���g����̐ڑ���҂��󂯂�T�[�o���J�n����B
     * @throws InitializeException �������Ɏ��s
     */
    public void startServer()
        throws InitializeException
    {
        startServer(config_.getAcceptPort());

        AgentSetting agentSetting = new AgentSetting();
        agentSetting.jvnLogStragePeriod = config_.getJvnLogStoragePeriod();
        agentSetting.measureStragePeriod = config_.getMeasurementLogStoragePeriod();
        RotateConfig rotateConfig = null;
        rotateConfig = createRotateConfig(agentSetting);
        setDefaultRotateConfig(rotateConfig);
        startLogRotate(true);
    }

    /**
     * �|�[�g�ԍ����w�肵�ăN���C�A���g����̐ڑ���҂��󂯂�T�[�o���J�n����B
     * @param port �҂��󂯃|�[�g�ԍ�
     */
    public void startServer(final int port)
    {
        checkRunning();

        server_ = new JavelinServer();
        JavelinDataQueue queue = this.javelinDataLogger_.getQueue();
        server_.setTelegramNotifyListener(this.telegramNotifyListenersMap_);
        server_.start(port, queue, config_.getDatabaseName(), resourceGetterTask_, behaviorMode_);
    }

    /**
     * �ݒ�ɋL�q���ꂽ���ׂĂ� Javelin �G�[�W�F���g�ɐڑ����܂��B<br />
     * {@link #init(DataCollectorConfig) init()} ���\�b�h�œn���ꂽ
     * {@link DataCollectorConfig} �I�u�W�F�N�g�ɏ]���āA�ڑ����s���܂��B<br />
     * �{���\�b�h�� {@link #startPluginMode()} ���\�b�h���Ă񂾂��ƂɎ��s���Ă��������B<br />
     * @throws InitializeException ��������O
     */
    public void connectAll()
        throws InitializeException
    {
        checkRunning();

        if (this.config_ == null)
        {
            return;
        }

        List<AgentSetting> agentList = this.config_.getAgentSettingList();
        for (AgentSetting agentSetting : agentList)
        {
            String databaseName = agentSetting.databaseName;
            if (databaseName == null || databaseName.length() == 0)
            {
                // �f�[�^�x�[�X�����w�肳��Ă��Ȃ���΁A
                // endosnipedb_(javelin.host.n�̒l)_(javelin.port.n�̒l) �ɂ���
                databaseName = "endosnipedb_" + agentSetting.hostName + "_" + agentSetting.port;
                agentSetting.databaseName = databaseName;
            }
            RotateConfig rotateConfig = createRotateConfig(agentSetting);
            addRotateConfig(rotateConfig);
            String clientId =
                              connect(databaseName, agentSetting.hostName, agentSetting.port,
                                      agentSetting.acceptPort);
            if (clientId == null)
            {
                LOGGER.log(DATABASE_ALREADY_USED, databaseName, agentSetting.hostName,
                           agentSetting.port);
            }
        }
        startLogRotate(true);
    }

    /**
     * Javelin �G�[�W�F���g�ɐڑ����܂��B<br />
     * �{���\�b�h�� {@link #startPluginMode()} ���\�b�h���Ă񂾂��ƂɎ��s���Ă��������B<br />
     *
     * ���łɓ����f�[�^�x�[�X���Q�Ƃ��Đڑ����Ă�����̂����݂����ꍇ�́A
     * �ڑ������� <code>null</code> ��Ԃ��܂��B<br />
     *
     * @param dbName �f�[�^�x�[�X��
     * @param javelinHost �ڑ��� Javelin �̃z�X�g���܂��� IP �A�h���X
     * @param javelinPort �ڑ��� Javelin �̃|�[�g�ԍ�
     * @param acceptPort BottleneckEye ����̐ڑ��҂��󂯃|�[�g�ԍ�
     * @return �N���C�A���g ID
     */
    public synchronized String connect(final String dbName, final String javelinHost,
            final int javelinPort, final int acceptPort)
    {
        DBManager.setDbName(dbName);

        checkRunning();

        String clientId = JavelinClient.createClientIdFromHost(javelinHost, javelinPort);

        // ���łɓ����f�[�^�x�[�X���Q�Ƃ��Đڑ����Ă�����̂����݂����ꍇ�́A�ڑ����Ȃ�
        DataBaseManager manager = DataBaseManager.getInstance();
        String hostInfo = manager.getHostInfo(dbName);
        if (hostInfo != null)
        {
            return null;
        }

        manager.addDbInfo(dbName, clientId);

        JavelinClient javelinClient = findJavelinClient(clientId);
        JavelinDataQueue queue = this.javelinDataLogger_.getQueue();
        if (javelinClient == null)
        {
            // ���ɃN���C�A���g�����݂��Ȃ��ꍇ�͐V���ɐ�������
            javelinClient = new JavelinClient();
            javelinClient.init(dbName, javelinHost, javelinPort, acceptPort);
            javelinClient.setTelegramNotifyListener(this.telegramNotifyListenersMap_.get(dbName));
            javelinClient.connect(queue, this.behaviorMode_, null);
            this.clientList_.add(javelinClient);
            this.resourceGetterTask_.addTelegramSenderList(javelinClient.getTelegramSender());
        }
        else
        {
            javelinClient.setTelegramNotifyListener(this.telegramNotifyListenersMap_.get(clientId));

            // ���ɃN���C�A���g�����݂��A���ڑ��̏ꍇ�͐ڑ�����
            if (javelinClient.isConnected() == false)
            {
                javelinClient.connect(queue, this.behaviorMode_, null);
            }
        }

        return clientId;
    }

    /**
     * �w�肳�ꂽ�N���C�A���g�� Javelin ����ؒf���܂��B<br />
     * 
     * @param clientId �N���C�A���g ID
     */
    public synchronized void disconnect(final String clientId)
    {
        JavelinClient client = findJavelinClient(clientId);
        if (client != null)
        {
            client.disconnect();
            DataBaseManager.getInstance().removeDbInfo(client.getDatabaseName());
        }
    }

    /**
     * ���ׂĂ� Javelin ����ؒf���܂��B<br />
     */
    public synchronized void disconnectAll()
    {
        for (JavelinClient javelinClient : clientList_)
        {
            javelinClient.disconnect();
            DataBaseManager.getInstance().removeDbInfo(javelinClient.getDatabaseName());
        }
        if (server_ != null)
        {
            server_.stop();
        }
    }

    /**
     * {@link ENdoSnipeDataCollector} �����s���ł��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return ���s���̏ꍇ�� <code>true</code>
     */
    public boolean isRunning()
    {
        return isRunning_;
    }

    private void checkRunning()
    {
        if (isRunning_ == false)
        {
            throw new IllegalStateException("ENdoSnipe DataCollector is not running.");
        }
    }

    /**
     * �w�肳�ꂽ�N���C�A���g ID �� {@link JavelinClient} �C���X�^���X��Ԃ��܂��B
     *
     * @param clientId �N���C�A���g ID
     * @return ���݂���ꍇ�� {@link JavelinClient} �̃C���X�^���X�A���݂��Ȃ��ꍇ�� <code>null</code>
     */
    private JavelinClient findJavelinClient(final String clientId)
    {
        for (JavelinClient javelinClient : clientList_)
        {
            if (clientId.equals(javelinClient.getClientId()) == true)
            {
                return javelinClient;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void sendTelegramToClient(final String clientId, final Telegram telegram)
    {
        if (server_ != null)
        {
            server_.sendTelegramToControlClient(clientId, telegram);
        }
        else
        {
            for (JavelinClient javelinClient : clientList_)
            {
                if (clientId.equals(javelinClient.getClientId()) == true)
                {
                    javelinClient.sendTelegramToClient(telegram);
                    return;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public TelegramSender getTelegramSender(final String clientId)
    {
        if (config_.getConnectionMode().equals(DataCollectorConfig.MODE_CLIENT))
        {
            JavelinClient client = findJavelinClient(clientId);
            if (client != null)
            {
                return client.getTelegramSender();
            }
        }
        else
        {
            DataCollectorClient client = server_.getClient(clientId);
            if (client != null)
            {
                return client;
            }
        }
        return null;
    }

    /**
     * ���O���[�e�[�g�p�ݒ��ǉ�
     * 
     * @param config ���O���[�e�[�g�ݒ�
     */
    public void addRotateConfig(final RotateConfig config)
    {
        synchronized (this.rotateConfigList_)
        {
            this.rotateConfigList_.add(config);
        }
        synchronized (this)
        {
            if (this.javelinDataLogger_ != null)
            {
                this.javelinDataLogger_.addRotateConfig(config);
            }
        }
    }

    /**
     * �f�t�H���g�̃��O���[�e�[�g�p�ݒ��ǉ�
     * 
     * @param config ���O���[�e�[�g�ݒ�
     */
    public void setDefaultRotateConfig(final RotateConfig config)
    {
        synchronized (this.rotateConfigList_)
        {
            this.rotateConfigList_.add(config);
        }
        synchronized (this)
        {
            if (this.javelinDataLogger_ != null)
            {
                this.javelinDataLogger_.setDefaultRotateConfig(config);
            }
            else
            {
                defaultRotateConfig_ = config;
            }
        }
    }

    /**
     * ���O���[�e�[�g�ݒ�𐶐����܂��B<br />
     * 
     * @param agentSetting �e�G�[�W�F���g�̐ݒ�
     */
    private RotateConfig createRotateConfig(final AgentSetting agentSetting)
        throws InitializeException
    {
        RotateConfig config = new RotateConfig();
        config.setDatabase(agentSetting.databaseName);
        config.setJavelinRotatePeriod(agentSetting.getJavelinRotatePeriod());
        config.setJavelinRotatePeriodUnit(agentSetting.getJavelinRotatePeriodUnit());
        config.setMeasureRotatePeriod(agentSetting.getMeasurementRotatePeriod());
        config.setMeasureRotatePeriodUnit(agentSetting.getMeasurementRotatePeriodUnit());

        return config;
    }
}
