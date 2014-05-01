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
package jp.co.acroquest.endosnipe.data.dto;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.data.entity.SignalDefinition;

/**
 * 臒l���������`���Dto�ł��B<br />
 * 
 * @author fujii
 *
 */
public class SignalDefinitionDto
{
    /** �V�O�i����`�e�[�u����ID */
    private long                 signalId_;

    /** �V�O�i���� */
    private String               signalName_;

    /** �}�b�`���O�p�^�[�� */
    private String               matchingPattern_;

    /** �ݒ�ł���臒l�̏�����x�� */
    private int                  level_;

    /** ���x�����Ƃ�臒l */
    private Map<Integer, Double> thresholdMaping_;

    /** �G�X�J���[�V�������� */
    private double               escalationPeriod_;

    /** 臒l�̃X�v���b�g�p�^�[�� */
    private static final Pattern SPLIT_PATERN = Pattern.compile(",");

    /**
     * �V�O�i����`�e�[�u����ID���擾����B
     * @return �V�O�i����`�e�[�u����ID
     */
    public long getSignalId()
    {
        return signalId_;
    }

    /**
     * �V�O�i����`�e�[�u����ID��ݒ肷��B
     * @param signalId �V�O�i����`�e�[�u����ID
     */
    public void setSignalId(long signalId)
    {
        this.signalId_ = signalId;
    }

    /**
     * �V�O�i�������擾����B
     * @return �V�O�i����
     */
    public String getSignalName()
    {
        return signalName_;
    }

    /**
     * �V�O�i������ݒ肷��B
     * @param signalName �V�O�i����
     */
    public void setSignalName(String signalName)
    {
        this.signalName_ = signalName;
    }

    /**
     * �}�b�`���O�p�^�[�����擾����B
     * @return �}�b�`���O�p�^�[����
     */
    public String getMatchingPattern()
    {
        return matchingPattern_;
    }

    /**
     * �}�b�`���O�p�^�[����ݒ肷��B
     * @param matchingPattern �}�b�`���O�p�^�[����
     */
    public void setMatchingPattern(String matchingPattern)
    {
        this.matchingPattern_ = matchingPattern;
    }

    /**
     * �ݒ�ł���臒l�̏�����x�����擾����B
     * @return �ݒ�ł���臒l�̏�����x��
     */
    public int getLevel()
    {
        return level_;
    }

    /**
     * �ݒ�ł���臒l�̏�����x����ݒ肷��B
     * @param level �ݒ�ł���臒l�̏�����x��
     */
    public void setLevel(int level)
    {
        this.level_ = level;
    }

    /**
     * ���x�����Ƃ�臒l���擾����B
     * @return ���x�����Ƃ�臒l
     */
    public Map<Integer, Double> getThresholdMaping()
    {
        return thresholdMaping_;
    }

    /**
     * ���x�����Ƃ�臒l��ݒ肷��B
     * @param thresholdMaping ���x�����Ƃ�臒l
     */
    public void setThresholdMaping(Map<Integer, Double> thresholdMaping)
    {
        this.thresholdMaping_ = thresholdMaping;
    }

    /**
     * �G�X�J���[�V�������Ԃ��擾����B
     * @return �G�X�J���[�V��������
     */
    public double getEscalationPeriod()
    {
        return escalationPeriod_;
    }

    /**
     * �G�X�J���[�V�������Ԃ�ݒ肷��B
     * @param escalationPeriod �G�X�J���[�V��������
     */
    public void setEscalationPeriod(double escalationPeriod)
    {
        this.escalationPeriod_ = escalationPeriod;
    }

    /**
     * {@link SignalDefinitionDto} �I�u�W�F�N�g�𐶐����܂��B
     * @param signalDefinition {@link SignalDefinition}�I�u�W�F�N�g
     */
    public SignalDefinitionDto(SignalDefinition signalDefinition)
    {
        this.signalId_ = signalDefinition.signalId;
        this.signalName_ = signalDefinition.signalName;
        this.matchingPattern_ = signalDefinition.matchingPattern;
        this.level_ = signalDefinition.level;
        this.escalationPeriod_ = signalDefinition.escalationPeriod;
        this.thresholdMaping_ = new TreeMap<Integer, Double>();

        String[] thresholdings = SPLIT_PATERN.split(signalDefinition.patternValue);
        if (thresholdings != null)
        {
            for (int cnt = 0; cnt < thresholdings.length; cnt++)
            {
                Double threshold = null;
                try
                {
                    String thresholdStr = thresholdings[cnt];
                    threshold = Double.valueOf(thresholdStr);
                }
                catch (NumberFormatException ex)
                {
                    // Do Nothing.
                }
                thresholdMaping_.put(Integer.valueOf(cnt + 1), threshold);
            }
        }
    }

    @Override
    public String toString()
    {
        return "SignalInfoDto [signalId=" + signalId_ + ", signalName=" + signalName_
            + ", matchingPattern=" + matchingPattern_ + ", level=" + level_ + ", thresholdMaping="
            + thresholdMaping_ + ", escalationPeriod=" + escalationPeriod_ + "]";
    }
}
