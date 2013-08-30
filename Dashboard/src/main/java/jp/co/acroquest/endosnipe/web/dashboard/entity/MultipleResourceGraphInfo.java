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

public class MultipleResourceGraphInfo
{
    /** シグナル定義テーブルのID。 */
    public long multipleResourceGraphId;

    /** シグナル名。 */
    public String multipleResourceGraphName;

    /** マッチングパターン。 */
    public String measurementItemIdList;

    /**
     * {@link MultipleResourceGraphInfo} オブジェクトを生成します。<br />
     */
    public MultipleResourceGraphInfo()
    {
        this.multipleResourceGraphId = -1;
    }

    @Override
    public String toString()
    {
        return String.format("MultipleResourceGraphID%d MultipleResourceGraphName:%s MeasurementItemIdList_:%s",
                             multipleResourceGraphId, multipleResourceGraphName,
                             measurementItemIdList);
    }
}
