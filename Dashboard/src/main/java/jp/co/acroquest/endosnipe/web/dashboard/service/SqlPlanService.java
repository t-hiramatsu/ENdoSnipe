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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.dao.SqlPlanDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SqlPlanDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SqlPlan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SQL実行計画機能のサービスクラス。
 * 
 * @author miyasaka
 *
 */
@Service
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
    public SqlPlanDto getSqlPlan(final String itemName)
    {
        List<SqlPlan> sqlPlanList = this.sqlPlanDao.selectByItemName(itemName);

        SqlPlanDto sqlPlanDto = convertSqlPlanDto(sqlPlanList);

        return sqlPlanDto;
    }

    /**
     * SqlPlanオブジェクトのリストを一つのSqlPlanDtoオブジェクトに変換する。
     * 
     * @param sqlPlanList SqlPlanオブジェクトのリスト
     * @return SqlPlanDtoオブジェクト
     */
    private SqlPlanDto convertSqlPlanDto(final List<SqlPlan> sqlPlanList)
    {
        SqlPlanDto sqlPlanDto = new SqlPlanDto();

        if (sqlPlanList == null || sqlPlanList.size() == 0)
        {
            return sqlPlanDto;
        }
        // 項目名、SQL文、スタックとレースは、リスト内のどのIndexにも同じ値が入っているので、
        // 代表としてIndex=0の値を取得しSqlPlanDtoオブジェクトに設定する
        SqlPlan sqlPlanFirstIndex = sqlPlanList.get(0);
        if (sqlPlanFirstIndex == null)
        {
            return sqlPlanDto;
        }
        sqlPlanDto.setMeasurementItemName(sqlPlanFirstIndex.measurementItemName);
        sqlPlanDto.setSqlStatement(sqlPlanFirstIndex.sqlStatement);
        sqlPlanDto.setStackTrace(sqlPlanFirstIndex.stackTrace);

        // SQL実行計画、SQL実行計画の取得時間は、リストの各Indexで違う値が入っている可能性があるので、
        // リストにして、SqlPlanDtoオブジェクトに設定する
        List<String> executionPlanList = new ArrayList<String>();
        List<Timestamp> gettingPlanTimeList = new ArrayList<Timestamp>();

        for (SqlPlan sqlPlan : sqlPlanList)
        {
            if (sqlPlan == null)
            {
                return new SqlPlanDto();
            }
            executionPlanList.add(sqlPlan.executionPlan);
            gettingPlanTimeList.add(sqlPlan.gettingPlanTime);
        }

        sqlPlanDto.setExecutionPlanList(executionPlanList);
        sqlPlanDto.setGettingPlanTimeList(gettingPlanTimeList);

        return sqlPlanDto;
    }
}
