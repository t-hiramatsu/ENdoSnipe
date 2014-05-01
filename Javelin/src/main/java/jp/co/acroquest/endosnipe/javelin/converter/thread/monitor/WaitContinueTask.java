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
package jp.co.acroquest.endosnipe.javelin.converter.thread.monitor;

import java.lang.management.ThreadInfo;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.converter.wait.monitor.WaitMonitor;

/**
 * waitが継続しているかどうかを判定する。
 * 
 * @author eriguchi
 */
public class WaitContinueTask implements ThreadMonitorTask
{

    /** waiしていたスレッドのキー。 */
    private static final String KEY_THREAD_WAIT_THREAD = "thread.wait.thread";

    /**
     * waitが継続しているかどうかを判定する。
     * 
     * 
     * @param prevThreadInfo 前回のスレッド情報。
     * @param threadInfo 現在のスレッド情報。
     * @return waitが継続しているかどうか。
     */
    public boolean isTarget(final ThreadInfo prevThreadInfo, final ThreadInfo threadInfo)
    {
        return threadInfo != null && threadInfo.getWaitedCount() == prevThreadInfo.getWaitedCount()
                && threadInfo.getWaitedTime() != prevThreadInfo.getWaitedTime();
    }

    /**
     * 指定したnodeにwaitの状況を追加する。
     * 
     * @param node 対象のCallTreeNode。
     * @param threadInfo 現在のスレッド情報。
     * @param prevThreadInfo 前回のスレッド情報。
     * @param maxDepth 取得するスタックトレースの深さ。
     */
    public void updateNode(final CallTreeNode node, final ThreadInfo threadInfo,
            final ThreadInfo prevThreadInfo, final int maxDepth)
    {
        if (node == null)
        {
            return;
        }

        String threadInfoStr = WaitMonitor.toString(threadInfo.getThreadId(), threadInfo);
        node.setLoggingValue(KEY_THREAD_WAIT_THREAD, threadInfoStr);
    }

    /**
     * {@inheritDoc}
     */
    public void sendEvent(final Long threadId, ThreadInfo threadInfo, ThreadInfo prevThreadInfo,
            int maxDepth)
    {
        //現状は何もしない
        return;
    }

    /**
     * {@inheritDoc}
     */
    public void updateInfo(final Long threadId, ThreadInfo prevThreadInfo)
    {
        //現状は何もしない
        return;
    }

    /**
     * {@inheritDoc}
     */
    public void clearInfo(Long threadId)
    {
        //現状は何もしない
        return;
    }
}
