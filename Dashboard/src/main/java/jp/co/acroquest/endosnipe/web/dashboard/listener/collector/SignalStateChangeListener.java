/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.web.dashboard.listener.collector;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalTreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

import org.wgp.manager.WgpDataManager;

/**
 * DataCollectorからシグナル状態通知電文
 * @author fujii
 *
 */
public class SignalStateChangeListener extends AbstractTelegramListener
{
    /**
     * コンストラクタです。
     */
    public SignalStateChangeListener()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        List<SignalTreeMenuDto> signalTreeMenuDtoList = new ArrayList<SignalTreeMenuDto>();

        Body[] resourceAlarmBodys = telegram.getObjBody();

        // DataCollectorから送信された電文から、
        // Dashboardのクライアントに通知するためのイベントを作成する。
        String[] treeIds = null;
        int[] levels = null;
        // String[] alarmTypes = null;

        for (Body body : resourceAlarmBodys)
        {
            String objectNameInTelegram = body.getStrObjName();
            String itemNameInTelegram = body.getStrItemName();
            if (TelegramConstants.OBJECTNAME_RESOURCEALARM.equals(objectNameInTelegram) == false)
            {
                continue;
            }
            int loopCount = body.getIntLoopCount();
            Object[] measurementItemValues = body.getObjItemValueArr();
            // 計測IDの項目に対する処理
            if (TelegramConstants.ITEMNAME_ALARM_ID.equals(itemNameInTelegram))
            {
                treeIds = getStringValues(loopCount, measurementItemValues);
            }
            // アラームレベルの項目に対する処理
            else if (TelegramConstants.ITEMNAME_ALARM_LEVEL.equals(itemNameInTelegram))
            {
                levels = getIntValues(loopCount, measurementItemValues);
            }
        }

        for (int cnt = 0; cnt < treeIds.length; cnt++)
        {
            SignalTreeMenuDto signalTreeMenu = new SignalTreeMenuDto();
            signalTreeMenu.setId(treeIds[cnt]);
            signalTreeMenu.setTreeId(treeIds[cnt]);
            signalTreeMenu.setType("signal");
            int signalValue = levels[cnt];
            signalTreeMenu.setSignalValue(Integer.valueOf(signalValue));
            signalTreeMenu.setIcon("signal_" + signalValue);

            signalTreeMenuDtoList.add(signalTreeMenu);
        }

        // シグナルの状態更新を通知する。
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager == null || resourceSender == null)
        {
            return null;
        }

        resourceSender.send(signalTreeMenuDtoList);

        return null;
    }

    /**
     * 電文をString型にして返します。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 電文の値オブジェクト
     * @return String型の電文の値配列
     */
    private String[] getStringValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        String[] telegramValues = new String[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            String alarmType = (String)telegramValuesOfobject[cnt];
            telegramValues[cnt] = alarmType;
        }
        return telegramValues;
    }

    /**
     * 電文の配列をInteger型にして返します。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 電文の値オブジェクト
     * @return Integer型の電文の値配列
     */
    private int[] getIntValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        int[] telegramValues = new int[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            Integer value = (Integer)telegramValuesOfobject[cnt];
            telegramValues[cnt] = value.intValue();
        }
        return telegramValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_SIGNAL_STATE_CHANGE;
    }

}
