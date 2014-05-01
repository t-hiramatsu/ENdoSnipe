package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.rule.PerformanceRuleFacade;

/**
 * ���\�b�h�̌Ăяo���񐔃��[���̃e�X�g<br>
 * @author tooru
 *
 */
public class MethodCallCountRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��CodeMetricsRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return CodeMetricsRule
     */
    private MethodCallCountRule createRule(int threshold)
    {
        MethodCallCountRule rule = createInstance(MethodCallCountRule.class);
        rule.id = "COD.MTRC.METHOD_CNT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * 臒l���w�肵��CodeMetricsRule�𐶐�����(�x�����x���w��t��)�B<br>
     * @param threshold 臒l
     * @param level �x�����x��
     * @return CodeMetricsRule
     */
    private MethodCallCountRule createRule(int threshold, String level)
    {
        MethodCallCountRule rule = createInstance(MethodCallCountRule.class);
        rule.id = "COD.MTRC.METHOD_CNT";
        rule.active = true;
        rule.level = level;
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-7-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E�Ăяo���񐔂�9�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_01()
    {
        MethodCallCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_th10_call9.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E�Ăяo���񐔂�10�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        MethodCallCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_th10_call10.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(18), rule.threshold, 10);
    }

    /**
     * [����] 2-7-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E�Ăяo���񐔂�11�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        MethodCallCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_th10_call11.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(18), rule.threshold, 11);
    }

    /**
     * [����] 2-7-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E�Ăяo���񐔂�3�B<br>
     * �E臒l��3�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_05()
    {
        MethodCallCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testDoJudge_03.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(4), rule.threshold, 3);
    }

    /**
     * [����] 2-7-9<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E8��Ă΂�郁�\�b�h�ƁA3��Ă΂�郁�\�b�h�����݁B<br>
     * �E臒l��10�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_09()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testDoJudge_8_and_3.jvn");

        MethodCallCountRule rule = createRule(10);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E���\�b�h�����󔒁B<br>
     * �E�N���X���A�X���b�h���͓���B<br>
     * �E���v�񐔂�臒l�𒴂���B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testDoJudge_empty.jvn");

        MethodCallCountRule rule = createRule(10);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(18), rule.threshold, 11);
    }

    /**
     * [����] 2-7-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testDoJudge_no_call.jvn");

        MethodCallCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-23<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����̃N���X�B<br>
     * �E�قȂ郁�\�b�h�B<br>
     * �E���v�񐔂�臒l�𒴂���B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_23()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_methods_in_same_class.jvn");

        MethodCallCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-24<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����̃��\�b�h���B<br>
     * �E�قȂ�N���X�B<br>
     * �E���v�񐔂�臒l�𒴂���B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_24()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_same_method_name.jvn");

        MethodCallCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-25<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����̃��\�b�h�B<br>
     * �E�قȂ�X���b�h�B<br>
     * �E���v�񐔂�臒l�𒴂���B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_25()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_different_thread.jvn");

        MethodCallCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-7-26<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testDoJudge_multi.jvn");

        MethodCallCountRule rule = createRule(10);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(18), 10, 11);
        assertErrorOccurred(elementList.get(40), 10, 11);
    }

    /**
     * [����] 2-7-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        MethodCallCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("CodeMetricsRuleTest_testDoJudge_th10_call11.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(19), rule.threshold, 11);
    }

    /**
     * SCR�Ή�<br>
     * [����] 1-3-1
     * ERROR�������B
     * WARN�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Error()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testJudge_RuleFacade_Error.jvn");

        MethodCallCountRule errorRule = createRule(5, "ERROR");
        MethodCallCountRule warnRule = createRule(3, "WARN");
        MethodCallCountRule infoRule = createRule(1, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("ERROR", list.get(0).getLevel());
    }

    /**
     * SCR�Ή�<br>
     * [����] 1-3-2
     * WARN�������B
     * ERROR�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Warn()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testJudge_RuleFacade_Warn.jvn");

        MethodCallCountRule errorRule = createRule(5, "ERROR");
        MethodCallCountRule warnRule = createRule(3, "WARN");
        MethodCallCountRule infoRule = createRule(1, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("WARN", list.get(0).getLevel());
    }

    /**
     * SCR�Ή�<br>
     * [����] 1-3-3
     * INFO�������B
     * ERROR�AWARN���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Info()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testJudge_RuleFacade_Info.jvn");

        MethodCallCountRule errorRule = createRule(5, "ERROR");
        MethodCallCountRule warnRule = createRule(3, "WARN");
        MethodCallCountRule infoRule = createRule(1, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("INFO", list.get(0).getLevel());
    }

    /**
     * SCR�Ή�<br>
     * [����] 1-5-1
     * INFO�������B
     * ERROR�AWARN���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_IgnoreSQL()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodCallCountRuleTest_testJudge_RuleFacade_IgnoreSQL.jvn");

        MethodCallCountRule errorRule = createRule(5, "ERROR");
        MethodCallCountRule warnRule = createRule(3, "WARN");
        MethodCallCountRule infoRule = createRule(1, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("INFO", list.get(0).getLevel());
    }
}
