/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.data.dto;

import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.data.entity.MultipleResourceGraphDefinition;

/**
 * 閾値判定条件定義情報Dtoです。<br />
 * 
 * @author fujii
 *
 */
public class MultipleResourceGraphDefinitionDto
{
    /** シグナル定義テーブルのID */
    private long                 multipleResourceGraphId_;

    /** シグナル名 */
    private String               multipleResourceGraphName_;

    /** マッチングパターン */
    private String               measurementItemIdList_;

    /** 閾値のスプリットパターン */
    private static final Pattern SPLIT_PATERN = Pattern.compile(",");

    /**
     * シグナル定義テーブルのIDを取得する。
     * @return シグナル定義テーブルのID
     */
    public long getMultipleResourceGraphId()
    {
        return multipleResourceGraphId_;
    }

    /**
     * シグナル定義テーブルのIDを設定する。
     * @param multipleResourceGraphId シグナル定義テーブルのID
     */
    public void setMultipleResourceGraphId(long multipleResourceGraphId)
    {
        this.multipleResourceGraphId_ = multipleResourceGraphId;
    }

    /**
     * シグナル名を取得する。
     * @return シグナル名
     */
    public String getMultipleResourceGraphName()
    {
        return multipleResourceGraphName_;
    }

    /**
     * シグナル名を設定する。
     * @param multipleResourceGraphName シグナル名
     */
    public void setMultipleResourceGraphName(String multipleResourceGraphName)
    {
        this.multipleResourceGraphName_ = multipleResourceGraphName;
    }

    /**
     * マッチングパターンを取得する。
     * @return マッチングパターン名
     */
    public String getMeasurementItemIdList()
    {
        return measurementItemIdList_;
    }

    /**
     * マッチングパターンを設定する。
     * @param measurementItemIdList マッチングパターン名
     */
    public void setMeasurementItemIdList(String measurementItemIdList)
    {
        this.measurementItemIdList_ = measurementItemIdList;
    }

    /**
     * {@link MultipleResourceGraphDefinitionDto} オブジェクトを生成します。
     * @param MultipleResourceGraphDefinition {@link MultipleResourceGraphDefinition}オブジェクト
     */
    public MultipleResourceGraphDefinitionDto(MultipleResourceGraphDefinition MultipleResourceGraphDefinition)
    {
        this.multipleResourceGraphId_ = MultipleResourceGraphDefinition.multipleResourceGraphId;
        this.multipleResourceGraphName_ = MultipleResourceGraphDefinition.multipleResourceGraphName;
        this.measurementItemIdList_ = MultipleResourceGraphDefinition.measurementItemIdList;
       
    }

    @Override
    public String toString()
    {
        return "MultipleResourceGraphInfoDto [multipleResourceGraphId=" + multipleResourceGraphId_ + ", multipleResourceGraphName=" + multipleResourceGraphName_
            + ", measurementItemIdList=" + measurementItemIdList_ +"]";
    }
}
