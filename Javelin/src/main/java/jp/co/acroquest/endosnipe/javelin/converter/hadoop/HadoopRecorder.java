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
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.MBeanManager;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.RootInvocationManager;
import jp.co.acroquest.endosnipe.javelin.VMStatus;
import jp.co.acroquest.endosnipe.javelin.bean.Component;
import jp.co.acroquest.endosnipe.javelin.bean.ExcludeMonitor;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.bean.TripleState;
import jp.co.acroquest.endosnipe.javelin.communicate.AlarmListener;
import jp.co.acroquest.endosnipe.javelin.converter.hadoop.HadoopObjectAnalyzer.HadoopJobStatus;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CallTreeNodeMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.util.ConverterUtil;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.EventRepository;
import jp.co.acroquest.endosnipe.javelin.event.InvocationFullEvent;
import jp.co.acroquest.endosnipe.javelin.event.JavelinEventCounter;
import jp.co.acroquest.endosnipe.javelin.helper.VMStatusHelper;
import jp.co.acroquest.endosnipe.javelin.log.JavelinFileGenerator;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;
import jp.co.acroquest.endosnipe.javelin.record.AllRecordStrategy;
import jp.co.acroquest.endosnipe.javelin.record.JvnFileNotifyCallback;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * Hadoop�p���O���L�^����
 * @author asazuma
 *
 */
// TODO JobTracker�N���X��getJobStatus()�AsubmitJob()�Aheartbeat()�̂݊Ď����邱�Ƃ�z��
public class HadoopRecorder
{
    /** �A���[�����X�i�̃��X�g */
    private static final List<AlarmListener> ALARM_LISTENER_LIST = new ArrayList<AlarmListener>();

    /** ����������t���O */
    private static boolean                 initialized__;

    /** Javelin���O�o�̓N���X */
    private static JavelinFileGenerator    generator__;

    /** �L�^��������N���X */
    private static RecordStrategy          recordStrategy__;

    /** javelin�̐ݒ�t�@�C�� */
    private static JavelinConfig           config__
                                               = new JavelinConfig();

    /** �N���X���̏ȗ����t���O */
    private static boolean                 isSimplification__ = false;

    /** VM��Ԃ̎擾�ƋL�^�N���X */
    private static VMStatusHelper          vmStatusHelper__
                                               = new VMStatusHelper();

    /** �C�x���g�̏d�����`�F�b�N���邽�߂̃��|�W�g�� */
    private static EventRepository         eventRepository__
                                               = new EventRepository();

    /** ���s���̃W���u�̃��X�g */
    private static List<String>            runningJobList__
                                               = Collections.synchronizedList(new ArrayList<String>());

    static
    {
        isSimplification__ = config__.isClassNameSimplify();
    }

    /**
     * �R���X�g���N�^���B�؂����ăC���X�^���X�������Ȃ�
     */
    private HadoopRecorder()
    {
        // �������Ȃ�
    }

    /**
     * ����������Ă��邩��Ԃ��B
     *
     * @return true:����������Ă���Afalse:����������Ă��Ȃ�.
     */
    public static boolean isInitialized()
    {
        return initialized__;
    }

    /**
     * �ݒ�N���X��ݒ肷��B
     *
     * @param config �R���t�B�O
     */
    public static void setJavelinConfig(final JavelinConfig config)
    {
        config__ = config;
    }

    /**
     * �X���b�h��ID��ݒ肷��
     *
     * @param threadId �X���b�hID
     */
    public static void setThreadid(final String threadId)
    {
        CallTree callTree = CallTreeRecorder.getInstance().getCallTree();
        callTree.setThreadID(threadId);
    }

    /**
     * �����������B AlarmListener�̓o�^���s���B RecordStrategy������������B
     * MBeanServer�ւ�ContainerMBean�̓o�^���s���B
     * ���J�pHTTP�|�[�g���w�肳��Ă����ꍇ�́AHttpAdaptor�̐����Ɠo�^���s���B
     *
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    public static void javelinInit(final JavelinConfig config)
    {
        if (initialized__ == true) return ;

        try
        {
            // �G���[���K�[������
            SystemLogger.initSystemLog(config);

            // ���O�o�̓N���X�̃C���X�^���X����
            generator__ = new JavelinFileGenerator(config);

            // AlarmListener��o�^
            registerAlarmListeners(config);

            // �L�^��������N���X������
            String strategyName = config.getRecordStrategy();
            try
            {   recordStrategy__ = (RecordStrategy)loadClass(strategyName).newInstance();
            }
            catch (ClassNotFoundException cfne)
            {
             // �w�肳�ꂽ�N���X��������΃f�t�H���g�̃N���X���g�p
                String defaultRecordstrategy = JavelinConfig.DEFAULT_RECORDSTRATEGY;
                SystemLogger.getInstance().info("Failed to load " + strategyName
                                                + ". Use default value "
                                                + defaultRecordstrategy
                                                + " as javelin.recordStrategy.");

                recordStrategy__ =
                    (RecordStrategy)loadClass(defaultRecordstrategy).newInstance();
            }

            // �X���b�h�̊Ď����J�n
            vmStatusHelper__.init();

            initialized__ = true;
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * AlarmListener�̃N���X��Javelin�ݒ肩��ǂݍ��݁A�o�^����B<br />
     * �N���X�̃��[�h�́A�ȉ��̏��ŃN���X���[�_�ł̃��[�h�����݂�B
     *
     * <ol>
     * <li>HadoopRecorder�����[�h�����N���X���[�_</li>
     * <li>�R���e�L�X�g�N���X���[�_</li>
     * </ol>
     *
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    private static void registerAlarmListeners(final JavelinConfig config)
    {
        String[] alarmListeners = config.getAlarmListeners().split(".");
        for (String alarmListenerName : alarmListeners)
        {
            try
            {
                if ("".equals(alarmListenerName)) continue;

                // alarmListenerName����C���X�^���X���擾
                Class<?> alarmListenerClass = loadClass(alarmListenerName);
                Object listener = alarmListenerClass.newInstance();
                if (listener instanceof AlarmListener)
                {
                    // �擾�����C���X�^���X��AlarmListener�C���^�[�t�F�C�X���������Ă�����o�^
                    AlarmListener alarmListener = (AlarmListener)listener;
                    alarmListener.init();
                    addListener(alarmListener);
                    String message = "Register " + alarmListenerName + "for AlarmListener.";
                    SystemLogger.getInstance().debug(message);
                }
                else
                {
                    String message = alarmListenerName +
                        " is not used for sending alarms because it doesn't implement AlarmListener.";
                    SystemLogger.getInstance().info(message);
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(
                    alarmListenerName +
                        " is not used for sending alarms because of failing to be registered.",
                    ex);
            }
        }
    }

    /**
     * �N���X�����[�h����B �ȉ��̏��ŃN���X���[�_�ł̃��[�h�����݂�B
     * <ol>
     * <li>HadoopRecorder�����[�h�����N���X���[�_</li>
     * <li>�R���e�L�X�g�N���X���[�_</li>
     * </ol>
     *
     * @param className ���[�h����N���X�̖��O�B
     * @return ���[�h�����N���X�B
     * @throws ClassNotFoundException �S�ẴN���X���[�_�ŃN���X��������Ȃ��ꍇ
     */
    private static Class<?> loadClass(final String className)
        throws ClassNotFoundException
    {
        Class<?> clazz;
        try
        {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException cnfe)
        {
            SystemLogger.getInstance().info(
                "Load classes from context class loader because of failing to load "
                + className + ".");
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        return clazz;
    }

    /**
     * Alarm�ʒm�ɗ��p����AlarmListener��o�^����
     *
     * @param alarmListener Alarm�ʒm�ɗ��p����AlarmListener
     */
    private static void addListener(final AlarmListener alarmListener)
    {
        synchronized (ALARM_LISTENER_LIST)
        {
            ALARM_LISTENER_LIST.add(alarmListener);
        }
    }

    /**
     * �O�����B
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param thisObject this�I�u�W�F�N�g
     */
    public static void preProcess(String className, String methodName, final Object[] args)
    {
        try
        {
            if (isSimplification__)
            {
                className = ConverterUtil.toSimpleName(className);
                methodName = ConverterUtil.toSimpleName(methodName);
            }

            preProcess(className, methodName, args, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * JavelinRecorder, JDBCJavelinRecorder����Ăяo�����Ƃ��̑O�����B
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     * @param thisObject this�I�u�W�F�N�g
     */
    private static void preProcess(final String className, final String methodName,
            final Object[] args, final boolean doExcludeProcess)
    {
        // �X�^�b�N�g���[�X
        StackTraceElement[] stacktrace = null;
        if (config__.isLogStacktrace())
        {
            stacktrace = ThreadUtil.getCurrentStackTrace();
        }
        preProcess(null, null, className, methodName, args, stacktrace, doExcludeProcess, false);
    }


    /**
     * �O�����B
     * @param component �R���|�[�l���g
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     * @param isResponse �f�t�H���g�Ń��X�|���X���L�^���邩�ǂ����B
     * @param thisObject this�I�u�W�F�N�g
     */
    private static void preProcess(Component component, Invocation invocation,
            final String className, final String methodName, final Object[] args,
            final StackTraceElement[] stacktrace, final boolean doExcludeProcess, final boolean isResponse)
    {
        synchronized (HadoopRecorder.class)
        {
            // ����������
            if (initialized__ == false)
            {
                javelinInit(config__);
            }
        }

        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();

        // Javelin�̃��O�o�͏������Ăяo����Ă���ꍇ�A�������s��Ȃ�
        if (callTreeRecorder.isRecordMethodCalled())
        {
            return;
        }

        // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X���Z�b�g
        callTreeRecorder.setRecordMethodCalled(true);

        try
        {
            boolean isRecorded = recordPreInvocation(component, invocation, className, methodName,
                                                     args, stacktrace, doExcludeProcess,
                                                     isResponse, callTreeRecorder);

            if (isRecorded)
            {
                // �L��������node�̐[����ۑ�����B
                callTreeRecorder.getCallTree().addDepth(callTreeRecorder.getDepth());
            }
        }
        finally
        {
            // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X������
            callTreeRecorder.setRecordMethodCalled(false);
            callTreeRecorder.setDepth(callTreeRecorder.getDepth() + 1);
        }
    }

    /**
     * �O�����B
     * @param component �R���|�[�l���g
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     * @param isResponse �f�t�H���g�Ń��X�|���X���L�^���邩�ǂ����B
     * @param callTreeRecorder �R�[���c���[
     * @param thisObject this�I�u�W�F�N�g
     *
     * @return �L�^�������ǂ���
     */
    private static boolean recordPreInvocation(Component component, Invocation invocation,
            final String className, final String methodName, final Object[] args,
            final StackTraceElement[] stacktrace, final boolean doExcludeProcess,
            final boolean isResponse, CallTreeRecorder callTreeRecorder)
    {

        // ������component��invocation�͕K��NULL
        component = MBeanManager.getComponent(className);
        invocation = getInvocation(component, methodName);

        boolean isExclude = ExcludeMonitor.isExclude(invocation);
        if (doExcludeProcess == true && isExclude == true)
        {
            return false;
        }

        // �p�����[�^�̉�͌�ACallTreeNode��o�^���ďI��
        if (methodName.equals("heartbeat"))
        {
            return recordPreHeartbeat(callTreeRecorder, component,
                                      invocation, className, methodName,
                                      args, stacktrace, isResponse);
        }
        else if (methodName.equals("submitJob"))
        {
            return recordPreSubmitJob(callTreeRecorder, component,
                                      invocation, className, methodName,
                                      args, stacktrace, isResponse);
        }
        // ��L�̃��\�b�h�ȊO�̓G���[
        return false;
    }

    /**
     * �R�[���c���[������������B
     *
     * @param callTree callTree
     * @param methodName ���\�b�h��
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     */
    private static void initCallTree(CallTree callTree,
                                     final String methodName,
                                     CallTreeRecorder callTreeRecorder)
    {
        // ����Ăяo�����̓R�[���c���[������������B
        callTree.clearDepth();

        callTree.setRootCallerName(config__.getRootCallerName());
        callTree.setEndCalleeName(config__.getEndCalleeName());

        String threadId = createThreadId(methodName, callTreeRecorder);
        if (threadId != null)
        {
            callTree.setThreadID(threadId);
        }
    }

    /**
     * �X���b�hID�𐶐�����B
     *
     * @param methodName ���\�b�h���B
     *
     * @return �X���b�hID�B
     */
    private static String createThreadId(final String methodName,
                                         final CallTreeRecorder callTreeRecorder)
    {
        String threadId = null;
        switch (config__.getThreadModel())
        {
        case JavelinConfig.TM_THREAD_ID:
            threadId = "" + callTreeRecorder.getThreadId();
            break;
        case JavelinConfig.TM_THREAD_NAME:
            threadId = Thread.currentThread().getName();
            break;
        case JavelinConfig.TM_CONTEXT_PATH:
            threadId = methodName;
            break;
        default:
            break;
        }
        return threadId;
    }

    /**
     * Invocation���擾����B
     *
     * @param component �N���X��
     * @param methodName ���\�b�h��
     *
     * @return {@link Invocation}�I�u�W�F�N�g
     */
    private static Invocation getInvocation(final Component component,
                                           final String methodName)
    {
        if (component == null)
        {
            return null;
        }

        Invocation invocation = component.getInvocation(methodName);
        return invocation;
    }

    /**
     * Invocation��o�^����B
     *
     * @param component �R���|�[�l���g
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param isResponse ���X�|���X
     *
     * @return �o�^����{@link Invocation}�I�u�W�F�N�g
     */
    private static Invocation registerInvocation(Component component,
                                                final String className,
                                                final String methodName,
                                                boolean isResponse)
    {
        if (component == null)
        {
            try
            {
                // �R���|�[�l���g���N���X�����琶��
                component = new Component(className);

                MBeanManager.setComponent(className, component);
            }
            catch (NullPointerException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        Invocation invocation = component.getInvocation(methodName);

        if (invocation == null)
        {
            invocation = registerInvocation(component, methodName, isResponse);
        }
        return invocation;
    }


    /**
     * {@link Invocation}�I�u�W�F�N�g���R���|�[�l���g�ɓo�^���܂��B
     *
     * @param component �o�^�ΏۃR���|�[�l���g
     * @param methodName �o�^���郁�\�b�h
     * @param isResponse ���X�|���X�O���t�ɕ\������ꍇ�� <code>true</code>
     *
     * @return �o�^���� Invocation
     */
    private static Invocation registerInvocation(final Component component,
                                                final String methodName,
                                                final boolean isResponse)
    {
        String className = component.getClassName();
        Invocation invocation;
        int recordedInvocationNum = component.getRecordedInvocationNum();

        String processName = VMStatusHelper.getProcessName();
        invocation =
                     new Invocation(processName, className, methodName,
                                    Invocation.THRESHOLD_NOT_SPECIFIED);

        // Invocation�̐����ő�l�ɒB���Ă���A����InvocationFullEvent�𑗐M����ݒ�̏ꍇ�A
        // InvocationFullEvent���M�̏������s���B
        if (config__.getSendInvocationFullEvent() == true
                && recordedInvocationNum >= config__.getRecordInvocationMax())
        {
            Invocation removedInvoction = component.addAndDeleteOldestInvocation(invocation);
            sendInvocationFullEvent(component, className, recordedInvocationNum, invocation,
                                    removedInvoction);
        }
        else
        {
            component.addInvocation(invocation);
        }

        if (isResponse)
        {
            invocation.setResponseGraphOutput(TripleState.ON);
            RootInvocationManager.addRootInvocation(invocation);
        }
        return invocation;
    }

    /**
     * VM��Ԃ��擾����B
     *
     * @param parent �Ăяo����
     * @param newNode �Ăяo����
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     *
     * @return
     */
    private static VMStatus createVMStatus(CallTreeNode parent,
                                           CallTreeNode newNode,
                                           CallTreeRecorder callTreeRecorder)
    {
        VMStatus vmStatus;

        if (parent == null && config__.isLogMBeanInfoRoot())
        {
            vmStatus = vmStatusHelper__.createVMStatus(callTreeRecorder);
        }
        else if (config__.isLogMBeanInfo())
        {
            vmStatus = vmStatusHelper__.createVMStatus(callTreeRecorder);
        }
        else
        {
            vmStatus = VMStatus.EMPTY_STATUS;
        }

        return vmStatus;
    }

    /**
     * InvocationFullEvent�𑗐M����B
     *
     * @param component �R���|�[�l���g�B
     * @param className �N���X��
     * @param invocationNum Invocation�̐�
     */
    private static void sendInvocationFullEvent(Component component, String className,
            int invocationNum, Invocation addInvocation, Invocation removedInvocation)
    {
        CommonEvent event = new InvocationFullEvent();
        event.addParam(EventConstants.PARAM_INVOCATION, String.valueOf(invocationNum));
        event.addParam(EventConstants.PARAM_INVOCATION_CLASS, className);
        boolean containsEvent = eventRepository__.containsEvent(event);
        if (containsEvent == false)
        {
            if (addInvocation != null)
            {
                event.addParam(EventConstants.PARAM_INVOCATION_METHOD_ADD,
                               addInvocation.getMethodName());
            }
            else
            {
                event.addParam(EventConstants.PARAM_INVOCATION_METHOD_ADD, "");
            }

            if (removedInvocation != null)
            {
                event.addParam(EventConstants.PARAM_INVOCATION_METHOD_REMOVE,
                               removedInvocation.getMethodName());
            }
            else
            {
                event.addParam(EventConstants.PARAM_INVOCATION_METHOD_REMOVE, "");
            }

            String stackTrace = ThreadUtil.getStackTrace(ThreadUtil.getCurrentStackTrace());
            event.addParam(EventConstants.PARAM_INVOCATION_STACKTRACE, stackTrace);
            addEvent(event);
        }
    }

    /**
     * �g�����U�N�V�������L�^����B
     *
     * @param node CallTreeNode
     */
    private static void recordTransaction(final CallTreeNode node)
    {
        if (node.isRecoreded() == false)
        {
            node.setRecoreded(true);

            Invocation invocation = node.getInvocation();
            if (invocation != null)
            {
                long elapsedTime     = node.getAccumulatedTime();
                long elapsedCpuTime  = node.getCpuTime();
                long elapsedUserTime = node.getUserTime();

                elapsedTime     = elapsedTime     - node.getChildrenTime();
                if (elapsedTime < 0)
                {
                    elapsedTime = 0;
                }
                elapsedCpuTime  = elapsedCpuTime  - node.getChildrenCpuTime();
                if (elapsedCpuTime < 0)
                {
                    elapsedCpuTime = 0;
                }
                elapsedUserTime = elapsedUserTime - node.getChildrenUserTime();
                if (elapsedUserTime < 0)
                {
                    elapsedUserTime = 0;
                }

                invocation.addInterval(node, elapsedTime, elapsedCpuTime, elapsedUserTime);

                if (node.getParent() != null)
                {
                    invocation.addCaller(node.getParent().getInvocation());
                }
            }

            List<CallTreeNode> children = node.getChildren();
            int size = children.size();
            for (int index = 0; index < size; index++)
            {
                CallTreeNode child = children.get(index);
                recordTransaction(child);
            }
        }
    }

    /**
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂷂�B
     *
     * @param event �C�x���g�B
     *
     * @return �ǉ�����CallTreeNode�B
     */
    private static CallTreeNode addEvent(CommonEvent event)
    {
        return addEvent(event, false, null, 0);
    }

    /**
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂷂�B
     *
     * @param event �C�x���g�B
     * @param clear ���ɃC�x���g������ꍇ�����񂷂�B
     * @param config �ݒ�B
     * @param telegramId �d�� ID
     *
     * @return �ǉ�����Node�B
     */
    private static CallTreeNode addEvent(CommonEvent event, boolean clear, JavelinConfig config,
            long telegramId)
    {
        JavelinEventCounter.getInstance().addEvent(event);

        boolean containsEvent = eventRepository__.containsEvent(event);
        if (containsEvent && clear == false)
        {
            return null;
        }

        if (config == null)
        {
            config = new JavelinConfig();
        }

        // �C�x���g�̏o�͐ݒ背�x�����A�����Ŏw�肵���C�x���g�̃��x�������傫���ꍇ�́A
        // �C�x���g���o�͂��Ȃ��B
        int outputEventLevel = convertEventLevel(config.getEventLevel());
        if (outputEventLevel > event.getLevel())
        {
            return null;
        }

        eventRepository__.putEvent(event);

        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();
        CallTreeNode callTreeNode = callTreeRecorder.getCallTreeNode();
        CallTree tree = callTreeRecorder.getCallTree();

        // �C�x���g�̃��x�����G���[�̏ꍇ�A�����ɃA���[�����グ��B
        if(event.getLevel() >= CommonEvent.LEVEL_ERROR)
        {
            Invocation invocation = null;
            if( callTreeNode != null)
            {
                invocation = callTreeNode.getInvocation();
            }
            sendEventImmediately(event, invocation, callTreeRecorder, telegramId);
            if (tree != null)
            {
                tree.addHighPriorityRecordStrategy("AllRecordStrategy", new AllRecordStrategy());
            }

            return null;
        }

        boolean isNewCallTree = false;
        if (tree == null)
        {
            callTreeRecorder.clearCallTree();
        }
        if (callTreeNode == null)
        {
            isNewCallTree = true;

            CallTreeNode node = createEventNode(event, callTreeRecorder, tree, null);

            tree.addEventNode(node);

            tree.clearDepth();
            tree.addDepth(0);
            callTreeRecorder.setDepth(1);
        }

        CallTree callTree = callTreeRecorder.getCallTree();
        CallTreeNode rootNode = callTree.getRootNode();

        CallTreeNode node = null;
        if (rootNode != null)
        {
            tree.addHighPriorityRecordStrategy("AllRecordStrategy", new AllRecordStrategy());

            node = callTreeNode;
            event.setTime(System.currentTimeMillis());
            node.addEvent(event);
        }
        if (isNewCallTree)
        {
            postProcessCommon(null, null, null, null, telegramId);
        }

        return node;
    }

    /**
     * �C�x���g���x���𕶎��񂩂琔�l�ɕϊ����܂��B<br />
     *
     * @param eventLevelStr �C�x���g���x��(������)
     * @return �C�x���g���x��(���l)
     */
    private static int convertEventLevel(final String eventLevelStr)
    {
        if ("ERROR".equals(eventLevelStr))
        {
            return CommonEvent.LEVEL_ERROR;
        }
        if ("WARN".equals(eventLevelStr))
        {
            return CommonEvent.LEVEL_WARN;
        }
        if ("INFO".equals(eventLevelStr))
        {
            return CommonEvent.LEVEL_INFO;
        }
        return CommonEvent.LEVEL_WARN;
    }

    /**
     * �����ɃC�x���g�𑗐M����B
     *
     * @param event �C�x���g
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     * @param telegramId �d��ID
     */
    private static void sendEventImmediately(CommonEvent event,
                                             Invocation invocation,
                                             CallTreeRecorder callTreeRecorder,
                                             long telegramId)
    {
        CallTree callTree = new CallTree();
        callTree.init();
        CallTreeNode node = createEventNode(event, callTreeRecorder, callTree, invocation);
        callTree.addEventNode(node);
        recordAndAlarmEvents(callTree, null, telegramId);
    }

    /**
     * �C�x���g�p�̃m�[�h���쐬����B
     *
     * @param event �C�x���g
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     * @param tree �c���[
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     *
     * @return �C�x���g�p�̃m�[�h�B
     */
    private static CallTreeNode createEventNode(CommonEvent event,
                                                CallTreeRecorder callTreeRecorder,
                                                CallTree tree,
                                                Invocation invocation)
    {
        if (invocation == null)
        {
            String className = config__.getRootCallerName();
            String methodName = "";

            // CallTree�ɃX���b�h����ݒ肷��B
            String threadId = createThreadId(methodName, callTreeRecorder);
            if (threadId != null)
            {
                tree.setThreadID(threadId);
            }

            String processName = VMStatusHelper.getProcessName();
            invocation = new Invocation(processName, className, methodName, 0);
        }

        event.setTime(System.currentTimeMillis());

        CallTreeNode node = new CallTreeNode();
        node.setInvocation(invocation);
        node.addEvent(event);
        return node;
    }

    /**
     * �C�x���g���������Ă���ꍇ�ɁA Javelin ���O�ւ̏o�͂ƃA���[���ʒm���s���B
     *
     * @param callTree �R�[���c���[
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     * @param telegramId �d��ID
     */
    private static void recordAndAlarmEvents(CallTree callTree,
                                             CallTreeRecorder callTreeRecorder,
                                             long telegramId)
    {
        // CallTree�ɑ΂���EventNode�����݂��Ȃ��󋵂�Event�����������ꍇ�̂݁A
        //  getEventNodeList�ɗv�f���ǉ�����Ă���B
        List<CallTreeNode> eventList = callTree.getEventNodeList();
        int size = eventList.size();
        if (size != 0)
        {
            // �C�x���g���������݂���ꍇ�A���O�o�͏����� �A���[���ʒm�������s���B
            for (int num = 0; num < size; num++)
            {
                // Javelin�̏��������ς�ł��Ȃ��ꍇ�A���O�t�@�C���쐬�ƃA���[���ʒm�̏������΂��B
                if (generator__ != null)
                {
                    generator__.generateJaveinFile(callTree, eventList.get(num),
                                                   new JvnFileNotifyCallback(), null,
                                                   telegramId);
                    sendEventAlarm();
                }

                // CallTree�ɕێ�����Ă���Node�����L�^����
                int totalNodeCount = callTree.getTotalNodeCount();
                CallTreeNodeMonitor.add(totalNodeCount);

            }

            if (callTreeRecorder != null)
            {
                callTreeRecorder.clearCallTree();
            }
        }
    }

    /**
     * Alarm�ʒm����B
     */
    private static void sendEventAlarm()
    {
        CallTreeNode eventNode = new CallTreeNode();
        String processName = VMStatusHelper.getProcessName();
        Invocation invocation =
                                new Invocation(processName, EventConstants.EVENT_CLASSNAME,
                                               EventConstants.EVENT_METHODNAME, 0);
        eventNode.setInvocation(invocation);
        sendAlarmImpl(eventNode);
    }

    /**
     * Alarm�ʒm����B
     * @param node CallTreeNode
     * @param telegramId �d�� ID
     */
    private static void sendAlarmImpl(final CallTreeNode node)
    {
        synchronized (ALARM_LISTENER_LIST)
        {
            for (AlarmListener alarmListener : ALARM_LISTENER_LIST)
            {
                // ���[�g�m�[�h�̂�Alarm�𑗐M����AlarmListener�́A
                // �e�����m�[�h�𖳎�����B
                boolean sendingRootOnly = alarmListener.isSendingRootOnly();
                if (sendingRootOnly == true && node.getParent() != null)
                {
                    continue;
                }

                try
                {
                    // AlarmListener�ɂ�CallTreeNode�����̂܂ܓn��
                    // ���A���[���ʒm�ŗݐώ��Ԃ��g�p������̂������
                    alarmListener.sendExceedThresholdAlarm(node);
                }
                catch (Throwable ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }
    }

    /**
     * �㏈���i�{�����������j
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param retValue �߂�l�B
     */
    public static void postProcessOK(String className, String methodName, final Object retValue, Object thisObject)
    {
        try
        {
            if (isSimplification__)
            {
                methodName = ConverterUtil.toSimpleName(methodName);
            }

            postProcessCommon(thisObject, methodName, retValue, null, 0);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���i�{�������s���j�B
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param cause ��O�̌���
     */
    public static void postProcessNG(String className,
                                     String methodName,
                                     final Throwable cause,
                                     Object thisObject)
    {
        try
        {
            if (isSimplification__)
            {
                methodName = ConverterUtil.toSimpleName(methodName);
            }
            HadoopRecorder.postProcessCommon(thisObject, methodName, null, cause, 0);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���̋��ʏ���<br />
     * <br />
     * CallTree �ɏ����i�[���܂��B
     * �܂��A�K�v�ɉ����� Javelin ���O�o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param methodName ���\�b�h��
     * @param returnValue �߂�l�i <code>null</code> ���j
     * @param cause ��O�����I�u�W�F�N�g�i <code>null</code> ���j
     * @param telegramId �d�� ID
     */
    private static void postProcessCommon(final Object thisObject,
                                          final String methodName,
                                          final Object returnValue,
                                          final Throwable cause,
                                          final long telegramId)
    {
        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();

        // Javelin�̃��O�o�͏������Ăяo����Ă���ꍇ�A�������s��Ȃ�
        if (callTreeRecorder.isRecordMethodCalled())
        {
            return;
        }

        // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X���Z�b�g
        callTreeRecorder.setRecordMethodCalled(true);

        try
        {
            Integer depth = callTreeRecorder.getDepth() - 1;
            callTreeRecorder.setDepth(depth);
            CallTree callTree = callTreeRecorder.getCallTree();

            // �v���ΏۊO�̐[�x�ł���Ή������Ȃ�
            if (callTree.containsDepth(depth) == false) return;

            callTree.removeDepth(depth);

            recordPostInvocation(thisObject, returnValue, methodName,
                                 cause, callTreeRecorder, telegramId);
        }
        finally
        {
            // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X������
            callTreeRecorder.setRecordMethodCalled(false);
        }
    }

    /**
     * �㏈���̋��ʏ���<br />
     * <br />
     * CallTree �ɏ����i�[���܂��B<br />
     * �܂��A�K�v�ɉ����� Javelin ���O�o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param returnValue �߂�l�i <code>null</code> ���j
     * @param cause ��O�����I�u�W�F�N�g�i <code>null</code> ���j
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param callTreeRecorder �R�[���c���[���R�[�_
     * @param telegramId �d�� ID
     */
    private static boolean recordPostInvocation(final Object thisObject,
                                                final Object returnValue,
                                                final String methodName,
                                                final Throwable cause,
                                                CallTreeRecorder callTreeRecorder,
                                                long telegramId)
    {
        boolean ret = false;
        // �p�����[�^�̉�͌�ACallTreeNode��o�^���ďI��
        if (methodName.equals("heartbeat"))
        {
            ret = recordPostHeartbeat(thisObject, returnValue, methodName,
                                       cause, callTreeRecorder, telegramId);
        }
        else if (methodName.equals("submitJob"))
        {
            ret = recordPostSubmitJob(thisObject, returnValue, methodName,
                                       cause, callTreeRecorder, telegramId);
        }
        // ��L�̃��\�b�h�ȊO�̓G���[

        // �Ăь����c���Ă��܂��ꍇ������̂ŃN���A����B
        CallTreeRecorder.getInstance().clearCallerNode();

        return ret;
    }

    /**
     * �m�[�h����VM��Ԃ�ݒ肷��B
     *
     * @param node �m�[�h
     * @param parent �Ăяo����
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     */
    private static void addEndVMStatus(CallTreeNode node,
                                       CallTreeNode parent,
                                       CallTreeRecorder callTreeRecorder)
    {
        long duration = node.getEndTime() - node.getStartTime();
        if (duration == 0)
        {
            node.setEndVmStatus(node.getStartVmStatus());
            return;
        }

        VMStatus vmStatus = createVMStatus(parent, node, callTreeRecorder);
        node.setEndVmStatus(vmStatus);

        setCpuTime(node);
        setUserTime(node);
    }

    /**
     * ���[�g�m�[�h�̏ꍇ�̌㏈�����s���B
     *
     * @param callTree �R�[���c���[
     * @param node �m�[�h
     * @param callTreeRecorder {@link CallTreeRecorder}�I�u�W�F�N�g
     */
    private static void postProcessOnRootNode(CallTree callTree, CallTreeNode node,
            CallTreeRecorder callTreeRecorder)
    {
        // Strategy�C���^�t�F�[�X�𗘗p���������̌㏈�����s��
        postJudge(callTree, node, recordStrategy__);

        callTree.executeCallback();
        callTreeRecorder.clearCallerNode();

        // CallTree�ɕێ�����Ă���Node�����L�^����
        int totalNodeCount = callTree.getTotalNodeCount();
        CallTreeNodeMonitor.add(totalNodeCount);
        callTreeRecorder.clearCallTree();
    }

    /**
     * �m�[�h���s��CPU���Ԃ�ݒ肷��B
     *
     * @param node �m�[�h
     */
    private static void setCpuTime(CallTreeNode node)
    {
        long endCpuTime = node.getEndVmStatus().getCpuTime();
        long startCpuTime = node.getStartVmStatus().getCpuTime();
        long cpuTime = endCpuTime - startCpuTime;
        if (cpuTime < 0)
        {
            cpuTime = 0;
        }
        node.setCpuTime(cpuTime);
    }

    /**
     * �m�[�h���s�̃��[�U���Ԃ�ݒ肷��B
     *
     * @param node �m�[�h
     */
    private static void setUserTime(CallTreeNode node)
    {
        long endUserTime = node.getEndVmStatus().getUserTime();
        long startUserTime = node.getStartVmStatus().getUserTime();
        long userTime = endUserTime - startUserTime;
        if (userTime < 0)
        {
            userTime = 0;
        }
        node.setUserTime(userTime);
    }

    /**
     * Config��CallTree�ɐݒ肳�ꂽ����N���X�ɑ΂��āA
     * �����Ɍ㏈�����s���B
     *
     * @param callTree �R�[���c���[
     * @param node �m�[�h
     * @param strategy Config�ɐݒ肳�ꂽ����N���X�B
     */
    private static void postJudge(final CallTree callTree, final CallTreeNode node,
                                 final RecordStrategy strategy)
    {
        strategy.postJudge();

        RecordStrategy[] highStrategyList = callTree.getHighPriorityRecordStrategy();
        for (RecordStrategy str : highStrategyList)
        {
            str.postJudge();
        }

        RecordStrategy[] strategyList = callTree.getRecordStrategy();
        for (RecordStrategy str : strategyList)
        {
            str.postJudge();
        }
    }

    /**
     * �K�v�ɉ����āA Javelin ���O�ւ̏o�́A�A���[���ʒm�������s���܂��B<br />
     *
     * @param callTree CallTree
     * @param node CallTreeNode
     * @param callTreeRecorder CallTreeRecorder
     * @param telegramId �d�� ID
     */
    private static void recordAndAlarmProcedure(CallTree callTree,
                                                CallTreeNode node,
                                                CallTreeRecorder callTreeRecorder,
                                                final long telegramId)
    {
        // �I�������W���u���o�͂���Ƃ������Ă΂����̂Ƃ���B
        generator__.generateJaveinFile(callTree, createCallback(
                callTree, node), node, telegramId);
        sendAlarm(node, callTreeRecorder);

    }

    /**
     * CallTreeNode�ɐݒ肳�ꂽ����N���X(����D��x�F��)�𗘗p���āA<br />
     * Javelin���O���t�@�C���ɏo�͂��邩�ǂ������肷��B
     *
     * @param node �m�[�h
     *
     * @return true:�o�͂���Afalse:�o�͂��Ȃ�
     */
    private static JavelinLogCallback createCallback(final CallTree tree,
                                                     final CallTreeNode node)
    {
        // CallTreeNode�ɐݒ肳��Ă�������N���X�ł̔��茋�ʂ�
        // 1�ł�true�ł���΁A�����߂�l�Ƃ���
        RecordStrategy[] strategyList = tree.getHighPriorityRecordStrategy();
        for (RecordStrategy str : strategyList)
        {
            JavelinLogCallback callback = str.createCallback();
            if (callback != null)
            {
                return callback;
            }
        }
        // ���肪���ׂ�false�̏ꍇ
        return recordStrategy__.createCallback();
    }

    /**
     * Alarm�ʒm����B
     *
     * @param node �m�[�h
     * @param callTreeRecorder {@link allTreeRecorder}�I�u�W�F�N�g
     */
    private static void sendAlarm(final CallTreeNode node,
                                 final CallTreeRecorder callTreeRecorder)
    {
        synchronized (ALARM_LISTENER_LIST)
        {
            for (AlarmListener alarmListener : ALARM_LISTENER_LIST)
            {
                // ���[�g�m�[�h�̂�Alarm�𑗐M����AlarmListener�́A
                // �e�����m�[�h�𖳎�����B
                boolean sendingRootOnly = alarmListener.isSendingRootOnly();
                if (sendingRootOnly == true && node.getParent() != null)
                {
                    continue;
                }

                try
                {
                    // AlarmListener�ɂ�CallTreeNode�����̂܂ܓn��
                    // ���A���[���ʒm�ŗݐώ��Ԃ��g�p������̂������
                    alarmListener.sendExceedThresholdAlarm(node);
                }
                catch (Throwable ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }
        CallTree tree = callTreeRecorder.getCallTree();
        List<CallTreeNode> eventList = tree.getEventNodeList();
        if (eventList.size() > 0)
        {
            sendEventAlarm();
        }
    }

    /**
     * Javelin���O�t�@�C�����o�͂���B
     *
     * @return Javelin���O�t�@�C��
     */
    public static String dumpJavelinLog()
    {
        String fileName = "";
        // Javelin���O�t�@�C�����o�͂���B
        JavelinFileGenerator generator = new JavelinFileGenerator(config__);

        CallTree callTree = CallTreeRecorder.getInstance().getCallTree();
        if (callTree == null)
        {
            return fileName;
        }

        CallTreeNode root = callTree.getRootNode();
        if (root != null)
        {
            fileName = generator.generateJaveinFile(callTree,
                                                    recordStrategy__.createCallback(),
                                                    null,
                                                    0);
        }
        return fileName;
    }

    /**
     * Heartbeat()�̑O�������s���B
     * 
     * @param callTreeRecorder
     * @param component
     * @param invocation
     * @param className
     * @param methodName
     * @param args
     * @param stacktrace
     * @param isResponse
     * 
     * @return {@code true}�F�����^{@code false}�F���s
     */
    private static boolean recordPreHeartbeat(CallTreeRecorder callTreeRecorder,
        Component component, Invocation invocation, final String className,
        final String methodName, final Object[] args,
        final StackTraceElement[] stacktrace, final boolean isResponse)
    {
        String hostName;
        ArrayList<HadoopTaskStatus> taskStatusList;
        try
        {
            // TaskTrackerStatus����z�X�g����TaskStatus���擾
            hostName = HadoopObjectAnalyzer.hostNamefromTaskTrackerStatus(args[0]);
            taskStatusList = HadoopObjectAnalyzer.transTaskStatus(args[0]);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }

        // TaskTrackerStatus��JobID���ɂ܂Ƃ߂�
        HashMap<String, ArrayList<HadoopTaskStatus>> arrangedMap = new HashMap<String, ArrayList<HadoopTaskStatus>>();
        for (HadoopTaskStatus status : taskStatusList)
        {
            String jobID = status.getJobID();
            ArrayList<HadoopTaskStatus> temp;
            if (arrangedMap.containsKey(jobID))
            {
                temp = arrangedMap.get(jobID);
            }
            else
            {
                temp = new ArrayList<HadoopTaskStatus>(1);
            }
            temp.add(status);
            arrangedMap.put(jobID, temp);
        }

        // ���[�g�Ăяo�����ɁA��O�����t���O���N���A����
        callTreeRecorder.setExceptionOccured(false);
        if (invocation == null)
            invocation = registerInvocation(component, className, methodName, isResponse);

        // ��x�ł����[�g����Ă΂ꂽ���Ƃ̂��郁�\�b�h��ۑ�����B
        ExcludeMonitor.addTargetPreferred(invocation);
        ExcludeMonitor.removeExcludePreferred(invocation);

        // HadoopCallTreeRecorder��Invocation�ƃz�X�g����ޔ�
        HadoopCallTreeRecorder recorder = HadoopCallTreeRecorder.getInstance();
        recorder.setInvocation(invocation);
        recorder.putHostName(hostName);
        recorder.putStartTime(System.currentTimeMillis());

        // JobID����callTree���쐬����B
        Set<String>jobIDSet = arrangedMap.keySet();
        for (String jobID : jobIDSet)
        {

            // �ŏ��̌Ăяo���Ȃ̂ŁACallTree�����������Ă����B
            // TODO CallTree������ɍ���Ă����v�H
            CallTree callTree = new CallTree();
            initCallTree(callTree, methodName, callTreeRecorder);
            CallTreeNode newNode = CallTreeRecorder.createNode(invocation, args, stacktrace, config__);
            newNode.setDepth(0);
            newNode.setTree(callTree);
            callTree.setRootNode(newNode);
            callTreeRecorder.setDepth(0);

            try
            {
                VMStatus vmStatus = createVMStatus(null, newNode, callTreeRecorder);
                newNode.setStartTime(System.currentTimeMillis());
                newNode.setStartVmStatus(vmStatus);

                // CallTreeNode��TaskStatus��ݒ�
                HadoopInfo hadoopInfo = new HadoopInfo();
                hadoopInfo.setHost(hostName);
                hadoopInfo.setTaskStatuses(arrangedMap.get(jobID));
                newNode.setHadoopInfo(hadoopInfo);

                // HadoopCallTreeRecorer��CallTree���L�^
                recorder.putCallTree(jobID, callTree);
                recorder.putCallTreeNode(jobID, newNode);
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
                return false;
            }
        }

        return true;
    }

    /**
     * Heartbeat()�̌㏈�����s���B
     * 
     * @param thisObject
     * @param returnValue
     * @param methodName
     * @param cause
     * @param callTreeRecorder
     * @param telegramId
     * 
     * @return {@code true}�F�����^{@code false}�F���s
     */
    private static boolean recordPostHeartbeat(final Object thisObject,
                                               final Object returnValue,
                                               final String methodName,
                                               final Throwable cause,
                                               CallTreeRecorder callTreeRecorder,
                                               long telegramId)
    {
        // �p�����[�^���
        ArrayList<HadoopAction> ttActionList;
        try
        {
            ttActionList
                = HadoopObjectAnalyzer.taskTrackerActionListFromHearbeatResponse(returnValue);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }
        
        // HadoopAction��JobID���ɂ܂Ƃ߂�
        HashMap<String, ArrayList<HadoopAction>> arrangedMap = new HashMap<String, ArrayList<HadoopAction>>();
        for (HadoopAction action : ttActionList)
        {
            String jobID = action.getJobID();
            ArrayList<HadoopAction> temp;
            if (arrangedMap.containsKey(jobID))
            {
                temp = arrangedMap.get(jobID);
            }
            else
            {
                temp = new ArrayList<HadoopAction>(1);
            }
            temp.add(action);
            arrangedMap.put(jobID, temp);
        }

        HadoopCallTreeRecorder recorder = HadoopCallTreeRecorder.getInstance();
        Invocation invocation = recorder.getInvocation();
        String hostName = recorder.takeHostname();
        long startTime = recorder.takeStartTime();

        // TaskTrackerStatus��JobID����callTree��o�^����B
        HashMap<String, CallTree> callTreeMap = recorder.takeAllCallTree();
        HashMap<String, CallTreeNode> callTreeNodeMap = recorder.takeAllCallTreeNode();

        // CallTree��CallTreeNode�̂ǂ��炩�Е����������擾�ł��Ȃ��ꍇ�̓G���[
        if (callTreeMap == null ^ callTreeNodeMap == null)
            return false;

        // ���CallTree
        if (callTreeMap != null)
        {
            // CallTree����CallTreeNode�����قȂ�ꍇ�̓G���[
            if (callTreeMap.size() != callTreeNodeMap.size())
                return false;

            // ��O��ۑ��������ǂ����̃t���O
            boolean saveException = false;

            Set<String> jobIDSet = callTreeMap.keySet();
            for (String jobID : jobIDSet)
            {
                CallTree callTree = callTreeMap.get(jobID);
                CallTreeNode node = callTreeNodeMap.get(jobID);

                // CallTreeNode���擾�ł��Ȃ��ꍇ�͏����𒆒f����B
                if (node == null)
                    return false;

                try
                {
                    // �A���[���ʒm�����A�C�x���g�o�͏������s���B
                    recordAndAlarmEvents(callTree, callTreeRecorder, telegramId);

                    // �Ăяo������񂪎擾�ł��Ȃ��ꍇ�͏������L�����Z������B
                    // (���ʃ��C���ŗ�O�����������ꍇ�̂��߁B)
                    if (node == null)
                        continue;

                    node.setEndTime(System.currentTimeMillis());

                    addEndVMStatus(node, null, callTreeRecorder);

                    if (cause != null && callTree.getCause() != cause)
                    {
                        callTreeRecorder.setExceptionOccured(true);

                        if (!saveException)
                        {
                            invocation.addThrowable(cause);
                            saveException = true;
                        }

                        callTree.setCause(cause);

                        if (config__.isAlarmException())
                        {
                            // ����������O���L�^���Ă���
                            node.setThrowable(cause);
                            node.setThrowTime(System.currentTimeMillis());
                        }
                    }

                    // JobID�ɑΉ�����TaskTrackerAction�������CallTreeNode�ɐݒ�
                    if (arrangedMap.containsKey(jobID))
                    {
                        node.getHadoopInfo().setTaskTrackerActions(arrangedMap.get(jobID));
                        // �㏈���̂��߂�Map����폜
                        arrangedMap.remove(jobID);
                    }

                    recorder.addCallTree(jobID, callTree);

                    if (invocation.getAlarmThreshold()    != Invocation.THRESHOLD_NOT_SPECIFIED ||
                        invocation.getAlarmCpuThreshold() != Invocation.THRESHOLD_NOT_SPECIFIED)
                    {
                        // �ȉ��ACallTreeNode��root�̏ꍇ�A�܂���臒l���ʂɎw�肳��Ă���ꍇ�̏����B
                        // CallTreeNode��root�ŁA���v�l�L�^��臒l�𒴂��Ă����ꍇ�ɁA�g�����U�N�V�������L�^����B
                        if (node.getAccumulatedTime() >= config__.getStatisticsThreshold())
                            recordTransaction(node);

                        // ���[�g�m�[�h�̏ꍇ�̏���
                        postProcessOnRootNode(callTree, node, callTreeRecorder);
                    }
                }
                catch (Throwable ex)
                {
                    SystemLogger.getInstance().warn(ex);
                    return false;
                }
            }
        }

        // TaskTrackerStatus�̃W���uID�ɑΉ�����TaskTrackerAction��
        // ������Ȃ������ꍇ�̌㏈��
        Set<String> jobIDSet = arrangedMap.keySet();
        for (String jobID : jobIDSet)
        {
            // �V�K�쐬����CallTreeNode��TaskTrackerAction��ݒ肵�Ď���
            CallTree callTree = new CallTree();
            initCallTree(callTree, methodName, callTreeRecorder);
            CallTreeNode newNode = CallTreeRecorder.createNode(invocation, null, null, config__);
            newNode.setDepth(0);
            newNode.setTree(callTree);
            callTree.setRootNode(newNode);
            callTreeRecorder.setDepth(0);

            // �J�n����VM��Ԃ��L�^
            VMStatus vmStatus = createVMStatus(null, newNode, callTreeRecorder);
            newNode.setStartTime(startTime);
            newNode.setStartVmStatus(vmStatus);

            HadoopInfo hadoopInfo = new HadoopInfo();
            hadoopInfo.setHost(hostName);
            hadoopInfo.setTaskTrackerActions(arrangedMap.get(jobID));
            newNode.setHadoopInfo(hadoopInfo);

            // �I������VM��Ԃ��L�^����callTreeNode��ۑ�
            newNode.setEndTime(System.currentTimeMillis());
            addEndVMStatus(newNode, null, callTreeRecorder);
            recorder.addCallTree(jobID, callTree);
        }

        // �I�������W���u�̊m�F
        ArrayList<String> succeededList = new ArrayList<String>(0);
        ArrayList<String> killedList = new ArrayList<String>(0);
        synchronized(runningJobList__)
        {
            Iterator<String> itr = runningJobList__.iterator();
            while (itr.hasNext())
            {
                try
                {
                    String jobID = itr.next();
                    HadoopJobStatus status = HadoopObjectAnalyzer.checkJobStatus(jobID, thisObject);
                    if (status == HadoopJobStatus.SUCCEEDED)
                    {
                        succeededList.add(jobID);
                        itr.remove();
                    }
                    else if (status == HadoopJobStatus.KILLED)
                    {
                        killedList.add(jobID);
                        itr.remove();
                    }
                }
                catch (Exception e)
                {
                    SystemLogger.getInstance().warn(e);
                }
            }
        }

        // �I�������W���u�����O�ɏ����o��
        makeCallHistory(invocation, hostName, telegramId, succeededList, killedList);

        return true;
    }

    /**
     * SubmitJob()�̑O�������s���B
     * 
     * @param callTreeRecorder
     * @param component
     * @param invocation
     * @param className
     * @param methodName
     * @param args
     * @param stacktrace
     * @param isResponse
     * 
     * @return {@code true}�F�����^{@code false}�F���s
     */
    private static boolean recordPreSubmitJob(CallTreeRecorder callTreeRecorder,
        Component component, Invocation invocation, final String className,
        final String methodName, final Object[] args, final StackTraceElement[] stacktrace,
        final boolean isResponse)
    {
        CallTree callTree = callTreeRecorder.getCallTree();
        CallTreeNode newNode;
        CallTreeNode parent = callTreeRecorder.getCallTreeNode();

        // JobID���擾
        String submitJobID;
        try
        {
            submitJobID = (String)(args[0].getClass().getMethod("toString", new Class[]{}).invoke(args[0], new Object[]{}));
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }
        
        // �Ăяo�����ɁA��O�����t���O���N���A����
        callTreeRecorder.setExceptionOccured(false);
        if (invocation == null)
            invocation = registerInvocation(component, className, methodName, isResponse);

        // ��x�ł����[�g����Ă΂ꂽ���Ƃ̂��郁�\�b�h��ۑ�����B
        ExcludeMonitor.addTargetPreferred(invocation);
        ExcludeMonitor.removeExcludePreferred(invocation);

        // �ŏ��̌Ăяo���Ȃ̂ŁACallTree�����������Ă����B
        initCallTree(callTree, "submitJob", callTreeRecorder);
        newNode = CallTreeRecorder.createNode(invocation, args, stacktrace, config__);
        newNode.setDepth(0);
        newNode.setTree(callTree);
        callTree.setRootNode(newNode);
        callTreeRecorder.setDepth(0);

        try
        {
            if (newNode == null)
            {
                newNode = CallTreeRecorder.createNode(invocation, args, stacktrace, config__);
                newNode.setDepth(callTreeRecorder.getDepth());
            }

            // CallTreeNode��ǉ�
            CallTreeRecorder.addCallTreeNode(parent, callTree, newNode, config__);
            // VM��Ԏ擾
            VMStatus vmStatus = createVMStatus(parent, newNode, callTreeRecorder);

            newNode.setStartTime(System.currentTimeMillis());
            newNode.setStartVmStatus(vmStatus);

            // �������ꂽJobID��CallTreeNode�ɓo�^
            HadoopInfo hadoopInfo = new HadoopInfo();
            hadoopInfo.setSubmitJobID(submitJobID);
            newNode.setHadoopInfo(hadoopInfo);

            callTreeRecorder.setCallerNode(newNode);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }

        return true;
    }

    /**
     * submitJob()�̌㏈�����s���B
     * 
     * @param thisObject
     * @param returnValue
     * @param methodName
     * @param cause
     * @param callTreeRecorder
     * @param telegramId
     * 
     * @return {@code true}�F�����^{@code false}�F���s
     */
    private static boolean recordPostSubmitJob(final Object thisObject,
                                               final Object returnValue,
                                               final String methodName,
                                               final Throwable cause,
                                               CallTreeRecorder callTreeRecorder,
                                               long telegramId)
    {
        CallTree callTree = callTreeRecorder.getCallTree();

        try
        {
            // �A���[���ʒm�����A�C�x���g�o�͏������s���B
            recordAndAlarmEvents(callTree, callTreeRecorder, telegramId);

            // �Ăяo�������擾�B
            CallTreeNode node = callTreeRecorder.getCallTreeNode();
            if (node == null)
            {
                // �Ăяo������񂪎擾�ł��Ȃ��ꍇ�͏������L�����Z������B
                // (���ʃ��C���ŗ�O�����������ꍇ�̂��߁B)
                return false;
            }

            node.setEndTime(System.currentTimeMillis());

            CallTreeNode parent = node.getParent();
            addEndVMStatus(node, parent, callTreeRecorder);

            Invocation invocation = node.getInvocation();

            if (cause != null && callTree.getCause() != cause)
            {
                callTreeRecorder.setExceptionOccured(true);

                invocation.addThrowable(cause);
                callTree.setCause(cause);

                if (config__.isAlarmException())
                {
                    // ����������O���L�^���Ă���
                    node.setThrowable(cause);
                    node.setThrowTime(System.currentTimeMillis());
                }
            }

            // �o�^���ꂽ�W���u�����s�����X�g�ɓo�^
            String jobID = HadoopObjectAnalyzer.getJobIDfromJobStatus(returnValue);
            synchronized(runningJobList__)
            {
                runningJobList__.add(jobID);
            }

            if (node.hasHadoopInfo())
                HadoopCallTreeRecorder.getInstance().addCallTree(node.getHadoopInfo().getSubmitJobID(), callTree);

            if (invocation.getAlarmThreshold()    != Invocation.THRESHOLD_NOT_SPECIFIED ||
                invocation.getAlarmCpuThreshold() != Invocation.THRESHOLD_NOT_SPECIFIED)
            {
                // �ȉ��ACallTreeNode��root�̏ꍇ�A�܂���臒l���ʂɎw�肳��Ă���ꍇ�̏����B
                // CallTreeNode��root�ŁA���v�l�L�^��臒l�𒴂��Ă����ꍇ�ɁA�g�����U�N�V�������L�^����B
                if (node.getAccumulatedTime() >= config__.getStatisticsThreshold())
                {
                    recordTransaction(node);
                }

                // ���[�g�m�[�h�̏ꍇ�̏���
                postProcessOnRootNode(callTree, node, callTreeRecorder);
            }

        }
        catch (Throwable ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }
        return true;
    }

    /**
     * �W���u�I����CallTree���쐬��A���O�������o���܂��B
     * 
     * @param invocation {@link Invocation}
     * @param hostName �z�X�g��
     * @param telegramID �d��ID
     * @param succeededList ���������W���u�̃��X�g
     * @param killedList ��~���ꂽ�W���u�̃��X�g
     */
    private static void makeCallHistory(Invocation invocation,
                                        String hostName,
                                        long telegramID,
                                        List<String> succeededList,
                                        List<String> killedList)
    {
        HadoopCallTreeRecorder recorder = HadoopCallTreeRecorder.getInstance();

        // �������X�g����JobID���ƂɁA�I�����̏���ݒ肵�A���O�����o��
        for (String jobID : succeededList)
        {
            CallTree tree = makeSucceededHistory(invocation, hostName, jobID);
            recorder.addCallTree(jobID, tree);

            recordAndAlarmProcedure(recorder.getCallTree(jobID), null, CallTreeRecorder.getInstance(), telegramID);
        }
        // ��~���X�g����JobID���ƂɁA�I�����̏���ݒ肵�A���O�����o��
        for (String jobID : killedList)
        {
            CallTree tree = makeKilledHistory(invocation, hostName, jobID);
            recorder.addCallTree(jobID, tree);

            recordAndAlarmProcedure(recorder.getCallTree(jobID), null, CallTreeRecorder.getInstance(), telegramID);
        }
    }

    /**
     * �W���u���������쐬���܂��B
     * 
     * @param invocation {@link Invocation}
     * @param hostName �z�X�g��
     * @param jobID ���������W���uID
     * 
     * @return {@link CallTree}
     */
    private static CallTree makeSucceededHistory(Invocation invocation,
                                                 String hostName,
                                                 String jobID)
    {
        // CallTreeRecorder�̎擾
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();

        // CallTree�̍쐬
        CallTree tree = new CallTree();
        initCallTree(tree, "getJobStatus", recorder); // �֋X��A���\�b�h����ݒ�

        // CallTreeNode�̍쐬
        CallTreeNode node = CallTreeRecorder.createNode(invocation, null, null, config__);
        node.setDepth(0);
        node.setTree(tree);

        tree.setRootNode(node);
        recorder.setDepth(0);

        // �J�n��Ԃ�VM��Ԃ��쐬
        VMStatus vmStatus = createVMStatus(null, node, recorder);
        node.setStartTime(System.currentTimeMillis());
        node.setStartVmStatus(vmStatus);

        // �W���u��������ݒ�
        HadoopInfo info = new HadoopInfo();
        info.setHost(hostName);
        info.setCompleteJobID(jobID);
        node.setHadoopInfo(info);

        // �I������VM��Ԃ��쐬
        node.setEndTime(System.currentTimeMillis());
        addEndVMStatus(node, null, recorder);

        return tree;
    }

    /**
     * �W���u��~�����쐬���܂��B
     * 
     * @param invocation {@link Invocation}
     * @param hostName �z�X�g��
     * @param jobID ��~���ꂽ�W���uID
     * 
     * @return {@link CallTree}
     */
    private static CallTree makeKilledHistory(Invocation invocation,
                                                 String hostName,
                                                 String jobID)
    {
        // CallTreeRecorder�̎擾
        CallTreeRecorder recorder = CallTreeRecorder.getInstance();

        // CallTree�̍쐬
        CallTree tree = new CallTree();
        initCallTree(tree, "killJob", recorder); // �֋X��A���\�b�h����ݒ�

        // CallTreeNode�̍쐬
        CallTreeNode node = CallTreeRecorder.createNode(invocation, null, null, config__);
        node.setDepth(0);
        node.setTree(tree);

        tree.setRootNode(node);
        recorder.setDepth(0);

        // �J�n��Ԃ�VM��Ԃ��쐬
        VMStatus vmStatus = createVMStatus(null, node, recorder);
        node.setStartTime(System.currentTimeMillis());
        node.setStartVmStatus(vmStatus);

        // �W���u��~����ݒ�
        HadoopInfo info = new HadoopInfo();
        info.setHost(hostName);
        info.setKilledJobID(jobID);
        node.setHadoopInfo(info);

        // �I������VM��Ԃ��쐬
        node.setEndTime(System.currentTimeMillis());
        addEndVMStatus(node, null, recorder);

        return tree;
    }
}
