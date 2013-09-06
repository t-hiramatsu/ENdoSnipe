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
 *　/proc/meminfoの内容を保持するBean
 * 
 * @author eriguchi
 */
public class MemInfo
{
    /** システム全体のメモリ最大値 */
    private long memTotal_;
    
    /** システム全体の空きメモリ */
    private long memFree_;
    
    /** システム全体のバッファ */ 
    private long bufferes_;

    /** システム全体のキャッシュ */ 
    private long cached_;
    
    /** システム全体のスワップ最大量 */ 
    private long swapTotal_;
    
    /** システム全体のスワップ空き容量 */ 
    private long swapFree_;
    
    /** システム全体の仮想メモリ使用量 */ 
    private long vmallocTotal_;
    
    /**
     * システム全体のメモリ最大値を取得します。
     * @return システム全体のメモリ最大値
     */
    public long getMemTotal()
    {
        return memTotal_;
    }
    
    /**
     * システム全体のメモリ最大値を設定します。
     * @param memTotal システム全体のメモリ最大値
     */
    public void setMemTotal(long memTotal)
    {
        memTotal_ = memTotal;
    }
    
    /**
     * システム全体の空きメモリを取得します。
     * @return システム全体の空きメモリ
     */
    public long getMemFree()
    {
        return memFree_;
    }
    
    /** 
     * システム全体の空きメモリを設定します。
     * @param memFree システム全体の空きメモリ
     */
    public void setMemFree(long memFree)
    {
        memFree_ = memFree;
    }
    
    /**
     * システム全体のバッファを取得します。   
     * @return システム全体のバッファ
     */
    public long getBufferes()
    {
        return bufferes_;
    }
    
    /**
     * システム全体のバッファを設定します。
     * @param bufferes システム全体のバッファ
     */
    public void setBufferes(long bufferes)
    {
        bufferes_ = bufferes;
    }
    
    /**
     * システム全体のキャッシュを取得します。
     * @return システム全体のキャッシュ
     */
    public long getCached()
    {
        return cached_;
    }
    
    /**
     * システム全体のキャッシュを設定します。
     * @param cached システム全体のキャッシュ
     */
    public void setCached(long cached)
    {
        cached_ = cached;
    }
    
    /**
     * システム全体のスワップ最大量を取得します。
     * @return システム全体のスワップ最大量
     */
    public long getSwapTotal()
    {
        return swapTotal_;
    }
    
    /**
     * システム全体のスワップ最大量を設定します。
     * @param swapTotal システム全体のスワップ最大量
     */
    public void setSwapTotal(long swapTotal)
    {
        swapTotal_ = swapTotal;
    }
    
    /**
     * システム全体のスワップ空き容量を取得します。
     * @return システム全体のスワップ空き容量
     */
    public long getSwapFree()
    {
        return swapFree_;
    }
    
    /**
     * システム全体のスワップ空き容量を設定します。
     * @param swapFree システム全体のスワップ空き容量
     */
    public void setSwapFree(long swapFree)
    {
        swapFree_ = swapFree;
    }
    
    /**
     * システム全体の仮想メモリ使用量を取得します。
     * @return システム全体の仮想メモリ使用量
     */
    public long getVmallocTotal()
    {
        return vmallocTotal_;
    }
    
    /**
     * システム全体の仮想メモリ使用量を設定します。
     * @param vmallocTotal システム全体の仮想メモリ使用量
     */
    public void setVmallocTotal(long vmallocTotal)
    {
        vmallocTotal_ = vmallocTotal;
    }
    
}
