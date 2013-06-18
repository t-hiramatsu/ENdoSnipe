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
package jp.co.acroquest.endosnipe.javelin.converter.concurrent.monitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ConcurrentMonitorObjectの追加、参照、削除を行うクラスです。
 * 
 * @author eriguchi
 */
public class ConcurrentAccessRegistry
{
    /** クリーンアップを行う間隔。 */
    private static final int                     CLEANUP_INTERVAL = 10000;

    /** 操作回数。 */
    private int                                  count_;

    /** キーがオブジェクト識別子、値がConcurrentMonitorObjectのマップ。 */
    private Map<Object, ConcurrentMonitorObject> entryMap_;

    /**
     * コンストラクタ。
     */
    public ConcurrentAccessRegistry()
    {
        Map<Object, ConcurrentMonitorObject> map = new HashMap<Object, ConcurrentMonitorObject>();
        entryMap_ = Collections.synchronizedMap(map);
        this.count_ = 0;
    }

    /**
     * ConcurrentMonitorObjectを追加します。
     * 
     * @param newEntry ConcurrentMonitorObject。
     */
    public synchronized void add(ConcurrentMonitorObject newEntry)
    {
        this.count_++;
        if (this.count_ > CLEANUP_INTERVAL)
        {
            this.count_ = 0;
            cleanup();
        }

        Object ref = newEntry.getRef();
        if (ref != null)
        {
            entryMap_.put(newEntry.getIdentifier(), newEntry);
        }
    }

    /**
     * 参照がなくなったエントリを削除する。
     */
    private void cleanup()
    {
        synchronized (this.entryMap_)
        {
            Set<Entry<Object, ConcurrentMonitorObject>> entrySet = this.entryMap_.entrySet();
            Iterator<Entry<Object, ConcurrentMonitorObject>> iterator = entrySet.iterator();
            while (iterator.hasNext())
            {
                Entry<Object, ConcurrentMonitorObject> entry = iterator.next();
                Object ref = entry.getValue().getRef();
                if (ref == null)
                {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * ConcurrentMonitorObjectを削除します。
     * 
     * @param identifier 監視対象オブジェクト。
     */
    public synchronized void remove(String identifier)
    {
        this.count_++;

        entryMap_.remove(identifier);
    }

    /**
     * ConcurrentMonitorObjectを取得します。
     * 
     * @param identifier 監視対象オブジェクト。
     * @return ConcurrentMonitorObject。
     */
    public ConcurrentMonitorObject get(String identifier)
    {
        this.count_++;
        ConcurrentMonitorObject entry;
        entry = entryMap_.get(identifier);

        return entry;
    }
}
