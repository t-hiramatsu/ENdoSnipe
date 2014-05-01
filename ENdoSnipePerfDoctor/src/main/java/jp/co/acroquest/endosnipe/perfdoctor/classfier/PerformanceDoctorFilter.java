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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * �x�����X�g�̒�����A���[��ID,�N���X���A���\�b�h���A�d�v�x�������x���ɑ΂��āA�t�B���^�[��������B
 * @author fujii
 *
 */
public class PerformanceDoctorFilter
{
    /** �Z�p���[�^ */
    private static final String SEPARATOR          = ",";

    /** �x�����b�Z�[�W��args���ɂ���A���o�l��index */
    public static final int     TARGET_VALUE_INDEX = 1;

    /** ��r����X�^�b�N�g���[�X�̒��� */
    private static final int    COMPARE_LENGTH     = 200;

    /**
     * PerformanceDoctor�̌��ʂ��t�B���^�����O���܂��B<br />
     * 
     * @param warningUnitList WarningUnit�̃��X�g
     * @return �t�B���^�[��̌���
     */
    public List<WarningUnit> doFilter(final List<WarningUnit> warningUnitList)
    {
        // ���[��ID�A�N���X���A���\�b�h���A�d�v�x���L�[��Map���쐬����B
        Map<String, List<WarningUnit>> warningMap = makeMap(warningUnitList);
        List<WarningUnit> resultList = new ArrayList<WarningUnit>();

        // Map�ɓ��͂��ꂽWarningUnit�̃��X�g���o�͂���B
        Collection<List<WarningUnit>> col = warningMap.values();
        for (List<WarningUnit> list : col)
        {
            // WarningUnit�̃��X�g�T�C�Y��FILTER_THRESHOLD�ȉ��̂Ƃ��A
            // SimpleClassifier�𗘗p���ĕ��ނ���B
            // ����ȊO�̂Ƃ��́AKMeans�@�𗘗p���ĕ��ނ���B
            Classifier classifier = ClassifierFactory.getInstance().getClassifier(list);
            List<WarningUnit> unitList = classifier.classify(list);

            // ���ނ������ʂ���������B
            joinList(resultList, unitList);
        }
        return resultList;
    }

    /**
     * WarningUnit�̃��X�g����A���ʂ̂��߂̃}�b�v���쐬����B
     * @param warningUnitList ���ʏ���ۑ�����Ώۂ�warningUnit�̃��X�g 
     * @return ����ۑ�����Map
     */
    private Map<String, List<WarningUnit>> makeMap(final List<WarningUnit> warningUnitList)
    {
        Map<String, List<WarningUnit>> map = new LinkedHashMap<String, List<WarningUnit>>();
        List<WarningUnit> unitList;
        // �S�Ă�warningUnit�����o���A���[��ID�A�N���X���A���\�b�h���A�d�v�x���L�[��
        // Map���쐬����B
        for (WarningUnit warningUnit : warningUnitList)
        {
            String key =
                         warningUnit.getId() + SEPARATOR + warningUnit.getClassName() + SEPARATOR
                                 + warningUnit.getMethodName() + SEPARATOR + warningUnit.getLevel();
            // �C�x���g�ɂ��x���̏ꍇ�A�X�^�b�N�g���[�X����r�ΏۂƂ���B
            if (warningUnit.isEvent())
            {
                String stackTrace = warningUnit.getStackTrace();
                if (stackTrace != null)
                {
                    int length = Math.min(stackTrace.length(), COMPARE_LENGTH);
                    String stackTraceCompare = stackTrace.substring(0, length);
                    key += SEPARATOR + stackTraceCompare;
                }
            }
            unitList = map.get(key);
            // Map�Ɏw�肵���L�[�����݂��Ȃ��Ƃ��A�V�������X�g���쐬����B
            // �L�[�����݂���ꍇ�́A�L�[�ɑΉ����郊�X�g�ɁAwarningUnit��ǉ�����B
            if (unitList == null)
            {
                unitList = new ArrayList<WarningUnit>();
                map.put(key, unitList);
            }
            unitList.add(warningUnit);
        }
        return map;
    }

    /**
     * 2�̃��X�g����������B
     * <code>oldList</code> �� <code>null</code> �̏ꍇ�͉������Ȃ��B
     *
     * @param oldList ������̃��X�g
     * @param unitList �V�K�Œǉ����郊�X�g
     */
    public void joinList(final List<WarningUnit> oldList, final List<WarningUnit> unitList)
    {
        if (oldList != null && unitList != null)
        {
            oldList.addAll(unitList);
        }
    }
}
