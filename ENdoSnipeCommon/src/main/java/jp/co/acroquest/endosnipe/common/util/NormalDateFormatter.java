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
package jp.co.acroquest.endosnipe.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一般的な形式の日付文字列とlong型の時刻値を相互変換するユーティリティ。
 * 
 * @author hayakawa
 */
public class NormalDateFormatter
{
	private static final String DATA_FORMAT_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";

	private static SimpleDateFormat formatter__ = new SimpleDateFormat(
			DATA_FORMAT_PATTERN);

	private static SimpleDateFormat syncFormatter__ = new SimpleDateFormat(
			DATA_FORMAT_PATTERN);

	private static Date tmpDateObject__ = new Date();

	private static Date syncTmpDateObject__ = new Date();

	/**
	 * コンストラクタを隠す
	 */
	private NormalDateFormatter()
	{

	}

	/**
	 * long値で渡された時刻の値を、"yyyy/MM/dd HH:mm:ss.SSS" という形式の文字列に変換する。
	 * 同期化していないため、複数スレッドからのアクセスに対する 呼び出しがあった場合は、結果を保証しない。
	 * 
	 * @param time
	 *            時刻
	 * @return フォーマットした時刻の文字列
	 */
	public static String format(final long time)
	{
		tmpDateObject__.setTime(time);
		return formatter__.format(tmpDateObject__);
	}

	/**
	 * long値で渡された時刻の値を、"yyyy/MM/dd HH:mm:ss.SSS" という形式の文字列に変換する。
	 * 同期化しているため、複数スレッドからのアクセスに対応可能である。 ただし、ロック取得によるパフォーマンスの劣化に留意すること。
	 * 
	 * @param time
	 *            時刻
	 * @return フォーマットした時刻の文字列
	 */
	public static synchronized String formatSync(final long time)
	{
		syncTmpDateObject__.setTime(time);
		return syncFormatter__.format(syncTmpDateObject__);
	}

	/**
	 * 文字列を日付に変換する。
	 * 
	 * @param dateString 日付文字列
	 * @return 変換結果。
	 * @throws ParseException 解析例外
	 */
	public static Date parse(final String dateString) throws ParseException
	{
		if (dateString == null)
		{
			throw new ParseException("NullPointerException", 0);
		}

		Date date = formatter__.parse(dateString);
		return date;
	}

}
