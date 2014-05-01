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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.data.entity.JavelinLog;

/**
 * {@link JavelinLogDao} �N���X�̃e�X�g�P�[�X�B<br />
 *
 * @author sakamoto
 */
public class JavelinLogDaoTest extends AbstractDaoTest
{
    /** Javelin���O�̃t�@�C���� */
    private static final String JVN_FILENAME = "JavelinLogFile.jvn";

    /** Javelin���O�f�[�^ */
    private static final byte[] JAVELIN_DATA = "1234567890abcdefg".getBytes();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    /**
     * @test ��̃e�[�u���ɑ΂��āA insert() �����s���܂��B<br />
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testInsert_one()
        throws SQLException
    {
        // ����
        Timestamp time = new Timestamp(System.currentTimeMillis());
        JavelinLog javelinLog = createJavelinLog(0, time, time);

        // ���s
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ����
        List<JavelinLog> actual = JavelinLogDao.selectByTerm(DB_NAME, time, time);
        assertEquals(1, actual.size());
    }

    /**
     * @test ���݂��Ȃ� Javelin ���O���擾���܂��B<br />
     * @throws Exception ��O
     */
    public void testSelectJavelinLogByLogId_notExist()
        throws Exception
    {
        // ����
        Timestamp time = new Timestamp(System.currentTimeMillis());
        JavelinLog javelinLog = createJavelinLog(0, time, time);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        InputStream actual = JavelinLogDao.selectJavelinLogByLogId(DB_NAME, -1);

        // ����
        assertNull(actual);
    }

    /**
     * @target {@link JavelinLogDao#selectByLogFileName(String, String)}
     * 
     * @test DB���瑶�݂���t�@�C������Javelin���O����������B
     * @condition <li>DB��"JavelinLogFile.jvn"�Ƃ������O�̃��O��insert����B</li>
     * <li>DB����"JavelinLogFile.jvn"�Ƃ������O�Ńt�@�C������������B
     * 
     * @result �������ʂ�null �łȂ����ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testSelectByLogFileName_existFile()
        throws Exception
    {
        // ����
        Timestamp time = new Timestamp(System.currentTimeMillis());
        JavelinLog javelinLog = createJavelinLog(0, time, time);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // Javelin���O�t�@�C����
        String jvnFileName = JVN_FILENAME;

        // ���s
        JavelinLog jvnLog = JavelinLogDao.selectByLogFileName(DB_NAME, jvnFileName);

        // ����
        assertNotNull(jvnLog);
    }

    /**
     * @target {@link JavelinLogDao#selectByLogFileName(String, String)}
     * 
     * @test DB���瑶�݂��Ȃ��t�@�C������Javelin���O����������B
     * @condition <li>DB��"JavelinLogFile.jvn"�Ƃ������O�̃��O��insert����B</li>
     * <li>DB����"NotExist.jvn"�Ƃ������O�Ńt�@�C������������B
     * 
     * @result �������ʂ�null �ł��邱�ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testSelectByLogFileName_notExist()
        throws Exception
    {
        // ����
        Timestamp time = new Timestamp(System.currentTimeMillis());
        JavelinLog javelinLog = createJavelinLog(0, time, time);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // Javelin���O�t�@�C����
        String jvnFileName = "NotExist.jvn";

        // ���s
        JavelinLog jvnLog = JavelinLogDao.selectByLogFileName(DB_NAME, jvnFileName);

        // ����
        assertNull(jvnLog);
    }

    /**
     * �͈͓��� Javelin ���O���擾���܂��B<br />
     *
     * Javelin ���O�̊J�n�����ƁA�w��J�n��������v���A
     * Javelin ���O�̏I�������ƁA�w��I����������v���鎎���ł��B
     *
     * @throws Exception ��O
     */
    public void testSelectByTerm_inJustRange()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        Timestamp timeStart2 = new Timestamp(time + 2000);
        Timestamp timeEnd2 = new Timestamp(time + 3000);
        Timestamp timeStart3 = new Timestamp(time + 4000);
        Timestamp timeEnd3 = new Timestamp(time + 5000);
        Timestamp timeStart4 = new Timestamp(time + 6000);
        Timestamp timeEnd4 = new Timestamp(time + 7000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(1, timeStart2, timeEnd2);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(2, timeStart3, timeEnd3);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(3, timeStart4, timeEnd4);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        List<JavelinLog> actual = JavelinLogDao.selectByTerm(DB_NAME, timeStart3, timeEnd3);

        // ����
        assertEquals(1, actual.size());
        assertJavelinLog(timeStart3, timeEnd3, actual.get(0));
    }

    /**
     * �͈͓��� Javelin ���O���擾���܂��B<br />
     *
     * Javelin ���O�̊J�n�����ƁA�w��J�n��������v�����A
     * Javelin ���O�̏I�������ƁA�w��I����������v���Ȃ������ł��B
     *
     * @throws Exception ��O
     */
    public void testSelectByTerm_inRange()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        Timestamp timeStart2 = new Timestamp(time + 2000);
        Timestamp timeEnd2 = new Timestamp(time + 3000);
        Timestamp timeStart3 = new Timestamp(time + 4000);
        Timestamp timeEnd3 = new Timestamp(time + 5000);
        Timestamp timeStart4 = new Timestamp(time + 6000);
        Timestamp timeEnd4 = new Timestamp(time + 7000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(1, timeStart2, timeEnd2);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(2, timeStart3, timeEnd3);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(3, timeStart4, timeEnd4);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        List<JavelinLog> actual = JavelinLogDao.selectByTerm(DB_NAME, timeEnd1, timeStart4);

        // ����
        assertEquals(2, actual.size());
        
        assertJavelinLog(timeStart3, timeEnd3, actual.get(0));
        assertJavelinLog(timeStart2, timeEnd2, actual.get(1));
    }

    /**
     * �͈͓��� Javelin ���O���擾���܂��B<br />
     *
     * �͈͓��� Javelin ���O�����݂��Ȃ������ł��B
     *
     * @throws Exception ��O
     */
    public void testSelectByTerm_outOfRange()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        Timestamp timeStart2 = new Timestamp(time + 2000);
        Timestamp timeEnd2 = new Timestamp(time + 3000);
        Timestamp timeStart3 = new Timestamp(time + 4000);
        Timestamp timeEnd3 = new Timestamp(time + 5000);
        Timestamp timeStart4 = new Timestamp(time + 6000);
        Timestamp timeEnd4 = new Timestamp(time + 7000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(1, timeStart2, timeEnd2);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(2, timeStart3, timeEnd3);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(3, timeStart4, timeEnd4);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        List<JavelinLog> actual = JavelinLogDao.selectByTerm(DB_NAME, timeEnd2, timeStart3);

        // ����
        assertEquals(0, actual.size());
    }

    /**
     * ���R�[�h���i�[����Ă��Ȃ��e�[�u���� Javelin ���O�͈̔͂��擾���܂��B<br />
     *
     * @throws Exception ��O
     */
    public void testGetLogTerm_empty()
        throws Exception
    {
        // ���s
        Timestamp[] actual = JavelinLogDao.getLogTerm(DB_NAME);

        // ����
        assertEquals(2, actual.length);
        assertNull(actual[0]);
        assertNull(actual[1]);
    }

    /**
     * 1 ���R�[�h���i�[����Ă���e�[�u���� Javelin ���O�͈̔͂��擾���܂��B<br />
     *
     * @throws Exception ��O
     */
    public void testGetLogTerm_one()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        Timestamp[] actual = JavelinLogDao.getLogTerm(DB_NAME);

        // ����
        assertEquals(2, actual.length);
        assertEquals(timeStart1, actual[0]);
        assertEquals(timeEnd1, actual[1]);
    }

    /**
     * 2 ���R�[�h���i�[����Ă���e�[�u���� Javelin ���O�͈̔͂��擾���܂��B<br />
     *
     * @throws Exception ��O
     */
    public void testGetLogTerm_two()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        Timestamp timeStart2 = new Timestamp(time + 2000);
        Timestamp timeEnd2 = new Timestamp(time + 3000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);
        javelinLog = createJavelinLog(1, timeStart2, timeEnd2);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        Timestamp[] actual = JavelinLogDao.getLogTerm(DB_NAME);

        // ����
        assertEquals(2, actual.length);
        assertEquals(timeStart1, actual[0]);
        assertEquals(timeEnd2, actual[1]);
    }

    /**
     * ���R�[�h���i�[����Ă��Ȃ��e�[�u���̃��R�[�h�����擾���܂��B<br />
     *
     * @throws Exception ��O
     */
    public void testCount_empty()
        throws Exception
    {
        // ���s
        int actual = JavelinLogDao.count(DB_NAME);

        // ����
        assertEquals(0, actual);
    }

    /**
     * ���R�[�h���i�[����Ă���e�[�u���̃��R�[�h�����擾���܂��B<br />
     *
     * @throws Exception ��O
     */
    public void testCount_one()
        throws Exception
    {
        // ����
        long time = System.currentTimeMillis();
        Timestamp timeStart1 = new Timestamp(time);
        Timestamp timeEnd1 = new Timestamp(time + 1000);
        JavelinLog javelinLog = createJavelinLog(0, timeStart1, timeEnd1);
        JavelinLogDao.insert(DB_NAME, javelinLog);

        // ���s
        int actual = JavelinLogDao.count(DB_NAME);

        // ����
        assertEquals(1, actual);
    }

    /**
     * Javelin ���O�I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param logId ���O ID
     * @param start �J�n����
     * @param end �I������
     * @return Javelin ���O�I�u�W�F�N�g
     */
    private JavelinLog createJavelinLog(final long logId, final Timestamp start, final Timestamp end)
    {
        JavelinLog javelinLog = new JavelinLog();
        javelinLog.logId = logId;
        javelinLog.sessionId = 0;
        javelinLog.sequenceId = 0;
        javelinLog.javelinLog = new ByteArrayInputStream(JAVELIN_DATA);
        javelinLog.logFileName = JVN_FILENAME;
        javelinLog.startTime = start;
        javelinLog.endTime = end;
        javelinLog.sessionDesc = "session description";
        javelinLog.calleeName = "calleeMethod";
        javelinLog.calleeSignature = "public";
        javelinLog.calleeClass = "CalleeClass";
        javelinLog.calleeFieldType = "int";
        javelinLog.calleeObjectId = 2;
        javelinLog.callerName = "callerMethod";
        javelinLog.callerSignature = "protected";
        javelinLog.callerClass = "CallerClass";
        javelinLog.callerObjectId = 3;
        javelinLog.elapsedTime = 123;
        javelinLog.modifier = "a";
        javelinLog.threadName = "thread_name";
        javelinLog.threadClass = "ThreadClass";
        javelinLog.threadObjectId = 5;
        return javelinLog;
    }

    /**
     * Javelin �I�u�W�F�N�g���`�F�b�N���܂��B<br />
     *
     * ���O ID �� Javelin ���O�̓`�F�b�N���܂���B
     *
     * @param start �J�n����
     * @param end �I������
     * @param actual �擾���� Javelin ���O�I�u�W�F�N�g
     */
    private void assertJavelinLog(final Timestamp start, final Timestamp end,
            final JavelinLog actual)
    {
        assertEquals(0, actual.sequenceId);
        assertEquals(JVN_FILENAME, actual.logFileName);
        assertEquals(start, actual.startTime);
        assertEquals(end, actual.endTime);
        assertEquals("session description", actual.sessionDesc);
        assertEquals("calleeMethod", actual.calleeName);
        assertEquals("public", actual.calleeSignature);
        assertEquals("CalleeClass", actual.calleeClass);
        assertEquals("int", actual.calleeFieldType);
        assertEquals(2, actual.calleeObjectId);
        assertEquals("callerMethod", actual.callerName);
        assertEquals("protected", actual.callerSignature);
        assertEquals("CallerClass", actual.callerClass);
        assertEquals(3, actual.callerObjectId);
        assertEquals(123, actual.elapsedTime);
        assertEquals("a", actual.modifier);
        assertEquals("thread_name", actual.threadName);
        assertEquals("ThreadClass", actual.threadClass);
        assertEquals(5, actual.threadObjectId);
    }
}
