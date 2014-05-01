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

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �X���b�h�̊J�n�A�I�����Ď����A�ȉ��̓��e���擾����B
 * 
 * �J�n���F
 * <ul>
 * <li>����</li>
 * <li>�X���b�hID</li>
 * <li>�X���b�h��</li>
 * <li>�J�n���̃X�^�b�N�g���[�X</li>
 * <li>�J�n���̃X���b�hID</li>
 * <li>�J�n���̃X���b�h��</li>
 * </ul>
 * 
 * �I�����F
 * <ul>
 * <li>�I������</li>
 * <li>�X���b�hID</li>
 * <li>�X���b�h��</li>
 * <li>��O</li>
 * </ul>
 * ���擾����B
 * 
 * @author eriguchi
 */
public class ThreadLeakMonitor
{
    /**
     * �R���X�g���N�^�B
     */
    private ThreadLeakMonitor()
    {
        // Do Nothing
    }

    /**
     * �X���b�h�̊J�n���ɌĂяo�����B
     * 
     * @param thread �J�n����X���b�h�B
     */
    public static void postThreadStart(final Thread thread)
    {
        try
        {
            StackTraceElement[] stackTraces = ThreadUtil.getCurrentStackTrace();
            CommonEvent event =
                    createEvent("Thread Start", ThreadUtil.getThreadId(), thread, stackTraces);
            event.setLevel(CommonEvent.LEVEL_INFO);
            StatsJavelinRecorder.addEvent(event);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �X���b�h�̏I�����ɌĂяo�����B
     */
    public static void postThreadRun()
    {
        try
        {
            Thread currentThread = Thread.currentThread();
            StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
            CommonEvent event =
                    createEvent("Thread Stop", ThreadUtil.getThreadId(), currentThread, stacktraces);
            event.setLevel(CommonEvent.LEVEL_INFO);
            StatsJavelinRecorder.addEvent(event);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * ��O�������̃X���b�h�̏I�����ɌĂяo�����B
     * 
     * @param throwable ����������O�B
     */
    public static void postThreadRunNG(final Throwable throwable)
    {
        try
        {
            Thread currentThread = Thread.currentThread();
            StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
            CommonEvent event =
                    createEvent("Exception Occur", ThreadUtil.getThreadId(), currentThread,
                                stacktraces);
            event.setLevel(CommonEvent.LEVEL_WARN);
            StatsJavelinRecorder.addEvent(event);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * Javelin���O�ɏo�͂���C�x���g���쐬���܂��B<br />
     * 
     * @param timing �C�x���g�ʒm�^�C�~���O(�J�n/�I��/���f)
     * @param threadId �X���b�hID
     * @param thread �X���b�h
     * @param stacktraces �X�^�b�N�g���[�X
     * @return�@{@link CommonEvent}�I�u�W�F�N�g
     */
    private static CommonEvent createEvent(String timing, final long threadId, final Thread thread,
            final StackTraceElement[] stacktraces)
    {
        CommonEvent event = new CommonEvent();
        event.setName(EventConstants.EVENT_THREAD_LEAK_CONTINUE);

        event.addParam(EventConstants.PARAM_THREAD_LEAK_TIMING, timing);
        event.addParam(EventConstants.PARAM_THREAD_LEAK_ID, String.valueOf(threadId));
        event.addParam(EventConstants.PARAM_THREAD_LEAK_NAME, thread.getName());
        event.addParam(EventConstants.PARAM_THREAD_LEAK_STATE, String.valueOf(thread.getState()));
        event.addParam(EventConstants.PARAM_THREAD_LEAK_STACKTRACE,
                       ThreadUtil.getStackTrace(stacktraces));

        return event;

    }
}
