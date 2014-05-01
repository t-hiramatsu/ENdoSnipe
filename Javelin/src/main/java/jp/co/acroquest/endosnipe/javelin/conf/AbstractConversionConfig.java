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

import java.util.regex.Pattern;

/**
 * Include/Exclude�ݒ���������ۃN���X�B
 *
 * @author yamasaki
 */
public abstract class AbstractConversionConfig
{
    /** �p���p�ݒ���s�����߂̐ړ��� */
    private static final String INHERITANCE = "[inheritance]";

    /** �p���p�ݒ肩�ǂ����������t���O�B */
    private boolean isInheritance_ = false;

    /** �N���X���̃p�^�[���B */
    private String className_;

    /** ���\�b�h���̃p�^�[���B */
    private String methodNamePattern_;

    /** �N���X���̃p�^�[���B */
    private Pattern classNamePattern_;

    /**
     * �ݒ�t�@�C����ǂݍ���
     * @param configLine �ݒ�t�@�C���̍s
     */
    public void readConfig(final String configLine)
    {
        if (configLine == null || configLine.length() == 0)
        {
            String key = "javelin.conf.AbstractConversionConfig.AnIllegalConfigurationLabel";
            String message = JavelinMessages.getMessage(key, configLine);
            throw new IllegalArgumentException(message);
        }

        String[] configElements = configLine.split(",");
        try
        {
            parseConfig(configElements);
        }
        catch (Exception ex)
        {
            String key = "javelin.conf.AbstractConversionConfig.AnIllegalConfigurationLabel";
            String message = JavelinMessages.getMessage(key, configLine);
            throw new IllegalArgumentException(message, ex);
        }
    }

    /**
     * �ݒ�t�@�C���̃p�^�[����ǂݍ��ށB
     * inheritance��ON/OFF�A�N���X���A���\�b�h�����擾����B
     * @param configElements �R�[�h���ߍ��ݑΏ�
     */
    protected void parseConfig(final String[] configElements)
    {
        configElements[0] = configElements[0].trim();
        if (configElements[0].startsWith(INHERITANCE))
        {
            setInheritance(true);
            configElements[0] = configElements[0].substring(INHERITANCE.length());
        }

        int splitIndex = configElements[0].indexOf("#");
        if (splitIndex < 0)
        {
            // �ݒ��#���܂܂�Ă��Ȃ���΁A�w��N���X�̑S�Ẵ��\�b�h��ΏۂƂ���B
            setClassName(configElements[0]);
            setClassNamePattern(Pattern.compile(configElements[0]));
            setMethodNamePattern(".*");
        }
        else
        {
            // �ݒ��#���܂܂�Ă���΁A�N���X���A���\�b�h�������Ɏw�肪����B
            String className = configElements[0].substring(0, splitIndex);
            String methodName = configElements[0].substring(splitIndex + 1);
            setClassName(className);
            setClassNamePattern(Pattern.compile(className));
            setMethodNamePattern(methodName);
        }
    }

    /**
     * �p���p�ݒ��ON/OFF��Ԃ��B
     * @return true:�p���p�ݒ肪�s���Ă���Afalse:�p���p�ݒ肪�s���Ă��Ȃ��B
     */
    public boolean isInheritance()
    {
        return this.isInheritance_;
    }

    /**
     * �p���p�t���O�̐ݒ���s���B
     * @param isInheritance true:�p���p�ݒ���s���Afalse:�p���p�ݒ���s��Ȃ��B
     */
    public void setInheritance(final boolean isInheritance)
    {
        this.isInheritance_ = isInheritance;
    }

    /**
     * �N���X���̃p�^�[�����擾����B
     * @return �N���X���̃p�^�[��
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * �N���X���̃p�^�[�����擾����B
     * @return �N���X���̃p�^�[��
     */
    public Pattern getClassNamePattern()
    {
        return this.classNamePattern_;
    }

    /**
     * �N���X���̃p�^�[����ݒ肷��B
     * @param classNamePattern �N���X���̃p�^�[��
     */
    public void setClassName(final String classNamePattern)
    {
        this.className_ = classNamePattern;
    }

    /**
     * �N���X���̃p�^�[����ݒ肷��B
     * @param classNamePattern �N���X���̃p�^�[��
     */
    public void setClassNamePattern(final Pattern classNamePattern)
    {
        this.classNamePattern_ = classNamePattern;
    }

    /**
     * ���\�b�h���̃p�^�[�����擾����B
     * @return ���\�b�h���̃p�^�[��
     */
    public String getMethodNamePattern()
    {
        return this.methodNamePattern_;
    }

    /**
     * ���\�b�h���̃p�^�[����ݒ肷��B
     * @param methodNamePattern ���\�b�h���̃p�^�[��
     */
    public void setMethodNamePattern(final String methodNamePattern)
    {
        this.methodNamePattern_ = methodNamePattern;
    }
}
