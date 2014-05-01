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
package jp.co.acroquest.endosnipe.common.entity;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * �e�O���t�̌v���f�[�^���i�[���邽�߂̃G���e�B�e�B�ł��B
 * @author fujii
 *
 */
public class MeasurementData implements Cloneable
{
    /** measurementDetailMap_�ɂ�����A�P���n��̃f�[�^�̃L�[�B */
    public static final String SINGLE_DETAIL_KEY = "";

    /**
     * �v���l��ʂ�\�� ID �B<br />
     *
     * JAVELIN_MESUREMENT �e�[�u���� MESUREMENT_TYPE �J�����ɗ��p�����l�B
     */
    public int measurementType;

    /** ���\�[�X�ʒm�d���̍��ږ� */
    public String itemName;

    /** �I�u�W�F�N�g�̕\���� */
    public String displayName;

    /** �v�������B */
    public Timestamp measurementTime;

    /** �v���f�[�^�̃}�b�v(�n�񖼁A�n����) */
    private final Map<String, MeasurementDetail> measurementDetailMap_ =
            new LinkedHashMap<String, MeasurementDetail>();

    /** �v���l�̌^ */
    public byte valueType;

    /**
     * {@link MeasurementDetail}�I�u�W�F�N�g��ۑ����Ă���}�b�v���擾���܂��B<br />
     * 
     * @return {@link MeasurementDetail}�I�u�W�F�N�g��ۑ����Ă���}�b�v
     */
    public Map<String, MeasurementDetail> getMeasurementDetailMap()
    {
        return this.measurementDetailMap_;
    }

    /**
     * �v���l��ۑ�����G���e�B�e�B��ۑ����܂��B<br />
     * 
     * @param measurementDetail {@link MeasurementDetail}�I�u�W�F�N�g
     */
    public void addMeasurementDetail(final MeasurementDetail measurementDetail)
    {
        this.measurementDetailMap_.put(measurementDetail.displayName, measurementDetail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(MeasurementType=");
        builder.append(this.measurementType);
        builder.append(",ItemName=");
        builder.append(this.itemName);
        builder.append(",ValueType=");
        builder.append(this.valueType);
        builder.append(",MeasurementDetailMap=");
        builder.append(this.measurementDetailMap_.toString());
        builder.append(")");
        return builder.toString();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MeasurementData clone()
	{
		MeasurementData cloneObj = null;
		try
		{
			cloneObj = (MeasurementData) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			cloneObj = new MeasurementData();
		}
		cloneObj.itemName = this.itemName;
		cloneObj.measurementType = this.measurementType;
		cloneObj.measurementTime = this.measurementTime;
		cloneObj.valueType = this.valueType;
		cloneObj.displayName = this.displayName;

        for (MeasurementDetail detail : this.measurementDetailMap_.values())
        {
            cloneObj.addMeasurementDetail(detail.clone());
        }

        return cloneObj;
    }
}
