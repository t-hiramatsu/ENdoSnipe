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
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * JMXの接続を行うためのスレッドクラスです。
 * @author fujii
 *
 */
public class JMXConnectThread extends Thread
{
    /** 再接続処理を行うまでのスリープ時間 */
    private static final long             SLEEP_TIME    = 60 * 1000;

    private Map<String, JMXConnectEntity> resourceMap_  =
                                                          new ConcurrentHashMap<String, JMXConnectEntity>();

    /** ループを継続するかどうかを表すフラグ */
    private boolean                       continueFlag_ = true;

    /**
     * コンストラクタです。
     */
    public JMXConnectThread()
    {
        super();
        setName("JMXConnectThread-" + getId());
        setDaemon(true);
    }

    /**
     * 
     */
    public void run()
    {
        while (continueFlag_)
        {
            try
            {
                synchronized (this)
                {
                    if (this.resourceMap_.size() == 0)
                    {
                        wait();
                    }
                }
                Set<Entry<String, JMXConnectEntity>> entitySet = resourceMap_.entrySet();
                Iterator<Entry<String, JMXConnectEntity>> it = entitySet.iterator();
                while (it.hasNext())
                {
                    Entry<String, JMXConnectEntity> entry = it.next();
                    JMXConnectEntity entity = entry.getValue();
                    boolean result = reconnect(entity);
                    if (result)
                    {
                        it.remove();
                    }
                }
                Thread.sleep(SLEEP_TIME);
            }
            catch (InterruptedException ex)
            {
                SystemLogger.getInstance().debug(ex);
            }
        }
    }

    /**
     * 再接続処理を行う。
     * @param entity {@link JMXConnectEntity}オブジェクト
     * @return 再接続処理に成功した場合はtrue、失敗した場合はfalseを返す。
     */
    private boolean reconnect(JMXConnectEntity entity)
    {
        try
        {
            String id = entity.getId();
            String remoteUrl = entity.getUrl();
            String user = entity.getUser();
            String password = entity.getPassword();
            JMXServiceURL url = new JMXServiceURL(remoteUrl);
            HashMap<String, String[]> env = new HashMap<String, String[]>();
            String[] credentials = new String[]{user, password};
            env.put(JMXConnector.CREDENTIALS, credentials);
            JMXConnector connector = JMXConnectorFactory.connect(url, env);
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            List<MBeanValueGetter> resourceList = entity.getResourceList();
            for (MBeanValueGetter resource : resourceList)
            {
                resource.setConnection(connection);
            }
            MBeanCollectorInitializer.addConnector(id, connector);
        }
        catch (MalformedURLException ex)
        {
            SystemLogger.getInstance().warn(ex);
            // URLが不正な場合は再接続できないため、以降の処理を行わないようにtrueを返す。
            return true;
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }
        return true;
    }

    /**
     * 接続対象を追加する。
     * @param entity JMX接続対象
     */
    public void addConnectEntity(JMXConnectEntity entity)
    {
        String id = entity.getId();
        if (resourceMap_.get(id) == null)
        {
            resourceMap_.put(id, entity);
        }
    }
}
