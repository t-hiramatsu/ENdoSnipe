/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

public class SummarySignalDefinitionDao extends AbstractDao implements TableNames
{
    private static final String SUMMARY_SIGNAL_DEFINITION = "SUMMARY_SIGNAL_DEFINITION";

    public static List<SummarySignalDefinition> selectAll(String databaseName)
        throws SQLException
    {
        List<SummarySignalDefinition> result = new ArrayList<SummarySignalDefinition>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(databaseName, false);
            stmt = conn.createStatement();
            String sql = "select * from " + SUMMARY_SIGNAL_DEFINITION;
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                // CHECKSTYLE:OFF
                SummarySignalDefinition summarySignalInfo = new SummarySignalDefinition();
                List<String> summarySignalList = new ArrayList<String>();
                summarySignalInfo.summarySignalId = rs.getLong(1);
                summarySignalInfo.summarySignalName = rs.getString(2);
                summarySignal = rs.getString(3);
                String[] temp = null;
                temp = summarySignal.split(",");
                for (int index = 0; index < temp.length; index++)
                {
                    summarySignalList.add(temp[index]);
                }
                summarySignalInfo.signalList = summarySignalList;
                summarySignalInfo.summarySignalType = rs.getInt(4);
                summarySignalInfo.priority = rs.getInt(5);
                // CHECKSTYLE:ON
                result.add(summarySignalInfo);
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

    public static List<String> getMinSummaryList(String databaseName)
        throws SQLException
    {
        List<String> result = new ArrayList<String>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(databaseName, false);
            stmt = conn.createStatement();
            String sql = "select summary_signal_name from " + SUMMARY_SIGNAL_DEFINITION + " where priority_no = (select min(priority_no) from "+ SUMMARY_SIGNAL_DEFINITION +" ) ";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
               
                result.add(rs.getString(1));
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
    
    public List<SummarySignalDefinition> selectByName(String dbName, String summarySignalName)
        throws SQLException
    {
        List<SummarySignalDefinition> result = new ArrayList<SummarySignalDefinition>();
        List<String> summarySignalList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(dbName, false);
            stmt = conn.createStatement();
            String sql =
                "select * from " + SUMMARY_SIGNAL_DEFINITION + " where SUMMARY_SIGNAL_NAME='"
                    + summarySignalName + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                // CHECKSTYLE:OFF
                SummarySignalDefinition summarySignalInfo = new SummarySignalDefinition();
                summarySignalInfo.summarySignalId = rs.getLong(1);
                summarySignalInfo.summarySignalName = rs.getString(2);
                summarySignal = rs.getString(3);
                String[] temp = null;
                temp = summarySignal.split(",");
                for (int index = 0; index < temp.length; index++)
                {
                    summarySignalList.add(temp[index]);
                }
                summarySignalInfo.signalList = summarySignalList;
                summarySignalInfo.summarySignalType = rs.getInt(4);
                summarySignalInfo.priority = rs.getInt(5);
                // CHECKSTYLE:ON
                result.add(summarySignalInfo);
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

    public static void insert(String dataBaseName, SummarySignalDefinition summarySignalDefinition)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = getConnection(dataBaseName);
            String sql =
                "insert into " + SUMMARY_SIGNAL_DEFINITION
                    + "(SUMMARY_SIGNAL_NAME, TARGET_SIGNAL_ID,SIGNAL_TYPE, PRIORITY_NO)"
                    + " values (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            pstmt.setString(1, summarySignalDefinition.getSummarySignalName());

            String signalList = "";
            if (summarySignalDefinition.getSignalList() != null
                && summarySignalDefinition.getSignalList().size() > 0)
            {
                for (int count = 0; count < summarySignalDefinition.getSignalList().size(); count++)
                {
                    signalList += summarySignalDefinition.getSignalList().get(count);
                    if (count != summarySignalDefinition.getSignalList().size() - 1)
                    {
                        signalList += ",";
                    }
                }
            }

            pstmt.setString(2, signalList);
            pstmt.setInt(3, summarySignalDefinition.getSummarySignalType());
            pstmt.setInt(4, summarySignalDefinition.getPriority());
            // CHECKSTYLE:ON
            pstmt.execute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    public int selectSequenceNum(String dataBaseName,
        SummarySignalDefinition summarySignalDefinition)
        throws SQLException
    {
        int result = 0;
        List<String> summarySignalList = new ArrayList<String>();
        String summarySignalName = summarySignalDefinition.getSummarySignalName();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(dataBaseName, false);
            stmt = conn.createStatement();
            String sql =
                "select SUMMARY_SIGNAL_ID from " + SUMMARY_SIGNAL_DEFINITION
                    + " where SUMMARY_SIGNAL_NAME='" + summarySignalName + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                int summarySignalId = rs.getInt(1);
                result = summarySignalId;
            }

        }
        finally
        {
            // SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }

        return result;

    }

    public List<SummarySignalDefinition> selectAllByPriority(String dataBaseName)
        throws SQLException
    {
        List<SummarySignalDefinition> result = new ArrayList<SummarySignalDefinition>();
       
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(dataBaseName, false);
            stmt = conn.createStatement();
            String sql =
                "select * from " + SUMMARY_SIGNAL_DEFINITION + " ORDER BY PRIORITY_NO DESC";
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                List<String> summarySignalList = new ArrayList<String>();
                // CHECKSTYLE:OFF
                SummarySignalDefinition summarySignalInfo = new SummarySignalDefinition();
                summarySignalInfo.summarySignalId = rs.getLong(1);
                summarySignalInfo.summarySignalName = rs.getString(2);
                summarySignal = rs.getString(3);
                String[] temp = null;
                temp = summarySignal.split(",");
                for (int index = 0; index < temp.length; index++)
                {
                    summarySignalList.add(temp[index]);
                }
                summarySignalInfo.signalList = summarySignalList;
                summarySignalInfo.summarySignalType = rs.getInt(4);
                summarySignalInfo.priority = rs.getInt(5);
                // CHECKSTYLE:ON
                result.add(summarySignalInfo);
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

    public void delete(String dataBaseName, String summarySignalName)
        throws SQLException
    {
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;

        try
        {
            conn = getConnection(dataBaseName, false);
            stmt = conn.createStatement();
            String sql =
                "delete from " + SUMMARY_SIGNAL_DEFINITION + " where SUMMARY_SIGNAL_NAME='"
                    + summarySignalName + "'";
            stmt.execute(sql);
        }

        finally
        {
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);

        }
    }

    public Map<String, SummarySignalDefinition> selectAllSignalName(String dataBaseName)
        throws SQLException
    {
        Map<String, SummarySignalDefinition> result =
            new HashMap<String, SummarySignalDefinition>();
        List<String> summarySignalList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String summarySignal;
        try
        {
            conn = getConnection(dataBaseName, false);
            stmt = conn.createStatement();
            String sql = "select * from " + SUMMARY_SIGNAL_DEFINITION;
            rs = stmt.executeQuery(sql);
            while (rs.next() == true)
            {
                // CHECKSTYLE:OFF
                SummarySignalDefinition summarySignalInfo = new SummarySignalDefinition();
                summarySignalInfo.summarySignalId = rs.getLong(1);
                summarySignalInfo.summarySignalName = rs.getString(2);
                summarySignal = rs.getString(3);
                String[] temp = null;
                temp = summarySignal.split(",");
                for (int index = 0; index < temp.length; index++)
                {
                    summarySignalList.add(temp[index]);
                }
                summarySignalInfo.signalList = summarySignalList;
                summarySignalInfo.summarySignalType = rs.getInt(4);
                summarySignalInfo.priority = rs.getInt(5);
                // CHECKSTYLE:ON
                result.put(summarySignalInfo.summarySignalName, summarySignalInfo);
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

    public void update(String dataBaseName, long summarySignalId,
        SummarySignalDefinitionDto summarySignalDefinitionDto,boolean priorityFlag)
        throws SQLException
    {

        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql =null;
        try
        {
            conn = getConnection(dataBaseName);
//            String sql =
//                "update " + SUMMARY_SIGNAL_DEFINITION
//                    + "(SUMMARY_SIGNAL_NAME, TARGET_SIGNAL_ID,SIGNAL_TYPE, PRIORITY_NO)"
//                    + " values (?, ?, ?, ?) where SUMMARY_SIGNAL_NAME='"
//                    + summarySignalDefinitionDto.summarySignalName + "'";
//            
            if(priorityFlag)
            {
                sql =
                    "update " + SUMMARY_SIGNAL_DEFINITION
                        + " SET PRIORITY_NO=? where SUMMARY_SIGNAL_NAME=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, summarySignalDefinitionDto.getPriority());
                pstmt.setString(2, summarySignalDefinitionDto.getSummarySignalName());
           
            }
            else
            {
            sql =
                "update " + SUMMARY_SIGNAL_DEFINITION
                    + " SET SUMMARY_SIGNAL_NAME=?, TARGET_SIGNAL_ID=?,SIGNAL_TYPE=?, PRIORITY_NO=? where SUMMARY_SIGNAL_NAME=?";
            
            pstmt = conn.prepareStatement(sql);
            // CHECKSTYLE:OFF
            
            
            pstmt.setString(1, summarySignalDefinitionDto.getSummarySignalName());
            String signalList = "";
            if (summarySignalDefinitionDto.getSignalList() != null
                && summarySignalDefinitionDto.getSignalList().size() > 0)
            {
                for (int count = 0; count < summarySignalDefinitionDto.getSignalList().size(); count++)
                {
                    signalList += summarySignalDefinitionDto.getSignalList().get(count);
                    if (count != summarySignalDefinitionDto.getSignalList().size() - 1)
                    {
                        signalList += ",";
                    }
                }
            }

            pstmt.setString(2, signalList);
            pstmt.setInt(3, summarySignalDefinitionDto.getSummmarySignalType());
            pstmt.setInt(4, summarySignalDefinitionDto.getPriority());
            pstmt.setString(5, summarySignalDefinitionDto.getSummarySignalName());
            
            }
           
            
          
            
            // CHECKSTYLE:ON
            pstmt.executeUpdate();//xecute();
        }
        finally
        {
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

    }
}