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
package jp.co.acroquest.endosnipe.javelin.communicate;

import java.io.BufferedReader;
import java.io.IOException;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.ClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.OracleClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.SunClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.servlet.HttpSessionMonitorConverter;
import jp.co.acroquest.endosnipe.javelin.converter.thread.monitor.ThreadDumpMonitor;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.acroquest.endosnipe.javelin.resource.sun.HeapDumpMonitor;

/**
 * �_���v�擾�v�����󂯎�郊�X�i�N���X�ł��B<br />
 * 
 * @author fujii
 */
public class GetDumpTelegramListener implements TelegramListener, TelegramConstants
{

    /**
     * �_���v�擾�v�����󂯎�����Ƃ��̂ݏ������s���܂��B<br />
     * 
     * @param telegram �d���I�u�W�F�N�g
     * @return �����d���i������Ԃ��Ȃ��ꍇ�� <code>null</code> �j
     */
    public Telegram receiveTelegram(Telegram telegram)
    {
        Telegram response = null;
        if (telegram.getObjHeader().getByteTelegramKind() == BYTE_TELEGRAM_KIND_GET_DUMP
                && telegram.getObjHeader().getByteRequestKind() == BYTE_REQUEST_KIND_REQUEST)
        {
            Body[] bodies = telegram.getObjBody();
            if (bodies != null && bodies.length == 1)
            {
                response = notifyDumpRequest(bodies, telegram.getObjHeader().getId());
            }
        }
        return response;
    }

    /**
     * �d���{�̂���_���v�v���C�x���g���擾���A�_���v�擾�C�x���g�𔭐������܂��B<br />
     * ��������C�x���g�́A�q�[�v�_���v�擾�v���ƁA�X���b�h�_���v�擾�v���ł��B<br />
     * [�q�[�v�_���v�擾�v��]<br />
     * <li>�I�u�W�F�N�g��:"dump"</li>
     * <li>���ږ�:"heapDump"</li>
     * [�X���b�h�_���v�擾�v��]<br />
     * <li>�I�u�W�F�N�g��:"dump"</li>
     * <li>���ږ�:"threadDump"</li>
     *
     * @param telegramId �d�� ID
     * @param bodies �d���{��
     * @return �����d���i������Ԃ��Ȃ��ꍇ�� <code>null</code> �j
     */
    private Telegram notifyDumpRequest(final Body[] bodies, final long telegramId)
    {
        Telegram response = null;
        Body body = bodies[0];
        String objectName = body.getStrObjName();
        if (OBJECTNAME_DUMP.equals(objectName))
        {
            String itemName = body.getStrItemName();
            if (ITEMNAME_HEAPDUMP.equals(itemName))
            {
                HeapDumpMonitor.createHeapDump();
                response = TelegramCreator.createHeapDumpResponseTelegram(telegramId);
            }
            else if (ITEMNAME_THREADDUMP.equals(itemName))
            {
                // �X���b�h�_���v�̏ꍇ�́AJavelin���O�ʒm�d���������d���Ƃ���
                ThreadDumpMonitor.getInstance().sendThreadDumpEvent(telegramId);
            }
            else if (ITEMNAME_CLASSHISTOGRAM.equals(itemName))
            {
                // �N���X�q�X�g�O�����̏ꍇ�́AJavelin���O�ʒm�d���������d���Ƃ���
                sendClassHistogramDumpEvent(telegramId);
            }
            else if (ITEMNAME_SESSIONDUMP.equals(itemName))
            {
                HttpSessionMonitorConverter.sendSessionDumpEvent();
            }
        }
        return response;
    }

    private CommonEvent createClassHistogramDumpEvent(ClassHistogramMonitor historgramMonitor)
    {
        CommonEvent event = new CommonEvent();

        event.setName(EventConstants.NAME_CLASSHISTOGRAM);

        StringBuilder classHistogramBuilder = new StringBuilder();
        try
        {
            BufferedReader reader = historgramMonitor.newReader(true);
            if (reader != null)
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    classHistogramBuilder.append(line);
                    classHistogramBuilder.append("\n");
                }
            }
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        String classHistogram = classHistogramBuilder.toString();
        event.addParam(EventConstants.PARAM_CLASSHISTOGRAM, classHistogram);
        return event;
    }

    private void sendClassHistogramDumpEvent(final long telegramId)
    {
        String vendor = System.getProperty("java.vendor");
        ClassHistogramMonitor historgramMonitor = null;

        if (vendor != null)
        {
            if (vendor.contains(ResourceCollector.VENDER_BEA) == true
                    || vendor.contains(ResourceCollector.VENDER_ORACLE))
            {
                historgramMonitor = new OracleClassHistogramMonitor();
            }
            else
            {
                historgramMonitor = new SunClassHistogramMonitor();
            }
        }
        
        if (historgramMonitor != null)
        {
            CommonEvent event = createClassHistogramDumpEvent(historgramMonitor);
            StatsJavelinRecorder.addEvent(event, telegramId);
        }
    }

}
