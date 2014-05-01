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
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.communicator.CommunicationServer;
import jp.co.acroquest.endosnipe.communicator.CommunicatorListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.ConnectNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.impl.DataCollectorClient.DataCollectorClientListener;

/**
 * �N���C�A���g����̐ڑ���҂��󂯂�DataCollector�T�[�o�B<br />
 * 
 * @author matsuoka
 */
public class DataCollectorServer implements CommunicationServer, Runnable
{
    /** �o�C���h�Ԋu�̃f�t�H���g�l (msec) */
    private static final int DEFAULT_BIND_INTERVAL = 5 * 1000;

    /** �ڑ��҂��󂯃X���b�h�� */
    private static final String ACCEPT_THREAD_NAME = "JavelinServerThread";

    /** �N���C�A���g�X���b�h�O���[�v�� */
    private static final String CLIENT_THREAD_GROUP_NAME = "JavelinClientThreadGroup";

    /** �N���C�A���g�X���b�h�� */
    private static final String CLIENT_THREAD_NAME = "JavelinClientThread";

    /** Javelin/BottleneckEye��҂��󂯂�|�[�g�ԍ� */
    private int port_;

    /** �T�[�o�\�P�b�g */
    private ServerSocket serverSocket_ = null;

    /** �N���C�A���gID���L�[�Ƃ����N���C�A���g�̃��X�g */
    protected Map<String, DataCollectorClient> clientList_ =
            new HashMap<String, DataCollectorClient>();

    /** �X���b�h���������ǂ�����\���t���O */
    boolean isRunning_ = false;

    /** �|�[�g���X�j���O�����ǂ�����\���t���O */
    boolean isListening_ = false;

    /** �ʐM�p�X���b�h */
    private Thread acceptThread_;

    /** Bind���s���̍Ď��s�Ԋu */
    private int bindInterval_;

    /** Javelin�N���C�A���g��DB�����Ƃ̃V�[�P���X�ԍ��}�b�v */
    private Map<String, Set<Integer>> javelinSeqMap_ = new HashMap<String, Set<Integer>>();

    /** �N���C�A���gID�V�[�P���X�ԍ� */
    private BigInteger seqClientId_ = BigInteger.ZERO;

    /** �N���C�A���g��Ԃ��ω��������̃��X�i */
    private List<ClientNotifyListener> clientNotifyListenerList_ =
            new ArrayList<ClientNotifyListener>();

    /** Javelin�N���C�A���g�̓d�����������郊�X�i */
    private List<TelegramListener> javelinClientTelegramListener_ =
            new ArrayList<TelegramListener>();

    /** ����N���C�A���g�̓d�����������郊�X�i */
    private List<TelegramListener> controlClientTelegramListener_ =
            new ArrayList<TelegramListener>();

    /**
     * �N���C�A���g����̐ڑ��E�ؒf���������邽�߂̃��X�i�B
     * @author matsuoka
     */
    public interface ClientNotifyListener
    {
        /**
         * �N���C�A���g�Ƃ̐ڑ����m�������Ƃ��ɃR�[�������B
         * @param client �N���C�A���g
         */
        void clientConnected(DataCollectorClient client);

        /**
         * �N���C�A���g�Ƃ̐ڑ����ؒf���ꂽ�Ƃ��ɃR�[�������B
         * @param client �N���C�A���g
         * @param forceDisconnected �����ؒf���ꂽ�ꍇ�� <code>true</code>
         */
        void clientDisconnected(DataCollectorClient client, boolean forceDisconnected);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // �������Ȃ�
    }

    /**
     * {@inheritDoc}
     */
    public void start(final int port)
    {
        start(port, DEFAULT_BIND_INTERVAL);
    }

    /**
     * �T�[�o���J�n����B
     * @param port �|�[�g�ԍ�
     * @param bindInterval Bind���s���̍Ď��s�Ԋu
     */
    public void start(final int port, final int bindInterval)
    {
        port_ = port;
        bindInterval_ = bindInterval;

        if (this.serverSocket_ != null)
        {
            return;
        }
        if (acceptThread_ != null)
        {
            return;
        }

        // �N���C�A���g�ڑ��̎�t���J�n����B
        acceptThread_ = new Thread(this, ACCEPT_THREAD_NAME);
        acceptThread_.setDaemon(true);
        acceptThread_.start();
    }

    /**
     * �ڑ��҂��󂯂̃��C�������B
     */
    public void run()
    {
        isRunning_ = true;

        serverSocket_ = createServerSocket();
        if (serverSocket_ == null)
        {
            return;
        }

        ThreadGroup group = new ThreadGroup(CLIENT_THREAD_GROUP_NAME);
        while (isRunning_)
        {
            try
            {
                accept(group);
            }
            catch (Throwable th)
            {
                // CHECKSTYLE:OFF
                ; // �������Ȃ�
                  // CHECKSTYLE:ON
            }
        }

        synchronized (clientList_)
        {
            for (DataCollectorClient client : clientList_.values())
            {
                client.stop();
            }
        }

        try
        {
            if (serverSocket_ != null && !serverSocket_.isClosed())
            {
                serverSocket_.close();
            }
        }
        catch (IOException e)
        {
            // CHECKSTYLE:OFF
            ; // �������Ȃ�
              // CHECKSTYLE:ON
        }
    }

    /**
     * �T�[�o�\�P�b�g���쐬����B
     * @return �쐬�����T�[�o�\�P�b�g��Ԃ��B
     */
    private ServerSocket createServerSocket()
    {
        ServerSocket socket = null;

        while (isRunning_)
        {
            try
            {
                socket = new ServerSocket(port_);
                break;
            }
            catch (IOException e)
            {
                // �\�P�b�g�̍쐬�Ɏ��s������A�w��Ԋu�X���[�v���Ă���Ď��s����B
                try
                {
                    Thread.sleep(bindInterval_);
                }
                catch (InterruptedException iex)
                {
                    // CHECKSTYLE:OFF
                    ; // �������Ȃ�
                      // CHECKSTYLE:ON
                }
            }
        }
        isListening_ = true;

        return socket;
    }

    private void accept(final ThreadGroup group)
        throws SocketException
    {
        Socket clientSocket = null;

        try
        {
            clientSocket = serverSocket_.accept();
        }
        catch (SocketException e)
        {
            // stop()�Ń\�P�b�g������ꍇ��SocketException����������B
            throw e;
        }
        catch (IOException e)
        {
            return;
        }

        sweepClient();

        // �N���C�A���g����̗v����t�p�ɁA�����X���b�h���N������B
        DataCollectorClient clientRunnable;
        try
        {
            String clientId = getClientId();
            clientRunnable = createClientThread(clientSocket, clientId);
            String clientThreadName = String.format("%s-%s", CLIENT_THREAD_NAME, clientId);
            Thread objHandleThread = new Thread(group, clientRunnable, clientThreadName);
            objHandleThread.setDaemon(true);
            objHandleThread.start();

            // �ʒm�̂��߂̃N���C�A���g���X�g�ɒǉ�����B
            synchronized (this.clientList_)
            {
                this.clientList_.put(clientId, clientRunnable);
            }
        }
        catch (IOException ioe)
        {
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void stop()
    {
        isRunning_ = false;

        if (this.isListening_ == false)
        {
            try
            {
                //�@�ʐM�p�|�[�gBind�҂���Ԃ̂��߂ɁA���荞�݂��s��
                Thread acceptThread = this.acceptThread_;
                if (acceptThread != null)
                {
                    acceptThread.interrupt();
                }
            }
            catch (Exception ex)
            {
                // CHECKSTYLE:OFF
                ;
                // CHECKSTYLE:ON
            }
        }

        if (this.isListening_)
        {
            // �҂��󂯃\�P�b�g����邱�Ƃɂ��Aaccept()��SocketException��
            // �������A�҂��󂯃X���b�h����~����B
            if (this.serverSocket_ != null)
            {
                try
                {
                    this.serverSocket_.close();
                }
                catch (Exception ex)
                {
                    // CHECKSTYLE:OFF
                    ;
                    // CHECKSTYLE:ON
                }
            }

            this.isListening_ = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isConnected()
    {
        int clientCount = sweepClient();
        if (clientCount == 0)
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int getActiveClient()
    {
        return clientList_.size();
    }

    /**
     * {@inheritDoc}
     */
    public void addCommunicatorListener(final CommunicatorListener listener)
    {
        // �������Ȃ�
    }

    /**
     * {@inheritDoc}
     */
    public void sendTelegram(final Telegram telegram)
    {
        // �������Ȃ�
    }

    /**
     * {@inheritDoc}
     */
    public void addTelegramListener(final TelegramListener listener)
    {
        // �������Ȃ�
    }

    /**
     * �ڑ����؂ꂽ�N���C�A���g��|������B
     * @return
     */
    private int sweepClient()
    {
        int size;
        synchronized (clientList_)
        {
            Map<String, DataCollectorClient> newClientList =
                    new HashMap<String, DataCollectorClient>();
            for (String key : clientList_.keySet())
            {
                DataCollectorClient client = clientList_.get(key);
                if (!client.isClosed())
                {
                    newClientList.put(key, client);
                }
            }
            clientList_.clear();
            clientList_ = newClientList;
            size = clientList_.size();
        }

        return size;
    }

    /**
     * JavelinClient�R�l�N�V�����I�u�W�F�N�g�𐶐����܂��B
     *
     * @param clientSocket �\�P�b�g
     * @param clientId �N���C�A���gID
     * @return JavelinClient�R�l�N�V�����I�u�W�F�N�g
     * @throws IOException ���o�͗�O�����������ꍇ
     */
    protected DataCollectorClient createClientThread(final Socket clientSocket, String clientId)
        throws IOException
    {
        DataCollectorClient client = new DataCollectorClient(clientSocket, false, clientId);

        DataCollectorClientListener listener = new DataCollectorClientListener() {
            public void disconnected(DataCollectorClient client, boolean forceDisconnected)
            {
                ConnectNotifyData notifyData = client.getConnectNotifyData();
                if (notifyData == null)
                {
                    client.stop();
                    return;
                }

                switch (notifyData.getKind())
                {
                case ConnectNotifyData.KIND_JAVELIN:
                    removeJavelinSequenceNo(notifyData.getAgentName(), client.getDbNo());
                    break;

                default:
                    break;
                }

                // �N���C�A���g���ؒf���ꂽ��A�o�^����Ă��郊�X�i�ɒʒm����B
                for (ClientNotifyListener listener : clientNotifyListenerList_)
                {
                    listener.clientDisconnected(client, forceDisconnected);
                }
            }

            public void receiveConnectNotify(DataCollectorClient client)
            {
                ConnectNotifyData notifyData = client.getConnectNotifyData();
                switch (notifyData.getKind())
                {
                case ConnectNotifyData.KIND_JAVELIN:
                    String agentName = notifyData.getAgentName();
                    int no = getJavelinSequenceNo(agentName);
                    agentName = ConnectNotifyAccessor.createAgentName(agentName, no);
                    client.setAgentName(agentName);
                    client.setDbNo(no);
                    break;
                case ConnectNotifyData.KIND_CONTROLLER:
                    switch (notifyData.getPurpose())
                    {
                    case ConnectNotifyData.PURPOSE_GET_RESOURCE:
                        client.setTelegramListener(controlClientTelegramListener_);
                        break;
                    case ConnectNotifyData.PURPOSE_GET_DATABASE:
                    default :
                        // �����Ȃ�
                        break;
                    
                    }

                    break;

                default:
                    break;
                }

                // �N���C�A���g��L��������B
                client.setEnabled(true);

                // �o�^����Ă��郊�X�i�ɒʒm����B
                for (ClientNotifyListener listener : clientNotifyListenerList_)
                {
                    listener.clientConnected(client);
                }
            }
        };
        client.setClientListener(listener);

        return client;
    }

    /**
     * �N���C�A���g��ԕϊ��ʒm�p�̃��X�i��o�^����B
     * @param listener ���X�i
     */
    public void addClientNotifyListener(final ClientNotifyListener listener)
    {
        clientNotifyListenerList_.add(listener);
    }

    /**
     * Javelin�N���C�A���g�p�̓d�����X�i��o�^����B
     * @param listener ���X�i
     */
    public void addJavelinClientTelegramListener(TelegramListener listener)
    {
        javelinClientTelegramListener_.add(listener);
    }

    /**
     * ����N���C�A���g�p�̓d�����X�i��o�^����B
     * @param listener ���X�i
     */
    public void addControlClientTelegramListener(TelegramListener listener)
    {
        controlClientTelegramListener_.add(listener);
    }

    /**
     * �N���C�A���gID���L�[�Ƃ��ăN���C�A���g���擾����B
     * @param clientId �L�[�ƂȂ�N���C�A���gID
     * @return �w�肳�ꂽ�N���C�A���gID�̃N���C�A���g��Ԃ��B
     */
    public DataCollectorClient getClient(String clientId)
    {
        return clientList_.get(clientId);
    }

    private int getJavelinSequenceNo(String dbName)
    {
        int seq = 0;

        synchronized (javelinSeqMap_)
        {
            Set<Integer> seqSet = javelinSeqMap_.get(dbName);
            if (seqSet == null)
            {
                seqSet = new HashSet<Integer>();
                javelinSeqMap_.put(dbName, seqSet);
            }

            while (seqSet.contains(seq))
            {
                seq++;
            }
            seqSet.add(seq);
        }

        return seq;
    }

    private void removeJavelinSequenceNo(String dbName, int seq)
    {
        synchronized (javelinSeqMap_)
        {
            Set<Integer> seqSet = javelinSeqMap_.get(dbName);
            if (seqSet == null)
            {
                return;
            }

            if (seqSet.contains(seq))
            {
                seqSet.remove(seq);
            }

            if (seqSet.size() == 0)
            {
                javelinSeqMap_.remove(dbName);
            }
        }
    }

    /**
     * �N���C�A���gID��Ԃ��B
     * @return �̔Ԃ��ꂽ�N���C�A���gID
     */
    private String getClientId()
    {
        String clientId = seqClientId_.toString();
        seqClientId_ = seqClientId_.add(BigInteger.valueOf(1));
        return clientId;
    }

}
