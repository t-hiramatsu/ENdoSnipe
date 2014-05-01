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
package jp.co.acroquest.endosnipe.collector.listener;

import java.io.File;

import jp.co.acroquest.endosnipe.collector.JavelinDataQueue;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.collector.data.JavelinLogData;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.JvnFileNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.JvnFileNotifyAccessor.JvnFileEntry;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * Javelin ���O�ʒm�d������M���邽�߂̃N���X�ł��B<br />
 * 
 * @author y-komori
 * @author nagai
 */
public class JvnFileNotifyListener extends AbstractTelegramListener implements TelegramListener,
        LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(JvnFileNotifyListener.class);

    private final String tempDir_;

    private final JavelinDataQueue queue_;

    private String databaseName_ = "";

    private String hostName_ = null;

    private String agentName_ = null;

    private String ipAddress_ = "";

    private int port_ = -1;

    private String clientId_ = null;

    /**
     * {@link JvnFileNotifyListener} ���\�z���܂��B<br />
     * @param queue �L���[
     */
    public JvnFileNotifyListener(final JavelinDataQueue queue)
    {
        this.queue_ = queue;

        String iotemp = System.getProperty("java.io.tmpdir");
        tempDir_ =
                   iotemp + System.getProperty("file.separator") + "collector"
                           + System.getProperty("file.separator");

        File tempFile = new File(tempDir_);
        if (tempFile.exists() == false)
        {
            if (tempFile.mkdir() == false)
            {
                // �e���|�����f�B���N�g�������Ɏ��s����
                LOGGER.log(MAKING_DIR_FAILED, tempFile.getAbsolutePath());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.log(JVN_FILE_NOTIFY_RECEIVED);
        }

        JvnFileEntry[] entries = JvnFileNotifyAccessor.getJvnFileEntries(telegram);

        for (JvnFileEntry entry : entries)
        {
            JavelinLogData logData = createJavelinLogDataByString(entry);
            logData.setAgentName(agentName_ + entry.itemName);
            if (logData != null)
            {
                setProperties(logData, telegram.getObjHeader().getId());
                this.queue_.offer(logData);
            }
        }
        return null;
    }

    private void setProperties(final JavelinData javelinData, final long telegramId)
    {
        javelinData.setDatabaseName(this.databaseName_);
        javelinData.setHost(this.hostName_);
        javelinData.setIpAddress(this.ipAddress_);
        javelinData.setPort(this.port_);
        javelinData.setClientId(this.clientId_);
        javelinData.setTelegramId(telegramId);
    }

    private JavelinLogData createJavelinLogDataByString(final JvnFileEntry entry)
    {
        JavelinLogData javelinLogData = new JavelinLogData(entry.contents);
        javelinLogData.setLogFileName(entry.fileName);
        javelinLogData.setAlarmThreshold(entry.alarmThreshold);
        javelinLogData.setCpuAlarmThreshold(entry.cpuAlarmThreshold);
        return javelinLogData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_JVN_FILE;
    }

    /**
     * {@link JavelinData} �p�̃f�[�^�x�[�X����ݒ肵�܂��B<br />
     *
     * @param databaseName �f�[�^�x�[�X��
     */
    public void setDatabaseName(final String databaseName)
    {
        this.databaseName_ = databaseName;
    }

    /**
     * {@link JavelinData} �p�̐ڑ���z�X�g����ݒ肵�܂��B<br />
     * @param hostName �ڑ���z�X�g��
     */
    public void setHostName(final String hostName)
    {
        this.hostName_ = hostName;
    }

    public String getAgentName()
    {
        return agentName_;
    }

    public void setAgentName(final String agentName)
    {
        this.agentName_ = agentName;
    }

    /**
     * {@link JavelinData} �p�̐ڑ��� IP �A�h���X��ݒ肵�܂��B<br />
     * 
     * @param ipAddress �ڑ��� IP �A�h���X
     */
    public void setIpAddress(final String ipAddress)
    {
        this.ipAddress_ = ipAddress;
    }

    /**
     * {@link JavelinData} �p�̐ڑ���|�[�g�ԍ���ݒ肵�܂��B<br />
     * 
     * @param port �ڑ���|�[�g�ԍ�
     */
    public void setPort(final int port)
    {
        this.port_ = port;
    }

    /**
     * {@link JavelinData} �p�̃N���C�A���gID��ݒ肵�܂��B
     * @param clientId �N���C�A���gID
     */
    public void setClientId(final String clientId)
    {
        clientId_ = clientId;
    }
}
