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

/**
 * シグナル定義テーブルに対するエンティティクラスです。<br />
 * 
 * @author miyasaka
 * 
 */
public class SignalInfo
{
    /** シグナル定義テーブルのID。 */
    public long signalId;

    /** シグナル名。 */
    public String signalName;

    /** マッチングパターン。 */
    public String matchingPattern;

    /** 設定できる閾値の上限レベル。 */
    public int level;

    /** 各レベルの閾値。 */
    public String patternValue;

    /** エスカレーション期間。 */
    public double escalationPeriod;

    /**
     * {@link SignalInfo} オブジェクトを生成します。<br />
     */
    public SignalInfo()
    {
        this.signalId = -1;
    }

    @Override
    public String toString()
    {
        return String.format("SignalID%d SignalName:%s MatchingPattern:%s Level:%d "
                                     + "PatternValue:%s escalationPeriod:%f", signalId, signalName,
                             matchingPattern,
                             level, patternValue, escalationPeriod);
    }
}
