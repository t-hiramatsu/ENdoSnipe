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
 * HttpResponseValueクラス
 * @author acroquest
 *
 */
public class HttpResponseValue
{
    /** コンテンツタイプ */
    private String    contentType_;

    /** ステータス */
    private int       status_ = -1;

    /** 例外　*/
    private Throwable throwable_;

    /**
     * コンテンツタイプを取得します。
     * @return コンテンツタイプ
     */
    public String getContentType()
    {
        return contentType_;
    }

    /**
     * コンテンツタイプを設定します。
     * @param contentType コンテンツタイプ
     */
    public void setContentType(String contentType)
    {
        contentType_ = contentType;
    }

    /**
     * ステータスを取得します。
     * @return ステータス
     */
    public int getStatus()
    {
        return status_;
    }

    /**
     * ステータスを設定します。
     * @param status ステータス
     */
    public void setStatus(int status)
    {
        status_ = status;
    }

    /**
     * 例外を取得します。
     * @return 例外
     */
    public Throwable getThrowable()
    {
        return throwable_;
    }

    /**
     * 例外を設定します。
     * @param throwable 例外
     */
    public void setThrowable(Throwable throwable)
    {
        throwable_ = throwable;
    }
}
