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

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.event.MethodStallEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �X�g�[�����\�b�h���Ď����邽�߂̃X���b�h�ł��B<br />
 * 
 * @author matsuoka
 *
 */
public class MethodStallMonitor implements Runnable
{
    /** Singleton�I�u�W�F�N�g */
    private static MethodStallMonitor instance__ = new MethodStallMonitor();

    /** Javelin�̐ݒ�B */
    private final JavelinConfig config_ = new JavelinConfig();
    
    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�B
     */
    private MethodStallMonitor()
    {
        // Do Nothing.
    }

    /**
     * {@link MethodStallMonitor}�I�u�W�F�N�g���擾���܂��B<br />
     * 
     * @return {@link MethodStallMonitor}�I�u�W�F�N�g
     */
    public static MethodStallMonitor getInstance()
    {
        return instance__;
    }

    /**
     * "javelin.method.stall.interval"�̊Ԋu���ƂɁA
     * ���\�b�h���X�g�[�����Ă��邩�ǂ������肵�܂��B
     */
    public void run()
    {
        try
        {
            Thread.sleep(JavelinTransformer.WAIT_FOR_THREAD_START);
        }
        catch (Exception ex)
        {
            // CHECKSTYLE:OFF
            ;
            // CHECKSTYLE:ON
        }
        
        while (true)
        {
            try
            {
                int sleepTime = this.config_.getMethodStallInterval();
                Thread.sleep(sleepTime);
                
                if (this.config_.isMethodStallMonitor()) 
                {
                    int threshold = this.config_.getMethodStallThreshold();
                    checkMethodStall(threshold);
                }
            }
            catch (Throwable ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }
    
    /**
     * �X�g�[�����\�b�h�̑��݂��`�F�b�N����B
     * @param threshold 臒l
     */
    private void checkMethodStall(int threshold)
    {
        long currentTime = System.currentTimeMillis();

        long []threadIds = ThreadUtil.getAllThreadIds();
        for (long id : threadIds)
        {
            CallTreeNode node = CallTreeRecorder.getNode(id);
            if (isStall(node, currentTime, threshold))
            {
                int depth = this.config_.getMethodStallTraceDepth();
                MethodStallEvent event =  createEvent(id, node, depth, threshold);
                StatsJavelinRecorder.addEvent(event);
                node.setStalled(true);
                
                Invocation rootInv = node.getRootNode().getInvocation();
                rootInv.addMethodStallCount(1);
            }
        }
    }

    /**
     * �Ώۂ�CallTreeNode���X�g�[�����Ă��邩�𔻒肵�܂��B
     * @param node ����Ώۂ�CallTreeNode
     * @param currentTime ���ݎ���
     * @param threshold 臒l
     * @return �X�g�[�����Ă�����<code>true</code>��Ԃ��܂��B
     */
    private boolean isStall(CallTreeNode node, long currentTime, int threshold)
    {
        if (node != null && !node.isStalled() && currentTime - node.getStartTime() > threshold)
        {
            return true;
        }
        return false;
    }
    
    /**
     * �X�g�[�����\�b�h���o�C�x���g���쐬���܂��B
     * @param threadId ���\�b�h���X�g�[�����Ă���X���b�h��ID
     * @param node CallTreeNode
     * @param maxDepth �擾����X�^�b�N�g���[�X�̐[��
     * @param threshold 臒l
     * @return �쐬�����C�x���g
     */
    private MethodStallEvent createEvent(
            long threadId, 
            CallTreeNode node, 
            int maxDepth, 
            int threshold)
    {
        MethodStallEvent event = new MethodStallEvent();

        event.addParam(EventConstants.PARAM_METHOD_STALL_THRESHOLD, String.valueOf(threshold));
        
        Invocation invocation = node.getInvocation();
        event.addParam(EventConstants.PARAM_METHOD_STALL_CLASS_NAME, invocation.getClassName());
        event.addParam(EventConstants.PARAM_METHOD_STALL_METHOD_NAME, invocation.getMethodName());
        
        ThreadInfo info = ThreadUtil.getThreadInfo(threadId, maxDepth);
        event.addParam(EventConstants.PARAM_METHOD_STALL_THREAD_ID,
                       String.valueOf(info.getThreadId()));
        event.addParam(EventConstants.PARAM_METHOD_STALL_THREAD_NAME,
                       info.getThreadName());
        event.addParam(EventConstants.PARAM_METHOD_STALL_THREAD_STATE,
                       info.getThreadState().toString());

        String stackTrace = ThreadUtil.getStackTrace(info.getStackTrace());
        event.addParam(EventConstants.PARAM_METHOD_STALL_STACKTRACE, stackTrace);
        
        return event;
    }
}
