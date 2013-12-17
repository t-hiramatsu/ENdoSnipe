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
package jp.co.acroquest.endosnipe.report.dao.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;

/**
 * 複数テーブルを結合してデータを取得するためのユーティリティクラス
 * 
 * @author M.Yoshida
 */
public class JoinDataAccessUtil
{
	/**
	 * インスタンス化を防止するためのコンストラクタ
	 */
	private JoinDataAccessUtil()
	{
	}

	/**
	 * データの項目名と計測期間を指定して、該当する平均レスポンスタイムを取得する。。
	 * 
	 * @param database   アクセス対象のデータベース名
	 * @param start      検索条件（開始時刻）
	 * @param end        検索条件（終了時刻）
	 * @param cntSumList 合計実行回数が記録されているデータリスト
	 * @return 条件に合致したデータのリスト。指定した項目名が存在しない場合はnull。条件に合致する計測値が存在しない場合は空リストを返す。
	 * @throws SQLException データベースエラー発生時
	 */
	public static List<MeasurementValueDto> getMearsumentValueAverageList(String database,
		Timestamp start, Timestamp end, List<MeasurementValueDto> cntSumList) throws SQLException
	{
		List<MeasurementValueDto> aveList = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithName(database, start, end,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);
		List<MeasurementValueDto> cntList = MeasurementValueDao
			.selectByTermAndMeasurementTypeWithName(database, start, end,
				TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);

		Map<String, Map<Long, MeasurementValueDto>> aveMap = classifiedItemMapFromList(aveList);

		Map<String, Map<Long, MeasurementValueDto>> cntMap = classifiedItemMapFromList(cntList);

		List<MeasurementValueDto> resultList = new ArrayList<MeasurementValueDto>();

		for (MeasurementValueDto elem : cntSumList)
		{
			MeasurementValueDto aveElem = new MeasurementValueDto();
			Map<Long, MeasurementValueDto> aveSamplingMap = aveMap.get(elem.measurementItemName);
			Map<Long, MeasurementValueDto> cntSamplingMap = cntMap.get(elem.measurementItemName);

			BigDecimal totalRuntime = new BigDecimal(0);
			for (Long measureNum : aveSamplingMap.keySet())
			{
				BigDecimal averageNum = new BigDecimal(aveSamplingMap.get(measureNum).value);
				BigDecimal cntNum = new BigDecimal(cntSamplingMap.get(measureNum).value);

				totalRuntime = totalRuntime.add(averageNum.multiply(cntNum));
			}

			BigDecimal totalAverage = new BigDecimal(0);

			if (Integer.parseInt(elem.value) != 0)
			{
				totalAverage = totalRuntime.divideToIntegralValue(new BigDecimal(elem.value));
			}
			aveElem.measurementItemId = elem.measurementItemId;
			aveElem.measurementItemName = elem.measurementItemName;
			aveElem.value = totalAverage + "";

			resultList.add(aveElem);
		}

		return resultList;
	}

	/**
	 * データリストから、系列名、計測番号をキーとするマップに変換する。
	 * 
	 * @param dataList データリスト
	 * @return 変換されたマップ
	 */
	private static Map<String, Map<Long, MeasurementValueDto>> classifiedItemMapFromList(
		List<MeasurementValueDto> dataList)
	{
		Map<String, Map<Long, MeasurementValueDto>> resultMap = new HashMap<String, Map<Long, MeasurementValueDto>>();

		for (MeasurementValueDto elem : dataList)
		{
			Map<Long, MeasurementValueDto> elemMap = resultMap.get(elem.measurementItemName);

			if (elemMap == null)
			{
				elemMap = new HashMap<Long, MeasurementValueDto>();
				resultMap.put(elem.measurementItemName, elemMap);
			}

			elemMap.put(elem.measurementTime.getTime(), elem);
		}

		return resultMap;
	}
}
