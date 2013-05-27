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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * LinkedHashMapクラス
 * 
 * @author acroquest
 * @param <K> キー
 * @param <V> 値
 */
public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V>
{
    /** シリアルバージョンID */
    private static final long serialVersionUID = 8219104330698553529L;

    /** ヘッダー */
    private transient Entry<K, V> header_;

    /** アクセス順 */
    private final boolean accessOrder_;

    /**
     * コンストラクタ
     * @param initialCapacity 初期容量
     * @param loadFactor 使用率
     */
    public LinkedHashMap(int initialCapacity, float loadFactor)
    {
        super(initialCapacity, loadFactor);
        accessOrder_ = false;
    }

    /**
     * コンストラクタ
     * @param initialCapacity 初期容量
     */
    public LinkedHashMap(int initialCapacity)
    {
        super(initialCapacity);
        accessOrder_ = false;
    }

    /**
     * コンストラクタ
     */
    public LinkedHashMap()
    {
        super();
        accessOrder_ = false;
    }

    /**
     * コンストラクタ
     * @param map マップ
     */
    public LinkedHashMap(Map<? extends K, ? extends V> map)
    {
        super(map);
        accessOrder_ = false;
    }

    /**
     * コンストラクタ
     * @param initialCapacity 初期容量
     * @param loadFactor 使用率
     * @param accessOrder アクセス順
     */
    public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
    {
        super(initialCapacity, loadFactor);
        this.accessOrder_ = accessOrder;
    }

    /**
     * {@inheritDoc}
     */
    void init()
    {
        header_ = new Entry<K, V>(-1, null, null, null);
        header_.beforeEntry_ = header_;
        header_.afterEntry_ = header_;
    }

    /**
     * {@inheritDoc}
     */
    void transfer(HashMap.Entry<K, V>[] newTable)
    {
        int newCapacity = newTable.length;
        for (Entry<K, V> entry = header_.afterEntry_; entry != header_; entry = entry.afterEntry_)
        {
            int index = indexFor(entry.hash_, newCapacity);
            entry.next_ = newTable[index];
            newTable[index] = entry;
        }
    }

    /**
     * {@inheritDoc}
     */
   public boolean containsValue(Object value)
    {
        if (value == null)
        {
            for (Entry<K, V> e = header_.afterEntry_; e != header_; e = e.afterEntry_)
            {
                if (e.value_ == null)
                {
                    return true;
                }
            }
        }
        else
        {
            for (Entry<K, V> e = header_.afterEntry_; e != header_; e = e.afterEntry_)
            {
                if (value.equals(e.value_))
                {
                    return true;
                }
            }
        }
        return false;
    }

   /**
    * {@inheritDoc}
    */
    public V get(Object key)
    {
        Entry<K, V> entry = (Entry<K, V>)getEntry(key);
        if (entry == null)
        {
            return null;
        }
        entry.recordAccess(this);
        return entry.value_;
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        super.clear();
        header_.beforeEntry_ = header_;
        header_.afterEntry_ = header_;
    }

    /**
     * エントリクラス
     * 
     * @param <K> キー
     * @param <V> 値
     */
    private static class Entry<K, V> extends HashMap.Entry<K, V>
    {
        /** 前のエントリ */
        Entry<K, V> beforeEntry_;
        
        /** 後のエントリ */
        Entry<K, V> afterEntry_;

        /**
         * コンストラクタ
         * @param hash ハッシュ
         * @param key キー
         * @param value 値
         * @param next 次のエントリ
         */
        public Entry(int hash, K key, V value, HashMap.Entry<K, V> next)
        {
            super(hash, key, value, next);
        }

        /**
         * エントリを削除する。
         */
        private void remove()
        {
            beforeEntry_.afterEntry_ = afterEntry_;
            afterEntry_.beforeEntry_ = beforeEntry_;
        }

        /**
         * エントリを前の位置に移動する。
         * @param existingEntry 前の位置にあるエントリ
         */
        private void addBefore(Entry<K, V> existingEntry)
        {
            afterEntry_ = existingEntry;
            beforeEntry_ = existingEntry.beforeEntry_;
            beforeEntry_.afterEntry_ = this;
            afterEntry_.beforeEntry_ = this;
        }

        /**
         * アクセスを記録します。
         * @param m マップ
         */
        void recordAccess(HashMap<K, V> m)
        {
            LinkedHashMap<K, V> lm = (LinkedHashMap<K, V>)m;
            if (lm.accessOrder_)
            {
                lm.modCount_++;
                remove();
                addBefore(lm.header_);
            }
        }
    }

    /**
     * LinkedHashIteratorクラス
     *
     * @param <T>
     */
    private abstract class LinkedHashIterator<T> implements Iterator<T>
    {
        Entry<K, V> nextEntry_ = header_.afterEntry_;

        Entry<K, V> lastReturned_ = null;

        int expectedModCount_ = modCount_;

        public boolean hasNext()
        {
            return nextEntry_ != header_;
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

            LinkedHashMap.this.remove(lastReturned_.key_);
            lastReturned_ = null;
            expectedModCount_ = modCount_;
        }

        Entry<K, V> nextEntry()
        {
            if (modCount_ != expectedModCount_)
            {
                throw new ConcurrentModificationException();
            }
            if (nextEntry_ == header_)
            {
                throw new NoSuchElementException();
            }

            Entry<K, V> entry = lastReturned_;
            lastReturned_ = nextEntry_;
            nextEntry_ = entry.afterEntry_;
            
            return entry;
        }
    }

    /**
     * キーイテレータクラス
     */
    private class KeyIterator extends LinkedHashIterator<K>
    {
        public K next()
        {
            return nextEntry().getKey();
        }
    }

    /**
     * 値のイテレータクラス
     */
    private class ValueIterator extends LinkedHashIterator<V>
    {
        public V next()
        {
            return nextEntry().value_;
        }
    }

    /**
     * エントリーのイテレータクラス
     */
    private class EntryIterator extends LinkedHashIterator<Map.Entry<K, V>>
    {
        public Map.Entry<K, V> next()
        {
            return nextEntry();
        }
    }

    // These Overrides alter the behavior of superclass view iterator() methods
    /**
     * {@inheritDoc}
     */
    Iterator<K> newKeyIterator()
    {
        return new KeyIterator();
    }

    /**
     * {@inheritDoc}
     */
    Iterator<V> newValueIterator()
    {
        return new ValueIterator();
    }

    /**
     * {@inheritDoc}
     */
    Iterator<Map.Entry<K, V>> newEntryIterator()
    {
        return new EntryIterator();
    }

    /**
     * {@inheritDoc}
     */
    void addEntry(int hash, K key, V value, int bucketIndex)
    {
        createEntry(hash, key, value, bucketIndex);

        // Remove eldest entry if instructed, else grow capacity if appropriate
        Entry<K, V> eldest = header_.afterEntry_;
        if (removeEldestEntry(eldest))
        {
            removeEntryForKey(eldest.key_);
        }
        else
        {
            if (size_ >= threshold_)
            {
                resize(2 * table_.length);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    void createEntry(int hash, K key, V value, int bucketIndex)
    {
        HashMap.Entry<K, V> old = table_[bucketIndex];
        Entry<K, V> entry = new Entry<K, V>(hash, key, value, old);
        table_[bucketIndex] = entry;
        entry.addBefore(header_);
        size_++;
    }

    /**
     * 一番古いエントリを削除します。
     * @param eldest 一番古いエントリのキー
     * @return false
     */
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
        return false;
    }
}
