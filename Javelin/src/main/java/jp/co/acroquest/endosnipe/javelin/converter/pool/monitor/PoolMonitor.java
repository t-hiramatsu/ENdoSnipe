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
package jp.co.acroquest.endosnipe.javelin.converter.pool.monitor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * プールを監視するクラス
 * 
 * @author eriguchi
 *
 */
public class PoolMonitor
{
    private static ConcurrentHashMap<String, WeakReference<MonitoredPool>> poolMap__;

    private static ConcurrentHashMap<String, AtomicInteger> sequenceMap__;

    private static ConcurrentHashMap<String, String> nameMap__;

    static
    {
        poolMap__ = new ConcurrentHashMap<String, WeakReference<MonitoredPool>>();
        sequenceMap__ = new ConcurrentHashMap<String, AtomicInteger>();
        nameMap__ = new ConcurrentHashMap<String, String>();
    }

    /**
     * インスタンス化を阻止するプライベートコンストラクタ。
     */
    private PoolMonitor()
    {
        // Do Nothing.
    }

    /**
     * プールを追加する。
     * @param pool プール
     */
    public static void addPool(final MonitoredPool pool)
    {
        String objectID = StatsUtil.createIdentifier(pool);
        String resourceName = nameMap__.get(objectID);
        if (resourceName != null)
        {
            return;
        }
        String className = pool.getClass().getName();
        AtomicInteger sequence = sequenceMap__.get(className);
        if (sequence == null)
        {
            sequence = new AtomicInteger(0);
            sequenceMap__.put(className, sequence);
        }
        int next = sequence.addAndGet(1);
        String key = className + "_" + next;

        poolMap__.put(key, new WeakReference<MonitoredPool>(pool));
        nameMap__.put(objectID, key);
        pool.setObjectId(key);

        CommonEvent event = createEvent(objectID);
        StatsJavelinRecorder.addEvent(event);

    }

    /**
     * プールのリストを取得する。
     * @return プールのリスト
     */
    public static List<MonitoredPool> getPoolList()
    {
        cleanUpPool();

        List<MonitoredPool> poolList = new ArrayList<MonitoredPool>();
        for (WeakReference<MonitoredPool> poolReference : poolMap__.values())
        {
            MonitoredPool monitoredPool = poolReference.get();
            if (monitoredPool != null)
            {
                poolList.add(monitoredPool);
            }
        }

        return poolList;
    }

    private static void cleanUpPool()
    {
        List<String> gabageIdList = new ArrayList<String>();
        for (Map.Entry<String, WeakReference<MonitoredPool>> entry : poolMap__.entrySet())
        {
            String id = entry.getKey();
            WeakReference<MonitoredPool> poolReference = entry.getValue();
            if (poolReference.get() == null)
            {
                gabageIdList.add(id);
            }
        }
        for (String id : gabageIdList)
        {
            poolMap__.remove(id);
            removeObject(id);
        }
    }

    /**
     * オブジェクトを削除する。
     * @param id ID
     */
    private static void removeObject(String id)
    {
        Set<Entry<String, String>> entrySet = nameMap__.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext())
        {
            Entry<String, String> entry = iterator.next();
            String value = entry.getValue();
            if (value.equals(id))
            {
                iterator.remove();
            }
        }
    }

    /**
     * CommonsPool開始イベントを作成します。<br />
     * 
     * @param objectID オブジェクトID
     * @return CommonsPool開始イベント
     */
    private static CommonEvent createEvent(String objectID)
    {
        CommonEvent event = new CommonEvent();
        event.setName(EventConstants.NAME_COMMONSPOOL_INIT);
        event.addParam(EventConstants.PARAM_COMMONSPOOL_OBJECTID, objectID);
        String stackTrace = ThreadUtil.getStackTrace(ThreadUtil.getCurrentStackTrace());
        event.addParam(EventConstants.PARAM_COMMONSPOOL_STACKTRACE, stackTrace);
        event.setLevel(CommonEvent.LEVEL_INFO);

        return event;
    }
}
