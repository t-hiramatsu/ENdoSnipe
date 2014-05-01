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
package jp.co.acroquest.endosnipe.web.explorer.listener.collector;

import java.util.Date;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;

import org.wgp.exception.WGPRuntimeException;
import org.wgp.manager.WgpDataManager;

/**
 * To receive telegram for thread dump
 * @author khinewai
 *
 */
public class AlarmThreadDumpNotifyListener extends AbstractTelegramListener
{
    /** ロガークラス */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(AlarmThreadDumpNotifyListener.class);

    /**
     * this is default constructor
     */
    public AlarmThreadDumpNotifyListener()
    {

    }

    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        Object wgpData = new Object();
        Date date = new Date();
        String data = date.toString();
        try
        {
            if (dataManager != null)
            {
                dataManager.setData("JvnLog_Notify", data, wgpData);
            }
        }
        catch (WGPRuntimeException exception)
        {
            LOGGER.log("WECC0301", exception, exception.getMessage());
            return null;
        }
        return null;
    }

    /**
     * this is threadDump kind
     * @return the constant number for request kind
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }

    /**
     * this is threadDump number
     *  @return the constant number for telegram kind
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_THREAD_DUMP;
    }
}
