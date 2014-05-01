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

import jp.co.acroquest.endosnipe.web.explorer.entity.SqlPlan;

/**
 * {@link SqlPlan} のための DAO のインターフェースです。
 *
 * @author miyasaka
 *
 */
public interface SqlPlanDao
{
    /**
     * measurement_item_nameが一致するSQL実行計画データのリストを取得する。
     * 
     * @param itemName measurement_item_name
     * @return SQL実行計画データのリスト
     */
    List<SqlPlan> selectByItemName(final String itemName);
}
