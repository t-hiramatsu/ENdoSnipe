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
package jp.co.acroquest.endosnipe.communicator;

import jp.co.acroquest.endosnipe.communicator.impl.CommunicationClientImpl;

/**
 * {@link CommunicationServer}、 {@link CommunicationClient} のためのファクトリです。<br />
 * 
 * @author y-komori
 */
public class CommunicationFactory
{
    private CommunicationFactory()
    {
    }

    /**
     * {@link CommunicationClient} インスタンスを生成します。<br />
     * 
     * @param threadName スレッド名 
     * @return {@link CommunicationClient} インスタンス
     */
    public static CommunicationClient getCommunicationClient(String threadName)
    {
        return new CommunicationClientImpl(threadName);
    }

    /**
     * {@link CommunicationServer} インスタンスを生成します。<br />
     * 
     * @return {@link CommunicationServer} インスタンス
     */
    public static CommunicationServer getCommunicationServer()
    {
        return null;
    }
}
