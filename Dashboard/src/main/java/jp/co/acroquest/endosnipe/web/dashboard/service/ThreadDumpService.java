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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ThreadDumpDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.form.ThreadDumpDataForm;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;

public class ThreadDumpService
{

    public List<ThreadDumpDefinitionDto> getTermThreadDumpData(final ThreadDumpDataForm termDataForm)
    {
        List<String> dataGroupIdList = termDataForm.getDataGroupIdList();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String dbName = dbManager.getDataBaseName(1);
        for (String dataGroupId : dataGroupIdList)
        {
            List<JavelinLog> list = new ArrayList<JavelinLog>();
            Timestamp start = new Timestamp(Long.valueOf(termDataForm.getStartTime()));
            Timestamp end = new Timestamp(Long.valueOf(termDataForm.getEndTime()));
            list =
                    JavelinLogDao.selectThreadDumpByTermAndName(dbName, start, end, dataGroupId,
                                                                true, true);
        }
        return null;
    }
}
