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
package jp.co.acroquest.endosnipe.communicator.util;

import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator.UpdateInvocationParam;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import junit.framework.TestCase;

/**
 * ���ؗp���[�e�B���e�B
 * @author fujii
 *
 */
public class TelegramAssertionUtil extends TestCase
{
    private TelegramAssertionUtil()
    {

    }

    /**
     * �d����Body�̌��؂��s���B(�d���̒��g�����؂���)
     * @param objName �I�u�W�F�N�g��
     * @param itemName ���ږ�
     * @param itemMode ���[�h
     * @param loop ���[�v��
     * @param detail �ڍ�
     * @param body �󂯎�����d��
     */
    public static void assertTelegram(final String objName, final String itemName,
            final ItemType itemMode, final int loop, final Object[] detail, final Body body)
    {
        assertEquals(objName, body.getStrObjName());
        assertEquals(itemName, body.getStrItemName());
        assertEquals(itemMode, body.getByteItemMode());
        assertEquals(loop, body.getIntLoopCount());
        assertDetail(detail, body);
    }

    /**
     * �d����Body�̌��؂��s���B(�d���̒��g�͌��؂��Ȃ�)
     * @param objName �I�u�W�F�N�g��
     * @param itemName ���ږ�
     * @param itemMode ���[�h
     * @param loop ���[�v��
     * @param body �󂯎�����d��
     */
    public static void assertResourceTelegram(final String objName, final String itemName,
            final byte itemMode, final int loop, final Body body)
    {
        assertEquals(objName, body.getStrObjName());
        assertEquals(itemName, body.getStrItemName());
        assertEquals(itemMode, body.getByteItemMode());
        assertEquals(loop, body.getIntLoopCount());
    }

    /**
     * Body�̏ڍׂ����؂���B
     * @param detail ���҂���ڍ�
     * @param body �󂯎�����d��
     */
    public static void assertDetail(final Object[] detail, final Body body)
    {
        if (detail.length != body.getObjItemValueArr().length)
        {
            fail();
            return;
        }
        Object[] objValue = body.getObjItemValueArr();

        for (int num = 0; num < detail.length; num++)
        {
            assertEquals(detail[num], objValue[num]);
        }
    }

    /**
     * �w�b�_�̌��؂��s�Ȃ��܂��B
     * @param telegramKind �d�����
     * @param requestKind �d���������
     * @param header �w�b�_
     */
    public static void assertHeader(final byte telegramKind, final byte requestKind,
            final Header header)
    {
        assertEquals(telegramKind, header.getByteTelegramKind());
        assertEquals(requestKind, header.getByteRequestKind());
    }

    /**
     * JVN���O�_�E�����[�h�����d���̌��؂��s���܂��B
     * 
     * @param expectedRequestKind ���N�G�X�g��ʁB
     * @param expectedFileContents �t�@�C���̓��e�B
     * @param expectedJvnFileNames �t�@�C�����B
     */
    public static void assertJvnLogDownloadTelegram(byte expectedRequestKind,
            Object[] expectedFileContents, Object[] expectedJvnFileNames, Telegram telegram)
    {
        // ����
        assertNotNull(telegram);

        int expectedObjBodyLength = 1;
        if (expectedRequestKind != TelegramConstants.BYTE_REQUEST_KIND_REQUEST)
        {
            expectedObjBodyLength = 4;
        }

        // �d���{�̃T�C�Y�̌���
        assertEquals(expectedObjBodyLength, telegram.getObjBody().length);

        // �w�b�_�̌���
        assertHeader(TelegramConstants.BYTE_TELEGRAM_KIND_JVN_FILE, expectedRequestKind,
                     telegram.getObjHeader());

        // jvn�t�@�C�����̌���
        assertTelegram(TelegramConstants.OBJECTNAME_JVN_FILE,
                       TelegramConstants.ITEMNAME_JVN_FILE_NAME,
                       ItemType.ITEMTYPE_STRING, expectedJvnFileNames.length,
                       expectedJvnFileNames, telegram.getObjBody()[0]);

        // jvn�t�@�C�����e�̌���
        if (expectedRequestKind != TelegramConstants.BYTE_REQUEST_KIND_REQUEST)
        {
            JavelinConfig config = new JavelinConfig();
            assertTelegram(TelegramConstants.OBJECTNAME_JVN_FILE,
                           TelegramConstants.ITEMNAME_JVN_FILE_CONTENT,
                           ItemType.ITEMTYPE_STRING,
                           expectedFileContents.length, expectedFileContents,
                           telegram.getObjBody()[1]);
            assertTelegram(TelegramConstants.OBJECTNAME_JVN_FILE,
                           TelegramConstants.ITEMNAME_ALARM_THRESHOLD,
                           ItemType.ITEMTYPE_LONG,
                           1, new Object[]{config.getAlarmThreshold()},
                           telegram.getObjBody()[2]);
            assertTelegram(TelegramConstants.OBJECTNAME_JVN_FILE,
                           TelegramConstants.ITEMNAME_ALARM_CPU_THRESHOLD,
                           ItemType.ITEMTYPE_LONG,
                           1, new Object[]{config.getAlarmCpuThreashold()},
                           telegram.getObjBody()[3]);
        }

    }

    /**
     * JVN���O�ꗗ�擾�����d���̌��؂��s���܂��B
     * 
     * @param expectedRequestKind ���N�G�X�g��ʁB
     * @param expectedJvnFileNames �t�@�C�����B
     */
    public static void assertJvnLogListTelegram(byte expectedRequestKind,
            Object[] expectedJvnFileNames, Telegram telegram)
    {
        // ����
        assertNotNull(telegram);

        int expectedObjBodyLength = 1;

        // �w�b�_�̌���
        assertHeader(TelegramConstants.BYTE_TELEGRAM_KIND_JVN_FILE_LIST, expectedRequestKind,
                     telegram.getObjHeader());

        // �d���{�̃T�C�Y�̌���
        assertEquals(expectedObjBodyLength, telegram.getObjBody().length);

        // jvn�t�@�C�����̌���
        assertTelegram(TelegramConstants.OBJECTNAME_JVN_FILE,
                       TelegramConstants.ITEMNAME_JVN_FILE_NAME,
                       ItemType.ITEMTYPE_STRING, expectedJvnFileNames.length,
                       expectedJvnFileNames, telegram.getObjBody()[0]);
    }

    /**
     * �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v���d���̌��؂��s���܂��B
     * 
     * @param expectedRequestKind ���N�G�X�g��ʁB
     * @param expectedParam Invocation�B
     */
    public static void assertUpdateInvocationTelegramTelegram(byte expectedRequestKind,
            UpdateInvocationParam[] expectedParam, Telegram telegram)
    {
        // ����
        assertNotNull(telegram);

        // �w�b�_�̌���
        assertHeader(TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_TARGET, expectedRequestKind,
                     telegram.getObjHeader());

        // Invocation�̌���
        int currentIndex = 0;
        if (expectedParam != null)
        {
            for (int index = 0; index < expectedParam.length; index++)
            {
                UpdateInvocationParam param = expectedParam[index];
                String className = param.getClassName();
                String methodName = param.getMethodName();
                String classMethodName = className + "###CLASSMETHOD_SEPARATOR###" + methodName;
    
                if (param.isTransactionGraphOutput() != null)
                {
                    assertTelegram(classMethodName, TelegramConstants.ITEMNAME_TRANSACTION_GRAPH,
                                   ItemType.ITEMTYPE_STRING, 1,
                                   new Object[]{String.valueOf(param.isTransactionGraphOutput())},
                                   telegram.getObjBody()[currentIndex++]);
                }
    
                if (param.isTarget() != null)
                {
                    assertTelegram(classMethodName, TelegramConstants.ITEMNAME_TARGET,
                                   ItemType.ITEMTYPE_STRING, 1,
                                   new Object[]{String.valueOf(param.isTarget())},
                                   telegram.getObjBody()[currentIndex++]);
                }
    
                if (param.getAlarmThreshold() != null)
                {
                    assertTelegram(classMethodName, TelegramConstants.ITEMNAME_ALARM_THRESHOLD,
                                   ItemType.ITEMTYPE_LONG, 1,
                                   new Object[]{Long.valueOf(param.getAlarmThreshold())},
                                   telegram.getObjBody()[currentIndex++]);
                }
    
                if (param.getAlarmCpuThreshold() != null)
                {
                    assertTelegram(classMethodName, TelegramConstants.ITEMNAME_ALARM_CPU_THRESHOLD,
                                   ItemType.ITEMTYPE_LONG, 1,
                                   new Object[]{Long.valueOf(param.getAlarmCpuThreshold())},
                                   telegram.getObjBody()[currentIndex++]);
                }
            }
        }

        // �d���{�̃T�C�Y�̌���
        assertEquals(currentIndex, telegram.getObjBody().length);
    }

    /**
     * �v���ΏہE�g�����U�N�V�����O���t�o�͍X�V�v���d���̌��؂��s���܂��B
     * 
     * @param expectedRequestKind ���N�G�X�g��ʁB
     * @param expectedClassNameList �N���X���̃��X�g�B
     */
    public static void assertRemoveClassTelegram(byte expectedRequestKind,
            List<String> expectedClassNameList, Telegram telegram)
    {
        // ����
        assertNotNull(telegram);

        // �w�b�_�̌���
        assertHeader(TelegramConstants.BYTE_TELEGRAM_KIND_REMOVE_CLASS, expectedRequestKind,
                     telegram.getObjHeader());

        // Invocation�̌���
        if (expectedClassNameList != null)
        {
            for (int index = 0; index < expectedClassNameList.size(); index++)
            {
                String className = expectedClassNameList.get(index);
    
                assertTelegram(className, TelegramConstants.ITEMNAME_CLASSTOREMOVE,
                               ItemType.ITEMTYPE_STRING, 0, new Object[]{},
                               telegram.getObjBody()[index]);
            }
            // �d���{�̃T�C�Y�̌���
            assertEquals(expectedClassNameList.size(), telegram.getObjBody().length);
        }
        else
        {
            // �d���{�̃T�C�Y�̌���
            assertEquals(0, telegram.getObjBody().length);
        }
    }
}
