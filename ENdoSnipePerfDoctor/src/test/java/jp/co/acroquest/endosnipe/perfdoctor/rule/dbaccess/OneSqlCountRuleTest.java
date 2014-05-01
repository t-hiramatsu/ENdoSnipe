package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.rule.PerformanceRuleFacade;

/**
 * ����SQL�̔��s�񐔃��[���̃e�X�g<br>
 * @author tooru
 *
 */
public class OneSqlCountRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��OneSqlCountRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return OneSqlCountRule
     */
    private OneSqlCountRule createRule(int threshold)
    {
        OneSqlCountRule rule = createInstance(OneSqlCountRule.class);
        rule.id = "DBA.MTRC.ONE_SQL_CNT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    private OneSqlCountRule createRule(int threshold, String level)
    {
        OneSqlCountRule rule = createInstance(OneSqlCountRule.class);
        rule.id = "DBA.MTRC.ONE_SQL_CNT";
        rule.active = true;
        rule.level = level;
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-3-1<br>
     * <br>
     * ����F臒l�`�F�b�N<br>
     * 臒l��菬�����l�����o���G���[�͏o�͂��Ȃ��B<br>
     * (臒l3�ɑ΂��A�����SQL��2�񔭍s)<br>
     */
    public void testDoJudge_01()
    {
        OneSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_th3_exe2.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-3-2<br>
     * <br>
     * ����F臒l�`�F�b�N<br>
     * 臒l�Ɠ����l�����o���G���[���o�́B<br>
     * (臒l3�ɑ΂��A�����SQL��3�񔭍s)<br>
     */
    public void testDoJudge_02()
    {
        OneSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_th3_exe3.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(2), 3, 3L, "50", 1, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-3-3<br>
     * <br>
     * ����F臒l�`�F�b�N<br>
     * 臒l�𒴂����l�����o���G���[���o�́B<br>
     * (臒l3�ɑ΂��A�����SQL��4�񔭍s)<br>
     */
    public void testDoJudge_03()
    {
        OneSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_th3_exe4.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(2), 3, 4L, "50", 1, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-3-5<br>
     * <br>
     * ����F臒l�`�F�b�N<br>
     * 臒l�Ɠ����l�����o���G���[���o�́B<br>
     * (臒l1�ɑ΂��A�����SQL��1�񔭍s)<br>
     */
    public void testDoJudge_05()
    {
        OneSqlCountRule rule = createRule(1);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_1.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1, 1L, "50", 1, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-3-9<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E2��ނ�SQL���B<br>
     * �E���s�񐔂͂��ꂼ��2�񂸂B<br>
     * �E臒l��3�B<br>
     * ���x���͔������Ȃ��B<br>
     */
    public void testDoJudge_09()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_2sqls_times2.jvn");

        OneSqlCountRule rule = createRule(3);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }

    }

    /**
     * [����] 3-3-13<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E[SQL]�Ƃ����^�O���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_13()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_no_sql.jvn");

        OneSqlCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-3-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_no_call.jvn");

        OneSqlCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-3-18<br>
     * <br>
     * �ُ�f�[�^<br>
     * SQL���ɂȂ��Ă��Ȃ��������񂪓���ł���΁A�����uSQL���v�ł���Ɣ��f����������B<br>
     * (臒l3�ɑ΂��A�����SQL��4�񔭍s)<br>
     */
    public void testDoJudge_18()
    {
        OneSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_not_sql.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(2), 3, 4L, "0", 1, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-3-26<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_multi.jvn");

        OneSqlCountRule rule = createRule(3);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(2), 3, 4L, "50", 1, "/employee/employeeSearch.html");
        assertErrorOccurred(elementList.get(6), 3, 4L, "50", 1, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-3-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        OneSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testDoJudge_th3_exe4.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(3), 3, 4L, "50", 1, "/employee/employeeSearch.html");

    }

    /**
     * SCR�Ή�<br>
     * [����] 1-3-7
     * ERROR�������B
     * WARN�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Error()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testJudge_RuleFacade_Error.jvn");

        OneSqlCountRule errorRule = createRule(5, "ERROR");
        OneSqlCountRule warnRule = createRule(3, "WARN");
        OneSqlCountRule infoRule = createRule(2, "INFO");

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
     * [����] 1-3-8
     * WARN�������B
     * ERROR�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Warn()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testJudge_RuleFacade_Warn.jvn");

        OneSqlCountRule errorRule = createRule(5, "ERROR");
        OneSqlCountRule warnRule = createRule(3, "WARN");
        OneSqlCountRule infoRule = createRule(2, "INFO");

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
     * [����] 1-3-9
     * INFO�������B
     * ERROR�AWARN���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Info()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("OneSqlCountRuleTest_testJudge_RuleFacade_Info.jvn");

        OneSqlCountRule errorRule = createRule(5, "ERROR");
        OneSqlCountRule warnRule = createRule(3, "WARN");
        OneSqlCountRule infoRule = createRule(2, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("INFO", list.get(0).getLevel());
    }
}
