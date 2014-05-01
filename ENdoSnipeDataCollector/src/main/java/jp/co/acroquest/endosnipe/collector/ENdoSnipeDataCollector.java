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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import jp.co.acroquest.endosnipe.communicator.accessor.ConnectNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.SystemResourceGetter;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dao.SignalDefinitionDao;
import jp.co.acroquest.endosnipe.data.dao.SummarySignalDefinitionDao;
import jp.co.acroquest.endosnipe.data.db.ConnectionManager;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.db.DatabaseType;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SignalDefinition;
import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

/**
 * ENdoSnipe DataCollector のメインクラスです。<br />
 * 
 * @author y-komori
 */
public class ENdoSnipeDataCollector implements CommunicationClientRepository, LogMessageCodes,
    TelegramConstants
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(ENdoSnipeDataCollector.class);

    /** JavelinDataLogger のスレッド名称 */
    private static final String LOGGER_THREAD_NAME = "JavelinDataLoggerThread";

    /** ローテート用スレッドの名称 */
    private static final String ROTATE_THREAD_NAME = "JavelinDataCollectorRotateThread";

    private static final String RESOURCE_GETTER_THREAD_NAME = "ResourceGetterThread";

    /** ミリを表す単位 */
    private static final int MILLI_UNIT = 1000;

    private DataCollectorConfig config_;

    /** ローテート用設定リスト */
    private final List<RotateConfig> rotateConfigList_ = new ArrayList<RotateConfig>();

    /** デフォルトのローテート設定 */
    private RotateConfig defaultRotateConfig_;

    /** ログローテートタスク */
    private LogRotator logRotator_;

    /** ログローテート用スレッド */
    private Thread rotateThread_;

    private volatile Thread javelinDataLoggerThread_;

    private JavelinDataLogger javelinDataLogger_;

    /** DB名をキーにした、クライアントに通知するためのリスナのリスト */
    private final Map<String, List<TelegramNotifyListener>> telegramNotifyListenersMap_ =
        new HashMap<String, List<TelegramNotifyListener>>();

    private static List<JavelinClient> clientList__ = new ArrayList<JavelinClient>();

    private volatile boolean isRunning_;

    /** リソースを取得するスレッド */
    private Timer timer_;

    /** リソースを取得するタイマータスク */
    private SystemResourceGetter resourceGetterTask_;

    /** サービスモードかどうか */
    private BehaviorMode behaviorMode_ = BehaviorMode.SERVICE_MODE;

    /** Javelinからの接続を待ち受けるサーバインスタンス */
    private JavelinServer server_;

    /** JavelinクライアントのDB名ごとのシーケンス番号マップ */
    private static Map<String, Set<Integer>> javelinSeqMap__ = new HashMap<String, Set<Integer>>();

    private static final int AGENTINDEX = 4;

    /**
     * {@link ENdoSnipeDataCollector} の設定を行います。<br />
     * 
     * @param config 設定オブジェクト
     */
    public void init(final DataCollectorConfig config)
    {
        this.config_ = config;
    }

    /**
     * クライアントに通知するためのリスナを登録します。
     * 
     * @param dbName DB名
     * @param notifyListener クライアントに通知するためのリスナ
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
     * プラグインモードで DataCollector を実行します。
     */
    public void startPluginMode()
    {
        start(BehaviorMode.PLUGIN_MODE);
    }

    /**
     * サービスモードで DataCollector を実行します。
     */
    public void startService()
    {
        start(BehaviorMode.SERVICE_MODE);
    }

    /**
     * {@link ENdoSnipeDataCollector} を開始します。<br />
     * ログローテートを行う場合は、{@link #addRotateConfig(RotateConfig)}を用いて
     * ローテート用の設定を追加した後に本メソッドを呼び出してください。
     * 
     * @param behaviorMode サービスモードの場合は <code>true</code> 、プラグインモードの場合は
     *            <code>false</code>
     */
    public synchronized void start(final BehaviorMode behaviorMode)
    {

        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STARTING, behaviorMode);

        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setItemNamePrefix("");
        javelinConfig.setClusterName("");

        Map<Long, SignalDefinitionDto> signalDefinitionMap = null;
        Map<Long, SummarySignalDefinitionDto> summarySignalDefinitionMap = null;
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
            summarySignalDefinitionMap = createSummarySignalDefinition(databaseName);
        }

        // JavelinDataLogger の開始
        if (javelinDataLogger_ != null)
        {
            javelinDataLogger_.stop();
        }
        // javelinDataLogger_ = new JavelinDataLogger(config_, this, signalDefinitionMap);

        /*need to change to test in the JUnit testcase class because in the test case it is 
        using only three argument constructor for creating JavelinDataLogger*/

        javelinDataLogger_ =
            new JavelinDataLogger(config_, this, signalDefinitionMap, summarySignalDefinitionMap);

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

            // BottleneckEyeへ通知する表示名の変換マップを登録
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
     * 閾値判定定義情報のマップを作成する。
     * @param databaseName データベース名 
     * @return 閾値判定定義情報のマップ
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

    private Map<Long, SummarySignalDefinitionDto> createSummarySignalDefinition(
        final String databaseName)
    {

        Map<Long, SummarySignalDefinitionDto> summarySignalDefinitionMap =
            new ConcurrentHashMap<Long, SummarySignalDefinitionDto>();
        try
        {
            List<SummarySignalDefinition> summarySignalDefinitionList =
                SummarySignalDefinitionDao.selectAll(databaseName);
            for (SummarySignalDefinition summarySignalDefinition : summarySignalDefinitionList)
            {
                summarySignalDefinition.errorMessage = "";
                SummarySignalDefinitionDto summarySignalDefinitionDto =
                    new SummarySignalDefinitionDto(summarySignalDefinition);
                summarySignalDefinitionMap.put(summarySignalDefinition.summarySignalId,
                                               summarySignalDefinitionDto);
            }
        }
        catch (SQLException ex)
        {
            LOGGER.log(FAIL_READ_SIGNAL_DEFINITION, ex, ex.getMessage());
        }
        return summarySignalDefinitionMap;
    }

    /**
     * DataCollectorが終了するまで、スレッドをブロックします。<br />
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
     * システムリソース情報取得のタイマータスクを、interval(単位:ms)ごとに実行する。
     * 既にタイマータスクがあれば、キャンセルした後新たにタイマータスクを作成する。
     * 
     * @param interval システムリソース情報取得の間隔(単位:ms)
     * @param behaviorMode {@link BehaviorMode.SERVICE_MODE} の場合、スレッドをデーモン化する。
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
        // システムリソース取得スレッドの停止
        if (this.resourceGetterTask_ != null)
        {
            this.resourceGetterTask_.cancel();
        }

        // システムリソース取得用タイマーの停止
        this.timer_.cancel();
    }

    /**
     * ログローテート用スレッドを開始する。
     * 
     * @param daemon <code>true</code> の場合、スレッドをデーモン化する。
     */
    private void startLogRotate(final boolean daemon)
    {
        if (DBManager.isDefaultDb() == false)
        {
            return;
        }

        // H2を使用しているときのみ、ローテート用スレッドを開始する
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
     * {@link ENdoSnipeDataCollector} を終了します。<br />
     */
    public synchronized void stop()
    {
        if (isRunning() == false)
        {
            return;
        }
        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STOPPING);

        // ログローテート機能の終了
        if (this.logRotator_ != null)
        {
            this.logRotator_.stop();
            this.rotateThread_.interrupt();
        }
        this.rotateConfigList_.clear();

        // リソース取得タイマの終了
        stopTimer();

        // JavelinClient の終了
        disconnectAll();

        // JavelinDataLogger の終了
        javelinDataLogger_.stop();

        LOGGER.log(ENDOSNIPE_DATA_COLLECTOR_STOPPED);
    }

    /**
     * クライアントからの接続を待ち受けるサーバを開始する。
     * @throws InitializeException 初期化に失敗
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
     * ポート番号を指定してクライアントからの接続を待ち受けるサーバを開始する。
     * @param port 待ち受けポート番号
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
     * 設定に記述されたすべての Javelin エージェントに接続します。<br />
     * {@link #init(DataCollectorConfig) init()} メソッドで渡された
     * {@link DataCollectorConfig} オブジェクトに従って、接続を行います。<br />
     * 本メソッドは {@link #startPluginMode()} メソッドを呼んだあとに実行してください。<br />
     * @throws InitializeException 初期化例外
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
                // データベース名が指定されていなければ、
                // endosnipedb_(javelin.host.nの値)_(javelin.port.nの値) にする
                databaseName = "endosnipedb_" + agentSetting.hostName + "_" + agentSetting.port;
                agentSetting.databaseName = databaseName;
            }
            RotateConfig rotateConfig = createRotateConfig(agentSetting);
            addRotateConfig(rotateConfig);

            String clientId =
                connect(databaseName, agentSetting.hostName, agentSetting.port,
                        agentSetting.acceptPort, agentSetting.agentId);
            if (clientId == null)
            {
                LOGGER.log(DATABASE_ALREADY_USED, databaseName, agentSetting.hostName,
                           agentSetting.port);
            }
        }
        startLogRotate(true);
    }

    /**
     * Javelin エージェントに接続します。<br />
     * 本メソッドは {@link #startPluginMode()} メソッドを呼んだあとに実行してください。<br />
     *
     * すでに同じデータベースを参照して接続しているものが存在した場合は、
     * 接続せずに <code>null</code> を返します。<br />
     *
     * @param dbName データベース名
     * @param javelinHost 接続先 Javelin のホスト名または IP アドレス
     * @param javelinPort 接続先 Javelin のポート番号
     * @param acceptPort BottleneckEye からの接続待ち受けポート番号
     * @return クライアント ID
     */
    public String connect(final String dbName, final String javelinHost, final int javelinPort,
        final int acceptPort)
    {
        return connect(dbName, javelinHost, javelinPort, acceptPort, 0);
    }

    /**
     * Javelin エージェントに接続します。<br />
     * 本メソッドは {@link #startPluginMode()} メソッドを呼んだあとに実行してください。<br />
     *
     * すでに同じデータベースを参照して接続しているものが存在した場合は、
     * 接続せずに <code>null</code> を返します。<br />
     *
     * @param dbName データベース名
     * @param javelinHost 接続先 Javelin のホスト名または IP アドレス
     * @param javelinPort 接続先 Javelin のポート番号
     * @param acceptPort BottleneckEye からの接続待ち受けポート番号
     * @param agentId エージェントID
     * @return クライアント ID
     */
    private synchronized String connect(final String dbName, final String javelinHost,
        final int javelinPort, final int acceptPort, final int agentId)
    {
        checkRunning();

        String clientId = JavelinClient.createClientIdFromHost(javelinHost, javelinPort);

        JavelinClient javelinClient = findJavelinClient(clientId);
        JavelinDataQueue queue = this.javelinDataLogger_.getQueue();
        if (javelinClient == null)
        {
            // 既にクライアントが存在しない場合は新たに生成する
            javelinClient = new JavelinClient();
            javelinClient.init(dbName, javelinHost, javelinPort, acceptPort);
            javelinClient.setTelegramNotifyListener(this.telegramNotifyListenersMap_.get(dbName));
            int no = getJavelinSequenceNo("/default/127.0.0.1/agent/");
            String agentName =
                ConnectNotifyAccessor.createAgentName("/default/127.0.0.1/agent/", no);
            javelinClient.setAgentName(agentName);
            ConnectNotifyData notifyData = new ConnectNotifyData();
            notifyData.setAgentName(agentName);
            //  javelinClient.connect(queue, this.behaviorMode_, null, agentId);
            //javelinClient.connect(queue, this.behaviorMode_, notifyData, agentId);
            javelinClient.connect(queue, this.behaviorMode_, notifyData, clientList__.size());
            clientList__.add(javelinClient);
            this.resourceGetterTask_.addTelegramSenderList(javelinClient.getTelegramSender());
        }
        else
        {
            javelinClient.setTelegramNotifyListener(this.telegramNotifyListenersMap_.get(clientId));

            // 既にクライアントが存在し、未接続の場合は接続する
            if (javelinClient.isConnected() == false)
            {
                javelinClient.connect(queue, this.behaviorMode_, null, agentId);
            }
        }

        return clientId;
    }

    /**
     * Send telegram for JavelinClient
     * @param telegram to send from JavelinClient
     */
    public static void sendJavelinClientTelegram(final Telegram telegram)
    {
        for (JavelinClient client : clientList__)
        {
            Header objHeader = telegram.getObjHeader();

            if (objHeader.getByteTelegramKind() == BYTE_TELEGRAM_KIND_GET_DUMP
                && objHeader.getByteRequestKind() == BYTE_REQUEST_KIND_REQUEST)
            {
                Body[] bodies = telegram.getObjBody();
                if (bodies.length == 2)
                {
                    String agentName = bodies[1].getStrItemName();
                    if (agentName.split("/").length < AGENTINDEX)
                    {
                        client.getTelegramSender().sendTelegram(telegram);
                    }
                    else
                    {
                        String[] agentSplit = agentName.split("/");
                        agentName = "/" + agentSplit[1];
                        for (int index = 2; index < AGENTINDEX; index++)
                        {
                            agentName += "/" + agentSplit[index];
                        }
                        //send ThreadDump for only related agent
                        if (agentName.equals(client.getAgentName()))
                        {
                            client.getTelegramSender().sendTelegram(telegram);
                        }
                    }
                }
                else
                {
                    client.getTelegramSender().sendTelegram(telegram);
                }
            }
            else
            {
                client.getTelegramSender().sendTelegram(telegram);
            }
        }
    }

    /**
     * 指定されたクライアントを Javelin から切断します。<br />
     * 
     * @param clientId クライアント ID
     */
    public synchronized void disconnect(final String clientId)
    {
        JavelinClient client = findJavelinClient(clientId);
        if (client != null)
        {
            client.disconnect();
            removeJavelinSequenceNo(client.getAgentName(), Integer.parseInt(client.getClientId()));
            DataBaseManager.getInstance().removeDbInfo(client.getDatabaseName());
        }
    }

    /**
     * すべての Javelin から切断します。<br />
     */
    public synchronized void disconnectAll()
    {
        for (JavelinClient javelinClient : clientList__)
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
     * {@link ENdoSnipeDataCollector} が実行中であるかどうかを返します。<br />
     * 
     * @return 実行中の場合は <code>true</code>
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
     * 指定されたクライアント ID の {@link JavelinClient} インスタンスを返します。
     *
     * @param clientId クライアント ID
     * @return 存在する場合は {@link JavelinClient} のインスタンス、存在しない場合は <code>null</code>
     */
    private JavelinClient findJavelinClient(final String clientId)
    {
        for (JavelinClient javelinClient : clientList__)
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
            for (JavelinClient javelinClient : clientList__)
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
     * ログローテート用設定を追加
     * 
     * @param config ログローテート設定
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
     * デフォルトのログローテート用設定を追加
     * 
     * @param config ログローテート設定
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
     * ログローテート設定を生成します。<br />
     * 
     * @param agentSetting 各エージェントの設定
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

    /**
     * calculate sequenceno for agent
     * @param dbName dbName of agent
     * @return sequence no
     */
    public static int getJavelinSequenceNo(final String dbName)
    {
        int seq = 0;

        synchronized (javelinSeqMap__)
        {
            Set<Integer> seqSet = javelinSeqMap__.get(dbName);
            if (seqSet == null)
            {
                seqSet = new HashSet<Integer>();
                javelinSeqMap__.put(dbName, seqSet);
            }

            while (seqSet.contains(seq))
            {
                seq++;
            }
            seqSet.add(seq);
        }

        return seq;
    }

    /**
     * Remove sequence no
     * @param dbName dbName of agent
     * @param seq sequence no of agent
     */
    public static void removeJavelinSequenceNo(final String dbName, final int seq)
    {
        synchronized (javelinSeqMap__)
        {
            Set<Integer> seqSet = javelinSeqMap__.get(dbName);
            if (seqSet == null)
            {
                return;
            }

            if (seqSet.contains(seq))
            {
                seqSet.remove(seq);
            }

            if (seqSet.size() == 0)
            {
                javelinSeqMap__.remove(dbName);
            }
        }
    }
}
