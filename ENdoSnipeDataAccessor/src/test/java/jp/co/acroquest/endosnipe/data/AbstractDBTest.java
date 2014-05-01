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
package jp.co.acroquest.endosnipe.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.IOUtil;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.db.ConnectionManager;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.dgic.testing.framework.DJUnitTestCase;

import org.apache.commons.io.FileUtils;

/**
 * �f�[�^�x�[�X�Ɋւ���e�X�g���s�����߂̊��N���X�ł��B<br />
 * 
 * @author y-komori
 */
public abstract class AbstractDBTest extends DJUnitTestCase
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(AbstractDBTest.class);

    /** �f�[�^�x�[�X�t�@�C����ۑ�����f�B���N�g���B */
    private static final String BASE_DIR = IOUtil.getTmpDirFile().getAbsolutePath();

    /** �e�X�g�Ŏg�p����f�[�^�x�[�X�̖��O�B */
    protected static final String DB_NAME = "endosnipedb";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        LOGGER.info("�e�X�g�J�n:" + getName());
        initConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown()
        throws Exception
    {
        super.tearDown();
        
        try
        {
            deleteDB();
        }
        catch (IOException ex)
        {
            LOGGER.info("DB�̍폜�Ɏ��s���܂����B" + getName());
        }
        LOGGER.info("�e�X�g�I��:" + getName());
    }

    protected void initConnection()
    {
        DBManager.updateSettings(false, "", "localhost", "5432", "endosnipedb", "postgres", "postgres");
        
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        File tempDir = new File(BASE_DIR);
        if (tempDir.exists() == false)
        {
            if (tempDir.mkdir() == false)
            {
                fail("�f�B���N�g���̍쐬�Ɏ��s���܂���. �f�B���N�g����:" + tempDir.getAbsolutePath());
            }
        }
        connectionManager.setBaseDir(BASE_DIR);
    }

    protected void initDB()
        throws SQLException,
            IOException
    {
        initConnection();
        Connection conn = getConnection();
        DBInitializer.initialize(conn);
        SQLUtil.closeConnection(conn);
    }

    /**
     * �f�[�^�x�[�X�t�@�C�����폜���܂��B<br />
     * 
     * @throws IOException �t�@�C�����폜�ł��Ȃ��ꍇ
     */
    protected void deleteDB()
        throws IOException
    {
        ConnectionManager.getInstance().closeAll();

        File dbDir = new File(BASE_DIR + "/" + DB_NAME);
        if (dbDir.exists())
        {
            FileUtils.deleteDirectory(dbDir);
        }
    }

    /**
     * �e�X�g�Ŏg�p����f�[�^�x�[�X�R�l�N�V�������擾���܂��B<br />
     * 
     * @return �R�l�N�V����
     * @throws SQLException
     */
    protected Connection getConnection()
        throws SQLException
    {
        return ConnectionManager.getInstance().getConnection(DB_NAME, false);
    }
}
