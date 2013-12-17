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

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.VmStatusRecord;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * VM状態計測データのレポート出力情報を取得するアクセサクラス。
 * 
 * @author akiba
 */
public class VmStatusRecordAccessor
{
	/**
	 * 期間を指定し、その期間内でのVM状態のレポートデータを取得する。<br/>
	 * 取得するデータは以下の通り。<br/>
	 * <ul>
	 * 	<li>スレッド数</li>
	 *  <li>GC停止時間</li>
	 *  <li>VMスループット</li>
	 *  <li>ファイナライズ待ちオブジェクト数</li>
	 *  <li>クラスロード数</li>
	 * </ul>
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return VM状態のレポートデータ。
	 * @throws SQLException データ取得時に例外が発生した場合
	 */
	public List<VmStatusRecord> findVmStatusStaticsByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<VmStatusRecord> result = new ArrayList<VmStatusRecord>();

		// データベースから値を取得する
		List<ReportItemValue> nativeThreadNumValues;
		List<ReportItemValue> threadNumValues;
		List<ReportItemValue> gcStopTimeValues;
		List<ReportItemValue> vmThroughputValues;
		List<ReportItemValue> finalizeObjNumValues;
		List<ReportItemValue> totalLoadedClassNumValues;
		List<ReportItemValue> loadedClassNumValues;

		// スレッド数(Native)
		nativeThreadNumValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_PROCESS_THREAD_TOTAL_COUNT);
		// スレッド数(Java)
		threadNumValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT);
		gcStopTimeValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL);
		vmThroughputValues = selectVMThroughput(database, startTime, endTime);
		finalizeObjNumValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT);
		// ロードされたクラスの合計数
		totalLoadedClassNumValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL);
		// 現在ロードされているクラスの数
		loadedClassNumValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT);

		for (int index = 0; index < threadNumValues.size(); index++)
		{
			VmStatusRecord record = new VmStatusRecord();

			ReportItemValue nativeThreadNum = nativeThreadNumValues.get(index);
			ReportItemValue threadNum = threadNumValues.get(index);
			ReportItemValue gcStopTime = gcStopTimeValues.get(index);
			ReportItemValue vmThroughput = vmThroughputValues.get(index);
			ReportItemValue finalizeObjNum = finalizeObjNumValues.get(index);
			ReportItemValue totalLoadedClassNum = totalLoadedClassNumValues.get(index);
			ReportItemValue loadedClassNum = loadedClassNumValues.get(index);

			if (threadNum != null)
			{
				record.setMeasurementTime(threadNum.measurementTime);
				record.setNativeThreadNum(nativeThreadNum.summaryValue.doubleValue());
				record.setNativeThreadNumMax(nativeThreadNum.maxValue.doubleValue());
				record.setNativeThreadNumMin(nativeThreadNum.minValue.doubleValue());
				record.setThreadNum(threadNum.summaryValue.doubleValue());
				record.setThreadNumMax(threadNum.maxValue.doubleValue());
				record.setThreadNumMin(threadNum.minValue.doubleValue());
				record.setGcStopTime(gcStopTime.summaryValue.doubleValue());
				record.setGcStopTimeMax(gcStopTime.maxValue.doubleValue());
				record.setGcStopTimeMin(gcStopTime.minValue.doubleValue());
				record.setVmThroughput(vmThroughput.summaryValue.doubleValue());
				record.setVmThroughputMax(vmThroughput.maxValue.doubleValue());
				record.setVmThroughputMin(vmThroughput.minValue.doubleValue());
				record.setFinalizeObjNum(finalizeObjNum.summaryValue.doubleValue());
				record.setFinalizeObjNumMax(finalizeObjNum.maxValue.doubleValue());
				record.setFinalizeObjNumMin(finalizeObjNum.minValue.doubleValue());
				record.setTotalLoadedClassNum(totalLoadedClassNum.summaryValue.doubleValue());
				record.setTotalLoadedClassNumMax(totalLoadedClassNum.maxValue.doubleValue());
				record.setTotalLoadedClassNumMin(totalLoadedClassNum.minValue.doubleValue());
				record.setLoadedClassNum(loadedClassNum.summaryValue.doubleValue());
				record.setLoadedClassNumMax(loadedClassNum.maxValue.doubleValue());
				record.setLoadedClassNumMin(loadedClassNum.minValue.doubleValue());
			}

			result.add(record);
		}

		return result;
	}

	/**
	 * DBからGC停止時間を取得し、VMスループットを割り出す。
	 * 
	 * @param database  データベース。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime   検索条件(終了時刻)。
	 * @return VMスループットのリスト。
	 * @throws SQLException データベースからの検索時にエラーが発生した場合。
	 */
	private List<ReportItemValue> selectVMThroughput(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<ReportItemValue> gcTotalTimeValues = ReportDao.selectAverage(database, startTime,
			endTime, Constants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL);
		List<ReportItemValue> upTimeValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_JAVAUPTIME);

		List<ReportItemValue> dtoList = new ArrayList<ReportItemValue>();

		for (int index = 0; index < gcTotalTimeValues.size(); index++)
		{
			ReportItemValue gcTotalTimeValue = gcTotalTimeValues.get(index);
			ReportItemValue upTimeValue = upTimeValues.get(index);
			if (gcTotalTimeValue == null)
			{
				continue;
			}
			else
			{
				double gcTotalTime = gcTotalTimeValue.summaryValue.doubleValue();
				long uptime = upTimeValue.summaryValue.longValue();
				double vmThroughput;
				if (0 < uptime)
				{
					vmThroughput = (1 - gcTotalTime / uptime) * 100;
				}
				else
				{
					vmThroughput = 100.0;
				}

				ReportItemValue dto = new ReportItemValue();
				dto.measurementTime = gcTotalTimeValue.measurementTime;
				dto.summaryValue = vmThroughput;
				dto.maxValue = vmThroughput;
				dto.minValue = vmThroughput;

				dtoList.add(dto);
			}
		}

		return dtoList;
	}

}
