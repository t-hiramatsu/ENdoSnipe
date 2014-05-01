package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * �f�B�X�N�o�͗ʔ��胋�[���p�̃e�X�g�P�[�X<br>
 * <br>
 * @author S.Kimura
 */
public class DiskOutputRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��DiskOutputRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return DiskOutputRule
     */
    private DiskOutputRule createRule(long threshold)
    {
        DiskOutputRule rule = createInstance(DiskOutputRule.class);
        rule.id = "COD.IO.DISK_OUTPUT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-10-1<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�999999<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_val999999()
    {
        DiskOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskOutputRuleTest_testDoJudge_th1000000_val999999.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-10-2<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�1000000<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000000()
    {
        DiskOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskOutputRuleTest_testDoJudge_th1000000_val1000000.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000000);
    }

    /**
     * [����] 2-10-3<br>
     * doJudge�̃e�X�g�B<br>
     * �E�f�B�X�N���͗ʂ�1000001<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000001()
    {
        DiskOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("DiskOutputRuleTest_testDoJudge_th1000000_val1000001.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000001);
    }
}
