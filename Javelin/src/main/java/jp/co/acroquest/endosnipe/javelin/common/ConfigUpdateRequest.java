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
package jp.co.acroquest.endosnipe.javelin.common;


/**
 * ConfigUpdateRequestクラス
 * @author acroquest
 *
 */
public class ConfigUpdateRequest
{
    /** キー */
    private String key_;

    /** 値 */
    private String value_;

    /** 更新日時 */
    private long   updateTime_;

    /**
     * コンストラクタ
     * @param key キー
     * @param value 値
     * @param updateTime 更新日時
     */
    public ConfigUpdateRequest(String key, String value, long updateTime)
    {
        this.key_ = key;
        this.value_ = value;
        this.updateTime_ = updateTime;
    }

    /**
     * キーを取得します。
     * @return キー
     */
    public String getKey()
    {
        return key_;
    }

    /**
     * キーを設定します。
     * @param key キー
     */
    public void setKey(String key)
    {
        key_ = key;
    }

    /**
     * 値を取得します。
     * @return 値
     */
    public String getValue()
    {
        return value_;
    }

    /**
     * 値を設定します。
     * @param value 値
     */
    public void setValue(String value)
    {
        value_ = value;
    }

    /**
     * 更新日時を取得します。
     * @return 更新日時
     */
    public long getUpdateTime()
    {
        return updateTime_;
    }

    /**
     * 更新日時を設定します。
     * @param updateTime 更新日時
     */
    public void setUpdateTime(long updateTime)
    {
        updateTime_ = updateTime;
    }
}
