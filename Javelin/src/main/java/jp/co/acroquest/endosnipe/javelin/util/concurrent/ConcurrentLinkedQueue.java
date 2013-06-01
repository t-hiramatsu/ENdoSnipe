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
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import jp.co.acroquest.endosnipe.javelin.util.ArrayList;

/**
 * ConcurrentLinkedQueueクラス
 * @author acroquest
 *
 * @param <E> 要素
 */
public class ConcurrentLinkedQueue<E> extends AbstractQueue<E> implements Queue<E>,
        java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long serialVersionUID = -3429890063062468928L;

    private transient volatile Node<E> head_ = new Node<E>(null, null);

    private transient volatile Node<E> tail_ = head_;

    private static final AtomicReferenceFieldUpdater<ConcurrentLinkedQueue, Node> TAIL_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(ConcurrentLinkedQueue.class, Node.class, "tail");

    private static final AtomicReferenceFieldUpdater<ConcurrentLinkedQueue, Node> HEAD_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(ConcurrentLinkedQueue.class, Node.class, "head");


    /**
     * Nodeクラス
     * @author acroquest
     *
     * @param <E> 要素
     */
    private static class Node<E>
    {
        /** 要素 */
        private volatile E item_;

        /** 次の要素 */
        private volatile Node<E> next_;

        /** 次のノードのUpdater */
        private static final AtomicReferenceFieldUpdater<Node, Node> NEXT_UPDATER =
                AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");

        /** 要素のUpdater */
        private static final AtomicReferenceFieldUpdater<Node, Object> ITEM_UPDATER =
                AtomicReferenceFieldUpdater.newUpdater(Node.class, Object.class, "item");

        /**
         * コンストラクタ
         * @param element 要素
         * @param next 次の要素
         */
        public Node(E element, Node<E> next)
        {
            item_ = element;
            next_ = next;
        }

        /**
         * 要素を取得します。
         * @return 要素
         */
        public E getItem()
        {
            return item_;
        }


        /**
         * 指定された要素を比較して設定します。
         * @param cmp 比較用要素
         * @param val 設定する値
         * @return 処理が成功した時true
         */
        boolean compareAndSetItem(E cmp, E val)
        {
            return ITEM_UPDATER.compareAndSet(this, cmp, val);
        }

        /**
         * 要素を設定します。
         * @param val 要素
         */
        void setItem(E val)
        {
            ITEM_UPDATER.set(this, val);
        }

        /**
         * 次のノードを取得します。
         * @return 次のノード
         */
        Node<E> getNext()
        {
            return next_;
        }

        /**
         * 次のノードを比較し、設定します。
         * @param cmp 比較用要素
         * @param val 設定する値
         * @return 処理が成功した時true
         */
        boolean compareAndSetNext(Node<E> cmp, Node<E> val)
        {
            return NEXT_UPDATER.compareAndSet(this, cmp, val);
        }

    }

    private boolean casTail(Node<E> cmp, Node<E> val)
    {
        return TAIL_UPDATER.compareAndSet(this, cmp, val);
    }

    private boolean casHead(Node<E> cmp, Node<E> val)
    {
        return HEAD_UPDATER.compareAndSet(this, cmp, val);
    }

    /**
     * Creates a <tt>ConcurrentLinkedQueue</tt> that is initially empty.
     */
    public ConcurrentLinkedQueue()
    {
    }

    /**
     * Creates a <tt>ConcurrentLinkedQueue</tt> 
     * initially containing the elements of the given collection,
     * added in traversal order of the collection's iterator.
     * @param c the collection of elements to initially contain
     * is <tt>null</tt>
     */
    public ConcurrentLinkedQueue(Collection<? extends E> c)
    {
        for (Iterator<? extends E> it = c.iterator(); it.hasNext();)
        {
            add(it.next());
        }
    }

    /**
     * Adds the specified element to the tail of this queue.
     * @param o the element to add.
     * @return <tt>true</tt> (as per the general contract of
     * <tt>Collection.add</tt>).
     *
     */
    public boolean add(E o)
    {
        return offer(o);
    }

    /**
     * Inserts the specified element to the tail of this queue.
     *
     * @param o the element to add.
     * @return <tt>true</tt> (as per the general contract of
     * <tt>Queue.offer</tt>).
     */
    public boolean offer(E o)
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        Node<E> n = new Node<E>(o, null);
        for (;;)
        {
            Node<E> tail = tail_;
            Node<E> s = tail.getNext();
            if (tail == tail_)
            {
                if (s == null)
                {
                    if (tail.compareAndSetNext(null, n))
                    {
                        casTail(tail, n);
                        return true;
                    }
                }
                else
                {
                    casTail(tail, s);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public E poll()
    {
        for (;;)
        {
            Node<E> h = head_;
            Node<E> t = tail_;
            Node<E> first = h.getNext();
            if (h == head_)
            {
                if (h == t)
                {
                    if (first == null)
                    {
                        return null;
                    }
                    else
                    {
                        casTail(t, first);
                    }
                }
                else if (casHead(h, first))
                {
                    E item = first.getItem();
                    if (item != null)
                    {
                        first.setItem(null);
                        return item;
                    }

                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public E peek()
    {
        for (;;)
        {
            Node<E> h = head_;
            Node<E> t = tail_;
            Node<E> first = h.getNext();
            if (h == head_)
            {
                if (h == t)
                {
                    if (first == null)
                    {
                        return null;
                    }
                    else
                    {
                        casTail(t, first);
                    }
                }
                else
                {
                    E item = first.getItem();
                    if (item != null)
                    {
                        return item;
                    }
                    else
                    {
                        casHead(h, first);
                    }
                }
            }
        }
    }

    /**
     * Returns the first actual (non-header) node on list.  This is yet
     * another variant of poll/peek; here returning out the first
     * node, not element (so we cannot collapse with peek() without
     * introducing race.)
     * 
     * @return the first actual (non-header) node on list
     */
    Node<E> first()
    {
        for (;;)
        {
            Node<E> h = head_;
            Node<E> t = tail_;
            Node<E> first = h.getNext();
            if (h == head_)
            {
                if (h == t)
                {
                    if (first == null)
                    {
                        return null;
                    }
                    else
                    {
                        casTail(t, first);
                    }
                }
                else
                {
                    if (first.getItem() != null)
                    {
                        return first;
                    }
                    else
                    {
                        casHead(h, first);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return first() == null;
    }

    /**
     * Returns the number of elements in this queue.  If this queue
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * <p>Beware that, unlike in most collections, this method is
     * <em>NOT</em> a constant-time operation. Because of the
     * asynchronous nature of these queues, determining the current
     * number of elements requires an O(n) traversal.
     *
     * @return  the number of elements in this queue.
     */
    public int size()
    {
        int count = 0;
        for (Node<E> p = first(); p != null; p = p.getNext())
        {
            if (p.getItem() != null)
            {

                if (++count == Integer.MAX_VALUE)
                {
                    break;
                }
            }
        }
        return count;
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
        for (Node<E> p = first(); p != null; p = p.getNext())
        {
            E item = p.getItem();
            if (item != null && o.equals(item))
            {
                return true;
            }
        }
        return false;
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
        for (Node<E> p = first(); p != null; p = p.getNext())
        {
            E item = p.getItem();
            if (item != null && o.equals(item) && p.compareAndSetItem(item, null))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray()
    {

        ArrayList<E> al = new ArrayList<E>();
        for (Node<E> p = first(); p != null; p = p.getNext())
        {
            E item = p.getItem();
            if (item != null)
            {
                al.add(item);
            }
        }
        return al.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a)
    {

        int k = 0;
        Node<E> p;
        for (p = first(); p != null && k < a.length; p = p.getNext())
        {
            E item = p.getItem();
            if (item != null)
            {
                a[k++] = (T)item;
            }
        }
        if (p == null)
        {
            if (k < a.length)
            {
                a[k] = null;
            }
            return a;
        }

        ArrayList<E> al = new ArrayList<E>();
        for (Node<E> q = first(); q != null; q = q.getNext())
        {
            E item = q.getItem();
            if (item != null)
            {
                al.add(item);
            }
        }
        return (T[])al.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator()
    {
        return new Itr();
    }

    /**
     * イテレータクラス
     * @author acroquest
     *
     */
    private class Itr implements Iterator<E>
    {
        /** 次のノード */
        private Node<E> nextNode_;

        /** 次の要素 */
        private E nextItem_;

        private Node<E> lastReturn_;

        Itr()
        {
            advance();
        }

        private E advance()
        {
            lastReturn_ = nextNode_;
            E x = nextItem_;

            Node<E> p = (nextNode_ == null) ? first() : nextNode_.getNext();
            for (;;)
            {
                if (p == null)
                {
                    nextNode_ = null;
                    nextItem_ = null;
                    return x;
                }
                E item = p.getItem();
                if (item != null)
                {
                    nextNode_ = p;
                    nextItem_ = item;
                    return x;
                }
                else
                {
                    p = p.getNext();
                }
            }
        }

        public boolean hasNext()
        {
            return nextNode_ != null;
        }

        public E next()
        {
            if (nextNode_ == null)
            {
                throw new NoSuchElementException();
            }
            return advance();
        }

        public void remove()
        {
            Node<E> l = lastReturn_;
            if (l == null)
            {
                throw new IllegalStateException();
            }

            l.setItem(null);
            lastReturn_ = null;
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {

        s.defaultWriteObject();

        for (Node<E> p = first(); p != null; p = p.getNext())
        {
            Object item = p.getItem();
            if (item != null)
            {
                s.writeObject(item);
            }
        }

        s.writeObject(null);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {
        s.defaultReadObject();
        head_ = new Node<E>(null, null);
        tail_ = head_;
        for (;;)
        {
            E item = (E)s.readObject();
            if (item == null)
            {
                break;
            }
            else
            {
                offer(item);
            }
        }
    }

}
