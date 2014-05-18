/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * システムリソースの閾値判定用ユーティリティクラス
 * @author fujii
 *
 */
public class AlarmThresholdUtil implements LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(AlarmThresholdUtil.class);

    private AlarmThresholdUtil()
    {
        //do nothing
    }

    /**
     * ResourceData から、指定したitemName の値を取得する
     * @param resourceData データの入っているresourceData
     * @param itemName 指定するkey
     * 
     * @return 指定したitemName の値がない場合、nullを返す
     */
    public static Number getNumberFromResourceData(final ResourceData resourceData,
        final String itemName)
    {
        Map<String, MeasurementData> measurementDataMap = resourceData.getMeasurementMap();
        Set<String> clusterNameSet = getClusterNameSet(resourceData);
        try
        {
            //itemNameを正規表現としてコンパイルする
            Pattern pattern = Pattern.compile(itemName);

            Number max = 0;
            boolean matchFlag = false;

            //正規表現で一致するMeasurementDataを検索する
            for (Entry<String, MeasurementData> entry : measurementDataMap.entrySet())
            {
                String key = entry.getKey();
                Matcher matcher = pattern.matcher(key);
                if (!matcher.find())
                {
                    continue;
                }
                matchFlag = true;
                MeasurementData measurementData = entry.getValue();
                Number num = getNumberFromDetailMap(key, measurementData.getMeasurementDetailMap());
                if (max.intValue() < num.intValue())
                {
                    max = num;
                }
            }
            if (matchFlag)
            {
                //正規表現で一致するMeasurementDataが少なくとも１つあれば、最大値を返す。
                return max;
            }
            else
            {
                //正規表現で一致するMeasurementDataが0の場合はnullを返す。
                return null;
            }
        }
        catch (PatternSyntaxException e)
        {
            return null;
        }
    }

    private static Set<String> getClusterNameSet(final ResourceData resourceData)
    {
        Map<String, MeasurementData> measurementMap = resourceData.getMeasurementMap();
        Set<String> clusterNameSet = new HashSet<String>();
        for (Entry<String, MeasurementData> entry : measurementMap.entrySet())
        {
            String key = entry.getKey();
            String clusterName = key.split("/")[1];
            clusterNameSet.add(clusterName);
        }
        return clusterNameSet;
    }

    private static Number getNumberFromDetailMap(final String itemName,
        final Map<String, MeasurementDetail> measurementDetailMap)
    {
        for (Entry<String, MeasurementDetail> measurementEntry : measurementDetailMap.entrySet())
        {
            String key = measurementEntry.getKey();
            MeasurementDetail measurementDetail = measurementEntry.getValue();
            if (measurementDetail == null)
            {
                return null;
            }
            // 単一のデータ系列のときは、空文字をキーに、一つの値のみが入っており、
            // 可変データ系列はホスト名、エージェント名を除いたキーで値が格納されるため、それ以外は無視する。
            if (!"".equals(key) && !itemName.endsWith(key))
            {
                continue;
            }

            String value = measurementDetail.value;

            try
            {
                return Double.valueOf(value);
            }
            catch (NumberFormatException ex)
            {
                LOGGER.log(SYSTEM_UNKNOW_ERROR, ex.getMessage(), ex);
            }
        }
        return null;
    }
}
