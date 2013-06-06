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
package jp.co.acroquest.endosnipe.javelin.util.concurrent;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * CopyOnWriteArraySetクラス
 * @author acroquest
 *
 * @param <E> element
 */
public class CopyOnWriteArraySet<E> extends AbstractSet<E> implements java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long serialVersionUID = 8560577487834038908L;
    
    /** copyOnWriteArrayList */
    private final CopyOnWriteArrayList<E> copyOnWriteArrayList_;

    /**
     * コンストラクタ
     */
    public CopyOnWriteArraySet()
    {
        copyOnWriteArrayList_ = new CopyOnWriteArrayList<E>();
    }

    /**
     * コンストラクタ
     * @param c コレクション
     */
    public CopyOnWriteArraySet(Collection<? extends E> c)
    {
        copyOnWriteArrayList_ = new CopyOnWriteArrayList<E>();
        copyOnWriteArrayList_.addAllAbsent(c);
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return copyOnWriteArrayList_.size();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return copyOnWriteArrayList_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o)
    {
        return copyOnWriteArrayList_.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray()
    {
        return copyOnWriteArrayList_.toArray();
    }

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a)
    {
        return copyOnWriteArrayList_.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        copyOnWriteArrayList_.clear();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator()
    {
        return copyOnWriteArrayList_.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o)
    {
        return copyOnWriteArrayList_.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E o)
    {
        return copyOnWriteArrayList_.addIfAbsent(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c)
    {
        return copyOnWriteArrayList_.containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends E> c)
    {
        return copyOnWriteArrayList_.addAllAbsent(c) > 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(Collection<?> c)
    {
        return copyOnWriteArrayList_.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean retainAll(Collection<?> c)
    {
        return copyOnWriteArrayList_.retainAll(c);
    }

}
