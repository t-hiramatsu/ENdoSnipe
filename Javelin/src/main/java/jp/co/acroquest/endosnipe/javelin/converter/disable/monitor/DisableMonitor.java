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
package jp.co.acroquest.endosnipe.javelin.converter.disable.monitor;

import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.JavelinMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.concurrent.monitor.ConcurrentAccessMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CollectionMonitor;

/**
 * ConcurrentAccessMonitor、CollectionMonitorを無効化する。
 * 
 * @author eriguchi
 */
public class DisableMonitor implements JavelinMonitor
{
    /** 複数スレッドによるアクセスの判定中かどうかを表すフラグ */
    private static ThreadLocal<Boolean> isConcurrentAccessTracing__;

    /** 複数スレッドによるアクセスの判定中かどうかを表すフラグ */
    private static ThreadLocal<Boolean> isCollectionTracing__;

    static
    {
        isConcurrentAccessTracing__ = new ThreadLocal<Boolean>() {
            protected Boolean initialValue()
            {
                return Boolean.TRUE;
            }
        };

        isCollectionTracing__ = new ThreadLocal<Boolean>() {
            protected Boolean initialValue()
            {
                return Boolean.TRUE;
            }
        };
    }

    public static void preProcess()
    {
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isConcurrentMonitorEnabled())
        {
            isConcurrentAccessTracing__.set(ConcurrentAccessMonitor.isTracing());
            ConcurrentAccessMonitor.setTracing(Boolean.FALSE);
        }

        if (tree.isCollectionMonitorEnabled())
        {
            isCollectionTracing__.set(CollectionMonitor.isTracing());
            CollectionMonitor.setTracing(Boolean.FALSE);
        }
    }

    public static void postProcess()
    {
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isConcurrentMonitorEnabled())
        {
            ConcurrentAccessMonitor.setTracing(isConcurrentAccessTracing__.get());
        }

        if (tree.isCollectionMonitorEnabled())
        {
            CollectionMonitor.setTracing(isCollectionTracing__.get());
        }
    }
}
