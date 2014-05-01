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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.db.OracleExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.AbstractProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinRecorder;

/**
 * Oracle�p
 * @author akiba
 *
 */
public class OracleProcessor extends AbstractProcessor
{
    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(Oracle Thin �h���C�o) */
    public static final String EXPLAIN_TARGET_ORACLE = "jdbc:oracle";

    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(BEA WebLogic Type 4 JDBC Oracle �h���C�o) */
    public static final String EXPLAIN_TARGET_BEA_ORACLE = "jdbc:bea:oracle:";
    
    /**
     * {@inheritDoc}
     */
    public boolean isTarget(final String jdbcUrl)
    {
        return jdbcUrl.startsWith(EXPLAIN_TARGET_ORACLE)
                || jdbcUrl.startsWith(EXPLAIN_TARGET_BEA_ORACLE);
    }

    /**
     * Oracle�Ŏ��s�v����擾����B
     * 
     * @param stmt �X�e�[�g�����g
     * @param originalSql SQL��
     * @param args �����B
     * @return ���s�v��
     * @throws SQLException Statement�N���[�Y���ɃG���[�����������Ƃ�
     */
    public String getOneExecPlan(final Statement stmt, final String originalSql, final List<?> args)
        throws SQLException
    {
        JdbcJavelinConfig config = new JdbcJavelinConfig();

        // ���s�v��擾�Ɏ��s�����ꍇ��args�ɃZ�b�g���镶����
        StringBuilder execPlanText = null;

        // ���s�v�搶��SQL���̐���    
        StringBuilder sql = new StringBuilder();
        sql.append("EXPLAIN PLAN FOR ");
        sql.append(originalSql);

        // ���s�v�搮�`�E�擾SQL�̐����B
        StringBuilder planTable = new StringBuilder();
        planTable.append("SELECT PLAN_TABLE_OUTPUT FROM TABLE"
                + "(DBMS_XPLAN.DISPLAY('PLAN_TABLE',NULL,'");
        planTable.append(config.getOutputOption());
        planTable.append("'))");

        // ���s�v��𐶐��iPLAN�e�[�u���ɓW�J�j
        ResultSet resultSet = null;
        Statement planStmt = null;
        try
        {
            planStmt = stmt.getConnection().createStatement();

            planStmt.execute(sql.toString());

            // ���s�v��𐮌`�E�擾
            resultSet = planStmt.executeQuery(planTable.toString());

            // �������ꂽ�s�������[�v
            execPlanText = new StringBuilder("");
            while (resultSet.next())
            {
                // PLAN_TABLE_OUTPUT���擾
                String planTableOutput = resultSet.getString(1);
                // ����
                execPlanText.append(planTableOutput);
                execPlanText.append('\n');
            }
        }
        catch (SQLException ex)
        {
            execPlanText = new StringBuilder(JdbcJavelinRecorder.EXPLAIN_PLAN_FAILED);
            // DB�A�N�Z�X�G���[/�z��O�̗�O�����������ꍇ�̓G���[���O�ɏo�͂��Ă����B
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
     * SQL�g���[�X�擾�pSQL�𔭍s����B
     * @param connection �R�l�N�V����
     */
    @Override
    public void startSqlTrace(final Connection connection)
    {
        // SQL�g���[�X�̃g���[�XID��ݒ肷��B
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String id = Thread.currentThread().getId() + "_" + dataFormat.format(new Date());

        Statement stmt = null;
        try
        {
            // SQL�g���[�X�擾�p��SQL�����s����B
            stmt = connection.createStatement();
            stmt.execute(SET_TRACE_ID + id + "'");

            // �����_�܂ł�SQL�g���[�X����U���ׂďo�͂��Ă���A�ēx�J�n����
            // ��Oracle��SQL�g���[�X�́A�Ō�̏o�͒�~���_���猻���_�܂ł��o�͂���(�����Ɍ�����)
            stmt.execute(START_SQL_TRACE);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            try
            {
                if (stmt != null)
                {
                    stmt.close();
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /**
     * SQL�g���[�X�I���p��SQL�𔭍s����B
     * @param connection �R�l�N�V����
     */
    public static void stopSqlTrace(final Connection connection)
    {
        Statement stmt = null;
        try
        {
            // SQL�g���[�X�I���p��SQL�����s����B
            stmt = connection.createStatement();
            stmt.execute(STOP_SQL_TRACE);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            try
            {
                if (stmt != null)
                {
                    stmt.close();
                }
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /** SQL�g���[�X��ID */
    private static final String SET_TRACE_ID = "alter session set tracefile_identifier='";

    /** SQL�g���[�X�擾�pSQL */
    private static final String START_SQL_TRACE = "alter session set sql_trace=true";

    /** SQL�g���[�X�I���pSQL */
    private static final String STOP_SQL_TRACE = "alter session set sql_trace=false";

    /**
     * {@inheritDoc}
     */
    public boolean needsLock()
    {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public AbstractExecutePlanChecker<?> getExecutePlanChecker()
    {
        return new OracleExecutePlanChecker();
    }
}
