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

import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.RandomAccess;

/**
 * ArrayListクラス
 * @author acroquest
 *
 * @param <E> 要素
 */
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable,
        java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long serialVersionUID       = 8960495295892130433L;

    /** デフォルトのArrayListのサイズ */
    private static final int  DEFAULT_ARRAYLIST_SIZE = 10;

    /** 容量計算用数値 */
    private static final long CAPACITY_MULTIPLE      = 110L;

    /** 容量計算用数値 */
    private static final int  CAPACITY_DIVISION      = 100;

    /** 3倍を示す数 */
    private static final int  THREE_TIMES            = 3;

    /** 要素のデータ */
    private transient E[]     elementData_;

    /** サイズ */
    private int               size_;

    /**
     * コンストラクタ
     * @param initialCapacity 初期容量
     */
    @SuppressWarnings("unchecked")
    public ArrayList(int initialCapacity)
    {
        super();
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elementData_ = (E[])new Object[initialCapacity];
    }

    /**
     * コンストラクタ
     */
    public ArrayList()
    {
        this(DEFAULT_ARRAYLIST_SIZE);
    }

    /**
     * コンストラクタ
     * @param c コレクション
     */
    @SuppressWarnings("unchecked")
    public ArrayList(Collection<? extends E> c)
    {
        size_ = c.size();

        int capacity =
                       (int)Math.min((size_ * CAPACITY_MULTIPLE) / CAPACITY_DIVISION,
                                     Integer.MAX_VALUE);
        elementData_ = (E[])c.toArray(new Object[capacity]);
    }

    /**
     * 配列をサイズにトリミングします。
     */
    @SuppressWarnings("unchecked")
    public void trimToSize()
    {
        modCount++;
        int oldCapacity = elementData_.length;
        if (size_ < oldCapacity)
        {
            Object[] oldData = elementData_;
            elementData_ = (E[])new Object[size_];
            System.arraycopy(oldData, 0, elementData_, 0, size_);
        }
    }

    /**
     * 配列の容量を確定します。 
     * @param minCapacity 最小容量
     */
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity)
    {
        modCount++;
        int oldCapacity = elementData_.length;
        if (minCapacity > oldCapacity)
        {
            Object[] oldData = elementData_;
            int newCapacity = (oldCapacity * THREE_TIMES) / 2 + 1;
            if (newCapacity < minCapacity)
            {
                newCapacity = minCapacity;
            }
            elementData_ = (E[])new Object[newCapacity];
            System.arraycopy(oldData, 0, elementData_, 0, size_);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return size_;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return size_ == 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object elem)
    {
        return indexOf(elem) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(Object elem)
    {
        if (elem == null)
        {
            for (int i = 0; i < size_; i++)
            {
                if (elementData_[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < size_; i++)
            {
                if (elem.equals(elementData_[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(Object elem)
    {
        if (elem == null)
        {
            for (int i = size_ - 1; i >= 0; i--)
            {
                if (elementData_[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = size_ - 1; i >= 0; i--)
            {
                if (elem.equals(elementData_[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        try
        {
            ArrayList<E> v = (ArrayList<E>)super.clone();
            v.elementData_ = (E[])new Object[size_];
            System.arraycopy(elementData_, 0, v.elementData_, 0, size_);
            v.modCount = 0;
            return v;
        }
        catch (CloneNotSupportedException e)
        {

            throw new InternalError();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray()
    {
        Object[] result = new Object[size_];
        System.arraycopy(elementData_, 0, result, 0, size_);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a)
    {
        if (a.length < size_)
        {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size_);
        }
        System.arraycopy(elementData_, 0, a, 0, size_);
        if (a.length > size_)
        {
            a[size_] = null;
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    public E get(int index)
    {
        checkRange(index);

        return elementData_[index];
    }

    /**
     * {@inheritDoc}
     */
    public E set(int index, E element)
    {
        checkRange(index);

        E oldValue = elementData_[index];
        elementData_[index] = element;
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E o)
    {
        ensureCapacity(size_ + 1);
        elementData_[size_++] = o;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void add(int index, E element)
    {
        if (index > size_ || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
        }

        ensureCapacity(size_ + 1);
        System.arraycopy(elementData_, index, elementData_, index + 1, size_ - index);
        elementData_[index] = element;
        size_++;
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index)
    {
        checkRange(index);

        modCount++;
        E oldValue = elementData_[index];

        int numMoved = size_ - index - 1;
        if (numMoved > 0)
        {
            System.arraycopy(elementData_, index + 1, elementData_, index, numMoved);
        }
        elementData_[--size_] = null;

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o)
    {
        if (o == null)
        {
            for (int index = 0; index < size_; index++)
            {
                if (elementData_[index] == null)
                {
                    fastRemove(index);
                    return true;
                }
            }
        }
        else
        {
            for (int index = 0; index < size_; index++)
            {
                if (o.equals(elementData_[index]))
                {
                    fastRemove(index);
                    return true;
                }
            }
        }
        return false;
    }

    private void fastRemove(int index)
    {
        modCount++;
        int numMoved = size_ - index - 1;
        if (numMoved > 0)
        {
            System.arraycopy(elementData_, index + 1, elementData_, index, numMoved);
        }
        elementData_[--size_] = null;
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        modCount++;

        for (int i = 0; i < size_; i++)
        {
            elementData_[i] = null;
        }

        size_ = 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends E> c)
    {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size_ + numNew);
        System.arraycopy(a, 0, elementData_, size_, numNew);
        size_ += numNew;
        return numNew != 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(int index, Collection<? extends E> c)
    {
        if (index > size_ || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
        }

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size_ + numNew);

        int numMoved = size_ - index;
        if (numMoved > 0)
        {
            System.arraycopy(elementData_, index, elementData_, index + numNew, numMoved);
        }

        System.arraycopy(a, 0, elementData_, index, numNew);
        size_ += numNew;
        return numNew != 0;
    }

    /**
     * {@inheritDoc}
     */
    protected void removeRange(int fromIndex, int toIndex)
    {
        modCount++;
        int numMoved = size_ - toIndex;
        System.arraycopy(elementData_, toIndex, elementData_, fromIndex, numMoved);

        int newSize = size_ - (toIndex - fromIndex);
        while (size_ != newSize)
        {
            elementData_[--size_] = null;
        }
    }

    private void checkRange(int index)
    {
        if (index >= size_)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {
        int expectedModCount = modCount;
        s.defaultWriteObject();

        s.writeInt(elementData_.length);

        for (int i = 0; i < size_; i++)
        {
            s.writeObject(elementData_[i]);
        }

        if (modCount != expectedModCount)
        {
            throw new ConcurrentModificationException();
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {
        s.defaultReadObject();

        int arrayLength = s.readInt();
        elementData_ = (E[])new Object[arrayLength];
        Object[] a = elementData_;

        for (int i = 0; i < size_; i++)
        {
            a[i] = s.readObject();
        }
    }
}
