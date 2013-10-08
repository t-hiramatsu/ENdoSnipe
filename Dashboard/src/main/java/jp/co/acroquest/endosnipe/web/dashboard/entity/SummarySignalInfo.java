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

public class SummarySignalInfo
{
    public int summarySignalId;

    public String summarySignalName;

    public static String targetSignalId;

    public int summarySignalType;

    public int priorityNo;

    public String errorMessage;

    /**
     * {@link SignalInfo} オブジェクトを生成します。<br />
     */

    public SummarySignalInfo()
    {
        this.summarySignalId = -1;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getTargetSignalId()
    {
        return targetSignalId;
    }

    public void setTargetSignalId(final String targetSignalId)
    {
        this.targetSignalId = targetSignalId;
    }

    public int getSummarySignalId()
    {
        return summarySignalId;
    }

    public void setSummarySignalId(final int summarySignalId)
    {
        this.summarySignalId = summarySignalId;
    }

    public String getSummarySignalName()
    {
        return summarySignalName;
    }

    public void setSummarySignalName(final String summarySignalName)
    {
        this.summarySignalName = summarySignalName;
    }

    public int getSummarySignalType()
    {
        return summarySignalType;
    }

    public void setSummarySignalType(final int summarySignalType)
    {
        this.summarySignalType = summarySignalType;
    }

    public int getPriorityNo()
    {
        return priorityNo;
    }

    public void setPriorityNo(final int priorityNo)
    {
        this.priorityNo = priorityNo;
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
