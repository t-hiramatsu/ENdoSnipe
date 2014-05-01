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
package jp.co.acroquest.endosnipe.javelin.converter.leak.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinMessages;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.LeakDetectEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

import org.netbeans.insane.scanner.ScannerUtils;

/**
 * �R���N�V�����N���X�A�}�b�v�N���X�̃T�C�Y���펞�Ď�����Ď��N���X
 * �Ď��Ώۂɑ΂����Q�ƂƃL�[��ێ����A���Ԋu�ɓo�^���ꂽ�R���N�V�����T�C�Y���擾����
 * �Ď��ΏۂƂȂ�N���X���������Ɉȉ��̏��������{���邱�ƂŁA�Ď��Ώ��Ɏ����ǉ�����B
 * �uCollectionTracer.addTraceTarget(this);�v
 * 
 * TODO ���̃N���X�����ŁA�Ď��Ώۂ�Map�Ȃǂ𗘗p���Ă��邽�߁AStackOverFlowError�ɂȂ�ꍇ������BThreadLocal�ȏ�Ԃ����ׂ����H
 * 
 * @author eriguchi
 */
public class CollectionMonitor
{
    private static JavelinConfig javelinConfig__ = new JavelinConfig();

    /** �Ď��Ώہi�R���N�V�����j��ێ�����}�b�v */
    private static Map<String, CollectionMonitorEntry> listMap__ =
            new ConcurrentHashMap<String, CollectionMonitorEntry>();

    /** �Ď��Ώہi�R���N�V�����j��ێ�����}�b�v */
    private static Map<String, CollectionMonitorEntry> queueMap__ =
            new ConcurrentHashMap<String, CollectionMonitorEntry>();

    /** �Ď��Ώہi�R���N�V�����j��ێ�����}�b�v */
    private static Map<String, CollectionMonitorEntry> setMap__ =
            new ConcurrentHashMap<String, CollectionMonitorEntry>();

    /** �Ď��Ώہi�}�b�v�j��ێ�����}�b�v */
    private static Map<String, CollectionMonitorEntry> mapMap__ =
            new ConcurrentHashMap<String, CollectionMonitorEntry>();

    /** �T�C�Y�̑傫��������A�ȉ��̐��̃G���g���[�����X�g�����ĕԂ� */
    public static final int TOPTRACENUMBER = 5;

    private static ThreadLocal<Boolean> isTracing__ = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue()
        {
            return Boolean.TRUE;
        }
    };

    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private CollectionMonitor()
    {
        // Do Nothing.
    }

    /**
     * �R���N�V�����N���X���Ď��Ώۂɉ�����
     * 
     * @param target �Ď��Ώۂɉ�����R���N�V�����N���X
     * @param element add��addAll���ꂽ�v�f
     */
    public static void addTrace(final Collection<?> target, Object element)
    {
        //���ʎq������
        String identifier = StatsUtil.createIdentifier(target);

        if (listMap__.containsKey(identifier) || queueMap__.containsKey(identifier)
                || setMap__.containsKey(identifier))
        {
            CollectionMonitorEntry collectionMonitorEntry = null;
            if (listMap__.containsKey(identifier))
            {
                collectionMonitorEntry = listMap__.get(identifier);
            }
            else if (queueMap__.containsKey(identifier))
            {
                collectionMonitorEntry = queueMap__.get(identifier);
            }
            else if (setMap__.containsKey(identifier))
            {
                collectionMonitorEntry = setMap__.get(identifier);
            }

            if (collectionMonitorEntry != null)
            {
                synchronized (collectionMonitorEntry)
                {
                    detect(identifier, collectionMonitorEntry, element);
                }
            }

            return;
        }

        CollectionMonitorEntry collectionMonitorEntry =
                new CollectionMonitorEntry(identifier, target);
        detect(identifier, collectionMonitorEntry, element);

        if (target instanceof Queue<?>)
        {
            queueMap__.put(identifier, collectionMonitorEntry);
            getSortedQueueList();
        }
        else if (target instanceof List<?>)
        {
            listMap__.put(identifier, collectionMonitorEntry);
            getSortedListList();
        }
        else if (target instanceof Set<?>)
        {
            setMap__.put(identifier, collectionMonitorEntry);
            getSortedSetList();
        }
        else
        {
            // target��Collection�̃C���X�^���X
            listMap__.put(identifier, collectionMonitorEntry);
            getSortedListList();
        }
    }

    /**
     * Leak�����o�������̃C�x���g���쐬���܂��B
     * @param identifier ���ʎq
     * @param target ���[�N���Ă���I�u�W�F�N�g���܂ރR���N�V����
     * @param count ���[�N���Ă���I�u�W�F�N�g�̗v�f��
     * @param element add��addAll�Aput���\�b�h�̗v�f
     * @return {@link CommonEvent}�I�u�W�F�N�g
     */
    static CommonEvent createLeakDetectedEvent(final String identifier, final Collection<?> target,
            int count, Object element)
    {
        CommonEvent event = new LeakDetectEvent();

        int leakSize = 0;

        //�R���N�V�����̃I�u�W�F�N�g�T�C�Y�Z�o�����̕��ׂ��������߁A
        //�T�C�Y�͐ݒ�t���O��ON�ɂȂ��Ă���ꍇ�̂ݎZ�o/�o�͂��s��
        if (javelinConfig__.isLeakCollectionSizePrint() == true && target != null)
        {
                try
                {
                    leakSize = ScannerUtils.recursiveSizeOf(target, null);
                }
                catch (Exception ex)
                {
                    String key = "javelin.converter.leak.monitor.CannotGetSize";
                    String message = JavelinMessages.getMessage(key);
                    SystemLogger.getInstance().warn(message, ex);
                }
        }
        
        // Leak���o�������N�������I�u�W�F�N�g�̃N���X�����擾����B
        String className = null;
        if (element != null)
        {
            className = element.getClass().getName();
        }

        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
        String staceTrace = ThreadUtil.getStackTrace(stacktraces, javelinConfig__.getTraceDepth());

        event.addParam(EventConstants.PARAM_LEAK_IDENTIFIER, identifier);
        event.addParam(EventConstants.PARAM_LEAK_THRESHOLD,
                       String.valueOf(javelinConfig__.getCollectionSizeThreshold()));
        event.addParam(EventConstants.PARAM_LEAK_COUNT, String.valueOf(count));
        if (javelinConfig__.isLeakCollectionSizePrint() == true)
        {
            event.addParam(EventConstants.PARAM_LEAK_SIZE, String.valueOf(leakSize));
        }
        if (className != null)
        {
            event.addParam(EventConstants.PARAM_LEAK_CLASS_NAME, className);
        }
        event.addParam(EventConstants.PARAM_LEAK_STACK_TRACE, staceTrace);

        return event;
    }

    /**
     * �}�b�v�N���X���Ď��Ώۂɉ�����
     * 
     * @param target �Ď��Ώۂɉ�����}�b�v�N���X
     * @param element put���ꂽ�v�f
     */
    public static void addTrace(final Map<?, ?> target, Object element)
    {
        //���ʎq������
        String identifier = StatsUtil.createIdentifier(target);

        if (mapMap__.containsKey(identifier))
        {
            CollectionMonitorEntry collectionMonitorEntry = mapMap__.get(identifier);
            detect(identifier, collectionMonitorEntry, element);

            return;
        }
        
        CollectionMonitorEntry collectionMonitorEntry =
                new CollectionMonitorEntry(identifier, target);
        
        synchronized (collectionMonitorEntry)
        {
            detect(identifier, collectionMonitorEntry, element);
        }

        mapMap__.put(identifier, collectionMonitorEntry);
        getSortedMapList();
    }

    /**
     * ���������[�N�̌��o���s���B
     * 
     * ����(��1)��1��X�^�b�N�g���[�X���擾���A
     * ���Ɍ��o�ς݂̃X�^�b�N�g���[�X(��2)�ƈ�v���Ȃ��ꍇ�̂�
     * ���������[�N�Ƃ��Č��o����B
     * 
     * <ul>
     * <li>javelin.leak.interval �X�^�b�N�g���[�X�����񂲂ƂɎ擾���邩(��1)</li>
     * <li>javelin.leak.traceMax �X�^�b�N�g���[�X��ێ������(��2)</li>
     * <li>javelin.leak.traceDepth �X�^�b�N�g���[�X�擾�̐[��(��2)</li>
     * </ul>
     * 
     * @param identifier ���[�N�����I�u�W�F�N�g�̎��ʎq�B
     * @param monitorEntry ���[�N�����I�u�W�F�N�g�̓��e�B
     * @param element add��addAll�Aput���ꂽ�v�f
     */
    static void detect(final String identifier,
            final CollectionMonitorEntry monitorEntry, Object element)
    {
        if (monitorEntry == null)
        {
            return;
        }

        monitorEntry.updateEntryNumber();

        int interval = javelinConfig__.getCollectionInterval();
        if (interval <= 0)
        {
            return;
        }

        if (monitorEntry.getDetectCount() % interval == 0)
        {
            Collection<?> target = getTargetByCollection(monitorEntry);
            CommonEvent detectedEvent;

            int hashCode = getLeakDetectHashCode(identifier);
            if (monitorEntry.containsTrace(hashCode) == false)
            {
                detectedEvent = createLeakDetectedEvent(identifier, target,
                                                        monitorEntry.getEntryNumber(), element);
                addEvent(monitorEntry, detectedEvent, hashCode, false);
            }
            else
            {
                int entryNumber = monitorEntry.getEntryNumber();
                int collectionSizeThreshold = javelinConfig__.getCollectionSizeThreshold();
                int detectedSize = monitorEntry.getDetectedSize();
                if (entryNumber >= detectedSize + collectionSizeThreshold)
                {
                    detectedEvent = createLeakDetectedEvent(identifier, target,
                                                            monitorEntry.getEntryNumber(), element);
                    monitorEntry.clearAllTrace();
                    addEvent(monitorEntry, detectedEvent, hashCode, true);
                }
            }
        }
        monitorEntry.setDetectCount(monitorEntry.getDetectCount() + 1);
    }

    private static void addEvent(final CollectionMonitorEntry monitorEntry,
            CommonEvent detectedEvent, int hashCode, boolean clear)
    {
        StatsJavelinRecorder.addEvent(detectedEvent, clear);
        
        int traceMax = javelinConfig__.getCollectionTraceMax();
        int traceCount = monitorEntry.getTraceCount();
        if (traceMax > 0)
        {
            if (traceCount >= traceMax)
            {
                monitorEntry.removeTrace();
            }
            monitorEntry.addTrace(hashCode);
            monitorEntry.setDetectedSize(monitorEntry.getEntryNumber());
        }
    }

    /**
     * �Ď��ΏۃR���N�V�����܂��̓}�b�v���A�R���N�V�����ŕԂ��܂��B<br />
     *
     * �Ď��Ώۂ� GC �ŉ������Ă���ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
     *
     * @param monitorEntry Collection �̃G���g��
     * @return �Ď��ΏۃR���N�V�����A�܂��͊Ď��Ώۃ}�b�v���܂ރR���N�V����
     */
    private static Collection<?> getTargetByCollection(final CollectionMonitorEntry monitorEntry)
    {
        Collection<?> target = monitorEntry.getCollection();
        if (target == null)
        {
            Map<?, ?> targetMap = monitorEntry.getMap();
            if (targetMap != null)
            {
                target = Arrays.asList(new Object[]{targetMap});
            }
        }
        return target;
    }

    private static int getLeakDetectHashCode(String identifier)
    {
        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
        String staceTrace =
                ThreadUtil.getStackTrace(stacktraces,
                                         javelinConfig__.getCollectionLeakDetectDepth());
        return staceTrace.hashCode();
    }

    /**
     * List�̗v�f���̃��X�g���擾����B
     * 
     * @return List�̗v�f���̃��X�g�B
     */
    public static List<CollectionMonitorEntry> getSortedListList()
    {
        return getSortedList(listMap__);
    }

    /**
     * Set�̗v�f���̃��X�g���擾����B
     * 
     * @return Set�̗v�f���̃��X�g�B
     */
    public static List<CollectionMonitorEntry> getSortedSetList()
    {
        return getSortedList(setMap__);
    }

    /**
     * Queue�̗v�f���̃��X�g���擾����B
     * 
     * @return Queue�̗v�f���̃��X�g�B
     */
    public static List<CollectionMonitorEntry> getSortedQueueList()
    {
        return getSortedList(queueMap__);
    }

    /**
     * Map�̗v�f���̃��X�g���擾����B
     * 
     * @return Map�̗v�f���̃��X�g�B
     */
    public static List<CollectionMonitorEntry> getSortedMapList()
    {
        List<CollectionMonitorEntry> returnList = getSortedList(mapMap__);

        return makeTopSizeList(returnList);
    }

    private static List<CollectionMonitorEntry> getSortedList(
            final Map<String, CollectionMonitorEntry> listMap)
    {
        List<CollectionMonitorEntry> returnList = new ArrayList<CollectionMonitorEntry>();
        Set<Entry<String, CollectionMonitorEntry>> collectionKeySet = listMap.entrySet();
        for (Entry<String, CollectionMonitorEntry> targetReferenceEntry : collectionKeySet)
        {
            String targetIdentifier = targetReferenceEntry.getKey();
            CollectionMonitorEntry target = targetReferenceEntry.getValue();
            target.updateEntryNumber();

            if (target.exists())
            {
                returnList.add(target);
            }
            else
            {
                listMap.remove(targetIdentifier);
            }
        }

        return makeTopSizeList(returnList);
    }

    /**
     * �Ώۂ̃��X�g�̃T�C�YTop�����X�g�Ƃ��ĕԂ�
     * ���̃��X�g�̓\�[�g���ꂽ��ԂƂȂ�
     * 
     * @param targetList �Ώۂ̃��X�g
     * @return �Ώۃ��X�g���̃T�C�Y�̗v�f�̃��X�g
     */
    private static List<CollectionMonitorEntry> makeTopSizeList(
            final List<CollectionMonitorEntry> targetList)
    {
        Collections.sort(targetList, new Comparator<CollectionMonitorEntry>() {
            public int compare(final CollectionMonitorEntry entry1,
                    final CollectionMonitorEntry entry2)
            {
                return entry2.getEntryNumber() - entry1.getEntryNumber();
            }
        });

        List<CollectionMonitorEntry> topSizeList =
                targetList.subList(0, (targetList.size() < TOPTRACENUMBER) ? targetList.size()
                        : TOPTRACENUMBER);
        return topSizeList;
    }

    /**
     * �w�肵��Collection���T�C�Y��臒l�𒴂��Ă�ꍇ�ɁACollection���Ď��Ώۂɉ�����B
     * 
     * @param target �Ď��Ώی���Collection�B
     * @param element add�܂���addAll���ꂽ�v�f
     */
    public static void preProcessCollectionAdd(final Collection<?> target, Object element)
    {
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isCollectionMonitorEnabled() == false)
        {
            return;
        }
        
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }

        isTracing__.set(Boolean.FALSE);
        try
        {
            if (isTraceTarget(target))
            {
                addTrace(target, element);
            }
        }
        finally
        {
            isTracing__.set(Boolean.TRUE);
        }
    }

    /**
     * �w�肵��Map���T�C�Y��臒l�𒴂��Ă�ꍇ�ɁAMap���Ď��Ώۂɉ�����B
     * 
     * @param targetMap �Ď��Ώی���Map�B
     * @param element put���ꂽ�v�f
     */
    public static void preProcessMapPut(final Map<?, ?> targetMap, Object element)
    {
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();
        CallTree         tree     = recorder.getCallTree();
        
        if (tree.isCollectionMonitorEnabled() == false)
        {
            return;
        }
        
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }

        isTracing__.set(Boolean.FALSE);
        try
        {
            if (isTraceTarget(targetMap))
            {
                addTrace(targetMap, element);
            }
        }
        finally
        {
            isTracing__.set(Boolean.TRUE);
        }
    }

    /**
     * �Ď��Ώۂɉ����邩�ǂ����𔻒肷��B
     * 
     * @param target �Ď��Ώی��B
     * @return �Ď��Ώۂɉ����邩�ǂ����B
     */
    private static boolean isTraceTarget(final Map<?, ?> target)
    {
        int size;
        try
        {
            size = target.size();
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            size = 0;
        }
        return size > javelinConfig__.getCollectionSizeThreshold();
    }

    /**
     * �Ď��Ώۂɉ����邩�ǂ����𔻒肷��B
     * 
     * @param target �Ď��Ώی��B
     * @return �Ď��Ώۂɉ����邩�ǂ����B
     */
    private static boolean isTraceTarget(final Collection<?> target)
    {
        int size;
        try
        {
            size = target.size();
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            size = 0;
        }
        return size > javelinConfig__.getCollectionSizeThreshold();
    }

    /**
     * �Ď������s���邩�ǂ�����ݒ肷��B
     * 
     * @param isTracing �Ď������s���邩�ǂ����B
     */
    public static void setTracing(final Boolean isTracing)
    {
        isTracing__.set(isTracing);
    }

    /**
     * �Ď������s���邩�ǂ������擾����B
     * 
     * @return �Ď������s���邩�ǂ����B
     */
    public static Boolean isTracing()
    {
        return isTracing__.get();
    }
}
