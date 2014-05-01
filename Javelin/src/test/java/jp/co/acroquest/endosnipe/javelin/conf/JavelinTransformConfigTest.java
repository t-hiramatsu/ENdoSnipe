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
package jp.co.acroquest.endosnipe.javelin.conf;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Javelin�̃R�[�h���ߍ��ݐݒ�悤�e�X�g�N���X�B
 * �����Exclude�Ώۂ̊m�F�̂݁B
 * @author tooru
 *
 */
public class JavelinTransformConfigTest extends TestCase
{
    /**
     * [����] 1-2-1
     * @throws IOException �ݒ�t�@�C�����ǂݍ��߂Ȃ��ꍇ
     */
    public void testLogExcludeSingle()
        throws IOException
    {
        // ����
        String includePath = "include.conf";
        String excludePath = "exclude1.conf";

        JavelinTransformConfig config = new JavelinTransformConfig();

        InputStream includeStream = null;
        InputStream excludeStream = null;
        try
        {
            includeStream = getClass().getResourceAsStream(includePath);
            excludeStream = getClass().getResourceAsStream(excludePath);

            // ���s
            config.readConfig(includeStream, excludeStream);

            // ����
            boolean result =
                    config.isExcludeClass("jp.co.acroquest.endosnipe.javelin.conf.TestExcludeClass1");
            assertTrue(result);
        }
        finally
        {
            if (includeStream != null)
            {
                includeStream.close();
            }
            if (excludeStream != null)
            {
                excludeStream.close();
            }
        }
    }

    /**
     * [����] 1-2-2
     * @throws IOException �ݒ�t�@�C�����ǂݍ��߂Ȃ��ꍇ
     */
    public void testLogExcludeMulti()
        throws IOException
    {
        // ����
        String includePath = "include.conf";
        String excludePath = "exclude2.conf";

        JavelinTransformConfig config = new JavelinTransformConfig();

        InputStream includeStream = null;
        InputStream excludeStream = null;
        try
        {
            includeStream = getClass().getResourceAsStream(includePath);
            excludeStream = getClass().getResourceAsStream(excludePath);

            // ���s
            config.readConfig(includeStream, excludeStream);

            // ����
            boolean result =
                    config.isExcludeClass("jp.co.acroquest.endosnipe.javelin.conf.TestExcludeClass2");
            assertTrue(result);
        }
        finally
        {
            if (includeStream != null)
            {
                includeStream.close();
            }
            if (excludeStream != null)
            {
                excludeStream.close();
            }
        }
    }

    /**
     * [����] 1-2-3
     * @throws IOException �ݒ�t�@�C�����ǂݍ��߂Ȃ��ꍇ
     */
    public void testLogExcludeMethod()
        throws IOException
    {
        // ����
        String includePath = "include.conf";
        String excludePath = "exclude3.conf";

        JavelinTransformConfig config = new JavelinTransformConfig();

        InputStream includeStream = null;
        InputStream excludeStream = null;
        try
        {
            includeStream = getClass().getResourceAsStream(includePath);
            excludeStream = getClass().getResourceAsStream(excludePath);

            // ���s
            config.readConfig(includeStream, excludeStream);

            // ����
            boolean result = config.isExcludeClass("jp.co.acroquest.endosnipe.javelin.conf.TestExcludeClass");
            assertFalse(result);
        }
        finally
        {
            if (includeStream != null)
            {
                includeStream.close();
            }
            if (excludeStream != null)
            {
                excludeStream.close();
            }
        }
    }

    /**
     * [����] 1-2-4
     * @throws IOException �ݒ�t�@�C�����ǂݍ��߂Ȃ��ꍇ
     */
    public void testLogExcludeNoClass()
        throws IOException
    {
        // ����
        String includePath = "include.conf";
        String excludePath = "exclude4.conf";

        JavelinTransformConfig config = new JavelinTransformConfig();

        InputStream includeStream = null;
        InputStream excludeStream = null;
        try
        {
            includeStream = getClass().getResourceAsStream(includePath);
            excludeStream = getClass().getResourceAsStream(excludePath);

            // ���s
            config.readConfig(includeStream, excludeStream);

            // ����
            boolean result =
                    config.isExcludeClass("jp.co.acroquest.endosnipe.javelin.conf.TestExcludeClass1");
            assertFalse(result);
        }
        finally
        {
            if (includeStream != null)
            {
                includeStream.close();
            }
            if (excludeStream != null)
            {
                excludeStream.close();
            }
        }
    }

}
