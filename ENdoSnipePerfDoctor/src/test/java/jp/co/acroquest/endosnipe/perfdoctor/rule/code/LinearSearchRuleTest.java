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
 * ���`�������o���[��
 * @author fujii
 *
 */
public class LinearSearchRuleTest extends PerformanceRuleTestCase
{
    /**
     * InitDupulicationRule�𐶐�����B<br />
     * @param threshold 臒l
     * @return InitDupulicationRule
     */
    private LinearSearchRule createRule(int threshold)
    {
        LinearSearchRule rule = createInstance(LinearSearchRule.class);
        rule.id = "COD.THRD.LINEAR_SEARCH";
        rule.active = true;
        rule.level = "WARN";
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-23-1<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l���1�������ꍇ)<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_1()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_99.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-2<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l�Ɠ������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_2()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_100.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, "100", "495", "TestList@123456");

    }

    /**
     * [����] 3-23-3<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l���1�傫���ꍇ)<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_3()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_101.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, "101", "500", "TestList@123456");

    }

    /**
     * [����] 3-23-4<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l��ʂ̒l�ɐݒ�/臒l�Ɠ������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_5()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_500.jvn");

        int threshold = 500;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, "500", "2495", "TestList@123456");
    }

    /**
     * [����] 3-23-5<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l0�A���o�l0�̏ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_7()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_0.jvn");

        int threshold = 0;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, "0", "0", "TestList@123456");
    }

    /**
     * [����] 3-23-6<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l0�A���o�l�����̒l�̏ꍇ)<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_8()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_threshold0.jvn");

        int threshold = 0;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, "100", "495", "TestList@123456");
    }

    /**
     * [����] 3-23-7<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l��������ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_10()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_parameterString.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-8<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l����ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_11()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_parameterEmpty.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-9<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_12()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_parameterNone.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-10<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽInfo���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_14()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_NoInfo.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-11<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�^�C�v�̃��b�Z�[�W���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_15()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_NoType.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-12<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�C�x���g�����Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_16()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_NoEventName.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-23-13<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E������JavelinLogElement�Ōx�����o��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_27()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_multiElement.jvn");

        int threshold = 100;

        LinearSearchRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(2, getErrorJavelinLogElements().size());

        // CALL �� EVENT �� RETURN �� CALL �� EVENT �� RETURN
        // �̏���elementList�̃C�x���g���쐬�����̂ŁA
        // 2�Ԗڂ�5�Ԗڂ̃C�x���g���x���ɏo�͂���邱�Ƃ��m�F����B
        assertErrorOccurred(elementList.get(1), threshold, "100", "495",
                            "java.util.ArrayList@6f50a8");
        assertErrorOccurred(elementList.get(4), threshold, "1000", "4995",
                            "java.util.ArrayList@b8deef");
    }

    /**
     * [����] 3-23-14<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���s����O����������ꍇ<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_29_RuntimeException()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("LinearSearchRule_testDoJudge_100.jvn");

        int threshold = 100;
        LinearSearchRule rule = createRule(threshold);

        elementList.add(0, null);

        // ���s
        rule.doJudge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), threshold, "100", "495", "TestList@123456");
    }

}
