/*
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
package jp.co.acroquest.endosnipe.javelin.resource.jmx;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;

/**
 * 複数のMBeanValueGetterを管理するクラス。
 * @author tsukano
 */
public class MBeanMultiResourceGetter implements MultiResourceGetter
{
    /** MBeanValueGetter */
    private List<MBeanValueGetter> getterList_ = new ArrayList<MBeanValueGetter>(1);

    /**
     * 本クラスが管理する計測クラスを追加する。
     * @param getter 計測クラス
     */
    public void addMBeanValueGetter(MBeanValueGetter getter)
    {
        this.getterList_.add(getter);
    }

    /**
     * 各計測値を取得し、ResourceItemのリストとして返却する
     * @return ResourceItemのリスト
     */
    public List<ResourceItem> getValues()
    {
        // 各計測値を取得し、ResourceItemのリストとして返却する
        List<ResourceItem> resourceItemList = null;
        for (MBeanValueGetter getter : this.getterList_)
        {
            if (resourceItemList == null)
            {
                resourceItemList = new ArrayList<ResourceItem>(this.getterList_.size());
            }

            List<ResourceItem> itemList = getter.getValue();
            if (itemList != null && itemList.size() != 0)
            {
                resourceItemList.addAll(itemList);
            }
        }
        return resourceItemList;
    }

    /**
     * {@inheritDoc}
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_STRING;
    }
}
