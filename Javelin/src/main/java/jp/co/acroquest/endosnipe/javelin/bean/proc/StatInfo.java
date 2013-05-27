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
 *　/proc/statの内容を保持するBean
 * 
 * @author eriguchi
 */
public class StatInfo
{
    /** CPUユーザ */
    private long   cpuUser_;

    /** CPUシステム */
    private long   cpuSystem_;

    /** CPUタスク */
    private long   cpuTask_;

    /** CPU配列 */
    private long[] cpuArray_;

    /** ページイン時間 */
    private long   pageIn_;

    /** ページアウト時間*/
    private long   pageOut_;

    /** FDカウント */
    private long   fdCount_;

    /** CPU　I/O待機時間 */
    private long   cpuIoWait_;

    /**
     * CPUユーザを取得します。
     * @return CPUユーザ
     */
    public long getCpuUser()
    {
        return cpuUser_;
    }

    /**
     * CPUユーザを設定します。
     * @param cpuUser CPUユーザ
     */
    public void setCpuUser(long cpuUser)
    {
        cpuUser_ = cpuUser;
    }

    /**
     * CPUシステムを取得します。
     * @return CPUシステム
     */
    public long getCpuSystem()
    {
        return cpuSystem_;
    }

    /**
     * CPUシステムを設定します。
     * @param cpuSystem CPUシステム
     */
    public void setCpuSystem(long cpuSystem)
    {
        cpuSystem_ = cpuSystem;
    }

    /**
     * CPUタスクを取得します。
     * @return CPUタスク
     */
    public long getCpuTask()
    {
        return cpuTask_;
    }

    /**
     * CPUタスクを設定します。
     * @param cpuTask CPUタスク
     */
    public void setCpuTask(long cpuTask)
    {
        cpuTask_ = cpuTask;
    }

    /**
     * CPU配列を取得します。
     * @return CPU配列
     */
    public long[] getCpuArray()
    {
        return cpuArray_.clone(); 
    }

    /**
     * CPU配列を設定します。
     * @param cpuArray CPU配列
     */
    public void setCpuArray(long[] cpuArray)
    {
        cpuArray_ = cpuArray.clone();
    }

    /**
     * ページイン時間を取得します。
     * @return ページイン時間
     */
    public long getPageIn()
    {
        return pageIn_;
    }

    /**
     * ページイン時間を設定します。
     * @param pageIn ページイン
     */
    public void setPageIn(long pageIn)
    {
        pageIn_ = pageIn;
    }

    /**
     * ページアウト時間を取得します。
     * @return ページアウト時間
     */
    public long getPageOut()
    {
        return pageOut_;
    }

    /**
     * ページアウト時間を設定します。
     * @param pageOut ページアウト時間
     */
    public void setPageOut(long pageOut)
    {
        pageOut_ = pageOut;
    }

    /**
     * FDカウントを取得します。
     * @return FDカウント
     */
    public long getFdCount()
    {
        return fdCount_;
    }

    /**
     * FDカウントを設定します。
     * @param fdCount FDカウント
     */
    public void setFdCount(long fdCount)
    {
        fdCount_ = fdCount;
    }

    /**
     * CPU　I/O待機時間を設定します。
     * @param cpuIoWait CPU I/O待機時間
     */
    public void setCpuIoWait(long cpuIoWait)
    {
        this.cpuIoWait_ = cpuIoWait;
    }

    /**
     * CPU　I/O待機時間を取得します。
     * @return CPU　I/O待機時間
     */
    public long getCpuIoWait()
    {
        return cpuIoWait_;
    }
}
