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

import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
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
    protected ReportService reportService_;

    /**
     * デフォルトコンストラクタ。
     */
    public ReportController()
    {

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

        this.reportService_.insertReportDefinition(reportDefinitionDto);

        reportDefinitionDto.setReportId(1);

        return reportDefinitionDto;
    }
}
