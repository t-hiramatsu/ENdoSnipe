/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.javelin.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * キーに指定したオブジェクトが生存している間、エントリを保持する。
 * 
 * @author eriguchi
 *
 * @param <T> キー。
 * @param <V> 値。
 */
public class IdentityWeakMap<T, V>
{
    /** クリーンアップを行う間隔。 */
    private static final int CLEANUP_INTERVAL = 10000;

    /** 操作回数。 */
    private int              count_;

    /** 値を保持するMap */
    private Map<Key<T>, V>   map_;

    /**
     * コンストラクタ。
     */
    public IdentityWeakMap()
    {
        this.map_ = Collections.synchronizedMap(new HashMap<Key<T>, V>());
    }

    /**
     * 追加する。
     * 
     * @param key キー。
     * @param value 値。
     * @return 値。
     */
    public V put(T key, V value)
    {
        this.count_++;

        if (this.count_ > CLEANUP_INTERVAL)
        {
            cleanup();
            this.count_ = 0;
        }

        return this.map_.put(new Key<T>(key), value);
    }

    /**
     * キーが参照されなくなった場合には、削除する。
     */
    private void cleanup()
    {
        synchronized (this.map_)
        {
            Iterator<Entry<Key<T>, V>> iterator = this.map_.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<Key<T>, V> entry = iterator.next();
                T referent = entry.getKey().get();
                if (referent == null)
                {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * キーに対応する値を取得する。
     * 
     * @param key キー。
     * @return 値。
     */
    public V get(T key)
    {
        this.count_++;
        return this.map_.get(new Key<T>(key));
    }

    /**
     * キーを保持するためのクラス。
     *
     * @param <T> キーのクラス
     */
    public static class Key<T> extends WeakReference<T>
    {
        
        /**
         * コンストラクタ。
         * 
         * @param referent 参照。
         */
        public Key(T referent)
        {
            super(referent);
        }

        /**
         * ハッシュコード。
         * 
         * @return ハッシュコード。
         */
        public int hashCode()
        {
            T referent = this.get();
            return System.identityHashCode(referent);
        }

        /**
         * 参照しているオブジェクトが同じであれば、同一とみなす。
         * 
         * @param obj 対象。
         * @return 同一かどうか。
         */
        public boolean equals(Object obj)
        {
            if (obj instanceof Key == false)
            {
                return false;
            }

            Key<?> key = (Key<?>)obj;

            Object referent2 = key.get();
            Object referent1 = get();

            return referent1 == referent2;
        }
    }
}

