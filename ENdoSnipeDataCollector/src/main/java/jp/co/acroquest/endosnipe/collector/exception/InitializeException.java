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
package jp.co.acroquest.endosnipe.collector.exception;

import org.seasar.framework.message.MessageFormatter;

/**
 * �T�[�r�X�������G���[�̂��߂̗�O�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class InitializeException extends Exception
{
    private static final long serialVersionUID = -1114506435525447808L;

    /**
     * {@link InitializeException} ���\�z���܂��B<br />
     * 
     * @param messageCode ���b�Z�[�W�R�[�h
     * @param args ���b�Z�[�W����
     */
    public InitializeException(final String messageCode, final Object... args)
    {
        this(MessageFormatter.getMessage(messageCode, args));
    }

    /**
     * {@link InitializeException} ���\�z���܂��B<br />
     * 
     * @param message ��O���b�Z�[�W
     */
    private InitializeException(final String message)
    {
        super(message);
    }

    /**
     * {@link InitializeException} ���\�z���܂��B<br />
     * 
     * @param cause �����ƂȂ��O�I�u�W�F�N�g
     */
    public InitializeException(final Throwable cause)
    {
        super(cause);
    }
}
