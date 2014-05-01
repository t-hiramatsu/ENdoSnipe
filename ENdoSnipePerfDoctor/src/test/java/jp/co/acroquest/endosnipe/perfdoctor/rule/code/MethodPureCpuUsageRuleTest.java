package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * ���\�b�h�̎�CPU�g�p���ԃ��[���p�̃e�X�g�P�[�X<br>
 */
public class MethodPureCpuUsageRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��MethodPureCpuUsageRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return MethodPureCpuUsageRule
     */
    private MethodPureCpuUsageRule createRule(long threshold)
    {
        MethodPureCpuUsageRule rule = createInstance(MethodPureCpuUsageRule.class);
        rule.id = "COD.MTRC.METHOD_PURE_CPU";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-13-1<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ�199<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_val199()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_val199.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-2<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ�200<br>
     * �E臒l��200<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th200_val200()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_val200.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)200, "21");
    }

    /**
     * [����] 2-13-3<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ�201<br>
     * �E臒l��200<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th200_val201()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_val201.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)201, "21");
    }

    /**
     * [����] 2-13-5<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ�5000<br>
     * �E臒l��5000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th5000_val5000()
    {
        MethodPureCpuUsageRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th5000_val5000.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)5000, "21");
    }

    /**
     * [����] 2-13-10<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ̒l��������iTest������)<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valstring()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_valstring.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-11<br>
     * doJudge�̃e�X�g�B<br>
     * �E��CPU�g�p���Ԃ̒l����<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valblank()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_valblank.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-12<br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU�g�p���Ԃ̒l�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnovalue()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_valnovalue.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-14<br>
     * doJudge�̃e�X�g�B<br>
     * �EJMXInfo�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnoinfotag()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_valnoinfotag.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-15<br>
     * doJudge�̃e�X�g�B<br>
     * �ECall��ʂ̃��O�����݂��Ȃ��B<br>
     * �E臒l��200<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th200_valnocall()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_valnocall.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-13-26<br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     * ���x�������������B<br>
     */
    public void testDoJudge_th200_multierror()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_multierror.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)201, "21");
        assertErrorOccurred(elementList.get(4), rule.threshold, (long)201, "21");
    }

    /**
     * [����] 2-13-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        MethodPureCpuUsageRule rule = createRule(200);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodPureCpuUsageRuleTest_testDoJudge_th200_val201.jvn");

        elementList.add(0, null);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), rule.threshold, (long)201, "21");
    }

}
