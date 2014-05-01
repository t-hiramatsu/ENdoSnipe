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
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ブロックが多すぎるかどうかをチェックする。
 * 
 * @author eriguchi
 *
 */
public class BlockManyTask implements ThreadMonitorTask
{
    /** ロック取得待ちスレッドのキー。 */
    private static final String KEY_THREAD_MONITOR_COUNTOVER = "thread.monitor.countover.";

    /** ブロックが多すぎるかどうかの閾値。 */
    private final long blockThreshold_;

    /** ブロック回数が多すぎる際に取得するスレッド情報の数。 */
    private final int threadInfoNum_;

    /**
     * コンストラクタ。
     * 
     * @param blockThreshold ブロックが多すぎるかどうかの閾値。
     * @param threadInfoNum ブロック回数が多すぎる際に取得するスレッド情報の数。 
     */
    public BlockManyTask(final long blockThreshold, final int threadInfoNum)
    {
        this.blockThreshold_ = blockThreshold;
        this.threadInfoNum_ = threadInfoNum;
    }

    /**
     * ブロックが多すぎるかどうかを判定する。
     * 
     * 
     * @param prevThreadInfo 前回のスレッド情報。
     * @param threadInfo 現在のスレッド情報。
     * @return ブロックが継続しているかどうか。
     */
    public boolean isTarget(final ThreadInfo prevThreadInfo, final ThreadInfo threadInfo)
    {

        if (threadInfo == null)
        {
            return false;
        }

        long blockDelta = threadInfo.getBlockedCount() - prevThreadInfo.getBlockedCount();
        return blockDelta > this.blockThreshold_;
    }

    /**
     * 指定したnodeにロックの状況を追加する。
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

        Long threadId = threadInfo.getThreadId();
        ThreadInfo[] threadInfos = new ThreadInfo[this.threadInfoNum_];

        for (int index = 0; index < threadInfos.length; index++)
        {
            ThreadInfo threadInfoForStackTrace = ThreadUtil.getThreadInfo(threadId, maxDepth);
            if (threadInfoForStackTrace == null)
            {
                break;
            }

            if (threadInfoForStackTrace.getThreadState() == Thread.State.RUNNABLE)
            {
                continue;
            }

            String threadInfoStr = WaitMonitor.toString(threadId, threadInfoForStackTrace);
            node.setLoggingValue(KEY_THREAD_MONITOR_COUNTOVER + index, threadInfoStr);
        }
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
