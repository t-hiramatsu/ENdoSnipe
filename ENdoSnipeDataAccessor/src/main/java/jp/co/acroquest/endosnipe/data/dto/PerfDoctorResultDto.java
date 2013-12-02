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

import java.sql.Timestamp;

/**
 * PerfDoctor診断結果用のDtoクラス。
 * 
 * @author hiramatsu
 *
 */
public class PerfDoctorResultDto
{
    /** ログID */
    private long logId_;

    /** 発生時刻 */
    private Timestamp occurrenceTime_;

    /** 概要 */
    private String description_;

    /** レベル */
    private String level_;

    /** クラス名 */
    private String className_;

    /** メソッド名 */
    private String methodName_;

    /** ログファイル名 */
    private String logFileName_;

    /** 計測項目名 */
    private String measurementItemName_;

    /** 閾値超過・回復時の概要フォーマット */
    private static final String DESCRIPTION_FORMAT =
         "Measurement value {0} the threshold." +
         "(Threshold:{1}, DetectedValue:{2}, SignalLevel:{3})";

    /**
     * ログIDを取得する。
     * 
     * @return ログID
     */
    public long getLogId()
    {
        return logId_;
    }

    /**
     * ログIDを設定する。
     * 
     * @param logId ログID
     */
    public void setLogId(long logId)
    {
        logId_ = logId;
    }

    /**
     * 発生時刻を取得する。
     * 
     * @return 発生時刻
     */
    public Timestamp getOccurrenceTime()
    {
        return occurrenceTime_;
    }

    /**
     * 発生時刻を設定する。
     * 
     * @param occurrenceTime 発生時刻
     */
    public void setOccurrenceTime(Timestamp occurrenceTime)
    {
        occurrenceTime_ = occurrenceTime;
    }

    /**
     * 概要を取得する。
     * 
     * @return 概要
     */
    public String getDescription()
    {
        return description_;
    }

    /**
     * 概要を設定する。
     * 
     * @param description 概要
     */
    public void setDescription(String description)
    {
        description_ = description;
    }

    /**
     * レベルを取得する。
     * 
     * @return レベル
     */
    public String getLevel()
    {
        return level_;
    }

    /**
     * レベルを設定する。
     * 
     * @param level レベル
     */
    public void setLevel(String level)
    {
        level_ = level;
    }

    /**
     * 計測項目名を取得する。
     * 
     * @return 計測項目名
     */
    public String getMeasurementItemName()
    {
        return measurementItemName_;
    }

    /**
     * 計測項目名を設定する。
     * 
     * @param measurementItemName 計測項目名
     */
    public void setMeasurementItemName(String measurementItemName)
    {
        measurementItemName_ = measurementItemName;
    }

    /**
     * クラス名を取得する。
     * 
     * @return クラス名
     */
    public String getClassName()
    {
        return className_;
    }

    /**
     * クラス名を設定する。
     * 
     * @param className クラス名
     */
    public void setClassName(String className)
    {
        className_ = className;
    }

    /**
     * メソッド名を取得する。
     * 
     * @return メソッド名
     */
    public String getMethodName()
    {
        return methodName_;
    }

    /**
     * メソッド名を設定する。
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(String methodName)
    {
        methodName_ = methodName;
    }
    
    /**
     * ログファイル名を取得する。
     * 
     * @return ログファイル名
     */
    public String getLogFileName()
    {
        return logFileName_;
    }

    /**
     * ログファイル名を設定する。
     * 
     * @param logFileName ログファイル名
     */
    public void setLogFileName(String logFileName)
    {
        logFileName_ = logFileName;
    }
}
