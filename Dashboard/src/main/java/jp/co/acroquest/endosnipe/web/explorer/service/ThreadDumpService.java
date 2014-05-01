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
package jp.co.acroquest.endosnipe.web.explorer.service;

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
import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.explorer.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.explorer.dto.ThreadDumpDto;
import jp.co.acroquest.endosnipe.web.explorer.form.TermDataForm;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.explorer.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.explorer.util.DaoUtil;

import org.springframework.stereotype.Service;

/**
 * This is service class for threadDump
 * 
 * @author khinewai
 *
 */
@Service
public class ThreadDumpService
{
    /**constants integer value is 4*/
    private static final int NUMBER_FOUR = 4;

    /**
     * this is mapping for threadDump node
     */
    private static final String THREADDUMP_POSTFIX_ID = "/JvnLog_Notify";

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(ThreadDumpService.class);

    /**
     * This is default constructor
     */
    public ThreadDumpService()
    {

    }

    /**
     * This function is to get all data from 
     * database by connection with JavelinDao class
     * @param termDataForm pass value from controller class
     * @return threadDump data
     */
    public Map<String, List<ThreadDumpDto>> getTermThreadDumpData(final TermDataForm termDataForm)
    {
        List<String> dataGroupIdList = termDataForm.getDataGroupIdList();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String dbName = dbManager.getDataBaseName(1);
        List<JavelinLog> list = new ArrayList<JavelinLog>();
        List<ThreadDumpDto> displayList = new ArrayList<ThreadDumpDto>();
        Map<String, List<ThreadDumpDto>> dataList = new HashMap<String, List<ThreadDumpDto>>();
        for (String dataGroupId : dataGroupIdList)
        {
            Timestamp start = new Timestamp(Long.valueOf(termDataForm.getStartTime()));
            Timestamp end = new Timestamp(Long.valueOf(termDataForm.getEndTime()));
            try
            {
                String nodeName = dataGroupId;
                if (nodeName != null)
                {
                    if (nodeName.split("/").length > NUMBER_FOUR)
                    {
                        nodeName = getAgentName(nodeName);
                    }
                }
                list =
                        JavelinLogDao.selectThreadDumpByTermAndName(dbName, start, end, nodeName,
                                                                    true, true);
            }
            catch (SQLException ex)
            {
                LOGGER.log(ex);
            }
            displayList = this.createThreadDumpDefinitionDto(list);
            dataList.put(dataGroupId + THREADDUMP_POSTFIX_ID, displayList);
            list.clear();
        }

        return dataList;
    }

    /**
     * Substract agent Name from node name
     * @param nodeName full node path name
     * @return agentName
     */
    private String getAgentName(final String nodeName)
    {
        String[] agentSplit = nodeName.split("/");
        String agentName = "/" + agentSplit[1];
        for (int index = 2; index < NUMBER_FOUR; index++)
        {
            agentName += "/" + agentSplit[index];
        }
        return agentName;
    }

    /**
     * This function is to convert all JavelinLog data to 
     * threadDump data
     * @param list is javelinLog data
     * @return threadDump data 
     */
    public List<ThreadDumpDto> createThreadDumpDefinitionDto(final List<JavelinLog> list)
    {
        List<ThreadDumpDto> displayList = new ArrayList<ThreadDumpDto>();
        for (JavelinLog table : list)
        {
            ThreadDumpDto result = new ThreadDumpDto();
            result.agentId_ = table.logId;
            result.date_ = table.startTime.toString();
            String name = table.logFileName;
            result.setLogFileName(name);
            result.threadDumpInfo_ = this.getThreadDumpDetailData(name);
            displayList.add(result);
        }
        return displayList;
    }

    /**
     * This function is to convert byte data to string data
     * @param fileName is JavelinLog file name
     * @return threadDumpInfo data
     */
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

    /**
     * to get all of threadDump data related with agent
     * @param measurementItem is agent name
     * @return threadDump Data
     */
    public List<ThreadDumpDto> getAllAgentData(final String measurementItem)
    {
        List<JavelinLog> list = new ArrayList<JavelinLog>();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String dbName = dbManager.getDataBaseName(1);
        List<ThreadDumpDto> displayList = new ArrayList<ThreadDumpDto>();
        try
        {
            //for get agentName's threadDump even selected node is subChild of agent
            String agentName = measurementItem;
            if (agentName.split("/").length > NUMBER_FOUR)
            {
                agentName = getAgentName(measurementItem);
            }

            list = JavelinLogDao.selectAllThreadDumpByMeasurementItem(dbName, agentName);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        displayList = this.createThreadDumpDefinitionDto(list);
        return displayList;
    }

    /**
     * This function to connect telegram
     * @param agentName is to create the thread dump by naming this parameter
     */
    public void createThreadDump(final String agentName)
    {
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram = TelegramCreator.createThreadDumpRequestTelegram(agentName);
            communicationClient.sendTelegram(telegram);
        }

    }
}
