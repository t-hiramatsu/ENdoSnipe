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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.entity.ArchivedValue;

/**
 * {@link ArchivedValue} �̂��߂� DAO �ł��B
 *
 * @author y-sakamoto
 */
public class ArchivedValueDao extends AbstractDao
{
    /** ARCHIVED_VALUE �e�[�u�����B */
    private static final String ARCHIVED_VALUE_TABLE = "ARCHIVED_VALUE";

    /** �v�� No. �̒l�𐶐�����V�[�P���X���B */
    private static final String SEQ_MEASUREMENT_NUM = "SEQ_MEASUREMENT_NUM";

    /**
     * ���R�[�h��}�����܂��B<br />
     *
     * {@link ArchivedValue#measurementValueId} �͎g�p����܂���B
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementValue �}������l
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, ArchivedValue measurementValue)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database);
            String sql =
                    "insert into " + ARCHIVED_VALUE_TABLE + "(" + "MEASUREMENT_NUM, " + "HOST_ID, "
                            + "MEASUREMENT_TIME, " + "MEASUREMENT_TYPE, " + "MEASUREMENT_ITEM_ID, "
                            + "VALUE" + ")" + " values (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setLong(1, measurementValue.measurementNum);
            pstmt.setInt(2, measurementValue.hostId);
            pstmt.setTimestamp(3, measurementValue.measurementTime);
            pstmt.setInt(4, measurementValue.measurementType);
            pstmt.setInt(5, measurementValue.measurementItemId);
            pstmt.setObject(6, measurementValue.value);
            // CHECKSTYLE:ON
            pstmt.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * ���ׂẴ��R�[�h���擾���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return {@link ArchivedValue} �I�u�W�F�N�g�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<ArchivedValue> selectAll(final String database)
        throws SQLException
    {
        List<ArchivedValue> result = new ArrayList<ArchivedValue>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql = "select * from " + ARCHIVED_VALUE_TABLE + " order by MEASUREMENT_TIME";
            rs = stmt.executeQuery(sql);
            getMeasurementValuesFromResultSet(result, rs);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }

        return result;
    }

    /**
     * ���Ԃ��w�肵�ă��R�[�h���擾���܂��B<br />
     *
     * ���R�[�h�͎����ŏ����ɕ��בւ��ĕԂ��܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @return {@link ArchivedValue} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<ArchivedValue> selectByTerm(final String database, final Timestamp start,
            final Timestamp end)
        throws SQLException
    {
        List<ArchivedValue> result = new ArrayList<ArchivedValue>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                    "select * from " + ARCHIVED_VALUE_TABLE + " where "
                            + "MEASUREMENT_TIME >= ? and MEASUREMENT_TIME <= ? "
                            + "order by MEASUREMENT_TIME";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, start);
            pstmt.setTimestamp(2, end);
            rs = pstmt.executeQuery();
            getMeasurementValuesFromResultSet(result, rs);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return result;
    }

    /**
     * {@link ResultSet} ���� {@link ArchivedValue} �̃��X�g���쐬���܂��B<br />
     *
     * @param result {@link ArchivedValue} �I�u�W�F�N�g�̊i�[��
     * @param rs {@link ResultSet}
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     */
    private static void getMeasurementValuesFromResultSet(List<ArchivedValue> result, ResultSet rs)
        throws SQLException
    {
        while (rs.next() == true)
        {
            ArchivedValue measurementValue = new ArchivedValue();
            // CHECKSTYLE:OFF
            measurementValue.measurementValueId = rs.getLong(1);
            measurementValue.measurementNum = rs.getLong(2);
            measurementValue.hostId = rs.getInt(3);
            measurementValue.measurementTime = rs.getTimestamp(4);
            measurementValue.measurementType = rs.getInt(5);
            measurementValue.measurementItemId = rs.getInt(6);
            measurementValue.value = rs.getBigDecimal(7);
            // CHECKSTYLE:ON
            result.add(measurementValue);
        }
    }

    /**
     * Javelin �v���l�e�[�u���ɓo�^����Ă���f�[�^�̍ŏ������ƍő厞����Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return Javelin �v���l�e�[�u���ɓo�^����Ă���f�[�^�� (�ŏ�����, �ő厞��) ��\���z��
     *         �i���s�����ꍇ�A���ꂼ��̗v�f�� <code>null</code> �j
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static Timestamp[] getTerm(final String database)
        throws SQLException
    {
        Timestamp[] result = new Timestamp[2];
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql =
                    "select min(MEASUREMENT_TIME), max(MEASUREMENT_TIME) from "
                            + ARCHIVED_VALUE_TABLE;
            rs = stmt.executeQuery(sql);
            if (rs.next() == true)
            {
                result[0] = rs.getTimestamp(1);
                result[1] = rs.getTimestamp(2);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }

        return result;
    }
    
    /**
     * �������w�肵�āA������Â����R�[�h���폜���܂��B
     * �폜���������̃L�[�Ƃ��ẮA�v����������Ƃ��܂��B
     * 
     * @param database �f�[�^�x�[�X��
     * @param deleteLimit �폜��������
     * @param hostId �z�X�gID
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteOldRecordByTime(final String database, final Timestamp deleteLimit,
            final int hostId)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection(database, true);
            String sql = "delete from " + ARCHIVED_VALUE_TABLE + " where MEASUREMENT_TIME <= ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, deleteLimit);
            pstmt.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * ���ׂẴ��R�[�h���폜���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteAll(final String database)
        throws SQLException
    {
        deleteAll(database, ARCHIVED_VALUE_TABLE);
    }

    /**
     * ���R�[�h�̐���Ԃ��܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @return ���R�[�h�̐�
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static int count(final String database)
        throws SQLException
    {
        int count = count(database, ARCHIVED_VALUE_TABLE, "MEASUREMENT_VALUE_ID");
        return count;
    }

    /**
     * �v�� No. �̒l�𐶐����܂��B <br />
     *
     * @param database �f�[�^�x�[�X��
     * @return �v�� No. �̒l
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static int createMeasurementNum(final String database)
        throws SQLException
    {
        int value = createValueFromSequenceId(database, SEQ_MEASUREMENT_NUM);
        return value;
    }

}
