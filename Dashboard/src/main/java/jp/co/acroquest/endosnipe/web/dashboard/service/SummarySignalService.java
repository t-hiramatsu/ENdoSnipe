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
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
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

/**
 * Service of Summary Signal
 * 
 * @author Khine Wai, Pin
 * 
 */
@Service
public class SummarySignalService
{
    /**
     * SummarySignalのDao。
     */
    @Autowired
    protected SummarySignalInfoDao summarySignalInfoDao;

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /**
     * コンストラクタ
     */
    public SummarySignalService()
    {

    }

    /**
     * Get all summary signal data to show on the select box of dialog box
     * 
     * @return SignalDefinitionDto List
     */
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
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
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

    /**
     * Get all summary signal data to by using telegram
     * 
     * @return SummarySignalDefinitionDto List
     */
    public List<SummarySignalDefinitionDto> getAllSummarySignals()
    {
        getAllSummarySignalDefinition(null);
        return null;
    }

    /**
     * summarySignalInfoオブジェクトをSignalDefinitionDtoオブジェクトに変換する。
     * 
     * @param summarySignalInfo
     *            summarySignalInfoオブジェクト
     * @return SignalDefinitionDtoオブジェクト
     */
    public SignalDefinitionDto convertSummarySignalDto(final SummarySignalInfo summarySignalInfo)
    {
        SignalDefinitionDto definitionDto = new SignalDefinitionDto();
        definitionDto.setSignalName(summarySignalInfo.summarySignalName);

        return definitionDto;
    }

    /**
     * SummarySignalDefinitionグラフ定義をDBに登録する。
     * 
     * @param summarySignalDefinitionDto
     *           SummarySignalグラフ定義
     * @return SummarySignal定義のDTOオブジェクト
     */
    public SummarySignalDefinitionDto insertSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinitionDto,
                                           SummarySignalConstants.OPERATION_TYPE_ADD);
        return null;

    }

    /**
     * summarySignalInfoオブジェクトをSummarySignalDefinitionDtoオブジェクトに変換する。
     * 
     * @param summarySignalInfo
     *            summarySignalInfoオブジェクト
     * @return SummarySignalDefinitionDtoオブジェクト
     */
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

    /**
     * SummarySignalグラフ定義情報の追加・更新・削除を各クライアントに送信する。
     * @param summarySignalDefinitionDto Summary Signalグラフ定義情報
     * @param type 送信タイプ(add:追加 update:変更 delete:削除 getAll)
     */
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

    /**
     * create telegram for summary Signal Definition for add process
     * @param summarySignalDefinitionDto Summary Signalグラフ定義情報
     * @return telegram of summary signal
     */
    private Telegram createAddSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD);
    }

    /**
     * create telegram for summary Signal Definition for delete process
     * @param summarySignalDefinitionDto Summary Signalグラフ定義情報
     * @return telegram of summary signal
     */
    private Telegram createDeleteSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE);
    }

    /**
     * create telegram for summary Signal Definition for getAll data process
     * @param summarySignalDefinitionDto Summary Signalグラフ定義情報
     * @return telegram of summary signal
     */
    private Telegram createGetAllSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ALL);
    }

    /**
     * create telegram for summary Signal Definition for update process
     * @param summarySignalDefinitionDto Summary Signalグラフ定義情報
     * @return telegram of summary signal
     */
    private Telegram createUpdateSummarySignalTelegram(
            final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        return SummarySignalUtil.createAddSummarySignalDefinition(summarySignalDefinitionDto,
                                                                  TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE);
    }

    /**
     * check about summary signal is already exist or not
     * @param summarySignalId  Id of summary signal
     * @param summarySignalName  Name of summary signal
     * @return duplicate or not
     */
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

    /**
     * get all summary signal list from the table
     *@return SummarySignalInfo List
     */
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
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            return new ArrayList<SummarySignalInfo>();
        }
        List<SummarySignalInfo> definitionDtoList = new ArrayList<SummarySignalInfo>();
        for (SummarySignalInfo summarySignalInfo : summarySignalList)
        {
            definitionDtoList.add(summarySignalInfo);
        }
        return definitionDtoList;
    }

    /**
     * SummarySignalグラフ名に該当する複数グラフ定義をDBから削除する。
     *
     * @param summarySignalName
     *            Summary Signalグラフ名
     */
    public void deleteSummarySignalDefinition(final String summarySignalName)
    {
        SummarySignalDefinitionDto summarySignalDefinitionDto = new SummarySignalDefinitionDto();
        summarySignalDefinitionDto.setSummarySignalName(summarySignalName);
        sendSummarySignalDefinitionRequest(summarySignalDefinitionDto,
                                           SummarySignalConstants.OPERATION_TYPE_DELETE);
    }

    /**
     * Summary Signalグラフ定義をDBから取得する。
     * @param summarySignalName Summary Signalグラフ名
     * @return SummarySignalDefinitionグラフ定義のDTOオブジェクト
     */
    public SummarySignalDefinitionDto getSummarySignalDefinition(final String summarySignalName)
    {
        SummarySignalInfo summarySignalInfo = new SummarySignalInfo();
        summarySignalInfo = summarySignalInfoDao.selectByName(summarySignalName);
        SummarySignalDefinitionDto summarySignalDto =
                this.convertSummarySignalDtos(summarySignalInfo);

        return summarySignalDto;

    }

    /**
     * SummarySignalグラフ定義を更新する。
     * 
     * @param summarySignalDefinition
     *            SummarySignalグラフ定義
     */
    public void updateSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinition)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinition,
                                           SummarySignalConstants.OPERATION_TYPE_UPDATE);

    }

    /**
     *get All summary signal definition data
     *
     *@param summarySignalDefinition
     *            SummarySignalグラフ定義    
     */
    public void getAllSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinition)
    {
        sendSummarySignalDefinitionRequest(summarySignalDefinition,
                                           SummarySignalConstants.OPERATION_TYPE_GETALL);

    }

}
