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
package jp.co.acroquest.endosnipe.javelin.converter.wait.monitor;

import java.lang.management.ThreadInfo;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * Object#wait、Thread.sleepの実行時間を監視し、
 * <ul>
 * <li>スレッドダンプ</li>
 * <li>実行時間</li>
 * </ul>
 * を取得する。
 *  
 * @author eriguchi
 */
public class WaitMonitor
{
    /** waitした際の情報を記録する。 */
    private static ThreadLocal<WaitMonitorInfo> waitMonitorInfo__ = createWaitMonitorInfo();

    /** 初期化済みかどうか。 */
    private static boolean initialized__ = false;

    /**
     * コンストラクタ。
     */
    private WaitMonitor()
    {
        // Do Nothing
    }

    /**
     * WaitMonitorInfoを生成する。
     * 
     * @return WaitMonitorInfo。
     */
    private static ThreadLocal<WaitMonitorInfo> createWaitMonitorInfo()
    {
        return new ThreadLocal<WaitMonitorInfo>() {
            @Override
            protected synchronized WaitMonitorInfo initialValue()
            {
                return new WaitMonitorInfo();
            }
        };
    }

    /**
     * ThreadMXBeanを初期化する。
     */
    private static void init()
    {
    }

    /**
     * waitする前に呼び出される。
     * 開始時刻を記録する。
     */
    public static void preProcess()
    {
        if (initialized__ == false)
        {
            init();
        }

        getMonitorInfo().setStartTime(System.currentTimeMillis());
    }

    /**
     * waitした後に呼び出される。
     * CallTreeNodeに情報を追加する。
     * 追加する情報は以下の通り。
     * <ul>
     * <li>wait.duration.シーケンス番号 waitした時間(ミリ秒)。</li>
     * <li>wait.stackTrace.シーケンス番号 waitした箇所のスタックトレース。</li>
     * </ul> 
     */
    public static void postProcess()
    {
        WaitMonitorInfo monitorInfo = getMonitorInfo();
        monitorInfo.setEndTime(System.currentTimeMillis());
        long duration = monitorInfo.getEndTime() - monitorInfo.getStartTime();

        long sequenceNum = monitorInfo.getSequenceNum() + 1;
        monitorInfo.setSequenceNum(sequenceNum);

        String stackTrace = getStackTrace();

        monitorInfo.setStackTrace(stackTrace);

        CallTreeNode node = CallTreeRecorder.getInstance().getCallTreeNode();
        if (node != null)
        {
            node.setLoggingValue("wait.duration." + sequenceNum, Long.valueOf(duration));
            node.setLoggingValue("wait.stackTrace." + sequenceNum, monitorInfo.getStackTrace());
        }
    }

    /**
     * スレッド情報を文字列に変換する。
     * 
     * @param threadId スレッドID。
     * @param threadInfo スレッド情報。
     * @return スレッド情報文字列。
     */
    public static String toString(final long threadId, final ThreadInfo threadInfo)
    {
        return toString(threadId, threadInfo, threadInfo.getStackTrace());
    }

    /**
     * スレッド情報を文字列に変換する。
     * 
     * @param threadId スレッドID。
     * @param threadInfo スレッド情報。
     * @param stacktraces スタックトレース。
     * @return スレッド情報文字列。
     */
    public static String toString(final long threadId, final ThreadInfo threadInfo,
            final StackTraceElement[] stacktraces)
    {
        StringBuffer threadBuffer = new StringBuffer();
        threadBuffer.append("ThreadId:");
        threadBuffer.append(threadId);
        if (threadInfo != null)
        {
            threadBuffer.append(",");
            threadBuffer.append("ThreadName:");
            threadBuffer.append(threadInfo.getThreadName());
            threadBuffer.append(",");
            threadBuffer.append("ThreadState:");
            threadBuffer.append(threadInfo.getThreadState());
            threadBuffer.append(",");
            threadBuffer.append("LockName:");
            threadBuffer.append(threadInfo.getLockName());
            threadBuffer.append(",");
            threadBuffer.append("LockOwnerId:");
            threadBuffer.append(threadInfo.getLockOwnerId());
            threadBuffer.append(",");
            threadBuffer.append("LockOwnerName:");
            threadBuffer.append(threadInfo.getLockOwnerName());
        }

        threadBuffer.append("\n");
        threadBuffer.append(ThreadUtil.getStackTrace(stacktraces));

        return threadBuffer.toString();
    }

    /**
     * スタックトレースを文字列に変換する。
     *  
     * @return スタックトレース文字列。
     */
    private static String getStackTrace()
    {
        long threadId = ThreadUtil.getThreadId();
        ThreadInfo threadInfo = null;
        if (threadId > 0)
        {
            threadInfo = ThreadUtil.getThreadInfo(Long.valueOf(threadId), Integer.MAX_VALUE);
        }

        String trace = toString(threadId, threadInfo, ThreadUtil.getCurrentStackTrace());
        return trace;
    }

    /**
     * WaitMonitorInfoを取得する。
     * 
     * @return WaitMonitorInfo。
     */
    private static WaitMonitorInfo getMonitorInfo()
    {
        return waitMonitorInfo__.get();
    }
}
