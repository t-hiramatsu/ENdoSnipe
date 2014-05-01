package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * �f�B�X�N���͗ʔ��胋�[���p�̃e�X�g�P�[�X<br>
 * <br>
 * @author S.Kimura
 */
public class DiskInputRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��DiskInputRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return DiskInputRule
     */
    private DiskInputRule createRule(long threshold)
    {
        DiskInputRule rule = createInstance(DiskInputRule.class);
        rule.id = "COD.IO.DISK_INPUT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-9-1<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�999999<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_val999999()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_val999999.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-2<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�1000000<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000000()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_val1000000.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000000);
    }

    /**
     * [����] 2-9-3<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�1000001<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000001()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_val1000001.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000001);
    }

    /**
     * [����] 2-9-5<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�1000<br>
     * �E臒l��1000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000_val1000()
    {
        DiskInputRule rule = createRule(1000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000_val1000.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000);
    }

    /**
     * [����] 2-9-10<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ̒l��������iTest������)<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_valstring()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_valstring.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-11<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ̒l����<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_valblank()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_valblank.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-12<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʒl�����݂��Ȃ��B<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_valnovalue()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_valnovalue.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-14<br>
     * doJudge�̃e�X�g�B<br>
     * �EIOInfo�����݂��Ȃ��B<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_valnoinfotag()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_valnoinfotag.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-15<br>
     * doJudge�̃e�X�g�B<br>
     * �ECall��ʂ̃��O�����݂��Ȃ��B<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_valnocall()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_valnocall.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-9-26<br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     * ���x�������������B<br>
     */
    public void testDoJudge_th1000000_multierror()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_multierror.jvn");

        rule.judge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000001);
        assertErrorOccurred(elementList.get(1), rule.threshold, (long)1000001);
    }

    /**
     * [����] 2-9-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        DiskInputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskInputRuleTest_testDoJudge_th1000000_val1000001.jvn");

        elementList.add(0, null);

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), rule.threshold, (long)1000001);
    }

}
