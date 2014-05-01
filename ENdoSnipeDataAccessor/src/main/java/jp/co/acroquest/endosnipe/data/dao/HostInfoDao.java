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

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.entity.HostInfo;

/**
 * {@link HostInfo} �̂��߂� DAO �ł��B
 *
 * @author y-sakamoto
 */
public class HostInfoDao extends AbstractDao implements TableNames
{

    /**
     * {@link HostInfo} �I�u�W�F�N�g��}�����܂��B<br />
     *
     * <code>hostInfo.hostId</code> �͎g�p����܂���B
     * 
     * @param database �}����f�[�^�x�[�X��
     * @param hostInfo �ΏۃI�u�W�F�N�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final HostInfo hostInfo)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(database);
            pstmt =
                    conn.prepareStatement("insert into " + HOST_INFO + " (" + "HOST_NAME, "
                            + "IP_ADDRESS, " + "PORT, " + "DESCRIPTION" + ")"
                            + " values (?, ?, ?, ?)");
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            // CHECKSTYLE:OFF
            delegated.setString(1, hostInfo.hostName);
            delegated.setString(2, hostInfo.ipAddress);
            delegated.setInt(3, hostInfo.port);
            delegated.setString(4, hostInfo.description);
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
     * �w�肳�ꂽ�f�[�^�x�[�X�̃z�X�g�����擾���܂��B<br />
     *
     * �z�X�g��񂪒�`����Ă��Ȃ��ꍇ�͋�̃��X�g��Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return �z�X�g���̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<HostInfo> selectAll(final String database)
        throws SQLException
    {
        List<HostInfo> result = new ArrayList<HostInfo>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, false);
            stmt = conn.createStatement();
            String sql = "select * from " + HOST_INFO;
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                // CHECKSTYLE:OFF
                HostInfo hostInfo = new HostInfo();
                hostInfo.hostId = rs.getInt(1);
                hostInfo.hostName = rs.getString(2);
                hostInfo.ipAddress = rs.getString(3);
                hostInfo.port = rs.getInt(4);
                hostInfo.description = rs.getString(5);
                // CHECKSTYLE:ON
                result.add(hostInfo);
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
     * ���ׂẴ��R�[�h���폜���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteAll(final String database)
        throws SQLException
    {
        deleteAll(database, HOST_INFO);
    }

}
