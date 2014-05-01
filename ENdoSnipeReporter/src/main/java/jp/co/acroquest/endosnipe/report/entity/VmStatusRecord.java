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
 * VM状態レポートに出力する１レコード分の情報を保持するエンティティ。
 * 
 * @author akiba
 */
public class VmStatusRecord
{
    /** 計測時刻 */
    private Timestamp measurementTime_;
    
    /** スレッド数(Native)(平均) */
    private double    nativeThreadNum_;

    /** スレッド数(Native)(最大) */
    private double    nativeThreadNumMax_;

    /** スレッド数(Native)(最小) */
    private double    nativeThreadNumMin_;

    /** スレッド数(Java)(平均) */
    private double    threadNum_;

    /** スレッド数(Java)(最大) */
    private double    threadNumMax_;

    /** スレッド数(Java)(最小) */
    private double    threadNumMin_;

    /** GC停止時間(平均) */
    private double    gcStopTime_;

    /** GC停止時間(最大) */
    private double    gcStopTimeMax_;

    /** GC停止時間(最小) */
    private double    gcStopTimeMin_;

    /** VMスループット(平均) */
    private double    vmThroughput_;

    /** VMスループット(最大) */
    private double    vmThroughputMax_;

    /** VMスループット(最小) */
    private double    vmThroughputMin_;

    /** ファイナライズ待ちオブジェクト数(平均) */
    private double    finalizeObjNum_;

    /** ファイナライズ待ちオブジェクト数(最大) */
    private double    finalizeObjNumMax_;

    /** ファイナライズ待ちオブジェクト数(最小) */
    private double    finalizeObjNumMin_;
    
    /** ロードされたクラスの合計数(平均) */
    private double    totalLoadedClassNum_;
    
    /** ロードされたクラスの合計数(最大) */
    private double    totalLoadedClassNumMax_;
    
    /** ロードされたクラスの合計数(最小) */
    private double    totalLoadedClassNumMin_;

    /** 現在ロードされているクラスの数 */
    private double    loadedClassNum_;
    
    /** 現在ロードされているクラスの数(最大) */
    private double    loadedClassNumMax_;
    
    /** 現在ロードされているクラスの数(最小) */
    private double    loadedClassNumMin_;
    
    /**
     * @return the measurementTime
     */
    public Timestamp getMeasurementTime()
    {
        return this.measurementTime_;
    }

    /**
     * @param measurementTime the measurementTime to set
     */
    public void setMeasurementTime(Timestamp measurementTime)
    {
        this.measurementTime_ = measurementTime;
    }

    /**
     * @return the nativeThreadNum
     */
    public double getNativeThreadNum( )
    {
        return this.nativeThreadNum_;
    }

    /**
     * @param nativeThreadNum the nativeThreadNum to set
     */
    public void setNativeThreadNum(double nativeThreadNum)
    {
        this.nativeThreadNum_ = nativeThreadNum;
    }

    /**
     * @return the nativeThreadNumMax
     */
    public double getNativeThreadNumMax( )
    {
        return this.nativeThreadNumMax_;
    }

    /**
     * @param nativeThreadNumMax the nativeThreadNumMax to set
     */
    public void setNativeThreadNumMax(double nativeThreadNumMax)
    {
        this.nativeThreadNumMax_ = nativeThreadNumMax;
    }

    /**
     * @return the nativeThreadNumMin
     */
    public double getNativeThreadNumMin( )
    {
        return this.nativeThreadNumMin_;
    }

    /**
     * @param nativeThreadNumMin the nativeThreadNumMin to set
     */
    public void setNativeThreadNumMin(double nativeThreadNumMin)
    {
        this.nativeThreadNumMin_ = nativeThreadNumMin;
    }

    /**
     * @return the threadNum
     */
    public double getThreadNum()
    {
        return this.threadNum_;
    }

    /**
     * @param threadNum the threadNum to set
     */
    public void setThreadNum(double threadNum)
    {
        this.threadNum_ = threadNum;
    }

    /**
     * @return the threadNumMax
     */
    public double getThreadNumMax()
    {
        return this.threadNumMax_;
    }

    /**
     * @param threadNumMax the threadNumMax to set
     */
    public void setThreadNumMax(double threadNumMax)
    {
        this.threadNumMax_ = threadNumMax;
    }

    /**
     * @return the threadNumMin
     */
    public double getThreadNumMin()
    {
        return this.threadNumMin_;
    }

    /**
     * @param threadNumMin the threadNumMin to set
     */
    public void setThreadNumMin(double threadNumMin)
    {
        this.threadNumMin_ = threadNumMin;
    }

    /**
     * @return the gcStopTime
     */
    public double getGcStopTime()
    {
        return this.gcStopTime_;
    }

    /**
     * @param gcStopTime the gcStopTime to set
     */
    public void setGcStopTime(double gcStopTime)
    {
        this.gcStopTime_ = gcStopTime;
    }

    /**
     * @return the gcStopTimeMax
     */
    public double getGcStopTimeMax()
    {
        return this.gcStopTimeMax_;
    }

    /**
     * @param gcStopTimeMax the gcStopTimeMax to set
     */
    public void setGcStopTimeMax(double gcStopTimeMax)
    {
        this.gcStopTimeMax_ = gcStopTimeMax;
    }

    /**
     * @return the gcStopTimeMin
     */
    public double getGcStopTimeMin()
    {
        return this.gcStopTimeMin_;
    }

    /**
     * @param gcStopTimeMin the gcStopTimeMin to set
     */
    public void setGcStopTimeMin(double gcStopTimeMin)
    {
        this.gcStopTimeMin_ = gcStopTimeMin;
    }

    /**
     * @return the vmThroughput
     */
    public double getVmThroughput()
    {
        return this.vmThroughput_;
    }

    /**
     * @param vmThroughput the vmThroughput to set
     */
    public void setVmThroughput(double vmThroughput)
    {
        this.vmThroughput_ = vmThroughput;
    }

    /**
     * @return the vmThroughputMax
     */
    public double getVmThroughputMax()
    {
        return this.vmThroughputMax_;
    }

    /**
     * @param vmThroughputMax the vmThroughputMax to set
     */
    public void setVmThroughputMax(double vmThroughputMax)
    {
        this.vmThroughputMax_ = vmThroughputMax;
    }

    /**
     * @return the vmThroughputMin
     */
    public double getVmThroughputMin()
    {
        return this.vmThroughputMin_;
    }

    /**
     * @param vmThroughputMin the vmThroughputMin to set
     */
    public void setVmThroughputMin(double vmThroughputMin)
    {
        this.vmThroughputMin_ = vmThroughputMin;
    }

    /**
     * @return the finalizeObjNum
     */
    public double getFinalizeObjNum()
    {
        return this.finalizeObjNum_;
    }

    /**
     * @param finalizeObjNum the finalizeObjNum to set
     */
    public void setFinalizeObjNum(double finalizeObjNum)
    {
        this.finalizeObjNum_ = finalizeObjNum;
    }

    /**
     * @return the finalizeObjNumMax
     */
    public double getFinalizeObjNumMax()
    {
        return this.finalizeObjNumMax_;
    }

    /**
     * @param finalizeObjNumMax the finalizeObjNumMax to set
     */
    public void setFinalizeObjNumMax(double finalizeObjNumMax)
    {
        this.finalizeObjNumMax_ = finalizeObjNumMax;
    }

    /**
     * @return the finalizeObjNumMin
     */
    public double getFinalizeObjNumMin()
    {
        return this.finalizeObjNumMin_;
    }

    /**
     * @param finalizeObjNumMin the finalizeObjNumMin to set
     */
    public void setFinalizeObjNumMin(double finalizeObjNumMin)
    {
        this.finalizeObjNumMin_ = finalizeObjNumMin;
    }

    /**
     * @return the totalLoadedClassNum
     */
    public double getTotalLoadedClassNum( )
    {
        return this.totalLoadedClassNum_;
    }

    /**
     * @param totalLoadedClassNum the totalLoadedClassNum to set
     */
    public void setTotalLoadedClassNum(double totalLoadedClassNum)
    {
        this.totalLoadedClassNum_ = totalLoadedClassNum;
    }

    /**
     * @return the totalLoadedClassNumMax
     */
    public double getTotalLoadedClassNumMax( )
    {
        return this.totalLoadedClassNumMax_;
    }

    /**
     * @param totalLoadedClassNumMax the totalLoadedClassNumMax to set
     */
    public void setTotalLoadedClassNumMax(double totalLoadedClassNumMax)
    {
        this.totalLoadedClassNumMax_ = totalLoadedClassNumMax;
    }

    /**
     * @return the totalLoadedClassNumMin
     */
    public double getTotalLoadedClassNumMin( )
    {
        return this.totalLoadedClassNumMin_;
    }

    /**
     * @param totalLoadedClassNumMin the totalLoadedClassNumMin to set
     */
    public void setTotalLoadedClassNumMin(double totalLoadedClassNumMin)
    {
        this.totalLoadedClassNumMin_ = totalLoadedClassNumMin;
    }

    /**
     * @return the loadedClassNum
     */
    public double getLoadedClassNum()
    {
        return this.loadedClassNum_;
    }

    /**
     * @param loadedClassNum the loadedClassNum to set
     */
    public void setLoadedClassNum(double loadedClassNum)
    {
        this.loadedClassNum_ = loadedClassNum;
    }

    /**
     * @return the loadedClassNumMax
     */
    public double getLoadedClassNumMax( )
    {
        return this.loadedClassNumMax_;
    }

    /**
     * @param loadedClassNumMax the loadedClassNumMax to set
     */
    public void setLoadedClassNumMax(double loadedClassNumMax)
    {
        this.loadedClassNumMax_ = loadedClassNumMax;
    }

    /**
     * @return the loadedClassNumMin
     */
    public double getLoadedClassNumMin( )
    {
        return this.loadedClassNumMin_;
    }

    /**
     * @param loadedClassNumMin the loadedClassNumMin to set
     */
    public void setLoadedClassNumMin(double loadedClassNumMin)
    {
        this.loadedClassNumMin_ = loadedClassNumMin;
    }
}
