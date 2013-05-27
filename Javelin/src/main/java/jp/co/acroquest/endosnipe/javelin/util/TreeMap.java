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

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * ツリーマップクラス
 *
 * @author acroquest
 *
 * @param <K> キー
 * @param <V> 値
 */
public class TreeMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V>, Cloneable,
        java.io.Serializable
{
    /** シリアルバージョンID */
    private static final long                       serialVersionUID = -5489490343290508150L;

    private Comparator<? super K>                   comparator_      = null;

    private transient Entry<K, V>                   root_            = null;

    private transient int                           size_            = 0;

    private transient int                           modCount_        = 0;

    private transient volatile Set<Map.Entry<K, V>> entrySet_        = null;

    /**
     * コンストラクタ
     */
    public TreeMap()
    {
    }

    /**
     * コンストラクタ
     * @param c コンパレータ
     */
    public TreeMap(Comparator<? super K> c)
    {
        this.comparator_ = c;
    }

    /**
     * コンストラクタ
     * @param m マップ
     */
    public TreeMap(Map<? extends K, ? extends V> m)
    {
        putAll(m);
    }

    /**
     * コンストラクタ
     * @param m ソート済みマップ
     */
    public TreeMap(SortedMap<K, ? extends V> m)
    {
        comparator_ = m.comparator();
        try
        {
            buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
        }
        catch (java.io.IOException cannotHappen)
        {
        }
        catch (ClassNotFoundException cannotHappen)
        {
        }
    }

    private void incrementSize()
    {
        modCount_++;
        size_++;
    }

    private void decrementSize()
    {
        modCount_++;
        size_--;
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
    public boolean containsKey(Object key)
    {
        return getEntry(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(Object value)
    {
        return (root_ == null ? false : (value == null ? valueSearchNull(root_)
                : valueSearchNonNull(root_, value)));
    }

    private boolean valueSearchNull(Entry<K, V> n)
    {
        if (n.value_ == null)
        {
            return true;
        }

        return (n.left_ != null && valueSearchNull(n.left_))
                || (n.right_ != null && valueSearchNull(n.right_));
    }

    private boolean valueSearchNonNull(Entry<K, V> n, Object value)
    {

        if (value.equals(n.value_))
        {
            return true;
        }

        return (n.left_ != null && valueSearchNonNull(n.left_, value))
                || (n.right_ != null && valueSearchNonNull(n.right_, value));
    }

    /**
     * {@inheritDoc}
     */
    public V get(Object key)
    {
        Entry<K, V> p = getEntry(key);
        return (p == null ? null : p.value_);
    }

    /**
     * {@inheritDoc}
     */
    public Comparator<? super K> comparator()
    {
        return comparator_;
    }

    /**
     * {@inheritDoc}
     */
    public K firstKey()
    {
        return key(firstEntry());
    }

    /**
     * {@inheritDoc}
     */
    public K lastKey()
    {
        return key(lastEntry());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> map)
    {
        int mapSize = map.size();
        if (size_ == 0 && mapSize != 0 && map instanceof SortedMap)
        {
            Comparator<?> c = ((SortedMap<K, V>)map).comparator();
            if (c == comparator_ || (c != null && c.equals(comparator_)))
            {
                ++modCount_;
                try
                {
                    buildFromSorted(mapSize, map.entrySet().iterator(), null, null);
                }
                catch (java.io.IOException cannotHappen)
                {
                }
                catch (ClassNotFoundException cannotHappen)
                {
                }
                return;
            }
        }
        super.putAll(map);
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> getEntry(Object key)
    {
        Entry<K, V> p = root_;
        K k = (K)key;
        while (p != null)
        {
            int cmp = compare(k, p.key_);
            if (cmp == 0)
            {
                return p;
            }
            else if (cmp < 0)
            {
                p = p.left_;
            }
            else
            {
                p = p.right_;
            }
        }
        return null;
    }

    private Entry<K, V> getCeilEntry(K key)
    {
        Entry<K, V> p = root_;
        if (p == null)
        {
            return null;
        }

        while (true)
        {
            int cmp = compare(key, p.key_);
            if (cmp == 0)
            {
                return p;
            }
            else if (cmp < 0)
            {
                if (p.left_ != null)
                {
                    p = p.left_;
                }
                else
                {
                    return p;
                }
            }
            else
            {
                if (p.right_ != null)
                {
                    p = p.right_;
                }
                else
                {
                    Entry<K, V> parent = p.parent_;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.right_)
                    {
                        ch = parent;
                        parent = parent.parent_;
                    }
                    return parent;
                }
            }
        }
    }

    private Entry<K, V> getPrecedingEntry(K key)
    {
        Entry<K, V> p = root_;
        if (p == null)
        {
            return null;
        }

        while (true)
        {
            int cmp = compare(key, p.key_);
            if (cmp > 0)
            {
                if (p.right_ != null)
                {
                    p = p.right_;
                }
                else
                {
                    return p;
                }
            }
            else
            {
                if (p.left_ != null)
                {
                    p = p.left_;
                }
                else
                {
                    Entry<K, V> parent = p.parent_;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.left_)
                    {
                        ch = parent;
                        parent = parent.parent_;
                    }
                    return parent;
                }
            }
        }
    }

    private static <K> K key(Entry<K, ?> e)
    {
        if (e == null)
        {
            throw new NoSuchElementException();
        }
        return e.key_;
    }

    /**
     * {@inheritDoc}
     */
    public V put(K key, V value)
    {
        Entry<K, V> t = root_;

        if (t == null)
        {
            incrementSize();
            root_ = new Entry<K, V>(key, value, null);
            return null;
        }

        while (true)
        {
            int cmp = compare(key, t.key_);
            if (cmp == 0)
            {
                return t.setValue(value);
            }
            else if (cmp < 0)
            {
                if (t.left_ != null)
                {
                    t = t.left_;
                }
                else
                {
                    incrementSize();
                    t.left_ = new Entry<K, V>(key, value, t);
                    fixAfterInsertion(t.left_);
                    return null;
                }
            }
            else
            {
                if (t.right_ != null)
                {
                    t = t.right_;
                }
                else
                {
                    incrementSize();
                    t.right_ = new Entry<K, V>(key, value, t);
                    fixAfterInsertion(t.right_);
                    return null;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public V remove(Object key)
    {
        Entry<K, V> p = getEntry(key);
        if (p == null)
        {
            return null;
        }

        V oldValue = p.value_;
        deleteEntry(p);
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        modCount_++;
        size_ = 0;
        root_ = null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        TreeMap<K, V> clone = null;
        try
        {
            clone = (TreeMap<K, V>)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError();
        }

        clone.root_ = null;
        clone.size_ = 0;
        clone.modCount_ = 0;
        clone.entrySet_ = null;

        try
        {
            clone.buildFromSorted(size_, entrySet().iterator(), null, null);
        }
        catch (java.io.IOException cannotHappen)
        {
        }
        catch (ClassNotFoundException cannotHappen)
        {
        }

        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public Set<K> keySet()
    {
        if (keySet_ == null)
        {
            keySet_ = new AbstractSet<K>() {
                public Iterator<K> iterator()
                {
                    return new KeyIterator();
                }

                public int size()
                {
                    return TreeMap.this.size();
                }

                public boolean contains(Object o)
                {
                    return containsKey(o);
                }

                public boolean remove(Object o)
                {
                    int oldSize = size_;
                    TreeMap.this.remove(o);
                    return size_ != oldSize;
                }

                public void clear()
                {
                    TreeMap.this.clear();
                }
            };
        }
        return keySet_;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<V> values()
    {
        if (values_ == null)
        {
            values_ = new AbstractCollection<V>() {
                public Iterator<V> iterator()
                {
                    return new ValueIterator();
                }

                public int size()
                {
                    return TreeMap.this.size();
                }

                public boolean contains(Object o)
                {
                    for (Entry<K, V> e = firstEntry(); e != null; e = successor(e))
                    {
                        if (valEquals(e.getValue(), o))
                        {
                            return true;
                        }
                    }
                    return false;
                }

                public boolean remove(Object o)
                {
                    for (Entry<K, V> e = firstEntry(); e != null; e = successor(e))
                    {
                        if (valEquals(e.getValue(), o))
                        {
                            deleteEntry(e);
                            return true;
                        }
                    }
                    return false;
                }

                public void clear()
                {
                    TreeMap.this.clear();
                }
            };
        }
        return values_;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Map.Entry<K, V>> entrySet()
    {
        if (entrySet_ == null)
        {
            entrySet_ = new AbstractSet<Map.Entry<K, V>>() {
                public Iterator<Map.Entry<K, V>> iterator()
                {
                    return new EntryIterator();
                }

                @SuppressWarnings("unchecked")
                public boolean contains(Object o)
                {
                    if (!(o instanceof Map.Entry))
                    {
                        return false;
                    }
                    Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
                    V value = entry.getValue();
                    Entry<K, V> p = getEntry(entry.getKey());
                    return p != null && valEquals(p.getValue(), value);
                }

                @SuppressWarnings("unchecked")
                public boolean remove(Object o)
                {
                    if (!(o instanceof Map.Entry))
                    {
                        return false;
                    }
                    Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
                    V value = entry.getValue();
                    Entry<K, V> p = getEntry(entry.getKey());
                    if (p != null && valEquals(p.getValue(), value))
                    {
                        deleteEntry(p);
                        return true;
                    }
                    return false;
                }

                public int size()
                {
                    return TreeMap.this.size();
                }

                public void clear()
                {
                    TreeMap.this.clear();
                }
            };
        }
        return entrySet_;
    }

    /**
     * {@inheritDoc}
     */
    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
        return new SubMap(fromKey, toKey);
    }

    /**
     * {@inheritDoc}
     */
    public SortedMap<K, V> headMap(K toKey)
    {
        return new SubMap(toKey, true);
    }

    /**
     * {@inheritDoc}
     */
    public SortedMap<K, V> tailMap(K fromKey)
    {
        return new SubMap(fromKey, false);
    }

    /**
     * サブのマップクラス
     * @author acroquest
     */
    private class SubMap extends AbstractMap<K, V> implements SortedMap<K, V>, java.io.Serializable
    {
        private static final long              serialVersionUID = -6520786458950516097L;

        private boolean                        fromStart_       = false;

        private boolean                        toEnd_           = false;

        private K                              fromKey_;

        private K                              toKey_;

        private transient Set<Map.Entry<K, V>> subMapEntrySet_  = new EntrySetView();

        public SubMap(K fromKey, K toKey)
        {
            if (compare(fromKey, toKey) > 0)
            {
                throw new IllegalArgumentException("fromKey > toKey");
            }
            this.fromKey_ = fromKey;
            this.toKey_ = toKey;
        }

        SubMap(K key, boolean headMap)
        {
            compare(key, key);

            if (headMap)
            {
                fromStart_ = true;
                toKey_ = key;
            }
            else
            {
                toEnd_ = true;
                fromKey_ = key;
            }
        }

        SubMap(boolean fromStart, K fromKey, boolean toEnd, K toKey)
        {
            this.fromStart_ = fromStart;
            this.fromKey_ = fromKey;
            this.toEnd_ = toEnd;
            this.toKey_ = toKey;
        }

        public boolean isEmpty()
        {
            return subMapEntrySet_.isEmpty();
        }

        @SuppressWarnings("unchecked")
        public boolean containsKey(Object key)
        {
            return inRange((K)key) && TreeMap.this.containsKey(key);
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public V get(Object key)
        {
            if (!inRange((K)key))
            {
                return null;
            }
            return TreeMap.this.get(key);
        }

        /**
         * {@inheritDoc}
         */
        public V put(K key, V value)
        {
            if (!inRange(key))
            {
                throw new IllegalArgumentException("key out of range");
            }
            return TreeMap.this.put(key, value);
        }

        /**
         * {@inheritDoc}
         */
        public Comparator<? super K> comparator()
        {
            return comparator_;
        }

        /**
         * {@inheritDoc}
         */
        public K firstKey()
        {
            TreeMap.Entry<K, V> e = fromStart_ ? firstEntry() : getCeilEntry(fromKey_);
            K first = key(e);
            if (!toEnd_ && compare(first, toKey_) >= 0)
            {
                throw (new NoSuchElementException());
            }
            return first;
        }

        /**
         * {@inheritDoc}
         */
        public K lastKey()
        {
            TreeMap.Entry<K, V> e = toEnd_ ? lastEntry() : getPrecedingEntry(toKey_);
            K last = key(e);
            if (!fromStart_ && compare(last, fromKey_) < 0)
            {
                throw (new NoSuchElementException());
            }
            return last;
        }

        public Set<Map.Entry<K, V>> entrySet()
        {
            return subMapEntrySet_;
        }

        /**
         * エントリセットのViewクラス
         * @author 
         *
         */
        private class EntrySetView extends AbstractSet<Map.Entry<K, V>>
        {
            private transient int entrySetViewSize_ = -1;

            private transient int sizeModCount_;

            public int size()
            {
                if (entrySetViewSize_ == -1 || sizeModCount_ != TreeMap.this.modCount_)
                {
                    entrySetViewSize_ = 0;
                    sizeModCount_ = TreeMap.this.modCount_;
                    Iterator<?> i = iterator();
                    while (i.hasNext())
                    {
                        entrySetViewSize_++;
                        i.next();
                    }
                }
                return entrySetViewSize_;
            }

            public boolean isEmpty()
            {
                return !iterator().hasNext();
            }

            @SuppressWarnings("unchecked")
            public boolean contains(Object o)
            {
                if (!(o instanceof Map.Entry))
                {
                    return false;
                }
                Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
                K key = entry.getKey();
                if (!inRange(key))
                {
                    return false;
                }
                TreeMap.Entry<K, V> node = getEntry(key);
                return node != null && valEquals(node.getValue(), entry.getValue());
            }

            @SuppressWarnings("unchecked")
            public boolean remove(Object o)
            {
                if (!(o instanceof Map.Entry))
                {
                    return false;
                }
                Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
                K key = entry.getKey();
                if (!inRange(key))
                {
                    return false;
                }
                TreeMap.Entry<K, V> node = getEntry(key);
                if (node != null && valEquals(node.getValue(), entry.getValue()))
                {
                    deleteEntry(node);
                    return true;
                }
                return false;
            }

            public Iterator<Map.Entry<K, V>> iterator()
            {
                return new SubMapEntryIterator(
                                               (fromStart_ ? firstEntry() : getCeilEntry(fromKey_)),
                                               (toEnd_ ? null : getCeilEntry(toKey_)));
            }
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey)
        {
            if (!inRange2(fromKey))
            {
                throw new IllegalArgumentException("fromKey out of range");
            }
            if (!inRange2(toKey))
            {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new SubMap(fromKey, toKey);
        }

        public SortedMap<K, V> headMap(K toKey)
        {
            if (!inRange2(toKey))
            {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new SubMap(fromStart_, fromKey_, false, toKey);
        }

        public SortedMap<K, V> tailMap(K fromKey)
        {
            if (!inRange2(fromKey))
            {
                throw new IllegalArgumentException("fromKey out of range");
            }
            return new SubMap(false, fromKey, toEnd_, toKey_);
        }

        private boolean inRange(K key)
        {
            return (fromStart_ || compare(key, fromKey_) >= 0)
                    && (toEnd_ || compare(key, toKey_) < 0);
        }

        private boolean inRange2(K key)
        {
            return (fromStart_ || compare(key, fromKey_) >= 0)
                    && (toEnd_ || compare(key, toKey_) <= 0);
        }
    }

    /**
     * エントリのイテレータクラス
     * @author
     *
     * @param <T>
     */
    private abstract class PrivateEntryIterator<T> implements Iterator<T>
    {
        private int         expectedModCount_ = TreeMap.this.modCount_;

        private Entry<K, V> lastReturned_     = null;

        Entry<K, V>         next_;

        PrivateEntryIterator()
        {
            next_ = firstEntry();
        }

        PrivateEntryIterator(Entry<K, V> first)
        {
            next_ = first;
        }

        public boolean hasNext()
        {
            return next_ != null;
        }

        final Entry<K, V> nextEntry()
        {
            if (next_ == null)
            {
                throw new NoSuchElementException();
            }
            if (modCount_ != expectedModCount_)
            {
                throw new ConcurrentModificationException();
            }
            lastReturned_ = next_;
            next_ = successor(next_);
            return lastReturned_;
        }

        public void remove()
        {
            if (lastReturned_ == null)
            {
                throw new IllegalStateException();
            }
            if (modCount_ != expectedModCount_)
            {
                throw new ConcurrentModificationException();
            }
            if (lastReturned_.left_ != null && lastReturned_.right_ != null)
            {
                next_ = lastReturned_;
            }
            deleteEntry(lastReturned_);
            expectedModCount_++;
            lastReturned_ = null;
        }
    }

    /**
     * エントリのイテレータクラス
     * @author
     *
     */
    private class EntryIterator extends PrivateEntryIterator<Map.Entry<K, V>>
    {
        public Map.Entry<K, V> next()
        {
            return nextEntry();
        }
    }

    /**
     * キーのイテレータクラス
     * @author
     *
     */
    private class KeyIterator extends PrivateEntryIterator<K>
    {
        public K next()
        {
            return nextEntry().key_;
        }
    }

    /**
     * 値のイテレータクラス
     * @author
     *
     */
    private class ValueIterator extends PrivateEntryIterator<V>
    {
        public V next()
        {
            return nextEntry().value_;
        }
    }

    /**
     * サブマップのエントリのイテレータクラス
     * @author
     *
     */
    private class SubMapEntryIterator extends PrivateEntryIterator<Map.Entry<K, V>>
    {
        private final K firstExcludedKey_;

        SubMapEntryIterator(Entry<K, V> first, Entry<K, V> firstExcluded)
        {
            super(first);
            firstExcludedKey_ = (firstExcluded == null ? null : firstExcluded.key_);
        }

        public boolean hasNext()
        {
            return next_ != null && next_.key_ != firstExcludedKey_;
        }

        public Map.Entry<K, V> next()
        {
            if (next_ == null || next_.key_ == firstExcludedKey_)
            {
                throw new NoSuchElementException();
            }
            return nextEntry();
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(K k1, K k2)
    {
        return (comparator_ == null ? ((Comparable<K>)k1).compareTo(k2)
                : comparator_.compare((K)k1, (K)k2));
    }

    private static boolean valEquals(Object o1, Object o2)
    {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    private static final boolean RED   = false;

    private static final boolean BLACK = true;

    /**
     * エントリクラス
     * @author
     *
     */
    static class Entry<K, V> implements Map.Entry<K, V>
    {
        /** キー */
        K           key_;

        /** 値 */
        V           value_;

        /** 前のエントリ */
        Entry<K, V> left_  = null;

        /** 後のエントリ */
        Entry<K, V> right_ = null;

        /** 親のエントリ */
        Entry<K, V> parent_;

        /** 色 */
        boolean     color_ = BLACK;

        /**
         * コンストラクタ
         * @param key キー
         * @param value 値
         * @param parent 親のエントリ
         */
        public Entry(K key, V value, Entry<K, V> parent)
        {
            this.key_ = key;
            this.value_ = value;
            this.parent_ = parent;
        }

        /**
         * {@inheritDoc}
         */
        public K getKey()
        {
            return key_;
        }

        /**
         * {@inheritDoc}
         */
        public V getValue()
        {
            return value_;
        }

        /**
         * {@inheritDoc}
         */
        public V setValue(V value)
        {
            V oldValue = this.value_;
            this.value_ = value;
            return oldValue;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public boolean equals(Object o)
        {
            if (!(o instanceof Map.Entry))
            {
                return false;
            }
            Map.Entry<K, V> e = (Map.Entry<K, V>)o;

            return valEquals(key_, e.getKey()) && valEquals(value_, e.getValue());
        }

        /**
         * {@inheritDoc}
         */
        public int hashCode()
        {
            int keyHash = (key_ == null ? 0 : key_.hashCode());
            int valueHash = (value_ == null ? 0 : value_.hashCode());
            return keyHash ^ valueHash;
        }

        /**
         * {@inheritDoc}
         */
        public String toString()
        {
            return key_ + "=" + value_;
        }
    }

    private Entry<K, V> firstEntry()
    {
        Entry<K, V> p = root_;
        if (p != null)
        {
            while (p.left_ != null)
            {
                p = p.left_;
            }
        }
        return p;
    }

    private Entry<K, V> lastEntry()
    {
        Entry<K, V> p = root_;
        if (p != null)
        {
            while (p.right_ != null)
            {
                p = p.right_;
            }
        }
        return p;
    }

    private Entry<K, V> successor(Entry<K, V> t)
    {
        if (t == null)
        {
            return null;
        }
        else if (t.right_ != null)
        {
            Entry<K, V> p = t.right_;
            while (p.left_ != null)
            {
                p = p.left_;
            }
            return p;
        }
        else
        {
            Entry<K, V> p = t.parent_;
            Entry<K, V> ch = t;
            while (p != null && ch == p.right_)
            {
                ch = p;
                p = p.parent_;
            }
            return p;
        }
    }

    private static <K, V> boolean colorOf(Entry<K, V> p)
    {
        return (p == null ? BLACK : p.color_);
    }

    private static <K, V> Entry<K, V> parentOf(Entry<K, V> p)
    {
        return (p == null ? null : p.parent_);
    }

    private static <K, V> void setColor(Entry<K, V> p, boolean c)
    {
        if (p != null)
        {
            p.color_ = c;
        }
    }

    private static <K, V> Entry<K, V> leftOf(Entry<K, V> p)
    {
        return (p == null) ? null : p.left_;
    }

    private static <K, V> Entry<K, V> rightOf(Entry<K, V> p)
    {
        return (p == null) ? null : p.right_;
    }

    private void rotateLeft(Entry<K, V> p)
    {
        Entry<K, V> r = p.right_;
        p.right_ = r.left_;
        if (r.left_ != null)
        {
            r.left_.parent_ = p;
        }
        r.parent_ = p.parent_;
        if (p.parent_ == null)
        {
            root_ = r;
        }
        else if (p.parent_.left_ == p)
        {
            p.parent_.left_ = r;
        }
        else
        {
            p.parent_.right_ = r;
        }
        r.left_ = p;
        p.parent_ = r;
    }

    private void rotateRight(Entry<K, V> p)
    {
        Entry<K, V> l = p.left_;
        p.left_ = l.right_;
        if (l.right_ != null)
        {
            l.right_.parent_ = p;
        }
        l.parent_ = p.parent_;
        if (p.parent_ == null)
        {
            root_ = l;
        }
        else if (p.parent_.right_ == p)
        {
            p.parent_.right_ = l;
        }
        else
        {
            p.parent_.left_ = l;
        }
        l.right_ = p;
        p.parent_ = l;
    }

    private void fixAfterInsertion(Entry<K, V> x)
    {
        x.color_ = RED;

        while (x != null && x != root_ && x.parent_.color_ == RED)
        {
            if (parentOf(x) == leftOf(parentOf(parentOf(x))))
            {
                Entry<K, V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED)
                {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                }
                else
                {
                    if (x == rightOf(parentOf(x)))
                    {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    if (parentOf(parentOf(x)) != null)
                    {
                        rotateRight(parentOf(parentOf(x)));
                    }
                }
            }
            else
            {
                Entry<K, V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED)
                {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                }
                else
                {
                    if (x == leftOf(parentOf(x)))
                    {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    if (parentOf(parentOf(x)) != null)
                    {
                        rotateLeft(parentOf(parentOf(x)));
                    }
                }
            }
        }
        root_.color_ = BLACK;
    }

    private void deleteEntry(Entry<K, V> p)
    {
        decrementSize();

        if (p.left_ != null && p.right_ != null)
        {
            Entry<K, V> s = successor(p);
            p.key_ = s.key_;
            p.value_ = s.value_;
            p = s;
        }

        Entry<K, V> replacement = (p.left_ != null ? p.left_ : p.right_);

        if (replacement != null)
        {

            replacement.parent_ = p.parent_;
            if (p.parent_ == null)
            {
                root_ = replacement;
            }
            else if (p == p.parent_.left_)
            {
                p.parent_.left_ = replacement;
            }
            else
            {
                p.parent_.right_ = replacement;
            }

            p.left_ = null;
            p.right_ = null;
            p.parent_ = null;

            if (p.color_ == BLACK)
            {
                fixAfterDeletion(replacement);
            }
        }
        else if (p.parent_ == null)
        {
            root_ = null;
        }
        else
        {
            if (p.color_ == BLACK)
            {
                fixAfterDeletion(p);
            }

            if (p.parent_ != null)
            {
                if (p == p.parent_.left_)
                {
                    p.parent_.left_ = null;
                }
                else if (p == p.parent_.right_)
                {
                    p.parent_.right_ = null;
                }
                p.parent_ = null;
            }
        }
    }

    private void fixAfterDeletion(Entry<K, V> x)
    {
        while (x != root_ && colorOf(x) == BLACK)
        {
            if (x == leftOf(parentOf(x)))
            {
                Entry<K, V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED)
                {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK)
                {
                    setColor(sib, RED);
                    x = parentOf(x);
                }
                else
                {
                    if (colorOf(rightOf(sib)) == BLACK)
                    {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root_;
                }
            }
            else
            {
                Entry<K, V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED)
                {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK)
                {
                    setColor(sib, RED);
                    x = parentOf(x);
                }
                else
                {
                    if (colorOf(leftOf(sib)) == BLACK)
                    {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root_;
                }
            }
        }

        setColor(x, BLACK);
    }

    /**
     * ツリーセットを読込みます。
     * @param size サイズ
     * @param s 入出力ストリーム
     * @param defaultVal デフォルト値
     * @throws java.io.IOException 入出力例外
     * @throws ClassNotFoundException 該当クラスがないときの例外
     */
    void readTreeSet(int size, java.io.ObjectInputStream s, V defaultVal)
        throws java.io.IOException,
            ClassNotFoundException
    {
        buildFromSorted(size, null, s, defaultVal);
    }

    /**
     * セットをツリーリストに追加します。
     * @param set セット
     * @param defaultVal デフォルト値
     */
    void addAllForTreeSet(SortedSet<Map.Entry<K, V>> set, V defaultVal)
    {
        try
        {
            buildFromSorted(set.size(), set.iterator(), null, defaultVal);
        }
        catch (java.io.IOException cannotHappen)
        {
        }
        catch (ClassNotFoundException cannotHappen)
        {
        }
    }

    private void buildFromSorted(int size, Iterator<?> it, java.io.ObjectInputStream str,
            V defaultVal)
        throws java.io.IOException,
            ClassNotFoundException
    {
        this.size_ = size;
        root_ = buildFromSorted(0, 0, size - 1, computeRedLevel(size), it, str, defaultVal);
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> buildFromSorted(int level, int lo, int hi, int redLevel, Iterator<?> it,
            java.io.ObjectInputStream str, V defaultVal)
        throws java.io.IOException,
            ClassNotFoundException
    {

        if (hi < lo)
        {
            return null;
        }

        int mid = (lo + hi) / 2;

        Entry<K, V> left = null;
        if (lo < mid)
        {
            left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
        }

        K key;
        V value;
        if (it != null)
        {
            if (defaultVal == null)
            {
                Map.Entry<K, V> entry = (Map.Entry<K, V>)it.next();
                key = entry.getKey();
                value = entry.getValue();
            }
            else
            {
                key = (K)it.next();
                value = defaultVal;
            }
        }
        else
        {
            key = (K)str.readObject();
            value = (defaultVal != null ? defaultVal : (V)str.readObject());
        }

        Entry<K, V> middle = new Entry<K, V>(key, value, null);

        if (level == redLevel)
        {
            middle.color_ = RED;
        }

        if (left != null)
        {
            middle.left_ = left;
            left.parent_ = middle;
        }

        if (mid < hi)
        {
            Entry<K, V> right =
                                buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str,
                                                defaultVal);
            middle.right_ = right;
            right.parent_ = middle;
        }

        return middle;
    }

    private static int computeRedLevel(int sz)
    {
        int level = 0;
        for (int m = sz - 1; m >= 0; m = m / 2 - 1)
        {
            level++;
        }
        return level;
    }

    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {
        s.defaultWriteObject();

        s.writeInt(size_);

        for (Iterator<Map.Entry<K, V>> i = entrySet().iterator(); i.hasNext();)
        {
            Map.Entry<K, V> e = i.next();
            s.writeObject(e.getKey());
            s.writeObject(e.getValue());
        }
    }

    private void readObject(final java.io.ObjectInputStream s)
        throws java.io.IOException,
            ClassNotFoundException
    {
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        buildFromSorted(size, null, s, null);
    }
}
