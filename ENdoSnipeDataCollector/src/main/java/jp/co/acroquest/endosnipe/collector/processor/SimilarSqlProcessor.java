/*******************************************************************************
 * ENdoSnipe 5.2 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.processor;

import static jp.co.acroquest.endosnipe.collector.LogMessageCodes.FIND_SIMILAR_SQL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import jp.co.acroquest.endosnipe.collector.JavelinDataLogger;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

import org.apache.lucene.search.spell.LevensteinDistance;

/**
 * 同一SQL判定を行い、類似したSQLがある場合には、同一SQLと見なし、処理を行うように変換します。<br />
 * 同一かどうかは、レーベンシュタイン距離が閾値以上のSQLにて判定します。
 * @author fujii
 *
 */
public class SimilarSqlProcessor
{
    /** DataCollectorの設定 */
    private DataCollectorConfig config_ = null;

    /** SQL判定用の接尾辞 */
    private static final String SQL_SUFFIX = "/process/response/jdbc/";

    /** 保持するSQLの一覧 */
    private final Set<String> sqlSet_ = new CopyOnWriteArraySet<String>();

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(JavelinDataLogger.class);

    /**
     * コンストラクタです。
     * @param config {@link DataCollectorConfig}
     */
    public SimilarSqlProcessor(final DataCollectorConfig config)
    {
        this.config_ = config;
    }

    /**
     * 類似したSQLが存在すれば、同一情報と判定して変換処理を行う。
     * @param resourceData {@link ResourceData}オブジェクト
     */
    public void convertSameSql(final ResourceData resourceData)
    {
        Map<String, MeasurementData> convertMeasurementMap = new HashMap<String, MeasurementData>();

        Map<String, MeasurementData> resourceDataMap = resourceData.getMeasurementMap();
        Iterator<Entry<String, MeasurementData>> it = resourceDataMap.entrySet().iterator();

        // 変換マップ
        Map<String, String> convertMap = new HashMap<String, String>();

        while (it.hasNext())
        {
            Entry<String, MeasurementData> measurementEntry = it.next();
            String itemName = measurementEntry.getKey();
            String sql = getSql(itemName);
            if (sql == null)
            {
                continue;
            }
            boolean hasSameSql = hasSameSql(sql);
            // 同一のSQLが存在する場合には判定処理を行わない。
            if (hasSameSql)
            {
                continue;
            }
            String similarSql = getSimilarSql(sql, convertMap);

            if (similarSql == null)
            {
                continue;
            }
            else
            {
                convertMap.put(sql, similarSql);
            }
            String newKey = createNewKey(itemName, sql, similarSql);
            MeasurementData current = measurementEntry.getValue();
            MeasurementData convertMeasurementData = current.clone();
            convertMeasurementData.itemName = newKey;
            convertMeasurementMap.put(itemName, convertMeasurementData);
            it.remove();
        }
        resourceDataMap.putAll(convertMeasurementMap);
    }

    /**
     * 系列名からSQLを取得する。
     * @param itemName 系列名
     * @return 系列名から取得したSQL
     */
    private String getSql(final String itemName)
    {
        int position = itemName.indexOf(SQL_SUFFIX);
        // SQL以外の場合は処理を行わない。
        if (position < 0)
        {
            return null;
        }
        String sqlConnection = itemName.substring(position + SQL_SUFFIX.length());

        int sqlStartPosition = sqlConnection.indexOf("/");
        if (sqlStartPosition < 0)
        {
            return null;
        }
        int sqlEndPosition = sqlConnection.lastIndexOf("/");
        if (sqlStartPosition == sqlEndPosition)
        {
            return null;
        }
        return sqlConnection.substring(sqlStartPosition + 1, sqlEndPosition);
    }

    /**
     * 同一のSQLを待つか。
     * @param sql 比較元SQL
     * @return 同一のSQLを持っている場合、trueを返す。
     */
    private boolean hasSameSql(final String sql)
    {
        return sqlSet_.contains(sql);
    }

    /**
     * 類似度の高いSQLを取得する。
     * @param sql 比較元SQL
     * @param convertMap 変換用Map
     * @return 類似度の高いSQL。類似度の高いSQLが見つからない場合はnullを返す。
     */
    private String getSimilarSql(final String sql, final Map<String, String> convertMap)
    {
        // 既に類似度の高いSQLが見つかっている場合は、前回の結果を返す。
        String similarSql = convertMap.get(sql);
        if (similarSql != null)
        {
            return similarSql;
        }
        for (String targetSql : sqlSet_)
        {
            LevensteinDistance distanceAlgorithm = new LevensteinDistance();
            float distance = distanceAlgorithm.getDistance(sql, targetSql);
            if (distance > config_.getJudgeSqlSimilarity())
            {
                LOGGER.log(FIND_SIMILAR_SQL, sql, targetSql, distance);
                similarSql = targetSql;
                break;
            }
        }
        if (similarSql == null)
        {
            sqlSet_.add(sql);
        }

        return similarSql;
    }

    /**
     * 類似したSQLで変換した後のキーを作成する。
     * @param itemName 変換前のキー
     * @param baseSql 変換前のSQL
     * @param similarSql 変換前のSQLに類似したSQL
     * @return 類似したSQLで変換した後のキー
     */
    private String
        createNewKey(final String itemName, final String baseSql, final String similarSql)
    {
        int position = itemName.indexOf(baseSql);
        return itemName.substring(0, position) + similarSql
            + itemName.substring(position + baseSql.length());
    }

}
