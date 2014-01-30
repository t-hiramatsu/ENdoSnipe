/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.web.explorer.listener.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.MeasurementConstants;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;

import org.wgp.manager.WgpDataManager;

/**
 * DataCollectorから計測データの通知を受け、クライアントに計測データを返すためのリスナです。
 * @author eriguchi
 *
 */
public class CollectorListener implements TelegramListener
{
    /** 各種データのMap */
    private static final Map<Integer, List<Integer>> SAME_DATA_MAP;

    /** データベース名 */
    private final String databaseName_;

    static
    {
        SAME_DATA_MAP = new HashMap<Integer, List<Integer>>();
        List<Integer> typeList = new ArrayList<Integer>();
        typeList.add(MeasurementConstants.TYPE_TURNAROUNDTIMECOUNT);
        typeList.add(MeasurementConstants.TYPE_PROC_RES_TOTAL_COUNT_EXCLUSION_SQL);
        typeList.add(MeasurementConstants.TYPE_PROC_RES_TOTAL_COUNT_ONLY_SQL);
        SAME_DATA_MAP.put(MeasurementConstants.TYPE_TURNAROUNDTIMECOUNT, typeList);
        typeList = new ArrayList<Integer>();
        typeList.add(MeasurementConstants.TYPE_TURNAROUNDTIME);
        typeList.add(MeasurementConstants.TYPE_PROC_RES_TIME_AVERAGE_EXCLUSION_SQL);
        typeList.add(MeasurementConstants.TYPE_PROC_RES_TIME_AVERAGE_ONLY_SQL);
        SAME_DATA_MAP.put(MeasurementConstants.TYPE_TURNAROUNDTIME, typeList);
        typeList = new ArrayList<Integer>();
        typeList.add(MeasurementConstants.TYPE_PHYSICALMEMORY_FREE);
        typeList.add(MeasurementConstants.TYPE_SYS_PHYSICALMEM_USED);
        SAME_DATA_MAP.put(MeasurementConstants.TYPE_PHYSICALMEMORY_FREE, typeList);

    }

    /**
     * コンストラクタです。
     * @param agentId エージェントID
     * @param databaseName DB名
     */
    public CollectorListener(final int agentId, final String databaseName)
    {
        this.databaseName_ = databaseName;
    }

    /**
     * 計測通知電文を受け、クライアントに計測データを返します。
     * @param telegram 計測通知電文
     * @return 応答電文(nullを返す。)
     */
    public Telegram receiveTelegram(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        if (header.getByteTelegramKind() == TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY
                && header.getByteRequestKind() == TelegramConstants.BYTE_REQUEST_KIND_RESPONSE)
        {
            // DataCollector側でエージェント名を設定するため、Explorerではエージェント名に空文字を指定する。
            ResourceData resourceData =
                    ResourceNotifyAccessor.createResourceData(telegram, this.databaseName_, "");

            EventManager eventManager = EventManager.getInstance();
            WgpDataManager dataManager = eventManager.getWgpDataManager();
            ResourceSender resourceSender = eventManager.getResourceSender();
            if (dataManager == null || resourceSender == null)
            {
                return null;
            }

            resourceSender.send(resourceData);
        }

        return null;
    }
}
