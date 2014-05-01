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

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.RequestBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.communicator.util.TelegramAssertionUtil;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.communicate.JavelinTelegramCreator;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.AssertUtil;
import jp.co.acroquest.endosnipe.javelin.communicate.telegram.util.CreateTelegramUtil;
import junit.framework.TestCase;

/**
 * �V�X�e�����\�[�X�擾�̃e�X�g�N���X�B
 * @author fujii
 */
public class SystemResourceTelegramListenerTest extends TestCase implements TelegramConstants
{
    private static final String BASE_DIR = ".";
    
    /** Javelin�̐ݒ�t�@�C�� */
    private JavelinConfig config_;

    /**
     * @test ���\�[�X�ʒm�����d��-���\�[�X�ʒm�v���O�d���̏���(����:2-1-1)
     * @condition 
     * @result
     */
    public void testCreate_NotResource()
    {
        // ����
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(new Invocation("processName", "className", "methodName", 3000));
        List<Long> accumulatedTimes = null;
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_GET;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();

        // ���{
        Telegram telegram =
                JavelinTelegramCreator.create(invocations, accumulatedTimes, telegramKind,
                                              requestKind);

        Telegram resourceTelegram = telegramListener.receiveTelegram(telegram);

        // ����
        assertNull(resourceTelegram);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-�I�u�W�F�N�g�P��(����:2-1-2)
     * @condition 
     * @result
     */
    public void testCreate_SingleItem()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, ITEMNAME_PROCESS_CPU_TOTAL_TIME);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames = new String[]{ITEMNAME_PROCESS_CPU_TOTAL_TIME};
        byte[] expectedItemModes = new byte[]{BYTE_ITEMMODE_KIND_8BYTE_INT};
        int[] expectedLoopCounts = new int[]{1};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);

    }

    /**
     * @test ���\�[�X�ʒm�����d��-�I�u�W�F�N�g����(����:2-1-3)
     * @condition 
     * @result
     */
    public void testCreate_MultiItem()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request =
                createRequestTelegram(telegramKind, requestKind, ITEMNAME_PROCESS_CPU_TOTAL_TIME,
                                      ITEMNAME_JAVAUPTIME, ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames =
                new String[]{ITEMNAME_PROCESS_CPU_TOTAL_TIME, ITEMNAME_JAVAUPTIME, ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT};
        byte[] expectedItemModes =
                new byte[]{BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_4BYTE_INT};
        int[] expectedLoopCounts = new int[]{1, 1, 1};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-�S�I�u�W�F�N�g(����:2-1-4)
     * @condition 
     * @result
     */
    public void testCreate_AllItem()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        String[] itemNames =
                new String[]{TelegramConstants.ITEMNAME_ACQUIREDTIME, 
                TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX, 
                TelegramConstants.ITEMNAME_JAVAUPTIME, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX, 
                TelegramConstants.ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX,
                TelegramConstants.ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE,
                TelegramConstants.ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT,
                TelegramConstants.ITEMNAME_SYSTEM_MEMORY_SWAP_MAX,
                TelegramConstants.ITEMNAME_SYSTEM_MEMORY_SWAP_FREE, 
                TelegramConstants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX, 
                TelegramConstants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE,
                TelegramConstants.ITEMNAME_NETWORKINPUTSIZEOFPROCESS,
                TelegramConstants.ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS,
                TelegramConstants.ITEMNAME_FILEINPUTSIZEOFPROCESS, 
                TelegramConstants.ITEMNAME_FILEOUTPUTSIZEOFPROCESS, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT, 
                TelegramConstants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL};

        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, itemNames);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames = itemNames;
        byte[] expectedItemModes =
                new byte[]{BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_4BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT, BYTE_ITEMMODE_KIND_8BYTE_INT,
                        BYTE_ITEMMODE_KIND_4BYTE_INT,
                        BYTE_ITEMMODE_KIND_8BYTE_INT,};
        int[] expectedLoopCounts =
                new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-initialize�w��F���I�u�W�F�N�g����(����:2-1-5)
     * @condition 
     * @result
     */
    public void testCreate_InitializeOnly()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, ITEMNAME_INITIALIZE);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        TelegramAssertionUtil.assertHeader(expectedTelegramKind, expectedRequestKind,
                                           response.getObjHeader());
        assertEquals(0, response.getObjBody().length);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-initialize�w��F���I�u�W�F�N�g�L��(����:2-1-6)
     * @condition 
     * @result
     */
    public void testCreate_Initialize()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request =
                createRequestTelegram(telegramKind, requestKind, ITEMNAME_INITIALIZE,
                                      ITEMNAME_CALLTREECOUNT, ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        TelegramAssertionUtil.assertHeader(expectedTelegramKind, expectedRequestKind,
                                           response.getObjHeader());
        assertEquals(0, response.getObjBody().length);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-�n��f�[�^�ݒ�F�Ȃ�(����:2-1-6)
     * @condition 
     * @result
     */
    public void testCreate_NoSeries()
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames = new String[]{ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT + "-name"};
        byte[] expectedItemModes =
                new byte[]{BYTE_ITEMMODE_KIND_4BYTE_INT, BYTE_ITEMMODE_KIND_STRING};
        int[] expectedLoopCounts = new int[]{0, 0};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-�n��f�[�^�ݒ�F�P��(����:2-1-7)
     * @condition 
     * @result
     */
    public void testCreate_SingleSeries() throws Exception
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT);

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames = new String[]{ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT + "-name"};
        byte[] expectedItemModes =
                new byte[]{BYTE_ITEMMODE_KIND_4BYTE_INT, BYTE_ITEMMODE_KIND_STRING};
        int[] expectedLoopCounts = new int[]{1, 1};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);
    }

    /**
     * @test ���\�[�X�ʒm�����d��-�n��f�[�^�ݒ�F����(����:2-1-8)
     * @condition 
     * @result
     */
    public void testCreate_Multieries() throws Exception
    {
        // ����
        byte telegramKind = TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
        byte requestKind = TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
        SystemResourceTelegramListener telegramListener = new SystemResourceTelegramListener();
        Telegram request = createRequestTelegram(telegramKind, requestKind, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT);
        List<String> largeList = createLargeList();
        List<String> largeList2 = createLargeList();

        // ���Ғl
        byte expectedTelegramKind = telegramKind;
        byte expectedRequestKind = TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
        String[] expectedItemNames = new String[]{ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT, ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT + "-name"};
        byte[] expectedItemModes =
                new byte[]{BYTE_ITEMMODE_KIND_4BYTE_INT, BYTE_ITEMMODE_KIND_STRING};
        int[] expectedLoopCounts = new int[]{2, 2};

        // ���{
        Telegram response = telegramListener.receiveTelegram(request);

        // ����
        assertResourceTelegram(expectedTelegramKind, expectedRequestKind, expectedItemNames,
                               expectedItemModes, expectedLoopCounts, response);
    }

    private List<String> createLargeList()
    {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < 60000; index++)
        {
            list.add(String.valueOf(index));
        }
        return list;
    }

    private void assertResourceTelegram(byte expectedTelegramKind, byte expectedRequestKind,
            String[] itemNames, byte[] expectedItemModes, int[] loopCounts, Telegram response)
    {
        // �w�b�_�̌���
        TelegramAssertionUtil.assertHeader(expectedTelegramKind, expectedRequestKind,
                                           response.getObjHeader());

        // Body�̌���
        int bodyIndex = 0;
        TelegramAssertionUtil.assertResourceTelegram(
                                                     TelegramConstants.TIME_RESOURCE,
                                                     TelegramConstants.ITEMNAME_TIME,
                                                     TelegramConstants.BYTE_ITEMMODE_KIND_8BYTE_INT,
                                                     1, response.getObjBody()[bodyIndex++]);

        for (int index = 0; index < itemNames.length; index++)
        {
            TelegramAssertionUtil.assertResourceTelegram(TelegramConstants.OBJECTNAME_RESOURCE,
                                                         itemNames[index],
                                                         expectedItemModes[index],
                                                         loopCounts[index],
                                                         response.getObjBody()[bodyIndex++]);
        }

        assertEquals(bodyIndex, response.getObjBody().length);
    }

    private Telegram createRequestTelegram(byte telegramKind, byte requestKind, String... itemNames)
    {
        Header header = new Header();
        header.setByteTelegramKind(telegramKind);
        header.setByteRequestKind(requestKind);

        Body[] bodies = new Body[itemNames.length];

        for (int index = 0; index < itemNames.length; index++)
        {
            bodies[index] = makeResourceRequestBody(itemNames[index]);
        }

        Telegram request = new Telegram();
        request.setObjHeader(header);
        request.setObjBody(bodies);

        return request;
    }

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B<br />
     */
    @Override
    public void setUp()
    {
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        this.config_ = new JavelinConfig(BASE_DIR + "/lib");
        this.config_.setJavelinFileDir(BASE_DIR);
        SystemLogger.initSystemLog(this.config_);
    }

    /**
     * [����] 3-5-1 �` 3-5-17, 3-5-22, 3-5-23<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E�V�X�e�����\�[�X�̎擾�B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * ���e�V�X�e�����\�[�X��null�łȂ��B<br />
     */
    public void testReceiveTelegram_ResourceGet()
    {
        // ����
        String[] resultItem =
                              new String[]{
                                      TelegramConstants.ITEMNAME_ACQUIREDTIME,
                                      TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME,
                                      TelegramConstants.ITEMNAME_JAVAUPTIME,
                                      TelegramConstants.ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT,
                                      TelegramConstants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX,
                                      TelegramConstants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE,
                                      TelegramConstants.ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX,
                                      TelegramConstants.ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX,
                                      TelegramConstants.ITEMNAME_SYSTEM_MEMORY_SWAP_MAX,
                                      TelegramConstants.ITEMNAME_SYSTEM_MEMORY_SWAP_FREE,
                                      TelegramConstants.ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT,
                                      TelegramConstants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL};
        
        Byte[] resultItemKind = {3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3};

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[resultItem.length];
        for (int num = 0; num < resultItem.length; num++)
        {
            String[] inputDetail = {""};
            sendBodies[num] =
                    CreateTelegramUtil.createBodyValue("resources", resultItem[num], ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);
        }

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        for (int num = 1; num < receiveBody.length; num++)
        {
            AssertUtil.assertResourceTelegram("resources", resultItem[num - 1], resultItemKind[num - 1], 1,
                                              receiveBody[num]);
            assertNotNull(receiveBody[num].getObjItemValueArr());
        }
    }

    /**
     * [����] 3-5-18, 3-5-19<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E�l�b�g���[�N��M�ʂ�1�A�l�b�g���[�N���M�ʂ�2�ɐݒ肵�āA<br />
     *  receiveTelegram���\�b�h�𗘗p���āA�l�b�g���[�N��M�ʁA���M�ʂ��擾����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * ���l�b�g���[�N��M�ʂ�1�A�l�b�g���[�N���M�ʂ�2�ɂȂ��Ă���B
     */
    public void testReceiveTelegram_NetWork()
    {
        // ����
        String[] resultItem = {TelegramConstants.ITEMNAME_NETWORKINPUTSIZEOFPROCESS,
                TelegramConstants.ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS};

        Byte[] resultItemKind = {3, 3};

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[resultItem.length];
        for (int num = 0; num < resultItem.length; num++)
        {
            String[] inputDetail = {""};
            sendBodies[num] =
                    CreateTelegramUtil.createBodyValue("resources", resultItem[num], ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);
        }

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        Long[] inputDetail1 = {(long)1};
        Long[] inputDetail2 = {(long)2};

        AssertUtil.assertTelegram("resources", resultItem[0], resultItemKind[0], 1, inputDetail1,
                                  receiveBody[1]);
        AssertUtil.assertTelegram("resources", resultItem[1], resultItemKind[1], 1, inputDetail2,
                                  receiveBody[2]);
    }

    /**
     * [����] 3-5-20, 3-5-21<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E�t�@�C�����͗ʂ�3�A�t�@�C���o�͗ʂ�4�ɐݒ肵�āA<br />
     *  receiveTelegram���\�b�h�𗘗p���āA�t�@�C�����͗ʁA�o�͗ʂ��擾����B<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     * ���t�@�C�����͗ʂ�3�A�t�@�C���o�͗ʂ�4�ɂȂ��Ă���B
     */
    public void testReceiveTelegram_File()
    {
        // ����
        String[] resultItem = {TelegramConstants.ITEMNAME_FILEINPUTSIZEOFPROCESS,
                TelegramConstants.ITEMNAME_FILEOUTPUTSIZEOFPROCESS};

        Byte[] resultItemKind = {3, 3};

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[resultItem.length];
        for (int num = 0; num < resultItem.length; num++)
        {
            String[] inputDetail = {""};
            sendBodies[num] =
                    CreateTelegramUtil.createBodyValue("resources", resultItem[num], ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);
        }

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        Long[] inputDetail1 = {(long)3};
        Long[] inputDetail2 = {(long)4};

        AssertUtil.assertTelegram("resources", resultItem[0], resultItemKind[0], 1, inputDetail1,
                                  receiveBody[1]);
        AssertUtil.assertTelegram("resources", resultItem[1], resultItemKind[1], 1, inputDetail2,
                                  receiveBody[2]);
    }

    /**
     * [����] 3-5-24<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E������Body�ƗL����Body�����d�����쐬���AreceiveTelegram���\�b�h���ĂԁB<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_OneInvalid_OneValidBody()
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[2];
        String[] inputDetail = {""};
        sendBodies[0] =
                CreateTelegramUtil.createBodyValue("resources", "testItem", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);
        sendBodies[1] =
                CreateTelegramUtil.createBodyValue("resources", "acquiredTime", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        AssertUtil.assertResourceTelegram("resources", "acquiredTime", (byte)3, 1, receiveBody[1]);
        assertNotNull(receiveBody[0].getObjItemValueArr());
    }

    /**
     * [����] 3-5-25<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E�L����Body�A������Body�A�L����Body�����d�����쐬���AreceiveTelegram���\�b�h���ĂԁB<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_TwoValid_OneInvalidBody()
    {
        // ����
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[3];
        String[] inputDetail = {""};
        sendBodies[0] =
                CreateTelegramUtil.createBodyValue("resources", TelegramConstants.ITEMNAME_ACQUIREDTIME, ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);
        sendBodies[1] =
                CreateTelegramUtil.createBodyValue("testObj", "testItem", ItemType.ITEMTYPE_SHORT, 0,
                                                   inputDetail);
        sendBodies[2] =
                CreateTelegramUtil.createBodyValue(
                                                           "resources",
                                                           TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME,
                                                           ItemType.ITEMTYPE_SHORT, 0, inputDetail);

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        AssertUtil.assertResourceTelegram("resources", TelegramConstants.ITEMNAME_ACQUIREDTIME,
                                          (byte)3, 1, receiveBody[1]);
        assertNotNull(receiveBody[0].getObjItemValueArr());
        AssertUtil.assertResourceTelegram("resources",
                                          TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME,
                                          (byte)3, 1, receiveBody[2]);
        assertNotNull(receiveBody[1].getObjItemValueArr());
    }

    /**
     * [����] 3-5-26<br />
     * receiveTelegram�̃e�X�g�B <br />
     * �E�L����Body��3���d�����쐬���AreceiveTelegram���\�b�h���ĂԁB<br />
     * ���쐬�����d�����w�肵�����̂ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_ThreeValidBody()
    {
        // ����
        String[] resultItem =
                              {TelegramConstants.ITEMNAME_ACQUIREDTIME,
                                      TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME,
                                      TelegramConstants.ITEMNAME_JAVAUPTIME,};

        Byte[] resultItemKind = {3, 3, 3};

        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Body[] sendBodies = new Body[resultItem.length];
        for (int num = 0; num < resultItem.length; num++)
        {
            String[] inputDetail = {""};
            sendBodies[num] =
                    CreateTelegramUtil.createBodyValue("resources", resultItem[num], ItemType.ITEMTYPE_SHORT, 0,
                                                       inputDetail);
        }

        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        // Body�̌���
        for (int num = 1; num < receiveBody.length; num++)
        {
            AssertUtil.assertResourceTelegram("resources", resultItem[num - 1],
                                              resultItemKind[num - 1], 1, receiveBody[num]);
            assertNotNull(receiveBody[num].getObjItemValueArr());
        }
    }

    /**
     * [����] 3-5-27 receiveTelegram�̃e�X�g�B <br />
     * �E�d����ʂ����\�[�X�ʒm�łȂ��Ƃ��ɁAreceiveTeleram���\�b�h���ĂԁB<br />
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_RequestKindOthers()
    {
        // ����
        byte[] requestKinds = {1, 2, 4, 5, 6, 7, 8, 9};

        for (byte requestKind : requestKinds)
        {

            Header sendHeader =
                    CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST, requestKind);
            Object[] detail = {new Object()};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-5-28 receiveTelegram�̃e�X�g�B <br />
     * �E�d��������ʂ��v���łȂ��Ƃ��ɁAreceiveTeleram���\�b�h���ĂԁB<br />
     * ���쐬�����d�����S��null�ɂȂ��Ă���B<br />
     */
    public void testReceiveTelegram_TelegramKindOthers()
    {
        // ����
        byte[] telegramKinds = {0, 2};

        for (byte telegramKind : telegramKinds)
        {

            Header sendHeader =
                    CreateTelegramUtil.createHeader(telegramKind, BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
            Object[] detail = {new Object()};
            Body sendBody = CreateTelegramUtil.createBodyValue("", "", ItemType.ITEMTYPE_SHORT, 0, detail);

            Body[] sendBodies = {sendBody};
            Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
            SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

            // ���s
            Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

            // ����
            assertNull(receiveTelegram);
        }
    }

    /**
     * [����] 3-5-29 receiveTelegram�̃e�X�g�B <br />
     * �E�I�u�W�F�N�g����"resources"�łȂ��Ƃ��AreceiveTeleram���\�b�h���ĂԁB<br />
     * ���쐬�����d����Body����ł���B<br />
     */
    public void testReceiveTelegram_ObjNameOther()
    {
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Object[] detail = {new Object()};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("test", "acquiredTime", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        assertEquals(1, receiveBody.length);
    }

    /**
     * [����] 3-5-30 receiveTelegram�̃e�X�g�B <br />
     * �E���ږ������݂��Ȃ����ږ��̂Ƃ��AreceiveTeleram���\�b�h���ĂԁB<br />
     * ���쐬�����d����Body����ł���B<br />
     */
    public void testReceiveTelegram_ItemNameOther()
    {
        Header sendHeader =
                CreateTelegramUtil.createHeader(BYTE_REQUEST_KIND_REQUEST,
                                                BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        Object[] detail = {new Object()};
        Body sendBody =
                CreateTelegramUtil.createBodyValue("resources", "test", ItemType.ITEMTYPE_SHORT, 0, detail);

        Body[] sendBodies = {sendBody};
        Telegram sendTelegram = CreateTelegramUtil.createTelegram(sendHeader, sendBodies);
        SystemResourceTelegramListener listener = new SystemResourceTelegramListener();

        // ���s
        Telegram receiveTelegram = listener.receiveTelegram(sendTelegram);

        // ����
        Header receiveHeader = receiveTelegram.getObjHeader();
        Body[] receiveBody = receiveTelegram.getObjBody();

        // �w�b�_�̌���
        assertEquals(BYTE_TELEGRAM_KIND_RESOURCENOTIFY, receiveHeader.getByteTelegramKind());
        assertEquals(BYTE_REQUEST_KIND_RESPONSE, receiveHeader.getByteRequestKind());

        assertEquals(1, receiveBody.length);
    }

    /**
     * �v���{�̂��쐬����B
     *
     * @param itemName ���ږ�
     * @return �v���{��
     */
    private RequestBody makeResourceRequestBody(final String itemName)
    {
        RequestBody requestBody = new RequestBody();
        requestBody.setStrObjName(OBJECTNAME_RESOURCE);
        requestBody.setStrItemName(itemName);
        return requestBody;
    }
}
