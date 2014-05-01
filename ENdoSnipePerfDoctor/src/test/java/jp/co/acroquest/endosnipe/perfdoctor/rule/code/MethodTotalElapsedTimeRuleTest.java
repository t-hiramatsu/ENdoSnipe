package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * MethodTotalElapsedTimeRule�̃e�X�g�N���X�B
 * @author tooru
 *
 */
public class MethodTotalElapsedTimeRuleTest extends PerformanceRuleTestCase
{
    /**
     * 臒l���w�肵��MethodTotalElapsedTimeRuleTest�𐶐�����B<br>
     * @param threshold 臒l
     * @return MethodTotalElapsedTimeRuleTest
     */
    private MethodTotalElapsedTimeRule createRule(int threshold)
    {
        MethodTotalElapsedTimeRule rule = createInstance(MethodTotalElapsedTimeRule.class);
        rule.id = "COD.MTRC.METHOD_TOTAL_ELAPSEDTIME";
        rule.active = true;
        rule.level = "WARN";
        rule.durationThreshold = 0;
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 2-1-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_th100000_call99999()
    {
        MethodTotalElapsedTimeRule rule = createRule(100000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_th100000_call99999.jvn");

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_th100000_call100000()
    {
        MethodTotalElapsedTimeRule rule = createRule(100000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_th100000_call100000.jvn");

        JavelinParser.initDetailInfo(elementList);
        List<WarningUnit> list = rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, 100000.0);
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,000msec)",
                     list.get(0).getDescription());
    }

    /**
     * [����] 2-1-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_th100000_call100001()
    {
        MethodTotalElapsedTimeRule rule = createRule(100000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_th100000_call100001.jvn");

        JavelinParser.initDetailInfo(elementList);
        List<WarningUnit> list = rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, 100001.0);
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,001msec)",
                     list.get(0).getDescription());

    }

    /**
     * [����] 2-1-4<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_th5000_call5000()
    {
        MethodTotalElapsedTimeRule rule = createRule(5000);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_th5000_call5000.jvn");

        JavelinParser.initDetailInfo(elementList);
        List<WarningUnit> list = rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, 5000.0);
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:5,000msec�A���o�l:5,000msec)",
                     list.get(0).getDescription());

    }

    /**
     * [����] 2-1-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>

     */
    public void testDoJudge_4000_times_4()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_4000_times_4.jvn");
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        List<WarningUnit> list = rule.judge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(8), rule.threshold, 160000.0);
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:160,000msec)",
                     list.get(0).getDescription());

    }

    /**
     * [����] 2-1-6<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_empty()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_empty.jvn");
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-7<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_no_call()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_no_call.jvn");
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-8<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_methods_in_same_class()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_methods_in_same_class.jvn");
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-9<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_same_method_name()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_same_method_name.jvn");
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-10<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_different_thread()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread1.jvn");
        List<JavelinLogElement> elementList2 =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread2.jvn");
        elementList.addAll(elementList2);
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        rule.judge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        assertEquals(0, errorListSize);
    }

    /**
     * [����] 2-1-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     */
    public void testDoJudge_different_thread_multi()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread_multi1.jvn");
        List<JavelinLogElement> elementList2 =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread_multi2.jvn");
        List<JavelinLogElement> elementList3 =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread_multi3.jvn");
        List<JavelinLogElement> elementList4 =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread_multi4.jvn");
        List<JavelinLogElement> elementList5 =
                createJavelinLogElement("MethodTotalElapsedTimeRuleTest_testDoJudge_different_thread_multi5.jvn");
        elementList.addAll(elementList2);
        elementList.addAll(elementList3);
        elementList.addAll(elementList4);
        elementList.addAll(elementList5);
        MethodTotalElapsedTimeRule rule = createRule(100000);

        JavelinParser.initDetailInfo(elementList);
        List<WarningUnit> list = rule.judge(elementList);

        assertEquals(5, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), rule.threshold, 100001.0);
        assertErrorOccurred(elementList.get(4), rule.threshold, 100002.0);
        assertErrorOccurred(elementList.get(8), rule.threshold, 100003.0);
        assertErrorOccurred(elementList.get(12), rule.threshold, 100004.0);
        assertErrorOccurred(elementList.get(16), rule.threshold, 100005.0);
        sortWarningUnit(list);
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,001msec)",
                     list.get(0).getDescription());
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,002msec)",
                     list.get(1).getDescription());
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,003msec)",
                     list.get(2).getDescription());
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,004msec)",
                     list.get(3).getDescription());
        assertEquals("���\�b�h�̎��������Ԃ̍��v��臒l�𒴂��Ă��܂��B(臒l:100,000msec�A���o�l:100,005msec)",
                     list.get(4).getDescription());

    }
}
