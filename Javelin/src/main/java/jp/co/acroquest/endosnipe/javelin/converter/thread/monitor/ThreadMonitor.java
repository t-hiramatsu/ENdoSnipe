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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.wait.monitor.WaitMonitor;
import jp.co.acroquest.endosnipe.javelin.event.DeadLockDetectedEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �X���b�h�̃��b�N��Ԃ��Ď�����B
 * 
 * @author eriguchi
 *
 */
public class ThreadMonitor implements Runnable
{
    /** �X���b�h���폜����臒l�B */
    private static final int THREAD_CLEAN_THRESHOLD = 50;

    /** �X���b�hID�A�X���b�h���̃}�b�v�B */
    private final Map<Long, ThreadInfo> prevThreadInfoMap_ = new HashMap<Long, ThreadInfo>();

    /** �A���u���b�N���Ԃ̕ێ��}�b�v */
    private final Map<Long, Long> blockContinueInfoMap_ = new HashMap<Long, Long>();

    /** Javelin�̐ݒ�B */
    private final JavelinConfig config_ = new JavelinConfig();

    /** �X���b�h�̊Ď����s�����ǂ����̑O��ݒ�B */
    private boolean prevThreadMonitor_ = false;

    /** ThreadMXBean�I�u�W�F�N�g */
    private static ThreadMXBean threadMXBeanInstance__ = null;

    /** java.lang.management.ThreadMXBean#findDeadlockedThreads�@�̎������\�b�h */
    private static Method findDeadlockedThreadsMethod__ = null;

    static
    {
        threadMXBeanInstance__ = ManagementFactory.getThreadMXBean();

        try
        {
            findDeadlockedThreadsMethod__ =
                    threadMXBeanInstance__.getClass().getDeclaredMethod("findDeadlockedThreads");
            findDeadlockedThreadsMethod__.setAccessible(true);
        }
        catch (NoSuchMethodException nsmex)
        {
            SystemLogger.getInstance().debug(nsmex);
        }
        catch (SecurityException sex)
        {
            SystemLogger.getInstance().debug(sex);
        }
    }

    /**
     * thread.monitor.interval(�~���b)�̊Ԋu�ŁA�X���b�h�̏󋵂��m�F���A
     * �O�񂩂�u���b�N���p�����Ă���ꍇ��CallTree�Ƀ��b�N�̏󋵂�ǉ�����B
     * �ǉ�������͈ȉ��̒ʂ�B
     * <ul>
     * <li>thread.monitor.owner ���b�N��ێ����Ă���X���b�h</li>
     * <li>thread.monitor.thread ���b�N�����҂��Ă���X���b�h</li>
     * </ul>
     */
    public void run()
    {
        try
        {
            Thread.sleep(JavelinTransformer.WAIT_FOR_THREAD_START);
        }
        catch (Exception ex)
        {
            ;
        }
        
        prevThreadMonitor_ = true;

        while (true)
        {
            try
            {
                boolean threadMonitor = config_.getThreadMonitor();
                if (prevThreadMonitor_ != threadMonitor)
                {
                    try
                    {
                        CallTreeRecorder.clearNode();
                    }
                    catch (Exception ex)
                    {
                        SystemLogger.getInstance().warn(ex);
                    }
                }

                sleep();
                
                boolean deadLockMonitor = config_.isDeadLockMonitor();
                if (deadLockMonitor == true)
                {
                    checkDeadLock();
                }

                if (threadMonitor == false)
                {
                    continue;
                }

                long[] threadIds = ThreadUtil.getAllThreadIds();

                checkThreads(threadIds);

                cleanupThreadInfoMap(threadIds);

                prevThreadMonitor_ = threadMonitor;

            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    private void checkThreads(final long[] threadIds)
    {
        int maxDepth = config_.getThreadMonitorDepth();
        for (long threadId : threadIds)
        {
            if (threadId == 0L)
            {
                continue;
            }
            checkThread(threadId, maxDepth);
        }
    }

    private void sleep()
    {
        try
        {
            Thread.sleep(config_.getThreadMonitorInterval());
        }
        catch (InterruptedException ie)
        {
            SystemLogger.getInstance().warn(ie);
        }
    }

    /**
     * �w�肵���X���b�hID�Ɋ܂܂�Ȃ��X���b�h�����폜����B
     * 
     * @param threadIds �X���b�hID�̔z��B
     */
    private void cleanupThreadInfoMap(final long[] threadIds)
    {
        int threadCount = threadIds.length;
        if (threadCount + THREAD_CLEAN_THRESHOLD >= prevThreadInfoMap_.size())
        {
            return;
        }

        Set<Long> threadSet = new HashSet<Long>();
        for (Long threadId : threadIds)
        {
            threadSet.add(threadId);
        }

        Set<Long> infoKeySet = prevThreadInfoMap_.keySet();

        infoKeySet.retainAll(threadSet);
    }

    private void checkThread(final long threadId, final int maxDepth)
    {
        ThreadInfo prevThreadInfo = prevThreadInfoMap_.remove(threadId);

        CallTreeNode node = CallTreeRecorder.getNode(threadId);

        Long threadIdLong = Long.valueOf(threadId);
        ThreadInfo threadInfo = ThreadUtil.getThreadInfo(threadIdLong, maxDepth);
        ThreadMonitorTask blockContinueTask = new BlockContinueTask(this.blockContinueInfoMap_);
        ThreadMonitorTask waitContinueTask = new WaitContinueTask();
        ThreadMonitorTask blockManyTask =
                new BlockManyTask(config_.getBlockThreshold(), config_.getBlockThreadInfoNum());
        ThreadMonitorTask[] tasks =
                new ThreadMonitorTask[]{blockContinueTask, waitContinueTask, blockManyTask};

        if (prevThreadInfo != null)
        {
            for (ThreadMonitorTask task : tasks)
            {
                boolean isTarget = task.isTarget(prevThreadInfo, threadInfo);
                if (isTarget)
                {
                    task.updateNode(node, threadInfo, prevThreadInfo, maxDepth);
                    task.sendEvent(threadIdLong, threadInfo, prevThreadInfo, maxDepth);
                    task.updateInfo(threadIdLong, prevThreadInfo);
                }
                else
                {
                    task.clearInfo(threadIdLong);
                }
            }
        }

        if (threadInfo != null)
        {
            prevThreadInfoMap_.put(threadIdLong, threadInfo);
        }
    }

    /**
     * �f�b�h���b�N���������Ă��邩�𔻒肷��B
     * �������Ă����ꍇ�A�C�x���g���o�͂���B
     */
    private void checkDeadLock()
    {
        long[] deadlockThreads = null;

        if (threadMXBeanInstance__ == null)
        {
            return;
        }

        if (findDeadlockedThreadsMethod__ == null)
        {
            return;
        }

        try
        {
            deadlockThreads = (long[])findDeadlockedThreadsMethod__.invoke(threadMXBeanInstance__);
        }
        catch (UnsupportedOperationException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        if (deadlockThreads == null || deadlockThreads.length == 0)
        {
            return;
        }

        Arrays.sort(deadlockThreads);
        SystemLogger.getInstance().debug("Deadlocks of thread " + Arrays.toString(deadlockThreads) + " are detected");

        DeadLockDetectedEvent event = new DeadLockDetectedEvent();
        int index = 1;
        int maxDepth = config_.getThreadMonitorDepth();

        for (long threadId : deadlockThreads)
        {
            Long threadIdLong = Long.valueOf(threadId);
            ThreadInfo threadInfo = ThreadUtil.getThreadInfo(threadIdLong, maxDepth);

            // �ŏ��̃X�^�b�N�g���[�X�����A�C�x���g�̔�r�Ɏg�p���邽�߁A�C�x���g�ɐݒ肷��
            if (index == 1)
            {
                String stackTraceStr = ThreadUtil.getStackTrace(threadInfo.getStackTrace());
                event.setStackTraceCompare(stackTraceStr);
            }

            String threadInfoStr = WaitMonitor.toString(threadId, threadInfo);
            event.addParam(EventConstants.PARAM_DEADLOCK_INFO + index, threadInfoStr);
            index++;
        }

        StatsJavelinRecorder.addEvent(event);
    }
}
