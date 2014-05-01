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
package jp.co.acroquest.endosnipe.perfdoctor;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import junit.framework.TestCase;

/**
 * {@link WarningUnitUtil}�̃e�X�g�P�[�X
 * @author fujii
 *
 */
public class WarningUnitUtilTest extends TestCase
{
    /** ���t�̃t�H�[�}�b�g */
    private static final String DATE_FORMAT = "yyyy/M/d HH:mm:ss.SSS";

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ���݂�����t�ɑ΂���e�X�g�B
     */
    public void testCalculateStartTimeExistDate()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/10 10:10:10.111";

        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);

        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});

        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(startTimeStr, formatter.format(resultDate));
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ����1�P�^
     */
    public void testCalculateStartTimeMonthSingleDigit()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/1/10 10:10:10.111";
    
        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);
    
        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});
    
        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(startTimeStr, formatter.format(resultDate));
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ����1�P�^
     */
    public void testCalculateStartTimeDaySingleDigit()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/1 10:10:10.111";
    
        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);
    
        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});
    
        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(startTimeStr, formatter.format(resultDate));
    }
    
    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ���Ԃ�1�P�^
     */
    public void testCalculateStartTimeHourSingleDigit()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/10 1:10:10.111";
        String expectedResult = "2010/10/10 01:10:10.111";
    
        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);
    
        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});
    
        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(expectedResult, formatter.format(resultDate));
    }
    
    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ����1�P�^
     */
    public void testCalculateStartTimeMinuteSingleDigit()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/10 10:1:10.111";
        String expectedResult = "2010/10/10 10:01:10.111";
    
        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);
    
        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});
    
        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(expectedResult, formatter.format(resultDate));
    }
    
    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * �b��1�P�^
     */
    public void testCalculateStartTimeSecondSingleDigit()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/10 10:10:1.111";
        String expectedResult = "2010/10/10 10:10:01.111";
    
        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);
    
        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});
    
        // ����
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date resultDate = new Date(result.longValue());
        assertEquals(expectedResult, formatter.format(resultDate));
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * �t�H�[�}�b�g�G���[(�~���b���Ȃ�)
     */
    public void testCalculateStartTimeIllegalFormat1()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010/10/10 10:10:10";

        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);

        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});

        // ����
        assertEquals(Long.valueOf(0), result);
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * ���t�̃Z�p���[�^��"-"
     */
    public void testCalculateStartTimeIllegalFormat2()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "2010-10-10 10:10:10.111";

        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);

        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});

        // ����
        assertEquals(Long.valueOf(0), result);
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * �J�n���������w��
     */
    public void testCalculateStartTimeSetNull()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();

        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);

        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});

        // ����
        assertEquals(Long.valueOf(0), result);
    }

    /**
     * calculateStartTimeExistDate �̃e�X�g�P�[�X�B
     * �󕶎����w��
     */
    public void testCalculateStartTimeSetEmpty()
        throws Exception
    {
        // ����
        Method method = getCalculateStartTimeMethod();
        String startTimeStr = "";

        List<String> infoList = new ArrayList<String>(10);
        infoList.add(JavelinLogColumnNum.ID, "CALL");
        infoList.add(JavelinLogColumnNum.CALL_TIME, startTimeStr);
        JavelinLogElement element = new JavelinLogElement();
        element.setBaseInfo(infoList);

        // ���s
        Long result = (Long)method.invoke(null, new Object[]{element});

        // ����
        assertEquals(Long.valueOf(0), result);
    }

    /**
     * WarningUnitUtil#calculateStartTimeMethod ���\�b�h���擾����B
     * @return calculateStartTimeMethod���\�b�h
     */
    private Method getCalculateStartTimeMethod()
        throws Exception
    {
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName("jp.co.acroquest.endosnipe.perfdoctor.WarningUnitUtil");
        }
        catch (ClassNotFoundException ex)
        {
            throw ex;
        }
        Method method;
        try
        {
            method =
                     clazz.getDeclaredMethod("calculateStartTime",
                                             new Class[]{JavelinLogElement.class});
        }
        catch (SecurityException ex)
        {
            throw ex;
        }
        catch (NoSuchMethodException ex)
        {
            throw ex;
        }
        method.setAccessible(true);
        return method;
    }

}
