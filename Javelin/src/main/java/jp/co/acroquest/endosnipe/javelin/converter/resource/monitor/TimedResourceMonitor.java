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
package jp.co.acroquest.endosnipe.javelin.converter.resource.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinAcceptThread;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinConnectThread;

/**
 * 時刻指定のリソース情報を記録するモニタ。
 * 通常、DataCollectorでは、現在時刻のリソース情報を記録するが、このモニタからの情報は
 * 指定した時刻で記録される。
 * 
 * @author eriguchi
 *
 */
public class TimedResourceMonitor
{
    private static Map<String, String> valueMap__ = new ConcurrentHashMap<String, String>();

    /**
     * インスタンス化を禁止するためのコンストラクタ。
     */
    private TimedResourceMonitor()
    {
        // 何もしない。
    }

    /**
     * 値を追加する。
     * 
     * @param key キー。
     * @param value 値。
     */
    public static void addValue(String key, String value)
    {
        valueMap__.put(key, value);
    }

    /**
     * バッファした測定データを送信する。 
     * @param time 時刻。
     */
    public static void sendAll(long time)
    {
        // これまでの測定データをTelegramに変換する。
        List<Body> responseBodyList = new ArrayList<Body>();
        long currentTime = time;
        ResponseBody timeBody = ResourceNotifyAccessor.makeTimeBody(currentTime);
        responseBodyList.add(timeBody);
        Map<String, String> map = valueMap__;
        valueMap__ = new ConcurrentHashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            String itemName = entry.getKey();
            Object value = entry.getValue();
            ItemType itemType = ItemType.ITEMTYPE_STRING;
            ResponseBody responseBody =
                ResourceNotifyAccessor.makeResourceResponseBody(itemName, value, itemType);
            responseBodyList.add(responseBody);
        }

        // 測定データを送信する。
        Telegram responseTelegram = ResourceNotifyAccessor.makeNotifyTelegram(responseBodyList);
        JavelinAcceptThread.getInstance().sendTelegram(responseTelegram);
        JavelinConnectThread.getInstance().sendTelegram(responseTelegram);

        if (SystemLogger.getInstance().isDebugEnabled())
        {
            SystemLogger.getInstance().debug("send timed resource data:" + map);
        }
    }

    /**
     * バッファした測定データを送信する。 
     * @param time 時刻。
     * @param valueMap 測定データ。
     */
    public static void sendNow(long time,  Map<String, String> valueMap)
    {
        // これまでの測定データをTelegramに変換する。
        List<Body> responseBodyList = new ArrayList<Body>();
        long currentTime = time;
        ResponseBody timeBody = ResourceNotifyAccessor.makeTimeBody(currentTime);
        responseBodyList.add(timeBody);
        Map<String, String> map = valueMap;
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            String itemName = entry.getKey();
            Object value = entry.getValue();
            ItemType itemType = ItemType.ITEMTYPE_STRING;
            ResponseBody responseBody =
                ResourceNotifyAccessor.makeResourceResponseBody(itemName, value, itemType);
            responseBodyList.add(responseBody);
        }

        // 測定データを送信する。
        Telegram responseTelegram = ResourceNotifyAccessor.makeNotifyTelegram(responseBodyList);
        JavelinAcceptThread.getInstance().sendTelegram(responseTelegram);
        JavelinConnectThread.getInstance().sendTelegram(responseTelegram);

        if (SystemLogger.getInstance().isDebugEnabled())
        {
            SystemLogger.getInstance().debug("send timed resource data:" + map);
        }
    }
}
