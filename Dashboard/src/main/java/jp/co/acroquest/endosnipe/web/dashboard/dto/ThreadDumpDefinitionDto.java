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

public class ThreadDumpDefinitionDto
{

    public long threadId;

    public String date;

    public String threadDumpInfo;

    private String logFileName_;

    public String getLogFileName()
    {
        return logFileName_;
    }

    public void setLogFileName(final String logFileName)
    {
        logFileName_ = logFileName;
    }

    public long getThreadId()
    {
        return threadId;
    }

    public void setThreadId(final long threadId)
    {
        this.threadId = threadId;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(final String date)
    {
        this.date = date;
    }

    public String getThreadDumpInfo()
    {
        return threadDumpInfo;
    }

    public void setThreadDumpInfo(final String threadDumpInfo)
    {
        this.threadDumpInfo = threadDumpInfo;
    }
}
