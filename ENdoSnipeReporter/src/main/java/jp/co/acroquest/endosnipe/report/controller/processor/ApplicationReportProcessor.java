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
import jp.co.acroquest.endosnipe.report.dao.ApplicationRecordAccessor;
import jp.co.acroquest.endosnipe.report.entity.ApplicationRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * アプリケーション情報のレポートを生成するレポートプロセッサ。
 * 
 * @author akiba
 */
public class ApplicationReportProcessor extends ReportPublishProcessorBase
{
	/** ロガー */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
		.getLogger(ApplicationReportProcessor.class);

	/**
	 * ReportProcessorを生成する。
	 * 
	 * @param type
	 *            レポート種別。
	 */
	public ApplicationReportProcessor(ReportType type)
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
		// 検索条件の取得
		String database = cond.getDatabases().get(0);
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();

		// DBから検索
		ApplicationRecordAccessor accessor = new ApplicationRecordAccessor();
		List<ApplicationRecord> data;
		try
		{
			data = accessor.findApplicationStaticsByTerm(database, startTime, endTime);
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
	protected Object convertPlotData(Object rawData, ReportSearchCondition cond,
		ReportProcessReturnContainer reportContainer)
	{
		List<ApplicationRecord> data = (List<ApplicationRecord>) rawData;
		return (ApplicationRecord[]) data.toArray(new ApplicationRecord[data.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void outputReport(Object plotData, ReportSearchCondition cond,
		ReportProcessReturnContainer reportContainer)
	{
		if ((plotData instanceof ApplicationRecord[]) == false)
		{
			return;
		}

		// 出力するレポートの種類にあわせてテンプレートのファイルパスを取得する
		String templateFilePath;
		try
		{
			templateFilePath = TemplateFileManager.getInstance().getTemplateFile(getReportType());
		}
		catch (IOException exception)
		{
			reportContainer.setHappendedError(exception);
			return;
		}

		// レポート出力の引数情報を取得する
		ApplicationRecord[] records = (ApplicationRecord[]) plotData;
		String outputFilePath = getOutputFileName();
		Timestamp startTime = cond.getStartDate();
		Timestamp endTime = cond.getEndDate();

		// レポート出力を実行する
		RecordReporter<ApplicationRecord> reporter = new RecordReporter<ApplicationRecord>(
			getReportType());
		reporter.outputReport(templateFilePath, outputFilePath, records, startTime, endTime);
	}
}
