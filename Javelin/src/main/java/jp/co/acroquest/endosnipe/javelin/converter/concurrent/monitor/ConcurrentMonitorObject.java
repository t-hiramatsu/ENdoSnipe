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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * オブジェクトのハッシュコードとスレッドIDの関係を保持するエントリ
 * @author fujii
 */
public class ConcurrentMonitorObject
{
    /** 監視対象のオブジェクト。 */
    private WeakReference<Object> ref_;

    /** ハッシュコード */
    private int hashCode_;

    /** 識別子。 */
    private String identifier_;

    /** 前回のスレッドID */
    private long prevThreadId_;

    /** 各項目。 */
    private List<ConcurrentMonitorItem> itemList_;

    /** ロックオブジェクトのリスト。 */
    private List<String> lockedObjectList_;

    /** スレッド番号順のコンパレータ。 */
    private Comparator<? super ConcurrentMonitorItem> comparator_ =
            new Comparator<ConcurrentMonitorItem>() {
                public int compare(ConcurrentMonitorItem o1, ConcurrentMonitorItem o2)
                {
                    if (o1 == o2)
                    {
                        return 0;
                    }
                    if (o1 == null || o2 == null)
                    {
                        return 0;
                    }

                    if (o1.getThreadId() == o2.getThreadId())
                    {
                        return 0;
                    }
                    if (o1.getThreadId() < o2.getThreadId())
                    {
                        return -1;
                    }
                    
                    return 1;
                }
            };

    /** 保存するメッセージの最大数 */
    private static final int MAX_MESSAGE_NUM = 50;

    /**
     * コンストラクタ。
     * 
     * @param obj 対象のオブジェクト。
     * @param identifier 識別子
     */
    public ConcurrentMonitorObject(final Object obj, final String identifier)
    {
        this.ref_ = new WeakReference<Object>(obj);
        this.hashCode_ = identifier.hashCode();
        this.identifier_ = identifier;
        this.itemList_ = new ArrayList<ConcurrentMonitorItem>();
    }

    /**
     * ハッシュコードを取得する。
     * @return ハッシュコード
     */
    public int getHashCode()
    {
        return this.hashCode_;
    }

    /**
     * スレッドIDが含まれているかどうかを取得する。
     * 
     * @param threadId スレッドＩＤ。
     * @return スレッドIDが含まれているかどうか。
     */
    public boolean containsThreadId(long threadId)
    {
        int result = search(threadId);
        return result >= 0;
    }

    private int search(long threadId)
    {
        synchronized (this.itemList_)
        {
            ConcurrentMonitorItem key = new ConcurrentMonitorItem();
            key.setThreadId(threadId);
            int result = Collections.binarySearch(this.itemList_, key, comparator_);
            return result;
        }
    }

    /**
     * スレッドアクセス情報を保存する。
     * 
     * @param threadId スレッドID。
     * @param stackTrace スタックトレース
     * @param lockeObjectList ロックしているオブジェクトのリスト。
     * 
     */
    public void addTrace(final long threadId, final String stackTrace,
            final List<String> lockeObjectList)
    {
        // 保存するメッセージの数が最大値に達している場合は、無視する。
        if (this.itemList_.size() >= MAX_MESSAGE_NUM)
        {
            return;
        }

        String threadName = Thread.currentThread().getName();
        ConcurrentMonitorItem item = new ConcurrentMonitorItem();
        item.setThreadId(threadId);
        item.setTime(System.currentTimeMillis());
        item.setStackTrace(stackTrace);
        item.setLockedObjectList(lockeObjectList);
        item.setThreadName(threadName);

        synchronized (this.itemList_)
        {
            int threadIndex = search(threadId);
            if (threadIndex < 0)
            {
                this.itemList_.add(-1 - threadIndex, item);
            }
        }

    }

    public List<String> getLockedObjectList()
    {
        return this.lockedObjectList_;
    }

    public void setLockedObjectList(List<String> prevList)
    {
        this.lockedObjectList_ = prevList;
    }

    public String getIdentifier()
    {
        return identifier_;
    }

    public void setIdentifier(String identifier)
    {
        identifier_ = identifier;
    }

    public List<ConcurrentMonitorItem> getItemMap()
    {
        return Collections.unmodifiableList(itemList_);
    }

    public Object getRef()
    {
        return ref_.get();
    }

    public long getPrevThreadId()
    {
        return prevThreadId_;
    }

    public void setPrevThreadId(long prevThreadId)
    {
        prevThreadId_ = prevThreadId;
    }
}
