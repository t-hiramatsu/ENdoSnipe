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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * SQL ���s�̂��߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class SQLExecutor
{
    private SQLExecutor()
    {
    }

    /**
     * �X�g���[������SQL��ǂݍ���Ŏ��s���܂��B<br />
     * 
     * �Z�~�R�����ŋ�؂�ꂽ������SQL���L�q����Ă���ꍇ�A���ԂɎ��s���܂��B<br />
     * �s���ɂ��� -- �ȍ~�̓R�����g�Ƃ��ēǂݔ�΂��܂��B
     * 
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @param stream SQL ��ǂݍ��ނ��߂̓��̓X�g���[��
     * @return ���X�V�s��
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws SQLException SQL���s�Ɏ��s�����ꍇ
     */
    public static int executeSQL(final Connection con, final InputStream stream)
        throws IOException,
            SQLException
    {
        return executeSQL(con, stream, null, null);
    }

    /**
     * �X�g���[������SQL��ǂݍ���Ŏ��s���܂��B<br />
     * 
     * �Z�~�R�����ŋ�؂�ꂽ������SQL���L�q����Ă���ꍇ�A���ԂɎ��s���܂��B<br />
     * �s���ɂ��� -- �ȍ~�̓R�����g�Ƃ��ēǂݔ�΂��܂��B
     * 
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @param stream SQL ��ǂݍ��ނ��߂̓��̓X�g���[��
     * @param replacer SQL ��u�����邽�߂� {@link SQLReplacer}
     * @return ���X�V�s��
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws SQLException SQL���s�Ɏ��s�����ꍇ
     */
    public static int executeSQL(final Connection con, final InputStream stream,
            final SQLReplacer replacer)
        throws IOException,
            SQLException
    {
        return executeSQL(con, stream, null, replacer);
    }

    /**
     * �G���R�[�f�B���O���w�肵���X�g���[������SQL��ǂݍ���Ŏ��s���܂��B<br />
     * 
     * �Z�~�R�����ŋ�؂�ꂽ������SQL���L�q����Ă���ꍇ�A���ԂɎ��s���܂��B<br />
     * �s���ɂ��� -- �ȍ~�̓R�����g�Ƃ��ēǂݔ�΂��܂��B
     * 
     * @param con  �f�[�^�x�[�X�R�l�N�V����
     * @param stream SQL ��ǂݍ��ނ��߂̓��̓X�g���[��
     * @param encoding SQL �̃G���R�[�f�B���O
     * @param replacer SQL ��u�����邽�߂� {@link SQLReplacer}
     * @return ���X�V�s��
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws SQLException SQL���s�Ɏ��s�����ꍇ

     */
    public static int executeSQL(final Connection con, final InputStream stream,
            final String encoding, final SQLReplacer replacer)
        throws IOException,
            SQLException
    {
        if (con == null)
        {
            throw new IllegalArgumentException("connection can't be null");
        }
        if (stream == null)
        {
            throw new IllegalArgumentException("stream can't be null");
        }

        InputStreamReader isr;
        if (encoding != null)
        {
            isr = new InputStreamReader(stream, encoding);
        }
        else
        {
            isr = new InputStreamReader(stream);
        }
        int updateCount = 0;
        SQLReader reader = new SQLReader(isr);
        List<String> sqls = null;
        try
        {
            sqls = reader.readSql();
        }
        catch (IOException ex)
        {
            isr.close();
            throw ex;
        }

        for (String sql : sqls)
        {
            Statement stmt = null;
            try
            {
                if (replacer != null)
                {
                    sql = replacer.replace(sql);
                }
                stmt = con.createStatement();
                //System.out.println("SQL: " + sql);
                if (!stmt.execute(sql))
                {
                    updateCount += stmt.getUpdateCount();
                }
            }
            catch (SQLException ex)
            {
                if (stmt != null)
                {
                    stmt.close();
                }
                throw ex;
            }
            if (stmt != null)
            {
                stmt.close();
            }
        }
        return updateCount;
    }

    /**
     * SQL�����s���܂��B
     *
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @param sql SQL��
     * @param replacer SQL ��u�����邽�߂� {@link SQLReplacer}
     * @return ���X�V�s��
     * @throws SQLException SQL���s�Ɏ��s�����ꍇ
     */
    public static int executeSQL(final Connection con, final String sql, final SQLReplacer replacer)
        throws SQLException
    {
        int updateCount = 0;
        String sqlConverted = sql;
        Statement stmt = null;
        try
        {
            if (replacer != null)
            {
                sqlConverted = replacer.replace(sql);
            }
            stmt = con.createStatement();
            if (!stmt.execute(sqlConverted))
            {
                updateCount = stmt.getUpdateCount();
            }
        }
        finally
        {
            if (stmt != null)
            {
                stmt.close();
            }
        }
        return updateCount;
    }
}
