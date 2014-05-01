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
package jp.co.acroquest.endosnipe.common.config;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

/**
 * Javelin�̐ݒ��ێ����邽�߂̃N���X�ł��B<br />
 * 
 * @author eriguchi
 */
public class JavelinConfigBase
{
    /** �X���b�h���f���̒l�F�X���b�hID */
    public static final int TM_THREAD_ID = 1;

    /** �X���b�h���f���̒l�F�X���b�h�� */
    public static final int TM_THREAD_NAME = 2;

    /** �X���b�h���f���̒l�F */
    public static final int TM_CONTEXT_PATH = 3;

    /** Javelin�n�p�����[�^�̐ړ��� */
    public static final String JAVELIN_PREFIX = "javelin.";

    /**
     * ConcurrentAccessMonitor��CollectionMonitor���쒆�ɃN���X���[�h���N�����Ƃ��ɁA
     * �o�C�g�R�[�h�ϊ����s�����ǂ�����ݒ肷��v���p�e�B�i�o�C�g�R�[�h�ϊ����s��Ȃ��ꍇ�� <code>true</code> �j */
    public static final String SKIPCLASS_ONPROCESSING_KEY = JAVELIN_PREFIX
            + "skipClassOnProcessing";

    /** ���\�b�h���ώ��Ԃ��o�͂��邽�߂ɋL�^����Invocation���̃v���p�e�B */
    public static final String INTERVALMAX_KEY = JAVELIN_PREFIX + "intervalMax";

    /** ��O�̐����L�^���邽�߂�Invocation���̃v���p�e�B */
    public static final String THROWABLEMAX_KEY = JAVELIN_PREFIX + "throwableMax";

    /** Javelin�̏��擾�Ώۂ��珜�O����A�ő�Bytecode���̃v���p�e�B */
    public static final String BYTECODE_EXCLUDE_LENGTH_KEY = JAVELIN_PREFIX
            + "bytecode.exclude.length";

    /** Javelin�̏��擾�Ώۂ��珜�O����A�ő�̐��䖽�ߐ��̃v���p�e�B */
    public static final String BYTECODE_EXCLUDE_CONTROLCOUNT_MAX_KEY = JAVELIN_PREFIX
            + "bytecode.exclude.controlCount";

    /** Bytecode�̓��e�����Ɍv���Ώۂ��珜�O����ۂ̃|���V�[�B0:���O���Ȃ� 1:BCI���Ȃ� */
    public static final String BYTECODE_EXCLUDE_POLICY_KEY = JAVELIN_PREFIX
            + "bytecode.exclude.policy";

    /** �������ɕۑ�����臒l�̃v���p�e�B */
    public static final String STATISTICSTHRESHOLD_KEY = JAVELIN_PREFIX + "statistics"
            + "Threshold";

    /** �A���[����ʒm����TAT��臒l�̃v���p�e�B */
    public static final String ALARMTHRESHOLD_KEY = JAVELIN_PREFIX + "alarmThreshold";

    /** Javelin���O���o�͂���t�@�C�����̃v���p�e�B */
    public static final String JAVELINFILEDIR_KEY = JAVELIN_PREFIX + "javelinFileDir";

    /** �X�^�b�N�g���[�X���o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String LOG_STACKTRACE_KEY = JAVELIN_PREFIX + "log.stacktrace";

    /** ���������o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String LOG_ARGS_KEY = JAVELIN_PREFIX + "log.args";

    /** �X���b�h�R���e���V�����Ď����s�����ǂ��������肷��v���p�e�B */
    public static final String THREAD_CONTENTION_KEY = JAVELIN_PREFIX + "thread.contention.monitor";

    /** JMXInfo���o�͂��邩�ǂ������肷��v���p�e�B */
    public static final String LOG_MBEANINFO_KEY = JAVELIN_PREFIX + "log.mbeaninfo";

    /** �[�_�ŁAJMXInfo���o�͂��邩�ǂ������肷��v���p�e�B */
    public static final String LOG_MBEANINFO_ROOT_KEY = JAVELIN_PREFIX + "log.mbeaninfo.root";

    /** �߂�l���o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String LOG_RETURN_KEY = JAVELIN_PREFIX + "log.return";

    /** �����̏ڍ׏����o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String ARGS_DETAIL_KEY = JAVELIN_PREFIX + "log.args.detail";

    /** �߂�l�̏ڍ׏����o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String RETURN_DETAIL_KEY = JAVELIN_PREFIX + "log.return.detail";

    /** �����̏ڍ׏��̐[����\���v���p�e�B */
    public static final String ARGS_DETAIL_DEPTH_KEY = JAVELIN_PREFIX + "log.args."
            + "detail.depth";

    /** �߂�l�̏ڍ׏��̐[����\���v���p�e�B */
    public static final String RETURN_DETAIL_DEPTH_KEY = JAVELIN_PREFIX + "log.return."
            + "detail.depth";

    /** �Ăяo�������s���̂Ƃ��ɐݒ肷�閼�O�̃v���p�e�B */
    public static final String ROOTCALLERNAME_KEY = JAVELIN_PREFIX + "rootCallerName";

    /** �ł��[���Ăяo���悪�s���̂Ƃ��ɐݒ肷�閼�O�̃v���p�e�B */
    public static final String ENDCALLEENAME_KEY = JAVELIN_PREFIX + "endCalleeName";

    /** �X���b�h�̖��̂̌�����@��\���v���p�e�B */
    public static final String THREADMODEL_KEY = JAVELIN_PREFIX + "threadModel";

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g�̃v���p�e�B�� */
    public static final String ACCEPTPORT_KEY = JAVELIN_PREFIX + "acceptPort";

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷�邩�A�̃v���p�e�B���� */
    public static final String ACCEPTPORT_ISRANGE = ACCEPTPORT_KEY + ".isRange";

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷��ۂ̍ő�l�A�̃v���p�e�B���� */
    public static final String ACCEPTPORT_RANGEMAX = ACCEPTPORT_KEY + ".rangeMax";

    /** JavelinAcceptThread�ł�accept�����̊J�n��x�点�鎞��(�~���b)�v���p�e�B����(�B���p�����[�^) */
    public static final String ACCEPT_DELAY_KEY = JAVELIN_PREFIX + "accept.delay";

    /** Javelin����BottleNeckEye/DataCollector�ւ̒ʐM�p�z�X�g���̃v���p�e�B�� */
    public static final String CONNECTHOST_KEY = JAVELIN_PREFIX + "connectHost";

    /** Javelin����BottleNeckEye/DataCollector�ւ̒ʐM�p�|�[�g�̃v���p�e�B�� */
    public static final String CONNECTPORT_KEY = JAVELIN_PREFIX + "connectPort";

    /** DataCollector�̃f�[�^�x�[�X�� */
    public static final String AGENTNAME_KEY = JAVELIN_PREFIX + "agentName";

    /** �����A�߂�l���̕����� */
    public static final String STRINGLIMITLENGTH_KEY = JAVELIN_PREFIX + "stringLimitLength";

    /** �V�X�e���g���[�X�t�@�C���̃v���p�e�B�� */
    public static final String SYSTEMLOG_KEY = JAVELIN_PREFIX + "system.log";

    /** �q�[�v�_���v�̃v���p�e�B�� */
    public static final String HEAPDUMPDIR_KEY = JAVELIN_PREFIX + "heapDumpDir";

    /** ���p����AlarmListener�� */
    public static final String ALARM_LISTENERS_KEY = JAVELIN_PREFIX + "alarmListeners";

    /** JMX�ʐM�ɂ������J���s�����ǂ�����\���v���p�e�B�� */
    public static final String RECORD_JMX_KEY = JAVELIN_PREFIX + "record.jmx";

    /** jvn���O�t�@�C���̍ő吔��\���v���p�e�B�� */
    public static final String LOG_JVN_MAX_KEY = JAVELIN_PREFIX + "log.jvn.max";

    /** jvn���O�t�@�C�������k����zip�t�@�C���̍ő吔��\���v���p�e�B�� */
    public static final String LOG_ZIP_MAX_KEY = JAVELIN_PREFIX + "log.zip.max";

    /** �L�^��������N���X */
    public static final String RECORDSTRATEGY_KEY = JAVELIN_PREFIX + "recordStrategy";

    /** ���p����TelegramListener�� */
    public static final String TELERAM_LISTENERS_KEY = JAVELIN_PREFIX + "telegramListeners";

    /** Javelin�̃V�X�e�����O�̍ő�t�@�C�����̃L�[ */
    private static final String SYSTEM_LOG_NUM_MAX_KEY = JAVELIN_PREFIX + "system.log.num.max";

    /** Javelin�̃V�X�e�����O�̍ő�t�@�C���T�C�Y�̃L�[ */
    private static final String SYSTEM_LOG_SIZE_MAX_KEY = JAVELIN_PREFIX + "system.log."
            + "size.max";

    /** Javelin�̃V�X�e�����O�̃��O���x���̃L�[ */
    private static final String SYSTEM_LOG_LEVEL_KEY = JAVELIN_PREFIX + "system.log.level";

    /** Javelin�̃C�x���g���x���̃L�[ */
    public static final String EVENT_LEVEL_KEY = JAVELIN_PREFIX + "event.level";

    /** MBeanManager���������V���A���C�Y����t�@�C���� */
    public static final String SERIALIZE_FILE_KEY = JAVELIN_PREFIX + "serializeFile";

    /** �ۑ�����CallTree���̃v���p�e�B */
    public static final String CALL_TREE_MAX_KEY = JAVELIN_PREFIX + "call.tree.max";

    /** �A�v���P�[�V�������s���̗�O���o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String ALARM_EXCEPTION_KEY = JAVELIN_PREFIX + "alarmException";

    /** HTTP�X�e�[�^�X�G���[���o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String HTTP_STATUS_ERROR_KEY = JAVELIN_PREFIX + "httpStatusError";

    /** �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔�̃L�[ */
    public static final String RECORD_INVOCATION_MAX_KEY = JAVELIN_PREFIX
            + "record.invocation.num.max";

    /** Turn Around Time���v�����邩�ǂ��������肷��v���p�e�B�̃L�[ */
    public static final String TAT_ENABLED_KEY = JAVELIN_PREFIX + "tat.monitor";

    /** Turn Around Time�̕ێ����Ԃ�\���v���p�e�B�̃L�[ */
    public static final String TAT_KEEP_TIME_KEY = JAVELIN_PREFIX + "tat.keepTime";

    /** Turn Around Time�̒l�� 0 �̏ꍇ�ɁA0 �̏o�͂� */
    public static final String TAT_ZERO_KEEP_TIME_KEY = JAVELIN_PREFIX + "tat.zeroKeepTime";

    /** �A���[�����M�Ԋu�̍ŏ��l�B�O��A���[�����M�EJavelin���O�o�͂��s�����ۂ���
     * �o�߂������Ԃ�����臒l�𒴂��Ă����ꍇ�̂݃A���[�����M�EJavelin���O�o�͂��s���B*/
    public static final String ALARM_MINIMUM_INTERVAL_KEY = JAVELIN_PREFIX + "minimumAlarmInterval";

    /** ����̃C�x���g�����o����Ԋu�B�O��̃C�x���g���炱�̎��Ԍo�߂��Ă���ꍇ�̂݁A�C�x���g�����o����B */
    public static final String EVENT_INTERVAL_KEY = JAVELIN_PREFIX + "eventInterval";

    /** �����X���b�h�����A�N�Z�X���v�����邩�ǂ��������肷��v���p�e�B�̃L�[ */
    public static final String CONCURRENT_ENABLED_KEY = JAVELIN_PREFIX + "concurrent.monitor";

    /** �^�C���A�E�g�l�ݒ�̊Ď����s�����ǂ��� */
    public static final String TIMEOUT_MONITOR = JAVELIN_PREFIX + "timeout.monitor";

    /** jvn���O�t�@�C�����o�͂��邩�ǂ����B */
    public static final String LOG_JVN_FILE = JAVELIN_PREFIX + "log.enable";

    /** BottleNeckEye�Ƃ̒ʐM�Ɏg�p����|�[�g���Ď擾����Ԋu */
    public static final String JAVELIN_BIND_INTERVAL = JAVELIN_PREFIX + "bind.interval";

    /** �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String COLLECT_SYSTEM_RESOURCES = JAVELIN_PREFIX
            + "resource.collectSystemResources";

    /** �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String ITEMNAME_PREFIX = JAVELIN_PREFIX + "resource.itemName.prefix";

    /** �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String ITEMNAME_NOPREFIX_LIST = JAVELIN_PREFIX
            + "resource.itemName.noPrefixList";

    /** �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String COLLECT_HADOOP_AGENT_RESOURCES = JAVELIN_PREFIX
            + "resource.collectHadoopAgentResources";

    /** HBase�̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String COLLECT_HBASE_AGENT_RESOURCES = JAVELIN_PREFIX
            + "resource.collectHBaseAgentResources";

    /** InvocationFullEvent�𑗐M���邩�ǂ����B */
    public static final String SEND_INVOCATION_FULL_EVENT = JAVELIN_PREFIX
            + "record.invocation.sendFullEvent";

    /** JMX�̃��\�[�X�f�[�^���擾���邩�ǂ����B */
    public static final String COLLECT_JMX_RESOURCES = JAVELIN_PREFIX
            + "resource.collectJmxResources";

    /** �X�g�[�����\�b�h���Ď����邩�ǂ��� */
    private static final String METHOD_STALL_MONITOR = JAVELIN_PREFIX + "method.stall.monitor";

    /** �X�g�[�����\�b�h���Ď�������� */
    private static final String METHOD_STALL_INTERVAL = JAVELIN_PREFIX + "method.stall.interval";

    /** �X�g�[�����\�b�h�Ɣ��f����臒l */
    private static final String METHOD_STALL_THRESHOLD = JAVELIN_PREFIX + "method.stall.threshold";

    /** �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[�� */
    private static final String METHOD_STALL_TRACE_DEPTH = JAVELIN_PREFIX
            + "method.stall.traceDepth";

    /** MBean�T�[�o�̃z�X�g�� */
    public static final String JMX_HOST = "javelin.jmx.host";

    /** MBean�T�[�o�̃|�[�g�ԍ� */
    public static final String JMX_PORT = "javelin.jmx.port";

    /** MBean�T�[�o�̔F�؃��[�U�� */
    public static final String JMX_USER_NAME = "javelin.jmx.user";

    /** MBean�T�[�o�̔F�؃p�X���[�h */
    public static final String JMX_PASSWORD = "javelin.jmx.password";

    /** �ڑ����[�h(server/client) */
    private static final String CONNECTION_MODE_KEY = JAVELIN_PREFIX + "connection.mode";

    /** ���\�b�h���̍ő啶���� */
    private static final String INVOCATION_NAME_LIMITLENGTH_KEY = JAVELIN_PREFIX
            + "invocation.name.limitLength";

    /**
     * ConcurrentAccessMonitor��CollectionMonitor���쒆�ɃN���X���[�h���N�����Ƃ��ɁA
     * �o�C�g�R�[�h�ϊ����s�����ǂ�����ݒ肷��v���p�e�B�̃f�t�H���g�l */
    private static final boolean DEFAULT_SKIPCLASS_ONPROCESSING = true;

    /** �ۑ�����CallTreeNode���̃f�t�H���g�l */
    private static final int DEFAULT_CALL_TREE_MAX = 5000;

    /** CallTreeNode�̌v���l�ۑ�臒l�̃f�t�H���g�l */
    public static final int DEFAULT_CALL_TREE_MAX_MEASURE = 2500;

    /** ���\�b�h���ώ��Ԃ��o�͂��邽�߂ɋL�^����Invocation���̃f�t�H���g�l */
    private static final int DEFAULT_INTERVALMAX = 500;

    /** ��O�̐����L�^���邽�߂�Invocation���̃f�t�H���g�l */
    private static final int DEFAULT_THROWABLEMAX = 100;

    /** Bytecode�̓��e�����Ɍv���Ώۂ��珜�O����A�ő�bytecode���B */
    public static final int DEFAULT_BYTECODE_LENGTH_MIN_KEY = 12;

    /** Javelin�̏��擾�Ώۂ��珜�O����A�ő�̐��䖽�ߐ��̃v���p�e�B */
    public static final int DEFAULT_BYTECODE_EXCLUDE_CONTROLCOUNT_MAX_KEY = 0;

    /** Bytecode�̓��e�����Ɍv���Ώۂ��珜�O����ۂ̃|���V�[�B0:���O���Ȃ� 1:BCI���Ȃ� */
    public static final int DEFAULT_BYTECODE_EXCLUDE_POLICY_KEY = 1;

    /** �������ɕۑ�����臒l�̃v���p�e�B */
    private static final long DEFAULT_STATISTICSTHRESHOLD = 0;

    /** �A���[����ʒm����TAT��臒l�̃v���p�e�B */
    private static final long DEFAULT_ALARMTHRESHOLD = 5000;

    /** ����̃C�x���g�����o����Ԋu�̃f�t�H���g�l */
    public static final long DEFAULT_EVENT_INTERVAL = 1000 * 60 * 60;

    /** Javelin���O���o�͂���t�@�C�����̃v���p�e�B */
    private static final String DEFAULT_JAVELINFILEDIR = "../logs";

    /** �X�^�b�N�g���[�X���o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_STACKTRACE = false;

    /** ���������o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_ARGS = true;

    /** �������ɃZ�b�V���������o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_HTTP_SESSION = true;

    /** �X���b�h�R���e���V�����Ď����s�����ǂ������肷��f�t�H���g�l */
    private static final boolean DEFAULT_THREAD_CONTENTION = true;

    /** JMXInfo���o�͂��邩�ǂ������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_MBEANINFO = true;

    /** JMXInfo���o�͂��邩�ǂ������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_MBEANINFO_ROOT = true;

    /** �߂�l���o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_LOG_RETURN = true;

    /** �����̏ڍ׏����o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_ARGS_DETAIL = false;

    /** �����̏ڍ׏��̐[���̃f�t�H���g�l */
    private static final int DEFAULT_ARGS_DETAIL_DEPTH = 1;

    /** �߂�l�̏ڍ׏����o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_RETURN_DETAIL = false;

    /** �߂�l�̏ڍ׏��̐[���̃f�t�H���g�l */
    private static final int DEFAULT_RETURN_DETAIL_DEPTH = 1;

    /** HTTP�Z�b�V�����̏ڍ׏����o�͂��邩�ǂ��������肷��f�t�H���g�l */
    private static final boolean DEFAULT_HTTP_SESSION_DETAIL = false;

    /** HTTP�Z�b�V�����̏ڍ׏��̐[���̃f�t�H���g�l */
    private static final int DEFAULT_HTTP_SESSION_DETAIL_DEPTH = 1;

    /** �Ăяo�������s���̂Ƃ��ɐݒ肷�閼�O�̃v���p�e�B */
    private static final String DEFAULT_ROOTCALLERNAME = "root";

    /** �ł��[���Ăяo���悪�s���̂Ƃ��ɐݒ肷�閼�O�̃v���p�e�B */
    private static final String DEFAULT_ENDCALLEENAME = "unknown";

    /** �X���b�h�̖��̂̌�����@��\���v���p�e�B */
    private static final int DEFAULT_THREADMODEL = 0;

    /** �����A�߂�l���̕����񒷂̃f�t�H���g�l */
    private static final int DEFAULT_STRINGLIMITLENGTH = 102400;

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g�̃f�t�H���g�l */
    public static final int DEFAULT_ACCEPTPORT = 18000;

    /** JavelinAcceptThread�ł�accept�����̊J�n��x�点�鎞��(�~���b)�̃f�t�H���g�l */
    public static final long DEFAULT_ACCEPT_DELAY = 0;

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷�邩�A�̃f�t�H���g�l */
    public static final boolean DEF_ACCEPTPORT_ISRANGE = false;

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷��ۂ̍ő�l�̃f�t�H���g�l */
    public static final int DEF_ACCEPTPORT_RANGEMAX = 18010;

    /** BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷��ۂ̍ŏ��l�̃f�t�H���g�l */
    public static final int DEF_ACCEPTPORT_RANGEMIN = 18000;

    /** Javelin����BottleNeckEye/DataCollector�ւ̒ʐM�p�z�X�g���̃f�t�H���g�l */
    public static final String DEFAULT_CONNECTHOST = "localhost";

    /** Javelin����BottleNeckEye/DataCollector�ւ̒ʐM�p�|�[�g�̃f�t�H���g�l */
    public static final int DEFAULT_CONNECTPORT = 19000;

    /** DataCollector�̃f�[�^�x�[�X���̃f�t�H���g�l */
    public static final String DEFAULT_AGENTNAME = "endosnipedb";

    /** Javelin�V�X�e�����O�̏o�͐�p�X�̃f�t�H���g�l */
    public static final String DEFAULT_SYSTEMLOG = "../traces";

    /** �q�[�v�_���v�̏o�͐�p�X�̃f�t�H���g�l */
    public static final String DEFAULT_HEAPDUMP_DIR = "../heapdump";

    /** �f�t�H���g�ŗ��p����AlarmListener�� */
    private static final String DEFAULT_ALARM_LISTENERS = "";

    /** �f�t�H���g��JMX�ʐM�ɂ������J���s�����ǂ��� */
    private static final boolean DEFAULT_RECORD_JMX = true;

    /** jvn���O�t�@�C���̍ő吔�̃f�t�H���g */
    private static final int DEFAULT_LOG_JVN_MAX = 256;

    /** jvn���O�t�@�C�������k����zip�t�@�C���̍ő吔�̃f�t�H���g */
    private static final int DEFAULT_LOG_ZIP_MAX = 256;

    /** �L�^��������N���X�̃f�t�H���g */
    public static final String DEFAULT_RECORDSTRATEGY =
            "jp.co.acroquest.endosnipe.javelin.record.CpuTimeRecordStrategy";

    /** �f�t�H���g�ŗ��p����TelegramListener�� */
    private static final String DEFAULT_TELEGEAM_LISTENERS =
            "jp.co.acroquest.endosnipe.javelin.communicate.GetRequestTelegramListener,"
                    + "jp.co.acroquest.endosnipe.javelin.communicate.ResetRequestTelegramListener";

    /** Javelin�̃V�X�e�����O�̍ő�t�@�C�����̃f�t�H���g */
    private static final int DEFAULT_SYSTEM_LOG_NUM_MAX = 16;

    /** Javelin�̃V�X�e�����O�̍ő�t�@�C���T�C�Y�̃f�t�H���g */
    private static final int DEFAULT_SYSTEM_LOG_SIZE_MAX = 1000000;

    /** MBeanManager���������V���A���C�Y����t�@�C�����̃f�t�H���g */
    public static final String DEFAULT_SERIALIZE_FILE = "../data/serialize.dat";

    /** Javelin�̃V�X�e�����O�̃��O���x���̃f�t�H���g */
    private static final String DEFAULT_SYSTEM_LOG_LEVEL = "INFO";

    /** Javelin�̃C�x���g���x���̃f�t�H���g */
    private static final String DEFAULT_EVENT_LEVEL = "WARN";

    /** �A�v���P�[�V�������s���̗�O���ɃA���[���ʒm����f�t�H���g�l */
    private static final boolean DEFAULT_ALARM_EXCEPTION = true;

    /** HTTP�X�e�[�^�X�G���[�̃A���[���ʒm����f�t�H���g�l */
    private static final boolean DEFAULT_HTTP_STATUS_ERROR = true;

    /** �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔�̃f�t�H���g�l */
    private static final int DEFAULT_REC_INVOCATION_MAX = 1024;

    /** �A���[�����M�Ԋu�̍ŏ��l�̃f�t�H���g�l�B*/
    public static final long DEFAULT_ALARM_MINIMUM_INTERVAL = 60000;

    /** Turn Around Time���v�����邩�ǂ����̃f�t�H���g�l */
    private static final boolean DEFAULT_TAT_ENABLED = true;

    /** Turn Around Time�̕ێ����Ԃ̃f�t�H���g�l�B*/
    public static final long DEFAULT_TAT_KEEP_TIME = 15000;

    /** Turn Around Time�̒l��0�̏ꍇ�ɁA0�̏o�͂��p�����鎞�Ԃ̃f�t�H���g�l�B */
    public static final long DEFAULT_TAT_ZERO_KEEP_TIME = 10000;

    /** jvn���O�t�@�C�����o�͂��邩�ǂ����̃f�t�H���g�l�B */
    private static final boolean DEFAULT_LOG_JVN_FILE = true;

    /** �����X���b�h�A�N�Z�X���Ď����邩�ǂ����̃f�t�H���g�l */
    private static final boolean DEFAULT_CONCURRENT_ENABLED = true;

    /** �^�C���A�E�g�l�̐ݒ肪�s���Ă��邩�ǂ����̃f�t�H���g�l */
    private static final boolean DEF_TIMEOUT_MONITOR = true;

    /** MBean�T�[�o�̃z�X�g���̃f�t�H���g�l */
    public static final String DEF_JMX_HOST = "localhost";

    /** MBean�T�[�o�̃|�[�g�ԍ��̃f�t�H���g�l */
    public static final int DEF_JMX_PORT = 0;

    /** MBean�T�[�o�̔F�؃��[�U���̃f�t�H���g�l */
    public static final String DEF_JMX_USER_NAME = "";

    /** MBean�T�[�o�̔F�؃p�X���[�h�̃f�t�H���g�l */
    public static final String DEF_JMX_PASSWORD = "";

    /** ���O�o�͑Ώۃp�^�[���̃v���p�e�B�� */
    public static final String INCLUDE = JAVELIN_PREFIX + "include";

    /** ���O�o�͏��O�p�^�[���̃v���p�e�B�� */
    public static final String EXCLUDE = JAVELIN_PREFIX + "exclude";

    /** JVN�t�@�C���_�E�����[�h���̍ő�o�C�g���̃v���p�e�B�� */
    public static final String JVN_DOWNLOAD_MAX = JAVELIN_PREFIX + "log.download.max";

    /** �x���𔭐�������CPU���Ԃ�臒l�� */
    public static final String ALARM_CPUTHRESHOLD = JAVELIN_PREFIX + "alarmCpuThreshold";

    /** ���C�Z���X�t�@�C���p�X�̃v���p�e�B�� */
    public static final String LICENSEPATH = JAVELIN_PREFIX + "license.path";

    /** �N���X�����ȗ�������v���p�e�B�� */
    public static final String CLASSNAME_SIMPLIFY = JAVELIN_PREFIX + "className.simplify";

    /** Collection�̃��������[�N���o���s�����ǂ��� */
    public static final String COLLECTION_MONITOR = JAVELIN_PREFIX + "leak.collection.monitor";

    /** �R���N�V�����̐����Ď�����ۂ�臒l */
    public static final String COLLECTION_SIZE_THRESHOLD = JAVELIN_PREFIX
            + "leak.collectionSizeThreshold";

    /** �R���N�V�����̐����Ď�����ۂ̏o�̓`�F�b�N�̊Ԋu */
    public static final String COLLECTION_INTERVAL = JAVELIN_PREFIX + "leak.interval";

    /** �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐��B */
    public static final String COLLECTION_TRACE_MAX = JAVELIN_PREFIX + "leak.traceMax";

    /** �X�^�b�N�g���[�X�̕\���Ɏg���[���B */
    public static final String TRACE_DEPTH = JAVELIN_PREFIX + "traceDepth";

    /** �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐[���B */
    public static final String COLLECTION_LEAKDETECT_DEPTH = JAVELIN_PREFIX
            + "leak.detect.traceDepth";

    /** �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ����B */
    public static final String CLASS_HISTO_GC = JAVELIN_PREFIX + "leak.class.histo.gc";

    /** �N���X�q�X�g�O�����̏�ʉ������擾���邩 */
    public static final String CLASS_HISTO_MAX = JAVELIN_PREFIX + "leak.class.histo.max";

    /** �N���X�q�X�g�O�����擾�Ԋu(�~���b) */
    public static final String CLASS_HISTO_INTERVAL = JAVELIN_PREFIX + "leak.class.histo.interval";

    /** ���`�������s�����ǂ��� */
    public static final String LINEARSEARCH_ENABLED_KEY = JAVELIN_PREFIX + "linearsearch.monitor";

    /** ���`�������o���s�����X�g�T�C�Y��臒l */
    public static final String LINEARSEARCH_SIZE = JAVELIN_PREFIX + "linearsearch.size";

    /** ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l */
    public static final String LINEARSEARCH_RATIO = JAVELIN_PREFIX + "linearsearch.ratio";

    /** �N���X�q�X�g�O�������擾���邩�ǂ����@ */
    public static final String CLASS_HISTO = JAVELIN_PREFIX + "leak.class.histo";

    /** �N���X�ϊ���Adetach���邩�ǂ���(����J�p�����[�^)�@ */
    public static final String DETACH = JAVELIN_PREFIX + "detach";

    /** �X���b�h�Ď����s�����ǂ����@ */
    public static final String THREAD_MONITOR = JAVELIN_PREFIX + "thread.monitor";

    /** �X���b�h�Ď����s������(�~���b)�@ */
    public static final String THREAD_MONITOR_INTERVAL = JAVELIN_PREFIX + "thread.monitor.interval";

    /** �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[���@ */
    public static final String THREAD_MONITOR_DEPTH = JAVELIN_PREFIX + "thread.monitor.depth";

    /** �t���X���b�h�_���v���o�͂��邩�ǂ����@ */
    public static final String THREAD_DUMP_MONITOR = JAVELIN_PREFIX + "thread.dump.monitor";

    /** �X���b�h�_���v�擾�̊Ԋu */
    public static final String THREAD_DUMP_INTERVAL = JAVELIN_PREFIX + "thread.dump.interval";

    /** �t���X���b�h�_���v�o�͂ɗ��p����X���b�h����臒l�@ */
    public static final String THREAD_DUMP_THREAD = JAVELIN_PREFIX + "thread.dump.threadnum";

    /** �t���X���b�h�_���v�o�͂ɗ��p����CPU�g�p����臒l�@ */
    public static final String THREAD_DUMP_CPU = JAVELIN_PREFIX + "thread.dump.cpu";

    /** �t��GC�����o���邩�ǂ��� */
    public static final String FULLGC_MONITOR = JAVELIN_PREFIX + "fullgc.monitor";

    /** �t��GC���s��GarbageCollector���̃��X�g */
    public static final String FULLGC_LIST = JAVELIN_PREFIX + "fullgc.list";

    /** �t��GC���Ԃ�臒l */
    public static final String FULLGC_THREASHOLD = JAVELIN_PREFIX + "fullgc.threshold";

    /** �l�b�g���[�N���͗ʂ��擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String NET_INPUT_MONITOR = JAVELIN_PREFIX + "net.input" + ".monitor";

    /** �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String NET_OUTPUT_MONITOR = JAVELIN_PREFIX + "net.output" + ".monitor";

    /** �t�@�C�����͗ʂ��擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String FILE_INPUT_MONITOR = JAVELIN_PREFIX + "file.input" + ".monitor";

    /** �t�@�C���o�͗ʂ��擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String FILE_OUTPUT_MONITOR = JAVELIN_PREFIX + "file.output" + ".monitor";

    /** �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String FINALIZATION_COUNT_MONITOR = JAVELIN_PREFIX
            + "finalizationCount.monitor";

    /** ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ��������肷��v���p�e�B�� */
    public static final String INTERVAL_ERROR_MONITOR = JAVELIN_PREFIX + "interval.monitor";

    /** �p���𒲂ׂ�[���̍ő�l�̃v���p�e�B���@ */
    public static final String INHERITANCE_DEPTH = JAVELIN_PREFIX + "inheritance" + ".depth";

    /** �u���b�N�񐔂��������邩�ǂ�����臒l */
    public static final String THREAD_BLOCK_THRESHOLD = JAVELIN_PREFIX + "thread.block"
            + ".threshold";

    /** �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l */
    public static final String THREAD_BLOCKTIME_THRESHOLD = JAVELIN_PREFIX + "thread.blocktime"
            + ".threshold";

    /** �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐��B */
    public static final String THREAD_BLOCK_THREADINFO_NUM = JAVELIN_PREFIX + "thread.block"
            + ".threadinfo.num";

    /** Java���x���f�b�h���b�N�̊Ď����s���� */
    public static final String THREAD_DEADLOCK_MONITOR = JAVELIN_PREFIX + "thread.deadlock.monitor";

    /** ���\�b�h�ɑ΂���Ăяo���Ԋu��臒l��`�B */
    public static final String INTERVAL_THRESHOLD = JAVELIN_PREFIX + "interval" + ".threshold";

    /** ���\�b�h�ɑ΂���A�����̒l���Ƃ̌Ăяo���Ԋu��臒l��`�B */
    public static final String INTERVAL_PER_ARGS_THRESHOLD = JAVELIN_PREFIX + "interval.perargs"
            + ".threshold";

    /** HttpSession�̃C���X�^���X�����擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String HTTP_SESSION_COUNT_MONITOR = JAVELIN_PREFIX + "httpSessionCount"
            + ".monitor";

    /** HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���擾���邩�ǂ����̃t���O�̃v���p�e�B���@ */
    public static final String HTTP_SESSION_SIZE_MONITOR = JAVELIN_PREFIX + "httpSessionSize"
            + ".monitor";

    /** ��������HTTP�Z�b�V���������o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String LOG_HTTP_SESSION_KEY = JAVELIN_PREFIX + "log.http.session";

    /** HTTP�Z�b�V�����̏ڍ׏����o�͂��邩�ǂ��������肷��v���p�e�B */
    public static final String HTTP_SESSION_DETAIL_KEY = JAVELIN_PREFIX + "log.http.session.detail";

    /** HTTP�Z�b�V�����̏ڍ׏��̐[����\���v���p�e�B */
    public static final String HTTP_SESSION_DETAIL_DEPTH_KEY = JAVELIN_PREFIX
            + "log.http.session.detail.depth";

    /** �v���Ώۂ��玩�����O����Ăяo���񐔂�臒l */
    private static final String AUTO_EXCLUDE_THRESHOLD_COUNT = JAVELIN_PREFIX
            + "autoExcludeThreshold.count";

    /** �v���Ώۂ��玩�����O������s���Ԃ�臒l */
    private static final String AUTO_EXCLUDE_THRESHOLD_TIME = JAVELIN_PREFIX
            + "autoExcludeThreshold.time";

    /** ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ��� */
    public static final String LEAK_COLLECTIONSIZE_OUT = JAVELIN_PREFIX + "leak.collectionSizeOut";

    /** Log4J�̃��O�o�͂̍ہA�X�^�b�N�g���[�X���o�͂��郌�x����臒l */
    public static final String LOG4J_PRINTSTACK_LEVEL = JAVELIN_PREFIX + "log4j.printstack.level";

    /** EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ��� */
    public static final String EJB_SESSION_MONITOR = JAVELIN_PREFIX + "ejb.session.monitor";

    /** CPU���Ԃɂ����鐔�B�f�t�H���g�ł�1 */
    private static final String CPU_TIME_UNIT_KEY = JAVELIN_PREFIX + "cpu.unit";

    /** ���O�o�͂�����Ώۂ��L�q�����t�B���^�t�@�C�����̃f�t�H���g�l */
    public static final String DEF_INCLUDE = "include.conf";

    /** ���O�o�͂��珜�O����Ώۂ��L�q�����t�B���^�t�@�C�����̃f�t�H���g�l */
    public static final String DEF_EXCLUDE = "exclude.conf";

    /** JVN�t�@�C���_�E�����[�h���̍ő�o�C�g���̃v���p�e�B�� */
    public static final int DEF_JVN_DOWNLOAD_MAX = 1024 * 1024;

    /** �x���𔭐�������b�o�t���Ԃ̃f�t�H���g�l */
    public static final long DEF_ALARM_CPUTHRESHOLD = 1000;

    /** �N���X�ϊ���Adetach���邩�ǂ����̃f�t�H���g�l(����J�p�����[�^)�@ */
    public static final boolean DEF_DETACH = true;

    /** ���C�Z���X�t�@�C���p�X�̃f�t�H���g�l */
    public static final String DEF_LICENSEPATH = "../license/ENdoSnipeLicense.dat";

    /** �N���X���ȗ�����\���t���O�̃f�t�H���g�l */
    public static final boolean DEF_CLASSNAME_SIMPLIFY = false;

    /** Collection�̃��������[�N���o���s�����ǂ����̃f�t�H���g�l */
    public static final boolean DEF_COLLECTION_MONITOR = true;

    /** ���������[�N�Ƃ��Č��o����Collection�AMap�̃T�C�Y��臒l */
    public static final int DEF_COLLECTION_SIZE = 2000;

    /** �R���N�V�����̐����Ď�����ۂ̏o�̓`�F�b�N�̊Ԋu */
    public static final int DEF_COLLECTION_INTERVAL = 11;

    /** �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐��B */
    public static final int DEF_COLLECTION_TRACE_MAX = 20;

    /** �X�^�b�N�g���[�X�̕\���Ɏg���[���B */
    public static final int DEF_COLLECTION_TRACE_DEPTH = 15;

    /** �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐[���B */
    public static final int DEF_COLLECTION_LEAKDETECT_DEPTH = 5;

    /** �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ����̃f�t�H���g�l�B */
    public static final boolean DEF_CLASS_HISTO_GC = false;

    /** �N���X�q�X�g�O�����̏�ʉ������擾���邩�̃f�t�H���g�l */
    public static final int DEF_CLASS_HISTO_MAX = 15;

    /** �N���X�q�X�g�O�����擾�Ԋu(�~���b)�̃f�t�H���g�l */
    public static final int DEF_CLASS_HISTO_INTERVAL = 60000;

    /** �N���X�q�X�g�O�������擾���邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_CLASS_HISTO = true;

    /** ���`�������o���Ď����邩�ǂ����̃f�t�H���g�l */
    private static final boolean DEF_LINEARSEARCH_ENABLED = true;

    /** ���`�������s�����X�g�T�C�Y�̃f�t�H���g�l */
    public static final int DEF_LINEARSEARCH_SIZE = 100;

    /** ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊����̃f�t�H���g�l */
    public static final double DEF_LINEARSEARCH_RATIO = 5;

    /** �X���b�h�Ď����s�����ǂ����̃f�t�H���g�l�@ */
    public static final boolean DEF_THREAD_MONITOR = true;

    /** �X���b�h�Ď����s������(�~���b)�̃f�t�H���g�l�@ */
    public static final long DEF_THREAD_MON_INTERVAL = 1000;

    /** �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[���̃f�t�H���g�l�@ */
    public static final int DEF_THREAD_MON_DEPTH = 10;

    /** �t���X���b�h�_���v���o�͂��邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_THREAD_DUMP_MONITOR = false;

    /** �t���X���b�h�_���v�o�͊Ԋu�̃f�t�H���g�l */
    public static final int DEF_THREAD_DUMP_INTERVAL = 10000;

    /** �t���X���b�h�_���v���o�͂���X���b�h����臒l�̃f�t�H���g�l */
    public static final int DEF_THREAD_DUMP_THREAD = 100;

    /** �t���X���b�h�_���v���o�͂���CPU�g�p����臒l�̃f�t�H���g�l */
    public static final int DEF_THREAD_DUMP_CPU = 50;

    /** �t��GC���o�͂��邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_FULLGC_MONITOR = true;

    /** �t��GC���s��GarbageCollector���̃��X�g�̃f�t�H���g�l */
    public static final String DEF_FULLGC_LIST =
            "MarkSweepCompact,Garbage collection optimized for throughput Old Collector";

    /** �t��GC���o���s��GC���Ԃ�臒l�̃f�t�H���g�l */
    public static final int DEF_FULLGC_THRESHOLD = 5000;

    /** Java���x���f�b�h���b�N�̊Ď����s�����̃f�t�H���g�l */
    public static final boolean DEF_THREAD_DEADLOCK_MONITOR = false;

    /** �l�b�g���[�N���͗ʂ��擾���邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_NET_INPUT_MONITOR = false;

    /** �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_NET_OUTPUT_MONITOR = false;

    /** �t�@�C�����͗ʂ��擾���邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_FILE_INPUT_MONITOR = false;

    /** �t�@�C���o�͗ʂ��擾���邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_FILE_OUTPUT_MONITOR = false;

    /** �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_FINALIZATION_COUNT_MONITOR = true;

    /** ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_INTERVAL_ERROR_MONITOR = true;

    /** HttpSession�̃C���X�^���X�����Ď����邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_HTTP_SESSION_COUNT_MONITOR = true;

    /** HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩�ǂ����̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_HTTP_SESSION_SIZE_MONITOR = true;

    /** �p���𒲂ׂ�[���̍ő�l�̃f�t�H���g�l�@ */
    public static final int DEF_INHERITANCE_DEPTH = 3;

    /** �u���b�N�񐔂��������邩�ǂ�����臒l�̃f�t�H���g�l�B */
    public static final long DEF_THREAD_BLOCK_THRESHOLD = 10;

    /** �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐��̃f�t�H���g�l�B */
    public static final int DEF_THREAD_BLOCK_THREADINFO_NUM = 10;

    /** �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l�̃f�t�H���g�l */
    public static final long DEF_THREAD_BLOCKTIME_THRESHOLD = 2000;

    /** ���\�b�h�̌Ăяo���Ԋu�ɑ΂���f�t�H���g�l(�ΏۂȂ�)�B */
    public static final String DEF_INTERVAL_THRESHOLD = "";

    /** ���\�b�h�́A�����̒l���Ƃ̌Ăяo���Ԋu�ɑ΂���f�t�H���g�l(�ΏۂȂ�)�B */
    public static final String DEF_INTERVAL_PER_ARGS_THRESHOLD = "";

    /** �v���ΏۂɊ܂߂邩���肷�邽�߂̉񐔂�臒l */
    public static final int DEF_AUTO_EXCLUDE_THRESHOLD_COUNT = 10;

    /** �v���ΏۂɊ܂߂邩���肷�邽�߂̎��Ԃ�臒l(�P��:�~���b) */
    public static final int DEF_AUTO_EXCLUDE_THRESHOLD_TIME = 100;

    /** ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�̃t���O�̃f�t�H���g�l */
    public static final boolean DEF_LEAK_COLLECTIONSIZE_OUT = false;

    /** BottleNeckEye�Ƃ̒ʐM�Ɏg�p����|�[�g���Ď擾����Ԋu�̃f�t�H���g�l */
    public static final int DEF_JAVELIN_BIND_INTERVAL = 5000;

    /** Log4J�̃��O�o�͂̍ہA�X�^�b�N�g���[�X���o�͂��郌�x����臒l�̃f�t�H���g�l */
    public static final String DEF_LOG4J_PRINTSTACK_LEVEL = "ERROR";

    /** EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ����̃f�t�H���g�l */
    public static final boolean DEF_EJB_SESSION_MONITOR = false;

    /** �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_COLLECT_SYSTEM_RESOURCES = true;

    /** InvocationFullEvent�𑗐M���邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_SEND_INVOCATION_FULL_EVENT = true;

    /** JMX�̃��\�[�X�f�[�^���擾���邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_COLLECT_JMX_RESOURCES = true;

    /** Javelin�̐ݒ�l��ۑ�����I�u�W�F�N�g */
    private static final JavelinConfigUtil CONFIGUTIL;

    /** CPU���Ԃɂ����鐔�B�f�t�H���g�ł�1 */
    private static final int DEF_CPU_TIME_UNIT = 1;

    /** �R�[���c���[����ꂽ�ꍇ�ɑS�Ă��L�^���邩�ǂ��� */
    private static final String CALL_TREE_ALL_KEY = JAVELIN_PREFIX + "call.tree.all";

    /** �R�[���c���[����ꂽ�ꍇ�ɑS�Ă��L�^���邩�ǂ��� */
    private static final boolean DEF_CALL_TREE_ALL = false;

    /** �R�[���c���[���L�^���邩�ǂ��� */
    public static final String CALL_TREE_ENABLE_KEY = JAVELIN_PREFIX + "call.tree.enable";

    /** �R�[���c���[���L�^���邩�ǂ��� */
    private static final boolean DEF_CALL_TREE_ENABLE = true;

    /** �X�g�[�����\�b�h���Ď����邩�ǂ����̃f�t�H���g�l */
    public static final boolean DEF_METHOD_STALL_MONITOR = false;

    /** �X�g�[�����\�b�h���Ď���������̃f�t�H���g�l */
    public static final int DEF_METHOD_STALL_INTERVAL = 10000;

    /** �X�g�[�����\�b�h�Ɣ��f����臒l�̃f�t�H���g�l */
    public static final int DEF_METHOD_STALL_THRESHOLD = 60000;

    /** �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[���̃f�t�H���g�l */
    public static final int DEF_METHOD_STALL_TRACE_DEPTH = 30;

    /** �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[���̃f�t�H���g�l */
    public static final String DEF_CONNNECTION_MODE = "server";

    /** �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[���̃f�t�H���g�l */
    public static final int DEF_INVOCATION_NAME_LIMITLENGTH = 2048;

    /** ���ږ��ɕt�^����ړ����̕�����B */
    private static final String DEF_ITEMNAME_PREFIX = "";

    /** ���ږ��ɐړ�����t�^���Ȃ��p�^�[���B */
    private static final String DEF_ITEMNAME_NOPREFIX_LIST = "/common/";

    /** Javelin��K�p�����v���Z�X��������N���X�^���̂��擾����L�[������B */
    private static final String CLUSTER_NAME_KEY = JAVELIN_PREFIX + "clusterName";

    /** Javelin��K�p�����v���Z�X��������N���X�^���̂̃f�t�H���g�l�B */
    private static final String DEF_CLUSTER_NAME = "/default";

    /** HadoopAgent����擾����B */
    private static final boolean DEF_COLLECT_HADOOP_AGENT_RESOURCES = false;

    /** HBaseAgent����擾����B */
    private static final boolean DEF_COLLECT_HBASE_AGENT_RESOURCES = false;

    /** �����|�[�g�ڑ����Ƀt�H���_�ɂ��閼�O */
    private static String logFolderName__;

    /** �����|�[�g�ڑ�����Javelin���O�t�@�C���ۑ�������������Ă��邩�ǂ��� */
    private static boolean isJvnDirInit__ = false;

    /** �����|�[�g�ڑ����ɃV�X�e�����O�t�@�C���ۑ�������������Ă��邩�ǂ��� */
    private static boolean isSysLogDirInit__ = false;

    static
    {
        CONFIGUTIL = JavelinConfigUtil.getInstance();
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        try
        {
            logFolderName__ = bean.getName().replaceAll("[^0-9a-zA-Z]", "_");
        }
        catch (Exception ex)
        {
            Random random = new Random();
            final int RANDOMNUM = 10000;
            logFolderName__ = String.valueOf(random.nextInt(RANDOMNUM));
        }
    }

    /**
     * {@link JavelinConfig} ���\�z���܂��B<br />
     */
    public JavelinConfigBase()
    {
        // Do Nothing.
    }

    /**
     * �w�肵���L�[�ɑ΂���Boolean�l�̍X�V���m���ɔ��f������B
     * 
     * @param key �X�V���f�Ώۂ̃L�[
     */
    public void updateBooleanValue(final String key)
    {
        CONFIGUTIL.updateBooleanValue(key);
    }

    /**
     * �w�肵���L�[�ɑ΂���Integer�l�̍X�V���m���ɔ��f������B
     * 
     * @param key �X�V���f�Ώۂ̃L�[
     */
    public void updateIntValue(final String key)
    {
        CONFIGUTIL.updateIntValue(key);
    }

    /**
     * �w�肵���L�[�ɑ΂���Long�l�̍X�V���m���ɔ��f������B
     * 
     * @param key �X�V���f�Ώۂ̃L�[
     */
    public void updateLongValue(final String key)
    {
        CONFIGUTIL.updateLongValue(key);
    }

    /**
     * ConcurrentAccessMonitor �� CollectionMonitor ���쒆�ɃN���X���[�h���N�����Ƃ��ɁA
     * �o�C�g�R�[�h�ϊ����s�����ǂ�����Ԃ��܂��B<br />
     *
     * @return �ϊ����s��Ȃ��ꍇ�� <code>true</code> �A�ϊ����s���ꍇ�� <code>false</code>
     */
    public boolean isSkipClassOnProcessing()
    {
        return CONFIGUTIL.getBoolean(SKIPCLASS_ONPROCESSING_KEY, DEFAULT_SKIPCLASS_ONPROCESSING);
    }

    /**
     * ConcurrentAccessMonitor �� CollectionMonitor ���쒆�ɃN���X���[�h���N�����Ƃ��ɁA
     * �o�C�g�R�[�h�ϊ����s�����ǂ�����ݒ肵�܂��B<br />
     *
     * @param skipClass �ϊ����s��Ȃ��ꍇ�� <code>true</code> �A�ϊ����s���ꍇ�� <code>false</code>
     */
    public void setSkipClassOnProcessing(final boolean skipClass)
    {
        CONFIGUTIL.setBoolean(SKIPCLASS_ONPROCESSING_KEY, skipClass);
    }

    /**
     * �Ăяo�������L�^����ۂ�臒l��Ԃ��B
     *
     * @return 臒l�i�~���b�j
     */
    public long getAlarmThreshold()
    {
        return CONFIGUTIL.getLong(ALARMTHRESHOLD_KEY, DEFAULT_ALARMTHRESHOLD);
    }

    /**
     * �Ăяo�������L�^����ۂ�臒l���ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetAlarmThreshold()
    {
        return isKeyExist(ALARMTHRESHOLD_KEY);
    }

    /**
     * �Ăяo�������L�^����ۂ�臒l��Ԃ��B
     *
     * @param alarmThreshold 臒l�i�~���b�j
     */
    public void setAlarmThreshold(final long alarmThreshold)
    {
        CONFIGUTIL.setLong(ALARMTHRESHOLD_KEY, alarmThreshold);
    }

    /**
     * JMX�ʐM�ɂ������J���s�����ǂ����̐ݒ��Ԃ��B
     *
     * @return JMX�ʐM�ɂ������J���s���Ȃ�true
     */
    public boolean isRecordJMX()
    {
        return CONFIGUTIL.getBoolean(RECORD_JMX_KEY, DEFAULT_RECORD_JMX);
    }

    /**
     * JMX�ʐM�ɂ������J���s�����ǂ�����ݒ肷��B
     *
     * @param isRecordJMX JMX�ʐM�ɂ������J���s���Ȃ�true
     */
    public void setRecordJMX(final boolean isRecordJMX)
    {
        CONFIGUTIL.setBoolean(RECORD_JMX_KEY, isRecordJMX);
    }

    /**
     * �Ăяo�������L�^����ő匏����Ԃ��B
     *
     * @return ����
     */
    public int getIntervalMax()
    {
        return CONFIGUTIL.getInteger(INTERVALMAX_KEY, DEFAULT_INTERVALMAX);
    }

    /**
     * �Ăяo�������L�^����ő匏�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetIntervalMax()
    {
        return isKeyExist(INTERVALMAX_KEY);
    }

    /**
     * �Ăяo�������L�^����ő匏�����Z�b�g����B
     *
     * @param intervalMax ����
     */
    public void setIntervalMax(final int intervalMax)
    {
        CONFIGUTIL.setInteger(INTERVALMAX_KEY, intervalMax);
    }

    /**
     * Javelin���O�t�@�C���̏o�͐���擾����B
     *
     * @return �o�͐�p�X
     */
    public String getJavelinFileDir()
    {
        String relativePath = CONFIGUTIL.getString(JAVELINFILEDIR_KEY, DEFAULT_JAVELINFILEDIR);

        // �����|�[�g�ڑ��ݒ���s���ꍇ�AJavelin���O�t�@�C���̕ۑ��ꏊ���Đݒ肷��B
        if (isJvnDirInit__ == false)
        {
            if (isAcceptPortIsRange())
            {
                relativePath = relativePath + File.separator + logFolderName__;
                setJavelinFileDir(relativePath);
            }
            isJvnDirInit__ = true;
        }
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * Javelin���O�t�@�C���̏o�͐悪�ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetJavelinFileDir()
    {
        return isKeyExist(JAVELINFILEDIR_KEY);
    }

    /**
     * Javelin���O�t�@�C���̏o�͐���Z�b�g����B
     *
     * @param javelinFileDir �o�͐�p�X
     */
    public void setJavelinFileDir(final String javelinFileDir)
    {
        CONFIGUTIL.setString(JAVELINFILEDIR_KEY, javelinFileDir);
    }

    /**
     * ��O�̔����������L�^����ő匏����Ԃ��B
     *
     * @return ����
     */
    public int getThrowableMax()
    {
        return CONFIGUTIL.getInteger(THROWABLEMAX_KEY, DEFAULT_THROWABLEMAX);
    }

    /**
     * Javelin�̏��擾�Ώۂ��珜�O����A�ő��Bytecode����Ԃ��B
     *
     * @return Javelin�̏��擾�Ώۂ��珜�O����A�ő��Bytecode���B
     */
    public int getBytecodeLengthMax()
    {
        return CONFIGUTIL.getInteger(BYTECODE_EXCLUDE_LENGTH_KEY, DEFAULT_BYTECODE_LENGTH_MIN_KEY);
    }

    /**
     * Javelin�̏��擾�Ώۂ��珜�O����A�ő�̐��䖽�ߐ���Ԃ��B
     *
     * @return Javelin�̏��擾�Ώۂ��珜�O����A�ő�̐��䖽�ߐ��B
     */
    public int getBytecodeControlCountMax()
    {
        return CONFIGUTIL.getInteger(BYTECODE_EXCLUDE_CONTROLCOUNT_MAX_KEY,
                                     DEFAULT_BYTECODE_EXCLUDE_CONTROLCOUNT_MAX_KEY);
    }

    /**
     * Bytecode�̓��e�����Ɍv���Ώۂ��珜�O����ۂ̃|���V�[���擾����B 
     * 
     * @return Bytecode�̓��e�����Ɍv���Ώۂ��珜�O����ۂ̃|���V�[�B
     */
    public int getByteCodeExcludePolicy()
    {
        return CONFIGUTIL.getInteger(BYTECODE_EXCLUDE_POLICY_KEY,
                                     DEFAULT_BYTECODE_EXCLUDE_POLICY_KEY);
    }

    /**
     * �Ăяo�������L�^����ۂ�臒l���ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetThrowableMax()
    {
        return isKeyExist(THROWABLEMAX_KEY);
    }

    /**
     * ��O�̔����������L�^����ő匏�����Z�b�g����B
     *
     * @param throwableMax ����
     */
    public void setThrowableMax(final int throwableMax)
    {
        CONFIGUTIL.setInteger(THROWABLEMAX_KEY, throwableMax);
    }

    /**
     * �Ăяo����ɂ��閼�̂�Ԃ��B
     *
     * @return ����
     */
    public String getEndCalleeName()
    {
        return CONFIGUTIL.getString(ENDCALLEENAME_KEY, DEFAULT_ENDCALLEENAME);
    }

    /**
     * �Ăяo����ɂ��閼�̂��ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetEndCalleeName()
    {
        return isKeyExist(ENDCALLEENAME_KEY);
    }

    /**
     * �Ăяo����ɂ��閼�̂��Z�b�g����B
     *
     * @param endCalleeName ����
     */
    public void setEndCalleeName(final String endCalleeName)
    {
        CONFIGUTIL.setString(ENDCALLEENAME_KEY, endCalleeName);
    }

    /**
     * �Ăяo�����ɂ��閼�̂�Ԃ��B
     *
     * @return ����
     */
    public String getRootCallerName()
    {
        return CONFIGUTIL.getString(ROOTCALLERNAME_KEY, DEFAULT_ROOTCALLERNAME);
    }

    /**
     * �Ăяo�����ɂ��閼�̂��ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetRootCallerName()
    {
        return isKeyExist(ROOTCALLERNAME_KEY);
    }

    /**
     * �Ăяo�����ɂ��閼�̂��Z�b�g����B
     *
     * @param rootCallerName ����
     */
    public void setRootCallerName(final String rootCallerName)
    {
        CONFIGUTIL.setString(ROOTCALLERNAME_KEY, rootCallerName);
    }

    /**
     * �X�^�b�N�g���[�X���o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �X�^�b�N�g���[�X���o�͂���Ȃ�true
     */
    public boolean isLogStacktrace()
    {
        return CONFIGUTIL.getBoolean(LOG_STACKTRACE_KEY, DEFAULT_LOG_STACKTRACE);
    }

    /**
     * �X�^�b�N�g���[�X���o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetLogStacktrace()
    {
        return isKeyExist(LOG_STACKTRACE_KEY);
    }

    /**
     * �X�^�b�N�g���[�X���o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogStacktrace �X�^�b�N�g���[�X���o�͂���Ȃ�true
     */
    public void setLogStacktrace(final boolean isLogStacktrace)
    {
        CONFIGUTIL.setBoolean(LOG_STACKTRACE_KEY, isLogStacktrace);
    }

    /**
     * �������o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �������o�͂���Ȃ�true
     */
    public boolean isLogArgs()
    {
        return CONFIGUTIL.getBoolean(LOG_ARGS_KEY, DEFAULT_LOG_ARGS);
    }

    /**
     * HTTP�Z�b�V�������o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return HTTP�Z�b�V�������o�͂���Ȃ�true
     */
    public boolean isLogHttpSession()
    {
        return CONFIGUTIL.getBoolean(LOG_HTTP_SESSION_KEY, DEFAULT_LOG_HTTP_SESSION);
    }

    /**
     * �X���b�h�R���e���V�����Ď����s�����ǂ����̐ݒ��Ԃ��B
     * 
     * @return �X���b�h�R���e���V�����Ď����s�����ǂ����B
     */
    public boolean isThreadContentionMonitor()
    {

        return CONFIGUTIL.getBoolean(THREAD_CONTENTION_KEY, DEFAULT_THREAD_CONTENTION);
    }

    /**
     * MBean�ɂ���Ď擾���������o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return MBean�ɂ���Ď擾���������o�͂���Ȃ�true
     */
    public boolean isLogMBeanInfo()
    {
        return CONFIGUTIL.getBoolean(LOG_MBEANINFO_KEY, DEFAULT_LOG_MBEANINFO);
    }

    /**
     * �[�_�ŁAMBean�ɂ���Ď擾���������o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �[�_�ŁAMBean�ɂ���Ď擾���������o�͂���Ȃ�true
     */
    public boolean isLogMBeanInfoRoot()
    {
        return CONFIGUTIL.getBoolean(LOG_MBEANINFO_ROOT_KEY, DEFAULT_LOG_MBEANINFO_ROOT);
    }

    /**
     * �������o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetLogArgs()
    {
        return isKeyExist(LOG_ARGS_KEY);
    }

    /**
     * �������o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogArgs �������o�͂���Ȃ�true
     */
    public void setLogArgs(final boolean isLogArgs)
    {
        CONFIGUTIL.setBoolean(LOG_ARGS_KEY, isLogArgs);
    }

    /**
     * �Z�b�V�����������Ƃ��ďo�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogArgs �������o�͂���Ȃ�true
     */
    public void setLogHttpSession(final boolean isLogArgs)
    {
        CONFIGUTIL.setBoolean(LOG_HTTP_SESSION_KEY, isLogArgs);
    }

    /**
     * MBean�ɂ���Ď擾���������o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogMBeanInfo MBean�ɂ���Ď擾���������o�͂���Ȃ�true
     */
    public void setLogMBeanInfo(final boolean isLogMBeanInfo)
    {
        CONFIGUTIL.setBoolean(LOG_MBEANINFO_KEY, isLogMBeanInfo);
    }

    /**
     * MBean�ɂ���Ď擾�������i���[�g�m�[�h�j���o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogMBeanInfo MBean�ɂ���Ď擾���������o�͂���Ȃ�true
     */
    public void setLogMBeanInfoRoot(final boolean isLogMBeanInfo)
    {
        CONFIGUTIL.setBoolean(LOG_MBEANINFO_ROOT_KEY, isLogMBeanInfo);
    }

    /**
     * �߂�l���o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �߂�l���o�͂���Ȃ�true
     */
    public boolean isLogReturn()
    {
        return CONFIGUTIL.getBoolean(LOG_RETURN_KEY, DEFAULT_LOG_RETURN);
    }

    /**
     * �߂�l���o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetLogReturn()
    {
        return isKeyExist(LOG_RETURN_KEY);
    }

    /**
     * �߂�l���o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isLogReturn �߂�l���o�͂���Ȃ�true
     */
    public void setLogReturn(final boolean isLogReturn)
    {
        CONFIGUTIL.setBoolean(LOG_RETURN_KEY, isLogReturn);
    }

    /**
     * �����̏ڍׂ��o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �����̏ڍׂ��o�͂���Ȃ�true
     */
    public boolean isArgsDetail()
    {
        return CONFIGUTIL.getBoolean(ARGS_DETAIL_KEY, DEFAULT_ARGS_DETAIL);
    }

    /**
     * �����̏ڍׂ��o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetArgsDetail()
    {
        return isKeyExist(ARGS_DETAIL_KEY);
    }

    /**
     * �����̏ڍׂ��o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isArgsDetail �����̏ڍׂ��o�͂���Ȃ�true
     */
    public void setArgsDetail(final boolean isArgsDetail)
    {
        CONFIGUTIL.setBoolean(ARGS_DETAIL_KEY, isArgsDetail);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return �߂�l�̏ڍׂ��o�͂���Ȃ�true
     */
    public boolean isReturnDetail()
    {
        return CONFIGUTIL.getBoolean(RETURN_DETAIL_KEY, DEFAULT_RETURN_DETAIL);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetReturnDetail()
    {
        return isKeyExist(RETURN_DETAIL_KEY);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isReturnDetail �߂�l�̏ڍׂ��o�͂���Ȃ�true
     */
    public void setReturnDetail(final boolean isReturnDetail)
    {
        CONFIGUTIL.setBoolean(RETURN_DETAIL_KEY, isReturnDetail);
    }

    /**
     * �����̏ڍׂ��o�͂���K�w���̐ݒ��Ԃ��B
     *
     * @return �����̏ڍׂ��o�͂���K�w��
     */
    public int getArgsDetailDepth()
    {
        return CONFIGUTIL.getInteger(ARGS_DETAIL_DEPTH_KEY, DEFAULT_ARGS_DETAIL_DEPTH);
    }

    /**
     * �����̏ڍׂ��o�͂���K�w�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetArgsDetailDepth()
    {
        return isKeyExist(ARGS_DETAIL_DEPTH_KEY);
    }

    /**
     * �����̏ڍׂ��o�͂���K�w����ݒ肷��B
     *
     * @param detailDepth �����̏ڍׂ��o�͂���K�w��
     */
    public void setArgsDetailDepth(final int detailDepth)
    {
        CONFIGUTIL.setInteger(ARGS_DETAIL_DEPTH_KEY, detailDepth);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂���K�w���̐ݒ��Ԃ��B
     *
     * @return �ڍׂ��o�͂���K�w��
     */
    public int getReturnDetailDepth()
    {
        return CONFIGUTIL.getInteger(RETURN_DETAIL_DEPTH_KEY, DEFAULT_RETURN_DETAIL_DEPTH);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂���K�w�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetReturnDetailDepth()
    {
        return isKeyExist(RETURN_DETAIL_DEPTH_KEY);
    }

    /**
     * �߂�l�̏ڍׂ��o�͂���K�w����ݒ肷��B
     *
     * @param returnDetailDepth �߂�l�̏ڍׂ��o�͂���K�w��
     */
    public void setReturnDetailDepth(final int returnDetailDepth)
    {
        CONFIGUTIL.setInteger(RETURN_DETAIL_DEPTH_KEY, returnDetailDepth);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂��邩�ǂ����̐ݒ��Ԃ��B
     *
     * @return HTTP�Z�b�V�����̏ڍׂ��o�͂���Ȃ�true
     */
    public boolean isHttpSessionDetail()
    {
        return CONFIGUTIL.getBoolean(HTTP_SESSION_DETAIL_KEY, DEFAULT_HTTP_SESSION_DETAIL);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂��邩�ǂ������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetHttpSessionDetail()
    {
        return isKeyExist(HTTP_SESSION_DETAIL_KEY);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂��邩�ǂ�����ݒ肷��B
     *
     * @param isHttpSessionDetail HTTP�Z�b�V�����̏ڍׂ��o�͂���Ȃ�true
     */
    public void setHttpSessionDetail(final boolean isHttpSessionDetail)
    {
        CONFIGUTIL.setBoolean(HTTP_SESSION_DETAIL_KEY, isHttpSessionDetail);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂���K�w���̐ݒ��Ԃ��B
     *
     * @return HTTP�Z�b�V�����̏ڍׂ��o�͂���K�w��
     */
    public int getHttpSessionDetailDepth()
    {
        return CONFIGUTIL.getInteger(HTTP_SESSION_DETAIL_DEPTH_KEY,
                                     DEFAULT_HTTP_SESSION_DETAIL_DEPTH);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂���K�w�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetHttpSessionDetailDepth()
    {
        return isKeyExist(HTTP_SESSION_DETAIL_DEPTH_KEY);
    }

    /**
     * HTTP�Z�b�V�����̏ڍׂ��o�͂���K�w����ݒ肷��B
     *
     * @param detailDepth HTTP�Z�b�V�����̏ڍׂ��o�͂���K�w��
     */
    public void setHttpSessionDetailDepth(final int detailDepth)
    {
        CONFIGUTIL.setInteger(HTTP_SESSION_DETAIL_DEPTH_KEY, detailDepth);
    }

    /**
     * �X���b�h���f����Ԃ��B
     *
     * @return �X���b�h���f��
     */
    public int getThreadModel()
    {
        return CONFIGUTIL.getInteger(THREADMODEL_KEY, DEFAULT_THREADMODEL);
    }

    /**
     * �X���b�h���f�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetThreadModel()
    {
        return isKeyExist(THREADMODEL_KEY);
    }

    /**
     * �X���b�h���f�����Z�b�g����B
     *
     * @param threadModel �X���b�h���f��
     */
    public void setThreadModel(final int threadModel)
    {
        CONFIGUTIL.setInteger(THREADMODEL_KEY, threadModel);
    }

    /**
     * �L�[�ɑΉ�����l���Z�b�g����Ă��邩�ǂ����𒲂ׂ�B
     *
     * @param key �L�[
     * @return �l���Z�b�g����Ă����true
     */
    private boolean isKeyExist(final String key)
    {
        return CONFIGUTIL.isKeyExist(key);
    }

    /**
     * �������ɕۑ�����臒l���擾����B
     * @return �������ɕۑ�����臒l
     */
    public long getStatisticsThreshold()
    {
        return CONFIGUTIL.getLong(STATISTICSTHRESHOLD_KEY, DEFAULT_STATISTICSTHRESHOLD);
    }

    /**
     * �������ɕۑ�����臒l��ݒ肷��B
     * @param statisticsThreshold �������ɕۑ�����臒l
     */
    public void setStatisticsThreshold(final long statisticsThreshold)
    {
        CONFIGUTIL.setLong(STATISTICSTHRESHOLD_KEY, statisticsThreshold);
    }

    /**
     * ���O�ɏo�͂���Args�̒�����臒l���擾����B
     * @return Args�̒�����臒l
     */
    public int getStringLimitLength()
    {
        return CONFIGUTIL.getInteger(STRINGLIMITLENGTH_KEY, DEFAULT_STRINGLIMITLENGTH);
    }

    /**
     * ���O�ɏo�͂���Args�̒�����臒l��ݒ肷��B
     * @param stringLimitLength Args�̒�����臒l
     */
    public void setStringLimitLength(final int stringLimitLength)
    {
        CONFIGUTIL.setLong(STRINGLIMITLENGTH_KEY, stringLimitLength);
    }

    /**
     * �҂������|�[�g�ԍ���Ԃ��B
     *
     * @return �|�[�g�ԍ�
     */
    public int getAcceptPort()
    {
        return CONFIGUTIL.getInteger(ACCEPTPORT_KEY, DEFAULT_ACCEPTPORT);
    }

    /**
     * �ڑ��z�X�g����Ԃ��B
     *
     * @return �z�X�g��
     */
    public String getConnectHost()
    {
        return CONFIGUTIL.getString(CONNECTHOST_KEY, DEFAULT_CONNECTHOST);
    }

    /**
     * �ڑ��|�[�g�ԍ���Ԃ��B
     *
     * @return �|�[�g�ԍ�
     */
    public int getConnectPort()
    {
        return CONFIGUTIL.getInteger(CONNECTPORT_KEY, DEFAULT_CONNECTPORT);
    }

    /**
     * �f�[�^�x�[�X����Ԃ��B
     *
     * @return �f�[�^�x�[�X��
     */
    public String getAgentName()
    {
        return CONFIGUTIL.getString(AGENTNAME_KEY, DEFAULT_AGENTNAME);
    }

    /**
     * Javelin�V�X�e�����O�̏o�͐�f�B���N�g����Ԃ��B
     *
     * @return Javelin�V�X�e�����O�̏o�͐�f�B���N�g���B
     */
    public String getSystemLog()
    {
        String relativePath = CONFIGUTIL.getString(SYSTEMLOG_KEY, DEFAULT_SYSTEMLOG);
        if (isSysLogDirInit__ == false)
        {
            if (isAcceptPortIsRange())
            {
                relativePath = relativePath + File.separator + logFolderName__;
                setSystemLog(relativePath);
            }
            isSysLogDirInit__ = true;
        }
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * Javelin�V�X�e�����O�̏o�͐�f�B���N�g����ݒ肵�܂��B<br />
     *
     * @param sysLogDir Javelin�V�X�e�����O�̏o�͐�f�B���N�g���B
     */
    public void setSystemLog(final String sysLogDir)
    {
        CONFIGUTIL.setString(SYSTEMLOG_KEY, sysLogDir);
    }

    /**
     * HeapDump�t�@�C���̏o�͐�f�B���N�g����Ԃ��B
     *
     * @return HeapDump�t�@�C���̏o�͐�f�B���N�g���B
     */
    public String getHeapDumpDir()
    {
        String relativePath = CONFIGUTIL.getString(HEAPDUMPDIR_KEY, DEFAULT_HEAPDUMP_DIR);
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * ���p����AlarmListener����Ԃ��B
     * ","��؂�ŕ����w�肷�邱�Ƃ��ł���B
     *
     * @return ���p����AlarmListener��
     */
    public String getAlarmListeners()
    {
        return CONFIGUTIL.getString(ALARM_LISTENERS_KEY, DEFAULT_ALARM_LISTENERS);
    }

    /**
     * ���p����AlarmListener�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetAlarmListeners()
    {
        return isKeyExist(ALARM_LISTENERS_KEY);
    }

    /**
     * ���p����AlarmListener�����Z�b�g����B
     * ","��؂�ŕ����w�肷�邱�Ƃ��ł���B
     *
     * @param alarmListeners ���p����AlarmListener��
     */
    public void setAlarmListeners(final String alarmListeners)
    {
        CONFIGUTIL.setString(ALARM_LISTENERS_KEY, alarmListeners);
    }

    /**
     * ���O�T�C�Y�̍ő�l���擾����B
     * @return ���O�T�C�Y�̍ő�l
     */
    public int getLogJvnMax()
    {
        return CONFIGUTIL.getInteger(LOG_JVN_MAX_KEY, DEFAULT_LOG_JVN_MAX);
    }

    /**
     * Zip�����郍�O�̃t�@�C�������擾����B
     * @return Zip�����郍�O�̃t�@�C����
     */
    public int getLogZipMax()
    {
        return CONFIGUTIL.getInteger(LOG_ZIP_MAX_KEY, DEFAULT_LOG_ZIP_MAX);
    }

    /**
     * Jvn���O�t�@�C�����o�͂��邩�ǂ�����Ԃ��B
     * @return ���O�t�@�C�����o�͂��邩�ǂ���
     */
    public boolean isLogJvnFile()
    {
        return CONFIGUTIL.getBoolean(LOG_JVN_FILE, DEFAULT_LOG_JVN_FILE);
    }

    /**
     * Jvn���O�t�@�C�����o�͂��邩�ǂ�����ݒ肷��B
     * @param logJvnFile ���O�t�@�C�����o�͂��邩�ǂ���
     */
    public void setLogJvnFile(final boolean logJvnFile)
    {
        CONFIGUTIL.setBoolean(LOG_JVN_FILE, logJvnFile);
    }

    /**
     * ���O��Zip�����邩�ǂ�����Ԃ��B
     * @return true:���O��Zip������Afalse:���O��Zip�����Ȃ��B
     */
    public boolean isLogZipMax()
    {
        return isKeyExist(LOG_ZIP_MAX_KEY);
    }

    /**
     * �L�^��������N���X����Ԃ�
     *
     * @return �N���X��
     */
    public String getRecordStrategy()
    {
        return CONFIGUTIL.getString(RECORDSTRATEGY_KEY, DEFAULT_RECORDSTRATEGY);
    }

    /**
     * �L�^��������N���X�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isRecordStrategy()
    {
        return isKeyExist(RECORDSTRATEGY_KEY);
    }

    /**
     * ���p����TelegramListener����Ԃ��B
     * ","��؂�ŕ����w�肷�邱�Ƃ��ł���B
     *
     * @return ���p����TelegramListener��
     */
    public String getTelegramListeners()
    {
        return CONFIGUTIL.getString(TELERAM_LISTENERS_KEY, DEFAULT_TELEGEAM_LISTENERS);
    }

    /**
     * ���p����TelegramListener�����ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return �ݒ肳��Ă����true
     */
    public boolean isSetTelegramListener()
    {
        return isKeyExist(TELERAM_LISTENERS_KEY);
    }

    /**
     * Javelin�̃V�X�e�����O�̍ő�t�@�C�������擾����B
     *
     * @return Javelin�̃V�X�e�����O�̍ő�t�@�C�����B
     */
    public int getSystemLogNumMax()
    {
        return CONFIGUTIL.getInteger(SYSTEM_LOG_NUM_MAX_KEY, DEFAULT_SYSTEM_LOG_NUM_MAX);
    }

    /**
     * MBeanManager���������V���A���C�Y����t�@�C������Ԃ��B
     *
     * @return ���p����t�@�C����
     */
    public String getSerializeFile()
    {
        String relativePath = CONFIGUTIL.getString(SERIALIZE_FILE_KEY, DEFAULT_SERIALIZE_FILE);
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * MBeanManager���������V���A���C�Y����t�@�C�������ݒ肳��Ă��邩�ǂ����𒲂ׂ�B
     *
     * @return ���p����t�@�C����
     */
    public boolean isSetSerializeFile()
    {
        return isKeyExist(SERIALIZE_FILE_KEY);
    }

    /**
     * Javelin�̃V�X�e�����O�̍ő�t�@�C���T�C�Y���擾����B
     *
     * @return Javelin�̃V�X�e�����O�̍ő�t�@�C���T�C�Y�B
     */
    public int getSystemLogSizeMax()
    {
        return CONFIGUTIL.getInteger(SYSTEM_LOG_SIZE_MAX_KEY, DEFAULT_SYSTEM_LOG_SIZE_MAX);
    }

    /**
     * �V�X�e�����O�̃��x�����擾����B
     * @return �V�X�e�����O�̃��x��
     */
    public String getSystemLogLevel()
    {
        return CONFIGUTIL.getLogLevel(SYSTEM_LOG_LEVEL_KEY, DEFAULT_SYSTEM_LOG_LEVEL);
    }

    /**
     * Javelin�̃C�x���g���x�����擾���܂��B
     * @return Javelin�̃C�x���g���x��
     */
    public String getEventLevel()
    {
        return CONFIGUTIL.getEventLevel(EVENT_LEVEL_KEY, DEFAULT_EVENT_LEVEL).toUpperCase();
    }

    /**
     * Javelin�̃C�x���g���x����ݒ肵�܂��B
     * @param eventLevel Javelin�̃C�x���g���x��
     */
    public void setEventLevel(final String eventLevel)
    {
        CONFIGUTIL.setString(EVENT_LEVEL_KEY, eventLevel);
    }

    /**
     * CallTree�̍ő�l���擾����B
     * @return CallTree�̍ő�l
     */
    public int getCallTreeMax()
    {
        return CONFIGUTIL.getInteger(CALL_TREE_MAX_KEY, DEFAULT_CALL_TREE_MAX);
    }

    /**
     * CallTree�̍ő�l��ݒ肷��B
     * @param callTreeMax CallTree�̍ő�l
     */
    public void setCallTreeMax(final int callTreeMax)
    {
        CONFIGUTIL.setInteger(CALL_TREE_MAX_KEY, callTreeMax);
    }

    /**
     * �A�v���P�[�V�������s���ɗ�O���A���[���ʒm���邩�ǂ����B
     * @return true:�A���[���ʒm����Afalse:�A���[���ʒm���Ȃ��B
     */
    public boolean isAlarmException()
    {
        return CONFIGUTIL.getBoolean(ALARM_EXCEPTION_KEY, DEFAULT_ALARM_EXCEPTION);
    }

    /**
     * �A�v���P�[�V�������s���ɗ�O���A���[���ʒm���邩�ǂ����ݒ肷��B
     *
     * @param isAlarmException ��O���A���[���ʒm����Ȃ�true
     */
    public void setAlarmException(final boolean isAlarmException)
    {
        CONFIGUTIL.setBoolean(ALARM_EXCEPTION_KEY, isAlarmException);
    }

    /**
     * �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔���擾����B
     * @return �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔
     */
    public int getRecordInvocationMax()
    {
        return CONFIGUTIL.getInteger(RECORD_INVOCATION_MAX_KEY, DEFAULT_REC_INVOCATION_MAX);
    }

    /**
     * �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔���擾����B
     * @param recInvocationMax �P�N���X�ӂ�ێ�����Invocation�i���\�b�h�Ăяo���j�ő吔
     */
    public void setRecordInvocationMax(final int recInvocationMax)
    {
        CONFIGUTIL.setInteger(RECORD_INVOCATION_MAX_KEY, recInvocationMax);
    }

    /**
     * �A���[�����M�Ԋu�̍ŏ��l���擾����B
     * 
     * �O��A���[�����M�EJavelin���O�o�͂��s�����ۂ���
     * �o�߂������Ԃ����̍ŏ��l�𒴂��Ă����ꍇ�̂݁A�A���[�����M�EJavelin���O�o�͂��s���B
     * @return �A���[�����M�Ԋu�̍ŏ��l�B
     */
    public long getAlarmMinimumInterval()
    {
        return CONFIGUTIL.getLong(ALARM_MINIMUM_INTERVAL_KEY, DEFAULT_ALARM_MINIMUM_INTERVAL);
    }

    /**
     * ����̃C�x���g�����o����Ԋu���擾����B
     * 
     * @return ����̃C�x���g�����o����Ԋu�B
     */
    public long getEventInterval()
    {
        return CONFIGUTIL.getLong(EVENT_INTERVAL_KEY, DEFAULT_EVENT_INTERVAL);
    }

    /**
     * �A���[�����M�Ԋu�̍ŏ��l��ݒ肷��B
     * 
     * �O��A���[�����M�EJavelin���O�o�͂��s�����ۂ���
     * �o�߂������Ԃ����̍ŏ��𒴂��Ă����ꍇ�̂݁A�A���[�����M�EJavelin���O�o�͂��s���B
     * 
     * @param alarmMinimumInterval 臒l�B
     */
    public void setAlarmMinimumInterval(final long alarmMinimumInterval)
    {
        CONFIGUTIL.setLong(ALARM_MINIMUM_INTERVAL_KEY, alarmMinimumInterval);
    }

    /**
     * Turn Around Time���v�����邩�ǂ�����ݒ肷��B
     *
     * @param tatEnabled Turn Around Time���v������Ȃ�true
     */
    public void setTatEnabled(final boolean tatEnabled)
    {
        CONFIGUTIL.setBoolean(TAT_ENABLED_KEY, tatEnabled);
    }

    /**
     * Turn Around Time���v�����邩�ǂ����B
     * @return true:�v������Afalse:�v�����Ȃ��B
     */
    public boolean isTatEnabled()
    {
        return CONFIGUTIL.getBoolean(TAT_ENABLED_KEY, DEFAULT_TAT_ENABLED);
    }

    /**
     * Turn Around Time�̕ێ����Ԃ��Z�b�g����B
     *
     * @param tatKeepTime Turn Around Time�̕ێ�����
     */
    public void setTatKeepTime(final long tatKeepTime)
    {
        CONFIGUTIL.setLong(TAT_KEEP_TIME_KEY, tatKeepTime);
    }

    /**
     * Turn Around Time�̕ێ����Ԃ��擾����B
     * 
     * @return Turn Around Time�̕ێ�����
     */
    public long getTatKeepTime()
    {
        return CONFIGUTIL.getLong(TAT_KEEP_TIME_KEY, DEFAULT_TAT_KEEP_TIME);
    }

    /**
     * Turn Around Time�̒l���@0�@�̏ꍇ�ɁA�@0�@�̏o�͂��p�����鎞�Ԃ��Z�b�g����B
     *
     * @param tatZeroKeepTime Turn Around Time�̒l���@0�@�̏ꍇ�ɁA�@0�@�̏o�͂��p�����鎞��
     */
    public void setTatZeroKeepTime(final long tatZeroKeepTime)
    {
        CONFIGUTIL.setLong(TAT_ZERO_KEEP_TIME_KEY, tatZeroKeepTime);
    }

    /**
     * Turn Around Time�̒l���@0�@�̏ꍇ�ɁA�@0�@�̏o�͂��p�����鎞�Ԃ��擾����B
     *
     * @return Turn Around Time�̒l���@0�@�̏ꍇ�ɁA�@0�@�̏o�͂��p�����鎞��
     */
    public long getTatZeroKeepTime()
    {
        return CONFIGUTIL.getLong(TAT_ZERO_KEEP_TIME_KEY, DEFAULT_TAT_ZERO_KEEP_TIME);
    }

    /**
     * �l�b�g���[�N���͗ʂ��擾���邩�ǂ������擾����B
     * 
     * @return �l�b�g���[�N���͗ʂ��擾���邩�ǂ���
     */
    public boolean isNetInputMonitor()
    {
        return CONFIGUTIL.getBoolean(NET_INPUT_MONITOR, DEF_NET_INPUT_MONITOR);
    }

    /**
     * �l�b�g���[�N���͗ʂ��擾���邩�ǂ�����ݒ肷��B
     * 
     * @param isNetInputMonitor �l�b�g���[�N���͗ʂ��擾���邩�ǂ���
     */
    public void setNetInputMonitor(final boolean isNetInputMonitor)
    {
        CONFIGUTIL.setBoolean(NET_INPUT_MONITOR, isNetInputMonitor);
    }

    /**
     * �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ������擾����B
     * 
     * @return �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ���
     */
    public boolean isNetOutputMonitor()
    {
        return CONFIGUTIL.getBoolean(NET_OUTPUT_MONITOR, DEF_NET_OUTPUT_MONITOR);
    }

    /**
     * �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ�����ݒ肷��B
     * 
     * @param isNetOutputMonitor �l�b�g���[�N�o�͗ʂ��擾���邩�ǂ���
     */
    public void setNetOutputMonitor(final boolean isNetOutputMonitor)
    {
        CONFIGUTIL.setBoolean(NET_OUTPUT_MONITOR, isNetOutputMonitor);
    }

    /**
     * �t�@�C�����͗ʂ��擾���邩�ǂ������擾����B
     * 
     * @return �t�@�C�����͗ʂ��擾���邩�ǂ���
     */
    public boolean isFileInputMonitor()
    {
        return CONFIGUTIL.getBoolean(FILE_INPUT_MONITOR, DEF_FILE_INPUT_MONITOR);
    }

    /**
     * �t�@�C�����͗ʂ��擾���邩�ǂ�����ݒ肷��B
     * 
     * @param isFileInputMonitor �t�@�C�����͗ʂ��擾���邩�ǂ���
     */
    public void setFileInputMonitor(final boolean isFileInputMonitor)
    {
        CONFIGUTIL.setBoolean(FILE_INPUT_MONITOR, isFileInputMonitor);
    }

    /**
     * �t�@�C���o�͗ʂ��擾���邩�ǂ������擾����B
     * 
     * @return �t�@�C���o�͗ʂ��擾���邩�ǂ���
     */
    public boolean isFileOutputMonitor()
    {
        return CONFIGUTIL.getBoolean(FILE_OUTPUT_MONITOR, DEF_FILE_OUTPUT_MONITOR);
    }

    /**
     * �t�@�C���o�͗ʂ��擾���邩�ǂ�����ݒ肷��B
     * 
     * @param isFileOutputMonitor �t�@�C���o�͗ʂ��擾���邩�ǂ���
     */
    public void setFileOutputMonitor(final boolean isFileOutputMonitor)
    {
        CONFIGUTIL.setBoolean(FILE_OUTPUT_MONITOR, isFileOutputMonitor);
    }

    /**
     * �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ������擾����B
     * 
     * @return �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ���
     */
    public boolean isFinalizationCount()
    {
        return CONFIGUTIL.getBoolean(FINALIZATION_COUNT_MONITOR, DEF_FINALIZATION_COUNT_MONITOR);
    }

    /**
     * �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ�����ݒ肷��B
     * 
     * @param isFinalizationCount �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾���邩�ǂ���
     */
    public void setFinalizationCount(final boolean isFinalizationCount)
    {
        CONFIGUTIL.setBoolean(FINALIZATION_COUNT_MONITOR, isFinalizationCount);
    }

    /**
     * ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ������擾����B
     * 
     * @return ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ���
     */
    public boolean isIntervalMonitor()
    {
        return CONFIGUTIL.getBoolean(INTERVAL_ERROR_MONITOR, DEF_INTERVAL_ERROR_MONITOR);
    }

    /**
     * ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ�����ݒ肷��B
     * 
     * @param isIntervalMonitor ���\�b�h�Ăяo���Ԋu���߂��Ď����邩�ǂ���
     */
    public void setIntervalMonitor(final boolean isIntervalMonitor)
    {
        CONFIGUTIL.setBoolean(INTERVAL_ERROR_MONITOR, isIntervalMonitor);
    }

    /**
     * HttpSession�̃C���X�^���X�����Ď����邩�ǂ������擾����B
     * 
     * @return HttpSession�̃C���X�^���X�����Ď����邩�ǂ���
     */
    public boolean isHttpSessionCount()
    {
        return CONFIGUTIL.getBoolean(HTTP_SESSION_COUNT_MONITOR, DEF_HTTP_SESSION_COUNT_MONITOR);
    }

    /**
     * HttpSession�̃C���X�^���X�����Ď����邩�ǂ�����ݒ肷��B
     * 
     * @param isHttpSessionCount HttpSession�̃C���X�^���X�����Ď����邩�ǂ���
     */
    public void setHttpSessionCount(final boolean isHttpSessionCount)
    {
        CONFIGUTIL.setBoolean(HTTP_SESSION_COUNT_MONITOR, isHttpSessionCount);
    }

    /**
     * HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩�ǂ������擾����B
     * 
     * @return HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩�ǂ���
     */
    public boolean isHttpSessionSize()
    {
        return CONFIGUTIL.getBoolean(HTTP_SESSION_SIZE_MONITOR, DEF_HTTP_SESSION_SIZE_MONITOR);
    }

    /**
     * HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩�ǂ�����ݒ肷��B
     * 
     * @param isHttpSessionSize HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y���Ď����邩�ǂ���
     */
    public void setHttpSessionSize(final boolean isHttpSessionSize)
    {
        CONFIGUTIL.setBoolean(HTTP_SESSION_SIZE_MONITOR, isHttpSessionSize);
    }

    /**
     * Javelin�̐ݒ�I�u�W�F�N�g���쐬����B Javelin�N�����ɂ̂݌Ăяo�����B
     * 
     * @param absoluteJarDirectory Jar�����݂����΃p�X
     */
    public JavelinConfigBase(final String absoluteJarDirectory)
    {
        CONFIGUTIL.setAbsoluteJarDirectory(absoluteJarDirectory);
    }

    /**
     * ���O�o�͂��珜�O����Ώۂ��L�q�����t�B���^�t�@�C������Ԃ��B
     * 
     * @return �t�@�C����
     */
    public String getExclude()
    {
        String relativePath = CONFIGUTIL.getString(EXCLUDE, DEF_EXCLUDE);
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * ���O�o�͂�����Ώۂ��L�q�����t�B���^�t�@�C������Ԃ��B
     * 
     * @return �t�@�C����
     */
    public String getInclude()
    {
        String relativePath = CONFIGUTIL.getString(INCLUDE, DEF_INCLUDE);
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * JVN�t�@�C���_�E�����[�h���̍ő�o�C�g����Ԃ��B
     * 
     * @return JVN�t�@�C���_�E�����[�h���̍ő�o�C�g���B
     */
    public int getJvnDownloadMax()
    {
        return CONFIGUTIL.getInteger(JVN_DOWNLOAD_MAX, DEF_JVN_DOWNLOAD_MAX);
    }

    /**
     * JVN�t�@�C���_�E�����[�h���̍ő�o�C�g����Ԃ��B
     * 
     * @param jvnDownloadMax JVN�t�@�C���_�E�����[�h���̍ő�o�C�g���B
     */
    public void setJvnDownloadMax(final int jvnDownloadMax)
    {
        CONFIGUTIL.setInteger(JVN_DOWNLOAD_MAX, jvnDownloadMax);
    }

    /**
     * "javelin." �ŊJ�n����I�v�V�������㏑������B
     * 
     * @param properties �I�v�V�������X�g
     */
    public void overwriteProperty(final Properties properties)
    {
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements())
        {
            String propertyName = (String)propertyNames.nextElement();
            if (propertyName.startsWith(JAVELIN_PREFIX))
            {
                String propertyValue = properties.getProperty(propertyName);
                CONFIGUTIL.setString(propertyName, propertyValue);
            }
        }
    }

    /**
     * �x���𔭐�������CPU���Ԃ�臒l��ݒ肷��B
     * 
     * @param cpuTime CPU����
     */
    public void setAlarmCpuThreashold(final long cpuTime)
    {
        CONFIGUTIL.setLong(ALARM_CPUTHRESHOLD, cpuTime);
    }

    /**
     * �x���𔭐�������CPU���Ԃ�臒l���擾����B
     * 
     * @return �x���𔭐�������CPU���Ԃ�臒l
     */
    public long getAlarmCpuThreashold()
    {
        return CONFIGUTIL.getLong(ALARM_CPUTHRESHOLD, DEF_ALARM_CPUTHRESHOLD);
    }

    /**
     * ���C�Z���X�t�@�C���p�X��Ԃ��B
     * 
     * @return ���C�Z���X�t�@�C���p�X�B
     */
    public String getLicensePath()
    {
        String relativePath = CONFIGUTIL.getString(LICENSEPATH, DEF_LICENSEPATH);
        return CONFIGUTIL.convertRelativePathtoAbsolutePath(relativePath);
    }

    /**
     * �N���X����P�������邩�ǂ����̃t���O�B
     * 
     * @return �P��������ꍇ��true�B
     */
    public boolean isClassNameSimplify()
    {
        boolean result = CONFIGUTIL.getBoolean(CLASSNAME_SIMPLIFY, DEF_CLASSNAME_SIMPLIFY);
        return result;
    }

    /**
     * Collection�̃��������[�N���o���s�����ǂ�����ݒ肵�܂��B<br />
     * 
     * @param collectionMonitor Collection�̃��������[�N���o���s���ꍇ�A<code>true</code>
     */
    public void setCollectionMonitor(final boolean collectionMonitor)
    {
        CONFIGUTIL.setBoolean(COLLECTION_MONITOR, collectionMonitor);
    }

    /**
     * Collection�̃��������[�N���o���s�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return Collection�̃��������[�N���o���s���ꍇ�A<code>true</code>
     */
    public boolean isCollectionMonitor()
    {
        return CONFIGUTIL.getBoolean(COLLECTION_MONITOR, DEF_COLLECTION_MONITOR);
    }

    /**
     * Collection�AMap�̃T�C�Y���L�^����臒l��ݒ肷��B
     * 
     * @param collectionSizeThreshold COLLECTION_SIZE_THRESHOLD
     */
    public void setCollectionSizeThreshold(final int collectionSizeThreshold)
    {
        CONFIGUTIL.setInteger(COLLECTION_SIZE_THRESHOLD, collectionSizeThreshold);
    }

    /**
     * Collection�AMap�̃T�C�Y���L�^����臒l��ݒ肷��B
     * 
     * @return Collection�AMap�̃T�C�Y���L�^����臒l�B
     */
    public int getCollectionSizeThreshold()
    {
        return CONFIGUTIL.getInteger(COLLECTION_SIZE_THRESHOLD, DEF_COLLECTION_SIZE);
    }

    /**
     * �R���N�V�����̐����Ď�����ۂ̏o�̓`�F�b�N�̊Ԋu���擾����B
     * 
     * @return �R���N�V�����̐����Ď�����ۂ̏o�̓`�F�b�N�̊Ԋu�B
     */
    public int getCollectionInterval()
    {
        return CONFIGUTIL.getInteger(COLLECTION_INTERVAL, DEF_COLLECTION_INTERVAL);
    }

    /**
     * �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐��B
     * 
     * @return �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐��B
     */
    public int getCollectionTraceMax()
    {
        return CONFIGUTIL.getInteger(COLLECTION_TRACE_MAX, DEF_COLLECTION_TRACE_MAX);
    }

    /**
     * �X�^�b�N�g���[�X�̕\���Ɏg���[���ł��B<br />
     * 
     * @return �X�^�b�N�g���[�X�̕\���Ɏg���[���B
     */
    public int getTraceDepth()
    {
        return CONFIGUTIL.getInteger(TRACE_DEPTH, DEF_COLLECTION_TRACE_DEPTH);
    }

    /**
     * �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐[���B
     * 
     * @return �R���N�V�����̐����Ď�����ۂɕێ�����X�^�b�N�g���[�X�̐[���B
     */
    public int getCollectionLeakDetectDepth()
    {
        return CONFIGUTIL.getInteger(COLLECTION_LEAKDETECT_DEPTH, DEF_COLLECTION_LEAKDETECT_DEPTH);
    }

    /**
     * �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ������擾����B
     * 
     * @return �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ����B
     */
    public boolean getClassHistoGC()
    {
        return CONFIGUTIL.getBoolean(CLASS_HISTO_GC, DEF_CLASS_HISTO_GC);
    }

    /**
     * �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ�����ݒ肷��B
     * 
     * @param classHistoGC �N���X�q�X�g�O�������擾����ۂɁAGC���s�����ǂ����B
     */
    public void setClassHistoGC(final boolean classHistoGC)
    {
        CONFIGUTIL.setBoolean(CLASS_HISTO_GC, classHistoGC);
    }

    /**
     * �N���X�q�X�g�O�����̏�ʉ������擾���邩���擾����B
     * 
     * @return �N���X�q�X�g�O�����̏�ʉ������擾���邩�B
     */
    public int getClassHistoMax()
    {
        return CONFIGUTIL.getInteger(CLASS_HISTO_MAX, DEF_CLASS_HISTO_MAX);
    }

    /**
     * �N���X�q�X�g�O�����̏�ʉ������擾���邩��ݒ肷��B
     * 
     * @param classHistoMax �N���X�q�X�g�O�����̏�ʉ������擾���邩�B
     */
    public void setClassHistoMax(final int classHistoMax)
    {
        CONFIGUTIL.setInteger(CLASS_HISTO_MAX, classHistoMax);
    }

    /**
     * �N���X�q�X�g�O�����擾�Ԋu(�~���b)���擾����B
     * 
     * @return �N���X�q�X�g�O�����擾�Ԋu(�~���b)�B
     */
    public int getClassHistoInterval()
    {
        return CONFIGUTIL.getInteger(CLASS_HISTO_INTERVAL, DEF_CLASS_HISTO_INTERVAL);
    }

    /**
     * �N���X�q�X�g�O�����擾�Ԋu(�~���b)��ݒ肷��B
     * 
     * @param classHistoInterval �N���X�q�X�g�O�����擾�Ԋu(�~���b)�B
     */
    public void setClassHistoInterval(final int classHistoInterval)
    {
        CONFIGUTIL.setInteger(CLASS_HISTO_INTERVAL, classHistoInterval);
    }

    /**
     * �N���X�q�X�g�O�������擾���邩�ǂ������擾����B
     * 
     * @return �N���X�q�X�g�O�������擾���邩�ǂ����B
     */
    public boolean getClassHisto()
    {
        return CONFIGUTIL.getBoolean(CLASS_HISTO, DEF_CLASS_HISTO);
    }

    /**
     * �N���X�q�X�g�O�������擾���邩�ǂ�����ݒ肷��B
     * 
     * @param classHisto �N���X�q�X�g�O�������擾���邩�ǂ����B
     */
    public void setClassHisto(final boolean classHisto)
    {
        CONFIGUTIL.setBoolean(CLASS_HISTO, classHisto);
    }

    /**
     * �N���X�q�X�g�O�������擾���邩�ǂ������擾����B
     * 
     * @return �N���X�q�X�g�O�������擾���邩�ǂ����B
     */
    public boolean getDetach()
    {
        return CONFIGUTIL.getBoolean(DETACH, DEF_DETACH);
    }

    /**
     * ���`�������o���s�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return ���`�����̌��o���s���ꍇ�A<code>true</code>
     */
    public boolean isLinearSearchMonitor()
    {
        return CONFIGUTIL.getBoolean(LINEARSEARCH_ENABLED_KEY, DEF_LINEARSEARCH_ENABLED);
    }

    /**
     * ���`�������o���s�����ǂ�����ݒ肵�܂��B<br />
     * 
     * @param linearSearchEnabled ���`�������o���s�����ǂ���
     */
    public void setLinearSearchMonitor(final boolean linearSearchEnabled)
    {
        CONFIGUTIL.setBoolean(LINEARSEARCH_ENABLED_KEY, linearSearchEnabled);
    }

    /**
     * ���`�����ΏۂƂȂ郊�X�g�T�C�Y��臒l���擾���܂��B<br />
     * 
     * @return ���`�����ΏۂƂȂ郊�X�g�T�C�Y��臒l
     */
    public int getLinearSearchListSize()
    {
        return CONFIGUTIL.getInteger(LINEARSEARCH_SIZE, DEF_LINEARSEARCH_SIZE);
    }

    /**
     * ���`�����ΏۂƂ��郊�X�g�T�C�Y��臒l��ݒ肵�܂��B<br />
     * 
     * @param size ���`�����ΏۂƂ��郊�X�g�T�C�Y��臒l
     */
    public void setLinearSearchListSize(final int size)
    {
        CONFIGUTIL.setInteger(LINEARSEARCH_SIZE, size);
    }

    /**
     * ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l���擾���܂��B<br />
     * 
     * @return ���X�g�ɑ΂�����`�A�N�Z�X�񐔂�臒l
     */
    public double getLinearSearchListRatio()
    {
        return CONFIGUTIL.getDouble(LINEARSEARCH_RATIO, DEF_LINEARSEARCH_RATIO);
    }

    /**
     * ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l���擾���܂��B<br />
     * 
     * @param linearSearchRatio ���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l
     */
    public void setLinearSearchListRatio(final double linearSearchRatio)
    {
        CONFIGUTIL.setDouble(LINEARSEARCH_RATIO, linearSearchRatio);
    }

    /**
     * �X���b�h�Ď����s�����ǂ������擾����B
     * 
     * @return �X���b�h�Ď����s���ꍇ��<code>true</code>�B
     */
    public boolean getThreadMonitor()
    {
        return CONFIGUTIL.getBoolean(THREAD_MONITOR, DEF_THREAD_MONITOR);
    }

    /**
     * �X���b�h�Ď����s���Ԋu(�~���b)���擾����B
     * 
     * @return �X���b�h�Ď����s���Ԋu(�~���b)�B
     */
    public long getThreadMonitorInterval()
    {
        return CONFIGUTIL.getLong(THREAD_MONITOR_INTERVAL, DEF_THREAD_MON_INTERVAL);
    }

    /**
     * �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[�����擾����B
     * 
     * @return �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[���B
     */
    public int getThreadMonitorDepth()
    {
        return CONFIGUTIL.getInteger(THREAD_MONITOR_DEPTH, DEF_THREAD_MON_DEPTH);
    }

    /**
     * �t���X���b�h�_���v���o�͂��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return �t���X���b�h�_���v���o�͂���Ƃ���<code>true</code>�B
     */
    public boolean isThreadDump()
    {
        return CONFIGUTIL.getBoolean(THREAD_DUMP_MONITOR, DEF_THREAD_DUMP_MONITOR);
    }

    /**
     * �t���X���b�h�_���v���o�͂��邩�ǂ�����ݒ�܂��B<br />
     * 
     * @param threadDumpMonitor �t���X���b�h�_���v���o�͂��邩�ǂ����B
     */
    public void setThreadDumpMonitor(final boolean threadDumpMonitor)
    {
        CONFIGUTIL.setBoolean(THREAD_DUMP_MONITOR, threadDumpMonitor);
    }

    /**
     * �t���X���b�h�_���v�o�͊Ԋu��Ԃ��܂��B<br />
     * 
     * @return �t���X���b�h�_���v�o�͊Ԋu�B
     */
    public int getThreadDumpInterval()
    {
        return CONFIGUTIL.getInteger(THREAD_DUMP_INTERVAL, DEF_THREAD_DUMP_INTERVAL);
    }

    /**
     * �t���X���b�h�_���v�o�͊Ԋu��ݒ�܂��B<br />
     * 
     * @param threadDumpInterval �t���X���b�h�_���v�o�͊Ԋu�B
     */
    public void setThreadDumpInterval(final int threadDumpInterval)
    {
        CONFIGUTIL.setInteger(THREAD_DUMP_INTERVAL, threadDumpInterval);
    }

    /**
     * �t���X���b�h�_���v�o�͂̃X���b�h����臒l��Ԃ��܂��B<br />
     * 
     * @return �t���X���b�h�_���v�o�͂̃X���b�h����臒l
     */
    public int getThreadDumpThreadNum()
    {
        return CONFIGUTIL.getInteger(THREAD_DUMP_THREAD, DEF_THREAD_DUMP_THREAD);
    }

    /**
     * �t���X���b�h�_���v�o�͂̃X���b�h����臒l��ݒ肵�܂��B<br />
     * 
     * @param threadDumpNum �t���X���b�h�_���v�o�͂̃X���b�h����臒l
     */
    public void setThreadDumpThreadNum(final int threadDumpNum)
    {
        CONFIGUTIL.setInteger(THREAD_DUMP_THREAD, threadDumpNum);
    }

    /**
     * �t���X���b�h�_���v�o�͂�CPU�g�p����臒l��Ԃ��܂��B<br />
     * 
     * @return �t���X���b�h�_���v�o�͂�CPU�g�p����臒l
     */
    public int getThreadDumpCpu()
    {
        return CONFIGUTIL.getInteger(THREAD_DUMP_CPU, DEF_THREAD_DUMP_CPU);
    }

    /**
     * �t���X���b�h�_���v�o�͂�CPU�g�p����臒l��ݒ肵�܂��B<br />
     * 
     * @param threadDumpCpu �t���X���b�h�_���v�o�͂�CPU�g�p����臒l
     */
    public void setThreadDumpCpu(final int threadDumpCpu)
    {
        CONFIGUTIL.setInteger(THREAD_DUMP_CPU, threadDumpCpu);
    }

    /**
     * �t��GC�����o���邩�ǂ�����ݒ肵�܂��B<br />
     * 
     * @param fullGCMonitor �t��GC�����o����ꍇ�A<code>true</code>
     */
    public void setFullGCMonitor(final boolean fullGCMonitor)
    {
        CONFIGUTIL.setBoolean(FULLGC_MONITOR, fullGCMonitor);
    }

    /**
     * �t��GC�����o���邩�ǂ������擾���܂��B<br />
     * 
     * @return �t��GC�����o����ꍇ�A<code>true</code>
     */
    public boolean isFullGCMonitor()
    {
        return CONFIGUTIL.getBoolean(FULLGC_MONITOR, DEF_FULLGC_MONITOR);
    }

    /**
     * �t��GC���s��GarbageCollector���̃��X�g���擾���܂��B<br />
     * 
     * @return �t��GC�����o����ꍇ�A<code>true</code>
     */
    public String getFullGCList()
    {
        return CONFIGUTIL.getString(FULLGC_LIST, DEF_FULLGC_LIST);
    }

    /**
     * �t��GC���o���s��GC���Ԃ�臒l���擾���܂��B<br />
     * 
     * @return �t��GC���o���s��GC���Ԃ�臒l
     */
    public int getFullGCThreshold()
    {
        return CONFIGUTIL.getInteger(FULLGC_THREASHOLD, DEF_FULLGC_THRESHOLD);
    }

    /**
     * �t��GC���o���s��GC���Ԃ�臒l��ݒ肵�܂��B<br />
     * 
     * @param threshold �t��GC���o���s��GC���Ԃ�臒l
     */
    public void setFullGCThreshold(final int threshold)
    {
        CONFIGUTIL.setInteger(FULLGC_THREASHOLD, threshold);
    }

    /**
     * �p���𒲂ׂ�[���̍ő�l���擾����B
     * 
     * @return �N���X�q�X�g�O�������擾���邩�ǂ����B
     */
    public int getInheritanceDepth()
    {
        return CONFIGUTIL.getInteger(INHERITANCE_DEPTH, DEF_INHERITANCE_DEPTH);
    }

    /**
     * �X���b�h�Ď����s�����ǂ�����ݒ肷��B
     * 
     * @param threadMonitor �X���b�h�Ď����s���ꍇ��true�B
     */
    public void setThreadMonitor(final boolean threadMonitor)
    {
        CONFIGUTIL.setBoolean(THREAD_MONITOR, threadMonitor);
    }

    /**
     * �X���b�h�Ď����s���Ԋu(�~���b)��ݒ肷��B
     * 
     * @param threadMonitorInterval �X���b�h�Ď����s���Ԋu(�~���b)�B
     */
    public void setThreadMonitorInterval(final long threadMonitorInterval)
    {
        CONFIGUTIL.setLong(THREAD_MONITOR_INTERVAL, threadMonitorInterval);
    }

    /**
     * �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[�����擾����B
     * 
     * @param threadMonitorDepth �X���b�h�Ď��̍ۂɏo�͂���X�^�b�N�g���[�X�̐[���B
     */
    public void setThreadMonitorDepth(final int threadMonitorDepth)
    {
        CONFIGUTIL.setInteger(THREAD_MONITOR_DEPTH, threadMonitorDepth);
    }

    /**
     * �u���b�N�񐔂��������邩�ǂ�����臒l���擾����B
     * 
     * @return �u���b�N�񐔂��������邩�ǂ�����臒l�B
     */
    public long getBlockThreshold()
    {
        return CONFIGUTIL.getLong(THREAD_BLOCK_THRESHOLD, DEF_THREAD_BLOCK_THRESHOLD);
    }

    /**
     * �u���b�N�񐔂��������邩�ǂ�����臒l��ݒ肷��B
     * 
     * @param blockThreshold �u���b�N�񐔂��������邩�ǂ�����臒l�B
     */
    public void setBlockThreshold(final long blockThreshold)
    {
        CONFIGUTIL.setLong(THREAD_BLOCK_THRESHOLD, blockThreshold);
    }

    /**
     * �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l���擾����B
     * 
     * @return �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l
     */
    public long getBlockTimeThreshold()
    {
        return CONFIGUTIL.getLong(THREAD_BLOCKTIME_THRESHOLD, DEF_THREAD_BLOCKTIME_THRESHOLD);
    }

    /**
     * �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l��ݒ肷��B
     * 
     * @param blockTimeThreshold �u���b�N�p���C�x���g���o�͂���ۂ̃u���b�N�p�����Ԃ�臒l
     */
    public void setBlockTimeThreshold(final long blockTimeThreshold)
    {
        CONFIGUTIL.setLong(THREAD_BLOCKTIME_THRESHOLD, blockTimeThreshold);
    }

    /**
     * �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐����擾����B
     * 
     * @return �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐��B
     */
    public int getBlockThreadInfoNum()
    {
        return CONFIGUTIL.getInteger(THREAD_BLOCK_THREADINFO_NUM, DEF_THREAD_BLOCK_THREADINFO_NUM);
    }

    /**
     * �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐���ݒ肷��B
     * 
     * @param blockThreadInfoNum �u���b�N�񐔂�臒l�𒴂����ۂɎ擾����X���b�h���̐��B
     */
    public void setBlockThreadInfoNum(final int blockThreadInfoNum)
    {
        CONFIGUTIL.setInteger(THREAD_BLOCK_THREADINFO_NUM, blockThreadInfoNum);
    }

    /**
     * ���\�b�h�ɑ΂���Ăяo���Ԋu��臒l��`���擾����B
     * 
     * @return ���\�b�h�ɑ΂���Ăяo���Ԋu��臒l��`�B
     */
    public String getIntervalThreshold()
    {
        return CONFIGUTIL.getString(INTERVAL_THRESHOLD, DEF_INTERVAL_THRESHOLD);
    }

    /**
     * ���\�b�h�ɑ΂���Ăяo���Ԋu��臒l��`��ݒ肷��B
     * 
     * @param callCountThreshold ���\�b�h�ɑ΂���Ăяo���Ԋu��臒l��`�B
     */
    public void setIntervalThreshold(final String callCountThreshold)
    {
        CONFIGUTIL.setString(INTERVAL_THRESHOLD, callCountThreshold);
    }

    /**
     * ���\�b�h�ɑ΂���A�����̒l���Ƃ̌Ăяo���Ԋu��臒l��`���擾����B
     * 
     * @return ���\�b�h�ɑ΂���A�����̒l���Ƃ̌Ăяo���Ԋu��臒l��`�B
     */
    public String getIntervalPerArgsThreshold()
    {
        return CONFIGUTIL.getString(INTERVAL_PER_ARGS_THRESHOLD, DEF_INTERVAL_PER_ARGS_THRESHOLD);
    }

    /**
     * ���\�b�h�ɑ΂���A�����̒l���Ƃ̌Ăяo���Ԋu��臒l��`��ݒ肷��B
     * 
     * @param callCountThreshold ���\�b�h�ɑ΂���A�����̒l���Ƃ̌Ăяo���Ԋu��臒l��`�B
     */
    public void setIntervalPerArgsThreshold(final String callCountThreshold)
    {
        CONFIGUTIL.setString(INTERVAL_PER_ARGS_THRESHOLD, callCountThreshold);
    }

    /**
     * �����X���b�h�A�N�Z�X�Ď����s�����ǂ�����ݒ肵�܂��B<br />
     *
     * @param concurrentEnabled �����X���b�h�A�N�Z�X�Ď����s���Ȃ�true
     */
    public void setConcurrentAccessMonitored(final boolean concurrentEnabled)
    {
        CONFIGUTIL.setBoolean(CONCURRENT_ENABLED_KEY, concurrentEnabled);
    }

    /**
     * �����X���b�h�A�N�Z�X�Ď����s���ǂ�����Ԃ��܂��B<br />
     * 
     * @return true:�Ď�����Afalse:�Ď����Ȃ��B
     */
    public boolean isConcurrentAccessMonitored()
    {
        return CONFIGUTIL.getBoolean(CONCURRENT_ENABLED_KEY, DEFAULT_CONCURRENT_ENABLED);
    }

    /**
     * �^�C���A�E�g�l�ݒ�̊Ď����s�����ǂ�����ݒ肵�܂��B<br />
     *
     * @param timeoutMonitor �^�C���A�E�g�l�̐ݒ�̊Ď����s���ꍇ�A<code>true</code>
     */
    public void setTimeoutMonitor(final boolean timeoutMonitor)
    {
        CONFIGUTIL.setBoolean(TIMEOUT_MONITOR, timeoutMonitor);
    }

    /**
     * �^�C���A�E�g�l�ݒ�̊Ď����s�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �^�C���A�E�g�l�̐ݒ�̊Ď����s���ꍇ�A<code>true</code>
     */
    public boolean isTimeoutMonitor()
    {
        return CONFIGUTIL.getBoolean(TIMEOUT_MONITOR, DEF_TIMEOUT_MONITOR);
    }

    /**
     * �v���Ώۂ��玩�����O����Ăяo���񐔂�臒l��Ԃ��܂��B<br />
     * 
     * @return �v���Ώۂ��玩�����O����Ăяo���񐔂�臒l
     */
    public int getAutoExcludeThresholdCount()
    {
        return CONFIGUTIL.getInteger(AUTO_EXCLUDE_THRESHOLD_COUNT,
        		DEF_AUTO_EXCLUDE_THRESHOLD_COUNT);
    }

    /**
     * �v���Ώۂ��玩�����O������s���Ԃ�臒l��Ԃ��܂��B<br />
     * 
     * @return �v���Ώۂ��玩�����O������s���Ԃ�臒l(�P��:�~���b)
     */
    public int getAutoExcludeThresholdTime()
    {
        return CONFIGUTIL.getInteger(AUTO_EXCLUDE_THRESHOLD_TIME, DEF_AUTO_EXCLUDE_THRESHOLD_TIME);
    }

    /**
     * ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ�����Ԃ��܂��B<br /> 
     * 
     * @return ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ���
     */
    public boolean isLeakCollectionSizePrint()
    {
        return CONFIGUTIL.getBoolean(LEAK_COLLECTIONSIZE_OUT, DEF_LEAK_COLLECTIONSIZE_OUT);
    }

    /**
     * ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ�����ݒ肷��B
     * 
     * @param leakCollectionSizePrint ���������[�N���o���ɁA���[�N�����R���N�V�����̃T�C�Y���o�͂��邩�ǂ���
     */
    public void setLeakCollectionSizePrint(final boolean leakCollectionSizePrint)
    {
        CONFIGUTIL.setBoolean(LEAK_COLLECTIONSIZE_OUT, leakCollectionSizePrint);
    }

    /**
     * Javelin�̍Đڑ��Ԋu���擾
     * 
     * @return Javelin�̍Đڑ��Ԋu
     */
    public int getJavelinBindInterval()
    {
        return CONFIGUTIL.getInteger(JAVELIN_BIND_INTERVAL, DEF_JAVELIN_BIND_INTERVAL);
    }

    /**
     * Javelin�̍Đڑ��Ԋu��ݒ�
     * 
     * @param javelinBindInterval �Đڑ��Ԋu
     */
    public void setJavelinBindInterval(final int javelinBindInterval)
    {
        CONFIGUTIL.setInteger(JAVELIN_BIND_INTERVAL, javelinBindInterval);
    }

    /**
     * Log4J�̃��O�o�͂ɂ����āA�X�^�b�N�g���[�X���o�͂���臒l���x�����擾����B
     * 
     * @return �X�^�b�N�g���[�X���o�͂���臒l���x��
     */
    public String getLog4jPrintStackLevel()
    {
        return CONFIGUTIL.getLogLevel(LOG4J_PRINTSTACK_LEVEL, DEF_LOG4J_PRINTSTACK_LEVEL);
    }

    /**
     * Log4J�̃��O�o�͂ɂ����āA�X�^�b�N�g���[�X���o�͂���臒l���x����ݒ肷��B
     * 
     * @param log4jPrintStackLevel �X�^�b�N�g���[�X���o�͂���臒l���x��
     */
    public void setLog4jPrintStackLevel(final String log4jPrintStackLevel)
    {
        CONFIGUTIL.setString(LOG4J_PRINTSTACK_LEVEL, log4jPrintStackLevel);
    }

    /**
     * �f�b�h���b�N�̊Ď����s�����A���擾����B<br /> 
     * 
     * @return �f�b�h���b�N�̊Ď����s����
     */
    public boolean isDeadLockMonitor()
    {
        return CONFIGUTIL.getBoolean(THREAD_DEADLOCK_MONITOR, DEF_THREAD_DEADLOCK_MONITOR);
    }

    /**
     * �f�b�h���b�N�̊Ď����s�����A��ݒ肷��B<br /> 
     * 
     * @param deadLockMonitor �f�b�h���b�N�̊Ď����s����
     */
    public void setDeadLockMonitor(final boolean deadLockMonitor)
    {
        CONFIGUTIL.setBoolean(THREAD_DEADLOCK_MONITOR, deadLockMonitor);
    }

    /**
     * EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ������擾����B<br /> 
     * 
     * @return EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ���
     */
    public boolean isEjbSessionMonitor()
    {
        return CONFIGUTIL.getBoolean(EJB_SESSION_MONITOR, DEF_EJB_SESSION_MONITOR);
    }

    /**
     * EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ�����ݒ肷��B<br /> 
     * 
     * @param ejbSessionMonitor EJB�̃Z�b�V����Bean�̌Ăяo���^�����܂ł̎��Ԃ̊Ď����s�����ǂ���
     */
    public void setEjbSessionMonitor(final boolean ejbSessionMonitor)
    {
        CONFIGUTIL.setBoolean(EJB_SESSION_MONITOR, ejbSessionMonitor);
    }

    /**
     * BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷�邩���擾����B<br /> 
     * 
     * @return BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷�邩
     */
    public boolean isAcceptPortIsRange()
    {
        return CONFIGUTIL.getBoolean(ACCEPTPORT_ISRANGE, DEF_ACCEPTPORT_ISRANGE);
    }

    /**
     * BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷��ۂ̍ő�l���擾
     * 
     * @return BottleNeckEye/DataCollector�Ƃ̒ʐM�p�|�[�g��͈͎w�肷��ۂ̍ő�l
     */
    public int getAcceptPortRangeMax()
    {
        return CONFIGUTIL.getInteger(ACCEPTPORT_RANGEMAX, DEF_ACCEPTPORT_RANGEMAX);
    }

    /**
     * AcceptDelay��Ԃ�
     * @return AcceptDelay
     */
    public long getAcceptDelay()
    {
        return CONFIGUTIL.getLong(ACCEPT_DELAY_KEY, DEFAULT_ACCEPT_DELAY);
    }

    /**
     * CpuTimeUnit��Ԃ�
     * @return CpuTimeUnit
     */
    public int getCpuTimeUnit()
    {
        return CONFIGUTIL.getInteger(CPU_TIME_UNIT_KEY, DEF_CPU_TIME_UNIT);
    }

    /**
     * CallTreeAll��Ԃ�
     * @return CallTreeAll
     */
    public boolean isCallTreeAll()
    {
        return CONFIGUTIL.getBoolean(CALL_TREE_ALL_KEY, DEF_CALL_TREE_ALL);
    }

    /**
     * CallTreeEnabled��Ԃ�
     * @return CallTreeEnabled
     */
    public boolean isCallTreeEnabled()
    {
        return CONFIGUTIL.getBoolean(CALL_TREE_ENABLE_KEY, DEF_CALL_TREE_ENABLE);
    }

    /**
     * �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ�����Ԃ��B
     * @return �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ���
     */
    public boolean getCollectSystemResources()
    {
        return CONFIGUTIL.getBoolean(COLLECT_SYSTEM_RESOURCES, DEF_COLLECT_SYSTEM_RESOURCES);
    }

    /**
     * �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ�����ݒ肷��B
     * @param collectSystemResources �V�X�e���̃��\�[�X�f�[�^���擾���邩�ǂ���
     */
    public void setCollectSystemResources(final boolean collectSystemResources)
    {
        CONFIGUTIL.setBoolean(COLLECT_SYSTEM_RESOURCES, collectSystemResources);
    }

    /**
     * InvocationFullEvent�𑗐M���邩�ǂ�����Ԃ��B
     * @return InvocationFullEvent�𑗐M���邩�ǂ���
     */
    public boolean getSendInvocationFullEvent()
    {
        return CONFIGUTIL.getBoolean(SEND_INVOCATION_FULL_EVENT, DEF_SEND_INVOCATION_FULL_EVENT);
    }

    /**
     * InvocationFullEvent�𑗐M���邩�ǂ�����ݒ肷��B
     * @param sendInvocationFullEvent InvocationFullEvent�𑗐M���邩�ǂ���
     */
    public void setSendInvocationFullEvent(final boolean sendInvocationFullEvent)
    {
        CONFIGUTIL.setBoolean(SEND_INVOCATION_FULL_EVENT, sendInvocationFullEvent);
    }

    /**
     * JMX�̃��\�[�X�f�[�^���擾���邩�ǂ�����Ԃ��B
     * @return JMX�̃��\�[�X�f�[�^���擾���邩�ǂ���
     */
    public boolean getCollectJmxResources()
    {
        return CONFIGUTIL.getBoolean(COLLECT_JMX_RESOURCES, DEF_COLLECT_JMX_RESOURCES);
    }

    /**
     * �VJMX�̃��\�[�X�f�[�^���擾���邩�ǂ�����ݒ肷��B
     * @param collectJmxResources JMX�̃��\�[�X�f�[�^���擾���邩�ǂ���
     */
    public void setCollectJmxResources(final boolean collectJmxResources)
    {
        CONFIGUTIL.setBoolean(COLLECT_JMX_RESOURCES, collectJmxResources);
    }

    /**
     * �X�g�[�����\�b�h���Ď����邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return �X�g�[�����\�b�h���Ď�����Ƃ���<code>true</code>�B
     */
    public boolean isMethodStallMonitor()
    {
        return CONFIGUTIL.getBoolean(METHOD_STALL_MONITOR, DEF_METHOD_STALL_MONITOR);
    }

    /**
     * �X�g�[�����\�b�h���Ď����邩�ǂ�����ݒ�܂��B<br />
     * 
     * @param methodStallMonitor �X�g�[�����\�b�h���Ď����邩�ǂ����B
     */
    public void setMethodStallMonitor(final boolean methodStallMonitor)
    {
        CONFIGUTIL.setBoolean(METHOD_STALL_MONITOR, methodStallMonitor);
    }

    /**
     * �X�g�[�����\�b�h���Ď����������Ԃ��܂��B<br />
     * 
     * @return �X�g�[�����\�b�h���Ď���������B
     */
    public int getMethodStallInterval()
    {
        return CONFIGUTIL.getInteger(METHOD_STALL_INTERVAL, DEF_METHOD_STALL_INTERVAL);
    }

    /**
     * �X�g�[�����\�b�h���Ď����������ݒ�܂��B<br />
     * 
     * @param methodStallInterval �X�g�[�����\�b�h���Ď���������B
     */
    public void setMethodStallInterval(final int methodStallInterval)
    {
        CONFIGUTIL.setInteger(METHOD_STALL_INTERVAL, methodStallInterval);
    }

    /**
     * �X�g�[�����\�b�h�Ɣ��f����臒l��Ԃ��܂��B<br />
     * 
     * @return �X�g�[�����\�b�h�Ɣ��f����臒l�B
     */
    public int getMethodStallThreshold()
    {
        return CONFIGUTIL.getInteger(METHOD_STALL_THRESHOLD, DEF_METHOD_STALL_THRESHOLD);
    }

    /**
     * �X�g�[�����\�b�h�Ɣ��f����臒l��ݒ�܂��B<br />
     * 
     * @param methodStallThreshold �X�g�[�����\�b�h�Ɣ��f����臒l�B
     */
    public void setMethodStallThreshold(final int methodStallThreshold)
    {
        CONFIGUTIL.setInteger(METHOD_STALL_THRESHOLD, methodStallThreshold);
    }

    /**
     * �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[����Ԃ��܂��B<br />
     * 
     * @return �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[���B
     */
    public int getMethodStallTraceDepth()
    {
        return CONFIGUTIL.getInteger(METHOD_STALL_TRACE_DEPTH, DEF_METHOD_STALL_TRACE_DEPTH);
    }

    /**
     * �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[����ݒ�܂��B<br />
     * 
     * @param methodStallTraceDepth �X�g�[�����\�b�h���o���ɏo�͂���X�^�b�N�g���[�X�̐[���B
     */
    public void setMethodStallTraceDepth(final int methodStallTraceDepth)
    {
        CONFIGUTIL.setInteger(METHOD_STALL_TRACE_DEPTH, methodStallTraceDepth);
    }

    /**
     * HTTP�X�e�[�^�X�o�͉ۂ�ݒ肵�܂��B
     * 
     * @param isHttpStatusError HTTP�X�e�[�^�X�G���[��ʒm���邩�ǂ���
     */
    public void setHttpStatusError(final boolean isHttpStatusError)
    {
        CONFIGUTIL.setBoolean(HTTP_STATUS_ERROR_KEY, isHttpStatusError);
    }

    /**
     * HTTP�X�e�[�^�X�o�͉ۂ��擾���܂��B
     * 
     * @return HTTP�X�e�[�^�X�G���[�̒ʒm��
     */
    public boolean isHttpStatusError()
    {
        return CONFIGUTIL.getBoolean(HTTP_STATUS_ERROR_KEY, DEFAULT_HTTP_STATUS_ERROR);
    }

    /**
     * MBean�T�[�o�̃z�X�g�����擾���܂��B
     * 
     * @return MBean�T�[�o�̃z�X�g��
     */
    public String getJMXHost()
    {
        return CONFIGUTIL.getString(JMX_HOST, DEF_JMX_HOST);
    }

    /**
     * MBean�T�[�o�̃z�X�g����ݒ肵�܂��B
     * 
     * @param hostname MBean�T�[�o�̃z�X�g��
     */
    public void setJMXHost(final String hostname)
    {
        CONFIGUTIL.setString(JMX_HOST, hostname);
    }

    /**
     * MBean�T�[�o�̃|�[�g�ԍ����擾���܂��B
     * 
     * @return MBean�T�[�o�̃|�[�g�ԍ�
     */
    public int getJMXPort()
    {
        return CONFIGUTIL.getInteger(JMX_PORT, DEF_JMX_PORT);
    }

    /**
     * MBean�T�[�o�̃|�[�g�ԍ���ݒ肵�܂��B
     * 
     * @param port MBean�T�[�o�̃|�[�g�ԍ�
     */
    public void setJMXPort(final int port)
    {
        CONFIGUTIL.setInteger(JMX_PORT, port);
    }

    /**
     * MBean�T�[�o�̔F�؃��[�U�����擾���܂��B
     * 
     * @return MBean�T�[�o�̔F�؃��[�U��
     */
    public String getJMXUserName()
    {
        return CONFIGUTIL.getString(JMX_USER_NAME, DEF_JMX_USER_NAME);
    }

    /**
     * MBean�T�[�o�̔F�؃��[�U����ݒ肵�܂��B
     * 
     * @param userName MBean�T�[�o�̔F�؃��[�U��
     */
    public void setJMXUserName(final String userName)
    {
        CONFIGUTIL.setString(JMX_USER_NAME, userName);
    }

    /**
     * MBean�T�[�o�̔F�؃p�X���[�h���擾���܂��B
     * 
     * @return MBean�T�[�o�̔F�؃p�X���[�h
     */
    public String getJMXPassword()
    {
        return CONFIGUTIL.getString(JMX_PASSWORD, DEF_JMX_PASSWORD);
    }

    /**
     * MBean�T�[�o�̔F�؃p�X���[�h��ݒ肵�܂��B
     * 
     * @param password MBean�T�[�o�̔F�؃p�X���[�h
     */
    public void setJMXPassword(final String password)
    {
        CONFIGUTIL.setString(JMX_PASSWORD, password);
    }

    /**
     * �ڑ����[�h(server/client)��Ԃ��܂��B<br />
     * 
     * @return �ڑ����[�h(server/client)�B
     */
    public String getConnectionMode()
    {
        return CONFIGUTIL.getString(CONNECTION_MODE_KEY, DEF_CONNNECTION_MODE);
    }

    /**
     * �ڑ����[�h(server/client)��Ԃ��܂��B<br />
     * 
     * @param connectionMode �ڑ����[�h(server/client)�B
     */
    public void setConnectionMode(final String connectionMode)
    {
        CONFIGUTIL.setString(CONNECTION_MODE_KEY, connectionMode);
    }

    /**
     * ���ږ��ɕt�^����ړ����̕�������擾���܂��B
     * 
     * @return ���ږ��ɕt�^����ړ����B
     */
    public String getItemNamePrefix()
    {
        return CONFIGUTIL.getString(ITEMNAME_PREFIX, DEF_ITEMNAME_PREFIX);
    }

    /**
     * ���ږ��ɐړ�����t�^���Ȃ��p�^�[�����擾���܂��B
     * 
     * @return ���ږ��ɐړ�����t�^���Ȃ��p�^�[��(�O����v)�B
     */
    public String getItemNameNoPrefixList()
    {
        return CONFIGUTIL.getString(ITEMNAME_NOPREFIX_LIST, DEF_ITEMNAME_NOPREFIX_LIST);
    }

    /**
     * HadoopAgent���烊�\�[�X�l���擾���邩�ǂ������w�肵�܂��B
     * 
     * @return HadoopAgent���烊�\�[�X�l���擾����ꍇ��true�A�����łȂ��ꍇ��false�B
     */
    public boolean isCollectHadoopAgentResources()
    {
        return CONFIGUTIL.getBoolean(COLLECT_HADOOP_AGENT_RESOURCES,
                                     DEF_COLLECT_HADOOP_AGENT_RESOURCES);
    }

    /**
     * HBaseAgent���烊�\�[�X�l���擾���邩�ǂ������w�肵�܂��B
     * 
     * @return HBaseAgent���烊�\�[�X�l���擾����ꍇ��true�A�����łȂ��ꍇ��false�B
     */
    public boolean isCollectHBaseAgentResources()
    {
        return CONFIGUTIL.getBoolean(COLLECT_HBASE_AGENT_RESOURCES,
                                     DEF_COLLECT_HBASE_AGENT_RESOURCES);
    }

    /**
     * Invocation���̒����̏���������B
     * 
     * @return Invocation���̒����̏���B
     */
    public int getInvocationNameLimitLength()
    {
        return CONFIGUTIL.getInteger(INVOCATION_NAME_LIMITLENGTH_KEY,
                                     DEF_INVOCATION_NAME_LIMITLENGTH);
    }

    /**
     * Javelin��K�p�����v���Z�X��������N���X�^���̂��擾����B
     * 
     * @return Javelin��K�p�����v���Z�X��������N���X�^���́B
     */
    public String getClusterName()
    {
        return CONFIGUTIL.getString(CLUSTER_NAME_KEY, DEF_CLUSTER_NAME);
    }
}
