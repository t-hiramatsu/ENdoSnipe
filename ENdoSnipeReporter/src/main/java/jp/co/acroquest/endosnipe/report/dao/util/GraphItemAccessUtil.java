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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;
import jp.co.acroquest.endosnipe.report.entity.ItemData;
import jp.co.acroquest.endosnipe.report.entity.ItemRecord;
import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.Constants;

/**
 * 複数系列のグラフ情報を、<br/>
 * 取得するクラス。
 * 
 * @author ochiai
 */
public class GraphItemAccessUtil
{
	/**
	 * コンストラクタ。
	 * インスタンス生成を防止するため、privateとする。
	 */
	private GraphItemAccessUtil()
	{
	    // Do nothing.
	}
	
    /**
     * グラフ名を元に、系列名ごとの値リストを返す。
     * 
     * @param database データベース名
     * @param graphName グラフ名
     * @param operator 圧縮方法
     * @param startTime 検索条件(開始時刻)
     * @param endTime 検索条件(終了時刻)
     * @return 「List」グラフのデータ
     * @throws SQLException データ取得時に例外が発生した場合
     */
    public static List<ItemData> findItemData(String database, String graphName,
            CompressOperator operator, Timestamp startTime, Timestamp endTime)
            throws SQLException
    {
		Map<String, List<ReportItemValue>> reportItemMap;
		if (operator == CompressOperator.TOTAL)
		{
			reportItemMap = ReportDao.selectSumMap(database, startTime, endTime, graphName);
		}
		else
		{
			reportItemMap = ReportDao.selectAverageMap(database, startTime, endTime, graphName);
		}
		
		return convertToItemDataList(operator, reportItemMap);
	}

	public static List<ItemData> convertToItemDataList(
			CompressOperator operator,
			Map<String, List<ReportItemValue>> reportItemMap) {
		List<ItemData> result = new ArrayList<ItemData>();
		for(Map.Entry<String, List<ReportItemValue>> entry: reportItemMap.entrySet())
		{
			List<ReportItemValue> list = entry.getValue();
			List<ItemRecord> records = new ArrayList<ItemRecord>();
			for(ReportItemValue itemValue: list)
			{
				ItemRecord itemRecord = new ItemRecord();
				itemRecord.setMeasurementTime(itemValue.measurementTime);
				itemRecord.setValue(itemValue.summaryValue.longValue());
				itemRecord.setValueMax(itemValue.maxValue.longValue());
				itemRecord.setValueMin(itemValue.minValue.longValue());
				
				records.add(itemRecord);
			}
			
			String itemName = entry.getKey();
			ItemData itemData = new ItemData();
			itemData.setItemName(itemName);
			itemData.setOperator(operator);
			itemData.setRecords(records);
			
			result.add(itemData);
		}
			
		return result;
	}

	/**
	 * グラフ名を元に、系列名ごとの値リストを返す。
	 * 
	 * @param database データベース名
	 * @param graphName グラフ名
	 * @param operator 圧縮方法
	 * @param startTime 検索条件(開始時刻)
	 * @param endTime 検索条件(終了時刻)
	 * @param tatData 
	 * @return 「List」グラフのデータ
     * @throws SQLException データ取得時に例外が発生した場合
	 */
    public static List<ItemData> findExceptionData(String database, CompressOperator operator,
            Timestamp startTime, Timestamp endTime, List<ItemData> tatData) throws SQLException
    {
        Map<String, List<ReportItemValue>> exceptionCountData;
        exceptionCountData = ReportDao.selectExceptionSumMap(database, startTime, endTime,
                Constants.ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT);

		long startMillis = startTime.getTime();
		long endMillis = endTime.getTime();
		for (ItemData tatItem : tatData)
		{
			String itemName = tatItem.getItemName();
			if(exceptionCountData.containsKey(itemName) == false)
			{
				ArrayList<ReportItemValue> list = new ArrayList<ReportItemValue>();
				for(int index = 0; index < ReportDao.ITEM_COUNT; index++)
				{
					ReportItemValue reportItemValue = new ReportItemValue();
					reportItemValue.itemName = itemName;
					reportItemValue.measurementTime = new Timestamp(startMillis
							+ (endMillis - startMillis)
							/ ReportDao.ITEM_COUNT
							* index);
					reportItemValue.summaryValue = 0;
					reportItemValue.maxValue = 0;
					reportItemValue.minValue = 0;
					list.add(reportItemValue);
				}

                exceptionCountData.put(itemName, list);
			}
			else
			{
                List<ReportItemValue> exceptionValue = exceptionCountData.get(itemName);
				exceptionCountData.remove(itemName);
				exceptionCountData.put(itemName, exceptionValue);
			}
		}
		return convertToItemDataList(operator, exceptionCountData);
	}

    /**
     * グラフ名を元に、系列名ごとの値リストを返す。
     * 
     * @param database データベース名
     * @param graphName グラフ名
     * @param operator 圧縮方法
     * @param startTime 検索条件(開始時刻)
     * @param endTime 検索条件(終了時刻)
     * @param tatData 
     * @return 「List」グラフのデータ
     * @throws SQLException データ取得時に例外が発生した場合
     */
    public static List<ItemData> findStallData(String database, CompressOperator operator,
            Timestamp startTime, Timestamp endTime, List<ItemData> tatData) throws SQLException
    {
        Map<String, List<ReportItemValue>> stallCountData;
        stallCountData = ReportDao.selectStallSumMap(database, startTime, endTime,
                Constants.ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT);

        long startMillis = startTime.getTime();
        long endMillis = endTime.getTime();
        for (ItemData tatItem : tatData)
        {
            String itemName = tatItem.getItemName();
            if(stallCountData.containsKey(itemName) == false)
            {
                ArrayList<ReportItemValue> list = new ArrayList<ReportItemValue>();
                for(int index = 0; index < ReportDao.ITEM_COUNT; index++)
                {
                    ReportItemValue reportItemValue = new ReportItemValue();
                    reportItemValue.itemName = itemName;
                    reportItemValue.measurementTime = new Timestamp(startMillis
                            + (endMillis - startMillis)
                            / ReportDao.ITEM_COUNT
                            * index);
                    reportItemValue.summaryValue = tatItem.getRecords().get(index).getValue();
                    reportItemValue.maxValue = tatItem.getRecords().get(index).getValueMax();
                    reportItemValue.minValue = tatItem.getRecords().get(index).getValueMin();
                    list.add(reportItemValue);
                }

                stallCountData.put(itemName, list);
            }
            else
            {
                List<ReportItemValue> exceptionValue = stallCountData.get(itemName);
                stallCountData.remove(itemName);
                stallCountData.put(itemName, exceptionValue);
            }
        }
        return convertToItemDataList(operator, stallCountData);
    }

    /**
	 * グラフ名を元に、系列名ごとの値リストを返す。
	 * 
	 * @param database データベース名
	 * @param graphName グラフ名
	 * @param operator 圧縮方法
	 * @param startTime 検索条件(開始時刻)
	 * @param endTime 検索条件(終了時刻)
	 * @return 「List」グラフのデータ
     * @throws SQLException データ取得時に例外が発生した場合
	 */
    public static Map<String, List<ReportItemValue>> findItemDataMap(String database,
            String graphName, CompressOperator operator, Timestamp startTime, Timestamp endTime)
            throws SQLException
    {
        Map<String, List<ReportItemValue>> result;
        if (operator == CompressOperator.TOTAL)
        {
            result = ReportDao.selectSumMap(database, startTime, endTime, graphName);
        }
        else
        {
            result = ReportDao.selectAverageMap(database, startTime, endTime, graphName);
        }

		return result;
	}

}
