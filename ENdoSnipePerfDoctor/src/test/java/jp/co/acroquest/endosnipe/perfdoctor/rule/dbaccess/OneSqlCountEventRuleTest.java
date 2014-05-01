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
package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * {@link OneSqlCountEventRule} �̂��߂̃e�X�g�N���X�ł��B<br/>
 * 
 * @author akita
 */
public class OneSqlCountEventRuleTest extends PerformanceRuleTestCase
{
    /**
     * {@link OneSqlCountEventRule} �𐶐����܂��B
     * 
     * @param threshold 臒l
     * @return {@link OneSqlCountEventRule} �I�u�W�F�N�g
     */
    private OneSqlCountEventRule createRule(int threshold)
    {
        OneSqlCountEventRule rule = createInstance(OneSqlCountEventRule.class);
        rule.id = "DBA.MTRC.ONE_SQL_CNT_EVT";
        rule.active = true;
        rule.level = "ERROR";
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-15-1<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l��9�ł���ꍇ�B<br/>
     * �E臒l��10�ł���ꍇ�B<br/>
     * ���x���͔������܂���B<br/>
     */
    public void testDoJudge_01()
    {
        OneSqlCountEventRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_9.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-15-2<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l��10�ł���ꍇ�B<br/>
     * �E臒l��10�ł���ꍇ�B<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudge_02()
    {
        OneSqlCountEventRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_10.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 10, "2", 10, "http-8080-1@30(java.lang.Thread@afca92)");
    }

    /**
     * [����] 3-15-3<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l��11�ł���ꍇ�B<br/>
     * �E臒l��10�ł���ꍇ�B<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudge_03()
    {
        OneSqlCountEventRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_11.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 10, "2", 11, "http-8080-1@30(java.lang.Thread@afca92)");
    }

    /**
     * [����] 3-15-4<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l��3�ł���ꍇ�B<br/>
     * �E臒l��3�ł���ꍇ�B<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudge_05()
    {
        OneSqlCountEventRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_3.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 3, "2", 3, "http-8080-1@30(java.lang.Thread@afca92)");
    }

    /**
     * [����] 3-15-5<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l�����l�ł͂Ȃ��ꍇ�B<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_stringVal.jvn");

        OneSqlCountEventRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-15-6<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l���󔒂ł���ꍇ�B<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_empty.jvn");

        OneSqlCountEventRule rule = createRule(10);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-15-7<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �ESQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l�̃p�����[�^�����݂��Ȃ��ꍇ�B<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudge_12()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_no_param.jvn");

        OneSqlCountEventRule rule = createRule(10);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
    * [����] 3-15-8<br/>
    * <br/>
    * doJudge�̃e�X�g�ł��B<br/>
    * �EeventInfo���Ȃ��ꍇ�B<br/>
    * ���x����\�����܂���B<br/>
    */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_no_eventInfo.jvn");

        OneSqlCountEventRule rule = createRule(10);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-15-9<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �EEVENT���Ȃ��ꍇ�B<br/>
     * ���x����\�����܂���B<br/>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_no_event.jvn");

        OneSqlCountEventRule rule = createRule(10);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-15-10<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �E������JavelinLogElement�Ōx�����o��ꍇ�B<br/>
     *���x����2�������܂��B<br/>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_multi.jvn");

        OneSqlCountEventRule rule = createRule(10);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 10, "2", 11, "http-8080-1@30(java.lang.Thread@afca92)");
        assertErrorOccurred(elementList.get(2), 10, "2", 11, "http-8080-2@30(java.lang.Thread@afca93)");
    }

    /**
     * [����] 3-15-11<br/>
     * <br/>
     * doJudge�̃e�X�g�ł��B<br/>
     * �E����JavelinLogElement�Ŏ��s����O����������ꍇ<br/>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br/>
     */
    public void testDoJudge_RuntimeException()
    {
        OneSqlCountEventRule rule = createRule(500);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountEventRuleTest_testDoJudge_11.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }

    }

}
