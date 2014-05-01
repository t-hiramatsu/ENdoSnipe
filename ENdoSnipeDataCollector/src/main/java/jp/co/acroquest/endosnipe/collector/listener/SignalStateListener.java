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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.manager.SignalStateManager;
import jp.co.acroquest.endosnipe.collector.util.CollectorTelegramUtil;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * Javelin シグナル状態取得要求電文を受信するためのクラスです。<br />
 * 
 * @author fujii
 */
public class SignalStateListener extends AbstractTelegramListener implements TelegramListener,
    LogMessageCodes
{
    /** LOGGER */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(SignalStateListener.class);

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
        List<SignalDefinitionDto> signalDefitionList = getSignalDefinitionList(telegram);

        Telegram responseTelegram =
            CollectorTelegramUtil.createResponseTelegram(signalDefitionList);

        return responseTelegram;
    }

    /**
     * 閾値定義情報の名称一覧を取得する。
     * @param telegram 閾値定義情報電文一覧
     * @return 閾値定義情報の名称一覧
     */
    private List<SignalDefinitionDto> getSignalDefinitionList(final Telegram telegram)
    {
        List<SignalDefinitionDto> signalDefinitionList = new ArrayList<SignalDefinitionDto>();
        Body[] bodys = telegram.getObjBody();
        Long[] signalIds = null;

        Body body = bodys[0];
        String objectNameInTelegram = body.getStrObjName();
        String itemNameInTelegram = body.getStrItemName();
        if (TelegramConstants.OBJECTNAME_RESOURCEALARM.equals(objectNameInTelegram) == false)
        {
            return signalDefinitionList;
        }
        if (TelegramConstants.ITEMNAME_SIGNAL_ID.equals(itemNameInTelegram) == false)
        {
            return signalDefinitionList;
        }
        Object[] measurementItemValues = body.getObjItemValueArr();
        int loopCount = body.getIntLoopCount();
        signalIds = getLongValues(loopCount, measurementItemValues);

        SignalStateManager signalStateManager = SignalStateManager.getInstance();
        Map<Long, SignalDefinitionDto> signalMap = signalStateManager.getSignalDeifinitionMap();

        // 監視対象の閾値判定定義情報を取得する。
        for (Long signalId : signalIds)
        {
            SignalDefinitionDto signalDefinitionDto = signalMap.get(signalId);
            if (signalDefinitionDto != null)
            {
                signalDefinitionList.add(signalDefinitionDto);
            }
        }
        return signalDefinitionList;
    }

    /**
     * 電文から文字列配列を取得する。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 詳細
     * @return 電文から取得した文字列配列
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
