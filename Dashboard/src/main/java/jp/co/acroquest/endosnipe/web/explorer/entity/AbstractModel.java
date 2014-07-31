/*
 * Copyright (c) 2004-2008 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
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