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
import java.util.Calendar;

/**
 * レポート出力定義のDtoクラス。
 * @author khinlay
 *
 */
public class SchedulingReportDefinitionDto
{
    /** レポート出力定義のID。 */
    private int reportId_;

    /** レポート名。 */
    private String reportName_;

    /** レポート出力の対象の計測対象名。 */
    private String targetMeasurementName_;

    /** To choose daily, weekly and monthly*/
    private String term_;

    /** day of month*/
    private String day_;

    /** 1 to 31 day*/
    private String date_;

    /** Hour and Minute*/
    private Calendar time_;

    /** HouCalendarMinute*/
    private Timestamp planExportedTime_;

    /**
     * コンストラクタ。
     */
    public SchedulingReportDefinitionDto()
    {

    }

    /**
     * get plan exported time
     * @return plan export time
     */
    public Timestamp getPlanExportedTime()
    {
        return planExportedTime_;
    }

    /**
     * set plan exported time
     * @param planExportedTime plan exported
     */
    public void setPlanExportedTime(final Timestamp planExportedTime)
    {
        planExportedTime_ = planExportedTime;
    }

    /**
     * get report id
     * @return report id
     */
    public int getReportId()
    {
        return reportId_;
    }

    /**
     * set report id
     * @param reportId reportid
     */
    public void setReportId(final int reportId)
    {
        reportId_ = reportId;
    }

    /**
     * レポート名を取得する。
     * 
     * @return レポート名
     */
    public String getReportName()
    {
        return reportName_;
    }

    /**
     * レポート名を設定する。
     * 
     * @param reportName レポート名
     */
    public void setReportName(final String reportName)
    {
        reportName_ = reportName;
    }

    /**
     * レポート出力の対象の計測対象名を取得する。
     * 
     * @return レポート出力の対象の計測対象名
     */
    public String getTargetMeasurementName()
    {
        return targetMeasurementName_;
    }

    /**
     * レポート出力の対象の計測対象名を設定する。
     * 
     * @param targetMeasurementName レポート出力の対象の計測対象名
     */
    public void setTargetMeasurementName(final String targetMeasurementName)
    {
        targetMeasurementName_ = targetMeasurementName;
    }

    /**
     * 
     * @return is used
     */
    public String getTerm()
    {
        return term_;
    }

    /**
     * 
     * @param term is used
     */
    public void setTerm(final String term)
    {
        term_ = term;
    }

    /**
     * 
     * @return is used
     */
    public String getDay()
    {
        return day_;
    }

    /**
     * 
     * @param day is used
     */
    public void setDay(final String day)
    {
        day_ = day;
    }

    /**
     * get date.
     * @return date
     */
    public String getDate()
    {
        return date_;
    }

    /**
     * get calendar time.
     * @return time
     */
    public Calendar getTime()
    {
        return time_;
    }

    /**
     * set calendar time
     * @param time time
     */
    public void setTime(final Calendar time)
    {
        time_ = time;
    }

    /**
     * 
     * @param date is used
     */
    public void setDate(final String date)
    {
        date_ = date;
    }

}
