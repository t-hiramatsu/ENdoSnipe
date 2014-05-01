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
package jp.co.acroquest.jsonic.io;


public class CharSequenceInputSource implements InputSource {
	private int lines = 1;
	private int columns = 0;
	private int offset = 0;
	
	private int start = 0;
	int mark = -1;
	
	private final CharSequence cs;
	
	public CharSequenceInputSource(CharSequence cs) {
		if (cs == null) {
			throw new NullPointerException();
		}
		this.cs = cs;
	}
	
	public int next() {
		int n = -1;
		if (start < cs.length()) {
			n = cs.charAt(start++);
			offset++;
			if (n == '\r') {
				lines++;
				columns = 0;
			} else if (n == '\n') {
				if (offset < 2 || cs.charAt(offset-2) != '\r') {
					lines++;
					columns = 0;
				}
			} else {
				columns++;
			}
		} else {
			start++;
			return -1;
		}
		return n;
	}
	
	public void back() {
		if (start == 0) {
			throw new IllegalStateException("no backup charcter");
		}
		start--;
		if (start < cs.length()) {
			offset--;
			columns--;
		}
	}
	
	public long getLineNumber() {
		return lines;
	}
	
	public long getColumnNumber() {
		return columns;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public String toString() {
		int spos = 0;
		int max = Math.min(start-1, cs.length()-1);
		int charCount = 0;
		for (int i = 0; i < max + 1 && i < 20; i++) {
			char c = cs.charAt(max-i);
			if (c == '\r' || (c == '\n' && (max-i-1 < 0 || cs.charAt(max-i-1) != '\r'))) {
				if (charCount > 0) break;
			} else if (c != '\n') {
				spos = max-i;
				charCount++;
			}
		}
		return (spos <= max) ? cs.subSequence(spos, max+1).toString() : "";
	}
}