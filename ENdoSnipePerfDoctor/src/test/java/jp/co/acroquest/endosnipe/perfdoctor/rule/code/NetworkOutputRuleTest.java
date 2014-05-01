package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * �l�b�g���[�N�o�͗ʔ��胋�[���p�̃e�X�g�P�[�X<br>
 * <br>
 * @author S.Kimura
 */
public class NetworkOutputRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��NetworkOutputRule�𐶐�����B<br>
     * @param threshold 臒l
     * @return NetworkOutputRule
     */
    private NetworkOutputRule createRule(long threshold)
    {
        NetworkOutputRule rule = createInstance(NetworkOutputRule.class);
        rule.id = "COD.IO.NETWORK_OUTPUT";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * �������͑S��AbstractSingleValueLimit�N���X�ɏ����Ă��邽�߁A<br>
     * �ڍׂȌ��؂͓�����AbstractSingleValueLimit�̎����N���X�ł���A<br>
     * DiskOutputRuleTest�ɂčs���B<br>
     * �{�e�X�g�P�[�X�ɂ����ẮA�G���[���o�͂���邱�Ƃ݂̂��m�F����B<br>
     */

    /**
     * [����] 2-11-1<br>
     * doJudge�̃e�X�g�B<br>
     * �E�l�b�g���[�N���͗ʂ�9999999<br>
     * �E臒l��1000000<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_th1000000_val999999()
    {
        NetworkOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NetworkOutputRuleTest_testDoJudge_th1000000_val999999.jvn");

        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 2-11-2<br>
     * doJudge�̃e�X�g�B<br>
     * �E�l�b�g���[�N���͗ʂ�1000000<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000000()
    {
        NetworkOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NetworkOutputRuleTest_testDoJudge_th1000000_val1000000.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000000);
    }

    /**
     * [����] 2-11-3<br>
     * doJudge�̃e�X�g�B<br>
     * �E�l�b�g���[�N���͗ʂ�1000001<br>
     * �E臒l��1000000<br>
     * ���x������������B<br>
     */
    public void testDoJudge_th1000000_val1000001()
    {
        NetworkOutputRule rule = createRule(1000000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("NetworkOutputRuleTest_testDoJudge_th1000000_val1000001.jvn");

        rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, (long)1000001);
    }

}
