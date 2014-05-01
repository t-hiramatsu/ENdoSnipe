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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.data.entity.MeasurementValue;

/**
 * {@link MeasurementValueDao} �N���X�̃e�X�g�P�[�X�B<br />
 *
 * @author y-sakamoto
 */
public class MeasurementValueDaoTest extends AbstractDaoTest
{
    /** �X���[�v���� */
    private static final long SLEEP_TIME = 100;

    /** Javelin �v�����ڃe�[�u���ɓo�^����Ă��郌�R�[�h�̃��X�g */
    private List<JavelinMeasurementItem> javelinMeasurementItemList_;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        initJavelinMeasurementItemTable();
    }

    /**
     * {@link MeasurementValue} �̒l���r���܂��B<br />
     *
     * @param expected ���҂���l
     * @param actual ���ۂ̒l
     */
    private static void assertEquals(final MeasurementValue expected, final MeasurementValue actual)
    {
        assertEquals(expected.measurementTime, actual.measurementTime);
        assertEquals(expected.measurementItemId, actual.measurementItemId);
        assertEquals(expected.value, actual.value);
    }

    /**
     * Javelin �v�����ڃe�[�u�������������܂��B<br />
     *
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private void initJavelinMeasurementItemTable()
        throws SQLException
    {
        for (int index = 1; index <= 3; index++)
        {
            JavelinMeasurementItem javelinMeasurementItem = new JavelinMeasurementItem();
            //javelinMeasurementItem.measurementType = 1;
            javelinMeasurementItem.itemName = "ItemName" + index;
            javelinMeasurementItem.lastInserted = new Timestamp(System.currentTimeMillis());
            JavelinMeasurementItemDao.insert(DB_NAME, javelinMeasurementItem);
        }
        this.javelinMeasurementItemList_ = JavelinMeasurementItemDao.selectAll(DB_NAME);
    }

    /**
     * Javelin �v���l�e�[�u���Ƀ��R�[�h��ǉ����܂��B<br />
     *
     * @return �ǉ��������R�[�h
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private MeasurementValue insertMeasurementValue()
        throws SQLException
    {
        JavelinMeasurementItem javelinMeasurementItem = this.javelinMeasurementItemList_.get(0);
        MeasurementValue measurementValue = new MeasurementValue();
        measurementValue.measurementTime = new Timestamp(System.currentTimeMillis());
        //measurementValue.measurementType = javelinMeasurementItem.measurementType;
        measurementValue.measurementItemId = javelinMeasurementItem.measurementItemId;
        measurementValue.value = "1";
        MeasurementValueDao.insert(DB_NAME, measurementValue);
        return measurementValue;
    }

    /**
     * @target testCount_notExist
     * @test ���R�[�h���̎擾
     *   condition:: ���R�[�h�����݂��Ȃ��B
     *   result:: 0 ���Ԃ邱�ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testCount_notExist()
        throws SQLException
    {
        // ���s
        int actual = MeasurementValueDao.count(DB_NAME);

        // ����
        assertEquals(0, actual);
    }

    /**
     * @target testCount_exist
     * @test ���R�[�h���̎擾
     *   condition:: ���R�[�h�����݂���B
     *   result:: ���R�[�h�����Ԃ邱�ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testCount_exist()
        throws SQLException
    {
        // ����
        initJavelinMeasurementItemTable();
        insertMeasurementValue();

        // ���s
        int actual = MeasurementValueDao.count(DB_NAME);

        // ����
        assertEquals(1, actual);
    }

    /**
     * @target testSelectByTerm_inRange
     * @test �w�肵���͈͂̎擾
     *   condition:: �͈͓��̃f�[�^�����݂���B
     *   result:: ���R�[�h���Ԃ邱�ƁB
     * @throws InterruptedException �X���[�v�����f�����ꍇ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectByTerm_inRange()
        throws InterruptedException,
            SQLException
    {
        // ����
        insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        MeasurementValue measurementValue1 = insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        MeasurementValue measurementValue2 = insertMeasurementValue();
        Timestamp end = new Timestamp(System.currentTimeMillis());
        Thread.sleep(SLEEP_TIME);
        insertMeasurementValue();

        // ���s
        List<MeasurementValue> actual =
                MeasurementValueDao.selectByTerm(DB_NAME, start, end);

        // ����
        assertEquals(2, actual.size());
        assertEquals(measurementValue1, actual.get(0));
        assertEquals(measurementValue2, actual.get(1));
    }

    /**
     * @target testSelectByTerm_lowerRange
     * @test �w�肵���͈͂̎擾
     *   condition:: �͈͂�������������Ă���B
     *   result:: ���R�[�h���Ԃ�Ȃ����ƁB
     * @throws InterruptedException �X���[�v�����f�����ꍇ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectByTerm_lowerRange()
        throws InterruptedException,
            SQLException
    {
        // ����
        Timestamp start = new Timestamp(System.currentTimeMillis());
        Thread.sleep(SLEEP_TIME);
        Timestamp end = new Timestamp(System.currentTimeMillis());
        Thread.sleep(SLEEP_TIME);
        insertMeasurementValue();
        insertMeasurementValue();
        insertMeasurementValue();
        insertMeasurementValue();

        // ���s
        List<MeasurementValue> actual =
                MeasurementValueDao.selectByTerm(DB_NAME, start, end);

        // ����
        assertEquals(0, actual.size());
    }

    /**
     * @target testSelectByTerm_upperRange
     * @test �w�肵���͈͂̎擾
     *   condition:: �͈͂�����������Ă���B
     *   result:: ���R�[�h���Ԃ�Ȃ����ƁB
     * @throws InterruptedException �X���[�v�����f�����ꍇ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectByTerm_upperRange()
        throws InterruptedException,
            SQLException
    {
        // ����
        insertMeasurementValue();
        insertMeasurementValue();
        insertMeasurementValue();
        insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        Thread.sleep(SLEEP_TIME);
        Timestamp end = new Timestamp(System.currentTimeMillis());

        // ���s
        List<MeasurementValue> actual =
                MeasurementValueDao.selectByTerm(DB_NAME, start, end);

        // ����
        assertEquals(0, actual.size());
    }

    /**
     * @target testSelectAll_exist
     * @test ���ׂĎ擾
     *   condition:: ���R�[�h�����݂���B
     *   result:: ���R�[�h���Ԃ邱�ƁB
     * @throws InterruptedException �X���[�v�����f�����ꍇ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectAll_exist()
        throws InterruptedException,
            SQLException
    {
        // ����
        MeasurementValue measurementValue1 = insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        MeasurementValue measurementValue2 = insertMeasurementValue();

        // ���s
        List<MeasurementValue> actual = MeasurementValueDao.selectAll(DB_NAME);

        // ����
        assertEquals(2, actual.size());
        assertEquals(measurementValue1, actual.get(0));
        assertEquals(measurementValue2, actual.get(1));
    }

    /**
     * @target testSelectAll_notExist
     * @test ���ׂĎ擾
     *   condition:: ���R�[�h�����݂��Ȃ��B
     *   result:: ���R�[�h���Ԃ�Ȃ����ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectAll_notExist()
        throws SQLException
    {
        // ���s
        List<MeasurementValue> actual = MeasurementValueDao.selectAll(DB_NAME);

        // ����
        assertEquals(0, actual.size());
    }

    /**
     * @target testGetTerm_existOne
     * @test �����͈͎̔擾
     *   condition:: ���R�[�h�� 1 ���݂���B
     *   result:: �����͈̔͂��Ԃ邱�ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testGetTerm_existOne()
        throws SQLException
    {
        // ����
        MeasurementValue measurementValue = insertMeasurementValue();

        // ���s
        Timestamp[] actual = MeasurementValueDao.getTerm(DB_NAME);

        // ����
        assertNotNull(actual);
        assertEquals(measurementValue.measurementTime, actual[0]);
        assertEquals(measurementValue.measurementTime, actual[1]);
    }

    /**
     * @target testGetTerm_existMany
     * @test �����͈͎̔擾
     *   condition:: ���R�[�h���������݂���B
     *   result:: �����͈̔͂��Ԃ邱�ƁB
     * @throws InterruptedException �X���[�v�����f�����ꍇ
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testGetTerm_existMany()
        throws InterruptedException,
            SQLException
    {
        // ����
        MeasurementValue measurementValue1 = insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        insertMeasurementValue();
        Thread.sleep(SLEEP_TIME);
        MeasurementValue measurementValue2 = insertMeasurementValue();

        // ���s
        Timestamp[] actual = MeasurementValueDao.getTerm(DB_NAME);

        // ����
        assertNotNull(actual);
        assertEquals(measurementValue1.measurementTime, actual[0]);
        assertEquals(measurementValue2.measurementTime, actual[1]);
    }

    /**
     * @target testGetTerm_notExist
     * @test �����͈͎̔擾
     *   condition:: ���R�[�h�����݂��Ȃ��B
     *   result:: <code>null</code> ���Ԃ邱�ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testGetTerm_notExist()
        throws SQLException
    {
        // ���s
        Timestamp[] actual = MeasurementValueDao.getTerm(DB_NAME);

        // ����
        assertNotNull(actual);
        assertNull(actual[0]);
        assertNull(actual[1]);
    }

}
