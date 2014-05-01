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
package jp.co.acroquest.endosnipe.javelin.converter;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.conf.ExcludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.IncludeConversionConfig;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;

/**
 * �R�[�h���ߍ��݂̃C���^�[�t�F�[�X
 * 
 * @author yamasaki
 */
public interface Converter
{
    /**
     * �R�[�h���ߍ��݂��s���B
     * 
     * @param className �N���X��
     * @param classfileBuffer �N���X�t�@�C���̃o�b�t�@
     * @param pool ClassPool
     * @param ctClass CtClass
     * @param config Include�̐ݒ�
     * @param excludeConfigList Exclude�̐ݒ胊�X�g
     * @return �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@
     */
    byte[] convert(String className, byte[] classfileBuffer, ClassPool pool, CtClass ctClass,
            IncludeConversionConfig config, List<ExcludeConversionConfig> excludeConfigList);

    /**
     * �R�[�h���ߍ��݌�̌��ʂ�Ԃ��B
     * @return �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@
     */
    byte[] getResult();

    /**
     * ���������\�b�h
     */
    void init();
}
