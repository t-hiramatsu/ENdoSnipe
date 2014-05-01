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
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.co.acroquest.endosnipe.common.jmx.JMXManager;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.NetworkUtil;
import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.CommunicatorListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramUtil;
import jp.co.acroquest.endosnipe.communicator.accessor.ConnectNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * {@link CommunicationClient} �̎����N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class CommunicationClientImpl implements CommunicationClient, Runnable
{
	/** ���K�[�N���X */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
			CommunicationClientImpl.class);

	/** ���~���b�P�ʂŕ\����1�b */
	private static final int SECOND_ON_MILLIS = 1000;

	/** �Đڑ����s���Ԋu�i�~���b�j */
	private static final int RETRY_INTERVAL = 10000;

	/** �d����]������^�[�Q�b�g�I�u�W�F�N�g�̃��X�g */
	private final List<CommunicatorListener> listenerList_;

	/** �ʐM�\�P�b�g */
	private SocketChannel socketChannel_ = null;

	/** �o�̓X�g���[�� */
	private PrintStream objPrintStream_ = null;

	private TelegramReader telegramReader_;

	private final ExecutorService writeExecutor_ = createThreadPoolExecutor();

	/** �z�X�g�i�z�X�g���܂��� IP �A�h���X�j */
	private String host_;

	/** �|�[�g�ԍ� */
	private int portNumber_;

	/** IP�A�h���X */
	private String ipAddress_;

	/** �ڑ���� */
	private volatile boolean isConnect_ = false;

	/** start��� */
	private volatile boolean started_ = true;

	/** �ǂݍ��݃X���b�h */
	private Thread readerThread_;

	/** �ǂݍ��݃X���b�h���Ď�����X���b�h */
	private Thread readerMonitorThread_;

	/** �X���b�h�� */
	private final String threadName_;

	/** ���O�o�͗L��. */
	private boolean isOutputLog_ = true;

	/** ��M�d�����������邽�߂̃��X�i */
	private List<TelegramListener> telegramListeners_;

	private boolean discard_;

	/** �ڑ�������ɑ��M����ڑ��ʒm */
	private ConnectNotifyData connectNotify_;

	/** Javelin���ǂ��� */
	protected boolean isJavelin_ = false;

	/**
	 * {@link CommunicationClientImpl} ���\�z���܂��B<br />
	 * 
	 * @param threadName
	 *            �X���b�h��
	 */
	public CommunicationClientImpl(final String threadName)
	{
		this.threadName_ = threadName;

		// listenerList_ �� synchronized ���s���K�v�����邪�A
		// �g�� for ���i iterator �j���g�p����ꍇ�� Collections.synchronizedList
		// �Ń��b�v���Ă����ʂȂ��߁A
		// ���O�� synchronized �������s��
		this.listenerList_ = new ArrayList<CommunicatorListener>();
	}

	/**
	 * {@link CommunicationClientImpl} ���\�z���܂��B<br />
	 * 
	 * @param threadName
	 *            �X���b�h��
	 * @param discard
	 *            discard
	 * @param isOutputLog
	 *            ���O�o�͗L��
	 */
	public CommunicationClientImpl(final String threadName,
			final boolean discard, final boolean isOutputLog)
	{
		this(threadName);
		this.discard_ = discard;
		this.isOutputLog_ = isOutputLog;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void connect(ConnectNotifyData connectNotify)
	{
		this.connectNotify_ = connectNotify;

		String threadName = this.threadName_ + "-ReaderMonitor";
		this.readerMonitorThread_ = new Thread(this, threadName);
		this.readerMonitorThread_.setDaemon(true);
		this.readerMonitorThread_.start();
		this.started_ = true;
	}

	/**
	 * �T�[�o����ؒf���܂��B<br />
	 */
	public void disconnect()
	{
		disconnect(false);
	}

	/**
	 * �T�[�o����ؒf���܂��B<br />
	 * 
	 * @param forceDisconnected
	 *            �����ؒf���ꂽ�ꍇ�� <code>true</code>
	 */
	public void disconnect(final boolean forceDisconnected)
	{
		this.started_ = false;
		this.writeExecutor_.execute(new Runnable() {
			public void run()
			{
				doClose(forceDisconnected);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void shutdown()
	{
		if (this.telegramReader_ != null)
		{
			this.telegramReader_.shutdown();
		}
		this.writeExecutor_.shutdown();
	}

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		// Javelin�Ƃ��ē��삵�Ă���ꍇ�Ajmx.properties�̓ǂݍ��݊�����҂�
		if (isJavelin_)
		{
			JMXManager.getInstance().waitInitialize();
		}
		// �J�n�{�^����������Ă���ԁi��~�{�^�����������܂Łj�Đڑ����J��Ԃ�
		while (this.started_)
		{
			try
			{
				if (doConnect())
				{
					startRead();

					boolean forceDisconnected = false;

					// ��~�{�^����������邩�A�����ؒf����܂Ń��[�v����
					while (this.started_)
					{
						if (this.readerThread_ == null)
						{
							// �����ؒf���ꂽ
							forceDisconnected = true;
							break;
						}
						if (this.readerThread_.isAlive() == false)
						{
							// �����ؒf���ꂽ
							forceDisconnected = true;
							break;
						}
						waitMilliseconds(SECOND_ON_MILLIS);
					}

					stopRead();
					doClose(forceDisconnected);
				}
				else
				{
					// �ڑ��Ɏ��s������A��莞�ԑ҂��Ă���Đڑ����s��
					waitMilliseconds(RETRY_INTERVAL);
				}
			}
			catch (InterruptedException ex)
			{
				LOGGER.log("WECC0202", ex);
			}
		}
		stopRead();
		doClose(false);
	}

	private void waitMilliseconds(final long milliseconds)
			throws InterruptedException
	{
		long remainedMilliseconds = milliseconds;
		while (remainedMilliseconds > 0 && this.started_)
		{
			long sleepTime = Math.min(remainedMilliseconds, SECOND_ON_MILLIS);
			Thread.sleep(sleepTime);
			remainedMilliseconds -= sleepTime;
		}
	}

	/**
	 * �T�[�o����̓d����M�X���b�h���J�n���܂��B<br />
	 */
	public void startRead()
	{
		if (this.readerThread_ != null && this.readerThread_.isAlive())
		{
			return;
		}

		this.readerThread_ = new Thread(this.telegramReader_, this.threadName_
				+ "-Reader");
		this.readerThread_.setDaemon(true);
		if (this.telegramReader_ != null)
		{
			for (TelegramListener listener : this.telegramListeners_)
			{
				this.telegramReader_.addTelegramListener(listener);
			}
			this.telegramReader_.setRunning(true);
		}
		this.readerThread_.start();
	}

	/**
	 * �T�[�o����̓d����M�X���b�h���I�����܂��B<br />
	 */
	private void stopRead()
	{
		if (this.telegramReader_ != null)
		{
			this.telegramReader_.shutdown();
		}

		if (this.readerThread_ != null && this.readerThread_.isAlive())
		{
			// ���荞�݂𔭐������A�I������܂ő҂B
			// join() ���s��Ȃ��ƁA�X���b�h�����S�ɏI�����Ȃ��ԂɍĐڑ����������s���悤�Ƃ��A
			// �X���b�h�������Ă��邽�ߍĐڑ����s��Ȃ��悤�ɂȂ��Ă��܂��B
			this.readerThread_.interrupt();
			try
			{
				this.readerThread_.join();
			}
			catch (InterruptedException ex)
			{
				LOGGER.log("WECC0204", ex);
			}
		}
		this.readerThread_ = null;
	}

	/**
	 * �T�[�o�ɓd���𑗐M���܂��B<br />
	 * 
	 * @param telegram
	 *            �d���I�u�W�F�N�g
	 */
	public void sendTelegram(final Telegram telegram)
	{
		this.writeExecutor_.execute(new Runnable() {
			public void run()
			{
				List<byte[]> byteList = TelegramUtil.createTelegram(telegram);
				for (byte[] byteOutputArr : byteList)
				{
					try
					{
						PrintStream objPrintStream
						= CommunicationClientImpl.this.objPrintStream_;
						if (objPrintStream != null)
						{
							objPrintStream.write(byteOutputArr);
							objPrintStream.flush();

							// �����I�����s��ꂽ�Ƃ��A�Đڑ����s��
							if (objPrintStream.checkError())
							{
								outputLog("WECC0201");
								// ReaderThread ���I��������΁A
								// ReaderMonitorThread
								// ���Đڑ������ɓ���
								stopRead();
							}
						}
					}
					catch (IOException objIOException)
					{
						outputLog("WECC0202", objIOException);
						CommunicationClientImpl.this.disconnect();
					}
				}
			}
		});
	}

	/**
	 * �\�P�b�g�`���l�����擾���܂��B<br />
	 * 
	 * @return �\�P�b�g�`���l��
	 */
	public SocketChannel getChannel()
	{
		return this.socketChannel_;
	}

	/**
	 * �ڑ���Ԃ��擾���܂��B<br />
	 * 
	 * @return �ڑ�����Ă���Ȃ� <code>true</code> �A�����łȂ��Ȃ� <code>false</code>
	 */
	public boolean isConnected()
	{
		return this.isConnect_;
	}

	/**
	 * �N���[�Y�������s���܂��B<br />
	 * 
	 * @param forceDisconnected
	 *            �����ؒf���ꂽ�ꍇ�� <code>true</code>
	 */
	private void doClose(final boolean forceDisconnected)
	{
		if (this.telegramReader_ != null)
		{
			this.telegramReader_.setRunning(false);
		}

		if (this.readerThread_ != null)
		{
			// ���荞�݂𔭐������A�I������܂ő҂B
			// join() ���s��Ȃ��ƁA�X���b�h�����S�ɏI�����Ȃ��ԂɍĐڑ����������s���悤�Ƃ��A
			// �X���b�h�������Ă��邽�ߍĐڑ����s��Ȃ��悤�ɂȂ��Ă��܂��B
			this.readerThread_.interrupt();
			try
			{
				this.readerThread_.join();
			}
			catch (InterruptedException ex)
			{
				outputLog("WECC0204", ex);
			}
		}

		if (this.isConnect_ == false)
		{
			return;
		}

		// �g�p�����ʐM�Ώۂ��N���A����
		if (this.objPrintStream_ != null)
		{
			this.objPrintStream_.close();
			this.objPrintStream_ = null;
		}

		try
		{
			if (this.socketChannel_ != null)
			{
				this.socketChannel_.close();
				this.socketChannel_ = null;
			}

			outputLog("IECC0205", this.threadName_);
			this.isConnect_ = false;
			if (this.telegramReader_ != null)
			{
				notifyClientDisconnected(forceDisconnected);
			}
		}
		catch (IOException objIOException)
		{
			// �G���[���o��
			outputLog("WECC0202", objIOException);
		}
	}

	/**
	 * �ڑ��������s���܂��B<br />
	 * 
	 * @return �ڑ��ɐ��������ꍇ�� <code>true</code>
	 */
	private boolean doConnect()
	{
		if (this.isConnect_ == true)
		{
			return false;
		}

		try
		{
			// �T�[�o�ɐڑ�����
			SocketAddress remote = new InetSocketAddress(this.host_,
					this.portNumber_);
			this.socketChannel_ = SocketChannel.open(remote);
			this.ipAddress_ = getIpAddress();
			// �ڑ����̃��b�Z�[�W
			outputLog("IECC0206", remote, this.threadName_);

			this.isConnect_ = true;
		}
		catch (Exception ex)
		{
			// �G���[���b�Z�[�W���o��
			logConnectException(this.host_, this.portNumber_);
			outputLog("WECC0202", ex);
			return false;
		}

		// ���X�i�ɒʒm����
		String hostName = null;
		if (NetworkUtil.isIpAddress(this.host_) == false)
		{
			hostName = this.host_;
		}
		try
		{
			if (this.socketChannel_ == null)
			{
				return false;
			}
			this.objPrintStream_ = new PrintStream(this.socketChannel_.socket()
					.getOutputStream(), true);

			this.telegramReader_ = new TelegramReader(this, this.threadName_
					+ "-Sender", this.socketChannel_.socket(), this.discard_,
					this.isOutputLog_);
		}
		catch (IOException objIOException)
		{
			outputLog("WECC0202", objIOException);

			// �ؒf����B
			doClose(false);
			return false;
		}

		// �ڑ��ʒm�𑗐M
		ConnectNotifyData connectNotify = new ConnectNotifyData();
		if (this.connectNotify_ != null)
		{
			// ��ʂ��R�s�[
			connectNotify.setKind(this.connectNotify_.getKind());
			connectNotify.setPurpose(this.connectNotify_.getPurpose());

			// DB���̂𐶐�
			String agentName = this.connectNotify_.getAgentName();
			InetAddress localAddress = this.socketChannel_.socket()
					.getLocalAddress();
			String ipAddr = localAddress.getHostAddress();
			String localhostName = localAddress.getHostName();

			String realAgentName = createAgentName(agentName, localhostName,
					ipAddr);
			connectNotify.setAgentName(realAgentName);
			sendTelegram(ConnectNotifyAccessor.createTelegram(connectNotify));
		}

		notifyClientConnected(hostName, this.ipAddress_, this.portNumber_);

		return true;
	}

	/**
	 * �J�n��Ԃ�Ԃ��܂��B<br />
	 * 
	 * @return �J�n���
	 */
	public boolean isStart()
	{
		return this.started_;
	}

	/**
	 * {@link TelegramListener} �I�u�W�F�N�g��ǉ����܂��B<br />
	 * 
	 * @param listener
	 *            �]����I�u�W�F�N�g
	 */
	public void addTelegramListener(final TelegramListener listener)
	{

		if (this.telegramListeners_ == null)
		{
			this.telegramListeners_ = new ArrayList<TelegramListener>();
		}
		this.telegramListeners_.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCommunicatorListener(final CommunicatorListener listener)
	{
		synchronized (this.listenerList_)
		{
			this.listenerList_.add(listener);
		}
	}

	/**
	 * �ڑ���� IP �A�h���X��Ԃ��܂��B<br />
	 * 
	 * @return �ڑ���� IP �A�h���X
	 */
	public String getIpAddress()
	{
		Socket socket = this.socketChannel_.socket();
		String inetAddr = socket.getInetAddress().toString();
		int delimiterPos = inetAddr.indexOf('/');
		String ipAddress = inetAddr.substring(delimiterPos + 1);
		return ipAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(final String host, final int port)
	{
		this.host_ = host;
		this.portNumber_ = port;
	}

	/**
	 * �d�����f�o�b�O�o�͂��܂��B<br />
	 * 
	 * @param message
	 *            ���b�Z�[�W
	 * @param response
	 *            ��M�d��
	 * @param length
	 *            �d����
	 */
	public void logTelegram(final String message, final Telegram response,
			final int length)
	{
		/*
		 * String telegramStr = TelegramUtil.toPrintStr(response, length);
		 * LOGGER.warn(message +
		 * this.socketChannel_.socket().getInetAddress().getHostAddress() + ":"
		 * + this.socketChannel_.socket().getPort() + SystemLogger.NEW_LINE +
		 * telegramStr);
		 */
	}

	private void logConnectException(final String ip, final int port)
	{
		outputLog("IECC0203", ip, port, this.threadName_);
	}

	private ThreadPoolExecutor createThreadPoolExecutor()
	{
		ThreadFactory factory = new ThreadFactory() {
			public Thread newThread(final Runnable r)
			{
				String name = CommunicationClientImpl.this.threadName_
						+ "-Writer";
				Thread thread = new Thread(r, name);
				thread.setDaemon(true);
				return thread;
			}
		};
		return new ThreadPoolExecutor(1, 1, 0, TimeUnit.NANOSECONDS,
				new LinkedBlockingQueue<Runnable>(), factory,
				new ThreadPoolExecutor.DiscardPolicy());
	}

	/**
	 * �ؒf���ꂽ���Ƃ��e���X�i�֒ʒm���܂��B<br />
	 * 
	 * @param forceDisconnected
	 *            �����ؒf���ꂽ�ꍇ�� <code>true</code>
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
	 * @param hostName
	 *            �z�X�g���i <code>null</code> �̉\������j
	 * @param ipAddr
	 *            IP �A�h���X
	 * @param port
	 *            �|�[�g�ԍ�
	 */
	private void notifyClientConnected(final String hostName,
			final String ipAddr, final int port)
	{
		synchronized (this.listenerList_)
		{
			for (CommunicatorListener listener : this.listenerList_)
			{
				listener.clientConnected(hostName, ipAddr, port);
			}
		}
	}

	/**
	 * ���O���o�͂��܂��B<br />
	 * 
	 * @param messageCode
	 *            ���b�Z�[�W�R�[�h
	 * @param args
	 *            ����
	 */
	private void outputLog(final String messageCode, final Object... args)
	{
		if (isOutputLog_)
		{
			LOGGER.log(messageCode, args);
		}
	}

	/**
	 * Agent���̂𐶐�����B "%H"��������z�X�g���ɁA "%I"�������IP�A�h���X�ɒu���������ʂ�Ԃ��B
	 * 
	 * @param agentName
	 *            �ڑ����Ɋi�[����Ă���DB��
	 * @param hostName
	 *            �z�X�g��
	 * @param ipAddr
	 *            IP�A�h���X
	 * @return �ԊҌ��DB����
	 */
	private static String createAgentName(String agentName, String hostName,
			String ipAddr)
	{
		if (agentName == null)
		{
			return "unknown";
		}

		if (hostName == null)
		{
			hostName = "";
		}

		if (ipAddr == null)
		{
			ipAddr = "";
		}

		String realDbName = agentName;
		realDbName = realDbName.replaceAll("%H", hostName == null ? ipAddr
				: hostName);

		return realDbName;
	}

}
