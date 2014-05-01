/*
 * Copyright (c) 2004-2008 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ���|�[�g�o�͗p�̃G���e�B�e�B<br /> 
 *
 * @author eriguchi
 */
public class ReportItemValue
{
    /**
     * �v�������B<br />
     */
    public Timestamp measurementTime;

    /**
     * �n��<br />
     */
    public String    itemName;

    /**
     * �v���l(����l)�B
     */
    public Number    limitValue;

    /**
     * �v���l(�T�}��)�B
     */
    public Number    summaryValue;

    /**
     * �v���l(�ő�)�B
     */
    public Number    maxValue;

    /**
     * �v���l(�ŏ�)�B
     */
    public Number    minValue;

    /**
     * �v���l�̃C���f�b�N�X
     */
    public int       index;

    /**
     * �R���X�g���N�^
     */
    public ReportItemValue()
    {
        itemName = "";
        limitValue = 0;
        summaryValue = 0;
        maxValue = 0;
        minValue = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[itemName=");
        builder.append(this.itemName);
        builder.append(", summaryValue=");
        builder.append(this.summaryValue);
        builder.append(", maxValue=");
        builder.append(this.maxValue);
        builder.append(", minValue=");
        builder.append(this.minValue);
        builder.append(", measurementTime=");
        builder.append(this.measurementTime);
        builder.append("]");
        return builder.toString();
    }
}
