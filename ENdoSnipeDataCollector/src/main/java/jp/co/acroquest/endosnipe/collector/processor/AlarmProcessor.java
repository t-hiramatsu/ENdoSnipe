/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.processor;

import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * 閾値超過のアラーム発生を判定する処理のインタフェース
 * @author ochiai
 *
 */
public interface AlarmProcessor
{
    /** パーセントを計算するときの定数（100） */
    int PERCENT_CONST = 100;

    /**
     * アラームレベルを計算する
     * @param currentResourceData 現在のリソースデータ
     * @param prevResourceData 一つ前のリソースデータ
     * @param signalDefinition 閾値判定定義情報
     * @param alarmData アラーム通知の有無を計算するためのデータ。このメソッドの中で更新する
     * @return アラーム通知の有無と、通知するアラームに表示させる情報を入れたAlarmEntry
     */
    AlarmEntry calculateAlarmLevel(ResourceData currentResourceData, ResourceData prevResourceData,
            SignalDefinitionDto signalDefinition, AlarmData alarmData);
}
