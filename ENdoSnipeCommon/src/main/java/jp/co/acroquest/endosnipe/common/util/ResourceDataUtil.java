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
package jp.co.acroquest.endosnipe.common.util;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;

/**
 * �V�X�e�����\�[�X���������߂̃��[�e�B���e�B�N���X<br />
 * 
 * @author ochiai
 */
public class ResourceDataUtil
{
    /** �p�[�Z���g�ɂ��邽�߂̒萔:100 */
    public static final int PERCENT_CONST = 100;

    /** nano �� mili �̕ϊ��̂��߂̒萔�F1000*1000 */
    public static final int NANO_TO_MILI = 1000 * 1000;

    /** CPU�g�p���i���j�̍ő�l */
    private static final double MAX_CPU_RATE = 100.0;

    /**
     * ������\���f�[�^�l��DB�ɓ���鎞�Ɋ|����l�B<br />
     * DB�ɓ������̂͐����l�Ȃ̂ŁA�����_�ȉ����ێ����邽�߂ɁA���̒l���|����B<br />
     */
    public static final int PERCENTAGE_DATA_MAGNIFICATION = 1000;

    private ResourceDataUtil()
    {
        // do nothing.
    }

    /**
     * �w�肳�ꂽ�f�[�^����ɂ��A�v���l��S��0�ɁA�v���������w�肳�ꂽ���̂ɕύX�����A�ʂ̃f�[�^�𐶐����A�Ԃ��܂��B<br>
     * @param srcData ���̃f�[�^
     * @param measurementTime �ύX��̌v������
     * @param connected �ڑ������ǂ���
     * @return �v���l��0�ɂ��A�v��������ύX�����f�[�^
     */
    public static ResourceData createAllZeroResourceData(final ResourceData srcData,
            final long measurementTime, final boolean connected)
    {
        ResourceData dstData = new ResourceData();
        dstData.measurementTime = measurementTime;
        dstData.hostName = srcData.hostName;
        dstData.ipAddress = srcData.ipAddress;
        dstData.portNum = srcData.portNum;

        Set<Entry<String, MeasurementData>> measurementMapEntrySet =
                srcData.getMeasurementMap().entrySet();

        for (Map.Entry<String, MeasurementData> measurementMapEntry : measurementMapEntrySet)
        {
            MeasurementData srcMeasurementData = measurementMapEntry.getValue();
            MeasurementData dstMeasurementData = new MeasurementData();
            dstMeasurementData.measurementType = srcMeasurementData.measurementType;
            dstMeasurementData.itemName = srcMeasurementData.itemName;
            dstMeasurementData.measurementTime = new Timestamp(measurementTime);
            dstMeasurementData.valueType = ItemType.getItemTypeNumber(ItemType.ITEMTYPE_STRING);

            Set<Entry<String, MeasurementDetail>> detailMapEntrySet =
                    srcMeasurementData.getMeasurementDetailMap().entrySet();

            for (Map.Entry<String, MeasurementDetail> measurementDetailMapEntry : detailMapEntrySet)
            {
                MeasurementDetail srcMeasurementDetail = measurementDetailMapEntry.getValue();
                MeasurementDetail dstMeasurementDetail = new MeasurementDetail();
                // JavaUpTime�̏ꍇ�A0�ł͂Ȃ�1���Z�b�g����B
                if (Constants.ITEMNAME_JAVAUPTIME.equals(dstMeasurementData.itemName))
                {
                    if (connected == true)
                    {
                        dstMeasurementDetail.value = "1";
                    }
                    else
                    {
                        dstMeasurementDetail.value = srcMeasurementDetail.value;
                    }
                }
                else
                {
                    dstMeasurementDetail.value = "0";
                }
                dstMeasurementDetail.displayName = srcMeasurementDetail.displayName;
                dstMeasurementDetail.itemId = srcMeasurementDetail.itemId;
                dstMeasurementDetail.itemName = srcMeasurementDetail.itemName;
                dstMeasurementDetail.itemNum = srcMeasurementDetail.itemNum;
                dstMeasurementDetail.type = srcMeasurementDetail.type;
                dstMeasurementDetail.typeItemName = srcMeasurementDetail.typeItemName;
                dstMeasurementDetail.valueId = srcMeasurementDetail.valueId;
                dstMeasurementData.addMeasurementDetail(dstMeasurementDetail);
            }

            dstData.addMeasurementData(dstMeasurementData);
        }

        return dstData;
    }

    /**
     * �w�肳�ꂽ�A�A��2��̃f�[�^���ׁA�ϐ��n��ɂ��āA��̂��̂ɂ����������̂�T���܂��B<br>
     * �����ɂ��āA�O���t�̎n�܂��\�����߂ɒǉ����ׂ��f�[�^�i�l��0�̂��́j���쐬���A�Ԃ��܂��B<br>
     * @param prevData �O��̃f�[�^
     * @param currData ����̃f�[�^
     * @return �O���t�̎n�܂��\�����߂ɒǉ����ׂ��f�[�^�B
     */
    public static ResourceData createAdditionalPreviousData(final ResourceData prevData,
            final ResourceData currData)
    {
        ResourceData additionalData = new ResourceData();
        // �����͑O��̂��̂�p����B
        additionalData.measurementTime = prevData.measurementTime;
        additionalData.hostName = currData.hostName;
        additionalData.ipAddress = currData.ipAddress;
        additionalData.portNum = currData.portNum;

        Set<Entry<String, MeasurementData>> measurementMapEntrySet =
                currData.getMeasurementMap().entrySet();

        for (Map.Entry<String, MeasurementData> measurementMapEntry : measurementMapEntrySet)
        {
            MeasurementData currMeasurementData = measurementMapEntry.getValue();
            MeasurementData prevMeasurementData =
                    prevData.getMeasurementMap().get(measurementMapEntry.getKey());
            if (prevMeasurementData == null)
            {
                prevMeasurementData = new MeasurementData();
            }
            MeasurementData additionalMeasurementData = new MeasurementData();
            additionalMeasurementData.measurementType = currMeasurementData.measurementType;
            additionalMeasurementData.itemName = currMeasurementData.itemName;
            // �����͑O��̂��̂�p����B
            additionalMeasurementData.measurementTime = new Timestamp(prevData.measurementTime);
            additionalMeasurementData.valueType =
                    ItemType.getItemTypeNumber(ItemType.ITEMTYPE_STRING);

            Set<Entry<String, MeasurementDetail>> detailMapEntry =
                    currMeasurementData.getMeasurementDetailMap().entrySet();

            for (Map.Entry<String, MeasurementDetail> measurementDetailMapEntry : detailMapEntry)
            {
                MeasurementDetail currMeasurementDetail = measurementDetailMapEntry.getValue();
                String key = measurementDetailMapEntry.getKey();
                MeasurementDetail prevMeasurementDetail =
                        prevMeasurementData.getMeasurementDetailMap().get(key);

                if (prevMeasurementDetail == null)
                {
                    MeasurementDetail addtionalMeasurementDetail = new MeasurementDetail();
                    // �l��0������B
                    addtionalMeasurementDetail.value = "0";
                    addtionalMeasurementDetail.displayName = currMeasurementDetail.displayName;
                    addtionalMeasurementDetail.itemId = currMeasurementDetail.itemId;
                    addtionalMeasurementDetail.itemName = currMeasurementDetail.itemName;
                    addtionalMeasurementDetail.itemNum = currMeasurementDetail.itemNum;
                    addtionalMeasurementDetail.type = currMeasurementDetail.type;
                    addtionalMeasurementDetail.typeItemName = currMeasurementDetail.typeItemName;
                    addtionalMeasurementDetail.valueId = currMeasurementDetail.valueId;
                    additionalMeasurementData.addMeasurementDetail(addtionalMeasurementDetail);
                }
            }

            additionalData.addMeasurementData(additionalMeasurementData);
        }

        return additionalData;
    }

    /**
     * CPU�g�p�����v�Z����
     * @param cpuTime �v�����Ԓ���CPU���ԁi�i�m�b�j
     * @param measurementInterval �v���Ԋu(�~���b)
     * @param processorCount �v���Z�b�T��
     * @return CPU�g�p��
     */
    public static double calcCPUUsage(final long cpuTime, final long measurementInterval,
            final long processorCount)
    {
        double cpuUsage = 0.0;
        if (measurementInterval * processorCount > 0)
        {
            cpuUsage =
                    (double)cpuTime / (measurementInterval * NANO_TO_MILI * processorCount)
                    * PERCENT_CONST;
            // �p�t�H�[�}���X�J�E���^�̎d�l��ACPU�g�p����100���𒴂��邱�Ƃ����邽�߁A
            // �ő�100���Ɋۂ߂�B�i#2006�j
            cpuUsage = Math.min(cpuUsage, MAX_CPU_RATE);
        }
        return cpuUsage;
    }
}
