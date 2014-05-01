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
package jp.co.acroquest.endosnipe.common.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ������Ɋ܂܂��L�[���[�h��ϊ�����N���X�B</br>
 * �L�[���[�h��Prefix�ASuffix���w�肷�邱�Ƃ��ł���B
 * addConverter���\�b�h�𗘗p���ăL�[���[�h�̒u����������w�肵�A
 * convert���\�b�h�Œu������B
 * 
 * @author tsukano
 */
public class KeywordConverter
{
    /** �L�[���[�h��Prefix */
    private final String keywordPrefix_;

    /** �L�[���[�h��Suffix */
    private final String keywordSuffix_;

    /** �L�[���[�h��ϊ����镶������`�������X�g */
    private final Map<String, String> converterMap_ = new LinkedHashMap<String, String>();

    /**
     * Prefix�ASuffix�Ȃ��̕ϊ��N���X�𐶐�����B</br>
     */
    public KeywordConverter()
    {
        this.keywordPrefix_ = "";
        this.keywordSuffix_ = "";
    }

    /**
     * Prefix�ASuffix���w�肵�ĕϊ��N���X�𐶐�����B</br>
     * 
     * @param keywordPrefix �L�[���[�h��Prefix
     * @param keywordSuffix �L�[���[�h��Suffix
     */
    public KeywordConverter(final String keywordPrefix, final String keywordSuffix)
    {
        this.keywordPrefix_ = keywordPrefix;
        this.keywordSuffix_ = keywordSuffix;
    }

    /**
     * �L�[���[�h�ƒu���������ǉ�����B</br>
     * 
     * @param keyword �L�[���[�h
     * @param convertedString �L�[���[�h�̒u��������
     */
    public void addConverter(final String keyword, final String convertedString)
    {
        converterMap_.put(keywordPrefix_ + keyword + keywordSuffix_, convertedString);
    }

    /**
     * �L�[���[�h�ƒu���������ǉ�����B</br>
     * �u���������int�l��ݒ肷��ׂ̊ȈՃ��\�b�h�B
     * 
     * @param keyword �L�[���[�h
     * @param convertedValue �L�[���[�h�̒u��������(int�l)
     */
    public void addConverter(final String keyword, final int convertedValue)
    {
        addConverter(keyword, String.valueOf(convertedValue));
    }

    /**
     * �L�[���[�h�ƒu���������ǉ�����B</br>
     * �u���������long�l��ݒ肷��ׂ̊ȈՃ��\�b�h�B
     * 
     * @param keyword �L�[���[�h
     * @param convertedValue �L�[���[�h�̒u��������(long�l)
     */
    public void addConverter(final String keyword, final long convertedValue)
    {
        addConverter(keyword, String.valueOf(convertedValue));
    }

    /**
     * �L�[���[�h�ƒu���������ǉ�����B</br>
     * �u���������Object�̕������ݒ肷��ׂ̊ȈՃ��\�b�h�B
     * toString()���������Ă���Object�Ȃ�΂��̏o�͂Œu������B
     * 
     * @param keyword �L�[���[�h
     * @param convertedValue �L�[���[�h�̒u��������(Object)
     */
    public void addConverter(final String keyword, final Object convertedValue)
    {
        addConverter(keyword, String.valueOf(convertedValue));
    }

    /**
     * �o�^�����u��������ɃL�[���[�h��u������B</br>
     * 
     * @param source �u���O�̕�����
     * @return �u����̕�����
     */
    public String convert(final String source)
    {
        String retValue = source;

        // �o�^���Ă�����𗘗p���Ēu������
        Set<Map.Entry<String, String>> entries = converterMap_.entrySet();
        for (Map.Entry<String, String> entry : entries)
        {
            if (entry.getValue() == null)
            {
                retValue = retValue.replace(entry.getKey(), "null");
            }
            else
            {
                retValue = retValue.replace(entry.getKey(), entry.getValue());
            }
        }

        // �u����̕������Ԃ�
        return retValue;
    }
}
