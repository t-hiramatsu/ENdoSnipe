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
 * Object#wait�AThread.sleep�̎��s���Ԃ��Ď����A
 * <ul>
 * <li>�X���b�h�_���v</li>
 * <li>���s����</li>
 * </ul>
 * ���擾����B
 *  
 * @author eriguchi
 */
public class WaitMonitor
{
    /** wait�����ۂ̏����L�^����B */
    private static ThreadLocal<WaitMonitorInfo> waitMonitorInfo__ = createWaitMonitorInfo();

    /** �������ς݂��ǂ����B */
    private static boolean initialized__ = false;

    /**
     * �R���X�g���N�^�B
     */
    private WaitMonitor()
    {
        // Do Nothing
    }

    /**
     * WaitMonitorInfo�𐶐�����B
     * 
     * @return WaitMonitorInfo�B
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
     * ThreadMXBean������������B
     */
    private static void init()
    {
    }

    /**
     * wait����O�ɌĂяo�����B
     * �J�n�������L�^����B
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
     * wait������ɌĂяo�����B
     * CallTreeNode�ɏ���ǉ�����B
     * �ǉ�������͈ȉ��̒ʂ�B
     * <ul>
     * <li>wait.duration.�V�[�P���X�ԍ� wait��������(�~���b)�B</li>
     * <li>wait.stackTrace.�V�[�P���X�ԍ� wait�����ӏ��̃X�^�b�N�g���[�X�B</li>
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
     * �X���b�h���𕶎���ɕϊ�����B
     * 
     * @param threadId �X���b�hID�B
     * @param threadInfo �X���b�h���B
     * @return �X���b�h��񕶎���B
     */
    public static String toString(final long threadId, final ThreadInfo threadInfo)
    {
        return toString(threadId, threadInfo, threadInfo.getStackTrace());
    }

    /**
     * �X���b�h���𕶎���ɕϊ�����B
     * 
     * @param threadId �X���b�hID�B
     * @param threadInfo �X���b�h���B
     * @param stacktraces �X�^�b�N�g���[�X�B
     * @return �X���b�h��񕶎���B
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
     * �X�^�b�N�g���[�X�𕶎���ɕϊ�����B
     *  
     * @return �X�^�b�N�g���[�X������B
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
     * WaitMonitorInfo���擾����B
     * 
     * @return WaitMonitorInfo�B
     */
    private static WaitMonitorInfo getMonitorInfo()
    {
        return waitMonitorInfo__.get();
    }
}
