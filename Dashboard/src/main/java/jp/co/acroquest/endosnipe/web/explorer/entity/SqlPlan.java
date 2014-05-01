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
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.sql.Timestamp;

/**
 * SQL_PLANテーブルに対するEntityクラスです。
 * 
 * @author miyasaka
 *
 */
public class SqlPlan
{
    /** 計測名。 */
    public String measurementItemName;

    /** SQL文。 */
    public String sqlStatement;

    /** SQLの実行計画。 */
    public String executionPlan;

    /** 実行計画が取得できた時間。 */
    public Timestamp gettingPlanTime;

    /** スタックトレース。 */
    public String stackTrace;

    /**
     * コンストラクタ。
     */
    public SqlPlan()
    {
        // Do nothing.
    }
}