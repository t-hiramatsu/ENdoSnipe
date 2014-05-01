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
 * �u���b�N���p�����Ă��邩�ǂ������`�F�b�N����B
 *
 * @author eriguchi
 *
 */
public class BlockContinueTask implements ThreadMonitorTask
{
    /** ���b�N�ێ��X���b�h�̃L�[�B */
    private static final String KEY_THREAD_MONITOR_OWNER = "thread.monitor.owner";

    /** ���b�N�擾�҂��X���b�h�̃L�[�B */
    private static final String KEY_THREAD_MONITOR_THREAD = "thread.monitor.thread";

    /** �u���b�N���Ԍp�����ێ��}�b�v */
    private Map<Long, Long> blockContinueInfoMap_;

    /**
     * �R���X�g���N�^�B
     * 
     * @param blockContinueInfoMap �u���b�N���Ԍp�����ێ��}�b�v
     */
    public BlockContinueTask(final Map<Long, Long> blockContinueInfoMap)
    {
        this.blockContinueInfoMap_ = blockContinueInfoMap;
    }

    /**
     * �u���b�N���p�����Ă��邩�ǂ����𔻒肷��B
     * 
     * 
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param threadInfo ���݂̃X���b�h���B
     * @return �u���b�N���p�����Ă��邩�ǂ����B
     */
    public boolean isTarget(final ThreadInfo prevThreadInfo, final ThreadInfo threadInfo)
    {

        return threadInfo != null
                && threadInfo.getBlockedCount() == prevThreadInfo.getBlockedCount()
                && threadInfo.getBlockedTime() != prevThreadInfo.getBlockedTime()
                && threadInfo.getLockOwnerId() != -1;
    }

    /**
     * �w�肵��node�Ƀ��b�N�̏󋵂�ǉ�����B
     * 
     * @param node �Ώۂ�CallTreeNode�B
     * @param threadInfo ���݂̃X���b�h���B
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param maxDepth �擾����X�^�b�N�g���[�X�̐[���B
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
     * ���݂̃X���b�h���A�O��̃X���b�h������Ƀu���b�N�p�����Ԃ��Z�o����B
     * �u���b�N�p�����Ԃ�臒l�ȏゾ�����ꍇ�A�C�x���g�𑗐M����B<br />
     * �u���b�N�p�����Ԃ͉��L�̎��ŎZ�o�B<br />
     * �u���b�N�p����񂪑��݂����ꍇ�F���݂̑��u���b�N���ԁ@�|�@�@�u���b�N�p�����̃u���b�N����<br />
     * �u���b�N�p����񂪖����݂̏ꍇ�F���݂̑��u���b�N���ԁ@�|�@�O��̑��u���b�N����<br />
     * 
     * @param threadId �X���b�hID
     * @param threadInfo ���݂̃X���b�h���B
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param maxDepth �擾����X�^�b�N�g���[�X�̐[���B
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
     * �O��̃X���b�h�����u���b�N�p�����Ƃ��ĕێ�����B<br />
     * 
     * @param threadId �X���b�hID
     * @param prevThreadInfo �O��̃X���b�h���B
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
