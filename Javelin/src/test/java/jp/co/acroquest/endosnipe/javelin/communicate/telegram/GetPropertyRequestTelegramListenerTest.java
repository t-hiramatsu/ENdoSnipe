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

import java.util.HashMap;
import java.util.Map;

import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
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

/**
 * �T�[�o�v���p�e�B�擾�e�X�g
 * @author fujii
 *
 */
public class GetPropertyRequestTelegramListenerTest extends DJUnitTestCase implements
        TelegramConstants
{
    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

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
    }

    /**
     * @test �T�[�o�v���p�e�B�ݒ�d��-�擾�����d���F�p�^�[���P(����:4-1-1)
     * @condition 
     * @result
     */
    public void testReceiveTelegram_Get_Pattern1()
    {
        // ����
        GetPropertyRequestTelegramListener telegramListener =
                new GetPropertyRequestTelegramListener();

        Telegram request = new Telegram();
        Header header = new Header();
        header.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_PROPERTY);
        header.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
        request.setObjHeader(header);

        // ���Ғl
        byte expectedTelegramKind = BYTE_TELEGRAM_KIND_GET_PROPERTY;
        byte expectedRequestKind = BYTE_REQUEST_KIND_RESPONSE;
        Map<String, String> expectedConfigMap = createPattern1();

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
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
            assertEquals(value, body.getStrItemName());
        }
    }

    /**
     * @test �T�[�o�v���p�e�B�ݒ�d��-�擾�����d���F�p�^�[��2(����:4-1-2)
     * @condition 
     * @result
     */
    public void testReceiveTelegram_Get_Pattern2()
    {
        // ����
        GetPropertyRequestTelegramListener telegramListener =
                new GetPropertyRequestTelegramListener();

        Telegram request = new Telegram();
        Header header = new Header();
        header.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_PROPERTY);
        header.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
        request.setObjHeader(header);
        HashMap<String, String> configMap = createPattern2();
        addReturnValue(ConfigUpdater.class, "getUpdatableConfig", configMap);

        // ���Ғl
        byte expectedTelegramKind = BYTE_TELEGRAM_KIND_GET_PROPERTY;
        byte expectedRequestKind = BYTE_REQUEST_KIND_RESPONSE;
        Map<String, String> expectedConfigMap = configMap;

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
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
            assertEquals(value, body.getStrItemName());
        }
    }

    private Map<String, String> createPattern1()
    {
        return ConfigUpdater.getUpdatableConfig();
    }

    private HashMap<String, String> createPattern2()
    {
        HashMap<String, String> configMap = new HashMap<String, String>();
        configMap.put("key1", "value1");
        configMap.put("key2", "value2");
        configMap.put("key3", "value3");
        return configMap;
    }
    
    /**
     * [����] 3-3-1 createPropertyResponse�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肵�āAcreatePropertyResponse���\�b�h�����s����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testCreatePropertyResponse()
    {
        // ����
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("javelin.alarmThreshold", "0");
        resultMap.put("javelin.alarmCpuThreshold", "0");
        resultMap.put("javelin.log.http.session", "true");
        resultMap.put("javelin.log.http.session.detail", "false");
        resultMap.put("javelin.log.http.session.detail.depth", "1");
        resultMap.put("javelin.log.mbeaninfo.root", "true");
        resultMap.put("javelin.log.mbeaninfo", "false");
        resultMap.put("javelin.log.args", "false");
        resultMap.put("javelin.log.args.detail", "false");
        resultMap.put("javelin.log.args.detail.depth", "0");
        resultMap.put("javelin.log.return", "false");
        resultMap.put("javelin.log.return.detail", "false");
        resultMap.put("javelin.log.return.detail.depth", "0");
        resultMap.put("javelin.log.stacktrace", "false");
        resultMap.put("javelin.leak.collection.monitor", "true");
        resultMap.put("javelin.leak.collectionSizeThreshold", "0");
        resultMap.put("javelin.leak.collectionSizeOut", "false");
        resultMap.put("javelin.leak.class.histo", "true");
        resultMap.put("javelin.leak.class.histo.interval", "60000");
        resultMap.put("javelin.leak.class.histo.max", "15");
        resultMap.put("javelin.leak.class.histo.gc", "false");
        resultMap.put("javelin.linearsearch.monitor", "true");
        resultMap.put("javelin.linearsearch.size", "100");
        resultMap.put("javelin.linearsearch.ratio", "5.0");
        resultMap.put("javelin.net.input.monitor", "false");
        resultMap.put("javelin.net.output.monitor", "false");
        resultMap.put("javelin.file.input.monitor", "false");
        resultMap.put("javelin.file.output.monitor", "false");
        resultMap.put("javelin.finalizationCount.monitor", "true");
        resultMap.put("javelin.interval.monitor", "true");
        resultMap.put("javelin.thread.monitor", "true");
        resultMap.put("javelin.thread.monitor.interval", "1000");
        resultMap.put("javelin.thread.monitor.depth", "10");
        resultMap.put("javelin.thread.block.threshold", "10");
        resultMap.put("javelin.thread.blocktime.threshold", "2000");
        resultMap.put("javelin.thread.dump.monitor", "false");
        resultMap.put("javelin.thread.dump.interval", "10000");
        resultMap.put("javelin.thread.dump.threadnum", "100");
        resultMap.put("javelin.thread.dump.cpu", "50");
        resultMap.put("javelin.fullgc.monitor", "true");
        resultMap.put("javelin.fullgc.threshold", "5000");
        resultMap.put("javelin.thread.deadlock.monitor", "false");
        resultMap.put("javelin.minimumAlarmInterval", "60000");
        resultMap.put("javelin.tat.monitor", "true");
        resultMap.put("javelin.tat.keepTime", "15000");
        resultMap.put("javelin.httpSessionCount.monitor", "true");
        resultMap.put("javelin.httpSessionSize.monitor", "true");
        resultMap.put("javelin.concurrent.monitor", "true");
        resultMap.put("javelin.timeout.monitor", "true");
        resultMap.put("javelin.log4j.printstack.level", "ERROR");
        resultMap.put("javelin.call.tree.enable", "true");
        resultMap.put("javelin.call.tree.max", "5000");
        resultMap.put("javelin.log.enable", "true");
        resultMap.put("javelin.resource.collectSystemResources", "true");
        resultMap.put("javelin.record.invocation.sendFullEvent", "true");
        resultMap.put("javelin.event.level", "WARN");
        resultMap.put("javelin.jdbc.enable", "true");
        resultMap.put("javelin.jdbc.execPlanThreshold", "0");
        resultMap.put("javelin.jdbc.oracle.allowSqlTrace", "false");
        resultMap.put("javelin.jdbc.postgres.verbosePlan", "false");
        resultMap.put("javelin.jdbc.recordBindVal", "true");
        resultMap.put("javelin.jdbc.recordDuplJdbcCall", "false");
        resultMap.put("javelin.jdbc.recordExecPlan", "false");
        resultMap.put("javelin.jdbc.stringLimitLength", "102400");
        resultMap.put("javelin.jdbc.sqlcount.monitor", "true");
        resultMap.put("javelin.jdbc.sqlcount", "20");
        resultMap.put("javelin.jdbc.record.stackTrace", "true");
        resultMap.put("javelin.jdbc.record.stacktraceThreashold", "0");
        resultMap.put("javelin.threadModel", "0");
        resultMap.put("javelin.alarmException", "false");

        // ���s
        Telegram receiveTelegram =
                GetPropertyRequestTelegramListener.createPropertyResponse(0, BYTE_TELEGRAM_KIND_GET_PROPERTY);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_GET_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
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
     * [����] 3-3-2 receiveTelegram�̃e�X�g�B <br />
     * �E�v���p�e�B�l��ݒ肵�āAreceiveTelegram���\�b�h�����s����B
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_Sucess()
        throws Exception
    {
        // ����

        // ���ؗp�f�[�^��Map�ɓ��͂���B
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("javelin.alarmThreshold", "0");
        resultMap.put("javelin.alarmCpuThreshold", "0");
        resultMap.put("javelin.log.http.session", "true");
        resultMap.put("javelin.log.http.session.detail", "false");
        resultMap.put("javelin.log.http.session.detail.depth", "1");
        resultMap.put("javelin.log.mbeaninfo.root", "true");
        resultMap.put("javelin.log.mbeaninfo", "false");
        resultMap.put("javelin.log.args", "false");
        resultMap.put("javelin.log.args.detail", "false");
        resultMap.put("javelin.log.args.detail.depth", "0");
        resultMap.put("javelin.log.return", "false");
        resultMap.put("javelin.log.return.detail", "false");
        resultMap.put("javelin.log.return.detail.depth", "0");
        resultMap.put("javelin.log.stacktrace", "false");
        resultMap.put("javelin.leak.collection.monitor", "true");
        resultMap.put("javelin.leak.collectionSizeThreshold", "0");
        resultMap.put("javelin.leak.collectionSizeOut", "false");
        resultMap.put("javelin.leak.class.histo", "true");
        resultMap.put("javelin.leak.class.histo.interval", "60000");
        resultMap.put("javelin.leak.class.histo.max", "15");
        resultMap.put("javelin.leak.class.histo.gc", "false");
        resultMap.put("javelin.linearsearch.monitor", "true");
        resultMap.put("javelin.linearsearch.size", "100");
        resultMap.put("javelin.linearsearch.ratio", "5.0");
        resultMap.put("javelin.net.input.monitor", "false");
        resultMap.put("javelin.net.output.monitor", "false");
        resultMap.put("javelin.file.input.monitor", "false");
        resultMap.put("javelin.file.output.monitor", "false");
        resultMap.put("javelin.finalizationCount.monitor", "true");
        resultMap.put("javelin.interval.monitor", "true");
        resultMap.put("javelin.thread.monitor", "true");
        resultMap.put("javelin.thread.monitor.interval", "1000");
        resultMap.put("javelin.thread.monitor.depth", "10");
        resultMap.put("javelin.thread.block.threshold", "10");
        resultMap.put("javelin.thread.blocktime.threshold", "2000");
        resultMap.put("javelin.thread.dump.monitor", "false");
        resultMap.put("javelin.thread.dump.interval", "10000");
        resultMap.put("javelin.thread.dump.threadnum", "100");
        resultMap.put("javelin.thread.dump.cpu", "50");
        resultMap.put("javelin.fullgc.monitor", "true");
        resultMap.put("javelin.fullgc.threshold", "5000");
        resultMap.put("javelin.thread.deadlock.monitor", "false");
        resultMap.put("javelin.minimumAlarmInterval", "60000");
        resultMap.put("javelin.tat.monitor", "true");
        resultMap.put("javelin.tat.keepTime", "15000");
        resultMap.put("javelin.httpSessionCount.monitor", "true");
        resultMap.put("javelin.httpSessionSize.monitor", "true");
        resultMap.put("javelin.concurrent.monitor", "true");
        resultMap.put("javelin.timeout.monitor", "true");
        resultMap.put("javelin.log4j.printstack.level", "ERROR");
        resultMap.put("javelin.call.tree.enable", "true");
        resultMap.put("javelin.call.tree.max", "5000");
        resultMap.put("javelin.log.enable", "true");
        resultMap.put("javelin.resource.collectSystemResources", "true");
        resultMap.put("javelin.record.invocation.sendFullEvent", "true");
        resultMap.put("javelin.event.level", "WARN");
        resultMap.put("javelin.jdbc.enable", "true");
        resultMap.put("javelin.jdbc.execPlanThreshold", "0");
        resultMap.put("javelin.jdbc.oracle.allowSqlTrace", "false");
        resultMap.put("javelin.jdbc.postgres.verbosePlan", "false");
        resultMap.put("javelin.jdbc.recordBindVal", "true");
        resultMap.put("javelin.jdbc.recordDuplJdbcCall", "false");
        resultMap.put("javelin.jdbc.recordExecPlan", "false");
        resultMap.put("javelin.jdbc.stringLimitLength", "102400");
        resultMap.put("javelin.jdbc.sqlcount.monitor", "true");
        resultMap.put("javelin.jdbc.sqlcount", "20");
        resultMap.put("javelin.jdbc.record.stackTrace", "true");
        resultMap.put("javelin.jdbc.record.stacktraceThreashold", "0");
        resultMap.put("javelin.threadModel", "0");
        resultMap.put("javelin.alarmException", "false");

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_GET_PROPERTY);
        String[] inputDetail = {""};
        Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        GetPropertyRequestTelegramListener listener = new GetPropertyRequestTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_GET_PROPERTY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        assertEquals(resultMap.size(), receiveBody.length);
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
     * [����] 3-3-3 receiveTelegram�̃e�X�g�B <br />
     * �E�d����ʂ��T�[�o�v���p�e�B�擾�ʒm�ȊO�̓d���ɑ΂��āA<br />
     * receiveTelegram���\�b�h�����s����B<br />
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testReceiveTelegram_RequestKindOthers()
        throws Exception
    {
        byte[] requestKinds = {1, 2, 3, 4, 5, 6, 8, 9};

        for (byte requestKind : requestKinds)
        {
            // ����
            Header sendHeader =
                    CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST, requestKind);
            String[] inputDetail = {""};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            GetPropertyRequestTelegramListener listener = new GetPropertyRequestTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-3-4 receiveTelegram�̃e�X�g�B <br />
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
                    CreateTelegramUtil.createHeader(telegramKind, BYTE_TELEGRAM_KIND_GET_PROPERTY);
            String[] inputDetail = {""};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, inputDetail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            GetPropertyRequestTelegramListener listener = new GetPropertyRequestTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }
}
