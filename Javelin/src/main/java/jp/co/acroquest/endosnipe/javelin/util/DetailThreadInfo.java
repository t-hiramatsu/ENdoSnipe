/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.javelin.util;

/**
 * スレッド情報を保持する。
 * 
 * @author eriguchi
 */
public class DetailThreadInfo
{
    /** RUNNABLEのスレッドの数 */
    private int runnableCount_;

    /** BLOCKEDのスレッドの数 */
    private int blockedCount_;

    /**
     * RUNNABLEのスレッドの数を取得する。
     * 
     * @return RUNNABLEのスレッドの数
     */
    public int getRunnableCount()
    {
        return runnableCount_;
    }

    /**
     * RUNNABLEのスレッドの数を設定する。
     * 
     * @param runnableCount RUNNABLEのスレッドの数
     */
    public void setRunnableCount(int runnableCount)
    {
        runnableCount_ = runnableCount;
    }

    /**
     * BLOCKEDのスレッドの数を取得する。
     * 
     * @return BLOCKEDのスレッドの数
     */
    public int getBlockedCount()
    {
        return blockedCount_;
    }

    /**
     * BLOCKEDのスレッドの数を設定する。
     * 
     * @param blockedCount BLOCKEDのスレッドの数
     */
    public void setBlockedCount(int blockedCount)
    {
        blockedCount_ = blockedCount;
    }

}
