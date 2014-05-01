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

import java.io.File;
import java.sql.Connection;

import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.common.util.IOUtil;

/**
 * ConnectionManager�̃e�X�g�R�[�h�B
 * Ver4.5 ���O���b�V���������p�R�[�h
 * 
 * @author eriguchi
 */
public class ConnectionManagerTest extends DJUnitTestCase
{
    /** �f�[�^�x�[�X�t�@�C����ۑ�����f�B���N�g���B */
    private static final String   BASE_DIR = IOUtil.getTmpDirFile().getAbsolutePath();

    /** �e�X�g�Ŏg�p����f�[�^�x�[�X�̖��O�B */
    protected static final String DB_NAME  = "endosnipedb";

    /**
     * Ver4.5 ���O���b�V���������p�R�[�h(1-1-1)
     * �R�l�N�V�������擾������Ԃ�ConnectionManager�ŃR�l�N�V������S�ؒf����ƁA
     * WEDA0106�̃��b�Z�[�W���o�͂��邱�Ƃ��m�F����B
     */
    public void testCloseAllMessage()
        throws Exception
    {
        // ����
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

        // ���s
        connectionManager.getConnection(DB_NAME, false);
        connectionManager.closeAll();

        // ����
        assertArgumentPassed("org.apache.commons.logging.Log", "warn", 0,
                             "[WEDA0106]�f�[�^�x�[�X����A�C�h���R�l�N�V������S�ؒf���܂������A�A�N�e�B�u�R�l�N�V�������c���Ă��܂�.(�R�l�N�V������:1)");
    }
}
