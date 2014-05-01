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
 * ConfigUpdateRequest�N���X
 * @author acroquest
 *
 */
public class ConfigUpdateRequest
{
    /** �L�[ */
    private String key_;

    /** �l */
    private String value_;

    /** �X�V���� */
    private long   updateTime_;

    /**
     * �R���X�g���N�^
     * @param key �L�[
     * @param value �l
     * @param updateTime �X�V����
     */
    public ConfigUpdateRequest(String key, String value, long updateTime)
    {
        this.key_ = key;
        this.value_ = value;
        this.updateTime_ = updateTime;
    }

    /**
     * �L�[���擾���܂��B
     * @return �L�[
     */
    public String getKey()
    {
        return key_;
    }

    /**
     * �L�[��ݒ肵�܂��B
     * @param key �L�[
     */
    public void setKey(String key)
    {
        key_ = key;
    }

    /**
     * �l���擾���܂��B
     * @return �l
     */
    public String getValue()
    {
        return value_;
    }

    /**
     * �l��ݒ肵�܂��B
     * @param value �l
     */
    public void setValue(String value)
    {
        value_ = value;
    }

    /**
     * �X�V�������擾���܂��B
     * @return �X�V����
     */
    public long getUpdateTime()
    {
        return updateTime_;
    }

    /**
     * �X�V������ݒ肵�܂��B
     * @param updateTime �X�V����
     */
    public void setUpdateTime(long updateTime)
    {
        updateTime_ = updateTime;
    }
}
