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
package jp.co.acroquest.endosnipe.collector.util;

import java.util.List;

import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.collector.processor.AlarmType;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;

/**
 * データコレクターで用いる電文のユーティリティ
 * 
 * @author fujii
 */
public class CollectorTelegramUtil
{
    /**  閾値超過アラームの電文本体のサイズ */
    public static final int RESPONSEALARM_BODY_SIZE = 4;

    /** size of telegram for summary signal */
    public static final int RESPONSEALARM_BODY_ADD_SUMMARY_SIGNAL_SIZE = 6;

    /** index for telegram of summary signal */
    public static final int SUMMARY_SIGNAL_INDEX1 = 3;

    /** index for telegram of summary signal */
    public static final int SUMMARY_SIGNAL_INDEX2 = 4;

    /** index for telegram of summary signal */
    public static final int SUMMARY_SIGNAL_INDEX3 = 5;

    /**
     * インスタンス化を防止するprivateコンストラクタです。
     */
    private CollectorTelegramUtil()
    {
        // Do Nothing.
    }

    /**
     * 閾値超過アラーム通知電文を作成します。
     * @param alarmEntryList {@link AlarmEntry}のリストオブジェクト
     * @return 閾値超過アラーム通知電文
     */
    public static Telegram createAlarmTelegram(final List<AlarmEntry> alarmEntryList)
    {
        return createAlarmTelegram(alarmEntryList, TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);
    }

    /**
     * 閾値超過アラーム通知電文を作成します。
     * @param alarmEntryList {@link AlarmEntry}のリストオブジェクト
     * @param requestKind リクエストの種類
     * @return 閾値超過アラーム通知電文
     */
    public static Telegram createAlarmTelegram(final List<AlarmEntry> alarmEntryList,
        final byte requestKind)
    {
        Header responseHeader = new Header();
        responseHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_SIGNAL_STATE_CHANGE);
        responseHeader.setByteRequestKind(requestKind);

        Telegram responseTelegram = new Telegram();

        Body[] responseBodys = new Body[CollectorTelegramUtil.RESPONSEALARM_BODY_SIZE];
        int alarmEntrySize = alarmEntryList.size();

        // 計測ID
        Body measurementTypeBody = new Body();

        measurementTypeBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        measurementTypeBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        measurementTypeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        measurementTypeBody.setIntLoopCount(alarmEntrySize);
        String[] measurementTypeItems = new String[alarmEntrySize];

        // アラームのレベル
        Body alarmLevelBody = new Body();

        alarmLevelBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        alarmLevelBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_STATE);
        alarmLevelBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        alarmLevelBody.setIntLoopCount(alarmEntrySize);
        Integer[] alarmLevelItems = new Integer[alarmEntrySize];

        // アラーム発生時の閾値状態
        Body signalLevelBody = new Body();

        signalLevelBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        signalLevelBody.setStrItemName(TelegramConstants.ITEMNAME_SIGNAL_LEVEL);
        signalLevelBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        signalLevelBody.setIntLoopCount(alarmEntrySize);
        Integer[] signalLevel = new Integer[alarmEntrySize];

        // アラームの種類
        Body alarmTypeBody = new Body();

        alarmTypeBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        alarmTypeBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_TYPE);
        alarmTypeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        alarmTypeBody.setIntLoopCount(alarmEntrySize);
        String[] alarmTypeItems = new String[alarmEntrySize];

        // 計測ID、アラーム種類のBodyにAlarmEntryの結果を格納する。
        for (int cnt = 0; cnt < alarmEntrySize; cnt++)
        {
            AlarmEntry alarmEntry = alarmEntryList.get(cnt);
            String itemId = alarmEntry.getAlarmID();
            measurementTypeItems[cnt] = itemId;
            signalLevel[cnt] = alarmEntry.getSignalLevel();

            int alarmLevel = alarmEntry.getAlarmState();
            alarmLevelItems[cnt] = Integer.valueOf(alarmLevel);

            AlarmType alarmType = alarmEntry.getAlarmType();
            alarmTypeItems[cnt] = String.valueOf(alarmType);

        }
        measurementTypeBody.setObjItemValueArr(measurementTypeItems);
        alarmLevelBody.setObjItemValueArr(alarmLevelItems);
        signalLevelBody.setObjItemValueArr(signalLevel);
        alarmTypeBody.setObjItemValueArr(alarmTypeItems);

        responseTelegram.setObjHeader(responseHeader);

        responseBodys[0] = measurementTypeBody;
        responseBodys[1] = alarmLevelBody;
        responseBodys[2] = signalLevelBody;
        responseBodys[SUMMARY_SIGNAL_INDEX1] = alarmTypeBody;

        responseTelegram.setObjBody(responseBodys);

        return responseTelegram;
    }

    /**
     * create response telegram for summary signal data
     * @param summarySignalDefinitionList list of summary signal data
     * @param process kind of telegram
     * @return telegram of summarysignal
     */
    public static Telegram createSummarySignalResponseTelegram(
        final List<SummarySignalDefinitionDto> summarySignalDefinitionList, final String process)
    {
        Header responseHeader = new Header();
        responseHeader
            .setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_ADD_STATE_CHANGE_SUMMARYSIGNAL_DEFINITION);
        responseHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        int summarySignalCount = summarySignalDefinitionList.size();
        Telegram responseTelegram = new Telegram();

        Body[] responseBodys =
            new Body[CollectorTelegramUtil.RESPONSEALARM_BODY_ADD_SUMMARY_SIGNAL_SIZE];

        Body summarySignalProcessBody = new Body();
        summarySignalProcessBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalProcessBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_TYPE);
        summarySignalProcessBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        summarySignalProcessBody.setIntLoopCount(summarySignalCount);
        String[] summarySignalProcess = new String[summarySignalCount];//{summaryProcess};

        Body summarySignalIDBody = new Body();

        summarySignalIDBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalIDBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ID);
        summarySignalIDBody.setByteItemMode(ItemType.ITEMTYPE_LONG);
        summarySignalIDBody.setIntLoopCount(summarySignalCount);
        Long[] summarySignalID = new Long[summarySignalCount];

        Body summarySignalNameBody = new Body();

        summarySignalNameBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalNameBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_NAME);
        summarySignalNameBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        summarySignalNameBody.setIntLoopCount(summarySignalCount);
        String[] summarySignalNameList = new String[summarySignalCount];

        Body signalListBody = new Body();

        signalListBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        signalListBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_CHILDLIST);
        signalListBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        signalListBody.setIntLoopCount(summarySignalCount);
        String[] childList = new String[summarySignalCount];

        Body summarySignalStateBody = new Body();
        summarySignalStateBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalStateBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_STATUS);
        summarySignalStateBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        summarySignalStateBody.setIntLoopCount(summarySignalCount);
        Integer[] summarySignalState = new Integer[summarySignalCount];

        Body errorMessageBody = new Body();

        errorMessageBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        errorMessageBody.setStrItemName(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ERROR);
        errorMessageBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        errorMessageBody.setIntLoopCount(summarySignalCount);
        String[] errorMessages = new String[summarySignalCount];

        for (int cnt = 0; cnt < summarySignalCount; cnt++)
        {
            SummarySignalDefinitionDto summarySignalDefinition =
                summarySignalDefinitionList.get(cnt);
            Long summarySignalId = summarySignalDefinition.getSummarySignalId();
            String summarySignalName = summarySignalDefinition.getSummarySignalName();

            String summarySignalList = "";
            if (summarySignalDefinition.getSignalList() != null
                && summarySignalDefinition.getSignalList().size() > 0)
            {
                for (int count = 0; count < summarySignalDefinition.getSignalList().size(); count++)
                {
                    summarySignalList += summarySignalDefinition.getSignalList().get(count);
                    if (count != summarySignalDefinition.getSignalList().size() - 1)
                    {
                        summarySignalList += ",";
                    }
                }
            }

            int summarySignalStatus = summarySignalDefinition.getSummarySignalStatus();
            String errorMessage = summarySignalDefinition.getErrorMessage();
            if (errorMessage == null)
            {
                errorMessage = "";
            }
            summarySignalProcess[cnt] = process;
            summarySignalID[cnt] = summarySignalId;
            summarySignalNameList[cnt] = summarySignalName;
            summarySignalState[cnt] = summarySignalStatus;
            childList[cnt] = summarySignalList;
            errorMessages[cnt] = errorMessage;
        }

        summarySignalProcessBody.setObjItemValueArr(summarySignalProcess);
        summarySignalIDBody.setObjItemValueArr(summarySignalID);
        summarySignalNameBody.setObjItemValueArr(summarySignalNameList);
        signalListBody.setObjItemValueArr(childList);
        summarySignalStateBody.setObjItemValueArr(summarySignalState);
        errorMessageBody.setObjItemValueArr(errorMessages);

        responseTelegram.setObjHeader(responseHeader);

        responseBodys[0] = summarySignalProcessBody;
        responseBodys[1] = summarySignalIDBody;
        responseBodys[2] = summarySignalNameBody;
        responseBodys[SUMMARY_SIGNAL_INDEX1] = signalListBody;
        responseBodys[SUMMARY_SIGNAL_INDEX2] = summarySignalStateBody;
        responseBodys[SUMMARY_SIGNAL_INDEX3] = errorMessageBody;

        responseTelegram.setObjBody(responseBodys);
        return responseTelegram;
    }
}
