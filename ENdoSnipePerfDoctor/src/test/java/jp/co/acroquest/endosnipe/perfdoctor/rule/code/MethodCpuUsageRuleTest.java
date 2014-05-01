package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * @author tsukano
 */
public class MethodCpuUsageRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��CpuUsageRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return CpuUsageRule
     */
    private MethodCpuUsageRule createRule(long threshold)
    {
        MethodCpuUsageRule rule = createInstance(MethodCpuUsageRule.class);
        rule.id = "COD.MTRC.METHOD_CPU";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-6-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ�2999�B<br>
     * �E臒l��3000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_01()
    {
        // ����
        MethodCpuUsageRule rule = createRule(3000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_2999.jvn");

        // ���s
        rule.doJudge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ�3000�B<br>
     * �E臒l��3000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        // ����
        MethodCpuUsageRule rule = createRule(3000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_3000.jvn");

        // ���s
        rule.doJudge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        assertEquals(1, errorList.size());

        assertErrorOccurred(elementList.get(0), rule.threshold, (long)3000,
                            "/employee/employeeSearch.html");
    }

    /**
     * [����] 2-6-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ�3001�B<br>
     * �E臒l��3000�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        // ����
        MethodCpuUsageRule rule = createRule(3000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_3001.jvn");

        // ���s
        rule.doJudge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        assertEquals(1, errorList.size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)3001,
                            "/employee/employeeSearch.html");
    }

    /**
     * [����] 2-6-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ�10�B<br>
     * �E臒l��10�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_05()
    {
        MethodCpuUsageRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_10.jvn");

        rule.doJudge(elementList);

        assertErrorOccurred(elementList.get(0), rule.threshold, (long)10,
                            "/employee/employeeSearch.html");
    }

    /**
     * [����] 2-6-10<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ̒l�����l�ł͂Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_invalid.jvn");

        MethodCpuUsageRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECPU���Ԃ̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_empty.jvn");

        MethodCpuUsageRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-12<br>
     * <br>
     * �ُ탍�O<br>
     * ���O���Ƀp�����[�^�������B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_12()
    {
        MethodCpuUsageRule rule = createRule(5);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_no_param.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-14<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_no_detailInfo.jvn");

        MethodCpuUsageRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ERETURN���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_no_return.jvn");

        MethodCpuUsageRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-6-26<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRule_testDoJudge_multi.jvn");

        MethodCpuUsageRule rule = createRule(3000);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), (long)3000, (long)3001,
                            "/employee/employeeSearch.html");
        assertErrorOccurred(elementList.get(1), (long)3000, (long)3001,
                            "/employee/employeeSearch.html");
    }

    /**
     * [����] 2-6-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        MethodCpuUsageRule rule = createRule(3000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("ThreadCpuUsageRuleTest_testDoJudge_3001.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), rule.threshold, (long)3001,
                            "/employee/employeeSearch.html");
    }

}
