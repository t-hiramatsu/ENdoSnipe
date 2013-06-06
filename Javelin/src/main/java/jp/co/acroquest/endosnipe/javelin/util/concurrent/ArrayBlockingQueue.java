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

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ArrayBlockingQueueクラス
 * @author acroquest
 *
 * @param <E> 要素
 */
public class ArrayBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>,
        java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long   serialVersionUID = -3591139035925002859L;

    private final E[]           items_;

    private transient int       takeIndex_ = 0;

    private transient int       putIndex_;

    private int                 count_;

    private final ReentrantLock lock_;

    private final transient Condition     notEmpty_;

    private final transient Condition     notFull_;

    /**
     * 指定した値に1加えた数を返します。要素のサイズ上限に達した時は0を返します。
     * @param i 値
     * @return 指定した値に1を加えた数
     */
    final int inc(int i)
    {
        return (++i == items_.length) ? 0 : i;
    }

    /**
     * 要素を追加します。
     * @param x 要素
     */
    private void insert(E x)
    {
        items_[putIndex_] = x;
        putIndex_ = inc(putIndex_);
        ++count_;
        notEmpty_.signal();
    }

    /**
     * 要素を除外します。
     * @return 除外された要素
     */
    private E extract()
    {
        final E[] ITEMS = this.items_;
        E x = ITEMS[takeIndex_];
        ITEMS[takeIndex_] = null;
        takeIndex_ = inc(takeIndex_);
        --count_;
        notFull_.signal();
        return x;
    }

    /**
     * 指定したインデックスの要素を削除します。
     * @param i 削除する要素のインデックス
     */
    void removeAt(int i)
    {
        final E[] ITEMS = this.items_;
        if (i == takeIndex_)
        {
            ITEMS[takeIndex_] = null;
            takeIndex_ = inc(takeIndex_);
        }
        else
        {
            for (;;)
            {
                int nexti = inc(i);
                if (nexti != putIndex_)
                {
                    ITEMS[i] = ITEMS[nexti];
                    i = nexti;
                }
                else
                {
                    ITEMS[i] = null;
                    putIndex_ = i;
                    break;
                }
            }
        }
        --count_;
        notFull_.signal();
    }

    /**
     * コンストラクタ
     * @param capacity キューの容量
     */
    public ArrayBlockingQueue(int capacity)
    {
        this(capacity, false);
    }

    /**
     * コンストラクタ
     * @param capacity キューの容量
     * @param fair 挿入または削除時にブロックされたスレッドに対するキューアクセスをFIFO の順序で処理するフラグ
     */
    @SuppressWarnings("unchecked")
    public ArrayBlockingQueue(int capacity, boolean fair)
    {
        if (capacity <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.items_ = (E[])new Object[capacity];
        lock_ = new ReentrantLock(fair);
        notEmpty_ = lock_.newCondition();
        notFull_ = lock_.newCondition();
    }

    /**
     * コンストラクタ
     * @param capacity キューの容量
     * @param fair 挿入または削除時にブロックされたスレッドに対するキューアクセスをFIFO の順序で処理するフラグ
     * @param c 要素のコレクション
     */
    public ArrayBlockingQueue(int capacity, boolean fair, Collection<? extends E> c)
    {
        this(capacity, fair);
        if (capacity < c.size())
        {
            throw new IllegalArgumentException();
        }
        for (Iterator<? extends E> it = c.iterator(); it.hasNext();)
        {
            add(it.next());
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean offer(E o)
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            if (count_ == items_.length)
            {
                return false;
            }
            else
            {
                insert(o);
                return true;
            }
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean offer(E o, long timeout, TimeUnit unit)
        throws InterruptedException
    {

        if (o == null)
        {
            throw new NullPointerException();
        }
        final ReentrantLock LOCK = this.lock_;
        LOCK.lockInterruptibly();
        try
        {
            long nanos = unit.toNanos(timeout);
            for (;;)
            {
                if (count_ != items_.length)
                {
                    insert(o);
                    return true;
                }
                if (nanos <= 0)
                {
                    return false;
                }
                try
                {
                    nanos = notFull_.awaitNanos(nanos);
                }
                catch (InterruptedException ie)
                {
                    notFull_.signal();
                    throw ie;
                }
            }
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public E poll()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            if (count_ == 0)
            {
                return null;
            }
            E x = extract();
            return x;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public E poll(long timeout, TimeUnit unit)
        throws InterruptedException
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lockInterruptibly();
        try
        {
            long nanos = unit.toNanos(timeout);
            for (;;)
            {
                if (count_ != 0)
                {
                    E x = extract();
                    return x;
                }
                if (nanos <= 0)
                {
                    return null;
                }
                try
                {
                    nanos = notEmpty_.awaitNanos(nanos);
                }
                catch (InterruptedException ie)
                {
                    notEmpty_.signal();
                    throw ie;
                }
            }
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o)
    {
        if (o == null)
        {
            return false;
        }
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            int i = takeIndex_;
            int k = 0;
            for (;;)
            {
                if (k++ >= count_)
                {
                    return false;
                }
                if (o.equals(ITEMS[i]))
                {
                    removeAt(i);
                    return true;
                }
                i = inc(i);
            }
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public E peek()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            return (count_ == 0) ? null : items_[takeIndex_];
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public E take()
        throws InterruptedException
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lockInterruptibly();
        try
        {
            try
            {
                while (count_ == 0)
                {
                    notEmpty_.await();
                }
            }
            catch (InterruptedException ie)
            {
                notEmpty_.signal();
                throw ie;
            }
            E x = extract();
            return x;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(E o)
        throws InterruptedException
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lockInterruptibly();
        try
        {
            try
            {
                while (count_ == ITEMS.length)
                {
                    notFull_.await();
                }
            }
            catch (InterruptedException ie)
            {
                notFull_.signal();
                throw ie;
            }
            insert(o);
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            return count_;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int remainingCapacity()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            return items_.length - count_;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o)
    {
        if (o == null)
        {
            return false;
        }
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            int i = takeIndex_;
            int k = 0;
            while (k++ < count_)
            {
                if (o.equals(ITEMS[i]))
                {
                    return true;
                }
                i = inc(i);
            }
            return false;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray()
    {
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            Object[] a = new Object[count_];
            int k = 0;
            int i = takeIndex_;
            while (k < count_)
            {
                a[k++] = ITEMS[i];
                i = inc(i);
            }
            return a;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a)
    {
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            if (a.length < count_)
            {
                Class<?> componentType = a.getClass().getComponentType();
                a = (T[])java.lang.reflect.Array.newInstance(componentType, count_);
            }

            int k = 0;
            int i = takeIndex_;
            while (k < count_)
            {
                a[k++] = (T)ITEMS[i];
                i = inc(i);
            }
            if (a.length > count_)
            {
                a[count_] = null;
            }
            return a;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            return super.toString();
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            int i = takeIndex_;
            int k = count_;
            while (k-- > 0)
            {
                ITEMS[i] = null;
                i = inc(i);
            }
            count_ = 0;
            putIndex_ = 0;
            takeIndex_ = 0;
            notFull_.signalAll();
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c)
    {
        if (c == null)
        {
            throw new NullPointerException();
        }
        if (c == this)
        {
            throw new IllegalArgumentException();
        }
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            int i = takeIndex_;
            int n = 0;
            int max = count_;
            while (n < max)
            {
                c.add(ITEMS[i]);
                ITEMS[i] = null;
                i = inc(i);
                ++n;
            }
            if (n > 0)
            {
                count_ = 0;
                putIndex_ = 0;
                takeIndex_ = 0;
                notFull_.signalAll();
            }
            return n;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c, int maxElements)
    {
        if (c == null)
        {
            throw new NullPointerException();
        }
        if (c == this)
        {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0)
        {
            return 0;
        }
        final E[] ITEMS = this.items_;
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            int i = takeIndex_;
            int n = 0;
            int max = (maxElements < count_) ? maxElements : count_;
            while (n < max)
            {
                c.add(ITEMS[i]);
                ITEMS[i] = null;
                i = inc(i);
                ++n;
            }
            if (n > 0)
            {
                count_ -= n;
                takeIndex_ = i;
                notFull_.signalAll();
            }
            return n;
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator()
    {
        final ReentrantLock LOCK = this.lock_;
        LOCK.lock();
        try
        {
            return new Itr();
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * イテレータクラス
     * @author acroquest
     */
    private class Itr implements Iterator<E>
    {
        /** 次のindex */
        private int nextIndex_;

        /** 次の要素 */
        private E   nextItem_;

        /** 最後に返したindex */
        private int lastReturn_;

        Itr()
        {
            lastReturn_ = -1;
            if (count_ == 0)
            {
                nextIndex_ = -1;
            }
            else
            {
                nextIndex_ = takeIndex_;
                nextItem_ = items_[takeIndex_];
            }
        }

        public boolean hasNext()
        {
            return nextIndex_ >= 0;
        }

        private void checkNext()
        {
            if (nextIndex_ == putIndex_)
            {
                nextIndex_ = -1;
                nextItem_ = null;
            }
            else
            {
                nextItem_ = items_[nextIndex_];
                if (nextItem_ == null)
                {
                    nextIndex_ = -1;
                }
            }
        }

        public E next()
        {
            final ReentrantLock LOCK = ArrayBlockingQueue.this.lock_;
            LOCK.lock();
            try
            {
                if (nextIndex_ < 0)
                {
                    throw new NoSuchElementException();
                }
                lastReturn_ = nextIndex_;
                E x = nextItem_;
                nextIndex_ = inc(nextIndex_);
                checkNext();
                return x;
            }
            finally
            {
                LOCK.unlock();
            }
        }

        public void remove()
        {
            final ReentrantLock LOCK = ArrayBlockingQueue.this.lock_;
            LOCK.lock();
            try
            {
                int i = lastReturn_;
                if (i == -1)
                {
                    throw new IllegalStateException();
                }

                lastReturn_ = -1;
                int ti = takeIndex_;
                removeAt(i);
                nextIndex_ = (i == ti) ? takeIndex_ : i;
                checkNext();
            }
            finally
            {
                LOCK.unlock();
            }
        }
    }
}
