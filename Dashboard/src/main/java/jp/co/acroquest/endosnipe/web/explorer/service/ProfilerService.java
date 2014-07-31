/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator.UpdateInvocationParam;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.web.explorer.entity.InvocationInfo;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;

import org.springframework.stereotype.Service;

/**
 * プロファイラ機能のサービスクラス
 * 
 * @author hiramatsu
 */
@Service
public class ProfilerService
{
    /**
     * デフォルトコンストラクタ
     */
    public ProfilerService()
    {

    }

    /**
     * 状態取得要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     */
    public void getStatus(final String agentName)
    {
        Telegram telegram = TelegramCreator.createProfileGetTelegram(agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * リセット要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     */
    public void resetProfile(final String agentName)
    {
        Telegram telegram = TelegramCreator.createProfileResetTelegram(agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * 計測対象更新要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     * @param invocations 更新する計測対象メソッドのリスト
     */
    public void updateTarget(final String agentName, final List<InvocationInfo> invocations)
    {
        List<UpdateInvocationParam> invocationParamList = new ArrayList<UpdateInvocationParam>();
        for (InvocationInfo invocation : invocations)
        {
            UpdateInvocationParam invocationParam =
                    new UpdateInvocationParam(invocation.getClassName(),
                                              invocation.getMethodName(),
                                              invocation.isResponseGraphOutput(),
                                              invocation.isTarget(),
                                              invocation.getAlarmThreshold(),
                                              invocation.getAlarmCpuThreshold());
            invocationParamList.add(invocationParam);
        }
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram =
                    TelegramCreator.createUpdateInvocationTelegram(invocationParamList.toArray(new UpdateInvocationParam[invocationParamList.size()]),
                                                                   agentName);
            communicationClient.sendTelegram(telegram);
        }
    }
}
