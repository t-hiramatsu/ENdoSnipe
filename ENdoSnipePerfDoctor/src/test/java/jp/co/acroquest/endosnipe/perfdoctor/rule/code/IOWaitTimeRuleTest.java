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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * ENdoSnipeVer.4.0�̐V���[��
 * TAT�͒������ACPU���ԁAWAIT���ԁA�u���b�N���Ԃ��Z�����[���̃e�X�g
 * @author akita
 *
 */
public class IOWaitTimeRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��LongTATShortCPUWaitBlockRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return IOWaitTimeRule
     */
    private IOWaitTimeRule createRule(final long threshold)
    {
        IOWaitTimeRule rule = createInstance(IOWaitTimeRule.class);
        rule.id = "COD.MTRC.METHOD_IO_WAIT_TIME";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-12-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���o�l��4999�B<br>
     * �E臒l��5000�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        IOWaitTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_4999.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���o�l��5000�B<br>
     * �E臒l��5000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        IOWaitTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_5000.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5000L);
    }

    /**
     * [����] 3-12-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���o�l��5001�B<br>
     * �E臒l��5000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        IOWaitTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_5001.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5001L);
    }

    /**
     * [����] 3-12-4<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���o�l��10�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_05()
    {
        IOWaitTimeRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_10.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 10L, 10L);
    }

    /**
     * [����] 3-12-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l�����l�ł͂Ȃ�������ł���ꍇ�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_invalid.jvn");

        IOWaitTimeRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-6<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_no_value.jvn");

        IOWaitTimeRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-7<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     *�@�w�肳�ꂽ�p�����[�^���Ȃ��B�u���b�N�̃p�����[�^�������ꍇ<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_12()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_no_time.jvn");

        IOWaitTimeRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-8<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B�u���b�N���Ԃ������ꍇ<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_no_detailInfo.jvn");

        IOWaitTimeRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-9<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_no_call.jvn");

        IOWaitTimeRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-12-10<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_multi.jvn");

        IOWaitTimeRule rule = createRule(5000);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5000L);
        assertErrorOccurred(elementList.get(4), 5000L, 5000L);
    }

    /**
     * [����] 3-12-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        IOWaitTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IOWaitTimeRuleTest_testDoJudge_5000.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 5000L, 5000L);
    }

}
