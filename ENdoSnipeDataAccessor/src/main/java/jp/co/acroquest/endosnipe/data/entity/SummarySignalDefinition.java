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
package jp.co.acroquest.endosnipe.data.entity;

import java.util.List;

/**
 * 
 * @author khinewai
 *
 */
public class SummarySignalDefinition
{

    public long summarySignalId;

    public String summarySignalName;

    public int summarySignalType;

    public List<String> signalList;

    public int summarySignalStatus;

    public String errorMessage;

    public int priority;

    public List<String> getSignalList()
    {
        return signalList;
    }

    public void setSignalList(List<String> signalList)
    {
        this.signalList = signalList;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public void setSummarySignalId(long summarySignalId)
    {
        this.summarySignalId = summarySignalId;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public SummarySignalDefinition()
    {
        this.summarySignalId = -1;
    }

    public long getSummarySignalId()
    {
        return summarySignalId;
    }

    public void setSummarySignalId(int summarySignalId)
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

    public int getSummarySignalType()
    {
        return summarySignalType;
    }

    public void setSummarySignalType(int summarySignalType)
    {
        this.summarySignalType = summarySignalType;
    }

    public int getSummarySignalStatus()
    {
        return summarySignalStatus;
    }

    public void setSummarySignalStatus(int summarySignalStatus)
    {
        this.summarySignalStatus = summarySignalStatus;
    }

    /* public String getErrorMessage()
     {
         return errorMessage;
     }

     public void setErrorMessage(String errorMessage)
     {
         this.errorMessage = errorMessage;
     }*/

}
