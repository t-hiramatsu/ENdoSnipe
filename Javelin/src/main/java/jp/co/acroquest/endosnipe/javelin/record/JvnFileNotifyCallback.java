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
package jp.co.acroquest.endosnipe.javelin.record;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinAcceptThread;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinConnectThread;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * Javelin���O�ʒm�d���𑗐M����R�[���o�b�N�B
 * 
 * @author eriguchi
 */
public class JvnFileNotifyCallback implements JavelinLogCallback
{
    /**
     * ���O�ʒm�d���𑗐M����B
     * @param jvnFileName JVN���O�t�@�C����
     * @param jvnLogContent JVN���O�t�@�C���̓��e
     * @param telegramId �d�� ID
     * @param itemName ���ږ�
     */
    public void execute(final String jvnFileName, final String jvnLogContent,
            final long telegramId, final String itemName)
    {
        // �N���C�A���g�����Ȃ��ꍇ�͓d�����쐬���Ȃ��B
        if (JavelinAcceptThread.getInstance().hasClient() == false
                && JavelinConnectThread.getInstance().isConnected() == false)
        {
            return;
        }

        // �ʒm�d�����쐬����B
        Telegram telegram = null;
        try
        {
            telegram = TelegramCreator.createJvnFileNotifyTelegram(jvnFileName,
                    jvnLogContent, telegramId, itemName);
        } 
        catch (IllegalArgumentException ex) 
        {
            SystemLogger logger = SystemLogger.getInstance();
            logger.warn(ex);
        }

        if (telegram == null)
        {
            return;
        }

        if (JavelinAcceptThread.getInstance().hasClient())
        {
            JavelinAcceptThread.getInstance().sendTelegram(telegram);
        }
        else 
        {
            JavelinConnectThread.getInstance().sendTelegram(telegram);
        }
    }
}
