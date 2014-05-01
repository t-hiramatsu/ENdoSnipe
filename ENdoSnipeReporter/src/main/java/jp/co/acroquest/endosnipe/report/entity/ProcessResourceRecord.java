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
 * CPU/�������g�p�ʂ̂P�W�{�������G���e�B�e�B�N���X�B
 * 
 * @author M.Yoshida
 */
public class ProcessResourceRecord
{
    /** �v������ */
    private Timestamp measurementTime_;

    /** CPU�g�p��[%] (��Ԋ��ԕ���) */
    private double    cpuUsage_;

    /** CPU�g�p��[%] (��Ԋ��ԍő�) */
    private double    cpuUsageMax_;

    /** CPU�g�p��[%] (��Ԋ��ԍŏ�) */
    private double    cpuUsageMin_;
    
    /** CPU�g�p���i�V�X�e���j[%] (��Ԋ��ԕ���) */
    private double    cpuUsageSys_;

    /** CPU�g�p���i�V�X�e���j[%] (��Ԋ��ԍő�) */
    private double    cpuUsageSysMax_;

    /** CPU�g�p���i�V�X�e���j[%] (��Ԋ��ԍŏ�) */
    private double    cpuUsageSysMin_;

    /** �����������g�p�ʁi�V�X�e���j[B] */
    private double    physicalMemoryAll_;

    /** �����������g�p�ʁi�v���Z�X�j[B] */
    private double    physicalMemoryProcess_;

    /** �X���b�v�������g�p��[B] */
    private double    swapMemory_;

    /** �q�[�v�������g�p�ʁi�ő�j[B] */
    private double    heapMemoryMax_;

    /** �q�[�v�������g�p�ʁi���݁j[B] */
    private double    heapMemoryNow_;

    /** �q�[�v�������g�p�ʁi���݁j[B] �ő�l */
    private double    heapMemoryNowMax_;

    /** �q�[�v�������g�p�ʁi���݁j[B] �ŏ��l */
    private double    heapMemoryNowMin_;

    /** ��q�[�v�������g�p�ʁi�ő�j[B] */
    private double    nonHeapMemoryMax_;

    /** ��q�[�v�������g�p�ʁi���݁j[B] */
    private double    nonHeapMemoryNow_;

    /** ��q�[�v�������g�p�ʁi���݁j[B] �ő�l*/
    private double    nonHeapMemoryNowMax_;

    /** ��q�[�v�������g�p�ʁi���݁j[B] �ŏ��l*/
    private double    nonHeapMemoryNowMin_;

    /** ���z�}�V���������g�p�ʁi�ő�j[B] */
    private double    vmMemoryMax_;

    /** ���z�}�V���������g�p�ʁi���݁j[B] */
    private double    vmMemoryNow_;

    /** ���z�}�V���������g�p�ʁi���݁j[B] �ő�l*/
    private double    vmMemoryNowMax_;

    /** ���z�}�V���������g�p�ʁi���݁j[B] �ŏ��l*/
    private double    vmMemoryNowMin_;

    /** �t�@�C���L�q�q/�n���h���� */
    private double    fdCount_;

    /** �t�@�C���L�q�q/�n���h���� �ő�l */
    private double    fdCountMax_;

    /** �t�@�C���L�q�q/�n���h���� �ŏ��l */
    private double    fdCountMin_;
    
    /** ���W���[�t�H�[���g�� */
    private double    majorFault_;
    
    /** ���W���[�t�H�[���g���@�ő�l */
    private double    majorFaultMax_;
    
    /** ���W���[�t�H�[���g���@�ŏ��l */
    private double    majorFaultMin_;
    
    /** �v���Z�X�̃������g�p�ʁ@���z�������g�p�ʁi�v���Z�X�j */
    private double    virtualMem_;
    
    /** �v���Z�X�̃������g�p�ʁ@���z�������g�p�ʁi�v���Z�X�j�@�ő�l */
    private double    virtualMemMax_;
    
    /** �v���Z�X�̃������g�p�ʁ@���z�������g�p�ʁi�v���Z�X�j�@�ŏ��l */
    private double    virtualMemMin_;
    
    /** �v���Z�X�̃������g�p�ʁ@�����������g�p�ʁi�v���Z�X�j */
    private double    physicalMem_;
    
    /** �v���Z�X�̃������g�p�ʁ@�����������g�p�ʁi�v���Z�X�j�@�ő�l */
    private double    physicalMemMax_;
    
    /** �v���Z�X�̃������g�p�ʁ@�����������g�p�ʁi�v���Z�X�j�@�ŏ��l */
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
