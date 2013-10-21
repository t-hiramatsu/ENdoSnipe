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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.SummarySignalConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dao.SummarySignalInfoDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SummarySignalInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.dashboard.util.SummarySignalUtil;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummarySignalService
{
    @Autowired
    protected SummarySignalInfoDao summarySignalInfoDao;

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    public List<SignalDefinitionDto> getAllSummarySignal()
    {
        List<SummarySignalInfo> summarySignalList = null;
        try
        {
            summarySignalList = summarySignalInfoDao.selectAll();
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
            }

            return new ArrayList<SignalDefinitionDto>();
        }
        List<SignalDefinitionDto> definitionDtoList = new ArrayList<SignalDefinitionDto>();
        for (SummarySignalInfo summarySignalInfo : summarySignalList)
        {
            SignalDefinitionDto summarySignalDto = this.convertSummarySignalDto(summarySignalInfo);
            definitionDtoList.add(summarySignalDto);
        }
        return definitionDtoList;
    }

    public List<SummarySignalDefinitionDto> getAllSummarySignals()
    {
        getAllSummarySignalDefinition(null);
        return null;
    }

    public SignalDefinitionDto convertSummarySignalDto(final SummarySignalInfo summarySignalInfo)
    {
        SignalDefinitionDto definitionDto = new SignalDefinitionDto();
        definitionDto.setSignalName(summarySignalInfo.summarySignalName);

        return definitionDto;
    }

    public SummarySignalDefinitionDto insertSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinitionDto,
                                           SummarySignalConstants.OPERATION_TYPE_ADD);
        return null;

    }

    private SummarySignalDefinitionDto convertSummarySignalDtos(
            final SummarySignalInfo summarySignalInfo)
    {
        List<String> summarySignalList = new ArrayList<String>();
        SummarySignalDefinitionDto definitionDto = new SummarySignalDefinitionDto();
        definitionDto.setSummarySignalId(summarySignalInfo.summarySignalId);
        definitionDto.setSummarySignalName(summarySignalInfo.summarySignalName);
        definitionDto.setSummarySignalType(summarySignalInfo.summarySignalType);
        definitionDto.setPriorityNo(summarySignalInfo.priorityNo);

        String[] temp = null;
        temp = SummarySignalInfo.targetSignalId.split(",");
        for (int index = 0; index < temp.length; index++)
        {
            summarySignalList.add(temp[index]);
        }
        definitionDto.setMessage(summarySignalInfo.errorMessage);
        definitionDto.setSignalList(summarySignalList);
        return definitionDto;
    }

    private void sendSummarySignalDefinitionRequest(
            final SummarySignalDefinitionDto summarySignalDefinitionDto, final String type)
    {
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram = null;
            if (SummarySignalConstants.OPERATION_TYPE_ADD.equals(type))
            {
                telegram = createAddSummarySignalTelegram(summarySignalDefinitionDto);
            }
            else if (SummarySignalConstants.OPERATION_TYPE_UPDATE.equals(type))
            {
                telegram = createUpdateSummarySignalTelegram(summarySignalDefinitionDto);
            }
            else if (SummarySignalConstants.OPERATION_TYPE_DELETE.equals(type))
            {
                telegram = createDeleteSummarySignalTelegram(summarySignalDefinitionDto);
            }
            else if (SummarySignalConstants.OPERATION_TYPE_GETALL.equals(type))
            {
                telegram = createGetAllSummarySignalTelegram(summarySignalDefinitionDto);
            }

            if (telegram != null)
            {
                communicationClient.sendTelegram(telegram);
            }
        }

    }

    private Telegram createAddSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD);
    }

    private Telegram createDeleteSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE);
    }

    private Telegram createGetAllSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ALL);
    }

    private Telegram createUpdateSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE);
    }

    public SummarySignalInfo convertSummarySignalInfo(final SummarySignalDefinitionDto definitionDto)
    {
        SummarySignalInfo summarySignalInfo = new SummarySignalInfo();

        summarySignalInfo.summarySignalId = definitionDto.getSummarySignalId();
        summarySignalInfo.summarySignalName = definitionDto.getSummarySignalName();
        summarySignalInfo.summarySignalType = definitionDto.getSummarySignalType();
        summarySignalInfo.priorityNo = definitionDto.getPriorityNo();
        summarySignalInfo.targetSignalId = definitionDto.signalList.toString();
        summarySignalInfo.errorMessage = definitionDto.getMessage();
        return summarySignalInfo;
    }

    public boolean checkDuplicate(final long summarySignalId, final String summarySignalName)
    {
        SummarySignalInfo summarySignalInfo =
                this.summarySignalInfoDao.selectByName(summarySignalName);
        if (summarySignalInfo != null)
        {
            return true;
        }

        return false;
    }

    public void calculatePriority(final SummarySignalDefinitionDto summarySignalDefinitionDto,
            final List<String> signalList)
    {
        List<SummarySignalInfo> summarySignalList = null;
        int childMaxPriority = 0;
        for (String signalName : signalList)
        {
            if (signalName.contains("summary signal"))
            {
                summarySignalList = this.getAllSummarySignalList();
                childMaxPriority = summarySignalList.get(0).getPriorityNo();
                for (int index = 1; index < summarySignalList.size() - 1; index++)
                {
                    if (childMaxPriority < summarySignalList.get(index).getPriorityNo())
                    {
                        childMaxPriority = summarySignalList.get(index).getPriorityNo();
                    }
                }
            }
        }
        summarySignalDefinitionDto.setPriorityNo(childMaxPriority);
    }

    public List<SummarySignalInfo> getAllSummarySignalList()
    {
        List<SummarySignalInfo> summarySignalList = null;
        try
        {
            summarySignalList = summarySignalInfoDao.selectAll();
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
            }

            return new ArrayList<SummarySignalInfo>();
        }
        List<SummarySignalInfo> definitionDtoList = new ArrayList<SummarySignalInfo>();
        for (SummarySignalInfo summarySignalInfo : summarySignalList)
        {
            definitionDtoList.add(summarySignalInfo);
        }
        return definitionDtoList;
    }

    private void sendGetAllStateRequest()
    {
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram = createAllStateTelegram();
            communicationClient.sendTelegram(telegram);
        }

    }

    private Telegram createAllStateTelegram()
    {
        Telegram telegram = new Telegram();

        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_SUMMARYSIGNAL_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        Body[] requestBodys = {};

        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }

    public void deleteSummarySignalDefinition(final String summarySignalName)
    {
        SummarySignalDefinitionDto summarySignalDefinitionDto = new SummarySignalDefinitionDto();
        summarySignalDefinitionDto.setSummarySignalName(summarySignalName);
        sendSummarySignalDefinitionRequest(summarySignalDefinitionDto,
                                           SummarySignalConstants.OPERATION_TYPE_DELETE);
    }

    public SummarySignalDefinitionDto getSummarySignalDefinition(final String summarySignalName)
    {
        SummarySignalInfo summarySignalInfo = new SummarySignalInfo();
        summarySignalInfo = summarySignalInfoDao.selectByName(summarySignalName);
        SummarySignalDefinitionDto summarySignalDto =
                this.convertSummarySignalDtos(summarySignalInfo);

        return summarySignalDto;

    }

    public void updateSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinition)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinition,
                                           SummarySignalConstants.OPERATION_TYPE_UPDATE);

    }

    public void getAllSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinition)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinition,
                                           SummarySignalConstants.OPERATION_TYPE_GETALL);

    }

}
