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
 * {@link SchedulingReportDefinition} のための DAO のインターフェースです。
 * @author khinlay
 *
 */
public interface SchedulingReportDefinitionDao
{
    /**
     * select all data from database.
     * @return is used
     */
    List<SchedulingReportDefinition> selectAll();

    /**
     * select data by using targetmeasurementitem 
     * by matching parent agentName.
     * @param agentName is used
     * @return is used
     */
    List<SchedulingReportDefinition> selectAllByAgentName(String agentName);

    /**
     * select data by using id.
     * @param reportId is used
     * @return is used
     */
    SchedulingReportDefinition selectById(int reportId);

    /**
     * select data by using report name.
     * @param reportName is used
     * @return is used
     */
    SchedulingReportDefinition selectByName(String reportName);

    /**
     * select data by using target name.
     * @param targetMeasurementName is used
     * @return is used
     */
    List<SchedulingReportDefinition> selectByTargetName(String targetMeasurementName);

    /**
     * select data by using schedule
     * @param currentTime timestamp
     * @return scheduling report definition
     */
    List<SchedulingReportDefinition> selectBySchedule(Timestamp currentTime);

    /**
     * insert into database.
     * @param schedulingReportDefinition is used
     */
    void insert(final SchedulingReportDefinition schedulingReportDefinition);

    /**
     * update scheduling report.
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
     * select sequence number.
     * @param schedulingReportDefinition is used
     * @return is used
     */
    int selectSequenceNum(final SchedulingReportDefinition schedulingReportDefinition);
}
