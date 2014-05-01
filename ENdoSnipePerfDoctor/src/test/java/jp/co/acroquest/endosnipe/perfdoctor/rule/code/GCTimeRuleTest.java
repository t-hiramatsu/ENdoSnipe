package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * GC���s���Ԕ��胋�[���p�̃e�X�g�P�[�X<br>
 * ver3.3SCR�Ή��p�̃e�X�g�R�[�h�Ƃ��č쐬����<br>
 * �����d�l���� ENdoSnipe�N���C�A���g �P�̎����d�l��.xls<br>
 * @author SUZUKI TOORU
 */
public class GCTimeRuleTest extends PerformanceRuleTestCase
{

    /**
     * 臒l���w�肵��GCTimeRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return GCTimeRule
     */
    private GCTimeRule createRule(double threshold)
    {
        GCTimeRule rule = createInstance(GCTimeRule.class);
        rule.id = "COD.MTRC.GC_TIME";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 1-2-1<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�l��0�B<br>
     * �E臒l��1�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_00.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(1);
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }

    }

    /**
     * [����] 1-2-2<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�l��1�B<br>
     * �E臒l��1�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_01.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(1);
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1.0, 1.0, 3.0);
    }

    /**
     * [����] 1-2-3<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�l��2�B<br>
     * �E臒l��1�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_02.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(1);
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1.0, 2.0, 3.0);
    }

    /**
     * [����] 1-2-4<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E���o�l��20�B<br>
     * �E臒l��20�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_05()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_20.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(20);
        rule.judge(elementList);

        // ����
        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 20.0, 20.0, 3.0);
    }

    /**
     * [����] 1-2-5<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EGC�񐔂̒l�����l�ł͂Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_invalid.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(5);
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 1-2-6<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EGC�񐔂̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_empty.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(5);
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 1-2-7<br>
     * <br>
     * �ُ탍�O<br>
     * ���O���Ƀp�����[�^�������B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_12()
    {
        // ����
        GCTimeRule rule = createRule(5);
        String jvnFile = "GCTimeRule_testDoJudge_no_param.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 1-2-8<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_no_detailInfo.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(5);
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 1-2-9<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_no_call.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(5);
        rule.judge(elementList);

        // ����
        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 1-2-10<br>
     * <br>
     * judge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        // ����
        String jvnFile = "GCTimeRule_testDoJudge_multi.jvn";
        List<JavelinLogElement> elementList = createJavelinLogElement(jvnFile);

        // ���s
        GCTimeRule rule = createRule(1);
        rule.judge(elementList);

        // ����
        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 1.0, 2.0, 3.0);
        assertErrorOccurred(elementList.get(4), 1.0, 2.0, 3.0);
    }

}
