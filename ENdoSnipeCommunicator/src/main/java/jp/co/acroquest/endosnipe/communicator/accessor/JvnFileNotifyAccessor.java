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
package jp.co.acroquest.endosnipe.communicator.accessor;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * Javelin ���O�ʒm�d���̂��߂̃A�N�Z�T�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class JvnFileNotifyAccessor implements TelegramConstants
{
    private JvnFileNotifyAccessor()
    {

    }

    /**
     * Javelin ���O�ʒm�d��������e�����o���܂��B<br />
     * �d����ʂ����O�ʒm�d���ł͂Ȃ��ꍇ��A���e���h���ł���ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
     * 
     * @param telegram Javelin ���O�ʒm�d��
     * @return �d�����e
     */
    public static JvnFileEntry[] getJvnFileEntries(final Telegram telegram)
    {
        if (checkTelegram(telegram) == false)
        {
            return null;
        }

        Body[] bodies = telegram.getObjBody();
        List<Object> jvnFileNames = new ArrayList<Object>(bodies.length);
        List<Object> jvnFileContents = new ArrayList<Object>(bodies.length);
        List<Object> jvnFileItemNames = new ArrayList<Object>(bodies.length);
        long alarmThreshold = -1;
        long cpuAlarmThreshold = -1;

        for (Body body : bodies)
        {
            String objectName = body.getStrObjName();
            String itemName = body.getStrItemName();

            ResponseBody responseBody = (ResponseBody)body;
            if (OBJECTNAME_JVN_FILE.equals(objectName) == true)
            {
                Object[] objItemValueArr = responseBody.getObjItemValueArr();
                if (objItemValueArr.length == 0)
                {
                    continue;
                }

                // �t�@�C�����̔z�񂾂����ꍇ�B
                if (ITEMNAME_JVN_FILE_NAME.equals(itemName) == true)
                {
                    jvnFileNames.clear();
                    for (Object objItem : objItemValueArr)
                    {
                        jvnFileNames.add(objItem);
                    }
                }
                // �t�@�C�����e�̔z�񂾂����ꍇ�B
                else if (TelegramConstants.ITEMNAME_JVN_FILE_CONTENT.equals(itemName) == true)
                {
                    jvnFileContents.clear();
                    for (Object objItem : objItemValueArr)
                    {
                        jvnFileContents.add(objItem);
                    }
                }
                // �A�C�e�����̔z�񂾂����ꍇ
                else if (TelegramConstants.ITEMNAME_JVN_ITEM_NAME.equals(itemName) == true)
                {
                	jvnFileItemNames.clear();
                	for (Object objItem : objItemValueArr)
                	{
                		jvnFileItemNames.add(objItem);
                	}
                }
                // �A���[��臒l�̏ꍇ
                if (TelegramConstants.ITEMNAME_ALARM_THRESHOLD.equals(itemName) == true)
                {
                    Long alarmThresholdLong = (Long)objItemValueArr[0];
                    if (alarmThresholdLong != null)
                    {
                        alarmThreshold = alarmThresholdLong.longValue();
                    }
                }
                // CPU�A���[��臒l�̏ꍇ
                else if (TelegramConstants.ITEMNAME_ALARM_CPU_THRESHOLD.equals(itemName) == true)
                {
                    Long alarmThresholdLong = (Long)objItemValueArr[0];
                    if (alarmThresholdLong != null)
                    {
                        cpuAlarmThreshold = alarmThresholdLong.longValue();
                    }
                }
            }
        }

        if (jvnFileNames.size() != jvnFileContents.size())
        {
            return null;
        }

        JvnFileEntry[] entries = new JvnFileEntry[jvnFileNames.size()];
        for (int i = 0; i < jvnFileNames.size(); i++)
        {
            entries[i] = new JvnFileEntry();
            entries[i].fileName = (String)jvnFileNames.get(i);
            entries[i].contents = (String)jvnFileContents.get(i);
            entries[i].itemName = (String)jvnFileItemNames.get(i);
            entries[i].alarmThreshold = alarmThreshold;
            entries[i].cpuAlarmThreshold = cpuAlarmThreshold;
        }
        return entries;
    }

    private static boolean checkTelegram(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        return BYTE_TELEGRAM_KIND_JVN_FILE == header.getByteTelegramKind() ? true : false;
    }

    /**
     * Javelin ���O��ێ����邽�߂̃N���X�ł��B<br />
     * 
     * @author y-komori
     */
    public static class JvnFileEntry
    {
        /** �t�@�C���� */
        public String fileName;

        /** �t�@�C�����e */
        public String contents;
        
        /** �A�C�e���� */
        public String itemName;

        /** �A���[��臒l */
        public long alarmThreshold;

        /** �A���[��CPU臒l */
        public long cpuAlarmThreshold;
    }
}
