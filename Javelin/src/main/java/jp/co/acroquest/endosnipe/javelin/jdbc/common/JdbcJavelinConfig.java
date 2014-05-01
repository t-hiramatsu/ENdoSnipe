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
package jp.co.acroquest.endosnipe.javelin.jdbc.common;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;

/**
 * JDBCJavelin�̐ݒ��ێ�����N���X�B
 * @author eriguchi
 */
public class JdbcJavelinConfig extends JavelinConfig
{
    /** ���s�v����擾���邩�ۂ���ݒ肷��L�[�B */
    public static final String RECORDEXECPLAN_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.recordExecPlan";
    
    /** SQL����Full Scan���Ď����邩�ۂ���ݒ肷��L�[�B */ 
    public static final String FULLSCAN_MONITOR_KEY =
        JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.monitor";

    /** JDBC�ďo���d���o�͂��s�����ۂ���ݒ肷��L�[�B */
    public static final String RECORDDUPLJDBCCALL_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.recordDuplJdbcCall";

    /** �o�C���h�ϐ����擾���邩�ۂ���ݒ肷��L�[�B */
    public static final String RECORDBINDVAL_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.recordBindVal";

    /** ���s�v����擾����臒l��ݒ肷��L�[�B */
    public static final String EXECPLANTHRESHOLD_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.execPlanThreshold";

    /** �o�C���h�ϐ��o�͂ł̕����񒷐�����ݒ肷��L�[�B */
    public static final String STRINGLIMITLENGTH_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.stringLimitLength";

    /** ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ���ݒ肷��L�[�B */
    public static final String SQLCOUNT_MONITOR_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount.monitor";

    /** ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l�B */
    public static final String SQLCOUNT_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount";

    /** SQL�g���[�X�o�͂��s�����ۂ���ݒ肷��L�[�B */
    public static final String ORACLE_ALLOW_SQL_TRACE_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.oracle.allowSqlTrace";

    /** PostgreSQL�ŏڍׂȎ��s�v����擾���邩�ۂ���ݒ肷��L�[�B */
    public static final String POSTGRES_VERBOSE_PLAN_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.postgres.verbosePlan";

    /** �ő�N�G���ۑ�����ݒ肷��L�[�B */
    public static final String RECORD_STATEMENT_NUM_MAXIMUM_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.record.statement.num.maximum";

    /** JDBCJavelin�ŃX�^�b�N�g���[�X�o�͂�ON/OFF��ݒ肷��L�[�B */
    public static final String RECORD_STACKTRACE_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.record.stackTrace";

    /** JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邽�߂�臒l�B */
    public static final String RECORD_STACKTRACE_THREADHOLD_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.record.stacktraceThreashold";

    /** JDBCJavelin��L���ɂ��邩�ǂ����̃L�[ */
    public static final String JDBC_JAVELIN_ENABLED_KEY = JAVELIN_PREFIX + "jdbc.enable";

    /** ���s�v��o��ON/OFF�t���O�̃f�t�H���g�l */
    private static final boolean DEFAULT_RECORDEXECPLAN = false;
    
    /** SQL����Full Scan���Ď����邩�ۂ���ݒ肷��l�̃f�t�H���g�l */
    private static final boolean DEFAULT_FULLSCAN_MONITOR = true; 

    /** JDBC�ďo���d���o�̓t���O�̃f�t�H���g�l */
    private static final boolean DEFAULT_RECORDDUPLJDBCCALL = false;

    /** �o�C���h�ϐ��o�̓t���O�̃f�t�H���g�l */
    private static final boolean DEFAULT_RECORDBINDVAL = true;

    /** SQL�̎��s�v����L�^����ۂ�臒l�̃f�t�H���g�l */
    private static final long DEFAULT_EXECPLANTHRESHOLD = 0;

    /** �o�C���h�ϐ��o�͂ł̕����񒷐����̃f�t�H���g�l */
    private static final long DEFAULT_STRINGLIMITLENGTH = 102400;

    /** ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ��̃f�t�H���g�l */
    private static final boolean DEFAULT_SQLCOUNT_MONITOR = true;

    /** 臒l�̃f�t�H���g */
    private static final int DEFAULT_SQLCOUNT = 20;
    
    /** SQL�g���[�X�o�̓t���O�̃f�t�H���g�l */
    private static final boolean DEFAULT_ORACLE_ALLOW_SQL_TRACE = false;

    /** PostgreSQL�p�ڍ׎��s�v��o�̓t���O�̃f�t�H���g�l */
    private static final boolean DEFAULT_POSTGRES_VERBOSE_PLAN = false;

    /** �ő�N�G���ۑ����̃f�t�H���g�l */
    private static final int DEFAULT_MAX_RECORD_STATEMENT_NUM_MAXIMUM = 256;

    /** JDBCJavelin�ŃX�^�b�N�g���[�X�o�͂̃f�t�H���g�l */
    private static final boolean DEF_RECORD_STACKTRACE = true;

    /** JDBCJavelin�ŃX�^�b�N�g���[�X�o�͂�SQL���s���Ԃ̃f�t�H���g�l */
    private static final int DEF_RECORD_STACKTRACE_THRESHOLD = 0;

    /** JDBCJavelin��L���ɂ��邩�ǂ����̃f�t�H���g�l */
    private static final boolean DEF_JDBC_JAVELIN_ENABLED = true;

    /** ���s�v����擾���邩�ۂ���ݒ肷��L�[�B */
    private static boolean isRecordExecPlan__;
    
    /** SQL����Full Scan���Ď����邩�ۂ���ݒ肷��L�[�B*/
    private static boolean isFullScanMonitor__;

    /** JDBC�ďo���d���o�͂��s�����ۂ���ݒ肷��L�[�B */
    private static boolean isRecordDuplJdbcCall__;

    /** �o�C���h�ϐ����擾���邩�ۂ���ݒ肷��L�[�B */
    private static boolean isRecordBindVal__;

    /** ���s�v����擾����臒l��ݒ肷��L�[�B */
    private static long execPlanThreshold__;

    /** �o�C���h�ϐ��o�͂ł̕����񒷐�����ݒ肷��L�[�B */
    private static long stringLimitLength__;

    /** ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ��B */
    private static boolean isSqlcountMonitor__;

    /**  ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l�B */
    private static long sqlcount__;

    /** SQL�g���[�X�o�͂��s�����ۂ���ݒ肷��L�[�B */
    private static boolean isOracleAllowSqlTrace__;

    /** PostgreSQL�ŏڍׂȎ��s�v����擾���邩�ۂ���ݒ肷��L�[�B */
    private static boolean isPostgresVerbosePlan__;

    /** �ő�N�G���ۑ�����ݒ肷��L�[�B */
    private static int recordStatementNumMaximum__;

    /** JDBCJavelin�ŃX�^�b�N�g���[�X�o�͂�ON/OFF��ݒ肷��L�[�B */
    private static boolean isRecordStackTrace__;

    /** JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邽�߂�臒l�B */
    private static int recordStacktraceThreashold__;

    /**
     * Oracle�̎��s�v��̏o�̓I�v�V�����B
     * "BASIC","SERIAL","TYPICAL","ALL"�̉��ꂩ���w�肷��B
     * �f�t�H���g�l��"SERIAL"
     */
    private final String outputOption_;

    /** JDBC Javelin���g�p���邩�ǂ��� */
    private static boolean isJdbcJavelinEnabled__;

    static
    {
        initialize();
    }

    /**
     * �ݒ�l�����������܂��B<br />
     *
     * ���̃��\�b�h�́A JUnit �R�[�h������Ă΂�܂��B<br />
     * �i {@link jp.co.acroquest.test.util.JavelinTestUtil#camouflageJavelinConfig(Class, String)} �j
     */
    private static void initialize()
    {
        JavelinConfigUtil configUtil = JavelinConfigUtil.getInstance();
        isRecordExecPlan__ = configUtil.getBoolean(RECORDEXECPLAN_KEY, DEFAULT_RECORDEXECPLAN);
        isFullScanMonitor__ = configUtil.getBoolean(FULLSCAN_MONITOR_KEY, DEFAULT_FULLSCAN_MONITOR);
        isRecordDuplJdbcCall__ =
                configUtil.getBoolean(RECORDDUPLJDBCCALL_KEY, DEFAULT_RECORDDUPLJDBCCALL);
        isRecordBindVal__ = configUtil.getBoolean(RECORDBINDVAL_KEY, DEFAULT_RECORDBINDVAL);
        execPlanThreshold__ = configUtil.getLong(EXECPLANTHRESHOLD_KEY, DEFAULT_EXECPLANTHRESHOLD);
        stringLimitLength__ = configUtil.getLong(STRINGLIMITLENGTH_KEY, DEFAULT_STRINGLIMITLENGTH);
        isSqlcountMonitor__ = configUtil.getBoolean(SQLCOUNT_MONITOR_KEY, DEFAULT_SQLCOUNT_MONITOR);
        sqlcount__ = configUtil.getLong(SQLCOUNT_KEY, DEFAULT_SQLCOUNT);
        isOracleAllowSqlTrace__ =
                configUtil.getBoolean(ORACLE_ALLOW_SQL_TRACE_KEY, DEFAULT_ORACLE_ALLOW_SQL_TRACE);
        isPostgresVerbosePlan__ =
                configUtil.getBoolean(POSTGRES_VERBOSE_PLAN_KEY, DEFAULT_POSTGRES_VERBOSE_PLAN);
        recordStatementNumMaximum__ =
                configUtil.getInteger(RECORD_STATEMENT_NUM_MAXIMUM_KEY,
                                      DEFAULT_MAX_RECORD_STATEMENT_NUM_MAXIMUM);
        isRecordStackTrace__ = configUtil.getBoolean(RECORD_STACKTRACE_KEY, DEF_RECORD_STACKTRACE);
        recordStacktraceThreashold__ =
                configUtil.getInteger(RECORD_STACKTRACE_THREADHOLD_KEY,
                                      DEF_RECORD_STACKTRACE_THRESHOLD);
        isJdbcJavelinEnabled__ = 
                configUtil.getBoolean(JDBC_JAVELIN_ENABLED_KEY, DEF_JDBC_JAVELIN_ENABLED);
    }

    /**
     * Javelin�̐ݒ�I�u�W�F�N�g���쐬����B
     */
    public JdbcJavelinConfig()
    {
        this.outputOption_ = "SERIAL";
    }

    /** 
     * SQL�̎��s�v����L�^����ۂ�臒l��Ԃ��B
     * �l�i�~���b�j������鏈�����Ԃ̌Ăяo�����͋L�^���Ȃ��B
     * �f�t�H���g�l��0�B
     *
     * @return 臒l�i�~���b�j
     */
    public long getExecPlanThreshold()
    {
        return execPlanThreshold__;
    }

    /**
     * SQL�̎��s�v����L�^����ۂ�臒l��ݒ肷��B
     * 
     * @param execPlanThreshold 臒l�i�~���b�j
     */
    public void setExecPlanThreshold(final long execPlanThreshold)
    {
        execPlanThreshold__ = execPlanThreshold;
    }

    /** 
     * JDBC�ďo���d���o�̓t���O��Ԃ��B
     * true�̏ꍇ�̓A�v�������Statement�Ăяo���y��DBMS�Ɉˑ�����
     * �����I��Statement�Ăяo���̗��҂����O�ɏo�́B
     * �f�t�H���g��false�B
     *
     * @return JDBC�ďo���d���o�̓t���O
     */
    public boolean isRecordDuplJdbcCall()
    {
        return isRecordDuplJdbcCall__;
    }

    /**
     * JDBC�ďo���d���o�̓t���O��ݒ肷��B
     * 
     * @param recordDuplJdbcCall JDBC�ďo���d���o�̓t���O
     */
    public void setRecordDuplJdbcCall(final boolean recordDuplJdbcCall)
    {
        isRecordDuplJdbcCall__ = recordDuplJdbcCall;
    }
 
    /** 
     * ���s�v��o��ON/OFF�t���O��Ԃ��B
     *
     * @return ���s�v��擾�t���O
     */
    public boolean isRecordExecPlan()
    {
        return isRecordExecPlan__;
    }

    /**
     * JDBC�ďo���d���o�̓t���O��ݒ肷��B
     * 
     * @param recordExecPlan ���s�v��擾�t���O
     */
    public void setRecordExecPlan(final boolean recordExecPlan)
    {
        isRecordExecPlan__ = recordExecPlan;
    }
    
    /** 
     * SQL����Full Scan���Ď����邩�ǂ��̃t���O��Ԃ��B
     *
     * @return ���s�v��擾�t���O
     */
    public boolean isFullScanMonitor()
    {
        return isFullScanMonitor__;
    }

    /** 
     * SQL����Full Scan���Ď����邩�ǂ��̃t���O���Z�b�g����B
     * 
     * @param isFullScanMonitor Full Scan�Ď��t���O
     */
    public void setIsFullScanMonitor(boolean isFullScanMonitor)
    {
        JdbcJavelinConfig.isFullScanMonitor__ = isFullScanMonitor;
    }

    /** 
     * �o�C���h�ϐ��o�̓t���O��Ԃ��B
     * true�̏ꍇ��PreparedStatement�̃o�C���h�ϐ������O�ɏo�́B
     * �f�t�H���g��true�B
     *
     * @return �o�C���h�ϐ��o�̓t���O
     */
    public boolean isRecordBindVal()
    {
        return isRecordBindVal__;
    }

    /**
     * �o�C���h�ϐ��o�̓t���O��ݒ肷��B
     * 
     * @param recordBindVal ���s�v��擾�t���O
     */
    public void setRecordBindVal(final boolean recordBindVal)
    {
        isRecordBindVal__ = recordBindVal;
    }

    /**
     * Oracle�̎��s�v��̏o�̓I�v�V������Ԃ��B
     *
     * @return �o�̓I�v�V����
     */
    public String getOutputOption()
    {
        return this.outputOption_;
    }

    /**
     * �o�C���h�ϐ��o�͂ɂ����镶���񒷐�����Ԃ��B
     * setString�����setObject���\�b�h�Ŏw�肳�ꂽ�o�C���h�ϐ��𕶎��񉻂���
     * �ۂ̍ő啶���񒷁B
     * �f�t�H���g�l��64�B
     *
     * @return �ő啶����
     */
    public long getJdbcStringLimitLength()
    {
        return stringLimitLength__;
    }

    /**
     * �o�C���h�ϐ��o�͂ɂ����镶���񒷐�����ݒ肷��B
     * 
     * @param jdbcStringLimitLength �ő啶����
     */
    public void setJdbcStringLimitLength(final long jdbcStringLimitLength)
    {
        stringLimitLength__ = jdbcStringLimitLength;
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ���Ԃ��B
     * 
     * @return ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ�
     */
    public boolean isSqlcountMonitor()
    {
        return isSqlcountMonitor__;
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ���ݒ肷��B
     * 
     * @param sqlcountMonitor ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l���Ď����邩�ۂ�
     */
    public void setSqlcountMonitor(final boolean sqlcountMonitor)
    {
        isSqlcountMonitor__ = sqlcountMonitor;
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l��Ԃ��B
     *
     * @return ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l
     */
    public long getSqlcount()
    {
        return sqlcount__;
    }

    /**
     * ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l��ݒ肷��B
     * 
     * @param sqlcount ����g�����U�N�V�������̓���SQL�Ăяo���񐔒��߂�臒l
     */
    public void setSqlcount(final long sqlcount)
    {
        sqlcount__ = sqlcount;
    }

    /** 
     * Oracle�ɑ΂���SQL�g���[�X�̏o�͎w���t���O�B
     * true�̏ꍇ�̓X���b�h�̊J�n-�I���Ԃ�SQL�g���[�X���o�͂���B
     * �f�t�H���g��false(�o�͂��Ȃ�)�B
     *
     * @return SQL�g���[�X�̏o�͎w���t���O
     */
    public boolean isAllowSqlTraceForOracle()
    {
        return isOracleAllowSqlTrace__;
    }

    /**
     * Oracle�ɑ΂���SQL�g���[�X�̏o�͎w���t���O��ݒ肷��B
     * 
     * @param allowSqlTraceForOracle SQL�g���[�X�̏o�͎w���t���O
     */
    public void setAllowSqlTraceForOracle(final boolean allowSqlTraceForOracle)
    {
        isOracleAllowSqlTrace__ = allowSqlTraceForOracle;
    }

    /**
     * PostgreSQL�ɑ΂�����s�v��ڍ׎擾�t���O�B
     * true�̏ꍇ�͎��s�v��̗v��ł͂Ȃ��A�����\���S�Ă��擾����B
     * false�̏ꍇ�͗v��݂̂��擾����B
     * 
     * @return PostgreSQL�Ŏ��s�v��ڍׂ��擾����ꍇ�́A<code>true</code>
     */
    public boolean isVerbosePlanForPostgres()
    {
        return isPostgresVerbosePlan__;
    }

    /**
     * PostgreSQL�ɑ΂�����s�v��ڍ׎擾�t���O��ݒ肷��B
     * 
     * @param verbosePlanForPostgres SQL�g���[�X�̏o�͎w���t���O
     */
    public void setVerbosePlanForPostgres(final boolean verbosePlanForPostgres)
    {
        isPostgresVerbosePlan__ = verbosePlanForPostgres;
    }

    /**
     * �N�G���̍ő�ۑ������擾����B
     * 
     * �f�t�H���g�l��1000�B
     *
     * @return �ő啶����
     */
    public int getRecordStatementNumMax()
    {
        return recordStatementNumMaximum__;
    }

    /**
     * �N�G���̍ő�ۑ�����ݒ肷��B
     * 
     * @param recordMaxStatementNum �N�G���̍ő�ۑ���
     */
    public void setRecordStatementNumMax(final int recordMaxStatementNum)
    {
        recordStatementNumMaximum__ = recordMaxStatementNum;
    }

    /**
     * JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���ꍇ�A<code>true</code>
     */
    public boolean isRecordStackTrace()
    {
        return isRecordStackTrace__;
    }

    /**
     * JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @param isRecordThreashold JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���ꍇ�A<code>true</code>
     */
    public void setRecordStackTrace(boolean isRecordThreashold)
    {
        isRecordStackTrace__ = isRecordThreashold;
    }

    /**
     * JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���SQL���s���Ԃ�臒l��Ԃ��܂��B<br />
     * 
     * @return JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���SQL���s���Ԃ�臒l
     */
    public int getRecordStackTraceThreshold()
    {
        return recordStacktraceThreashold__;
    }

    /**
     * JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���SQL���s���Ԃ�臒l��ݒ肵�܂��B<br />
     * 
     * @param stackTraceThreshold JDBCJavelin�ŃX�^�b�N�g���[�X���o�͂���SQL���s���Ԃ�臒l
     */
    public void setRecordStackTraceThreshold(int stackTraceThreshold)
    {
        recordStacktraceThreashold__ = stackTraceThreshold;
    }

    /**
     * JDBC Javelin���L�����ǂ������擾����
     * 
     * @return JDBC Javelin���L�����ǂ���
     */
    public boolean isJdbcJavelinEnabled()
    {
        return isJdbcJavelinEnabled__;
    }

    /**
     * JDBC Javelin���g�p���邩�ǂ�����ݒ肷��B
     * 
     * @param isJdbcJavelinEnabled JDBC Javelin���g�p���邩�ǂ���
     */
    public void setJdbcJavelinEnabled(boolean isJdbcJavelinEnabled)
    {
        isJdbcJavelinEnabled__ = isJdbcJavelinEnabled;
    }
}
