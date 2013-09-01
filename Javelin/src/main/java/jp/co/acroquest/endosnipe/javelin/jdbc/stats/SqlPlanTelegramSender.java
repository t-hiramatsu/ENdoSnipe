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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats;

import java.sql.Timestamp;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinAcceptThread;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinConnectThread;

/**
 * SQL実行計画の通知電文を送信するクラス。
 * 
 * @author miyasaka
 *
 */
public class SqlPlanTelegramSender
{
    /**
     * SQL実行計画の通知電文を送信する。
     * 
     * @param measurementItemName 項目名
     * @param sqlStatement SQL文
     * @param executionPlan 実行計画
     * @param gettingPlanTime 実行計画取得時間
     */
    public void execute(final String measurementItemName, final String sqlStatement,
        final String executionPlan, final Timestamp gettingPlanTime)
    {
        // クライアントがいない場合は電文を作成しない。
        if (JavelinAcceptThread.getInstance().hasClient() == false
            && JavelinConnectThread.getInstance().isConnected() == false)
        {
            return;
        }

        // 通知電文を作成する。
        Telegram telegram = null;
        try
        {
            telegram =
                TelegramCreator.createSqlPlanTelegram(measurementItemName, sqlStatement,
                                                      executionPlan, gettingPlanTime);
        }
        catch (IllegalArgumentException ex)
        {
            SystemLogger logger = SystemLogger.getInstance();
            logger.warn(ex);
        }

        if (telegram == null)
        {
            return;
        }

        if (JavelinAcceptThread.getInstance().hasClient())
        {
            JavelinAcceptThread.getInstance().sendTelegram(telegram);
        }
        else
        {
            JavelinConnectThread.getInstance().sendTelegram(telegram);
        }
    }
}
