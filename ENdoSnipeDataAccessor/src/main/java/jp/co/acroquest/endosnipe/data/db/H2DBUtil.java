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
import java.sql.Connection;
import java.sql.SQLException;

/**
 * H2 �f�[�^�x�[�X�𑀍삷�邽�߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class H2DBUtil
{
    private H2DBUtil()
    {
    }

    /**
     * �N���X�p�X��ɑ��݂���DDL�t�@�C�������s���܂��B<br />
     * 
     * @param con �R�l�N�V����
     * @param path ���s����DDL�t�@�C���̃p�X
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws SQLException SQL�G���[�����������ꍇ
     */
    public static void executeDDL(final Connection con, final String path)
        throws IOException,
            SQLException
    {
        executeDDL(con, path, null);
    }

    /**
     * �N���X�p�X��ɑ��݂���DDL�t�@�C�������s���܂��B<br />
     * 
     * @param con �R�l�N�V����
     * @param path ���s����DDL�t�@�C���̃p�X
     * @param replacer SQL ��u�����邽�߂� {@link SQLReplacer}
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws SQLException SQL�G���[�����������ꍇ
     */
    public static void executeDDL(final Connection con, final String path,
            final SQLReplacer replacer)
        throws IOException,
            SQLException
    {
        if (path == null)
        {
            throw new IllegalArgumentException("schemaPath can't be null");
        }
        InputStream is = H2DBUtil.class.getResourceAsStream(path);
        SQLExecutor.executeSQL(con, is, "Shift_JIS", replacer);
        is.close();
    }
}
