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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.Reporter;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dao.ReportDefinitionDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * レポート出力機能のサービスクラス。
 * 
 * @author miyasaka
 *
 */
@Service
public class ReportService
{
    /** レポートの出力先ディレクトリ名。 */
    private static final String REPORT_PATH = "report";

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /** レポート情報Dao */
    @Autowired
    protected ReportDefinitionDao reportDefinitionDao;

    /** 日付のフォーマット。 */
    private static final String DATE_FORMAT = "yyyy-mm-dd hh:MM:ss";

    /** バッファのサイズ */
    private static final int BUFFER_SIZE = 1024;

    /** ファイルのセパレータ */
    private static final String FOLDER_SEPARATOR = File.separator;

    /**
     * デフォルトコンストラクタ。
     */
    public ReportService()
    {

    }

    /**
     * すべてのシグナルデータを返す。
     * 
     * @return シグナルデータ一覧
     */
    public List<ReportDefinitionDto> getAllReport()
    {
        List<ReportDefinition> reportList = null;
        try
        {
            reportList = reportDefinitionDao.selectAll();
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());

            return new ArrayList<ReportDefinitionDto>();
        }

        List<ReportDefinitionDto> definitionDtoList = new ArrayList<ReportDefinitionDto>();

        for (ReportDefinition reportDefinitioin : reportList)
        {
            ReportDefinitionDto reportDto = this.convertReportDifinitionDto(reportDefinitioin);
            definitionDtoList.add(reportDto);
        }

        return definitionDtoList;
    }

    /**
     * 指定したレポート名をキーに、レポート定義の一覧を取得する。
     * 
     * @param reportName レポート名
     * @return レポート定義の一覧
     */
    public List<ReportDefinitionDto> getReportByReportName(final String reportName)
    {
        List<ReportDefinitionDto> reportDefinitionDtos = new ArrayList<ReportDefinitionDto>();
        List<ReportDefinition> definitionDtos = reportDefinitionDao.selectByName(reportName);

        int definitionDtosLength = definitionDtos.size();

        for (int index = 0; index < definitionDtosLength; index++)
        {
            ReportDefinition definition = definitionDtos.get(index);
            ReportDefinitionDto definitionDto = this.convertReportDifinitionDto(definition);
            reportDefinitionDtos.add(definitionDto);
        }

        return reportDefinitionDtos;
    }

    /**
     * 指定したレポートIDをキーに、レポート定義を取得する。
     * 
     * @param reportId レポートID
     * @return レポート定義
     */
    public ReportDefinitionDto getReportByReportId(final int reportId)
    {
        ReportDefinition reportDefinition = reportDefinitionDao.selectById(reportId);
        ReportDefinitionDto reportDefinitionDto = this.convertReportDifinitionDto(reportDefinition);

        return reportDefinitionDto;
    }

    /**
     * レポート出力の定義をDBに登録する。
     * 
     * @param reportDefinition レポート出力の定義
     * @return レポート出力の定義
     */
    public ReportDefinitionDto insertReportDefinition(final ReportDefinition reportDefinition)
    {
        int reportId = 0;
        try
        {
            reportDefinitionDao.insert(reportDefinition);
            reportId = reportDefinitionDao.selectSequenceNum(reportDefinition);
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            return new ReportDefinitionDto();
        }

        ReportDefinitionDto reportDefinitionDto = this.convertReportDifinitionDto(reportDefinition);
        reportDefinitionDto.setReportId(reportId);

        return reportDefinitionDto;
    }

    /**
     * 
     * @param reportDefinitionDto レポート出力の定義
     */
    public void createReport(final ReportDefinitionDto reportDefinitionDto)
    {
        Reporter reporter = new Reporter();

        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);
        Calendar fmTime = reportDefinitionDto.getReportTermFrom();
        Calendar toTime = reportDefinitionDto.getReportTermTo();
        String targetItemName = reportDefinitionDto.getTargetMeasurementName();
        String reportNamePath = reportDefinitionDto.getReportName();
        String[] reportNameSplitList = reportNamePath.split("/");
        int reportNameSplitLength = reportNameSplitList.length;
        String reportName = reportNameSplitList[reportNameSplitLength - 1];

        reporter.createReport(dbName, fmTime, toTime, REPORT_PATH, targetItemName, reportName);
    }

    /**
     * ReportDefinitionDtoオブジェクトをReportDefinitionオブジェクトに変換する。
     * 
     * @param definitionDto
     *            ReportDefinitionDtoオブジェクト
     * 
     * @return ReportDefinitionオブジェクト
     */
    public ReportDefinition convertReportDefinition(final ReportDefinitionDto definitionDto)
    {
        ReportDefinition reportDefinition = new ReportDefinition();

        reportDefinition.reportId_ = definitionDto.getReportId();
        reportDefinition.reportName_ = definitionDto.getReportName();
        reportDefinition.targetMeasurementName_ = definitionDto.getTargetMeasurementName();

        Calendar fmTimeCal = definitionDto.getReportTermFrom();
        Calendar toTimeCal = definitionDto.getReportTermTo();

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        reportDefinition.fmTime_ = dateFormat.format(fmTimeCal.getTime());
        reportDefinition.toTime_ = dateFormat.format(toTimeCal.getTime());

        return reportDefinition;
    }

    /**
     * ReportDefinitionオブジェクトをReportDefinitionDtoオブジェクトに変換する。
     * 
     * @param reportDefinition
     *            ReportDefinitionオブジェクト
     * @return ReportDefinitionDtoオブジェクト
     */
    private ReportDefinitionDto convertReportDifinitionDto(final ReportDefinition reportDefinition)
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
            // TODO Logに出力する様にする
            ex.printStackTrace();
            System.out.println("日付のフォーマットが不正です。");
        }

        definitionDto.setReportTermFrom(fmTime);
        definitionDto.setReportTermTo(toTime);

        return definitionDto;
    }

    /**
     * クライアントからのレポート出力ダウンロードを受信するためのサーブレットです。
     * 
     * @param response {@link HttpServletResponse}オブジェクト
     * @param fileName ファイル名
     */
    public void doRequest(final HttpServletResponse response, final String fileName)
    {
        if (fileName == null)
        {
            LOGGER.log(LogMessageCodes.UNKNOWN_REPORT_FILE_NAME);
            return;
        }

        String currentDir = new File(".").getAbsolutePath();
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        String filePath =
                currentDir + FOLDER_SEPARATOR + REPORT_PATH + FOLDER_SEPARATOR + dbName
                        + FOLDER_SEPARATOR + fileName;

        OutputStream outputStream = null;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream buffInputStream = new BufferedInputStream(fileInputStream);

            String nonSpaceFileName = fileName.replaceAll("[ 　]", "");

            response.setHeader("Content-Disposition", "attachment; filename=" + nonSpaceFileName);

            outputStream = response.getOutputStream();

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = fileInputStream.read(buffer, 0, buffer.length)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }

            buffInputStream.close();
        }
        catch (IOException e)
        {
            printOutNotFound(response);
        }
        finally
        {

            if (outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    outputStream = null;
                }
            }
        }
    }

    /**
     * ファイルが存在しないことをクライアント側に表示する。
     * 
     * @param responce {@link HttpServletResponse}オブジェクト
     */
    private void printOutNotFound(final HttpServletResponse responce)
    {
        try
        {
            OutputStream toClient = responce.getOutputStream();
            responce.setContentType("text/html;charset=utf-8");
            toClient.write("File not found".getBytes());
            toClient.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
