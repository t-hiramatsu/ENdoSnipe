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
import java.util.Map;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;

/**
 * {@link JavelinMeasurementItem} �̂��߂� DAO �ł��B
 *
 * @author y-sakamoto
 */
public class JavelinMeasurementItemDao extends AbstractDao implements TableNames
{
    /** ���s�R�[�h�̑�֕����B */
    private static final String ALTERNATE_LINE_FEED_CODE = " ";

    /**
     * ���ځi�n��j���̂̃��R�[�h��ǉ����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param item �}������l
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final JavelinMeasurementItem item)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database);
            pstmt =
                conn.prepareStatement("insert into " + JAVELIN_MEASUREMENT_ITEM
                    + " (MEASUREMENT_ITEM_NAME, LAST_INSERTED)" + " values (?,?)");
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            // CHECKSTYLE:OFF
            String measurementItemName = item.itemName;

            delegated.setString(1, measurementItemName);
            delegated.setTimestamp(2, item.lastInserted);
            // CHECKSTYLE:ON
            delegated.execute();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * �w�肳�ꂽ�v������ ID �̌v�����ڏ����擾���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementItemId �v������ ID
     * @return ���R�[�h�����݂���ꍇ�͌v�����ڏ��I�u�W�F�N�g�A���݂��Ȃ��ꍇ�� <code>null</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static JavelinMeasurementItem selectById(final String database,
        final int measurementItemId)
        throws SQLException
    {
        Connection conn = null;
        JavelinMeasurementItem result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select * from " + JAVELIN_MEASUREMENT_ITEM + " where MEASUREMENT_ITEM_ID = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setInt(1, measurementItemId);
            rs = delegated.executeQuery();

            if (rs.next() == true)
            {
                result = new JavelinMeasurementItem();
                // CHECKSTYLE:OFF
                result.measurementItemId = rs.getInt(1);
                result.itemName = rs.getString(2);
                result.lastInserted = rs.getTimestamp(3);
                // CHECKSTYLE:ON
            }
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
     * �w�肳�ꂽItemName�z���̌v�����ږ��̈ꗗ���擾���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementItemName �v�����ږ�
     * @return �v�����ږ��̈ꗗ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<String> selectItemNameListByParentItemName(final String database,
        final String measurementItemName)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> itemNameList = new ArrayList<String>();

        try
        {
            conn = getConnection(database, true);
            String sql =
                "select MEASUREMENT_ITEM_NAME from " + JAVELIN_MEASUREMENT_ITEM
                    + " where replace(replace(replace(MEASUREMENT_ITEM_NAME,chr(13)"
                    + "||chr(10),' '),chr(13),' '),chr(10),' ') LIKE ? "
                    + "order by MEASUREMENT_ITEM_NAME";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement preparedStatement = getDelegatingStatement(pstmt);
            String tempStr = measurementItemName + "%";
            preparedStatement.setString(1, tempStr);
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                String itemName = rs.getString(1);
                // ���s�R�[�h��ϊ�����
                itemName = itemName.replaceAll("\\r\\n", ALTERNATE_LINE_FEED_CODE);
                itemName = itemName.replaceAll("\\r", ALTERNATE_LINE_FEED_CODE);
                itemName = itemName.replaceAll("\\n", ALTERNATE_LINE_FEED_CODE);
                // CHECKSTYLE:OFF
                itemNameList.add(itemName);
                // CHECKSTYLE:ON
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return itemNameList;
    }

    /**
     * �w�肳�ꂽ�v���l��ʂƍ��ږ��̂̃��R�[�h�́@ID ��Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param itemName ���ځi�n��j����
     * @return ���R�[�h�����݂���ꍇ�̓��R�[�h�� ID �A���݂��Ȃ��ꍇ�� <code>-1</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static int selectMeasurementItemIdFromItemName(final String database,
        final String itemName)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int itemId = -1;
        try
        {
            conn = getConnection(database, true);
            pstmt =
                conn.prepareStatement("select MEASUREMENT_ITEM_ID from " + JAVELIN_MEASUREMENT_ITEM
                    + " where replace(replace(replace(MEASUREMENT_ITEM_NAME,chr(13)||"
                    + "chr(10),' '),chr(13),' '),chr(10),' ') = ?");
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            String measurementItemID = itemName;
            delegated.setString(1, measurementItemID);
            rs = delegated.executeQuery();
            if (rs.next() == true)
            {
                itemId = rs.getInt(1);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return itemId;
    }

    /**
     * ���R�[�h�����ׂĎ擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @return {@link JavelinMeasurementItem} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinMeasurementItem> selectAll(final String database)
        throws SQLException
    {
        List<JavelinMeasurementItem> result = new ArrayList<JavelinMeasurementItem>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql =
                "select * from " + JAVELIN_MEASUREMENT_ITEM + " order by MEASUREMENT_ITEM_NAME";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                JavelinMeasurementItem item = new JavelinMeasurementItem();
                // CHECKSTYLE:OFF
                item.measurementItemId = rs.getInt(1);
                item.itemName = rs.getString(2);
                item.lastInserted = rs.getTimestamp(3);
                // CHECKSTYLE:ON
                result.add(item);
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
     * ���R�[�h�����ׂĎ擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @param maxCount �ő吔
     * @return {@link JavelinMeasurementItem} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinMeasurementItem> selectAll(final String database, int maxCount)
        throws SQLException
    {
        List<JavelinMeasurementItem> result = new ArrayList<JavelinMeasurementItem>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql =
                "select * from " + JAVELIN_MEASUREMENT_ITEM + " order by MEASUREMENT_ITEM_NAME";
            rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next() == true)
            {
                if (maxCount <= count)
                {
                    break;
                }
                count++;
                JavelinMeasurementItem item = new JavelinMeasurementItem();
                // CHECKSTYLE:OFF
                item.measurementItemId = rs.getInt(1);
                item.itemName = rs.getString(2);
                item.lastInserted = rs.getTimestamp(3);
                // CHECKSTYLE:ON
                result.add(item);
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
     * ���ږ������ׂĎ擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @return {@link JavelinMeasurementItem} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<String> selectAllItemName(final String database)
        throws SQLException
    {
        List<String> result = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql =
                "select MEASUREMENT_ITEM_NAME from " + JAVELIN_MEASUREMENT_ITEM
                    + " order by MEASUREMENT_ITEM_NAME";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                String itemName = rs.getString(1);
                // ���s�R�[�h��ϊ�����
                itemName = itemName.replaceAll("\\r\\n", ALTERNATE_LINE_FEED_CODE);
                itemName = itemName.replaceAll("\\r", ALTERNATE_LINE_FEED_CODE);
                itemName = itemName.replaceAll("\\n", ALTERNATE_LINE_FEED_CODE);
                // CHECKSTYLE:OFF
                result.add(itemName);
                // CHECKSTYLE:ON
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
     * �w�肵�����ږ��̃��R�[�h��S�Ď擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @param measurementType ���ږ�
     * @return {@link JavelinMeasurementItem} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinMeasurementItem> selectByMeasurementType(final String database,
        long measurementType)
        throws SQLException
    {
        List<JavelinMeasurementItem> result = new ArrayList<JavelinMeasurementItem>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select * from " + JAVELIN_MEASUREMENT_ITEM + " where MEASUREMENT_ITEM_ID=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, measurementType);

            rs = pstmt.executeQuery();
            while (rs.next() == true)
            {
                JavelinMeasurementItem item = new JavelinMeasurementItem();
                // CHECKSTYLE:OFF
                item.measurementItemId = rs.getInt(1);
                item.itemName = rs.getString(2);
                item.lastInserted = rs.getTimestamp(3);
                // CHECKSTYLE:ON
                result.add(item);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
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
     * �w�肳�ꂽ MEASUREMENT_ITEM_ID �̃��R�[�h���폜���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementItemName �폜���郌�R�[�h�� MEASUREMENT_ITEM_NAME
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteByMeasurementItemId(final String database,
        final String measurementItemName)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "delete from " + JAVELIN_MEASUREMENT_ITEM
                    + " where replace(replace(replace(MEASUREMENT_ITEM_NAME,chr(13)||"
                    + "chr(10),' '),chr(13),' '),chr(10),' ') = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setString(1, measurementItemName);
            pstmt.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * �w�肳�ꂽ MEASUREMENT_ITEM_ID �̃��R�[�h���폜���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementItemIdList �폜���郌�R�[�h�� MEASUREMENT_ITEM_ID
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteByMeasurementItemId(final String database,
        final List<Integer> measurementItemIdList)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database, true);
            conn.setAutoCommit(false);

            String sql =
                "delete from " + JAVELIN_MEASUREMENT_ITEM + " where MEASUREMENT_ITEM_ID = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);

            for (Integer measurementItemId : measurementItemIdList)
            {
                delegated.setInt(1, measurementItemId);
                delegated.addBatch();
            }
            pstmt.executeBatch();

            conn.commit();
        }
        catch (SQLException sqle)
        {
            if (conn != null)
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
    }

    /**
     * �v���f�[�^���}�����ꂽ�����𔽉f���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param beforeItemName
     *            �X�V�O��measurement_item_name
     * @param afterItemName
     *            �X�V�O��measurement_item_name
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void updateMeasurementItemName(final String database,
        final String beforeItemName, final String afterItemName)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "update " + JAVELIN_MEASUREMENT_ITEM + " set MEASUREMENT_ITEM_NAME = ?"
                    + " where replace(replace(replace(MEASUREMENT_ITEM_NAME,chr(13)||"
                    + "chr(10),' '),chr(13),' '),chr(10),' ') = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setString(1, afterItemName);
            delegated.setString(2, beforeItemName);
            delegated.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * �v���f�[�^���}�����ꂽ�����𔽉f���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param map �n��ITEM�ƍŏI�}�������̃}�b�v
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void updateLastInserted(final String database, final Map<String, Timestamp> map)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "update " + JAVELIN_MEASUREMENT_ITEM + " set LAST_INSERTED = ?"
                    + " where replace(replace(replace(MEASUREMENT_ITEM_NAME,chr(13)||"
                    + "chr(10),' '),chr(13),' '),chr(10),' ') = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            for (Map.Entry<String, Timestamp> entry : map.entrySet())
            {
                delegated.setTimestamp(1, entry.getValue());
                delegated.setString(2, entry.getKey());
                delegated.addBatch();
            }
            pstmt.executeBatch();
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
        deleteAll(database, JAVELIN_MEASUREMENT_ITEM);
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
        int count = count(database, JAVELIN_MEASUREMENT_ITEM, "MEASUREMENT_ITEM_NAME");
        return count;
    }

    /**
     * measurement_value�e�[�u���Ŏg�p����Ă��Ȃ����R�[�h���A<br />
     * ���̃e�[�u������폜���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @throws SQLException SQL���s���ɗ�O�����������ꍇ
     */
    public static void deleteNotUsedRecord(final String database)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection(database, true);
            String sql =
                "delete from " + JAVELIN_MEASUREMENT_ITEM + " items "
                    + "where not exists( select vals.measurement_item_id from " + MEASUREMENT_VALUE
                    + " vals " + "where items.measurement_item_id = vals.measurement_item_id)";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }
}
