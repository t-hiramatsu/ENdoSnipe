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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.manager.SummarySignalStateManager;
import jp.co.acroquest.endosnipe.collector.util.CollectorTelegramUtil;
import jp.co.acroquest.endosnipe.collector.util.SignalSummarizer;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

public class SummarySignalChangeListener extends AbstractTelegramListener implements
    TelegramListener, LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(SummarySignalChangeListener.class);

    private static final int ITEM_VALUE_SUMMARY_SIGNAL_NAME = 0;

    private static final int ITEM_VALUE_SIGNAL_LIST_NAME = 1;

    private static final int ITEM_VALUE_SUMMARY_SIGNAL_TYPE = 2;

    private String summaryProcess = "";

    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.log(SUMMARY_SIGNAL_DEFINITION_CHANGE_NOTIFY_RECEIVED);
        }
        Body[] bodys = telegram.getObjBody();
        List<SummarySignalDefinitionDto> summarySignalDefinitionDto = null;
        if (bodys[0].getStrItemName().equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ALL))
        {
            summarySignalDefinitionDto = getSummarySignalDefinitionList(telegram);
            summaryProcess = bodys[0].getStrItemName();
        }
        else
        {
            // �d������͂��A�V�O�i����`�����X�V����B
            summarySignalDefinitionDto = updateSummarySignalDefinition(telegram);
            if (summarySignalDefinitionDto.get(0).getErrorMessage().equals(""))
            {
                if (!bodys[0].getStrItemName()
                    .equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE))
                {
                    summarySignalDefinitionDto =
                        SignalSummarizer.getInstance()
                            .calculateChangeSummarySignalState(summarySignalDefinitionDto,
                                                               bodys[0].getStrItemName());
                }
                else if (bodys[0].getStrItemName()
                    .equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE))
                {
                    summarySignalDefinitionDto
                        .addAll(SignalSummarizer
                            .getInstance()
                            .calculateChangeSummarySignalState(summarySignalDefinitionDto,
                                                               TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE));
                }
            }
        }

        //        SignalSummarizer.getInstance()
        //            .calculateChangeSummarySignalState(summarySignalDefinitionDto);
        Telegram responseTelegram =
            CollectorTelegramUtil
                .createResponseTelegram(summarySignalDefinitionDto, summaryProcess);
        summaryProcess = "";
        return responseTelegram;
    }

    public Telegram createResponseTelegram(
        final List<SummarySignalDefinitionDto> summarySignalDefinitionList)
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
        String[] childList = new String[summarySignalCount];//{signalList};

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
            // AlarmData alarmData = manager.getAlarmData(summarySignalName);

            summarySignalProcess[cnt] = summaryProcess;
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
        responseBodys[3] = signalListBody;
        responseBodys[4] = summarySignalStateBody;
        responseBodys[5] = errorMessageBody;

        responseTelegram.setObjBody(responseBodys);
        summaryProcess = "";
        return responseTelegram;
    }

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

    private List<SummarySignalDefinitionDto> updateSummarySignalDefinition(final Telegram telegram)
    {
        Body[] bodys = telegram.getObjBody();
        List<SummarySignalDefinitionDto> summaryDefinitionDtoList =
            new ArrayList<SummarySignalDefinitionDto>();
        SummarySignalDefinitionDto summarySignalDefinitionDto = null;
        for (Body body : bodys)
        {
            String itemName = body.getStrItemName();
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD.equals(itemName)
                || TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE.equals(itemName))
            {
                summarySignalDefinitionDto = setSummarySignalDefinition(body, itemName);

            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE.equals(itemName))
            {
                summarySignalDefinitionDto = deleteSignalDefinition(body);
            }
            summaryProcess = itemName;
            //This is for all get Process
            //  this.getAllSummarySignalDefinition();
        }
        summaryDefinitionDtoList.add(summarySignalDefinitionDto);
        return summaryDefinitionDtoList;
    }

    private void getAllSummarySignalDefinition()
    {
        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();
        List<SummarySignalDefinition> summarySignalDefinition =
            summarySignalStateManager.getAllSummarySignalDefinition();

    }

    private SummarySignalDefinitionDto setSummarySignalDefinition(final Body body,
        final String itemName)
    {
        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();
        SummarySignalDefinitionDto summarySignalDefinitionDto = null;
        Object[] itemValues = body.getObjItemValueArr();
        String summarySignalName = (String)itemValues[ITEM_VALUE_SUMMARY_SIGNAL_NAME];
        String signalLists = (String)itemValues[ITEM_VALUE_SIGNAL_LIST_NAME];
        String summarySignalType = (String)itemValues[ITEM_VALUE_SUMMARY_SIGNAL_TYPE];

        try
        {

            SummarySignalDefinition summarySignalDefinition = new SummarySignalDefinition();
            summarySignalDefinition.summarySignalName = summarySignalName;
            summarySignalDefinition.summarySignalType = Integer.parseInt(summarySignalType);

            List<String> signalList = new ArrayList<String>(Arrays.asList(signalLists.split(",")));
            summarySignalDefinition.signalList = signalList;
            summarySignalDefinitionDto = new SummarySignalDefinitionDto(summarySignalDefinition);
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD.equals(itemName))
            {
                summarySignalStateManager.addSummarySignalDefinition(summarySignalDefinitionDto);
            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE.equals(itemName))
            {

                summarySignalStateManager.updateSummarySignalDefinition(summarySignalDefinitionDto);
            }
            return summarySignalDefinitionDto;
        }
        catch (NumberFormatException ex)
        {
            // �V�O�i����`���̒ǉ��Ɏ��s
            return summarySignalDefinitionDto;

        }
    }

    /**
     * 
     * @param body {@link Body}�I�u�W�F�N�g
     * @param itemName ���ږ�
     */
    private SummarySignalDefinitionDto deleteSignalDefinition(final Body body)
    {
        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();

        Object[] itemValues = body.getObjItemValueArr();
        //String signalIdStr = (String)itemValues[ITEM_VALUE_SIGNAL_ID];
        String summarySignalName = (String)itemValues[ITEM_VALUE_SUMMARY_SIGNAL_NAME];
        SummarySignalDefinition sumDef = new SummarySignalDefinition();
        sumDef.summarySignalName = summarySignalName;
        SummarySignalDefinitionDto summarySignalDefinitionDto =
            new SummarySignalDefinitionDto(sumDef);

        return summarySignalStateManager.deleteSummarySignalDefinition(summarySignalName);

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
        return TelegramConstants.BYTE_TELEGRAM_KIND_SUMMARYSIGNAL_DEFINITION;
    }

}
