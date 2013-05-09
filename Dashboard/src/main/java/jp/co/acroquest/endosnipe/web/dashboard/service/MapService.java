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
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dao.MapInfoDao;
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
    protected MapInfoDao mapInfoDao;

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
            mapList = mapInfoDao.selectAll();
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
     * @return 例外が発生しないとき0
     */
    public long insert(final MapInfo mapInfo)
    {
        // 最終更新日時を設定
        mapInfo.lastUpdate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        try
        {
            int count = mapInfoDao.insert(mapInfo);
            if (count > 0)
            {
                return mapInfoDao.selectSequenceNum();
            }
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
        }
        return 0;
    }

    /**
     * マップを更新する。
     * 
     * @param mapInfo マップ情報
     */
    public void update(final MapInfo mapInfo)
    {
        // 最終更新日時を設定
        mapInfo.lastUpdate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        try
        {
            mapInfoDao.update(mapInfo);
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
        }
    }

    /**
     * マップを取得する。
     * @param mapId Target mapId
     * @return Map
     */
    public Map<String, String> getById(final long mapId)
    {
        try
        {
            MapInfo mapInfo = mapInfoDao.selectById(mapId);
            return this.convertDataMap(mapInfo);
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
            throw pEx;
        }
    }

    /**
     * マップを削除する。
     * @param mapId マップID
     * @return 例外が発生しないとき0
     */
    public int removeMapById(final long mapId)
    {
        try
        {
            return mapInfoDao.deleteById(mapId);
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
            return 0;
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
