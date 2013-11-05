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
package jp.co.acroquest.endosnipe.web.dashboard.util;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SummarySignalDefinitionDto;

/**
 * SummarySignalのUtilクラスです。
 * @author khine wai, pin
 *
 */
public class SummarySignalUtil
{

    /** Count for summary signal add and update process */
    private static final int SUMMARY_SIGNAL_ARGUMENT_COUNT = 3;

    /** Count for summary signal delete process */
    private static final int SUMMARY_SIGNAL_DELETE_COUNT = 1;

    /**
     * インスタンス化を阻止するprivateコンストラクタです。
     */
    private SummarySignalUtil()
    {
        // Do Nothing.
    }

    /**
     * For telegram add and update process
     * 閾値判定定義情報の追加を通知する電文を作成する。
     * 
     * @param summarySignalDefinitionDto summarySignal定義情報のリスト
     * @param itemName 項目名
     * @return 全閾値情報を取得する電文
     */
    public static Telegram createAddSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinitionDto, final String itemName)
    {

        Telegram telegram = new Telegram();
        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_SUMMARYSIGNAL_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        Body summarySignalBody = new Body();
        summarySignalBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalBody.setStrItemName(itemName);
        summarySignalBody.setByteItemMode(ItemType.ITEMTYPE_STRING);

        if (summarySignalDefinitionDto != null)
        {
            String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
            String signalList = "";
            if (summarySignalDefinitionDto.getSignalList() != null
                    && summarySignalDefinitionDto.getSignalList().size() > 0)
            {
                for (int count = 0; count < summarySignalDefinitionDto.getSignalList().size(); count++)
                {
                    signalList += summarySignalDefinitionDto.getSignalList().get(count);
                    if (count != summarySignalDefinitionDto.getSignalList().size() - 1)
                    {
                        signalList += ",";
                    }
                }
            }

            int summarySignalType = summarySignalDefinitionDto.getSummarySignalType();

            int dtoCount = SUMMARY_SIGNAL_ARGUMENT_COUNT;
            summarySignalBody.setIntLoopCount(dtoCount);
            String[] summarySignalDefObj =
                    { String.valueOf(summarySignalName), signalList,
                            String.valueOf(summarySignalType) };
            summarySignalBody.setObjItemValueArr(summarySignalDefObj);
        }
        Body[] requestBodys = { summarySignalBody };
        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }

    /**
     * For telegram delete process
     * 閾値判定定義情報の追加を通知する電文を作成する。
     * 
     * @param summarySignalDefinitionDto summarySignal定義情報のリスト
     * @param itemName 項目名
     * @return 全閾値情報を取得する電文
     */
    public static Telegram createDeleteSummarySignalDefinition(
            final SummarySignalDefinitionDto summarySignalDefinitionDto, final String itemName)
    {
        Telegram telegram = new Telegram();
        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_SUMMARYSIGNAL_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        Body summarySignalBody = new Body();
        summarySignalBody.setStrObjName(TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE);
        summarySignalBody.setStrItemName(itemName);
        summarySignalBody.setByteItemMode(ItemType.ITEMTYPE_STRING);

        String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
        int dtoCount = SUMMARY_SIGNAL_DELETE_COUNT;
        summarySignalBody.setIntLoopCount(dtoCount);
        String[] summarySignalDefObj = { String.valueOf(summarySignalName) };
        summarySignalBody.setObjItemValueArr(summarySignalDefObj);

        Body[] requestBodys = { summarySignalBody };
        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }
}
