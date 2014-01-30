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

import jp.co.acroquest.endosnipe.web.explorer.entity.PropertySettingDefinition;

/**
 * {@link PropertySettingDefinition} のための DAO のインターフェースです。
 * @author khinlay
 *
 */
public interface PropertySettingDao
{
    /**
     * レポート定義情報をレポート名をキーにして取得する。
     * @param key レポート名
     * @return レポート定義の配列
     */
    PropertySettingDefinition selectByKey(String key);

}
