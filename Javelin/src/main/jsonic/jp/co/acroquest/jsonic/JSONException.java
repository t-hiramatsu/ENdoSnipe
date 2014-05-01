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
package jp.co.acroquest.jsonic;

/**
 * Signals that an error has been reached unexpectedly while formating or parsing.
 * 
 * <h4>Summary of error codes</h4>
 * <table border="1" cellpadding="1" cellspacing="0">
 * <tr>
 * 	<th bgcolor="#CCCCFF" align="left">code(range)</th>
 * 	<th bgcolor="#CCCCFF" align="left">error code</th>
 * 	<th bgcolor="#CCCCFF" align="left">description</th>
 * </tr>
 * <tr><td>000-099</td><td>(all)</td><td>reserved.</td></tr>
 * <tr><td rowspan="2">100-199</td><td>100</td><td>fails to format.</td></tr>
 * <tr>                            <td>150</td><td>fails to preformat.</td></tr>
 * <tr>                            <td>(others)</td><td>reserved.</td></tr>
 * <tr><td rowspan="2">200-299</td><td>200</td><td>fails to parse.</td></tr>
 * <tr>                            <td>250</td><td>fails to postparse.</td></tr>
 * <tr>                            <td>(others)</td><td>reserved.</td></tr>
 * <tr><td>300-899</td><td>(all)</td><td>reserved.</td></tr>
 * <tr><td>900-</td><td>(all)</td><td>user's area.</td></tr>
 * </table>
 * 
 * @author izuno
 */
public class JSONException extends RuntimeException {
	private static final long serialVersionUID = -8323989588488596436L;

	public static final int FORMAT_ERROR = 100;
	public static final int PREFORMAT_ERROR = 150;
	public static final int PARSE_ERROR = 200;
	public static final int POSTPARSE_ERROR = 250;
	
	private int errorID;
	private long lineNumber = -1l;
	private long columnNumber = -1l;
	private long offset = -1l;
	
	JSONException(String message, int id, long lineNumber, long columnNumber, long offset) {
		super(message);
		this.errorID = id;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.offset = offset;
	}
	
	public JSONException(String message, int id,  Throwable cause) {
		super(message, cause);
		this.errorID = id;
	}
	
	public JSONException(String message, int id) {
		super(message);
		this.errorID = id;
	}
	
	public int getErrorCode() {
		return errorID;
	}
	
	/**
	 * Returns the line number where the error was found.
	 */
	public long getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Returns the column number where the error was found.
	 */
	public long getColumnNumber() {
		return columnNumber;
	}
	
	/**
	 * Returns the offset in line where the error was found.
	 */
	public long getErrorOffset() {
		return offset;
	}
}
