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
package jp.co.acroquest.endosnipe.javelin.resource.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;

/**
 * JMX計測値を取得するためのクラスを可変系列用マップに登録します。
 *
 * @author y_asazuma
 */
public class MBeanCollectorInitializer
{
    /** JMX接続用スレッド */
    private static JMXConnectThread              jmxConnectThread__;

    /** Javelinで監視するJMXの設定ファイル */
    private static final String                  JMX_PROP                   =
                                                                              "../conf/jmx.properties";

    /** 設定ファイルのObjectNameを表すキー */
    private static final String                  PREFIX_OBJECTNAME          = "objectName.";

    /** 設定ファイルのattributeを表すキー */
    private static final String                  PREFIX_ATTRIBUTE           = "attribute.";

    /** JMXのリモート接続を行うかどうかを表すキー */
    private static final String                  PREFIX_JMX_ENABLE          = "jmx.remote.enable.";

    /** JMXのリモート接続先URLを表すキー */
    private static final String                  PREFIX_JMX_REMOTE_URL      = "jmx.remote.url.";

    /** JMXのリモートユーザを表すキー */
    private static final String                  PREFIX_JMX_REMOTE_USER     = "jmx.remote.user.";

    /** JMXのリモートパスワード
     * を表すキー */
    private static final String                  PREFIX_JMX_REMOTE_PASSWORD =
                                                                              "jmx.remote.password.";

    /** JMXのリモート接続オブジェクトを保持するMap */
    private static Map<String, JMXConnector>     jmxConnectorMap__          =
                                                                              new ConcurrentHashMap<String, JMXConnector>();

    /** JMXのリモート接続情報を保持するMap */
    private static Map<String, JMXConnectEntity> jmxConnectEntityMap__      =
                                                                              new ConcurrentHashMap<String, JMXConnectEntity>();

    /**
     * コンストラクタ
     */
    private MBeanCollectorInitializer()
    {

    };

    /**
     * リソース取得インスタンスをマップに登録します。
     *
     * @param multiResourceMap リソース取得インスタンスを登録するマップ（可変系列用）
     */
    public static void init(Map<String, MultiResourceGetter> multiResourceMap)
    {
        Properties properties = JavelinConfigUtil.loadProperties(JMX_PROP);
        if (properties == null)
        {
            SystemLogger.getInstance().warn(JMX_PROP + " is null.");
            return;
        }

        jmxConnectThread__ = new JMXConnectThread();
        jmxConnectThread__.start();

        MBeanMultiResourceGetter getters = new MBeanMultiResourceGetter();
        Enumeration<?> enumetarion = properties.propertyNames();
        while (enumetarion.hasMoreElements())
        {
            String propKey = (String)enumetarion.nextElement();
            String objectStr = properties.getProperty(propKey);

            // PREFIXが"objectName."でない場合は読み飛ばす
            if (propKey.startsWith(PREFIX_OBJECTNAME) == false)
            {
                continue;
            }

            // 設定ファイルからオブジェクトの定義を取得する
            String objectName = objectStr;

            String id = propKey.substring(PREFIX_OBJECTNAME.length());
            String attrListStr = properties.getProperty(PREFIX_ATTRIBUTE + id);
            String remoteEnableStr = properties.getProperty(PREFIX_JMX_ENABLE + id);
            boolean remoteEnable = Boolean.valueOf(remoteEnableStr);
            String remoteUrl = properties.getProperty(PREFIX_JMX_REMOTE_URL + id);
            String user = properties.getProperty(PREFIX_JMX_REMOTE_USER + id);
            String password = properties.getProperty(PREFIX_JMX_REMOTE_PASSWORD + id);

            MBeanServer mbeanServer = null;
            MBeanServerConnection mbeanServerConnection = null;
            JMXConnectEntity entity = null;
            if (remoteEnable)
            {
                entity = new JMXConnectEntity();
                entity.setId(id);
                entity.setUrl(remoteUrl);
                entity.setUser(user);
                entity.setPassword(password);
                jmxConnectEntityMap__.put(id, entity);

                try
                {
                    JMXServiceURL url = new JMXServiceURL(remoteUrl);
                    HashMap<String, String[]> env = new HashMap<String, String[]>();
                    String[] credentials = new String[]{user, password};
                    env.put(JMXConnector.CREDENTIALS, credentials);
                    JMXConnector connector = JMXConnectorFactory.connect(url, env);
                    jmxConnectorMap__.put(id, connector);
                    mbeanServerConnection = connector.getMBeanServerConnection();
                }
                catch (MalformedURLException muex)
                {
                    // JMXのURL不正の場合は再接続できないため、次の要素に処理をうつる。
                    SystemLogger.getInstance().warn(muex);
                    continue;
                }
                catch (IOException ioex)
                {
                    // 接続失敗の場合は再接続処理をおこなうため、処理は継続する。
                    reconnect(entity);
                    SystemLogger.getInstance().warn(ioex);
                }
            }
            else
            {
                mbeanServer = ManagementFactory.getPlatformMBeanServer();
            }
            // 設定ファイルから属性の定義を取得する
            // 変数attrListStrの中身は以下の形式になっている
            //   <attribute n1>,<attribute n2>,...
            String[] attrList = attrListStr.split(",");
            for (String attrStr : attrList)
            {
                String attrName = attrStr;

                // 取得するJMXの計測値の設定情報を出力する
                StringBuilder sb = new StringBuilder();
                sb.append("(JMX mesuerment) ");
                sb.append("ObjectName[").append(objectName).append("] ");
                sb.append("attribute[").append(attrName).append("] ");
                SystemLogger.getInstance().info(sb.toString());

                try
                {
                    // JMXの計測値を取得するクラスを初期化して追加する
                    MBeanValueGetter getter =
                                              new MBeanValueGetter(mbeanServer,
                                                                   mbeanServerConnection,
                                                                   objectName, attrName,
                                                                   remoteEnable, id);
                    getters.addMBeanValueGetter(getter);
                    if (entity != null)
                    {
                        entity.addResource(getter);
                    }

                }
                catch (MalformedObjectNameException ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }

        // 可変系列用のリソース取得としてJMXの計測値を登録する
        multiResourceMap.put(TelegramConstants.ITEMNAME_JMX, getters);
    }

    /**
     * JMX接続オブジェクトを追加する。
     * @param id ID
     * @param connector {@link JMXConnector}オブジェクト
     */
    public static void addConnector(String id, JMXConnector connector)
    {
        jmxConnectorMap__.put(id, connector);
    }

    /**
     * 指定したIDに対応するJMXの再接続要求を行う。
     * @param id 再接続要求を行うEntityのID
     */
    public static void recconect(String id)
    {
        JMXConnectEntity entity = jmxConnectEntityMap__.get(id);
        if (entity != null)
        {
            reconnect(entity);
        }
    }

    /**
     * JMXの再接続要求を行う。
     * @param entity JMXの再接続要求
     */
    public static void reconnect(JMXConnectEntity entity)
    {
        synchronized (jmxConnectThread__)
        {
            jmxConnectThread__.addConnectEntity(entity);
            jmxConnectThread__.notifyAll();
        }
    }

    /**
     * JMXの切断処理を行う。
     */
    public static void close()
    {
        for (Entry<String, JMXConnector> connectorEntry : jmxConnectorMap__.entrySet())
        {
            JMXConnector connector = connectorEntry.getValue();
            try
            {
                connector.close();
            }
            catch (IOException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }
}
