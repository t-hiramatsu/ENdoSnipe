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
package jp.co.acroquest.endosnipe.javelin.testutil;

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * SystemLogger�̃��O�o�͏����̌Ăяo��������p���ďo�̓��b�Z�[�W�����؂��郆�[�e�B���e�B�N���X
 * 
 * ���R�͕s�������Alog���\�b�h�̌Ăяo���񐔁A
 * ���������擾�ł��Ȃ����߁A�Ăяo�����ƂȂ郁�\�b�h�ɑ΂��Ďd�|����
 * 
 * @author kimura
 *
 */
public class SystemLoggerTestUtil
{
    /**
     * 
     */
    private SystemLoggerTestUtil()
    {
        //No Operation
    }

    /**
     * SystemLogger�ŁA�����Ɏw�肵�����\�b�h���\�b�h�œ���̃��O���b�Z�[�W���o�͂��ꂽ���Ƃ����邩�m�F���郁�\�b�h
     * @param targetMessage ���ؑΏۃ��O���b�Z�[�W
     * @param methodName �o�͂��s�������\�b�h��
     * @return �o�͂��ꂽ���Ƃ����邩
     */
    public static boolean isContainSystemLogMethod(final String targetMessage,
            final String methodName)
    {
        int infoPrintlnCallCount = MockObjectManager.getCallCount(SystemLogger.class, methodName);

        for (int logCnt = 0; logCnt < infoPrintlnCallCount; logCnt++)
        {
            String firstElementMessage = null;
            String secondElementMessage = null;
            Object firstLogElement =
                    MockObjectManager.getArgument(SystemLogger.class, methodName, logCnt, 0);

            Object secondLogElement = null;
            try
            {
                secondLogElement =
                        MockObjectManager.getArgument(SystemLogger.class, methodName, logCnt, 1);
            }
            catch (Exception e)
            {
                // null���Ԃ�Ƒz�肵�Ă������A��O����Ԃ̂Ŗ������������
            }

            if (firstLogElement != null && firstLogElement instanceof Throwable)
            {
                firstElementMessage = getStackTraceMessage((Throwable)firstLogElement);
            }
            else
            {
                firstElementMessage = (String)firstLogElement;
            }

            if (secondLogElement != null && secondLogElement instanceof Throwable)
            {
                secondElementMessage = getStackTraceMessage((Throwable)secondLogElement);
            }
            else
            {
                secondElementMessage = (String)secondLogElement;
            }

            boolean resultFirst = false;
            boolean resultSecond = false;

            if (firstElementMessage != null)
            {
                resultFirst = firstElementMessage.contains(targetMessage);
            }

            if (secondElementMessage != null)
            {
                resultSecond = secondElementMessage.contains(targetMessage);
            }

            if (resultFirst || resultSecond)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Throwable�C���X�^���X�̃X�^�b�N�g���[�X�𕶎���Ƃ��Ď擾����
     * @param th Throwable�C���X�^���X
     * @return Throwable�C���X�^���X�̃X�^�b�N�g���[�X������
     */
    public static String getStackTraceMessage(final Throwable th)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        th.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
