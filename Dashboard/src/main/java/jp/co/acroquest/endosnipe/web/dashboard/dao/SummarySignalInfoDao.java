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

import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.entity.SummarySignalInfo;

public interface SummarySignalInfoDao
{

    public List<SummarySignalInfo> selectAll();

    //  void insert(final SignalInfo signalInfo);
    void insert(final SummarySignalInfo summarySignalInfo);

    int selectSequenceNum(final SummarySignalInfo SummarySignalInfo);

    SummarySignalInfo selectByName(String summarySignalName);

}
