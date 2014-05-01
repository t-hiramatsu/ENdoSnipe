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
package jp.co.acroquest.endosnipe.web.explorer.dao;

import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.entity.SummarySignalInfo;

/**
 * {@link SummarySignalInfo} のための DAO のインターフェースです。
 *
 * @author pin
 *
 */
public interface SummarySignalInfoDao
{
    /**
     * Get the all data of SummarySignal.<br />
     * @return SummarySignalのリスト
     */
    List<SummarySignalInfo> selectAll();

    /**
     * {@link SummarySignalInfo} オブジェクトを挿入します。<br />
     *
     * @param summarySignalInfo
     *            Object to insert
     */
    void insert(final SummarySignalInfo summarySignalInfo);

    /**
     * Get the sequence number of summarySignal.<br />
     * 
     * @param summarySignalInfo
     *           Object of SummarySignal
     * @return sequence no of SummarySignal
     */
    int selectSequenceNum(final SummarySignalInfo summarySignalInfo);

    /**
     * 正規表現で指定した名前のサマリシグナルをDBから削除する
     * @param nameRe サマリシグナル名の正規表現
     */
    void deleteChildren(final String nameRe);

    /**
     * Get the SummarySignalInfo by Name.
     *
     * @param summarySignalName
     *            summarySignal's Name
     * @return summarySignal's data
     */
    SummarySignalInfo selectByName(String summarySignalName);

}
