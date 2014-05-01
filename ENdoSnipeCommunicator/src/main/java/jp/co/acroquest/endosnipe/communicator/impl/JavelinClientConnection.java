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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * �N���C�A���g�ƒʐM���s���N���X�ł��B<br />
 * 
 * @author eriguchi
 *
 */
public class JavelinClientConnection
{
    private static final int SO_TIMEOUT = 10000;

    private static final int SEND_QUEUE_SIZE = 100;

    private static final int BUFFER_SIZE = 512 * 1024;

    /** �d���j����\������Ԋu */
    private static final int TELEGRAM_DISCARD_INTERVAL = 5000;

    /** �O��d����j���������� */
    private long lastDiscard_;

    /** �j�������d���̍��v�T�C�Y */
    private int discardSum_;

    /** �N���C�A���g�\�P�b�g */
    private Socket clientSocket_ = null;

    /** ���͗p�X�g���[�� */
    private BufferedInputStream inputStream_ = null;

    /** ���͗p�X�g���[�� */
    private BufferedOutputStream outputStream_ = null;

    private final BlockingQueue<byte[]> queue_;

    /**
     * ���M�p�L���[�A�\�P�b�g�A���o�̓X�g���[�����\�z���܂��B<br />
     * 
     * @param objSocket �N���C�A���g�\�P�b�g
     * @param discard �A���[�����M�Ԋu���ɔ������������A���[����j�����邩�ǂ���
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    public JavelinClientConnection(final Socket objSocket, boolean discard)
        throws IOException
    {
        this.queue_ = new ArrayBlockingQueue<byte[]>(SEND_QUEUE_SIZE);
        this.clientSocket_ = objSocket;
        this.lastDiscard_ = 0;
        this.discardSum_ = 0;
        try
        {
            this.inputStream_ = new BufferedInputStream(this.clientSocket_.getInputStream());
            this.outputStream_ = new BufferedOutputStream(this.clientSocket_.getOutputStream());
        }
        catch (IOException ioe)
        {
            close();
            throw ioe;
        }
    }

    /**
     * �I�������ł��B<br />
     * 
     */
    void close()
    {
        String key = "";
        String message = "";
        try
        {
            if (this.clientSocket_ != null && this.clientSocket_.isClosed() == false)
            {
                key = "javelin.communicate.commonMessage.clientDisconnected";
                message = CommunicatorMessages.getMessage(key, this.clientSocket_.getInetAddress());
                stopSendThread();
                this.clientSocket_.close();
                SystemLogger.getInstance().info(message);
            }
        }
        catch (IOException ioe)
        {
            key = "javelin.communicate.commonMessage.clientSocketCloseError";
            message = CommunicatorMessages.getMessage(key);
            SystemLogger.getInstance().warn(message, ioe);
        }

    }

    private void stopSendThread()
    {
        this.sendAlarm(new byte[0]);
    }

    /**
     * ���M�����ł��B<br />
     * 
     * @param byteOutputArr �o�̓f�[�^�̃o�C�g�z��
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    void send(final byte[] byteOutputArr)
        throws IOException
    {
        if (this.clientSocket_.isClosed() == true)
        {
            return;
        }

        int headerLength = TelegramUtil.TELEGRAM_HEADER_LENGTH;
        this.outputStream_.write(byteOutputArr, 0, headerLength);
        this.outputStream_.flush();

        int currentPos = headerLength;
        int remainLength = byteOutputArr.length - headerLength;
        while(remainLength > 0)
        {
            int writeLength = Math.min(BUFFER_SIZE, remainLength);
            this.outputStream_.write(byteOutputArr, currentPos, writeLength);
            this.outputStream_.flush();
            remainLength -= writeLength;
            currentPos += writeLength;
        }
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
        String hostAddress = this.clientSocket_.getInetAddress().getHostAddress();
        SystemLogger.getInstance().warn(
                                        message + hostAddress + ":" + this.clientSocket_.getPort()
                                                + SystemLogger.NEW_LINE + telegramStr);
    }

    /**
     * ��M�d����byte�z���Ԃ��B
     * 
     * @return byte�z��
     * @throws IOException ���o�͗�O�̔���
     */
    byte[] recvRequest()
        throws IOException
    {
        this.clientSocket_.setSoTimeout(SO_TIMEOUT);

        byte finalTelegram = TelegramConstants.HALFWAY_TELEGRAM;
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        do
        {
            // �w�b�_��ǂݍ���
            byte[] header = new byte[Header.HEADER_LENGTH];
            readFull(header, 0, header.length);

            ByteBuffer headerBuffer = ByteBuffer.wrap(header);
            int telegramLength = headerBuffer.getInt();
            headerBuffer.getLong();
            finalTelegram = headerBuffer.get();
            
            if (resultStream.size() == 0)
            {
                resultStream.write(headerBuffer.array());
            }

            if (telegramLength - Header.HEADER_LENGTH < 0)
            {
                throw new IOException("Telegram length is abnormal.");
            }
            if (telegramLength > TelegramUtil.TELEGRAM_LENGTH_MAX)
            {
                throw new IOException("Telegram length is too long.");
            }

            SystemLogger.getInstance().debug("telegramLength  = [" + telegramLength + "]");

            // �w�b�_�Ɩ{�����܂߂��f�[�^���i�[����z��
            byte[] telegram = new byte[telegramLength - header.length];

            // �{����ǂݍ���
            readFull(telegram, 0, telegramLength - header.length);

            resultStream.write(telegram);

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
     * �X�g���[������A�w�肳�ꂽ�o�C�g����ǂݍ��݂܂��B<br />
     *
     * �w�肳�ꂽ�o�C�g�����̃f�[�^��ǂݍ��߂�܂ŁA���̃��\�b�h�͏�����Ԃ��܂���B<br />
     *
     * �f�[�^�i�[��ɂ́A�Œ� <code>(offset + length)</code> ���̗̈悪�K�v�ł��B<br />
     *
     * @param data �f�[�^�i�[��
     * @param offset �f�[�^���i�[����ŏ��̈ʒu�i data[offset] �ɁA�ŏ��̃f�[�^���i�[�����j
     * @param length �f�[�^��ǂݍ��ރo�C�g��
     * @throws IOException �ǂݍ��ݒ��ɃG���[�����������ꍇ
     */
    private void readFull(byte[] data, final int offset, final int length)
        throws IOException
    {
        // read() ��1��ł��ׂẴf�[�^��ǂݍ��ނ��Ƃ��ł��Ȃ����߁A���ׂẴf�[�^��ǂݍ��ނ܂ŌJ��Ԃ�
        int pos = offset;
        int remainLength = length;
        while (remainLength > 0)
        {
            int inputCount = this.inputStream_.read(data, pos, remainLength);
            if (inputCount < 0)
            {
                throw new IOException("Cannot read.");
            }
            pos += inputCount;
            remainLength -= inputCount;
        }
    }

    /**
     * �A���[���𑗐M���܂��B<br />
     * ���̃��\�b�h�̓X���b�h�O����Ă΂�܂��B<br />
     * 
     * @param telegramArray �d���̃o�C�g�z��
     */
    public void sendAlarm(final byte[] telegramArray)
    {
        Socket clientSocket = this.clientSocket_;
        if (clientSocket == null || clientSocket.isClosed())
        {
            return;
        }        
        
        boolean offerResult = this.queue_.offer(telegramArray);
        
        if (offerResult == false && telegramArray != null && telegramArray.length > 0)
        {
            long time = System.currentTimeMillis();
            if (time - this.lastDiscard_ > TELEGRAM_DISCARD_INTERVAL)
            {
                SystemLogger.getInstance().warn(
                                                "Telegram Discard:length = "
                                                        + telegramArray.length);
                this.lastDiscard_ = time;
                this.discardSum_ = 0;
            }
            else
            {
                this.discardSum_ += telegramArray.length;
            }
        }
    }

    /**
     * �\�P�b�g�����Ă��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return �\�P�b�g�����Ă���ꍇ�A<code>true</code>
     */
    public boolean isClosed()
    {
        Socket clientSocket = this.clientSocket_;
        return clientSocket == null || clientSocket.isClosed();
    }
    
    /**
     * �\�P�b�g�̐ڑ���Ԃ�Ԃ��܂��B
     * @return �\�P�b�g���ڑ�����Ă���ꍇ�A<code>true</code>
     */
    public boolean isConnected()
    {
        Socket clientSocket = this.clientSocket_;
        return clientSocket != null && clientSocket.isConnected();
    }

    /**
     * �L���[����f�[�^�����o���܂��B<br />
     *
     * @return ���o�����f�[�^
     * @throws InterruptedException ���荞�ݏ������������Ƃ�
     */
    byte[] take()
        throws InterruptedException
    {
        return queue_.take();
    }
}
