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
package jp.co.acroquest.endosnipe.javelin.conf;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Javelin�̃��b�Z�[�W���擾���邽�߂̃N���X�ł��B<br />
 * 
 * @author tooru
 */
public final class JavelinMessages
{
    /** ���b�Z�[�W�v���p�e�B�t�@�C���̖��� */
    private static final String BUNDLE_NAME = "JavelinMessages";

    /** ���\�[�X�o���h�� */
    private static ResourceBundle bundle__ = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private JavelinMessages()
    {
        //�������Ȃ�
    }

    /**
     * ���\�[�X�o���h����胁�b�Z�[�W���擾���܂��B<br />
     * 
     * @param messageId ���b�Z�[�WID
     * @param args ���b�Z�[�W�̈���
     * @return ���b�Z�[�W
     */
    public static String getMessage(final String messageId, final Object... args)
    {
        String message = null;
        try
        {
            message = bundle__.getString(messageId);
            message = MessageFormat.format(message, args);
        }
        catch (MissingResourceException mre)
        // CHECKSTYLE:OFF
        {
            // ��������B
        }
        // CHECKSTYLE:ON
        return message;
    }
}
