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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * �X�g���[���Ɋւ��郆�[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class StreamUtil
{
    private StreamUtil()
    {

    }

    /**
     * {@link File} �I�u�W�F�N�g������̓X�g���[���𐶐����܂��B<br />
     * 
     * @param file {@link File} �I�u�W�F�N�g
     * @return ���̓X�g���[��
     */
    public static InputStream getStream(final File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file can't be null");
        }

        if (file.exists() == false)
        {
            throw new IllegalArgumentException("file dosen't exist : " + file.getAbsolutePath());
        }

        try
        {
            return new BufferedInputStream(new FileInputStream(file));
        }
        catch (FileNotFoundException ex)
        // CHECKSTYLE:OFF
        {
            // Do nothing.
        }
        // CHECKSTYLE:ON
        return null;
    }

    /**
     * {@link File} �I�u�W�F�N�g�̎����t�@�C����ǂݍ��ނ��߂� {@link BufferedReader} �𐶐����܂��B<br />
     * 
     * @param file {@link File} �I�u�W�F�N�g
     * @return {@link BufferedReader} �I�u�W�F�N�g
     */
    public static BufferedReader getBufferedReader(final File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file can't be null");
        }

        if (file.exists() == false)
        {
            throw new IllegalArgumentException("file dosen't exist : " + file.getAbsolutePath());
        }

        try
        {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            return new BufferedReader(reader);
        }
        catch (FileNotFoundException ex)
        // CHECKSTYLE:OFF
        {
            // Do nothing.
        }
        // CHECKSTYLE:ON
        return null;
    }

    /**
     * {@link InputStream} ���� {@link BufferedReader} �𐶐����܂��B<br />
     * 
     * @param is {@link InputStream} �I�u�W�F�N�g
     * @return {@link BufferedReader} �I�u�W�F�N�g
     */
    public static BufferedReader getBufferedReader(final InputStream is)
    {
        if (is == null)
        {
            throw new IllegalArgumentException("inputStream can't be null");
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    /**
     * �����s�������ǂݍ��ނ��߂� {@link BufferedReader} �𐶐����܂��B<br />
     * 
     * @param contents ������
     * @return {@link BufferedReader} �I�u�W�F�N�g
     */
    public static BufferedReader getBufferedReader(final String contents)
    {
        if (contents == null)
        {
            throw new IllegalArgumentException("contents can't be null");
        }

        StringReader reader = new StringReader(contents);
        return new BufferedReader(reader);
    }

    /**
     * �X�g���[�����N���[�Y���܂��B<br />
     * ������ <code>null</code> �̏ꍇ�͉����s���܂���B
     * 
     * @param stream �X�g���[��

     */
    public static void closeStream(final Closeable stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            // CHECKSTYLE:OFF
            {
                // Do nothing.
            }
            // CHECKSTYLE:ON
        }
    }
}
