/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.dto;

/**
 * ダッシュボードのレポート出力定義のDtoクラス
 * 
 * @author kamo
 *
 */
public class DashboardReportDefinitionDto
{
    /** レポート出力対象のクラスタ名 */
    private String clusterName_;

    /** レポート出力対象のダッシュボード名 */
    private String dashboardName_;

    /** レポート出力名 */
    private String reportName_;

    /** レポート出力期間の開始時刻 */
    private String reportTermFrom_;

    /** レポート出力期間の終了時刻 */
    private String reportTermTo_;

    /**
     * コンストラクタ
     */
    public DashboardReportDefinitionDto()
    {
    }

    /**
     * レポート出力対象のクラスタ名を取得する
     * @return レポート出力対象のクラスタ名
     */
    public String getClusterName()
    {
        return clusterName_;
    }

    /**
     * レポート出力対象のクラスタ名を設定する
     * @param clusterName レポート出力対象のクラスタ名
     */
    public void setClusterName(final String clusterName)
    {
        clusterName_ = clusterName;
    }

    /**
     * レポート出力対象のダッシュボード名を取得する
     * @return レポート出力対象のダッシュボード名
     */
    public String getReportName()
    {
        return reportName_;
    }

    /**
     * レポート出力対象のダッシュボード名を設定する
     * @param dashboardName レポート出力対象のダッシュボード名
     */
    public void setReportName(final String dashboardName)
    {
        reportName_ = dashboardName;
    }

    /**
     * レポート出力期間の開始時刻を取得する
     * @return レポート出力期間の開始時刻
     */
    public String getReportTermFrom()
    {
        return reportTermFrom_;
    }

    /**
     * レポート出力期間の開始時刻を設定する
     * @param reportTermFrom レポート出力期間の開始時刻
     */
    public void setReportTermFrom(final String reportTermFrom)
    {
        reportTermFrom_ = reportTermFrom;
    }

    /**
     * レポート出力期間の終了時刻
     * @return レポート出力期間の終了時刻を取得する
     */
    public String getReportTermTo()
    {
        return reportTermTo_;
    }

    /**
     * レポート出力期間の終了時刻
     * @param reportTermTo レポート出力期間の終了時刻を設定する
     */
    public void setReportTermTo(final String reportTermTo)
    {
        reportTermTo_ = reportTermTo;
    }

    /**
     * レポート出力対象のダッシュボード名を取得する
     * @return レポート出力対象のダッシュボード名
     */
    public String getDashboardName()
    {
        return dashboardName_;
    }

    /**
     * レポート出力対象のダッシュボード名を設定する
     * @param dashboardName レポート出力対象のダッシュボード名
     */
    public void setDashboardName(final String dashboardName)
    {
        dashboardName_ = dashboardName;
    }

    @Override
    public String toString()
    {
        return "DashboardReportDefinitionDto [clusterName_=" + clusterName_ + ", dashboardName_="
                + reportName_ + ", reportTermFrom_=" + reportTermFrom_ + ", reportTermTo_="
                + reportTermTo_ + "]";
    }

}
