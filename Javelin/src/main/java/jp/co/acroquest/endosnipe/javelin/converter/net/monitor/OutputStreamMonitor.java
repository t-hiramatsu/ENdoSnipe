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
package jp.co.acroquest.endosnipe.javelin.converter.net.monitor;

import static jp.co.acroquest.endosnipe.javelin.converter.net.NetMonitorConstants.KEY_NETWORK_THREAD_WRITE_LENGTH;
import static jp.co.acroquest.endosnipe.javelin.converter.net.NetMonitorConstants.KEY_NETWORK_WRITE_LENGTH;

import java.io.IOException;
import java.io.OutputStream;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.converter.util.StreamMonitorUtil;

/**
 * ネットワークI/Oの流量をモニタリングする。
 * 
 * @author yamasaki
 */
public class OutputStreamMonitor extends OutputStream
{
    /** 委譲クラス */
    private final OutputStream delegated_;

    /** メソッドの実行を表すフラグ */
    private boolean inProcess_;

    /**
     * コンストラクタ
     * 
     * @param delegated 委譲クラス
     */
    public OutputStreamMonitor(final OutputStream delegated)
    {
        this.delegated_ = delegated;
    }

    /**
     * データを送信し、ネットワークの送信量を保存する。
     * 
     * @param b ネットワークの送信量
     * @throws IOException 入出力例外
     */
    @Override
    public void write(final int b)
        throws IOException
    {
        if (this.inProcess_)
        {
            this.delegated_.write(b);
        }
        else
        {
            this.inProcess_ = true;
            try
            {
                this.delegated_.write(b);
                JavelinConfig config = new JavelinConfig();
                if (config.isNetOutputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(1, KEY_NETWORK_THREAD_WRITE_LENGTH,
                                                         KEY_NETWORK_WRITE_LENGTH);
                }
            }
            finally
            {
                this.inProcess_ = false;
            }
        }
    }

    /**
     * データを送信し、ネットワークの送信量を保存する。
     * 
     * @param b データのバイト列
     * @param off オフセット
     * @param len データ長
     * @throws IOException 入出力例外
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
        throws IOException
    {
        if (this.inProcess_)
        {
            this.delegated_.write(b, off, len);
        }
        else
        {
            this.inProcess_ = true;
            try
            {
                this.delegated_.write(b, off, len);
                JavelinConfig config = new JavelinConfig();
                if (config.isNetOutputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(len, KEY_NETWORK_THREAD_WRITE_LENGTH,
                                                         KEY_NETWORK_WRITE_LENGTH);
                }
            }
            finally
            {
                this.inProcess_ = false;
            }
        }
    }

    /**
     * データを送信し、ネットワークの送信量を保存する。
     * 
     * @param b データのバイト列
     * @throws IOException 入出力例外
     */
    @Override
    public void write(final byte[] b)
        throws IOException
    {
        if (this.inProcess_)
        {
            this.delegated_.write(b);
        }
        else
        {
            this.inProcess_ = true;
            try
            {
                this.delegated_.write(b);
                JavelinConfig config = new JavelinConfig();
                if (config.isNetOutputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(b.length, KEY_NETWORK_THREAD_WRITE_LENGTH,
                                                         KEY_NETWORK_WRITE_LENGTH);
                }
            }
            finally
            {
                this.inProcess_ = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
        throws IOException
    {
        this.delegated_.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
        throws IOException
    {
        this.delegated_.flush();
    }
}
