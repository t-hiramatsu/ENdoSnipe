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
package jp.co.acroquest.endosnipe.web.dashboard.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.report.ReportData;
import jp.co.acroquest.endosnipe.report.Reporter;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.service.ReportService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * レポート出力機能のコントローラクラス。
 * 
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController
{
    /** シグナル定義のサービスクラスのオブジェクト。 */
    @Autowired
    protected ReportService reportService;

    /**
     * デフォルトコンストラクタ。
     */
    public ReportController()
    {
        new ReportData();
    }

    /**
     * レポート出力の定義をすべて取得する。
     * 
     * @return 全てのレポート出力の定義
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<ReportDefinitionDto> getAllDefinition()
    {
        List<ReportDefinitionDto> reportDefinitionDtos = new ArrayList<ReportDefinitionDto>();

        reportDefinitionDtos = this.reportService.getAllReport();

        return reportDefinitionDtos;
    }

    /**
     * 指定したレポート対象名をキーに、レポート定義の一覧を取得する。
     * @param reportName 閾値判定の定義を一意に取得するためのシグナル名
     * @return 閾値判定の定義
     */
    @RequestMapping(value = "/getDefinitionByReportName", method = RequestMethod.POST)
    @ResponseBody
    public List<ReportDefinitionDto> getByTarget(
            @RequestParam(value = "reportName") final String reportName)
    {
        List<ReportDefinitionDto> reportDefinitionDtos =
                this.reportService.getReportByReportName(reportName);
        return reportDefinitionDtos;
    }

    /**
     * レポート出力の定義を新規に追加する。
     * 
     * @param reportDefinition
     *            レポート出力定義のJSONデータ
     * @return 追加したレポート出力の定義
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ReportDefinitionDto addReportDefinition(
            @RequestParam(value = "reportDefinition") final String reportDefinition)
    {
        ReportDefinitionDto reportDefinitionDto =
                JSON.decode(reportDefinition, ReportDefinitionDto.class);

        // レポートを生成する
        this.reportService.createReport(reportDefinitionDto);

        ReportDefinition definition =
                this.reportService.convertReportDefinition(reportDefinitionDto);

        // レポート定義をDBに登録する
        ReportDefinitionDto addedDefinitionDto =
                this.reportService.insertReportDefinition(definition);

        return addedDefinitionDto;
    }

    /**
     * レポートをダウンロードする。
     * 
     * @param res {@link HttpServletResponse}オブジェクト
     * @param reportId レポートID
     * @return ファイル名
     * @throws IOException ファイルへの入出力エラー
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public String downloadReport(final HttpServletResponse res,
            @RequestParam(value = "reportId") final int reportId)
        throws IOException
    {
        ReportDefinitionDto reportDefinitionDto = this.reportService.getReportByReportId(reportId);
        // ファイル名を取得する
        String fileName = createFileNameByDefinitionDto(reportDefinitionDto);

        this.reportService.doRequest(res, fileName);
        return fileName;
    }

    /**
     * レポート名をキーにノードごと削除する。
     *
     * @param reportName
     *            レポート名
     * @return 削除したレポート情報
     */
    @RequestMapping(value = "/deleteByName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteByName(
            @RequestParam(value = "reportName") final String reportName)
    {
        List<ReportDefinitionDto> definitionDtoList =
                this.reportService.getReportByReportName(reportName);

        int definitionDtosLength = definitionDtoList.size();

        for (int index = 0; index < definitionDtosLength; index++)
        {
            ReportDefinitionDto definitionDto = definitionDtoList.get(index);
            String fileName = this.createFileNameByDefinitionDto(definitionDto);
            // ファイルを削除する
            this.reportService.deleteReportFile(fileName);
        }

        this.reportService.deleteReportDefinitionByName(reportName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reportName", reportName);

        return map;
    }

    /**
     * レポートIDをキーにレポート単体を削除する。
     *
     * @param reportId
     *            レポートID
     * @return 削除したレポート情報
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteById(@RequestParam(value = "reportId") final int reportId)
    {
        ReportDefinitionDto reportDefinitionDto = this.reportService.getReportByReportId(reportId);
        // ファイル名を取得する
        String fileName = createFileNameByDefinitionDto(reportDefinitionDto);
        // ファイルを削除する
        boolean isDeleteSuccess = this.reportService.deleteReportFile(fileName);

        Map<String, Object> map = new HashMap<String, Object>();

        if (isDeleteSuccess)
        {
            this.reportService.deleteReportDefinitionById(reportId);
        }

        map.put("reportId", reportId);
        map.put("isSuccess", isDeleteSuccess);

        return map;
    }

    /**
     * レポートIDからファイル名を作成する。
     * 
     * @param reportDefinitionDto レポート出力定義
     * @return ファイル名
     */
    private String createFileNameByDefinitionDto(final ReportDefinitionDto reportDefinitionDto)
    {

        String reportNamePath = reportDefinitionDto.getReportName();
        String[] reportNameSplitList = reportNamePath.split("/");
        int reportNameSplitLength = reportNameSplitList.length;
        String reportName = reportNameSplitList[reportNameSplitLength - 1];

        Calendar fmTime = reportDefinitionDto.getReportTermFrom();
        Calendar toTime = reportDefinitionDto.getReportTermTo();

        SimpleDateFormat format = new SimpleDateFormat(Reporter.TIME_FORMAT);
        String start = format.format(fmTime.getTime());
        String end = format.format(toTime.getTime());

        // ダウンロードするZIPファイル名を作成する
        String fileName = reportName + "_" + start + "-" + end + ".zip";

        return fileName;
    }
}