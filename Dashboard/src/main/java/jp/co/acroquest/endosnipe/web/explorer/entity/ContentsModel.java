/*******************************************************************************
 * ENdoSnipe 5.3 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.acroquest.endosnipe.common.event.EventConstants;

/**
 * StatsVisionのルートコンテンツモデル。
 * 
 * @author yamasaki
 */
public class ContentsModel extends AbstractModel implements Iterable<ComponentModel>
{

    /** 変更の種類を識別するための文字列。 */
    public static final String P_CONTENTS_NAME = "_contents_name";

    /** 子モデルの変更を識別するための文字列。 */
    public static final String P_CHILDREN = "_children";

    // 子モデルのリスト
    private final List<ComponentModel> children_ = new ArrayList<ComponentModel>();

    // モデルの名前
    private String contentsName_;

    /**
     * コンストラクタ。
     */
    public ContentsModel()
    {
        super();
    }

    /**
     * モデルの名前を取得する。
     * 
     * @return モデルの名前。
     */
    public String getContentsName()
    {
        return this.contentsName_;
    }

    /**
     * モデルの名前を設定する。
     * 
     * @param contentsName モデルの名前。
     */
    public void setContentsName(final String contentsName)
    {
        this.contentsName_ = contentsName;
        firePropertyChange(P_CONTENTS_NAME, null, this.contentsName_);
    }

    /**
     * 子モデルを追加する。
     * 
     * @param newChild 追加する子モデル。
     */
    public void addChild(final ComponentModel newChild)
    {
        addChild(newChild, true);
    }

    /**
     * 子モデルを追加する。
     * 
     * @param newChild 追加する子モデル。
     * @param firePropertyChange 変更を通知するかどうか。
     */
    public void addChild(final ComponentModel newChild, final boolean firePropertyChange)
    {
        if (EventConstants.EVENT_CLASSNAME.equals(newChild.getComponentName()) == false)
        {
            this.children_.add(newChild);
            if (firePropertyChange)
            {
                childrenChanged();
            }
        }
    }

    /**
     * 子モデルに変更があった場合に通知する。
     */
    public void childrenChanged()
    {
        firePropertyChange(P_CHILDREN, null, this.children_);
    }

    /**
     * 子モデルを削除する。
     *
     * @param child 削除する子モデル。
     */
    public void removeChild(final ComponentModel child)
    {
        this.children_.remove(child);
        childrenChanged();
    }

    /**
     * 子モデルをすべて削除します。<br />
     */
    public void removeAllChildren()
    {
        this.children_.clear();
        childrenChanged();
    }

    /**
     * 子モデルのリストを取得する。
     * 
     * @return 子モデルのリスト。
     */
    public List<ComponentModel> getChildren()
    {
        return this.children_;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<ComponentModel> iterator()
    {
        return new ComponentModelIterator(this.children_.iterator());
    }

    /**
     * 指定された名前のコンポーネントを返します。<br />
     *
     * @param componentName 取得するコンポーネント名
     * @return 存在した場合ばコンポーネントのインスタンス、存在しない場合は <code>null</code>
     */
    public ComponentModel getChild(final String componentName)
    {
        for (ComponentModel componentModel : this.children_)
        {
            if (componentModel.getComponentName().equals(componentName))
            {
                return componentModel;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getContentsName() + getChildren().toString();
    }

    /**
     * remove() 時にリストが変更されたことを通知するイテレータです。<br />
     *
     * @author sakamoto
     */
    private class ComponentModelIterator implements Iterator<ComponentModel>
    {
        private final Iterator<ComponentModel> iterator_;

        public ComponentModelIterator(final Iterator<ComponentModel> iterator)
        {
            this.iterator_ = iterator;
        }

        public boolean hasNext()
        {
            return this.iterator_.hasNext();
        }

        public ComponentModel next()
        {
            return this.iterator_.next();
        }

        public void remove()
        {
            this.iterator_.remove();
            firePropertyChange(P_CHILDREN, null, ContentsModel.this.children_);
        }

    }

}
