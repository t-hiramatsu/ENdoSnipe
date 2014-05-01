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
package jp.co.acroquest.endosnipe.javelin.converter.util;


/**
 * �J���A�v���P�[�V�������ŕϊ����s�������\�b�h���ƁA
 * �������O���ꂽ���\�b�h����ێ�����J�E���^�N���X
 * 
 * @author S.Kimura
 */
public class ConvertedMethodCounter
{
    /** �J���A�v���P�[�V�������ŕϊ����ꂽ���\�b�h�� */
    private static long convertedMethodCount__;

    /** �J���A�v���P�[�V�������ŕϊ����玩�����O���ꂽ���\�b�h�� */
    private static long excludedMethodCount__;

    /**
     * �C���X�^���X���h�~�̂��߂̃R���X�g���N�^
     */
    private ConvertedMethodCounter()
    {
    };

    static
    {
        convertedMethodCount__ = 0;
        excludedMethodCount__ = 0;
    }

    /**
     * �J���A�v���P�[�V�������ŕϊ����ꂽ���\�b�h�����C���N�������g
     */
    public static void incrementConvertedCount()
    {
        convertedMethodCount__++;
    }

    /**
     * �J���A�v���P�[�V�������ŕϊ����ꂽ���\�b�h�����擾����
     * 
     * @return �J���A�v���P�[�V�������ŕϊ����ꂽ���\�b�h��
     */
    public static long getConvertedCount()
    {
        return convertedMethodCount__;
    }
    
    /**
     * �J���A�v���P�[�V�������ŕϊ����玩�����O���ꂽ���\�b�h�����C���N�������g
     */
    public static void incrementExcludedCount()
    {
        excludedMethodCount__++;
    }

    /**
     * �J���A�v���P�[�V�������ŕϊ����玩�����O���ꂽ���\�b�h�����擾����
     * 
     * @return �J���A�v���P�[�V�������ŕϊ����玩�����O���ꂽ���\�b�h��
     */
    public static long getExcludedCount()
    {
        return excludedMethodCount__;
    }
}
