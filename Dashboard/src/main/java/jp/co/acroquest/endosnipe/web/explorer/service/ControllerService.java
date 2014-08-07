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

import java.util.List;

import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.PropertyEntry;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;

import org.springframework.stereotype.Service;

/**
 * コントロール機能のサービスクラス
 * 
 * @author hiramatsu
 */
@Service
public class ControllerService
{
    /**
     * デフォルトコンストラクタ
     */
    public ControllerService()
    {

    }

    /**
     * サーバプロパティ設定取得要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     */
    public void getProperty(final String agentName)
    {
        Telegram telegram = TelegramCreator.createGetServerPropertyTelegram(agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * サーバプロパティ設定更新要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     * @param propertyList 更新する設定のリスト
     */
    public void updateProperty(final List<PropertyEntry> propertyList, final String agentName)
    {
        Telegram telegram = TelegramCreator.createUpdateTelegram(propertyList, agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }
}
