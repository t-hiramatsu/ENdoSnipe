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
package jp.co.acroquest.endosnipe.web.dashboard.listener.collector;

import java.util.Date;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;

import org.wgp.manager.WgpDataManager;

/**
 * To receive telegram for thread dump
 * @author khinewai
 *
 */
public class AlarmThreadDumpNotifyListener extends AbstractTelegramListener
{
    public AlarmThreadDumpNotifyListener()
    {

    }

    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        /* List<JavelinLog> javelinLog = new ArrayList<JavelinLog>();
         ThreadDumpService threadDumpService = new ThreadDumpService();
         DatabaseManager dbManager = DatabaseManager.getInstance();
         String dbName = dbManager.getDataBaseName(1);
         try
         {
             javelinLog = JavelinLogDao.selectAllThreadDump(dbName);
         }
         catch (SQLException ex)
         {
             ex.printStackTrace();
         }
         List<ThreadDumpDefinitionDto> definitionDto =
                 threadDumpService.createThreadDumpDefinitionDto(javelinLog);
         EventManager eventManager = EventManager.getInstance();
         WgpDataManager dataManager = eventManager.getWgpDataManager();
         ResourceSender resourceSender = eventManager.getResourceSender();
         if (dataManager == null || resourceSender == null)
         {
             return null;
         }
        */
        //   resourceSender.send(definitionDto);
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        //  dataManager.setData("JvnLog_Notify", new Date(), null);
        Object wgpData = null;
        String date = new Date().toString();
        dataManager.setData("JvnLog_Notify", date, wgpData);

        return null;
    }

    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }

    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_THREAD_DUMP;
    }
}
