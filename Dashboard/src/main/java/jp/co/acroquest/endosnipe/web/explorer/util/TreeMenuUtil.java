/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.util;

/**
 * TreeMenuを使用する際のユーティリティ。
 * @author eriguchi
 */
public class TreeMenuUtil
{
    /**
     * インスタンス化を阻止するprivateコンストラクタ。
     */
    private TreeMenuUtil()
    {
        // Do Nothing.
    }

    /**
     * TreeMenuDtoのidに使用できない文字を空白にする。
     * 
     * @param id TreeMenuDtoのid
     * @return TreeMenuDtoのidとして使用できる文字のみのid。
     */
    public static String getCannonicalId(final String id)
    {
        if (id != null)
        {
            return id.replaceAll("[\\r\\n]", " ");
        }
        return null;
    }
}
