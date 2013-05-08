/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.web.dashboard.entity;

import java.util.Date;

import net.arnx.jsonic.JSONHint;

/**
 * アラーム通知オブジェクト
 * @author eriguchi
 *
 */
public class AlarmNotifyEntity
{
    /** イベントID */
    public long eventId_;

    /** 通知要求エージェントID */
    public int agentId_;

    /** 発生時刻 */
    @JSONHint(format = "yyyy/MM/dd HH:mm:ss.SSS")
    public Date timestamp_;

    /** 発生個所：クラス */
    public String className_;

    /** 発生個所：メソッド */
    public String methodName_;

    /** アラームレベル */
    public String level_;

    /** アラーム内容詳細 */
    public String description_;

    /** jvnファイル名 */
    public String fileName_;

    /** jvnファイル内の開始行 */
    public int logFileLineNumber_;

    /** jvnファイル内の対応するEventInfo文字列 */
    public String javelinLog_;

    /** アラームのルールID */
    public String ruleId_;

    /**
     * コンストラクタ。
     */
    public AlarmNotifyEntity()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "AlarmNotifyEntity [event_id=" + eventId_ + ", agent_id=" + agentId_
                + ", timestamp=" + timestamp_ + ", class_name=" + className_ + ", method_name="
                + methodName_ + ", level=" + level_ + ", description=" + description_
                + ", file_name=" + fileName_ + ", log_file_line_number=" + logFileLineNumber_
                + ", rule_id=" + ruleId_ + "]";
    }
}
