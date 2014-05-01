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
package jp.co.acroquest.endosnipe.web.dashboard.service.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ConnectionClient;

/**
 * エージェント一覧取得要求を処理するクラスです。
 * @author ochiai
 *
 */
public class ResourceStateAllProcessor implements EventProcessor
{
    /**
     * コンストラクタ
     */
    public ResourceStateAllProcessor()
    {

    }

    /**
     * {@inheritDoc}
     */
    public void process(final HttpServletRequest request, final HttpServletResponse response)
    {
        // 電文の作成
        Header header = new Header();
        header.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_SIGNAL_STATE_CHANGE);
        header.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_REQUEST);
        Telegram telegram = new Telegram();
        telegram.setObjHeader(header);

        // 電文のDataCollector への送信
        sendTelegram(telegram);

    }

    /**
     * 電文をDataCollectorに送信する
     * @param telegram 電文
     */
    private void sendTelegram(final Telegram telegram)
    {
        // コミュニケーションクライアントのリストを取得
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();

        // リソース取得要求を送る
        for (CommunicationClient client : clientList)
        {
            client.sendTelegram(telegram);
        }
    }
}
