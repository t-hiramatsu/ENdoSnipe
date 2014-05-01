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
package jp.co.acroquest.endosnipe.javelin.bean.proc;

/**
 *�@/proc/self/stat�̓��e��ێ�����Bean
 * 
 * @author eriguchi
 */
public class SelfStatInfo
{
    /** USER�������� */
    private long utime_;

    /** CPU�V�X�e���������� */
    private long stime_;

    private long cutime_;

    private long cstime_;

    private long vsize_;

    /** RSS */
    private long rss_;

    /** �X���b�h�� */
    private long numThreads_;

    /** ���W���[�t�H�[���g */
    private long majflt_;

    /** �v���Z�X�̃n���h���g�p�� */
    private int  fdCount_;

    /**
     * USER�������Ԃ��擾���܂��B
     * @return USER��������
     */
    public long getUtime()
    {
        return utime_;
    }

    /**
     * USER�������Ԃ�ݒ肵�܂��B
     * @param utime USER��������
     */
    public void setUtime(final long utime)
    {
        utime_ = utime;
    }

    /**
     * CPU�V�X�e���������Ԃ��擾���܂��B
     * @return CPU�V�X�e����������
     */
    public long getStime()
    {
        return stime_;
    }

    /**
     * CPU�V�X�e���������Ԃ�ݒ肵�܂��B
     * @param stime CPU�V�X�e����������
     */
    public void setStime(final long stime)
    {
        stime_ = stime;
    }

    public long getCutime()
    {
        return cutime_;
    }

    public void setCutime(long cutime)
    {
        cutime_ = cutime;
    }

    public long getCstime()
    {
        return cstime_;
    }

    public void setCstime(long cstime)
    {
        cstime_ = cstime;
    }

    public long getVsize()
    {
        return vsize_;
    }

    /**
     * vsize��ݒ肵�܂��B
     * @param vsize vsize
     */
    public void setVsize(final long vsize)
    {
        vsize_ = vsize;
    }

    /**
     * rss���擾���܂��B
     * @return rss
     */
    public long getRss()
    {
        return rss_;
    }

    /**
     * rss��ݒ肵�܂��B
     * @param rss rss
     */
    public void setRss(final long rss)
    {
        rss_ = rss;
    }

    /**
     * �X���b�h�����擾���܂��B
     * @return �X���b�h��
     */
    public long getNumThreads()
    {
        return numThreads_;
    }

    /**
     * �X���b�h����ݒ肵�܂��B
     * @param numThreads �X���b�h��
     */
    public void setNumThreads(final long numThreads)
    {
        numThreads_ = numThreads;
    }

    /**
     * ���W���[�t�H�[���g���擾���܂��B
     * @return ���W���[�t�H�[���g
     */
    public long getMajflt()
    {
        return majflt_;
    }

    /**
     * ���W���[�t�H�[���g��ݒ肵�܂��B
     * @param majflt ���W���[�t�H�[���g
     */
    public void setMajflt(final long majflt)
    {
        majflt_ = majflt;
    }

    /**
     * �v���Z�X�̃n���h���g�p�����擾���܂��B
     * @return �v���Z�X�̃n���h���g�p��
     */
    public int getFdCount()
    {
        return fdCount_;
    }

    /**
     * �v���Z�X�̃n���h���g�p�����擾���܂��B
     * @param fdcount �v���Z�X�̃n���h���g�p��
     */
    public void setFdCount(final int fdcount)
    {
        fdCount_ = fdcount;
    }

}
