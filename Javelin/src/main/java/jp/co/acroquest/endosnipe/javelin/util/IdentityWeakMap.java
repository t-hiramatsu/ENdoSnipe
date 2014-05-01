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
 * �L�[�Ɏw�肵���I�u�W�F�N�g���������Ă���ԁA�G���g����ێ�����B
 * 
 * @author eriguchi
 *
 * @param <T> �L�[�B
 * @param <V> �l�B
 */
public class IdentityWeakMap<T, V>
{
    /** �N���[���A�b�v���s���Ԋu�B */
    private static final int CLEANUP_INTERVAL = 10000;

    /** ����񐔁B */
    private int              count_;

    /** �l��ێ�����Map */
    private Map<Key<T>, V>   map_;

    /**
     * �R���X�g���N�^�B
     */
    public IdentityWeakMap()
    {
        this.map_ = Collections.synchronizedMap(new HashMap<Key<T>, V>());
    }

    /**
     * �ǉ�����B
     * 
     * @param key �L�[�B
     * @param value �l�B
     * @return �l�B
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
     * �L�[���Q�Ƃ���Ȃ��Ȃ����ꍇ�ɂ́A�폜����B
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
     * �L�[�ɑΉ�����l���擾����B
     * 
     * @param key �L�[�B
     * @return �l�B
     */
    public V get(T key)
    {
        this.count_++;
        return this.map_.get(new Key<T>(key));
    }

    /**
     * �L�[��ێ����邽�߂̃N���X�B
     *
     * @param <T> �L�[�̃N���X
     */
    public static class Key<T> extends WeakReference<T>
    {
        
        /**
         * �R���X�g���N�^�B
         * 
         * @param referent �Q�ƁB
         */
        public Key(T referent)
        {
            super(referent);
        }

        /**
         * �n�b�V���R�[�h�B
         * 
         * @return �n�b�V���R�[�h�B
         */
        public int hashCode()
        {
            T referent = this.get();
            return System.identityHashCode(referent);
        }

        /**
         * �Q�Ƃ��Ă���I�u�W�F�N�g�������ł���΁A����Ƃ݂Ȃ��B
         * 
         * @param obj �ΏہB
         * @return ���ꂩ�ǂ����B
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

