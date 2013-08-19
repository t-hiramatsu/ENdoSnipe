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
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.data.dto.MultipleResourceGraphDefinitionDto;

/**
 * シグナルの状態を保持するクラス
 * @author pin
 *
 */
public class MultipleResourceGraphStateManager
{
    /** MultipleResourceGraphStateManagerインスタンス */
    public static MultipleResourceGraphStateManager instance__ =
        new MultipleResourceGraphStateManager();

    /** シグナル定義を保持するマップ */
    private Map<Long, MultipleResourceGraphDefinitionDto> multipleResourceGraphDefinitionMap_ =
        null;

    /**
     * インスタンス化を阻止するprivateコンストラクタです。
     */
    private MultipleResourceGraphStateManager()
    {
        // Do Nothing.
    }

    /**
     * {@link MultipleResourceGraphStateManager}インスタンスを取得する。
     * @return {@link MultipleResourceGraphStateManager}インスタンス
     */
    public static MultipleResourceGraphStateManager getInstance()
    {
        return instance__;
    }

    /**
    * シグナル定義情報のマップを返却する。
    * @return シグナル定義情報のマップ
    */
    public Map<Long, MultipleResourceGraphDefinitionDto> getMultipleResourceGraphDefinition()
    {
        return this.multipleResourceGraphDefinitionMap_;
    }

    /**
     * シグナル定義情報のマップを設定する。
     * @param multipleResourceGraphDefinitionMap シグナル定義情報のマップ
     */
    public void setMultipleResourceGraphDefinitionMap(
        final Map<Long, MultipleResourceGraphDefinitionDto> multipleResourceGraphDefinitionMap)
    {
        this.multipleResourceGraphDefinitionMap_ = multipleResourceGraphDefinitionMap;
    }

    /**
     * シグナル定義情報を追加する。
     * @param multipleResourceGraphId シグナルID
     * @param multipleResourceGraphDefinitionDto シグナル定義情報
     * 
     */
    public void addMultipleResourceGraphDefinition(final Long multipleResourceGraphId,
        final MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto)
    {
        if (this.multipleResourceGraphDefinitionMap_ == null)
        {
            this.multipleResourceGraphDefinitionMap_ =
                new ConcurrentHashMap<Long, MultipleResourceGraphDefinitionDto>();
        }
        this.multipleResourceGraphDefinitionMap_.put(multipleResourceGraphId,
                                                     multipleResourceGraphDefinitionDto);
    }

    /**
     * シグナル定義情報を削除する。
     * @param multipleResourceGraphId シグナルID
     * 
     * @return 削除したシグナル定義情報
     * 
     */
    public MultipleResourceGraphDefinitionDto removeMultipleResourceGraphDefinition(
        final Long multipleResourceGraphId)
    {
        if (this.multipleResourceGraphDefinitionMap_ == null)
        {
            return null;
        }
        return this.multipleResourceGraphDefinitionMap_.remove(multipleResourceGraphId);
    }

}
