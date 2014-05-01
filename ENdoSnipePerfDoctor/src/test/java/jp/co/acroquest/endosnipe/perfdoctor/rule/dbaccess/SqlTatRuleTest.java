package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRuleTestCase;

/**
 * {@link SqlTatRule} �̂��߂̃e�X�g�N���X�B
 * 
 * @author y-komori
 */
public class SqlTatRuleTest extends PerformanceRuleTestCase
{
    /**
     * {@link SqlTatRule} �𐶐�����B
     * 
     * @param threshold 臒l
     * @return {@link SqlTatRule} �I�u�W�F�N�g
     */
    private SqlTatRule createRule(long threshold)
    {
        SqlTatRule rule = createInstance(SqlTatRule.class);
        rule.id = "DBA.MTRC.SQL_TAT";
        rule.active = true;
        rule.level = "WARN";
        rule.threshold = threshold;
        return rule;
    }

    /**
     * [����] 3-1-1<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��499�B<br>
     * �E臒l��500�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_01()
    {
        SqlTatRule rule = createRule(500);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_499.jvn");

        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-2<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��500�B<br>
     * �E臒l��500�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_02()
    {
        SqlTatRule rule = createRule(500);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_500.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 500L, 500L);
    }

    /**
     * [����] 3-1-3<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��501�B<br>
     * �E臒l��500�B<br>
     * ���x������������B<br>
     */
    public void testDoJudge_03()
    {
        SqlTatRule rule = createRule(500);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_501.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 500L, 501L);
    }

    /**
     * [����] 3-1-5<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT��10�B<br>
     * �E臒l��10�B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_05()
    {
        SqlTatRule rule = createRule(10);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_10.jvn");

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 10L, 10L);
    }

    /**
     * [����] 3-1-10<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l�����l�ł͂Ȃ��B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_10()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_invalid.jvn");

        SqlTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-11<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ETAT�̒l���󔒁B<br>
     * ���x�����������Ȃ��B<br>
     */
    public void testDoJudge_11()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_empty.jvn");

        SqlTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-13<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E[TIME]�Ƃ����^�O���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_13()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_no_time.jvn");

        SqlTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-14<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �EdetailInfo���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_14()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_no_detailInfo.jvn");

        SqlTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-15<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �ECALL���Ȃ��B<br>
     * ���x����\�����Ȃ��B<br>
     */
    public void testDoJudge_15()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_no_call.jvn");

        SqlTatRule rule = createRule(5);
        rule.doJudge(elementList);

        List<JavelinLogElement> errorList = getErrorJavelinLogElements();
        int errorListSize = errorList.size();
        if (errorListSize != 0)
        {
            fail("�������Ȃ��͂��̃G���[�������B");
        }
    }

    /**
     * [����] 3-1-26<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E������JavelinLogElement�Ōx�����o��B<br>
     */
    public void testDoJudge_26()
    {
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_multi.jvn");

        SqlTatRule rule = createRule(500);
        rule.doJudge(elementList);

        assertEquals(2, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(0), 500L, 501L);
        assertErrorOccurred(elementList.get(1), 500L, 501L);
    }

    /**
     * [����] 3-1-27<br>
     * <br>
     * doJudge�̃e�X�g�B<br>
     * �E����JavelinLogElement�Ŏ��s����O������<br>
     * ������JavelinLogElement�̓X�L�b�v���ď�������B<br>
     */
    public void testDoJudge_RuntimeException()
    {
        SqlTatRule rule = createRule(500);
        List<JavelinLogElement> elementList =
                createJavelinLogElement("SqlTatRuleTest_testDoJudge_501.jvn");

        elementList.add(0, null);

        rule.doJudge(elementList);

        assertEquals(1, getErrorJavelinLogElements().size());
        assertErrorOccurred(elementList.get(1), 500L, 501L);

    }

}
