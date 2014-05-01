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

import java.util.Collections;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * ���ׂĂ̓d����ʒm���郊�X�i�B
 *
 * @author sakamoto
 */
public class AllNotifyListener extends AbstractTelegramListener
{

    private List<TelegramNotifyListener> telegramNotifyListenerList_;

    /**
     * �R���X�g���N�^�B
     */
    public AllNotifyListener()
    {
        this.telegramNotifyListenerList_ = Collections.emptyList();
    }

    /**
     * �N���C�A���g�ɒʒm���邽�߂̃��X�i��o�^���܂��B
     *
     * @param notifyListenerList �N���C�A���g�ɒʒm���邽�߂̃��X�i�̃��X�g
     */
    public void setTelegramNotifyListener(final List<TelegramNotifyListener> notifyListenerList)
    {
        if (notifyListenerList != null)
        {
            this.telegramNotifyListenerList_ = notifyListenerList;
        }
        else
        {
            this.telegramNotifyListenerList_ = Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        for (TelegramNotifyListener notifyListener : this.telegramNotifyListenerList_)
        {
            if (notifyListener.isRawTelegramNeeded())
            {
                notifyListener.receiveTelegram(telegram);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean judgeTelegram(final Telegram telegram)
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return 0;
    }

}
