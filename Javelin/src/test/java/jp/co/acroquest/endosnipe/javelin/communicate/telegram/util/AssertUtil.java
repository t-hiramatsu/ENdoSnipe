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
package jp.co.acroquest.endosnipe.javelin.communicate.telegram.util;

import jp.co.acroquest.endosnipe.communicator.entity.Body;
import junit.framework.TestCase;

/**
 * ���ؗp���[�e�B���e�B
 *
 * @author fujii
 */
public abstract class AssertUtil extends TestCase
{
    /**
     * �d����Body�̌��؂��s���B(�d���̒��g�����؂���)
     * @param objName �I�u�W�F�N�g��
     * @param itemName ���ږ�
     * @param itemMode ���[�h
     * @param loop ���[�v��
     * @param detail �ڍ�
     * @param body �󂯎�����d��
     */
    public static void assertTelegram(final String objName, final String itemName,
            final byte itemMode, final int loop, final Object[] detail, final Body body)
    {
        assertEquals(objName, body.getStrObjName());
        assertEquals(itemName, body.getStrItemName());
        assertEquals(itemMode, body.getByteItemMode());
        assertEquals(loop, body.getIntLoopCount());
        assertDetail(detail, body);
    }

    /**
     * �d����Body�̌��؂��s���B(�d���̒��g�͌��؂��Ȃ�)
     * @param objName �I�u�W�F�N�g��
     * @param itemName ���ږ�
     * @param itemMode ���[�h
     * @param loop ���[�v��
     * @param body �󂯎�����d��
     */
    public static void assertResourceTelegram(final String objName, final String itemName,
            final byte itemMode, final int loop, final Body body)
    {
        assertEquals(objName, body.getStrObjName());
        assertEquals(itemName, body.getStrItemName());
        assertEquals(itemMode, body.getByteItemMode());
        assertEquals(loop, body.getIntLoopCount());
    }

    /**
     * Body�̏ڍׂ����؂���B
     * @param detail ���҂���ڍ�
     * @param body �󂯎�����d��
     */
    public static void assertDetail(final Object[] detail, final Body body)
    {
        if (detail.length != body.getObjItemValueArr().length)
        {
            fail();
        }
        Object[] objValue = body.getObjItemValueArr();

        for (int num = 0; num < detail.length; num++)
        {
            assertEquals(detail[num], objValue[num]);
        }
    }
}
