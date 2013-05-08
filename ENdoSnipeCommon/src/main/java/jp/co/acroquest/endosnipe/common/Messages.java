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
package jp.co.acroquest.endosnipe.common;

import org.eclipse.osgi.util.NLS;

/**
 * メッセージ外部化のためのクラスです。<br />
 * 
 * @author y-komori
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME
		= "jp.co.acroquest.endosnipe.common.messages"; //$NON-NLS-1$

	/** Preference ページで設定するログレベルの項目ラベル */
	public static String endoSnipeCommonLogLevel__;

	/** Preference ページで設定するログレベルの項目値（デバッグ） */
	public static String endoSnipeCommonLogLevelDebug__;

	/** Preference ページで設定するログレベルの項目値（エラー） */
	public static String endoSnipeCommonLogLevelError__;

	/** Preference ページで設定するログレベルの項目値（情報） */
	public static String endoSnipeCommonLogLevelInfo__;

	/** Preference ページで設定するログレベルの項目値（警告） */
	public static String endoSnipeCommonLogLevelWarning__;

	/** メモリ不足でコピーできないときに表示するメッセージ */
	public static String endoSnipeCommonTooFewMemoryToCopy__;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
		// Do nothing.
	}
}
