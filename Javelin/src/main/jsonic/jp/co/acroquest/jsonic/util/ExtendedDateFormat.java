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
package jp.co.acroquest.jsonic.util;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtendedDateFormat extends SimpleDateFormat {
	boolean escape = false;
	
	public ExtendedDateFormat(String pattern, Locale locale) {
		super(escape(pattern), locale);
		escape = !pattern.equals(this.toPattern());
	}
	
	public ExtendedDateFormat(String pattern) {
		super(escape(pattern));
		escape = !pattern.equals(this.toPattern());
	}
	
	static String escape(String pattern) {
		boolean skip = false;
		int count = 0;
		StringBuilder sb = null;
		int last = 0;
		for (int i = 0; i < pattern.length(); i++) {
			char c = pattern.charAt(i);
			if (c == '\'') {
				skip = !skip;
			} else if (c == 'Z' && !skip) {
				count++;
				if (count == 2) {
					if (sb == null) sb = new StringBuilder(pattern.length() + 4);
					sb.append(pattern, last, i-1);
					sb.append("Z\0");
					last = i+1;
				}
			} else {
				count = 0;
			}
		}
		if (sb != null) {
			if (last < pattern.length()) sb.append(pattern, last, pattern.length());
			return sb.toString();
		} else {
			return pattern;
		}
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
		super.format(date, toAppendTo, pos);
		if (escape) {
			for (int i = 5; i < toAppendTo.length(); i++) {
				if (toAppendTo.charAt(i) == '\0') {
					toAppendTo.setCharAt(i, toAppendTo.charAt(i-1));
					toAppendTo.setCharAt(i-1, toAppendTo.charAt(i-2));
					toAppendTo.setCharAt(i-2, ':');
				}
			}
		}
		return toAppendTo;
	}
}
