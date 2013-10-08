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
package jp.co.acroquest.endosnipe.data.dto;

import java.util.List;

import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

public class SummarySignalDefinitionDto
{

    public long summarySignalId;

    public String summarySignalName;

    public int summmarySignalType;

    public List<String> signalList;

    public int summarySignalStatus;

    public String errorMessage;

    public int priority;

    @Override
    public String toString()
    {
        return "SummarySignalDefinitionDto [summarySignalId=" + summarySignalId
            + ", summarySignalName=" + summarySignalName + ", summmarySignalType="
            + summmarySignalType + ", signalList=" + signalList + ", summarySignalStatus="
            + summarySignalStatus + ", errorMessage=" + errorMessage + ", priority=" + priority
            + "]";
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getSummarySignalId()
    {
        return summarySignalId;
    }

    public void setSummarySignalId(long summarySignalId)
    {
        this.summarySignalId = summarySignalId;
    }

    public String getSummarySignalName()
    {
        return summarySignalName;
    }

    public void setSummarySignalName(String summarySignalName)
    {
        this.summarySignalName = summarySignalName;
    }

    public int getSummmarySignalType()
    {
        return summmarySignalType;
    }

    public void setSummmarySignalType(int summmarySignalType)
    {
        this.summmarySignalType = summmarySignalType;
    }

    public List<String> getSignalList()
    {
        return signalList;
    }

    public void setSignalList(List<String> signalList)
    {
        this.signalList = signalList;
    }

    public int getSummarySignalStatus()
    {
        return summarySignalStatus;
    }

    public void setSummarySignalStatus(int summarySignalStatus)
    {
        this.summarySignalStatus = summarySignalStatus;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public SummarySignalDefinitionDto(SummarySignalDefinition summarySignalDefinition)
    {
        this.summarySignalId = summarySignalDefinition.summarySignalId;
        this.summarySignalName = summarySignalDefinition.summarySignalName;
        this.summmarySignalType = summarySignalDefinition.summarySignalType;
        this.summarySignalStatus = summarySignalDefinition.summarySignalStatus;
        this.signalList = summarySignalDefinition.signalList;
        this.errorMessage=summarySignalDefinition.errorMessage;
        this.priority=summarySignalDefinition.priority;

    }

}
