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
package jp.co.acroquest.endosnipe.web.explorer.form;

import java.util.List;

/**
 * ThreadDump data getting class
 * @author khinewai
 *
 */
public class ThreadDumpDataForm
{
    /**　starting time　*/
    private String       startTime_;

    /** ending time */
    private String       endTime_;

    /**　data id　*/
    private List<String> dataGroupList_;

    /**　maximum line number　*/
    private String       maxLineNum_;

    /**
     * this is default constructor
     */
    public ThreadDumpDataForm()
    {

    }

    /**
     * starting time
     * 
     * @return start time
     */
    public String getStartTime()
    {
        return startTime_;
    }

    /**
     * set starting time
     * 
     * @param startTime data
     */
    public void setStartTime(final String startTime)
    {
        startTime_ = startTime;
    }

    /**
     * end time
     * 
     * @return end time
     */
    public String getEndTime()
    {
        return endTime_;
    }

    /**
     * end time
     * 
     * @param endTime data
     */
    public void setEndTime(final String endTime)
    {
        endTime_ = endTime;
    }

    /**
     * data id
     * @return data id
     */
    public List<String> getDataGroupList()
    {
        return dataGroupList_;
    }

    /**
     * adding data id
     * @param dataGroupList id
     */
    public void setDataGroupList(final List<String> dataGroupList)
    {
        dataGroupList_ = dataGroupList;
    }

    /**
     * maximum line
     * @return maximum line data
     */
    public String getMaxLineNum()
    {
        return maxLineNum_;
    }

    /**
     * maximum line 
     * @param maxLineNum line data
     */
    public void setMaxLineNum(final String maxLineNum)
    {
        this.maxLineNum_ = maxLineNum;
    }
}
