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
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

/**
 * SummarySignal定義変更要求電文を受信するためのクラスです。<br />
 * 
 * @author pin
 */
public class SummarySignalChangeListener extends AbstractTelegramListener implements
    TelegramListener, LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(SummarySignalChangeListener.class);

    private static final int ITEM_VALUE_SUMMARY_SIGNAL_NAME = 0;

    private static final int ITEM_VALUE_SIGNAL_LIST_NAME = 1;

    private static final int ITEM_VALUE_SUMMARY_SIGNAL_TYPE = 2;

    private String summaryProcess_ = "";

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
            summaryProcess_ = bodys[0].getStrItemName();
        }
        else
        {
            summarySignalDefinitionDto = updateSummarySignalDefinition(telegram);
            if (summarySignalDefinitionDto.get(0).getErrorMessage().equals(""))
            {
                if (!bodys[0].getStrItemName()
                    .equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE))
                {
                    List<SummarySignalDefinitionDto> summarySignalDefinitionDtoList =
                        SignalSummarizer.getInstance()
                            .calculateChangeSummarySignalState(summarySignalDefinitionDto,
                                                               bodys[0].getStrItemName());
                    if (summarySignalDefinitionDtoList != null
                        && summarySignalDefinitionDtoList.size() > 0)
                    {
                        summarySignalDefinitionDto = summarySignalDefinitionDtoList;
                    }
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

        Telegram responseTelegram =
            CollectorTelegramUtil.createSummarySignalResponseTelegram(summarySignalDefinitionDto,
                                                                      summaryProcess_);
        summaryProcess_ = "";
        return responseTelegram;
    }

    /**
     * Get all Summary Signal List from the map
     * @return Summary Signal Definition List
     */
    private List<SummarySignalDefinitionDto>
        getSummarySignalDefinitionList(final Telegram telegram)
    {

        List<SummarySignalDefinitionDto> summarySignalDefinitionList =
            new ArrayList<SummarySignalDefinitionDto>();

        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();
        Map<Long, SummarySignalDefinitionDto> summarySignalMap =
            summarySignalStateManager.getSummarySignalDefinitionMap();

        for (SummarySignalDefinitionDto summarySignalDefinitionDto : summarySignalMap.values())
        {

            summarySignalDefinitionList.add(summarySignalDefinitionDto);

        }
        return summarySignalDefinitionList;
    }

    /**
     * Update Summary Signal Definition
     * @param telegram of summary signal data
     * @return Summary Signal Definition List
     */
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
            summaryProcess_ = itemName;
        }
        summaryDefinitionDtoList.add(summarySignalDefinitionDto);
        return summaryDefinitionDtoList;
    }

    /**
     * Process for dividing the add or delete
     * @param body Body of Telegram
     * @param telegram of summary signal data
     * @return Summary Signal Definition List
     */
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
            summarySignalDefinition.errorMessage = "";

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
            return summarySignalDefinitionDto;

        }
    }

    /**
     * Delete the summary signal definition
     * @param body {@link Body}
     * @return SummarySignalDefinitionDto
     */
    private SummarySignalDefinitionDto deleteSignalDefinition(final Body body)
    {
        SummarySignalStateManager summarySignalStateManager =
            SummarySignalStateManager.getInstance();

        Object[] itemValues = body.getObjItemValueArr();
        String summarySignalName = (String)itemValues[ITEM_VALUE_SUMMARY_SIGNAL_NAME];
        SummarySignalDefinition sumDef = new SummarySignalDefinition();
        sumDef.summarySignalName = summarySignalName;

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
