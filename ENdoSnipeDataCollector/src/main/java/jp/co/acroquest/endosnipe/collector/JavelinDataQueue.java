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
package jp.co.acroquest.endosnipe.collector;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * Javelin ���O���L���[�C���O���邽�߂̃L���[�ł��B<br />
 * 
 * @author y-komori
 */
public class JavelinDataQueue implements LogMessageCodes, CommonLogMessageCodes
{
    /** �L���[�̍ő�v�f�� */
    private static final int QUEUE_SIZE = 100;

    /** �L���[�փf�[�^�𓊓�����ۂ̃^�C���A�E�g����(�~���b) */
    private static final int OFFERING_TIMEOUT = 10000;

    /** �L���[����f�[�^�����o���ۂ̃^�C���A�E�g����(�~���b) */
    private static final int TAKING_TIMEOUT = 1000;

    private static ENdoSnipeLogger logger__ = ENdoSnipeLogger.getLogger(JavelinDataQueue.class);

    private final BlockingQueue<JavelinData> queue_ =
                                                      new ArrayBlockingQueue<JavelinData>(
                                                                                          QUEUE_SIZE);

    /**
     * �L���[�� {@link JavelinData} ��ǉ����܂��B<br />
     * 
     * @param data {@link JavelinData} �I�u�W�F�N�g
     */
    public void offer(final JavelinData data)
    {
        if (data != null)
        {
            try
            {
                queue_.offer(data, OFFERING_TIMEOUT, TimeUnit.MILLISECONDS);
                if (logger__.isDebugEnabled())
                {
                    logger__.log(QUEUE_OFFERED, data.toString());
                }
            }
            catch (InterruptedException ex)
            {
                logger__.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
            }
        }
    }

    /**
     * �L���[���� {@link JavelinData} �����o���܂��B<br />
     * 
     * �L���[�Ƀf�[�^�����݂��Ȃ��ꍇ�A�X���b�h�̓u���b�N���܂��B<br />
     * 
     * @return {@link JavelinData} �I�u�W�F�N�g
     */
    public JavelinData take()
    {
        try
        {
            JavelinData data = queue_.poll(TAKING_TIMEOUT, TimeUnit.MILLISECONDS);
            if (logger__.isDebugEnabled() && data != null)
            {
                logger__.log(QUEUE_TAKEN, data.toString());
            }
            return data;
        }
        catch (InterruptedException ex)
        {
            logger__.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
            return null;
        }
    }

    /**
     * �L���[�Ɏc���Ă���f�[�^�̐���Ԃ��܂��B<br />
     * 
     * @return �f�[�^�̐�
     */
    public int size()
    {
        return queue_.size();
    }
}
