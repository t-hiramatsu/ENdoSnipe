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

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.AssertUtil;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.CreateTelegramUtil;
import junit.framework.TestCase;

/**
 * JVN���O�t�@�C���ꗗ�擾�̃e�X�g�N���X�B
 * @author fujii
 *
 */
public class JvnLogListTelegramListenerTest extends TestCase implements TelegramConstants
{
    private static final String BASE_DIR = "./src/test/resources/telegram";
    
    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp()
    {
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        this.config_ = new JavelinConfig(BASE_DIR + "/conf");
        this.config_.setJavelinFileDir(BASE_DIR);
        SystemLogger.initSystemLog(this.config_);
    }

    /**
     * [����] 3-2-4 receiveTelegram�̃e�X�g�B <br />
     * �E�t�@�C������1�̃t�H���_�ɑ΂��郍�O�t�@�C���ꗗ�d���̎擾�B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_FileNum1()
    {
        this.config_.setJavelinFileDir(BASE_DIR + "/fileNum1");

        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {"file1.jvn"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 1, detail1,
                                  receiveBody[0]);
    }

    /**
     * [����] 3-2-5 receiveTelegram�̃e�X�g�B <br />
     * �E�t�@�C������3�̃t�H���_�ɑ΂��郍�O�t�@�C���ꗗ�d���̎擾�B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_FileNum3()
    {
        this.config_.setJavelinFileDir(BASE_DIR + "/fileNum3");

        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {"file1.jvn", "file2.jvn", "file3.jvn"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 3, detail1,
                                  receiveBody[0]);
    }

    /**
     * [����] 3-2-6 receiveTelegram�̃e�X�g�B <br />
     * �EJvn�t�@�C����2�A����ȊO�̃t�@�C����2�����Ă���t�H���_
     *  �ɑ΂��郍�O�t�@�C���ꗗ�d���̎擾�B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_JvnFileNum2_OtherFileNum2()
    {
        this.config_.setJavelinFileDir(BASE_DIR + "/jvn2_other2");

        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {"file1.jvn", "file2.jvn"};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 2, detail1,
                                  receiveBody[0]);
    }

    /**
     * [����] 3-2-7 receiveTelegram�̃e�X�g�B <br />
     * �EJvn�t�@�C���������Ă��Ȃ��t�H���_�ɑ΂��郍�O�t�@�C���ꗗ�d���̎擾�B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_OtherFiles()
    {
        this.config_.setJavelinFileDir(BASE_DIR + "/otherFiles");

        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 0, detail1,
                                  receiveBody[0]);
    }

    /**
     * [����] 3-2-8 receiveTelegram�̃e�X�g�B <br />
     * �E�t�@�C������̃t�H���_�ɑ΂��郍�O�t�@�C���ꗗ�d���̎擾�B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_FileNum0()
    {
        this.config_.setJavelinFileDir(BASE_DIR + "/fileNum0");

        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE_LIST, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {};

        // Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 0, detail1,
                                  receiveBody[0]);
    }

    /**
     * [����] 3-2-9 receiveTelegram�̃e�X�g�B <br />
     * �E�d����ʂ����O�t�@�C���ꗗ�擾�łȂ��Ƃ��ɁA
     *  receiveTeleram���\�b�h���ĂԁB
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_RequestKindOthers()
    {
        // ����
        byte[] requestKinds = {1, 2, 3, 4, 5, 6, 7, 8};

        for (byte requestKind : requestKinds)
        {

            Header sendHeader =
                    CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST, requestKind);
            Object[] detail = {new Object()};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-2-10 receiveTelegram�̃e�X�g�B <br />
     * �E�d��������ʂ��v���łȂ��Ƃ��ɁA
     *  receiveTeleram���\�b�h���ĂԁB
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_TelegramKindOthers()
    {
        // ����
        byte[] telegramKinds = {0, 2};

        for (byte telegramKind : telegramKinds)
        {

            Header sendHeader =
                    CreateTelegramUtil.createHeader(telegramKind, BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
            Object[] detail = {new Object()};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-2-11 receiveTelegram�̃e�X�g�B <br />
     * �E���݂��Ȃ��t�H���_�ɑ΂��āAreceiveTeleram���\�b�h���ĂԁB
     * ���쐬�����d����null�ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_FolderNotExist()
    {
        // ����
        this.config_.setJavelinFileDir(BASE_DIR + "/FolderNotExist");

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE_LIST);
        Object[] detail = {new Object()};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogListTelegramListener listener = new JvnLogListTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        assertNull(receiveTelegram);
    }

}
