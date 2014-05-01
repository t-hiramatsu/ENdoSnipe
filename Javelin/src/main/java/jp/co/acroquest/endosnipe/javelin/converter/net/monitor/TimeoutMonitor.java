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
package jp.co.acroquest.endosnipe.javelin.converter.net.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.NoTimeoutDetectedEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �^�C���A�E�g�ݒ肪�s���Ă��邩���肵�܂��B<br />
 * 
 * @author fujii
 *
 */
public class TimeoutMonitor extends InputStream
{
    /** �^�C���A�E�g�l���擾����\�P�b�g�I�u�W�F�N�g */
    private final SocketImpl socket_;

    /** �������Ϗ�����N���X */
    private final InputStream delegated_;

    /** ���\�b�h�̎��s��\���t���O */
    private boolean inProcess_;

    /** ���łɌx���������������ǂ�����\���t���O */
    private boolean isAlarmed_ = false;

    /** ���\�b�h����������[���̍ő吔 */
    private static final int MAX_DEPTH = 3;

    /** Javelin�̐ݒ�l */
    private static JavelinConfig config__ = new JavelinConfig();

    /** getTimeout���\�b�h */
    private Method getTimeoutMethod_;

    /** �|�[�g�ԍ��B */
    private int port_;

    /** �A�h���X�B */
    private InetAddress address_;

    /** ���[�J���A�h���X�̃��X�g�B */
    private Set<String> localAddressSet_;

    /**
     * �R���X�g���N�^�ł��B<br />
     * 
     * @param socket �\�P�b�g�I�u�W�F�N�g
     * @param delegated {@link InputStream}�I�u�W�F�N�g
     */
    public TimeoutMonitor(final SocketImpl socket, final InputStream delegated)
    {
        this.socket_ = socket;
        this.delegated_ = delegated;

        Class<?> cls = this.socket_.getClass();
        try
        {
            this.getTimeoutMethod_ = getAccesibleMethod(cls, "getTimeout");
            Method getPortMethod = getAccesibleMethod(cls, "getPort");
            Method getInetAddressMethod = getAccesibleMethod(cls, "getInetAddress");

            this.port_ = (Integer)getPortMethod.invoke(this.socket_);
            this.address_ = (InetAddress)getInetAddressMethod.invoke(this.socket_);

        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read()
        throws IOException
    {
        if (isMonitor())
        {
            detect();
        }

        return this.delegated_.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final byte[] b, final int off, final int len)
        throws IOException
    {
        if (isMonitor())
        {
            detect();
        }

        return this.delegated_.read(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final byte[] b)
        throws IOException
    {
        if (isMonitor())
        {
            detect();
        }

        return this.delegated_.read(b);
    }

    /**
     * �^�C���A�E�g�ݒ肪�s���Ă��邩���肵�A<br />
     * �ݒ肪�s���Ă��Ȃ��ꍇ�AJavelin���O�ɃG���[���b�Z�[�W���o�͂��܂��B<br />
     * 
     * @throws SocketException �g�p���Ă���v���g�R���ŃG���[�����������ꍇ
     */
    private void detect()
        throws SocketException
    {
        this.inProcess_ = true;

        int timeout = 0;
        try
        {
            Object ret = this.getTimeoutMethod_.invoke(this.socket_);
            timeout = (Integer)ret;
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        if (timeout <= 0)
        {
            CommonEvent detectecdEvent = createNoTimeOutDetectedEvent(timeout);
            StatsJavelinRecorder.addEvent(detectecdEvent);
            this.isAlarmed_ = true;
        }
        this.inProcess_ = false;
    }

    private Method getAccesibleMethod(Class<?> defaultClass, String methodName)
    {
        Class<?> cls = defaultClass;
        Method method = null;
        for (int num = 0; num < MAX_DEPTH; num++)
        {
            try
            {
                method = cls.getDeclaredMethod(methodName);
            }
            catch (NoSuchMethodException ex)
            {
                cls = cls.getSuperclass();
            }
            if (method != null)
            {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    /**
     * �^�C���A�E�g���ݒ茟�o�C�x���g���쐬���܂��B
     * @param timeout �w�肳��Ă���^�C���A�E�g����
     * @return {@link CommonEvent}�I�u�W�F�N�g
     */
    private CommonEvent createNoTimeOutDetectedEvent(int timeout)
    {
        CommonEvent event = new NoTimeoutDetectedEvent();

        String identifier = StatsUtil.createIdentifier(this.socket_);

        String socketHostAddress = this.address_.getHostAddress();
        boolean isLocalAddress = isLocalAddress(socketHostAddress);

        if (isLocalAddress == false)
        {
            event.setLevel(CommonEvent.LEVEL_ERROR);
        }

        event.addParam(EventConstants.NOTIMEOUT_IDENTIFIER, identifier);

        event.addParam(EventConstants.NOTIMEOUT_ADDRESS, socketHostAddress);
        event.addParam(EventConstants.NOTIMEOUT_PORT, String.valueOf(this.port_));
        event.addParam(EventConstants.NOTIMEOUT_TIMEOUT, String.valueOf(timeout));

        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
        String stackTrace = ThreadUtil.getStackTrace(stacktraces, config__.getTraceDepth());
        event.addParam(EventConstants.NOTIMEOUT_STACKTRACE, stackTrace);

        return event;
    }

    /**
     * ���[�J���z�X�g���ǂ����𔻒肷��B
     * 
     * @param inetAddress ����Ώۂ̃A�h���X�B
     * @return ���[�J���z�X�g���ǂ����B
     */
    private boolean isLocalAddress(String inetAddress)
    {
        boolean result = false;

        Set<String> localAddressSet = getLocalAddressSet();
        if (localAddressSet.contains(inetAddress))
        {
            result = true;
        }

        return result;
    }

    /**
     * ���[�J���̃A�h���X�̃Z�b�g���擾����B
     * 
     * @return ���[�J���A�h���X�̃Z�b�g�B
     */
    private Set<String> getLocalAddressSet()
    {
        if (this.localAddressSet_ == null)
        {
            Set<String> localAddressSet = initLocalAddressSet();
            this.localAddressSet_ = Collections.unmodifiableSet(localAddressSet);
        }
        
        return this.localAddressSet_;
    }

    private Set<String> initLocalAddressSet()
    {
        Set<String> localAddressSet = new HashSet<String>();

        Enumeration<NetworkInterface> interfaces = null;
        try
        {
            interfaces = NetworkInterface.getNetworkInterfaces();
        }
        catch (SocketException se)
        {
            SystemLogger.getInstance().warn(se);
        }

        if (interfaces == null)
        {
            return localAddressSet;
        }

        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                InetAddress address = addresses.nextElement();
                localAddressSet.add(address.getHostAddress());
            }
        }
        return localAddressSet;
    }

    /**
     * �^�C���A�E�g�ݒ茟�o���s���K�v�����邩�A���肵�܂��B<br />
     * 
     * @return true:���o������s���Afalse:���o������s��Ȃ�
     */
    private boolean isMonitor()
    {
        boolean isMonitor = config__.isTimeoutMonitor();
        return this.inProcess_ == false && isAlarmed_ == false && isMonitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available()
        throws IOException
    {
        return this.delegated_.available();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
        throws IOException
    {
        this.delegated_.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void mark(final int readlimit)
    {
        this.delegated_.mark(readlimit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported()
    {
        return this.delegated_.markSupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset()
        throws IOException
    {
        this.delegated_.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(final long n)
        throws IOException
    {
        return this.delegated_.skip(n);
    }
}
