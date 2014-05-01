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
package jp.co.acroquest.endosnipe.collector.request;

import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * Javelin �ɗv���𑗐M����N���X�B
 * Executor �ɂ���ėv���𑗐M���܂��B
 *
 * @author sakamoto
 */
public class RequestSender implements Runnable
{
    private final TelegramSender sender_;

    private final Telegram       telegram_;

    /**
     * �I�u�W�F�N�g�𐶐����܂��B
     *
     * @param sender Javelin ���M�I�u�W�F�N�g
     * @param telegram �d��
     */
    public RequestSender(final TelegramSender sender, final Telegram telegram)
    {
        this.sender_ = sender;
        this.telegram_ = telegram;
    }

    /**
     * �d���𑗐M���܂��B
     */
    public void run()
    {
        this.sender_.sendTelegram(this.telegram_);
    }

}
