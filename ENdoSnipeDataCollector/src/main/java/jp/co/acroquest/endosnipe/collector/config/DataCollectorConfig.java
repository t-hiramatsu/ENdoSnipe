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
package jp.co.acroquest.endosnipe.collector.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.data.db.DatabaseType;

/**
 * ENdoSnipe DataCollector �̐ݒ�/�萔��ێ�����N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class DataCollectorConfig
{
    /** Javelin���O�𕪊����邩�ǂ��� */
    private boolean isLogSplit_ = DEF_IS_LOG_SPLIT;

    /** Javelin���O�𕪊��ۑ�����ꍇ�̂P���R�[�h������̍ő�T�C�Y */
    private int logSplitSize_ = DEF_LOG_SPLIT_SIZE;

    /** Javelin���O�𕪊�����ꍇ��臒l */
    private int logSplitThreshold_ = DEF_LOG_SPLIT_THRESHOLD;

    /** Javelin���O�ő�~�ό����̃L�[ */
    public static final String JAVELIN_LOG_MAX_KEY = "javelin.log.max.record";

    /** Javelin���O�ő�~�ό����̃f�t�H���g�l */
    public static final int DEF_JAVELIN_LOG_MAX = 260000;

    /** Javelin���O�ő�~�ό����̍ŏ��l */
    public static final int MIN_JAVELIN_LOG_MAX = 1;

    /** �v���f�[�^�ő�~�ό����̃L�[ */
    public static final String MEASUREMENT_LOG_MAX_KEY = "measurement.log.max.record";

    /** �v���f�[�^�ő�~�ό����̃f�t�H���g�l */
    public static final int DEF_MEASUREMENT_LOG_MAX = 870000;

    /** �v���f�[�^�ő�~�ό����̍ŏ��l */
    public static final int MIN_MEASUREMENT_LOG_MAX = 1;

    /** �f�[�^�x�[�X�̎�� */
    private DatabaseType databaseType_ = DEF_DATABASE_TYPE;

    /** �f�[�^�x�[�X�f�B���N�g�� */
    private String baseDir_ = DEF_DATABASE_DIR;

    /** �f�[�^�x�[�X�̃z�X�g�A�h���X */
    private String databaseHost_ = DEF_DATABASE_HOST;

    /** �f�[�^�x�[�X�̃|�[�g�ԍ� */
    private String databasePort_ = DEF_DATABASE_PORT;

    /** �f�[�^�x�[�X�� */
    private String databaseName_ = DEF_DATABASE_NAME;

    /** �f�[�^�x�[�X���O�C�����[�U�� */
    private String databaseUserName_ = DEF_DATABASE_USER;

    /** �f�[�^�x�[�X���O�C���p�X���[�h */
    private String databasePassword_ = DEF_DATABASE_PASSWORD;

    /** ���\�[�X���j�^�����O�̐ݒ�t�@�C���� */
    private String resourceMonitoringConf_ = DEF_RESOURCE_MONITORING_CONF;

    /** ���|�[�g���o�͂���f�B���N�g���B */
    private String reportOutputPath_ = DEF_REPORT_OUTPUT_PATH;

    /** �ڑ����[�h */
    private String connectionMode_ = DEF_MODE;

    /** �҂��󂯃z�X�g */
    private String acceptHost_ = DEF_ACCEPT_HOST;

    /** �҂��󂯃|�[�g */
    private int acceptPort_ = DEF_ACCEOT_PORT;

    /** Javelin���O�̍ő�~�ϊ��� */
    private String jvnLogStoragePeriod_ = DEF_MEASUREMENT_LOG_STORAGE_PERIOD;

    /** �v���f�[�^�̍ő�~�ϊ��� */
    private String measurementLogStoragePeriod_ = DEF_MEASUREMENT_LOG_STORAGE_PERIOD;

    /** Agent���̐ݒ��ێ����郊�X�g */
    private final List<AgentSetting> agentSttingList_ = new ArrayList<AgentSetting>();

    /** �f�[�^�x�[�X�̎�ނ̃f�t�H���g�l */
    private static final DatabaseType DEF_DATABASE_TYPE = DatabaseType.H2;

    /** �f�[�^�x�[�X�ۑ���x�[�X�f�B���N�g���̃f�t�H���g�l */
    private static final String DEF_DATABASE_DIR = "../data";

    /** �f�[�^�x�[�X�̃z�X�g�A�h���X�̃f�t�H���g�l */
    private static final String DEF_DATABASE_HOST = "localhost";

    /** �f�[�^�x�[�X�̃|�[�g�ԍ��̃f�t�H���g�l */
    private static final String DEF_DATABASE_PORT = "5432";

    /** �f�[�^�x�[�X���̃f�t�H���g�l */
    private static final String DEF_DATABASE_NAME = "endosnipedb";

    /** �f�[�^�x�[�X���O�C�����[�U���̃f�t�H���g�l */
    private static final String DEF_DATABASE_USER = "";

    /** �f�[�^�x�[�X���O�C���p�X���[�h�̃f�t�H���g�l */
    private static final String DEF_DATABASE_PASSWORD = "";

    /** Javelin���O�����ۑ����s�����ǂ����̃f�t�H���g�l */
    private static final boolean DEF_IS_LOG_SPLIT = false;

    /** Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h�ӂ�̍ő�T�C�Y�̃f�t�H���g�l */
    public static final int DEF_LOG_SPLIT_SIZE = 300;

    /** Javelin���O�𕪊��ۑ�����ꍇ��臒l�̃f�t�H���g�l */
    public static final int DEF_LOG_SPLIT_THRESHOLD = 1024;

    /** �T�[�o�̃��\�[�X�Ԋu�i���ʁj */
    private long resourceInterval_ = DEF_RESOURCE_INTERVAL;

    /** ���\�[�X�擾�Ԋu�̃f�t�H���g�l(�~���b) */
    public static final int DEF_RESOURCE_INTERVAL = 5000;

    /** ���\�[�X���j�^�����O�̐ݒ�t�@�C�����̃f�t�H���g�l */
    private static final String DEF_RESOURCE_MONITORING_CONF = "../conf/resource_monitoring.conf";

    /** ���|�[�g�o�͐�f�B���N�g���̃f�t�H���g�l */
    private static final String DEF_REPORT_OUTPUT_PATH = null;

    /** �ڑ����� Client */
    public static final String MODE_CLIENT = "client";

    /** �ڑ����� Server */
    public static final String MODE_SERVER = "server";

    /** �ڑ������̃f�t�H���l */
    private static final String DEF_MODE = MODE_CLIENT;

    /** �T�[�o�z�X�g���̃f�t�H���g�l */
    private static final String DEF_ACCEPT_HOST = "localhost";

    /** �҂��󂯃|�[�g�̃f�t�H���g�l */
    private static final int DEF_ACCEOT_PORT = 19000;

    /** �v���f�[�^�̍ő�~�ϊ��Ԃ̃f�t�H���g�l */
    private static final String DEF_MEASUREMENT_LOG_STORAGE_PERIOD = "7d";

    //--------------------
    // SMTP settings(default)
    //--------------------
    /** ���[���ʒm�𑗐M���邩�ǂ����̃f�t�H���g�l�B */
    public static final boolean DEF_SEND_MAIL = false;

    /** ���[���T�[�o�̃f�t�H���g�l�B */
    private static final String DEF_SMTP_SERVER = "mail.example.com";

    /** ���[���̃G���R�[�f�B���O�ݒ�f�t�H���g�l�B */
    private static final String DEF_SMTP_ENCODING = "iso-2022-jp";

    /** ���M�����[���A�h���X�ݒ�f�t�H���g�l�B */
    private static final String DEF_SMTP_FROM = "endosnipe@example.com";

    /** ���M�惁�[���A�h���X�ݒ�f�t�H���g�l�B */
    private static final String DEF_SMTP_TO = "endosnipe@example.com";

    /** ���[��Subject�̃f�t�H���g�l�B */
    private static final String DEF_SMTP_SUBJECT = "[javelin] ${eventName} is occurred.";

    /** ���[���e���v���[�g(jvn�A���[���p)�̃f�t�H���g�l�B */
    private static final String DEF_SMTP_TEMPLATE_JVN = "mai_template_jvn.txt";

    /** ���[���e���v���[�g(�v���l�A���[���p)�̃f�t�H���g�l�B */
    private static final String DEF_SMTP_TEMPLATE_MEASUREMENT = "mai_template_measurement.txt";

    //--------------------
    // SMTP settings
    //--------------------
    /** ���[���ʒm�𑗐M���邩�ǂ��� */
    private boolean sendMail_ = DEF_SEND_MAIL;

    /** ���[���T�[�o */
    private String smtpServer_ = DEF_SMTP_SERVER;

    /** ���[���̃G���R�[�f�B���O */
    private String smtpEncoding_ = DEF_SMTP_ENCODING;

    /** ���M�����[���A�h���X */
    private String smtpFrom_ = DEF_SMTP_FROM;

    /** ���M�惁�[���A�h���X */
    private String smtpTo_ = DEF_SMTP_TO;

    /** ���[��Subject */
    private String smtpSubject_ = DEF_SMTP_SUBJECT;

    /** ���[���e���v���[�g(jvn�A���[���p) */
    private String smtpTemplateJvn_ = DEF_SMTP_TEMPLATE_JVN;

    /** ���[���e���v���[�g(�v���l�A���[���p) */
    private String smtpTemplateMeasurement_ = DEF_SMTP_TEMPLATE_MEASUREMENT;

    /** ���[���e���v���[�g�i�L�[�F�ݒ荀�ږ��A�l�F�e���v���[�g�t�@�C�����j */
    private final Map<String, MailTemplateEntity> smtpTemplateMap_ =
                     new HashMap<String, MailTemplateEntity>();

    //--------------------
    // SNMP settings(default)
    //--------------------
    /** SNMPTrap�𑗐M���邩�ǂ����̃f�t�H���g�ltrue(=���M����) */
    public static final boolean DEF_SEND_TRAP = false;

    /** �}�l�[�W�����X�g�̃f�t�H���g�llocalhost */
    private static final String DEF_MANAGERS = "localhost";

    /** SNMP Trap�|�[�g�ԍ��̃f�t�H���g�l162 */
    public static final int DEF_TRAP_PORT = 162;

    /** Trap�R�~���j�e�B���̃f�t�H���g�lpublic */
    private static final String DEF_TRAP_COMMUNITY = "public";

    /** SNMP Version: v1 */
    public static final String VERSION_V1 = "v1";

    /** SNMP Version: v2c */
    public static final String VERSION_V2C = "v2c";

    /** SNMP Version: v3 */
    public static final String VERSION_V3 = "v3";

    /** SNMP Version�̃f�t�H���g�lv2c */
    private static final String DEF_VERSION = VERSION_V2C;

    //--------------------
    // SNMP settings
    //--------------------
    /** SNMPTrap�𑗐M���邩�ǂ����Btrue:���M����Afalse:���M���Ȃ� */
    private boolean sendTrap_ = DEF_SEND_TRAP;

    /** �}�l�[�W�����X�g */
    private String managers_ = DEF_MANAGERS;

    /** SNMP Trap�|�[�g�ԍ� */
    private int trapPort_ = DEF_TRAP_PORT;

    /** SNMP Version */
    private String version_ = DEF_VERSION;

    /** Trap�R�~���j�e�B�� */
    private String trapCommunity_ = DEF_TRAP_COMMUNITY;

    /** �o�b�`�T�C�Y */
    private int batchSize_ = DEF_BATCH_SIZE;

    /** �o�b�`�X�V */
    public static final int DEF_BATCH_SIZE = 100;

    /** itemId�L���b�V�� */
    private int itemIdCacheSize_ = DEF_CACHE_SIZE;

    /** itemId�L���b�V�� */
    public static final int DEF_CACHE_SIZE = 50000;

    //--------------------
    // Language settings
    //--------------------
    /** �f�t�H���g�̌��� */
    private static final String DEF_LANGUAGE = "";

    /** �g�p���� */
    private String language_ = DEF_LANGUAGE;

    /**
     * �f�[�^�x�[�X�̎�ނ�Ԃ��܂��B<br />
     *
     * @return �f�[�^�x�[�X�̎��
     */
    public DatabaseType getDatabaseType()
    {
        return this.databaseType_;
    }

    /**
     * �f�[�^�x�[�X�̎�ނ�ݒ肵�܂��B<br />
     *
     * @param type �f�[�^�x�[�X�̎��
     */
    public void setDatabaseType(final DatabaseType type)
    {
        this.databaseType_ = type;
    }

    /**
     * �f�[�^�x�[�X�f�B���N�g����Ԃ��܂��B<br />
     * 
     * @return �f�[�^�x�[�X�f�B���N�g��
     */
    public String getBaseDir()
    {
        return baseDir_;
    }

    /**
     * �f�[�^�x�[�X�f�B���N�g����ݒ肵�܂��B<br />
     * 
     * @param baseDir �f�[�^�x�[�X�f�B���N�g��
     */
    public void setBaseDir(final String baseDir)
    {
        baseDir_ = baseDir;
    }

    /**
     * �f�[�^�x�[�X�̃z�X�g�A�h���X��Ԃ��܂��B<br />
     *
     * @return �z�X�g�A�h���X�܂��̓z�X�g��
     */
    public String getDatabaseHost()
    {
        return this.databaseHost_;
    }

    /**
     * �f�[�^�x�[�X�̃z�X�g�A�h���X��ݒ肵�܂��B<br />
     *
     * @param host �z�X�g�A�h���X�i�z�X�g���ł��j
     */
    public void setDatabaseHost(final String host)
    {
        this.databaseHost_ = host;
    }

    /**
     * �f�[�^�x�[�X�̃|�[�g�ԍ���Ԃ��܂��B<br />
     *
     * @return �|�[�g�ԍ�
     */
    public String getDatabasePort()
    {
        return this.databasePort_;
    }

    /**
     * �f�[�^�x�[�X�̃|�[�g�ԍ���ݒ肵�܂��B<br />
     *
     * @param port �|�[�g�ԍ�
     */
    public void setDatabasePort(final String port)
    {
        this.databasePort_ = port;
    }

    /**
     * �f�[�^�x�[�X�����擾���܂��B<br />
     * 
     * @return DataCollector���������݂Ɏg�p����f�[�^�x�[�X���B
     */
    public String getDatabaseName()
    {
        return databaseName_;
    }

    /**
     * �f�[�^�x�[�X����ݒ肵�܂��B<br />
     * 
     * @param dbname DataCollector���������݂Ɏg�p����f�[�^�x�[�X���B
     */
    public void setDatabaseName(final String dbname)
    {
        this.databaseName_ = dbname;
    }

    /**
     * �f�[�^�x�[�X���O�C�����[�U����Ԃ��܂��B<br />
     *
     * @return ���[�U��
     */
    public String getDatabaseUserName()
    {
        return this.databaseUserName_;
    }

    /**
     * �f�[�^�x�[�X���O�C�����[�U����ݒ肵�܂��B<br />
     *
     * @param user ���[�U��
     */
    public void setDatabaseUserName(final String user)
    {
        this.databaseUserName_ = user;
    }

    /**
     * �f�[�^�x�[�X���O�C���p�X���[�h��Ԃ��܂��B<br />
     *
     * @return �p�X���[�h
     */
    public String getDatabasePassword()
    {
        return this.databasePassword_;
    }

    /**
     * �f�[�^�x�[�X���O�C���p�X���[�h��ݒ肵�܂��B<br />
     *
     * @param password �p�X���[�h
     */
    public void setDatabasePassword(final String password)
    {
        this.databasePassword_ = password;
    }

    /**
     * ���\�[�X�擾�Ԋu��ݒ肵�܂��B<br />
     *
     * @param interval ���\�[�X�擾�Ԋu
     */
    public void setResourceInterval(final long interval)
    {
        this.resourceInterval_ = interval;
    }

    /**
     * ���\�[�X�擾�Ԋu��Ԃ��܂��B<br />
     *
     * @return Interval ���\�[�X�擾�Ԋu
     */
    public long getResourceInterval()
    {
        return this.resourceInterval_;
    }

    /**
     * {@link AgentSetting} ��ǉ����܂��B<br />
     * 
     * @param agentSetting {@link AgentSetting} �I�u�W�F�N�g
     */
    public void addAgentSetting(final AgentSetting agentSetting)
    {
        agentSttingList_.add(agentSetting);
    }

    /**
     * {@link AgentSetting} �̃��X�g��Ԃ��܂��B<br />
     * 
     * @return {@link AgentSetting} �̃��X�g
     */
    public List<AgentSetting> getAgentSettingList()
    {
        return Collections.unmodifiableList(agentSttingList_);
    }

    /**
     * Javelin���O�𕪊����邩�ǂ����B<br />
     * 
     * @return Javelin���O�𕪊�����ꍇ�A<code>true</code>
     */
    public boolean isLogSplit()
    {
        return this.isLogSplit_;
    }

    /**
     * Javelin���O�𕪊����邩�ǂ�����ݒ肵�܂��B<br />
     * 
     * @param isLogSplit Javelin���O�𕪊�����ꍇ�A<code>true</code>
     */
    public void setLogSplit(final boolean isLogSplit)
    {
        this.isLogSplit_ = isLogSplit;
    }

    /**
     * Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h������̍ő�T�C�Y��Ԃ��܂��B<br />
     * 
     * @return Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h������̍ő�T�C�Y
     */
    public int getLogSplitSize()
    {
        return this.logSplitSize_;
    }

    /**
     * Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h������̍ő�T�C�Y��ݒ肵�܂��B<br />
     * 
     * @param logSplitSize Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h������̍ő�T�C�Y
     */
    public void setLogSplitSize(final int logSplitSize)
    {
        this.logSplitSize_ = logSplitSize;
    }

    /**
     * Javelin���O�𕪊��ۑ�����ꍇ��臒l��Ԃ��܂��B<br />
     * 
     * @return Javelin���O�𕪊��ۑ�����ꍇ��臒l
     */
    public int getLogSplitThreshold()
    {
        return this.logSplitThreshold_;
    }

    /**
     * Javelin���O�𕪊��ۑ�����ꍇ��臒l��ݒ肵�܂��B<br />
     * 
     * @param logSplitThreshold Javelin���O�𕪊��ۑ�����ꍇ��臒l
     */
    public void setLogSplitThreshold(final int logSplitThreshold)
    {
        this.logSplitThreshold_ = logSplitThreshold;
    }

    /**
     * @param resourceMonitoringConf �Z�b�g���� resourceMonitoringConf
     */
    public void setResourceMonitoringConf(final String resourceMonitoringConf)
    {
        resourceMonitoringConf_ = resourceMonitoringConf;
    }

    /**
     * @return resourceMonitoringConf
     */
    public String getResourceMonitoringConf()
    {
        return resourceMonitoringConf_;
    }

    /**
     * ���|�[�g���o�͂���f�B���N�g�����擾����B
     * 
     * @return reportOutputPath ���|�[�g���o�͂���f�B���N�g���B
     */
    public String getReportOutputPath()
    {
        return reportOutputPath_;
    }

    /**
     * ���|�[�g���o�͂���f�B���N�g����ݒ肷��B
     * 
     * @param reportOutputPath ���|�[�g���o�͂���f�B���N�g���B
     */
    public void setReportOutputPath(final String reportOutputPath)
    {
        reportOutputPath_ = reportOutputPath;
    }

    /**
     * ���[���ʒm�𑗐M���邩�ǂ������擾����B
     * 
     * @return ���[���ʒm�𑗐M���邩�ǂ����̐ݒ�
     */
    public boolean isSendMail()
    {
        return sendMail_;
    }

    /**
     *�@���[���ʒm�𑗐M���邩�ǂ�����ݒ肷��B
     * 
     * @param sendMail ���[���ʒm�𑗐M���邩�ǂ����̐ݒ�
     */
    public void setSendMail(final boolean sendMail)
    {
        sendMail_ = sendMail;
    }

    /**
     * ���[���̃G���R�[�f�B���O���擾����B
     * 
     * @return ���[���̃G���R�[�f�B���O�̐ݒ�
     */
    public String getSmtpEncoding()
    {
        return smtpEncoding_;
    }

    /**
     *�@���[���̃G���R�[�f�B���O��ݒ肷��B
     * 
     * @param smtpEncoding ���[���̃G���R�[�f�B���O�̐ݒ�
     */
    public void setSmtpEncoding(final String smtpEncoding)
    {
        smtpEncoding_ = smtpEncoding;
    }

    /**
     * ���[���T�[�o���擾����B
     * 
     * @return ���[���T�[�o�̐ݒ�
     */
    public String getSmtpServer()
    {
        return smtpServer_;
    }

    /**
     *�@���[���T�[�o��ݒ肷��B
     * 
     * @param smtpServer ���[���T�[�o�̐ݒ�
     */
    public void setSmtpServer(final String smtpServer)
    {
        smtpServer_ = smtpServer;
    }

    /**
     * ���M�����[���A�h���X���擾����B
     * 
     * @return ���M�����[���A�h���X�̐ݒ�
     */
    public String getSmtpFrom()
    {
        return smtpFrom_;
    }

    /**
     *�@���M�����[���A�h���X��ݒ肷��B
     * 
     * @param smtpFrom ���M�����[���A�h���X�̐ݒ�
     */
    public void setSmtpFrom(final String smtpFrom)
    {
        smtpFrom_ = smtpFrom;
    }

    /**
     * ���M�惁�[���A�h���X���擾����B
     * 
     * @return ���M�惁�[���A�h���X�̐ݒ�
     */
    public String getSmtpTo()
    {
        return smtpTo_;
    }

    /**
     *�@���M�惁�[���A�h���X��ݒ肷��B
     * 
     * @param smtpTo ���M�惁�[���A�h���X�̐ݒ�
     */
    public void setSmtpTo(final String smtpTo)
    {
        smtpTo_ = smtpTo;
    }

    /**
     * ���[���e���v���[�g(jvn�A���[���p)���擾����B
     * 
     * @return ���[���e���v���[�g(jvn�A���[���p)�̐ݒ�
     */
    public String getSmtpTemplateJvn()
    {
        return smtpTemplateJvn_;
    }

    /**
     *�@���[���e���v���[�g(jvn�A���[���p)��ݒ肷��B
     * 
     * @param smtpTemplateJvn ���[���e���v���[�g(jvn�A���[���p)�̐ݒ�
     */
    public void setSmtpTemplateJvn(final String smtpTemplateJvn)
    {
        smtpTemplateJvn_ = smtpTemplateJvn;
    }

    /**
     * ���[���e���v���[�g(�v���l�A���[���p)���擾����B
     * 
     * @return ���[���e���v���[�g(�v���l�A���[���p)�̐ݒ�
     */
    public String getSmtpTemplateMeasurement()
    {
        return smtpTemplateMeasurement_;
    }

    /**
     *�@���[���e���v���[�g(�v���l�A���[���p)��ݒ肷��B
     * 
     * @param smtpTemplateMeasurement ���[���e���v���[�g(�v���l�A���[���p)�̐ݒ�
     */
    public void setSmtpTemplateMeasurement(final String smtpTemplateMeasurement)
    {
        smtpTemplateMeasurement_ = smtpTemplateMeasurement;
    }

    /**
     * ���[���e���v���[�g�t�@�C�������擾����B
     *
     * @param name �e���v���[�g��
     * @return �e���v���[�g�t�@�C����
     */
    public MailTemplateEntity getSmtpTemplate(final String name)
    {
        if (name == null)
        {
            return null;
        }
        return this.smtpTemplateMap_.get(name);
    }

    /**
     * ���[���e���v���[�g�t�@�C������ݒ肷��B
     *
     * @param name �e���v���[�g��
     * @param template �e���v���[�g�t�@�C����
     */
    public void setSmtpTemplate(final String name, final MailTemplateEntity template)
    {
        this.smtpTemplateMap_.put(name, template);
    }

    /**
     * ���[��Subject���擾����B
     * 
     * @return ���[��Subject�̐ݒ�
     */
    public String getSmtpSubject()
    {
        return smtpSubject_;
    }

    /**
     *�@���[��Subject��ݒ肷��B
     * 
     * @param smtpSubject ���[��Subject�̐ݒ�
     */
    public void setSmtpSubject(final String smtpSubject)
    {
        smtpSubject_ = smtpSubject;
    }

    /**
     * SNMPTrap�𑗐M���邩�ǂ������擾����B
     * 
     * @return SNMPTrap�𑗐M���邩�ǂ���
     */
    public boolean isSendTrap()
    {
        return sendTrap_;
    }

    /**
     *�@SNMPTrap�𑗐M���邩�ǂ�����ݒ肷��B
     * 
     * @param sendTrap SNMPTrap�𑗐M���邩�ǂ���
     */
    public void setSendTrap(final boolean sendTrap)
    {
        sendTrap_ = sendTrap;
    }

    /**
     * �}�l�[�W�����X�g���擾����B
     * 
     * @return �}�l�[�W�����X�g
     */
    public String getManagers()
    {
        return managers_;
    }

    /**
     *�@�}�l�[�W�����X�g��ݒ肷��B
     * 
     * @param managers �}�l�[�W�����X�g
     */
    public void setManagers(final String managers)
    {
        managers_ = managers;
    }

    /**
     * SNMP Trap�|�[�g�ԍ����擾����B
     * 
     * @return SNMP Trap�|�[�g�ԍ�
     */
    public int getTrapPort()
    {
        return trapPort_;
    }

    /**
     *�@SNMP Trap�|�[�g�ԍ���ݒ肷��B
     * 
     * @param trapPort SNMP Trap�|�[�g�ԍ�
     */
    public void setTrapPort(final int trapPort)
    {
        trapPort_ = trapPort;
    }

    /**
     * Trap�R�~���j�e�B�����擾����B
     * 
     * @return Trap�R�~���j�e�B��
     */
    public String getTrapCommunity()
    {
        return trapCommunity_;
    }

    /**
     *�@Trap�R�~���j�e�B����ݒ肷��B
     * 
     * @param trapCommunity Trap�R�~���j�e�B��
     */
    public void setTrapCommunity(final String trapCommunity)
    {
        trapCommunity_ = trapCommunity;
    }

    /**
     * SNMP Version���擾����B
     * 
     * @return SNMP Version
     */
    public String getVersion()
    {
        return version_;
    }

    /**
     *�@SNMP Version��ݒ肷��B
     * 
     * @param version SNMP Version
     */
    public void setVersion(final String version)
    {
        version_ = version;
    }

    /**
     * �ڑ����[�h���擾����B
     * @return connectionMode �ڑ����[�h
     */
    public String getConnectionMode()
    {
        return connectionMode_;
    }

    /**
     * �ڑ����[�h��ݒ肷��B
     * @param connectionMode �ڑ����[�h
     */
    public void setConnectionMode(final String connectionMode)
    {
        connectionMode_ = connectionMode;
    }

    /**
     * �҂��󂯃z�X�g���擾����B
     * @return acceptHost �҂��󂯃z�X�g
     */
    public String getAcceptHost()
    {
        return acceptHost_;
    }

    /**
     * �҂��󂯃z�X�g��ݒ肷��B
     * @param acceptHost �҂��󂯃z�X�g
     */
    public void setAcceptHost(final String acceptHost)
    {
        acceptHost_ = acceptHost;
    }

    /**
     * �҂��󂯃|�[�g���擾����B
     * @return acceptPort �҂��󂯃|�[�g
     */
    public int getAcceptPort()
    {
        return acceptPort_;
    }

    /**
     * �҂��󂯃|�[�g��ݒ肷��B
     * @param acceptPort �҂��󂯃|�[�g
     */
    public void setAcceptPort(final int acceptPort)
    {
        acceptPort_ = acceptPort;
    }

    /**
     * Javelin���O�̍ő�~�ϊ��Ԃ��擾����B
     * @return Javelin���O�̍ő�~�ϊ���
     */
    public String getJvnLogStoragePeriod()
    {
        return jvnLogStoragePeriod_;
    }

    /**
     * Javelin���O�̍ő�~�ϊ��Ԃ�ݒ肷��B
     * @param jvnLogStoragePeriod Javelin���O�̍ő�~�ϊ���
     */
    public void setJvnLogStoragePeriod(final String jvnLogStoragePeriod)
    {
        this.jvnLogStoragePeriod_ = jvnLogStoragePeriod;
    }

    /**
     * �v���f�[�^�̍ő�~�ϊ��Ԃ��擾����B
     * @return �v���f�[�^�̍ő�~�ϊ���
     */
    public String getMeasurementLogStoragePeriod()
    {
        return measurementLogStoragePeriod_;
    }

    /**
     * �v���f�[�^�̍ő�~�ϊ��Ԃ�ݒ肷��B
     * @param measurementLogStoragePeriod �v���f�[�^�̍ő�~�ϊ���
     */
    public void setMeasurementLogStoragePeriod(final String measurementLogStoragePeriod)
    {
        this.measurementLogStoragePeriod_ = measurementLogStoragePeriod;
    }

    /**
     * �g�p������擾����B
     * 
     * @return �g�p����
     */
    public String getLanguage()
    {
        return language_;
    }

    /**
     * �g�p�����ݒ肷��B
     * 
     * @param language �g�p����
     */
    public void setLanguage(final String language)
    {
        language_ = language;
    }

    /**
     * �o�b�`�T�C�Y���擾���܂��B
     * @return �o�b�`�T�C�Y
     */
    public int getBatchSize()
    {
        return this.batchSize_;
    }

    /**
     * �o�b�`�T�C�Y��ݒ肵�܂��B
     * @param batchSize �o�b�`�T�C�Y
     */
    public void setBatchSize(final int batchSize)
    {
        batchSize_ = batchSize;
    }

    /**
     * ����ID�̃L���b�V���T�C�Y���擾���܂��B
     * @return �L���b�V���T�C�Y
     */
    public int getItemIdCacheSize()
    {
        return itemIdCacheSize_;
    }

    /**
     * ����ID�̃L���b�V���T�C�Y��ݒ肵�܂��B
     * @param itemIdCacheSize ����ID�̃L���b�V���T�C�Y
     */
    public void setItemIdCacheSize(final int itemIdCacheSize)
    {
        itemIdCacheSize_ = itemIdCacheSize;
    }
}
