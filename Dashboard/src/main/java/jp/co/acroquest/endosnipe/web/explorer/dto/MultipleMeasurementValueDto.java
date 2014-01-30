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
package jp.co.acroquest.endosnipe.web.explorer.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 複数系列のデータを扱うことができるDto
 * 
 * @author nakagawa
 *
 */
public class MultipleMeasurementValueDto
{

    /** 検索文字列 */
    private String searchCondition_;

    /** 計測時刻。 */
    private long measurementTime_;

    /** 複数系列の測定結果 */
    private Map<String, String> measurementValue_ = new HashMap<String, String>();

    /**
     * コンストラクタ。
     */
    public MultipleMeasurementValueDto()
    {

    }

    /**
     * 検索文字列を取得する。
     * 
     * @return 検索文字列
     */
    public String getSearchCondition()
    {
        return searchCondition_;
    }

    /**
     * 検索文字列を設定する。
     * 
     * @param searchCondition 検索文字列
     */
    public void setSearchCondition(final String searchCondition)
    {
        this.searchCondition_ = searchCondition;
    }

    /**
     * 計測時刻を取得する。
     * 
     * @return 計測時刻
     */
    public long getMeasurementTime()
    {
        return measurementTime_;
    }

    /**
     * 計測時刻を設定する。
     * 
     * @param measurementTime 計測時刻
     */
    public void setMeasurementTime(final long measurementTime)
    {
        this.measurementTime_ = measurementTime;
    }

    /**
     * 複数系列の測定結果を取得する。
     * 
     * @return 複数系列の測定結果
     */
    public Map<String, String> getMeasurementValue()
    {
        return measurementValue_;
    }

    /**
     * 複数系列の測定結果を設定する。
     * 
     * @param measurementValue 複数系列の測定結果
     */
    public void setMeasurementValue(final Map<String, String> measurementValue)
    {
        this.measurementValue_ = measurementValue;
    }

}
