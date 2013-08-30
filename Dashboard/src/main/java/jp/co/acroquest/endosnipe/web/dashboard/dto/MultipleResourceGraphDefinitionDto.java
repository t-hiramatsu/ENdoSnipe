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
package jp.co.acroquest.endosnipe.web.dashboard.dto;

public class MultipleResourceGraphDefinitionDto
{
    /** シグナル定義テーブルのID。 */
    private long multipleResourceGraphId_;

    /** シグナル名。 */
    private String multipleResourceGraphName_;

    /** マッチングパターン。 */
    private String measurementItemIdList_;

    public long getMultipleResourceGraphId()
    {
        return multipleResourceGraphId_;
    }

    public void setMultipleResourceGraphId(final long multipleResourceGraphId)
    {
        multipleResourceGraphId_ = multipleResourceGraphId;
    }

    public String getMultipleResourceGraphName()
    {
        return multipleResourceGraphName_;
    }

    public void setMultipleResourceGraphName(final String multipleResourceGraphName)
    {
        multipleResourceGraphName_ = multipleResourceGraphName;
    }

    public String getMeasurementItemIdList()
    {
        return measurementItemIdList_;
    }

    public void setMeasurementItemIdList(final String measurementItemIdList)
    {
        measurementItemIdList_ = measurementItemIdList;
    }

}
