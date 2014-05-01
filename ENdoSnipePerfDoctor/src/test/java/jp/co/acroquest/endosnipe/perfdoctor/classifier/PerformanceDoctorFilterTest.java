package jp.co.acroquest.endosnipe.perfdoctor.classifier;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnitGetter;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.PerformanceDoctorFilter;
import junit.framework.TestCase;

/**
 * PerformanceDoctorFilter�̃e�X�g�N���X
 * @author fujii
 *
 */
public class PerformanceDoctorFilterTest extends TestCase
{
    /**
     * [����] 1-1-13 doFilter�̃e�X�g�B <br />
     * �E�f�[�^�����WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * �����X�g�̃f�[�^�����̂܂ܕԂ��Ă��邱�ƁB
     * 
     */
    public void testDoFilter_ListSizeOne()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        warningUnitList.add(unit);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));
    }

    /**
     * [����] 1-1-14 doFilter�̃e�X�g�B <br />
     * �E�f�[�^��10��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A2�s�ځA6�s�ځA10�s��)�B
     * 
     */
    public void testDoFilter_ListSizeTen()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(3, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(1), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(5), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(9), resultList.get(2));
    }

    /**
     * [����] 1-1-15 doFilter�̃e�X�g�B <br />
     * �E�f�[�^��20��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A4�s�ځA8�s�ځA12�s�ځA16�s�ځA20�s��)�B
     * 
     */
    public void testDoFilter_ListSizeTwelve()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{0, 0});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});
        WarningUnit unit11 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 60});
        WarningUnit unit12 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 65});
        WarningUnit unit13 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 70});
        WarningUnit unit14 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 75});
        WarningUnit unit15 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 80});
        WarningUnit unit16 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 85});
        WarningUnit unit17 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 90});
        WarningUnit unit18 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 95});
        WarningUnit unit19 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 100});
        WarningUnit unit20 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 105});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);
        warningUnitList.add(unit11);
        warningUnitList.add(unit12);
        warningUnitList.add(unit13);
        warningUnitList.add(unit14);
        warningUnitList.add(unit15);
        warningUnitList.add(unit16);
        warningUnitList.add(unit17);
        warningUnitList.add(unit18);
        warningUnitList.add(unit19);
        warningUnitList.add(unit20);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(5, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(7), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(11), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(15), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(19), resultList.get(4));
    }

    /**
     * [����] 1-1-16 doFilter�̃e�X�g�B <br />
     * �E���ꂼ��̗v�f��10�ȉ���2��ނ̃��[��ID���܂܂ꂽWarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A2�s�ځA5�s�ځA24�s�ځA27�s��)�B
     * 
     */
    public void testDoFilter_TwoSimpleClassifier()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});

        ClassifierUtil.setID("testRuleId2");
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(4, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(1), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(6), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(9), resultList.get(3));
    }

    /**
     * [����] 1-1-17 doFilter�̃e�X�g�B <br />
     * �E�v�f����10�ȉ��̃��[��ID��11�ȏ�̃��[��ID���܂܂ꂽWarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A2�s�ځA5�s�ځA26�s�ځA30�s�ځA34�s�ځA38�s�ځA42�s��)�B
     * 
     */
    public void testDoFilter_OneSimpleClassifierOneKmeanClassifier()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        ClassifierUtil.setID("testRuleId");
        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});

        ClassifierUtil.setID("testRuleId2");
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit11 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit12 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit13 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit14 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit15 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});
        WarningUnit unit16 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 60});
        WarningUnit unit17 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 65});
        WarningUnit unit18 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 70});
        WarningUnit unit19 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 75});
        WarningUnit unit20 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 80});
        WarningUnit unit21 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 85});
        WarningUnit unit22 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 90});
        WarningUnit unit23 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 95});
        WarningUnit unit24 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 100});
        WarningUnit unit25 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 105});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);
        warningUnitList.add(unit11);
        warningUnitList.add(unit12);
        warningUnitList.add(unit13);
        warningUnitList.add(unit14);
        warningUnitList.add(unit15);
        warningUnitList.add(unit16);
        warningUnitList.add(unit17);
        warningUnitList.add(unit18);
        warningUnitList.add(unit19);
        warningUnitList.add(unit20);
        warningUnitList.add(unit21);
        warningUnitList.add(unit22);
        warningUnitList.add(unit23);
        warningUnitList.add(unit24);
        warningUnitList.add(unit25);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(7, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(1), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(8), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(12), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(16), resultList.get(4));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(20), resultList.get(5));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(24), resultList.get(6));
    }

    /**
     * [����] 1-1-18 doFilter�̃e�X�g�B <br />
     * �E�v�f����11�ȏ�̃��[��ID��10�ȉ��̃��[��ID���܂܂ꂽWarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A4�s�ځA8�s�ځA12�s�ځA16�s�ځA20�s�ځA24�s�ځA27�s��)�B
     * 
     */
    public void testDoFilter_OneKmeanClassifierOneSimpleClassifier()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        ClassifierUtil.setID("testRuleId");
        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});
        WarningUnit unit11 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 60});
        WarningUnit unit12 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 65});
        WarningUnit unit13 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 70});
        WarningUnit unit14 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 75});
        WarningUnit unit15 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 80});
        WarningUnit unit16 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 85});
        WarningUnit unit17 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 90});
        WarningUnit unit18 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 95});
        WarningUnit unit19 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 100});
        WarningUnit unit20 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 105});

        ClassifierUtil.setID("testRuleId2");
        WarningUnit unit21 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit22 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit23 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit24 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit25 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);
        warningUnitList.add(unit11);
        warningUnitList.add(unit12);
        warningUnitList.add(unit13);
        warningUnitList.add(unit14);
        warningUnitList.add(unit15);
        warningUnitList.add(unit16);
        warningUnitList.add(unit17);
        warningUnitList.add(unit18);
        warningUnitList.add(unit19);
        warningUnitList.add(unit20);
        warningUnitList.add(unit21);
        warningUnitList.add(unit22);
        warningUnitList.add(unit23);
        warningUnitList.add(unit24);
        warningUnitList.add(unit25);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(7, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(7), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(11), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(15), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(19), resultList.get(4));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(21), resultList.get(5));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(24), resultList.get(6));
    }

    /**
     * [����] 1-1-18 doFilter�̃e�X�g�B <br />
     * �E�v�f����11�ȏ�̃��[��ID��10�ȉ��̃��[��ID���܂܂ꂽWarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A4�A8�A12�A16�A20�A26�A30�A34,38�A42�s��)�B
     * 
     */
    public void testDoFilter_TwoKmeanClassifier()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        ClassifierUtil.setID("testRuleId");
        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit6 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit7 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit8 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit9 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit10 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});
        WarningUnit unit11 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 60});
        WarningUnit unit12 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 65});
        WarningUnit unit13 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 70});
        WarningUnit unit14 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 75});
        WarningUnit unit15 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 80});
        WarningUnit unit16 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 85});
        WarningUnit unit17 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 90});
        WarningUnit unit18 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 95});
        WarningUnit unit19 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 100});
        WarningUnit unit20 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 105});

        ClassifierUtil.setID("testRuleId2");
        WarningUnit unit21 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit22 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit23 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit24 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit25 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 30});
        WarningUnit unit26 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 35});
        WarningUnit unit27 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 40});
        WarningUnit unit28 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 45});
        WarningUnit unit29 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 50});
        WarningUnit unit30 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 55});
        WarningUnit unit31 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 60});
        WarningUnit unit32 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 65});
        WarningUnit unit33 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 70});
        WarningUnit unit34 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 75});
        WarningUnit unit35 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 80});
        WarningUnit unit36 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 85});
        WarningUnit unit37 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 90});
        WarningUnit unit38 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 95});
        WarningUnit unit39 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 100});
        WarningUnit unit40 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 105});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);
        warningUnitList.add(unit11);
        warningUnitList.add(unit12);
        warningUnitList.add(unit13);
        warningUnitList.add(unit14);
        warningUnitList.add(unit15);
        warningUnitList.add(unit16);
        warningUnitList.add(unit17);
        warningUnitList.add(unit18);
        warningUnitList.add(unit19);
        warningUnitList.add(unit20);
        warningUnitList.add(unit21);
        warningUnitList.add(unit22);
        warningUnitList.add(unit23);
        warningUnitList.add(unit24);
        warningUnitList.add(unit25);
        warningUnitList.add(unit26);
        warningUnitList.add(unit27);
        warningUnitList.add(unit28);
        warningUnitList.add(unit29);
        warningUnitList.add(unit30);
        warningUnitList.add(unit31);
        warningUnitList.add(unit32);
        warningUnitList.add(unit33);
        warningUnitList.add(unit34);
        warningUnitList.add(unit35);
        warningUnitList.add(unit36);
        warningUnitList.add(unit37);
        warningUnitList.add(unit38);
        warningUnitList.add(unit39);
        warningUnitList.add(unit40);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(10, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(7), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(11), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(15), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(19), resultList.get(4));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(23), resultList.get(5));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(27), resultList.get(6));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(31), resultList.get(7));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(35), resultList.get(8));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(39), resultList.get(9));
    }

    /**
     * [����] 1-1-20 doFilter�̃e�X�g�B <br />
     * �E���[��ID�A�N���X���A���\�b�h���A�d�v�x�̈قȂ�WarinigUnit�����X�g�Ɏ��Ƃ��ɁA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A1�A23�A43�A44�A45�A46�s��)�B
     * 
     */
    public void testDoFilter_DifferenceRule()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String ruleIdDef = "testRuleId";
        String idDef = "testWarningId";
        String descriptionDef = "This is a testWarningUnit";
        String classNameDef = "testClass";
        String methodNameDef = "testMethod";
        String levelDef = "ERROR";
        String fileNameDef = "file";
        WarningUnit unit1 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, idDef, descriptionDef, classNameDef,
                                                    methodNameDef, levelDef, fileNameDef, 1, 0, 0,
                                                    new Integer[]{5, 10});
        WarningUnit unit2 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, "testRuleId2", descriptionDef,
                                                    classNameDef, methodNameDef, levelDef,
                                                    fileNameDef, 1, 0, 0, new Integer[]{5, 10});
        WarningUnit unit3 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, idDef, descriptionDef, "testClass2",
                                                    methodNameDef, levelDef, fileNameDef, 1, 0, 0,
                                                    new Integer[]{5, 10});
        WarningUnit unit4 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, idDef, descriptionDef, classNameDef,
                                                    "testMethod2", levelDef, fileNameDef, 1, 0, 0,
                                                    new Integer[]{5, 10});
        WarningUnit unit5 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, idDef, descriptionDef, classNameDef,
                                                    methodNameDef, "WARN", fileNameDef, 1, 0, 0,
                                                    new Integer[]{5, 10});
        WarningUnit unit6 =
                WarningUnitGetter.createWarningUnit(ruleIdDef, idDef, descriptionDef, classNameDef,
                                                    methodNameDef, "INFO", fileNameDef, 1, 0, 0,
                                                    new Integer[]{5, 10});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(6, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(1), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(2), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(4));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(5), resultList.get(5));
    }

    /**
     * [����] <br />
     * �E�f�[�^����̃C�x���g�p��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     * 
     * �����X�g�̃f�[�^�����̂܂ܕԂ��Ă��邱�ƁB
     * 
     */
    public void testDoFilterEventListSizeOne()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String stackTrace = "aaa";

        WarningUnit unit =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 10});
        warningUnitList.add(unit);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));
    }

    /**
     * [����]  <br />
     * �E�f�[�^��5�̃C�x���g�p��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     *  WarningUnit�ɕۑ�����X�^�b�N�g���[�X�͂��ׂē����ł���Ƃ���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A5�s��)�B
     * 
     */
    public void testDoFilterEventSameStackTrace()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String stackTrace = "test1";

        WarningUnit unit1 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 10});
        WarningUnit unit2 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 11});
        WarningUnit unit3 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 12});
        WarningUnit unit4 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 13});
        WarningUnit unit5 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 14});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(0));
    }

    /**
     * [����]  <br />
     * �E�f�[�^��5�̃C�x���g�p��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     *  WarningUnit�ɕۑ�����X�^�b�N�g���[�X�͂��ׂĈقȂ�B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(1�s�ځA2�s�ځA3�s�ځA4�s�ځA5�s��)�B
     * 
     */
    public void testDoFilterEventDiffStackTrace()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String stackTrace1 = "test1";
        String stackTrace2 = "test2";
        String stackTrace3 = "test3";
        String stackTrace4 = "test4";
        String stackTrace5 = "test5";

        WarningUnit unit1 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace1, new Integer[]{5, 10});
        WarningUnit unit2 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace2, new Integer[]{5, 11});
        WarningUnit unit3 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace3, new Integer[]{5, 12});
        WarningUnit unit4 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace4, new Integer[]{5, 13});
        WarningUnit unit5 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace5, new Integer[]{5, 14});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(5, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(1), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(2), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(4));
    }

    /**
     * [����]  <br />
     * �E�f�[�^��5�̃C�x���g�p��WarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     *  WarningUnit�ɕۑ�����X�^�b�N�g���[�X���������̂ƈقȂ���̂��������Ă���B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A3�s�ځA4�s�ځA2�s��)�B
     * 
     */
    public void testDoFilterEventSomeStackTrace()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String stackTrace1 = "test1";
        String stackTrace2 = "test2";
        String stackTrace3 = "test3";

        WarningUnit unit1 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace1, new Integer[]{5, 10});
        WarningUnit unit2 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace2, new Integer[]{5, 11});
        WarningUnit unit3 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace3, new Integer[]{5, 12});
        WarningUnit unit4 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace1, new Integer[]{5, 13});
        WarningUnit unit5 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace2, new Integer[]{5, 14});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(3, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(4), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(2), resultList.get(2));
    }

    /**
     * [����]  <br />
     * �E�v�f����11�ȏ�̃��[��ID���܂܂ꂽWarningUnit�̃��X�g�ɑ΂��āA
     *  PerformanceDoctorFilter#doFilter���Ăяo���B<br />
     *  WarningUnit�ɕۑ�����X�^�b�N�g���[�X�͂��ׂē����ł���Ƃ���B
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A4�s�ځA8�s�ځA12�s�ځA16�s�ځA20�s��)�B
     * 
     */
    public void testDoFilterEventOneKmeanClassifier()
    {
        // ����
        PerformanceDoctorFilter filter = createFilter();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        String stackTrace = "test";

        ClassifierUtil.setID("testRuleId");
        WarningUnit unit1 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 10});
        WarningUnit unit2 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 15});
        WarningUnit unit3 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 20});
        WarningUnit unit4 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 25});
        WarningUnit unit5 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 30});
        WarningUnit unit6 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 35});
        WarningUnit unit7 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 40});
        WarningUnit unit8 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 45});
        WarningUnit unit9 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 50});
        WarningUnit unit10 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 55});
        WarningUnit unit11 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 60});
        WarningUnit unit12 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 65});
        WarningUnit unit13 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 70});
        WarningUnit unit14 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 75});
        WarningUnit unit15 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 80});
        WarningUnit unit16 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 85});
        WarningUnit unit17 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 90});
        WarningUnit unit18 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 95});
        WarningUnit unit19 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 100});
        WarningUnit unit20 =
                ClassifierUtil.createDefaultEventWarningUnit(stackTrace, new Integer[]{5, 105});

        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);
        warningUnitList.add(unit6);
        warningUnitList.add(unit7);
        warningUnitList.add(unit8);
        warningUnitList.add(unit9);
        warningUnitList.add(unit10);
        warningUnitList.add(unit11);
        warningUnitList.add(unit12);
        warningUnitList.add(unit13);
        warningUnitList.add(unit14);
        warningUnitList.add(unit15);
        warningUnitList.add(unit16);
        warningUnitList.add(unit17);
        warningUnitList.add(unit18);
        warningUnitList.add(unit19);
        warningUnitList.add(unit20);

        // ���s
        List<WarningUnit> resultList = filter.doFilter(warningUnitList);

        // ����
        assertEquals(5, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(3), resultList.get(0));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(7), resultList.get(1));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(11), resultList.get(2));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(15), resultList.get(3));
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(19), resultList.get(4));
    }

    /**
     * PerformaceDoctroFilter���쐬����B
     * @return PerformanceDoctorFilter
     */
    public PerformanceDoctorFilter createFilter()
    {
        return new PerformanceDoctorFilter();
    }
}
