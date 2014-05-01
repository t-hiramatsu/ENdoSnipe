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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;

/**
 * �X���b�h�Ɋւ������\���N���X�B
 * 
 * @author tsukano
 * @param <E> �X���b�h���Ƃ��ĕێ�����l�̌^
 */
public class ThreadInfo<E>
{
    /** �{�N���X�̃C���X�^���X�쐬�ɗ��p����JavelinLogElement */
    private JavelinLogElement javelinLogElement_;

    /** �X���b�h�� */
    private String            threadName_;

    /** �X���b�h���Ƃ̌v���l */
    private E                 value_;

    /**
     * @return javelinLogElement
     */
    public JavelinLogElement getJavelinLogElement()
    {
        return this.javelinLogElement_;
    }

    /**
     * @param javelinLogElement �ݒ肷�� javelinLogElement
     */
    public void setJavelinLogElement(final JavelinLogElement javelinLogElement)
    {
        this.javelinLogElement_ = javelinLogElement;
    }

    /**
     * @return value
     */
    public E getValue()
    {
        return this.value_;
    }

    /**
     * @param value �ݒ肷�� value
     */
    public void setValue(final E value)
    {
        this.value_ = value;
    }

    /**
     * @return threadName
     */
    public String getThreadName()
    {
        return this.threadName_;
    }

    /**
     * @param threadName �ݒ肷�� threadName
     */
    public void setThreadName(final String threadName)
    {
        this.threadName_ = threadName;
    }
}
