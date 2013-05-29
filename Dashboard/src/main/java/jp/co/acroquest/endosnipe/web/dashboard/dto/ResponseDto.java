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
package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * クライアントにレスポンスを返すためのDtoです。
 * @author fujii
 *
 */
public class ResponseDto
{
    /** 結果 */
    private String result_;

    /** データ */
    private Object data_;

    /**
     * デフォルトコンストラクタです。
     */
    public ResponseDto()
    {
        // Do Nothing.
    }

    /**
     * 
     * @return 結果
     */
    public String getResult()
    {
        return result_;
    }

    /**
     * 
     * @param result 結果
     */
    public void setResult(final String result)
    {
        this.result_ = result;
    }

    /**
     * 
     * @return データ
     */
    public Object getData()
    {
        return data_;
    }

    /**
     * 
     * @param data データ
     */
    public void setData(final Object data)
    {
        this.data_ = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data_ == null) ? 0 : data_.hashCode());
        result = prime * result + ((this.result_ == null) ? 0 : this.result_.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        ResponseDto other = (ResponseDto)obj;
        if (data_ == null)
        {
            if (other.data_ != null)
            {
                return false;
            }
        }
        else if (!data_.equals(other.data_))
        {
            return false;
        }
        if (result_ == null)
        {
            if (other.result_ != null)
            {
                return false;
            }
        }
        else if (!result_.equals(other.result_))
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "ResponseDto [result=" + result_ + ", data=" + data_ + "]";
    }

}
