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
 * �R�[�h���ߍ��ݎ��Ɏg�p���郆�[�e�B���e�B�N���X�B
 *
 * @author Sakamoto
 */
public class ConverterUtil
{
    /**
     * �R���X�g���N�^
     */
    private ConverterUtil()
    {
        // Do Nothing.
    }

    /**
     * �N���X������A$$�ȍ~���폜���āA�P���ȃN���X���ɕς���B<br>
     * Seasar�A�v���P�[�V�����ł́A�R���e�i�ɂ���āA�����N���X����������鎖�������A �N���X�����ω����邽�߁A���̑Ώ����K�v�ɂȂ�܂��B
     * 
     * @param className �X�V�Ώۂ̃N���X���B
     * @return $$�ȍ~���폜�����N���X���B
     */
    public static String toSimpleName(String className)
    {
        if (className == null)
        {
            return null;
        }
        
        if(className.startsWith("$$"))
        {
            className = className.substring("$$".length());
        }

        int indexOfDollarDollar = className.indexOf("$$", 1);
        if (indexOfDollarDollar < 0)
        {
            return className;
        }

        return className.substring(0, indexOfDollarDollar);
    }

    
}
