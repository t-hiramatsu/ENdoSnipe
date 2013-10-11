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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.dashboard.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ThreadDumpDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.form.TermDataForm;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.dashboard.util.DaoUtil;

import org.springframework.stereotype.Service;

/**
 * 
 * @author khinewai
 *
 */
@Service
public class ThreadDumpService
{
    private static final String THREADDUMP_POSTFIX_ID = "/threadDump";

    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(ConfigurationReader.class);

    public Map<String, List<ThreadDumpDefinitionDto>> getTermThreadDumpData(
            final TermDataForm termDataForm)
    {
        List<String> dataGroupIdList = termDataForm.getDataGroupIdList();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String dbName = dbManager.getDataBaseName(1);
        List<JavelinLog> list = new ArrayList<JavelinLog>();
        List<ThreadDumpDefinitionDto> displayList = new ArrayList<ThreadDumpDefinitionDto>();
        Map<String, List<ThreadDumpDefinitionDto>> dataList =
                new HashMap<String, List<ThreadDumpDefinitionDto>>();
        for (String dataGroupId : dataGroupIdList)
        {
            //Timestamp starts = new Timestamp(Long.valueOf(termDataForm.getStartTime()));
            // Timestamp ends = new Timestamp(Long.valueOf(termDataForm.getEndTime()));
            String startTime = "1281468362337";
            String endTime = "3381468362337";
            Timestamp start = new Timestamp(Long.valueOf(startTime));
            Timestamp end = new Timestamp(Long.valueOf(endTime));

            try
            {
                list =
                        JavelinLogDao.selectThreadDumpByTermAndName(dbName, start, end,
                                                                    dataGroupId, true, true);
            }
            catch (SQLException ex)
            {
                LOGGER.log(ex);
            }
            for (JavelinLog table : list)
            {
                ThreadDumpDefinitionDto result = new ThreadDumpDefinitionDto();
                result.threadId = table.getLogId();
                result.date = table.getStartTime().toString();
                String name = table.getLogFileName();
                result.threadDumpInfo = this.getThreadDumpDetailData(name);
                displayList.add(result);
                dataList.put(dataGroupId + THREADDUMP_POSTFIX_ID, displayList);

            }
            list.clear();
            /*
                        for (int count = 1; count <= 10; count++)
                        {
                            ThreadDumpDefinitionDto result = new ThreadDumpDefinitionDto();
                            result.threadId = count;
                            result.date = "2013/" + count + "/09 03:00";
                            result.threadDumpInfo = "this is testing";
                            displayList.add(result);
                            dataList.put(dataGroupId + THREADDUMP_POSTFIX_ID, displayList);
                        }*/
        }

        return dataList;
    }

    public String getThreadDumpDetailData(final String fileName)
    {
        StringBuffer detailData = new StringBuffer();

        try
        {

            JavelinLog jvnLog = DaoUtil.getJavelinLog("1", fileName);
            if (jvnLog == null)
            {
                LOGGER.log(LogMessageCodes.FAIL_GET_JVNLOG);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(jvnLog.javelinLog));
            String line;
            while ((line = reader.readLine()) != null)
            {
                detailData.append(line + "%");
            }

            reader.close();

        }
        catch (IOException ex)
        {
            // Do Nothing.
            SystemLogger.getInstance().warn(ex);
        }
        return detailData.toString();
    }
}
