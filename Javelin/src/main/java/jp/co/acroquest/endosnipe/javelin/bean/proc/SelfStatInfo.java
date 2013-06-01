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
 *　/proc/self/statの内容を保持するBean
 * 
 * @author eriguchi
 */
public class SelfStatInfo
{
    /** USER処理時間 */
    private long utime_;

    /** CPUシステム処理時間 */
    private long stime_;

    private long cutime_;

    private long cstime_;

    private long vsize_;

    /** RSS */
    private long rss_;

    /** スレッド数 */
    private long numThreads_;

    /** メジャーフォールト */
    private long majflt_;

    /** プロセスのハンドル使用数 */
    private int  fdCount_;

    /**
     * USER処理時間を取得します。
     * @return USER処理時間
     */
    public long getUtime()
    {
        return utime_;
    }

    /**
     * USER処理時間を設定します。
     * @param utime USER処理時間
     */
    public void setUtime(final long utime)
    {
        utime_ = utime;
    }

    /**
     * CPUシステム処理時間を取得します。
     * @return CPUシステム処理時間
     */
    public long getStime()
    {
        return stime_;
    }

    /**
     * CPUシステム処理時間を設定します。
     * @param stime CPUシステム処理時間
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
     * vsizeを設定します。
     * @param vsize vsize
     */
    public void setVsize(final long vsize)
    {
        vsize_ = vsize;
    }

    /**
     * rssを取得します。
     * @return rss
     */
    public long getRss()
    {
        return rss_;
    }

    /**
     * rssを設定します。
     * @param rss rss
     */
    public void setRss(final long rss)
    {
        rss_ = rss;
    }

    /**
     * スレッド数を取得します。
     * @return スレッド数
     */
    public long getNumThreads()
    {
        return numThreads_;
    }

    /**
     * スレッド数を設定します。
     * @param numThreads スレッド数
     */
    public void setNumThreads(final long numThreads)
    {
        numThreads_ = numThreads;
    }

    /**
     * メジャーフォールトを取得します。
     * @return メジャーフォールト
     */
    public long getMajflt()
    {
        return majflt_;
    }

    /**
     * メジャーフォールトを設定します。
     * @param majflt メジャーフォールト
     */
    public void setMajflt(final long majflt)
    {
        majflt_ = majflt;
    }

    /**
     * プロセスのハンドル使用数を取得します。
     * @return プロセスのハンドル使用数
     */
    public int getFdCount()
    {
        return fdCount_;
    }

    /**
     * プロセスのハンドル使用数を取得します。
     * @param fdcount プロセスのハンドル使用数
     */
    public void setFdCount(final int fdcount)
    {
        fdCount_ = fdcount;
    }

}
