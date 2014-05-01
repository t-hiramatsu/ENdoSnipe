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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.Callback;
import jp.co.acroquest.endosnipe.javelin.MBeanManager;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.Component;
import jp.co.acroquest.endosnipe.javelin.bean.ExcludeMonitor;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CollectionMonitor;
import jp.co.acroquest.endosnipe.javelin.event.FullScanEvent;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinMessages;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.SqlUtil;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.db2.DB2Processor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.mysql.MySQLProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.oracle.OracleProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.oracle.OracleSessionStopCallback;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.postgres.PostgresProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.sqlserver.SQLServerProcessor;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * Jdbc�̃W���x�������O���L�^����.
 * @author eriguchi
 *
 */
public class JdbcJavelinRecorder
{
    /** SQL�̍ŏ��̃C���f�b�N�X. */
    private static final int         FIRST_SQL_INDEX       = 0;

    /** SQL�������Ԃ̃v���t�B�b�N�X. */
    public static final String       TIME_PREFIX           = "[Time] ";

    /** �o�C���h�ϐ��̃v���t�B�b�N�X. */
    public static final String       BIND_PREFIX           = "[VALUE] ";

    /** ���s�v��̃v���t�B�b�N�X. */
    public static final String       PLAN_PREFIX           = "[PLAN] ";

    /** �X�^�b�N�g���[�X�̃v���t�B�b�N�X. */
    public static final String       STACKTRACE_PREFIX     = "[STACKTRACE] ";

    private static final String      STACKTRACE_BASE       =
                                                             STACKTRACE_PREFIX
                                                                     + "Get a stacktrace." + '\n';

    /** ���s���\�b�h�̃p�����^�̃v���t�B�b�N�X. */
    public static final String       PARAM_PREFIX          = "[ExecuteParam] ";
    
    /** javelin.jdbc.stringLimitLength�ɂ���āASQL�����؂�l�߂�ꂽ���ɕ\�������L��. */
    private static final String      STRING_LIMITED_MARK   = "...";

    /** ���s�v��擾�Ɏ��s�����ꍇ�̃��b�Z�[�W�B */
    public static final String       EXPLAIN_PLAN_FAILED   =
                                             JdbcJavelinMessages.getMessage("javelin.jdbc.stats."
                                                 + "JdbcJavelinRecorder.FailExplainPlanMessage");

    /** �ݒ�l�ێ�Bean */
    private static JdbcJavelinConfig config__;

    private static JavelinConfig     logArgsConfig__       = new JavelinConfig() {
                                                               public boolean isLogArgs()
                                                               {
                                                                   return true;
                                                               }
                                                           };

    /** �Ώە����񂪌�����Ȃ��Ƃ� */
    public static final int          NOT_FOUND             = -1;

    /** �����s�R�����g�̊J�n��\��������̒��� */
    public static final int          COMMENT_FOOTER_LENGTH = "*/".length();

    /** �����s�R�����g�̏I����\��������̒��� */
    public static final int          COMMENT_HEADER_LENGTH = "/*".length();

    /** DBProcessor�̃��X�g�B */
    private static List<DBProcessor> processorList__;

    static
    {
        config__ = new JdbcJavelinConfig();
        processorList__ = new ArrayList<DBProcessor>();

        processorList__.add(new OracleProcessor());
        processorList__.add(new PostgresProcessor());
        processorList__.add(new SQLServerProcessor());
        processorList__.add(new MySQLProcessor());
        processorList__.add(new DB2Processor());
    }

    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private JdbcJavelinRecorder()
    {
        // Do Nothing.
    }

    /**
     * �O�����B(SQL��args�Ɏw�肳��Ă���ꍇ)
     * 
     * @param stmt �ΏۂƂȂ�Statement
     * @param args SQL��String�z��
     */
    public static void preProcessSQLArgs(final Statement stmt, final Object[] args)
    {
        JdbcJvnStatus    jdbcJvnStatus    = JdbcJvnStatus.getInstance();
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTree         tree             = callTreeRecorder.getCallTree();
        if (tree.getRootNode() == null)
        {
            tree.loadConfig();
        }
        
        if (tree.isJdbcEnabled() == false)
        {
            return;
        }

        jdbcJvnStatus.setExecPlanSql(args);
        preProcess(stmt, args, jdbcJvnStatus);
    }

    /**
     * �O�����B(���\�b�h�̃p�����[�^��args�Ɏw�肳��Ă���ꍇ)
     * �ŏ��̃p�����[�^��SQL�Ƃ��Ĉ����B
     * 
     * @param stmt �ΏۂƂȂ�Statement
     * @param args ���\�b�h�̃p�����[�^
     */
    public static void preProcessParam(final Statement stmt, final Object[] args)
    {
        JdbcJvnStatus    jdbcJvnStatus    = JdbcJvnStatus.getInstance();
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTree         tree             = callTreeRecorder.getCallTree();
        
        if (tree.getRootNode() == null)
        {
            tree.loadConfig();
        }
        
        if (tree.isJdbcEnabled() == false)
        {
            return;
        }

        if (args != null && args.length > 0)
        {
            jdbcJvnStatus.setExecPlanSql(new Object[]{args[0]});
        }
        preProcess(stmt, args, jdbcJvnStatus);
    }

    /**
     * �O�����B
     * 
     * @param stmt �X�e�[�g�����g�B
     * @param args �����B
     * @param jdbcJvnStatus JDBC Javelin�̏��
     */
    public static void preProcess(final Statement stmt, final Object[] args,
            JdbcJvnStatus jdbcJvnStatus)
    {
        try
        {
            if (jdbcJvnStatus.getCallDepth() == 0)
            {
                jdbcJvnStatus.clearPreprocessedDepthSet();
            }

            try
            {
                // �������g���q�A���S�𖄂ߍ��ށB
                // ���s�v��擾���ł���΁A�O�����E�㏈�����Ă�ł͂����Ȃ��B
                if (jdbcJvnStatus.getNowExpalaining() != null)
                {
                    return;
                }

                // JDBC�ďo���d���o�̓t���O��OFF�Ȃ�
                // �e�m�[�h���폜���Ď������c���[�ɒǉ�����B
                // ���̃R�[�h�́AStatsJavelinRecorder#preProcess���Ăяo���O�ɍs���B
                // �������Ȃ��ƁA���[�g�̏ꍇ��VMStatus���i�[����Ȃ����߁B
                CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
                CallTree tree = callTreeRecorder.getCallTree();

                // �Z�b�V�����I�������ɓ����Ă���ꍇ�́A�������Ȃ��B
                if (tree != null && (config__.isAllowSqlTraceForOracle() // 
                        && (tree.containsFlag(SqlTraceStatus.KEY_SESSION_CLOSING) //
                        || tree.containsFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING))))
                {
                    return;
                }

                recordPre(stmt, args, jdbcJvnStatus);
            }
            finally
            {
                jdbcJvnStatus.incrementCallDepth();
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * �O�����B
     * 
     * @param stmt �X�e�[�g�����g�B
     * @param args �����B
     * @param jdbcJvnStatus JDBC Javelin�̏��
     */
    public static void recordPre(final Statement stmt, final Object[] args,
            JdbcJvnStatus jdbcJvnStatus)
    {
        try
        {
            String jdbcUrl = "DB-Server";
            Connection connection = stmt.getConnection();
            JdbcJavelinConnection jvnConnection = null;
            if (connection != null)
            {
                jvnConnection = (JdbcJavelinConnection)connection;
                jdbcUrl = getJdbcUrl(connection, jvnConnection);
            }
            String className = jdbcUrl;
            String methodName = null;
            Boolean noSql;
            if (args.length <= FIRST_SQL_INDEX)
            {
                noSql = Boolean.TRUE;
                jdbcJvnStatus.setNoSql(noSql);
                return;
            }

            methodName = ((String)args[FIRST_SQL_INDEX]);
            
            // SQL�����Ajavelin.jdbc.stringLimitLength�Őݒ肵�������ɐ؂�l�߂�B
            int stringLimitLength = (int)config__.getJdbcStringLimitLength();
            if (stringLimitLength < methodName.length())
            {
                methodName = methodName.substring(0, stringLimitLength) + STRING_LIMITED_MARK;
            }
            
            noSql = Boolean.FALSE;
            jdbcJvnStatus.setNoSql(noSql);

            Component component = getComponent(className);
            Invocation invocation = StatsJavelinRecorder.getInvocation(component, methodName);
            if (invocation == null)
            {
                invocation =
                             StatsJavelinRecorder.registerInvocation(component, methodName,
                                                                       config__, false);
            }

            boolean isTarget = ExcludeMonitor.isMeasurementTarget(invocation);
            if (isTarget == false)
            {
                return;
            }
            if (config__.isRecordDuplJdbcCall() == false)
            {
                CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
                CallTree callTree = callTreeRecorder.getCallTree();
                execNoDuplicateCall(jdbcJvnStatus, callTreeRecorder, callTree);
            }

            // StatsJavelinRecorder�ɏ������Ϗ�����
            StatsJavelinRecorder.preProcess(component, invocation, args, logArgsConfig__, true);
            jdbcJvnStatus.savePreprocessDepth();

            DBProcessor processor = getProcessor(jdbcUrl, jvnConnection);
            onExecStatement(processor, connection, jdbcJvnStatus);
        }
        catch (Exception ex)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(ex);
        }
    }

    private static String getJdbcUrl(Connection connection, JdbcJavelinConnection jvnConnection)
        throws SQLException
    {
        String jdbcUrl;
        jdbcUrl = jvnConnection.getJdbcUrl();
        if (jdbcUrl == null)
        {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData != null)
            {
                jdbcUrl = metaData.getURL();
                jvnConnection.setJdbcUrl(jdbcUrl);
            }
        }
        return jdbcUrl;
    }

    private static Component getComponent(String className)
    {
        Component component = MBeanManager.getComponent(className);
        if (component == null)
        {
            component = new Component(className);
            Component oldComponent = MBeanManager.setComponent(className, component);
            if (oldComponent != null)
            {
                component = oldComponent;
            }
        }
        return component;
    }

    private static DBProcessor getProcessor(String jdbcUrl, JdbcJavelinConnection jvnConnection)
    {
        DBProcessor processor;
        if (jvnConnection != null)
        {
            processor = jvnConnection.getJdbcJavelinProcessor();
            if (processor == null)
            {
                processor = getProcessor(jdbcUrl);
                jvnConnection.setJdbcJavelinProcessor(processor);
            }
        }
        else
        {
            processor = getProcessor(jdbcUrl);
        }
        return processor;
    }

    private static void execNoDuplicateCall(JdbcJvnStatus jdbcJvnStatus,
            CallTreeRecorder callTreeRecorder, CallTree tree)
    {
        int depth = jdbcJvnStatus.incrementDepth();
        if (depth > 1)
        {
            CallTreeNode node = callTreeRecorder.getCallTreeNode();
            CallTreeNode parent = node.getParent();
            if (parent != null)
            {
                callTreeRecorder.removeChildNode(parent, node);
            }
            else
            {
                // �e�m�[�h�����[�g�̏ꍇ�́A���[�g�� null �ɂ���
                tree.setRootNode(null);
            }
            callTreeRecorder.setCallerNode(parent);
        }
    }

    private static void onExecStatement(final DBProcessor processor, final Connection connection,
            final JdbcJvnStatus jdbcJvnStatus)
    {
        if (processor == null)
        {
            return;
        }

        CallTree tree = jdbcJvnStatus.getCallTreeRecorder().getCallTree();

        // �Ώۃf�[�^�x�[�X��Oracle�ŁA
        // SQL�g���[�X�t���O���ݒ肳��Ă���A
        // ���A�Z�b�V�����ł͂��߂Ă�SQL���s�ł���΁A
        // SQL�g���[�X���J�n����B
        if (JdbcJavelinRecorder.config__.isAllowSqlTraceForOracle()
                && processor instanceof OracleProcessor
                && tree.containsFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING) == false
                && tree.containsFlag(SqlTraceStatus.KEY_SESSION_STARTED) == false)
        {
            // �uSQL�g���[�X�������v�ɑJ�ڂ���B
            tree.removeFlag(SqlTraceStatus.KEY_SESSION_CLOSING);
            tree.removeFlag(SqlTraceStatus.KEY_SESSION_FINISHED);
            tree.setFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING,
                         SqlTraceStatus.KEY_SESSION_INITIALIZING);

            Callback callback = new OracleSessionStopCallback(connection);
            tree.addCallback(callback);
            processor.startSqlTrace(connection);

            // �uSQL�g���[�X�擾���v�ɑJ�ڂ���B
            tree.removeFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING);
            tree.setFlag(SqlTraceStatus.KEY_SESSION_STARTED, SqlTraceStatus.KEY_SESSION_STARTED);
        }
    }

    /**
     * �㏈���i�{�����������j�B
     * 
     * @param stmt Statement�I�u�W�F�N�g
     * @param paramNum �p�����[�^�̐��i0:�p�����[�^�Ȃ��A1:�p�����[�^1�ȏ�j
     */
    public static void postProcessOK(final Statement stmt, final int paramNum)
    {
        try
        {
            JdbcJvnStatus jdbcJvnStatus = JdbcJvnStatus.getInstance();
            jdbcJvnStatus.decrementCallDepth();

            // ���s�v��擾���ł���΁A�O�����E�㏈���͍s��Ȃ��B
            if (jdbcJvnStatus.getNowExpalaining() != null)
            {
                return;
            }

            try
            {
                boolean result = ignore(jdbcJvnStatus);
                if (result == true)
                {
                    jdbcJvnStatus.removePreProcessDepth();
                    return;
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }

            if (jdbcJvnStatus.isPreprocessDepth() == false)
            {
                return;
            }
            jdbcJvnStatus.removePreProcessDepth();

            // SQL�g���[�X�擾����ԈȊO�̏ꍇ�́A���s�v��͎擾���Ȃ��B
            CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
            CallTree tree = callTreeRecorder.getCallTree();
            if (tree == null || (config__.isAllowSqlTraceForOracle() //
                    && (tree.containsFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING) //
                            || tree.containsFlag(SqlTraceStatus.KEY_SESSION_CLOSING) //
                    || tree.containsFlag(SqlTraceStatus.KEY_SESSION_FINISHED))))
            {
                return;
            }

            recordPostOK(stmt, paramNum, jdbcJvnStatus);

        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * �㏈���i�{�����������j�B
     * 
     * @param stmt Statement�I�u�W�F�N�g
     * @param paramNum �p�����[�^�̐��i0:�p�����[�^�Ȃ��A1:�p�����[�^1�ȏ�j
     * @param jdbcJvnStatus jdbcJvnStatus
     */
    public static void recordPostOK(final Statement stmt, final int paramNum,
            JdbcJvnStatus jdbcJvnStatus)
    {
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTree tree = callTreeRecorder.getCallTree();
        CallTreeNode node = null;
        try
        {
            // �Ăяo�������擾�B
            node = callTreeRecorder.getCallTreeNode();

            // �I���W�i����args�ւ̎Q�Ƃ����[�J���ϐ��Ɉꎞ�ۑ�
            String[] oldArgs = node.getArgs();
            if (oldArgs == null)
            {
                oldArgs = new String[0];
            }

            // �N�G�����ԎZ�o�Aargs[0]�ɓ����
            long queryTime = calcQueryTime(stmt, paramNum, node, oldArgs);

            // �č\������args���ꎞ�I�ɓ����List
            List<String> tempArgs = new LinkedList<String>();

            tempArgs.add(TIME_PREFIX + queryTime);

            // JDBC�ڑ�URL�擾
            Connection connection = stmt.getConnection();
            JdbcJavelinConnection jvnConnection = null;
            if (connection != null)
            {
                jvnConnection = (JdbcJavelinConnection)connection;
            }

            // SQL�Ăяo���񐔂�root��CallTreeNode�ɕێ�����
            if (config__.isSqlcountMonitor())
            {
                RecordStrategy rs = getRecordStrategy(tree, EventConstants.NAME_SQLCOUNT);
                if (rs != null && rs instanceof SqlCountStrategy && oldArgs.length > 0)
                {
                    // SQLCountStrategy��SQL�Ăяo���񐔂𑝉�������
                    SqlCountStrategy strategy = (SqlCountStrategy)rs;
                    strategy.incrementSQLCount(oldArgs[0]);
                }
            }

            DBProcessor processor = null;
            String jdbcUrl = null;
            if (jvnConnection != null)
            {
                jdbcUrl = jvnConnection.getJdbcUrl();
                processor = jvnConnection.getJdbcJavelinProcessor();
                if (processor == null)
                {
                    processor = getProcessor(jdbcUrl);
                    jvnConnection.setJdbcJavelinProcessor(processor);
                }
            }

            // �ȉ��̂R�̏����𖞂����Ƃ��̂݁A�X�^�b�N�g���[�X���擾����B
            // 1.���s�v��擾�Ή�DB�ł���B
            // 2.�N�G�����Ԃ�臒l�𒴂��Ă���B
            // 3.���s�v��擾�t���O��ON�ł���B
            if (processor != null && queryTime >= config__.getExecPlanThreshold()
                    && config__.isRecordExecPlan())
            {

                long startTime = System.currentTimeMillis();
                // ���s�v��擾
                List<String> newArgs =
                                       getExecPlan(tree, node, processor, jdbcUrl, oldArgs, stmt,
                                                   paramNum, jdbcJvnStatus);
                jdbcJvnStatus.setExecPlanSql(null);
                // args�ɒǉ�
                tempArgs.addAll(newArgs);

                long endTime = System.currentTimeMillis();
                node.addJavelinTime(endTime - startTime);
            }
            else
            {
                // ���s�v��擾���Ȃ��Ȃ�ASQL���Ƀv���t�B�b�N�X��t�^����̂݁B
                addPrefix(stmt, paramNum, tempArgs, oldArgs);
            }

            // �X�^�b�N�g���[�X�擾�t���O��ON�ł���A���N�G�����Ԃ�臒l�𒴂��Ă���Ƃ��A�X�^�b�N�g���[�X���擾����B
            if (config__.isRecordStackTrace()
                    && queryTime >= config__.getRecordStackTraceThreshold())
            {
                tempArgs.add(getStackTrace());
            }

            // �č\�z����args��node�ɃZ�b�g
            node.setArgs(tempArgs.toArray(new String[tempArgs.size()]));
        }
        catch (Exception ex)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            StatsJavelinRecorder.postProcess(null, null, (Object)null, config__, true);
            jdbcJvnStatus.setExecPlanSql(null);
        }

    }

    /**
     * Full Scan�C�x���g�𐶐����A�o�^���܂��B
     * 
     * @param processor DB���Ƃ̃v���Z�b�T
     * @param newArgs ���s�v��
     * @param stmt Statement�I�u�W�F�N�g
     * @param paramNum �p�����[�^���i0:�p�����[�^�Ȃ��A1:�p�����[�^1�ȏ�j
     * @param node CallTreeNode
     * @param execPlanSql SQL��
     * @param resultText ���s�v��
     */
    private static void sendFullScanEvent(DBProcessor processor,
            List<String> newArgs, final Statement stmt, final int paramNum,
            final CallTreeNode node, String[] execPlanSql)
    {
        try
        {
            AbstractExecutePlanChecker<?> executeChecker = processor.getExecutePlanChecker();
            if (executeChecker != null)
            {
                String exePlan = executeChecker.parseExecutePlan(newArgs);
                
                // �t���X�L�����̔��蒆�́ACollection�̃g���[�X��OFF�ɂ���B
                Boolean prevTracing = CollectionMonitor.isTracing();
                CollectionMonitor.setTracing(Boolean.FALSE);
                Set<String> fullScanTableNameSet;
                try
                {
                    fullScanTableNameSet = executeChecker.getFullScanTableNameSet(exePlan, null);
                }
                finally
                {
                    CollectionMonitor.setTracing(prevTracing);
                }
                
                if (0 < fullScanTableNameSet.size())
                {
                    // �C�x���g�p�����[�^���Z�b�g����B
                    FullScanEvent event = new FullScanEvent();
                    
                    // �e�[�u������SQL���s���Ԃ͏�ɏo�͂���B
                    String fullScanTableNames = fullScanTableNameSet.toString();
                    fullScanTableNames = fullScanTableNames
                            .substring(1, fullScanTableNames.length() - 1);
                    event.addParam(EventConstants.PARAM_FULL_SCAN_TABLE_NAME,
                                   fullScanTableNames);
                    String[] oldArgs = node.getArgs();
                    if (oldArgs == null)
                    {
                        oldArgs = new String[0];
                    }
                    long queryTime = calcQueryTime(stmt, paramNum, node, oldArgs);
                    event.addParam(EventConstants.PARAM_FULL_SCAN_DURATION,
                                   String.valueOf(queryTime));
                    
                    // �R�[���c���[���g�p���郂�[�h�̏ꍇ�B
                    if (logArgsConfig__.isCallTreeEnabled())
                    {
                        // �X�^�b�N�g���[�X�擾���s��Ȃ��ꍇ�A
                        // �܂��͎擾���s���ꍇ�ł��A����臒l�ɒB���Ă��Ȃ��ꍇ�A�X�^�b�N�g���[�X���o�͂���B
                        if (config__.isRecordStackTrace() == false
                                || config__.getRecordStackTraceThreshold() < queryTime)
                        {
                            
                            event.addParam(EventConstants.PARAM_FULL_SCAN_STACK_TRACE,
                                           getStackTrace());
                        }
                    }
                    // �R�[���c���[���g�p���Ȃ����[�h�̏ꍇ�B
                    else
                    {
                        // ���s�v��̓��e�A�X�^�b�N�g���[�X�Ƃ��ɏo�͂���B
                        event.addParam(EventConstants.PARAM_FULL_SCAN_EXEC_PLAN,
                                       newArgs.toString());
                        event.addParam(EventConstants.PARAM_FULL_SCAN_STACK_TRACE,
                                       getStackTrace());
                    }
                    
                    StatsJavelinRecorder.addEvent(event);
                }
            }
        }
        catch (Exception ex)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(ex);
        }
    }

    private static void saveExecPlan(CallTreeNode node, String[] execPlan,
            JdbcJvnStatus jdbcJvnStatus)
    {
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        SqlPlanStrategy sqlPlanRecordStrategy = getSqlPlanStrategy(callTreeRecorder.getCallTree());
        if (sqlPlanRecordStrategy == null || execPlan == null || execPlan.length == 0)
        {
            return;
        }

        sqlPlanRecordStrategy.setExecPlan(node, execPlan);

        // ���s�v��o�͗p��SQL��ۑ�����B
        String sql = node.getInvocation().getMethodName();
        sqlPlanRecordStrategy.recordPlanOutputSql(sql);
    }

    private static SqlPlanStrategy getSqlPlanStrategy(CallTree callTree)
    {
        RecordStrategy recordStrategy = getRecordStrategy(callTree, SqlPlanStrategy.KEY);
        SqlPlanStrategy sqlPlanRecordStrategy = null;
        if (recordStrategy instanceof SqlPlanStrategy)
        {
            sqlPlanRecordStrategy = (SqlPlanStrategy)recordStrategy;
        }
        return sqlPlanRecordStrategy;
    }

    private static String[] getPrevExecPlan(CallTree tree, CallTreeNode node)
    {
        SqlPlanStrategy sqlPlanRecordStrategy = getSqlPlanStrategy(tree);
        if (sqlPlanRecordStrategy == null)
        {
            return null;
        }

        return sqlPlanRecordStrategy.getExecPlan(node);
    }

    /**
     *�@SQL�̎��s�v�����x�擾������A��莞�Ԍo�߂������ǂ����𔻒肷��B
     * 
     * @param node �m�[�h�B
     * @return SQL�̎��s�v�����x�擾������A��莞�Ԍo�߂������ǂ����B 
     */
    private static boolean isRecordIntervalExpired(CallTreeNode node, JdbcJvnStatus jdbcJvnStatus)
    {
        boolean expired = false;

        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        SqlPlanStrategy sqlPlanRecordStrategy = getSqlPlanStrategy(callTreeRecorder.getCallTree());
        if (sqlPlanRecordStrategy == null)
        {
            return expired;
        }
        String sql = node.getInvocation().getMethodName();
        expired = !sqlPlanRecordStrategy.existPlanOutputSql(sql);
        return expired;
    }

    private static RecordStrategy getRecordStrategy(CallTree callTree, String strategyKey)
    {
        RecordStrategy rs = callTree.getRecordStrategy(strategyKey);
        if (rs == null)
        {
            if (EventConstants.NAME_SQLCOUNT.equals(strategyKey))
            {
                // SQLCountStrategy���o�^����Ă��Ȃ��ꍇ�́A�V�K�ɓo�^����
                rs = new SqlCountStrategy();
                callTree.addRecordStrategy(strategyKey, rs);
            }
            else if (SqlPlanStrategy.KEY.equals(strategyKey))
            {
                // SQLPlanStrategy���o�^����Ă��Ȃ��ꍇ�́A�V�K�ɓo�^����
                rs = new SqlPlanStrategy();
                callTree.addRecordStrategy(strategyKey, rs);
            }
        }
        return rs;
    }

    /**
     * �X�^�b�N�g���[�X���擾����B
     * @return �X�^�b�N�g���[�X
     */
    private static String getStackTrace()
    {
        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();

        StringBuilder builder = new StringBuilder(STACKTRACE_BASE);
        builder.append(ThreadUtil.getStackTrace(stacktraces, stacktraces.length));
        return builder.toString();
    }

    private static long calcQueryTime(final Statement stmt, final int paramNum,
            final CallTreeNode node, final String[] oldArgs)
        throws Exception
    {
        long queryTime = System.currentTimeMillis() - node.getStartTime();

        // �o�b�`���s�̏ꍇ�́A���ώ��Ԃ��Z�o����
        int addBatchCount = 1;
        if (stmt instanceof PreparedStatement)
        {
            // PreparedStatement�̏ꍇ�́A�o�C���h�ϐ��̃C���f�b�N�X�����ɁA
            // �o�b�`�����̐��𓾂�
            addBatchCount = SqlUtil.getPreparedStatementAddBatchCount(stmt);
        }
        else
        {
            // ���ʂ�Statement�̏ꍇ�͈�����SQL�z�񂪓����Ă���̂ŁA
            // �������琔�𓾂�
            // �iparamNum �� 0 �Ȃ�o�b�`���s�j
            if (paramNum == 0)
            {
                addBatchCount = oldArgs.length;
            }
        }
        if (addBatchCount >= 2)
        {
            // ���ώ��Ԃ��v�Z
            queryTime /= addBatchCount;
        }
        return queryTime;
    }

    private static boolean ignore(JdbcJvnStatus jdbcJvnStatus)
    {
        boolean result = false;
        if (Boolean.TRUE.equals(jdbcJvnStatus.isNoSql()))
        {
            Boolean noSql = Boolean.FALSE;
            jdbcJvnStatus.setNoSql(noSql);
            result = true;
        }
        else
        {
            // JDBC�ďo���d���o�̓t���O��OFF�A���Ő[�m�[�h�łȂ���΁A
            // �������Ȃ��B
            int depth = jdbcJvnStatus.getDepth();
            if (config__.isRecordDuplJdbcCall() == false && depth > 0)
            {
                jdbcJvnStatus.decrementDepth();
                if (depth < jdbcJvnStatus.getDepthMax())
                {
                    result = true;
                }
            }
        }

        return result;
    }

    private static void addPrefix(final Statement stmt, final int paramNum,
            final List<String> tempArgs, final String[] oldArgs)
    {
        // �o�C���h�ϐ��o�̓t���O��ON�Ȃ�A�o�C���h�ϐ��o�͕�������쐬����B
        List<?> bindList = null;
        if (config__.isRecordBindVal())
        {
            bindList = SqlUtil.getJdbcJavelinBindValByRef(stmt);
        }

        if (paramNum == 1)
        {
            String bindVals = SqlUtil.getBindValCsv(bindList, 0);
            if (bindVals != null)
            {
                tempArgs.add(BIND_PREFIX + bindVals);
            }
            for (int count = 1; count < oldArgs.length; count++)
            {
                tempArgs.add(PARAM_PREFIX + oldArgs[count]);
            }
        }
        else
        {
            // SQL�iargs�S�āj�Ƀv���t�B�b�N�X��t�^
            for (int count = 0; count < oldArgs.length; count++)
            {
                String bindVals = SqlUtil.getBindValCsv(bindList, count);
                if (bindVals != null)
                {
                    tempArgs.add(BIND_PREFIX + bindVals);
                }

            }
        }
    }

    /**
     * �㏈���i�{�������s���j�B
     * 
     * @param cause ����
     */
    public static void postProcessNG(final Throwable cause)
    {
        try
        {
            JdbcJvnStatus    jdbcJvnStatus    = JdbcJvnStatus.getInstance();
            CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
            CallTree         tree             = callTreeRecorder.getCallTree();
            
            if (tree.isJdbcEnabled() == false)
            {
                return;
            }

            jdbcJvnStatus.decrementCallDepth();

            // ���s�v��擾���ł���΁A�O�����E�㏈���͍s��Ȃ��B
            if (jdbcJvnStatus.getNowExpalaining() != null)
            {
                return;
            }

            // �Z�b�V�����I�������ɓ����Ă���ꍇ�́A���s�v��͎擾���Ȃ��B
            if ((config__.isAllowSqlTraceForOracle() //
                    && (tree.containsFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING) //
                            || tree.containsFlag(SqlTraceStatus.KEY_SESSION_CLOSING) //
                    || tree.containsFlag(SqlTraceStatus.KEY_SESSION_FINISHED))))
            {
                return;
            }

            if (jdbcJvnStatus.isPreprocessDepth() == false)
            {
                return;
            }
            jdbcJvnStatus.removePreProcessDepth();

            try
            {
                // �e�m�[�h��"DB-Server"�A����JDBC�ďo���d���o�̓t���O��OFF�Ȃ珈�����I���B
                if (ignore(jdbcJvnStatus))
                {
                    return;
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }

            recordPostNG(cause, jdbcJvnStatus);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * �㏈���i�{�������s���j�B
     * 
     * @param cause ����
     * @param jdbcJvnStatus jdbcJvnStatus
     */
    public static void recordPostNG(final Throwable cause, JdbcJvnStatus jdbcJvnStatus)
    {
        // JavelinRecorder�ɏ����Ϗ�
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTreeNode node = callTreeRecorder.getCallTreeNode();
        if (node != null)
        {
            StatsJavelinRecorder.postProcess(null, null, cause, config__, true);
            jdbcJvnStatus.setExecPlanSql(null);
        }
    }

    /**
     * ���s�v��擾
     * @param callTree CallTree
     * @param node CallTreeNode
     * @param jdbcUrl �ڑ�URL
     * @param args node�ɃZ�b�g���ꂽargs
     * @param stmt Statement�I�u�W�F�N�g
     * @param paramNum �p�����[�^���i0:�p�����[�^�Ȃ��A1:�p�����[�^1�ȏ�j
     * 
     * @return�@SQL�̎��s�v��
     */
    private static List<String> getExecPlan(CallTree callTree, CallTreeNode node,
            final DBProcessor processor, final String jdbcUrl, final String[] args,
            final Statement stmt, final int paramNum, JdbcJvnStatus jdbcJvnStatus)
    {
        // ���ʃ��X�g�inode�ɓo�^���Ȃ���args�j
        List<String> resultText = new LinkedList<String>();

        // ���s�v��擾
        try
        {
            // ���s�v��擾���̏�Ԃ�ݒ�
            jdbcJvnStatus.setNowExpalaining(stmt);

            // args���Ȃ����(SQL���o�^����Ă��Ȃ�)�A���s�v��͎擾���Ȃ�
            if (args == null || args.length == 0)
            {
                return resultText;
            }

            // ���s�v����擾���ׂ�SQL����z��
            // paramNum��1�Ȃ�args��1�߂�SQL���B0�Ȃ炷�ׂĂ�SQL���B
            String[] originalSql = null;
            String[] execPlanSql = jdbcJvnStatus.getExecPlanSql();
            if (execPlanSql == null)
            {
                String key = "javelin.jdbc.stats.JdbcJavelinRecorder.NotSavedExecPlanSQLMessage";
                String message = JdbcJavelinMessages.getMessage(key);
                SystemLogger.getInstance().warn(message);
            }
            else
            {
                originalSql = execPlanSql;
            }

            // �o�C���h�ϐ��擾
            List<?> bindList = null;
            if (config__.isRecordBindVal())
            {
                bindList = SqlUtil.getJdbcJavelinBindValByRef(stmt);
            }
            String bindVals = "";

            // PreparedStatement�̃o�b�`���s�̏ꍇ�́A
            // �o�b�`���s����SQL�̐������̔z��ioriginalSql�j���쐬����
            if (stmt instanceof PreparedStatement && paramNum == 0 && args.length == 1)
            {
                originalSql = createBindValArray(stmt, originalSql, execPlanSql);
            }

            StringBuffer execPlanText = new StringBuffer();
            Statement planStmt = null;
            List<String> execPlans = new ArrayList<String>();
            // SQL�̐��������[�v
            boolean recordIntervalExpired = isRecordIntervalExpired(node, jdbcJvnStatus);
            String[] prevExecPlans = getPrevExecPlan(callTree, node);
            for (int count = 0; originalSql != null && count < originalSql.length; count++)
            {
                execPlanText.setLength(0);

                //�o�C���h�ϐ��擾
                bindVals = SqlUtil.getBindValCsv(bindList, count);

                // SQL��DML�ł͂Ȃ��ꍇ�A���s�v��͎擾���Ȃ��B
                String originalSqlElement = appendLineBreak(originalSql[count]);
                if (SqlUtil.checkDml(originalSqlElement) == false)
                {

                    if (bindVals != null)
                    {
                        resultText.add(BIND_PREFIX + bindVals);
                    }
                    continue;
                }

                try
                {
                    if (count == 0 || (stmt instanceof PreparedStatement) == false)
                    {
                        // PreparedStatement�̂Ƃ��́A
                        // �o�C���h�ϐ����Z�b�g���ꂽ�A���s�v��擾�p��PreparedStatement�����s����B
                        String execPlanResult = null;

                        // TODO �O��̎��s�v�悪�擾�ł��Ȃ��ꍇ������̂ŁAnull�`�F�b�N�Ŏb��Ώ�����B
                        if (recordIntervalExpired || prevExecPlans == null)
                        {
                            planStmt = stmt.getConnection().createStatement();
                            
                            if (processor.needsLock())
                            {
                                synchronized (processor)
                                {
                                    recordIntervalExpired =
                                                            isRecordIntervalExpired(node,
                                                                                    jdbcJvnStatus);
                                    prevExecPlans = getPrevExecPlan(callTree, node);
                                    if (recordIntervalExpired || prevExecPlans == null)
                                    {
                                        execPlanResult =
                                                         doExecPlan(processor, stmt, bindList,
                                                                    planStmt, originalSqlElement);
                                    }
                                    else
                                    {
                                        execPlanResult = prevExecPlans[count];
                                    }
                                }
                            }
                            else
                            {
                                execPlanResult =
                                                 doExecPlan(processor, stmt, bindList, planStmt,
                                                            originalSqlElement);
                            }
                            
                            execPlans.add(execPlanResult);
                        }
                        else
                        {
                            execPlanResult = prevExecPlans[count];
                        }
                        execPlanText.append(execPlanResult);
                    }
                }
                catch (Exception ex)
                {
                    // DB�A�N�Z�X�G���[/�z��O�̗�O�����������ꍇ�̓G���[���O�ɏo�͂��Ă����B
                    SystemLogger.getInstance().warn(ex);
                }
                finally
                {
                    // ���\�[�X���
                    if (planStmt != null)
                    {
                        planStmt.close();
                        planStmt = null;
                    }

                    if (paramNum != 1)
                    {
                        // ���s�v����擾���ׂ�SQL����������ꍇ�A�o�C���h�ϐ��A���s�v��𖖔��ɒǉ�
                                 if (bindVals != null)
                        {
                            resultText.add(BIND_PREFIX + bindVals);
                        }
                    }

                    // ���s�v����擾���ׂ�SQL��1�����Ȃ��ꍇ�A���s�v��𖖔��ɒǉ�
                    if (execPlanText.length() > 0)
                    {
                        resultText.add(PLAN_PREFIX + execPlanText.toString());
                    }
                }
            }

            // �p�����[�^��1�Ȃ�A���\�b�h������args�ɒǉ�
            if (paramNum == 1)
            {
                for (int count = 1; count < args.length; count++)
                {
                    resultText.add(PARAM_PREFIX + args[count]);
                }
            }

            if (recordIntervalExpired)
            {
                String[] execPlanArray = execPlans.toArray(new String[execPlans.size()]);
                saveExecPlan(node, execPlanArray, jdbcJvnStatus);

                // Full Scan�Ď��t���O��ON�ł���A���s�v��擾�����{����AFull Scan���������Ă���ꍇ�̂݁A
                // �C�x���g�𔭐�������B
                if (config__.isFullScanMonitor())
                {
                    sendFullScanEvent(processor, resultText, stmt,
                                      paramNum, node, execPlanSql);
                }
            }
        }
        catch (Exception ex)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            // ���s�v��擾���̏�Ԃ�����
            jdbcJvnStatus.setNowExpalaining(null);
        }

        return resultText;
    }

    private static String appendLineBreak(String str)
    {
        if (str == null || str.endsWith("\n"))
        {
            return str;
        }

        return str + '\n';
    }

    private static String doExecPlan(DBProcessor processor, Statement stmt, List<?> bindList,
            Statement planStmt, String originalSqlElement)
        throws SQLException
    {
        String execPlanResult;
        if (stmt instanceof PreparedStatement)
        {
            execPlanResult = processor.getExecPlanPrepared(stmt, originalSqlElement, bindList);
        }
        else
        {
            // ���s�v����擾�iDBMS�̎�ނɂ���ĕ���j
            execPlanResult = processor.execPlan(stmt, originalSqlElement, planStmt);
        }
        return execPlanResult;
    }

    private static String[] createBindValArray(final Statement stmt, String[] originalSql,
            final String[] execPlanSql)
        throws Exception
    {
        int bindValCount = SqlUtil.getPreparedStatementAddBatchCount(stmt);
        if (bindValCount > 0)
        {
            if (execPlanSql == null)
            {
                String key = "javelin.jdbc.stats.JdbcJavelinRecorder.NotSavedExecPlanSQLMessage";
                String message = JdbcJavelinMessages.getMessage(key);
                SystemLogger.getInstance().warn(message);
            }
            else
            {
                originalSql = new String[bindValCount];
                for (int index = 0; index < bindValCount; index++)
                {
                    originalSql[index] = execPlanSql[0];
                }
            }
        }
        return originalSql;
    }

    /**
     * JDBCJavelin�̃p�����[�^��ݒ肷��B
     * @param config JDBCJavelin�̐ݒ�
     */
    public static void setJdbcJavelinConfig(final JdbcJavelinConfig config)
    {
        JdbcJavelinRecorder.config__ = config;
    }

    /**
     * �X���b�hID��ݒ肷��.
     * @param threadId �X���b�hID
     */
    public static void setThreadId(final String threadId)
    {
        // JavelinRecorder�ɏ����Ϗ�
        StatsJavelinRecorder.setThreadId(threadId);
    }

    private static DBProcessor getProcessor(final String jdbcUrl)
    {
        if (jdbcUrl == null)
        {
            return null;
        }

        for (DBProcessor processor : processorList__)
        {
            if (processor.isTarget(jdbcUrl))
            {
                return processor;
            }
        }

        return null;
    }

    /**
     * Connection.prepareStatement���\�b�h�Ăяo����ɌĂ΂�郁�\�b�h�B
     *
     * @param connection �ڑ��I�u�W�F�N�g
     * @param sql PreparedStatement������
     * @param pstmt Connection.prepareStatement()�̖߂�l
     * @param methodName ���\�b�h��
     */
    public static void postPrepareStatement(final Connection connection, final String sql,
            final PreparedStatement pstmt, final String methodName)
    {
        JdbcJvnStatus    jdbcJvnStatus    = JdbcJvnStatus.getInstance();
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTree         tree             = callTreeRecorder.getCallTree();

        if (tree.getRootNode() == null)
        {
            tree.loadConfig();
        }
                
        if (tree.isJdbcEnabled() == false)
        {
            return;
        }

        // ���s���Ă���SQL����ǉ�����
        if (pstmt != null)
        {
            try
            {
                JdbcJavelinStatement jStmt = (JdbcJavelinStatement)pstmt;
                jStmt.getJdbcJavelinSql().clear();
                jStmt.getJdbcJavelinSql().add(sql);
            }
            catch (Exception e)
            {
                SystemLogger.getInstance().warn(e);
            }
        }

        // �ȉ��A���s�v��擾�pPreparedStatement���쐬����

        // Connection.prepareStatement()�ȊO�́A���s�v��擾�pPreparedStatement���쐬���Ȃ�
        if ("prepareStatement".equals(methodName) == false)
        {
            return;
        }

        // ���s�v��擾�pPreparedStatement���쐬���ł���΁A�㏈���͍s��Ȃ�
        if (jdbcJvnStatus.getNowCalling() != null)
        {
            return;
        }

        // �Q�d�Ăяo�����֎~����
        jdbcJvnStatus.setNowCalling(connection);

        try
        {
            JdbcJavelinConnection jvnConnection = (JdbcJavelinConnection)connection;
            String jdbcUrl = jvnConnection.getJdbcUrl();
            DBProcessor processor = jvnConnection.getJdbcJavelinProcessor();
            if (processor == null)
            {
                processor = getProcessor(jdbcUrl);
                jvnConnection.setJdbcJavelinProcessor(processor);
            }

            if (processor != null)
            {
                processor.postPrepareStatement(sql, pstmt);
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            jdbcJvnStatus.setNowCalling(null);
        }
    }

    /**
     * �ݒ���擾����B
     * 
     * @return �ݒ�B
     */
    public static JdbcJavelinConfig getConfig()
    {
        return config__;
    }
}
