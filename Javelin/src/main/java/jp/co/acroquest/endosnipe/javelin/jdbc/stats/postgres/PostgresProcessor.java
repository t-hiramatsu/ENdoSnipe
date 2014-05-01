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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.db.PostgreSQLExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.SqlUtil;
import jp.co.acroquest.endosnipe.javelin.jdbc.instrument.PreparedStatementPair;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.AbstractProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinStatement;

/**
 * Postgres�ŗL�̏������s���B
 * @author eriguchi
 */
public class PostgresProcessor extends AbstractProcessor
{

    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(PostgreSQL) */
    public static final String EXPLAIN_TARGET_POSTGRE = "jdbc:postgresql";

    /**
     * {@inheritDoc}
     */
    public boolean isTarget(final String jdbcUrl)
    {
        return jdbcUrl.startsWith(EXPLAIN_TARGET_POSTGRE);
    }

    /**
     * PostgreSQL��PreparedStatement�̎��s�v����擾����B
     *
     * @param stmt �X�e�[�g�����g
     * @param sql SQL��
     * @param args �����B
     * @return ���s�v��
     */
    @Override
    public String getExecPlanPrepared(final Statement stmt, final String sql, final List<?> args)
    {
        // ���s�v����i�[����o�b�t�@
        StringBuffer execPlanText = new StringBuffer();

        try
        {
            if(stmt instanceof JdbcJavelinStatement)
            {
                // ���s�v��擾�p�ɏ������ꂽPreparedStatement�����s����
                JdbcJavelinStatement jdbcJavelinStatement = (JdbcJavelinStatement)stmt;
                PreparedStatementPair[] pstmtList = jdbcJavelinStatement.getStmtForPlan();
                if (pstmtList != null)
                {
                    for (PreparedStatementPair pair : pstmtList)
                    {
                        if (pair.isDml() == false)
                        {
                            continue;
                        }
                        PreparedStatement pstmt = pair.getPreparedStatement();
                        ResultSet resultSet = pstmt.executeQuery();
                        if (resultSet != null)
                        {
                            // �������ꂽ�s�������[�v
                            while (resultSet.next())
                            {
                                // ���s�v����擾
                                String planTableOutput = resultSet.getString(1);
                                // ����
                                execPlanText.append(planTableOutput);
                                execPlanText.append("\n");
                            }
                            resultSet.close();
                        }
                        execPlanText.append("\n");
                    }
                }
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        if (execPlanText.length() == 0)
        {
            execPlanText.append("EXPLAIN PLAN failed.");
        }

        return execPlanText.toString();
    }

    /**
     * PostgreSQL��Statement�̎��s�v����擾����B
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
        ResultSet resultSet = null;

        // ���s�v��擾�Ɏ��s�����ꍇ��args�ɃZ�b�g���镶����
        StringBuffer execPlanText = new StringBuffer("EXPLAIN PLAN failed.");

        Statement planStmt = null;
        try
        {
            planStmt = stmt.getConnection().createStatement();

            if (JdbcJavelinRecorder.getConfig().isVerbosePlanForPostgres())
            {
                resultSet = planStmt.executeQuery("EXPLAIN VERBOSE " + originalSql);
            }
            else
            {
                resultSet = planStmt.executeQuery("EXPLAIN " + originalSql);
            }

            if (resultSet != null)
            {
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
            }
        }
        catch (Exception ex)
        {
            // �����������ꂽ catch �u���b�N
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
    public void postPrepareStatement(final String sql, final PreparedStatement pstmt)
        throws SQLException
    {
        Connection connection = pstmt.getConnection();
        // PostgresSQL ���� ���s�v��擾�ݒ肪ON �Ȃ�
        // ���s�v��擾�pPreparedStatement���쐬����

        // �Z�~�R�����ŋ�؂�ꂽ������Statement�𕪊����āA
        // ���ꂼ��PreparedStatement���쐬����B
        List<String> sqlList = SqlUtil.splitSqlStatement(sql);
        int sqlListSize = sqlList.size();
        PreparedStatementPair[] pstmtList = new PreparedStatementPair[sqlListSize];
        for (int index = 0; index < sqlListSize; index++)
        {
            String splitedSql = sqlList.get(index);
            boolean isDml = SqlUtil.checkDml(splitedSql);
            pstmtList[index] =
                    new PreparedStatementPair(connection, "EXPLAIN VERBOSE " + splitedSql,
                                              isDml);
        }

        // �쐬����PreparedStatement���t�B�[���h�ɓo�^����
        try
        {
            JdbcJavelinStatement jdbcJavelinStatement = (JdbcJavelinStatement)pstmt;
            jdbcJavelinStatement.setStmtForPlan(pstmtList);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractExecutePlanChecker<?> getExecutePlanChecker()
    {
        return new PostgreSQLExecutePlanChecker();
    }
}
