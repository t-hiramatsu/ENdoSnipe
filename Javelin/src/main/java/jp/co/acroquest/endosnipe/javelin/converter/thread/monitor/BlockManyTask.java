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
 * �u���b�N���������邩�ǂ������`�F�b�N����B
 * 
 * @author eriguchi
 *
 */
public class BlockManyTask implements ThreadMonitorTask
{
    /** ���b�N�擾�҂��X���b�h�̃L�[�B */
    private static final String KEY_THREAD_MONITOR_COUNTOVER = "thread.monitor.countover.";

    /** �u���b�N���������邩�ǂ�����臒l�B */
    private final long blockThreshold_;

    /** �u���b�N�񐔂���������ۂɎ擾����X���b�h���̐��B */
    private final int threadInfoNum_;

    /**
     * �R���X�g���N�^�B
     * 
     * @param blockThreshold �u���b�N���������邩�ǂ�����臒l�B
     * @param threadInfoNum �u���b�N�񐔂���������ۂɎ擾����X���b�h���̐��B 
     */
    public BlockManyTask(final long blockThreshold, final int threadInfoNum)
    {
        this.blockThreshold_ = blockThreshold;
        this.threadInfoNum_ = threadInfoNum;
    }

    /**
     * �u���b�N���������邩�ǂ����𔻒肷��B
     * 
     * 
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param threadInfo ���݂̃X���b�h���B
     * @return �u���b�N���p�����Ă��邩�ǂ����B
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
        //����͉������Ȃ�
        return;
    }

    /**
     * {@inheritDoc}
     */
    public void updateInfo(final Long threadId, ThreadInfo prevThreadInfo)
    {
        //����͉������Ȃ�
        return;
    }

    /**
     * {@inheritDoc}
     */
    public void clearInfo(Long threadId)
    {
        //����͉������Ȃ�
        return;
    }
}
