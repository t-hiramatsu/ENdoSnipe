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

/**
 * �R���o�[�^�̐ݒ�
 * @author yamasaki
 *
 */
public class ConverterConfig
{
    /** �R���o�[�^��\���ړ��� */
    public static final String PREFIX = "[Converter]";

    /** �ݒ�t�@�C���ŃR���o�[�^���ƃR���o�[�^�̃N���X����؂邽�߂̃g�[�N�� */
    private static final String NAME_TOKEN = "=";

    /** �ݒ�t�@�C���ŃR�[�h���ߍ��݃N���X�ƃR���o�[�^����؂邽�߂̃g�[�N�� */
    private static final String CONVERTER_TOKEN = ",";

    /** ���[�U���ݒ肷��R���o�[�^�� */
    private String name_;

    /** �R���o�[�^�̃N���X�� */
    private String[] converterNames_;

    /**
     * �ݒ�t�@�C����ǂݍ��ށB
     * @param line �ݒ�t�@�C���̍s
     */
    public void readConfig(String line)
    {
        //[Converter]����������B
        line = line.substring(PREFIX.length());

        // ���[�U�̐ݒ肷��R���o�[�^���ƃR���o�[�^�̃N���X���𕪗�����B
        String[] nameAndConfig = line.split(NAME_TOKEN);
        this.name_ = nameAndConfig[0].trim();

        this.converterNames_ = nameAndConfig[1].split(CONVERTER_TOKEN);

        for (int index = 0; index < this.converterNames_.length; index++)
        {
            this.converterNames_[index] = this.converterNames_[index].trim();
        }
    }

    /**
     * ���[�U�̐ݒ肵���R���o�[�^�����擾����B
     * @return �R���o�[�^��
     */
    public String getName()
    {
        return this.name_;
    }

    /**
     * �R���o�[�^����ݒ肷��B
     * @param name �R���o�[�^��
     */
    public void setName(final String name)
    {
        this.name_ = name;
    }

    /**
     * �R���o�[�^�̃N���X�����擾����B
     * @return �R���o�[�^�̃N���X��
     */
    public String[] getConverterNames()
    {
        return this.converterNames_;
    }

    /**
     * �R���o�[�^�̃N���X����ݒ肷��B
     * @param converterNames �R���o�[�^�̃N���X��
     */
    public void setConverterNames(final String[] converterNames)
    {
        this.converterNames_ = converterNames;
    }
}
