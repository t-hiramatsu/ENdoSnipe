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

import java.util.List;

public class SummarySignalDefinitionDto
{
    public int summarySignalId;

    public String summarySignalName;

    public int summarySignalType = -1;

    public List<String> signalList;

    private int summarySignalStatus;

    private int priorityNo;

    private String message;

    public int getPriorityNo()
    {
        return priorityNo;
    }

    public void setPriorityNo(final int priorityNo)
    {
        this.priorityNo = priorityNo;
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

    public List<String> getSignalList()
    {
        return signalList;
    }

    public void setSignalList(final List<String> signalList)
    {
        this.signalList = signalList;
    }

    public int getSummarySignalStatus()
    {
        return summarySignalStatus;
    }

    public void setSummarySignalStatus(final int summarySignalStatus)
    {
        this.summarySignalStatus = summarySignalStatus;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "SummarySignalDefinitionDto [summarySignalId=" + summarySignalId
                + ", summarySignalName=" + summarySignalName + ", summarySignalType="
                + summarySignalType + ", signalList=" + signalList + ", summarySignalStatus="
                + summarySignalStatus + ", priorityNo=" + priorityNo + ", message=" + message + "]";
    }
}
