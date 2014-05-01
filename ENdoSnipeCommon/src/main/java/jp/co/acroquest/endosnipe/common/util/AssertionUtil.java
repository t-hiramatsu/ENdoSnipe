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

import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;

import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * �A�T�[�V�������s�����߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class AssertionUtil implements CommonLogMessageCodes
{
    private AssertionUtil()
    {

    }

    /**
     * <code>arg</code> �� <code>null</code> �łȂ����Ƃ��`�F�b�N���܂��B<br />
     * 
     * @param name
     *            �I�u�W�F�N�g����
     * @param arg
     *            �`�F�b�N�ΏۃI�u�W�F�N�g
     */
    public static void assertNotNull(final String name, final Object arg)
    {
        if (arg == null)
        {
            throw new SIllegalArgumentException(CANT_BE_NULL, new Object[]{name});
        }
    }

    /**
     * <code>arg</code> �� <code>null</code> �܂��͋󕶎���ł͂Ȃ����Ƃ��`�F�b�N���܂��B<br />
     * 
     * @param name
     *            �I�u�W�F�N�g����
     * @param arg
     *            �`�F�b�N�Ώە�����
     */
    public static void assertNotEmpty(final String name, final String arg)
    {
        if (arg == null || arg.length() == 0)
        {
            throw new SIllegalArgumentException(CANT_BE_EMPTY_STRING, new Object[]{name});
        }
    }

    /**
     * <code>arg</code> �� <code>clazz</code> �̃T�u�N���X�ł��邱�Ƃ��`�F�b�N���܂��B<br />
     * 
     * @param name
     *            �I�u�W�F�N�g����
     * @param clazz
     *            �N���X
     * @param arg
     *            �`�F�b�N�ΏۃI�u�W�F�N�g
     */
    public static void assertInstanceOf(final String name, final Class<?> clazz, final Object arg)
    {
        if (!clazz.isAssignableFrom(arg.getClass()))
        {
            throw new SIllegalArgumentException(TYPE_MISS_MATCH, new Object[]{name,
                    clazz.getClass().getName()});
        }
    }
}
