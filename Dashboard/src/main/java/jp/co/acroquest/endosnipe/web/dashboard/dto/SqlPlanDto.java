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

import java.sql.Timestamp;

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

    /** SQLの実行計画。 */
    private String executionPlan_;

    /** 実行計画が取得できた時間。 */
    private Timestamp gettingPlanTime_;

    /** スタックトレース。 */
    private String stackTrace_;

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
     * SQLの実行計画を取得する。
     * 
     * @return SQLの実行計画
     */
    public String getExecutionPlan()
    {
        return executionPlan_;
    }

    /**
     * SQLの実行計画を設定する。
     * 
     * @param executionPlan SQLの実行計画
     */
    public void setExecutionPlan(final String executionPlan)
    {
        executionPlan_ = executionPlan;
    }

    /**
     * 実行計画が取得できた時間を取得する。
     * 
     * @return 実行計画が取得できた時間
     */
    public Timestamp getGettingPlanTime()
    {
        return gettingPlanTime_;
    }

    /**
     * 実行計画が取得できた時間を設定する。
     * 
     * @param gettingPlanTime 実行計画が取得できた時間
     */
    public void setGettingPlanTime(final Timestamp gettingPlanTime)
    {
        gettingPlanTime_ = gettingPlanTime;
    }

    /**
     * スタックトレースを取得する。
     * 
     * @return スタックトレース
     */
    public String getStackTrace()
    {
        return stackTrace_;
    }

    /**
     * スタックトレースを設定する。
     * 
     * @param stackTrace スタックトレース
     */
    public void setStackTrace(final String stackTrace)
    {
        stackTrace_ = stackTrace;
    }

}
