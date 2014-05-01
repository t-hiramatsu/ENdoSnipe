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
package jp.co.acroquest.endosnipe.collector.notification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.collector.processor.AlarmType;

/**
 * �V�O�i���̃A���[����ʒm�Ɏg�p����Entity�N���X�B
 * 
 * @author fujii
 */
public class AlarmEntry
{
    /** �A���[���� */
    private String alarmID_;

    /** 臒l */
    private Double risingThreshold_;

    /** ��Ԓl */
    private Double alarmValue_;

    /** �v���Ԋu(�P��:�~���b) */
    private double alarmInterval_;

    /** �A���[���̏�Ԓl */
    private int alarmState_;

    /** �A���[�����o�����ǂ��� */
    private boolean sendAlarm_;

    /** �A���[���̎�� */
    private AlarmType alarmType_;

    /** Javelin��IP�A�h���X */
    private String ipAddress_;

    /** Javelin�̃|�[�g�ԍ� */
    private int port_;

    /** ���O�f�[�^�̕ۑ������DB�̖��O */
    private String databaseName_;

    /** �p�����[�^�i�ǉ����j */
    private final Map<String, Object> parameters_;

    /** �V�O�i���̃��x��(3 or 5) */
    private int signalLevel_;

    /**
     * �R���X�g���N�^�B
     */
    public AlarmEntry()
    {
        this.parameters_ = new HashMap<String, Object>();
    }

    /**
     * �A���[�������擾����B
     * @return �A���[����
     */
    public String getAlarmID()
    {
        return this.alarmID_;
    }

    /**
     * �A���[������ݒ肷��B
     * @param alarmID �A���[����
     */
    public void setAlarmID(final String alarmID)
    {
        this.alarmID_ = alarmID;
    }

    /**
     * ����臒l���擾����B
     * @return ����臒l
     */
    public Double getRisingThreshold()
    {
        return this.risingThreshold_;
    }

    /**
     * ����臒l��ݒ肷��B
     * @param risingThreshold ����臒l
     */
    public void setRisingThreshold(final Double risingThreshold)
    {
        this.risingThreshold_ = risingThreshold;
    }

    /**
     * ��Ԓl���擾����B
     * @return ��Ԓl
     */
    public Double getAlarmValue()
    {
        return this.alarmValue_;
    }

    /**
     * ��Ԓl��ݒ肷��B
     * @param alarmValue ��Ԓl
     */
    public void setAlarmValue(final Double alarmValue)
    {
        this.alarmValue_ = alarmValue;
    }

    /**
     * �v���Ԋu(�P��:�~���b���擾����B
     * @return �v���Ԋu(�P��:�~���b
     */
    public double getAlarmInterval()
    {
        return this.alarmInterval_;
    }

    /**
     * �v���Ԋu(�P��:�~���b��ݒ肷��B
     * @param alarmInterval �v���Ԋu(�P��:�~���b
     */
    public void setAlarmInterval(final double alarmInterval)
    {
        this.alarmInterval_ = alarmInterval;
    }

    /**
     * �A���[���̏�Ԓl���擾����B
     * @return �A���[���̏�Ԓl
     */
    public int getAlarmState()
    {
        return this.alarmState_;
    }

    /**
     * �A���[���̏�Ԓl��ݒ肷��B
     * @param alarmState �A���[���̏�Ԓl
     */
    public void setAlarmState(final int alarmState)
    {
        this.alarmState_ = alarmState;
    }

    /**
     * �A���[�����o�����ǂ�����ݒ肷��B
     * @param sendAlarm �Z�b�g���� sendAlarm
     */
    public void setSendAlarm(final boolean sendAlarm)
    {
        this.sendAlarm_ = sendAlarm;
    }

    /**
     * �A���[�����o�����ǂ������擾����B
     * @return �Ώۂ̌n��
     */
    public boolean isSendAlarm()
    {
        return this.sendAlarm_;
    }

    /**
     * @param alarmType �Z�b�g���� alarmType
     */
    public void setAlarmType(final AlarmType alarmType)
    {
        this.alarmType_ = alarmType;
    }

    /**
     * @return alarmType�@�A���[�����
     */
    public AlarmType getAlarmType()
    {
        return this.alarmType_;
    }

    /**
     * ���̃A���[�������m����Javelin��IP�A�h���X��Ԃ��܂��B<br />
     * 
     * @return IP�A�h���X
     */
    public String getIpAddress()
    {
        return ipAddress_;
    }

    /**
     * ���̃A���[�������m����Javelin��IP�A�h���X��ݒ肵�܂��B<br />
     * 
     * @param ipAddress IP�A�h���X
     */
    public void setIpAddress(final String ipAddress)
    {
        ipAddress_ = ipAddress;
    }

    /**
     * ���̃A���[�������m����Javelin�̃|�[�g�ԍ���Ԃ��܂��B<br />
     * 
     * @return port
     */
    public int getPort()
    {
        return port_;
    }

    /**
     * ���̃A���[�������m����Javelin�̃|�[�g�ԍ���ݒ肵�܂��B<br />
     * 
     * @param port �|�[�g�ԍ�
     */
    public void setPort(final int port)
    {
        port_ = port;
    }

    /**
     * ���̃A���[�������m����Javelin����̃f�[�^��ۑ�����DB�̖��O��Ԃ��܂��B<br />
     * 
     * @return databaseName DB��
     */
    public String getDatabaseName()
    {
        return databaseName_;
    }

    /**
     * ���̃A���[�������m����Javelin����̃f�[�^��ۑ�����DB�̖��O��ݒ肵�܂��B<br />
     * 
     * @param databaseName DB��
     */
    public void setDatabaseName(final String databaseName)
    {
        databaseName_ = databaseName;
    }

    /**
     * �p�����[�^��ǉ����܂��B
     *
     * @param key �L�[
     * @param value �l
     */
    public void addParamter(final String key, final Object value)
    {
        this.parameters_.put(key, value);
    }

    /**
     * �p�����[�^���擾���܂��B
     *
     * @return �p�����[�^
     */
    public Map<String, Object> getParameters()
    {
        return Collections.unmodifiableMap(this.parameters_);
    }

    /**
     * �V�O�i���̃��x�����擾����B
     * @return �V�O�i���̃��x��
     */
    public int getSignalLevel()
    {
        return signalLevel_;
    }

    /**
     * �V�O�i���̃��x����ݒ肷��B
     * @param signalLevel �V�O�i���̃��x��
     */
    public void setSignalLevel(final int signalLevel)
    {
        signalLevel_ = signalLevel;
    }

}
