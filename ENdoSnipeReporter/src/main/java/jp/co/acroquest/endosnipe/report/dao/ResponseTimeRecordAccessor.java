/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.ResponseTimeRecord;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * レスポンスタイムレポート用のデータにアクセスするためのDAO。
 * DBから取得したデータの加工を行い、レポートに出力できるデータ形式に変換する。
 * 
 * @author M.Yoshida
 */
public class ResponseTimeRecordAccessor
{
    /**
     * 期間を指定し、その期間内でのレスポンスタイムレポートのデータを取得する。
     * 
     * @param database  データベース名
     * @param startTime 検索条件(開始時刻)
     * @param endTime   検索条件(終了時刻)
     * @return レスポンスタイムレポートのデータ
     * @throws SQLException データ取得時にエラーが発生した場合
     */
    public List<ResponseTimeRecord> findResponseStatisticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<ResponseTimeRecord> result = new ArrayList<ResponseTimeRecord>();

        List<ReportItemValue> minValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MIN);

        List<ReportItemValue> maxValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MAX);

        List<ReportItemValue> cntValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);

        List<ReportItemValue> aveValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);

//        for (String accessedTarget : cntMap.keySet())
//        {
//            ResponseTimeRecord resultElem = new ResponseTimeRecord();
//            resultElem.setAccessTarget(accessedTarget);
//            resultElem.setAccessCount(cntMap.get(accessedTarget).value.longValue());
//            resultElem.setAveResponseTime(aveMap.get(accessedTarget).value.longValue());
//            resultElem.setMaxResponseTime(maxMap.get(accessedTarget).value.longValue());
//            resultElem.setMinResponseTime(minMap.get(accessedTarget).value.longValue());
//
//            result.add(resultElem);
//        }

        return result;
    }

    /**
     * リストデータを、系列名称（アクセス対象メソッド／URL)をキーとするマップ形式のデータに変換する。
     * 
     * @param list リストデータ
     * @return 変換後のマップデータ
     */
    private Map<String, MeasurementValueDto> convertListToItemKeyMap(List<MeasurementValueDto> list)
    {
        Map<String, MeasurementValueDto> resultMap = new HashMap<String, MeasurementValueDto>();

        for (MeasurementValueDto elem : list)
        {
            resultMap.put(elem.measurementItemName, elem);
        }

        return resultMap;
    }

}
