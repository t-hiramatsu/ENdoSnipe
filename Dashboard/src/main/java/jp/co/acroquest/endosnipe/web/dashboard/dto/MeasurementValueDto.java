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
package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * 計測項目のDTO。
 * 
 * @author akiba
 */
public class MeasurementValueDto
{
    /** 計測項目ID。 */
    private int measurementItemId_;

    /** 計測項目名。 */
    private String measurementItemName_;

    /** 計測時刻。 */
    private long measurementTime_;

    /** 計測項目値。 */
    private String measurementValue_;

    /**
     * コンストラクタ。
     */
    public MeasurementValueDto()
    {

    }

    /**
     * @return the measurementItemId
     */
    public int getMeasurementItemId()
    {
        return this.measurementItemId_;
    }

    /**
     * @param measurementItemId the measurementItemId to set
     */
    public void setMeasurementItemId(final int measurementItemId)
    {
        this.measurementItemId_ = measurementItemId;
    }

    /**
     * @return the measurementItemName
     */
    public String getMeasurementItemName()
    {
        return this.measurementItemName_;
    }

    /**
     * @param measurementItemName the measurementItemName to set
     */
    public void setMeasurementItemName(final String measurementItemName)
    {
        this.measurementItemName_ = measurementItemName;
    }

    /**
     * @return the measurementTime
     */
    public long getMeasurementTime()
    {
        return this.measurementTime_;
    }

    /**
     * @param measurementTime the measurementTime to set
     */
    public void setMeasurementTime(final long measurementTime)
    {
        this.measurementTime_ = measurementTime;
    }

    /**
     * @return the measurementValue
     */
    public String getMeasurementValue()
    {
        return this.measurementValue_;
    }

    /**
     * @param measurementValue the measurementValue to set
     */
    public void setMeasurementValue(final String measurementValue)
    {
        this.measurementValue_ = measurementValue;
    }
}
