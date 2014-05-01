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
public class ConcurrentAccessRuleTest extends PerformanceRuleTestCase
{
    /** ���O�t�@�C���ɗ��p����Map�̃I�u�W�F�N�gID */
    private static final String MAP_OBJECT_ID = "java.util.HashMap@1b134a0";

    /**
     * ConcurrentAccessRule�𐶐�����B<br>
     * @return ConcurrentAccessRule
     */
    private ConcurrentAccessRule createRule()
    {
        ConcurrentAccessRule rule = createInstance(ConcurrentAccessRule.class);
        rule.id = "COD.THRD.CONCURRENT_ACCESS";
        rule.active = true;
        rule.level = "ERROR";
        return rule;
    }

    /**
     * [����] 3-14-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E�w�肳�ꂽ�l��������ł���ꍇ�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_10()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_valstring.jvn");

        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());

        // elementList�ɂ́ACALL��EVENT�̏��ɓ��͂���Ă���̂ŁA
        // 2�Ԗڂ�JavelinLogElement���x���ɏo�͂����B
        assertErrorOccurred(elementList.get(1), MAP_OBJECT_ID, "Thread-0,Thread-1");

    }

    /**
     * [����] 3-14-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �w�肳�ꂽ�l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_empty.jvn");

        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-14-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     *�@�w�肳�ꂽ�p�����[�^���Ȃ��B�u���b�N�̃p�����[�^�������ꍇ<br>
     * ���x������������B<br>
     */
    public void testDoJudge_12()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_no_param.jvn");
        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-14-4<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EEventInfo���Ȃ��B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_14()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_no_EventInfo.jvn");

        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-14-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EEVENT���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_no_type.jvn");
        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-14-6<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EEVENT���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_16()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_no_eventname.jvn");
        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-14-7<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_27()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_multi_Element.jvn");
        ConcurrentAccessRule rule = createRule();

        // ���s
        rule.doJudge(elementList);

        // ����
        assertEquals(2, getErrorJavelinLogElements().size());
        // elementList�ɂ́ACALL��EVENT��EVENT�̏��ɓ��͂���Ă���̂ŁA
        // 2�ԖځA3�Ԗڂ�JavelinLogElement���x���ɏo�͂����B
        assertErrorOccurred(elementList.get(1), MAP_OBJECT_ID, "Thread-0,Thread-1");
        assertErrorOccurred(elementList.get(2), MAP_OBJECT_ID, "Thread-1,Thread-0");
    }

    /**
     * [����] 3-14-8<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_28_RuntimeException()
    {
        // ����
        ConcurrentAccessRule rule = createRule();
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ConcurrentAccessRuleTest_testDoJudge_valstring.jvn");
        elementList.add(0, null);

        // ���s
        rule.doJudge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        // elementList�ɂ́Anull��CALL��EVENT�̏��ɓ��͂���Ă���̂ŁA
        // 2�Ԗڂ�JavelinLogElement���x���ɏo�͂����B
        assertErrorOccurred(elementList.get(2), MAP_OBJECT_ID, "Thread-0,Thread-1");
    }

}
