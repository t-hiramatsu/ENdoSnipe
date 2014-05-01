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
package jp.co.acroquest.endosnipe.data.db;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * �X�g���[������ SQL �𕶒P�ʂœǂݍ��ރN���X�ł��B<br />
 * 
 * @author y-komori
 */
public class SQLReader
{
    private static final int STRING_BUFFER_SIZE = 1024;

    private final InputStreamReader reader_;

    private int ch_ = -1; // �Ǎ��Ώە���

    private int nextCh_ = -1; // 1������ǂݗp�o�b�t�@

    /**
     * {@link SQLReader} ���\�z���܂��B<br />
     * 
     * @param reader ���̓X�g���[��
     */
    public SQLReader(final InputStreamReader reader)
    {
        if (reader == null)
        {
            throw new IllegalArgumentException("reader can't be null");
        }

        this.reader_ = reader;
    }

    /**
     * ���̓X�g���[������ SQL ����ǂݍ��݂܂��B<br />
     * 
     * SQL �̓Z�~�R�����ŋ�؂��܂��BSQL �R�����g�͖�������܂��B<br />SQL
     * �R�����g��1�s�R�����g�ƁA�����s�R�����g�̗������T�|�[�g���܂��B
     * 
     * @return �ǂݍ��� SQL �̃��X�g
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public List<String> readSql()
        throws IOException
    {
        List<String> result = new ArrayList<String>();
        StringBuffer sql = new StringBuffer(STRING_BUFFER_SIZE);
        int commentDepth = 0;

        for (;;)
        {
            read();
            if (ch_ == -1)
            {
                // �X�g���[���̏I�[�ɒB����
                break;
            }
            if (ch_ == '\n' || ch_ == '\r')
            {
                // ���s�̓ǂݔ�΂�
                continue;
            }

            if (commentDepth > 0)
            {
                // �R�����g���̏ꍇ
                if (ch_ == '/' && nextCh_ == '*')
                {
                    commentDepth++;
                    continue;
                }
                if (ch_ == '*' && nextCh_ == '/')
                {
                    commentDepth--;
                    read(); // '/' ��ǂݔ�΂�
                    continue;
                }
            }
            else
            {
                if (ch_ == '-' && nextCh_ == '-')
                {
                    skipToLineEnd();
                    continue;
                }
                else if (ch_ == '/' && nextCh_ == '*')
                {
                    commentDepth++;
                    continue;
                }
                else if (ch_ == ';')
                {
                    result.add(sql.toString());
                    sql = new StringBuffer(STRING_BUFFER_SIZE);
                    continue;
                }

                sql.append((char)ch_);
            }
        }
        return result;
    }

    private void skipToLineEnd()
        throws IOException
    {
        for (;;)
        {
            read();
            if (ch_ == -1 || ch_ == '\n' || ch_ == '\r')
            {
                return;
            }
        }
    }

    private void read()
        throws IOException
    {
        if (ch_ == -1)
        {
            ch_ = reader_.read();
        }
        else
        {
            ch_ = nextCh_;
        }
        nextCh_ = reader_.read();
    }
}
