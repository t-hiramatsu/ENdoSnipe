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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Mapの抽象クラス
 * @author acroquest
 *
 * @param <K> キー
 * @param <V> 値
 */
public abstract class AbstractMap<K, V> implements Map<K, V>
{
    /** キーセット */
    transient volatile Set<K>        keySet_ = null;

    /** 値の一覧 */
    transient volatile Collection<V> values_ = null;

    /**
     * コンストラクタ
     */
    protected AbstractMap()
    {
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return entrySet().size();
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
    public boolean containsValue(Object value)
    {
        Iterator<Entry<K, V>> i = entrySet().iterator();
        if (value == null)
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (e.getValue() == null)
                {
                    return true;
                }
            }
        }
        else
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (value.equals(e.getValue()))
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
    public boolean containsKey(Object key)
    {
        Iterator<Map.Entry<K, V>> i = entrySet().iterator();
        if (key == null)
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (e.getKey() == null)
                {
                    return true;
                }
            }
        }
        else
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
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
        Iterator<Entry<K, V>> i = entrySet().iterator();
        if (key == null)
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (e.getKey() == null)
                {
                    return e.getValue();
                }
            }
        }
        else
        {
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
                {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public V put(K key, V value)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public V remove(Object key)
    {
        Iterator<Entry<K, V>> i = entrySet().iterator();
        Entry<K, V> correctEntry = null;
        if (key == null)
        {
            while (correctEntry == null && i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (e.getKey() == null)
                {
                    correctEntry = e;
                }
            }
        }
        else
        {
            while (correctEntry == null && i.hasNext())
            {
                Entry<K, V> e = i.next();
                if (key.equals(e.getKey()))
                {
                    correctEntry = e;
                }
            }
        }

        V oldValue = null;
        if (correctEntry != null)
        {
            oldValue = correctEntry.getValue();
            i.remove();
        }
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(Map<? extends K, ? extends V> t)
    {
        Iterator<? extends Entry<? extends K, ? extends V>> i = t.entrySet().iterator();
        while (i.hasNext())
        {
            Entry<? extends K, ? extends V> e = i.next();
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        entrySet().clear();
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
                    return new Iterator<K>() {

                        private Iterator<Entry<K, V>> iterator_ = entrySet().iterator();

                        public boolean hasNext()
                        {
                            return iterator_.hasNext();
                        }

                        public K next()
                        {
                            return iterator_.next().getKey();
                        }

                        public void remove()
                        {
                            iterator_.remove();
                        }
                    };
                }

                public int size()
                {
                    return AbstractMap.this.size();
                }

                public boolean contains(Object k)
                {
                    return AbstractMap.this.containsKey(k);
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
                    return new Iterator<V>() {
                        private Iterator<Entry<K, V>> iterator_ = entrySet().iterator();

                        public boolean hasNext()
                        {
                            return iterator_.hasNext();
                        }

                        public V next()
                        {
                            return iterator_.next().getValue();
                        }

                        public void remove()
                        {
                            iterator_.remove();
                        }
                    };
                }

                public int size()
                {
                    return AbstractMap.this.size();
                }

                public boolean contains(Object v)
                {
                    return AbstractMap.this.containsValue(v);
                }
            };
        }
        return values_;
    }

    /**
     * エントリーセットを取得します。
     * @return エントリーセット
     */
    public abstract Set<Entry<K, V>> entrySet();

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

        if (!(o instanceof Map))
        {
            return false;
        }
        Map<K, V> t = (Map<K, V>)o;
        if (t.size() != size())
        {
            return false;
        }

        try
        {
            Iterator<Entry<K, V>> i = entrySet().iterator();
            while (i.hasNext())
            {
                Entry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null)
                {
                    if (!(t.get(key) == null && t.containsKey(key)))
                    {
                        return false;
                    }
                }
                else
                {
                    if (!value.equals(t.get(key)))
                    {
                        return false;
                    }
                }
            }
        }
        catch (ClassCastException unused)
        {
            return false;
        }
        catch (NullPointerException unused)
        {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        int h = 0;
        Iterator<Entry<K, V>> i = entrySet().iterator();
        while (i.hasNext())
        {
            h += i.next().hashCode();
        }
        return h;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("{");

        Iterator<Entry<K, V>> i = entrySet().iterator();
        boolean hasNext = i.hasNext();
        while (hasNext)
        {
            Entry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            if (key == this)
            {
                buf.append("(this Map)");
            }
            else
            {
                buf.append(key);
            }
            buf.append("=");
            if (value == this)
            {
                buf.append("(this Map)");
            }
            else
            {
                buf.append(value);
            }
            hasNext = i.hasNext();
            if (hasNext)
            {
                buf.append(", ");
            }
        }

        buf.append("}");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    protected Object clone()
        throws CloneNotSupportedException
    {
        AbstractMap<K, V> result = (AbstractMap<K, V>)super.clone();
        result.keySet_ = null;
        result.values_ = null;
        return result;
    }

    /**
     * SimpleEntryクラス
     * @author acroquest
     *
     * @param <K> キー
     * @param <V> 値
     */
    static class SimpleEntry<K, V> implements Entry<K, V>
    {
        /** キー */
        K key_;

        /** 値 */
        V value_;

        /**
         * コンストラクタ 
         * @param key キー
         * @param value 値
         */
        public SimpleEntry(K key, V value)
        {
            this.key_ = key;
            this.value_ = value;
        }

        /**
         * コンストラクタ
         * @param e エントリ
         */
        public SimpleEntry(Entry<K, V> e)
        {
            this.key_ = e.getKey();
            this.value_ = e.getValue();
        }

        /**
         * キーを取得します。
         * @return キー
         */
        public K getKey()
        {
            return key_;
        }

        /**
         * 値を取得します。
         * @return 値
         */
        public V getValue()
        {
            return value_;
        }

        /**
         * 値を設定します。
         * @param value 値
         * @return 設定前の値
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
            return eq(key_, e.getKey()) && eq(value_, e.getValue());
        }

        /**
         * {@inheritDoc}
         */
        public int hashCode()
        {
            return ((key_ == null) ? 0 : key_.hashCode())
                    ^ ((value_ == null) ? 0 : value_.hashCode());
        }

        /**
         * {@inheritDoc}
         */
        public String toString()
        {
            return key_ + "=" + value_;
        }

        private static boolean eq(Object o1, Object o2)
        {
            return (o1 == null ? o2 == null : o1.equals(o2));
        }
    }
}
