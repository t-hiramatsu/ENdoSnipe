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

import java.util.Collections;
import java.util.Map;


/**
 * Turn Around Time���B
 * 
 * @author tsukano
 */
public class TurnAroundTimeInfo
{
    /** Turn Around Time�̕��ϒl(�P�ʁF�~���b) */
    private final long turnAroundTime_;

    /** Turn Around Time�̍ő�l(�P�ʁF�~���b) */
    private final long turnAroundTimeMax_;

    /** Turn Around Time�̍ŏ��l(�P�ʁF�~���b) */
    private final long turnAroundTimeMin_;

    /** �Ăяo���� */
    private final int callCount_;

    /** ��O������ */
    private final Map<String, Integer> throwableCountMap_;
    
    /** HTTP�G���[������ */
    private final Map<String, Integer> httpStatusCountMap_;
    
    /** �X�g�[�����o�� */
    private final int methodStallCount_;

    /**
     * Turn Around Time����ݒ肷��B
     * 
     * @param tat Turn Around Time(���ϒl)
     * @param tatMax Turn Around Time(�ő�l)
     * @param tatMin Turn Around Time(�ŏ��l)
     * @param callCount �Ăяo����
     * @param throwableCountMap ��O������
     * @param httpStatusCountMap http�X�e�[�^�X�G���[������
     * @param methodStallCount �X�g�[�����o��
     */
    public TurnAroundTimeInfo(final long tat, final long tatMax,
            final long tatMin, final int callCount, final Map<String, Integer> throwableCountMap,
            final Map<String, Integer> httpStatusCountMap, final int methodStallCount)
    {
        this.turnAroundTime_ = tat;
        this.turnAroundTimeMax_ = tatMax;
        this.turnAroundTimeMin_ = tatMin;
        this.callCount_ = callCount;
        this.throwableCountMap_ = throwableCountMap;
        this.httpStatusCountMap_ = httpStatusCountMap;
        this.methodStallCount_ = methodStallCount;
    }

    /**
     * Turn Around Time(���ϒl)��Ԃ��B
     * 
     * @return Turn Around Time�̕��ϒl
     */
    public long getTurnAroundTime()
    {
        return this.turnAroundTime_;
    }

    /**
     * Turn Around Time(�ő�l)��Ԃ��B
     * 
     * @return Turn Around Time�̍ő�l
     */
    public long getTurnAroundTimeMax()
    {
        return this.turnAroundTimeMax_;
    }

    /**
     * Turn Around Time��Ԃ��B
     * 
     * @return Turn Around Time�̍ŏ��l
     */
    public long getTurnAroundTimeMin()
    {
        return this.turnAroundTimeMin_;
    }

    /**
     * �Ăяo���񐔂�Ԃ��B
     * 
     * @return �Ăяo����
     */
    public int getCallCount()
    {
        return this.callCount_;
    }

    /**
     * ��O�����񐔂�Ԃ��B
     * 
     * @return ��O������
     */
    public Map<String, Integer> getThrowableCountMap()
    {
        return Collections.unmodifiableMap(this.throwableCountMap_);
    } 
    
    /**
     * HTTP�X�e�[�^�X�G���[�����񐔂�Ԃ��B
     * 
     * @return HTTP�X�e�[�^�X�G���[������
     */
    public Map<String, Integer> getHttpStatusCountMap()
    {
        return Collections.unmodifiableMap(this.httpStatusCountMap_);
    } 
    
    /**
     * �X�g�[�����o�񐔂�Ԃ��B
     * 
     * @return �X�g�[�����o��
     */
    public int getMethodStallCount()
    {
        return this.methodStallCount_;
    }
}
