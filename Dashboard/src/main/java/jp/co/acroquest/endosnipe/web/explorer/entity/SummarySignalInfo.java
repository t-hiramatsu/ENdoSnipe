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
package jp.co.acroquest.endosnipe.web.explorer.entity;

/**
 * SummarySignalのentityクラス。
 * 
 * @author pin
 * 
 */
public class SummarySignalInfo
{
    /** SummarySignalのID。 */
    public int summarySignalId;

    /** SummarySignalのName。 */
    public String summarySignalName;

    /** SummarySignalのsignalList。 */
    public static String targetSignalId;

    /** SummarySignalのType。 */
    public int summarySignalType;

    /** SummarySignalのpriorityNo。 */
    public int priorityNo;

    /** SummarySignalのmessage。 */
    public String errorMessage;

    /**
     * {@link SummarySignalInfo} SummarySignalを生成します。<br />
     */

    public SummarySignalInfo()
    {
        this.summarySignalId = -1;
    }

    @Override
    public String toString()
    {
        return "SummarySignalInfo [summarySignalId=" + summarySignalId + ", summarySignalName="
                + summarySignalName + ", targetSignalId=" + targetSignalId + ", summarySignalType="
                + summarySignalType + ", priorityNo=" + priorityNo + ", errorMessage="
                + errorMessage + "]";
    }

}
