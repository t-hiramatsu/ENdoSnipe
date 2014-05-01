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

import static jp.co.acroquest.endosnipe.javelin.converter.net.NetMonitorConstants.KEY_NETWORK_READ_LENGTH;
import static jp.co.acroquest.endosnipe.javelin.converter.net.NetMonitorConstants.KEY_NETWORK_THREAD_READ_LENGTH;

import java.io.IOException;
import java.io.InputStream;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.converter.util.StreamMonitorUtil;

/**
 * �l�b�g���[�N�̎�M�ʂ��Ď�����N���X�B
 * �v���Z�X�S�̂̃l�b�g���[�N�̎�M�ʂ�ۑ�����B
 * 
 * @author yamasaki
 * 
 */
public class InputStreamMonitor extends InputStream
{
    /** �������Ϗ�����N���X */
    private final InputStream delegated_;

    /** ���\�b�h�̎��s��\���t���O */
    private boolean inProcess_;

    /**
     * �R���X�g���N�^
     * 
     * @param delegated �Ϗ��N���X
     */
    public InputStreamMonitor(final InputStream delegated)
    {
        this.delegated_ = delegated;
    }

    /**
     * �f�[�^����M���A�l�b�g���[�N�̎�M�ʂ�ۑ�����B
     * @return �l�b�g���[�N��M��
     * @throws IOException ���o�͗�O
     */
    @Override
    public int read()
        throws IOException
    {
        if (this.inProcess_)
        {
            return this.delegated_.read();
        }

        this.inProcess_ = true;
        try
        {
            int value = this.delegated_.read();

            // �l�b�g���[�N��M�ʂ�0�ȏ�̂Ƃ��̂ݕۑ�����B
            if (value >= 0)
            {
                JavelinConfig config = new JavelinConfig();
                if (config.isNetInputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(1, KEY_NETWORK_THREAD_READ_LENGTH,
                                                         KEY_NETWORK_READ_LENGTH);
                }
            }

            return value;
        }
        finally
        {
            this.inProcess_ = false;
        }
    }

    /**
     * �f�[�^����M���A�l�b�g���[�N�̎�M�ʂ�ۑ�����B
     * @param b �f�[�^�̃o�C�g��
     * @param off �f�[�^�̃I�t�Z�b�g
     * @param len �f�[�^�̒���
     * @return �l�b�g���[�N��M��
     * @throws IOException ���o�͗�O
     */
    @Override
    public int read(final byte[] b, final int off, final int len)
        throws IOException
    {
        if (this.inProcess_)
        {
            return this.delegated_.read(b, off, len);
        }
        this.inProcess_ = true;
        try
        {
            int length = this.delegated_.read(b, off, len);

            // �l�b�g���[�N��M�ʂ�0�ȏ�̂Ƃ��̂ݕۑ�����B
            if (length >= 0)
            {
                JavelinConfig config = new JavelinConfig();
                if (config.isNetInputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(length, KEY_NETWORK_THREAD_READ_LENGTH,
                                                         KEY_NETWORK_READ_LENGTH);
                }
            }
            return length;
        }
        finally
        {
            this.inProcess_ = false;
        }
    }

    /**
     * �f�[�^����M���A�l�b�g���[�N�̎�M�ʂ�ۑ�����B
     * @param b �f�[�^�̃o�C�g��
     * @return �l�b�g���[�N��M��
     * @throws IOException ���o�͗�O
     */
    @Override
    public int read(final byte[] b)
        throws IOException
    {
        if (this.inProcess_)
        {
            return this.delegated_.read(b);
        }
        this.inProcess_ = true;
        try
        {
            int length = this.delegated_.read(b);

            // �l�b�g���[�N��M�ʂ�0�ȏ�̂Ƃ��̂ݕۑ�����B
            if (length >= 0)
            {
                JavelinConfig config = new JavelinConfig();
                if (config.isNetInputMonitor())
                {
                    StreamMonitorUtil.recordStreamAmount(length, KEY_NETWORK_THREAD_READ_LENGTH,
                                                         KEY_NETWORK_READ_LENGTH);
                }
            }
            return length;
        }
        finally
        {
            this.inProcess_ = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available()
        throws IOException
    {
        return this.delegated_.available();
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
    public synchronized void mark(final int readlimit)
    {
        this.delegated_.mark(readlimit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported()
    {
        return this.delegated_.markSupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset()
        throws IOException
    {
        this.delegated_.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(final long n)
        throws IOException
    {
        return this.delegated_.skip(n);
    }
}
