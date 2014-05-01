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
package jp.co.acroquest.endosnipe.report.controller.processor.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperation;
import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.converter.compressor.SamplingCompressor;
import jp.co.acroquest.endosnipe.report.entity.ItemData;
import jp.co.acroquest.endosnipe.report.entity.ItemRecord;

/**
 * 複数系列のグラフに対して、converter を適用するクラス
 * 
 * @author ochiai
 */
public class ItemConvertUtil
{
	/**
	 * コンストラクタ
	 */
	private ItemConvertUtil()
	{
	}

	/**
	 * 系列データ、期間を指定し、その期間内での<br/>
	 * データを取得する。
	 * 
	 * @param itemData
	 *            系列データ。
	 * @param startTime
	 *            検索条件(開始時刻)。
	 * @param endTime
	 *            検索条件(終了時刻)。
	 * @return 指定したグラフのデータ。
	 */
	public static ItemData convertItemData(ItemData itemData, Timestamp startTime, Timestamp endTime)
	{
		ItemData convertedData = new ItemData();
		List<ItemRecord> convertedRecords = new ArrayList<ItemRecord>();

		// 圧縮前のデータ
		List<ItemRecord> rawRecords = itemData.getRecords();

		SamplingCompressor compresser = new SamplingCompressor();

		List<? extends Object> compressedRecords = null;

		List<CompressOperation> operation = new ArrayList<CompressOperation>();

		// 最大、最小を求める圧縮方法
		operation.add(new CompressOperation("value", itemData.getOperator()));

		try
		{
			compressedRecords = compresser.compressSamplingList(rawRecords, startTime, endTime,
				"measurementTime", operation, ItemRecord.class);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		// ItemRecordの配列に変換する
		for (int index = 0; index < compressedRecords.size(); index++)
		{
			ItemRecord itemRecord = (ItemRecord) compressedRecords.get(index);
			convertedRecords.add(itemRecord);
		}

		convertedData.setItemName(itemData.getItemName());
		convertedData.setRecords(convertedRecords);

		return convertedData;
	}

	/**
	 * 系列データのリスト、期間を指定し、<br/>
	 * その期間内でのデータを取得する。
	 * 
	 * @param itemDataList
	 *            系列データのリスト。
	 * @param startTime
	 *            検索条件(開始時刻)。
	 * @param endTime
	 *            検索条件(終了時刻)。
	 * @return 指定したグラフのデータ。
	 */
	public static List<ItemData> convertItemDataList(List<ItemData> itemDataList,
		Timestamp startTime, Timestamp endTime)
	{
		List<ItemData> result = new ArrayList<ItemData>();
		for (ItemData itemData : itemDataList)
		{
			ItemData convertedData = convertItemData(itemData, startTime, endTime);
			result.add(convertedData);
		}
		return result;
	}

}
