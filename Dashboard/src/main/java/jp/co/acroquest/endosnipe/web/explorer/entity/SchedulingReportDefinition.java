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
 *
 * レポート定義テーブルに対するエンティティクラスです。
 * @author khinlay
 * 
 */
public class SchedulingReportDefinition
{
    /** レポート出力定義のID。 */
    public int reportId_;

    /** レポート名。 */
    public String reportName_;

    /** レポート出力の対象の計測対象名。 */
    public String targetMeasurementName_;

    /** radio button */
    public String term_;

    /** to select hour and minute */
    public String time_;

    /** day */
    public String day_;

    /** date */
    public String date_;

    /** plan export time**/
    public Timestamp planExportedTime_;

    /**
     * {@link SignalInfo} オブジェクトを生成します。<br />
     */
    public SchedulingReportDefinition()
    {
        this.reportId_ = -1;
    }

    @Override
    public String toString()
    {
        return String.format("ReportID%d ReportName:%s TargetMeasurementName:%s "
                                     + "Term:%s Time:%s Day:%s Date:%s PlanTime:%s", reportId_,
                             reportName_,
                             targetMeasurementName_, term_, time_, day_, date_, planExportedTime_);
    }
}
