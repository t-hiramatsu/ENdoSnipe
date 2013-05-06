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
package jp.co.acroquest.endosnipe.web.dashboard.dao;

import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.entity.MapInfo;

/**
 * {@link MapInfo} のための DAO のインターフェースです。
 * 
 * @author miyasaka
 *
 */
public interface MapInfoDao
{

    /**
     * マップ情報を全て取得する。<br />
     * 
     * マップ情報が登録されていない場合は空のリストを返却する。<br />
     * 
     * @return マップ情報のリスト
     */
    List<MapInfo> selectAll();

    /**
     * マップ情報を取得する。
     * @param mapId マップID
     * @return マップ情報
     */
    MapInfo selectById(final long mapId);

    /**
     * マップ情報を新規登録する。<br />
     * @param mapInfo マップ情報
     * @return 登録件数
     */
    int insert(final MapInfo mapInfo);

    /**
     * マップ情報を更新する。
     * @param mapInfo マップ情報
     * @return 更新件数
     */
    int update(final MapInfo mapInfo);

    /**
     * マップ情報を削除する。
     * @param mapId マップID
     * @return 削除件数
     */
    int deleteById(final long mapId);

    /**
     * すべてのマップ情報を削除する。<br />
     * @return 削除件数
     */
    int deleteAll();

    /**
     * 直前のシーケンス情報を取得する。<br />
     * @return 直前のシーケンス番号
     */
    long selectSequenceNum();
}
