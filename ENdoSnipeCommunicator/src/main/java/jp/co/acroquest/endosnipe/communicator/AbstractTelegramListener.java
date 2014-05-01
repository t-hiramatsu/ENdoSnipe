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
package jp.co.acroquest.endosnipe.communicator;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * �d�������̂��߂̊��N���X�ł��B<br />
 * 
 * @author y-komori
 */
public abstract class AbstractTelegramListener implements TelegramListener
{
    /** ���K�[�N���X */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(AbstractTelegramListener.class);

    /**
     * {@inheritDoc}
     */
    public Telegram receiveTelegram(final Telegram telegram)
    {
        if (judgeTelegram(telegram) == true)
        {
            try
            {
                Telegram response = doReceiveTelegram(telegram);
                return response;
            }
            catch (Throwable ex)
            {
                LOGGER.log("WECC0103", ex, ex.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * ��M�����d�����������邩�ǂ����𔻒肵�܂��B<br />
     * ���菈�����J�X�^�}�C�Y�������ꍇ�́A�{���\�b�h���T�u�N���X�ŃI�[�o�[���C�h���Ă��������B
     * 
     * @param telegram ��M�d��
     * @return �d���������s���ꍇ�A<code>true</code>�B
     */
    protected boolean judgeTelegram(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        return header.getByteRequestKind() == getByteRequestKind()
                && header.getByteTelegramKind() == getByteTelegramKind();
    }

    /**
     * �d���������s���܂��B<br />
     * �{���\�b�h�̓T�u�N���X�Ŏ������Ă��������B<br />
     * 
     * @param telegram ��M�����d���I�u�W�F�N�g
     * @return �����d���I�u�W�F�N�g�B�������s��Ȃ��ꍇ�� <code>null</code>�B
     */
    protected abstract Telegram doReceiveTelegram(Telegram telegram);

    /**
     * ��������d���̗v��������ʂ�Ԃ��܂��B<br />
     * �{���\�b�h�̓T�u�N���X�Ŏ������Ă��������B
     * 
     * @return ��������d���̗v���������
     */

    protected abstract byte getByteRequestKind();

    /**
     * ��������d���̎�ʂ�Ԃ��܂��B<br />
     * �{���\�b�h�̓T�u�N���X�Ŏ������Ă��������B
     * 
     * @return ��������d���̎��
     */
    protected abstract byte getByteTelegramKind();
}
