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

/**
 * �f�[�^�R���N�^�[�ŗp����d���̃��[�e�B���e�B
 * 
 * @author fujii
 */
public class CollectorTelegramUtil
{
    /**  臒l���߃A���[���̓d���{�̂̃T�C�Y */
    public static final int RESPONSEALARM_BODY_SIZE = 4;

    /**
     * �C���X�^���X����h�~����private�R���X�g���N�^�ł��B
     */
    private CollectorTelegramUtil()
    {
        // Do Nothing.
    }

    /**
     * 臒l���߃A���[���ʒm�d�����쐬���܂��B
     * @param alarmEntryList {@link AlarmEntry}�̃��X�g�I�u�W�F�N�g
     * @return 臒l���߃A���[���ʒm�d��
     */
    public static Telegram createAlarmTelegram(final List<AlarmEntry> alarmEntryList)
    {
        return createAlarmTelegram(alarmEntryList, TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);
    }

    /**
     * 臒l���߃A���[���ʒm�d�����쐬���܂��B
     * @param alarmEntryList {@link AlarmEntry}�̃��X�g�I�u�W�F�N�g
     * @param requestKind ���N�G�X�g�̎��
     * @return 臒l���߃A���[���ʒm�d��
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

        // �v��ID
        Body measurementTypeBody = new Body();

        measurementTypeBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        measurementTypeBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        measurementTypeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        measurementTypeBody.setIntLoopCount(alarmEntrySize);
        String[] measurementTypeItems = new String[alarmEntrySize];

        // �A���[���̃��x��
        Body alarmLevelBody = new Body();

        alarmLevelBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        alarmLevelBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_STATE);
        alarmLevelBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        alarmLevelBody.setIntLoopCount(alarmEntrySize);
        Integer[] alarmLevelItems = new Integer[alarmEntrySize];

        // �A���[����������臒l���
        Body signalLevelBody = new Body();

        signalLevelBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        signalLevelBody.setStrItemName(TelegramConstants.ITEMNAME_SIGNAL_LEVEL);
        signalLevelBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        signalLevelBody.setIntLoopCount(alarmEntrySize);
        Integer[] signalLevel = new Integer[alarmEntrySize];

        // �A���[���̎��
        Body alarmTypeBody = new Body();

        alarmTypeBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        alarmTypeBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_TYPE);
        alarmTypeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        alarmTypeBody.setIntLoopCount(alarmEntrySize);
        String[] alarmTypeItems = new String[alarmEntrySize];

        // �v��ID�A�A���[����ނ�Body��AlarmEntry�̌��ʂ��i�[����B
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
        responseBodys[3] = alarmTypeBody;

        responseTelegram.setObjBody(responseBodys);

        return responseTelegram;
    }

}
