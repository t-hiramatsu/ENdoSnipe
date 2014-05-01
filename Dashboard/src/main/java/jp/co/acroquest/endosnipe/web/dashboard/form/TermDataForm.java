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
package jp.co.acroquest.endosnipe.web.dashboard.form;

import java.util.List;

/**
 * 期間データ取得用のメソッド。
 * @author nakagawa
 */
public class TermDataForm
{
    /**
     * 開始時間
     */
    private String startTime_;

    /**
     * 終了時間
     */
    private String endTime_;

    /**
     * データグループIDのリスト
     */
    private List<String> dataGroupIdList_;

    /**
     * コンストラクタ。
     */
    public TermDataForm()
    {

    }

    /**
     * 開始時間を取得する。
     * @return 開始時間
     */
    public String getStartTime()
    {
        return startTime_;
    }

    /**
     * 開始時間を設定する。
     * @param startTime 開始時間
     */
    public void setStartTime(final String startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * 終了時間を取得する。
     * @return 終了時間
     */
    public String getEndTime()
    {
        return endTime_;
    }

    /**
     * 終了時間を設定する。
     * @param endTime 終了時間
     */
    public void setEndTime(final String endTime)
    {
        this.endTime_ = endTime;
    }

    /**
     * データグループIDのリストを取得する。
     * @return データグループIDのリスト
     */
    public List<String> getDataGroupIdList()
    {
        return dataGroupIdList_;
    }

    /**
     * データグループIDのリストを設定する。
     * @param dataGroupIdList データグループIDのリスト
     */
    public void setDataGroupIdList(final List<String> dataGroupIdList)
    {
        this.dataGroupIdList_ = dataGroupIdList;
    }
}
