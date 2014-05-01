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
package jp.co.acroquest.endosnipe.javelin.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * �X���b�h���������߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author eriguchi
 */
public class ThreadUtil
{
    /** �X���b�h���擾�pMXBean�B */
    private static ThreadMXBean threadMBean__ = ManagementFactory.getThreadMXBean();

    /** �X���b�h�_���v�̃w�b�_�J�n�� */
    private static final String THREAD_DUMP_HEAD_START = "Full thread dump ";

    /** �X���b�h�_���v�̃w�b�_�I���� */
    private static final String THREAD_DUMP_HEAD_END = "):";

    /** �X�^�b�N�g���[�X�����̊J�n�C���f�b�N�X�B */
    private static final int STACK_SEARCH_START_INDEX = 0;

    /** StringBuilder�I�u�W�F�N�g�̃f�t�H���g�T�C�Y */
    private static final int DEF_BUILDER_SIZE = 512;

    private static Field tidField__;

    /** java.lang.management.ThreadInfo#getLockedSynchronizers */
    private static Method getLockedSynchronizersMethod__ = null;

    /** java.lang.management.ThreadInfo#getLockedMonitors */
    private static Method getLockedMonitorsMethod__ = null;

    /** java.lang.management.ThreadInfo#getLockInfo*/
    private static Method getLockInfoMethod__ = null;

    /** java.lang.management.MonitorInfo#getLockedStackDepth*/
    private static Method getLockedStackDepthMethod__ = null;

    /** ���s���� */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * ���p���郁�\�b�h�AMXBean�̏��������s���܂��B
     * 
     * @param config Javelin�̐ݒ�
     */
    public static void init(JavelinConfig config)
    {
        try
        {
            // OracleAS�𗘗p�����ꍇ�AThead#getThreadID()����X���b�hID���擾����ƁA
            // ��ɌŒ�l���o�͂����̂ŁAThread�N���X�̃t�B�[���h����擾����B
            // �܂��AIBM��VM�𗘗p�������p�����ꍇ�ɂ́A"tid"�Ƃ����t�B�[���h���Ȃ����߁A"uniqueId"�𗘗p����B
            try
            {
                tidField__ = Thread.class.getDeclaredField("tid");
            }
            catch (SecurityException se)
            {
                SystemLogger.getInstance().debug(se);
            }
            catch (NoSuchFieldException nsfe)
            {
                tidField__ = Thread.class.getDeclaredField("uniqueId");
            }
            try
            {
                getLockedSynchronizersMethod__ =
                        ThreadInfo.class.getDeclaredMethod("getLockedSynchronizers");
                getLockInfoMethod__ = ThreadInfo.class.getDeclaredMethod("getLockInfo");
                getLockedMonitorsMethod__ = ThreadInfo.class.getDeclaredMethod("getLockedMonitors");
                try
                {
                    Class<?> monitorInfoClass = Class.forName("java.lang.management.MonitorInfo");
                    getLockedStackDepthMethod__ =
                            monitorInfoClass.getDeclaredMethod("getLockedStackDepth");
                }
                catch (ClassNotFoundException cne)
                {
                    SystemLogger.getInstance().debug(cne);
                }
            }
            catch (SecurityException se)
            {
                SystemLogger.getInstance().debug(se);
            }
            catch (NoSuchMethodException nme)
            {
                SystemLogger.getInstance().debug(nme);
            }

            if (tidField__ != null)
            {
                tidField__.setAccessible(true);
            }

            if (config.isThreadContentionMonitor()
                    && threadMBean__.isThreadContentionMonitoringSupported())
            {
                threadMBean__.setThreadContentionMonitoringEnabled(true);
            }
            if (threadMBean__.isThreadCpuTimeSupported())
            {
                threadMBean__.setThreadCpuTimeEnabled(true);
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    private static ThreadLocal<Long> tid__ = new ThreadLocal<Long>()
    {
        @Override
        protected Long initialValue()
        {
            Thread thread = Thread.currentThread();
            Long   tid    = ThreadUtil.getThreadId(thread);
            return tid;
        }
    };

    /**
     * �X�^�b�N�g���[�X���擾����B
     * 
     * @return �X�^�b�N�g���[�X�B
     */
    public static StackTraceElement[] getCurrentStackTrace()
    {
        Throwable throwable = new Throwable();
        return throwable.getStackTrace();
    }

    /**
     * �X�^�b�N�g���[�X�𕶎���ɕϊ����܂��B<br />
     * �������A�N���X��"javelin"���܂ރX�^�b�N�g���[�X�̍s�͕\�����܂���B<br />
     *  
     * @param stacktraces �X�^�b�N�g���[�X�B
     * @param depth �X�^�b�N�g���[�X�擾�̐[���B
     * @return �X�^�b�N�g���[�X������B
     */
    public static String getStackTrace(final StackTraceElement[] stacktraces, int depth)
    {
        StringBuilder traceBuffer = new StringBuilder();

        // �擪��javelin���܂ރX�^�b�N�͓ǂݔ�΂��B
        int index;
        for (index = STACK_SEARCH_START_INDEX; index < stacktraces.length; index++)
        {
            StackTraceElement stackTraceElement = stacktraces[index];
            String className = stackTraceElement.getClassName();
            if (className.contains("javelin") == false)
            {
                break;
            }
        }

        traceBuffer.append(NEW_LINE);
        for (; index < stacktraces.length && depth != 0; index++)
        {
            StackTraceElement stackTraceElement = stacktraces[index];
            String stackTraceLine = stackTraceElement.toString();
            traceBuffer.append("\tat ");
            traceBuffer.append(stackTraceLine);
            traceBuffer.append(NEW_LINE);

            depth--;
        }

        return traceBuffer.toString();
    }

    /**
     * "javelin"���܂߂��S�ẴX�^�b�N�g���[�X���擾���܂��B<br />
     * 
     * @param stacktraces �X�^�b�N�g���[�X
     * @return �X�^�b�N�g���[�X������
     */
    public static String getAllStackTrace(final StackTraceElement[] stacktraces)
    {
        StringBuilder builder = new StringBuilder(DEF_BUILDER_SIZE);
        for (int index = 0; index < stacktraces.length; index++)
        {
            StackTraceElement stackTraceElement = stacktraces[index];
            builder.append("\tat ");
            builder.append(stackTraceElement);
            builder.append(NEW_LINE);
        }
        return builder.toString();
    }

    /**
     * �X�^�b�N�g���[�X�𕶎���ɕϊ�����B
     *  
     * @param stacktraces �X�^�b�N�g���[�X�B
     * @return �X�^�b�N�g���[�X������B
     */
    public static String getStackTrace(final StackTraceElement[] stacktraces)
    {
        return getStackTrace(stacktraces, -1);
    }

    /**
     * �X���b�hID���擾����B
     * 
     * @return �X���b�hID�B
     */
    public static long getThreadId()
    {
        return tid__.get().longValue();
    }

    /**
     * �X���b�hID���擾����B
     * @param thread �X���b�h�B
     * 
     * @return �X���b�hID�B
     */
    public static Long getThreadId(final Thread thread)
    {
        Long tid = Long.valueOf(0);
        try
        {
            if (tidField__ != null)
            {
                tid = (Long)tidField__.get(thread);
            }
            else
            {
                tid = thread.getId();
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        return tid;
    }

    /**
     * �S�ẴX���b�hID���擾����B
     * 
     * @return �S�ẴX���b�hID�B
     */
    public static long[] getAllThreadIds()
    {
        return threadMBean__.getAllThreadIds();
    }

    /**
     * �X���b�h�����擾����B
     * 
     * @param maxDepth �[���B
     * @param threadId �X���b�hID�B
     * @return �X���b�h���B
     */
    public static ThreadInfo getThreadInfo(final long threadId, final int maxDepth)
    {
        return threadMBean__.getThreadInfo(threadId, maxDepth);
    }

    /**
     * �X���b�h�����擾����B
     * 
     * @param maxDepth �[���B
     * @param threadIds �X���b�hI�̔z��B
     * @return �X���b�h���̔z��B
     */
    public static ThreadInfo[] getThreadInfo(final long[] threadIds, final int maxDepth)
    {
        return threadMBean__.getThreadInfo(threadIds, maxDepth);
    }

    /**
     * �w�肵���X�^�b�N�g���[�X���������A�A�v���P�[�V�����̍ŏ��̃X�^�b�N�g���[�X�v�f���擾���܂��B
     * 
     * @param stackTrace �����Ώۂ̃X�^�b�N�g���[�X�B
     * 
     * @return �A�v���P�[�V�����̍ŏ��̃X�^�b�N�g���[�X�v�f�B
     */
    public static StackTraceElement getApplicationStack(StackTraceElement[] stackTrace)
    {
        for (int index = STACK_SEARCH_START_INDEX; index < stackTrace.length; index++)
        {
            StackTraceElement element = stackTrace[index];
            if (element.getClassName().contains("javelin") == false)
            {
                return element;
            }
        }

        return null;
    }

    /**
     * Full�X���b�h�_���v���擾���܂��B<br />
     * 
     * @return Full�X���b�h�_���v
     */
    public static String getFullThreadDump()
    {
        StringBuilder builder = new StringBuilder(DEF_BUILDER_SIZE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // �������o�͂���B
        long now = System.currentTimeMillis();
        builder.append(NEW_LINE);
        builder.append(format.format(now));
        builder.append(NEW_LINE);

        // �w�b�_�����o�͂���B
        builder.append(THREAD_DUMP_HEAD_START);
        builder.append(System.getProperty("java.vm.name"));
        builder.append(" (");
        builder.append(System.getProperty("java.version"));
        builder.append(THREAD_DUMP_HEAD_END);
        builder.append(NEW_LINE);

        // �e�X���b�h�̏����擾����B
        long[] threadIds = getAllThreadIds();
        ThreadInfo[] threadInfos = getThreadInfo(threadIds, Integer.MAX_VALUE);
        for (ThreadInfo threadInfo : threadInfos)
        {
            StackTraceElement[] elements = threadInfo.getStackTrace();
            String threadDump = null;
            if (getLockedSynchronizersMethod__ != null && getLockedMonitorsMethod__ != null
                    && getLockInfoMethod__ != null && getLockedStackDepthMethod__ != null)
            {
                threadDump = getThreadDumpJava6(threadInfo, elements);
            }
            else
            {
                threadDump = getThreadDumpJava5(threadInfo, elements);

            }
            builder.append(threadDump);
        }

        return builder.toString();

    }

    /**
     * Java6 �𗘗p�����ꍇ�ɃX���b�h�_���v���擾���܂��B<br />
     * 
     * @param info {@link ThreadInfo}�I�u�W�F�N�g
     * @param elements {@link StackTraceElement}�I�u�W�F�N�g�̔z��
     * @return
     */
    private static String getThreadDumpJava6(ThreadInfo info, StackTraceElement[] elements)
    {
        StringBuilder sb = getThreadInfoBuffer(info);
        sb.append(NEW_LINE);
        try
        {
            for (int i = 0; i < elements.length; i++)
            {
                StackTraceElement ste = elements[i];
                sb.append("\tat " + ste.toString());
                sb.append(NEW_LINE);
                if (i == 0 && getLockInfoMethod__.invoke(info) != null)
                {
                    Thread.State ts = info.getThreadState();
                    switch (ts)
                    {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + getLockInfoMethod__.invoke(info));
                        sb.append(NEW_LINE);
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + getLockInfoMethod__.invoke(info));
                        sb.append(NEW_LINE);
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + getLockInfoMethod__.invoke(info));
                        sb.append(NEW_LINE);
                        break;
                    default:
                    }
                }

                Object[] lockedMonitors = (Object[])getLockedMonitorsMethod__.invoke(info);

                for (Object mi : lockedMonitors)
                {
                    Integer num = (Integer)getLockedStackDepthMethod__.invoke(mi);
                    if (num.intValue() == i)
                    {
                        sb.append("\t-  locked " + mi);
                        sb.append(NEW_LINE);
                    }
                }
            }
            Object[] locks = (Object[])getLockedSynchronizersMethod__.invoke(info);
            if (locks.length > 0)
            {
                sb.append(NEW_LINE);
                sb.append("\tNumber of locked synchronizers = " + locks.length);
                sb.append(NEW_LINE);
                for (Object li : locks)
                {
                    sb.append("\t- " + li);
                    sb.append(NEW_LINE);
                }
            }
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        sb.append(NEW_LINE);
        return sb.toString();
    }

    /**
     * Java5 �𗘗p�����ꍇ�ɃX���b�h�_���v���擾���܂��B<br />
     * 
     * @param info {@link ThreadInfo}�I�u�W�F�N�g
     * @param elements {@link StackTraceElement}�I�u�W�F�N�g�̔z��
     * @return
     */
    private static String getThreadDumpJava5(ThreadInfo info, StackTraceElement[] elements)
    {
        StringBuilder sb = getThreadInfoBuffer(info);
        sb.append(NEW_LINE);
        sb.append(ThreadUtil.getAllStackTrace(elements));
        sb.append(NEW_LINE);

        return sb.toString();
    }

    /**
     * �X���b�h�̏�Ԃ�StringBuffer�ɂ��ĕԂ��܂��B<br />
     * 
     * @param info {@link ThreadInfo}�I�u�W�F�N�g
     * @return �X���b�h�̏�Ԃ�StringBuffer�ɂ��ĕԂ������́B
     */
    private static StringBuilder getThreadInfoBuffer(ThreadInfo info)
    {
        StringBuilder sb =
                new StringBuilder("\"" + info.getThreadName() + "\"" + " Id=" + info.getThreadId()
                        + " " + info.getThreadState());
        if (info.getLockName() != null)
        {
            sb.append(" on " + info.getLockName());
        }
        if (info.getLockOwnerName() != null)
        {
            sb.append(" owned by \"" + info.getLockOwnerName() + "\" Id=" + info.getLockOwnerId());
        }
        if (info.isSuspended())
        {
            sb.append(" (suspended)");
        }
        if (info.isInNative())
        {
            sb.append(" (in native)");
        }
        return sb;
    }

    /**
     * �C���X�^���X�����֎~����B
     */
    private ThreadUtil()
    {
        //
    }

}
