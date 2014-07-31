/*
 * Copyright (c) 2004-2008 SMG Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.entity;

/**
 * {@link ComponentModel} の種類を表します。<br />
 *
 * @author sakamoto
 */
public enum ComponentType
{
    /** {@link ComponentModel} は Web ページを表します。 */
    WEB,

    /** {@link ComponentModel} は クラスを表します。 */
    CLASS,

    /** {@link ComponentModel} は データベースを表します。 */
    DATABASE,

    /** {@link ComponentModel} は パッケージを表します。 */
    PACKAGE;

    /** WEB のコンポーネント名のプレフィックス文字 */
    public static final String WEB_PREFIX = "/";

    /** DATABASE のコンポーネント名のプレフィックス文字 */
    public static final String DATABASE_PREFIX = "jdbc:";

    /**
     * 指定した文字列から、 {@link ComponentModel} の種類を判定します。<br />
     *
     * "/" で始まる場合は Web ページ、 "jdbc:" で始まる場合はデータベース、
     * その他はクラスと判定します。
     *
     * @param className クラス名
     * @return {@link ComponentModel} の種類
     */
    public static ComponentType getComponentType(final String className)
    {
        if (className.startsWith(WEB_PREFIX))
        {
            return WEB;
        }
        else if (className.startsWith(DATABASE_PREFIX))
        {
            return DATABASE;
        }
        else
        {
            return CLASS;
        }
    }

}
