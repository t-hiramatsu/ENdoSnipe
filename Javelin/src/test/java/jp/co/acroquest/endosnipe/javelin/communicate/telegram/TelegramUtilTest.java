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
package jp.co.acroquest.endosnipe.javelin.communicate.telegram;

import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.AssertUtil;
import jp.co.acroquest.test.util.JavelinTestUtil;
import junit.framework.TestCase;

/**
 * TelegramUtil�̃e�X�g�R�[�h
 * @author fujii
 *
 */
public class TelegramUtilTest extends TestCase implements TelegramConstants
{

    private static final String CONFIG_PATH = "/telegram/conf/javelin.properties";

    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp() throws Exception
    {
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        MockObjectManager.initialize();
        JavelinTestUtil.camouflageJavelinConfig(getClass(), CONFIG_PATH);
        this.config_ = new JavelinConfig();
        this.config_.setJavelinFileDir(JavelinTestUtil.getAbsolutePath(getClass(), "/telegram/logs"));
        SystemLogger.initSystemLog(this.config_);
    }

    /**
     * [����] 3-1-1 createJvnLogDownloadTelegram�̃e�X�g�B <br />
     * �E"file1.jvn"�ɑ΂��āA�_�E�����[�h�p���O�d�����쐬����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogDownloadTelegram_FileNum1()
    {
        // ����
        String[] jvnFileNames = {"file1.jvn"};

        // ���s
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                             jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail1 = {"file1.jvn"};
        String[] detail2 = {"contentOfFirstJavelinFile"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 1, detail1,
                                  body[0]);
        AssertUtil.assertTelegram("jvnFile", "jvnFileContent", BYTE_ITEMMODE_KIND_STRING, 1,
                                  detail2, body[1]);
    }

    /**
     * [����] 3-1-2 createJvnLogDownloadTelegram�̃e�X�g�B <br />
     * �E"file1.jvn","file2.jvn","file3.jvn"�ɑ΂��āA�_�E�����[�h�p���O�d�����쐬����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogDownloadTelegram_FileNum3()
    {
        // ����
        String[] jvnFileNames = {"file1.jvn", "file2.jvn", "file3.jvn"};

        // ���s
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                             jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail1 = {"file1.jvn", "file2.jvn", "file3.jvn"};
        String[] detail2 =
                {"contentOfFirstJavelinFile", "contentOfSecondJavelinFile",
                        "contentOfThirdJavelinFile"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 3, detail1,
                                  body[0]);
        AssertUtil.assertTelegram("jvnFile", "jvnFileContent", BYTE_ITEMMODE_KIND_STRING, 3,
                                  detail2, body[1]);
    }

    /**
     * [����] 3-1-3 createJvnLogDownloadTelegram�̃e�X�g�B <br />
     * �E���݂��Ȃ��t�@�C��"nofile1.jvn"��,���݂���t�@�C��"file1.jvn"�ɑ΂��āA
     * �_�E�����[�h�p���O�d�����쐬����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogDownloadTelegram_NotExistFileAndExistFile()
    {
        // ����
        String[] jvnFileNames = {"nofile1.jvn", "file1.jvn"};

        // ���s
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                             jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail1 = {"nofile1.jvn", "file1.jvn"};
        String[] detail2 = {"", "contentOfFirstJavelinFile"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 2, detail1,
                                  body[0]);
        AssertUtil.assertTelegram("jvnFile", "jvnFileContent", BYTE_ITEMMODE_KIND_STRING, 2,
                                  detail2, body[1]);
    }

    /**
     * [����] 3-1-4 createJvnLogDownloadTelegram�̃e�X�g�B <br />
     * �E���݂��Ȃ��t�@�C��"nofile1.jvn"�ɑ΂��āA�_�E�����[�h�p���O�d�����쐬����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogDownloadTelegram_NotExist()
    {
        // ����
        String[] jvnFileNames = {"nofile1.jvn"};

        // ���s
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                             jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail1 = {"nofile1.jvn"};
        String[] detail2 = {""};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 1, detail1,
                                  body[0]);
        AssertUtil.assertTelegram("jvnFile", "jvnFileContent", BYTE_ITEMMODE_KIND_STRING, 1,
                                  detail2, body[1]);
    }

    /**
     * [����] 3-2-1 createJvnLogListTelegram�̃e�X�g�B <br />
     * �E������{""}�ɂ��āAcreateJvnLogListTelegram���Ăяo���B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogListTelegram_Empty()
    {
        // ����
        String[] jvnFileNames = {};

        // ���s
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail = {};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 0, detail,
                                  body[0]);
    }

    /**
     * [����] 3-2-2 createJvnLogListTelegram�̃e�X�g�B <br />
     * �E������{"file1.jvn"}�ɂ��āAcreateJvnLogListTelegram���Ăяo���B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogListTelegram_FileNum1()
    {
        // ����
        String[] jvnFileNames = {"file1.jvn"};

        // ���s
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail = {"file1.jvn"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 1, detail,
                                  body[0]);
    }

    /**
     * [����] 3-2-3 createJvnLogListTelegram�̃e�X�g�B <br />
     * �E������{"file1.jvn","file2.jvn","file3.jvn"}�ɂ��āAcreateJvnLogListTelegram���Ăяo���B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreateJvnLogListTelegram_FileNum3()
    {
        // ����
        String[] jvnFileNames = {"file1.jvn", "file2.jvn", "file3.jvn"};

        // ���s
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        Header header = telegram.getObjHeader();
        Body[] body = telegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, header.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, header.getByteRequestKind());

        String[] detail = {"file1.jvn", "file2.jvn", "file3.jvn"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 3, detail,
                                  body[0]);
    }
}
