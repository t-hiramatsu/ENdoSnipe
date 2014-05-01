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
package jp.co.acroquest.endosnipe.collector.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.collector.processor.AlarmData;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * シグナルの状態を保持するクラス
 * @author fujii
 *
 */
public class SignalStateManager
{
    /** SignalStateManagerインスタンス */
    public static SignalStateManager instance__ = new SignalStateManager();

    /** アラームを出すかどうか判定するために保持し続けるデータ */
    private final Map<Long, AlarmData> alarmDataMap_ = new ConcurrentHashMap<Long, AlarmData>();

    /** シグナル定義を保持するマップ */
    private Map<Long, SignalDefinitionDto> signalDefinitionMap_ = null;

    /**
     * インスタンス化を阻止するprivateコンストラクタです。
     */
    private SignalStateManager()
    {
        // Do Nothing.
    }

    /**
     * {@link SignalStateManager}インスタンスを取得する。
     * @return {@link SignalStateManager}インスタンス
     */
    public static SignalStateManager getInstance()
    {
        return instance__;
    }

    /**
     * 閾値判定定義情報を取得する。
     * @param signalId 閾値判定定義ID
     * @return signalIdに一致する閾値情報
     */
    public AlarmData getAlarmData(final long signalId)
    {
        return this.alarmDataMap_.get(signalId);
    }

    /**
     * 閾値判定定義情報を登録する。
     * @param signalId 閾値判定定義ID
     * @param alarmData 閾値情報
     */
    public void addAlarmData(final long signalId, final AlarmData alarmData)
    {
        this.alarmDataMap_.put(signalId, alarmData);
    }

    /**
     * 閾値情報を削除する。
     * @param signalId 閾値判定定義ID
     */
    public void removeAlarmData(final long signalId)
    {
        this.alarmDataMap_.remove(signalId);
    }

    /**
     * シグナル定義情報のマップを返却する。
     * @return シグナル定義情報のマップ
     */
    public Map<Long, SignalDefinitionDto> getSignalDeifinitionMap()
    {
        return this.signalDefinitionMap_;
    }

    /**
     * シグナル定義情報のマップを設定する。
     * @param signalDefinitionMap シグナル定義情報のマップ
     */
    public void setSignalDeifinitionMap(final Map<Long, SignalDefinitionDto> signalDefinitionMap)
    {
        this.signalDefinitionMap_ = signalDefinitionMap;
    }

    /**
     * シグナル定義情報を追加する。
     * @param signalId シグナルID
     * @param signalDefinitionDto シグナル定義情報
     * 
     */
    public void addSignalDefinition(final Long signalId,
        final SignalDefinitionDto signalDefinitionDto)
    {
        if (this.signalDefinitionMap_ == null)
        {
            this.signalDefinitionMap_ = new ConcurrentHashMap<Long, SignalDefinitionDto>();
        }
        this.signalDefinitionMap_.put(signalId, signalDefinitionDto);
    }

    /**
     * シグナル定義情報を削除する。
     * @param signalId シグナルID
     * 
     * @return 削除したシグナル定義情報
     * 
     */
    public SignalDefinitionDto removeSignalDefinition(final Long signalId)
    {
        if (this.signalDefinitionMap_ == null)
        {
            return null;
        }
        return this.signalDefinitionMap_.remove(signalId);
    }

    /**
     * シグナル名を指定してシグナルIDを取得する
     * @param name シグナル名
     * @return シグナルID
     */
    public Long getSignalId(final String name)
    {
        for (Entry<Long, SignalDefinitionDto> entry : signalDefinitionMap_.entrySet())
        {
            SignalDefinitionDto dto = entry.getValue();
            if (!dto.getSignalName().equals(name))
            {
                continue;
            }
            return entry.getKey();
        }
        return null;
    }

}
