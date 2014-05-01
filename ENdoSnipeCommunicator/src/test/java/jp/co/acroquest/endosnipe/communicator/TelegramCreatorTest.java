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
package jp.co.acroquest.endosnipe.communicator;

import static jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
import static jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants.BYTE_REQUEST_KIND_REQUEST;
import static jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.common.util.ResourceUtil;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator.UpdateInvocationParam;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.util.TelegramAssertionUtil;

/**
 * TelegramCreator�̎����N���X�ł��B
 * 
 * @author eriguchi
 */
public class TelegramCreatorTest extends DJUnitTestCase
{

    private static final String JVN_FILE_CONTENTS_SINGLE = "fileContents";

    private static final String JVN_FILE_NAMES_SINGLE = "TelegramCreatorTest_Single.jvn";

    private static final Object[] JVN_FILE_CONTENTS_MULTI =
            new Object[]{"fileContents0", "fileContents1", "fileContents2", "fileContents3",
                    "fileContents4"};

    private static final Object[] JVN_FILE_CONTENTS_ONE_CONTENT_NOT_NULL =
            new Object[]{"memContents0", null, "memContents2", "memContents3", "memContents4"};

    private static final String[] JVN_FILE_NAMES_MULTI =
            new String[]{"TelegramCreatorTest_Multi0.jvn", "TelegramCreatorTest_Multi1.jvn",
                    "TelegramCreatorTest_Multi2.jvn", "TelegramCreatorTest_Multi3.jvn",
                    "TelegramCreatorTest_Multi4.jvn"};
    
    private static final String[] JVN_FILE_CONTENTS_ONE_IS_LACKED =
        new String[]{"TelegramCreatorTest_Multi0.jvn", "TelegramCreatorTest_Multi1.jvn",
                "TelegramCreatorTest_Multi2.jvn", "TelegramCreatorTest_Multi3.jvn"};

    protected void setUp()
        throws Exception
    {
        super.setUp();
        File file =
                ResourceUtil.getResourceAsFile(getClass(), getClass().getSimpleName() + ".class");

        JavelinConfig config = new JavelinConfig();
        config.setJavelinFileDir(file.getParentFile().getAbsolutePath());

        SystemLogger.initSystemLog(config);
    }

    /**
     * @test JVN���O�d��-�v���d��:���O�P��(����:3-1-1)
     * @condition requestKind��1�AjvnFileNames���P��("test1.jvn")
     * @result JVN���O�_�E�����[�h�v���d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�������P���ł��邱�ƁB
     */
    public void testCreateJvnLogTelegram_Unit()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_REQUEST;
        List<String> jvnFileNames = new ArrayList<String>();
        jvnFileNames.add("test1.jvn");

        // ���Ғl
        Object[] expectedFileContents = new Object[]{JVN_FILE_CONTENTS_SINGLE};
        Object[] expectedJvnFileNames = jvnFileNames.toArray();

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogTelegram(requestKind, jvnFileNames);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test �vJVN���O�d��-���d��:���O����(����:3-1-2)
     * @condition requestKind��1
     *            jvnFileNames������("test1.jvn","test2.jvn","test3.jvn","test4.jvn","test5.jvn")
     * @result JVN���O�_�E�����[�h�v���d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�������P���ł��邱�ƁB
     */
    public void testCreateJvnLogTelegram_Multi()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_REQUEST;
        List<String> jvnFileNames = new ArrayList<String>();
        jvnFileNames.add("test1.jvn");
        jvnFileNames.add("test2.jvn");
        jvnFileNames.add("test3.jvn");
        jvnFileNames.add("test4.jvn");
        jvnFileNames.add("test5.jvn");

        // ���Ғl
        Object[] expectedFileContents = new Object[]{JVN_FILE_CONTENTS_SINGLE};
        Object[] expectedJvnFileNames = jvnFileNames.toArray();

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogTelegram(requestKind, jvnFileNames);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P���^���̃p�����[�^�w��null(����:3-1-3)
     * @condition requestKind��2�AjvnFileNames���P��("test1.jvn")
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�������P���ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e�����[�J���ɔz�u���Ă���Javelin���O�t�@�C���̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Unit()
    {
        // ����
        String name = "TelegramCreatorTest_Unit.jvn";
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = new Object[]{name};
        Object[] jvnFileContents = null;

        Object[] expectedFileContents = new Object[]{JVN_FILE_CONTENTS_SINGLE};
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P��(����:3-1-4)
     * @condition requestKind��2�AjvnFileNames������(5��)
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e�����[�J���ɔz�u���Ă���Javelin���O�t�@�C���̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Multi_ContentNull()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = JVN_FILE_NAMES_MULTI;
        Object[] jvnFileContents = null;

        // ���Ғl
        Object[] expectedFileContents = JVN_FILE_CONTENTS_MULTI;
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P��(����:3-1-5)
     * @condition requestKind��2�AjvnFileNames���P��(1��), jvnFileContents���P��(1��)
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�������P���ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e���̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Single_AllContentNotNull()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = new Object[]{JVN_FILE_NAMES_SINGLE};
        Object[] jvnFileContents = new Object[]{JVN_FILE_CONTENTS_SINGLE};

        // ���Ғl
        Object[] expectedFileContents = jvnFileContents;
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P��(����:3-1-6)
     * @condition requestKind��2�AjvnFileNames������(5��), jvnFileContents������(5��)
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e���̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Multi_AllContentNotNull()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = JVN_FILE_NAMES_MULTI;
        Object[] jvnFileContents = JVN_FILE_CONTENTS_MULTI;

        // ���Ғl
        Object[] expectedFileContents = jvnFileContents;
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P��(����:3-1-7)
     * @condition requestKind��2�AjvnFileNames������(5��), jvnFileContents������(5��)
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e�̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Multi_OneContentNotNull()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = JVN_FILE_NAMES_MULTI;
        Object[] jvnFileContents = JVN_FILE_CONTENTS_ONE_CONTENT_NOT_NULL;

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;
        Object[] expectedFileContents =
                new Object[]{"memContents0", "fileContents1", "memContents2", "memContents3",
                        "memContents4"};

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�P��(����:3-1-8)
     * @condition requestKind��2�AjvnFileNames���P�������݂��Ȃ��t�@�C��, jvnFileContents��null
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�������P���ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e��""�ł��邱�ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Single_ContentNull_FileNotFound()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames = new Object[]{"TelegramCreatorTest_Single_NotFound.jvn"};
        Object[] jvnFileContents = null;

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;
        Object[] expectedFileContents = new Object[]{""};

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O����(����:3-1-9)
     * @condition requestKind��2�AjvnFileNames������(5��)�����݂��Ȃ��t�@�C��, jvnFileContents��null
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e��""�ł��邱�ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Multi_ContentNull_FileNotFound()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_RESPONSE;
        Object[] jvnFileNames =
                new Object[]{"TelegramCreatorTest_Multi_NotFound0.jvn",
                        "TelegramCreatorTest_Multi_NotFound1.jvn",
                        "TelegramCreatorTest_Multi_NotFound2.jvn",
                        "TelegramCreatorTest_Multi_NotFound3.jvn",
                        "TelegramCreatorTest_Multi_NotFound4.jvn"};
        Object[] jvnFileContents = null;

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;
        Object[] expectedFileContents = new Object[]{"", "", "", "", ""};

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�ʒm�d��:���O�P���^���̃p�����[�^�w��(����:3-1-10)
     * @condition requestKind��0�AjvnFileNames���P��, jvnFileContents��null�łȂ����ƁB
     * @result JVN���O�_�E�����[�h�ʒm�d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e�̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Notify_Single()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_NOTIFY;
        Object[] jvnFileNames = new Object[]{JVN_FILE_NAMES_SINGLE};
        Object[] jvnFileContents = {JVN_FILE_CONTENTS_SINGLE};

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;
        Object[] expectedFileContents = new Object[]{JVN_FILE_CONTENTS_SINGLE};

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_NOTIFY,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�d��-�����d��:���O�����^���̃p�����[�^�w��null(����:3-1-11)
     * @condition requestKind��2�AjvnFileNames������(5��), jvnFileContents��null
     * @result JVN���O�_�E�����[�h�����d�����擾�ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����������ł��邱�ƁB
     *         �ݒ肳��Ă��郍�O�t�@�C�����e�̓��e�Ɠ��������ƁB
     */
    public void testCreateJvnLogDownloadTelegram_Notify_Multi()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_NOTIFY;
        Object[] jvnFileNames = JVN_FILE_NAMES_MULTI;
        Object[] jvnFileContents = null;

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;
        Object[] expectedFileContents = JVN_FILE_CONTENTS_MULTI;

        // ���{
        Telegram telegram =
                TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);

        // ����
        TelegramAssertionUtil.assertJvnLogDownloadTelegram(BYTE_REQUEST_KIND_NOTIFY,
                                                           expectedFileContents,
                                                           expectedJvnFileNames, telegram);
    }
    
    /**
     * @test JVN���O�d��-�����d��:���O�Ǝ��̃p�����[�^���قȂ�
     * @condition jvnFileNames��5��, jvnFileContents��4��
     * @result IllegalArgumentException���X���[����邱�ƁB
     */
    public void testCreateJvnLogDownloadTelegram_IllegalArgument()
    {
        // ����
        byte requestKind = BYTE_REQUEST_KIND_NOTIFY;
        Object[] jvnFileNames = JVN_FILE_NAMES_MULTI;
        Object[] jvnFileContents = JVN_FILE_CONTENTS_ONE_IS_LACKED;

        // ���{�E����
        try
        {
            TelegramCreator.createJvnLogDownloadTelegram(requestKind, jvnFileNames,
                                                             jvnFileContents, 0, null);
            assertTrue(false);
        }
        catch (IllegalArgumentException ex)
        {
            assertTrue(true);
        }
    }

    /**
     * @test JVN���O�ꗗ�d��-JVN���Onull(����:5-1-1)
     * @condition jvnFileNames��null
     * @result �d�����擾�ł����Anull���Ԃ邱�ƁB
     */
    public void testCreateJvnLogListTelegram_Null()
    {
        // ����
        String[] jvnFileNames = null;

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        assertNull(telegram);
    }

    /**
     * @test JVN���O�ꗗ�d��-JVN���O�󃊃X�g(����:5-1-2)
     * @condition jvnFileNames���󃊃X�g
     * @result �d����Body����jvnFile�̍��ڒl��0���ł��邱�ƁB
     */
    public void testCreateJvnLogListTelegram_Empty()
    {
        // ����
        String[] jvnFileNames = new String[]{};

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        TelegramAssertionUtil.assertJvnLogListTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                       expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�ꗗ�d��-JVN���O�P��(����:5-1-3)
     * @condition jvnFileNames���P��
     * @result �d����Body����jvnFile�̍��ڒl���w�肵�����e�Ɠ���ł��邱�ƁB
     */
    public void testCreateJvnLogListTelegram_Single()
    {
        // ����
        String[] jvnFileNames = new String[]{JVN_FILE_NAMES_SINGLE};

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        TelegramAssertionUtil.assertJvnLogListTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                       expectedJvnFileNames, telegram);
    }

    /**
     * @test JVN���O�ꗗ�d��-JVN���O����(����:5-1-4)
     * @condition jvnFileNames������
     * @result �d����Body����jvnFile�̍��ڒl���w�肵�����e�Ɠ���ł��邱�ƁB
     */
    public void testCreateJvnLogListTelegram_Multi()
    {
        // ����
        String[] jvnFileNames = JVN_FILE_NAMES_MULTI;

        // ���Ғl
        Object[] expectedJvnFileNames = jvnFileNames;

        // ���{
        Telegram telegram = TelegramCreator.createJvnLogListTelegram(jvnFileNames);

        // ����
        TelegramAssertionUtil.assertJvnLogListTelegram(BYTE_REQUEST_KIND_RESPONSE,
                                                       expectedJvnFileNames, telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-�Ώ�Invocation null(����:6-1-1)
     * @condition Invocation��null
     * @result �d����Body����ł��邱�ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_Null()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray = null;

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-�Ώ�Invocation�󃊃X�g(����:6-1-2)
     * @condition �Ώ�Invocation���󃊃X�g
     * @result �d����Body����ł��邱�ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_Empty()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray = new UpdateInvocationParam[]{};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v���Ώ�Invocation�P��(����:6-1-3)
     * @condition �Ώ�Invocation�P��
     * @result �einvocation��4�̍��ڂ��A�p�����[�^�̒ʂ�ɓd���ɐݒ肳��Ă��邱�ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_Single()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{new UpdateInvocationParam("className", "methodName",
                                                                      Boolean.TRUE, Boolean.TRUE,
                                                                      Long.valueOf(5000),
                                                                      Long.valueOf(3000))};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v���Ώ�Invocation����(����:6-1-4)
     * @condition �Ώ�Invocation����
     * @result �einvocation��4�̍��ڂ��A�p�����[�^�̒ʂ�ɓd���ɐݒ肳��Ă��邱�ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_Multi()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{
                        new UpdateInvocationParam("className", "methodName", Boolean.TRUE,
                                                  Boolean.TRUE, Long.valueOf(5000),
                                                  Long.valueOf(3000)),
                        new UpdateInvocationParam("className1", "methodName1", Boolean.TRUE,
                                                  Boolean.FALSE, Long.valueOf(4000),
                                                  Long.valueOf(2000)),
                        new UpdateInvocationParam("className2", "methodName2", Boolean.TRUE,
                                                  Boolean.FALSE, Long.valueOf(3000),
                                                  Long.valueOf(1000)),
                        new UpdateInvocationParam("className3", "methodName3", Boolean.TRUE,
                                                  Boolean.FALSE, Long.valueOf(2000),
                                                  Long.valueOf(1000)),
                        new UpdateInvocationParam("className4", "methodName4", Boolean.TRUE,
                                                  Boolean.FALSE, Long.valueOf(1000),
                                                  Long.valueOf(2000))

                };

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-transactionGraph null(����:6-1-5)
     * @condition transactionGraph null
     * @result �d����Body���ɍ���target�����݂��Ȃ����ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_TransactionNull()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{new UpdateInvocationParam("className", "methodName",
                                                                      null, Boolean.TRUE,
                                                                      Long.valueOf(5000),
                                                                      Long.valueOf(3000))};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-target null(����:6-1-6)
     * @condition target null
     * @result �d����Body���ɍ���alarmThreshold�����݂��Ȃ����ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_TargetNull()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{new UpdateInvocationParam("className", "methodName",
                                                                      Boolean.TRUE, null,
                                                                      Long.valueOf(5000),
                                                                      Long.valueOf(3000))};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-alarmThreshold null(����:6-1-7)
     * @condition alarmThreshold null
     * @result �d����Body���ɍ���alarmThreshold�����݂��Ȃ����ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_AlarmThresholdNull()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{new UpdateInvocationParam("className", "methodName",
                                                                      Boolean.TRUE, Boolean.TRUE,
                                                                      null, Long.valueOf(3000))};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v��-alarmCpuThreshold null(����:6-1-8)
     * @condition alarmCpuThreshold null
     * @result �d����Body���ɍ���alarmCpuThreshold�����݂��Ȃ����ƁB
     */
    public void testCreateUpdateInvocationTelegramTelegram_AlarmCpuThresholdNull()
    {
        // ����
        UpdateInvocationParam[] invocationParamArray =
                new UpdateInvocationParam[]{new UpdateInvocationParam("className", "methodName",
                                                                      Boolean.TRUE, Boolean.TRUE,
                                                                      Long.valueOf(3000), null)};

        // ���Ғl
        UpdateInvocationParam[] expectedInvocationParamArray = invocationParamArray;

        // ���{
        Telegram telegram = TelegramCreator.createUpdateInvocationTelegram(invocationParamArray);

        // ����
        TelegramAssertionUtil.assertUpdateInvocationTelegramTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                                     expectedInvocationParamArray,
                                                                     telegram);
    }

    /**
     * @test �N���X�폜�d��-�ΏۃN���Xnull(����:7-1-1)
     * @condition �ΏۃN���X null
     * @result �d����Body������ł��邱�ƁB
     */
    public void testCreateRemoveClassTelegram_Null()
    {
        // ����
        List<String> classNameList = null;

        // ���Ғl
        List<String> expectedClassNameList = classNameList;

        // ���{
        Telegram telegram = TelegramCreator.createRemoveClassTelegram(classNameList);

        // ����
        TelegramAssertionUtil.assertRemoveClassTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                        expectedClassNameList, telegram);
    }

    /**
     * @test �N���X�폜�d��-�ΏۃN���X�󃊃X�g(����:7-1-2)
     * @condition �ΏۃN���X �󃊃X�g
     * @result �d����Body������ł��邱�ƁB
     */
    public void testCreateRemoveClassTelegram_Empty()
    {
        // ����
        List<String> classNameList = new ArrayList<String>();

        // ���Ғl
        List<String> expectedClassNameList = classNameList;

        // ���{
        Telegram telegram = TelegramCreator.createRemoveClassTelegram(classNameList);

        // ����
        TelegramAssertionUtil.assertRemoveClassTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                        expectedClassNameList, telegram);
    }

    /**
     * @test �N���X�폜�d��-�ΏۃN���X�P��(����:7-1-3)
     * @condition �ΏۃN���X�P��
     * @result �d����Body���ɁA�p�����[�^�Ŏw�肵���S�ẴN���X�����ݒ肳��Ă��邱�ƁB
     */
    public void testCreateRemoveClassTelegram_Single()
    {
        // ����
        List<String> classNameList = new ArrayList<String>();
        classNameList.add("className1");

        // ���Ғl
        List<String> expectedClassNameList = classNameList;

        // ���{
        Telegram telegram = TelegramCreator.createRemoveClassTelegram(classNameList);

        // ����
        TelegramAssertionUtil.assertRemoveClassTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                        expectedClassNameList, telegram);
    }

    /**
     * @test �N���X�폜�d��-�ΏۃN���X����(����:7-1-4)
     * @condition �ΏۃN���X����
     * @result �d����Body���ɁA�p�����[�^�Ŏw�肵���S�ẴN���X�����ݒ肳��Ă��邱�ƁB
     */
    public void testCreateRemoveClassTelegram_Multi()
    {
        // ����
        List<String> classNameList = new ArrayList<String>();
        classNameList.add("className1");
        classNameList.add("className2");
        classNameList.add("className3");
        classNameList.add("className4");
        classNameList.add("className5");

        // ���Ғl
        List<String> expectedClassNameList = classNameList;

        // ���{
        Telegram telegram = TelegramCreator.createRemoveClassTelegram(classNameList);

        // ����
        TelegramAssertionUtil.assertRemoveClassTelegram(BYTE_REQUEST_KIND_REQUEST,
                                                        expectedClassNameList, telegram);
    }
}
