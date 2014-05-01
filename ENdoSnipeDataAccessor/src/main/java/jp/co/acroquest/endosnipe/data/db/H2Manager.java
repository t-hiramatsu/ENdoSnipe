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
package jp.co.acroquest.endosnipe.data.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.Server;

/**
 * H2 �f�[�^�x�[�X���Ǘ����邽�߂̃N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class H2Manager
{
    private static final int TCP_PORT = 9092;

    private static H2Manager instance__;

    private String baseDir_;

    private int tcpPort_ = TCP_PORT;

    private Server tcpServer_;

    private Server webServer_;

    private H2Manager()
    {

    }

    /**
     * {@link H2Manager} �̃C���X�^���X���擾���܂��B<br />
     * 
     * @return {@link H2Manager} �̃C���X�^���X

     */
    public static H2Manager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new H2Manager();
        }
        return instance__;
    }

    /**
     * �f�[�^�x�[�X���J�n���܂��B<br />
     * 
     * @throws SQLException �f�[�^�x�[�X�̊J�n�Ɏ��s�����ꍇ
     */
    public void start()
        throws SQLException
    {
        if (tcpServer_ != null)
        {
            if (tcpServer_.isRunning(false) == true)
            {
                return;
            }
            else
            {
                tcpServer_ = null;
            }
        }

        List<String> argList = new ArrayList<String>();
        argList.add("-tcpAllowOthers");

        if (baseDir_ != null)
        {
            argList.add("-baseDir");
            argList.add(baseDir_);
        }

        argList.add("-tcpPort");
        argList.add(Integer.toString(tcpPort_));

        String[] arg = new String[argList.size()];
        argList.toArray(arg);
        tcpServer_ = Server.createTcpServer(arg);
        tcpServer_.start();

        webServer_ = Server.createWebServer(arg);
        webServer_.start();

    }

    /**
     * �f�[�^�x�[�X���~���܂��B<br />
     */
    public void stop()
    {
        // CHECKSTYLE:OFF
        if (stopServer(tcpServer_) == true)
        {
        }
        if (stopServer(webServer_) == true)
        {
        }
        // CHECKSTYLE:ON
    }

    /**
     * �f�[�^�x�[�X�̃x�[�X�f�B���N�g����Ԃ��܂��B<br />
     * 
     * @return �f�[�^�x�[�X�̃x�[�X�f�B���N�g��
     */
    public String getBaseDir()
    {
        return baseDir_;
    }

    /**
     * �f�[�^�x�[�X�̃x�[�X�f�B���N�g����ݒ肵�܂��B<br />
     * 
     * @param baseDir �f�[�^�x�[�X�̃x�[�X�f�B���N�g��
     */
    public void setBaseDir(final String baseDir)
    {
        baseDir_ = baseDir;
    }

    /**
     * TCP �҂��󂯃|�[�g�ԍ���Ԃ��܂��B<br />
     * 
     * @return TCP �҂��󂯃|�[�g�ԍ�

     */
    public int getTcpPort()
    {
        return tcpPort_;
    }

    /**
     * TCP �҂��󂯃|�[�g�ԍ���ݒ肵�܂��B<br />
     * 
     * �ݒ肵�Ȃ��ꍇ�̃f�t�H���g�l�� 9092 �ƂȂ�܂��B
     * 
     * @param tcpPort TCP �҂��󂯃|�[�g�ԍ�

     */
    public void setTcpPort(final int tcpPort)
    {
        tcpPort_ = tcpPort;
    }

    /**
     * �f�[�^�x�[�X�T�[�o���~���܂��B<br />
     *
     * @param server �T�[�o
     * @return ��~�����ꍇ�� <code>true</code> �A���łɒ�~���Ă���ꍇ�� <code>false</code>
     */
    protected boolean stopServer(final Server server)
    {
        if (server != null && server.isRunning(false) == true)
        {
            server.stop();
            return true;
        }
        else
        {
            return false;
        }
    }
}
