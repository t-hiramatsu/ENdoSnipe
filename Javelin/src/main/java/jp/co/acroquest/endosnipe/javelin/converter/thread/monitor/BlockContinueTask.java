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
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.wait.monitor.WaitMonitor;
import jp.co.acroquest.endosnipe.javelin.event.BlockContinueEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ブロックが継続しているかどうかをチェックする。
 *
 * @author eriguchi
 *
 */
public class BlockContinueTask implements ThreadMonitorTask
{
    /** ロック保持スレッドのキー。 */
    private static final String KEY_THREAD_MONITOR_OWNER = "thread.monitor.owner";

    /** ロック取得待ちスレッドのキー。 */
    private static final String KEY_THREAD_MONITOR_THREAD = "thread.monitor.thread";

    /** ブロック時間継続情報保持マップ */
    private Map<Long, Long> blockContinueInfoMap_;

    /**
     * コンストラクタ。
     * 
     * @param blockContinueInfoMap ブロック時間継続情報保持マップ
     */
    public BlockContinueTask(final Map<Long, Long> blockContinueInfoMap)
    {
        this.blockContinueInfoMap_ = blockContinueInfoMap;
    }

    /**
     * ブロックが継続しているかどうかを判定する。
     * 
     * 
     * @param prevThreadInfo 前回のスレッド情報。
     * @param threadInfo 現在のスレッド情報。
     * @return ブロックが継続しているかどうか。
     */
    public boolean isTarget(final ThreadInfo prevThreadInfo, final ThreadInfo threadInfo)
    {

        return threadInfo != null
                && threadInfo.getBlockedCount() == prevThreadInfo.getBlockedCount()
                && threadInfo.getBlockedTime() != prevThreadInfo.getBlockedTime()
                && threadInfo.getLockOwnerId() != -1;
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

        String threadInfoStr = WaitMonitor.toString(threadInfo.getThreadId(), threadInfo);
        node.setLoggingValue(KEY_THREAD_MONITOR_THREAD, threadInfoStr);
        long lockOwnerId = threadInfo.getLockOwnerId();
        if (lockOwnerId == 0)
        {
            return;
        }

        ThreadInfo lockOwnerThreadInfo = ThreadUtil.getThreadInfo(lockOwnerId, maxDepth);
        if (lockOwnerThreadInfo != null)
        {
            String lockOwnerThreadInfoStr = WaitMonitor.toString(lockOwnerId, lockOwnerThreadInfo);
            node.setLoggingValue(KEY_THREAD_MONITOR_OWNER, lockOwnerThreadInfoStr);
        }
    }

    /**
     * 現在のスレッド情報、前回のスレッド情報を基にブロック継続時間を算出する。
     * ブロック継続時間が閾値以上だった場合、イベントを送信する。<br />
     * ブロック継続時間は下記の式で算出。<br />
     * ブロック継続情報が存在した場合：現在の総ブロック時間　－　　ブロック継続情報のブロック時間<br />
     * ブロック継続情報が未存在の場合：現在の総ブロック時間　－　前回の総ブロック時間<br />
     * 
     * @param threadId スレッドID
     * @param threadInfo 現在のスレッド情報。
     * @param prevThreadInfo 前回のスレッド情報。
     * @param maxDepth 取得するスタックトレースの深さ。
     */
    public void sendEvent(Long threadId, ThreadInfo threadInfo, ThreadInfo prevThreadInfo,
            int maxDepth)
    {

        Long blockStartTime = this.blockContinueInfoMap_.get(threadId);

        long blockDuration;

        if (blockStartTime != null)
        {
            blockDuration = threadInfo.getBlockedTime() - blockStartTime.longValue();
        }
        else
        {
            blockDuration = threadInfo.getBlockedTime() - prevThreadInfo.getBlockedTime();
        }
        
        JavelinConfig config = new JavelinConfig();
        
        if(config.getBlockTimeThreshold() > blockDuration)
        {
            return;
        }
        
        BlockContinueEvent event = new BlockContinueEvent();
        event.setName(EventConstants.EVENT_THREAD_BLOCK_CONTINUE);
        event.addParam(EventConstants.PARAM_THREAD_BLOCK_DURATION, String.valueOf(blockDuration));

        String threadInfoStr = WaitMonitor.toString(threadInfo.getThreadId(), threadInfo);
        event.addParam(EventConstants.PARAM_THREAD_MONITOR_THREAD, threadInfoStr);
        event.setStackTraceCompare(ThreadUtil.getStackTrace(threadInfo.getStackTrace()));

        long lockOwnerId = threadInfo.getLockOwnerId();

        if (lockOwnerId != 0)
        {
            ThreadInfo lockOwnerThreadInfo = ThreadUtil.getThreadInfo(lockOwnerId, maxDepth);
            if (lockOwnerThreadInfo != null)
            {
                String lockOwnerThreadInfoStr =
                        WaitMonitor.toString(lockOwnerId, lockOwnerThreadInfo);
                event.addParam(EventConstants.PARAM_THREAD_MONITOR_OWNER, lockOwnerThreadInfoStr);
            }
        }

        StatsJavelinRecorder.addEvent(event);
    }

    /**
     * 前回のスレッド情報をブロック継続情報として保持する。<br />
     * 
     * @param threadId スレッドID
     * @param prevThreadInfo 前回のスレッド情報。
     */
    public void updateInfo(Long threadId, ThreadInfo prevThreadInfo)
    {
        Long continueInfo = this.blockContinueInfoMap_.get(threadId);

        if (continueInfo == null)
        {
            Long prevBlockedTime = Long.valueOf(prevThreadInfo.getBlockedTime());
            this.blockContinueInfoMap_.put(threadId, prevBlockedTime);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clearInfo(Long threadId)
    {
        this.blockContinueInfoMap_.remove(threadId);
    }
}
