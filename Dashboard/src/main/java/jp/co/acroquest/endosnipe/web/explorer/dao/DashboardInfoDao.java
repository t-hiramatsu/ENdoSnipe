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
package jp.co.acroquest.endosnipe.web.explorer.dao;

import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.entity.DashboardInfo;

/**
 * {@link DashboardInfo} のための DAO のインターフェースです。
 *
 * @author miyasaka
 *
 */
public interface DashboardInfoDao
{

    /**
     * ダッシュボード情報を全て取得する。<br />
     *
     * ダッシュボード情報が登録されていない場合は空のリストを返却する。<br />
     *
     * @return ダッシュボード情報のリスト
     */
    List<DashboardInfo> selectAll();

    /**
     * ダッシュボード情報を取得する。
     * @param dashboardId ダッシュボードID
     * @return ダッシュボード情報
     */
    DashboardInfo selectById(final long dashboardId);

    /**
     * ダッシュボード情報を取得する。
     * @param name ダッシュボード名
     * @return ダッシュボード情報
     */
    List<DashboardInfo> selectByName(final String name);

    /**
     * ダッシュボード情報を新規登録する。<br />
     * @param dashboardInfo ダッシュボード情報
     * @return 登録件数
     */
    int insert(final DashboardInfo dashboardInfo);

    /**
     * ダッシュボード情報を更新する。
     * @param dashboardInfo ダッシュボード情報
     * @return 更新件数
     */
    int update(final DashboardInfo dashboardInfo);

    /**
     * ダッシュボード情報を削除する。
     * @param dashboardId ダッシュボードID
     * @return 削除件数
     */
    int deleteById(final long dashboardId);

    /**
     * すべてのダッシュボード情報を削除する。<br />
     * @return 削除件数
     */
    int deleteAll();

    /**
     * 直前のシーケンス情報を取得する。<br />
     * @return 直前のシーケンス番号
     */
    long selectSequenceNum();
}
