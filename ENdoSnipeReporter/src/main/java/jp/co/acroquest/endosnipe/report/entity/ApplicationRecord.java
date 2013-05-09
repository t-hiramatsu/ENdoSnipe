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
 * アプリケーション情報レポートに出力する１レコード分の情報を保持するエンティティ。
 * 
 * @author akiba
 */
public class ApplicationRecord
{
    /** 計測時刻 */
    private Timestamp httpInstanceMeasurementTime_;

    private Timestamp httpSessionMeasurementTime_;
    
    /** HttpSessionのインスタンス数[数]（補間期間平均） */
    private long      httpSessionInstanceNum_;

    /** HttpSessionのインスタンス数[数]（補間期間最大） */
    private long      httpSessionInstanceNumMax_;

    /** HttpSessionのインスタンス数[数]（補間期間最小） */
    private long      httpSessionInstanceNumMin_;

    /** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間平均） */
    private long      httpSessionObjectSize_;

    /** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間最大） */
    private long      httpSessionObjectSizeMax_;

    /** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間最小） */
    private long      httpSessionObjectSizeMin_;

    /**
     * @return the httpSessionInstanceNum_
     */
    public long getHttpSessionInstanceNum()
    {
        return httpSessionInstanceNum_;
    }

    /**
     * @param httpSessionInstanceNum_ the httpSessionInstanceNum_ to set
     */
    public void setHttpSessionInstanceNum(long httpSessionInstanceNum_)
    {
        this.httpSessionInstanceNum_ = httpSessionInstanceNum_;
    }

    /**
     * @return the httpSessionInstanceNumMax_
     */
    public long getHttpSessionInstanceNumMax()
    {
        return httpSessionInstanceNumMax_;
    }

    /**
     * @param httpSessionInstanceNumMax_ the httpSessionInstanceNumMax_ to set
     */
    public void setHttpSessionInstanceNumMax(long httpSessionInstanceNumMax_)
    {
        this.httpSessionInstanceNumMax_ = httpSessionInstanceNumMax_;
    }

    /**
     * @return the httpSessionInstanceNumMin_
     */
    public long getHttpSessionInstanceNumMin()
    {
        return httpSessionInstanceNumMin_;
    }

    /**
     * @param httpSessionInstanceNumMin_ the httpSessionInstanceNumMin_ to set
     */
    public void setHttpSessionInstanceNumMin(long httpSessionInstanceNumMin_)
    {
        this.httpSessionInstanceNumMin_ = httpSessionInstanceNumMin_;
    }

    /**
     * @return the httpSessionObjectSize_
     */
    public long getHttpSessionObjectSize()
    {
        return httpSessionObjectSize_;
    }

    /**
     * @param httpSessionObjectSize_ the httpSessionObjectSize_ to set
     */
    public void setHttpSessionObjectSize(long httpSessionObjectSize_)
    {
        this.httpSessionObjectSize_ = httpSessionObjectSize_;
    }

    /**
     * @return the httpSessionObjectSizeMax_
     */
    public long getHttpSessionObjectSizeMax()
    {
        return httpSessionObjectSizeMax_;
    }

    /**
     * @param httpSessionObjectSizeMax_ the httpSessionObjectSizeMax_ to set
     */
    public void setHttpSessionObjectSizeMax(long httpSessionObjectSizeMax_)
    {
        this.httpSessionObjectSizeMax_ = httpSessionObjectSizeMax_;
    }

    /**
     * @return the httpSessionObjectSizeMin_
     */
    public long getHttpSessionObjectSizeMin()
    {
        return httpSessionObjectSizeMin_;
    }

    /**
     * @param httpSessionObjectSizeMin_ the httpSessionObjectSizeMin_ to set
     */
    public void setHttpSessionObjectSizeMin(long httpSessionObjectSizeMin_)
    {
        this.httpSessionObjectSizeMin_ = httpSessionObjectSizeMin_;
    }

    /**
     * @param httpInstanceMeasurementTime_ the httpInstanceMeasurementTime_ to set
     */
    public void setHttpInstanceMeasurementTime(
            Timestamp httpInstanceMeasurementTime_)
    {
        this.httpInstanceMeasurementTime_ = httpInstanceMeasurementTime_;
    }

    /**
     * @return the httpInstanceMeasurementTime_
     */
    public Timestamp getHttpInstanceMeasurementTime()
    {
        return httpInstanceMeasurementTime_;
    }

    /**
     * @param httpSessionMeasurementTime_ the httpSessionMeasurementTime_ to set
     */
    public void setHttpSessionMeasurementTime(
            Timestamp httpSessionMeasurementTime_)
    {
        this.httpSessionMeasurementTime_ = httpSessionMeasurementTime_;
    }

    /**
     * @return the httpSessionMeasurementTime_
     */
    public Timestamp getHttpSessionMeasurementTime()
    {
        return httpSessionMeasurementTime_;
    }

}
