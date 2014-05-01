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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.db.DB2ExecutePlanChecker;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.AbstractProcessor;

/**
 * DB2�ŗL�̏������s���B
 * @author ochiai
 */
public class DB2Processor extends AbstractProcessor
{

    /** JDBC�ڑ�URL�����̕�����Ŏn�܂�Ƃ��A���s�v����擾����(DB2) */
    public static final String EXPLAIN_TARGET_DB2 = "jdbc:db2";
    
    /** Javelin���O���̉��s������\�� */
    public static final String NEW_LINE = "\n";
    
    /** Javelin���O���̋�؂蕶���i�J���}�j��\�� */
    public static final String COMMA = ",";
    
    /** �z��̍ŏ���index */
    private static final int COLUMN_INDEX_FIRST = 1;

    /** �z���2�Ԗڂ�index */
    private static final int COLUMN_INDEX_SECOND = 2;

    /** �z���3�Ԗڂ�index */
    private static final int COLUMN_INDEX_THIRD = 3;

    /** �z���4�Ԗڂ�index */
    private static final int COLUMN_INDEX_FOURTH = 4;

    /**
     * {@inheritDoc}
     */
    public boolean isTarget(final String jdbcUrl)
    {
        return jdbcUrl.startsWith(EXPLAIN_TARGET_DB2);
    }

    /**
     * DB2��Statement�̎��s�v����擾����B
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
        StringBuilder execPlanText = new StringBuilder("Access Plan failed.");

        //DB2��EXPLAIN�\�ɋL�����A
        //EXPLAIN_OPERATOR�e�[�u����OPERATOR_TYPE�𓾂�
        String clearExplainTables = "DELETE FROM EXPLAIN_INSTANCE";
        String explainModeExplain = "SET CURRENT EXPLAIN MODE EXPLAIN";
        String explainModeNo = "SET CURRENT EXPLAIN MODE NO";
        
        //EXPLAIN�\����t���X�L�����A�C���f�b�N�X�X�L�����̏����擾
        String operatorType =
            "SELECT ope.OPERATOR_ID, ope.OPERATOR_TYPE, stm.OBJECT_NAME, ope.TOTAL_COST "+
            "from EXPLAIN_OPERATOR ope LEFT JOIN EXPLAIN_STREAM stm "+
            "ON ope.EXPLAIN_REQUESTER = stm.EXPLAIN_REQUESTER and "+
            "ope.EXPLAIN_TIME = stm.EXPLAIN_TIME and "+
            "ope.SOURCE_NAME = stm.SOURCE_NAME and "+
            "ope.SOURCE_SCHEMA = stm.SOURCE_SCHEMA and "+
            "ope.SOURCE_VERSION = stm.SOURCE_VERSION and "+
            "ope.EXPLAIN_LEVEL = stm.EXPLAIN_LEVEL and "+
            "ope.STMTNO = stm.STMTNO and "+
            "ope.SECTNO = stm.SECTNO and "+
            "ope.OPERATOR_ID = stm.TARGET_ID ORDER BY ope.OPERATOR_ID";
        
        //EXPLAIN�\���� Optimized Statement ���擾
        String selectOptimizedStatement =
            "SELECT STATEMENT_TEXT FROM EXPLAIN_STATEMENT WHERE EXPLAIN_LEVEL='P'";
        
        String header = "Data from EXPLAIN tables";
        String optimizedStatement = "Optimized Statement:";
        String optimizedStatementSeparator = "------------------";
        String accessPlan = "Access Plan:";
        String accessPlanSeparator = "-----------";
        String meaningOfItems = "OPERATOR_ID,OPERATOR_TYPE,OBJECT_NAME,TOTAL_COST";
        
        // ���s�v��𐶐��iEXPLAIN�\�ɓW�J�j
        ResultSet resultSet = null;
        Statement planStmt = null;
        try
        {
            planStmt = stmt.getConnection().createStatement();
            
            // EXPLAIN�\�ɓW�J
            planStmt.execute(clearExplainTables);
            planStmt.execute(explainModeExplain);
            planStmt.execute(originalSql);
            planStmt.execute(explainModeNo);
            
            // EXPLAIN�\����Optimized Statement���擾
            resultSet = planStmt.executeQuery(selectOptimizedStatement);
            
            if (resultSet != null)
            {
                execPlanText = new StringBuilder("");
                
                execPlanText.append(header);
                execPlanText.append(NEW_LINE);
                execPlanText.append(NEW_LINE);
                execPlanText.append(optimizedStatement);
                execPlanText.append(NEW_LINE);
                execPlanText.append(optimizedStatementSeparator);
                execPlanText.append(NEW_LINE);

                resultSet.next();
                
                execPlanText.append(resultSet.getString(1));
                execPlanText.append(NEW_LINE);
                execPlanText.append(NEW_LINE);
            }
             
            // EXPLAIN�\����Access Plan���擾
            resultSet = planStmt.executeQuery(operatorType);

            if (resultSet != null)
            {
                // �������ꂽ�s�������[�v
                execPlanText.append(accessPlan);
                execPlanText.append(NEW_LINE);
                execPlanText.append(accessPlanSeparator);
                execPlanText.append(NEW_LINE);
                execPlanText.append(meaningOfItems);
                execPlanText.append(NEW_LINE);
                execPlanText.append(NEW_LINE);

                while (resultSet.next())
                {
                    // OPERATOR_TYPE���擾
                    // ���R�[�h�擾���������������邽�߁AResultSet�ւ̃A�N�Z�X��
                    // �J�������ł͂Ȃ��C���f�b�N�X��p���čs��
                    StringBuilder planTableOutputBuilder = new StringBuilder();
                    planTableOutputBuilder.append(resultSet.getString(COLUMN_INDEX_FIRST));
                    planTableOutputBuilder.append(COMMA);
                    planTableOutputBuilder.append(resultSet.getString(COLUMN_INDEX_SECOND));
                    planTableOutputBuilder.append(COMMA);
                    planTableOutputBuilder.append(resultSet.getString(COLUMN_INDEX_THIRD));
                    planTableOutputBuilder.append(COMMA);
                    planTableOutputBuilder.append(resultSet.getString(COLUMN_INDEX_FOURTH));
                    // ����
                    execPlanText.append(planTableOutputBuilder.toString());
                    execPlanText.append(NEW_LINE);
                }
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            // ���\�[�X���
            SQLUtil.closeResultSet(resultSet);
            SQLUtil.closeStatement(planStmt);
        }

        return execPlanText.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractExecutePlanChecker<?> getExecutePlanChecker()
    {
        return new DB2ExecutePlanChecker();
    }
}
