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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.dao.DataIORecordAccessor;
import jp.co.acroquest.endosnipe.report.entity.DataIORecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * �f�[�^���o�͏��̃��|�[�g�𐶐����郌�|�[�g�v���Z�b�T�B
 * 
 * @author akiba
 */
public class DataIOReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            DataIOReportProcessor.class);

	/**
	 * ReportProcessor�𐶐�����B
	 * 
	 * @param type
	 *            ���|�[�g��ʁB
	 */
	public DataIOReportProcessor(ReportType type)
	{
		super(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getReportPlotData(ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer)
	{
		// ���������̎擾
		String database = cond.getDatabases().get(0);
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();

		// DB���猟��
		DataIORecordAccessor accessor = new DataIORecordAccessor();
		List<DataIORecord> data;
		try
		{
		    data = accessor.findDataIOStaticsByTerm(database, startTime, endTime);
		}
		catch (SQLException ex)
		{
		    LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
		}

		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convertPlotData(Object rawData,
			ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer)
	{
	    List<DataIORecord> data = (List<DataIORecord>)rawData;
        return (DataIORecord[])data.toArray(new DataIORecord[data.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void outputReport(Object plotData, ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer)
	{
		if ((plotData instanceof DataIORecord[]) == false)
		{
			return;
		}

		// �o�͂��郌�|�[�g�̎�ނɂ��킹�ăe���v���[�g�̃t�@�C���p�X���擾����
		String templateFilePath;
		try
		{
			templateFilePath = TemplateFileManager.getInstance()
					.getTemplateFile(getReportType());
		}
		catch (IOException exception)
		{
			reportContainer.setHappendedError(exception);
			return;
		}

		// ���|�[�g�o�͂̈��������擾����
		DataIORecord[] records = (DataIORecord[]) plotData;
		String outputFilePath = getOutputFileName();
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();

		// ���|�[�g�o�͂����s����
		RecordReporter<DataIORecord> reporter =
			new RecordReporter<DataIORecord>(getReportType());
		reporter.outputReport(templateFilePath, outputFilePath, records,
				startTime, endTime);
	}
}
