package jp.co.acroquest.endosnipe.perfdoctor.classifier;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.Classifier;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.SimpleClassifier;
import junit.framework.TestCase;

/**
 * SimpleClassifier�̃e�X�g�f�[�^
 * @author fujii
 *
 */
public class SimpleClassifierTest extends TestCase
{
    /**
     * [����] 1-1-1 convert�̃e�X�g�B <br />
     * �E�f�[�^�����WarningUnit�̃��X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * �����X�g�̃f�[�^�����̂܂ܕԂ��Ă��邱�ƁB
     * 
     */
    public void testClassify_OneData()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        warningUnitList.add(unit);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        ClassifierUtil.assertWarningUnitList(unit, resultList.get(0));
    }

    /**
     * [����] 1-1-2 convert�̃e�X�g�B <br />
     * �E10��WarningUnit�̗v�f����Ȃ郊�X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ���t�B���^�[���������āA���X�g���Ԃ��Ă��邱��(�����ł́A2�s�ځA6�s�ځA10�s��)�B
     * 
     */
    public void testClassify_TenData()
    {
        // ����
        Classifier classifier = createClassifier();
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
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(3, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit2, resultList.get(0));
        ClassifierUtil.assertWarningUnitList(unit6, resultList.get(1));
        ClassifierUtil.assertWarningUnitList(unit10, resultList.get(2));
    }

    /**
     * [����] 1-1-3 convert�̃e�X�g�B <br />
     * �Eargs�̒l��0���܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��1�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsZero()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{0, 0});
                
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit1, resultList.get(0));
    }

    /**
     * [����] 1-1-4 convert�̃e�X�g�B <br />
     * �Eargs�̒l�ɕ�������܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��1�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsString()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new String[]{"test", "test"});
                
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit1, resultList.get(0));
    }

    /**
     * [����] 1-1-26 convert�̃e�X�g�B <br />
     * �Eargs�̒l��-5���܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��1�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsMinus()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, -5});
                
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit1, resultList.get(0));
    }

    /**
     * [����] 1-1-27 convert�̃e�X�g�B <br />
     * �Eargs�̒�����1�ł���悤��WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��1�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_argsLengthOne()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5});
                
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);
        warningUnitList.add(unit5);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit1, resultList.get(0));
    }

    /**
     * [����] 1-1-30 convert�̃e�X�g�B <br />
     * �Eargs�̒l��0���܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��21�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsZero_AnotherOrder()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{0, 0});
                
        warningUnitList.add(unit5);
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit5, resultList.get(0));
    }

    /**
     * [����] 1-1-31 convert�̃e�X�g�B <br />
     * �Eargs�̒l�ɕ�������܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��22�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsString_AnotherOrder()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new String[]{"test", "test"});
                
        warningUnitList.add(unit5);
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit5, resultList.get(0));
    }
    
    
    /**
     * [����] 1-1-34 convert�̃e�X�g�B <br />
     * �Eargs�̒l��-5���܂�ł���WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��48�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_ContainsMinus_AnotherOrder()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, -5});
                
        warningUnitList.add(unit5);
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit5, resultList.get(0));
    }

    /**
     * [����] 1-1-35 convert�̃e�X�g�B <br />
     * �Eargs�̒�����1�ł���悤��WarningUnit�̗v�f�������X�g�ɑ΂��āA
     *  SimpleClassifier��K�p����B<br />
     * 
     * ��47�s�ڂ̃f�[�^���Ԃ��Ă���B
     * 
     */
    public void testClassify_argsLengthOne_AnotherOrder()
    {
        // ����
        Classifier classifier = createClassifier();
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 10});
        WarningUnit unit2 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 15});
        WarningUnit unit3 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 20});
        WarningUnit unit4 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5, 25});
        WarningUnit unit5 = ClassifierUtil.createDefaultWarningUnit(new Integer[]{5});
                
        warningUnitList.add(unit5);
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);
        warningUnitList.add(unit3);
        warningUnitList.add(unit4);

        // ���s
        List<WarningUnit> resultList = classifier.classify(warningUnitList);
        
        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(unit5, resultList.get(0));
    }

    
    /**
     * SimpleClassifier���쐬����B
     * @return SimpleClassifier
     */
    public Classifier createClassifier()
    {
        return new SimpleClassifier();
    }
}
