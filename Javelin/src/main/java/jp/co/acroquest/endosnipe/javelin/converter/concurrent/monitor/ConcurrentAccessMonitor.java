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
package jp.co.acroquest.endosnipe.javelin.converter.concurrent.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CollectionMonitor;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �����X���b�h�̓����A�N�Z�X���Ď�����N���X
 * 
 * @author fujii
 */
public class ConcurrentAccessMonitor
{
    /** ClassLoader.checkPackageAccess */
    private static final String CLASSLOADER_CHECKPACKAGEACCESS =
            "java.lang.ClassLoader.checkPackageAccess";

    /** �X���b�h���擾�pMXBean�B */
    private static ThreadMXBean threadMBean__ = ManagementFactory.getThreadMXBean();

    /** Finalizer�X���b�h�̖��́B */
    private static final String FINALIZER_NAME = "Finalizer";

    /** �����X���b�h�ɂ��A�N�Z�X�̔��蒆���ǂ�����\���t���O */
    private static ThreadLocal<Boolean> isTracing__;

    /** �A�N�Z�X����HashMap��o�^����B */
    private static Map<String, Object> accessMap__;

    /** �A�N�Z�X����HashMap��o�^����B */
    private static Map<String, AtomicInteger> countMap__;

    /** Javelin�̐ݒ�l */
    private static JavelinConfig config__ = new JavelinConfig();

    /** java.lang.management.ThreadMXBean#getThreadInfo */
    private static Method threadInfoMethod__ = null;

    /** java.lang.management.ThreadInfo#getLockedSynchronizers */
    private static Method lockedSynchronizersMethod__ = null;

    /** java.lang.management.LockInfo#getClassName */
    private static Method monitorClassNameMethod__ = null;

    /** java.lang.management.LockInfo#getIdentityHashCode */
    private static Method monitorHashCodeMethod__ = null;

    /** �Ď��Ώۂ̃I�u�W�F�N�g�Ǘ��N���X�B */
    private static ConcurrentAccessRegistry registry__ = new ConcurrentAccessRegistry();

    static
    {
        accessMap__ = Collections.synchronizedMap(new HashMap<String, Object>());
        countMap__ = Collections.synchronizedMap(new HashMap<String, AtomicInteger>());

        isTracing__ = new ThreadLocal<Boolean>() {
            protected Boolean initialValue()
            {
                return Boolean.TRUE;
            }
        };

        // Java6�ł̃��b�N���擾�p�̃��\�b�h������������B
        try
        {
            threadInfoMethod__ =
                    ThreadMXBean.class.getDeclaredMethod("getThreadInfo", long[].class,
                                                         boolean.class, boolean.class);
            lockedSynchronizersMethod__ =
                    ThreadInfo.class.getDeclaredMethod("getLockedSynchronizers");

            Class<?> monitorInfo = Class.forName("java.lang.management.LockInfo");
            monitorClassNameMethod__ = monitorInfo.getDeclaredMethod("getClassName");
            monitorHashCodeMethod__ = monitorInfo.getDeclaredMethod("getIdentityHashCode");
        }
        catch (Exception ex)
        {
            // ��������B
            SystemLogger.getInstance().debug(ex);
        }
    }

    /**
     * �v���C�x�[�g�R���X�g���N�^
     */
    private ConcurrentAccessMonitor()
    {
        // Do Nothing.
    }

    /**
     * ���\�b�h�Ăяo���O�̏���
     * @param obj �Ď�����I�u�W�F�N�g
     */
    public static void postProcess(final Object obj)
    {
        if (threadInfoMethod__ == null)
        {
            return;
        }

        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isConcurrentMonitorEnabled() == false)
        {
            return;
        }

        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }

        setTracing(Boolean.FALSE);

        synchronized (obj)
        {
            String identifier = StatsUtil.createIdentifier(obj);
            AtomicInteger integer = countMap__.get(identifier);
            if (integer == null || integer.decrementAndGet() == 0)
            {
                accessMap__.remove(identifier);
                countMap__.remove(identifier);
            }
        }

        setTracing(Boolean.TRUE);
    }

    /**
     * ���\�b�h�Ăяo����̏���
     * @param obj �Ď�����I�u�W�F�N�g
     */
    public static void preProcess(final Object obj)
    {
        if (threadInfoMethod__ == null)
        {
            return;
        }

        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isConcurrentMonitorEnabled() == false)
        {
            return;
        }
        
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }

        Boolean prev = CollectionMonitor.isTracing();
        CollectionMonitor.setTracing(Boolean.FALSE);
        setTracing(Boolean.FALSE);
        synchronized (obj)
        {
            String identifier = StatsUtil.createIdentifier(obj);
            checkConcurrentAccess(identifier, obj);
            accessMap__.put(identifier, obj);
            AtomicInteger integer = countMap__.get(identifier);
            if (integer == null)
            {
                countMap__.put(identifier, new AtomicInteger(1));
            }
            else
            {
                integer.incrementAndGet();
            }
        }
        setTracing(Boolean.TRUE);
        CollectionMonitor.setTracing(prev);
    }

    /**
     * ���j�^�����������ǂ�����Ԃ��܂��B<br />
     *
     * @return �������̏ꍇ�� <code>true</code>
     */
    public static Boolean isTracing()
    {
        return isTracing__.get();
    }

    /**
     * ���j�^���s�����ǂ�����ݒ肷��B
     * 
     * @param isTracing ���j�^���s�����ǂ����B
     */
    public static void setTracing(Boolean isTracing)
    {
        isTracing__.set(isTracing);
    }

    /**
     * �ΏۂƂ���I�u�W�F�N�g�ɑ΂��A�ȉ��̏����𖞂����Ă��邩�ǂ����𔻒肵�A
     * �������Ă���ꍇ�ɂ�Event�Ƃ��Ēʒm����B
     * 
     * <ol>
     * <li>�����X���b�h�ɂ��A�N�Z�X���s���Ă���B</li>
     * <li>�A�N�Z�X���Ƀ��b�N���Ă���I�u�W�F�N�g�ɋ��ʕ��������B</li>
     * </ol>
     * 
     * @param identifier ���ʎq�B
     * @param obj �I�u�W�F�N�g
     */
    private static void checkConcurrentAccess(String identifier, Object obj)
    {
        String threadName = Thread.currentThread().getName();
        if (FINALIZER_NAME.equals(threadName))
        {
            return;
        }

        // ���݃A�N�Z�X���Ă���X���b�h�̏����擾����B
        long threadId = ThreadUtil.getThreadId();
        ConcurrentMonitorObject entry = registry__.get(identifier);
        if (entry == null || entry.getRef() != obj)
        {
            ConcurrentMonitorObject newEntry = new ConcurrentMonitorObject(obj, identifier);
            registry__.add(newEntry);
            newEntry.setPrevThreadId(threadId);
            return;
        }

        // ���݂̃X���b�h�����Ɍ��o�ς݂̂��̂ł���΁A�������Ȃ��B
        if (entry.containsThreadId(threadId) || entry.getPrevThreadId() == threadId)
        {
            return;
        }

        // �񓯊������X���b�h�A�N�Z�X���s���Ă��Ȃ��ꍇ�́A�������Ȃ��B
        boolean concurrentAccess = isConcurrentAccess(threadId, entry);
        if (concurrentAccess == false)
        {
            return;
        }

        boolean isError = false;
        if (accessMap__.containsKey(identifier))
        {
            Object oldObj = accessMap__.get(identifier);
            if (obj == oldObj)
            {
                isError = true;
            }
        }

        // �g���[�X���O�ɔ񓯊������X���b�h�A�N�Z�X���o��ʒm����B
        CommonEvent event = createDetectedEvent(entry, isError);

        if (event != null)
        {
            StatsJavelinRecorder.addEvent(event);
        }
        else
        {
            registry__.remove(identifier);
        }
    }

    /**
     * �񓯊������X���b�h�A�N�Z�X���s���Ă��邩�ǂ����𔻒肷��B
     * 
     * @param threadId 
     * @param obj �Ώۂ̃I�u�W�F�N�g�B
     * @return�@�񓯊������X���b�h�A�N�Z�X���s���Ă��邩�ǂ����B
     */
    private static boolean isConcurrentAccess(long threadId, ConcurrentMonitorObject entry)
    {
        // ���b�N���擾�p��API�����݂��Ȃ���΁A�񓯊��A�N�Z�X�ƌ��Ȃ��Ȃ��B
        List<String> lockedObjectList;
        if (threadInfoMethod__ == null)
        {
            return false;
        }
        else
        {
            lockedObjectList = createLockObjectList();
        }

        // ���߂Ă̏ꍇ�͔񓯊��A�N�Z�X�Ƃ݂Ȃ��Ȃ��B
        List<String> prevList = entry.getLockedObjectList();
        if (prevList == null)
        {
            entry.setLockedObjectList(lockedObjectList);
            entry.setPrevThreadId(threadId);
            addTrace(threadId, entry, lockedObjectList);
            return false;
        }

        // ���b�N����Ă��Ȃ��ꍇ�́A�񓯊��A�N�Z�X�ƌ��Ȃ��B
        int lockCount = lockedObjectList.size();
        if (lockCount == 0 || prevList.size() == 0)
        {
            entry.setLockedObjectList(lockedObjectList);
            addTrace(threadId, entry, lockedObjectList);
            return true;
        }

        // ���b�N�̋��ʕ����݂̂��c���B
        prevList.retainAll(lockedObjectList);

        // ���b�N�̋��ʕ����������ꍇ�́A�񓯊��A�N�Z�X�ƌ��Ȃ��B
        if (prevList.size() == 0)
        {
            addTrace(threadId, entry, lockedObjectList);
            return true;
        }

        return false;
    }

    /**
     * ���b�N�I�u�W�F�N�g���擾����B
     * Java 6.0�ł̂ݎ��s�\�ł���B
     * 
     * @return ���b�N�I�u�W�F�N�g�̈ꗗ�B
     */
    private static List<String> createLockObjectList()
    {
        List<String> lockedObjectList = new CopyOnWriteArrayList<String>();
        ThreadInfo[] threadInfo;
        try
        {
            // TODO: ���\�ɑ傫�ȉe�������邽�߁AgetLockedSynchronizers���Ăԏ����͖����Ƃ��Ă���B
            //       Ver4.1�ł́AgetLockedSynchronizers���ĂԃR�[�h��L���ɂ��A�ݒ�ɂ��ON/OFF�o����悤�ɂ���B
            threadInfo =
                    (ThreadInfo[])threadInfoMethod__.invoke(threadMBean__,
                                                            new long[]{ThreadUtil.getThreadId()},
                                                            true, false);

            Object[] lockedMonitors = (Object[])lockedSynchronizersMethod__.invoke(threadInfo[0]);
            addLockObject(lockedObjectList, lockedMonitors);

          // Object[] lockedSynchronizers = (Object[])lockedMonitorsMethod__.invoke(threadInfo[0]);
          // addLockObject(lockedObjectList, lockedSynchronizers);

        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        return lockedObjectList;
    }

    /**
     * ���b�N�I�u�W�F�N�g�̃��X�g�Ƀ��b�N�I�u�W�F�N�g��ǉ�����B
     * 
     * @param lockedObjectList ���b�N�I�u�W�F�N�g�̃��X�g�B
     * @param lockedMonitors ���b�N�I�u�W�F�N�g�B
     * @throws IllegalAccessException�@���\�b�h���s���̃A�N�Z�X�G���[�B
     * @throws InvocationTargetException ���\�b�h���s���̗�O�B
     */
    private static void addLockObject(List<String> lockedObjectList, Object[] lockedMonitors)
        throws IllegalAccessException,
            InvocationTargetException
    {
        for (Object monitorInfo : lockedMonitors)
        {
            Object className = monitorClassNameMethod__.invoke(monitorInfo);
            Integer hashCode = (Integer)monitorHashCodeMethod__.invoke(monitorInfo);
            lockedObjectList.add(className + "@" + Integer.toHexString(hashCode));
        }
    }

    /** 
     * �g���[�X��ǉ�����B
     * 
     * @param threadId �X���b�hID�B 
     * @param entry �G���g���B
     */
    private static void addTrace(long threadId, ConcurrentMonitorObject entry,
            List<String> lockedObjectList)
    {
        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
        String stackTrace = ThreadUtil.getStackTrace(stacktraces, config__.getTraceDepth());

        entry.addTrace(threadId, stackTrace, lockedObjectList);
    }

    /**
     * �����X���b�h���oEvent���o�͂���B
     * 
     * @param entry �I�u�W�F�N�g�ւ̃A�N�Z�X���B
     * @param isError ��肻�̂��̂����o�������ǂ����B
     * 
     * @return �����X���b�h���oEvent�B
     */
    private static CommonEvent createDetectedEvent(ConcurrentMonitorObject entry, boolean isError)
    {
        String identifier = entry.getIdentifier();

        CommonEvent event = new CommonEvent();
        event.setName(EventConstants.NAME_CONCURRENT_ACCESS);
        event.addParam(EventConstants.PARAM_CONCURRENT_IDENTIFIER, identifier);

        if (isError)
        {
            event.setLevel(CommonEvent.LEVEL_ERROR);
        }
        else
        {
            return null;
        }

        List<ConcurrentMonitorItem> itemList = entry.getItemMap();
        int count = 0;
        for (ConcurrentMonitorItem item : itemList)
        {
            String stackTrace = item.getStackTrace();
            // ClassLoader.checkPackageAccess������HashMap��Java��synchronized�Ƃ͕ʂ̎d�g�݂œ�������邽�߁A��������B

            if (stackTrace.contains(CLASSLOADER_CHECKPACKAGEACCESS))
            {
                return null;
            }
            event.addParam(EventConstants.PARAM_CONCURRENT_THREAD + "." + count,
                           item.getThreadName());
            event.addParam(EventConstants.PARAM_CONCURRENT_TIME + "." + count, "" + item.getTime());
            event.addParam(EventConstants.PARAM_CONCURRENT_LOCK + "." + count, ""
                    + item.getLockedObjectList());
            event.addParam(EventConstants.PARAM_CONCURRENT_STACKTRACE + "." + count, stackTrace);
            count++;
        }

        return event;
    }
}
