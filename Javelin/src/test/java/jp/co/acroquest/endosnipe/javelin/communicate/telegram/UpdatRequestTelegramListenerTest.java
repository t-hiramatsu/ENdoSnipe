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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.communicator.util.TelegramAssertionUtil;
import jp.co.acroquest.endosnipe.javelin.common.ConfigUpdater;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.CreateTelegramUtil;
import jp.co.acroquest.test.util.JavelinTestUtil;
import junit.framework.TestCase;

/**
 * �T�[�o�v���p�e�B�X�V�p�e�X�g�N���X
 * @author fujii
 *
 */
public class UpdatRequestTelegramListenerTest extends TestCase implements TelegramConstants
{
    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

    private static String[] resultObj_ = {"javelin.alarmThreshold", "javelin.alarmCpuThreshold",
            "javelin.alarmException", "javelin.log.args", "javelin.log.args.detail",
            "javelin.log.args.detail.depth", "javelin.log.return", "javelin.log.return.detail",
            "javelin.log.return.detail.depth", "javelin.log.stacktrace",
            "javelin.log.http.session", "javelin.log.http.session.detail",
            "javelin.log.http.session.detail.depth", "javelin.log.mbeaninfo",
            "javelin.log.mbeaninfo.root", "javelin.event.level", "javelin.threadModel",
            "javelin.leak.collection.monitor", "javelin.leak.collectionSizeThreshold",
            "javelin.leak.collectionSizeOut", "javelin.leak.class.histo",
            "javelin.leak.class.histo.interval", "javelin.leak.class.histo.max",
            "javelin.leak.class.histo.gc", "javelin.linearsearch.monitor",
            "javelin.linearsearch.size", "javelin.linearsearch.ratio", "javelin.net.input.monitor",
            "javelin.net.output.monitor", "javelin.file.input.monitor",
            "javelin.file.output.monitor", "javelin.finalizationCount.monitor",
            "javelin.interval.monitor", "javelin.thread.monitor",
            "javelin.thread.monitor.interval", "javelin.thread.monitor.depth",
            "javelin.thread.block.threshold", "javelin.thread.blocktime.threshold",
            "javelin.thread.dump.monitor", "javelin.thread.dump.interval",
            "javelin.thread.dump.threadnum", "javelin.thread.dump.cpu", "javelin.fullgc.monitor",
            "javelin.fullgc.threshold", "javelin.thread.deadlock.monitor",
            "javelin.minimumAlarmInterval", "javelin.tat.monitor", "javelin.tat.keepTime",
            "javelin.httpSessionCount.monitor", "javelin.httpSessionSize.monitor",
            "javelin.concurrent.monitor", "javelin.timeout.monitor",
            "javelin.log4j.printstack.level", "javelin.call.tree.enable", "javelin.call.tree.max",
            "javelin.log.enable", "javelin.resource.collectSystemResources",
            "javelin.record.invocation.sendFullEvent", "javelin.jdbc.enable",
            "javelin.jdbc.recordExecPlan", "javelin.jdbc.execPlanThreshold",
            "javelin.jdbc.recordDuplJdbcCall", "javelin.jdbc.recordBindVal",
            "javelin.jdbc.stringLimitLength", "javelin.jdbc.sqlcount.monitor",
            "javelin.jdbc.sqlcount", "javelin.jdbc.oracle.allowSqlTrace",
            "javelin.jdbc.postgres.verbosePlan", "javelin.jdbc.record.stackTrace",
            "javelin.jdbc.record.stacktraceThreashold"};

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp()
        throws Exception
    {
        super.setUp();
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        MockObjectManager.initialize();
        JavelinTestUtil.camouflageJavelinConfig(getClass(), "/telegram/conf/javelin.properties");
        this.config_ = new JavelinConfig();
        SystemLogger.initSystemLog(this.config_);

        // �C�����������N���X�̃C���X�^���X���쐬����B
        JavelinConfigUtil configUtil = JavelinConfigUtil.getInstance();
        Class<JavelinConfigUtil> cls = JavelinConfigUtil.class;
        Method method = cls.getDeclaredMethod("load", (Class[])null);
        method.setAccessible(true);

        method.invoke(configUtil, (Object[])null);

    }

    /**
     * @throws Exception 
     * @test �T�[�o�v���p�e�B�ݒ�d��-�擾�����d���F�p�^�[���P(����:4-1-3)
     * @condition 
     * @result
     */
    public void testReceiveTelegram_Update_Pattern1()
        throws Exception
    {
        // ����
        UpdateRequestTelegramListener telegramListener = new UpdateRequestTelegramListener();

        String[] propertyLines = new String[]{"javelin.call.tree.max=50000"};
        Telegram request = createRequestTelegram(propertyLines);

        // ���Ғl
        byte expectedTelegramKind = BYTE_TELEGRAM_KIND_UPDATE_PROPERTY;
        byte expectedRequestKind = BYTE_REQUEST_KIND_RESPONSE;
        Map<String, String> expectedConfigMap = createPattern1();
        expectedConfigMap.put("javelin.call.tree.max", "50000");

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertTelegram(expectedTelegramKind, expectedRequestKind, expectedConfigMap, response);
    }

    /**
     * @test �T�[�o�v���p�e�B�ݒ�d��-�擾�����d���F�p�^�[��2(����:4-1-4)
     * @condition 
     * @result
     */
    public void testReceiveTelegram_Update_Pattern2()
    {
        // ����
        UpdateRequestTelegramListener telegramListener = new UpdateRequestTelegramListener();

        String[] propertyLines = new String[]{"javelin.log.args=true"};
        Telegram request = createRequestTelegram(propertyLines);

        // ���Ғl
        byte expectedTelegramKind = BYTE_TELEGRAM_KIND_UPDATE_PROPERTY;
        byte expectedRequestKind = BYTE_REQUEST_KIND_RESPONSE;
        Map<String, String> expectedConfigMap = createPattern1();
        expectedConfigMap.put("javelin.log.args", "true");

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertTelegram(expectedTelegramKind, expectedRequestKind, expectedConfigMap, response);
    }

    private void assertTelegram(byte expectedTelegramKind, byte expectedRequestKind,
            Map<String, String> expectedConfigMap, Telegram response)
    {
        TelegramAssertionUtil.assertHeader(expectedTelegramKind, expectedRequestKind,
                                           response.getObjHeader());

        // Body�̌���
        for (Body body : response.getObjBody())
        {
            String value = expectedConfigMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail();
            }
            assertEquals(body.getStrObjName(), value, body.getStrItemName());
        }
    }

    private Telegram createRequestTelegram(String[] propertyLines)
    {
        Telegram request = new Telegram();
        Header header = new Header();
        header.setByteTelegramKind(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY);
        header.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
        request.setObjHeader(header);

        List<Body> updatePropertyList = new ArrayList<Body>();

        for (String propertyLine : propertyLines)
        {
            String[] propertyLineArray = propertyLine.split("=");
            String updateProperty = propertyLineArray[0];
            String updateValue = propertyLineArray[1];

            if (updateValue == null || "".equals(updateValue))
            {
                continue;
            }

            Body addParam = new Body();
            addParam.setStrObjName(updateProperty);
            addParam.setStrItemName(updateValue);
            addParam.setObjItemValueArr(new Object[]{});

            updatePropertyList.add(addParam);
        }

        Body[] updatePropertyArray =
                                     updatePropertyList.toArray(new Body[updatePropertyList.size()]);

        request.setObjBody(updatePropertyArray);
        return request;
    }

    private Map<String, String> createPattern1()
    {
        return ConfigUpdater.getUpdatableConfig();
    }

    /**
     * [����] 3-4-1 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肷��B<br />
     * �E�X�V�p�̃f�[�^��d���ɓ��͂����AreceiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_NoRefresh()
        throws Exception
    {
        // ����
        String resultItem[] =
                              {"0", "0", "false", "false", "false", "0", "false", "false", "0",
                                      "false", "true", "false", "1", "false", "true", "WARN", "0",
                                      "true", "0", "false", "true", "60000", "15", "false", "true",
                                      "100", "5.0", "false", "false", "false", "false", "true",
                                      "true", "true", "1000", "10", "10", "2000", "false", "10000",
                                      "100", "50", "true", "5000", "false", "60000", "true",
                                      "15000", "true", "true", "true", "true", "ERROR", "true",
                                      "5000", "true", "true", "true", "true", "false", "0",
                                      "false", "true", "102400", "true", "20", "false", "false",
                                      "true", "0"};

        Map<String, String> resultMap = makeResultMap(resultItem);

        String[] sendItem = {};
        String[] sendObjArray = {};

        Telegram sendTelegram = createTelegram(sendObjArray, sendItem);

        UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // �S�Ă�Body�̌���
        for (Body body : receiveBody)
        {
            String value = resultMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail(body.getStrObjName() + " is not defined.");
            }
            assertEquals(body.getStrObjName(), value, body.getStrItemName());
        }

    }

    /**
     * [����] 3-4-2 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肷��B<br />
     * �E�X�V�p�̃f�[�^��d���ɓ��͂��āAreceiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�̔���
     */
    public void testReceiveTelegram_Refresh()
        throws Exception
    {
        // ����

        String resultItem[] =
                              {"1000", "1000", "true", "true", "true", "1000", "true", "true",
                                      "1000", "true", "false", "true", "1000", "true", "false",
                                      "ERROR", "1000", "false", "0", "true", "false", "1000",
                                      "1000", "true", "false", "1000", "1000.0", "true", "true",
                                      "true", "true", "false", "false", "false", "1000", "1000",
                                      "1000", "1000", "true", "1000", "1000", "1000", "false",
                                      "1000", "true", "1000", "false", "1000", "false", "false",
                                      "false", "false", "WARN", "false", "1000", "false", "false",
                                      "false", "false", "true", "1000", "true", "false", "1000",
                                      "false", "1000", "true", "true", "false", "1000"};

        Map<String, String> resultMap = makeResultMap(resultItem);

        Telegram sendTelegram = createTelegram(resultObj_, resultItem);

        UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // �S�Ă�Body�̌���
        for (Body body : receiveBody)
        {
            String value = resultMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail();
            }
            assertEquals(body.getStrObjName(), value, body.getStrItemName());
        }
    }

    /**
     * [����] 3-4-3 receiveTelegram�̃e�X�g�B <br />
     * �E�d����ʂ��T�[�o�v���p�e�B�X�V�ʒm�ȊO�̓d���ɑ΂��āA<br />
     * receiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     *
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_RequestKindOthers()
        throws Exception
    {
        byte[] requestKinds = {1, 2, 3, 4, 5, 6, 7, 9};

        for (byte requestKind : requestKinds)
        {
            // ����
            Header sendHeader =
                                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                                requestKind);
            String[] inputDetail = {""};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-4-4 receiveTelegram�̃e�X�g�B <br />
     * �E�d��������ʂ��v���ȊO�̓d���ɑ΂��āA<br />
     * receiveTelegram���\�b�h�����s����B<br />
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
                                CreateTelegramUtil.createHeader(telegramKind,
                                                                BYTE_TELEGRAM_KIND_UPDATE_PROPERTY);
            String[] inputDetail = {""};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-4-5 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肷��B<br />
     * �E�I�u�W�F�N�g�����p�����[�^���ɂȂ��d�����쐬���AreceiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�̔���
     */
    public void testReceiveTelegram_ObjNotExist()
        throws Exception
    {
        // ����
        String resultItem[] =
                              {"0", "0", "false", "false", "false", "0", "false", "false", "0",
                                      "false", "true", "false", "1", "false", "true", "WARN", "0",
                                      "true", "0", "false", "true", "60000", "15", "false", "true",
                                      "100", "5.0", "false", "false", "false", "false", "true",
                                      "true", "true", "1000", "10", "10", "2000", "false", "10000",
                                      "100", "50", "true", "5000", "false", "60000", "true",
                                      "15000", "true", "true", "true", "true", "ERROR", "true",
                                      "5000", "true", "true", "true", "true", "false", "0",
                                      "false", "true", "102400", "true", "20", "false", "false",
                                      "true", "0"};

        Map<String, String> resultMap = makeResultMap(resultItem);

        String[] sendObjArray = {"test"};
        String[] sendItemArray = {"1000"};

        Telegram sendTelegram = createTelegram(sendObjArray, sendItemArray);
        UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // �S�Ă�Body�̌���
        for (Body body : receiveBody)
        {
            String value = resultMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail();
            }
            assertEquals(value, body.getStrItemName());
        }
    }

    /**
     * [����] 3-4-6 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肷��B<br />
     * �E���ږ�(int)�ɕs���Ȓl(String)����͂����d�����쐬���A<br />
     *  receiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * �V�X�e�����O�̖ڎ��̕K�v����B
     * 
     * @throws Exception ��O�̔���
     */
    public void testReceiveTelegram_itemName_IntToString()
        throws Exception
    {
        // ����
        String resultItem[] =
                              {"0", "0", "false", "false", "false", "0", "false", "false", "0",
                                      "false", "true", "false", "1", "false", "true", "WARN", "0",
                                      "true", "0", "false", "true", "60000", "15", "false", "true",
                                      "100", "5.0", "false", "false", "false", "false", "true",
                                      "true", "true", "1000", "10", "10", "2000", "false", "10000",
                                      "100", "50", "true", "5000", "false", "60000", "true",
                                      "15000", "true", "true", "true", "true", "ERROR", "true",
                                      "5000", "true", "true", "true", "true", "false", "0",
                                      "false", "true", "102400", "true", "20", "false", "false",
                                      "true", "0"};

        Map<String, String> resultMap = makeResultMap(resultItem);

        String[] sendObjArray = {"javelin.alarmThreshold"};
        String[] sendItemArray = {"test"};

        Telegram sendTelegram = createTelegram(sendObjArray, sendItemArray);

        UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // �S�Ă�Body�̌���
        for (Body body : receiveBody)
        {
            String value = resultMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail();
            }
            assertEquals(value, body.getStrItemName());
        }
    }

    /**
     * [����] 3-4-7 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肷��B<br />
     * �E���ږ�(boolean)�ɕs���Ȓl(String)����͂����d�����쐬���A<br />
     *  receiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * �V�X�e�����O�̖ڎ��̕K�v����B
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_itemName_BooleanToString()
        throws Exception
    {
        String resultItem[] =
                              {"0", "0", "false", "false", "false", "0", "false", "false", "0",
                                      "false", "true", "false", "1", "false", "true", "WARN", "0",
                                      "true", "0", "false", "true", "60000", "15", "false", "true",
                                      "100", "5.0", "false", "false", "false", "false", "true",
                                      "true", "true", "1000", "10", "10", "2000", "false", "10000",
                                      "100", "50", "true", "5000", "false", "60000", "true",
                                      "15000", "true", "true", "true", "true", "ERROR", "true",
                                      "5000", "true", "true", "true", "true", "false", "0",
                                      "false", "true", "102400", "true", "20", "false", "false",
                                      "true", "0"};

        // ����
        Map<String, String> resultMap = makeResultMap(resultItem);

        String[] sendObjArray = {"javelin.jdbc.oracle.allowSqlTrace"};
        String[] sendItemArray = {"test"};

        Telegram sendTelegram = createTelegram(sendObjArray, sendItemArray);

        UpdateRequestTelegramListener listener = new UpdateRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_UPDATE_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // �S�Ă�Body�̌���
        for (Body body : receiveBody)
        {
            String value = resultMap.get(body.getStrObjName());
            // �\�������p�����[�^�������Ă��Ȃ��Ƃ��ɂ́A���؂Ɏ��s�B
            if (value == null)
            {
                fail();
            }
            assertEquals(value, body.getStrItemName());
        }
    }

    /**
     * ���ʂ�Map���쐬����B
     * @param resultItem ���ږ��̌���
     * @return ���ʂ�ۑ�����Map
     */
    private synchronized Map<String, String> makeResultMap(final String[] resultItem)
    {
        Map<String, String> resultMap = new HashMap<String, String>();

        //�@���ؗp�f�[�^��Map�ɕۑ�����B
        for (int num = 0; num < resultObj_.length; num++)
        {
            resultMap.put(resultObj_[num], resultItem[num]);
        }
        return resultMap;
    }

    /**
     * �d�����쐬����B
     * @param sendItemArray �T�[�o����M���鍀�ږ��̔z��
     * @return �d��
     */
    private Telegram createTelegram(final String[] sendObjArray, final String[] sendItemArray)
    {
        Header sendHeader =
                            CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                            BYTE_TELEGRAM_KIND_UPDATE_PROPERTY);

        // �x���X�V�͍s��Ȃ�
        Long[] inputDetail = {};

        Telegram sendTelegram = null;

        // ���M���鍀�ڂ̒�����0�̏ꍇ��0�łȂ��ꍇ�ŏ����𕪂���B
        if (sendItemArray.length == 0)
        {
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);
            Body[] sendBodies = {sendBody};
            sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        }
        else
        {
            Body[] sendBodies = new Body[sendItemArray.length];
            for (int num = 0; num < sendItemArray.length; num++)
            {
                sendBodies[num] =
                                  CreateTelegramUtil.createBodyValue(sendObjArray[num],
                                                                     sendItemArray[num],
                                                                     ItemType.ITEMTYPE_SHORT, 0, inputDetail);
            }
            sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        }
        return sendTelegram;
    }
}
