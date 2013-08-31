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
 * ProcInfoクラス
 * @author acroquest
 *
 */
public class ProcInfo
{
    /** /proc/meminfoの以下の情報 */
    private MemInfo      memInfo_;

    /** /proc/statの以下の情報 */
    private StatInfo     statInfo_;

    /** /proc/self/statの以下の情報 */
    private SelfStatInfo selfStatInfo_;

    /** /proc/diskStatsの以下の情報 */
    private DiskStats    diskStats_;

    /**
     * /proc/meminfoの以下の情報を取得します。
     * @return /proc/meminfoの以下の情報
     */
    public MemInfo getMemInfo()
    {
        return this.memInfo_;
    }

    /**
     * /proc/meminfoの以下の情報を設定します。
     * @param memInfo /proc/meminfoの以下の情報
     */
    public void setMemInfo(MemInfo memInfo)
    {
        this.memInfo_ = memInfo;
    }

    /**
     * /proc/statの以下の情報を取得します。
     * @return /proc/statの以下の情報
     */
    public StatInfo getStatInfo()
    {
        return this.statInfo_;
    }

    /**
     * /proc/statの以下の情報を設定します。
     * @param statInfo /proc/statの以下の情報
     */
    public void setStatInfo(StatInfo statInfo)
    {
        this.statInfo_ = statInfo;
    }

    /**
     * /proc/self/statの以下の情報を取得します。
     * @return /proc/self/statの以下の情報
     */
    public SelfStatInfo getSelfStatInfo()
    {
        return this.selfStatInfo_;
    }

    /**
     * /proc/self/statの以下の情報を設定します。
     * @param selfStatInfo /proc/self/statの以下の情報
     */
    public void setSelfStatInfo(SelfStatInfo selfStatInfo)
    {
        this.selfStatInfo_ = selfStatInfo;
    }

    /** 
     * /proc/diskStatsの以下の情報を設定します。
     * @param diskStats /proc/diskStatsの以下の情報
     */
    public void setDiskStats(DiskStats diskStats)
    {
        this.diskStats_ = diskStats;
    }

    /**
     * /proc/diskStatsの以下の情報を取得します。
     * @return /proc/diskStatsの以下の情報
     */
    public DiskStats getDiskStats()
    {
        return diskStats_;
    }
}
