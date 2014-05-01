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
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.data.entity.MeasurementValue;
import jp.co.acroquest.endosnipe.test.DataAccessorTestUtil;

/**
 * �v���f�[�^�~�ϋ@�\�̃e�X�g�N���X
 * 
 * @author M.Yoshida
 */
public class MeasurementValueAccumulationTest extends AbstractDaoTest
{
    private static final String[] MEASUREMENT_VALUE_DATA =
        {
        "1, 1, 2009/05/29 12:21:33, 1, 1, 350"   ,
        "2, 1, 2009/05/29 12:22:33, 1, 1, 420"   ,
        "3, 1, 2009/05/29 12:22:45, 1, 1, 470"   ,
        "4, 2, 2009/05/29 12:24:12, 1, 1, 23.5"  ,
        "5, 2, 2009/05/29 12:24:31, 1, 1, 31.5"  ,
        "6, 3, 2009/05/29 12:27:31, 120, 1, 521" ,
        "7, 3, 2009/05/29 12:31:31, 1, 2, 42.5"
        };

    private static final String[] JAVELIN_MEASUREMENT_ITEM_DATA = 
        {
        "1, 1, Test1, 2010/10/9 21:10:07",
        "2, 1, Test2, 2010/10/9 21:10:07",
        "3, 50, Test3, 2010/10/9 21:10:07"
        };
    
    private static final String[] MEASUREMENT_INFO = 
        {
        "1, TESTITEM1, TEST_ITEM_1, This is Test Data"
        };    
    
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    /**
     * ��������3-1-2
     */
    public void testValueInsert1()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> valueInfoList
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[1], MEASUREMENT_VALUE_DATA[2]});

        // ���{
        for(Object entity : valueInfoList)
        {
            try
            {
                MeasurementValueDao.insert(DB_NAME, (MeasurementValue)entity);
            }
            catch (SQLException ex)
            {
                fail(ex.getMessage());
            }
        }
        
        // ����
        List<Object> expectedData 
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[0], 
                             MEASUREMENT_VALUE_DATA[1],
                             MEASUREMENT_VALUE_DATA[2]
                            });
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(MeasurementValueDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertMeasurementValue(expectedData, actualInsert);
    }
    
    /**
     * ��������3-1-1
     */
    public void testValueInsert2()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> valueInfoList
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[3], MEASUREMENT_VALUE_DATA[4]});

        // ���{
        for(Object entity : valueInfoList)
        {
            try
            {
                MeasurementValueDao.insert(DB_NAME, (MeasurementValue)entity);
            }
            catch (SQLException ex)
            {
                fail(ex.getMessage());
            }
        }
        
        // ����
        List<Object> expectedData 
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[0], 
                             MEASUREMENT_VALUE_DATA[3],
                             MEASUREMENT_VALUE_DATA[4]
                            });
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(MeasurementValueDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertMeasurementValue(expectedData, actualInsert);
    }
    
    /**
     * ��������3-1-3
     */
    public void testValueInsert3()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> valueInfoList
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[5]});

        // ���{
        for(Object entity : valueInfoList)
        {
            try
            {
                MeasurementValueDao.insert(DB_NAME, (MeasurementValue)entity);
            }
            catch (SQLException ex)
            {
            }
        }
        
        // ����
        List<Object> expectedData 
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[0], MEASUREMENT_VALUE_DATA[5]});
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(MeasurementValueDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertMeasurementValue(expectedData, actualInsert);
    }

    /**
     * ��������3-1-4
     */
    public void testValueInsert4()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> valueInfoList
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[6]});

        // ���{
        for(Object entity : valueInfoList)
        {
            try
            {
                MeasurementValueDao.insert(DB_NAME, (MeasurementValue)entity);
            }
            catch (SQLException ex)
            {
            }
        }
        
        // ����
        List<Object> expectedData 
            = DataAccessorTestUtil.createMeasurementValueEntities(
                new String[]{MEASUREMENT_VALUE_DATA[0]});
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(MeasurementValueDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertMeasurementValue(expectedData, actualInsert);
    }

    /**
     * ��������3-1-5
     */
    public void testItemInsert1()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> itemInfoList
            = DataAccessorTestUtil.createJavelinMeasurementItemEntities(
                new String[]{JAVELIN_MEASUREMENT_ITEM_DATA[1]});

        // ���{
        for(Object entity : itemInfoList)
        {
            try
            {
                JavelinMeasurementItemDao.insert(DB_NAME, (JavelinMeasurementItem)entity);
            }
            catch (SQLException ex)
            {
            }
        }
        
        // ����
        List<Object> expectedData
            = DataAccessorTestUtil.createJavelinMeasurementItemEntities(
                new String[]{JAVELIN_MEASUREMENT_ITEM_DATA[0],
                             JAVELIN_MEASUREMENT_ITEM_DATA[1]});
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(JavelinMeasurementItemDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertJavelinMeasurementItem(expectedData, actualInsert);
    }
    
    /**
     * ��������3-1-5
     */
    public void testItemInsert2()
    {
        // ����
        // --> DB�o�^�ς݃f�[�^
        initDatabase();
        
        // --> �ǉ��Ώۃf�[�^
        List<Object> itemInfoList
            = DataAccessorTestUtil.createJavelinMeasurementItemEntities(
                new String[]{JAVELIN_MEASUREMENT_ITEM_DATA[2]});

        // ���{
        for(Object entity : itemInfoList)
        {
            try
            {
                JavelinMeasurementItemDao.insert(DB_NAME, (JavelinMeasurementItem)entity);
            }
            catch (SQLException ex)
            {
            }
        }
        
        // ����
        List<Object> expectedData
            = DataAccessorTestUtil.createJavelinMeasurementItemEntities(
                new String[]{JAVELIN_MEASUREMENT_ITEM_DATA[0], JAVELIN_MEASUREMENT_ITEM_DATA[2]});
        
        List<Object> actualInsert = new ArrayList<Object>();
        try
        {
            actualInsert.addAll(JavelinMeasurementItemDao.selectAll(DB_NAME));
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        DataAccessorTestUtil.assertJavelinMeasurementItem(expectedData, actualInsert);
    }
    
    
    private void initDatabase()
    {
        // MeasurementInfoTable��src/main/resources/measurementInfo.tsv����擾���Ă���
//        DataAccessorTestUtil.initializeMeasurementInfoTable(
//            new String[]{MEASUREMENT_INFO[0]});
        DataAccessorTestUtil.initializeJavelinMeasurementItemTable(
            new String[]{JAVELIN_MEASUREMENT_ITEM_DATA[0]});
        DataAccessorTestUtil.initializeMeasurementValueTable(
            new String[]{MEASUREMENT_VALUE_DATA[0]});
    }
    
    
}
