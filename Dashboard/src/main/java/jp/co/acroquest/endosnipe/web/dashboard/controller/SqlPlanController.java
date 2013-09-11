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

import jp.co.acroquest.endosnipe.web.dashboard.dto.SqlPlanDto;
import jp.co.acroquest.endosnipe.web.dashboard.service.SqlPlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SQL実行計画機能のコントローラークラス。
 * 
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/sqlPlan")
public class SqlPlanController
{
    /** シグナル定義のサービスクラスのオブジェクト。 */
    @Autowired
    protected SqlPlanService sqlPlanService;

    /**
     * コンストラクタ。
     */
    public SqlPlanController()
    {
        // Do nothing
    }

    /**
     * SQL実行計画を取得する。
     * 
     * @param itemName 項目名
     * @return SQL実行計画
     */
    @RequestMapping(value = "/getSqlPlan", method = RequestMethod.POST)
    @ResponseBody
    public SqlPlanDto getSqlPlanList(@RequestParam(value = "itemName") final String itemName)
    {
        SqlPlanDto sqlPlanDto = this.sqlPlanService.getSqlPlan(itemName);

        return sqlPlanDto;
    }
}
