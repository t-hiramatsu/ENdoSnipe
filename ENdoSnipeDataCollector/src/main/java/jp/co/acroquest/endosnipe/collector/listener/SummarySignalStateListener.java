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
package jp.co.acroquest.endosnipe.collector.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.manager.SummarySignalStateManager;
import jp.co.acroquest.endosnipe.collector.util.CollectorTelegramUtil;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;

public class SummarySignalStateListener extends AbstractTelegramListener implements
    TelegramListener, LogMessageCodes
{

    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(SummarySignalStateListener.class);

    /**
    * {@inheritDoc}
    */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.log(SUMMARY_SIGNAL_STATE_NOTIFY_RECEIVED);
        }

        // 臒l��`���̖��̈ꗗ���擾����B
        List<SummarySignalDefinitionDto> summarySignalDefitionList =
            getSummarySignalDefinitionList(telegram);

        Telegram responseTelegram = createResponseTelegram(summarySignalDefitionList);

        return responseTelegram;
    }

    /**
    * 臒l��`���̖��̈ꗗ���擾����B
    * @param telegram 臒l��`���d���ꗗ
    * @return 臒l��`���̖��̈ꗗ
    */
    private List<SummarySignalDefinitionDto>
        getSummarySignalDefinitionList(final Telegram telegram)
    {

        List<SummarySignalDefinitionDto> summarySignalDefinitionList =
            new ArrayList<SummarySignalDefinitionDto>();
        /*Body[] bodys = telegram.getObjBody();
        Long[] summarySignalIds = null;

        Body body = bodys[0];
        String objectNameInTelegram = body.getStrObjName();
        String itemNameInTelegram = body.getStrItemName();
        if (TelegramConstants.OBJECTNAME_RESOURCEALARM.equals(objectNameInTelegram) == false)
        {
            return summarySignalDefinitionList;
        }
        if (TelegramConstants.ITEMNAME_SIGNAL_ID.equals(itemNameInTelegram) == false)
        {
            return summarySignalDefinitionList;
        }
        Object[] measurementItemValues = body.getObjItemValueArr();
        int loopCount = body.getIntLoopCount();
        summarySignalIds = getLongValues(loopCount, measurementItemValues);*/

        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();
        Map<Long, SummarySignalDefinitionDto> summarySignalMap =
            summarySignalStateManager.getSummarySignalDefinitionMap();

        // �Ď��Ώۂ�臒l�����`�����擾����B
        for (SummarySignalDefinitionDto summarySignalDefinitionDto : summarySignalMap.values())
        {

            summarySignalDefinitionList.add(summarySignalDefinitionDto);

        }
        return summarySignalDefinitionList;
    }

    /**
    * �d�����當����z����擾����B
    * @param loopCount ���[�v��
    * @param telegramValuesOfobject �ڍ�
    * @return �d������擾����������z��
    */
    private Long[] getLongValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        Long[] telegramValues = new Long[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            Long longValue = (Long)telegramValuesOfobject[cnt];
            telegramValues[cnt] = longValue;
        }
        return telegramValues;
    }

    /**
    * �V�O�i����ԍX�V�d���𐶐�����B
    * @param signalDefitionList 臒l�����`���ꗗ
    * @return �V�O�i����ԍX�V�d��
    */
    private Telegram createResponseTelegram(
        final List<SummarySignalDefinitionDto> summarySignalDefitionList)
    {
        Header responseHeader = new Header();
        // BYTE_TELEGRAM_KIND_ADD_STATE_CHANGE_SUMMARYSIGNAL_DEFINITION
        responseHeader
            .setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_ADD_STATE_CHANGE_SUMMARYSIGNAL_DEFINITION);
        responseHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        Telegram responseTelegram = new Telegram();

        Body[] responseBodys =
            new Body[CollectorTelegramUtil.RESPONSEALARM_BODY_ADD_SUMMARY_SIGNAL_SIZE];
        int summarySignalCount = summarySignalDefitionList.size();
        // summarySignal Id
        Body summarySignalIdBody = new Body();
        summarySignalIdBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        summarySignalIdBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        summarySignalIdBody.setByteItemMode(ItemType.ITEMTYPE_LONG);
        summarySignalIdBody.setIntLoopCount(summarySignalCount);
        Long[] summarySignalIds = new Long[summarySignalCount];

        // 臒l�����`���
        Body summarySignalNamesBody = new Body();

        summarySignalNamesBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        summarySignalNamesBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        summarySignalNamesBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        summarySignalNamesBody.setIntLoopCount(summarySignalCount);
        String[] summarySignalNames = new String[summarySignalCount];

        //summarySignalchild
        Body summarySignalChildListBody = new Body();

        summarySignalChildListBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        summarySignalChildListBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        summarySignalChildListBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        summarySignalChildListBody.setIntLoopCount(summarySignalCount);
        String[] summarySignalLists = new String[summarySignalCount];

        // summarySignal status
        Body summarySignalStatusBody = new Body();
        summarySignalStatusBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        summarySignalStatusBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        summarySignalStatusBody.setByteItemMode(ItemType.ITEMTYPE_INT);
        summarySignalStatusBody.setIntLoopCount(summarySignalCount);
        Integer[] summarySignalsStatus = new Integer[summarySignalCount];

        Body errorMessageBody = new Body();

        errorMessageBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        errorMessageBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        errorMessageBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        errorMessageBody.setIntLoopCount(summarySignalCount);
        String[] errorMessages = new String[summarySignalCount];

        SummarySignalStateManager manager = SummarySignalStateManager.getInstance();
        for (int cnt = 0; cnt < summarySignalCount; cnt++)
        {
            SummarySignalDefinitionDto summarySignalDefition = summarySignalDefitionList.get(cnt);
            Long summarySignalId = summarySignalDefitionList.get(cnt).getSummarySignalId();
            String summarySignalName = summarySignalDefition.getSummarySignalName();
            String summarySignalList = summarySignalDefition.getSignalList().toString();
            int summarySignalStatus = summarySignalDefition.getSummarySignalStatus();
            String errorMessage = summarySignalDefition.getErrorMessage();
            // AlarmData alarmData = manager.getAlarmData(summarySignalName);

            summarySignalIds[cnt] = summarySignalId;
            summarySignalNames[cnt] = summarySignalName;
            summarySignalLists[cnt] = summarySignalList;
            summarySignalsStatus[cnt] = summarySignalStatus;
            errorMessages[cnt] = errorMessage;
        }
        summarySignalIdBody.setObjItemValueArr(summarySignalIds);
        summarySignalNamesBody.setObjItemValueArr(summarySignalNames);
        summarySignalChildListBody.setObjItemValueArr(summarySignalLists);
        summarySignalStatusBody.setObjItemValueArr(summarySignalsStatus);
        errorMessageBody.setObjItemValueArr(errorMessages);

        //alarmStateBody.setObjItemValueArr(summarySignalState);
        // alarmTypeBody.setObjItemValueArr(alarmTypeItems);
        //summarySignalLevelBody.setObjItemValueArr(summarySignalLevel);

        responseTelegram.setObjHeader(responseHeader);

        responseBodys[0] = summarySignalIdBody;
        responseBodys[1] = summarySignalNamesBody;
        responseBodys[2] = summarySignalChildListBody;
        responseBodys[3] = summarySignalStatusBody;
        responseBodys[4] = errorMessageBody;

        /* responseBodys[0] = summarySignalNamesBody;
         responseBodys[1] = alarmStateBody;
         responseBodys[2] = summarySignalLevelBody;
         responseBodys[3] = alarmTypeBody;
         responseBodys[4] = alarmTypeBody;*/

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
        return TelegramConstants.BYTE_TELEGRAM_KIND_SUMMARYSIGNAL_STATE;//ADD_STATE_CHANGE_SUMMARYSIGNAL_DEFINITION;
    }
}
