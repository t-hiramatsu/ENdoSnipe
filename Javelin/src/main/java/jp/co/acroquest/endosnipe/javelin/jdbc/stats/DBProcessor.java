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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.db.AbstractExecutePlanChecker;

/**
 * DB���ƂɈقȂ鏈�������{����B
 * 
 * @author eriguchi
 *
 */
public interface DBProcessor
{
    /**
     * �����Ώۂ�DB�ւ̐ڑ����ǂ����𔻒肷��B
     * 
     * @param jdbcUrl DB�ڑ�������B
     * @return �����Ώۂ�DB�ւ̐ڑ����ǂ����B
     */
    boolean isTarget(String jdbcUrl);

    /**
     * PreparedStatement�̎��s�v����擾����B
     *
     * @param stmt �X�e�[�g�����g
     * @param originalSql SQL��
     * @param args TODO
     * @return ���s�v��
     * 
     * @throws SQLException ���s�v��擾���ɃG���[�����������ꍇ�B
     */
    String getExecPlanPrepared(Statement stmt, String originalSql, List<?> args)
        throws SQLException;

    /**
     * Statement�̎��s�v����擾����B
     *
     * @param stmt �X�e�[�g�����g
     * @param originalSql SQL��
     * @param args TODO
     * @return ���s�v��
     * 
     * @throws SQLException ResultSet�N���[�Y���ɃG���[�����������Ƃ�
     */
    String getOneExecPlan(Statement stmt, String originalSql, List<?> args)
        throws SQLException;

    /**
     * Statement�̎��s�v����擾����B
     *
     * @param stmt �X�e�[�g�����g
     * @param originalSqlElement SQL��
     * @param planStmt ���s�v��擾�p�X�e�[�g�����g
     * @return ���s�v��
     * @throws SQLException ResultSet�N���[�Y���ɃG���[�����������Ƃ�
     */
    String execPlan(Statement stmt, String originalSqlElement, Statement planStmt)
        throws SQLException;

    /**
     * SQL�g���[�X�擾�pSQL�𔭍s����B
     * @param connection �R�l�N�V����
     */
    void startSqlTrace(Connection connection);

    /**
     * Connection.prepareStatement���\�b�h�Ăяo����ɌĂ΂�郁�\�b�h�B
     *
     * @param sql PreparedStatement������
     * @param pstmt Connection.prepareStatement()�̖߂�l
     * 
     * @throws SQLException ResultSet�N���[�Y���ɃG���[�����������Ƃ�
     */
    void postPrepareStatement(String sql, PreparedStatement pstmt)
        throws SQLException;
    
    /**
     * DB���Ƃ̎��s�v��̒�������N���X�̃C���X�^���X��Ԃ��܂��B<br>
     * @return ���s�v�撲���N���X�̃C���X�^���X
     */
    AbstractExecutePlanChecker<?> getExecutePlanChecker();
    
    /**
     * SQL��Full Scan���s���Ă��邩�ǂ����𒲍����A<br>
     * �s���Ă���e�[�u�����̃Z�b�g���쐬���ĕԂ��B
     * @param executePlan ���s�v��̕�����
     * @return Full Scan���s���Ă���e�[�u�����̃Z�b�g
     */
    Set<String> checkFullScan(String executePlan);

    /**
     * ���s�v��擾���Ƀ��b�N����K�v�����邩�ǂ����B
     * 
     * @return ���s�v��擾���Ƀ��b�N����K�v�����邩�ǂ���
     */
    boolean needsLock();
}
