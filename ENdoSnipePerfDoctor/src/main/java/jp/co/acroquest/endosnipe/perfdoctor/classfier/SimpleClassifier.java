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
 * �x�����X�g�̒�����A���o�l�̍ŏ��l��2�{�̒l���L�[�Ƃ��āA<br />
 * �x�����X�g�𕪗ނ���B<br />
 * ���o�l��������̏ꍇ�A���邢�͌��o�l��0�̏ꍇ�A�x�����X�g�̐擪��Ԃ��B<br />
 * 
 * @author fujii
 * 
 */
public class SimpleClassifier implements Classifier
{
    /** Double�^�̍ő�l */
    private static final double MAX_VALUE = Double.MAX_VALUE;

    /**
     * ���͂������X�g�𕪗ނ��āA���o�l���ő�ƂȂ���̂�Ԃ��B
     * @param warningUnitList WarningUnit�̃��X�g
     * @return WarningUnit�̃��X�g�Ƀt�B���^�[�����������X�g
     */
    public List<WarningUnit> classify(final List<WarningUnit> warningUnitList)
    {
        List<WarningUnit> resultList;
        // ��ƂȂ�L�[���擾����B
        // �������A�L�[�������łȂ��ꍇ(�e�[�u�����Ȃ�)�̂Ƃ��́A
        // List�̐擪�������擾����List��Ԃ��B
        double keyvalue = getKeyValue(warningUnitList);

        if (keyvalue > 0)
        {
            resultList = selectList(warningUnitList, keyvalue);
        }
        else
        {
            resultList = new ArrayList<WarningUnit>();
            resultList.add(warningUnitList.get(0));
        }

        return resultList;
    }

    /**
     * ���ނ��邽�߂ɕK�v�ȃL�[�̒l��Ԃ��B
     * ���̃��\�b�h�ł́A���o�l�̍ŏ��l��2�{��Ԃ��B
     * @param warningUnitList WarningUnit�̃��X�g
     * @return �L�[�̒l
     * @throws NumberFormatException�@���o�l�������ȊO�̂��̂ł���Ƃ��B
     */
    private double getKeyValue(final List<WarningUnit> warningUnitList)
    {
        double min = MAX_VALUE;
        // �S�Ă�WaningUnit�����āA�ŏ��̂��̂𒊏o����B
        for (WarningUnit unit : warningUnitList)
        {
            Object[] args = unit.getArgs();

            int targetValueIndex = PerformanceDoctorFilter.TARGET_VALUE_INDEX;
            if (args == null || args.length < targetValueIndex + 1)
            {
                min = 0;
                break;
            }

            // ���o�l�������ȊO�̃f�[�^�ɂ��ẮA��O��Ԃ��B
            double argNum;
            try
            {
                String argString = args[targetValueIndex].toString();
                argNum = Double.parseDouble(argString);
            }
            catch (NumberFormatException ex)
            {
                min = 0;
                break;
            }
            if (min > argNum)
            {
                min = argNum;
            }
        }
        return min * 2;
    }

    /**
     * �t�B���^�[��̃��X�g�𒊏o����B
     * @param warningUnitList WarningUnit�̃��X�g
     * @param keyValue ���ނŃL�[�ƂȂ�l�B
     * @return �t�B���^�[��̃��X�g
     */
    private List<WarningUnit> selectList(final List<WarningUnit> warningUnitList,
            final double keyValue)
    {

        Map<Integer, WarningUnit> resultMap = new LinkedHashMap<Integer, WarningUnit>();
        for (WarningUnit unit : warningUnitList)
        {
            Object[] args = unit.getArgs();
            // try-catch ��getKeyValue�Ŏ��s�ς݂̂��߁A�ȗ�����B
            double argNum =
                            Double.parseDouble(args[PerformanceDoctorFilter.TARGET_VALUE_INDEX].toString());
            int type = (int)(argNum / keyValue);
            WarningUnit oldUnit = resultMap.get(type);
            if (oldUnit == null)
            {
                resultMap.put(type, unit);
            }
            else
            {
                updateWarningUnit(type, resultMap, oldUnit, unit);
            }
        }
        List<WarningUnit> resultList = convertMapToList(resultMap);
        return resultList;
    }

    /**
     * �ۑ�����Ă���Unit(����܂ł̍ő�̌��o�l������Unit)�Ɣ�r�Ώۂ�Unit���r���A<br/>
     * ��r�Ώۂ̕��̒l���傫����΁AresultMap���̃L�[type�ɑ΂���l���X�V����B
     * @param type �L�[
     * @param resultMap ���ʂ��o�͂���Map
     * @param oldUnit �L�[type�ɑ΂���l(WarningUnit)
     * @param comparedUnit ��r����WarningUnit
     */
    private void updateWarningUnit(final int type, final Map<Integer, WarningUnit> resultMap,
            final WarningUnit oldUnit, final WarningUnit comparedUnit)
    {
        String oldValueString =
                                (oldUnit.getArgs()[PerformanceDoctorFilter.TARGET_VALUE_INDEX]).toString();
        String compareValueString =
                                    (comparedUnit.getArgs()[PerformanceDoctorFilter.TARGET_VALUE_INDEX]).toString();
        double oldValue = Double.parseDouble(oldValueString);
        double comparedValue = Double.parseDouble(compareValueString);

        // ���o�l���傫�����̂��D�悳��郋�[���̏ꍇ�ɂ͌��o�l���傫���x����o�^���A
        // ���o�l�����������̂��D�悳��郋�[���̏ꍇ�ɂ͌��o�l���������x����o�^����B
        boolean isDescend = oldUnit.isDescend();
        if (isDescend)
        {
            if (oldValue < comparedValue)
            {
                resultMap.put(type, comparedUnit);
            }
        }
        else
        {
            if (oldValue > comparedValue)
            {
                resultMap.put(type, comparedUnit);
            }
        }
    }

    /**
     * �t�B���^�[��̃}�b�v���烊�X�g�^�ɕϊ�����B
     * @param resultMap �t�B���^�[��̃}�b�v 
     * @return�@�t�B���^�[���WarningUnit�̃��X�g
     */
    private List<WarningUnit> convertMapToList(final Map<Integer, WarningUnit> resultMap)
    {
        List<WarningUnit> resultList = new ArrayList<WarningUnit>();

        Collection<WarningUnit> col = resultMap.values();
        resultList.addAll(col);
        return resultList;
    }
}
