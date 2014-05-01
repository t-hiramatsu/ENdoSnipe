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

import jp.co.acroquest.endosnipe.collector.JavelinDataQueue;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.collector.data.JavelinMeasurementData;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * ���\�[�X�ʒm�����d����M�p�̃��X�i�N���X�ł��B<br />
 * ��M�������\�[�X�����f�[�^�x�[�X�i�[�p�̃L���[�֒ǉ����܂��B<br />
 * 
 * @author fujii
 * @author nagai
 * @author y-komori
 */
public class SystemResourceListener extends AbstractTelegramListener implements TelegramListener,
        LogMessageCodes, CommonLogMessageCodes
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER;
    static
    {
        LOGGER = ENdoSnipeLogger.getLogger(SystemResourceListener.class);
    }

    /** �f�[�^�x�[�X���B<br /> */
    private String databaseName_;

    /** �ڑ���z�X�g���B<br /> */
    private String hostName_;

    /** �ڑ���z�X�g�� IP �A�h���X�B<br /> */
    private String ipAddress_;

    /** �ڑ���|�[�g�ԍ��B<br /> */
    private int port_;

    /** �ڑ���̃N���C�A���gID */
    private String clientId_;

    /** ��M�f�[�^�i�[�p�L���[ */
    private final JavelinDataQueue queue_;

    /** �G�[�W�F���g�� */
    private String agentName_;

    /**
     * {@link SystemResourceListener} ���\�z���܂��B<br />
     * @param queue �L���[
     */
    public SystemResourceListener(final JavelinDataQueue queue)
    {
        queue_ = queue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        ResourceData resourceData =
                                    ResourceNotifyAccessor.createResourceData(telegram,
                                                                              this.databaseName_,
                                                                              agentName_);
        resourceData.hostName = this.hostName_;
        resourceData.ipAddress = this.ipAddress_;
        resourceData.portNum = this.port_;
        resourceData.clientId = this.clientId_;

        if (LOGGER.isDebugEnabled() == true)
        {
            LOGGER.log(RESOURCE_NOTIFY_RECEIVED, resourceData.hostName, resourceData.ipAddress,
                       resourceData.portNum);
        }

        // �v���l�f�[�^���L���[�֊i�[����
        JavelinMeasurementData measurementData = new JavelinMeasurementData(resourceData);
        setProperties(measurementData, telegram.getObjHeader().getId());
        queue_.offer(measurementData);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_RESOURCENOTIFY;
    }

    /**
     * �G�[�W�F���g�����擾���܂��B
     * @return �G�[�W�F���g��
     */
    public String getAgentName()
    {
        return agentName_;
    }

    /**
     * �f�[�^�x�[�X�����Z�b�g���܂��B<br />
     *
     * @param databaseName �f�[�^�x�[�X��
     */
    public void setDatabaseName(final String databaseName)
    {
        this.databaseName_ = databaseName;
    }

    /**
     * �ڑ���z�X�g�����Z�b�g���܂��B<br />
     *
     * @param hostName �ڑ���z�X�g��
     */
    public void setHostName(final String hostName)
    {
        this.hostName_ = hostName;
    }

    /**
     * �ڑ���z�X�g�� IP �A�h���X���Z�b�g���܂��B<br />
     *
     * @param ipAddress IP �A�h���X
     */
    public void setIpAddress(final String ipAddress)
    {
        this.ipAddress_ = ipAddress;
    }

    /**
     * �ڑ���|�[�g�ԍ����Z�b�g���܂��B<br />
     *
     * @param port �|�[�g�ԍ�
     */
    public void setPort(final int port)
    {
        this.port_ = port;
    }

    /**
     * �ڑ���̃N���C�A���gID��ݒ肵�܂��B
     * @param clientId �N���C�A���gID
     */
    public void setClientId(final String clientId)
    {
        clientId_ = clientId;
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

    /**
     * �G�[�W�F���g����ݒ肵�܂��B
     * @param agentName �G�[�W�F���g��
     */
    public void setAgentName(final String agentName)
    {
        this.agentName_ = agentName;
    }
}
