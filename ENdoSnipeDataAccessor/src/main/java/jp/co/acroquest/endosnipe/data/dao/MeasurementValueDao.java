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
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.data.entity.MeasurementValue;
import jp.co.acroquest.endosnipe.util.ResourceDataDaoUtil;

import org.apache.log4j.Logger;

/**
 * {@link MeasurementValue} �̂��߂� DAO �ł��B
 *
 * @author sakamoto
 */
public class MeasurementValueDao extends AbstractDao implements TableNames
{
    /** �����Ń\�[�g����SQL */
    private static final String WITH_NAME_ACCESS_SQL_TIME_ORDER =
            "SELECT mv.measurement_value_id measurement_value_id," +
            " mv.measurement_num measurement_num," +
            " mv.measurement_time measurement_time, mv.measurement_type measurement_type," +
            " mv.measurement_item_id measurement_item_id, mv.value resultvalue," +
            " mi.item_name measurement_type_name," +
            " mi.display_name measurement_display_name," +
            " ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi," +
            " javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? ) ORDER BY mv.measurement_time";

    private static final String WITH_NAME_SUM_VALUE_ACCESS_SQL_TIME_ORDER =
            "SELECT 0 measurement_value_id, 0 measurement_num," +
            " mv.measurement_time measurement_time," +
            " mv.measurement_type measurement_type," +
            " mv.measurement_item_id measurement_item_id," +
            " sum(mv.value) resultvalue, mi.item_name measurement_type_name," +
            " mi.display_name measurement_display_name, ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? )" +
            " GROUP BY mv.measurement_type, mv.measurement_item_id," +
            " mv.measurement_time, mi.item_name, mi.display_name," +
            " ji.item_name ORDER BY mv.measurement_time";

    /** ���ږ��Ń\�[�g����SQL */
    private static final String WITH_NAME_ACCESS_SQL =
            "SELECT mv.measurement_value_id measurement_value_id," +
            " mv.measurement_num measurement_num," +
            " mv.measurement_time measurement_time, mv.measurement_type measurement_type," +
            " mv.measurement_item_id measurement_item_id, mv.value resultvalue," +
            " mi.item_name measurement_type_name, mi.display_name measurement_display_name," +
            " ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? ) ORDER BY ji.item_name";

    private static final String WITH_NAME_MAX_VALUE_ACCESS_SQL =
            "SELECT 0 measurement_value_id, 0 measurement_num, '1970-01-01' measurement_time," +
            " mv.measurement_type measurement_type, mv.measurement_item_id measurement_item_id," +
            " max(mv.value) resultvalue, mi.item_name measurement_type_name," +
            " mi.display_name measurement_display_name, ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? )" +
            " GROUP BY mv.measurement_type, mv.measurement_item_id," +
            " mi.item_name, mi.display_name, ji.item_name ORDER BY ji.item_name";

    private static final String WITH_NAME_MIN_VALUE_ACCESS_SQL =
            "SELECT 0 measurement_value_id, 0 measurement_num, '1970-01-01' measurement_time," +
            " mv.measurement_type measurement_type, mv.measurement_item_id measurement_item_id," +
            " min(mv.value) resultvalue, mi.item_name measurement_type_name," +
            " mi.display_name measurement_display_name, ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? )" +
            " GROUP BY mv.measurement_type, mv.measurement_item_id," +
            " mi.item_name, mi.display_name," +
            " ji.item_name ORDER BY ji.item_name";

    private static final String WITH_NAME_SUM_VALUE_ACCESS_SQL =
            "SELECT 0 measurement_value_id, 0 measurement_num, '1970-01-01' measurement_time," +
            " mv.measurement_type measurement_type, mv.measurement_item_id measurement_item_id," +
            " sum(mv.value) resultvalue, mi.item_name measurement_type_name," +
            " mi.display_name measurement_display_name, ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? )" +
            " GROUP BY mv.measurement_type, mv.measurement_item_id, mi.item_name," +
            " mi.display_name, ji.item_name" +
            " ORDER BY ji.item_name";

    /** �n�񖼈ꗗ���擾����SQL */
    private static final String WITH_NAME_ITEM_NAME =
        "SELECT 0 measurement_value_id, 0 measurement_num, '1970-01-01' measurement_time," +
        " 0 measurement_type, 0 measurement_item_id, 0 resultvalue, '0' measurement_type_name," +
        " '0' measurement_display_name, ji.item_name measurement_item_name" +
        " FROM measurement_info mi, javelin_measurement_item ji" +
        " WHERE mi.item_name = ? AND ji.measurement_type = mi.measurement_type";

    /**
     * �����Ń\�[�g����SQL
     * �n�񖼂��w�肷��
     */
    private static final String WITH_ITEM_NAME_ACCESS_SQL_TIME_ORDER =
            "SELECT mv.measurement_value_id measurement_value_id," +
            " mv.measurement_num measurement_num," +
            " mv.measurement_time measurement_time, mv.measurement_type measurement_type," +
            " mv.measurement_item_id measurement_item_id, mv.value resultvalue," +
            " mi.item_name measurement_type_name, mi.display_name measurement_display_name," +
            " ji.item_name measurement_item_name" +
            " FROM measurement_value mv, measurement_info mi, javelin_measurement_item ji" +
            " WHERE mi.item_name = ? AND mv.measurement_type = mi.measurement_type" +
            " AND ji.item_name = ? AND mv.measurement_item_id = ji.measurement_item_id" +
            " AND ( mv.measurement_time BETWEEN ? AND ? ) ORDER BY mv.measurement_time";

    /** �~�ϊ��Ԃ��擾���� SQL */
    private static final String GET_TERM_SQL_PARTIOTION = createGetTermSql();

    /** �~�ϊ��Ԃ��擾���� SQL */
    private static final String GET_TERM_SQL =
    	"select min(MEASUREMENT_TIME) MIN_TIME, max(MEASUREMENT_TIME) MAX_TIME from " +
        MEASUREMENT_VALUE;

    /**
     * �f�[�^��}������e�[�u���̖��O��Ԃ��܂��B
     *
     * @param date �}������f�[�^�̓��t
     * @return �e�[�u����
     */
    public static String getTableNameToInsert(final Date date)
    {
        String tableName = ResourceDataDaoUtil.getTableNameToInsert(date, MEASUREMENT_VALUE);
        return tableName;
    }

    /**
     * ���R�[�h��}�����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementValue �}������l
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final MeasurementValue measurementValue)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String tableName = getTableNameToInsert(measurementValue.measurementTime);
        try
        {
            conn = getConnection(database);
            String sql = "insert into " + tableName +
                         " (MEASUREMENT_TIME, MEASUREMENT_ITEM_ID, MEASUREMENT_VALUE)" +
                         " values (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, measurementValue.measurementTime);
            pstmt.setInt(2, measurementValue.measurementItemId);
            pstmt.setObject(3, measurementValue.value);
            
            Logger logger = Logger.getLogger(MeasurementValueDao.class);
            if (logger.isDebugEnabled())
            {
                logger.debug("MeasurementValueDto: SQL=[" + sql + "]" +
                             ", 1=" + measurementValue.measurementTime +
                             ", 2=" + measurementValue.measurementItemId +
                             ", 3=" + measurementValue.value);
            }
           
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
     * �w�肵���C���f�b�N�X�̃e�[�u���� truncate ���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param tableIndex �e�[�u���C���f�b�N�X
     * @param year ���ɂ��̃e�[�u���ɓ����f�[�^�̔N
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void truncate(final String database, final int tableIndex, final int year)
        throws SQLException
    {
        String tableName = String.format("%s_%02d", MEASUREMENT_VALUE, tableIndex);
        truncate(database, tableName);
        alterCheckConstraint(database, tableName, tableIndex, "MEASUREMENT_TIME", year);
    }

    /**
     * ���ׂẴ��R�[�h���擾���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return {@link MeasurementValue} �I�u�W�F�N�g�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValue> selectAll(final String database)
        throws SQLException
    {
        List<MeasurementValue> result = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);

            stmt = conn.createStatement();
            String sql = "select * from " + MEASUREMENT_VALUE + " order by MEASUREMENT_TIME";
            rs = stmt.executeQuery(sql);
            result = getMeasurementValuesFromResultSet(rs);
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
     * @return {@link MeasurementValue} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValue> selectByTerm(final String database, final Timestamp start,
            final Timestamp end)
        throws SQLException
    {
        List<MeasurementValue> result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                    "select * from " + MEASUREMENT_VALUE + " where "
                            + "MEASUREMENT_TIME >= ? and MEASUREMENT_TIME <= ?"
                            + "order by MEASUREMENT_TIME";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, start);
            pstmt.setTimestamp(2, end);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValuesFromResultSet(rs);
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
     * ���Ԃ��w�肵�āA����̃O���t�̃��R�[�h���擾���܂��B<br />
     *
     * ���R�[�h�͎����ŏ����ɕ��בւ��ĕԂ��܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @param measurementType �v���l���
     * @return {@link MeasurementValue} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndMeasurementType(final String database,
            final Timestamp start, final Timestamp end, final long measurementType)
        throws SQLException
    {
        List<MeasurementValueDto> result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select " +
                ", mv.measurement_time" +
                ", mv.measurement_item_id" +
                ", mv.value resultvalue" +
                ", jmi.item_name measurement_item_name" +
                " from " + MEASUREMENT_VALUE + " mv, " +
                JAVELIN_MEASUREMENT_ITEM + " jmi" +
                " where"
                    + "mv.measurement_item_id = jmi.measurement_item_id "
                    + " and (mv.MEASUREMENT_TIME between ? and ?)"
                    + " and jmi.MEASUREMENT_ITEM_ID = ?"
                    + " order by mv.MEASUREMENT_TIME";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, start);
            pstmt.setTimestamp(2, end);
            pstmt.setLong(3, measurementType);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * ���Ԃƌn�񖼂��w�肵�āA����̃O���t�̃��R�[�h���擾���܂��B<br />
     * JMX�p�����[�^��\�����邽�߂̐�p���\�b�h�ł��B
     *
     * ���R�[�h�͎����ŏ����ɕ��בւ��ĕԂ��܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @param itemName ���ږ�
     * @return {@link MeasurementValue} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndJMXItemName(final String database,
            final Timestamp start, final Timestamp end, final String itemName)
        throws SQLException
    {
        List<MeasurementValueDto> result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select " +
                " mv.measurement_value_id" +
                ", mv.measurement_num" +
                ", mv.measurement_time" +
                ", mv.measurement_type" +
                ", mv.measurement_item_id" +
                ", mv.value resultvalue" +
                ", jmi.item_name measurement_item_name" +
                ", 'jmx' || '' measurement_type_name" +
                ", jmi.item_name measurement_display_name" +
                " from " + MEASUREMENT_VALUE + " mv, " + JAVELIN_MEASUREMENT_ITEM
                    + " jmi where"
                    + " mv.measurement_item_id = jmi.measurement_item_id "
                    + " and mv.measurement_type = jmi.measurement_type "
                    + " and (mv.MEASUREMENT_TIME between ? and ?)"
                    + " and mv.MEASUREMENT_TYPE > 255"
                    + " and jmi.ITEM_NAME like ?"
                    + " order by mv.MEASUREMENT_TIME";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, start);
            pstmt.setTimestamp(2, end);
            pstmt.setString(3, itemName +"%");
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return result;
    }

    /** ���Ԃƍ��ږ����w�肵�Čv���l�̌n����擾����SQL�B */
    private static final String SQL_SELECT_BY_TERM_AND_MEASUREMENT_ITEM_NAME =
        "SELECT jmi.measurement_item_name," +
        "       mv.measurement_item_id," +
        "       mv.measurement_time," +
        "       mv.measurement_value" +
        "  FROM measurement_value mv, javelin_measurement_item jmi" +
        "  WHERE mv.measurement_item_id = jmi.measurement_item_id" +
        "    AND (mv.measurement_time BETWEEN ? and ?)" +
        "    AND jmi.measurement_item_name LIKE ?" +
        "  ORDER BY mv.measurement_time, measurement_item_name";

    /**
     * ���Ԃƍ��ږ����w�肵�āA����̃O���t�̃��R�[�h���擾���܂��B<br />
     * ���R�[�h�͎����ŏ����ɕ��בւ��ĕԂ��܂��B
     *
     * @param database �f�[�^�x�[�X���B
     * @param start �J�n�����B
     * @param end �I�������B
     * @param measurementItemName �v�����ږ��B
     * @return {@link MeasurementValueDto} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndMeasurementItemName(String database,
            Date start, Date end, String measurementItemName)
        throws SQLException
    {
        List<MeasurementValueDto> result = null;
        
        // Date �� Timestamp�ւ̕ϊ�
        Timestamp tsStart = new Timestamp(start.getTime());
        Timestamp tsEnd   = new Timestamp(end.getTime());

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            pstmt = conn.prepareStatement(SQL_SELECT_BY_TERM_AND_MEASUREMENT_ITEM_NAME);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, tsStart);
            pstmt.setTimestamp(2, tsEnd);
            pstmt.setString(3, measurementItemName);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * �����͈̔͂��w�肵�āA����̃O���t�̃��R�[�h���擾���܂��B<br />
     *
     * ���R�[�h�͎����ŏ����ɕ��בւ��ĕԂ��܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param startTime �J�n����
     * @param endTime �I������
     * @param measurementType �v���l���
     * @return {@link MeasurementValue} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTimeAndMeasurementType(final String database,
            final Timestamp startTime, final Timestamp endTime, final long measurementType)
        throws SQLException
    {
        List<MeasurementValueDto> result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select " +
                " mv.measurement_value_id" +
                ", mv.measurement_num" +
                ", mv.measurement_time" +
                ", mv.measurement_type" +
                ", mv.measurement_item_id" +
                ", mv.value resultvalue" +
                ", jmi.item_name measurement_item_name" +
                ", mi.item_name measurement_type_name" +
                ", mi.display_name measurement_display_name" +
                " from " + MEASUREMENT_VALUE + " mv, " + JAVELIN_MEASUREMENT_ITEM
                    + " jmi, " + MEASUREMENT_INFO + " mi where"
                    + " mv.measurement_type = mi.measurement_type "
                    + " and mv.measurement_item_id = jmi.measurement_item_id "
                    + " and mv.measurement_type = jmi.measurement_type "
                    + " and (mv.MEASUREMENT_TIME between ? and ?)" + " and mv.MEASUREMENT_TYPE = ?"
                    + " order by mv.MEASUREMENT_TIME";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setTimestamp(1, startTime);
            pstmt.setTimestamp(2, endTime);
            pstmt.setLong(3, measurementType);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * ���ԁA����ьv���l��ʖ����w�肵�āA�v���l���擾���܂��B<br/>
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndMeasurementTypeWithName(String database,
            Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
                           database,
                           start,
                           end,
                           typeName,
                           MeasurementValueDao.WITH_NAME_ACCESS_SQL);
    }
    
    /**
     * �v���l��ʖ����w�肵�Čn�񖼈ꗗ���擾���܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByMeasurementTypeWithName(String database,
            String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByMeasurementTypeWithNameBase(
                 database,
                 typeName,
                 MeasurementValueDao.WITH_NAME_ITEM_NAME);
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA���n��Ń\�[�g�����v���l���擾���܂��B<br/>
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndMeasurementTypeWithNameOrderByTime(
            String database, Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
                      database,
                      start,
                      end,
                      typeName,
                      MeasurementValueDao.WITH_NAME_ACCESS_SQL_TIME_ORDER);
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA���n��Ń\�[�g�����v���l���擾���܂��B<br/>
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @param itemName ���������i�n�񖼁j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectByTermAndMeasurementTypeWithItemNameOrderByTime(
            String database, Timestamp start, Timestamp end, String typeName, String itemName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithItemNameBase(
                  database,
                  start,
                  end,
                  typeName,
                  itemName,
                  MeasurementValueDao.WITH_ITEM_NAME_ACCESS_SQL_TIME_ORDER );
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA�v���l�̌n�񖈂̍ő�l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectMaxValueByTermAndMeasurementTypeWithName(
            String database, Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
                  database,
                  start,
                  end,
                  typeName,
                  MeasurementValueDao.WITH_NAME_MAX_VALUE_ACCESS_SQL);
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA�v���l�̌n�񖈂̍ŏ��l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectMinValueByTermAndMeasurementTypeWithName(
            String database, Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
                                                                              database,
                                                                              start,
                                                                              end,
              typeName,
              MeasurementValueDao.WITH_NAME_MIN_VALUE_ACCESS_SQL);
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA�v���l�̌n�񖈂̍��v�l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     * ���v�l�Z�o�̊�́u�n�񖼁v�ɂȂ�܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectSumValueByTermAndMeasurementTypeWithName(
            String database, Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
              database,
              start,
              end,
              typeName,
              MeasurementValueDao.WITH_NAME_SUM_VALUE_ACCESS_SQL);
    }

    /**
     * ���ԁA����ьv���l��ʖ����w�肵�āA�v���l�̌n�񖈂̍��v�l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     * ���v�l�Z�o�̊�́u�v�������v�ɂȂ�܂��B
     *
     * @deprecated measurement_info�e�[�u���͔p�~����܂����B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MeasurementValueDto> selectSumValueByTermAndMeasurementTypeGroupingTime(
            String database, Timestamp start, Timestamp end, String typeName)
        throws SQLException
    {
        return MeasurementValueDao.selectByTermAndMeasurementTypeWithNameBase(
              database,
              start,
              end,
              typeName,
              MeasurementValueDao.WITH_NAME_SUM_VALUE_ACCESS_SQL_TIME_ORDER);

    }

    /**
     * ���ԁA����ьv���l��ʖ��A�����SQL�N�G�����w�肵�āA�v���l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @param sqlBase  ���s����SQL�N�G��
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private static List<MeasurementValueDto> selectByTermAndMeasurementTypeWithNameBase(
            String database, Timestamp start, Timestamp end, String typeName, String sqlBase)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MeasurementValueDto> result = null;
        try
        {
            conn = getConnection(database, true);
            pstmt = conn.prepareStatement(sqlBase);
            // CHECKSTYLE:OFF
            pstmt.setString(1, typeName);
            pstmt.setTimestamp(2, start);
            pstmt.setTimestamp(3, end);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * ���ԁA�v���l��ʖ��A�n�񖼂����SQL�N�G�����w�肵�āA�v���l���擾���܂��B
     * �擾�������ʂɂ́A�v���l��ʖ��A�v���l�n�񖼂�t�����܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param start    ���������i�J�n�����j
     * @param end      ���������i�I�������j
     * @param typeName ���������i�v���l��ʖ��j
     * @param typeName ���������i�n�񖼁j
     * @param sqlBase  ���s����SQL�N�G��
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private static List<MeasurementValueDto> selectByTermAndMeasurementTypeWithItemNameBase(
            String database, Timestamp start, Timestamp end,
            String typeName, String itemName, String sqlBase)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MeasurementValueDto> result = null;
        try
        {
            conn = getConnection(database, true);
            pstmt = conn.prepareStatement(sqlBase);
            // CHECKSTYLE:OFF
            pstmt.setString(1, typeName);
            pstmt.setString(2, itemName);
            pstmt.setTimestamp(3, start);
            pstmt.setTimestamp(4, end);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * �v���l��ʖ��A�����SQL�N�G�����w�肵�āA�n�񖼂��擾���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param typeName ���������i�v���l��ʖ��j
     * @param sqlBase  ���s����SQL�N�G��
     * @return ���������ɍ��v�����f�[�^�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private static List<MeasurementValueDto> selectByMeasurementTypeWithNameBase(
            String database, String typeName, String sqlBase)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<MeasurementValueDto> result = null;
        try
        {
            conn = getConnection(database, true);
            pstmt = conn.prepareStatement(sqlBase);
            // CHECKSTYLE:OFF
            pstmt.setString(1, typeName);
            // CHECKSTYLE:ON
            rs = pstmt.executeQuery();
            result = getMeasurementValueDtosFromResultSet(rs);
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
     * {@link ResultSet} ���� {@link MeasurementValue} �̃��X�g���쐬���܂��B<br />
     *
     * @param rs {@link ResultSet}
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     * @return ��������
     */
    private static List<MeasurementValue> getMeasurementValuesFromResultSet(final ResultSet rs)
        throws SQLException
    {
        List<MeasurementValue> result = new ArrayList<MeasurementValue>();

        while (rs.next() == true)
        {
            MeasurementValue measurementValue = new MeasurementValue();
            // CHECKSTYLE:OFF
            measurementValue.measurementTime = rs.getTimestamp(1);
            measurementValue.measurementItemId = rs.getInt(2);
            measurementValue.value = rs.getString(3);
            // CHECKSTYLE:ON
            result.add(measurementValue);
        }
        return result;
    }

    /**
     * {@link ResultSet}�C���X�^���X����A{@link MeasurementValueDto}�̃��X�g�𐶐����܂��B
     *
     * @param rs �f�[�^���܂܂�Ă���{@link ResultSet} �C���X�^���X
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     */
    private static List<MeasurementValueDto>
    	getMeasurementValueDtosFromResultSet(final ResultSet rs)
        throws SQLException
    {
        List<MeasurementValueDto> result = new ArrayList<MeasurementValueDto>();

        while (rs.next() == true)
        {
            MeasurementValueDto measurementValueDto = new MeasurementValueDto();

            measurementValueDto.measurementItemId = rs.getInt("measurement_item_id");
            measurementValueDto.measurementTime = rs.getTimestamp("measurement_time");
            measurementValueDto.value = rs.getString("measurement_value");
            measurementValueDto.measurementItemName = rs.getString("measurement_item_name");
            //measurementValueDto.measurementValueId = rs.getLong("measurement_value_id");
            //measurementValueDto.measurementNum = rs.getLong("measurement_num");
            //measurementValueDto.measurementType = rs.getInt("measurement_type");
            //measurementValueDto.value = rs.getBigDecimal("resultvalue");
            //measurementValueDto.measurementTypeItemName = rs.getString("measurement_type_name");
            //measurementValueDto.measurementTypeDisplayName =
            //        rs.getString("measurement_display_name");

            result.add(measurementValueDto);
        }
        return result;
    }

    /**
     * Javelin �v���l�e�[�u���ɓo�^����Ă���f�[�^�̍ŏ������ƍő厞����Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return Javelin �v���l�e�[�u���ɓo�^����Ă���f�[�^�� (�ŏ�����, �ő厞��) ��\���z��
     *         �i���s�����ꍇ�͋�̔z��j
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static Timestamp[] getTerm(final String database)
        throws SQLException
    {
        Timestamp[] result = new Timestamp[0];
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(getTermSql());
            if (rs.next() == true)
            {
                result = new Timestamp[2];
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
     * ���Ԃ��擾����SQL���擾����B
     * @return�@���Ԃ��擾����SQL
     */
    private static String getTermSql()
    {
        if(DBManager.isDefaultDb())
        {
            return GET_TERM_SQL;
        }
        return GET_TERM_SQL_PARTIOTION;
    }

    /**
     * �������w�肵�āA������Â����R�[�h���폜���܂��B
     * �폜���������̃L�[�Ƃ��ẮA�v����������Ƃ��܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param deleteLimit �폜��������
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteOldRecordByTime(final String database,
            final Timestamp deleteLimit)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection(database, true);
            String sql = "delete from " + MEASUREMENT_VALUE + " where MEASUREMENT_TIME <= ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setTimestamp(1, deleteLimit);
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
        deleteAll(database, MEASUREMENT_VALUE);
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
        int count = count(database, MEASUREMENT_VALUE, "MEASUREMENT_VALUE_ID");
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

    /**
     * �~�ϊ��Ԃ��擾���� SQL �𐶐����܂��B
     *
     * @return SQL
     */
    private static String createGetTermSql()
    {
        String unionSql = "";
        StringBuilder sql = new StringBuilder("select min(MIN_TIME), max(MAX_TIME) from (");
        for (int index = 1; index <= ResourceDataDaoUtil.PARTITION_TABLE_COUNT; index++)
        {
            sql.append(unionSql);
            String tableName = ResourceDataDaoUtil.getTableName(MEASUREMENT_VALUE, index);
            sql.append("select min(MEASUREMENT_TIME)" +
            		" MIN_TIME, max(MEASUREMENT_TIME) MAX_TIME from ");
            sql.append(tableName);
            unionSql = " union all ";
        }
        sql.append(") MERGED_TIME");
        return sql.toString();
    }

    /**
     * ���R�[�h��}�����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param tableName �e�[�u����
     * @param updateValueList �}������l
     * @return �}�����R�[�h
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static int insertBatch(String database, String tableName,
        List<MeasurementValue> updateValueList) throws SQLException
    {
        int insertCount = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database);
            conn.setAutoCommit(false);
            String sql = "insert into " + tableName +
                " (MEASUREMENT_TIME, MEASUREMENT_ITEM_ID, MEASUREMENT_VALUE)" +
                " values (?, ?, ?)";
  
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            
            for (MeasurementValue measurementValue : updateValueList)
            {
                // CHECKSTYLE:OFF
                pstmt.setTimestamp(1, measurementValue.measurementTime);
                pstmt.setInt(2, measurementValue.measurementItemId);
                pstmt.setObject(3, measurementValue.value);
                
                Logger logger = Logger.getLogger(MeasurementValueDao.class);
                if (logger.isDebugEnabled())
                {
                    logger.debug("MeasurementValueDto: SQL=[" + sql + "]" +
                                 ", 1=" + measurementValue.measurementTime +
                                 ", 2=" + measurementValue.measurementItemId +
                                 ", 3=" + measurementValue.value);
                }
                
                pstmt.addBatch();
            }
            // CHECKSTYLE:ON
            
            int[] executeCounts = pstmt.executeBatch();
            for (int executeCount : executeCounts)
            {
                insertCount += executeCount;
            }
            
            conn.commit();
        }
        catch(SQLException sqle)
        {
            if(conn != null)
            {
                conn.rollback();
            }
            throw sqle;
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        
        return insertCount;
    }
}
