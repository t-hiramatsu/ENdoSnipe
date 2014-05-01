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
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.DBInitializer;
import jp.co.acroquest.endosnipe.data.db.ConnectionManager;

import org.apache.commons.dbcp.DelegatingPreparedStatement;

/**
 * Dao �̂��߂̊��N���X�ł��B<br />
 * 
 * @author y-komori
 */
public abstract class AbstractDao
{
    /**
     * ���N���X�I�u�W�F�N�g�𐶐����܂��B<br />
     */
    protected AbstractDao()
    {
        // Do nothing.
    }

    /**
     * �R�l�N�V�������擾���܂��B<br />
     *
     * �f�[�^�x�[�X�����݂��Ȃ��ꍇ�́A�쐬���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @return {@link Connection} �I�u�W�F�N�g
     * @throws SQLException �R�l�N�V�������擾�ł��Ȃ��ꍇ
     */
    protected static Connection getConnection(final String database)
        throws SQLException
    {
        return getConnection(database, false);
    }

    /**
     * �R�l�N�V�������擾���܂��B<br />
     * 
     * @param databaseName �f�[�^�x�[�X��
     * @param connectOnlyExists �f�[�^�x�[�X�����݂���Ƃ��̂ݐڑ�����ꍇ�� <code>true</code> �A
     *                          ���݂��Ȃ��Ƃ��Ƀf�[�^�x�[�X�𐶐�����ꍇ�� <code>false</code>
     * @return {@link Connection} �I�u�W�F�N�g
     * @throws SQLException �R�l�N�V�������擾�ł��Ȃ��ꍇ
     */
    protected static Connection getConnection(final String databaseName,
            final boolean connectOnlyExists)
        throws SQLException
    {
        return ConnectionManager.getInstance().getConnection(databaseName, connectOnlyExists);
    }

    /**
     * �f���Q�[�g���ꂽ {@link PreparedStatement} �I�u�W�F�N�g���擾���܂��B<br />
     * 
     * commons-dbcp �� {@link DelegatingPreparedStatement} �o�R�� blob ��ݒ肷��ƁA
     * {@link AbstractMethodError} ���������邽�߂̏��u�B
     * 
     * @param pstmt {@link PreparedStatement}
     * @return {@link PreparedStatement} �I�u�W�F�N�g
     */
    protected static PreparedStatement getDelegatingStatement(final PreparedStatement pstmt)
    {
        if (pstmt instanceof DelegatingPreparedStatement)
        {
            return (PreparedStatement)((DelegatingPreparedStatement)pstmt).getDelegate();
        }
        else
        {
            return pstmt;
        }
    }

    /**
     * �w�肳�ꂽ�e�[�u���̂��ׂẴ��R�[�h���폜���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param table �e�[�u����
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static void deleteAll(final String database, final String table)
        throws SQLException
    {
        Connection conn = null;
        try
        {
            conn = getConnection(database, true);
            deleteAll(conn, table);
        }
        finally
        {
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * �w�肳�ꂽ�e�[�u���̂��ׂẴ��R�[�h���폜���܂��B<br />
     *
     * @param conn �R�l�N�V����
     * @param table �e�[�u����
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static void deleteAll(final Connection conn, final String table)
        throws SQLException
    {
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.execute("delete from " + table);
        }
        finally
        {
            SQLUtil.closeStatement(stmt);
        }
    }

    /**
     * �w�肵���e�[�u���� truncate ���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param tableName �e�[�u����
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static void truncate(final String database, final String tableName)
        throws SQLException
    {
        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn = getConnection(database);
            stmt = conn.createStatement();

            // �f�[�^���폜����
            String sql = "truncate table " + tableName;
            stmt.execute(sql);
        }
        finally
        {
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * �w�肵���e�[�u���� CHECK �������X�V���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param tableName �e�[�u�����i�C���f�b�N�X���܂߂��e�[�u�����j
     * @param tableIndex �e�[�u���C���f�b�N�X
     * @param column ���������J������
     * @param year �N
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static void alterCheckConstraint(final String database, final String tableName,
            final int tableIndex, final String column, final int year)
        throws SQLException
    {
        Connection conn = null;
        Statement stmt = null;

        String checkConstraintName = DBInitializer.createCheckConstraintName(tableName, column);

        try
        {
            conn = getConnection(database);
            stmt = conn.createStatement();

            // CHECK�������폜����
            String sqlToDropCheck =
                    "ALTER TABLE " + tableName + " DROP CONSTRAINT " + checkConstraintName
                            + " RESTRICT";
            stmt.execute(sqlToDropCheck);

            // CHECK�������X�V����
            String checkConstraint =
                    DBInitializer.createCheckConstraintText(column, tableIndex, year);
            String sqlToCreateCheck =
                    String.format("ALTER TABLE %s ADD CONSTRAINT %s %s", tableName,
                                  checkConstraintName, checkConstraint);
            stmt.execute(sqlToCreateCheck);
        }
        finally
        {
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * ���R�[�h�̐���Ԃ��܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @param table �e�[�u����
     * @param notNullKey NULL �łȂ��L�[
     * @return ���R�[�h�̐�
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static int count(final String database, final String table, final String notNullKey)
        throws SQLException
    {
        int count = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql = "select count(" + notNullKey + ") from " + table;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next() == true)
            {
                count = rs.getInt(1);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }
        return count;
    }

    /**
     * �w�肳�ꂽ�V�[�P���X������l�𐶐����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param sequenceName �V�[�P���X��
     * @return �l�̐����ɐ��������ꍇ�͐������ꂽ�l�A���s�����ꍇ�� <code>-1</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    protected static int createValueFromSequenceId(final String database, final String sequenceName)
        throws SQLException
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int newMeasurementItemId = -1;
        try
        {
            conn = getConnection(database);
            stmt = conn.createStatement();
            String sql = ConnectionManager.getInstance().getSequenceSql(sequenceName);
            rs = stmt.executeQuery(sql);
            if (rs.next() == true)
            {
                newMeasurementItemId = rs.getInt(1);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }
        return newMeasurementItemId;
    }
}
