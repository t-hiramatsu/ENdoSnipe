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
package jp.co.acroquest.endosnipe.javelin;

/**
 * CallTree���ŌĂ΂ꂽ���\�b�h�̌v������ۑ�����I�u�W�F�N�g�ł��B

 * @author fujii
 *
 */
public class CallTreeMeasurement
{
    /** �N���X�� */
    private String className_;

    /** ���\�b�h�� */
    private String methodName_;

    /** ���\�b�h�Ăяo���� */
    private long count_;

    /** ���\�b�h�̎������� */
    private long total_;

    /**
     * ���\�b�h�̌Ăяo���񐔂ƃ��\�b�h�̎��s���Ԃ�ݒ肵�܂��B<br />
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param count ���\�b�h�̌Ăяo����
     * @param total ���\�b�h�̎��s����
     */
    public CallTreeMeasurement(String className, String methodName, long count, long total)
    {
        this.className_ = className;
        this.methodName_ = methodName;
        this.count_ = count;
        this.total_ = total;
    }

    /**
     * �v���l�������܂��B<br />
     * 
     * @param interval �v���l
     */
    public void addInterval(long interval)
    {
        this.total_ += interval;
    }

    /**
     * ���s�񐔂��P���₵�܂��B<br />
     * 
     */
    public void incrementCount()
    {
        this.count_++;
    }

    /**
     * @return count
     */
    public long getCount()
    {
        return this.count_;
    }

    /**
     * @return interval
     */
    public long getTotal()
    {
        return this.total_;
    }

    /**
     * @return className
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * @return methodName
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.className_ == null) ? 0 : this.className_.hashCode());
        result = PRIME * result + ((this.methodName_ == null) ? 0 : this.methodName_.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CallTreeMeasurement OTHER = (CallTreeMeasurement)obj;
        if (this.className_ == null)
        {
            if (OTHER.className_ != null)
            {
                return false;
            }
        }
        else if (!this.className_.equals(OTHER.className_))
        {
            return false;
        }
        if (this.methodName_ == null)
        {
            if (OTHER.methodName_ != null)
            {
                return false;
            }
        }
        else if (!this.methodName_.equals(OTHER.methodName_))
        {
            return false;
        }
        return true;
    }
}
