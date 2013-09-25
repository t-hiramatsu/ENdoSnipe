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
package jp.co.acroquest.endosnipe.common.entity;

/**
 * MBeanで計測した値を表す列挙体です。<br />
 * データ型は以下のコードで示します。<br />
 * <br />
 * <table border="1" cellspacing="0">
 * <tr>
 * <th>データ型</th>
 * <th>数値</th>
 * </tr>
 * <tr>
 * <td>不明</td>
 * <td>-1</td>
 * </tr>
 * <tr>
 * <td>byte</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>short</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>int</td>
 * <td>2</td>
 * </tr>
 * <tr>
 * <td>long</td>
 * <td>3</td>
 * </tr>
 * <tr>
 * <td>float</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>double</td>
 * <td>5</td>
 * </tr>
 * <tr>
 * <td>String</td>
 * <td>6</td>
 * </tr>
 * <tr>
 * <td>JSON</td>
 * <td>7</td>
 * </tr>
 * </table>
 * 
 * @author y_asazuma
 */
public enum ItemType
{
    /** 不明 */
    ITEMTYPE_UNKNOWN, // -1
    /** byte型 */
    ITEMTYPE_BYTE, // 0
    /** short型 */
    ITEMTYPE_SHORT, // 1
    /** int型 */
    ITEMTYPE_INT, // 2
    /** long型 */
    ITEMTYPE_LONG, // 3
    /** float型 */
    ITEMTYPE_FLOAT, // 4
    /** double型 */
    ITEMTYPE_DOUBLE, // 5
    /** String型 */
    ITEMTYPE_STRING; // 6

    private static final byte BYTE_UNKNOWN = -1;

    private static final byte BYTE_BYTE = 0;

    private static final byte BYTE_SHORT = 1;

    private static final byte BYTE_INT = 2;

    private static final byte BYTE_LONG = 3;

    private static final byte BYTE_FLOAT = 4;

    private static final byte BYTE_DOUBLE = 5;

    private static final byte BYTE_STRING = 6;

    /**
     * 数値からデータ型を返す。
     * 
     * @param n
     *            数値
     * @return データ型
     */
    public static ItemType getItemType(final byte n)
    {
        switch (n)
        {
        case BYTE_UNKNOWN:
            return ITEMTYPE_UNKNOWN;
        case BYTE_BYTE:
            return ITEMTYPE_BYTE;
        case BYTE_SHORT:
            return ITEMTYPE_SHORT;
        case BYTE_INT:
            return ITEMTYPE_INT;
        case BYTE_LONG:
            return ITEMTYPE_LONG;
        case BYTE_FLOAT:
            return ITEMTYPE_FLOAT;
        case BYTE_DOUBLE:
            return ITEMTYPE_DOUBLE;
        case BYTE_STRING:
            return ITEMTYPE_STRING;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * データ型に応じた数値を取得します。
     * 
     * @param type
     *            データ型
     * @return 数値
     */
    public static byte getItemTypeNumber(final ItemType type)
    {
        switch (type)
        {
        case ITEMTYPE_UNKNOWN:
            return BYTE_UNKNOWN;
        case ITEMTYPE_BYTE:
            return BYTE_BYTE;
        case ITEMTYPE_SHORT:
            return BYTE_SHORT;
        case ITEMTYPE_INT:
            return BYTE_INT;
        case ITEMTYPE_LONG:
            return BYTE_LONG;
        case ITEMTYPE_FLOAT:
            return BYTE_FLOAT;
        case ITEMTYPE_DOUBLE:
            return BYTE_DOUBLE;
        case ITEMTYPE_STRING:
            return BYTE_STRING;
        default:
            throw new IllegalArgumentException();
        }
    }
}
