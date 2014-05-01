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
import jp.co.acroquest.endosnipe.data.entity.MapInfo;

/**
 * {@link MapInfo} �̂��߂� DAO �ł��B
 *
 * @author fujii
 */
public class MapInfoDao extends AbstractDao
{
    private static final String MAP_INFO_TABLE = "MAP_INFO";
    
    private static final int SQL_DATA = 1;
    private static final int SQL_LAST_UPDATE = 2;
    private static final int SQL_MAP_ID = 3;

    /**
     * ���R�[�h��}�����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param mapInfo �}������l
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, MapInfo mapInfo)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database);
            String sql =
                "insert into " + MAP_INFO_TABLE + "(NAME, DATA, LAST_UPDATE)" + " values (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setString(1, mapInfo.name);
            pstmt.setString(2, mapInfo.data);
            Timestamp current = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(3, current);
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
     * @return ���R�[�h�̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<MapInfo> selectAll(final String database)
        throws SQLException
    {
        List<MapInfo> result = new ArrayList<MapInfo>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql = "select * from " + MAP_INFO_TABLE + " order by NAME";
            rs = stmt.executeQuery(sql);
            getMapInfoFromResultSet(result, rs);
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
     * @param mapId �}�b�vID
     * @return {@link MapInfo} �I�u�W�F�N�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static MapInfo selectById(final String database, final long mapId)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MapInfo mapInfo = new MapInfo();
        try
        {
            conn = getConnection(database, true);
            String sql = "select * from " + MAP_INFO_TABLE + " where MAP_ID = ? order by NAME";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, mapId);
            rs = pstmt.executeQuery();
            // CHECKSTYLE:OFF
            if(rs.next()){
                mapInfo.mapId = rs.getLong(1);
                mapInfo.name = rs.getString(2);
                mapInfo.data = rs.getString(3);
                mapInfo.lastUpdate = rs.getTimestamp(4);
            }
            // CHECKSTYLE:ON
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return mapInfo;
    }

    /**
     * �}�b�v��o�^����B
     * @param database �f�[�^�x�[�X��
     * @param mapInfo �}�b�v���
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void update(String database, final MapInfo mapInfo)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            String sql =
                "update " + MAP_INFO_TABLE + " set data = ? , last_update = ? where MAP_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(SQL_DATA, mapInfo.data);
            Timestamp current = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(SQL_LAST_UPDATE, current);
            pstmt.setLong(SQL_MAP_ID, mapInfo.mapId);
            rs = pstmt.executeQuery();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * {@link ResultSet} ���� {@link MapInfo} �̃��X�g���쐬���܂��B<br />
     *
     * @param result {@link MapInfo} �I�u�W�F�N�g�̊i�[��
     * @param rs {@link ResultSet}
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     */
    private static void getMapInfoFromResultSet(List<MapInfo> result, ResultSet rs)
        throws SQLException
    {
        while (rs.next() == true)
        {
            MapInfo mapInfo = new MapInfo();
            // CHECKSTYLE:OFF
            mapInfo.mapId = rs.getInt(1);
            mapInfo.name = rs.getString(2);
            mapInfo.data = rs.getString(3);
            mapInfo.lastUpdate = rs.getTimestamp(4);
            // CHECKSTYLE:ON
            result.add(mapInfo);
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
        deleteAll(database, MAP_INFO_TABLE);
    }

}
