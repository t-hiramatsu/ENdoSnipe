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
package jp.co.acroquest.endosnipe.javelin.communicate.dump;

import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.accessor.JvnFileNotifyAccessor.JvnFileEntry;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.javelin.communicate.SimpleENdoSnipeClient;

/**
 * スレッドダンプ取得クライアント
 * 
 * @author eriguchi
 */
public class ThreadDumpClient extends SimpleENdoSnipeClient
{
    /** デフォルトのインターバル */
    private static final int DEFAULT_INTERVAL = 1000;
    
    /** Jvnログ取得応答 */
    private JvnFileNotifyListener listener_;

    /**
     * スレッドダンプクライアント。
     */
    public ThreadDumpClient()
    {
        super("ThreadDumpClient");
        listener_ = new JvnFileNotifyListener(timeoutObject_);
        client_.addTelegramListener(listener_);
    }

    /**
     * エントリポイント。
     * 
     * @param args <host> <port> [interval(msec)] [count]
     */
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("usage : <Main Class> <host> <port> [interval(msec)] [count]");
            return;
        }

        int argIndex = 0;
        String hostName = args[argIndex++];
        int port;
        try
        {
            port = Integer.parseInt(args[argIndex]);
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
            System.out.println("illegal port number : " + args[argIndex]);
            return;
        }
        argIndex++;

        long interval = DEFAULT_INTERVAL;
        if (args.length > argIndex)
        {
            try
            {
                interval = Integer.parseInt(args[argIndex]);
            }
            catch (NumberFormatException nfe)
            {
                nfe.printStackTrace();
                System.out.println("illegal interval : " + args[argIndex]);
            }
        }
        argIndex++;

        int count = Integer.MAX_VALUE;
        if (args.length > argIndex)
        {
            try
            {
                count = Integer.parseInt(args[argIndex]);
            }
            catch (NumberFormatException nfe)
            {
                nfe.printStackTrace();
                System.out.println("illegal count : " + args[argIndex]);
            }
        }

        ThreadDumpClient client = new ThreadDumpClient();

        boolean success = client.connect(hostName, port);
        try
        {
            if (success)
            {
                client.dump(count, interval);
            }
        }
        finally
        {
            client.disconnect();
        }
    }

    /**
     * 指定した回数、ThreadDumpを要求する。
     * @param countMax 回数。
     * @param interval 間隔(msec)
     */
    private void dump(int countMax, long interval)
    {
        for (int count = 0; count < countMax; count++)
        {
            if (count != 0)
            {
                try
                {
                    Thread.sleep(interval);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }

            dump();

            if (countMax == Integer.MAX_VALUE)
            {
                count = 1;
            }
        }
    }

    /**
     * ダンプを1回実行する。
     */
    private void dump()
    {
        Telegram telegram = TelegramCreator.createThreadDumpRequestTelegram();
        this.client_.sendTelegram(telegram);

        // 応答を待つ。
        if (listener_.getEntries() == null)
        {
            try
            {
                synchronized (this.timeoutObject_)
                {
                    this.timeoutObject_.wait(TIMEOUT_MILLIS);
                }
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            if (listener_.getEntries() == null)
            {
                System.out.println("thread dump timeout");
                return;
            }
        }

        for (JvnFileEntry entry : listener_.getEntries())
        {
            System.out.println(entry.contents);
        }
    }
}
