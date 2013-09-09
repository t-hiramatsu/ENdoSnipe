/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * CPU/メモリ使用量の１標本を示すエンティティクラス。
 * 
 * @author M.Yoshida
 */
public class ProcessResourceRecord
{
    /** 計測時刻 */
    private Timestamp measurementTime_;

    /** CPU使用率[%] (補間期間平均) */
    private double    cpuUsage_;

    /** CPU使用率[%] (補間期間最大) */
    private double    cpuUsageMax_;

    /** CPU使用率[%] (補間期間最小) */
    private double    cpuUsageMin_;
    
    /** CPU使用率（システム）[%] (補間期間平均) */
    private double    cpuUsageSys_;

    /** CPU使用率（システム）[%] (補間期間最大) */
    private double    cpuUsageSysMax_;

    /** CPU使用率（システム）[%] (補間期間最小) */
    private double    cpuUsageSysMin_;

    /** 物理メモリ使用量（システム）[B] */
    private double    physicalMemoryAll_;

    /** 物理メモリ使用量（プロセス）[B] */
    private double    physicalMemoryProcess_;

    /** スワップメモリ使用量[B] */
    private double    swapMemory_;

    /** ヒープメモリ使用量（最大）[B] */
    private double    heapMemoryMax_;

    /** ヒープメモリ使用量（現在）[B] */
    private double    heapMemoryNow_;

    /** ヒープメモリ使用量（現在）[B] 最大値 */
    private double    heapMemoryNowMax_;

    /** ヒープメモリ使用量（現在）[B] 最小値 */
    private double    heapMemoryNowMin_;

    /** 非ヒープメモリ使用量（最大）[B] */
    private double    nonHeapMemoryMax_;

    /** 非ヒープメモリ使用量（現在）[B] */
    private double    nonHeapMemoryNow_;

    /** 非ヒープメモリ使用量（現在）[B] 最大値*/
    private double    nonHeapMemoryNowMax_;

    /** 非ヒープメモリ使用量（現在）[B] 最小値*/
    private double    nonHeapMemoryNowMin_;

    /** 仮想マシンメモリ使用量（最大）[B] */
    private double    vmMemoryMax_;

    /** 仮想マシンメモリ使用量（現在）[B] */
    private double    vmMemoryNow_;

    /** 仮想マシンメモリ使用量（現在）[B] 最大値*/
    private double    vmMemoryNowMax_;

    /** 仮想マシンメモリ使用量（現在）[B] 最小値*/
    private double    vmMemoryNowMin_;

    /** ファイル記述子/ハンドル数 */
    private double    fdCount_;

    /** ファイル記述子/ハンドル数 最大値 */
    private double    fdCountMax_;

    /** ファイル記述子/ハンドル数 最小値 */
    private double    fdCountMin_;
    
    /** メジャーフォールト数 */
    private double    majorFault_;
    
    /** メジャーフォールト数　最大値 */
    private double    majorFaultMax_;
    
    /** メジャーフォールト数　最小値 */
    private double    majorFaultMin_;
    
    /** プロセスのメモリ使用量　仮想メモリ使用量（プロセス） */
    private double    virtualMem_;
    
    /** プロセスのメモリ使用量　仮想メモリ使用量（プロセス）　最大値 */
    private double    virtualMemMax_;
    
    /** プロセスのメモリ使用量　仮想メモリ使用量（プロセス）　最小値 */
    private double    virtualMemMin_;
    
    /** プロセスのメモリ使用量　物理メモリ使用量（プロセス） */
    private double    physicalMem_;
    
    /** プロセスのメモリ使用量　物理メモリ使用量（プロセス）　最大値 */
    private double    physicalMemMax_;
    
    /** プロセスのメモリ使用量　物理メモリ使用量（プロセス）　最小値 */
    private double    physicalMemMin_;
    
    /**
     * @return the measurementTime
     */
    public Timestamp getMeasurementTime()
    {
        return measurementTime_;
    }

    /**
     * @return the cpuUsage
     */
    public double getCpuUsage()
    {
        return cpuUsage_;
    }

    /**
     * @return the cpuUsageMax
     */
    public double getCpuUsageMax()
    {
        return cpuUsageMax_;
    }

    /**
     * @return the cpuUsageMin
     */
    public double getCpuUsageMin()
    {
        return cpuUsageMin_;
    }
    
    /**
     * @return the cpuUsageSys
     */
    public double getCpuUsageSys( )
    {
        return this.cpuUsageSys_;
    }

    /**
     * @return the cpuUsageSysMax
     */
    public double getCpuUsageSysMax( )
    {
        return this.cpuUsageSysMax_;
    }

    /**
     * @return the cpuUsageSysMin
     */
    public double getCpuUsageSysMin( )
    {
        return this.cpuUsageSysMin_;
    }

    /**
     * @return the physicalMemoryAll
     */
    public double getPhysicalMemoryAll()
    {
        return physicalMemoryAll_;
    }

    /**
     * @return the physicalMemoryProcess
     */
    public double getPhysicalMemoryProcess()
    {
        return physicalMemoryProcess_;
    }

    /**
     * @return the swapMemory
     */
    public double getSwapMemory()
    {
        return swapMemory_;
    }

    /**
     * @return the heapMemoryMax
     */
    public double getHeapMemoryMax()
    {
        return heapMemoryMax_;
    }

    /**
     * @return the heapMemoryNow
     */
    public double getHeapMemoryNow()
    {
        return heapMemoryNow_;
    }

    /**
     * @return the heapMemoryNowMax
     */
    public double getHeapMemoryNowMax()
    {
        return heapMemoryNowMax_;
    }

    /**
     * @return the heapMemoryNowMin
     */
    public double getHeapMemoryNowMin()
    {
        return heapMemoryNowMin_;
    }

    /**
     * @return the nonHeapMemoryMax
     */
    public double getNonHeapMemoryMax()
    {
        return nonHeapMemoryMax_;
    }

    /**
     * @return the nonHeapMemoryNow
     */
    public double getNonHeapMemoryNow()
    {
        return nonHeapMemoryNow_;
    }

    /**
     * @return the nonHeapMemoryNowMax
     */
    public double getNonHeapMemoryNowMax()
    {
        return nonHeapMemoryNowMax_;
    }

    /**
     * @return the nonHeapMemoryNowMin
     */
    public double getNonHeapMemoryNowMin()
    {
        return nonHeapMemoryNowMin_;
    }

    /**
     * @return the vmMemoryMax
     */
    public double getVmMemoryMax()
    {
        return vmMemoryMax_;
    }

    /**
     * @return the vmMemoryNow
     */
    public double getVmMemoryNow()
    {
        return vmMemoryNow_;
    }

    /**
     * @return the vmMemoryNowMax
     */
    public double getVmMemoryNowMax()
    {
        return vmMemoryNowMax_;
    }

    /**
     * @return the vmMemoryNowMin
     */
    public double getVmMemoryNowMin()
    {
        return vmMemoryNowMin_;
    }

    /**
     * @param measurementTime the measurementTime to set
     */
    public void setMeasurementTime(Timestamp measurementTime)
    {
        measurementTime_ = measurementTime;
    }

    /**
     * @param cpuUsage the cpuUsage to set
     */
    public void setCpuUsage(double cpuUsage)
    {
        cpuUsage_ = cpuUsage;
    }

    /**
     * @param cpuUsageMax the cpuUsageMax to set
     */
    public void setCpuUsageMax(double cpuUsageMax)
    {
        cpuUsageMax_ = cpuUsageMax;
    }

    /**
     * @param cpuUsageMin the cpuUsageMin to set
     */
    public void setCpuUsageMin(double cpuUsageMin)
    {
        cpuUsageMin_ = cpuUsageMin;
    }
    
    /**
     * @param cpuUsageSys the cpuUsageSys to set
     */
    public void setCpuUsageSys(double cpuUsageSys)
    {
        this.cpuUsageSys_ = cpuUsageSys;
    }
    
    /**
     * @param cpuUsageSysMax the cpuUsageSysMax to set
     */
    public void setCpuUsageSysMax(double cpuUsageSysMax)
    {
        this.cpuUsageSysMax_ = cpuUsageSysMax;
    }
    
    /**
     * @param cpuUsageSysMin the cpuUsageSysMin to set
     */
    public void setCpuUsageSysMin(double cpuUsageSysMin)
    {
        this.cpuUsageSysMin_ = cpuUsageSysMin;
    }

    /**
     * @param physicalMemoryAll the physicalMemoryAll to set
     */
    public void setPhysicalMemoryAll(double physicalMemoryAll)
    {
        physicalMemoryAll_ = physicalMemoryAll;
    }

    /**
     * @param physicalMemoryProcess the physicalMemoryProcess to set
     */
    public void setPhysicalMemoryProcess(double physicalMemoryProcess)
    {
        physicalMemoryProcess_ = physicalMemoryProcess;
    }

    /**
     * @param swapMemory the swapMemory to set
     */
    public void setSwapMemory(double swapMemory)
    {
        swapMemory_ = swapMemory;
    }

    /**
     * @param heapMemoryMax the heapMemoryMax to set
     */
    public void setHeapMemoryMax(double heapMemoryMax)
    {
        heapMemoryMax_ = heapMemoryMax;
    }

    /**
     * @param heapMemoryNow the heapMemoryNow to set
     */
    public void setHeapMemoryNow(double heapMemoryNow)
    {
        heapMemoryNow_ = heapMemoryNow;
    }

    /**
     * @param heapMemoryNowMax the heapMemoryNowMax to set
     */
    public void setHeapMemoryNowMax(double heapMemoryNowMax)
    {
        heapMemoryNowMax_ = heapMemoryNowMax;
    }

    /**
     * @param heapMemoryNowMin the heapMemoryNowMin to set
     */
    public void setHeapMemoryNowMin(double heapMemoryNowMin)
    {
        heapMemoryNowMin_ = heapMemoryNowMin;
    }

    /**
     * @param nonHeapMemoryMax the nonHeapMemoryMax to set
     */
    public void setNonHeapMemoryMax(double nonHeapMemoryMax)
    {
        nonHeapMemoryMax_ = nonHeapMemoryMax;
    }

    /**
     * @param nonHeapMemoryNow the nonHeapMemoryNow to set
     */
    public void setNonHeapMemoryNow(double nonHeapMemoryNow)
    {
        nonHeapMemoryNow_ = nonHeapMemoryNow;
    }

    /**
     * @param nonHeapMemoryNowMax the nonHeapMemoryNowMax to set
     */
    public void setNonHeapMemoryNowMax(double nonHeapMemoryNowMax)
    {
        nonHeapMemoryNowMax_ = nonHeapMemoryNowMax;
    }

    /**
     * @param nonHeapMemoryNowMin the nonHeapMemoryNowMin to set
     */
    public void setNonHeapMemoryNowMin(double nonHeapMemoryNowMin)
    {
        nonHeapMemoryNowMin_ = nonHeapMemoryNowMin;
    }

    /**
     * @param vmMemoryMax the vmMemoryMax to set
     */
    public void setVmMemoryMax(double vmMemoryMax)
    {
        vmMemoryMax_ = vmMemoryMax;
    }

    /**
     * @param vmMemoryNow the vmMemoryNow to set
     */
    public void setVmMemoryNow(double vmMemoryNow)
    {
        vmMemoryNow_ = vmMemoryNow;
    }

    /**
     * @param vmMemoryNowMax the vmMemoryNowMax to set
     */
    public void setVmMemoryNowMax(double vmMemoryNowMax)
    {
        vmMemoryNowMax_ = vmMemoryNowMax;
    }

    /**
     * @param vmMemoryNowMin the vmMemoryNowMin to set
     */
    public void setVmMemoryNowMin(double vmMemoryNowMin)
    {
        vmMemoryNowMin_ = vmMemoryNowMin;
    }

    public double getFdCount()
    {
        return fdCount_;
    }

    public void setFdCount(double fdCountNow)
    {
        fdCount_ = fdCountNow;
    }

    public double getFdCountMax()
    {
        return fdCountMax_;
    }

    public void setFdCountMax(double fdCountNowMax)
    {
        fdCountMax_ = fdCountNowMax;
    }

    public double getFdCountMin()
    {
        return fdCountMin_;
    }

    public void setFdCountMin(double fdCountNowMin)
    {
        fdCountMin_ = fdCountNowMin;
    }

    /**
     * @return the majorFault
     */
    public double getMajorFault( )
    {
        return this.majorFault_;
    }

    /**
     * @param majorFault the majorFault to set
     */
    public void setMajorFault(double majorFault)
    {
        this.majorFault_ = majorFault;
    }

    /**
     * @return the majorFaultMax
     */
    public double getMajorFaultMax( )
    {
        return this.majorFaultMax_;
    }

    /**
     * @param majorFaultMax the majorFaultMax to set
     */
    public void setMajorFaultMax(double majorFaultMax)
    {
        this.majorFaultMax_ = majorFaultMax;
    }

    /**
     * @return the majorFaultMin
     */
    public double getMajorFaultMin( )
    {
        return this.majorFaultMin_;
    }

    /**
     * @param majorFaultMin the majorFaultMin to set
     */
    public void setMajorFaultMin(double majorFaultMin)
    {
        this.majorFaultMin_ = majorFaultMin;
    }

    /**
     * @return the virtualMem
     */
    public double getVirtualMem( )
    {
        return this.virtualMem_;
    }

    /**
     * @param virtualMem the virtualMem to set
     */
    public void setVirtualMem(double virtualMem)
    {
        this.virtualMem_ = virtualMem;
    }

    /**
     * @return the virtualMemMax
     */
    public double getVirtualMemMax( )
    {
        return this.virtualMemMax_;
    }

    /**
     * @param virtualMemMax the virtualMemMax to set
     */
    public void setVirtualMemMax(double virtualMemMax)
    {
        this.virtualMemMax_ = virtualMemMax;
    }

    /**
     * @return the virtualMemMin
     */
    public double getVirtualMemMin( )
    {
        return this.virtualMemMin_;
    }

    /**
     * @param virtualMemMin the virtualMemMin to set
     */
    public void setVirtualMemMin(double virtualMemMin)
    {
        this.virtualMemMin_ = virtualMemMin;
    }

    /**
     * @return the physicalMem
     */
    public double getPhysicalMem( )
    {
        return this.physicalMem_;
    }

    /**
     * @param physicalMem the physicalMem to set
     */
    public void setPhysicalMem(double physicalMem)
    {
        this.physicalMem_ = physicalMem;
    }

    /**
     * @return the physicalMemMax
     */
    public double getPhysicalMemMax( )
    {
        return this.physicalMemMax_;
    }

    /**
     * @param physicalMemMax the physicalMemMax to set
     */
    public void setPhysicalMemMax(double physicalMemMax)
    {
        this.physicalMemMax_ = physicalMemMax;
    }

    /**
     * @return the physicalMemMin
     */
    public double getPhysicalMemMin( )
    {
        return this.physicalMemMin_;
    }

    /**
     * @param physicalMemMin the physicalMemMin to set
     */
    public void setPhysicalMemMin(double physicalMemMin)
    {
        this.physicalMemMin_ = physicalMemMin;
    }
}
