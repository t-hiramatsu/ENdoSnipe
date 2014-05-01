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
package jp.co.acroquest.endosnipe.web.explorer.dto;

/**
 * MultipleResourceGraphのDTOクラス。
 * 
 * @author pin
 * 
 */

public class MultipleResourceGraphDefinitionDto
{
    /** multipleResourceGraphのID。 */
    private long multipleResourceGraphId_;

    /** multipleResourceGraph名。 */
    private String multipleResourceGraphName_;

    /** measurementItemId's list. */
    private String measurementItemIdList_;

    /** measurementItemPattern . */
    private String measurementItemPattern_;

    /**
     * コンストラクタ。
     */
    public MultipleResourceGraphDefinitionDto()
    {
        //nothing
    }

    /**
     * MultipleResourceGraph IDを取得する。
     * 
     * @return MultipleResourceGraph ID
     */
    public long getMultipleResourceGraphId()
    {
        return multipleResourceGraphId_;
    }

    /**
     * MultipleResourceGraphIDを設定する。
     * 
     * @param multipleResourceGraphId
     *            MultipleResourceGraphID
     */
    public void setMultipleResourceGraphId(final long multipleResourceGraphId)
    {
        multipleResourceGraphId_ = multipleResourceGraphId;
    }

    /**
     * MultipleResourceGraph名を取得する。
     * 
     * @return MultipleResourceGraph名
     */
    public String getMultipleResourceGraphName()
    {
        return multipleResourceGraphName_;
    }

    /**
     * MultipleResourceGraph名を設定する。
     * 
     * @param multipleResourceGraphName
     *            multipleResourceGraph名
     */
    public void setMultipleResourceGraphName(final String multipleResourceGraphName)
    {
        multipleResourceGraphName_ = multipleResourceGraphName;
    }

    /**
     * MeasurementItemIdListを取得する。
     * 
     * @return MeasurementItemId List
     */
    public String getMeasurementItemIdList()
    {
        return measurementItemIdList_;
    }

    /**
     * measurementItemIdListを設定する。
     * 
     * @param measurementItemIdList
     *            measurementItemId list
     */
    public void setMeasurementItemIdList(final String measurementItemIdList)
    {
        measurementItemIdList_ = measurementItemIdList;
    }

    /**
     * measurementItemPatternを取得する。
     * @return measurement Item Pattern
     */
    public String getMeasurementItemPattern()
    {
        return measurementItemPattern_;
    }

    /**
     * measurementItemPatternを設定する。
     * @param measurementItemPattern regular expression pattern
     */
    public void setMeasurementItemPattern(final String measurementItemPattern)
    {
        measurementItemPattern_ = measurementItemPattern;
    }

}
