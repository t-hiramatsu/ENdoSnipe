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
package jp.co.acroquest.endosnipe.collector.config;

import jp.co.acroquest.endosnipe.collector.rotate.RotateUtil;

/**
 * �t�@�C�����[�e�[�g�ɗp����ݒ�
 * 
 * @author S.Kimura
 *
 */
public class RotateConfig
{
    /** �f�[�^�x�[�X���� */
    private String database_;

    /** Javelin���O�ێ������̒l */
    private int javelinRotatePeriod_;

    /** Javelin���O�ێ������̒P�ʎ�� */
    private PeriodUnit javelinRotatePeriodUnit_ = PeriodUnit.DAY;

    /** �v���f�[�^�ێ������̒l */
    private int measureRotatePeriod_;

    /** �v���f�[�^�ێ������̒P�ʎ�� */
    private PeriodUnit measureRotatePeriodUnit_ = PeriodUnit.DAY;

    /**
     * @return javelinRotatePeriod
     */
    public int getJavelinRotatePeriod()
    {
        return this.javelinRotatePeriod_;
    }

    /**
     * @param javelinRotatePeriod �Z�b�g���� javelinRotatePeriod
     */
    public void setJavelinRotatePeriod(final int javelinRotatePeriod)
    {
        this.javelinRotatePeriod_ = javelinRotatePeriod;
    }

    /**
     * @param javelinRotatePeriodUnit �Z�b�g���� javelinRotatePeriodUnit
     */
    public void setJavelinRotatePeriodUnit(final PeriodUnit javelinRotatePeriodUnit)
    {
        this.javelinRotatePeriodUnit_ = javelinRotatePeriodUnit;
    }

    /**
     * @param measureRotatePeriod �Z�b�g���� measureRotatePeriod
     */
    public void setMeasureRotatePeriod(final int measureRotatePeriod)
    {
        this.measureRotatePeriod_ = measureRotatePeriod;
    }

    /**
     * @return measureRotatePeriod
     */
    public int getMeasureRotatePeriod()
    {
        return this.measureRotatePeriod_;
    }

    /**
     * @param measureRotatePeriodUnit �Z�b�g���� measureRotatePeriodUnit
     */
    public void setMeasureRotatePeriodUnit(final PeriodUnit measureRotatePeriodUnit)
    {
        this.measureRotatePeriodUnit_ = measureRotatePeriodUnit;
    }

    /**
     * Javelin���O�ێ������̒P�ʂ�
     * Calendar�N���X�̃C���f�b�N�X�Ƃ��Ď擾
     * 
     * @return Javelin���O�ێ������̒P��
     */
    public int getJavelinUnitByCalendar()
    {
        return convertUnit(this.javelinRotatePeriodUnit_);
    }

    /**
     * �v���f�[�^�ێ������̒P�ʂ�
     * Calendar�N���X�̃C���f�b�N�X�Ƃ��Ď擾
     * 
     * @return Javelin���O�ێ������̒P��
     */
    public int getMeasureUnitByCalendar()
    {
        return convertUnit(this.measureRotatePeriodUnit_);
    }

    /**
     * �ێ����Ԃ̒P�ʂ�Calendar�N���X�̃C���f�b�N�X�ɕϊ�
     * 
     * @param unit �ێ����Ԃ̒P��
     * @return Calendar�N���X�̃C���f�b�N�X
     */
    private int convertUnit(final PeriodUnit unit)
    {
        int result = RotateUtil.convertUnit(unit);
        return result;
    }

    /**
     * @return database
     */
    public String getDatabase()
    {
        return this.database_;
    }

    /**
     * @param database �f�[�^�x�[�X��
     */
    public void setDatabase(final String database)
    {
        this.database_ = database;
    }

}
