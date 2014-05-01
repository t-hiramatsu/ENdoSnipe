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
 * �����n��̃O���t�ɑ΂��āAconverter ��K�p����N���X
 * 
 * @author ochiai
 */
public class ItemConvertUtil
{
	/**
	 * �R���X�g���N�^
	 */
	private ItemConvertUtil()
	{
	}

	/**
	 * �n��f�[�^�A���Ԃ��w�肵�A���̊��ԓ��ł�<br/>
	 * �f�[�^���擾����B
	 * 
	 * @param itemData
	 *            �n��f�[�^�B
	 * @param startTime
	 *            ��������(�J�n����)�B
	 * @param endTime
	 *            ��������(�I������)�B
	 * @return �w�肵���O���t�̃f�[�^�B
	 */
	public static ItemData convertItemData(
			ItemData itemData, Timestamp startTime, Timestamp endTime)
	{
		ItemData convertedData = new ItemData();
		List<ItemRecord> convertedRecords = new ArrayList<ItemRecord>();

		// ���k�O�̃f�[�^
		List<ItemRecord> rawRecords = itemData.getRecords();

		SamplingCompressor compresser = new SamplingCompressor();

		List<? extends Object> compressedRecords = null;

		List<CompressOperation> operation = new ArrayList<CompressOperation>();

		// �ő�A�ŏ������߂鈳�k���@
		operation.add(new CompressOperation("value",
				itemData.getOperator()));

		try
		{
			compressedRecords = compresser.compressSamplingList(
					rawRecords, startTime, endTime,
					"measurementTime", operation, ItemRecord.class);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		// ItemRecord�̔z��ɕϊ�����
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
	 * �n��f�[�^�̃��X�g�A���Ԃ��w�肵�A<br/>
	 * ���̊��ԓ��ł̃f�[�^���擾����B
	 * 
	 * @param itemDataList
	 *            �n��f�[�^�̃��X�g�B
	 * @param startTime
	 *            ��������(�J�n����)�B
	 * @param endTime
	 *            ��������(�I������)�B
	 * @return �w�肵���O���t�̃f�[�^�B
	 */
	public static List<ItemData> convertItemDataList(
			List<ItemData> itemDataList, Timestamp startTime, Timestamp endTime)
	{
		List<ItemData> result = new ArrayList<ItemData>();
		for (ItemData itemData : itemDataList)
		{
			ItemData convertedData =
				convertItemData(itemData, startTime, endTime);
			result.add(convertedData);
		}
		return result;
	}

}
