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
package jp.co.acroquest.endosnipe.web.explorer.listener.collector;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.util.ControllerMessages;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.PropertyEntry;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.explorer.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.web.explorer.manager.ControlSender;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;

import org.wgp.manager.WgpDataManager;

/**
 * DataCollectorからの設定更新応答を受け、クライアントに設定情報を返すためのリスナです。
 * 
 * @author hiramatsu
 */
public class ControlUpdatePropertyListener extends AbstractTelegramListener
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(ConfigurationReader.class);

    /**
     * デフォルトコンストラクタ
     */
    public ControlUpdatePropertyListener()
    {

    }

    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        // 電文⇒オブジェクトに変換
        List<PropertyEntry> propertyList = makePropertyInfoList(telegram);

        // Senderを確保
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ControlSender controlSender = eventManager.getControlSender();
        if (dataManager == null || controlSender == null)
        {
            return null;
        }

        // エージェント名を取得
        Body[] bodies = telegram.getObjBody();
        String agentName = null;
        if (bodies != null && bodies.length > 0)
        {
            Body lastBody = bodies[bodies.length - 1];
            if (lastBody.getStrObjName().equals("agentName"))
            {
                agentName = lastBody.getStrItemName();
            }
        }

        // 送信
        controlSender.send(propertyList, agentName);
        return null;
    }

    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
    }

    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_PROPERTY;
    }

    /**
     * 電文からテーブルに表示するプロパティ情報を作成する
     * 
     * @param telegram 受信電文
     * @return 表示するプロパティ情報
     */
    private List<PropertyEntry> makePropertyInfoList(final Telegram telegram)
    {
        List<PropertyEntry> propertyInfoList = new ArrayList<PropertyEntry>();

        // 受信電文からテーブルに表示するデータを作成する
        Body[] bodyList = telegram.getObjBody();

        if (bodyList == null)
        {
            return propertyInfoList;
        }

        // 各電文本体要素からPropertyEntryの作成（最後尾の電文本体要素はエージェント名伝達用なので、扱わない）。
        for (int bodyIndex = 0; bodyIndex < bodyList.length - 1; bodyIndex++)
        {
            Body body = bodyList[bodyIndex];
            String propertyName = body.getStrObjName();
            String propertyDetail = "";
            String propertyValue = body.getStrItemName();

            if (propertyName == null || "".equals(propertyName))
            {
                continue;
            }

            //項目詳細の取得　取得に失敗してもメッセージ表示をするのみ。項目の表示は行う。
            propertyDetail = ControllerMessages.getMessage(propertyName);
            if (propertyDetail == null || "".equals(propertyDetail))
            {
                String failGetKey =
                        "endosnipe.bottleneckeye.editors.control.ControlTab.FailGetProperty";
                LOGGER.error(ControllerMessages.getMessage(failGetKey));
                String nameLabelKey =
                        "endosnipe.bottleneckeye.editors.control.ControlTab.PropertyNameLabel";
                LOGGER.error(ControllerMessages.getMessage(nameLabelKey, propertyName));
            }

            if (propertyValue == null || "".equals(propertyValue))
            {
                String failReadKey =
                        "endosnipe.bottleneckeye.editors.control.ControlTab.FailReadProperty";
                LOGGER.error(ControllerMessages.getMessage(failReadKey));
                String nameLabelKey =
                        "endosnipe.bottleneckeye.editors.control.ControlTab.PropertyNameLabel";
                LOGGER.error(ControllerMessages.getMessage(nameLabelKey, propertyName));
                continue;
            }

            PropertyEntry entry = new PropertyEntry();
            entry.setProperty(propertyName);
            entry.setPropertyDetail(propertyDetail);
            entry.setCurrentValue(propertyValue);
            propertyInfoList.add(entry);
        }
        return propertyInfoList;
    }

}
