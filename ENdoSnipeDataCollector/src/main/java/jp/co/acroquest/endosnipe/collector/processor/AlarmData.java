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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import jp.co.acroquest.endosnipe.collector.JavelinDataLogger;
import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * 臒l���茋�ʂ�ێ�����N���X
 * @author fujii
 *
 */
public class AlarmData
{
    /** ���݂�臒l���背�x�� */
    private int alarmLevel_;

    /** ���݂̃A���[�����x���ŃA���[���𔭕񂵂��v������ */
    private Long alarmedTime_;

    /** ���x�����Ƃ�臒l�𒴂����v������ */
    private Map<Integer, Long> startExceedanceTimeMap_ = new TreeMap<Integer, Long>();

    /** ���x�����Ƃ�臒l����������v������ */
    private final Map<Integer, Long> recoverTimeMap_ = new TreeMap<Integer, Long>();

    /** ���݂̃A���[����ԁ@*/
    private int alarmStatus_;

    /**
     * �R���X�g���N�^
     */
    public AlarmData()
    {
        this.alarmLevel_ = JavelinDataLogger.STOP_ALARM_LEVEL;
    }

    /**
     * @return alarmLevel
     */
    public int getAlarmLevel()
    {
        return this.alarmLevel_;
    }

    /**
     * @param alarmLevel �Z�b�g���� alarmLevel
     */
    public void setAlarmLevel(final int alarmLevel)
    {
        this.alarmLevel_ = alarmLevel;
    }

    /**
     * @return alarmedTime
     */
    public Long getAlarmedTime()
    {
        return this.alarmedTime_;
    }

    /**
     * @param alarmedTime �Z�b�g���� alarmedTime
     */
    public void setAlarmedTime(final Long alarmedTime)
    {
        this.alarmedTime_ = alarmedTime;
    }

    /**
     * @return alarmStatus
     */
    public int getAlarmStatus()
    {
        return this.alarmStatus_;
    }

    /**
     * @param alarmStatus �Z�b�g���� alarmStatus
     */
    public void setAlarmStatus(final int alarmStatus)
    {
        this.alarmStatus_ = alarmStatus;
    }

    /**
     * ���x�����Ƃ�臒l�𒴂����v���������擾����B
     * @return ���x�����Ƃ�臒l�𒴂����v������
     */
    public Map<Integer, Long> getStartExceedanceTimeMap()
    {
        return this.startExceedanceTimeMap_;
    }

    /**
     * �����Ŏw�肵��臒l���x����臒l���ߊJ�n�������擾����B
     * @param level 臒l���x��
     * @return �����Ŏw�肵��臒l���x����臒l���ߊJ�n����
     */
    public Long getStartExceedanceTime(final int level)
    {
        return this.startExceedanceTimeMap_.get(level);
    }

    /**
     * �����Ŏw�肵��臒l���x����菬����臒l���x���̍ő�臒l���ߊJ�n�������擾����B
     * @param level 臒l���x��
     * @return �����Ŏw�肵��臒l���x����菬����臒l���x���̍ő�臒l���ߊJ�n����
     */
    public Long getMaxStartExceedanceTime(final int level)
    {
        Long maxStartExceedanceTime = null;
        for (Entry<Integer, Long> startExceedanceEntry : this.startExceedanceTimeMap_.entrySet())
        {
            Integer targetLevel = startExceedanceEntry.getKey();
            Long value = startExceedanceEntry.getValue();
            if (targetLevel.intValue() < level)
            {
                maxStartExceedanceTime = value;
            }
            else
            {
                // TreeMap�ɂ��臒l���x�����Ƀ\�[�g����邽�߁A�����Ŏw�肵��臒l���x�����傫�����x���������������_�ŁA
                // �������I������B
                break;
            }
        }
        return maxStartExceedanceTime;
    }

    /**
     * ���x�����Ƃ�臒l�𒴂����v��������ݒ肷��B
     * @param startExceedanceTimeMap ���x�����Ƃ�臒l�𒴂����v������
     */
    public void setStartExceedanceTimeMap(final Map<Integer, Long> startExceedanceTimeMap)
    {
        this.startExceedanceTimeMap_ = startExceedanceTimeMap;
    }

    /**
     * 臒l���ߊJ�n������ݒ肷��B
     * @param level 臒l���x��
     * @param exceedanceTime 臒l���ߎ���
     */
    public void addStartExceedanceTime(final int level, final Long exceedanceTime)
    {
        this.startExceedanceTimeMap_.put(level, exceedanceTime);
    }

    /**
     * 臒l���߃A���[���̒��ł����Ƃ�臒l���x��������臒l�̃A���[�����擾����B
     * @param level 臒l���x��
     * @param signalDefinition 臒l�������
     * @return 臒l���߃A���[���̒��ł����Ƃ�臒l���x��������臒l�̃A���[��
     */
    public AlarmEntry getExceedanceAlarmEntry(final int level,
            final SignalDefinitionDto signalDefinition)
    {
        double escalationPeriod = signalDefinition.getEscalationPeriod();
        long currentTime = System.currentTimeMillis();
        AlarmEntry alarmEntry = null;
        List<Integer> removeList = new ArrayList<Integer>();

        for (Entry<Integer, Long> exceedanceTimeEntry : this.startExceedanceTimeMap_.entrySet())
        {
            Integer targetLevel = exceedanceTimeEntry.getKey();
            Long value = exceedanceTimeEntry.getValue();

            // �w�肵��臒l���x����������臒l���x���̒��ŁA��Q�����������ݒ肳��Ă�����̂��ΏہB
            if (targetLevel.intValue() > level && (value + escalationPeriod) < currentTime)
            {
                alarmEntry = new AlarmEntry();
                alarmEntry.setAlarmType(AlarmType.FAILURE);
                alarmEntry.setAlarmInterval(escalationPeriod);
                alarmEntry.setAlarmState(targetLevel);
                alarmEntry.setSendAlarm(true);
                alarmEntry.setSignalLevel(signalDefinition.getLevel());
                setAlarmLevel(targetLevel.intValue());
                removeList.add(targetLevel);
            }
        }
        // ����Ɏg�p������Q���x�����L�[����폜����B
        for (Integer removeLevel : removeList)
        {
            this.startExceedanceTimeMap_.remove(removeLevel);
        }

        return alarmEntry;
    }

    /**
     * 臒l���ߊJ�n�������N���A����B
     */
    public void clearStartExceedance()
    {
        this.startExceedanceTimeMap_.clear();
    }

    /**
     * �����Ŏw�肵��臒l���x����������臒l���x���Ŕ�������臒l���߃A���[�����폜����B
     * @param level �폜����ɗ��p����臒l���x��
     */
    public void clearStartExceedance(final int level)
    {
        Set<Entry<Integer, Long>> startExceedanceEntrySet = this.startExceedanceTimeMap_.entrySet();
        Iterator<Entry<Integer, Long>> it = startExceedanceEntrySet.iterator();
        while (it.hasNext())
        {
            Entry<Integer, Long> startExceedanceEntry = it.next();
            Integer targetLevel = startExceedanceEntry.getKey();
            if (targetLevel.intValue() > level)
            {
                it.remove();
            }
        }
    }

    /**
     * �w�肵��臒l���x���ƈ�v���镜���A���[���̎������擾����B
     * @param level 臒l���x��
     * @return �w�肵��臒l���x���ƈ�v���镜���A���[���̎���
     */
    public Long getRecoverTime(final int level)
    {
        return this.recoverTimeMap_.get(level);
    }

    /**
     * �����A���[���̒��ł����Ƃ�臒l���x�����Ⴂ�������擾����B
     * @param level 臒l���x��
     * @return �����A���[���̒��ł����Ƃ�臒l���x�����Ⴂ����
     */
    public Long getMinRecoverTime(final int level)
    {
        Long minRecoverTime = null;
        for (Entry<Integer, Long> recoverEntry : this.recoverTimeMap_.entrySet())
        {
            Integer targetLevel = recoverEntry.getKey();
            Long value = recoverEntry.getValue();
            if (targetLevel.intValue() > level)
            {
                minRecoverTime = value;
                break;
            }
        }
        return minRecoverTime;
    }

    /**
     * �����A���[���̒��ł����Ƃ�臒l���x�����Ⴂ�������擾����B
     * @param alarmData ���݂�臒l���
     * @param level 臒l���x��
     * @param signalDefinition 臒l�������
     * @return �����A���[���̒��ł����Ƃ�臒l���x�����Ⴂ����
     */
    public AlarmEntry getRecoverAlarmEntry(final AlarmData alarmData, final int level,
            final SignalDefinitionDto signalDefinition)
    {
        double escalationPeriod = signalDefinition.getEscalationPeriod();
        long currentTime = System.currentTimeMillis();
        AlarmEntry alarmEntry = null;
        // 
        for (Entry<Integer, Long> recoverTimeEntry : this.recoverTimeMap_.entrySet())
        {
            Integer targetLevel = recoverTimeEntry.getKey();
            Long value = recoverTimeEntry.getValue();

            // �w�肵��臒l���x���������������x���̒��ŁA�����������ݒ肳��Ă�����̂��ΏہB
            if (targetLevel.intValue() < level && (value + escalationPeriod) < currentTime)
            {
                alarmEntry = new AlarmEntry();
                alarmEntry.setAlarmType(AlarmType.RECOVER);
                alarmEntry.setAlarmInterval(escalationPeriod);
                alarmEntry.setAlarmState(targetLevel);
                alarmEntry.setSendAlarm(true);
                alarmEntry.setSignalLevel(signalDefinition.getLevel());
                setAlarmLevel(targetLevel.intValue());
                break;
            }
        }
        return alarmEntry;
    }

    /**
     * �����J�n�������N���A����B
     */
    public void clearRecoverExceedance()
    {
        this.recoverTimeMap_.clear();
    }

    /**
     * �����Ŏw�肵��臒l���x�������Ⴂ臒l���x���Ŕ������������A���[�����폜����B
     * @param level �폜����ɗ��p����臒l���x��
     */
    public void clearRecoverTimeMap(final int level)
    {
        Set<Entry<Integer, Long>> recoverEntrySet = this.recoverTimeMap_.entrySet();
        Iterator<Entry<Integer, Long>> it = recoverEntrySet.iterator();
        while (it.hasNext())
        {
            Entry<Integer, Long> recoverEntry = it.next();
            Integer targetLevel = recoverEntry.getKey();
            if (targetLevel.intValue() < level)
            {
                it.remove();
            }
        }
    }

    /**
     * �����J�n������ݒ肷��B
     * @param level 臒l���x��
     * @param recoverTime 臒l���ߎ���
     */
    public void addRecoverTime(final int level, final Long recoverTime)
    {
        this.recoverTimeMap_.put(level, recoverTime);
    }

    /**
     * 臒l���茋�ʂ����Z�b�g����B
     */
    public void reset()
    {
        this.alarmLevel_ = -1;
        this.alarmStatus_ = -1;

        this.startExceedanceTimeMap_.clear();
        this.recoverTimeMap_.clear();

    }

}
