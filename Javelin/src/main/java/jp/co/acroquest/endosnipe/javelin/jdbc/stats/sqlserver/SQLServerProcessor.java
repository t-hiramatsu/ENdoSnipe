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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats.sqlserver;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.db.SQLServerExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinMessages;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.AbstractProcessor;

/**
 * SQLServer�ŗL�̏������s���B
 * @author eriguchi
 */
public class SQLServerProcessor extends AbstractProcessor
{
    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(SQL Server SQLJDBC Ver1.1�ȍ~) */
    public static final String EXPLAIN_TARGET_SQLSERVER = "jdbc:sqlserver";

    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(SQL Server SQLJDBC Ver1.0) */
    public static final String EXPLAIN_TARGET_SQLSERVER_1_0 = "jdbc:sqljdbc";

    /** SQL Server �Ŏ��s�v�����邽�߂̐ݒ�R�}���h */
    public static final String SQLSERVER_SHOWPLAN = "SHOWPLAN_XML";

    /** SQL Server �Ŏ��s�v��擾���J�n���邽�߂̐ݒ�R�}���h */
    private static final String SHOWPLAN_ON = "SET " + SQLSERVER_SHOWPLAN + " ON;";

    /** SQL Server �Ŏ��s�v��擾���I�����邽�߂̐ݒ�R�}���h */
    private static final String SHOWPLAN_OFF = "SET " + SQLSERVER_SHOWPLAN + " OFF;";
    
    /** ���b�Z�[�W�擾�p�̃L�[ */
    private static final String KEY = "javelin.jdbc.stats.sqlserver." 
                                    + "SQLServerProcessor.NoSuchFieldExceptionMessage";

    /**
     * {@inheritDoc}
     */
    public boolean isTarget(final String jdbcUrl)
    {
        boolean isSqlServer =
                jdbcUrl.startsWith(EXPLAIN_TARGET_SQLSERVER)
                        || jdbcUrl.startsWith(EXPLAIN_TARGET_SQLSERVER_1_0);
        return isSqlServer;
    }

    /**
     * SQL Server�Ŏ��s�v����擾����B
     * 
     * @param stmt �X�e�[�g�����g
     * @param originalSql SQL��
     * @param args �����B
     * @return ���s�v��
     * @throws SQLException ResultSet�N���[�Y���ɃG���[�����������Ƃ�
     */
    public String getOneExecPlan(final Statement stmt, final String originalSql, final List<?> args)
        throws SQLException
    {
        // ���s�v��擾�Ɏ��s�����ꍇ��args�ɃZ�b�g���镶����
        StringBuffer execPlanText = new StringBuffer("EXPLAIN PLAN failed.");

        Statement planStmt = null;
        ResultSet resultSet = null;
        try
        {
            planStmt = stmt.getConnection().createStatement();

            // PreparedStatement�Ȃ�A�h���C�o������SQL�������p���ăL���b�V������擾�����݂�B
            // Statement�Ȃ�A�t���O�ύX�Ŏ��s�v����擾����B
            if (stmt instanceof PreparedStatement)
            {
                try
                {
                    // SQLServerPreparedStatement#preparedSQL�ɁASQL���̓����`�����i�[����Ă���
                    Field preparedSQL = stmt.getClass().getDeclaredField("preparedSQL");

                    // SQLServerPreparedStatement#preparedTypeDefinitions�ɁA�ϐ��̌^���i�[����Ă���
                    Field preparedTypeDefinitions =
                            stmt.getClass().getDeclaredField("preparedTypeDefinitions");
                    preparedSQL.setAccessible(true);
                    preparedTypeDefinitions.setAccessible(true);
                    String typeDefinitionsString = (String)preparedTypeDefinitions.get(stmt);
                    StringBuffer internalSQL = new StringBuffer();
                    if (typeDefinitionsString != null && typeDefinitionsString.length() > 0)
                    {
                        internalSQL.append("(");
                        internalSQL.append(typeDefinitionsString);
                        internalSQL.append(")");
                    }
                    internalSQL.append((String)preparedSQL.get(stmt));
                    String internalSQLText = new String(internalSQL);

                    // �L���b�V���e�[�u���̒�����ASQL Handle �� Plan Handle �ōi�荞�݁A
                    // �����`����SQL���Ɉ�v������̂�T��
                    String sql =
                            "SELECT query_plan " + "FROM sys.dm_exec_query_stats qs "
                                    + "CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) "
                                    + "CROSS APPLY sys.dm_exec_query_plan(qs.plan_handle) "
                                    + "WHERE text='" + internalSQLText.replaceAll("'", "''") + "'";
                    resultSet = planStmt.executeQuery(sql);
                    if (resultSet.next())
                    {
                        // SQL���ɑΉ�������s�v�悪����������A
                        // �ŏ��̂��̂����o���i�������̂������Ԃ��Ă���ꍇ������̂Łj
                        execPlanText.setLength(0);
                        execPlanText.append(resultSet.getString("query_plan"));
                        execPlanText.append('\n');
                    }
                }
                catch (NoSuchFieldException e)
                {
                    String key = KEY;
                    String message = JdbcJavelinMessages.getMessage(key);
                    SystemLogger.getInstance().warn(message, e);
                }
            }
            else
            {
                planStmt.addBatch(SHOWPLAN_ON);
                planStmt.executeBatch();

                // ���s�v������SQL���̑��M�����s�v��̎擾
                resultSet = planStmt.executeQuery(originalSql);

                // �������ꂽ�s�������[�v
                execPlanText.setLength(0);
                while (resultSet.next())
                {
                    // ���s�v����擾
                    String planTableOutput = resultSet.getString(1);
                    // ����
                    execPlanText.append(planTableOutput);
                    execPlanText.append('\n');
                }

                // ���s�v��擾����������
                planStmt.addBatch(SHOWPLAN_OFF);
                planStmt.executeBatch();
            }
        }
        catch (SQLException sqle)
        {
            // DB�A�N�Z�X�G���[�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(sqle);
        }
        catch (IllegalAccessException iae)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(iae);
        }
        catch (RuntimeException ex)
        {
            // �z��O�̗�O�����������ꍇ�͕W���G���[�o�͂ɏo�͂��Ă����B
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            // ���\�[�X���
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
            }
            finally
            {
                if (planStmt != null)
                {
                    planStmt.close();
                }
            }
        }

        return execPlanText.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractExecutePlanChecker<?> getExecutePlanChecker()
    {
        return new SQLServerExecutePlanChecker();
    }
}
