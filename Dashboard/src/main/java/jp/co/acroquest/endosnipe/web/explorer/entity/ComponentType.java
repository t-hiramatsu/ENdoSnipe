/*******************************************************************************
 * ENdoSnipe 5.3 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Acroquest Technology Co.,Ltd.
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
