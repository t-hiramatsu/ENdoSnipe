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
import jp.co.acroquest.test.util.JavelinTestUtil;
import junit.framework.TestCase;

/**
 * JVN���O�_�E�����[�h�@�\�̃e�X�g�R�[�h
 * @author fujii
 *
 */
public class JvnLogDownloadTelegramListenerTest extends TestCase implements TelegramConstants
{
    private static final String CONF_PATH = "/telegram/conf/javelin.properties";
    private static final String LOGS_PATH = "/telegram/logs";
    
    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinTestUtil.camouflageJavelinConfig(getClass(), CONF_PATH);
        String logDir = JavelinTestUtil.getAbsolutePath(getClass(), LOGS_PATH);
        this.config_ = new JavelinConfig();
        this.config_.setJavelinFileDir(logDir);
        SystemLogger.initSystemLog(this.config_);
    }

    /**
     * [����] 3-1-5 receiveTelegram�̃e�X�g�B <br />
     * �EJVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_FileNum1()
        throws Exception
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE);
        String[] inputDetail = {"file1.jvn"};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("jvnFile", "jvnFileName", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_JVN_FILE, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        String[] detail1 = {"file1.jvn"};
        String[] detail2 = {"contentOfFirstJavelinFile"};

        // 1�Ԗڂ�Body�̌���
        AssertUtil.assertTelegram("jvnFile", "jvnFileName", BYTE_ITEMMODE_KIND_STRING, 1, detail1,
                                  receiveBody[0]);
        AssertUtil.assertTelegram("jvnFile", "jvnFileContent", BYTE_ITEMMODE_KIND_STRING, 1,
                                  detail2, receiveBody[1]);
    }

    /**
     * [����] 3-1-6 receiveTelegram�̃e�X�g�B <br />
     * �E�d����ʂ�JVN���O�o�͒ʒm�ȊO{1,2,3,4,5,7,8,9}�ɂ��āA
     *  JVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_RequestKindOthers()
        throws Exception
    {
        byte[] requestKinds = {1, 2, 3, 4, 5, 7, 8, 9};

        for (byte requestKind : requestKinds)
        {

            // ����
            Header sendHeader =
                    CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST, requestKind);
            String[] inputDetail = {"file1.jvn"};
            Body sendBody =
                    CreateTelegramUtil.createBodyValue("jvnFile", "jvnFileName", ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-1-7 receiveTelegram�̃e�X�g�B <br />
     * �E�d��������ʂ�v���ȊO{0,2}�ɂ��āA
     *  JVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_TelegramKindOthers()
        throws Exception
    {
        byte[] telegramKinds = {0, 2};

        for (byte telegramKind : telegramKinds)
        {

            // ����
            Header sendHeader =
                    CreateTelegramUtil.createHeader(telegramKind, BYTE_TELEGRAM_KIND_JVN_FILE);
            String[] inputDetail = {"file1.jvn"};
            Body sendBody =
                    CreateTelegramUtil.createBodyValue("jvnFile", "jvnFileName", ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-1-8 receiveTelegram�̃e�X�g�B <br />
     * �E�I�u�W�F�N�g����"test"�ɂ��āAJVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d����null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_ObjNameOther()
        throws Exception
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE);
        String[] inputDetail = {"file1.jvn"};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("test", "jvnFileName", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        assertNull(receiveTelegram);
    }

    /**
     * [����] 3-1-9 receiveTelegram�̃e�X�g�B <br />
     * �E���ږ���"testItem"�ɂ��āAJVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d����null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_ItemNameOther()
        throws Exception
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE);
        String[] inputDetail = {"file1.jvn"};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("jvnFile", "testItem", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        assertNull(receiveTelegram);
    }

    /**
     * [����] 3-1-10 receiveTelegram�̃e�X�g�B <br />
     * �E�ڍׂ���ɂ��āAJVN���O�_�E�����[�h�擾�p�̓d������M����B
     * ���쐬�����d����null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_DetailEmpty()
        throws Exception
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_JVN_FILE);
        String[] inputDetail = {};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("jvnFile", "jvnFileName", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        JvnLogDownloadTelegramListener listener = new JvnLogDownloadTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        assertNull(receiveTelegram);
    }

}
