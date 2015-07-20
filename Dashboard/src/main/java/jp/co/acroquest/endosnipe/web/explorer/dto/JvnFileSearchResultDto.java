/*
 * Copyright (c) 2004-2015 Acroquest Technology Co., Ltd. All Rights Reserved.
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

/**
 * JVNファイルの検索結果DTO
 * @author kawasaki
 *
 */
public class JvnFileSearchResultDto
{
    /**
     * ログID
     */
    private Long logId_;

    /**
     * ログファイル名
     */
    private String logFileName_;

    /**
     * 開始時刻
     */
    private String startTime_;

    /**
     * 終了時刻
     */
    private String endTime_;

    /**
     * 呼び出し先クラス
     */
    private String calleeClass_;

    /**
     * 呼び出し先メソッド
     */
    private String calleeName_;

    /**
     * 処理時間
     */
    private long elapsedTime_;

    /**
     * スレッド名
     */
    private String threadName_;

    public Long getLogId()
    {
        return logId_;
    }

    public void setLogId(final Long logId)
    {
        logId_ = logId;
    }

    public String getLogFileName()
    {
        return logFileName_;
    }

    public void setLogFileName(final String logFileName)
    {
        logFileName_ = logFileName;
    }

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

    public String getCalleeClass()
    {
        return calleeClass_;
    }

    public void setCalleeClass(final String calleeClass)
    {
        calleeClass_ = calleeClass;
    }

    public String getCalleeName()
    {
        return calleeName_;
    }

    public void setCalleeName(final String calleeName)
    {
        calleeName_ = calleeName;
    }

    public long getElapsedTime()
    {
        return elapsedTime_;
    }

    public void setElapsedTime(final long elapsedTime)
    {
        elapsedTime_ = elapsedTime;
    }

    public String getThreadName()
    {
        return threadName_;
    }

    public void setThreadName(final String threadName)
    {
        threadName_ = threadName;
    }
}
