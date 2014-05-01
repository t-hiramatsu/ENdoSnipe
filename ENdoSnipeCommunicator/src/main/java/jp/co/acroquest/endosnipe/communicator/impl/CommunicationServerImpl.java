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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.CommunicationServer;
import jp.co.acroquest.endosnipe.communicator.CommunicatorListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.communicator.impl.JavelinClientThread.JavelinClientThreadListener;

/**
 * �ʐM�����̃T�[�o���̎����B
 *
 * @author eriguchi
 */
public class CommunicationServerImpl implements Runnable, CommunicationServer, TelegramConstants
{
    /** ���K�[�N���X */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(CommunicationServerImpl.class);


    private static final int MAX_SOCKET = 1000;

    /** �|�[�g�ԍ��̍ő�l */
    private static final int MAX_PORT = 65535;
    
    /** �T�[�o�\�P�b�g */
    ServerSocket objServerSocket_ = null;


    /** �N���C�A���g�̃��X�g */
    protected List<JavelinClientThread> clientList_ = new ArrayList<JavelinClientThread>();

    /** �X���b�h���������ǂ�����\���t���O */
    boolean isRunning_ = false;

    /** �ʐM������\���t���O */
    private boolean isListening_ = false;

    /** Javelin�ƒʐM���s���|�[�g */
    private int port_;

    /** Javelin�ƒʐM���s�������|�[�g�ԍ� */
    private int startPort_;
    
    /** �ʐM�p�X���b�h�� */
    private String acceptThreadName_ = "JavelinAcceptThread";

    /** �ʐM�p�X���b�h */
    private Thread acceptThread_;

    /** �ʐM�Ɏg�p����|�[�g��͈͎w�肷�邩�A�̃t���O */
    private boolean isRange_ = false;

    /** �ʐM�Ɏg�p����|�[�g��͈͎w�肷��ۂ̍ő�l */
    private int rangeMax_;

    private long waitForThreadStart_;

    private int bindInterval_;
    
    private boolean discard_;
    
    private String[] listeners_;
    
    /** CommunicationServer�̏�ԕω���ʒm���郊�X�i�̃��X�g */
    private final List<CommunicatorListener> listenerList_;

    /** Javelin���ǂ��� */
    protected boolean isJavelin_ = false;

    /**
     * @return discard
     */
    public boolean isDiscard()
    {
        return this.discard_;
    }
    
    /**
     * @return listeners
     */
    public String[] getListeners()
    {
        return this.listeners_;
    }

    
    /**
     * �T�[�o�C���X�^���X�𐶐����܂��B
     *
     * @param isRange �ڑ��|�[�g�ɔ͈͎w��𗘗p����ꍇ�� <code>true</code>
     * @param rangeMax �ڑ��|�[�g�ɔ͈͎w��𗘗p����ꍇ�͈̔͂̍ő�l
     * @param waitForThreadStart �X���b�h�J�n�܂ł̑҂����ԁi�~���b�j
     * @param bindInterval �|�[�g�I�[�v���̎��s�Ԋu�i�b�j
     * @param listeners ���p����TelegramListener��
     */
    public CommunicationServerImpl(boolean isRange, int rangeMax, long waitForThreadStart,
            int bindInterval, String[] listeners)
    {
        this.isRange_ = isRange;
        this.rangeMax_ = rangeMax;
        this.waitForThreadStart_ = waitForThreadStart;
        this.bindInterval_ = bindInterval;
        this.listeners_ = listeners;
        this.listenerList_ = new ArrayList<CommunicatorListener>();
    }

    
    /**
     * �T�[�o�C���X�^���X�𐶐����܂��B
     *
     * @param isRange �ڑ��|�[�g�ɔ͈͎w��𗘗p����ꍇ�� <code>true</code>
     * @param rangeMax �ڑ��|�[�g�ɔ͈͎w��𗘗p����ꍇ�͈̔͂̍ő�l
     * @param waitForThreadStart �X���b�h�J�n�܂ł̑҂����ԁi�~���b�j
     * @param bindInterval �|�[�g�I�[�v���̎��s�Ԋu�i�b�j
     * @param listeners ���p����TelegramListener��
     * @param threadName �ʐM�p�X���b�h��
     */
    public CommunicationServerImpl(boolean isRange, int rangeMax, long waitForThreadStart,
            int bindInterval, String[] listeners, String threadName)
    {
        this(isRange, rangeMax, waitForThreadStart, bindInterval, listeners);
        this.acceptThreadName_ = threadName;
    }

    /**
     * {@inheritDoc}
     */
    public int getActiveClient()
    {
        return this.clientList_.size();
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // �������Ȃ��B
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
	public void start(int port)
    {
        if (this.objServerSocket_ != null)
        {
            return;
        }

        this.startPort_ = port;
        this.port_ = this.startPort_;

        if (this.isRange_ == true)
        {
            String key = "";
            String message = "";
            if (isPortNumValid(this.port_, this.rangeMax_) == true)
            {
                key = "javelin.communicate.JavelinAcceptThread.initRange";
                message = CommunicatorMessages.getMessage(key, this.startPort_, this.rangeMax_);
                LOGGER.info(message);
            }
            else
            {
                key = "javelin.communicate.JavelinAcceptThread.rangeError";
                message = CommunicatorMessages.getMessage(key, this.startPort_);
                LOGGER.warn(message);
                this.isRange_ = false;
            }
        }

        // �N���C�A���g�ڑ��̎�t���J�n����B
        try
        {
            this.acceptThread_ = new Thread(this, acceptThreadName_);
            this.acceptThread_.setDaemon(true);
            this.acceptThread_.start();
        }
        catch (Exception objException)
        {
            LOGGER.warn(objException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
	public void stop()
    {
        this.isRunning_ = false;
        
        if (this.isListening_ == false)
        {
            try
            {
                //�@�ʐM�p�|�[�gBind�҂���Ԃ̂��߂ɁA���荞�݂��s��
                Thread acceptThread = this.acceptThread_;
                if(acceptThread != null)
                {
                    acceptThread.interrupt();
                }
            }
            catch (Exception ex)
            {
                LOGGER.warn(ex);
            }
        }
        
        if (this.isListening_)
        {
            // �҂��󂯃\�P�b�g����邱�Ƃɂ��Aaccept()��SocketException��
            // �������A�҂��󂯃X���b�h����~����B
            if (this.objServerSocket_ != null)
            {
                try
                {
                    this.objServerSocket_.close();
                }
                catch (Exception ex)
                {
                    LOGGER.warn(ex);
                }
            }

            this.isListening_ = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isConnected()
    {
        return false;
    }
    
    /**
     * �N���C�A���g��Telegram�𑗐M����B
     * 
     * @param telegram ���M����d���B
     */
    public void sendTelegram(Telegram telegram)
    {

        if (telegram == null)
        {
            return;
        }
        
        boolean isSweep = false;

        List<byte[]> byteList = TelegramUtil.createTelegram(telegram);
        for (byte[] bytes : byteList)
        {
            List<JavelinClientThread> clientList = this.clientList_;
            int size = clientList.size();
            for (int index = size - 1; index >= 0; index--)
            {
                JavelinClientThread client = null;
                synchronized (clientList)
                {
                    if (index < clientList.size())
                    {
                        client = clientList.get(index);
                    }
                    else
                    {
                        continue;
                    }
                }
                
                if (client.isClosed())
                {
                    isSweep = true;
                    continue;
                }
                
                client.sendAlarm(bytes);
                if (LOGGER.isDebugEnabled())
                {
                    client.logTelegram(telegram, bytes);
                }
            }
        }                    
        
        
        if (isSweep == true)
        {
            sweepClient();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addTelegramListener(TelegramListener listener)
    {
        // Do nothing.
    }

    /**
     * �ʐM�p�X���b�h�����s����B
     */
    @SuppressWarnings("deprecation")
	public void run()
    {
        try
        {
            Thread.sleep(this.waitForThreadStart_);
        }
        catch (Exception ex)
        {
            // Do nothing.
            SystemLogger.getInstance().warn(ex);
        }

        ThreadGroup group = new ThreadGroup("JavelinThreadGroup");
        String key = "";
        String message = "";

        this.isRunning_ = true;

        while (this.isRunning_ == true && this.isListening_ == false)
        {
            try
            {
                this.objServerSocket_ = new ServerSocket(this.port_);

                key = "javelin.communicate.JavelinAcceptThread.start";
                message = CommunicatorMessages.getMessage(key, this.port_);
                LOGGER.info(message);
                this.isListening_ = true;
            }
            catch (IOException objIOException)
            {
                int interval = this.bindInterval_;
                key = "javelin.communicate.JavelinAcceptThread.restart";
                message = CommunicatorMessages.getMessage(key, this.port_, interval);
                LOGGER.warn(message);
                if (this.isRange_ == true)
                {
                    // �|�[�g�ԍ����P���₵�čĐڑ����s���B
                    // �ڑ��͈͂𒴂����ꍇ�ɂ́Ajavelin.bind.interval�̊ԃX���[�v������A�������ēx���s����B 
                    this.port_++;
                    if (this.port_ > this.rangeMax_)
                    {
                        key = "javelin.communicate.JavelinAcceptThread.overRange";
                        message =
                                CommunicatorMessages.getMessage(key, this.rangeMax_,
                                                                this.startPort_);
                        LOGGER.info(message);
                        this.port_ = this.startPort_;
                    }
                }
                sleep();
            }
        }

        while (this.isRunning_)
        {
            try
            {
                try
                {
                    accept(group);
                }
                catch (RuntimeException re)
                {
                    key = "javelin.communicate.snmp.TrapListener.SendingTelegramErrorMessage";
                    message = CommunicatorMessages.getMessage(key);
                    LOGGER.warn(message, re);
                }
            }
            catch (Throwable th)
            {
                LOGGER.warn(th);
            }
        }

        synchronized (this.clientList_)
        {
            for (int index = this.clientList_.size() - 1; index >= 0; index--)
            {
                JavelinClientThread client = this.clientList_.get(index);
                client.stop();
            }
        }

        try
        {
            if (this.objServerSocket_ != null && this.isConnected())
            {
                this.objServerSocket_.close();
            }
        }
        catch (IOException ioe)
        {
            key = "javelin.communicate.commonMessage.serverSocketCloseError";
            message = CommunicatorMessages.getMessage(key);
            LOGGER.warn(message, ioe);
        }
    }

    @SuppressWarnings("deprecation")
	private void accept(final ThreadGroup group) throws SocketException
    {
        Socket clientSocket = null;
        
        String key = "";
        String message = "";
        try
        {
            // ���j�^�[
            clientSocket = this.objServerSocket_.accept();
        }
        catch (SocketException se)
        {
            // stop()�Ń\�P�b�g������ꍇ��SocketException����������B
            throw se;
        }
        catch (IOException ioe)
        {
            key = "javelin.communicate.commonMessage.serverSocketAcceptError";
            message = CommunicatorMessages.getMessage(key);
            LOGGER.warn(message, ioe);
            return;
        }

        int clientCount = sweepClient();
        if (clientCount > MAX_SOCKET)
        {
            LOGGER.info("�ڑ������ő吔[" + MAX_SOCKET + "]�𒴂������߁A�ڑ������ۂ��܂��B");
            try
            {
                clientSocket.close();
            }
            catch (IOException ioe)
            {
                key = "javelin.communicate.commonMessage.clientSocketCloseError";
                message = CommunicatorMessages.getMessage(key);
                LOGGER.warn(message, ioe);
            }
            return;
        }

        InetAddress clientIP = clientSocket.getInetAddress();
        key = "javelin.communicate.commonMessage.clientConnect";
        message = CommunicatorMessages.getMessage(key, clientIP);
        LOGGER.info(message);
        
        // �N���C�A���g����̗v����t�p�ɁA�����X���b�h���N������B
        JavelinClientThread clientRunnable;
        try
        {
            clientRunnable = createJavelinClientThread(clientSocket);
            Thread objHandleThread =
                    new Thread(group, clientRunnable,
                               acceptThreadName_ + "-JavelinClientThread-" + clientCount);
            objHandleThread.setDaemon(true);
            objHandleThread.start();

            // �ʒm�̂��߂̃N���C�A���g���X�g�ɒǉ�����B
            synchronized (this.clientList_)
            {
                this.clientList_.add(clientRunnable);
            }
        }
        catch (IOException ioe)
        {
            LOGGER.warn("�N���C�A���g�ʐM�X���b�h�̐����Ɏ��s���܂����B", ioe);
        }
        
        // �ڑ����������X�i�ɒʒm
        String hostName = clientIP.getHostName();
        String ip = clientIP.getHostAddress();
        int port = clientSocket.getPort();
        notifyClientConnected(hostName, ip, port);
    }

    /**
     * JavelinClient�R�l�N�V�����I�u�W�F�N�g�𐶐����܂��B
     *
     * @param clientSocket �\�P�b�g
     * @return JavelinClient�R�l�N�V�����I�u�W�F�N�g
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    protected JavelinClientThread createJavelinClientThread(final Socket clientSocket)
        throws IOException
    {
        JavelinClientThreadListener listener = new JavelinClientThreadListener() {
            public void disconnected(boolean forceDisconnected)
            {
                notifyClientDisconnected(forceDisconnected);
            }
        };
        return new JavelinClientThread(clientSocket, this.discard_, this.listeners_, listener);
    }

    /**
     * �|�[�g�����ɊJ����Ă���ꍇ�ɑҋ@����B
     */
    @SuppressWarnings("deprecation")
	private void sleep()
    {
        int interval = this.bindInterval_;

        try
        {
            Thread.sleep(interval);
        }
        catch (InterruptedException iex)
        {
            LOGGER.warn(iex);
        }
    }

    private int sweepClient()
    {
        int size;
        synchronized (this.clientList_)
        {
            for (int index = this.clientList_.size() - 1; index >= 0; index--)
            {
                JavelinClientThread client = this.clientList_.get(index);
                if (client.isClosed())
                {
                    this.clientList_.remove(index);
                }
            }
            size = this.clientList_.size();
        }

        return size;
    }

    /**
     * �����|�[�g�A�|�[�g�ő�l������Ȕ͈͂̒l�ɂȂ��Ă��邩�𔻒肷��B
     * 
     * @param port �����|�[�g
     * @param portMax �|�[�g�ő�l
     * @return true �����|�[�g�A�|�[�g�ő�l������Ȕ͈͂̒l�ɂȂ��Ă���ꍇ�A<code>true</code>
     */
    private static boolean isPortNumValid(final int port, final int portMax)
    {
        if (portMax < 0 || portMax > MAX_PORT)
        {
            return false;
        }
        if (port > portMax || port < 0)
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void addCommunicatorListener(CommunicatorListener listener)
    {
        synchronized (this.listenerList_)
        {
            this.listenerList_.add(listener);
        }
    }
    
    /**
     * �ؒf���ꂽ���Ƃ��e���X�i�֒ʒm���܂��B<br />
     *
     * @param forceDisconnected �����ؒf���ꂽ�ꍇ�� <code>true</code>
     */
    private void notifyClientDisconnected(boolean forceDisconnected)
    {
        synchronized (this.listenerList_)
        {
            for (CommunicatorListener listener : this.listenerList_)
            {
                listener.clientDisconnected(forceDisconnected);
            }
        }
    }

    /**
     * �ڑ����ꂽ���Ƃ��e���X�i�֒ʒm���܂��B<br />
     *
     * @param hostName �z�X�g���i <code>null</code> �̉\������j
     * @param ipAddr IP �A�h���X
     * @param port �|�[�g�ԍ�
     */
    private void notifyClientConnected(final String hostName, final String ipAddr, final int port)
    {
        synchronized (this.listenerList_)
        {
            for (CommunicatorListener listener : this.listenerList_)
            {
                listener.clientConnected(hostName, ipAddr, port);
            }
        }
    }

}
