/*
 * Copyright (c) 2004-2010 SMG Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.report.dao.util;

import java.util.List;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.util.ResourceDataUtil;

/**
 * ������\���O���t�̃f�[�^�̕ϊ����s���A���[�e�B���e�B�N���X�ł��B
 * 
 * @author iida
 */
public class PercentageDataUtil
{
    /**
     * �R���X�g���N�^���B�����܂��B<br />
     */
    private PercentageDataUtil()
    {
        // DO NOTHING
    }
    
    /**
     * �w�肳�ꂽ�A������\���O���t�̃f�[�^���A�{�����|������O�̂��̂ɖ߂��܂��B<br />
     * 
     * @param percentageValues
     *            �{�����|����ꂽ��̃f�[�^
     * @return �{�����|������O�̃f�[�^
     */
    public static List<ReportItemValue> reconstitutePercentageData(
            List<ReportItemValue> percentageValues)
    {
        for (ReportItemValue value : percentageValues)
        {
            value.maxValue = value.maxValue.intValue()
                    / ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
            value.minValue = value.minValue.intValue()
                    / ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
            value.summaryValue = value.summaryValue.intValue()
                    / ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
        }
        return percentageValues;
    }
}
