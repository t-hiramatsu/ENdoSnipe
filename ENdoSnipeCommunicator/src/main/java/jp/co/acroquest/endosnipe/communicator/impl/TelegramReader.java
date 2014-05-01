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
package jp.co.acroquest.endosnipe.communicator.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * �d���̎�M�������s�����߂̃N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class TelegramReader implements Runnable
{
    /** ���K�[�N���X */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(TelegramReader.class);

    private static final int SO_TIMEOUT = 10000;

    /***/
    private static final int READ_WAIT = 100;

    private static final int RETRY_MAX = 5;

    private volatile boolean isRunning_;
    
    private volatile boolean isShutdown_ = false;

    private SocketChannel channel_;

    /** �T�[�o������̃f�[�^��Head�p�ϐ� */
    private final ByteBuffer headerBuffer_ = ByteBuffer.allocate(Header.HEADER_LENGTH);

    /** �d����]������^�[�Q�b�g�I�u�W�F�N�g�̃��X�g */
    private final List<TelegramListener> telegramListenerList_;

    /** �ċN���pTcpStatsVisionEditor */
    private CommunicationClientImpl comminicationClient_ = null;

    /** ���g���C���� */
    private static final int RETRY_INTERVAL = 10000;

    private int retryCount_ = 0;
    
    /** ���O�o�͗L�� */
    private boolean isOutputLog_ = true;
    
    /** �T�[�o�Ƃ̒ʐM�p�R�l�N�V���� */
    private JavelinClientConnection clientConnection_;
    
    /** �d�����M�p�X���b�h��. */
    private String sendThreadName_;

    /** �d�����M�p�X���b�h. */
    private Thread clientSendThread_;
    
    /**
     * {@link TelegramReader} ���\�z���܂��B<br />
     *
     * @param communicationClient {@link CommunicationClientImpl} �I�u�W�F�N�g
     * @param sendThreadName �d�����M�p�X���b�h��
     * @param objSocket �d�����M�p�\�P�b�g
     * @param discard Discard�̎��{�L��
     * @param isOutputLog ���O�o�͗L��
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    public TelegramReader(final CommunicationClientImpl communicationClient,
            final String sendThreadName, final Socket objSocket,
            final boolean discard, final boolean isOutputLog) throws IOException
    {
        this.clientConnection_ = new JavelinClientConnection(objSocket, discard);

        this.isRunning_ = false;
        this.comminicationClient_ = communicationClient;
        this.sendThreadName_ = sendThreadName;
        this.isOutputLog_ = isOutputLog;

        // telegramListenerList_ �� synchronized ���s���K�v�����邪�A
        // �g�� for ���i iterator �j���g�p����ꍇ�� Collections.synchronizedList �Ń��b�v���Ă����ʂȂ��߁A
        // ���O�� synchronized �������s��
        this.telegramListenerList_ = new ArrayList<TelegramListener>();
    }

    /**
     * �d�������p�I�u�W�F�N�g��ǉ����܂��B<br />
     *
     * @param telegramListener �d�������p�I�u�W�F�N�g
     */
    public void addTelegramListener(final TelegramListener telegramListener)
    {
        synchronized (this.telegramListenerList_)
        {
            this.telegramListenerList_.add(telegramListener);
        }
    }

    /**
     * �d����M���[�v�B<br />
     */
    public void run()
    {
        JavelinClientSendRunnable clientSendRunnable =
            new JavelinClientSendRunnable(this.clientConnection_);
        this.clientSendThread_ = new Thread(clientSendRunnable, this.sendThreadName_);
        this.clientSendThread_.setDaemon(true);
        this.clientSendThread_.start();

        this.retryCount_ = 0;
        this.headerBuffer_.rewind();
        while (this.isRunning_)
        {
            try
            {
                mainLoop();
            }
            catch (Exception ex)
            {
                outputLog("WECC0105", ex);
            }
        }

    }

    private void mainLoop()
    {
        if (this.comminicationClient_.isStart() == false)
        {
            try
            {
                Thread.sleep(RETRY_INTERVAL);
            }
            catch (InterruptedException ex)
            // CHECKSTYLE:OFF
            {
                // interrupt����B
            }
            // CHECKSTYLE:ON
            return;
        }
        this.channel_ = this.comminicationClient_.getChannel();

        if (this.channel_ == null)
        {
            setRunning(false);
            this.clientConnection_.close();
            return;
        }

        byte[] telegramBytes = null;
        try
        {
            telegramBytes = this.readTelegramBytes();
        }
        catch (SocketTimeoutException ste)
        {
            outputLog("WECC0107", ste);
            return;
        }
        catch (IOException ioe)
        {
            if(isShutdown_ == false)
            {
                // �ؒf���ꂽ
                outputLog("WECC0201", ioe);
                setRunning(false);
                this.clientConnection_.close();
            }
            return;
        }
        Telegram telegram = TelegramUtil.recoveryTelegram(telegramBytes);

        if (telegram == null)
        {
            outputLog("WECC0106");
            return;
        }

        synchronized (this.telegramListenerList_)
        {
            for (TelegramListener listener : this.telegramListenerList_)
            {
                try
                {
                    Telegram response = listener.receiveTelegram(telegram);
                    
                    // �����d��������ꍇ�̂݁A������Ԃ�
                    if (response != null)
                    {
                        List<byte[]> byteList = TelegramUtil.createTelegram(response);
                        for (byte[] byteOutputArr : byteList)
                        {
                            this.clientConnection_.sendAlarm(byteOutputArr);
                        }
                    }
                }
                catch (Exception ex)
                {
                    outputLog("WECC0105", ex);
                }
            }
        }
    }

    /**
     * �T�[�o����f�[�^��ǂݍ��݂܂��B<br />
     *
     * @return ��M�����f�[�^
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    @SuppressWarnings("deprecation")
	public byte[] readTelegramBytes()
        throws IOException
    {
        this.channel_.socket().setSoTimeout(SO_TIMEOUT);
        byte finalTelegram = TelegramConstants.HALFWAY_TELEGRAM;

        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        do
        {
            int readCount = 0;
            while (readCount < Header.HEADER_LENGTH)
            {
                int count = this.channel_.read(this.headerBuffer_);
                if (count < 0)
                {
                    throw new IOException();
                }
                else if (count == 0)
                {
                    if (this.retryCount_ > RETRY_MAX)
                    {
                        throw new IOException();
                    }
                    this.retryCount_++;
                    try
                    {
                        Thread.sleep(READ_WAIT);
                    }
                    catch (InterruptedException ex)
                    {
                        LOGGER.warn(ex);
                    }
                    continue;
                }

                this.retryCount_ = 0;
                readCount += count;
            }

            this.headerBuffer_.rewind();
            int telegramLength = this.headerBuffer_.getInt();
            this.headerBuffer_.getLong();
            finalTelegram = this.headerBuffer_.get();

            // �w�b�_�������Ȃ��ꍇ�͂��̂܂ܕԂ��B
            if (telegramLength <= Header.HEADER_LENGTH)
            {
                this.headerBuffer_.rewind();
                return this.headerBuffer_.array();
            }

            readCount = 0;
            ByteBuffer bodyBuffer =
                    ByteBuffer.allocate(telegramLength - headerBuffer_.array().length);
            
            if (resultStream.size() == 0)
            {
                resultStream.write(this.headerBuffer_.array());
            }

            while (bodyBuffer.remaining() > 0)
            {
                int count = this.channel_.read(bodyBuffer);
                if (count < 0)
                {
                    throw new IOException();
                }
                else if (count == 0)
                {
                    if (this.retryCount_ > RETRY_MAX)
                    {
                        throw new IOException();
                    }
                    this.retryCount_++;
                    try
                    {
                        Thread.sleep(READ_WAIT);
                    }
                    catch (InterruptedException ex)
                    {
                        LOGGER.warn(ex);
                    }

                    continue;
                }

                this.retryCount_ = 0;
            }

            this.headerBuffer_.rewind();
            resultStream.write(bodyBuffer.array());
        }
        while (finalTelegram != TelegramConstants.FINAL_TELEGRAM);
        
        byte[] telegramBytes = resultStream.toByteArray();
        int telegramLength = telegramBytes.length;
        ByteBuffer outputBuffer = ByteBuffer.wrap(telegramBytes);

        // �w�b�_��ϊ�����
        outputBuffer.rewind();
        outputBuffer.putInt(telegramLength);

        return outputBuffer.array();
    }

    /**
     * ���s��Ԃ�ݒ肵�܂��B<br />
     * 
     * @param isRunning ���s���
     */
    public void setRunning(final boolean isRunning)
    {
        this.isRunning_ = isRunning;
    }

    /**
     * �d�����f�o�b�O�o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param response ��M�d��
     * @param length �d����
     */
    public void logTelegram(final String message, final Telegram response, final int length)
    {
        String telegramStr = TelegramUtil.toPrintStr(response, length);
        outputLog("DECC0108", message, this.channel_.socket().getInetAddress().getHostAddress(),
                   this.channel_.socket().getPort(), telegramStr);
    }

    /**
     * close
     */
    public void shutdown()
    {
        isShutdown_ = true;
        setRunning(false);    

        // �d�����M�X���b�h�Ɋ��荞��Œ�~����B
        this.clientSendThread_.interrupt();
    }
    
    /**
     * ���O���o�͂��܂��B<br />
     * 
     * @param messageCode ���b�Z�[�W�R�[�h
     * @param args ����
     */
    private void outputLog(final String messageCode, final Object... args)
    {
        if (isOutputLog_)
        {
            LOGGER.log(messageCode, args);
        }
    }
}
