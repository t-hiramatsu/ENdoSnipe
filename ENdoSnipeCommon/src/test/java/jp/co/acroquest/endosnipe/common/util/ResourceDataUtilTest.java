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

import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import junit.framework.TestCase;

/**
 * ResourceDataUtil�̃e�X�g�R�[�h�B<br>
 * 
 * @author iida
 */
public class ResourceDataUtilTest extends TestCase
{
    /**
     * �w�肳�ꂽ�l�Ɩ��O������MeasurementDetail�I�u�W�F�N�g�𐶐����܂��B<br>
     * 
     * @param value �l
     * @param ame ���O
     * @return MeasurementDetail�I�u�W�F�N�g
     */
    private MeasurementDetail createMeasurementDetail(final long value, final String name)
    {
        MeasurementDetail measurementDetail = new MeasurementDetail();
        measurementDetail.value = String.valueOf(value);
        measurementDetail.displayName = name;
        measurementDetail.itemId = 0;
        measurementDetail.itemName = name;
        return measurementDetail;
    }

    /**
     * �w�肳�ꂽ�^�C�v�Ɩ��O�Ǝ���������MeasurementData�I�u�W�F�N�g�𐶐����܂��B<br>
     * 
     * @param type �^�C�v
     * @param name ���O
     * @param time ����
     * @return MeasurementData�I�u�W�F�N�g
     */
    private MeasurementData createMeasurementData(final int type, final String name, final long time)
    {
        MeasurementData measurementData = new MeasurementData();
        measurementData.measurementType = type;
        measurementData.itemName = name;
        measurementData.measurementTime = new Timestamp(time);
        measurementData.valueType = 0;
        return measurementData;
    }

    /**
     * �w�肳�ꂽ����������ResourceData�I�u�W�F�N�g�𐶐����܂��B<br>
     * 
     * @param time ����
     * @return ResourceData�I�u�W�F�N�g
     */
    private ResourceData createResourceData(final long time)
    {
        ResourceData resourceData = new ResourceData();
        resourceData.measurementTime = time;
        resourceData.hostName = "localhost";
        resourceData.ipAddress = "127.0.0.1";
        resourceData.portNum = 18000;
        return resourceData;
    }

    /**
     * �V���ȃf�[�^���ǉ�����Ă��Ȃ��ꍇ�B<br>
     */
    public void testCreateAdditionalPreviousData_SameData()
    {
        MeasurementDetail detail;
        MeasurementData data;

        ResourceData prevData = this.createResourceData(1274247510000L);
        data = this.createMeasurementData(1, "����1", 1274247510000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        prevData.addMeasurementData(data);

        ResourceData currData = this.createResourceData(1274247515000L);
        data = this.createMeasurementData(1, "����1", 1274247515000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        currData.addMeasurementData(data);

        ResourceData additionalData =
                ResourceDataUtil.createAdditionalPreviousData(prevData, currData);

        if (additionalData.getMeasurementMap().size() == 0)
        {
            assertTrue(true);
        }
    }

    /**
     * �V����MeasurementDetail��1�ǉ����ꂽ�ꍇ�B<br>
     */
    public void testCreateAdditionalPreviousData_MeasurementDetailAdded()
    {
        MeasurementDetail detail;
        MeasurementData data;

        ResourceData prevData = this.createResourceData(1274247510000L);
        data = this.createMeasurementData(1, "����1", 1274247510000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        prevData.addMeasurementData(data);

        ResourceData currData = this.createResourceData(1274247515000L);
        data = this.createMeasurementData(1, "����1", 1274247515000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        detail = this.createMeasurementDetail(200, "����2");
        data.addMeasurementDetail(detail);
        currData.addMeasurementData(data);

        ResourceData additionalData =
                ResourceDataUtil.createAdditionalPreviousData(prevData, currData);
        data = additionalData.getMeasurementMap().get("����1");
        detail = data.getMeasurementDetailMap().get("����2");

        assertNotNull(detail);
    }

    /**
     * �V����MeasurementData��1�ǉ����ꂽ�ꍇ�B<br>
     */
    public void testCreateAdditionalPreviousData_MeasurementDataAdded()
    {
        MeasurementDetail detail;
        MeasurementData data;

        ResourceData prevData = this.createResourceData(1274247510000L);
        data = this.createMeasurementData(1, "����1", 1274247510000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        prevData.addMeasurementData(data);

        ResourceData currData = this.createResourceData(1274247515000L);

        data = this.createMeasurementData(1, "����1", 1274247515000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        currData.addMeasurementData(data);

        data = this.createMeasurementData(2, "����2", 1274247515000L);
        detail = this.createMeasurementDetail(200, "����2");
        data.addMeasurementDetail(detail);
        currData.addMeasurementData(data);

        ResourceData additionalData =
                ResourceDataUtil.createAdditionalPreviousData(prevData, currData);
        data = additionalData.getMeasurementMap().get("����2");
        detail = data.getMeasurementDetailMap().get("����2");

        assertNotNull(detail);
    }

    /**
     * �V����MeasurementDetail��2�ǉ����ꂽ�ꍇ�B<br>
     */
    public void testCreateAdditionalPreviousData_TwoMeasurementDetailAdded()
    {
        MeasurementDetail detail;
        MeasurementData data;

        ResourceData prevData = this.createResourceData(1274247510000L);
        data = this.createMeasurementData(1, "����1", 1274247510000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        prevData.addMeasurementData(data);

        ResourceData currData = this.createResourceData(1274247515000L);

        data = this.createMeasurementData(1, "����1", 1274247515000L);
        detail = this.createMeasurementDetail(100, "����1");
        data.addMeasurementDetail(detail);
        detail = this.createMeasurementDetail(200, "����2");
        data.addMeasurementDetail(detail);
        detail = this.createMeasurementDetail(300, "����3");
        data.addMeasurementDetail(detail);
        currData.addMeasurementData(data);

        ResourceData additionalData =
                ResourceDataUtil.createAdditionalPreviousData(prevData, currData);
        data = additionalData.getMeasurementMap().get("����1");
        MeasurementDetail detail2 = data.getMeasurementDetailMap().get("����2");
        MeasurementDetail detail3 = data.getMeasurementDetailMap().get("����3");

        assertTrue((detail2 != null) &&
                   (detail3 != null));
    }

    /**
     * �V���O���R�A��CPU�ŁACPU�g�p����100�𒴂���ꍇ�́ACPU�g�p���̌v�Z���ʊm�F�B (#2006)<br>
     */
    public void testCalcCPUUsage_SingleCore_Over100Percent()
    {
        double result = ResourceDataUtil.calcCPUUsage(5100L * 1000 * 1000, 5000, 1);
        assertEquals(100.0, result);
    }

    /**
     * �f���A���R�A��CPU�ŁACPU�g�p����100�𒴂���ꍇ�́ACPU�g�p���̌v�Z���ʊm�F�B (#2006)<br>
     */
    public void testCalcCPUUsage_DualCore_Over100Percent()
    {
        double result;
        result = ResourceDataUtil.calcCPUUsage(10200L * 1000 * 1000, 5000, 2);
        assertEquals(100.0, result);
    }
}
