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
package jp.co.acroquest.endosnipe.collector;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
import jp.co.acroquest.endosnipe.collector.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.config.DisplayNameManager;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.PathUtil;
import jp.co.acroquest.endosnipe.data.db.ConnectionManager;

/**
 * DataCollector �T�[�r�X�p�̃��C���N���X�ł��B<br />
 * <ul>
 * <li>Windows�� commons-daemon (procrun) �̏ꍇ
 *   <dl>
 *     <dt>�J�n��<dd>main() ���\�b�h�������ustart�v�ŌĂяo����܂��B<br />
 *     <dt>�I����<dd>main() ���\�b�h�������ustop�v�ŌĂяo����܂��B<br />
 *   </dl>
 * </li>
 * <li>Linux�� commons-daemon (jsvc) �̏ꍇ
 *   <dl>
 *     <dt>�J�n��<dd>init()�Astart() �̏��ɌĂяo����܂��B<br />
 *     <dt>�I����<dd>stop()�Adestroy() �̏��ɌĂяo����܂��B<br />
 *   </dl>
 * </li>
 * </ul>
 * 
 * @author y-komori
 */
public class Bootstrap implements LogMessageCodes
{
    private static Bootstrap main__ = null;

    /** ���K�[ */
    public static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(Bootstrap.class);

    // �ݒ�t�@�C�����w�肷�邽�߂̃v���p�e�B��
    private static final String COLLECTOR_PROP_NAME = "collector.property";

    // �f�t�H���g�̐ݒ�t�@�C��
    private static final String DEF_COLLECTOR_PROPERTY = "../conf/collector.properties";

    // �ُ�I�����̃X�e�[�^�X
    private static final int STATUS_ERROR = 1;

    // �J�n��������\���t���O
    private volatile boolean starting_;

    // �I����������\���t���O
    private volatile boolean stopping_;

    private ENdoSnipeDataCollector collector_;

    /**
     * �G���g���|�C���g���\�b�h�ł��B<br />
     * 
     * @param args �R�}���h���C������
     */
    public static void main(final String[] args)
    {
        if (args.length < 1)
        {
            printUsage();
            System.exit(STATUS_ERROR);
        }
        if (main__ == null)
        {
            main__ = new Bootstrap();
        }

        String cmd = args[0];
        if (cmd.equals("start"))
        {
            try
            {
                main__.start();
            }
            catch (InitializeException ex)
            {
                LOGGER.log(ERROR_OCCURED_ON_STARTING, ex);
                System.exit(STATUS_ERROR);
            }
            catch (Exception ex)
            {
                LOGGER.log(ERROR_OCCURED_ON_STARTING, ex);
                System.exit(STATUS_ERROR);
            }
        }
        else if (cmd.equals("stop"))
        {
            main__.stop();
        }
    }

    /**
     * �T�[�r�X���J�n���܂��B<br />
     * @throws InitializeException �T�[�r�X�̏������Ɏ��s
     */
    public void start()
        throws InitializeException
    {
        if (starting_ == true)
        {
            // ���ɊJ�n�������̏ꍇ�͉����s��Ȃ�
            throw new InitializeException(DATA_COLLECTOR_ALREADY_STARTING);
        }
        this.starting_ = true;

        initContextClassLoader();

        LOGGER.log(DATA_COLLECTOR_SERVICE_STARTING);
        // �V���b�g�_�E���t�b�N�̓o�^
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        DataCollectorConfig config = loadConfig();

        try
        {
            this.collector_ = new ENdoSnipeDataCollector();
            this.collector_.init(config);
            this.collector_.startService();

            if (config.getConnectionMode().equals(DataCollectorConfig.MODE_SERVER))
            {
                this.collector_.startServer();
            }
            else
            {
                this.collector_.connectAll();
            }

            LOGGER.log(DATA_COLLECTOR_SERVICE_STARTED);
            this.starting_ = false;

            // �I������܂ŃX���b�h���u���b�N����
            this.collector_.blockTillStop();
        }
        catch (Throwable ex)
        {
            LOGGER.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
            this.starting_ = false;

            stop();
            throw new InitializeException(ex);
        }
    }

    /**
     * �T�[�r�X�̒�~���s���܂��B<br />
     */
    public void stop()
    {
        if (this.collector_ != null)
        {
            if (stopping_ == true)
            {
                // ���ɏI���������̏ꍇ�͉����s��Ȃ�
                return;
            }
            this.stopping_ = true;

            LOGGER.log(DATA_COLLECTOR_SERVICE_STOPPING);

            this.collector_.stop();
            ConnectionManager.getInstance().closeAll();
            this.collector_ = null;

            LOGGER.log(DATA_COLLECTOR_SERVICE_STOPPED);
            this.stopping_ = false;
        }
    }

    /**
     * Linux �� Commons-daemon ����Ăяo����鏉�������\�b�h�ł��B<br />
     * �폜���Ȃ��ł��������B<br />
     * @param args ����
     */
    public void init(final String[] args)
    {
        // Do nothing.
    }

    /**
     * Linux �� Commons-daemon ����Ăяo�����j�����\�b�h�ł��B<br />
     * �폜���Ȃ��ł��������B<br />
     */
    public void destroy()
    {
        // Do nothing.
    }

    private DataCollectorConfig loadConfig()
        throws InitializeException
    {
        String fileName = System.getProperty(COLLECTOR_PROP_NAME);
        if (fileName == null)
        {
            fileName = DEF_COLLECTOR_PROPERTY;
        }

        // �ݒ�t�@�C�������΃p�X�w��̏ꍇ�A��΃p�X�ɕϊ�����
        if (PathUtil.isRelativePath(fileName))
        {
            String jarPath = PathUtil.getJarDir(Bootstrap.class);
            fileName = jarPath + fileName;
        }

        DataCollectorConfig config = null;
        try
        {
            config = ConfigurationReader.load(fileName);
        }
        catch (IOException ex)
        {
            throw new InitializeException(CANNOT_FIND_PROPERTY,
                                          ConfigurationReader.getAbsoluteFilePath());
        }
        List<AgentSetting> agentList = config.getAgentSettingList();
        if (agentList == null || agentList.size() == 0)
        {
            throw new InitializeException(CANNOT_FIND_HOST,
                                          ConfigurationReader.getAbsoluteFilePath());
        }

        // �f�[�^�x�[�X��f�B���N�g�������΃p�X�w��̏ꍇ�A
        // Jar �����݂���f�B���N�g������̑��΃p�X�ƌ��Ȃ���
        // ��΃p�X�ɕϊ�����
        String baseDir = config.getBaseDir();
        if (PathUtil.isRelativePath(baseDir) == true)
        {
            String jarPath = PathUtil.getJarDir(Bootstrap.class);
            config.setBaseDir(jarPath + baseDir);
        }

        // ���\�[�X���j�^�����O�̐ݒ�t�@�C���������΃p�X�w��̏ꍇ�A
        // Jar �����݂���f�B���N�g������̑��΃p�X�ƌ��Ȃ���
        // ��΃p�X�ɕϊ�����
        String resourceMonitoringConf = config.getResourceMonitoringConf();
        if (PathUtil.isRelativePath(resourceMonitoringConf) == true)
        {
            String jarPath = PathUtil.getJarDir(Bootstrap.class);
            config.setResourceMonitoringConf(jarPath + resourceMonitoringConf);

        }

        // ����ʂ�DisplayName�ݒ�t�@�C����ǂݍ���
        DisplayNameManager.getManager().init(config.getLanguage());

        return config;
    }

    private void initContextClassLoader()
    {
        // CommonsDaemon ����N�������ꍇ�A�X���b�h�ɃR���e�N�X�g�N���X���[�_
        // ���ݒ肳��Ă��Ȃ����߁A�����I�Ɏw�肷��
        ClassLoader loader = getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
    }

    private static void printUsage()
    {
        System.err.println("Usage: java -D" + COLLECTOR_PROP_NAME
                + "=PROPFILENAME -jar endosnipe-datacollector.jar {start|stop}");
    }

    /**
     * �V���b�g�_�E���t�b�N�N���X�ł��B<br />
     * 
     * @author fujii
     */
    private static class ShutdownHook extends Thread
    {
        @Override
        public void run()
        {
            if (main__ != null)
            {
                main__.stop();
            }
        }
    }
}
