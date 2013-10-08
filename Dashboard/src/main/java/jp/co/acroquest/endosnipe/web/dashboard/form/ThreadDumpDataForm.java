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
package jp.co.acroquest.endosnipe.web.dashboard.form;

import java.util.List;

/**
 * 
 * @author khinewai
 *
 */
public class ThreadDumpDataForm
{

    private String startTime_;

    private String endTime_;

    private List<String> dataGroupList_;

    private String maxLineNum;

    public String getStartTime()
    {
        return startTime_;
    }

    public void setStartTime(final String startTime)
    {
        startTime_ = startTime;
    }

    public String getEndTime()
    {
        return endTime_;
    }

    public void setEndTime(final String endTime)
    {
        endTime_ = endTime;
    }

    public List<String> getDataGroupList()
    {
        return dataGroupList_;
    }

    public void setDataGroupList(final List<String> dataGroupList)
    {
        dataGroupList_ = dataGroupList;
    }

    public String getMaxLineNum()
    {
        return maxLineNum;
    }

    public void setMaxLineNum(final String maxLineNum)
    {
        this.maxLineNum = maxLineNum;
    }
}
