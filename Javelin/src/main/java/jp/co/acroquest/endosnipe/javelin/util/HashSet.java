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
package jp.co.acroquest.endosnipe.javelin.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * HashSetクラス
 * @author acroquest
 *
 * @param <E> element
 */
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long serialVersionUID = 7481282919838399045L;

    /** マップ */
    private transient HashMap<E, Object> map_;

    /** 現在のオブジェクト　*/
    private static final Object PRESENT = new Object();

    /** デフォルトのハッシュマップの初期容量 */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** デフォルトのハッシュマップの負荷係数  */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * コンストラクタ
     */
    public HashSet()
    {
        map_ = new HashMap<E, Object>();
    }

    /**
     * コンストラクタ
     * @param collection コレクション 
     */
    public HashSet(Collection<? extends E> collection)
    {
        int capacity = (int)(collection.size() / DEFAULT_LOAD_FACTOR) + 1;
        int initCapacity = Math.max(capacity, DEFAULT_INITIAL_CAPACITY);
        map_ = new HashMap<E, Object>(initCapacity);
        addAll(collection);
    }

    /**
     * コンストラクタ
     * @param initialCapacity ハッシュマップの初期容量
     * @param loadFactor ハッシュマップの負荷係数 
     */
    public HashSet(int initialCapacity, float loadFactor)
    {
        map_ = new HashMap<E, Object>(initialCapacity, loadFactor);
    }

    /**
     * コンストラクタ
     * @param initialCapacity ハッシュマップの初期容量
     */
    public HashSet(int initialCapacity)
    {
        map_ = new HashMap<E, Object>(initialCapacity);
    }

    /**
     * コンストラクタ
     * @param initialCapacity ハッシュマップの初期容量
     * @param loadFactor ハッシュマップの負荷係数
     * @param dummy ダミー
     */
    public HashSet(int initialCapacity, float loadFactor, boolean dummy)
    {
        map_ = new LinkedHashMap<E, Object>(initialCapacity, loadFactor);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator()
    {
        return map_.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return map_.size();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return map_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o)
    {
        return map_.containsKey(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E o)
    {
        return map_.put(o, PRESENT) == null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o)
    {
        return map_.remove(o) == PRESENT;
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        map_.clear();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        try
        {
            HashSet<E> newSet = (HashSet<E>)super.clone();
            newSet.map_ = (HashMap<E, Object>)map_.clone();
            return newSet;
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError();
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {
        s.defaultWriteObject();

        s.writeInt(map_.getLength());
        s.writeFloat(map_.loadFactor());

        s.writeInt(map_.size());

        for (Iterator<E> i = map_.keySet().iterator(); i.hasNext();)
        {
            s.writeObject(i.next());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {
        s.defaultReadObject();

        int capacity = s.readInt();
        float loadFactor = s.readFloat();
        map_ = (((HashSet<?>)this) instanceof LinkedHashSet ? new LinkedHashMap<E, Object>(capacity,
                                                                                         loadFactor)
                        : new HashMap<E, Object>(capacity, loadFactor));

        int size = s.readInt();

        for (int i = 0; i < size; i++)
        {
            E e = (E)s.readObject();
            map_.put(e, PRESENT);
        }
    }
}
