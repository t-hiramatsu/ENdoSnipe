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
package jp.co.acroquest.endosnipe.collector.util;

import java.util.Map;
import java.util.Map.Entry;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * �V�X�e�����\�[�X��臒l����p���[�e�B���e�B�N���X
 * @author fujii
 *
 */
public class AlarmThresholdUtil implements LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(AlarmThresholdUtil.class);

    private AlarmThresholdUtil()
    {
        //do nothing
    }

    /**
     * ResourceData ����A�w�肵��itemName �̒l���擾����
     * @param resourceData �f�[�^�̓����Ă���resourceData
     * @param itemName �w�肷��key
     * 
     * @return �w�肵��itemName �̒l���Ȃ��ꍇ�Anull��Ԃ�
     */
    public static Number getNumberFromResourceData(final ResourceData resourceData,
            final String itemName)
    {
        MeasurementData measurementData = resourceData.getMeasurementMap().get(itemName);

        if (measurementData == null)
        {
            return null;
        }

        Map<String, MeasurementDetail> measurementDetailMap =
                                                              measurementData.getMeasurementDetailMap();

        Number number = null;
        for (Entry<String, MeasurementDetail> measurementEntry : measurementDetailMap.entrySet())
        {
            String key = measurementEntry.getKey();
            MeasurementDetail measurementDetail = measurementEntry.getValue();
            if (measurementDetail == null)
            {
                return null;
            }
            // �P��̃f�[�^�n��̂Ƃ��́A�󕶎����L�[�ɁA��̒l�݂̂������Ă���A
            // �σf�[�^�n��̓z�X�g���A�G�[�W�F���g�����������L�[�Œl���i�[����邽�߁A����ȊO�͖�������B
            if (!"".equals(key) && !itemName.endsWith(key))
            {
                continue;
            }

            String value = measurementDetail.value;

            try
            {
                number = Double.valueOf(value);
            }
            catch (NumberFormatException ex)
            {
                LOGGER.log(SYSTEM_UNKNOW_ERROR, ex.getMessage(), ex);
            }
        }

        return number;
    }
}
