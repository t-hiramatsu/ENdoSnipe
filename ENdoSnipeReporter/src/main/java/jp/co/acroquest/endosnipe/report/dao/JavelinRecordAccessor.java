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

import jp.co.acroquest.endosnipe.report.dao.util.PercentageDataUtil;
import jp.co.acroquest.endosnipe.report.entity.JavelinRecord;
import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * Javelin �O���t�f�[�^��DB����擾����A�N�Z�T�N���X�B
 * 
 * @author akiba
 */
public class JavelinRecordAccessor
{
    private static final double DECIMAL_TO_PERCENT = 100.0;

    /**
     * ���Ԃ��w�肵�A���̊��ԓ��ł�Javelin�̃��|�[�g�f�[�^���擾����B
     * 
     * @param database �f�[�^�x�[�X���B
     * @param startTime ��������(�J�n����)�B
     * @param endTime ��������(�I������)�B
     * @return Javelin�̃��|�[�g�̃f�[�^�B
     * @throws SQLException �f�[�^�擾���ɗ�O�����������ꍇ
     */
    public List<JavelinRecord> findJavelinStaticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<JavelinRecord> result = new ArrayList<JavelinRecord>();

        // �f�[�^�x�[�X����l���擾����
        List<ReportItemValue> maxCallTreeNodeValues;
        List<ReportItemValue> convertedValues;
        List<ReportItemValue> excludedValues;
        List<ReportItemValue> executedValues;
        List<ReportItemValue> allNodeCountValues;
        List<ReportItemValue> callTreeCountValues;
        List<ReportItemValue> coverageValues;
        List<ReportItemValue> eventValues;

        // CallTreeNode �������i�ő�j
        maxCallTreeNodeValues = ReportDao.selectCallTreeAverage(database,
                startTime, endTime, Constants.ITEMNAME_MAX_NODECOUNT);
        allNodeCountValues = ReportDao.selectCallTreeAverage(database,
                startTime, endTime, Constants.ITEMNAME_ALL_NODECOUNT);
        callTreeCountValues = ReportDao.selectCallTreeAverage(database,
                startTime, endTime, Constants.ITEMNAME_CALLTREECOUNT);
        convertedValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_CONVERTEDMETHOD);
        excludedValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_EXCLUDEDMETHOD);
        executedValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_CALLEDMETHODCOUNT);
        eventValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_EVENT_COUNT);

        coverageValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_COVERAGE);

        // �J�o���b�W�̃f�[�^��DB�ɂ���A�擾�ł����ꍇ�́A�����ɕϊ��������s���B
        if (coverageValues != null && 0 < coverageValues.size())
        {
            coverageValues = PercentageDataUtil.reconstitutePercentageData(coverageValues);
        }

        for (int index = 0; index < maxCallTreeNodeValues.size(); index++)
        {
            JavelinRecord record = new JavelinRecord();

            ReportItemValue maxCallTreeNode = maxCallTreeNodeValues.get(index);
            ReportItemValue converted = convertedValues.get(index);
            ReportItemValue excluded = excludedValues.get(index);
            ReportItemValue executed = executedValues.get(index);
            ReportItemValue allNodeCount = allNodeCountValues.get(index);
            ReportItemValue callTreeCount = callTreeCountValues.get(index);
            ReportItemValue eventCount = eventValues.get(index);

            record.setEvent(eventCount.summaryValue.longValue());
            record.setEventMax(eventCount.maxValue.longValue());
            record.setEventMin(eventCount.minValue.longValue());

            if (maxCallTreeNode != null)
            {
                record.setMeasurementTime(maxCallTreeNode.measurementTime);
                record.setCallTreeNodeNumMax(maxCallTreeNode.maxValue.longValue());
                double allNodeCountValue = allNodeCount.summaryValue.longValue();
                double callTreeCountValue = callTreeCount.summaryValue.longValue();
                if (callTreeCountValue == 0.0)
                {
                    callTreeCountValue = Double.MAX_VALUE;
                }
                record.setCallTreeNodeNumAverage(allNodeCountValue
                        / callTreeCountValue);
                record.setJavelinConverterMethodNum(converted.summaryValue.longValue());
                record.setJavelinConverterExcludedMethodNum(excluded.summaryValue.longValue());
                record.setExecutedMethodNum(executed.summaryValue.longValue());
                // �J�o���b�W�f�[�^�̏������s���B
                ReportItemValue coverageRecord = new ReportItemValue();
                if (coverageValues != null)
                {
                    ReportItemValue coverage = coverageValues.get(index);
                    record.setCoverage(coverage.summaryValue.doubleValue());
                    record.setCoverageMax(coverage.maxValue.doubleValue());
                    record.setCoverageMin(coverage.minValue.doubleValue());
                }
                else
                {
                    double convertedValue = record.getJavelinConverterMethodNum();
                    if (convertedValue < 1)
                    {
                        convertedValue = 1;
                    }
                    coverageRecord.summaryValue = executed.summaryValue.longValue()
                            / convertedValue * DECIMAL_TO_PERCENT;
                    coverageRecord.maxValue = executed.maxValue.longValue()
                            / convertedValue * DECIMAL_TO_PERCENT;
                    coverageRecord.minValue = executed.minValue.longValue()
                            / convertedValue * DECIMAL_TO_PERCENT;
                    record.setCoverage(coverageRecord.summaryValue.doubleValue());
                    record.setCoverageMax(coverageRecord.maxValue.doubleValue());
                    record.setCoverageMin(coverageRecord.minValue.doubleValue());
                }
            }
            result.add(record);
        }

        return result;
    }

    //	/**
    //	 * ���Ԃ��w�肵�A���̊��ԓ��ł�<br/>
    //	 * �uCallTreeNode �������v�O���t�̃f�[�^���擾����B
    //	 * 
    //	 * @param database
    //	 *            �f�[�^�x�[�X���B
    //	 * @param startTime
    //	 *            ��������(�J�n����)�B
    //	 * @param endTime
    //	 *            ��������(�I������)�B
    //	 * @return �uCallTreeNode �������v�O���t�̃f�[�^�B
    //	 */
    //	public List<CallTreeNodeRecord> findCallTreeNodeByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<CallTreeNodeRecord> result = new ArrayList<CallTreeNodeRecord>();
    //
    //		// �f�[�^�x�[�X����l���擾����
    //		List<MeasurementValueDto> maxCallTreeNodeValues;
    //		List<MeasurementValueDto> aveCallTreeNodeValues;
    //
    //		try
    //		{
    //			// CallTreeNode �������i�ő�j
    //			maxCallTreeNodeValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_MAX_NODECOUNT);
    //			// CallTreeNode �������i���ρj
    //			aveCallTreeNodeValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_NODECOUNT);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ���O���b�Z�[�W�̒�`���s���āA�����ɒǋL���邱�ƁB
    //			LOGGER.log("", exception, new Object[0]);
    //			return null;
    //		}
    //
    //		// �f�[�^�x�[�X����擾�����f�[�^���ACallTreeNodeRecord �̃��X�g�ɕϊ�
    //		for (int index = 0; index < aveCallTreeNodeValues.size(); index++)
    //		{
    //			CallTreeNodeRecord record = new CallTreeNodeRecord();
    ////			MeasurementValueDto maxCallTreeNode = maxCallTreeNodeValues.get(index);
    //			MeasurementValueDto aveCallTreeNode = aveCallTreeNodeValues.get(index);
    //			
    //			record.setMeasurementTime(aveCallTreeNode.measurementTime);
    ////			record.setCallTreeNodeNumMax(maxCallTreeNode.value.longValue());
    //			record.setCallTreeNodeNumAverage(aveCallTreeNode.value.longValue());
    //			
    //			result.add(record);
    //		}
    //		
    //		return result;
    //	}
    //
    //	/**
    //	 * ���Ԃ��w�肵�A���̊��ԓ��ł�<br/>
    //	 * �u�ϊ����\�b�h���v�O���t�̃f�[�^���擾����B
    //	 * 
    //	 * @param database
    //	 *            �f�[�^�x�[�X���B
    //	 * @param startTime
    //	 *            ��������(�J�n����)�B
    //	 * @param endTime
    //	 *            ��������(�I������)�B
    //	 * @return �u�ϊ����\�b�h���v�O���t�̃f�[�^�B
    //	 */
    //	public List<ConvertedMethodNumRecord> findConvertedMethodNumByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<ConvertedMethodNumRecord> result = new ArrayList<ConvertedMethodNumRecord>();
    //
    //		// �f�[�^�x�[�X����l���擾����
    //		List<MeasurementValueDto> convertedValues;
    //		List<MeasurementValueDto> excludedValues;
    //		List<MeasurementValueDto> executedValues;
    //
    //		try
    //		{
    //			// JavelinConverter�ϊ����\�b�h��
    //			convertedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_CONVERTEDMETHOD);
    //			// JavelinConverter�ϊ����O���\�b�h��
    //			excludedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_EXCLUDEDMETHOD);
    //			// ���s���\�b�h��
    //			executedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_CALLEDMETHODCOUNT);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ���O���b�Z�[�W�̒�`���s���āA�����ɒǋL���邱�ƁB
    //			LOGGER.log("", exception, new Object[0]);
    //			return null;
    //		}
    //
    //		// �f�[�^�x�[�X����擾�����f�[�^���AConvertedMethodNumRecord �̃��X�g�ɕϊ�
    //		for (int index = 0; index < convertedValues.size(); index++)
    //		{
    //			ConvertedMethodNumRecord record = new ConvertedMethodNumRecord();
    //			MeasurementValueDto convertedNode = convertedValues.get(index);
    //			MeasurementValueDto excludedNode = excludedValues.get(index);
    //			MeasurementValueDto executedNode = executedValues.get(index);
    //			
    //			record.setMeasurementTime(convertedNode.measurementTime);
    //			record.setJavelinConverterMethodNum(convertedNode.value.longValue());
    //			record.setJavelinConverterExcludedMethodNum(excludedNode.value.longValue());
    //			record.setExecutedMethodNum(executedNode.value.longValue());
    //			
    //			result.add(record);
    //		}
    //		
    //		return result;
    //	}
    //
    //	/**
    //	 * ���Ԃ��w�肵�A���̊��ԓ��ł�<br/>
    //	 * �u�J�o���b�W�v�O���t�̃f�[�^���擾����B
    //	 * 
    //	 * @param database
    //	 *            �f�[�^�x�[�X���B
    //	 * @param startTime
    //	 *            ��������(�J�n����)�B
    //	 * @param endTime
    //	 *            ��������(�I������)�B
    //	 * @return �uHttpSession�̃C���X�^���X���v�O���t�̃f�[�^�B
    //	 */
    //	public List<CoverageRecord> findCoverageByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<CoverageRecord> result =
    //			new ArrayList<CoverageRecord>();
    //
    //		// �f�[�^�x�[�X����l���擾����
    //		List<MeasurementValueDto> coverageRecordValues;
    //
    //		try
    //		{
    //			// �J�o���b�W
    //			coverageRecordValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_HTTPSESSION_NUM);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ���O���b�Z�[�W�̒�`���s���āA�����ɒǋL���邱�ƁB
    //			LOGGER.log("", exception, new Object[0]);
    //			return result;
    //		}
    //
    //		for (int index = 0; index < coverageRecordValues.size(); index++)
    //		{
    //			CoverageRecord record = new CoverageRecord();
    //			MeasurementValueDto httpSessionInstanceNum =
    //				coverageRecordValues.get(index);
    //
    //			record.setMeasurementTime(httpSessionInstanceNum.measurementTime);
    //			record.setCoverage(httpSessionInstanceNum.value.longValue());
    //
    //			result.add(record);
    //		}
    //
    //		return result;
    //	}

}
