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

import java.util.ArrayList;
import java.util.List;

/**
 * �g�����X�t�H�[���ΏۃN���X�������̓��\�b�h�������ݒ�B
 * 
 * @author yamasaki
 */
public class IncludeConversionConfig extends AbstractConversionConfig
{
    /** �K�p����R���o�[�^�̃��X�g */
    private final List<String> converterNameList_ = new ArrayList<String>();

    /**
     * �ݒ�t�@�C����ǂݍ��ށB
     * @param configElements �R�[�h���ߍ��ݑΏ�
     */
    @Override
    protected void parseConfig(final String[] configElements)
    {
        super.parseConfig(configElements);

        // �ȍ~�̓R���o�[�^�̖��O�̃��X�g�������B
        for (int index = 1; index < configElements.length; index++)
        {
            this.converterNameList_.add(configElements[index]);
        }
    }

    /**
     * �R���o�[�^���̃��X�g��Ԃ��B
     * @return �R���o�[�^��
     */
    public List<String> getConverterNameList()
    {
        return this.converterNameList_;
    }
}
