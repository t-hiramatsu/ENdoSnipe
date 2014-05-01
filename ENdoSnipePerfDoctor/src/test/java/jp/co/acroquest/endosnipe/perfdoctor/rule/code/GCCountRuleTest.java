package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * GC���s�p�x���胋�[���p�̃e�X�g�P�[�X<br>
 * <br>
 * @author S.Kimura
 */
public class GCCountRuleTest extends PerformanceRuleTestCase
{

    /**
     * 臒l���w�肵��GCFrequencyRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return GCFrequencyRule
     */
    private GCCountRule createRule(int threshold)
    {
        GCCountRule rule = createInstance(GCCountRule.class);
        rule.id = "COD.MTRC.GC_CNT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-5-1<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�p�x��0�B<br>
     * �E臒l��1�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_00.jvn");

        GCCountRule rule = createRule(1);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }

    }

    /**
     * [����] 2-5-2<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�񐔂�1�B<br>
     * �E臒l��1�B<br>
     * ���x�����������Ȃ��B(�x�����X�g�̃T�C�Y��0)<br>
     */
    public void testDoJudge_02()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_01.jvn");

        GCCountRule rule = createRule(1);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1, 1, 413.0);
    }

    /**
     * [����] 2-5-3<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�p�x��2�B<br>
     * �E臒l��1�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_02.jvn");

        GCCountRule rule = createRule(1);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1, 2, 413.0);
    }

    /**
     * [����] 2-5-5<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�p�x��20�B<br>
     * �E臒l��20�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_05()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_20.jvn");

        GCCountRule rule = createRule(20);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 20, 20, 413.0);
    }

    /**
     * [����] 2-5-10<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EGC�񐔂̒l�����l�ł͂Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_invalid.jvn");

        GCCountRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-5-11<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EGC�񐔂̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_empty.jvn");

        GCCountRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-5-12<br>
     * <br>
     * �ُ탍�O<br>
     * ���O���Ƀp�����[�^�������B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_12()
    {
        GCCountRule rule = createRule(5);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_no_param.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-5-14<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_no_detailInfo.jvn");

        GCCountRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-5-15<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_no_call.jvn");

        GCCountRule rule = createRule(5);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-5-26<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_multi.jvn");

        GCCountRule rule = createRule(1);
        rule.judge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1, 2, 413.0);
        assertErrorOccurred(elementList.get(4), 1, 2, 413.0);
    }

    /**
     * [����] 2-5-27<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("GCFrequencyRule_testDoJudge_02.jvn");

        elementList.add(0, null);

        GCCountRule rule = createRule(1);
        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 1, 2, 413.0);

    }

}
