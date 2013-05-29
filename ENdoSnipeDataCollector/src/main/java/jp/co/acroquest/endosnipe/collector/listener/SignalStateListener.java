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
package jp.co.acroquest.endosnipe.collector.listener;

import jp.co.acroquest.endosnipe.collector.ENdoSnipeDataCollectorPluginProvider;
import jp.co.acroquest.endosnipe.collector.JavelinDataLogger;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.manager.SignalStateManager;
import jp.co.acroquest.endosnipe.collector.processor.AlarmData;
import jp.co.acroquest.endosnipe.collector.processor.AlarmType;
import jp.co.acroquest.endosnipe.collector.util.CollectorTelegramUtil;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * Javelin ログ通知電文を受信するためのクラスです。<br />
 * 
 * @author fujii
 */
public class SignalStateListener extends AbstractTelegramListener implements TelegramListener,
        LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER =
                      ENdoSnipeLogger.getLogger(SignalStateListener.class,
                                                ENdoSnipeDataCollectorPluginProvider.INSTANCE);

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.log(SIGNAL_STATE_NOTIFY_RECEIVED);
        }

        // 閾値定義情報の名称一覧を取得する。
        String[] signalNames = getSignalNames(telegram);

        Telegram responseTelegram = createResponseTelegram(signalNames);

        return responseTelegram;
    }

    /**
     * 閾値定義情報の名称一覧を取得する。
     * @param telegram 閾値定義情報電文一覧
     * @return 閾値定義情報の名称一覧
     */
    private String[] getSignalNames(final Telegram telegram)
    {
        Body[] bodys = telegram.getObjBody();
        String[] signalNames = null;

        Body body = bodys[0];
        String objectNameInTelegram = body.getStrObjName();
        String itemNameInTelegram = body.getStrItemName();
        if (TelegramConstants.OBJECTNAME_RESOURCEALARM.equals(objectNameInTelegram) == false)
        {
            return null;
        }
        if (TelegramConstants.ITEMNAME_ALARM_ID.equals(itemNameInTelegram) == false)
        {
            return null;
        }
        Object[] measurementItemValues = body.getObjItemValueArr();
        int loopCount = body.getIntLoopCount();
        signalNames = getStringValues(loopCount, measurementItemValues);

        return signalNames;
    }

    /**
     * 電文から文字列配列を取得する。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 詳細
     * @return 電文から取得した文字列配列
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
            String signalName = (String)telegramValuesOfobject[cnt];
            telegramValues[cnt] = signalName;
        }
        return telegramValues;
    }

    /**
     * シグナル状態更新電文を生成する。
     * @param signalNames シグナル名称一覧
     * @return シグナル状態更新電文
     */
    private Telegram createResponseTelegram(final String[] signalNames)
    {
        Header responseHeader = new Header();
        responseHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_SIGNAL_STATE_CHANGE);
        responseHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        Telegram responseTelegram = new Telegram();

        Body[] responseBodys = new Body[CollectorTelegramUtil.RESPONSEALARM_BODY_SIZE];
        int signalCount = signalNames.length;

        // 計測ID
        Body signalNamesBody = new Body();

        signalNamesBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        signalNamesBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        signalNamesBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        signalNamesBody.setIntLoopCount(signalCount);

        // アラームのレベル
        Body signalStateBody = new Body();

        signalStateBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        signalStateBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_LEVEL);
        signalStateBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        signalStateBody.setIntLoopCount(signalCount);
        Integer[] signalState = new Integer[signalCount];

        // アラームの種類
        Body alarmTypeBody = new Body();

        alarmTypeBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        alarmTypeBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_TYPE);
        alarmTypeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        alarmTypeBody.setIntLoopCount(signalCount);
        String[] alarmTypeItems = new String[signalCount];

        // 計測ID、アラーム種類のBodyにAlarmEntryの結果を格納する。
        SignalStateManager manager = SignalStateManager.getInstance();
        for (int cnt = 0; cnt < signalCount; cnt++)
        {
            String signalName = signalNames[cnt];

            AlarmData alarmData = manager.getAlarmData(signalName);
            if (alarmData == null)
            {
                signalState[cnt] = JavelinDataLogger.STOP_ALARM_LEVEL;
            }
            else
            {
                signalState[cnt] = alarmData.getAlarmLevel();
            }

            alarmTypeItems[cnt] = String.valueOf(AlarmType.NONE);
        }
        signalNamesBody.setObjItemValueArr(signalNames);
        signalStateBody.setObjItemValueArr(signalState);
        alarmTypeBody.setObjItemValueArr(alarmTypeItems);

        responseTelegram.setObjHeader(responseHeader);

        responseBodys[0] = signalNamesBody;
        responseBodys[1] = signalStateBody;
        responseBodys[2] = alarmTypeBody;

        responseTelegram.setObjBody(responseBodys);

        return responseTelegram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_SIGNAL_STATE;
    }

}
