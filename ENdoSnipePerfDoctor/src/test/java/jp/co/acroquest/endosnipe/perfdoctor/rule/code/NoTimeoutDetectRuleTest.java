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
 * �^�C���A�E�g�l�������`�F�b�N���[���̃e�X�g
 * @author fujii
 *
 */
public class NoTimeoutDetectRuleTest extends PerformanceRuleTestCase
{
    /**
     * NoTimeoutDetectRule�𐶐�����B<br />
     * @return NoTimeoutDetectRule
     */
    private NoTimeoutDetectRule createRule()
    {
        NoTimeoutDetectRule rule = createInstance(NoTimeoutDetectRule.class);
        rule.id = "COD.IO.NOTIMEOUT";
        rule.active = true;
        rule.level = "ERROR";
        return rule;
    }

    /**
     * [����] 3-22-1<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E���E�l����(臒l�Ɠ������ꍇ)<br />
     * ���x������������B<br />
     */
    public void testDoJudge_2()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_0.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), "java.net.SocksSocketImpl@f0c0d3", 0);

    }

    /**
     * [����] 3-22-2<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l��������ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_10()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_parameterString.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-3<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^�̒l����ɂȂ��Ă���ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_11()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_parameterEmpty.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-4<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�p�����[�^���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_12()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_parameterNone.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-5<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽInfo���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_14()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_noEventInfo.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-6<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�^�C�v�̃��b�Z�[�W���Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_15()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_noEventType.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-7<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E�w�肳�ꂽ�C�x���g�����Ȃ��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_16()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_noEventName.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(0, getErrorJavelinLogElements().size());
    }

    /**
     * [����] 3-22-8<br />
     * <br />
     * doJudge�̃e�X�g�B<br />
     * �E������JavelinLogElement�Ōx�����o��ꍇ<br />
     * ���x�����������Ȃ��B<br />
     */
    public void testDoJudge_27()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_multiElement.jvn");

        NoTimeoutDetectRule rule = createRule();

        // ���s
        rule.judge(elementList);

        // ����
        assertEquals(2, getErrorJavelinLogElements().size());

        // CALL �� EVENT �� RETURN �� CALL �� EVENT �� RETURN
        // �̏���elementList�̃C�x���g���쐬�����̂ŁA
        // 2�Ԗڂ�5�Ԗڂ̃C�x���g���x���ɏo�͂���邱�Ƃ��m�F����B
        assertErrorOccurred(elementList.get(1), "java.net.SocksSocketImpl@5f8172", 0);
        assertErrorOccurred(elementList.get(4), "java.net.SocksSocketImpl@5f8173", 0);
    }

    /**
     * [����] 3-22-9<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���s����O����������ꍇ<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_29_RuntimeException()
    {
        // ����
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NoTimeoutDetect_testDoJudge_0.jvn");

        NoTimeoutDetectRule rule = createRule();

        elementList.add(0, null);

        // ���s
        rule.doJudge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), "java.net.SocksSocketImpl@f0c0d3", 0);
    }
}
