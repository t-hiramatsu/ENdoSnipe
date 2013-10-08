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
package jp.co.acroquest.endosnipe.web.dashboard.dao;

import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.entity.SchedulingReportDefinition;

/**
 * 
 * @author khinlay
 *
 */
public interface SchedulingReportDefinitionDao
{
    /**
     * 
     * @return is used
     */
    List<SchedulingReportDefinition> selectAll();

    /**
     * 
     * @param reportId is used
     * @return is used
     */
    SchedulingReportDefinition selectById(int reportId);

    /**
     * 
     * @param reportName is used
     * @return is used
     */
    SchedulingReportDefinition selectByName(String reportName);

    /**
     * 
     * @param targetMeasurementName is used
     * @return is used
     */
    List<SchedulingReportDefinition> selectByTargetName(String targetMeasurementName);

    List<SchedulingReportDefinition> selectBySchedule(Timestamp currentTime);

    /**
     * 
     * @param schedulingReportDefinition is used
     */
    void insert(final SchedulingReportDefinition schedulingReportDefinition);

    /**
     * 
     * @param schedulingReportDefinition is used
     */
    void update(SchedulingReportDefinition schedulingReportDefinition);

    /**
     * 指定されたレポート名に該当するレポート情報をDBから削除する。
     *
     * @param reportName
     *            レポート名
     */
    void deleteByName(final String reportName);

    /**
     * 指定されたレポートIDに該当するレポート情報をDBから削除する。
     * 
     * @param reportId レポートID
     */
    void deleteById(final int reportId);

    /**
     * すべてのレコードを削除します。<br />
     */
    void deleteAll();

    /**
     * 
     * @param schedulingReportDefinition is used
     * @return is used
     */
    int selectSequenceNum(final SchedulingReportDefinition schedulingReportDefinition);
}
