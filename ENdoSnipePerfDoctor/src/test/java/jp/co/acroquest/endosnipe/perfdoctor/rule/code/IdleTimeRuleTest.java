package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * IdleTimeRule�̃e�X�g�P�[�X�ł��B<br/>
 * @author fujii
 *
 */
public class IdleTimeRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��IdleTimeRule�𐶐����܂��B<br/>
     * @param threshold 臒l
     * @return IdleTimeRule
     */
    private IdleTimeRule createRule(long threshold)
    {
        IdleTimeRule rule = createInstance(IdleTimeRule.class);
        rule.id = "COD.THRD.IDLE_TIME";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����]3-12-1 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF2999<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeUnderThreashold()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_1999.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-2 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF3000<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudgeEqualThreashold()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_2000.jvn");

        rule.doJudge(elementList);

        assertErrorOccurred(elementList.get(0), rule.threshold, (long)2000, "/add/add.html");
    }

    /**
     * [����]3-12-3 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF3001<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudgeOverThreashold()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_2001.jvn");

        rule.doJudge(elementList);

        assertErrorOccurred(elementList.get(0), rule.threshold, (long)2001, "/add/add.html");
    }

    /**
     * [����]3-12-4 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF20<br/>
     * �ECPU���ԁF10<br/>
     * �E臒l�F10<br/>
     * ���x�����������܂��B<br/>
     */
    public void testDoJudgeOtherThreashold()
    {
        IdleTimeRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_10.jvn");

        rule.doJudge(elementList);

        assertErrorOccurred(elementList.get(0), rule.threshold, (long)10, "/add/add.html");
    }

    /**
     * [����]3-12-9 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF�Ȃ�<br/>
     * �ECPU���ԁF�Ȃ�<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExecNoneCpuNone()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_ExecNoneCpuNone.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-10 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF�Ȃ�<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExecNoneCpu1000()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_ExecNoneCpu1000.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-5 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF������<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExecStringCpu1000()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_ExecStringCpu1000.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-7 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF��<br/>
     * �ECPU���ԁF1000<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExecEmptyCpu1000()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_ExecEmptyCpu1000.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-11 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF1000<br/>
     * �ECPU���ԁF�Ȃ�<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExec1000CpuNone()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_Exec1000CpuNone.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-6 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF1000<br/>
     * �ECPU���ԁF������<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExec1000CpuString()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_Exec1000CpuString.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-8 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E���s���ԁF1000<br/>
     * �ECPU���ԁF��<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeExec1000CpuEmpty()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_Exec1000CpuEmpty.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-12 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �EdetailInfo���Ȃ��ꍇ<br/>
     * �E臒l�F2000<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeNoDetailInfo()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_NoDetailInfo.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-13<br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B�B<br/>
     * �E�uCall�v���Ȃ��ꍇ�B<br/>
     * ���x�����������܂���B<br/>
     */
    public void testDoJudgeCall()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_NoCall.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����]3-12-14 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E������JavelinLogElement�Ōx�����o�܂��B<br/>
     */
    public void testDoJudgeMulti()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_Multi.jvn");

        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)2000, "/add/add.html");
        assertErrorOccurred(elementList.get(1), rule.threshold, (long)2001, "/add/add.html");
    }

    /**
     * [����]3-12-15 <br/>
     * <br/>
     * doJudge�̃e�X�g�����܂��B<br/>
     * �E����JavelinLogElement�Ŏ��s����O����������ꍇ<br/>
     * ������JavelinLogElement�̓X�L�b�v���ď������܂��B<br/>
     */
    public void testDoJudgeRuntimeException()
    {
        IdleTimeRule rule = createRule(2000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("IdleTimeRuleTest_testDoJudge_2000.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());

        assertErrorOccurred(elementList.get(1), rule.threshold, (long)2000,
                            "/add/add.html");
    }
}
