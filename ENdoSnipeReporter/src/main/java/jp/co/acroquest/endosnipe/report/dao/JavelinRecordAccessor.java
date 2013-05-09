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
 * Javelin グラフデータをDBから取得するアクセサクラス。
 * 
 * @author akiba
 */
public class JavelinRecordAccessor
{
    private static final double DECIMAL_TO_PERCENT = 100.0;

    /**
     * 期間を指定し、その期間内でのJavelinのレポートデータを取得する。
     * 
     * @param database データベース名。
     * @param startTime 検索条件(開始時刻)。
     * @param endTime 検索条件(終了時刻)。
     * @return Javelinのレポートのデータ。
     * @throws SQLException データ取得時に例外が発生した場合
     */
    public List<JavelinRecord> findJavelinStaticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<JavelinRecord> result = new ArrayList<JavelinRecord>();

        // データベースから値を取得する
        List<ReportItemValue> maxCallTreeNodeValues;
        List<ReportItemValue> convertedValues;
        List<ReportItemValue> excludedValues;
        List<ReportItemValue> executedValues;
        List<ReportItemValue> allNodeCountValues;
        List<ReportItemValue> callTreeCountValues;
        List<ReportItemValue> coverageValues;
        List<ReportItemValue> eventValues;

        // CallTreeNode 生成数（最大）
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

        // カバレッジのデータがDBにあり、取得できた場合は、それらに変換処理を行う。
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
                // カバレッジデータの処理を行う。
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
    //	 * 期間を指定し、その期間内での<br/>
    //	 * 「CallTreeNode 生成数」グラフのデータを取得する。
    //	 * 
    //	 * @param database
    //	 *            データベース名。
    //	 * @param startTime
    //	 *            検索条件(開始時刻)。
    //	 * @param endTime
    //	 *            検索条件(終了時刻)。
    //	 * @return 「CallTreeNode 生成数」グラフのデータ。
    //	 */
    //	public List<CallTreeNodeRecord> findCallTreeNodeByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<CallTreeNodeRecord> result = new ArrayList<CallTreeNodeRecord>();
    //
    //		// データベースから値を取得する
    //		List<MeasurementValueDto> maxCallTreeNodeValues;
    //		List<MeasurementValueDto> aveCallTreeNodeValues;
    //
    //		try
    //		{
    //			// CallTreeNode 生成数（最大）
    //			maxCallTreeNodeValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_MAX_NODECOUNT);
    //			// CallTreeNode 生成数（平均）
    //			aveCallTreeNodeValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_NODECOUNT);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ログメッセージの定義を行って、ここに追記すること。
    //			LOGGER.log("", exception, new Object[0]);
    //			return null;
    //		}
    //
    //		// データベースから取得したデータを、CallTreeNodeRecord のリストに変換
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
    //	 * 期間を指定し、その期間内での<br/>
    //	 * 「変換メソッド数」グラフのデータを取得する。
    //	 * 
    //	 * @param database
    //	 *            データベース名。
    //	 * @param startTime
    //	 *            検索条件(開始時刻)。
    //	 * @param endTime
    //	 *            検索条件(終了時刻)。
    //	 * @return 「変換メソッド数」グラフのデータ。
    //	 */
    //	public List<ConvertedMethodNumRecord> findConvertedMethodNumByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<ConvertedMethodNumRecord> result = new ArrayList<ConvertedMethodNumRecord>();
    //
    //		// データベースから値を取得する
    //		List<MeasurementValueDto> convertedValues;
    //		List<MeasurementValueDto> excludedValues;
    //		List<MeasurementValueDto> executedValues;
    //
    //		try
    //		{
    //			// JavelinConverter変換メソッド数
    //			convertedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_CONVERTEDMETHOD);
    //			// JavelinConverter変換除外メソッド数
    //			excludedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_EXCLUDEDMETHOD);
    //			// 実行メソッド数
    //			executedValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_CALLEDMETHODCOUNT);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ログメッセージの定義を行って、ここに追記すること。
    //			LOGGER.log("", exception, new Object[0]);
    //			return null;
    //		}
    //
    //		// データベースから取得したデータを、ConvertedMethodNumRecord のリストに変換
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
    //	 * 期間を指定し、その期間内での<br/>
    //	 * 「カバレッジ」グラフのデータを取得する。
    //	 * 
    //	 * @param database
    //	 *            データベース名。
    //	 * @param startTime
    //	 *            検索条件(開始時刻)。
    //	 * @param endTime
    //	 *            検索条件(終了時刻)。
    //	 * @return 「HttpSessionのインスタンス数」グラフのデータ。
    //	 */
    //	public List<CoverageRecord> findCoverageByTerm(
    //			String database, Timestamp startTime, Timestamp endTime)
    //			{
    //		List<CoverageRecord> result =
    //			new ArrayList<CoverageRecord>();
    //
    //		// データベースから値を取得する
    //		List<MeasurementValueDto> coverageRecordValues;
    //
    //		try
    //		{
    //			// カバレッジ
    //			coverageRecordValues = MeasurementValueDao
    //					.selectByTermAndMeasurementTypeWithNameOrderByTime(
    //							database, startTime, endTime,
    //							Constants.ITEMNAME_HTTPSESSION_NUM);
    //		}
    //		catch (SQLException exception)
    //		{
    //			// TODO ログメッセージの定義を行って、ここに追記すること。
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
