/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.javelin.jdbc.stats;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;

/**
 * JDBCJavelinLightweightモード詳細設計仕様書
 * @author heinminn
 *
 */
public class LightweightJdbcJavelinRecorder
{

    /** 設定値保持Bean */
    private static JdbcJavelinConfig config__ = new JdbcJavelinConfig();

    private static JavelinConfig logArgsConfig__ = new JavelinConfig()
    {
        public boolean isLogArgs()
        {
            return true;
        }
    };

    /**
     * constructor of this class
     */
    public LightweightJdbcJavelinRecorder()
    {
        //Do nothing
    }

    /**
     * JDBCJavelinのパラメータを設定する。
     * @param config JDBCJavelinの設定
     */
    public static void setJdbcJavelinConfig(final JdbcJavelinConfig config)
    {
        LightweightJdbcJavelinRecorder.config__ = config;
    }

    /**
     * 設定を取得する。
     * 
     * @return 設定。
     */
    public static JdbcJavelinConfig getConfig()
    {
        return config__;
    }

    /**
     * 
     * @param stmt Statement object
     */
    public static void preProcess(Statement stmt)
    {
        JdbcJvnStatus jdbcJvnStatus = JdbcJvnStatus.getInstance();

        if (jdbcJvnStatus.getCallDepth() == 0)
        {
            jdbcJvnStatus.clearPreprocessedDepthSet();
        }

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
            Boolean noSql = Boolean.FALSE;

            jdbcJvnStatus.setNoSql(noSql);

            CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
            CallTree callTree = callTreeRecorder.getCallTree();

            execNoDuplicateCall(jdbcJvnStatus, callTreeRecorder, callTree);
            jdbcJvnStatus.savePreprocessDepth();

            // StatsJavelinRecorderに処理を委譲する
            StatsJavelinRecorder.preProcess(className, "sql", null, logArgsConfig__, true);
        }
        catch (Exception ex)
        {
            // 想定外の例外が発生した場合は標準エラー出力に出力しておく。
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            jdbcJvnStatus.incrementCallDepth();
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
                // 親ノードがルートの場合は、ルートを null にする
                tree.setRootNode(null);
            }
            callTreeRecorder.setCallerNode(parent);
        }
    }

    /**
     * 後処理（本処理成功時）。
     * 
     */
    public static void postProcessOK()
    {
        JdbcJvnStatus jdbcJvnStatus = JdbcJvnStatus.getInstance();
        try
        {
            jdbcJvnStatus.decrementCallDepth();

            // 実行計画取得中であれば、前処理・後処理は行わない。
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

            // SQLトレース取得中状態以外の場合は、実行計画は取得しない。
            CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
            CallTree tree = callTreeRecorder.getCallTree();
            CallTreeNode node = null;
            if (tree == null || (config__.isAllowSqlTraceForOracle() //
                && (tree.containsFlag(SqlTraceStatus.KEY_SESSION_INITIALIZING) //
                    || tree.containsFlag(SqlTraceStatus.KEY_SESSION_CLOSING) //
                || tree.containsFlag(SqlTraceStatus.KEY_SESSION_FINISHED))))
            {
                return;
            }

            // 呼び出し元情報取得。
            node = callTreeRecorder.getCallTreeNode();

            // オリジナルのargsへの参照をローカル変数に一時保存
            String[] oldArgs = node.getArgs();
            if (oldArgs == null)
            {
                oldArgs = new String[0];
            }

            // SQL呼び出し回数をrootのCallTreeNodeに保持する
            if (config__.isSqlcountMonitor())
            {
                RecordStrategy rs = getRecordStrategy(tree, EventConstants.NAME_SQLCOUNT);
                if (rs != null && rs instanceof SqlCountStrategy && oldArgs.length > 0)
                {
                    // SQLCountStrategyのSQL呼び出し回数を増加させる
                    SqlCountStrategy strategy = (SqlCountStrategy)rs;
                    strategy.incrementSQLCount(oldArgs[0]);
                }
            }

        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            StatsJavelinRecorder.postProcess(null, null, (Object)null, config__, true);
            jdbcJvnStatus.setExecPlanSql(null);
        }
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
            // JDBC呼出し重複出力フラグがOFF、かつ最深ノードでなければ、
            // 何もしない。
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

    private static RecordStrategy getRecordStrategy(CallTree callTree, String strategyKey)
    {
        RecordStrategy rs = callTree.getRecordStrategy(strategyKey);
        if (rs == null)
        {
            if (EventConstants.NAME_SQLCOUNT.equals(strategyKey))
            {
                // SQLCountStrategyが登録されていない場合は、新規に登録する
                rs = new SqlCountStrategy();
                callTree.addRecordStrategy(strategyKey, rs);
            }
            else if (SqlPlanStrategy.KEY.equals(strategyKey))
            {
                // SQLPlanStrategyが登録されていない場合は、新規に登録する
                rs = new SqlPlanStrategy();
                callTree.addRecordStrategy(strategyKey, rs);
            }
        }
        return rs;
    }

    /**
     * 後処理（本処理失敗時）。
     * 
     */
    public static void postProcessNG(final Throwable cause)
    {
        try
        {
            JdbcJvnStatus jdbcJvnStatus = JdbcJvnStatus.getInstance();
            CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
            CallTree tree = callTreeRecorder.getCallTree();

            if (tree.isJdbcEnabled() == false)
            {
                return;
            }

            jdbcJvnStatus.decrementCallDepth();

            // 実行計画取得中であれば、前処理・後処理は行わない。
            if (jdbcJvnStatus.getNowExpalaining() != null)
            {
                return;
            }

            // セッション終了処理に入っている場合は、実行計画は取得しない。
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
                // 親ノードが"DB-Server"、かつJDBC呼出し重複出力フラグがOFFなら処理を終了。
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
        finally
        {
            StatsJavelinRecorder.postProcess(null, null, cause, config__, true);
        }
    }
    
    public static void recordPostNG(final Throwable cause, JdbcJvnStatus jdbcJvnStatus)
    {
        // JavelinRecorderに処理委譲
        CallTreeRecorder callTreeRecorder = jdbcJvnStatus.getCallTreeRecorder();
        CallTreeNode node = callTreeRecorder.getCallTreeNode();
        if (node != null)
        {
            StatsJavelinRecorder.postProcess(null, null, cause, config__, true);
            jdbcJvnStatus.setExecPlanSql(null);
        }
    }
}
