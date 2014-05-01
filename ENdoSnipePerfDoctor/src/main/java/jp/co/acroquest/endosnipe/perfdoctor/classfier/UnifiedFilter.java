/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.perfdoctor.classfier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * �����̃��[�������ʉ�����t�B���^
 * @author fujii
 *
 */
public class UnifiedFilter
{
    /** �Z�p���[�^ */
    private static final String  SEPARATOR = ",";

    /** ���ꂷ�郋�[���̃��X�g */
    private final List<String[]> unifiedRulesList_;

    /**
     * �R���X�g���N�^<br />
     * 
     */
    public UnifiedFilter()
    {
        this.unifiedRulesList_ = new ArrayList<String[]>();
        this.unifiedRulesList_.add(new String[]{"COD.MTRC.METHOD_ELAPSEDTIME",
                "COD.MTRC.METHOD_TAT"});
        this.unifiedRulesList_.add(new String[]{"COD.MTRC.METHOD_PURE_CPU", "COD.MTRC.METHOD_CPU"});
        this.unifiedRulesList_.add(new String[]{"COD.MTRC.METHOD_PURE_WAIT", "COD.THRD.WAIT_TIME"});
        this.unifiedRulesList_.add(new String[]{"COD.THRD.BLK_TIME", "COD.THRD.BLK_TIME"});
    }

    /**
     * �t�B���^�[��������B
     * @param warningUnitList WarningUnit�̃��X�g
     * @return �t�B���^�[��̌���
     */
    public List<WarningUnit> doFilter(final List<WarningUnit> warningUnitList)
    {
        List<WarningUnit> deleteList = new ArrayList<WarningUnit>();
        List<WarningUnit> copyList = new ArrayList<WarningUnit>(warningUnitList);

        Map<String, List<WarningUnit>> warningMap = new LinkedHashMap<String, List<WarningUnit>>();

        // ���[��ID�A�N���X���A���\�b�h���A�d�v�x���L�[��Map���쐬����B
        for (WarningUnit warningUnit : warningUnitList)
        {
            String[] idArray = searchUnifiedId(warningUnit.getId(), this.unifiedRulesList_);
            if (idArray != null)
            {
                createWarningMap(warningUnit, idArray, warningMap);
            }
        }

        // Map�ɓ��͂��ꂽWarningUnit�̃��X�g���o�͂���B
        Collection<List<WarningUnit>> col = warningMap.values();
        for (List<WarningUnit> list : col)
        {
            // �D��x�̍������[����ID�ɂ��x�������ׂă��X�g�ɓ���A
            // ���̂Ƃ��̎�����Map�ɓo�^����B
            List<WarningUnit> deleteTargetList = createDeleteList(list);

            // ���ނ������ʂ���������B
            joinList(deleteList, deleteTargetList);
        }
        copyList.removeAll(deleteList);

        return copyList;
    }

    /**
     * �w�肵��WarningUnit��ID�����ꂷ��WarningUnit��ID�̃��X�g�ƈ�v���邩���肷��B
     * @param id WarningUnit��ID
     * @param unifiedList ���ꂷ��WarningUnit�̃��X�g
     * @return �����Ŏw�肵��ID���܂܂�Ă��铝�ꂷ��ID�̑g(������Ȃ��ꍇ��null��Ԃ�)
     */
    private String[] searchUnifiedId(final String id, final List<String[]> unifiedList)
    {
        for (Iterator<String[]> iterator = unifiedList.iterator(); iterator.hasNext();)
        {
            String[] idArray = iterator.next();
            for (String unifiedId : idArray)
            {
                if (id.equals(unifiedId))
                {
                    return idArray;
                }
            }
        }
        return null;
    }

    /**
     * �w�肵��WarningUnit��ID���D�惋�[����ID�ł��邩�𔻒肷��B
     * @param id WarningUnit��ID
     * @param unifiedList ���ꂷ�邽�߂�WarningUnit�̃��X�g
     * @return �����Ŏw�肵��ID���D�惋�[���ł���A�������[���ɑ΂���t�B���^�����łȂ��B
     */
    private boolean containsSupperiorId(final String id, final List<String[]> unifiedList)
    {
        for (Iterator<String[]> iterator = unifiedList.iterator(); iterator.hasNext();)
        {
            String[] idArray = iterator.next();
            boolean isSupperiorRule = id.equals(idArray[0]);
            boolean isSameRule = idArray[0].equals(idArray[1]);
            if (isSupperiorRule == true && isSameRule == false)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * �D��x�Ɋ�Â��A�x�����X�g����폜���郊�X�g���쐬����B<br>
     * �ȉ��̏��Ɉ����ŗ^�������X�g���������ƂŁA�폜�Ώۂ̃��X�g���쐬����B<br>
     * <ol>
     * <li>�D��x�̍������[���ɑ΂���x��</li>
     * <li>�K�w�̐[���x��</li>
     * </ol>
     * �Ȃ��A�D��x�̒Ⴂ���[���ɑ΂���x���́A���łɓo�^�����x���̎��ԂƏd������������ꍇ�́A
     * �폜�Ώۂ̃��X�g�ɉ�����B<br />
     * <br />
     * @param list �폜�O�̌x���̃��X�g
     * @return �폜�Ώۂ̌x���̃��X�g
     * 
     */
    private List<WarningUnit> createDeleteList(final List<WarningUnit> list)
    {
        List<WarningUnit> inferriorList = new ArrayList<WarningUnit>();
        List<long[]> timeList = new ArrayList<long[]>();

        // �D��x�̍������[��ID�ɑΉ�����x���̃��X�g�ƗD��x�̒Ⴂ���[��ID�ɑΉ�����x���̃��X�g�ɕ��ނ���B
        for (WarningUnit warningUnit : list)
        {
            String ruleId = warningUnit.getId();
            boolean isContains = containsSupperiorId(ruleId, this.unifiedRulesList_);
            if (isContains)
            {
                long[] time = {warningUnit.getStartTime(), warningUnit.getEndTime()};
                timeList.add(time);
            }
            else
            {
                inferriorList.add(warningUnit);
            }
        }

        // �D��x�̒Ⴂ���[��ID�ɑΉ�����x���̃��X�g����̏ꍇ�A��̂܂ܕԂ��B
        if (inferriorList.size() == 0)
        {
            return inferriorList;
        }

        // ���o�l�����L�[�ɕ��т�����B
        Collections.sort(inferriorList, new FileLineComparator());

        List<WarningUnit> deleteList = new ArrayList<WarningUnit>();

        // �D��x�̒Ⴂ���X�g���x�����X�g�ɉ����邩���肷��B
        for (WarningUnit warningUnit : inferriorList)
        {
            if (isTimeContains(warningUnit, timeList))
            {
                deleteList.add(warningUnit);
            }
            else
            {
                long[] time = {warningUnit.getStartTime(), warningUnit.getEndTime()};
                timeList.add(time);
            }
        }
        return deleteList;
    }

    /**
     * �w�肵���x���̎��Ԃ��L�����ǂ����B<br />
     * ���łɓo�^�����x���̎��ԂƏd�Ȃ邩�Ŕ��f����B<br />
     * @param warningUnit �x��
     * @param timeList ���łɓo�^�������Ԃ̃��X�g
     * @return true:���łɓo�^�����x���̎��ԂƏd�Ȃ�Afalse:���Ԃ��d�Ȃ�Ȃ��B
     */
    private boolean isTimeContains(final WarningUnit warningUnit, final List<long[]> timeList)
    {
        long startTime = warningUnit.getStartTime();
        long endTime = warningUnit.getEndTime();

        for (long[] time : timeList)
        {
            // �J�n���Ԃ��I�����Ԃ����܂œo�^�����x���̎��ԓ��Ɋ܂܂�邩�ǂ����Ŕ��f����B
            if ((time[0] <= endTime && startTime <= time[1]))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * WarningUnit�̃��X�g����A���ʂ̂��߂�Map���쐬����B<br />
     * ���[��ID�A���O�t�@�C�����A�d�v�x���L�[��Map���쐬����B<br />
     * @param warningUnitList ���ʏ���ۑ�����Ώۂ�warningUnit�̃��X�g 
     * @param idArray 2�̃��[���̌��ʂ𓝈ꂷ�邽�߂̃��X�g
     * @param warningMap �x�����i�[���邽�߂�Map
     */
    private void createWarningMap(final WarningUnit warningUnit, final String[] idArray,
            final Map<String, List<WarningUnit>> warningMap)
    {
        List<WarningUnit> unitList;
        // �S�Ă�warningUnit�����o���A���O�t�@�C�����A�d�v�x���L�[��Map���쐬����B
        String key =
                     idArray[0] + SEPARATOR + warningUnit.getLogFileName() + SEPARATOR
                             + warningUnit.getLevel();
        unitList = warningMap.get(key);
        // Map�Ɏw�肵���L�[�����݂��Ȃ��Ƃ��A�V�������X�g���쐬����B
        // �L�[�����݂���ꍇ�́A�L�[�ɑΉ����郊�X�g�ɁAwarningUnit��ǉ�����B
        if (unitList == null)
        {
            unitList = new ArrayList<WarningUnit>();
            warningMap.put(key, unitList);
        }
        unitList.add(warningUnit);
    }

    /**
     * 2�̃��X�g����������B
     * <code>oldList</code> �� <code>null</code> �̏ꍇ�͉������Ȃ��B
     *
     * @param oldList ������̃��X�g
     * @param unitList �V�K�Œǉ����郊�X�g
     */
    private void joinList(final List<WarningUnit> oldList, final List<WarningUnit> unitList)
    {
        if (oldList != null && unitList != null)
        {
            oldList.addAll(unitList);
        }
    }
}
