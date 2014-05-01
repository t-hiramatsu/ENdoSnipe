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
package jp.co.acroquest.endosnipe.web.explorer.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * WebDashボードの通信を行うサーブレットです。
 * @author s_fujii
 *
 */
public class ExplorerServlet extends HttpServlet
{
    /** 停止時間   */
    private static final int SLEEP_TIME = 5000;

    /** シリアルID */
    private static final long serialVersionUID = 3003980920335995413L;

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(ExplorerServlet.class);

    /**
     * コンストラクタ
     */
    public ExplorerServlet()
    {

    }

    /**
     * 初期処理を行います。
     * @throws ServletException サーブレット上で例外が発生した場合
     */
    @Override
    public void init()
        throws ServletException
    {
        //        //　通信用オブジェクトの作成
        //        DataBaseConfig dbConfig = null;
        //
        //        // DBの設定が行われるのを待ち続ける。
        //        while (true)
        //        {
        //
        //            DatabaseManager manager = DatabaseManager.getInstance();
        //            dbConfig = manager.getDataBaseConfig();
        //            if (dbConfig != null)
        //            {
        //                break;
        //            }
        //            try
        //            {
        //                Thread.sleep(SLEEP_TIME);
        //            }
        //            catch (InterruptedException ex)
        //            {
        //                LOGGER.log(LogMessageCodes.FAIL_READ_DB_SETTING, SLEEP_TIME);
        //            }
        //        }
        //        ApplicationContext context =
        //                new ClassPathXmlApplicationContext(
        //                                                   "jp/co/acroquest/endosnipe/web/explorer/quartz/propertySetting-quartz.xml");
        //        // client modeの場合、設定ファイルのエージェントごとに、threadを作成する
        //        if ("client".equals(dbConfig.getConnectionMode()))
        //        {
        //
        //            List<AgentSetting> agentSettings = dbConfig.getAgentSettingList();
        //
        //            ConnectionClient connectionClient = ConnectionClient.getInstance();
        //            List<CommunicationClient> clientList = connectionClient.getClientList();
        //            for (int cnt = 0; cnt < agentSettings.size(); cnt++)
        //            {
        //                AgentSetting setting = agentSettings.get(cnt);
        //                // DataCollectorに接続する。
        //                // TODO 複数エージェント対応・エラーチェック
        //                String javelinHost = setting.acceptHost_;
        //                int javelinPort = setting.acceptPort_;
        //                int agentId = cnt + 1;
        //                String clientId = createClientId(javelinHost, javelinPort);
        //
        //                CommunicationClient client =
        //                        CommunicationFactory.getCommunicationClient("DataCollector-ClientThread-"
        //                                + clientId);
        //
        //                client.init(javelinHost, javelinPort);
        //                client.addTelegramListener(new CollectorListener(agentId, setting.databaseName_));
        //                client.addTelegramListener(new AlarmNotifyListener(agentId));
        //                client.addTelegramListener(new SignalStateChangeListener());
        //                client.addTelegramListener(new SummarySignalStateChangeListener());
        //                client.addTelegramListener(new TreeStateAddListener());
        //                client.addTelegramListener(new TreeStateDeleteListener());
        //                client.addTelegramListener(new AlarmThreadDumpNotifyListener());
        //
        //                ConnectNotifyData connectNotify = new ConnectNotifyData();
        //                connectNotify.setKind(ConnectNotifyData.KIND_CONTROLLER);
        //                connectNotify.setPurpose(ConnectNotifyData.PURPOSE_GET_RESOURCE);
        //                connectNotify.setAgentName(setting.databaseName_);
        //
        //                client.connect(connectNotify);
        //                clientList.add(client);
        //            }
        //        }
        //        else if ("server".equals(dbConfig.getConnectionMode()))
        //        {
        //            ConnectionClient connectionClient = ConnectionClient.getInstance();
        //            List<CommunicationClient> clientList = connectionClient.getClientList();
        //
        //            CommunicationClient client =
        //                    CommunicationFactory.getCommunicationClient("DataCollector-JavelinNotify-Thread");
        //            client.init(dbConfig.getServerModeAgentSetting().acceptHost_,
        //                        dbConfig.getServerModeAgentSetting().acceptPort_);
        //            ConnectNotifyData connectNotify = new ConnectNotifyData();
        //            connectNotify.setKind(ConnectNotifyData.KIND_CONTROLLER);
        //            connectNotify.setPurpose(ConnectNotifyData.PURPOSE_GET_DATABASE);
        //            connectNotify.setAgentName("noDatabase");
        //            client.addTelegramListener(new JavelinNotifyListener());
        //            client.addTelegramListener(new AlarmThreadDumpNotifyListener());
        //
        //            client.connect(connectNotify);
        //            clientList.add(client);
        //        }
    }

    /**
     * ホスト名とポート番号からクライアント ID を生成します。<br />
     *
     * @param host ホスト名
     * @param port ポート番号
     * @return クライアント ID
     */
    public static String createClientId(final String host, final int port)
    {
        return host + ":" + port;
    }

}
