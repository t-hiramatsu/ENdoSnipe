/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.dashboard.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.ReportData;
import jp.co.acroquest.endosnipe.report.ReportDataQueue;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.service.MapService;

/**
 * SummarySignalのUtilクラスです。
 * @author khine wai, pin
 *
 */
public class ReportUtil
{

    /** log about signal to finish report to put to the queue*/
    private static final String QUEUE_OFFERED = "DEDC0201";

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /** 日付のフォーマット。 */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 
     */
    private ReportUtil()
    {
        // Do Nothing.
    }

    /**
     * レポートを作成する。
     * 
     * @param reportDefinitionDto レポート出力の定義
     */
    public static void createReport(final ReportDefinitionDto reportDefinitionDto)
    {

        ReportData reportData = new ReportData();
        reportData = convertReportData(reportDefinitionDto);
        ReportDataQueue.getInstance().offer(reportData);
        LOGGER.log(QUEUE_OFFERED);

    }

    /**
     * @author khine wai oo
     * ReportDefinitionDtoオブジェクトをReportDataオブジェクトに変換する。
     * 
     * @param definitionDto
     *            ReportDefinitionDtoオブジェクト
     * @return ReportDataオブジェクト
     */

    public static ReportData convertReportData(final ReportDefinitionDto definitionDto)
    {
        ReportData reportData = new ReportData();
        reportData.reportId = definitionDto.getReportId();
        reportData.reportName = definitionDto.getReportName();
        reportData.targetMeasurementName = definitionDto.getTargetMeasurementName();
        Calendar fmTimeCal = definitionDto.getReportTermFrom();
        Calendar toTimeCal = definitionDto.getReportTermTo();
        reportData.reportTermFrom = fmTimeCal;
        reportData.reportTermTo = toTimeCal;
        reportData.status_ = definitionDto.getStatus();
        return reportData;
    }

    /**
     * ReportDefinitionオブジェクトをReportDefinitionDtoオブジェクトに変換する。
     * 
     * @param reportDefinition
     *            ReportDefinitionオブジェクト
     * @return ReportDefinitionDtoオブジェクト
     */
    public static ReportDefinitionDto convertReportDifinitionDto(
            final ReportDefinition reportDefinition)
    {

        ReportDefinitionDto definitionDto = new ReportDefinitionDto();

        definitionDto.setReportId(reportDefinition.reportId_);
        definitionDto.setReportName(reportDefinition.reportName_);
        definitionDto.setTargetMeasurementName(reportDefinition.targetMeasurementName_);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Calendar fmTime = Calendar.getInstance();
        Calendar toTime = Calendar.getInstance();
        try
        {
            fmTime.setTime(dateFormat.parse(reportDefinition.fmTime_));
            toTime.setTime(dateFormat.parse(reportDefinition.toTime_));

        }
        catch (ParseException ex)
        {
            LOGGER.log(LogMessageCodes.UNSUPPORTED_REPORT_FILE_DURATION_FORMAT);
        }

        definitionDto.setReportTermFrom(fmTime);
        definitionDto.setReportTermTo(toTime);
        definitionDto.setStatus(reportDefinition.status_);
        return definitionDto;
    }
}
