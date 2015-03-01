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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel
{

    // リスナのリスト
    private final PropertyChangeSupport listeners_ = new PropertyChangeSupport(this);

    // リスナの追加
    public void addPropertyChangeListener(final PropertyChangeListener listener)
    {
        this.listeners_.addPropertyChangeListener(listener);
    }

    // モデルの変更を通知
    public void firePropertyChange(final String propName, final Object oldValue,
            final Object newValue)
    {
        this.listeners_.firePropertyChange(propName, oldValue, newValue);
    }

    // リスナの削除
    public void removePropertyChangeListener(final PropertyChangeListener listener)
    {
        this.listeners_.removePropertyChangeListener(listener);
    }

    public Object getEditableValue()
    {
        // 編集可能な値として自身を返す
        return this;
    }

    public Object getPropertyValue(final Object id)
    {
        return null;
    }

    public boolean isPropertySet(final Object id)
    {
        return false;
    }

    public void resetPropertyValue(final Object id)
    {
        // Do nothing.
    }

    public void setPropertyValue(final Object id, final Object value)
    {
        // Do nothing.
    }
}