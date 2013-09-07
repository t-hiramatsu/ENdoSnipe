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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.web.dashboard.dao.SqlPlanDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SqlPlanDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SqlPlan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blanco.commons.sql.format.BlancoSqlFormatter;
import blanco.commons.sql.format.BlancoSqlFormatterException;
import blanco.commons.sql.format.BlancoSqlRule;

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

        String formattedSqlStatement = this.formatTextSQL(sqlPlanFirstIndex.sqlStatement, null);
        sqlPlanDto.setSqlStatement(formattedSqlStatement);
        sqlPlanDto.setMeasurementItemName(sqlPlanFirstIndex.measurementItemName);

        String stackTrace = this.formatStackTrace(sqlPlanFirstIndex.stackTrace);
        sqlPlanDto.setStackTrace(stackTrace);

        // SQL実行計画、SQL実行計画の取得時間は、リストの各Indexで違う値が入っている可能性があるので、
        // リストにして、SqlPlanDtoオブジェクトに設定する
        List<String> executionPlanList = new ArrayList<String>();
        List<String> gettingPlanTimeList = new ArrayList<String>();

        for (SqlPlan sqlPlan : sqlPlanList)
        {
            if (sqlPlan == null)
            {
                return new SqlPlanDto();
            }

            String timeStr =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sqlPlan.gettingPlanTime);

            executionPlanList.add(sqlPlan.executionPlan);
            gettingPlanTimeList.add(timeStr);
        }

        sqlPlanDto.setExecutionPlanList(executionPlanList);
        sqlPlanDto.setGettingPlanTimeList(gettingPlanTimeList);

        return sqlPlanDto;
    }

    /**
     * SQL文をHTML表示用に整形する。
     * 
     * @param text SQL文
     * @param sqlName SQL名
     * @return 整形されたSQL文
     */
    private String formatTextSQL(final String text, final String sqlName)
    {
        BlancoSqlFormatter formatter = new BlancoSqlFormatter(new BlancoSqlRule());

        String formattedSql;
        try
        {
            formattedSql = formatter.format(text);
            formattedSql = formattedSql.replace("\r\n", "<br>");
            formattedSql = formattedSql.replace("\n", "<br>");
            formattedSql = formattedSql.replace("\r", "<br>");

            // 半角スペース2つを全角スペース2つに変更する
            Pattern pattern = Pattern.compile("  ");
            Matcher matcher = pattern.matcher(formattedSql);
            formattedSql = matcher.replaceAll("　　");
        }
        catch (BlancoSqlFormatterException ex)
        {
            formattedSql = text;
        }

        return formattedSql;
    }

    /**
     * スタックトレースをHTML表示用に整形する。
     * 
     * @param stackTrace スタックトレース
     * @return 整形後のスタックトレース
     */
    private String formatStackTrace(final String stackTrace)
    {
        String formattedStackTrace = stackTrace.replace("\r\n", "<br>");
        formattedStackTrace = stackTrace.replace("\n", "<br>");
        formattedStackTrace = stackTrace.replace("\r", "<br>");

        return formattedStackTrace;
    }
}
