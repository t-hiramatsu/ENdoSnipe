package jp.co.acroquest.endosnipe.perfdoctor.classifier;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.UnifiedFilter;
import junit.framework.TestCase;

/**
 * UnifiedFilter�̃e�X�g�P�[�X
 * @author fujii
 *
 */
public class UnifiedFilterTest extends TestCase
{
    /** TAT�̌x�����o�����[��ID */
    private static final String RULEID_TAT     = "COD.MTRC.METHOD_TAT";

    /** ���������Ԃ̌x�����o�����[��ID */
    private static final String RULEID_ELAPSED = "COD.MTRC.METHOD_ELAPSEDTIME";

    /** �����̌x�����[�����i��t�B���^ */
    private UnifiedFilter       filter_        = new UnifiedFilter();

    /**
     * [����] <br>
     * <br>
     * doFilter�̃e�X�g<br>
     * �E���[��ID�F{"COD.MTRC.METHOD_ELAPSEDTIME","COD.MTRC.METHOD_TAT"}<br>
     * �E�x�����X�g���P�쐬����<br>
     * ���쐬�����x�����X�g�����̂܂ܕԂ�B<br>
     */
    public void testDoFilterTatOneData()
    {
        // ����
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit =
                ClassifierUtil.createWarningUnit(RULEID_ELAPSED, "testFile", 0, 100, new Integer[]{
                        5, 10});
        warningUnitList.add(unit);

        // ���s
        List<WarningUnit> resultList = this.filter_.doFilter(warningUnitList);

        // ����
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));

    }

    /**
     * [����] <br>
     * <br>
     * doFilter�̃e�X�g<br>
     * �E���[��ID�F{"COD.MTRC.METHOD_ELAPSEDTIME","COD.MTRC.METHOD_TAT"}<br>
     * �ETAT�̊J�n���Ԃ������Ԃ͈̔͂Ɋ܂܂�Ă���<br>
     * ���x���̃��X�g�̃T�C�Y��1�ł��邱�ƁB
     * ��ELAPSEDTIME�̌x�����[���̂ݕ\�������B<br>
     */
    public void testDoFilterTatStartIn()
    {
        // ����
        List<WarningUnit> warningUnitList = new ArrayList<WarningUnit>();

        WarningUnit unit1 =
                ClassifierUtil.createWarningUnit(RULEID_ELAPSED, "testFile", 10, 30, new Integer[]{
                        5, 10});
        WarningUnit unit2 =
                ClassifierUtil.createWarningUnit(RULEID_TAT, "testFile", 20, 70, new Integer[]{5,
                        10});
        warningUnitList.add(unit1);
        warningUnitList.add(unit2);

        // ���s
        List<WarningUnit> resultList = this.filter_.doFilter(warningUnitList);

        // ����
        assertEquals(1, resultList.size());
        ClassifierUtil.assertWarningUnitList(warningUnitList.get(0), resultList.get(0));
    }

    /**
     * [����] <br>
     * <br>
     * doFilter�̃e�X�g<br>
     * �E���[��ID�F{"COD.MTRC.METHOD_ELAPSEDTIME","COD.MTRC.METHOD_TAT"}<br>
     * �ETAT�̏I�����Ԃ������Ԃ͈̔͂Ɋ܂܂�Ă���<br>
     * ���x���̃��X�g�̃T�C�Y��1�ł��邱�ƁB
     * ��ELAPSEDTIME�̌x�����[���̂ݕ\�������B<br>
     */

    /**
     * [����] <br>
     * <br>
     * doFilter�̃e�X�g<br>
     * �E���[��ID�F{"COD.MTRC.METHOD_ELAPSEDTIME","COD.MTRC.METHOD_TAT"}<br>
     * �ETAT�̏I�����Ԃ������Ԃ͈̔͂Ɋ܂܂�Ă��Ȃ�<br>
     * ��ELAPSEDTIME�ATAT�̌x�����[���̗������\�������B<br>
     */

}
