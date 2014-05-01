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

import jp.co.acroquest.endosnipe.collector.JavelinDataQueue;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.data.JavelinMeasurementNotifyData;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * リソース通知通知電文受信用のリスナクラスです。<br />
 * 受信したリソース情報をデータベース格納用のキューへ追加します。<br />
 * 
 * @author fujii
 * @author nagai
 * @author y-komori
 */
public class SystemResourceNotifyListener extends SystemResourceListener implements
    TelegramListener, LogMessageCodes, CommonLogMessageCodes
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER;
    static
    {
        LOGGER = ENdoSnipeLogger.getLogger(SystemResourceNotifyListener.class);
    }

    /**
     * {@link SystemResourceNotifyListener} を構築します。<br />
     * @param queue キュー
     */
    public SystemResourceNotifyListener(final JavelinDataQueue queue)
    {
        super(queue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        ResourceData resourceData =
            ResourceNotifyAccessor.createResourceData(telegram, this.getDatabaseName(),
                                                      this.getAgentName());
        resourceData.hostName = this.getHostName();
        resourceData.ipAddress = this.getIpAddress();
        resourceData.portNum = this.getPort();
        resourceData.clientId = this.getClientId();

        if (LOGGER.isDebugEnabled() == true)
        {
            LOGGER.log(RESOURCE_NOTIFY_RECEIVED, resourceData.hostName, resourceData.ipAddress,
                       resourceData.portNum);
        }

        // 計測値データをキューへ格納する
        JavelinMeasurementNotifyData measurementData =
            new JavelinMeasurementNotifyData(resourceData);
        setProperties(measurementData, telegram.getObjHeader().getId());
        this.getQueue().offer(measurementData);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }
}
