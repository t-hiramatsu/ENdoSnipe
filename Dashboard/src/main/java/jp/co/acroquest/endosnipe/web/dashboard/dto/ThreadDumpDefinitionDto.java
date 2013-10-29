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

/**
 * This is threadDumpDefinitionDto class
 * @author khinewai
 *
 */
public class ThreadDumpDefinitionDto
{

    /** threadDump Id */
    public long    threadId_;

    /** threadDump date */
    public String  date_;

    /** threadDump Info */
    public String  threadDumpInfo_;

    /** threadDump log file Name */
    private String logFileName_;

    /**
     * this is default constructor
     */
    public ThreadDumpDefinitionDto()
    {

    }

    /**
     * Javelin log file name
     * @return logFileName
     */
    public String getLogFileName()
    {
        return logFileName_;
    }

    /**
     * Javelin log file name
     * @param logFileName get file name from Javelin
     */
    public void setLogFileName(final String logFileName)
    {
        logFileName_ = logFileName;
    }

    /**
     * starting time from dataBase
     * @return date
     */
    public String getDate()
    {
        return date_;
    }

    /**
     * starting time from dataBase
     * @param date is return
     */
    public void setDate(final String date)
    {
        this.date_ = date;
    }

    /**
     * threadDump Info
     * @return threadDump Info data
     */
    public String getThreadDumpInfo()
    {
        return threadDumpInfo_;
    }

    /**
     * threadDump Info
     * @param threadDumpInfo data 
     */
    public void setThreadDumpInfo(final String threadDumpInfo)
    {
        this.threadDumpInfo_ = threadDumpInfo;
    }

    /**
     * get ThreadDump Id
     * @return threadDump Id
     */
    public long getThreadId()
    {
        return threadId_;
    }

    /**
     * threadDump Id
     * @param threadId data
     */
    public void setThreadId(final long threadId)
    {
        this.threadId_ = threadId;
    }
}
