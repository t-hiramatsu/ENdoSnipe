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
 * 系列ごとにグラフを出力する場合のデータを
 * 1個保持するクラスです。
 * 
 * @author ochiai
 */
public class ItemRecord
{
	/** 計測時刻 */
    private Timestamp measurementTime_;
    
    /** 補間期間平均　*/
    private long      value_;
    
    /** 補間期間最大 */
    private long      valueMax_;
    
    /** 補間期間最小 */
    private long      valueMin_;
    
    /**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime()
	{
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime)
	{
		this.measurementTime_ = measurementTime;
	}
	
	/**
	 * @return the value_
	 */
	public long getValue()
	{
		return value_;
	}

	/**
	 * @param value the value_ to set
	 */
	public void setValue(long value)
	{
		this.value_ = value;
	}

	/**
	 * @return the valueMax_
	 */
	public long getValueMax()
	{
		return valueMax_;
	}

	/**
	 * @param valueMax the valueMax_ to set
	 */
	public void setValueMax(long valueMax)
	{
		this.valueMax_ = valueMax;
	}

	/**
	 * @return the valueMin_
	 */
	public long getValueMin()
	{
		return valueMin_;
	}

	/**
	 * @param valueMin the valueMin_ to set
	 */
	public void setValueMin(long valueMin)
	{
		this.valueMin_ = valueMin;
	}
}
