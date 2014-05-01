package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * ���\�b�h��TAT���[���̃e�X�g<br>
 * @author tooru
 *
 */
public class MethodTatRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��CodeMetricsRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return CodeMetricsRule
     */
    private MethodTatRule createRule(long threshold)
    {
        MethodTatRule rule = createInstance(MethodTatRule.class);
        rule.id = "COD.MTRC.METHOD_CNT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-8-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��4999�B<br>
     * �E臒l��5000�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        MethodTatRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_4999.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��5000�B<br>
     * �E臒l��5000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        MethodTatRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_5000.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5000L);
    }

    /**
     * [����] 2-8-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��5001�B<br>
     * �E臒l��5000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        MethodTatRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_5001.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5001L);
    }

    /**
     * [����] 2-8-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��10�B<br>
     * �E臒l��10�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_05()
    {
        MethodTatRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_10.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 10L, 10L);
    }

    /**
     * [����] 2-8-10<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l�����l�ł͂Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_invalid.jvn");

        MethodTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_empty.jvn");

        MethodTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-13<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E[TIME]�Ƃ����^�O���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_13()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_no_time.jvn");

        MethodTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-14<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_no_detailInfo.jvn");

        MethodTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_no_call.jvn");

        MethodTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-8-26<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_multi.jvn");

        MethodTatRule rule = createRule(5000);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 5000L, 5001L);
        assertErrorOccurred(elementList.get(4), 5000L, 5001L);
    }

    /**
     * [����] 2-8-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        MethodTatRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTatRuleTest_testDoJudge_5001.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 5000L, 5001L);
    }

}
