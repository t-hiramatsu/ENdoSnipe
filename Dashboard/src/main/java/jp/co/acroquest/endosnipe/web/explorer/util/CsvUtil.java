/*
 * Copyright (c) 2004-2015 Acroquest Technology Co., Ltd. All Rights Reserved.
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
 * CSV処理を行うためのユーティリティクラス
 * @author fujii
 *
 */
public class CsvUtil
{
    /** CSVのデリミタ */
    public static final String CSV_DELIMITER = ",";

    /** CSVの改行 */
    public static final String CSV_LINE = "\n";

    /**
     * インスタンス化を防止するプライベートコンストラクタ
     */
    private CsvUtil()
    {

    }

    /**
     * CSV用にエスケープした文字を取得する。
     * @param value 値
     * @return エスケープした文字
     */
    public static String escaped(final String value)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\"");
        char[] charArr = value.toCharArray();
        for (int cnt = 0; cnt < charArr.length; cnt++)
        {
            char c = charArr[cnt];
            if (c == '\"')
            {
                builder.append('\"');
                builder.append(c);
            }
            else
            {
                builder.append(c);
            }
        }
        builder.append("\"");
        return builder.toString();
    }
}
