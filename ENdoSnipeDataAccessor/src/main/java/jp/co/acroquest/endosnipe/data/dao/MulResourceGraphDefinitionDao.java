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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.entity.MultipleResourceGraphInfo;

/**
 * {@link MulResourceGraphDefinitionDao} のための DAO です。
 *
 * @author pin
 */
public class MulResourceGraphDefinitionDao extends AbstractDao implements TableNames
{

    /**
     * レコードをすべて取得します。<br />
     * 
     * @param database データベース名
     * @param regExp regularexpression
     * @return {@link MultipleResourceGraphInfo} のリスト
     * @throws SQLException SQL 実行時に例外が発生した場合
     */
    public static List<MultipleResourceGraphInfo> selectMatchPattern(final String database,
        final String regExp)
        throws SQLException
    {
        List<MultipleResourceGraphInfo> result = new ArrayList<MultipleResourceGraphInfo>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "select * from " + MULTIPLE_RESOURCE_GRAPH
                    + " where ? ~* MEASUREMENT_ITEM_PATTERN order by MULTIPLE_RESOURCE_GRAPH_NAME";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, regExp);
            rs = pstmt.executeQuery();
            while (rs.next() == true)
            {
                MultipleResourceGraphInfo item = new MultipleResourceGraphInfo();
                // CHECKSTYLE:OFF
                item.multipleResourceGraphId_ = rs.getInt(1);
                item.multipleResourceGraphName_ = rs.getString(2);
                item.measurementItemIdList_ = rs.getString(3);
                item.measurementItemPattern_ = rs.getString(4);
                // CHECKSTYLE:ON
                result.add(item);
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
     * Update measurementNameList by matching with ID.<br />
     * 
     * @param database データベース名
     * @param measurementItemNameMap measurementItemNameMap
     * @throws SQLException SQL 実行時に例外が発生した場合
     */
    public static void updateItemListById(final String database,
        Map<Long, String> measurementItemNameMap)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {

            conn = getConnection(database, true);
            String sql =
                "update " + MULTIPLE_RESOURCE_GRAPH
                    + " set MEASUREMENT_ITEM_ID_LIST = ? where MULTIPLE_RESOURCE_GRAPH_ID = ?";
            pstmt = conn.prepareStatement(sql);

            PreparedStatement delegated = getDelegatingStatement(pstmt);

            for (Entry<Long, String> measurementItemName : measurementItemNameMap.entrySet())
            {
                delegated.setString(1, measurementItemName.getValue());
                delegated.setLong(2, measurementItemName.getKey());
                delegated.addBatch();
            }
            pstmt.executeBatch();

        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * get not used record from database。<br />
     * 
     * @param database データベース名
     * @return measurementNameList List<String>
     * @throws SQLException SQL 実行時に例外が発生した場合
     */
    public static List<String> selectOnNotUsedRecord(final String database)
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
                "select MEASUREMENT_ITEM_NAME from " + JAVELIN_MEASUREMENT_ITEM + " items "
                    + "where not exists( select vals.measurement_item_id from " + MEASUREMENT_VALUE
                    + " vals " + "where items.measurement_item_id = vals.measurement_item_id)";

            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                result.add(rs.getString(1));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

}
