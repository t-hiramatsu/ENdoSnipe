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
 * �V���O���X���b�h�pHttPClient�̕����񏉊����`�F�b�N���[���̃e�X�g
 * @author fujii
 *
 */
public class InitDuplicationRuleHttpClientTest extends PerformanceRuleTestCase
{
    /** HttpClient�̏������N���X */
    private static final String HTTPCLIENT_INITIAL_CLASS =
            "org.apache.commons.httpclient.SimpleHttpConnectionManager";

    /** HttpClient�̏��������\�b�h */
    private static final String HTTPCLIENT_INITIAL_METHOD = "SimpleHttpConnectionManager";

    /**
     * InitDupulicationRule�𐶐�����B<br />
     * @param threshold 臒l
     * @return InitDupulicationRule
     */
    private InitDupulicationRule createRule(long threshold)
    {
        InitDupulicationRule rule = createInstance(InitDupulicationRule.class);
        rule.id = "LIB.HTTPCLIENT.SINGLE_THREAD_USE";
        rule.active = true;
        rule.level = "WARN";
        rule.threshold = threshold;
        rule.classNameList = HTTPCLIENT_INITIAL_CLASS;
        rule.methodNameList = HTTPCLIENT_INITIAL_METHOD;
        return rule;
    }

    /**
     * [����] 3-17-1<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l���1�������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_1()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_999.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, (long)999, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);

    }

    /**
     * [����] 3-17-2<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l�Ɠ������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_2()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_1000.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, (long)1000, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);

    }

    /**
     * [����] 3-17-3<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l���1�傫���ꍇ)<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_3()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_1001.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-4<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l��ʂ̒l�ɐݒ�/臒l�Ɠ������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_5()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_500.jvn");

        long threshold = 500;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, (long)500, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);
    }

    /**
     * [����] 3-17-5<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l0�A���o�l0�̏ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_7()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_0.jvn");

        long threshold = 0;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), threshold, (long)0, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);
    }

    /**
     * [����] 3-17-6<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l0�A���o�l�����̒l�̏ꍇ)<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_8()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_threshold0.jvn");

        long threshold = 0;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-7<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l��������ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_10()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_parameterString.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-8<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l����ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_11()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_parameterEmpty.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-9<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_12()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_parameterNone.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-10<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽInfo���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_14()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_NoInfo.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-11<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�^�C�v�̃��b�Z�[�W���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_15()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_NoType.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-12<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�C�x���g�����Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_16()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_NoEventName.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-13<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�N���X�������ꂾ���A���\�b�h�����قȂ�ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_24()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_wrongMethodName.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-14<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���\�b�h�������ꂾ���A�N���X�����قȂ�ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_25()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_wrongClassName.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-17-15<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E������JavelinLogElement�Ōx�����o��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_27()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_multiElement.jvn");

        long threshold = 1000;

        InitDupulicationRule rule = createRule(threshold);

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(2, getErrorJavelinLogElements().size());

        // CALL �� EVENT �� RETURN �� CALL �� EVENT �� RETURN
        // �̏���elementList�̃C�x���g���쐬�����̂ŁA
        // 2�Ԗڂ�5�Ԗڂ̃C�x���g���x���ɏo�͂���邱�Ƃ��m�F����B
        assertErrorOccurred(elementList.get(1), threshold, (long)1000, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);
        assertErrorOccurred(elementList.get(4), threshold, (long)500, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);
    }

    /**
     * [����] 3-17-16<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���s����O����������ꍇ<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_29_RuntimeException()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("InitDuplicateTest_testDoJudge_httpclient_1000.jvn");

        long threshold = 1000;
        InitDupulicationRule rule = createRule(threshold);

        elementList.add(0, null);

        // ���s
        rule.doJudge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), threshold, (long)1000, HTTPCLIENT_INITIAL_CLASS,
                            HTTPCLIENT_INITIAL_METHOD);
    }

}
