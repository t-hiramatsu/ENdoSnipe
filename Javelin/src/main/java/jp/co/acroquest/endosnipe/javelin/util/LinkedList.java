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

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * LinkedListクラス
 * @author acroquest

 * @param <E> エレメント
 */
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Queue<E>,
        Cloneable, java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long  serialVersionUID = 6287226297445011619L;

    /** ヘッダ */
    private transient Entry<E> header_          = new Entry<E>(null, null, null);

    /** サイズ */
    private transient int      size_            = 0;

    /**
     * コンストラクタ
     */
    public LinkedList()
    {
        header_.next_ = header_;
        header_.previous_ = header_;
    }

    /**
     * コンストラクタ
     * @param c コレクション
     */
    public LinkedList(Collection<? extends E> c)
    {
        this();
        addAll(c);
    }

    /**
     * 最初のエレメントを取得します。
     * @return 最初のエレメント
     */
    public E getFirst()
    {
        if (size_ == 0)
        {
            throw new NoSuchElementException();
        }

        return header_.next_.element_;
    }

    /**
     * 最後のエレメントを取得します。
     * @return 最後のエレメント
     */
    public E getLast()
    {
        if (size_ == 0)
        {
            throw new NoSuchElementException();
        }

        return header_.previous_.element_;
    }

    /**
     * 最初のエレメントを削除します。
     * @return 削除したエレメント
     */
    public E removeFirst()
    {
        return remove(header_.next_);
    }

    /**
     * 最後のエレメントを削除します。
     * @return 削除したエレメント
     */
    public E removeLast()
    {
        return remove(header_.previous_);
    }

    /**
     * 最初にエレメントを追加します。
     * @param element エレメント
     */
    public void addFirst(E element)
    {
        addBefore(element, header_.next_);
    }

    /**
     * 最後にエレメントを追加します。
     * @param element エレメント
     */
    public void addLast(E element)
    {
        addBefore(element, header_);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o)
    {
        return indexOf(o) != -1;
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
    public boolean add(E o)
    {
        addBefore(o, header_);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o)
    {
        if (o == null)
        {
            for (Entry<E> e = header_.next_; e != header_; e = e.next_)
            {
                if (e.element_ == null)
                {
                    remove(e);
                    return true;
                }
            }
        }
        else
        {
            for (Entry<E> e = header_.next_; e != header_; e = e.next_)
            {
                if (o.equals(e.element_))
                {
                    remove(e);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends E> c)
    {
        return addAll(size_, c);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends E> collection)
    {
        if (index < 0 || index > size_)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
        }
        Object[] array = collection.toArray();
        int numNew = array.length;
        if (numNew == 0)
        {
            return false;
        }
        modCount++;

        Entry<E> successor = (index == size_ ? header_ : entry(index));
        Entry<E> predecessor = successor.previous_;
        for (int loopIndex = 0; loopIndex < numNew; loopIndex++)
        {
            Entry<E> entry = new Entry<E>((E)array[loopIndex], successor, predecessor);
            predecessor.next_ = entry;
            predecessor = entry;
        }
        successor.previous_ = predecessor;

        size_ += numNew;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        Entry<E> entry = header_.next_;
        while (entry != header_)
        {
            Entry<E> next = entry.next_;
            entry.next_ = null;
            entry.previous_ = null;
            entry.element_ = null;
            entry = next;
        }
        header_.next_ = header_;
        header_.previous_ = header_;
        size_ = 0;
        modCount++;
    }

    /**
     * {@inheritDoc}
     */
    public E get(int index)
    {
        return entry(index).element_;
    }

    /**
     * {@inheritDoc}
     */
    public E set(int index, E element)
    {
        Entry<E> e = entry(index);
        E oldVal = e.element_;
        e.element_ = element;
        return oldVal;
    }

    /**
     * {@inheritDoc}
     */
    public void add(int index, E element)
    {
        addBefore(element, (index == size_ ? header_ : entry(index)));
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index)
    {
        return remove(entry(index));
    }

    /**
     * Entryを指定したindexに追加します。
     * @param index
     * @return　指定したindexにセットされたEntry
     */
    private Entry<E> entry(int index)
    {
        if (index < 0 || index >= size_)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
        }
        Entry<E> entry = header_;
        if (index < (size_ >> 1))
        {
            for (int i = 0; i <= index; i++)
            {
                entry = entry.next_;
            }
        }
        else
        {
            for (int i = size_; i > index; i--)
            {
                entry = entry.previous_;
            }
        }
        
        return entry;
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(Object object)
    {
        int index = 0;
        if (object == null)
        {
            for (Entry<E> entry = header_.next_; entry != header_; entry = entry.next_)
            {
                if (entry.element_ == null)
                {
                    return index;
                }
                index++;
            }
        }
        else
        {
            for (Entry<E> e = header_.next_; e != header_; e = e.next_)
            {
                if (object.equals(e.element_))
                {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(Object object)
    {
        int index = size_;
        if (object == null)
        {
            for (Entry<E> entry = header_.previous_; entry != header_; entry = entry.previous_)
            {
                index--;
                if (entry.element_ == null)
                {
                    return index;
                }
            }
        }
        else
        {
            for (Entry<E> entry = header_.previous_; entry != header_; entry = entry.previous_)
            {
                index--;
                if (object.equals(entry.element_))
                {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public E peek()
    {
        if (size_ == 0)
        {
            return null;
        }
        return getFirst();
    }

    /**
     * {@inheritDoc}
     */
    public E element()
    {
        return getFirst();
    }

    /**
     * {@inheritDoc}
     */
    public E poll()
    {
        if (size_ == 0)
        {
            return null;
        }
        return removeFirst();
    }

    /**
     * {@inheritDoc}
     */
    public E remove()
    {
        return removeFirst();
    }

    /**
     * {@inheritDoc}
     */
    public boolean offer(E o)
    {
        return add(o);
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index)
    {
        return new ListItr(index);
    }

    /**
     * ListIteratorクラス
     *
     */
    private class ListItr implements ListIterator<E>
    {

        private Entry<E> lastReturned_     = header_;

        private Entry<E> next_;

        private int      nextIndex_;

        private int      expectedModCount_ = modCount;

        ListItr(int index)
        {
            if (index < 0 || index > size_)
            {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size_);
            }
            if (index < (size_ >> 1))
            {
                next_ = header_.next_;
                for (nextIndex_ = 0; nextIndex_ < index; nextIndex_++)
                {
                    next_ = next_.next_;
                }
            }
            else
            {
                next_ = header_;
                for (nextIndex_ = size_; nextIndex_ > index; nextIndex_--)
                {
                    next_ = next_.previous_;
                }
            }
        }

        public boolean hasNext()
        {
            return nextIndex_ != size_;
        }

        public E next()
        {
            checkForComodification();
            if (nextIndex_ == size_)
            {
                throw new NoSuchElementException();
            }

            lastReturned_ = next_;
            next_ = next_.next_;
            nextIndex_++;
            return lastReturned_.element_;
        }

        public boolean hasPrevious()
        {
            return nextIndex_ != 0;
        }

        public E previous()
        {
            if (nextIndex_ == 0)
            {
                throw new NoSuchElementException();
            }

            lastReturned_ = next_.previous_;
            next_ = next_.previous_;
            nextIndex_--;
            checkForComodification();
            return lastReturned_.element_;
        }

        public int nextIndex()
        {
            return nextIndex_;
        }

        public int previousIndex()
        {
            return nextIndex_ - 1;
        }

        public void remove()
        {
            checkForComodification();
            Entry<E> lastNext = lastReturned_.next_;
            try
            {
                LinkedList.this.remove(lastReturned_);
            }
            catch (NoSuchElementException e)
            {
                throw new IllegalStateException();
            }
            if (next_ == lastReturned_)
            {
                next_ = lastNext;
            }
            else
            {
                nextIndex_--;
            }
            lastReturned_ = header_;
            expectedModCount_++;
        }

        public void set(E o)
        {
            if (lastReturned_ == header_)
            {
                throw new IllegalStateException();
            }
            checkForComodification();
            lastReturned_.element_ = o;
        }

        public void add(E o)
        {
            checkForComodification();
            lastReturned_ = header_;
            addBefore(o, next_);
            nextIndex_++;
            expectedModCount_++;
        }

        final void checkForComodification()
        {
            if (modCount != expectedModCount_)
            {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Entryクラス
     * @param <E>
     */
    private static class Entry<E>
    {
        private E        element_;

        private Entry<E> next_;

        private Entry<E> previous_;

        public Entry(E element, Entry<E> next, Entry<E> previous)
        {
            this.element_ = element;
            this.next_ = next;
            this.previous_ = previous;
        }
    }

    private Entry<E> addBefore(E o, Entry<E> e)
    {
        Entry<E> newEntry = new Entry<E>(o, e, e.previous_);
        newEntry.previous_.next_ = newEntry;
        newEntry.next_.previous_ = newEntry;
        size_++;
        modCount++;
        return newEntry;
    }

    private E remove(Entry<E> entry)
    {
        if (entry == header_)
        {
            throw new NoSuchElementException();
        }

        E result = entry.element_;
        entry.previous_.next_ = entry.next_;
        entry.next_.previous_ = entry.previous_;
        entry.next_ = null;
        entry.previous_ = null;
        entry.element_ = null;
        size_--;
        modCount++;

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        LinkedList<E> clone = null;
        try
        {
            clone = (LinkedList<E>)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError();
        }

        clone.header_ = new Entry<E>(null, null, null);
        clone.header_.next_ = clone.header_;
        clone.header_.previous_ = clone.header_;
        clone.size_ = 0;
        clone.modCount = 0;

        for (Entry<E> entry = header_.next_; entry != header_; entry = entry.next_)
        {
            clone.add(entry.element_);
        }

        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray()
    {
        Object[] result = new Object[size_];
        int i = 0;
        for (Entry<E> entry = header_.next_; entry != header_; entry = entry.next_)
        {
            result[i++] = entry.element_;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array)
    {
        if (array.length < size_)
        {
            Class<?> componentType = array.getClass().getComponentType();
            array = (T[])java.lang.reflect.Array.newInstance(componentType, size_);
        }
        int i = 0;
        Object[] result = array;
        for (Entry<E> e = header_.next_; e != header_; e = e.next_)
        {
            result[i++] = e.element_;
        }

        if (array.length > size_)
        {
            array[size_] = null;
        }

        return array;
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {
        s.defaultWriteObject();

        s.writeInt(size_);

        for (Entry<E> e = header_.next_; e != header_; e = e.next_)
        {
            s.writeObject(e.element_);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {
        s.defaultReadObject();

        int size = s.readInt();

        header_ = new Entry<E>(null, null, null);
        header_.next_ = header_;
        header_.previous_ = header_;

        for (int i = 0; i < size; i++)
        {
            addBefore((E)s.readObject(), header_);
        }
    }
}
