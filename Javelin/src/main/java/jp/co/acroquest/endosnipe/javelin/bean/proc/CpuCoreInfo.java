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
package jp.co.acroquest.endosnipe.javelin.bean.proc;

/**
 * コアごとのCPU使用時間。
 * 
 * @author eriguchi
 */
public class CpuCoreInfo
{
    /** CPUユーザ */
    private long cpuUser_;

    /** CPUシステム */
    private long cpuSystem_;

    /** CPUタスク */
    private long cpuTask_;

    /** CPU　I/O待機時間 */
    private long cpuIoWait_;

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public long getCpuUser()
    {
        return cpuUser_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuUser CPUユーザ。
     */
    public void setCpuUser(long cpuUser)
    {
        cpuUser_ = cpuUser;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public long getCpuSystem()
    {
        return cpuSystem_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuSystem CPUユーザ。
     */
    public void setCpuSystem(long cpuSystem)
    {
        cpuSystem_ = cpuSystem;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public long getCpuTask()
    {
        return cpuTask_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuTask CPUユーザ。
     */
    public void setCpuTask(long cpuTask)
    {
        cpuTask_ = cpuTask;
    }

    /**
     * CPUユーザを取得する。
     * 
     * @return CPUユーザ。
     */
    public long getCpuIoWait()
    {
        return cpuIoWait_;
    }

    /**
     * CPUユーザを設定する。
     * 
     * @param cpuIoWait CPUユーザ。
     */
    public void setCpuIoWait(long cpuIoWait)
    {
        cpuIoWait_ = cpuIoWait;
    }

}
