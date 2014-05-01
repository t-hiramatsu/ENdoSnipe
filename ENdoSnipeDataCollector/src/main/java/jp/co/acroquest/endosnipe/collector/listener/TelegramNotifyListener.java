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

import jp.co.acroquest.endosnipe.communicator.CommunicatorListener;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * DataCollector ����N���C�A���g�ɒʒm���邽�߂̃��X�i�B<br />
 *
 * ���̃C���^�t�F�[�X����������C���X�^���X�� JavelinClient �ɓo�^���邱�ƂŁA
 * ���̃C���X�^���X�ɃA���[���ʒm����܂��B
 *
 * @author sakamoto
 */
public interface TelegramNotifyListener extends CommunicatorListener
{
    /**
     * �N���C�A���g�ɒʒm���܂��B
     *
     * @param telegram �ʒm����f�[�^
     */
    void receiveTelegram(Telegram telegram);

    /**
     * �d�����M�I�u�W�F�N�g���Z�b�g���܂��B
     *
     * @param telegramSender �d�����M�I�u�W�F�N�g���Z�b�g���܂��B
     */
    void setTelegramSender(TelegramSender telegramSender);

    /**
     * ��M�����d�����̂܂܂�ʒm���邩�A
     * �ϊ��������s�����f�[�^��ʒm���邩�A�̂ǂ��炩�����肵�܂��B<br />
     *
     * ���̃��\�b�h�� <code>true</code> ��Ԃ����ꍇ�A
     * {@link #receiveTelegram(Telegram)} �̈����ɂ́A
     * ��M�����d�������̂܂ܓn����܂��B<br />
     * ���̃��\�b�h�� <code>false</code> ��Ԃ����ꍇ�A
     * {@link #receiveTelegram(Telegram)} �̈����ɂ́A
     * DataCollector �ɂ���ĕϊ����ꂽ�f�[�^���n����܂��B<br />
     *
     * @return ��M�����d�����̂܂܂�ʒm����ꍇ�� <code>true</code> �A
     */
    boolean isRawTelegramNeeded();
}
