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
package jp.co.acroquest.endosnipe.javelin;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.bean.Component;
import jp.co.acroquest.endosnipe.javelin.bean.ExcludeMonitor;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.bean.TripleState;
import jp.co.acroquest.endosnipe.javelin.communicate.AlarmListener;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinAcceptThread;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinConnectThread;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CallTreeNodeMonitor;
import jp.co.acroquest.endosnipe.javelin.event.CallTreeEventCreator;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.EventRepository;
import jp.co.acroquest.endosnipe.javelin.event.InvocationFullEvent;
import jp.co.acroquest.endosnipe.javelin.event.JavelinEventCounter;
import jp.co.acroquest.endosnipe.javelin.helper.VMStatusHelper;
import jp.co.acroquest.endosnipe.javelin.log.JavelinFileGenerator;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;
import jp.co.acroquest.endosnipe.javelin.record.AllRecordStrategy;
import jp.co.acroquest.endosnipe.javelin.record.JvnFileNotifyCallback;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ���\�b�h�Ăяo�����̎擾���s���N���X�ł��B
 *
 * @author acroquest
 */
public class StatsJavelinRecorder
{
    /** ����������t���O */
    private static boolean                   initialized__;

    private static VMStatusHelper            vmStatusHelper__       = new VMStatusHelper();

    /** �L�^��������N���X */
    private static RecordStrategy            recordStrategy__;

    /** Javelin���O�o�̓N���X�B */
    private static JavelinFileGenerator      generator__;

    /** �A���[�����X�i�̃��X�g */
    private static final List<AlarmListener> ALARM_LISTENER_LIST    =
                                                          new ArrayList<AlarmListener>();

    /** �o�b�t�@�T�C�Y�̃f�t�H���g�l */
    private static final int                 DEF_BUFFER_SIZE        = 1024;

    /** �A���[���폜���b�Z�[�W��ۑ����܂��B */
    private static StringBuffer              discardBuffer__        =
                                                          new StringBuffer(DEF_BUFFER_SIZE);

    /** �O��폜��ʒm�������Ԃ�ۑ����܂��B */
    private static long                      lastDiscardTime__      = 0;

    /** �C�x���g�̏d�����`�F�b�N���邽�߂̃��|�W�g���B */
    private static EventRepository           eventRepository__      = new EventRepository();

    /** �N���C�A���g���[�h */
    private static final String              CONNECTION_MODE_CLIENT = "client";

    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�ł��B<br />
     */
    private StatsJavelinRecorder()
    {
        // Do Nothing.
    }

    /**
     * �����������B AlarmListener�̓o�^���s���B RecordStrategy������������B
     * MBeanServer�ւ�ContainerMBean�̓o�^���s���B
     * ���J�pHTTP�|�[�g���w�肳��Ă����ꍇ�́AHttpAdaptor�̐����Ɠo�^���s���B
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    public static void javelinInit(final JavelinConfig config)
    {
        if (initialized__ == true)
        {
            return;
        }
        try
        {
            // �G���[���K�[������������B
            SystemLogger.initSystemLog(config);

            generator__ = new JavelinFileGenerator(config);

            // AlarmListener��o�^����
            registerAlarmListeners(config);

            // RecordStrategy������������
            String strategyName = config.getRecordStrategy();
            try
            {
                recordStrategy__ = (RecordStrategy)loadClass(strategyName).newInstance();
            }
            catch (ClassNotFoundException cfne)
            {
                String defaultRecordstrategy = JavelinConfig.DEFAULT_RECORDSTRATEGY;
                SystemLogger.getInstance().info("Failed to load " + strategyName
                                                        + ". Use default value "
                                                        + defaultRecordstrategy
                                                        + " as javelin.recordStrategy.");
                recordStrategy__ = (RecordStrategy)loadClass(defaultRecordstrategy).newInstance();
            }

            // �X���b�h�̊Ď����J�n����B
            vmStatusHelper__.init();

            // �N���C�A���g���[�h�̏ꍇ�̂݁ATCP�ł̐ڑ����J�n����B
            // �N���C�A���g���[�h�łȂ��ꍇ�ATCP�ł̐ڑ���t���J�n����B
            if (CONNECTION_MODE_CLIENT.equals(config.getConnectionMode()))
            {
                JavelinConnectThread.getInstance().connect();
            }
            else
            {
                // TCP�ł̐ڑ���t���J�n����B
                JavelinAcceptThread.getInstance().start();
            }

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
     * <li>StatsJavelinRecorder�����[�h�����N���X���[�_</li>
     * <li>�R���e�L�X�g�N���X���[�_</li>
     * </ol>
     *
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    private static void registerAlarmListeners(final JavelinConfig config)
    {
        String[] alarmListeners = config.getAlarmListeners().split(",");
        for (String alarmListenerName : alarmListeners)
        {
            try
            {
                if ("".equals(alarmListenerName))
                {
                    continue;
                }

                Class<?> alarmListenerClass = loadClass(alarmListenerName);
                Object listener = alarmListenerClass.newInstance();
                if (listener instanceof AlarmListener)
                {
                    AlarmListener alarmListener = (AlarmListener)listener;
                    alarmListener.init();
                    addListener(alarmListener);
                    String message = "Register " + alarmListenerName + " for AlarmListener.";
                    SystemLogger.getInstance().debug(message);
                }
                else
                {
                    String message = 
                       alarmListenerName
                    + " is not used for sending alarms because it doesn't implement AlarmListener.";
                    SystemLogger.getInstance().info(message);
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(
                        alarmListenerName
                        + " is not used for sending alarms because of failing to be registered.",
                                                ex);
            }
        }
    }

    /**
     * �N���X�����[�h����B �ȉ��̏��ŃN���X���[�_�ł̃��[�h�����݂�B
     * <ol>
     * <li>StatsJavelinRecorder�����[�h�����N���X���[�_</li>
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
     * JavelinRecorder, JDBCJavelinRecorder����Ăяo�����Ƃ��̑O�����B
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param args ����
     * @param doExclude ���O�Ώۏ������s�����ǂ���
     */
    public static void preProcess(final String className, final String methodName,
            final Object[] args, final JavelinConfig config, final boolean doExclude)
    {
        StackTraceElement[] stacktrace = null;
        if (config.isLogStacktrace())
        {
            stacktrace = ThreadUtil.getCurrentStackTrace();
        }
        String normalizedMName = methodName;
        if (methodName.length() > config.getInvocationNameLimitLength())
        {
            normalizedMName = methodName.substring(0, config.getInvocationNameLimitLength());
        }

        preProcess(null, null, className, normalizedMName, args, stacktrace, config, doExclude);
    }

    /**
     * JavelinRecorder, JDBCJavelinRecorder����Ăяo�����Ƃ��̑O�����B
     * @param component �N���X��
     * @param invocation ���\�b�h��
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param args ����
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     */
    public static void preProcess(final Component component, final Invocation invocation,
            final Object[] args, final JavelinConfig config, final boolean doExcludeProcess)
    {
        StackTraceElement[] stacktrace = null;
        if (config.isLogStacktrace())
        {
            stacktrace = ThreadUtil.getCurrentStackTrace();
        }
        preProcess(component, invocation, component.getClassName(), invocation.getMethodName(),
                   args, stacktrace, config, doExcludeProcess);
    }

    /**
     * �O�����B
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    public static void preProcess(final String className, final String methodName,
            final Object[] args, final StackTraceElement[] stacktrace, final JavelinConfig config)
    {
        preProcess(null, null, className, methodName, args, stacktrace, config, true);
    }

    /**
     * �O�����B
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExclude ���O�Ώۏ������s�����ǂ���
     */
    public static void preProcess(final String className, final String methodName,
            final Object[] args, final StackTraceElement[] stacktrace, final JavelinConfig config,
            final boolean doExclude)
    {
        String normalizedMName = methodName;
        if (methodName.length() > config.getInvocationNameLimitLength())
        {
            normalizedMName = methodName.substring(0, config.getInvocationNameLimitLength());
        }

        preProcess(null, null, className, normalizedMName, args, stacktrace, config, doExclude,
                   false);
    }

    /**
     * �O�����B
     * @param component �R���|�[�l���g
     * @param invocation �C���{�P�[�V����
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     */
    public static void preProcess(Component component, Invocation invocation,
            final String className, final String methodName, final Object[] args,
            final StackTraceElement[] stacktrace, final JavelinConfig config,
            final boolean doExcludeProcess)
    {
        preProcess(component, invocation, className, methodName, args, stacktrace, config,
                   doExcludeProcess, false);
    }

    /**
     * �O�����B
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExclude ���O�Ώۏ������s�����ǂ���
     * @param isResponse �f�t�H���g�Ń��X�|���X���L�^���邩�ǂ����B
     */
    public static void preProcess(final String className, final String methodName,
            final Object[] args, final StackTraceElement[] stacktrace, final JavelinConfig config,
            final boolean doExclude, final boolean isResponse)
    {
        String normalizedMName = methodName;
        if (methodName.length() > config.getInvocationNameLimitLength())
        {
            normalizedMName = methodName.substring(0, config.getInvocationNameLimitLength());
        }

        preProcess(null, null, className, normalizedMName, args, stacktrace, config, doExclude,
                   isResponse);
    }

    /**
     * �O�����B
     * @param component �R���|�[�l���g
     * @param invocation Invocation
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     * @param isResponse �f�t�H���g�Ń��X�|���X���L�^���邩�ǂ����B
     */
    public static void preProcess(Component component, Invocation invocation,
            final String className, final String methodName, final Object[] args,
            final StackTraceElement[] stacktrace, final JavelinConfig config,
            final boolean doExcludeProcess, final boolean isResponse)
    {
        synchronized (StatsJavelinRecorder.class)
        {
            // ����������
            if (initialized__ == false)
            {
                javelinInit(config);
            }
        }
        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();

        // Javelin�̃��O�o�͏������Ăяo����Ă���ꍇ�A�������s��Ȃ�
        if (callTreeRecorder.isRecordMethodCalled_)
        {
            return;
        }

        // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X���Z�b�g
        callTreeRecorder.isRecordMethodCalled_ = true;

        try
        {
            boolean isRecorded =
                                 recordPreInvocation(component, invocation, className, methodName,
                                                     args, stacktrace, config, doExcludeProcess,
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
            callTreeRecorder.isRecordMethodCalled_ = false;
            callTreeRecorder.setDepth(callTreeRecorder.getDepth() + 1);
        }
    }

    /**
     * �O�����B
     * @param component �R���|�[�l���g
     * @param invocation Invocation
     * @param className  �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param stacktrace �X�^�b�N�g���[�X
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     * @param isResponse �f�t�H���g�Ń��X�|���X���L�^���邩�ǂ����B
     * @param callTreeRecorder �R�[���c���[
     *
     * @return �L�^�������ǂ���
     */
    private static boolean recordPreInvocation(Component component, Invocation invocation,
            final String className, final String methodName, final Object[] args,
            final StackTraceElement[] stacktrace, final JavelinConfig config,
            final boolean doExcludeProcess, final boolean isResponse,
            CallTreeRecorder callTreeRecorder)
    {
        if (component == null)
        {
            component = MBeanManager.getComponent(className);
        }
        if (invocation == null)
        {
            invocation = getInvocation(component, methodName);
        }

        boolean isExclude = ExcludeMonitor.isExclude(invocation);
        if (doExcludeProcess == true && isExclude == true)
        {
            return false;
        }

        CallTree callTree = callTreeRecorder.getCallTree();
        CallTreeNode newNode = null;
        CallTreeNode parent = callTreeRecorder.getCallTreeNode();
        if (parent == null)
        {
            // ���[�g�Ăяo�����ɁA��O�����t���O���N���A����
            callTreeRecorder.isExceptionOccured_ = false;
            if (invocation == null)
            {
                invocation =
                             registerInvocation(component, className, methodName, config,
                                                isResponse);
            }

            // ��x�ł����[�g����Ă΂ꂽ���Ƃ̂��郁�\�b�h��ۑ�����B
            ExcludeMonitor.addTargetPreferred(invocation);
            ExcludeMonitor.removeExcludePreferred(invocation);

            // �ŏ��̌Ăяo���Ȃ̂ŁACallTree�����������Ă����B
            initCallTree(callTree, methodName, config, callTreeRecorder);
            newNode = CallTreeRecorder.createNode(invocation, args, stacktrace, config);
            newNode.setDepth(0);
            newNode.setTree(callTree);
            callTree.setRootNode(newNode);
            callTreeRecorder.setDepth(0);
        }
        else
        {
            // CallTreeNode�������ꍇ�̓C�x���g�𑗐M����B
            sendCallTreeEvent(callTree, className, methodName, config, callTreeRecorder);

            if (invocation == null)
            {
                // ��x���Ăяo����Ă��Ȃ��ꍇ�́A�L�^����B
                invocation =
                             registerInvocation(component, className, methodName, config,
                                                isResponse);
            }
            else if (doExcludeProcess == true)
            {
                // ���O�Ώۃ��j�^����Ă΂�郁�\�b�h�ɑ΂��āA
                // ���[�g����Ă΂ꂽ���Ƃ��Ȃ��A���@���O�Ώۃ��X�g�ɂ��郁�\�b�h�����O����B
                boolean isTargetPreferred = ExcludeMonitor.isTargetPreferred(invocation);
                if (isTargetPreferred == false)
                {
                    boolean isExcludePreferred = ExcludeMonitor.isExcludePreffered(invocation);
                    if (isExcludePreferred == true)
                    {
                        return false;
                    }
                }
            }
        }

        try
        {
            try
            {
                if (newNode == null)
                {
                    newNode = CallTreeRecorder.createNode(invocation, args, stacktrace, config);
                    newNode.setDepth(callTreeRecorder.getDepth());
                }

                CallTreeRecorder.addCallTreeNode(parent, callTree, newNode, config);
                VMStatus vmStatus = createVMStatus(parent, newNode, config, callTreeRecorder);
                newNode.setStartTime(System.currentTimeMillis());
                newNode.setStartVmStatus(vmStatus);

                callTreeRecorder.setCallerNode(newNode);
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }

            boolean isTarget = ExcludeMonitor.isTarget(invocation);
            if (isTarget == false)
            {
                judgeExclude(doExcludeProcess, invocation);
            }
            else
            {
                boolean isTargetPreferred = ExcludeMonitor.isTargetPreferred(invocation);
                if (isTargetPreferred == false)
                {
                    judgeExclude(doExcludeProcess, invocation);
                }
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return false;
        }

        return true;
    }

    private static void judgeExclude(final boolean doExcludeProcess, Invocation invocation)
    {
        if (doExcludeProcess == true)
        {
            judgeExclude(invocation);
        }
        else
        {
            ExcludeMonitor.removeExcludePreferred(invocation);
        }
    }

    private static void judgeExclude(Invocation invocation)
    {
        ExcludeMonitor.judgeExclude(invocation);
    }

    /**
     * �w�肵���m�[�hparent�̎q�m�[�h�̏���jvn���O�ɏo�͂��AInvocation�ɔ��f����B
     *
     * @param tree �R�[���c���[�B
     * @param finishedNodeList �m�[�h�B
     * @param callTreeRecorder callTreeRecorder
     */
    private static void recordChildNodes(CallTree tree, List<CallTreeNode> finishedNodeList,
            CallTreeRecorder callTreeRecorder, JavelinConfig config)
    {
        CallTreeNode dummyParent = createDummyNode(finishedNodeList);
        List<CallTreeNode> children = dummyParent.getChildren();

        if (children.size() == 0)
        {
            return;
        }

        generator__.generateJaveinFile(tree, dummyParent, new JvnFileNotifyCallback(),
                                       children.get(children.size() - 1), 0);

        // �q�m�[�h�̏����L�^����B
        for (CallTreeNode child : children)
        {
            recordTransaction(child);
        }

        // �q�m�[�h���N���A����B
        callTreeRecorder.clearChildren();
    }

    /**
     * �v���Ώۂ̕����\���̂��߂ɁA�_�~�[��CallTreeNode�𐶐�����B
     *
     * @param finishedNodeList ���ƂȂ�CallTreeNode
     * @return �_�~�[��CallTreeNode�B
     */
    private static CallTreeNode createDummyNode(List<CallTreeNode> finishedNodeList)
    {
        CallTreeNode dummyNode = new CallTreeNode();
        dummyNode.setChildren(new java.util.ArrayList<CallTreeNode>());
        dummyNode.getChildren().addAll(finishedNodeList);
        return dummyNode;
    }

    /**
     * �R�[���c���[�����������܂��B
     *
     * @param callTree callTree
     * @param methodName ���\�b�h��
     * @param config �ݒ�
     * @param callTreeRecorder callTreeRecorder
     */
    private static void initCallTree(CallTree callTree, final String methodName,
            final JavelinConfig config, CallTreeRecorder callTreeRecorder)
    {
        // ����Ăяo�����̓R�[���c���[������������B
        callTree.clearDepth();

        callTree.setRootCallerName(config.getRootCallerName());
        callTree.setEndCalleeName(config.getEndCalleeName());

        String threadId = createThreadId(methodName, config, callTreeRecorder);
        if (threadId != null)
        {
            callTree.setThreadID(threadId);
        }
    }

    private static VMStatus createVMStatus(CallTreeNode parent, CallTreeNode newNode,
            final JavelinConfig config, CallTreeRecorder callTreeRecorder)
    {
        VMStatus vmStatus;

        if (parent == null && config.isLogMBeanInfoRoot())
        {
            vmStatus = vmStatusHelper__.createVMStatus(callTreeRecorder);
        }
        else if (config.isLogMBeanInfo())
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
     * CallTreeNode�̐���臒l�𒴂��Ă���ꍇ�ɁA�C�x���g�𑗐M���܂��B
     *
     * @param callTree �R�[���c���[�B
     * @param className �N���X���B
     * @param methodName ���\�b�h���B
     * @param config �ݒ�B
     * @param callTreeRecorder callTreeRecorder
     */
    private static boolean sendCallTreeEvent(CallTree callTree, final String className,
            final String methodName, final JavelinConfig config, CallTreeRecorder callTreeRecorder)
    {
        boolean isCallTreeFull = false;

        if (CallTreeRecorder.isCallTreeFull(callTree, config))
        {
            if (callTree.getFlag(EventConstants.NAME_CALLTREE_FULL) == null)
            {
                // ��������CallTreeNode���t�@�C���ɏ����o���B
                CommonEvent event;
                event =
                        CallTreeEventCreator.createTreeFullEvent(className, methodName,
                                                                 config.getCallTreeMax());
                addEvent(event);
                callTree.setFlag(EventConstants.NAME_CALLTREE_FULL, "");
            }

            if (config.isCallTreeAll())
            {
                List<CallTreeNode> finishedNodeList = callTreeRecorder.removeFinishedNode();
                recordChildNodes(callTree, finishedNodeList, callTreeRecorder, config);
            }
            else
            {
                isCallTreeFull = true;
            }
        }

        return isCallTreeFull;
    }

    /**
     * �X���b�hID�𐶐�����B
     *
     * @param methodName ���\�b�h���B
     * @param config �ݒ�B
     * @param callTreeRecorder CallTreeRecorder
     * @return �X���b�hID�B
     */
    private static String createThreadId(final String methodName, final JavelinConfig config,
            final CallTreeRecorder callTreeRecorder)
    {
        String threadId = null;
        switch (config.getThreadModel())
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
     * @return Invocation
     */
    public static Invocation getInvocation(final Component component, final String methodName)
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
     * @param config �ݒ�
     * @param isResponse ���X�|���X
     * @return �o�^����Invocation
     */
    public static Invocation registerInvocation(Component component, final String className,
            final String methodName, final JavelinConfig config, boolean isResponse)
    {
        if (component == null)
        {
            try
            {
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
            invocation = registerInvocation(component, methodName, config, isResponse);
        }
        return invocation;
    }

    /**
     * Invocation ���R���|�[�l���g�ɓo�^���܂��B
     *
     * @param component �o�^�ΏۃR���|�[�l���g
     * @param methodName �o�^���郁�\�b�h
     * @param config �ݒ�
     * @param isResponse ���X�|���X�O���t�ɕ\������ꍇ�� <code>true</code>
     * @return �o�^���� Invocation
     */
    public static Invocation registerInvocation(final Component component, final String methodName,
            final JavelinConfig config, final boolean isResponse)
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
        if (config.getSendInvocationFullEvent() == true
                && recordedInvocationNum >= config.getRecordInvocationMax())
        {
            Invocation removedInvoction = component.addAndDeleteOldestInvocation(invocation);
            sendInvocationFullEvent(component, className, recordedInvocationNum, invocation,
                                    removedInvoction);
        }
        else
        {
            component.addInvocation(invocation);
        }

        invocation.setResponseGraphOutput(TripleState.ON);
        RootInvocationManager.addRootInvocation(invocation);
        return invocation;
    }

    /**
     * InvocationFullEvent�𑗐M����B
     *
     * @param component �R���|�[�l���g�B
     * @param className �N���X��
     * @param invocationNum Invocation�̐�
     * @param addInvocation �ǉ���Invocation
     * @param removedInvocation ���O����Invocation
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
     * �㏈���i�{�����������j�B
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param returnValue �߂�l
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    public static void postProcess(String className, String methodName, final Object returnValue,
            final JavelinConfig config)
    {
        postProcess(className, methodName, returnValue, config, true);
    }

    /**
    * �㏈���i�{�����������j�B<br />
    *
    * @param className �N���X��
    * @param methodName ���\�b�h��
    * @param returnValue �߂�l
    * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
    * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
    */
    public static void postProcess(String className, String methodName, final Object returnValue,
            final JavelinConfig config, boolean doExcludeProcess)
    {
        postProcessCommon(returnValue, null, config);
    }

    /**
    * �㏈���i�{�����������j�B<br />
    *
    * @param className �N���X��
    * @param methodName ���\�b�h��
    * @param returnValue �߂�l
    * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
    * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
    * @param telegramId �d�� ID
    */
    public static void postProcess(String className, String methodName, final Object returnValue,
            final JavelinConfig config, boolean doExcludeProcess, final long telegramId)
    {
        postProcessCommon(returnValue, null, config, telegramId);
    }

    /**
     * �㏈���̋��ʏ����B<br />
     *
     *�@CallTree �ɏ����i�[���܂��B
     * �܂��A�K�v�ɉ����� Javelin ���O�o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param returnValue �߂�l�i <code>null</code> ���j
     * @param cause ��O�����I�u�W�F�N�g�i <code>null</code> ���j
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    private static void postProcessCommon(final Object returnValue, final Throwable cause,
            final JavelinConfig config)
    {
        postProcessCommon(returnValue, cause, config, 0);
    }

    /**
     * �㏈���̋��ʏ����B<br />
     *
     *�@CallTree �ɏ����i�[���܂��B
     * �܂��A�K�v�ɉ����� Javelin ���O�o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param returnValue �߂�l�i <code>null</code> ���j
     * @param cause ��O�����I�u�W�F�N�g�i <code>null</code> ���j
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
    * @param telegramId �d�� ID
     */
    private static void postProcessCommon(final Object returnValue, final Throwable cause,
            final JavelinConfig config, final long telegramId)
    {
        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();

        // Javelin�̃��O�o�͏������Ăяo����Ă���ꍇ�A�������s��Ȃ�
        if (callTreeRecorder.isRecordMethodCalled_)
        {
            return;
        }

        // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X���Z�b�g
        callTreeRecorder.isRecordMethodCalled_ = true;

        try
        {
            Integer depth = callTreeRecorder.getDepth() - 1;
            callTreeRecorder.setDepth(depth);
            CallTree callTree = callTreeRecorder.getCallTree();
            if (callTree.containsDepth(depth) == false)
            {
                return;
            }
            callTree.removeDepth(depth);

            recordPostInvocation(returnValue, cause, config, callTreeRecorder, telegramId);
        }
        finally
        {
            // Javelin�̃��O�o�͏����Ăяo���X�e�[�^�X������
            callTreeRecorder.isRecordMethodCalled_ = false;
        }
    }

    /**
     * �㏈���̋��ʏ����B<br />
     *
     *�@CallTree �ɏ����i�[���܂��B
     * �܂��A�K�v�ɉ����� Javelin ���O�o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param returnValue �߂�l�i <code>null</code> ���j
     * @param cause ��O�����I�u�W�F�N�g�i <code>null</code> ���j
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param callTreeRecorder �R�[���c���[���R�[�_
     * @param telegramId �d�� ID
     */
    private static boolean recordPostInvocation(final Object returnValue, final Throwable cause,
            final JavelinConfig config, CallTreeRecorder callTreeRecorder, long telegramId)
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
            addEndVMStatus(node, parent, config, callTreeRecorder);

            Invocation invocation = node.getInvocation();

            if (cause != null && callTree.getCause() != cause)
            {
                callTreeRecorder.isExceptionOccured_ = true;

                invocation.addThrowable(cause);
                callTree.setCause(cause);

                if (config.isAlarmException())
                {
                    // ����������O���L�^���Ă���
                    node.setThrowable(cause);
                    node.setThrowTime(System.currentTimeMillis());
                }
            }

            if (returnValue != null && config.isLogReturn())
            {
                // �߂�l���擾����
                String returnString = getReturnValueString(returnValue, config);
                node.setReturnValue(returnString);
            }

            if (parent != null)
            {
                parent.addChildrenTime(node.getAccumulatedTime());
                parent.addChildrenCpuTime(node.getCpuTime());
                parent.addChildrenUserTime(node.getUserTime());

                callTreeRecorder.setCallerNode(parent);
                boolean isTarget = ExcludeMonitor.isTarget(invocation);
                boolean isTargetPreferred = ExcludeMonitor.isTargetPreferred(invocation);
                if (isTarget == false || isTargetPreferred == false)
                {
                    judgeExclude(invocation);
                }

                recordTransaction(node);

                // CallTree�����̏ꍇ�܂��̓m�[�h��������ɒB���Ă���ꍇ�́A
                // ���������������q�m�[�h��Tree����폜����B
                if (callTree.isCallTreeEnabled() == false
                        || CallTreeRecorder.isCallTreeFull(callTree, config))
                {
                    parent.removeChild(node);
                }

            }

            if (parent == null
                    || invocation.getAlarmThreshold() != Invocation.THRESHOLD_NOT_SPECIFIED
                    || invocation.getAlarmCpuThreshold() != Invocation.THRESHOLD_NOT_SPECIFIED)
            {
                // �ȉ��ACallTreeNode��root�̏ꍇ�A�܂���臒l���ʂɎw�肳��Ă���ꍇ�̏����B
                // CallTreeNode��root�ŁA���v�l�L�^��臒l�𒴂��Ă����ꍇ�ɁA�g�����U�N�V�������L�^����B
                if (parent == null && node.getAccumulatedTime() >= config.getStatisticsThreshold())
                {
                    recordTransaction(node);
                }

                try
                {
                    // �K�v�ɉ����āAJavelin���O�ւ̏o�́A�A���[���ʒm�������s��
                    recordAndAlarmProcedure(config, callTree, node, callTreeRecorder, telegramId);
                }
                finally
                {
                    if (parent == null)
                    {
                        // ���[�g�m�[�h�̏ꍇ
                        postProcessOnRootNode(callTree, node, callTreeRecorder);
                    }
                }
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
     * ���[�g�m�[�h�̏ꍇ�̌㏈�����s���܂��B<br />
     *
     * @param callTree CallTree
     * @param node CallTreeNode
     * @param callTreeRecorder callTreeRecorder
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
     * �C�x���g���������Ă���ꍇ�ɁA Javelin ���O�ւ̏o�͂ƃA���[���ʒm���s���܂��B<br />
     *
     * @param callTree CallTree
     * @param callTreeRecorder callTreeRecorder
     * @param telegramId �d�� ID
     */
    private static void recordAndAlarmEvents(CallTree callTree,
            CallTreeRecorder callTreeRecorder, long telegramId)
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
                                                   new JvnFileNotifyCallback(), null, telegramId);
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
     * �K�v�ɉ����āA Javelin ���O�ւ̏o�́A�A���[���ʒm�������s���܂��B<br />
     *
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param callTree CallTree
     * @param node CallTreeNode
     * @param callTreeRecorder callTreeRecorder
     * @param telegramId �d�� ID
     */
    private static void recordAndAlarmProcedure(final JavelinConfig config, CallTree callTree,
            CallTreeNode node, CallTreeRecorder callTreeRecorder, final long telegramId)
    {
        // �A���[���ʒm�̗L���𔻒肷��(����D��x�F��)
        boolean judgeHighSendAlarm = judgeHighPrioritySendExceedThresholdAlarm(callTree, node);

        // �A���[���ʒm�̗L���𔻒肷��
        boolean judgeSendAlarm =
                                 judgeSendExceedThresholdAlarm(callTree, node, config,
                                                               recordStrategy__, callTreeRecorder);

        boolean isLastAlarmTooNear = false;
        if (judgeHighSendAlarm || judgeSendAlarm)
        {
            isLastAlarmTooNear = checkLastAlarmTime(node, config, judgeHighSendAlarm);
        }

        // �A���[����臒l�𒴂��Ă����ꍇ�ɁA�A���[����ʒm����B
        if (judgeHighSendAlarm == true || (judgeSendAlarm == true && isLastAlarmTooNear == false))
        {
            callTree.addHighPriorityRecordStrategy("AllRecordStrategy", new AllRecordStrategy());

            if (node.getParent() == null)
            {
                generator__.generateJaveinFile(callTree, createCallback(callTree, node), node,
                                               telegramId);
                sendAlarm(node, callTreeRecorder);
            }
        }
    }

    /**
     * ���\�b�h�̖߂�l�𕶎���Ŏ擾���܂��B<br />
     *
     * @param returnValue �߂�l�I�u�W�F�N�g
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @return �߂�l�̕�����\��
     */
    private static String getReturnValueString(final Object returnValue, final JavelinConfig config)
    {
        String returnString;
        if (config.isReturnDetail())
        {
            int returnDetailDepth = config.getReturnDetailDepth();
            returnString = StatsUtil.buildDetailString(returnValue, returnDetailDepth);
            returnString = StatsUtil.toStr(returnString, config.getStringLimitLength());
        }
        else
        {
            returnString = StatsUtil.toStr(returnValue, config.getStringLimitLength());
        }
        return returnString;
    }

    private static void addEndVMStatus(CallTreeNode node, CallTreeNode parent,
            final JavelinConfig config, CallTreeRecorder callTreeRecorder)
    {
        long duration = node.getEndTime() - node.getStartTime();
        if (duration == 0)
        {
            node.setEndVmStatus(node.getStartVmStatus());
            return;
        }

        VMStatus vmStatus = createVMStatus(parent, node, config, callTreeRecorder);
        node.setEndVmStatus(vmStatus);

        setCpuTime(node);
        setUserTime(node);
    }

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
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂵂܂��B
     *
     * @param event �C�x���g�B
     * @param config �ݒ�B
     * @param telegramId �d�� ID
     *
     * @return �ǉ�����Node�B
     */
    public static CallTreeNode addEvent(CommonEvent event, JavelinConfig config, long telegramId)
    {
        return addEvent(event, false, config, telegramId);
    }

    /**
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂷂�B
     *
     * @param event �C�x���g�B
     * @param clear ���ɃC�x���g������ꍇ�����񂷂�B
     *
     * @return �ǉ�����Node�B
     */
    public static CallTreeNode addEvent(CommonEvent event, boolean clear)
    {
        return addEvent(event, clear, null, 0);
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
    public static CallTreeNode addEvent(CommonEvent event, boolean clear, JavelinConfig config,
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
        if (event.getLevel() >= CommonEvent.LEVEL_ERROR)
        {
            Invocation invocation = null;
            if (callTreeNode != null)
            {
                invocation = callTreeNode.getInvocation();
            }
            sendEventImmediately(event, config, invocation, callTreeRecorder, telegramId);
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

            CallTreeNode node = createEventNode(event, config, callTreeRecorder, tree);

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
            postProcess(null, null, (Object)null, config, false, telegramId);
        }

        return node;
    }

    /**
     * �C�x���g�p�̃m�[�h���쐬����B
     *
     * @param event �C�x���g
     * @param config �ݒ�
     * @param callTreeRecorder CallTreeRecorder
     * @param tree �c���[
     * @return �C�x���g�p�̃m�[�h�B
     */
    private static CallTreeNode createEventNode(CommonEvent event, JavelinConfig config,
            CallTreeRecorder callTreeRecorder, CallTree tree)
    {
        return createEventNode(event, config, callTreeRecorder, tree, null);
    }

    /**
     * �C�x���g�p�̃m�[�h���쐬����B
     *
     * @param event �C�x���g
     * @param config �ݒ�
     * @param callTreeRecorder CallTreeRecorder
     * @param tree �c���[
     * @param invocation Invocation
     * @return �C�x���g�p�̃m�[�h�B
     */
    private static CallTreeNode createEventNode(CommonEvent event, JavelinConfig config,
            CallTreeRecorder callTreeRecorder, CallTree tree, Invocation invocation)
    {
        if (invocation == null)
        {
            String className = config.getRootCallerName();
            String methodName = "";

            // CallTree�ɃX���b�h����ݒ肷��B
            String threadId = createThreadId(methodName, config, callTreeRecorder);
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
     * �����ɃC�x���g�𑗐M����B
     *
     * @param event ���M����C�x���g
     * @param config Javelin��config
     * @param invocation invocation
     * @param callTreeRecorder callTreeRecorder
     * @param telegram �d��
     */
    private static void sendEventImmediately(CommonEvent event, JavelinConfig config,
            Invocation invocation, CallTreeRecorder callTreeRecorder, long telegramId)
    {
        CallTree callTree = new CallTree();
        callTree.init();
        CallTreeNode node = createEventNode(event, config, callTreeRecorder, callTree, invocation);
        callTree.addEventNode(node);
        recordAndAlarmEvents(callTree, null, telegramId);
    }

    /**
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂷂�B
     *
     * @param event �C�x���g�B
     * @return �ǉ�����CallTreeNode�B
     */
    public static CallTreeNode addEvent(CommonEvent event)
    {
        return addEvent(event, null, 0);
    }

    /**
     * node�ɃC�x���g��ǉ����܂��B<br />
     * CallTree�������ꍇ�́A�V�K�쐬���܂��B
     * �K�����񂵂܂��B
     *
     * @param event �C�x���g
     * @param telegramId �d�� ID
     * @return �ǉ����� CallTreeNode
     */
    public static CallTreeNode addEvent(CommonEvent event, long telegramId)
    {
        return addEvent(event, null, telegramId);
    }

    /**
     * CallTreeNode�ɐݒ肳�ꂽ����N���X(����D��x�F��)�𗘗p���āA
     * �A���[����ʒm���邩�ǂ������肷��B
     *
     *�@@param callTree CallTree
     * @param node CallTreeNode
     * @return true:�ʒm����Afalse:�ʒm���Ȃ�
     */
    private static boolean judgeHighPrioritySendExceedThresholdAlarm(final CallTree callTree,
            final CallTreeNode node)
    {
        // CallTree�ɐݒ肳��Ă�������N���X�ł̔��茋�ʂ�
        // 1�ł�true�ł���΁A�����߂�l�Ƃ���
        RecordStrategy[] strategyList = callTree.getHighPriorityRecordStrategy();
        for (RecordStrategy str : strategyList)
        {
            if (str.judgeSendExceedThresholdAlarm(node))
            {
                return true;
            }
        }

        // ���肪���ׂ�false�̏ꍇ
        return false;
    }

    /**
     * S2JavelinConfig��CallTreeNode�ɐݒ肳�ꂽ����N���X�𗘗p���āA
     * �A���[����ʒm���邩�ǂ������肷��B
     *
     * @param tree CallTree
     * @param node CallTreeNode
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param strategy S2JavelinConfig�ɐݒ肳�ꂽ����N���X�B
     * @return true:�ʒm����Afalse:�ʒm���Ȃ�
     */
    private static boolean judgeSendExceedThresholdAlarm(final CallTree tree,
            final CallTreeNode node, final JavelinConfig config, final RecordStrategy strategy,
            final CallTreeRecorder callTreeRecorder)
    {
        // ��O���������Ă��āA��O�������ɃA���[���ʒm����ݒ�ł���΁A�K���A���[���ʒm���s��
        if (config.isAlarmException() && callTreeRecorder.isExceptionOccured_)
        {
            return true;
        }

        // �����ɐݒ肳��Ă�������N���X�ł̔��茋�ʂ�true�ł���΁A�����߂�l�Ƃ���
        if (strategy.judgeSendExceedThresholdAlarm(node))
        {
            return true;
        }

        // CallTreeNode�ɐݒ肳��Ă�������N���X�ł̔��茋�ʂ�
        // 1�ł�true�ł���΁A�����߂�l�Ƃ���
        RecordStrategy[] strategyList = tree.getRecordStrategy();
        for (RecordStrategy str : strategyList)
        {
            if (str.judgeSendExceedThresholdAlarm(node))
            {
                return true;
            }
        }

        // ���肪���ׂ�false�̏ꍇ
        return false;
    }

    /**
     * S2JavelinConfig��CallTree�ɐݒ肳�ꂽ����N���X�ɑ΂��āA
     * �����Ɍ㏈�����s���B
     *
     * @param callTree CallTree
     * @param node CallTreeNode
     * @param strategy S2JavelinConfig�ɐݒ肳�ꂽ����N���X�B
     */
    public static void postJudge(final CallTree callTree, final CallTreeNode node,
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
     * �ŏI�A���[�����M�������m�F���A�X�V����B
     * �A���[�����M�������猻�݂܂ł̌o�ߎ��Ԃ�臒l(javelin.alarmIntervalThreshold)��
     * �����Ă����ꍇ�ɂ́A���O���o�͂�true��Ԃ��B
     * �������A����D��x�F���̎��s���ʂ�true�̏ꍇ�́A�o�ߎ��ԂɊ֌W�Ȃ��A���O�o�͂�true��Ԃ��B
     *
     * @param node �Ώۂ̃m�[�h�B
     * @param config �ݒ�B
     * @param judgeHigh ����D��x�F���̎��s����
     * @return �A���[�����M�������猻�݂܂ł̌o�ߎ��Ԃ�臒l�𒴂��Ă����ꍇ�Ɍ���true��Ԃ��B
     */
    private static boolean checkLastAlarmTime(final CallTreeNode node, final JavelinConfig config,
            final boolean judgeHigh)
    {
        boolean isLastAlarmTooNear = false;
        Invocation invocation = node.getInvocation();
        long lastAlarmTime = invocation.getLastAlarmTime();
        long currentTime = System.currentTimeMillis();
        long alarmIntervalThreshold = config.getAlarmMinimumInterval();
        if (judgeHigh == false && currentTime - lastAlarmTime <= alarmIntervalThreshold)
        {
            isLastAlarmTooNear = true;

            // �A���[���폜���b�Z�[�W�́A�A���[�����M�Ԋu�Ɠ����Ԋu�ŏo�͂���B
            // �܂��A�A���[���폜���b�Z�[�W�T�C�Y���A"DEF_BUFFER_SIZE"�𒴂���ꍇ�A
            // �����������񕪂��폜����B
            synchronized (discardBuffer__)
            {
                if (discardBuffer__.length() < DEF_BUFFER_SIZE)
                {
                    discardBuffer__.append(invocation.getClassName());
                    discardBuffer__.append('#');
                    discardBuffer__.append(invocation.getMethodName());
                }
                if (currentTime - lastDiscardTime__ >= alarmIntervalThreshold)
                {
                    String discardMessage = createDiscardMessage(discardBuffer__);
                    SystemLogger.getInstance().warn(discardMessage);
                    discardBuffer__ = new StringBuffer(DEF_BUFFER_SIZE);
                    lastDiscardTime__ = currentTime;
                }
                else
                {
                    if (discardBuffer__.length() < DEF_BUFFER_SIZE)
                    {
                        discardBuffer__.append(", ");
                    }
                }
            }
        }
        else
        {
            invocation.setLastAlarmTime(currentTime);
        }

        return isLastAlarmTooNear;
    }

    private static String createDiscardMessage(StringBuffer discardBuffer)
    {
        String header = "Alarm Discard: ";
        if (discardBuffer.length() >= DEF_BUFFER_SIZE)
        {
            String message = discardBuffer.substring(0, DEF_BUFFER_SIZE - 1) + "...";
            return header + message;
        }
        return header + discardBuffer.toString();
    }

    /**
     * �㏈���i�{�������s���j�B
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param cause ��O�I�u�W�F�N�g
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     */
    public static void postProcess(String className, String methodName, final Throwable cause,
            final JavelinConfig config)
    {
        postProcess(className, methodName, cause, config, true);
    }

    /**
     * �㏈���i�{�������s���j�B
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param cause ��O�I�u�W�F�N�g
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @param doExcludeProcess ���O�Ώۏ������s�����ǂ���
     */
    public static void postProcess(String className, String methodName, final Throwable cause,
            final JavelinConfig config, final boolean doExcludeProcess)
    {
        postProcessCommon(null, cause, config);
    }

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
     * Javelin���O�t�@�C�����o�͂���B
     * @param config �p�����[�^�̐ݒ�l��ۑ�����I�u�W�F�N�g
     * @return Javelin���O�t�@�C��
     */
    public static String dumpJavelinLog(final JavelinConfig config)
    {
        String fileName = "";
        // Javelin���O�t�@�C�����o�͂���B
        JavelinFileGenerator generator = new JavelinFileGenerator(config);

        CallTree callTree = CallTreeRecorder.getInstance().getCallTree();
        if (callTree == null)
        {
            return fileName;
        }

        CallTreeNode root = callTree.getRootNode();
        if (root != null)
        {
            fileName =
                       generator.generateJaveinFile(callTree, recordStrategy__.createCallback(),
                                                    null, 0);
        }
        return fileName;
    }

    /**
     * �g�����U�N�V�������L�^����B
     *
     * @param node CallTreeNode
     */
    public static void recordTransaction(final CallTreeNode node)
    {
        if (node.isRecoreded() == false)
        {
            node.setRecoreded(true);

            Invocation invocation = node.getInvocation();
            if (invocation != null)
            {
                long elapsedTime = node.getAccumulatedTime();
                long elapsedCpuTime = node.getCpuTime();
                long elapsedUserTime = node.getUserTime();

                elapsedTime = elapsedTime - node.getChildrenTime();
                if (elapsedTime < 0)
                {
                    elapsedTime = 0;
                }
                elapsedCpuTime = elapsedCpuTime - node.getChildrenCpuTime();
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
     * Alarm�ʒm����B
     * @param node CallTreeNode
     * @param callTreeRecorder callTreeRecorder
     */
    public static void sendAlarm(final CallTreeNode node, final CallTreeRecorder callTreeRecorder)
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
     * Alarm�ʒm�ɗ��p����AlarmListener��o�^����
     *
     * @param alarmListener Alarm�ʒm�ɗ��p����AlarmListener
     */
    public static void addListener(final AlarmListener alarmListener)
    {
        synchronized (ALARM_LISTENER_LIST)
        {
            ALARM_LISTENER_LIST.add(alarmListener);
        }
    }

    /**
     * �X���b�h��ID��ݒ肷��
     * @param threadId �X���b�hID
     */
    public static void setThreadId(final String threadId)
    {
        CallTree callTree = CallTreeRecorder.getInstance().getCallTree();
        callTree.setThreadID(threadId);
    }

    /**
     * ����������Ă��邩��Ԃ��B
     * @return true:����������Ă���Afalse:����������Ă��Ȃ�.
     */
    public static boolean isInitialized()
    {
        return initialized__;
    }

    /**
     * CallTreeNode�ɐݒ肳�ꂽ����N���X(����D��x�F��)�𗘗p���āA
     * Javelin���O���t�@�C���ɏo�͂��邩�ǂ������肷��B
     *
     * @param node CallTreeNode
     * @return true:�o�͂���Afalse:�o�͂��Ȃ�
     */
    private static JavelinLogCallback createCallback(final CallTree tree, final CallTreeNode node)
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
}
