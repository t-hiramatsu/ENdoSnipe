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
 * Invocation��Bean
 * @author eriguchi
 *
 */
public interface InvocationMBean
{
    /**
     * �N���X�����擾����B
     * @return �N���X��
     */
    String getClassName();

    /**
     * ���\�b�h�����擾����B
     * @return ���\�b�h��
     */
    String getMethodName();

    /**
     * �Ăяo���񐔂��擾����B
     * @return �Ăяo����
     */
    long getCount();

    /**
     * �ŏ��l���擾����B
     * @return �ŏ��l
     */
    long getMinimum();

    /**
     * �ő�l���擾����B
     * @return �ő�l
     */
    long getMaximum();

    /**
     * ���ϒl���擾����B
     * @return ���ϒl
     */
    long getAverage();

    /**
     * CPU���Ԃ̍ŏ��l���擾����B
     * @return CPU���Ԃ̍ŏ��l
     */
    long getCpuMinimum();

    /**
     * CPU���Ԃ̍ő�l���擾����B
     * @return CPU���Ԃ̍ő�l
     */
    long getCpuMaximum();

    /**
     * CPU���Ԃ̕��ϒl���擾����B
     * @return CPU���Ԃ̕��ϒl
     */
    long getCpuAverage();

    /**
     * ��O�����񐔂��擾����B
     * @return ��O������
     */
    long getThrowableCount();

    /**
     * �ŏI�X�V�������擾����B
     * @return �ŏI�X�V����
     */
    long getLastUpdatedTime();

    /**
     * �A���[����������̂��߂�臒l��Ԃ��B
     * @return �A���[����������̂��߂�臒l
     */
    long getAlarmThreshold();

    /**
     * �A���[����������̂��߂�臒l��Ԃ��B
     * @param alarmThreshold �A���[����������̂��߂�臒l
     */
    void setAlarmThreshold(long alarmThreshold);

    /**
     * ���Z�b�g����B
     */
    void reset();
}
