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

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.dao.SqlPlanDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SqlPlanDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SqlPlan;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * SQL実行計画機能のサービスクラス。
 * 
 * @author miyasaka
 *
 */
public class SqlPlanService
{
    /** SQL実行計画のDaoオブジェクト。 */
    @Autowired
    protected SqlPlanDao sqlPlanDao;

    /**
     * コンストラクタ。
     */
    public SqlPlanService()
    {
        // Do nothing
    }

    /**
     * SQL実行計画のリストを取得する。
     * 
     * @param itemName 項目名
     * @return SQL実行計画のリスト
     */
    public List<SqlPlanDto> getSqlPlanList(final String itemName)
    {
        List<SqlPlanDto> sqlPlanDtoList = new ArrayList<SqlPlanDto>();

        List<SqlPlan> sqlPlanList = this.sqlPlanDao.selectByItemName(itemName);

        for (SqlPlan sqlPlan : sqlPlanList)
        {
            SqlPlanDto sqlPlanDto = convertSqlPlanDto(sqlPlan);
            sqlPlanDtoList.add(sqlPlanDto);
        }

        return sqlPlanDtoList;
    }

    /**
     * SqlPlanオブジェクトをSqlPlanDtoオブジェクトに変換する。
     * 
     * @param sqlPlan SqlPlanオブジェクト
     * @return SqlPlanDtoオブジェクト
     */
    private SqlPlanDto convertSqlPlanDto(final SqlPlan sqlPlan)
    {
        SqlPlanDto sqlPlanDto = new SqlPlanDto();

        sqlPlanDto.setMeasurementItemName(sqlPlan.measurementItemName);
        sqlPlanDto.setSqlStatement(sqlPlan.sqlStatement);
        sqlPlanDto.setExecutionPlan(sqlPlan.executionPlan);
        sqlPlanDto.setGettingPlanTime(sqlPlan.gettingPlanTime);
        sqlPlanDto.setStackTrace(sqlPlan.stackTrace);

        return sqlPlanDto;
    }
}
