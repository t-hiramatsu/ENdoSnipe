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
package jp.co.acroquest.endosnipe.web.dashboard.dto;

import java.util.List;

/**
 * SQL実行計画のDtoクラス。
 * 
 * @author miyasaka
 *
 */
public class SqlPlanDto
{

    /** 計測名。 */
    private String measurementItemName_;

    /** SQL文。 */
    private String sqlStatement_;

    /** SQLの実行計画(gettingPlanTimeList_のindex番号と関連している)。 */
    private List<String> executionPlanList_;

    /** 実行計画が取得できた時間(executionPlanList_のindex番号と関連している)。 */
    private List<String> gettingPlanTimeList_;

    /** スタックトレース。 */
    private List<String> stackTraceList_;

    /**
     * コンストラクタ。
     */
    public SqlPlanDto()
    {
        // Do nothing
    }

    /**
     * 計測名を取得する。
     * 
     * @return 計測名
     */
    public String getMeasurementItemName()
    {
        return measurementItemName_;
    }

    /**
     * 計測名を設定する。
     * 
     * @param measurementItemName 計測名
     */
    public void setMeasurementItemName(final String measurementItemName)
    {
        measurementItemName_ = measurementItemName;
    }

    /**
     * SQL文を取得する。
     *  
     * @return SQL文
     */
    public String getSqlStatement()
    {
        return sqlStatement_;
    }

    /**
     * SQL文を設定する。
     * 
     * @param sqlStatement SQL文
     */
    public void setSqlStatement(final String sqlStatement)
    {
        sqlStatement_ = sqlStatement;
    }

    /**
     * SQLの実行計画のリストを取得する。
     * 
     * @return SQLの実行計画
     */
    public List<String> getExecutionPlanList()
    {
        return executionPlanList_;
    }

    /**
     * SQLの実行計画のリストを設定する。
     * 
     * @param executionPlanList SQLの実行計画のリスト
     */
    public void setExecutionPlanList(final List<String> executionPlanList)
    {
        executionPlanList_ = executionPlanList;
    }

    /**
     * 実行計画が取得できた時間のリストを取得する。
     * 
     * @return 実行計画が取得できた時間のリスト
     */
    public List<String> getGettingPlanTimeList()
    {
        return gettingPlanTimeList_;
    }

    /**
     * 実行計画が取得できた時間のリストを設定する。
     * 
     * @param gettingPlanTimeList 実行計画が取得できた時間のリスト
     */
    public void setGettingPlanTimeList(final List<String> gettingPlanTimeList)
    {
        gettingPlanTimeList_ = gettingPlanTimeList;
    }

    /**
     * スタックトレースのリストを取得する。
     * 
     * @return スタックトレースのリスト
     */
    public List<String> getStackTraceList()
    {
        return stackTraceList_;
    }

    /**
     * スタックトレースのリストを設定する。
     * 
     * @param stackTraceList スタックトレースのリスト
     */
    public void setStackTraceList(final List<String> stackTraceList)
    {
        stackTraceList_ = stackTraceList;
    }

}
