package jp.co.acroquest.endosnipe.javelin.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRUキャッシュ。同期化されてはいない。
 * 
 * @author eriguchi
 *
 * @param <K> キー型
 * @param <V> 値の型
 */
public class LruCache<K, V> extends LinkedHashMap<K, V>
{
    /** 初期容量 */
    private static final int INITIAL_CAPACITY = 16;

    /** 負荷係数 */
    private static final float LOAD_FACTOR = 0.75f;

    /** シリアルバージョンID。 */
    private static final long serialVersionUID = -5811292825186851249L;

    /** 最大エントリ数。 */
    private final int maxEntries_;

    /**
     * 最大エントリ数を指定して生成する。
     * 
     * @param maxEntries 最大エントリ数。
     */
    public LruCache(final int maxEntries)
    {
        super(INITIAL_CAPACITY, LOAD_FACTOR, true);
        this.maxEntries_ = maxEntries;
    }

    /**
     * 最も古いエントリを削除するかどうかを返す。
     * 最大エントリ数を超えている場合に削除する。
     * 
     * @param eldest 最も古いエントリ。
     * @return 最もフルエントリを削除するかどうか。
     */
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest)
    {
        return super.size() > this.maxEntries_;
    }
}