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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.StreamUtil;
import jp.co.acroquest.endosnipe.data.db.DatabaseType;

/**
 * ENdoSnipe DataCollector �̐ݒ�t�@�C����ǂݍ��ނ��߂̃N���X�ł��B<br />
 * 
 * <p>
 * <b>�ݒ�t�@�C���̏���</b><br />
 * �ݒ�t�@�C���� Java �W���̃v���p�e�B�t�@�C���`���ł��B<br />
 * �e�L�[�́A�u<i>�L�[��</i>.<i>�G�[�W�F���gID</i>�v�̌`���ŁA
 * �����̃G�[�W�F���g�ւ̐ڑ��p�����[�^���L�q�ł��܂��B<br />
 * �����ŁA�G�[�W�F���gID�̓s���I�h���܂܂Ȃ��C�ӂ̕�����Ƃ��܂��B<br />
 * </p>
 * 
 * <p>
 * <b>�L�[�ꗗ</b><br />
 * <dl>
 *   <dt>agent.host</dt>
 *   <dd>�G�[�W�F���g�̐ڑ���z�X�g��</dd>
 *   <dt>agent.port</dt>
 *   <dd>�G�[�W�F���g�̐ڑ���|�[�g�ԍ�
 *   (���l�ȊO�̕����񂪎w�肳�ꂽ�ꍇ�A18000 �Ƃ��ĉ��߂��܂�)</dd>
 *   <dt>database.name</dt>
 *   <dd>�G�[�W�F���g�̃f�[�^�x�[�X��</dd>
 *   <dt>database.dir</dt>
 *   <dd>���|�[�g���o�͂���f�B���N�g��</dd>
 *   <dt>report.output.dir</dt>
 *   <dd>�G�[�W�F���g�̃f�[�^�x�[�X�f�B���N�g��</dd>
 *   <dt>resource.get.interval</dt>
 *   <dd>�G�[�W�F���g�̃��\�[�X�擾�Ԋu
 *   (���l�ȊO�̕����񂪎w�肳�ꂽ�ꍇ�A5000 �Ƃ��ĉ��߂��܂�)</dd>
 *   <dt>javelin.log.storage.period</dt>
 *   <dd>�G�[�W�F���g��Javelin���O�̍ő�~�ϊ���</dd>
 *   <dt>measurement.log.storage.period</dt>
 *   <dd>�G�[�W�F���g�̌v���f�[�^�̍ő�~�ϊ���</dd>
 *   <dt>javelin.javelin.log.split</dt>
 *   <dd>�G�[�W�F���g��Javelin���O�𕪊��ۑ����邩�ǂ���</dd>
 *   <dt>javelin.log.split.size</dt>
 *   <dd>�G�[�W�F���g��1���R�[�h�ӂ�̍ő�T�C�Y
 *   (javelin.javelin.log.split��true�̎��̂ݗL��)<br />
 *   (���l�ȊO�̕����񂪎w�肳�ꂽ�ꍇ�A300(KBytes) �Ƃ��ĉ��߂��܂�)<br />
 *   <dd>�G�[�W�F���g��Javelin���O�𕪊��ۑ�����ꍇ��臒l
 *   (javelin.javelin.log.split��true�̎��̂ݗL��)<br />
 *   (���l�ȊO�̕����񂪎w�肳�ꂽ�ꍇ�A1024(KBytes) �Ƃ��ĉ��߂��܂�)<br />
 *   </dd>
 *   <dt>collector.smtp.sendMail</dt>
 *   <dd>���[���ʒm�𑗐M���邩�ǂ���</dd>
 *   <dt>collector.smtp.server</dt>
 *   <dd>���[���T�[�o</dd>
 *   <dt>collector.smtp.encoding</dt>
 *   <dd>���[���̃G���R�[�f�B���O</dd>
 *   <dt>collector.smtp.from</dt>
 *   <dd>���M�����[���A�h���X</dd>
 *   <dt>collector.smtp.to</dt>
 *   <dd>���M�惁�[���A�h���X</dd>
 *   <dt>collector.smtp.subject</dt>
 *   <dd>���[��Subject</dd>
 *   <dt>collector.smtp.template.jvn</dt>
 *   <dd>���[���e���v���[�g(jvn�A���[���p)</dd>
 *   <dt>collector.smtp.template.measurement</dt>
 *   <dd>���[���e���v���[�g(�v���l�A���[���p)</dd>
 *   <dt>collector.snmp.sendTrap</dt>
 *   <dd>SNMPTrap�𑗐M���邩�ǂ���</dd>
 *   <dt>collector.snmp.managers</dt>
 *   <dd>�}�l�[�W�����X�g</dd>
 *   <dt>collector.snmp.trapPort</dt>
 *   <dd>SNMP Trap�|�[�g�ԍ�</dd>
 *   <dt>collector.snmp.version</dt>
 *   <dd>SNMP Version</dd>
 *   <dt>collector.snmp.trapCommunity</dt>
 *   <dd>Trap�R�~���j�e�B��</dd>
 * </dl>
 * </p>
 * 
 * <p>
 * <b>�L�q��</b><br />
 * <code>
 * agent.host.1=localhost<br />
 * agent.port.1=18000<br />
 * <br />
 * agent.host.2=192.168.1.1<br />
 * agent.port.2=18001<br />
 * </code>
 * </p>
 * 
 * @author y-komori
 */
public class ConfigurationReader
{
    private static final ENdoSnipeLogger logger_ =
                                                   ENdoSnipeLogger.getLogger(ConfigurationReader.class);

    /** DataCollectorConfig */
    private static DataCollectorConfig config__ = null;

    /** ���s�����B */
    private static final String LS = System.getProperty("line.separator");

    /** �z�X�g����\���ړ��� */
    private static final String SERVER_HOST = "server.host.";

    /** �|�[�g�ԍ���\���ړ��� */
    private static final String SERVER_PORT = "server.port.";

    /** �z�X�g����\���ړ��� */
    private static final String AGENT_HOST = "javelin.host.";

    /** �|�[�g�ԍ���\���ړ��� */
    private static final String AGENT_PORT = "javelin.port.";

    /** �z�X�g��\���ړ��� */
    private static final String ACCEPT_HOST = "datacollector.accepthost.";

    /** �|�[�g�ԍ���\���ړ��� */
    private static final String ACCEPT_PORT = "datacollector.acceptport.";

    /** �f�[�^�x�[�X����\���ړ��� */
    private static final String DATABASE_NAME = "database.name";

    /** �f�[�^�x�[�X����\���ړ���(�G�[�W�F���g��) */
    private static final String DATABASE_NAME_PREFIX = "database.name.";

    /** �f�[�^�x�[�X�̎�ނ�\���ړ��� */
    private static final String DATABASE_TYPE = "database.type";

    /** �f�[�^�x�[�X�f�B���N�g����\���ړ��� */
    private static final String DATABASE_DIR = "database.dir";

    /** �f�[�^�x�[�X�̃z�X�g�A�h���X��\���ړ��� */
    private static final String DATABASE_HOST = "database.host";

    /** �f�[�^�x�[�X�̃|�[�g�ԍ���\���ړ��� */
    private static final String DATABASE_PORT = "database.port";

    /** �f�[�^�x�[�X�̃��O�C�����[�U����\���ړ��� */
    private static final String DATABASE_USERNAME = "database.username";

    /** �f�[�^�x�[�X�̃��O�C���p�X���[�h��\���ړ��� */
    private static final String DATABASE_PASSWORD = "database.password";

    /** ���\�[�X�擾�Ԋu��\���ړ��� */
    private static final String RESOURCE_INTERVAL = "resource.get.interval";

    /** ���\�[�X���j�^�����O�̐ݒ�t�@�C������\���ړ��� */
    private static final String RESOURCE_MONITORING = "resource.config.filename";

    /** ���|�[�g�o�͐�f�B���N�g�� */
    private static final String REPORT_OUTPUT_DIR = "report.output.dir";

    /** �ڑ����[�h */
    private static final String CONNECTION_MODE = "connection.mode";

    /** �҂��󂯃z�X�g */
    private static final String SERVER_ACCEPT_HOST = "accept.host";

    /** �҂��󂯃|�[�g */
    private static final String SERVER_ACCEPT_PORT = "accept.port";

    /** Javelin���O�̍ő�~�ϊ��� (���ʐݒ�) */
    private static final String COMMON_JVN_LOG_STORAGE_PERIOD = "javelin.log.storage.period";

    /** �v���f�[�^�̍ő�~�ϊ��Ԃ�\���ړ��� (���ʐݒ�) */
    private static final String COMMON_MEASUREMENT_LOG_STORAGE_PERIOD =
                                                                        "measurement.log.storage.period";

    /** Javelin���O�̍ő�~�ϊ��Ԃ�\���ړ��� */
    private static final String JVN_LOG_STRAGE_PERIOD = "javelin.log.storage.period.";

    /** �v���f�[�^�̍ő�~�ϊ��Ԃ�\���ړ��� */
    private static final String MEASUREMENT_LOG_STRAGE_PERIOD = "measurement.log.storage.period.";

    /** Javelin���O�𕪊��ۑ����邩�ǂ�����\���ړ��� */
    private static final String LOG_SPLIT = "javelin.log.split";

    /** Javelin���O�𕪊��ۑ�����ꍇ��1���R�[�h�ӂ�̍ő�T�C�Y��\���ړ��� */
    private static final String LOG_SPLIT_SIZE = "javelin.log.split.size";

    /** Javelin���O�𕪊��ۑ�����ꍇ��臒l��\���ړ��� */
    private static final String LOG_SPLIT_THRESHOLD = "javelin.log.split.threshold";

    /** �f�[�^�x�[�X���Ŏg�p�ł��镶�����A���K�\���ŕ\�������� */
    private static final String DATABASE_NAME_USABLE_PATTERN = "[A-Za-z0-9#$%@=\\+\\-_~\\.]*";

    /** �p�����[�^��`�t�@�C���̃p�X */
    private static String configFilePath_;

    /** jvn���O�̃��[�e�[�g���Ԃ̍ő�l�i���j */
    private static final int LOG_ROTATE_MAX_PERIOD_MONTH = 24;

    /** jvn���O�̃��[�e�[�g���Ԃ̍ő�l�i���j */
    private static final int LOG_ROTATE_MAX_PERIOD_DAY = 365;

    /** jvn���O�̃��[�e�[�g���Ԃ̍ő�l�i���ԁj */
    private static final int LOG_ROTATE_MAX_PERIOD_HOUR = LOG_ROTATE_MAX_PERIOD_DAY * 24;

    //--------------------
    // SMTP settings
    //--------------------
    /** SMTP���[���ʒm�@�\�֘A�ݒ荀�ڂ̐ړ����B */
    private static final String SMTP_PREFIX = "collector.smtp.";

    /** ���[���ʒm�𑗐M���邩�ǂ������w�肷��ݒ荀�ږ��B */
    private static final String SEND_MAIL = SMTP_PREFIX + "sendMail";

    /** ���[���T�[�o���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_SERVER = SMTP_PREFIX + "server";

    /** ���[���̃G���R�[�f�B���O���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_ENCODING = SMTP_PREFIX + "encoding";

    /** ���M�����[���A�h���X���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_FROM = SMTP_PREFIX + "from";

    /** ���M�惁�[���A�h���X���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_TO = SMTP_PREFIX + "to";

    /** ���[����Subject���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_SUBJECT = SMTP_PREFIX + "subject";

    /** ���[���e���v���[�g���w�肷��ݒ荀�ڂ̐ړ����B */
    public static final String SMTP_TEMPLATE_PREFIX = SMTP_PREFIX + "template.";

    /** ���[���̌����e���v���[�g���w�肷��ݒ荀�ڂ̐ڔ����B */
    private static final String SMTP_TEMPLATE_SUBJECT_SUFFIX = ".subject";

    /** ���[���̖{���e���v���[�g���w�肷��ݒ荀�ڂ̐ڔ����B */
    private static final String SMTP_TEMPLATE_BODY_SUFFIX = ".body";

    /** ���[���e���v���[�g(jvn�A���[���p)���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_TEMPLATE_JVN = SMTP_TEMPLATE_PREFIX + "jvn";

    /** ���[���e���v���[�g(�v���l�A���[���p)���w�肷��ݒ荀�ږ��B */
    private static final String SMTP_TEMPLATE_MEASUREMENT = SMTP_TEMPLATE_PREFIX + "measurement";

    public static final String SMTP_TEMPLATE_COLLECT_COMPLETED = SMTP_TEMPLATE_PREFIX
            + "collectCompleted";

    //--------------------
    // SNMP settings
    //--------------------
    /** SNMP�n�p�����[�^�̐ړ��� */
    public static final String SNMP_PREFIX = "collector.snmp.";

    /** SNMPTrap�𑗐M���邩�ǂ����̐ݒ荀�ږ� */
    public static final String SEND_TRAP = SNMP_PREFIX + "sendTrap";

    /** �}�l�[�W�����X�g�̐ݒ荀�ږ� */
    public static final String MANAGERS = SNMP_PREFIX + "managers";

    /** SNMP Trap�|�[�g�ԍ��̐ݒ荀�ږ� */
    public static final String TRAP_PORT = SNMP_PREFIX + "trapPort";

    /** SNMP Version�̐ݒ荀�ږ� */
    public static final String VERSION = SNMP_PREFIX + "version";

    /** Trap�R�~���j�e�B���̐ݒ荀�ږ� */
    public static final String TRAP_COMMUNITY = SNMP_PREFIX + "trapCommunity";

    /** �g�p����̐ݒ荀�� */
    public static final String LANGUAGE = "language";

    public static final String BATCH_SIZE = "insert.batch.size";

    public static final String ITEMID_CACHE_SIZE = "itemid.cache.size";

    /** �G�[�W�F���g��`�̐ݒ��\��prefix */
    private static final Set<String> AGENT_PREFIXES = new CopyOnWriteArraySet<String>();

    static
    {
        AGENT_PREFIXES.add(AGENT_HOST);
        AGENT_PREFIXES.add(AGENT_PORT);
        AGENT_PREFIXES.add(ACCEPT_HOST);
        AGENT_PREFIXES.add(ACCEPT_PORT);
        AGENT_PREFIXES.add(JVN_LOG_STRAGE_PERIOD);
        AGENT_PREFIXES.add(MEASUREMENT_LOG_STRAGE_PERIOD);
        AGENT_PREFIXES.add(DATABASE_NAME_PREFIX);

    }

    /**
     * �X�g���[������ݒ�t�@�C����ǂݍ��݁A{@link DataCollectorConfig} ���\�z���܂��B<br />
     * 
     * @param inputStream ���̓X�g���[��
     * @return {@link DataCollectorConfig} �I�u�W�F�N�g
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws InitializeException ��������O�����������ꍇ
     */
    public static DataCollectorConfig load(final InputStream inputStream)
        throws IOException,
            InitializeException
    {

        Properties props = new Properties();
        try
        {
            props.load(inputStream);
            StreamUtil.closeStream(inputStream);
        }
        catch (IOException ex)
        {
            StreamUtil.closeStream(inputStream);
            throw ex;
        }

        Map<Integer, AgentSetting> settings = new TreeMap<Integer, AgentSetting>();

        DataCollectorConfig config = new DataCollectorConfig();

        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            setCommonValue(config, key, props.getProperty(key));
            int agentId = getAgentId(key);
            if (agentId > 0)
            {
                AgentSetting setting = settings.get(agentId);
                if (setting == null)
                {
                    setting = new AgentSetting();
                    settings.put(agentId, setting);
                    setting.agentId = agentId;
                }

                setValue(setting, key, props.getProperty(key));
            }
        }

        int agentSequece = 1;
        // 
        for (AgentSetting setting : settings.values())
        {
            if (setting.hostName != null && setting.hostName != "")
            {
                if (agentSequece == setting.agentId)
                {
                    config.addAgentSetting(setting);
                    agentSequece++;
                }
                else
                {
                    break;
                }
            }
        }
        return config;
    }

    /**
     * �w�肳�ꂽ�p�X����ݒ�t�@�C����ǂݍ��݁A{@link DataCollectorConfig} ���\�z���܂��B<br />
     * 
     * @param path �p�X
     * @return {@link DataCollectorConfig} �I�u�W�F�N�g
     * @throws IOException ���o�̓G���[�����������ꍇ
     * @throws InitializeException ��������O�����������ꍇ
     */
    public static DataCollectorConfig load(final String path)
        throws IOException,
            InitializeException
    {
        if (config__ != null)
        {
            return config__;
        }

        File file = new File(path);
        configFilePath_ = file.getCanonicalPath();
        FileInputStream is = new FileInputStream(file);
        try
        {
            config__ = load(is);
        }
        finally
        {
            StreamUtil.closeStream(is);
        }
        return config__;
    }

    /**
     * �ݒ�t�@�C���ɋL�q���ꂽ�l��ݒ肵�܂��B
     *
     * @param setting �e�G�[�W�F���g�̐ݒ�
     * @param key �L�[
     * @param value �l
     * @throws InitializeException �p�����[�^��������O�����������ꍇ
     */
    private static void setValue(final AgentSetting setting, final String key, final String value)
        throws InitializeException
    {
        if (key.startsWith(AGENT_HOST))
        {
            setting.hostName = value;
        }
        else if (key.startsWith(AGENT_PORT))
        {
            try
            {
                setting.port = Integer.parseInt(value);
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            AgentSetting.DEF_PORT);
            }
        }
        else if (key.startsWith(ACCEPT_PORT))
        {
            try
            {
                setting.acceptPort = Integer.parseInt(value);
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            AgentSetting.DEF_ACCEPT_PORT);
            }
        }
        else if (key.startsWith(DATABASE_NAME))
        {
            // database.name.n = xxx
            if (isValidDBName(value))
            {
                setting.databaseName = value;
            }
            else
            {
                logger_.log(LogMessageCodes.FAIL_TO_READ_PARAMETER, configFilePath_, key);
                throw new InitializeException("Invalid Unit.");
            }
        }
        else if (key.startsWith(JVN_LOG_STRAGE_PERIOD))
        {
            try
            {
                setting.jvnLogStragePeriod = getStragePeriod(value);
            }
            catch (InitializeException ex)
            {
                String defaultValue =
                                      AgentSetting.DEF_PERIOD
                                              + AgentSetting.DEF_PERIOD_UNIT.toString();
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            defaultValue);
            }
        }
        else if (key.startsWith(MEASUREMENT_LOG_STRAGE_PERIOD))
        {
            try
            {
                setting.measureStragePeriod = getStragePeriod(value);
            }
            catch (InitializeException ex)
            {
                String defaultValue =
                                      AgentSetting.DEF_PERIOD
                                              + AgentSetting.DEF_PERIOD_UNIT.toString();
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            defaultValue);
            }
        }
    }

    /**
     * �~�ϊ��ԂɈُ�l���Ȃ����`�F�b�N���s���܂��B<br />
     * �l������ł���΁A���͂��ꂽ����������̂܂ܕԂ��܂��B<br />
     * 
     * @param value ���͒l
     * @return ���͒l
     * @throws InitializeException �p�����[�^�̏������Ɏ��s�����ꍇ
     */
    private static String getStragePeriod(final String value)
        throws InitializeException
    {
        if (value == null || value.length() < 2)
        {
            throw new InitializeException("Invalid Unit.");
        }

        if (AgentSetting.NONE.equals(value))
        {
            return value;
        }
        String num = value.substring(0, value.length() - 1);
        String unit = value.substring(value.length() - 1);
        int intNum;
        try
        {
            intNum = Integer.parseInt(num);
        }
        catch (NumberFormatException ex)
        {
            throw new InitializeException(ex);
        }

        if ("h".equals(unit))
        {
            if (intNum < 1 || LOG_ROTATE_MAX_PERIOD_HOUR < intNum)
            {
                throw new InitializeException("Invalid Unit.");
            }
        }
        else if ("d".equals(unit))
        {
            if (intNum < 1 || LOG_ROTATE_MAX_PERIOD_DAY < intNum)
            {
                throw new InitializeException("Invalid Unit.");
            }
        }
        else if ("m".equals(unit))
        {
            if (intNum < 1 || LOG_ROTATE_MAX_PERIOD_MONTH < intNum)
            {
                throw new InitializeException("Invalid Unit.");
            }
        }
        else
        {
            throw new InitializeException("Invalid Unit.");
        }
        return value;
    }

    /**
     * ���ʃp�����[�^��ݒ肵�܂��B<br />
     * 
     * @param config {@link DataCollectorConfig}�I�u�W�F�N�g
     * @param key �L�[
     * @param value �l
     * @throws InitializeException �p�����[�^�̏������Ɏ��s�����ꍇ
     */
    private static void setCommonValue(final DataCollectorConfig config, final String key,
            final String value)
        throws InitializeException
    {
        if (DATABASE_TYPE.equals(key))
        {
            DatabaseType databaseType = DatabaseType.fromId(value);
            if (databaseType == null)
            {
                databaseType = DatabaseType.H2;
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            databaseType.toString());
            }
            config.setDatabaseType(databaseType);
        }
        else if (DATABASE_DIR.equals(key))
        {
            config.setBaseDir(value);
        }
        else if (DATABASE_HOST.equals(key))
        {
            config.setDatabaseHost(value);
        }
        else if (DATABASE_PORT.equals(key))
        {
            config.setDatabasePort(value);
        }
        else if (key.equals(DATABASE_NAME))
        {
            // database.name = xxx
            if (isValidDBName(value))
            {
                config.setDatabaseName(value);
            }
            else
            {
                logger_.log(LogMessageCodes.FAIL_TO_READ_PARAMETER, configFilePath_, key);
                throw new InitializeException("Invalid Unit.");
            }
        }
        else if (DATABASE_USERNAME.equals(key))
        {
            config.setDatabaseUserName(value);
        }
        else if (DATABASE_PASSWORD.equals(key))
        {
            config.setDatabasePassword(value);
        }
        else if (REPORT_OUTPUT_DIR.equals(key))
        {
            config.setReportOutputPath(value);
        }
        else if (RESOURCE_INTERVAL.equals(key))
        {
            try
            {
                config.setResourceInterval(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_RESOURCE_INTERVAL);
            }
        }
        else if (RESOURCE_MONITORING.equals(key))
        {
            config.setResourceMonitoringConf(value);
        }
        else if (CONNECTION_MODE.equals(key))
        {
            config.setConnectionMode(value);
        }
        else if (SERVER_ACCEPT_HOST.equals(key))
        {
            config.setAcceptHost(value);
        }
        else if (SERVER_ACCEPT_PORT.equals(key))
        {
            try
            {
                config.setAcceptPort(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_TO_READ_PARAMETER, configFilePath_, key);
                throw new InitializeException(ex);
            }
        }
        else if (COMMON_JVN_LOG_STORAGE_PERIOD.equals(key))
        {
            config.setJvnLogStoragePeriod(value);
        }
        else if (COMMON_MEASUREMENT_LOG_STORAGE_PERIOD.equals(key))
        {
            config.setMeasurementLogStoragePeriod(value);
        }
        else if (LOG_SPLIT.equals(key))
        {
            config.setLogSplit(Boolean.parseBoolean(value));
        }
        else if (LOG_SPLIT_SIZE.equals(key))
        {
            try
            {
                config.setLogSplitSize(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_LOG_SPLIT_SIZE);
            }
        }
        else if (LOG_SPLIT_THRESHOLD.equals(key))
        {
            try
            {
                config.setLogSplitThreshold(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_LOG_SPLIT_THRESHOLD);
            }
        }
        // SMTP settings
        else if (SEND_MAIL.equals(key))
        {
            if (value.equalsIgnoreCase("true"))
            {
                config.setSendMail(true);
            }
            else if (value.equalsIgnoreCase("false"))
            {
                config.setSendMail(false);
            }
            else
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_SEND_MAIL);
            }
        }
        else if (SMTP_SERVER.equals(key))
        {
            config.setSmtpServer(value);
        }
        else if (SMTP_FROM.equals(key))
        {
            config.setSmtpFrom(value);
        }
        else if (SMTP_TO.equals(key))
        {
            config.setSmtpTo(value);
        }
        else if (SMTP_TEMPLATE_JVN.equals(key))
        {
            config.setSmtpTemplateJvn(value);
        }
        else if (SMTP_TEMPLATE_MEASUREMENT.equals(key))
        {
            config.setSmtpTemplateMeasurement(value);
        }
        else if (SMTP_ENCODING.equals(key))
        {
            config.setSmtpEncoding(value);
        }
        else if (SMTP_SUBJECT.equals(key))
        {
            config.setSmtpSubject(value);
        }
        else if (key.startsWith(SMTP_TEMPLATE_PREFIX))
        {
            // ���[���e���v���[�g�ݒ�
            if (key.endsWith(SMTP_TEMPLATE_SUBJECT_SUFFIX))
            {
                // ����
                String name =
                              key.substring(0, key.length() - SMTP_TEMPLATE_SUBJECT_SUFFIX.length());
                MailTemplateEntity entity = getMailTemplateEntity(config, name);
                entity.subject = value;
            }
            else if (key.endsWith(SMTP_TEMPLATE_BODY_SUFFIX))
            {
                // �{��
                String name = key.substring(0, key.length() - SMTP_TEMPLATE_BODY_SUFFIX.length());
                MailTemplateEntity entity = getMailTemplateEntity(config, name);
                try
                {
                    entity.body = readTemplate(value);
                }
                catch (IOException ex)
                {
                    logger_.log(LogMessageCodes.FAIL_READ_MAIL_TEMPLATE, value);
                }
            }
        }
        // SNMP settings
        else if (SEND_TRAP.equals(key))
        {
            if (value.equalsIgnoreCase("true"))
            {
                config.setSendTrap(true);
            }
            else if (value.equalsIgnoreCase("false"))
            {
                config.setSendTrap(true);
            }
            else
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_SEND_TRAP);
            }
        }
        else if (MANAGERS.equals(key))
        {
            config.setManagers(value);
        }
        else if (TRAP_PORT.equals(key))
        {
            try
            {
                config.setTrapPort(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_TRAP_PORT);
            }
        }
        else if (TRAP_COMMUNITY.equals(key))
        {
            config.setTrapCommunity(value);
        }
        else if (VERSION.equals(key))
        {
            config.setVersion(value);
        }
        else if (LANGUAGE.equals(key))
        {
            config.setLanguage(value);
        }
        else if (BATCH_SIZE.equals(key))
        {
            try
            {
                config.setBatchSize(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_BATCH_SIZE);
            }

        }
        else if (ITEMID_CACHE_SIZE.equals(key))
        {
            try
            {
                config.setItemIdCacheSize(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                logger_.log(LogMessageCodes.FAIL_READ_PARAMETER_USE_DEFAULT, configFilePath_, key,
                            DataCollectorConfig.DEF_CACHE_SIZE);
            }

        }

    }

    /**
     * �w�肳�ꂽ���[���e���v���[�g���ڂ̐ݒ�I�u�W�F�N�g���擾���܂��B<br />
     * �I�u�W�F�N�g�����݂��Ȃ��ꍇ�͐������܂��B
     *
     * @param config {@link DataCollectorConfig} �I�u�W�F�N�g
     * @param name ���[���e���v���[�g���ږ��i SUFFIX �������������j
     * @return �ݒ�I�u�W�F�N�g
     */
    private static MailTemplateEntity getMailTemplateEntity(final DataCollectorConfig config,
            final String name)
    {
        MailTemplateEntity entity = config.getSmtpTemplate(name);
        if (entity == null)
        {
            entity = new MailTemplateEntity();
            config.setSmtpTemplate(name, entity);
        }
        return entity;
    }

    /**
     * �e�G�[�W�F���g��ID���擾���܂��B<br />
     * 
     * @param key �L�[
     * @return�@�G�[�W�F���g��ID
     * @throws InitializeException �p�����[�^�̏������Ɏ��s�����ꍇ
     */
    private static int getAgentId(final String key)
        throws InitializeException
    {
        int pos = key.lastIndexOf(".");
        if (pos < 0)
        {
            return -1;
        }
        // �G�[�W�F���g���Ƃɐݒ肷��L�[�ȊO�̏ꍇ�́A-1��Ԃ��A�������I������B
        String keyPrefix = key.substring(0, pos + 1);
        if (!AGENT_PREFIXES.contains(keyPrefix))
        {
            return -1;
        }

        String hostNumStr = key.substring(pos + 1);
        int hostNum = -1;
        try
        {
            hostNum = Integer.parseInt(hostNumStr);
        }
        catch (NumberFormatException ex)
        {
            logger_.log(LogMessageCodes.FAIL_GET_AGENT_ID, key);
        }
        return hostNum;
    }

    /**
     * �ݒ�t�@�C���̐�΃p�X��Ԃ��܂��B<br />
     * 
     * @return �ݒ�t�@�C���̐�΃p�X
     */
    public static String getAbsoluteFilePath()
    {
        return configFilePath_;
    }

    /**
     * �����Ŏw�肳�ꂽ�f�[�^�x�[�X�����L���ł��邩�ǂ������肵�܂��B<br />
     * �L�������ꗗ<br />
     * <li>���p�A���t�@�x�b�g</li>
     * <li>���p����</li>
     * <li>�u#�v�A�u$�v�A�u%�v�A�u@�v�A�u=�v�A�u+�v�A�u-�v�i�n�C�t���j�A�u_�v�i�A���_�[�X�R�A�j�A�u~�v</li>
     * 
     * @param databaseName �f�[�^�x�[�X��
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    private static boolean isValidDBName(final String databaseName)
    {
        Pattern pattern = Pattern.compile(DATABASE_NAME_USABLE_PATTERN);
        Matcher matcher = pattern.matcher(databaseName);
        return matcher.matches();
    }

    /**
     * �t�@�C����S�ēǂݍ��݁A�P���String�I�u�W�F�N�g�Ƃ��ĕԂ��B �e���s��String�I�u�W�F�N�g���ɑ}�������B
     * 
     * @param filePath �ǂݍ��ރe���v���[�g�̃p�X�B
     * @return �ǂݍ��񂾕�����B
     * @throws IOException �t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ�B
     */
    private static String readTemplate(final String filePath)
        throws IOException
    {
        StringBuilder template = new StringBuilder();
        FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader);

        try
        {
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                {
                    break;
                }

                template.append(line);
                template.append(LS);
            }
        }
        finally
        {
            br.close();
        }

        return template.toString();
    }
}
