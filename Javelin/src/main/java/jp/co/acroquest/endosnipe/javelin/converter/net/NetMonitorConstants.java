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
package jp.co.acroquest.endosnipe.javelin.converter.net;

/**
 * ネットワークI/Oのモニタリングに使用する定数の集合。
 * 
 * @author yamasaki
 */
public interface NetMonitorConstants
{
    /** スレッド毎のネットワーク受信量を保存・参照する際のキー。 */
    String KEY_NETWORK_THREAD_READ_LENGTH = "net.currentThreadReadLength";

    /** スレッド毎のネットワーク送信量を保存・参照する際のキー。 */
    String KEY_NETWORK_THREAD_WRITE_LENGTH = "net.currentThreadWriteLength";

    /** プロセス全体でのネットワーク受信量を保存・参照する際のキー。 */
    String KEY_NETWORK_READ_LENGTH = "net.readLength";

    /** プロセス全体でのネットワーク送信量を保存・参照する際のキー。 */
    String KEY_NETWORK_WRITE_LENGTH = "net.writeLength";
}
