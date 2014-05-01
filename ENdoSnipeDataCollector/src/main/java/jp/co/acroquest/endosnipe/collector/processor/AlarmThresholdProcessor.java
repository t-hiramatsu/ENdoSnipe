/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.processor;

import java.util.Map;
import java.util.Map.Entry;

import jp.co.acroquest.endosnipe.collector.JavelinDataLogger;
import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.collector.util.AlarmThresholdUtil;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * �w�肳�ꂽ臒l�𒴂����Ƃ��ɃA���[���𔭐�������B
 * @author fujii
 *
 */
public class AlarmThresholdProcessor implements AlarmProcessor
{
    /**
     * �w�肳�ꂽ臒l�ɑ΂���A���[������
     * @param currentResourceData ���݂̃��\�[�X�f�[�^
     * @param prevResourceData ��O�̃��\�[�X�f�[�^
     * @param signalDefinition �A���[���ʒm�̐ݒ�i臒l�Ȃǁj
     * @param alarmData �A���[���ʒm�̗L�����v�Z���邽�߂̃f�[�^�B���̃��\�b�h�̒��ōX�V����
     * @return �A���[���ʒm�̗L���ƁA�ʒm����A���[���ɕ\�������������ꂽAlarmEntry
     */
    public AlarmEntry calculateAlarmLevel(final ResourceData currentResourceData,
            final ResourceData prevResourceData, final SignalDefinitionDto signalDefinition,
            final AlarmData alarmData)
    {
        // �������
        if (currentResourceData == null)
        {
            AlarmEntry alarmEntry = new AlarmEntry();
            alarmEntry.setSendAlarm(false);
            alarmEntry.setAlarmState(JavelinDataLogger.NORMAL_ALARM_LEVEL);
            alarmEntry.setSignalLevel(signalDefinition.getLevel());
            return alarmEntry;
        }
        String matchingPattern = signalDefinition.getMatchingPattern();

        Number itemValueNumber =
                                 AlarmThresholdUtil.getNumberFromResourceData(currentResourceData,
                                                                              matchingPattern);
        if (itemValueNumber == null)
        {
            if (alarmData == null)
            {
                return null;
            }
            int currentLevel = alarmData.getAlarmLevel();
            if (currentLevel <= JavelinDataLogger.NORMAL_ALARM_LEVEL)
            {
                return null;
            }
            // �σO���t�ł͊Ď����Ă��Ȃ���Ԃł͒l�����Ȃ��Ȃ邽�߁A�����A���[���𔭐�������悤�ɂ���B
            itemValueNumber = Double.valueOf(0);
        }
        double itemValue = itemValueNumber.doubleValue();
        AlarmEntry alarmEntry =
                                createAlarmEntry(currentResourceData.measurementTime, itemValue,
                                                 alarmData, signalDefinition);
        return alarmEntry;
    }

    /**
     * 
     * @param measurementTime
     * @param value
     * @param alarmData
     * @param signalDefinition
     * @return
     */
    private AlarmEntry createAlarmEntry(final long measurementTime, final double value,
            final AlarmData alarmData, final SignalDefinitionDto signalDefinition)
    {
        // �Ԃ�l�ƂȂ� AlarmEntry �̏�����
        AlarmEntry entry = null;
        Map<Integer, Double> thresholdMaping = signalDefinition.getThresholdMaping();

        int currentLevel = alarmData.getAlarmLevel();
        int tmpLevel = JavelinDataLogger.NORMAL_ALARM_LEVEL;
        for (Entry<Integer, Double> thresholdEntry : thresholdMaping.entrySet())
        {
            Integer judgeLevel = thresholdEntry.getKey();
            Double threshold = thresholdEntry.getValue();
            if (value >= threshold)
            {
                tmpLevel = judgeLevel;
            }
        }

        if (tmpLevel < currentLevel)
        {
            // �s�v�ɂȂ�A���[�����폜����B
            // ���݂�臒l���x��������������ꍇ�͒��߃A���[����S�č폜����B
            // ���݂�臒l���x����������臒l���x�����������A���[����S�č폜����B
            alarmData.clearStartExceedance();
            alarmData.clearRecoverTimeMap(tmpLevel);
            Long stopExceedanceTime = alarmData.getRecoverTime(tmpLevel);
            if (stopExceedanceTime == null)
            {
                addStopExceedanceTime(alarmData, tmpLevel);
            }
            entry = alarmData.getRecoverAlarmEntry(alarmData, currentLevel, signalDefinition);
        }
        else if (tmpLevel > currentLevel)
        {
            // �s�v�ɂȂ�A���[�����폜����B
            // ���݂�臒l���x��������������ꍇ�͕����A���[����S�Ă���B
            // ���݂�臒l���x����������臒l���x���������߃A���[����S�č폜����B
            alarmData.clearRecoverExceedance();
            alarmData.clearStartExceedance(tmpLevel);
            Long startExceedanceTime = alarmData.getStartExceedanceTime(tmpLevel);
            if (startExceedanceTime == null)
            {
                addStartExceedanceTime(alarmData, tmpLevel);
            }
            entry = alarmData.getExceedanceAlarmEntry(currentLevel, signalDefinition);
        }
        if (entry != null)
        {
            entry.setAlarmValue(value);
            entry.setAlarmID(signalDefinition.getSignalName());
        }
        return entry;
    }

    /**
     * ���݂�臒l�ɑΉ����钴�ߊJ�n������ݒ肷��B
     * @param alarmData 臒l����f�[�^
     * @param level 臒l���x��
     */
    private void addStartExceedanceTime(final AlarmData alarmData, final int level)
    {
        Long targetExceedanceTime = alarmData.getMinRecoverTime(level);
        if (targetExceedanceTime == null)
        {
            targetExceedanceTime = Long.valueOf(System.currentTimeMillis());
        }
        alarmData.addStartExceedanceTime(level, targetExceedanceTime);
    }

    /**
     * ���݂�臒l�ɑΉ����镜���J�n������ݒ肷��B
     * @param alarmData 臒l����f�[�^
     * @param level 臒l���x��
     */
    private void addStopExceedanceTime(final AlarmData alarmData, final int level)
    {
        Long targetExceedanceTime = alarmData.getMinRecoverTime(level);
        if (targetExceedanceTime == null)
        {
            targetExceedanceTime = Long.valueOf(System.currentTimeMillis());
        }
        alarmData.addRecoverTime(level, targetExceedanceTime);
    };

}
