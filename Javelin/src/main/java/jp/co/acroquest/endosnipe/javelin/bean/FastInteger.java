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
package jp.co.acroquest.endosnipe.javelin.bean;


/**
 * �R���N�V�����Ɋi�[���邽�߂� Integer �N���X�B<br />
 *
 * ���������Ȃ����߁A AtomicInteger ���������ł��B<br />
 *
 * @author sakamoto
 */
public class FastInteger
{

    private int value_;

    /**
     * �l�� 0 �o�����������I�u�W�F�N�g�𐶐����܂��B<br />
     */
    public FastInteger()
    {
        this.value_ = 0;
    }

    /**
     * �w�肳�ꂽ�l�ŏ����������I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param value �l
     */
    public FastInteger(final int value)
    {
        this.value_ = value;
    }

    /**
     * �l���Z�b�g���܂��B<br />
     *
     * @param value �l
     */
    public void setValue(final int value)
    {
        this.value_ = value;
    }

    /**
     * �l�� <code>1</code> ���₵�܂��B<br />
     */
    public void increment()
    {
        this.value_++;
    }

    /**
     * �l�� <code>1</code> ���炵�܂��B<br />
     */
    public void decrement()
    {
        this.value_--;
    }

    /**
     * �l�����Z���܂��B<br />
     *
     * @param addValue ���Z����l
     */
    public void add(final int addValue)
    {
        this.value_ += addValue;
    }

    /**
     * �l�����Z���܂��B<br />
     *
     * @param subValue ���Z����l
     */
    public void subtract(final int subValue)
    {
        this.value_ -= subValue;
    }

    /**
     * �l���擾���܂��B<br />
     *
     * @return �l
     */
    public int getValue()
    {
        return this.value_;
    }
}
