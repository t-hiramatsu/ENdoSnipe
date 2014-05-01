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
package jp.co.acroquest.endosnipe.javelin.converter.thread.monitor;

/**
 * CPU使用率。
 * 
 * @author eriguchi
 */
public class CpuUsage
{
    /** CPUユーザ */
    private double cpuUser_;

    /** CPUシステム */
    private double cpuSystem_;

    /** CPU　I/O待機時間 */
    private double cpuIoWait_;

    /**
     * CPU使用率の合計を取得する。
     * 
     * @return CPU使用率の合計
     */
    public double getCpuTotal()
    {
        return this.cpuSystem_ + this.cpuUser_ + this.cpuIoWait_;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public double getCpuUser()
    {
        return cpuUser_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuUser CPUユーザ。
     */
    public void setCpuUser(double cpuUser)
    {
        cpuUser_ = cpuUser;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public double getCpuSystem()
    {
        return cpuSystem_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuSystem CPUユーザ。
     */
    public void setCpuSystem(double cpuSystem)
    {
        cpuSystem_ = cpuSystem;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public double getCpuIoWait()
    {
        return cpuIoWait_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuIoWait CPUユーザ。
     */
    public void setCpuIoWait(double cpuIoWait)
    {
        cpuIoWait_ = cpuIoWait;
    }

}
