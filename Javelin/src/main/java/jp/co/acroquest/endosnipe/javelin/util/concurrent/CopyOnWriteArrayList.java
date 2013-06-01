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

import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * CopyOnWriteArrayListクラス
 * @author acroquest
 *
 * @param <E> element
 */
public class CopyOnWriteArrayList<E> implements List<E>, RandomAccess, Cloneable,
        java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long      serialVersionUID = -7056280058773009144L;

    /** ハッシュコード用整数 */
    private static final int       HASH_CODE_NUMBER = 31;

    /** 配列 */
    private transient volatile E[] array_;

    /**
     * コンストラクタ
     */
    @SuppressWarnings("unchecked")
    public CopyOnWriteArrayList()
    {
        array_ = (E[])new Object[0];
    }

    /**
     * コンストラクタ
     * @param c コレクション
     */
    @SuppressWarnings("unchecked")
    public CopyOnWriteArrayList(Collection<? extends E> c)
    {
        array_ = (E[])new Object[c.size()];
        Iterator<? extends E> i = c.iterator();
        int size = 0;
        while (i.hasNext())
        {
            array_[size++] = i.next();
        }
    }

    /**
     * コンストラクタ
     * @param toCopyIn コピーする配列
     */
    public CopyOnWriteArrayList(E[] toCopyIn)
    {
        copyIn(toCopyIn, 0, toCopyIn.length);
    }

    /**
     * 配列を取得します。
     * @return 配列
     */
    private E[] getArray()
    {
        return array_;
    }

    /**
     * 配列をコピーします。
     * @param toCopyIn コピー元配列
     * @param first コピー開始インデックス
     * @param length コピーの配列の長さ
     */
    @SuppressWarnings("unchecked")
    private synchronized void copyIn(E[] toCopyIn, int first, int length)
    {
        array_ = (E[])new Object[length];
        System.arraycopy(toCopyIn, first, array_, 0, length);
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return getArray().length;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object elem)
    {
        E[] elementData = getArray();
        int len = elementData.length;
        return indexOf(elem, elementData, len) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(Object elem)
    {
        E[] elementData = getArray();
        int len = elementData.length;
        return indexOf(elem, elementData, len);
    }

    private static int indexOf(Object elem, Object[] elementData, int len)
    {
        if (elem == null)
        {
            for (int i = 0; i < len; i++)
            {
                if (elementData[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < len; i++)
            {
                if (elem.equals(elementData[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 指定した要素のインデックスを取得します。
     * @param element 要素
     * @param index インデックス
     * @return 指定した要素のインデックス
     */
    public int indexOf(E element, int index)
    {
        E[] elementData = getArray();
        int elementCount = elementData.length;

        if (element == null)
        {
            for (int i = index; i < elementCount; i++)
            {
                if (elementData[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = index; i < elementCount; i++)
            {
                if (element.equals(elementData[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 指定した要素が格納されている最後のインデックスを取得する。
     * @param elem 要素
     * @return 指定した要素が格納されている最後のインデックス
     */
    public int lastIndexOf(Object elem)
    {
        E[] elementData = getArray();
        int len = elementData.length;
        return lastIndexOf(elem, elementData, len);
    }

    /**
     * 指定した要素が格納されている最後のインデックスを取得する。
     * @param elem 要素
     * @param elementData 要素の配列
     * @param len 配列の長さ
     * @return 指定した要素が格納されている最後のインデックス
     */
    private static int lastIndexOf(Object elem, Object[] elementData, int len)
    {
        if (elem == null)
        {
            for (int i = len - 1; i >= 0; i--)
            {
                if (elementData[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = len - 1; i >= 0; i--)
            {
                if (elem.equals(elementData[i]))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 指定した要素が格納されている指定したインデックス以前のインデックスを取得する。
     * @param elem 要素
     * @param index インデックス
     * @return 指定した要素が格納されている最後のインデックス
     */    
    public int lastIndexOf(E elem, int index)
    {

        E[] elementData = getArray();
        if (elem == null)
        {
            for (int i = index; i >= 0; i--)
            {
                if (elementData[i] == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = index; i >= 0; i--)
            {
                if (elem.equals(elementData[i]))
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
            E[] elementData = getArray();
            CopyOnWriteArrayList<E> v = (CopyOnWriteArrayList<E>)super.clone();
            v.array_ = (E[])new Object[elementData.length];
            System.arraycopy(elementData, 0, v.array_, 0, elementData.length);
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
        Object[] elementData = getArray();
        Object[] result = new Object[elementData.length];
        System.arraycopy(elementData, 0, result, 0, elementData.length);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array)
    {
        E[] elementData = getArray();

        if (array.length < elementData.length)
        {
            array =
                    (T[])java.lang.reflect.Array.newInstance(array.getClass().getComponentType(),
                                                             elementData.length);
        }

        System.arraycopy(elementData, 0, array, 0, elementData.length);

        if (array.length > elementData.length)
        {
            array[elementData.length] = null;
        }

        return array;
    }

    /**
     * {@inheritDoc}
     */
    public E get(int index)
    {
        E[] elementData = getArray();
        rangeCheck(index, elementData.length);
        return elementData[index];
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized E set(int index, E element)
    {
        int len = array_.length;
        rangeCheck(index, len);
        E oldValue = array_[index];

        boolean same = (oldValue == element || (element != null && element.equals(oldValue)));
        if (!same)
        {
            E[] newArray = (E[])new Object[len];
            System.arraycopy(array_, 0, newArray, 0, len);
            newArray[index] = element;
            array_ = newArray;
        }
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean add(E element)
    {
        int len = array_.length;
        E[] newArray = (E[])new Object[len + 1];
        System.arraycopy(array_, 0, newArray, 0, len);
        newArray[len] = element;
        array_ = newArray;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized void add(int index, E element)
    {
        int len = array_.length;
        if (index > len || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + len);
        }

        E[] newArray = (E[])new Object[len + 1];
        System.arraycopy(array_, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array_, index, newArray, index + 1, len - index);
        array_ = newArray;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized E remove(int index)
    {
        int len = array_.length;
        rangeCheck(index, len);
        E oldValue = array_[index];
        E[] newArray = (E[])new Object[len - 1];
        System.arraycopy(array_, 0, newArray, 0, index);
        int numMoved = len - index - 1;
        if (numMoved > 0)
        {
            System.arraycopy(array_, index + 1, newArray, index, numMoved);
        }
        array_ = newArray;
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean remove(Object o)
    {
        int len = array_.length;
        if (len == 0)
        {
            return false;
        }

        int newlen = len - 1;
        E[] newArray = (E[])new Object[newlen];

        for (int i = 0; i < newlen; ++i)
        {
            if (o == array_[i] || (o != null && o.equals(array_[i])))
            {

                for (int k = i + 1; k < len; ++k)
                {
                    newArray[k - 1] = array_[k];
                }
                array_ = newArray;
                return true;
            }
            else
            {
                newArray[i] = array_[i];
            }
        }

        if (o == array_[newlen] || (o != null && o.equals(array_[newlen])))
        {
            array_ = newArray;
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void removeRange(int fromIndex, int toIndex)
    {
        int len = array_.length;

        if (fromIndex < 0 || fromIndex >= len || toIndex > len || toIndex < fromIndex)
        {
            throw new IndexOutOfBoundsException();
        }

        int numMoved = len - toIndex;
        int newlen = len - (toIndex - fromIndex);
        E[] newArray = (E[])new Object[newlen];
        System.arraycopy(array_, 0, newArray, 0, fromIndex);
        System.arraycopy(array_, toIndex, newArray, fromIndex, numMoved);
        array_ = newArray;
    }

    /**
     * 指定した要素が配列にない場合、配列の最後にその要素を追加する。
     * @param element 要素
     * @return 要素を追加した時true/そうでないときfalse
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean addIfAbsent(E element)
    {
        int len = array_.length;
        E[] newArray = (E[])new Object[len + 1];
        for (int i = 0; i < len; ++i)
        {
            if (element == array_[i] || (element != null && element.equals(array_[i])))
            {
                return false;
            }
            else
            {
                newArray[i] = array_[i];
            }
        }
        newArray[len] = element;
        array_ = newArray;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c)
    {
        E[] elementData = getArray();
        int len = elementData.length;
        Iterator<?> e = c.iterator();
        while (e.hasNext())
        {
            if (indexOf(e.next(), elementData, len) < 0)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean removeAll(Collection<?> c)
    {
        E[] elementData = array_;
        int len = elementData.length;
        if (len == 0)
        {
            return false;
        }

        E[] temp = (E[])new Object[len];
        int newlen = 0;
        for (int i = 0; i < len; ++i)
        {
            E element = elementData[i];
            if (!c.contains(element))
            {
                temp[newlen++] = element;
            }
        }

        if (newlen == len)
        {
            return false;
        }

        E[] newArray = (E[])new Object[newlen];
        System.arraycopy(temp, 0, newArray, 0, newlen);
        array_ = newArray;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean retainAll(Collection<?> c)
    {
        E[] elementData = array_;
        int len = elementData.length;
        if (len == 0)
        {
            return false;
        }

        E[] temp = (E[])new Object[len];
        int newlen = 0;
        for (int i = 0; i < len; ++i)
        {
            E element = elementData[i];
            if (c.contains(element))
            {
                temp[newlen++] = element;
            }
        }

        if (newlen == len)
        {
            return false;
        }

        E[] newArray = (E[])new Object[newlen];
        System.arraycopy(temp, 0, newArray, 0, newlen);
        array_ = newArray;
        return true;
    }

    /**
     * 指定したコレクションの全要素が配列に含まれない場合に、それらを全て配列に追加する。
     * @param c コレクション
     * @return 追加した要素の数
     */
    @SuppressWarnings("unchecked")
    public synchronized int addAllAbsent(Collection<? extends E> c)
    {
        int numNew = c.size();
        if (numNew == 0)
        {
            return 0;
        }

        E[] elementData = array_;
        int len = elementData.length;

        E[] temp = (E[])new Object[numNew];
        int added = 0;
        Iterator<? extends E> iterator = c.iterator();
        while (iterator.hasNext())
        {
            E element = iterator.next();
            if (indexOf(element, elementData, len) < 0)
            {
                if (indexOf(element, temp, added) < 0)
                {
                    temp[added++] = element;
                }
            }
        }

        if (added == 0)
        {
            return 0;
        }

        E[] newArray = (E[])new Object[len + added];
        System.arraycopy(elementData, 0, newArray, 0, len);
        System.arraycopy(temp, 0, newArray, len, added);
        array_ = newArray;
        return added;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized void clear()
    {
        array_ = (E[])new Object[0];
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean addAll(Collection<? extends E> c)
    {
        int numNew = c.size();
        if (numNew == 0)
        {
            return false;
        }

        int len = array_.length;
        E[] newArray = (E[])new Object[len + numNew];
        System.arraycopy(array_, 0, newArray, 0, len);
        Iterator<? extends E> e = c.iterator();
        for (int i = 0; i < numNew; i++)
        {
            newArray[len++] = e.next();
        }
        array_ = newArray;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean addAll(int index, Collection<? extends E> c)
    {
        int len = array_.length;
        if (index > len || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + len);
        }

        int numNew = c.size();
        if (numNew == 0)
        {
            return false;
        }

        E[] newArray = (E[])new Object[len + numNew];
        System.arraycopy(array_, 0, newArray, 0, len);
        int numMoved = len - index;
        if (numMoved > 0)
        {
            System.arraycopy(array_, index, newArray, index + numNew, numMoved);
        }
        Iterator<? extends E> e = c.iterator();
        for (int i = 0; i < numNew; i++)
        {
            newArray[index++] = e.next();
        }
        array_ = newArray;

        return true;
    }

    private void rangeCheck(int index, int length)
    {
        if (index >= length || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {

        s.defaultWriteObject();

        E[] elementData = getArray();

        s.writeInt(elementData.length);

        for (int i = 0; i < elementData.length; i++)
        {
            s.writeObject(elementData[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {

        s.defaultReadObject();

        int arrayLength = s.readInt();
        E[] elementData = (E[])new Object[arrayLength];

        for (int i = 0; i < elementData.length; i++)
        {
            elementData[i] = (E)s.readObject();
        }
        array_ = elementData;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        Iterator<?> e = iterator();
        buf.append("[");
        int maxIndex = size() - 1;
        for (int i = 0; i <= maxIndex; i++)
        {
            buf.append(String.valueOf(e.next()));
            if (i < maxIndex)
            {
                buf.append(", ");
            }
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof List))
        {
            return false;
        }

        List<E> l2 = (List<E>)(o);
        if (size() != l2.size())
        {
            return false;
        }

        ListIterator<E> e1 = listIterator();
        ListIterator<E> e2 = l2.listIterator();
        while (e1.hasNext())
        {
            E o1 = e1.next();
            E o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        int hashCode = 1;
        Iterator<E> i = iterator();
        while (i.hasNext())
        {
            E obj = i.next();
            hashCode = HASH_CODE_NUMBER * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator()
    {
        return new COWIterator<E>(getArray(), 0);
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<E> listIterator()
    {
        return new COWIterator<E>(getArray(), 0);
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<E> listIterator(final int index)
    {
        E[] elementData = getArray();
        int len = elementData.length;
        if (index < 0 || index > len)
        {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new COWIterator<E>(getArray(), index);
    }

    /**
     * COWIteratorクラス
     * @author acroquest
     *
     * @param <E> 要素
     */
    private static class COWIterator<E> implements ListIterator<E>
    {
        /** 配列 */
        private final E[] cowIteratorArray_;

        /** カーソル　*/
        private int       cursor_;

        private COWIterator(E[] elementArray, int initialCursor)
        {
            cowIteratorArray_ = elementArray;
            cursor_ = initialCursor;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            return cursor_ < cowIteratorArray_.length;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasPrevious()
        {
            return cursor_ > 0;
        }

        /**
         * {@inheritDoc}
         */
        public E next()
        {
            try
            {
                return cowIteratorArray_[cursor_++];
            }
            catch (IndexOutOfBoundsException ex)
            {
                throw new NoSuchElementException();
            }
        }

        /**
         * {@inheritDoc}
         */
        public E previous()
        {
            try
            {
                return cowIteratorArray_[--cursor_];
            }
            catch (IndexOutOfBoundsException e)
            {
                throw new NoSuchElementException();
            }
        }

        public int nextIndex()
        {
            return cursor_;
        }

        public int previousIndex()
        {
            return cursor_ - 1;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public void set(E o)
        {
            throw new UnsupportedOperationException();
        }

        public void add(E o)
        {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<E> subList(int fromIndex, int toIndex)
    {

        int len = array_.length;
        if (fromIndex < 0 || toIndex > len || fromIndex > toIndex)
        {
            throw new IndexOutOfBoundsException();
        }
        return new COWSubList<E>(this, fromIndex, toIndex);
    }

    /**
     * COWSubListクラス
     * @author acroquest
     *
     * @param <E> 要素
     */
    private static class COWSubList<E> extends AbstractList<E>
    {

        private final CopyOnWriteArrayList<E> list_;

        private final int                     offset_;

        private int                           size_;

        private E[]                           expectedArray_;

        private COWSubList(CopyOnWriteArrayList<E> list, int fromIndex, int toIndex)
        {
            list_ = list;
            expectedArray_ = list_.getArray();
            offset_ = fromIndex;
            size_ = toIndex - fromIndex;
        }

        private void checkForComodification()
        {
            if (list_.array_ != expectedArray_)
            {
                throw new ConcurrentModificationException();
            }
        }

        private void rangeCheck(int index)
        {
            if (index < 0 || index >= size_)
            {
                throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size_);
            }
        }

        /**
         * {@inheritDoc}
         */
        public E set(int index, E element)
        {
            synchronized (list_)
            {
                rangeCheck(index);
                checkForComodification();
                E x = list_.set(index + offset_, element);
                expectedArray_ = list_.array_;
                return x;
            }
        }

        /**
         * {@inheritDoc}
         */
        public E get(int index)
        {
            synchronized (list_)
            {
                rangeCheck(index);
                checkForComodification();
                return list_.get(index + offset_);
            }
        }

        /**
         * {@inheritDoc}
         */
        public int size()
        {
            synchronized (list_)
            {
                checkForComodification();
                return size_;
            }
        }

        /**
         * {@inheritDoc}
         */
        public void add(int index, E element)
        {
            synchronized (list_)
            {
                checkForComodification();
                if (index < 0 || index > size_)
                {
                    throw new IndexOutOfBoundsException();
                }
                list_.add(index + offset_, element);
                expectedArray_ = list_.array_;
                size_++;
            }
        }

        /**
         * {@inheritDoc}
         */
        public void clear()
        {
            synchronized (list_)
            {
                checkForComodification();
                list_.removeRange(offset_, offset_ + size_);
                expectedArray_ = list_.array_;
                size_ = 0;
            }
        }

        /**
         * {@inheritDoc}
         */
        public E remove(int index)
        {
            synchronized (list_)
            {
                rangeCheck(index);
                checkForComodification();
                E result = list_.remove(index + offset_);
                expectedArray_ = list_.array_;
                size_--;
                return result;
            }
        }

        /**
         * {@inheritDoc}
         */
        public Iterator<E> iterator()
        {
            synchronized (list_)
            {
                checkForComodification();
                return new COWSubListIterator<E>(list_, 0, offset_, size_);
            }
        }

        /**
         * {@inheritDoc}
         */
        public ListIterator<E> listIterator(final int index)
        {
            synchronized (list_)
            {
                checkForComodification();
                if (index < 0 || index > size_)
                {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
                }
                return new COWSubListIterator<E>(list_, index, offset_, size_);
            }
        }

        /**
         * {@inheritDoc}
         */
        public List<E> subList(int fromIndex, int toIndex)
        {
            synchronized (list_)
            {
                checkForComodification();
                if (fromIndex < 0 || toIndex > size_)
                {
                    throw new IndexOutOfBoundsException();
                }
                return new COWSubList<E>(list_, fromIndex + offset_, toIndex + offset_);
            }
        }

    }

    /**
     * COWSubListIterator
     * @author acroquest
     *
     * @param <E> element
     */
    private static class COWSubListIterator<E> implements ListIterator<E>
    {
        /** イテレータ */
        private final ListIterator<E> iterator_;

        /** オフセット */
        private final int             offset_;

        /** サイズ */
        private final int             size_;

        /**
         * コンストラクタ
         * @param list リスト
         * @param index インデックス
         * @param offset オフセット
         * @param size サイズ
         */
        private COWSubListIterator(List<E> list, int index, int offset, int size)
        {
            this.offset_ = offset;
            this.size_ = size;
            iterator_ = list.listIterator(index + offset);
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            return nextIndex() < size_;
        }

        /**
         * {@inheritDoc}
         */
        public E next()
        {
            if (hasNext())
            {
                return iterator_.next();
            }
            else
            {
                throw new NoSuchElementException();
            }
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasPrevious()
        {
            return previousIndex() >= 0;
        }

        /**
         * {@inheritDoc}
         */
        public E previous()
        {
            if (hasPrevious())
            {
                return iterator_.previous();
            }
            else
            {
                throw new NoSuchElementException();
            }
        }

        /**
         * {@inheritDoc}
         */
        public int nextIndex()
        {
            return iterator_.nextIndex() - offset_;
        }

        /**
         * {@inheritDoc}
         */
        public int previousIndex()
        {
            return iterator_.previousIndex() - offset_;
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public void set(E o)
        {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public void add(E o)
        {
            throw new UnsupportedOperationException();
        }
    }
}
