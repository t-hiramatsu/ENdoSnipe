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
package jp.co.acroquest.endosnipe.web.dashboard.entity;

/**
 * MultipleResourceGraphのentityクラス。
 * 
 * @author pin
 * 
 */
public class MultipleResourceGraphInfo
{
    /** multipleResourceGraphのID。*/
    public long multipleResourceGraphId_;

    /** multipleResourceGraphのID。 */
    public String multipleResourceGraphName_;

    /** measurementItemId's list. */
    public String measurementItemIdList_;

    /**
     * {@link MultipleResourceGraphInfo} MultipleResourceGraphトを生成します。<br />
     */
    public MultipleResourceGraphInfo()
    {
        this.multipleResourceGraphId_ = -1;
    }

    @Override
    public String toString()
    {
        return String.format("MultipleResourceGraphID%d MultipleResourceGraphName:%s MeasurementItemIdList_:%s",
                             multipleResourceGraphId_, multipleResourceGraphName_,
                             measurementItemIdList_);
    }
}
