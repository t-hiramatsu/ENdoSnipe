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

/**
 * �l�b�g���[�N�n�̃��[�e�B���e�B�N���X�B<br />
 *
 * @author sakamoto
 */
public class NetworkUtil
{
    private NetworkUtil()
    {
        // Do nothing.
    }

    /**
     * �w�肳�ꂽ������ IP �A�h���X��\�����ǂ����𔻒肵�܂��B<br />
     *
     * @param text ������
     * @return IP �A�h���X�̏ꍇ�� <code>true</code> �A IP �A�h���X�łȂ��ꍇ�� <code>false/code>
     */
    public static boolean isIpAddress(final String text)
    {
        return text.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$");
    }

}
