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
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;

/**
 * �����n��̃O���t�����A<br/>
 * �擾����N���X�B
 * 
 * @author ochiai
 */
public class GraphItemAccessUtil {
	/**
	 * �R���X�g���N�^�B �C���X�^���X������h�~���邽�߁Aprivate�Ƃ���B
	 */
	private GraphItemAccessUtil() {
		// Do nothing.
	}

	/**
	 * �O���t�������ɁA�n�񖼂��Ƃ̒l���X�g��Ԃ��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param graphName
	 *            �O���t��
	 * @param operator
	 *            ���k���@
	 * @param startTime
	 *            ��������(�J�n����)
	 * @param endTime
	 *            ��������(�I������)
	 * @return �uList�v�O���t�̃f�[�^
	 * @throws SQLException
	 *             �f�[�^�擾���ɗ�O�����������ꍇ
	 */
	public static List<ItemData> findItemData(String database,
			String graphName, CompressOperator operator, Timestamp startTime,
			Timestamp endTime) throws SQLException {
		Map<String, List<ReportItemValue>> reportItemMap;
		if (operator == CompressOperator.TOTAL) {
			reportItemMap = ReportDao.selectSumMap(database, startTime,
					endTime, graphName);
		} else {
			reportItemMap = ReportDao.selectAverageMap(database, startTime,
					endTime, graphName);
		}

		return convertToItemDataList(operator, reportItemMap);
	}

	public static List<ItemData> convertToItemDataList(
			CompressOperator operator,
			Map<String, List<ReportItemValue>> reportItemMap) {
		List<ItemData> result = new ArrayList<ItemData>();
		for (Map.Entry<String, List<ReportItemValue>> entry : reportItemMap
				.entrySet()) {
			List<ReportItemValue> list = entry.getValue();
			List<ItemRecord> records = new ArrayList<ItemRecord>();
			for (ReportItemValue itemValue : list) {
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
	 * �O���t�������ɁA�n�񖼂��Ƃ̒l���X�g��Ԃ��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param graphName
	 *            �O���t��
	 * @param operator
	 *            ���k���@
	 * @param startTime
	 *            ��������(�J�n����)
	 * @param endTime
	 *            ��������(�I������)
	 * @param tatData
	 * @return �uList�v�O���t�̃f�[�^
	 * @throws SQLException
	 *             �f�[�^�擾���ɗ�O�����������ꍇ
	 */
	public static List<ItemData> findExceptionData(String database,
			CompressOperator operator, Timestamp startTime, Timestamp endTime,
			List<ItemData> tatData) throws SQLException {
		Map<String, List<ReportItemValue>> exceptionCountData;
		exceptionCountData = ReportDao.selectExceptionSumMap(database,
				startTime, endTime,
				Constants.ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT);

		long startMillis = startTime.getTime();
		long endMillis = endTime.getTime();
		for (ItemData tatItem : tatData) {
			String itemName = tatItem.getItemName();
			if (exceptionCountData.containsKey(itemName) == false) {
				ArrayList<ReportItemValue> list = new ArrayList<ReportItemValue>();
				for (int index = 0; index < ReportDao.ITEM_COUNT; index++) {
					ReportItemValue reportItemValue = new ReportItemValue();
					reportItemValue.itemName = itemName;
					reportItemValue.measurementTime = new Timestamp(startMillis
							+ (endMillis - startMillis) / ReportDao.ITEM_COUNT
							* index);
					reportItemValue.summaryValue = 0;
					reportItemValue.maxValue = 0;
					reportItemValue.minValue = 0;
					list.add(reportItemValue);
				}

				exceptionCountData.put(itemName, list);
			} else {
				List<ReportItemValue> exceptionValue = exceptionCountData
						.get(itemName);
				exceptionCountData.remove(itemName);
				exceptionCountData.put(itemName, exceptionValue);
			}
		}
		return convertToItemDataList(operator, exceptionCountData);
	}

	/**
	 * �O���t�������ɁA�n�񖼂��Ƃ̒l���X�g��Ԃ��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param graphName
	 *            �O���t��
	 * @param operator
	 *            ���k���@
	 * @param startTime
	 *            ��������(�J�n����)
	 * @param endTime
	 *            ��������(�I������)
	 * @param tatData
	 * @return �uList�v�O���t�̃f�[�^
	 * @throws SQLException
	 *             �f�[�^�擾���ɗ�O�����������ꍇ
	 */
	public static List<ItemData> findStallData(String database,
			CompressOperator operator, Timestamp startTime, Timestamp endTime,
			List<ItemData> tatData) throws SQLException {
		Map<String, List<ReportItemValue>> stallCountData;
		stallCountData = ReportDao.selectStallSumMap(database, startTime,
				endTime, Constants.ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT);

		long startMillis = startTime.getTime();
		long endMillis = endTime.getTime();
		for (ItemData tatItem : tatData) {
			String itemName = tatItem.getItemName();
			if (stallCountData.containsKey(itemName) == false) {
				ArrayList<ReportItemValue> list = new ArrayList<ReportItemValue>();
				for (int index = 0; index < ReportDao.ITEM_COUNT; index++) {
					ReportItemValue reportItemValue = new ReportItemValue();
					reportItemValue.itemName = itemName;
					reportItemValue.measurementTime = new Timestamp(startMillis
							+ (endMillis - startMillis) / ReportDao.ITEM_COUNT
							* index);
					reportItemValue.summaryValue = tatItem.getRecords()
							.get(index).getValue();
					reportItemValue.maxValue = tatItem.getRecords().get(index)
							.getValueMax();
					reportItemValue.minValue = tatItem.getRecords().get(index)
							.getValueMin();
					list.add(reportItemValue);
				}

				stallCountData.put(itemName, list);
			} else {
				List<ReportItemValue> exceptionValue = stallCountData
						.get(itemName);
				stallCountData.remove(itemName);
				stallCountData.put(itemName, exceptionValue);
			}
		}
		return convertToItemDataList(operator, stallCountData);
	}

	/**
	 * �O���t�������ɁA�n�񖼂��Ƃ̒l���X�g��Ԃ��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param graphName
	 *            �O���t��
	 * @param operator
	 *            ���k���@
	 * @param startTime
	 *            ��������(�J�n����)
	 * @param endTime
	 *            ��������(�I������)
	 * @return �uList�v�O���t�̃f�[�^
	 * @throws SQLException
	 *             �f�[�^�擾���ɗ�O�����������ꍇ
	 */
	public static Map<String, List<ReportItemValue>> findItemDataMap(
			String database, String graphName, CompressOperator operator,
			Timestamp startTime, Timestamp endTime) throws SQLException {
		Map<String, List<ReportItemValue>> result;
		if (operator == CompressOperator.TOTAL) {
			result = ReportDao.selectSumMap(database, startTime, endTime,
					graphName);
		} else {
			result = ReportDao.selectAverageMap(database, startTime, endTime,
					graphName);
		}

		return result;
	}

	/**
	 * �����Ɏw�肵��ItemName�z���̌v���Ώۖ��̈ꗗ���擾����B
	 * 
	 * @param parentItemName
	 *            �擾�������v���Ώۖ��̐e�̖��O
	 * @return �v���Ώۖ��̈ꗗ
	 * @throws SQLException
	 *             �f�[�^�擾���ɗ�O�����������ꍇ
	 */
	public static List<String> findChildMeasurementItemName(String database,
			String parentItemName) throws SQLException {
		List<String> measurementItemNameList = JavelinMeasurementItemDao
				.selectItemNameListByParentItemName(database, parentItemName);

		return measurementItemNameList;
	}
}
