/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.report.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.ResponseTimeSummaryRecord;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * ���X�|���X�^�C���̌v���������_�ł̃T�}�����擾����f�[�^�A�N�Z�T
 * 
 * @author M.Yoshida
 */
public class ResponseTimeSummaryRecordAccessor
{    
    /**
     * ���Ԃ��w�肵�A���̊��ԓ��ł̃��X�|���X�^�C�����|�[�g�̃f�[�^���擾����B
     * 
     * @param database  �f�[�^�x�[�X��
     * @param startTime ��������(�J�n����)
     * @param endTime   ��������(�I������)
     * @return ���X�|���X�^�C�����|�[�g�̃f�[�^
     * @throws SQLException �f�[�^�擾���ɃG���[�����������ꍇ
     */
    public List<ResponseTimeSummaryRecord> findResponseStatisticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<ResponseTimeSummaryRecord> result = new ArrayList<ResponseTimeSummaryRecord>();

        Map<String, List<ReportItemValue>> minValues;
        Map<String, List<ReportItemValue>> maxValues;
		Map<String, List<ReportItemValue>> cntValues;
		Map<String, List<ReportItemValue>> aveValues;
		Map<String, List<ReportItemValue>> exceptionValues;
		Map<String, List<ReportItemValue>> stallValues;

		minValues = ReportDao.selectAverageAll(database, startTime, endTime,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MIN);

		maxValues = ReportDao.selectAverageAll(database, startTime, endTime,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MAX);

		cntValues = ReportDao.selectSumAll(database, startTime, endTime,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);

		aveValues = ReportDao.selectAverageAll(database, startTime, endTime,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);

		exceptionValues = ReportDao.selectSumAll(database, startTime,
				endTime, TelegramConstants.ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT);

	    stallValues = ReportDao.selectSumAll(database, startTime, endTime,
	            TelegramConstants.ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT);

        for (Map.Entry<String, List<ReportItemValue>> entry : cntValues.entrySet())
        {
			for (int cnt = 0; cnt < entry.getValue().size(); cnt++)
			{
				String itemName = entry.getKey();
				List<ReportItemValue> exceptionList = exceptionValues
						.get(itemName);
				ReportItemValue exceptionValue = null;
				if(exceptionList != null)
				{
					exceptionValue = exceptionList.get(cnt);
				}
				else
				{
					exceptionValue = new ReportItemValue();
					exceptionValue.summaryValue = 0;
					exceptionValue.maxValue = 0;
					exceptionValue.minValue = 0;
				}

				List<ReportItemValue> stallList = stallValues.get(itemName);
				ReportItemValue stallValue = null;
				if (stallList != null)
				{
				    stallValue = stallList.get(cnt);
				}
				else
				{
				    stallValue = new ReportItemValue();
				    stallValue.summaryValue = 0;
				    stallValue.maxValue = 0;
				    stallValue.minValue = 0;
				}

				ResponseTimeSummaryRecord record = null;
				if (cnt < result.size())
				{
					record = result.get(cnt);
				}
				else
				{
				    record = new ResponseTimeSummaryRecord();
                    result.add(record);
				}
				
                List<ReportItemValue> minValueList = minValues.get(itemName);
                List<ReportItemValue> maxValueList = maxValues.get(itemName);
                List<ReportItemValue> cntValueList = cntValues.get(itemName);
                List<ReportItemValue> aveValueList = aveValues.get(itemName);
                ReportItemValue minValue = minValueList.get(cnt);
                ReportItemValue maxValue = maxValueList.get(cnt);
                ReportItemValue cntValue = cntValueList.get(cnt);
                ReportItemValue aveValue = aveValueList.get(cnt);
                record =
                        updateResponseTimeSummaryRecord(record, minValue, maxValue, cntValue,
                                                        aveValue, exceptionValue, stallValue);
			}
		}

        return result;
    }

    /**
     * �v���l�̐��f�[�^����A�T�}���f�[�^�𐶐�����B
     *
     * @param minValue �ŏ��l�f�[�^
     * @param maxValue �ő�l�f�[�^
     * @param cntValue �A�N�Z�X�񐔃f�[�^
     * @param aveValue ���ϒl�f�[�^
     * @param exceptionValue ��O�f�[�^ 
     * @param stallValue �X�g�[���f�[�^
     * @return �T�}���f�[�^
     */
    private ResponseTimeSummaryRecord updateResponseTimeSummaryRecord(
			ResponseTimeSummaryRecord record,
			ReportItemValue minValue, ReportItemValue maxValue,
			ReportItemValue cntValue, ReportItemValue aveValue,
			ReportItemValue exceptionValue, ReportItemValue stallValue)
    {
    	if (record == null)
    	{
			record = new ResponseTimeSummaryRecord();
		}

        record.setMeasurementTime(minValue.measurementTime);
        if (cntValue.itemName.startsWith("/"))
        {
            record.setWebAccessCount(cntValue.summaryValue.longValue());
            record.setWebAccessCountMax(cntValue.maxValue.longValue());
            record.setWebAccessCountMin(cntValue.minValue.longValue());
            record.setWebMaxResponseTime(maxValue.summaryValue.longValue());
            record.setWebMaxResponseTimeMax(maxValue.maxValue.longValue());
            record.setWebMaxResponseTimeMin(maxValue.minValue.longValue());
            record.setWebMinResponseTime(minValue.summaryValue.longValue());
            record.setWebMinResponseTimeMax(minValue.maxValue.longValue());
            record.setWebMinResponseTimeMin(minValue.minValue.longValue());
            record.setWebAveResponseTime(aveValue.summaryValue.longValue());
            record.setWebAveResponseTimeMax(aveValue.maxValue.longValue());
            record.setWebAveResponseTimeMin(aveValue.minValue.longValue());
            record.setWebExceptionCount(exceptionValue.summaryValue.longValue());
            record.setWebExceptionCountMax(exceptionValue.maxValue.longValue());
            record.setWebExceptionCountMin(exceptionValue.minValue.longValue());
            record.setWebStallCount(stallValue.summaryValue.longValue());
            record.setWebStallCountMax(stallValue.maxValue.longValue());
            record.setWebStallCountMin(stallValue.minValue.longValue());
        }
        else if (cntValue.itemName.startsWith("jdbc:"))
        {
            record.setSqlAccessCount(cntValue.summaryValue.longValue());
            record.setSqlAccessCountMax(cntValue.maxValue.longValue());
            record.setSqlAccessCountMin(cntValue.minValue.longValue());
            record.setSqlMaxResponseTime(maxValue.summaryValue.longValue());
            record.setSqlMaxResponseTimeMax(maxValue.maxValue.longValue());
            record.setSqlMaxResponseTimeMin(maxValue.minValue.longValue());
            record.setSqlMinResponseTime(minValue.summaryValue.longValue());
            record.setSqlMinResponseTimeMax(minValue.maxValue.longValue());
            record.setSqlMinResponseTimeMin(minValue.minValue.longValue());
            record.setSqlAveResponseTime(aveValue.summaryValue.longValue());
            record.setSqlAveResponseTimeMax(aveValue.maxValue.longValue());
            record.setSqlAveResponseTimeMin(aveValue.minValue.longValue());
            record.setSqlExceptionCount(exceptionValue.summaryValue.longValue());
            record.setSqlExceptionCountMax(exceptionValue.maxValue.longValue());
            record.setSqlExceptionCountMin(exceptionValue.minValue.longValue());
            record.setSqlStallCount(stallValue.summaryValue.longValue());
            record.setSqlStallCountMax(stallValue.maxValue.longValue());
            record.setSqlStallCountMin(stallValue.minValue.longValue());
        }
        else
        {
            record.setJavaAccessCount(cntValue.summaryValue.longValue());
            record.setJavaAccessCountMax(cntValue.maxValue.longValue());
            record.setJavaAccessCountMin(cntValue.minValue.longValue());
            record.setJavaMaxResponseTime(maxValue.summaryValue.longValue());
            record.setJavaMaxResponseTimeMax(maxValue.maxValue.longValue());
            record.setJavaMaxResponseTimeMin(maxValue.minValue.longValue());
            record.setJavaMinResponseTime(minValue.summaryValue.longValue());
            record.setJavaMinResponseTimeMax(minValue.maxValue.longValue());
            record.setJavaMinResponseTimeMin(minValue.minValue.longValue());
            record.setJavaAveResponseTime(aveValue.summaryValue.longValue());
            record.setJavaAveResponseTimeMax(aveValue.maxValue.longValue());
            record.setJavaAveResponseTimeMin(aveValue.minValue.longValue());
            record.setJavaExceptionCount(exceptionValue.summaryValue.longValue());
            record.setJavaExceptionCountMax(exceptionValue.maxValue.longValue());
            record.setJavaExceptionCountMin(exceptionValue.minValue.longValue());
            record.setJavaStallCount(stallValue.summaryValue.longValue());
            record.setJavaStallCountMax(stallValue.maxValue.longValue());
            record.setJavaStallCountMin(stallValue.minValue.longValue());
        }

        return record;
    }
}
