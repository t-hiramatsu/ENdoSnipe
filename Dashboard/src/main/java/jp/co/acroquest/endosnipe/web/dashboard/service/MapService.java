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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.MessageUtil;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.constants.ResponseConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dao.MapInfoDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.MapInfo;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Map用サービスクラス。
 *
 * @author fujii
 */
@Service
public class MapService
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /**
     * マップ情報Dao
     */
    @Autowired
    protected MapInfoDao mapInfoDao_;

    /**
     * コンストラクタ
     */
    public MapService()
    {

    }

    /**
     * 全てのマップデータを返す。
     *
     * @return マップデータ
     */
    public List<Map<String, String>> getAllMap()
    {
        List<MapInfo> mapList = null;
        try
        {
            mapList = mapInfoDao_.selectAll();
        }
        catch (PersistenceException pe)
        {
            Throwable cause = pe.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION);
            return new ArrayList<Map<String, String>>();
        }

        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (MapInfo mapInfo : mapList)
        {
            Map<String, String> dataMap = this.convertDataMap(mapInfo);
            resultList.add(dataMap);
        }
        return resultList;
    }

    /**
     * マップを登録する。
     * @param mapInfo 登録するマップ情報
     * @return 登録結果電文
     */
    public ResponseDto insert(final MapInfo mapInfo)
    {
        ResponseDto responseDto = new ResponseDto();
        if (getByName(mapInfo.name).size() > 0)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0160", new Object[] {});
            responseDto.setMessage(errorMessage);
            responseDto.setResult(ResponseConstants.RESULT_FAIL);
            return responseDto;
        }

        // 最終更新日時を設定
        mapInfo.lastUpdate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        int count = 0;
        try
        {
            count = mapInfoDao_.insert(mapInfo);
            if (count > 0)
            {
                responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
            }
            else
            {
                String errorMessage = MessageUtil.getMessage("WEWD0161", new Object[] {});
                responseDto.setResult(ResponseConstants.RESULT_FAIL);
                responseDto.setMessage(errorMessage);
            }

            return responseDto;
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            String errorMessage = MessageUtil.getMessage("WEWD0160", new Object[] {});
            responseDto.setMessage(errorMessage);
            responseDto.setResult(ResponseConstants.RESULT_FAIL);
            return responseDto;
        }
    }

    /**
     * マップを更新する。
     *
     * @param mapInfo マップ情報
     * @return 更新結果電文
     */
    public ResponseDto update(final MapInfo mapInfo)
    {
        // 最終更新日時を設定
        mapInfo.lastUpdate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        int count = 0;
        ResponseDto responseDto = new ResponseDto();
        try
        {
            count = mapInfoDao_.update(mapInfo);
            if (count > 0)
            {
                responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
            }
            else
            {
                String errorMessage = MessageUtil.getMessage("WEWD0162", new Object[] {});
                responseDto.setResult(ResponseConstants.RESULT_FAIL);
                responseDto.setMessage(errorMessage);
            }

            return responseDto;
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }

            String errorMessage = MessageUtil.getMessage("WEWD0163", new Object[] {});
            responseDto.setMessage(errorMessage);
            responseDto.setResult(ResponseConstants.RESULT_FAIL);
            return responseDto;
        }
    }

    /**
     * マップを取得する。
     * @param mapId Target mapId
     * @return 取得結果
     */
    public ResponseDto getById(final long mapId)
    {
        ResponseDto responseDto = new ResponseDto();
        try
        {
            MapInfo mapInfo = mapInfoDao_.selectById(mapId);
            if (mapInfo == null)
            {
                String errorMessage = MessageUtil.getMessage("WEWD0164", new Object[] {});
                responseDto.setMessage(errorMessage);
                responseDto.setResult(ResponseConstants.RESULT_FAIL);
                return responseDto;
            }

            Map<String, String> convertData = this.convertDataMap(mapInfo);
            responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
            responseDto.setData(convertData);

            return responseDto;
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }

            String errorMessage = MessageUtil.getMessage("WEWD0165", new Object[] {});
            responseDto.setMessage(errorMessage);
            responseDto.setResult(ResponseConstants.RESULT_FAIL);
            return responseDto;
        }
    }

    /**
     * マップを取得する。
     * @param name マップ名
     * @return 取得結果
     */
    public List<MapInfo> getByName(final String name)
    {
        try
        {
            return mapInfoDao_.selectByName(name);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }
            return new ArrayList<MapInfo>();
        }
    }

    /**
     * マップを削除する。
     * @param mapId マップID
     * @return 削除結果
     */
    public ResponseDto removeMapById(final long mapId)
    {
        int count = 0;
        ResponseDto responseDto = new ResponseDto();
        try
        {
            count = mapInfoDao_.deleteById(mapId);
            if (count == 0)
            {
                String errorMessage = MessageUtil.getMessage("WEWD0166", new Object[] {});
                responseDto.setMessage(errorMessage);
                responseDto.setResult(ResponseConstants.RESULT_FAIL);
                return responseDto;
            }

            responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
            return responseDto;
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }

            String errorMessage = MessageUtil.getMessage("WEWD0167", new Object[] {});
            responseDto.setMessage(errorMessage);
            responseDto.setResult(ResponseConstants.RESULT_FAIL);
            return responseDto;
        }
    }

    /**
     * マップ情報をMap形式に変換する。
     * @param mapInfo マップ情報
     * @return Map形式のマップ情報
     */
    private Map<String, String> convertDataMap(final MapInfo mapInfo)
    {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("id", String.valueOf(mapInfo.mapId));
        dataMap.put("parentTreeId", "");
        dataMap.put("data", mapInfo.name);
        dataMap.put("treeId", String.valueOf(mapInfo.mapId));
        dataMap.put("mapData", mapInfo.data);
        return dataMap;
    }
}
