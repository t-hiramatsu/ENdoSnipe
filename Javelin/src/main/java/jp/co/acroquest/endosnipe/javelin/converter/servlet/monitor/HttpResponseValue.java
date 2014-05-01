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
package jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor;

/**
 * HttpResponseValue�N���X
 * @author acroquest
 *
 */
public class HttpResponseValue
{
    /** �R���e���c�^�C�v */
    private String    contentType_;

    /** �X�e�[�^�X */
    private int       status_ = -1;

    /** ��O�@*/
    private Throwable throwable_;

    /**
     * �R���e���c�^�C�v���擾���܂��B
     * @return �R���e���c�^�C�v
     */
    public String getContentType()
    {
        return contentType_;
    }

    /**
     * �R���e���c�^�C�v��ݒ肵�܂��B
     * @param contentType �R���e���c�^�C�v
     */
    public void setContentType(String contentType)
    {
        contentType_ = contentType;
    }

    /**
     * �X�e�[�^�X���擾���܂��B
     * @return �X�e�[�^�X
     */
    public int getStatus()
    {
        return status_;
    }

    /**
     * �X�e�[�^�X��ݒ肵�܂��B
     * @param status �X�e�[�^�X
     */
    public void setStatus(int status)
    {
        status_ = status;
    }

    /**
     * ��O���擾���܂��B
     * @return ��O
     */
    public Throwable getThrowable()
    {
        return throwable_;
    }

    /**
     * ��O��ݒ肵�܂��B
     * @param throwable ��O
     */
    public void setThrowable(Throwable throwable)
    {
        throwable_ = throwable;
    }
}
