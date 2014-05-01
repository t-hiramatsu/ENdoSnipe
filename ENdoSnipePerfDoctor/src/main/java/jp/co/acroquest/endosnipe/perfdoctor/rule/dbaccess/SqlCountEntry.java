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
package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;

/**
 * SQL�̉�/���Ԃ𐔂������ʂ�ۑ�����B
 * 
 * @author eriguchi
 *
 */
public class SqlCountEntry
{
    /** SQL�̉� */
    private long                       count_;

    /** �o�C���h���� */
    private final Map<String, Integer> bindValCountMap_;

    /** ���s�񐔂��ŏ��ɒ�����JavelinLogElement */
    JavelinLogElement                  errorElement_;

    /**
     * �R���X�g���N�^�B
     */
    public SqlCountEntry()
    {
        count_ = 1;
        this.bindValCountMap_ = new HashMap<String, Integer>();
    }

    /**
     * ���s�񐔂��ŏ��ɒ�����JavelinLogElement���擾����B
     * 
     * @return ���s�񐔂��ŏ��ɒ�����JavelinLogElement
     */
    public JavelinLogElement getErrorElement()
    {
        return errorElement_;
    }

    /***
     * �o�C���h�����̃p�^�[�������擾����B
     * @return �o�C���h�����̃p�^�[����
     */
    public int getBindValCount()
    {
        return this.bindValCountMap_.size();
    }

    /**
     * ���s�񐔂��ŏ��ɒ�����JavelinLogElement���擾����B
     * 
     * @param errorElement ���s�񐔂��ŏ��ɒ�����JavelinLogElement
     */
    public void setErrorElement(final JavelinLogElement errorElement)
    {
        errorElement_ = errorElement;
    }

    /**
     * ���s�񐔂��擾����B
     * 
     * @return ���s��
     */
    public long getCount()
    {
        return count_;
    }

    /**
     * ���s�񐔂��擾����B
     * 
     * @param count ���s��
     */
    public void setCount(final long count)
    {
        count_ = count;
    }

    /***
     * �o�C���h������ǉ�����B
     * @param bindVal �o�C���h����
     */
    public void addBindValCount(final String bindVal)
    {
        Integer bindValCount = this.bindValCountMap_.get(bindVal);

        if (bindValCount == null)
        {
            this.bindValCountMap_.put(bindVal, Integer.valueOf(1));
        }
        else
        {
            this.bindValCountMap_.put(bindVal, Integer.valueOf(bindValCount.intValue() + 1));
        }
    }

}
