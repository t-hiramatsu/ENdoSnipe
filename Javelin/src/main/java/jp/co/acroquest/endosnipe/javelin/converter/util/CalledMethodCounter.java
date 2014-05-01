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
 * JavelinConverter�ŕϊ����s�������\�b�h�̂����A
 * �Ăяo���ꂽ���\�b�h�����J�E���g����J�E���^�N���X
 * 
 * @author S.Kimura
 */
public class CalledMethodCounter
{
    /** �Ăяo���ꂽ���\�b�h���̃J�E���^ */
    private static long calledMethodCount__;
    
    /**
     * �C���X�^���X���h�~�̂��߂̃R���X�g���N�^
     */
    private CalledMethodCounter()
    {
    };
    
    static
    {
        calledMethodCount__ = 0;
    }
    
    /**
     * �Ăяo���ꂽ���\�b�h���̃J�E���^��������
     */
    public static void clear()
    {
        calledMethodCount__ = 0;
    }
    
    /**
     * �Ăяo���ꂽ���\�b�h���̃J�E���^���C���N�������g
     */
    public static void incrementCounter()
    {
        calledMethodCount__++;
    }
    
    /**
     * �Ăяo���ꂽ���\�b�h���̃J�E���^���擾
     * @return �Ăяo���ꂽ���\�b�h��
     */
    public static long getCounter()
    {
        return calledMethodCount__;
    }
    

}
