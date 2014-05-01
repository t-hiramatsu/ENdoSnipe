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
package jp.co.acroquest.endosnipe.report.controller.processor;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.dao.util.GraphItemAccessUtil;
import jp.co.acroquest.endosnipe.report.entity.ItemData;
import jp.co.acroquest.endosnipe.report.entity.ObjectRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * �I�u�W�F�N�g���̃��|�[�g�𐶐����郌�|�[�g�v���Z�b�T�B
 * 
 * @author akiba
 */
public class ObjectReportProcessor extends ReportPublishProcessorBase {
	/** ���K�[ */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
			.getLogger(ObjectReportProcessor.class);

	/** �ώZ�O���t��measurement_item_name�ɑ΂��Č����v���镶����̃��X�g�B */
	private List<String> sumGraphBackwardMatchList = new ArrayList<String>();

	/** �ώZ�O���t��measurement_item_name�ɑ΂��ĕ�����v���鐳�K�\���̃��X�g�B */
	private List<String> sumGraphPartialMatchList = new ArrayList<String>();

	/**
	 * ReportProcessor�𐶐�����B
	 * 
	 * @param type
	 *            ���|�[�g��ʁB
	 */
	public ObjectReportProcessor(ReportType type) {
		super(type);

		// �ώZ�O���t��measurement_item_name�ɑ΂��Č����v���镶��������X�g�ɓo�^����
		sumGraphBackwardMatchList.add("count");
		sumGraphBackwardMatchList.add("(d)");

		// �ώZ�O���t��measurement_item_name�ɑ΂��ĕ�����v���镶��������X�g�ɓo�^����
		sumGraphPartialMatchList.add(".*/response/.*/error/.*");
		sumGraphPartialMatchList.add(".*/response/.*/stalled/.*");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getReportPlotData(ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer) {
		// ���������̎擾
		String database = cond.getDatabases().get(0);
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();
		String parentItemName = cond.getTargetItemName();
		Map<String, List<ItemData>> itemMap = new HashMap<String, List<ItemData>>();

		try {
			List<String> measurementItemNameList = GraphItemAccessUtil
					.findChildMeasurementItemName(database, parentItemName);

			int itemNameListLength = measurementItemNameList.size();

			for (int index = 0; index < itemNameListLength; index++) {
				String measurementItemName = measurementItemNameList.get(index);

				CompressOperator compressOperator;
				
				// �ώZ�T�}�����A���σT�}�����𔻕ʂ��A�K�؂ȃT�}�����@�ŃT�}�������s����
				boolean isTotalSummary = this.judgeTotalSummary(measurementItemName);
				if (isTotalSummary) {
					compressOperator = CompressOperator.TOTAL;
				} else {
					compressOperator = CompressOperator.SIMPLE_AVERAGE;
				}

				List<ItemData> itemDataList = GraphItemAccessUtil.findItemData(
						database, measurementItemName,
						compressOperator, startTime, endTime);

				itemMap.put(measurementItemName, itemDataList);
			}
		} catch (SQLException ex) {
			LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
					ReporterConfigAccessor.getReportName(getReportType()));
			return null;
		}

		return itemMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convertPlotData(Object rawData,
			ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer) {
		// �f�[�^�ϊ��͓��ɍs���܂���B
		return rawData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void outputReport(Object convertedData,
			ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer) {
		// map �̃f�[�^��6�O���t�̌ʂ̃f�[�^�ɕ�����
		Map<String, List<? extends Object>> data = (Map<String, List<? extends Object>>) convertedData;

		// ���|�[�g�o�͂̈��������擾����
		String outputFolderPath = getOutputFolderName();
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();

		// Map����S�ẴL�[�ƒl�̃G���g����Set�^�̃R���N�V�����Ƃ��Ď擾����
		Set<Map.Entry<String, List<? extends Object>>> entrySet = data
				.entrySet();

		// �L�[�ƒl�̃R���N�V�����̔����q���擾����
		Iterator<Entry<String, List<? extends Object>>> it = entrySet
				.iterator();

		// ���̗v�f���܂����݂���ꍇ��true���Ԃ����
		while (it.hasNext()) {
			// �L�[�ƒl���Z�b�g�����AMap.Entry�^�̃I�u�W�F�N�g���擾����
			Entry<String, List<? extends Object>> entry = it.next();

			// Map.Entry�^�̃I�u�W�F�N�g����L�[���擾����
			String key = entry.getKey();

			// �L�[��null�������ꍇ�́A���|�[�g���o�͂��Ȃ��B
			if (key == null) {
				continue;
			}

			// Map.Entry�^�̃I�u�W�F�N�g����l���擾����
			List<ItemData> value = (List<ItemData>) entry.getValue();

			// �o�͂��郌�|�[�g�̎�ނɂ��킹�ăe���v���[�g�̃t�@�C���p�X���擾����
			String templateFilePath;
			try {
				ReportType reportType;

				// �ώZ�T�}�����A���σT�}�����𔻕ʂ���
				boolean isTotalSummary = this.judgeTotalSummary(key);
				if (isTotalSummary) {
					reportType = ReportType.OBJECT_TOTAL;
				} else {
					reportType = ReportType.OBJECT_AVERAGE;
				}

				templateFilePath = TemplateFileManager.getInstance()
						.getTemplateFile(reportType);
			} catch (IOException exception) {
				reportContainer.setHappendedError(exception);
				return;
			}

			// ���|�[�g�o�͂����s����
			RecordReporter<ObjectRecord> reporter = new RecordReporter<ObjectRecord>(
					getReportType());
			reporter.outputReports(templateFilePath, outputFolderPath
					+ File.separator + key, value, startTime, endTime);
		}
	}

	/**
	 * �ώZ�T�}�������鍀�ڂ����f����B<br>
	 * �ώZ�T�}�������鍀�ڂł���ꍇ��true��Ԃ��B
	 * 
	 * @param itemName
	 *            ���ږ�
	 * @return �ώZ�T�}�������鍀�ڂł���ꍇ��true
	 */
	private boolean judgeTotalSummary(String itemName) {
		if (itemName == null) {
			return false;
		}

		// �ώZ�O���t��measurement_item_name�ɑ΂��Č����v���镶������g���āA
		// �ώZ�O���t���ǂ����m�F����B
		for (String backwardMatchStr : sumGraphBackwardMatchList) {
			if (itemName.endsWith(backwardMatchStr)) {
				return true;
			}
		}

		// �ώZ�O���t��measurement_item_name�ɑ΂��ĕ�����v���鐳�K�\�����g���āA
		// �ώZ�O���t���ǂ����m�F����B
		for (String particalMatchStr : sumGraphPartialMatchList) {
			if (itemName.matches(particalMatchStr)) {
				return true;
			}
		}

		return false;
	}
}
