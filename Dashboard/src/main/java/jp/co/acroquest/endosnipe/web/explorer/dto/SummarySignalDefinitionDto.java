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

import java.util.List;

/**
 * SummarySignalのDTOクラス。
 * 
 * @author pin
 * 
 */
public class SummarySignalDefinitionDto
{
    /** SummarySignalのID。 */
    public int summarySignalId_;

    /** SummarySignalのName。 */
    public String summarySignalName_;

    /** SummarySignalのType。 */
    public int summarySignalType_ = -1;

    /** SummarySignalのsignalList。 */
    public List<String> signalList_;

    /** SummarySignalのStatus。 */
    private int summarySignalStatus_;

    /** SummarySignalのpriorityNo。 */
    private int priorityNo_;

    /** SummarySignalのmessage。 */
    private String message_;

    /**
     * コンストラクタ。
     */
    public SummarySignalDefinitionDto()
    {
        //nothing
    }

    /**
     * SummarySignalのpriorityNoを取得する。
     * 
     * @return SummarySignalのpriorityNo
     */
    public int getPriorityNo()
    {
        return priorityNo_;
    }

    /**
     * SummarySignalのpriorityNoを設定する。
     * 
     * @param priorityNo
     *            SummarySignalのpriorityNo
     */
    public void setPriorityNo(final int priorityNo)
    {
        this.priorityNo_ = priorityNo;
    }

    /**
     * SummarySignalのIdを取得する。
     * 
     * @return SummarySignalのId
     */
    public int getSummarySignalId()
    {
        return summarySignalId_;
    }

    /**
     * SummarySignalのIdを設定する。
     * 
     * @param summarySignalId
     *            SummarySignalのId
     */
    public void setSummarySignalId(final int summarySignalId)
    {
        this.summarySignalId_ = summarySignalId;
    }

    /**
     * SummarySignalのNameを取得する。
     * 
     * @return SummarySignalのName
     */
    public String getSummarySignalName()
    {
        return summarySignalName_;
    }

    /**
     * SummarySignalのNameを設定する。
     * 
     * @param summarySignalName
     *            SummarySignalのName
     */
    public void setSummarySignalName(final String summarySignalName)
    {
        this.summarySignalName_ = summarySignalName;
    }

    /**
     * SummarySignalのTypeを取得する。
     * 
     * @return SummarySignalのType
     */
    public int getSummarySignalType()
    {
        return summarySignalType_;
    }

    /**
     * SummarySignalのTypeを設定する。
     * 
     * @param summarySignalType
     *            SummarySignalのType
     */
    public void setSummarySignalType(final int summarySignalType)
    {
        this.summarySignalType_ = summarySignalType;
    }

    /**
     * SummarySignalのSignalListを取得する。
     * 
     * @return SummarySignalのSignalList
     */
    public List<String> getSignalList()
    {
        return signalList_;
    }

    /**
     * SummarySignalのSignalListを設定する。
     * 
     * @param signalList
     *            SummarySignalのSignalList
     */
    public void setSignalList(final List<String> signalList)
    {
        this.signalList_ = signalList;
    }

    /**
     * SummarySignalのStatusを取得する。
     * 
     * @return SummarySignalのStatus
     */
    public int getSummarySignalStatus()
    {
        return summarySignalStatus_;
    }

    /**
     * SummarySignalのStatusを設定する。
     * 
     * @param summarySignalStatus
     *            SummarySignalのStatus
     */
    public void setSummarySignalStatus(final int summarySignalStatus)
    {
        this.summarySignalStatus_ = summarySignalStatus;
    }

    /**
     * SummarySignalのMessageを取得する。
     * 
     * @return SummarySignalのMessage
     */
    public String getMessage()
    {
        return message_;
    }

    /**
     * SummarySignalのMessageを設定する。
     * 
     * @param message
     *            SummarySignalのmessage
     */
    public void setMessage(final String message)
    {
        this.message_ = message;
    }

    @Override
    public String toString()
    {
        return "SummarySignalDefinitionDto [summarySignalId=" + summarySignalId_
                + ", summarySignalName=" + summarySignalName_ + ", summarySignalType="
                + summarySignalType_ + ", signalList=" + signalList_ + ", summarySignalStatus="
                + summarySignalStatus_ + ", priorityNo=" + priorityNo_ + ", message=" + message_
                + "]";
    }
}
