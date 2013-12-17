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

import jp.co.acroquest.endosnipe.report.entity.DataIORecord;
import jp.co.acroquest.endosnipe.report.entity.DataReceiveRecord;
import jp.co.acroquest.endosnipe.report.entity.DataTransmitRecord;
import jp.co.acroquest.endosnipe.report.entity.FileInputRecord;
import jp.co.acroquest.endosnipe.report.entity.FileOutputRecord;
import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * データ入出力情報をDBから取得するアクセサクラス。
 * 
 * @author akiba
 */
public class DataIORecordAccessor
{
	/**
	 * 期間を指定し、その期間内でのデータ入出力のレポートデータを取得する。<br/>
	 * 取得するデータは以下の通り。<br/>
	 * <ul>
	 *  <li>ネットワーク経由でのデータ受信量</li>
	 *  <li>ネットワーク経由でのデータ送信量</li>
	 *  <li>ファイル入力量</li>
	 *  <li>ファイル出力量</li>
	 * </ul>
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return データ入出力のレポートデータ。
	 * @throws SQLException データ取得時に例外が発生した場合
	 */
	public List<DataIORecord> findDataIOStaticsByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<DataIORecord> result = new ArrayList<DataIORecord>();

		// データベースから値を取得する
		List<ReportItemValue> dataReceiveValues;
		List<ReportItemValue> dataTransmitValues;
		List<ReportItemValue> fileInputValues;
		List<ReportItemValue> fileOutputValues;

		dataReceiveValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_NETWORKINPUTSIZEOFPROCESS);
		dataTransmitValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS);
		fileInputValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_FILEINPUTSIZEOFPROCESS);
		fileOutputValues = ReportDao.selectAverage(database, startTime, endTime,
			Constants.ITEMNAME_FILEOUTPUTSIZEOFPROCESS);

		for (int index = 0; index < dataReceiveValues.size(); index++)
		{
			DataIORecord record = new DataIORecord();

			ReportItemValue dataReceive = dataReceiveValues.get(index);
			ReportItemValue dataTransmit = dataTransmitValues.get(index);
			ReportItemValue fileInput = fileInputValues.get(index);
			ReportItemValue fileOutput = fileOutputValues.get(index);

			if (dataReceive != null)
			{
				record.setMeasurementTime(dataReceive.measurementTime);
				record.setDataReceive(dataReceive.summaryValue.longValue());
				record.setDataReceiveMax(dataReceive.maxValue.longValue());
				record.setDataReceiveMin(dataReceive.minValue.longValue());
				record.setDataTransmit(dataTransmit.summaryValue.longValue());
				record.setDataTransmitMax(dataTransmit.maxValue.longValue());
				record.setDataTransmitMin(dataTransmit.minValue.longValue());
				record.setFileInput(fileInput.summaryValue.longValue());
				record.setFileInputMax(fileInput.maxValue.longValue());
				record.setFileInputMin(fileInput.minValue.longValue());
				record.setFileOutput(fileOutput.summaryValue.longValue());
				record.setFileOutputMax(fileOutput.maxValue.longValue());
				record.setFileOutputMin(fileOutput.minValue.longValue());
			}

			result.add(record);
		}

		return result;
	}

	/**
	 * 期間を指定し、その期間内での<br/>
	 * 「データ受信量」グラフのデータを取得する。
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return 「データ受信量」グラフのデータ。
	 * @throws SQLException データ取得時に例外が発生した場合
	 */
	public List<DataReceiveRecord> findDataReceiveByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<DataReceiveRecord> result = new ArrayList<DataReceiveRecord>();

		// データ受信量
		List<MeasurementValueDto> dataReceiveValues = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithNameOrderByTime(database, startTime, endTime,
				Constants.ITEMNAME_NETWORKINPUTSIZEOFPROCESS);

		for (int index = 0; index < dataReceiveValues.size(); index++)
		{
			DataReceiveRecord record = new DataReceiveRecord();
			MeasurementValueDto dataReceive = dataReceiveValues.get(index);

			// データベースに保存されているのが合計値なので、差を保存する
			long previousValue = 0;
			if (index > 0)
			{
				previousValue = Long.valueOf(dataReceiveValues.get(index - 1).value).longValue();
			}

			record.setMeasurementTime(dataReceive.measurementTime);

			long currentValue = Long.valueOf(dataReceive.value).longValue() - previousValue;
			currentValue = Math.max(currentValue, 0);
			record.setDataReceive(currentValue);

			result.add(record);
		}

		return result;
	}

	/**
	 * 期間を指定し、その期間内での<br/>
	 * 「データ送信量」グラフのデータを取得する。
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return 「データ送信量」グラフのデータ。
	 * @throws SQLException データ取得時に例外が発生した場合
	 */
	public List<DataTransmitRecord> findDataTransmitByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<DataTransmitRecord> result = new ArrayList<DataTransmitRecord>();

		// データ送信量
		List<MeasurementValueDto> dataTransmitValues = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithNameOrderByTime(database, startTime, endTime,
				Constants.ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS);

		for (int index = 0; index < dataTransmitValues.size(); index++)
		{
			DataTransmitRecord record = new DataTransmitRecord();
			MeasurementValueDto dataTransmit = dataTransmitValues.get(index);

			record.setMeasurementTime(dataTransmit.measurementTime);
			record.setDataTransmit(Long.valueOf(dataTransmit.value).longValue());

			// データベースに保存されているのが合計値なので、差を保存する
			long previousValue = 0;
			if (index > 0)
			{
				previousValue = Long.valueOf(dataTransmitValues.get(index - 1).value).longValue();
			}

			record.setMeasurementTime(dataTransmit.measurementTime);

			long currentValue = Long.valueOf(dataTransmit.value).longValue() - previousValue;
			currentValue = Math.max(currentValue, 0);
			record.setDataTransmit(currentValue);

			result.add(record);
		}

		return result;
	}

	/**
	 * 期間を指定し、その期間内での<br/>
	 * 「ファイル入力量」グラフのデータを取得する。
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return 「ファイル入力量」グラフのデータ。
	 */
	public List<FileInputRecord> findFileInputByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<FileInputRecord> result = new ArrayList<FileInputRecord>();

		// ファイル入力量
		List<MeasurementValueDto> fileInputValues = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithNameOrderByTime(database, startTime, endTime,
				Constants.ITEMNAME_FILEINPUTSIZEOFPROCESS);

		for (int index = 0; index < fileInputValues.size(); index++)
		{
			FileInputRecord record = new FileInputRecord();
			MeasurementValueDto fileInput = fileInputValues.get(index);

			record.setMeasurementTime(fileInput.measurementTime);
			record.setFileInput(Long.valueOf(fileInput.value).longValue());

			// データベースに保存されているのが合計値なので、差を保存する
			long previousValue = 0;
			if (index > 0)
			{
				previousValue = Long.valueOf(fileInputValues.get(index - 1).value).longValue();
			}

			record.setMeasurementTime(fileInput.measurementTime);

			long currentValue = Long.valueOf(fileInput.value).longValue() - previousValue;
			currentValue = Math.max(currentValue, 0);
			record.setFileInput(currentValue);

			result.add(record);
		}

		return result;
	}

	/**
	 * 期間を指定し、その期間内での<br/>
	 * 「ファイル出力量」グラフのデータを取得する。
	 * 
	 * @param database データベース名。
	 * @param startTime 検索条件(開始時刻)。
	 * @param endTime 検索条件(終了時刻)。
	 * @return 「ファイル出力量」グラフのデータ。
	 * @throws SQLException データ取得時に例外が発生した場合
	 */
	public List<FileOutputRecord> findFileOutputByTerm(String database, Timestamp startTime,
		Timestamp endTime) throws SQLException
	{
		List<FileOutputRecord> result = new ArrayList<FileOutputRecord>();

		// ファイル出力量
		List<MeasurementValueDto> fileOutputValues = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithNameOrderByTime(database, startTime, endTime,
				Constants.ITEMNAME_FILEOUTPUTSIZEOFPROCESS);

		for (int index = 0; index < fileOutputValues.size(); index++)
		{
			FileOutputRecord record = new FileOutputRecord();
			MeasurementValueDto fileOutput = fileOutputValues.get(index);

			record.setMeasurementTime(fileOutput.measurementTime);
			record.setFileOutput(Long.valueOf(fileOutput.value).longValue());

			// データベースに保存されているのが合計値なので、差を保存する
			long previousValue = 0;
			if (index > 0)
			{
				previousValue = Long.valueOf(fileOutputValues.get(index - 1).value).longValue();
			}

			record.setMeasurementTime(fileOutput.measurementTime);

			long currentValue = Long.valueOf(fileOutput.value).longValue() - previousValue;
			currentValue = Math.max(currentValue, 0);
			record.setFileOutput(currentValue);

			result.add(record);
		}

		return result;
	}
}
