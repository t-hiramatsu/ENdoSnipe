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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.common.util.StreamUtil;
import jp.co.acroquest.endosnipe.data.LogMessageCodes;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.data.util.ZipUtil;
import jp.co.acroquest.endosnipe.util.ResourceDataDaoUtil;

/**
 * {@link JavelinLog} �̂��߂� DAO �ł��B
 * 
 * @author y-komori
 */
public class JavelinLogDao extends AbstractDao implements LogMessageCodes, TableNames
{
    private static final ENdoSnipeLogger LOGGER                  =
                                                                        ENdoSnipeLogger.getLogger(JavelinLogDao.class);

    /** ZIP ���k�p�X�g���[���̃o�b�t�@�T�C�Y */
    private static final int             BUF_SIZE                  = 8192;

    /** �~�ϊ��Ԃ��擾���� SQL */
    private static final String       GET_LOG_TERM_SQL_PARTITION = createGetLogTermSql();

    /** �~�ϊ��Ԃ��擾���� SQL */
    private static final String       GET_LOG_TERM_SQL         =
                                                                        "select min(START_TIME) START_TIME, max(END_TIME) END_TIME from "
                                                                            + JAVELIN_LOG;

    /**
     * �f�[�^��}������e�[�u���̖��O��Ԃ��܂��B
     *
     * @param date �}������f�[�^�̓��t
     * @return �e�[�u����
     */
    public static String getTableNameToInsert(final Date date)
    {
        String tableName = ResourceDataDaoUtil.getTableNameToInsert(date, JAVELIN_LOG);
        return tableName;
    }

    /**
     * {@link JavelinLog} �I�u�W�F�N�g��}�����܂��B<br />
     *
     * @param database �}����f�[�^�x�[�X��
     * @param javelinLog �ΏۃI�u�W�F�N�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final JavelinLog javelinLog)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        InputStream is = null;
        String tableName = getTableNameToInsert(javelinLog.endTime);
        try
        {
            conn = getConnection(database);
            String sql =
                "insert into " + tableName + " (" + "SESSION_ID, " + "SEQUENCE_ID, "
                    + "JAVELIN_LOG, " + "LOG_FILE_NAME, " + "START_TIME, END_TIME, "
                    + "SESSION_DESC, " + "LOG_TYPE, " + "CALLEE_NAME, " + "CALLEE_SIGNATURE, "
                    + "CALLEE_CLASS, " + "CALLEE_FIELD_TYPE, " + "CALLEE_OBJECTID, "
                    + "CALLER_NAME, " + "CALLER_SIGNATURE, " + "CALLER_CLASS, "
                    + "CALLER_OBJECTID, " + "EVENT_LEVEL, " + "ELAPSED_TIME, " + "MODIFIER, "
                    + "THREAD_NAME, " + "THREAD_CLASS, " + "THREAD_OBJECTID, "
                    + "MEASUREMENT_ITEM_NAME" + ") values (" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            // CHECKSTYLE:OFF
            delegated.setLong(1, javelinLog.sessionId);
            delegated.setInt(2, javelinLog.sequenceId);
            ByteArrayOutputStream baos = zip(javelinLog.javelinLog, javelinLog.logFileName);
            delegated.setBytes(3, baos.toByteArray());
            delegated.setString(4, javelinLog.logFileName);
            delegated.setTimestamp(5, javelinLog.startTime);
            delegated.setTimestamp(6, javelinLog.endTime);
            delegated.setString(7, javelinLog.sessionDesc);
            delegated.setInt(8, javelinLog.logType);
            delegated.setString(9, javelinLog.calleeName);
            delegated.setString(10, javelinLog.calleeSignature);
            delegated.setString(11, javelinLog.calleeClass);
            delegated.setString(12, javelinLog.calleeFieldType);
            delegated.setInt(13, javelinLog.calleeObjectId);
            delegated.setString(14, javelinLog.callerName);
            delegated.setString(15, javelinLog.callerSignature);
            delegated.setString(16, javelinLog.callerClass);
            delegated.setInt(17, javelinLog.callerObjectId);
            delegated.setInt(18, javelinLog.eventLevel);
            delegated.setLong(19, javelinLog.elapsedTime);
            delegated.setString(20, javelinLog.modifier);
            delegated.setString(21, javelinLog.threadName);
            delegated.setString(22, javelinLog.threadClass);
            delegated.setInt(23, javelinLog.threadObjectId);
            delegated.setString(24, javelinLog.measurementItemName);
            // CHECKSTYLE:ON

            pstmt.execute();
        }
        catch (IOException ex)
        {
            LOGGER.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
        }
        catch (SQLException ex)
        {
            LOGGER.log(DB_ACCESS_ERROR, ex, ex.getMessage());
            throw ex;
        }
        finally
        {
            StreamUtil.closeStream(is);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    /**
     * Javelin ���O ID ���w�肵�ă��R�[�h���擾���܂��B<br />
     * JAVELIN_LOG �e�[�u���ɑ΂��Č������s���A�����������R�[�h�� 1 ���Ԃ��܂��B<br />
     * 
     * {@link JavelinLog#javelinLog} �͎擾���܂���B
     * �ʓr�A {@link JavelinLogDao#selectJavelinLogByLogId(String, long)} ���g�p���Ă��������B
     *
     * @param database �f�[�^�x�[�X��
     * @param logId ���O ID
     * @return ���R�[�h�B�擾�ł��Ȃ��ꍇ�� <code>null</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static JavelinLog selectByLogId(final String database, final long logId)
        throws SQLException
    {
        JavelinLog javelinLog = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        conn = getConnection(database, true);

        try
        {
            String sql =
                "select LOG_ID, SESSION_ID, SEQUENCE_ID, JAVELIN_LOG, LOG_FILE_NAME, "
                    + "START_TIME, END_TIME, SESSION_DESC, LOG_TYPE, "
                    + "CALLEE_NAME, CALLEE_SIGNATURE, CALLEE_CLASS, "
                    + "CALLEE_FIELD_TYPE, CALLEE_OBJECTID, CALLER_NAME, "
                    + "CALLER_SIGNATURE, CALLER_CLASS, CALLER_OBJECTID, "
                    + "EVENT_LEVEL, ELAPSED_TIME, MODIFIER, THREAD_NAME, "
                    + "THREAD_CLASS, THREAD_OBJECTID from " + JAVELIN_LOG + " where LOG_ID = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setLong(1, logId);
            rs = delegated.executeQuery();

            if (rs.next() == true)
            {
                javelinLog = new JavelinLog();
                setJavelinLogFromResultSet(javelinLog, rs);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return javelinLog;
    }

    /**
     * Javelin ���O�t�@�C�������w�肵�ă��R�[�h���擾���܂��B<br />
     * JAVELIN_LOG �e�[�u���ɑ΂��Č������s���A�����������R�[�h�����ׂĕԂ��܂��B<br />
     * 
     * {@link JavelinLog#javelinLog} ���擾���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param fileName ���O�t�@�C����
     * @return ���R�[�h�B�擾�ł��Ȃ��ꍇ�� <code>null</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static JavelinLog selectByLogFileNameWithBinary(final String database,
        final String fileName)
        throws SQLException
    {
        return selectByLogFileName(database, fileName, true);
    }

    /**
     * Javelin ���O�t�@�C�������w�肵�ă��R�[�h���擾���܂��B<br />
     * JAVELIN_LOG �e�[�u���ɑ΂��Č������s���A�����������R�[�h�����ׂĕԂ��܂��B<br />
     * 
     * {@link JavelinLog#javelinLog} �͎擾���܂���B
     * �ʓr�A {@link JavelinLogDao#selectJavelinLogByLogId(String, long)} ���g�p���Ă��������B
     *
     * @param database �f�[�^�x�[�X��
     * @param fileName ���O�t�@�C����
     * @return ���R�[�h�B�擾�ł��Ȃ��ꍇ�� <code>null</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static JavelinLog selectByLogFileName(final String database, final String fileName)
        throws SQLException
    {
        return selectByLogFileName(database, fileName, false);
    }

    /**
     * Javelin ���O�t�@�C�������w�肵�ă��R�[�h���擾���܂��B<br />
     * JAVELIN_LOG �e�[�u���ɑ΂��Č������s���A�����������R�[�h�����ׂĕԂ��܂��B<br />
     * 
     * {@link JavelinLog#javelinLog} �͎擾���܂���B
     * �ʓr�A {@link JavelinLogDao#selectJavelinLogByLogId(String, long)} ���g�p���Ă��������B
     *
     * @param database �f�[�^�x�[�X��
     * @param fileName ���O�t�@�C����
     * @param outputLog true�̏ꍇ��{@link JavelinLog#javelinLog}���擾����Bfalse�̏ꍇ�͎擾���Ȃ��B
     * @return ���R�[�h�B�擾�ł��Ȃ��ꍇ�� <code>null</code>
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private static JavelinLog selectByLogFileName(final String database, final String fileName,
        final boolean outputLog)
        throws SQLException
    {
        JavelinLog javelinLog = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        conn = getConnection(database, true);

        try
        {
            String sql =
                "select LOG_ID, SESSION_ID, SEQUENCE_ID, JAVELIN_LOG, LOG_FILE_NAME, "
                    + "START_TIME, END_TIME, SESSION_DESC, LOG_TYPE, "
                    + "CALLEE_NAME, CALLEE_SIGNATURE, CALLEE_CLASS, "
                    + "CALLEE_FIELD_TYPE, CALLEE_OBJECTID, CALLER_NAME, "
                    + "CALLER_SIGNATURE, CALLER_CLASS, CALLER_OBJECTID, "
                    + "EVENT_LEVEL, ELAPSED_TIME, MODIFIER, THREAD_NAME, "
                    + "THREAD_CLASS, THREAD_OBJECTID from " + JAVELIN_LOG
                    + " where LOG_FILE_NAME = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setString(1, fileName);
            rs = delegated.executeQuery();

            if (rs.next() == true)
            {
                javelinLog = new JavelinLog();
                setJavelinLogFromResultSet(javelinLog, rs, outputLog);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }

        return javelinLog;
    }

    /**
     * ���Ԃ��w�肵�đS�z�X�g�̃��R�[�h���擾���܂��B<br />
     * �J�n�����A�I��������null �̏ꍇ�́A�w�肪�s���Ă��Ȃ����̂Ƃ��A<br />
     * �S�f�[�^���擾���܂��B
     *
     * {@link JavelinLog#javelinLog} �͎擾���܂���B
     * �ʓr�A {@link JavelinLogDao#selectJavelinLogByLogId(String, long)} ���g�p���Ă��������B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @return {@link JavelinLog} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinLog> selectByTerm(final String database, final Timestamp start,
        final Timestamp end)
        throws SQLException
    {
        return selectByTerm(database, start, end, false);
    }

    /**
     * ���Ԃ��w�肵�đS�z�X�g�̃��R�[�h���擾���܂��B<br />
     * �J�n�����A�I��������null �̏ꍇ�́A�w�肪�s���Ă��Ȃ����̂Ƃ��A<br />
     * �S�f�[�^���擾���܂��B
     *
     * ���̃��\�b�h��{@link JavelinLog#javelinLog} ���擾���܂��B
     * �I�u�W�F�N�g�T�C�Y���傫���Ȃ�ꍇ������̂ŁA���ӂ��Ă��������B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @return {@link JavelinLog} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinLog> selectByTermWithLog(final String database,
        final Timestamp start, final Timestamp end)
        throws SQLException
    {
        return selectByTerm(database, start, end, true);
    }

    /**
     * ���Ԃ��w�肵�đS�z�X�g�̃��R�[�h���擾���܂��B<br />
     * �J�n�����A�I��������null �̏ꍇ�́A�w�肪�s���Ă��Ȃ����̂Ƃ��A<br />
     * �S�f�[�^���擾���܂��B
     *
     * {@link JavelinLog#javelinLog} �͎擾���܂���B
     * �ʓr�A {@link JavelinLogDao#selectJavelinLogByLogId(String, long)} ���g�p���Ă��������B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @param outputLog Javelin���O���o�͂���ꍇ<code>true</code>
     * @return {@link JavelinLog} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinLog> selectByTerm(final String database, final Timestamp start,
        final Timestamp end, final boolean outputLog)
        throws SQLException
    {
        List<JavelinLog> result = new ArrayList<JavelinLog>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {

            conn = getConnection(database, true);
            String sql = createSelectSqlByTerm(JAVELIN_LOG, start, end);
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            setTimestampByTerm(delegated, start, end);
            rs = delegated.executeQuery();

            // ���ʂ����X�g�ɂP���i�[����
            while (rs.next() == true)
            {
                JavelinLog log = new JavelinLog();
                setJavelinLogFromResultSet(log, rs, outputLog);
                result.add(log);
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
     * �����̎w��ɉ�����SELECT����SQL���쐬���܂��B<br />
     * 
     * @param tableName �e�[�u����
     * @param start �J�n����
     * @param end �I������
     * @return
     */
    private static String createSelectSqlByTerm(final String tableName, final Timestamp start,
        final Timestamp end)
    {
        String sql = "select * from " + tableName;
        if (start != null && end != null)
        {
            sql += " where ? <= START_TIME and END_TIME <= ?";
        }
        else if (start != null && end == null)
        {
            sql += " where ? <= START_TIME";
        }
        else if (start == null && end != null)
        {
            sql += " where END_TIME <= ?";
        }
        sql += " order by START_TIME desc";
        return sql;
    }

    /**
     * �����̎w��ɉ�����SELECT����SQL���쐬���܂��B<br />
     * 
     * @param delegated PreparedStatement
     * @param start �J�n����
     * @param end �I������
     */
    private static void setTimestampByTerm(final PreparedStatement delegated,
        final Timestamp start, final Timestamp end)
        throws SQLException
    {
        if (start != null && end != null)
        {
            delegated.setTimestamp(1, start);
            delegated.setTimestamp(2, end);
        }
        else if (start != null && end == null)
        {
            delegated.setTimestamp(1, start);
        }
        else if (start == null && end != null)
        {
            delegated.setTimestamp(1, end);
        }
    }

    /**
     * ���ԂƖ��O���w�肵�đS�z�X�g�̃��R�[�h���擾���܂��B<br />
     * �J�n�����A�I��������null �̏ꍇ�́A�w�肪�s���Ă��Ȃ����̂Ƃ��A<br />
     * �S�f�[�^���擾���܂��B<br />
     * �A�C�e������null �̏ꍇ�́A�w�肪�s���Ă��Ȃ����̂Ƃ��A<br />
     * �S�f�[�^���擾���܂��B
     * 
     * @param database �f�[�^�x�[�X��
     * @param start �J�n����
     * @param end �I������
     * @param name �A�C�e����
     * @param outputLog Javelin���O���o�͂���ꍇ<code>true</code>
     * @return {@link JavelinLog} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinLog> selectByTermAndName(final String database,
        final Timestamp start, final Timestamp end, final String name, final boolean outputLog)
        throws SQLException
    {
        List<JavelinLog> result = new ArrayList<JavelinLog>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {

            conn = getConnection(database, true);
            String sql = createSelectSqlByTermAndName(JAVELIN_LOG, start, end, name);
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            setTimestampByTerm(delegated, start, end);
            rs = delegated.executeQuery();

            // ���ʂ����X�g�ɂP���i�[����
            while (rs.next() == true)
            {
                JavelinLog log = new JavelinLog();
                setJavelinLogFromResultSet(log, rs, outputLog);
                result.add(log);
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
     * �����A�A�C�e�����̎w��ɉ�����SELECT����SQL���쐬���܂��B<br />
     * 
     * @param tableName �e�[�u����
     * @param start �J�n����
     * @param end �I������
     * @param name �A�C�e����
     * @return
     */
    private static String createSelectSqlByTermAndName(final String tableName,
        final Timestamp start, final Timestamp end, final String name)
    {
        String sql = "select * from " + tableName;
        if (start != null && end != null)
        {
            sql += " where ? <= START_TIME and END_TIME <= ?";
        }
        else if (start != null && end == null)
        {
            sql += " where ? <= START_TIME";
        }
        else if (start == null && end != null)
        {
            sql += " where END_TIME <= ?";
        }
        if (name != null)
        {
            sql +=
                ((start == null && end == null) ? " where " : " and ")
                    + "MEASUREMENT_ITEM_NAME like '" + name + "%'";
        }
        sql += " order by START_TIME desc";
        return sql;
    }

    /**
     * ���ׂẴ��R�[�h���擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X��
     * @return {@link JavelinLog} �̃��X�g
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static List<JavelinLog> selectAll(final String database)
        throws SQLException
    {
        List<JavelinLog> result = new ArrayList<JavelinLog>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from " + JAVELIN_LOG + " order by START_TIME desc");

            // ���ʂ����X�g�ɂP���i�[����
            while (rs.next())
            {
                JavelinLog log = new JavelinLog();
                setJavelinLogFromResultSet(log, rs);
                result.add(log);
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
     * �P���R�[�h�� Javelin ���O�G���e�B�e�B�Ɋi�[���܂��B<br />
     * �������AJavelinLog �t�B�[���h�͎擾���܂���B<br />
     * 
     * @param log �i�[�� Javelin ���O�G���e�B�e�B
     * @param rs {@link ResultSet} �I�u�W�F�N�g
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     */
    private static void setJavelinLogFromResultSet(final JavelinLog log, final ResultSet rs)
        throws SQLException
    {
        setJavelinLogFromResultSet(log, rs, false);
    }

    /**
     * �P���R�[�h�� Javelin ���O�G���e�B�e�B�Ɋi�[���܂��B<br />
     * �������AJavelinLog �t�B�[���h�͎擾���܂���B<br />
     * 
     * @param log �i�[�� Javelin ���O�G���e�B�e�B
     * @param rs {@link ResultSet} �I�u�W�F�N�g
     * @throws SQLException SQL ���s���ʎ擾���ɗ�O�����������ꍇ
     */
    private static void setJavelinLogFromResultSet(final JavelinLog log, final ResultSet rs,
        final boolean outputLog)
        throws SQLException
    { // CHECKSTYLE:OFF
        log.logId = rs.getLong(1);
        log.sessionId = rs.getLong(2);
        log.sequenceId = rs.getInt(3);
        if (outputLog == true)
        {
            InputStream is = rs.getBinaryStream(4);
            try
            {
                log.javelinLog = ZipUtil.unzipFromByteArray(is);
            }
            catch (IOException ex)
            {
                log.javelinLog = null;
            }
        }
        log.logFileName = rs.getString(5);
        log.startTime = rs.getTimestamp(6);
        log.endTime = rs.getTimestamp(7);
        log.sessionDesc = rs.getString(8);
        log.logType = rs.getInt(9);
        log.calleeName = rs.getString(10);
        log.calleeSignature = rs.getString(11);
        log.calleeClass = rs.getString(12);
        log.calleeFieldType = rs.getString(13);
        log.calleeObjectId = rs.getInt(14);
        log.callerName = rs.getString(15);
        log.callerSignature = rs.getString(16);
        log.callerClass = rs.getString(17);
        log.callerObjectId = rs.getInt(18);
        log.eventLevel = rs.getInt(19);
        log.elapsedTime = rs.getLong(20);
        log.modifier = rs.getString(21);
        log.threadName = rs.getString(22);
        log.threadClass = rs.getString(23);
        log.threadObjectId = rs.getInt(24);
        // CHECKSTYLE:ON
    }

    /**
     * �e�[�u���ɋL�^����Ă��郍�O�̊��Ԃ�Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return �J�n�����A�I�������̔z��A�擾�Ɏ��s�����ꍇ�͋�̔z��
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static Timestamp[] getLogTerm(final String database)
        throws SQLException
    {
        Connection conn = null;
        Timestamp[] result = new Timestamp[0];
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(getLogTermSql());
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
    private static String getLogTermSql()
    {
        if (DBManager.isDefaultDb())
        {
            return GET_LOG_TERM_SQL;
        }
        else
        {
            return GET_LOG_TERM_SQL_PARTITION;
        }
    }

    /**
     * ���O ID ���w�肵�� Javelin ���O���擾���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param logId ���O ID
     * @return Javelin ���O
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public static InputStream selectJavelinLogByLogId(final String database, final long logId)
        throws SQLException,
            IOException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        InputStream result = null;
        try
        {
            conn = getConnection(database, true);
            String sql = "select JAVELIN_LOG from " + JAVELIN_LOG + " where LOG_ID = ?";
            pstmt = conn.prepareStatement(sql);
            PreparedStatement delegated = getDelegatingStatement(pstmt);
            delegated.setLong(1, logId);

            rs = delegated.executeQuery();

            if (rs.next() == true)
            {
                InputStream is = rs.getBinaryStream(1);
                result = ZipUtil.unzipFromByteArray(is);
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
     * �������w�肵�āA������Â����R�[�h���폜���܂��B
     * �폜���������̃L�[�Ƃ��ẮA�Z�b�V�����I����������Ƃ��܂��B
     * 
     * @param database �f�[�^�x�[�X��
     * @param deleteLimit �폜��������
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void deleteOldRecordByTime(final String database, final Timestamp deleteLimit)
        throws SQLException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection(database, true);
            String sql = "delete from " + JAVELIN_LOG + " where END_TIME <= ?";
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
        deleteAll(database, JAVELIN_LOG);
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
        String tableName = String.format("%s_%02d", JAVELIN_LOG, tableIndex);
        truncate(database, tableName);
        alterCheckConstraint(database, tableName, tableIndex, "END_TIME", year);
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
        int count = count(database, JAVELIN_LOG, "LOG_ID");
        return count;
    }

    private static ByteArrayOutputStream zip(final InputStream is, final String path)
        throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE);
        ZipUtil.createZip(out, is, path);
        return out;
    }

    /**
     * �~�ϊ��Ԃ��擾���� SQL �𐶐����܂��B
     *
     * @return SQL
     */
    private static String createGetLogTermSql()
    {
        String unionSql = "";
        StringBuilder sql = new StringBuilder("select min(START_TIME), max(END_TIME) from (");
        for (int index = 1; index <= ResourceDataDaoUtil.PARTITION_TABLE_COUNT; index++)
        {
            sql.append(unionSql);
            String tableName = ResourceDataDaoUtil.getTableName(JAVELIN_LOG, index);
            sql.append("select min(START_TIME) START_TIME, max(END_TIME) END_TIME from ");
            sql.append(tableName);
            unionSql = " union all ";
        }
        sql.append(") MERGED_TIME");
        return sql.toString();
    }
}
