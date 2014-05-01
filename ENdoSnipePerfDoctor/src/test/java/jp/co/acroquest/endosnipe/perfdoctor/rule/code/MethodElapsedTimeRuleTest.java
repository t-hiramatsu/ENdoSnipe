package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * ���\�b�h�̎��������ԃ��[���p�̃e�X�g�P�[�X
 */
public class MethodElapsedTimeRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��MethodElapsedTimeRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return MethodElapsedTimeRule
     */
    private MethodElapsedTimeRule createRule(long threshold)
    {
        MethodElapsedTimeRule rule = createInstance(MethodElapsedTimeRule.class);
        rule.id = "COD.MTRC.METHOD_ELAPSEDTIME";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-14-1<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ�199<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_val199()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_val199.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-2<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ�200<br>
     * �E臒l��200<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th200_val200()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_val200.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (double)200);
    }

    /**
     * [����] 2-14-3<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ�201<br>
     * �E臒l��200<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th200_val201()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_val201.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (double)201);
    }

    /**
     * [����] 2-14-5<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ�5000<br>
     * �E臒l��5000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th5000_val5000()
    {
        MethodElapsedTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th5000_val5000.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (double)5000);
    }

    /**
     * [����] 2-14-10<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ̒l��������iTest������)<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valstring()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_valstring.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-11<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ̒l����<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valblank()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_valblank.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-12<br>
     * doJudge�̃e�X�g�B<br>
     * �E���������Ԃ̒l�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnovalue()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_valnovalue.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-14<br>
     * doJudge�̃e�X�g�B<br>
     * �EExtraInfo�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnoinfotag()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_valnoinfotag.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-15<br>
     * doJudge�̃e�X�g�B<br>
     * �ECall��ʂ̃��O�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnocall()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_valnocall.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-14-26<br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     * ���x�������������B<br>
     */
    public void testDoJudge_th200_multierror()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_multierror.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (double)201);
        assertErrorOccurred(elementList.get(4), rule.threshold, (double)201);
    }

    /**
     * [����] 2-14-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        MethodElapsedTimeRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodElapsedTimeRuleTest_testDoJudge_th200_val201.jvn");

        elementList.add(0, null);

        JavelinParser.initDetailInfo(elementList);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), rule.threshold, (double)201);

    }

}
