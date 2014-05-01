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
 * ProcInfo�N���X
 * @author acroquest
 *
 */
public class ProcInfo
{
    /** /proc/meminfo�̈ȉ��̏�� */
    private MemInfo      memInfo_;

    /** /proc/stat�̈ȉ��̏�� */
    private StatInfo     statInfo_;

    /** /proc/self/stat�̈ȉ��̏�� */
    private SelfStatInfo selfStatInfo_;

    /** /proc/diskStats�̈ȉ��̏�� */
    private DiskStats    diskStats_;

    /**
     * /proc/meminfo�̈ȉ��̏����擾���܂��B
     * @return /proc/meminfo�̈ȉ��̏��
     */
    public MemInfo getMemInfo()
    {
        return this.memInfo_;
    }

    /**
     * /proc/meminfo�̈ȉ��̏���ݒ肵�܂��B
     * @param memInfo /proc/meminfo�̈ȉ��̏��
     */
    public void setMemInfo(MemInfo memInfo)
    {
        this.memInfo_ = memInfo;
    }

    /**
     * /proc/stat�̈ȉ��̏����擾���܂��B
     * @return /proc/stat�̈ȉ��̏��
     */
    public StatInfo getStatInfo()
    {
        return this.statInfo_;
    }

    /**
     * /proc/stat�̈ȉ��̏���ݒ肵�܂��B
     * @param statInfo /proc/stat�̈ȉ��̏��
     */
    public void setStatInfo(StatInfo statInfo)
    {
        this.statInfo_ = statInfo;
    }

    /**
     * /proc/self/stat�̈ȉ��̏����擾���܂��B
     * @return /proc/self/stat�̈ȉ��̏��
     */
    public SelfStatInfo getSelfStatInfo()
    {
        return this.selfStatInfo_;
    }

    /**
     * /proc/self/stat�̈ȉ��̏���ݒ肵�܂��B
     * @param selfStatInfo /proc/self/stat�̈ȉ��̏��
     */
    public void setSelfStatInfo(SelfStatInfo selfStatInfo)
    {
        this.selfStatInfo_ = selfStatInfo;
    }

    /** 
     * /proc/diskStats�̈ȉ��̏���ݒ肵�܂��B
     * @param diskStats /proc/diskStats�̈ȉ��̏��
     */
    public void setDiskStats(DiskStats diskStats)
    {
        this.diskStats_ = diskStats;
    }

    /**
     * /proc/diskStats�̈ȉ��̏����擾���܂��B
     * @return /proc/diskStats�̈ȉ��̏��
     */
    public DiskStats getDiskStats()
    {
        return diskStats_;
    }
}
