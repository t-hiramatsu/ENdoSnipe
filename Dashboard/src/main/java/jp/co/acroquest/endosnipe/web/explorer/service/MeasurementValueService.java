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
package jp.co.acroquest.endosnipe.web.explorer.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.web.explorer.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.explorer.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.web.explorer.manager.DatabaseManager;

import org.springframework.stereotype.Service;

/**
 * MeasurementValueの取得用のインタフェースを提供する。
 * 
 * @author akiba
 */
@Service
public class MeasurementValueService
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(MeasurementValueService.class);

    /**
     * コンストラクタ
     */
    public MeasurementValueService()
    {

    }

    /**
     * 条件を指定してMeasurementValueのリストを取得する。
     * 
     * @param starttime  範囲開始時刻
     * @param endtime    範囲終了時刻
     * @param itemNameList 項目名のリスト
     * @return MeasurementValueのリスト。
     */
    public Map<String, List<MeasurementValueDto>> getMeasurementValueList(final Date starttime,
            final Date endtime, final List<String> itemNameList)
    {
        // TODO データベース名が固定
        // →以下のコードは、collector.propertiesからデータベース名を取得するもの(clientモード想定)
        // →DataCollectorをserverモードで動かす場合は、Database名はあらかじめAgentから
        //   通知されているものを使用する。
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        Map<String, List<MeasurementValueDto>> valueMap = null;
        try
        {
            List<jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto> queryResultList =
                    MeasurementValueDao.selectByTermAndMeasurementItemNameList(dbName, starttime,
                                                                               endtime,
                                                                               itemNameList);
            valueMap = createMeasurementValueMap(queryResultList);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION);
        }

        return valueMap;
    }

    /**
     * DataAccesesorを使ってDBから取得した計測値の情報から、Explorer表示用のMapを作成する。
     * Explorer表示用の
     * @param queryResultList DBから取得した計測値のリスト。
     * @return Explorer表示用のMap。    
     */
    private Map<String, List<MeasurementValueDto>> createMeasurementValueMap(
            final List<jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto> queryResultList)
    {
        Map<String, List<MeasurementValueDto>> valueMap =
                new HashMap<String, List<MeasurementValueDto>>();

        for (jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto queryDto : queryResultList)
        {
            String itemName = queryDto.measurementItemName;
            List<MeasurementValueDto> explorerDtoList = valueMap.get(itemName);
            if (explorerDtoList == null)
            {
                explorerDtoList = new ArrayList<MeasurementValueDto>();
                valueMap.put(itemName, explorerDtoList);
            }
            MeasurementValueDto explorerDto = new MeasurementValueDto();
            explorerDto.setMeasurementItemId(queryDto.measurementItemId);
            explorerDto.setMeasurementItemName(queryDto.measurementItemName);
            explorerDto.setMeasurementTime(queryDto.measurementTime.getTime());
            explorerDto.setMeasurementValue(queryDto.value);
            explorerDtoList.add(explorerDto);
        }
        return valueMap;
    }
}
