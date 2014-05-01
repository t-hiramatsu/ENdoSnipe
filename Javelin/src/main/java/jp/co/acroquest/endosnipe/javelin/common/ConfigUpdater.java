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
package jp.co.acroquest.endosnipe.javelin.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.javelin.RootInvocationManager;
import jp.co.acroquest.endosnipe.javelin.converter.linear.monitor.LinearSearchMonitor;
import jp.co.acroquest.endosnipe.javelin.event.JavelinEventCounter;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
/**
 * �����[�g�ݒ�@�\���猻�݂̐ݒ�l���X�V����A�_�v�^
 * 
 * @author kimura
 */
public class ConfigUpdater
{
    /** ���O���x���Ƃ��ċ��e���镶���� */
    private static final Set<String> LOGLEVELS = new HashSet<String>();

    /** �C�x���g���x���Ƃ��ċ��e���镶���� */
    private static final Set<String>                EVENTLEVELS      = new HashSet<String>();

    /** �X�V���Map */
    private static Map<String, ConfigUpdateRequest> updateLaterMap__ =
                                                       new HashMap<String, ConfigUpdateRequest>();

    static
    {
        LOGLEVELS.add("FATAL");
        LOGLEVELS.add("ERROR");
        LOGLEVELS.add("WARN");
        LOGLEVELS.add("INFO");
        LOGLEVELS.add("DEBUG");

        EVENTLEVELS.add("ERROR");
        EVENTLEVELS.add("WARN");
        EVENTLEVELS.add("INFO");

    }

    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private ConfigUpdater()
    {
        // Do Nothing.
    }

    /**
     * �X�V�\�Ȑݒ�l���擾����
     * 
     * @return �X�V�\�Ȑݒ�l��Map
     */
    public static Map<String, String> getUpdatableConfig()
    {
        Map<String, String> properties = new LinkedHashMap<String, String>();

        JavelinConfig config = new JavelinConfig();
        // �Ăяo�������A���[���Ƃ��ďo�͂���ۂ�臒l
        properties.put(JavelinConfig.ALARMTHRESHOLD_KEY, 
                       String.valueOf(config.getAlarmThreshold()));
        // �x���𔭐�������ۂ�CPU���Ԃ�臒l
        properties.put(JavelinConfig.ALARM_CPUTHRESHOLD,
                       String.valueOf(config.getAlarmCpuThreashold()));
        // ��O���A���[���ʒm���邩�̃t���O
        properties.put(JavelinConfig.ALARM_EXCEPTION_KEY, 
                       String.valueOf(config.isAlarmException()));
        // HTTP�X�e�[�^�X�G���[��ʒm���邩�̃t���O
        properties.put(JavelinConfig.HTTP_STATUS_ERROR_KEY,
                       String.valueOf(config.isHttpStatusError()));
        // �������t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_ARGS_KEY, String.valueOf(config.isLogArgs()));
        // �����ڍׂ��o�͂��邩�̃t���O
        properties.put(JavelinConfig.ARGS_DETAIL_KEY, String.valueOf(config.isArgsDetail()));
        // �����ڍ׏o�͎��̐[�x
        properties.put(JavelinConfig.ARGS_DETAIL_DEPTH_KEY,
                       String.valueOf(config.getArgsDetailDepth()));

        // �Ԃ�l���t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_RETURN_KEY, String.valueOf(config.isLogReturn()));
        // �Ԃ�l�ڍׂ��o�͂��邩�̃t���O
        properties.put(JavelinConfig.RETURN_DETAIL_KEY, String.valueOf(config.isReturnDetail()));
        // �Ԃ�l�ڍ׏o�͎��̐[�x
        properties.put(JavelinConfig.RETURN_DETAIL_DEPTH_KEY,
                       String.valueOf(config.getReturnDetailDepth()));

        // �X�^�b�N�g���[�X���t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_STACKTRACE_KEY, String.valueOf(config.isLogStacktrace()));

        // HTTP�Z�b�V�������t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_HTTP_SESSION_KEY,
                       String.valueOf(config.isLogHttpSession()));
        // HTTP�Z�b�V�����ڍׂ��o�͂��邩�̃t���O
        properties.put(JavelinConfig.HTTP_SESSION_DETAIL_KEY,
                       String.valueOf(config.isHttpSessionDetail()));
        // HTTP�Z�b�V�����ڍ׏o�͎��̐[�x
        properties.put(JavelinConfig.HTTP_SESSION_DETAIL_DEPTH_KEY,
                       String.valueOf(config.getHttpSessionDetailDepth()));

        // JMX�����t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_MBEANINFO_KEY, String.valueOf(config.isLogMBeanInfo()));
        // JMX�����t�@�C���ɏo�͂��邩�̃t���O
        properties.put(JavelinConfig.LOG_MBEANINFO_ROOT_KEY,
                       String.valueOf(config.isLogMBeanInfoRoot()));
        // �C�x���g���x��
        properties.put(JavelinConfig.EVENT_LEVEL_KEY, config.getEventLevel());

        // �X���b�h���f����`
        properties.put(JavelinConfig.THREADMODEL_KEY, String.valueOf(config.getThreadModel()));
        // Collection�̃��������[�N���o���s�����ǂ���
        properties.put(JavelinConfig.COLLECTION_MONITOR,
                       String.valueOf(config.isCollectionMonitor()));
        // ���������[�N臒l
        properties.put(JavelinConfig.COLLECTION_SIZE_THRESHOLD,
                       String.valueOf(config.getCollectionSizeThreshold())); // ���������[�N臒l
        // ���������[�N���o���A���[�N���N�������R���N�V�����̃I�u�W�F�N�g�T�C�Y���o�͂��邩�̃t���O
        properties.put(JavelinConfig.LEAK_COLLECTIONSIZE_OUT,
                       String.valueOf(config.isLeakCollectionSizePrint()));
        // �N���X�q�X�g�O�������擾���邩�ǂ����@
        properties.put(JavelinConfig.CLASS_HISTO, String.valueOf(config.getClassHisto()));
        // �N���X�q�X�g�O�����擾�Ԋu(�~���b) 
        properties.put(JavelinConfig.CLASS_HISTO_INTERVAL,
                       String.valueOf(config.getClassHistoInterval()));
        // �N���X�q�X�g�O�����̏�ʉ������擾���邩 �@
        properties.put(JavelinConfig.CLASS_HISTO_MAX, String.valueOf(config.getClassHistoMax()));
        // �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ���
        properties.put(JavelinConfig.CLASS_HISTO_GC, String.valueOf(config.getClassHistoGC()));
        // ���`�������s�����ǂ���
        properties.put(JavelinConfig.LINEARSEARCH_ENABLED_KEY,
                       String.valueOf(config.isLinearSearchMonitor()));
        // ���`�������o���s�����X�g�T�C�Y��臒l
        properties.put(JavelinConfig.LINEARSEARCH_SIZE,
                       String.valueOf(config.getLinearSearchListSize()));
        // ���`�������o���s�����`�A�N�Z�X�񐔂̊�����臒l
        properties.put(JavelinConfig.LINEARSEARCH_RATIO,
                       String.valueOf(config.getLinearSearchListRatio()));
        // �l�b�g���[�N���͗ʂ��擾���邩
        properties.put(JavelinConfig.NET_INPUT_MONITOR, String.valueOf(config.isNetInputMonitor()));
        // �l�b�g���[�N�o�͗ʂ��擾���邩
        properties.put(JavelinConfig.NET_OUTPUT_MONITOR,
                       String.valueOf(config.isNetOutputMonitor()));
        // �t�@�C�����͗ʂ��擾���邩
        properties.put(JavelinConfig.FILE_INPUT_MONITOR,
                       String.valueOf(config.isFileInputMonitor()));
        // �t�@�C���o�͗ʂ��擾���邩
        properties.put(JavelinConfig.FILE_OUTPUT_MONITOR,
                       String.valueOf(config.isFileOutputMonitor()));
        // �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩
        properties.put(JavelinConfig.FINALIZATION_COUNT_MONITOR,
                       String.valueOf(config.isFinalizationCount()));
        // ���\�b�h�Ăяo���Ԋu���߂��Ď����邩
        properties.put(JavelinConfig.INTERVAL_ERROR_MONITOR,
                       String.valueOf(config.isIntervalMonitor()));
        // �X���b�h�Ď����s�����ǂ���
        properties.put(JavelinConfig.THREAD_MONITOR, String.valueOf(config.getThreadMonitor()));
        // �X���b�h�Ď����s������(�~���b)
        properties.put(JavelinConfig.THREAD_MONITOR_INTERVAL,
                       String.valueOf(config.getThreadMonitorInterval()));
        // �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[��
        properties.put(JavelinConfig.THREAD_MONITOR_DEPTH,
                       String.valueOf(config.getThreadMonitorDepth()));
        // �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�񐔂�臒l
        properties.put(JavelinConfig.THREAD_BLOCK_THRESHOLD,
                       String.valueOf(config.getBlockThreshold()));
        // �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l
        properties.put(JavelinConfig.THREAD_BLOCKTIME_THRESHOLD,
                       String.valueOf(config.getBlockTimeThreshold()));
        // �t���X���b�h�_���v���o�͂��邩�ǂ���
        properties.put(JavelinConfig.THREAD_DUMP_MONITOR, String.valueOf(config.isThreadDump()));
        // �t���X���b�h�_���v�o�͔�����s������(�~���b)
        properties.put(JavelinConfig.THREAD_DUMP_INTERVAL,
                       String.valueOf(config.getThreadDumpInterval()));
        // �t���X���b�h�_���v�o�͂̃X���b�h����臒l
        properties.put(JavelinConfig.THREAD_DUMP_THREAD,
                       String.valueOf(config.getThreadDumpThreadNum()));
        // �t���X���b�h�_���v�o�͂�CPU�g�p����臒l
        properties.put(JavelinConfig.THREAD_DUMP_CPU, String.valueOf(config.getThreadDumpCpu()));
        // �t��GC�����o���邩�ǂ���
        properties.put(JavelinConfig.FULLGC_MONITOR, String.valueOf(config.isFullGCMonitor()));
        // �t��GC���o��GC���Ԃ�臒l
        properties.put(JavelinConfig.FULLGC_THREASHOLD, 
                       String.valueOf(config.getFullGCThreshold()));
        // Java6�ȍ~�Ńf�b�h���b�N�Ď����s�����ǂ���
        properties.put(JavelinConfig.THREAD_DEADLOCK_MONITOR,
                       String.valueOf(config.isDeadLockMonitor()));
        // �A���[�����M�Ԋu�̍ŏ��l
        properties.put(JavelinConfig.ALARM_MINIMUM_INTERVAL_KEY,
                       String.valueOf(config.getAlarmMinimumInterval()));
        // Turn Around Time���v�����邩�ǂ����B
        properties.put(JavelinConfig.TAT_ENABLED_KEY, String.valueOf(config.isTatEnabled()));
        // Turn Around Time�̕ێ����ԁB
        properties.put(JavelinConfig.TAT_KEEP_TIME_KEY, String.valueOf(config.getTatKeepTime()));
        // HttpSession�̃C���X�^���X�����擾���邩
        properties.put(JavelinConfig.HTTP_SESSION_COUNT_MONITOR,
                       String.valueOf(config.isHttpSessionCount()));
        // HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���擾���邩
        properties.put(JavelinConfig.HTTP_SESSION_SIZE_MONITOR,
                       String.valueOf(config.isHttpSessionSize()));
        // �����X���b�h�A�N�Z�X���Ď����邩�ǂ����B
        properties.put(JavelinConfig.CONCURRENT_ENABLED_KEY,
                       String.valueOf(config.isConcurrentAccessMonitored()));
        // �^�C���A�E�g�l�ݒ�̊Ď����s�����ǂ����B
        properties.put(JavelinConfig.TIMEOUT_MONITOR, String.valueOf(config.isTimeoutMonitor()));
        // Log4j�̃X�^�b�N�g���[�X���s�����O���x����臒l
        properties.put(JavelinConfig.LOG4J_PRINTSTACK_LEVEL, config.getLog4jPrintStackLevel());
        // CallTree�T�C�Y�̍ő�l�B
        properties.put(JavelinConfig.CALL_TREE_ENABLE_KEY,
                       String.valueOf(config.isCallTreeEnabled()));
        // CallTree�T�C�Y�̍ő�l�B
        properties.put(JavelinConfig.CALL_TREE_MAX_KEY, String.valueOf(config.getCallTreeMax()));
        // Jvn���O�t�@�C�����o�͂��邩�ǂ����B
        properties.put(JavelinConfig.LOG_JVN_FILE, String.valueOf(config.isLogJvnFile()));
        // �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����B
        properties.put(JavelinConfig.COLLECT_SYSTEM_RESOURCES,
                       String.valueOf(config.getCollectSystemResources()));
        // InvocationFullEvent�𑗐M���邩�ǂ����B
        properties.put(JavelinConfig.SEND_INVOCATION_FULL_EVENT,
                       String.valueOf(config.getSendInvocationFullEvent()));

        /** JdbcJavelinConfig����擾�\�Ȑݒ���擾���� */
        JdbcJavelinConfig jdbcConfig = new JdbcJavelinConfig();

        // JDBCJavelin��L���ɂ��邩�ǂ���
        properties.put(JdbcJavelinConfig.JDBC_JAVELIN_ENABLED_KEY,
                       String.valueOf(jdbcConfig.isJdbcJavelinEnabled()));
        // ���s�v��擾�t���O
        properties.put(JdbcJavelinConfig.RECORDEXECPLAN_KEY,
                       String.valueOf(jdbcConfig.isRecordExecPlan()));
        // SQL�̎��s�v����L�^����ۂ�臒l
        properties.put(JdbcJavelinConfig.EXECPLANTHRESHOLD_KEY,
                       String.valueOf(jdbcConfig.getExecPlanThreshold()));
        // JDBC�ďo���d���o�̓t���O
        properties.put(JdbcJavelinConfig.RECORDDUPLJDBCCALL_KEY,
                       String.valueOf(jdbcConfig.isRecordDuplJdbcCall()));
        // �o�C���h�ϐ��o�̓t���O
        properties.put(JdbcJavelinConfig.RECORDBINDVAL_KEY,
                       String.valueOf(jdbcConfig.isRecordBindVal()));
        // �o�C���h�ϐ��o�͂ɂ����镶���񒷐���
        properties.put(JdbcJavelinConfig.STRINGLIMITLENGTH_KEY,
                       String.valueOf(jdbcConfig.getJdbcStringLimitLength()));
        // ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ�
        properties.put(JdbcJavelinConfig.SQLCOUNT_MONITOR_KEY,
                       String.valueOf(jdbcConfig.isSqlcountMonitor()));
        // ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l
        properties.put(JdbcJavelinConfig.SQLCOUNT_KEY, String.valueOf(jdbcConfig.getSqlcount()));
        // Oracle�ɑ΂���SQL�g���[�X�̏o�͎w���t���O
        properties.put(JdbcJavelinConfig.ORACLE_ALLOW_SQL_TRACE_KEY,
                       String.valueOf(jdbcConfig.isAllowSqlTraceForOracle()));
        // PostgreSQL�ɑ΂�����s�v��ڍ׎擾�t���O
        properties.put(JdbcJavelinConfig.POSTGRES_VERBOSE_PLAN_KEY,
                       String.valueOf(jdbcConfig.isVerbosePlanForPostgres()));
        // JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邽�߂̃t���O
        properties.put(JdbcJavelinConfig.RECORD_STACKTRACE_KEY,
                       String.valueOf(jdbcConfig.isRecordStackTrace()));
        // JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邽�߂�SQL���s���Ԃ�臒l
        properties.put(JdbcJavelinConfig.RECORD_STACKTRACE_THREADHOLD_KEY,
                       String.valueOf(jdbcConfig.getRecordStackTraceThreshold()));

        return properties;
    }

    /**
     * �Ăяo�������L�^����ۂ�臒l���X�V����
     * 
     * @param alarmThreshold 臒l�i�~���b�j
     */
    public static void updateAlarmThreshold(final long alarmThreshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setAlarmThreshold(alarmThreshold);
    }

    /**
     * ���O���擾����ۂ�CPU���Ԃ�臒l���X�V����
     * 
     * @param alarmCpuThreshold CPU���Ԃ�臒l�i�~���b�j
     */
    public static void updateAlarmCpuThreshold(final long alarmCpuThreshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setAlarmCpuThreashold(alarmCpuThreshold);
    }

    /**
     * �X�^�b�N�g���[�X���t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogStacktrace �X�^�b�N�g���[�X���t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogStacktrace(final boolean isLogStacktrace)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogStacktrace(isLogStacktrace);
    }

    /**
     * �A�v���P�[�V������O���ɃA���[���ʒm���邩�̃t���O���X�V����
     * 
     * @param isAlarmException �A���[���ʒm���邩�̃t���O
     */
    public static void updateAlarmException(final boolean isAlarmException)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setAlarmException(isAlarmException);
    }

    /**
     * HTTP�X�e�[�^�X�G���[�������ɃA���[���ʒm���邩�̃t���O���X�V����
     * 
     * @param isHttpStatusError �A���[���ʒm���邩�̃t���O
     */
    public static void updateHttpStatusError(final boolean isHttpStatusError)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setHttpStatusError(isHttpStatusError);
    }

    /**
     * �������t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogArgs �������t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogArgs(final boolean isLogArgs)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogArgs(isLogArgs);
    }

    /**
     * �Ԃ�l���t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogReturn �Ԃ�l���t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogReturn(final boolean isLogReturn)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogReturn(isLogReturn);
    }

    /**
     * �������t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogArgs �������t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogHttpSession(final boolean isLogArgs)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogHttpSession(isLogArgs);
    }

    /**
     * MBean�����t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogMBeanInfo MBean�����t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogMBeanInfo(final boolean isLogMBeanInfo)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogMBeanInfo(isLogMBeanInfo);
    }

    /**
     * MBean�����t�@�C���ɏo�͂��邩�̃t���O���X�V����
     * 
     * @param isLogMBeanInfo MBean�����t�@�C���ɏo�͂��邩�̃t���O
     */
    public static void updateLogMBeanInfoRoot(final boolean isLogMBeanInfo)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setLogMBeanInfoRoot(isLogMBeanInfo);
    }

    /**
     * �����ڍׂ��o�͂��邩�̃t���O���X�V����
     * 
     * @param isArgsDetail �����ڍׂ��o�͂��邩�̃t���O
     */
    public static void updateArgsDetail(final boolean isArgsDetail)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setArgsDetail(isArgsDetail);
    }

    /**
     * �����ڍ׏o�͎��̐[�x���X�V����
     * 
     * @param argsDetailDepth �����ڍ׏o�͎��̐[�x
     */
    public static void updateArgsDetailDepth(final int argsDetailDepth)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setArgsDetailDepth(argsDetailDepth);
    }

    /**
     * �Ԃ�l�ڍׂ��o�͂��邩�̃t���O���X�V����
     * 
     * @param isReturnDetail �Ԃ�l�ڍׂ��o�͂��邩�̃t���O
     */
    public static void updateReturnDetail(final boolean isReturnDetail)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setReturnDetail(isReturnDetail);
    }

    /**
     * �Ԃ�l�ڍ׏o�͎��̐[�x
     * 
     * @param returnDetailDepth �Ԃ�l�ڍ׏o�͎��̐[�x
     */
    public static void updateReturnDetailDepth(final int returnDetailDepth)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setReturnDetailDepth(returnDetailDepth);
    }

    /**
     * HTTP�Z�b�V�����ڍׂ��o�͂��邩�̃t���O���X�V����
     * 
     * @param isHttpSessionDetail HTTP�Z�b�V�����ڍׂ��o�͂��邩�̃t���O
     */
    public static void updateHttpSessionDetail(final boolean isHttpSessionDetail)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setHttpSessionDetail(isHttpSessionDetail);
    }

    /**
     * HTTP�Z�b�V�����ڍ׏o�͎��̐[�x���X�V����
     * 
     * @param httpSessionDetailDepth HTTP�Z�b�V�����ڍ׏o�͎��̐[�x
     */
    public static void updateHttpSessionDetailDepth(final int httpSessionDetailDepth)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setHttpSessionDetailDepth(httpSessionDetailDepth);
    }

    /**
     * �X���b�h���f����`���X�V����
     * 
     * @param threadModel �X���b�h���f����`
     */
    public static void updateThreadModel(final int threadModel)
    {
        JavelinConfig javelinConfig = new JavelinConfig();
        javelinConfig.setThreadModel(threadModel);
    }

    /**
     * JDBC Javelin�̗L��/�����t���O���X�V����
     * 
     * @param isJdbcEnabled JDBC Javelin�̗L��/�����t���O
     */
    public static void updateJdbcEnabled(final boolean isJdbcEnabled)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setJdbcJavelinEnabled(isJdbcEnabled);
    }

    /**
     * ���s�v��擾�t���O���X�V����
     * 
     * @param isRecordExecPlan SQL�̎��s�v����L�^����ۂ�臒l
     */
    public static void updateRecordExecPlan(final boolean isRecordExecPlan)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setRecordExecPlan(isRecordExecPlan);
    }

    /**
     * SQL�̎��s�v����L�^����ۂ�臒l���X�V����
     * 
     * @param execPlanThreshold SQL�̎��s�v����L�^����ۂ�臒l
     */
    public static void updateExecPlanThreshold(final long execPlanThreshold)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setExecPlanThreshold(execPlanThreshold);
    }

    /**
     * JDBC�ďo���d���o�̓t���O���X�V����
     * 
     * @param isRecordDuplJdbcCall JDBC�ďo���d���o�̓t���O
     */
    public static void updateRecordDuplJdbcCall(final boolean isRecordDuplJdbcCall)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setRecordDuplJdbcCall(isRecordDuplJdbcCall);
    }

    /**
     * �o�C���h�ϐ��o�̓t���O���X�V����
     * 
     * @param isRecordBindVal �o�C���h�ϐ��o�̓t���O
     */
    public static void updateRecordBindVal(final boolean isRecordBindVal)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setRecordBindVal(isRecordBindVal);
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ����X�V����
     * 
     * @param isSqlcountMonitor ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ�
     */
    public static void updateSqlcountMonitor(final boolean isSqlcountMonitor)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setSqlcountMonitor(isSqlcountMonitor);
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���X�V����
     * 
     * @param sqlCount ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l
     */
    public static void updateSqlcount(final long sqlCount)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setSqlcount(sqlCount);
    }

    /**
     * �o�C���h�ϐ��o�͂ɂ����镶���񒷐������X�V����
     * 
     * @param jdbcStringLimitLength �o�C���h�ϐ��o�͂ɂ����镶���񒷐���
     */
    public static void updateJdbcStringLimitLength(final long jdbcStringLimitLength)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setJdbcStringLimitLength(jdbcStringLimitLength);
    }

    /**
     * Oracle�ɑ΂���SQL�g���[�X�̏o�͎w���t���O���X�V����
     * 
     * @param isAllowSqlTraceForOracle Oracle�ɑ΂���SQL�g���[�X�̏o�͎w���t���O
     */
    public static void updateAllowSqlTraceForOracle(final boolean isAllowSqlTraceForOracle)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setAllowSqlTraceForOracle(isAllowSqlTraceForOracle);
    }

    /**
     * PostgreSQL�ɑ΂�����s�v��ڍ׎擾�t���O���X�V����
     * 
     * @param isVerbosePlanForPostgres PostgreSQL�ɑ΂�����s�v��ڍ׎擾�t���O
     */
    public static void updateVerbosePlanForPostgres(final boolean isVerbosePlanForPostgres)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setVerbosePlanForPostgres(isVerbosePlanForPostgres);
    }

    /**
     * JDBCJavelin�X�^�b�N�g���[�X�擾�t���O���X�V����
     * 
     * @param recordStackTrace JDBCJavelin�X�^�b�N�g���[�X�擾�t���O
     */
    public static void updateRecordStackTrace(final boolean recordStackTrace)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setRecordStackTrace(recordStackTrace);
    }

    /**
     * JDBCJavelin�X�^�b�N�g���[�X�擾�t���O���X�V����
     * 
     * @param stackTraceThreshold JDBCJavelin�ŃX�^�b�N�g���[�X���擾���邽�߂�臒l
     */
    public static void updateRecordStackTraceThreshold(final int stackTraceThreshold)
    {
        JdbcJavelinConfig jdbcJavelinConfig = new JdbcJavelinConfig();
        jdbcJavelinConfig.setRecordStackTraceThreshold(stackTraceThreshold);
    }

    /**
     * JDBC Javelin���g�p���邩�ǂ�����ݒ肷��B
     * 
     * @param isJdbcJavelinEnabled JDBC Javelin���g�p���邩�ǂ���
     */
    public static void updateJdbcJavelinEnabled(final boolean isJdbcJavelinEnabled)
    {
        JdbcJavelinConfig config = new JdbcJavelinConfig();
        config.setJdbcJavelinEnabled(isJdbcJavelinEnabled);
    }

    /**
     * Collection�̃��������[�N���o���s�����ǂ������X�V���܂��B<br />
     * 
     * @param collectionMonitor Collection�̃��������[�N���o���s���ꍇ�A<code>true</code>
     */
    public static void updateCollectionMonitor(final boolean collectionMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setCollectionMonitor(collectionMonitor);
    }

    /**
     * �ۑ�����R���N�V�����I�u�W�F�N�g�̃T�C�Y��臒l���X�V����B
     * 
     * @param collectionSizeThreshold �ۑ�����R���N�V�����I�u�W�F�N�g�̃T�C�Y
     */
    public static void updateCollectionSizesThreshold(final int collectionSizeThreshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setCollectionSizeThreshold(collectionSizeThreshold);
    }

    /**
     * �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ�����ݒ肷��B
     * 
     * @param classHistoGC �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ����B
     */
    public static void updateClassHistoGC(final boolean classHistoGC)
    {
        JavelinConfig config = new JavelinConfig();
        config.setClassHistoGC(classHistoGC);
    }

    /**
     * �N���X�q�X�g�O�����擾�Ԋu(�~���b)��ݒ肷��B
     * 
     * @param classHistoInterval �N���X�q�X�g�O�����擾�Ԋu(�~���b)�B
     */
    public static void updateClassHistoInterval(final int classHistoInterval)
    {
        JavelinConfig config = new JavelinConfig();
        config.setClassHistoInterval(classHistoInterval);
    }

    /**
     * �N���X�q�X�g�O�����̏�ʉ������擾���邩��ݒ肷��B
     * 
     * @param classHistoMax �N���X�q�X�g�O�����̏�ʉ������擾���邩�B
     */
    public static void updateClassHistoMax(final int classHistoMax)
    {
        JavelinConfig config = new JavelinConfig();
        config.setClassHistoMax(classHistoMax);
    }

    /**
     * �N���X�q�X�g�O�������擾���邩�ǂ�����ݒ肷��B
     * 
     * @param classHisto �N���X�q�X�g�O�������擾���邩�ǂ����B
     */
    public static void updateClassHisto(final boolean classHisto)
    {
        JavelinConfig config = new JavelinConfig();
        config.setClassHisto(classHisto);
    }

    /**
     * ���`�������o���s�����ǂ�����ݒ肵�܂��B<br />
     * 
     * @param isLinearSearchMonitor ���`�������o���s�����ǂ���
     */
    public static void updateLinearSearchMonitor(final boolean isLinearSearchMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setLinearSearchMonitor(isLinearSearchMonitor);
    }

    /**
     * ���`�����ΏۂƂȂ郊�X�g�T�C�Y��臒l��ݒ肵�܂��B
     * 
     * @param size ���`�����ΏۂƂȂ郊�X�g�T�C�Y��臒l
     */
    public static void updateLinearSearchSize(final int size)
    {
        JavelinConfig config = new JavelinConfig();
        config.setLinearSearchListSize(size);
    }

    /**
     * ���X�g�T�C�Y�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l��ݒ肵�܂��B
     * 
     * @param ratio ���X�g�T�C�Y�ɑ΂�����`�A�N�Z�X�񐔂̊���
     */
    public static void updateLinearSearchRatio(final double ratio)
    {
        JavelinConfig config = new JavelinConfig();
        config.setLinearSearchListRatio(ratio);
    }

    /**
     * �l�b�g���[�N���͗ʂ��擾���邩��ݒ肷��B
     * 
     * @param isNetInputMonitor �N���X�q�X�g�O�����̏�ʉ������擾���邩�B
     */
    public static void updateNetInputMonitor(final boolean isNetInputMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setNetInputMonitor(isNetInputMonitor);
    }

    /**
     * �l�b�g���[�N�o�͗ʂ��擾���邩��ݒ肷��
     * 
     * @param isNetOutputMonitor �l�b�g���[�N�o�͗ʂ��擾���邩
     */
    public static void updateNetOutputMonitor(final boolean isNetOutputMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setNetOutputMonitor(isNetOutputMonitor);
    }

    /**
     * �t�@�C�����͗ʂ��擾���邩��ݒ肷��
     * 
     * @param isFileInputMonitor �t�@�C�����͗ʂ��擾���邩
     */
    public static void updateFileInputMonitor(final boolean isFileInputMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setFileInputMonitor(isFileInputMonitor);
    }

    /**
     * �t�@�C���o�͗ʂ��擾���邩��ݒ肷��
     * 
     * @param isFileOutputMonitor �t�@�C���o�͗ʂ��擾���邩
     */
    public static void updateFileOutputMonitor(final boolean isFileOutputMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setFileOutputMonitor(isFileOutputMonitor);
    }

    /**
     * �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩��ݒ肷��
     * 
     * @param isFinalizationCount �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩
     */
    public static void updateFinalizationCount(final boolean isFinalizationCount)
    {
        JavelinConfig config = new JavelinConfig();
        config.setFinalizationCount(isFinalizationCount);
    }

    /**
     * ���\�b�h�Ăяo���Ԋu���߂��Ď����邩��ݒ肷��
     * 
     * @param isIntervalMonitor ���\�b�h�Ăяo���Ԋu���߂��Ď����邩
     */
    public static void updateIntervalMonitor(final boolean isIntervalMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setIntervalMonitor(isIntervalMonitor);
    }

    /**
     * HttpSession�̃C���X�^���X�����Ď����邩��ݒ肷��
     * 
     * @param isHttpSessionCount HttpSession�̃C���X�^���X�����Ď����邩
     */
    public static void updateHttpSessionCount(final boolean isHttpSessionCount)
    {
        JavelinConfig config = new JavelinConfig();
        config.setHttpSessionCount(isHttpSessionCount);
    }

    /**
     * HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩��ݒ肷��
     * 
     * @param isHttpSessionSize HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩
     */
    public static void updateHttpSessionSize(final boolean isHttpSessionSize)
    {
        JavelinConfig config = new JavelinConfig();
        config.setHttpSessionSize(isHttpSessionSize);
    }

    /**
     * �X���b�h�Ď����s�����ǂ�����ݒ肷��B
     * 
     * @param threadMonitor �X���b�h�Ď����s���ꍇ��true�B
     */
    public static void updateThreadMonitor(final boolean threadMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadMonitor(threadMonitor);
    }

    /**
     * �X���b�h�Ď����s���Ԋu(�~���b)��ݒ肷��B
     * 
     * @param threadMonitorInterval �X���b�h�Ď����s���Ԋu(�~���b)�B
     */
    public static void updateThreadMonitorInterval(final long threadMonitorInterval)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadMonitorInterval(threadMonitorInterval);
    }

    /**
     * �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[�����擾����B
     * 
     * @param threadMonitorDepth �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[���B
     */
    public static void updateThreadMonitorDepth(final int threadMonitorDepth)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadMonitorDepth(threadMonitorDepth);
    }

    /**
     * �u���b�N�񐔂��������邩�ǂ�����臒l���X�V����B
     * 
     * @param blockThreshold �u���b�N�񐔂��������邩�ǂ�����臒l�B
     */
    public static void updateBlockThreshold(final long blockThreshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setBlockThreshold(blockThreshold);
    }

    /**
     * �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l���X�V����B
     * 
     * @param blockTimeThreshold �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l�B
     */
    public static void updateBlockTimeThreshold(final long blockTimeThreshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setBlockTimeThreshold(blockTimeThreshold);
    }

    /**
     * �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐����X�V����B
     * 
     * @param blockThreadInfoNum �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐��B
     */
    public static void updateBlockThreadInfoNum(final int blockThreadInfoNum)
    {
        JavelinConfig config = new JavelinConfig();
        config.setBlockThreadInfoNum(blockThreadInfoNum);
    }

    /**
     * �t���X���b�h�_���v�̏o�͂��s�����ǂ������X�V����B
     * 
     * @param threadDumpMonitor �t���X���b�h�_���v���o�͂��邩�ǂ���
     */
    public static void updateThreadDumpMonitor(final boolean threadDumpMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadDumpMonitor(threadDumpMonitor);
    }

    /**
     * �t���X���b�h�_���v�o�͔���̊Ԋu���X�V���܂��B
     * 
     * @param threadDumpInterval �t���X���b�h�_���v�o�͔���̊Ԋu
     */
    public static void updateThreadDumpInterval(final int threadDumpInterval)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadDumpInterval(threadDumpInterval);
    }

    /**
     * �t���X���b�h�_���v�o�͂̃X���b�h����臒l���X�V���܂��B
     * 
     * @param threadDumpNum �t���X���b�h�_���v�o�͂̃X���b�h����臒l
     */
    public static void updateThreadDumpNum(final int threadDumpNum)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadDumpThreadNum(threadDumpNum);
    }

    /**
     * �t���X���b�h�_���v�o�͂�CPU�g�p����臒l���X�V���܂��B
     * 
     * @param threadDumpCpu �t���X���b�h�_���v�o�͂�CPU�g�p����臒l
     */
    public static void updateThreadDumpCpu(final int threadDumpCpu)
    {
        JavelinConfig config = new JavelinConfig();
        config.setThreadDumpCpu(threadDumpCpu);
    }

    /**
     * �t��GC�̌��o���s�����ǂ������X�V����B
     * 
     * @param fullGCMonitor �t��GC�̌��o���s�����ǂ���
     */
    public static void updateFullGCMonitor(final boolean fullGCMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setFullGCMonitor(fullGCMonitor);
    }

    /**
     * �t��GC���s���Ԃ�臒l���X�V����B
     * 
     * @param threshold �t��GC���s���Ԃ�臒l
     */
    public static void updateFullGCThreshold(final int threshold)
    {
        JavelinConfig config = new JavelinConfig();
        config.setFullGCThreshold(threshold);
    }

    /**
     * Java6�ȍ~�Ńf�b�h���b�N�Ď����s�����ǂ������X�V����B
     * 
     * @param deadLockMonitor Java6�ȍ~�Ńf�b�h���b�N�Ď����s�����ǂ���
     */
    public static void updateDeadLockMonitor(final boolean deadLockMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setDeadLockMonitor(deadLockMonitor);
    }

    /**
     * �A���[�����M�Ԋu�̍ŏ��l��ݒ肷��B
     * 
     * �O��A���[�����M�EJavelin���O�o�͂��s�����ۂ��� �o�߂������Ԃ����̒l�𒴂��Ă����ꍇ�̂݁A�A���[�����M�EJavelin���O�o�͂��s���B
     * 
     * @param alarmMinimumInterval 臒l�B
     */
    public static void updateAlarmMinimumInterval(final long alarmMinimumInterval)
    {
        JavelinConfig config = new JavelinConfig();
        config.setAlarmMinimumInterval(alarmMinimumInterval);
    }

    /**
     * Turn Around Time���v�����邩�ǂ������X�V����B
     * 
     * @param tatEnabled Turn Around Time���v������ꍇ��true�B
     */
    public static void updateTatEnabled(final boolean tatEnabled)
    {
        JavelinConfig config = new JavelinConfig();
        config.setTatEnabled(tatEnabled);
        RootInvocationManager.setTatEnabled(tatEnabled);
    }

    /**
     * Turn Around Time�̕ێ����Ԃ��X�V����B
     * 
     * @param tatKeepTime Turn Around Time�̕ێ�����
     */
    public static void updateTatKeepTime(final long tatKeepTime)
    {
        JavelinConfig config = new JavelinConfig();
        config.setTatKeepTime(tatKeepTime);
        RootInvocationManager.setTatKeepTime(tatKeepTime);
        JavelinEventCounter.getInstance().setPoolStorePeriod(tatKeepTime);
    }

    /**
     * �����X���b�h�A�N�Z�X���Ď����邩�ǂ������X�V���܂��B<br />
     * 
     * @param concurrentMonitored �����X���b�h�A�N�Z�X���Ď�����ꍇ��true�B
     */
    public static void updateConcurrentAccessMonitor(final boolean concurrentMonitored)
    {
        JavelinConfig config = new JavelinConfig();
        config.setConcurrentAccessMonitored(concurrentMonitored);
    }

    /**
     * �^�C���A�E�g�l�̐ݒ肪�s���Ă��邩�ǂ������X�V���܂��B<br />
     * 
     * @param timeoutMonitor �^�C���A�E�g�l�̐ݒ�̊Ď����s���ꍇ�A<code>true</code>
     */
    public static void updateTimeoutMonitor(final boolean timeoutMonitor)
    {
        JavelinConfig config = new JavelinConfig();
        config.setTimeoutMonitor(timeoutMonitor);
    }

    /**
     * �R�[���c���[�̗L��/�����t���O���X�V���܂��B
     * @param isCallTreeEnabled �R�[���c���[�̗L��/�����t���O
     */
    public static void updateCallTreeEnabled(final boolean isCallTreeEnabled)
    {
        JavelinConfig config = new JavelinConfig();
        config.setCallTreeEnabled(isCallTreeEnabled);
    }

    /**
     * CallTree�T�C�Y�̍ő�l���X�V���܂��B<br />
     * 
     * @param callTreeMax CallTree�T�C�Y�̍ő�l
     */
    public static void updateCallTreeMaxSize(final int callTreeMax)
    {
        JavelinConfig config = new JavelinConfig();
        config.setCallTreeMax(callTreeMax);
    }

    /**
     * Jvn���O�t�@�C�����o�͂��邩�ǂ������X�V���܂��B
     * @param logJvnFile ���O�t�@�C�����o�͂��邩�ǂ���
     */
    public static void updateLogJvnFile(final boolean logJvnFile)
    {
        JavelinConfig config = new JavelinConfig();
        config.setLogJvnFile(logJvnFile);
    }

    /**
     * ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ������X�V���܂��B<br />
     * @param leakCollectionSizePrint ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ���
     */
    public static void updateLeakCollectionSizePrint(final boolean leakCollectionSizePrint)
    {
        JavelinConfig config = new JavelinConfig();
        config.setLeakCollectionSizePrint(leakCollectionSizePrint);
    }

    /**
     * Log4j�̃X�^�b�N�g���[�X���s�����O���x����臒l���X�V���܂��B<br />
     * 
     * @param log4jPrintStackLevel Log4j�̃X�^�b�N�g���[�X���s�����O���x����臒l
     */
    public static void updateLog4jPrintStackLevel(final String log4jPrintStackLevel)
    {
        String log4jLevelToUpper = log4jPrintStackLevel.toUpperCase();
        if (LOGLEVELS.contains(log4jLevelToUpper) == false)
        {
            return;
        }
        JavelinConfig config = new JavelinConfig();
        config.setLog4jPrintStackLevel(log4jLevelToUpper);
    }

    /**
     * Javelin�̃C�x���g���x�����X�V���܂��B<br />
     * 
     * @param eventLevel Javelin�̃C�x���g���x�� 
     */
    public static void updateEventLevel(final String eventLevel)
    {
        String eventLevelToUpper = eventLevel.toUpperCase();
        if (LOGLEVELS.contains(eventLevelToUpper) == false)
        {
            return;
        }
        JavelinConfig config = new JavelinConfig();
        config.setEventLevel(eventLevelToUpper);
    }

    /**
     * �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����̐ݒ���X�V���܂��B<br />
     * 
     * @param collectSystemResources �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ���
     */
    public static void updateCollectSystemResources(final boolean collectSystemResources)
    {
        JavelinConfig config = new JavelinConfig();
        config.setCollectSystemResources(collectSystemResources);
    }

    /**
     * InvocationFullEvent�𑗐M���邩�ǂ����̐ݒ���X�V���܂��B<br />
     * 
     * @param sendInvocationFullEvent InvocationFullEvent�𑗐M���邩�ǂ���
     */
    public static void updateSendInvocationFullEvent(final boolean sendInvocationFullEvent)
    {
        JavelinConfig config = new JavelinConfig();
        config.setSendInvocationFullEvent(sendInvocationFullEvent);
    }

    /**
     * �w�肵�����Ԍ�ɐݒ���X�V����B
     * 
     * @param key �L�[
     * @param value �l
     * @param delay ����(ms)
     */
    public static void updateLater(final String key, final String value, long delay)
    {
        long updateTime = System.currentTimeMillis() + delay;
        synchronized (updateLaterMap__)
        {
            updateLaterMap__.put(key, new ConfigUpdateRequest(key, value, updateTime));
        }
    }

    /**
     * �ݒ���X�V����B
     * 
     * @param key �L�[
     * @param value �l
     */
    public static void update(String key, String value)
    {
        // JavelinConfig�����ݒ�̍X�V
        // JavelinConfig�����ݒ�̍X�V
        if (JavelinConfig.ALARMTHRESHOLD_KEY.equals(key))
        {
            ConfigUpdater.updateAlarmThreshold(Long.parseLong(value));
        }
        else if (JavelinConfig.LOG_STACKTRACE_KEY.equals(key))
        {
            ConfigUpdater.updateLogStacktrace(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.ALARM_EXCEPTION_KEY.equals(key))
        {
            ConfigUpdater.updateAlarmException(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LOG_ARGS_KEY.equals(key))
        {
            ConfigUpdater.updateLogArgs(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LOG_HTTP_SESSION_KEY.equals(key))
        {
            ConfigUpdater.updateLogHttpSession(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LOG_RETURN_KEY.equals(key))
        {
            ConfigUpdater.updateLogReturn(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LOG_MBEANINFO_KEY.equals(key))
        {
            ConfigUpdater.updateLogMBeanInfo(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LOG_MBEANINFO_ROOT_KEY.equals(key))
        {
            ConfigUpdater.updateLogMBeanInfoRoot(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.EVENT_LEVEL_KEY.equals(key))
        {
            ConfigUpdater.updateEventLevel(value);
        }
        else if (JavelinConfig.ARGS_DETAIL_KEY.equals(key))
        {
            ConfigUpdater.updateArgsDetail(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.ARGS_DETAIL_DEPTH_KEY.equals(key))
        {
            ConfigUpdater.updateArgsDetailDepth(Integer.parseInt(value));
        }
        else if (JavelinConfig.RETURN_DETAIL_KEY.equals(key))
        {
            ConfigUpdater.updateReturnDetail(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.RETURN_DETAIL_DEPTH_KEY.equals(key))
        {
            ConfigUpdater.updateReturnDetailDepth(Integer.parseInt(value));
        }
        else if (JavelinConfig.HTTP_SESSION_DETAIL_KEY.equals(key))
        {
            ConfigUpdater.updateHttpSessionDetail(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.HTTP_SESSION_DETAIL_DEPTH_KEY.equals(key))
        {
            ConfigUpdater.updateHttpSessionDetailDepth(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREADMODEL_KEY.equals(key))
        {
            ConfigUpdater.updateThreadModel(Integer.parseInt(value));
        }
        else if (JavelinConfig.ALARM_CPUTHRESHOLD.equals(key))
        {
            ConfigUpdater.updateAlarmCpuThreshold(Long.parseLong(value));
        }
        else if (JavelinConfig.NET_INPUT_MONITOR.equals(key))
        {
            ConfigUpdater.updateNetInputMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.NET_OUTPUT_MONITOR.equals(key))
        {
            ConfigUpdater.updateNetOutputMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.FILE_INPUT_MONITOR.equals(key))
        {
            ConfigUpdater.updateFileInputMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.FILE_OUTPUT_MONITOR.equals(key))
        {
            ConfigUpdater.updateFileOutputMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.FINALIZATION_COUNT_MONITOR.equals(key))
        {
            ConfigUpdater.updateFinalizationCount(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.INTERVAL_ERROR_MONITOR.equals(key))
        {
            ConfigUpdater.updateIntervalMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.HTTP_SESSION_COUNT_MONITOR.equals(key))
        {
            ConfigUpdater.updateHttpSessionCount(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.HTTP_SESSION_SIZE_MONITOR.equals(key))
        {
            ConfigUpdater.updateHttpSessionSize(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.CALL_TREE_ENABLE_KEY.equals(key))
        {
            ConfigUpdater.updateCallTreeEnabled(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.CALL_TREE_MAX_KEY.equals(key))
        {
            ConfigUpdater.updateCallTreeMaxSize(Integer.parseInt(value));
        }
        else if (JavelinConfig.LOG4J_PRINTSTACK_LEVEL.equals(key))
        {
            ConfigUpdater.updateLog4jPrintStackLevel(value);
        }
        else if (JavelinConfig.COLLECTION_MONITOR.equals(key))
        {
            ConfigUpdater.updateCollectionMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.COLLECTION_SIZE_THRESHOLD.equals(key))
        {
            ConfigUpdater.updateCollectionSizesThreshold(Integer.parseInt(value));
        }
        else if (JavelinConfig.LEAK_COLLECTIONSIZE_OUT.equals(key))
        {
            ConfigUpdater.updateLeakCollectionSizePrint(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.CLASS_HISTO.equals(key))
        {
            ConfigUpdater.updateClassHisto(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.CLASS_HISTO_INTERVAL.equals(key))
        {
            ConfigUpdater.updateClassHistoInterval(Integer.parseInt(value));
        }
        else if (JavelinConfig.CLASS_HISTO_MAX.equals(key))
        {
            ConfigUpdater.updateClassHistoMax(Integer.parseInt(value));
        }
        else if (JavelinConfig.CLASS_HISTO_GC.equals(key))
        {
            ConfigUpdater.updateClassHistoGC(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.LINEARSEARCH_ENABLED_KEY.equals(key))
        {
            boolean isMonitor = Boolean.parseBoolean(value);
            ConfigUpdater.updateLinearSearchMonitor(isMonitor);
            LinearSearchMonitor.setMonitor(isMonitor);
        }
        else if (JavelinConfig.LINEARSEARCH_SIZE.equals(key))
        {
            int listSize = Integer.parseInt(value);
            ConfigUpdater.updateLinearSearchSize(listSize);
            LinearSearchMonitor.setLinearSearchListSize(listSize);
        }
        else if (JavelinConfig.LINEARSEARCH_RATIO.equals(key))
        {
            double listRatio = Double.parseDouble(value);
            ConfigUpdater.updateLinearSearchRatio(listRatio);
            LinearSearchMonitor.setListRatio(listRatio);
        }
        else if (JavelinConfig.THREAD_MONITOR.equals(key))
        {
            ConfigUpdater.updateThreadMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.THREAD_MONITOR_INTERVAL.equals(key))
        {
            ConfigUpdater.updateThreadMonitorInterval(Long.parseLong(value));
        }
        else if (JavelinConfig.THREAD_MONITOR_DEPTH.equals(key))
        {
            ConfigUpdater.updateThreadMonitorDepth(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREAD_BLOCK_THRESHOLD.equals(key))
        {
            ConfigUpdater.updateBlockThreshold(Long.parseLong(value));
        }
        else if (JavelinConfig.THREAD_BLOCKTIME_THRESHOLD.equals(key))
        {
            ConfigUpdater.updateBlockTimeThreshold(Long.parseLong(value));
        }
        else if (JavelinConfig.THREAD_BLOCK_THREADINFO_NUM.equals(key))
        {
            ConfigUpdater.updateBlockThreadInfoNum(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREAD_DUMP_MONITOR.equals(key))
        {
            ConfigUpdater.updateThreadDumpMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.THREAD_DUMP_INTERVAL.equals(key))
        {
            ConfigUpdater.updateThreadDumpInterval(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREAD_DUMP_THREAD.equals(key))
        {
            ConfigUpdater.updateThreadDumpNum(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREAD_DUMP_CPU.equals(key))
        {
            ConfigUpdater.updateThreadDumpCpu(Integer.parseInt(value));
        }
        else if (JavelinConfig.FULLGC_MONITOR.equals(key))
        {
            ConfigUpdater.updateFullGCMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.FULLGC_THREASHOLD.equals(key))
        {
            ConfigUpdater.updateFullGCThreshold(Integer.parseInt(value));
        }
        else if (JavelinConfig.THREAD_DEADLOCK_MONITOR.equals(key))
        {
            ConfigUpdater.updateDeadLockMonitor(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.ALARM_MINIMUM_INTERVAL_KEY.equals(key))
        {
            ConfigUpdater.updateAlarmMinimumInterval(Long.parseLong(value));
        }
        else if (JavelinConfig.TAT_ENABLED_KEY.equals(key))
        {
            ConfigUpdater.updateTatEnabled(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.TAT_KEEP_TIME_KEY.equals(key))
        {
            ConfigUpdater.updateTatKeepTime(Long.parseLong(value));
        }
        else if (JavelinConfig.CONCURRENT_ENABLED_KEY.equals(key))
        {
            ConfigUpdater.updateConcurrentAccessMonitor(Boolean.valueOf(value));
        }
        else if (JavelinConfig.TIMEOUT_MONITOR.equals(key))
        {
            ConfigUpdater.updateTimeoutMonitor(Boolean.valueOf(value));
        }
        else if (JavelinConfig.LOG_JVN_FILE.equals(key))
        {
            ConfigUpdater.updateLogJvnFile(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.COLLECT_SYSTEM_RESOURCES.equals(key))
        {
            ConfigUpdater.updateCollectSystemResources(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.SEND_INVOCATION_FULL_EVENT.equals(key))
        {
            ConfigUpdater.updateSendInvocationFullEvent(Boolean.parseBoolean(value));
        }
        // JdbcJavelin�����ݒ�̍X�V
        else if (JdbcJavelinConfig.JDBC_JAVELIN_ENABLED_KEY.equals(key))
        {
            ConfigUpdater.updateJdbcEnabled(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.RECORDEXECPLAN_KEY.equals(key))
        {
            ConfigUpdater.updateRecordExecPlan(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.EXECPLANTHRESHOLD_KEY.equals(key))
        {
            ConfigUpdater.updateExecPlanThreshold(Long.parseLong(value));
        }
        else if (JdbcJavelinConfig.RECORDDUPLJDBCCALL_KEY.equals(key))
        {
            ConfigUpdater.updateRecordDuplJdbcCall(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.RECORDBINDVAL_KEY.equals(key))
        {
            ConfigUpdater.updateRecordBindVal(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.STRINGLIMITLENGTH_KEY.equals(key))
        {
            ConfigUpdater.updateJdbcStringLimitLength(Long.parseLong(value));
        }
        else if (JdbcJavelinConfig.SQLCOUNT_MONITOR_KEY.equals(key))
        {
            ConfigUpdater.updateSqlcountMonitor(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.SQLCOUNT_KEY.equals(key))
        {
            ConfigUpdater.updateSqlcount(Long.parseLong(value));
        }
        else if (JdbcJavelinConfig.ORACLE_ALLOW_SQL_TRACE_KEY.equals(key))
        {
            ConfigUpdater.updateAllowSqlTraceForOracle(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.POSTGRES_VERBOSE_PLAN_KEY.equals(key))
        {
            ConfigUpdater.updateVerbosePlanForPostgres(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.RECORD_STACKTRACE_KEY.equals(key))
        {
            ConfigUpdater.updateRecordStackTrace(Boolean.parseBoolean(value));
        }
        else if (JdbcJavelinConfig.RECORD_STACKTRACE_THREADHOLD_KEY.equals(key))
        {
            ConfigUpdater.updateRecordStackTraceThreshold(Integer.parseInt(value));
        }
        else if (JdbcJavelinConfig.JDBC_JAVELIN_ENABLED_KEY.equals(key))
        {
            ConfigUpdater.updateJdbcJavelinEnabled(Boolean.parseBoolean(value));
        }
        else if (JavelinConfig.HTTP_STATUS_ERROR_KEY.equals(key))
        {
            ConfigUpdater.updateHttpStatusError(Boolean.parseBoolean(value));
        }
        JavelinConfigUtil.getInstance().update();
    }

    /**
     * �X�V�����𒴂��Ă���X�V�v�������s����B
     */
    public static void executeScheduledRequest()
    {
        List<String> removeList = new ArrayList<String>();
        synchronized (updateLaterMap__)
        {
            long currentTime = System.currentTimeMillis();
            for (ConfigUpdateRequest entry : updateLaterMap__.values())
            {
                if (entry.getUpdateTime() < currentTime)
                {
                    update(entry.getKey(), entry.getValue());
                    removeList.add(entry.getKey());
                }
            }
            
            for (String key : removeList)
            {
                updateLaterMap__.remove(key);
            }
        }
        
    }
}
