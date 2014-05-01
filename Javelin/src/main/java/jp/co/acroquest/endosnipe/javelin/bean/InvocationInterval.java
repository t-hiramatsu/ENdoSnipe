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

import java.io.Serializable;

/**
 * ���\�b�h�̓��v���
 * @author eriguchi
 *
 */
public class InvocationInterval implements Serializable
{
    /** �V���A��ID */
    private static final long serialVersionUID = 7855547784390235717L;

    /** �����l */
    private static final long INITIAL = -1;

    /** ���\�b�h�̎������� */
    private long interval_;

    /** ���\�b�h��CPU���� */
    private long cpuInterval_;

    /** ���\�b�h�̃��[�U���� */
    private long userInterval_;

    /**
     * �R���X�g���N�^
     */
    public InvocationInterval()
    {
        this.interval_ = INITIAL;
        this.cpuInterval_ = INITIAL;
        this.userInterval_ = INITIAL;
    }

    /**
     * ���\�b�h�̎��s���Ԃ��擾����B
     * @return ���\�b�h�̎��s����
     */
    public long getInterval()
    {
        return this.interval_;
    }

    /**
     * ���\�b�h�̎��s���Ԃ�ݒ肷��B
     * @param interval ���\�b�h�̎��s����
     */
    public void setInterval(final long interval)
    {
        this.interval_ = interval;
    }

    /**
     * ���\�b�h��CPU���Ԃ��擾����B
     * @return ���\�b�h��CPU����
     */
    public long getCpuInterval()
    {
        return this.cpuInterval_;
    }

    /**
     * ���\�b�h��CPU���Ԃ�ݒ肷��B
     * @param cpuInterval ���\�b�h��CPU����
     */
    public void setCpuInterval(final long cpuInterval)
    {
        this.cpuInterval_ = cpuInterval;
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ��擾����B
     * @return ���\�b�h�̃��[�U����
     */
    public long getUserInterval()
    {
        return this.userInterval_;
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ�ݒ肷��B
     * @param userInterval ���\�b�h�̃��[�U����
     */
    public void setUserInterval(final long userInterval)
    {
        this.userInterval_ = userInterval;
    }

    /**
     * �R���X�g���N�^
     * @param interval ���\�b�h�̎��s����
     * @param cpuInterval ���\�b�h��CPU����
     * @param userInterval ���\�b�h�̃��[�U����
     */
    public InvocationInterval(final long interval, final long cpuInterval, final long userInterval)
    {
        super();
        this.interval_ = interval;
        this.cpuInterval_ = cpuInterval;
        this.userInterval_ = userInterval;
    }
}
