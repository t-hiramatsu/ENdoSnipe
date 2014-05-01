package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.rule.PerformanceRuleFacade;

/**
 * {@link AllSqlCountRule} �p�̃e�X�g�N���X�B
 * 
 * @author S.Kimura
 *
 */
public class AllSqlCountRuleTest extends PerformanceRuleTestCase
{
    /**
     * {@link AllSqlCountRule} �𐶐�����B
     * 
     * @param threshold 臒l
     * @return {@link AllSqlCountRule} �I�u�W�F�N�g
     */
    private AllSqlCountRule createRule(int threshold)
    {
        AllSqlCountRule rule = createInstance(AllSqlCountRule.class);
        rule.id = "DBA.MTRC.ALL_SQL_CNT";
        rule.active = true;
        rule.level = "WARN";
        rule.threshold = threshold;
        return rule;
    }

    /**
     * {@link AllSqlCountRule} �𐶐�����(�x�����x�����ݒ�ł���)�B
     * 
     * @param threshold 臒l
     * @param level �x�����x��
     * @return {@link AllSqlCountRule} �I�u�W�F�N�g
     */
    private AllSqlCountRule createRule(int threshold, String level)
    {
        AllSqlCountRule rule = createInstance(AllSqlCountRule.class);
        rule.id = "DBA.MTRC.ALL_SQL_CNT";
        rule.active = true;
        rule.level = level;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-2-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ESQL�̔��s�񐔂�9�B<br>
     * �E臒l��10�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        AllSqlCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_09.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-2-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ESQL�̔��s�񐔂�10�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        AllSqlCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_10.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(9), 10, 10, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-2-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ESQL�̔��s�񐔂�11�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        AllSqlCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_11.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(9), 10, 11, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-2-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ESQL�̔��s�񐔂�3�B<br>
     * �E臒l��3�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_05()
    {
        AllSqlCountRule rule = createRule(3);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_03.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(2), 3, 3, "/employee/employeeSearch.html");
    }

    /**
     * [����] 3-2-13<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E[SQL]�Ƃ����^�O���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_13()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_no_sql.jvn");

        AllSqlCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-2-14<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_no_detailInfo.jvn");

        AllSqlCountRule rule = createRule(5);
        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(4), 5, 6, "main@java.lang.Thread@dda25b");
    }

    /**
     * [����] 3-2-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_no_call.jvn");

        AllSqlCountRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-2-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        AllSqlCountRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testDoJudge_11.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(10), 10, 11, "/employee/employeeSearch.html");
    }

    /**
     * SCR�Ή�<br>
     * [����] 1-3-4
     * ERROR�������B
     * WARN�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Error()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testJudge_RuleFacade_Error.jvn");

        AllSqlCountRule errorRule = createRule(5, "ERROR");
        AllSqlCountRule warnRule = createRule(3, "WARN");
        AllSqlCountRule infoRule = createRule(1, "INFO");

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
     * [����] 1-3-5
     * WARN�������B
     * ERROR�AINFO���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Warn()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testJudge_RuleFacade_Warn.jvn");

        AllSqlCountRule errorRule = createRule(5, "ERROR");
        AllSqlCountRule warnRule = createRule(3, "WARN");
        AllSqlCountRule infoRule = createRule(1, "INFO");

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
     * [����] 1-3-6
     * INFO�������B
     * ERROR�AWARN���������Ȃ����Ƃ��m�F����B<br>
     */
    public void testJudge_RuleFacade_Info()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("AllSqlCountRuleTest_testJudge_RuleFacade_Info.jvn");

        AllSqlCountRule errorRule = createRule(5, "ERROR");
        AllSqlCountRule warnRule = createRule(3, "WARN");
        AllSqlCountRule infoRule = createRule(1, "INFO");

        PerformanceRuleFacade facade = new PerformanceRuleFacade();
        facade.setErrorRule(errorRule);
        facade.setWarnRule(warnRule);
        facade.setInfoRule(infoRule);

        List<WarningUnit> list = facade.judge(elementList);

        assertEquals(1, list.size());
        assertEquals("INFO", list.get(0).getLevel());
    }

}
