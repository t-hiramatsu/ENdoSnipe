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
 * @author eriguchi
 */
public class SystemResourceRecord
{
    /** �v������ */
    private Timestamp measurementTime_;

    /** ���vCPU�g�p��[%] (��Ԋ��ԕ���) */
    private double    cpuUsage_;

    /** ���vCPU�g�p��[%] (��Ԋ��ԍő�) */
    private double    cpuUsageMax_;

    /** ���vCPU�g�p��[%] (�ŏ�) */
    private double    cpuUsageMin_;

    /** �V�X�e��CPU�g�p��[%] (����) */
    private double    sysCpuUsage_;

    /** �V�X�e��CPU�g�p��[%] (�ő�) */
    private double    sysCpuUsageMax_;

    /** �V�X�e��CPU�g�p��[%] (��Ԋ��ԍŏ�) */
    private double    sysCpuUsageMin_;

    /** �����������g�p�ʁi�ő�j[B] */
    private double    physicalMemoryMax_;

    /** �����������g�p�ʁi���ݍő�j[B] */
    private double    physicalMemoryUseMax_;

    /** �����������g�p�ʁi���ݍŏ��j[B] */
    private double    physicalMemoryUseMin_;

    /** �����������g�p�ʁi���ݕ��ρj[B] */
    private double    physicalMemoryUse_;

    /** �X���b�v�������g�p��(�ő�)[B] */
    private double    swapMemoryMax_;

    /** �X���b�v�������g�p��(���ݍő�)[B] */
    private double    swapMemoryUseMax_;

    /** �X���b�v�������g�p��(���ݍŏ�)[B] */
    private double    swapMemoryUseMin_;

    /** �X���b�v�������g�p��(���ݕ���)[B] */
    private double    swapMemoryUse_;

    /** �y�[�W�C��(���ݍő�) */
    private double    pageInMax_;

    /** �y�[�W�C��(���ݍŏ�) */
    private double    pageInMin_;

    /** �y�[�W�C��(���ݕ���) */
    private double    pageOut_;

    /** �y�[�W�C��(���ݍő�) */
    private double    pageOutMax_;
    
    /** �t�@�C���L�q�q/�n���h���� */
    private double    fdCount_;

    /** �t�@�C���L�q�q/�n���h���� �ő�l */
    private double    fdCountMax_;

    /** �t�@�C���L�q�q/�n���h���� �ŏ��l */
    private double    fdCountMin_;

    public double getPhysicalMemoryUseMax()
    {
        return physicalMemoryUseMax_;
    }

    public void setPhysicalMemoryUseMax(double physicalMemoryUseMax)
    {
        physicalMemoryUseMax_ = physicalMemoryUseMax;
    }

    public double getPhysicalMemoryUseMin()
    {
        return physicalMemoryUseMin_;
    }

    public void setPhysicalMemoryUseMin(double physicalMemoryUseMin)
    {
        physicalMemoryUseMin_ = physicalMemoryUseMin;
    }

    public double getPhysicalMemoryUse()
    {
        return physicalMemoryUse_;
    }

    public void setPhysicalMemoryUse(double physicalMemoryUse)
    {
        physicalMemoryUse_ = physicalMemoryUse;
    }

    public double getSwapMemoryUseMax()
    {
        return swapMemoryUseMax_;
    }

    public void setSwapMemoryUseMax(double swapMemoryUseMax)
    {
        swapMemoryUseMax_ = swapMemoryUseMax;
    }

    public double getSwapMemoryUseMin()
    {
        return swapMemoryUseMin_;
    }

    public void setSwapMemoryUseMin(double swapMemoryUseMin)
    {
        swapMemoryUseMin_ = swapMemoryUseMin;
    }

    public double getSwapMemoryUse()
    {
        return swapMemoryUse_;
    }

    public void setSwapMemoryUse(double swapMemoryUse)
    {
        swapMemoryUse_ = swapMemoryUse;
    }

    public double getPageInMax()
    {
        return pageInMax_;
    }

    public void setPageInMax(double pageInMax)
    {
        pageInMax_ = pageInMax;
    }

    public double getPageInMin()
    {
        return pageInMin_;
    }

    public void setPageInMin(double pageInMin)
    {
        pageInMin_ = pageInMin;
    }

    public double getPageOut()
    {
        return pageOut_;
    }

    public void setPageOut(double pageOut)
    {
        pageOut_ = pageOut;
    }

    public double getPageOutMax()
    {
        return pageOutMax_;
    }

    public void setPageOutMax(double pageOutMax)
    {
        pageOutMax_ = pageOutMax;
    }

    public double getPageOutMin()
    {
        return pageOutMin_;
    }

    public void setPageOutMin(double pageOutMin)
    {
        pageOutMin_ = pageOutMin;
    }

    public double getPageIn()
    {
        return pageIn_;
    }

    public void setPageIn(double pageIn)
    {
        pageIn_ = pageIn;
    }

    /** �y�[�W�C��(���ݍŏ�) */
    private double pageOutMin_;

    /** �y�[�W�C��(���ݕ���) */
    private double pageIn_;

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

    public double getSysCpuUsage()
    {
        return sysCpuUsage_;
    }

    public void setSysCpuUsage(double sysCpuUsage)
    {
        sysCpuUsage_ = sysCpuUsage;
    }

    public double getSysCpuUsageMax()
    {
        return sysCpuUsageMax_;
    }

    public void setSysCpuUsageMax(double sysCpuUsageMax)
    {
        sysCpuUsageMax_ = sysCpuUsageMax;
    }

    public double getSysCpuUsageMin()
    {
        return sysCpuUsageMin_;
    }

    public void setSysCpuUsageMin(double sysCpuUsageMin)
    {
        sysCpuUsageMin_ = sysCpuUsageMin;
    }

    public double getPhysicalMemoryMax()
    {
        return physicalMemoryMax_;
    }

    public void setPhysicalMemoryMax(double physicalMemoryMax)
    {
        physicalMemoryMax_ = physicalMemoryMax;
    }


    public double getSwapMemoryMax()
    {
        return swapMemoryMax_;
    }

    public void setSwapMemoryMax(double swapMemoryMax)
    {
        swapMemoryMax_ = swapMemoryMax;
    }

    public double getFdCount()
    {
        return fdCount_;
    }

    public void setFdCount(double fdCount)
    {
        fdCount_ = fdCount;
    }

    public double getFdCountMax()
    {
        return fdCountMax_;
    }

    public void setFdCountMax(double fdCountMax)
    {
        fdCountMax_ = fdCountMax;
    }

    public double getFdCountMin()
    {
        return fdCountMin_;
    }

    public void setFdCountMin(double fdCountMin)
    {
        fdCountMin_ = fdCountMin;
    }

}
