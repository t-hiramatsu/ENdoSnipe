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
package jp.co.acroquest.endosnipe.common.event;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;

/**
 * �C�x���g���A�p�����[�^�����`����C���^�t�F�[�X�B<br />
 * 
 * �萔���́A�ȉ��̖����K���ɏ]�����̂Ƃ���B
 * <ul>
 * <li>NAME_�C�x���g��</li>
 * <li>PARAM_�C�x���g��_�p�����[�^��</li>
 * </ul>
 * 
 * @author eriguchi
 */
public interface EventConstants
{
    /** SQL���s�񐔒��߃C�x���g�̃C�x���g���B */
    String NAME_SQLCOUNT = "SqlCountOver";

    /** SQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂̌��o�l�̃p�����[�^���B */
    String PARAM_SQLCOUNT_ACTUAL = JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount.acutual";

    /** SQL���s�񐔒��߃C�x���g�ł̌Ăяo�����񐔂�臒l�̃p�����[�^���B */
    String PARAM_SQLCOUNT_THRESHOLD = JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount.threshold";

    /** SQL���s�񐔒��߃C�x���g�ł̌Ăяo����SQL�̌��o�l�̃p�����[�^���B */
    String PARAM_SQLCOUNT_SQL = JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount.sql";

    /** SQL���s�񐔒��߃C�x���g�ł̃X�^�b�N�g���[�X�̃p�����[�^���B */
    String PARAM_SQLCOUNT_STACKTRACE = JavelinConfig.JAVELIN_PREFIX + "jdbc.sqlcount.stackTrace";

    /** SQL��Full Scan�C�x���g�̃C�x���g���B */
    String NAME_FULL_SCAN = "FullScan";

    /** SQL��Full Scan���s���Ă���e�[�u�����B */
    String PARAM_FULL_SCAN_TABLE_NAME = JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.tableName";

    /** SQL��Full Scan���s��������Stack Trace�B */
    String PARAM_FULL_SCAN_STACK_TRACE = JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.stackTrace";

    /** SQL��Full Scan���s��������SQL���B */
    String PARAM_FULL_SCAN_SQL = JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.sql";

    /** SQL��Full Scan���s�������̎��s�v��B */
    String PARAM_FULL_SCAN_EXEC_PLAN = JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.execPlan";

    /** SQL��Full Scan���s��������SQL���s���ԁB */
    String PARAM_FULL_SCAN_DURATION = JavelinConfig.JAVELIN_PREFIX + "jdbc.fullScan.duration";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̃C�x���g���B */
    String NAME_INTERVALERROR = "IntervalError";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̃N���X���B */
    String PARAM_INTERVALERROR_CLASSNAME = "javelin.interval.classname";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̃��\�b�h���B */
    String PARAM_INTERVALERROR_METHODNAME = "javelin.interval.methodname";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̈����̒l�i������ "1" �A "2" �������j�B */
    String PARAM_INTERVALERROR_ARGUMENTS = "javelin.interval.arguments.";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̌Ăяo���Ԋu���o�l(ms)�B */
    String PARAM_INTERVALERROR_ACTUAL_INTERVAL = "javelin.interval.actual";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̌Ăяo���Ԋu臒l(ms)�B */
    String PARAM_INTERVALERROR_THRESHOLD = "javelin.interval.threshold";

    /** ���\�b�h�Ăяo���Ԋu���߃C�x���g�̌Ăяo�����̃X�^�b�N�g���[�X�B */
    String PARAM_INTERVALERROR_STACKTRACE = "javelin.interval.stackTrace";

    /** ���������[�N���o�C�x���g�̃��\�b�h���B */
    String NAME_LEAK_DETECTED = "LeakDetected";

    /** ���������[�N���o�C�x���g�̎��ʎq�̃p�����[�^���B */
    String PARAM_LEAK_IDENTIFIER = "javelin.leak.identifier";

    /** ���������[�N���o�C�x���g��臒l�̃p�����[�^�� */
    String PARAM_LEAK_THRESHOLD = "javelin.leak.threshold";

    /** ���������[�N���o�C�x���g�̐��̃p�����[�^�� */
    String PARAM_LEAK_COUNT = "javelin.leak.count";

    /** ���������[�N���o�C�x���g�̃T�C�Y�̃p�����[�^�� */
    String PARAM_LEAK_SIZE = "javelin.leak.size";

    /** ���������[�N���o�C�x���g�̒ǉ����ꂽ�I�u�W�F�N�g�̃N���X�� */
    String PARAM_LEAK_CLASS_NAME = "javelin.leak.className";

    /** ���������[�N���o�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^���B */
    String PARAM_LEAK_STACK_TRACE = "javelin.leak.stackTrace";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃C�x���g�� */
    String NAME_CONCURRENT_ACCESS = "ConcurrentAccess";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̎��ʎq�̃p�����[�^�� */
    String PARAM_CONCURRENT_IDENTIFIER = "javelin.concurrent.identifier";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃X���b�h���̃p�����[�^�� */
    String PARAM_CONCURRENT_THREAD = "javelin.concurrent.thread";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̎����̃p�����[�^�� */
    String PARAM_CONCURRENT_TIME = "javelin.concurrent.time";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃X���b�h���̃p�����[�^�� */
    String PARAM_CONCURRENT_LOCK = "javelin.concurrent.lock";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃G���[�̃p�����[�^�� */
    String PARAM_CONCURRENT_ERROR = "javelin.concurrent.error";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^�� */
    String PARAM_CONCURRENT_STACKTRACE = "javelin.concurrent.stackTrace";

    /** �^�C���A�E�g���ݒ茟�o�C�x���g�̃��\�b�h�� */
    String NAME_NOTIMEOUT_DETECTED = "NoTimeoutDetected";

    /** �^�C���A�E�g���ݒ茟�o�C�x���g�̎��ʎq�̃p�����[�^���B */
    String NOTIMEOUT_IDENTIFIER = "javelin.notimeout.identifier";

    /** �^�C���A�E�g���ݒ茟�o�C�x���g�̃^�C���A�E�g�l�̃p�����[�^���B */
    String NOTIMEOUT_TIMEOUT = "javelin.notimeout.timeout";

    /** �^�C���A�E�g���ݒ茟�o�C�x���g�̃A�h���X�̃p�����[�^���B */
    String NOTIMEOUT_ADDRESS = "javelin.notimeout.address";

    /** �^�C���A�E�g���ݒ茟�o�C�x���g�̃|�[�g�ԍ��̃p�����[�^���B */
    String NOTIMEOUT_PORT = "javelin.notimeout.port";

    /** �����X���b�h����̃A�N�Z�X�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^�� */
    String NOTIMEOUT_STACKTRACE = "javelin.notimeout.stackTrace";

    /** �t���X���b�h�_���v�擾�̃C�x���g�� */
    String NAME_THREAD_DUMP = "FullThreadDump";

    /** �t���X���b�h�_���v�擾�C�x���g�̃p�����[�^�� */
    String PARAM_THREAD_DUMP = "javelin.thread.dump";

    /** �t���X���b�h�_���v�擾����CPU�g�p���̃p�����[�^�� */
    String PARAM_THREAD_DUMP_CPU_TOTAL = "javelin.thread.dump.cpu.total";

    /** �t���X���b�h�_���v�擾����CPU�g�p���̃p�����[�^�� */
    String PARAM_THREAD_DUMP_CPU = "javelin.thread.dump.cpu";

    /** �t���X���b�h�_���v�擾���̃X���b�h���̃p�����[�^�� */
    String PARAM_THREAD_DUMP_THREADNUM = "javelin.thread.dump.threadNum";

    /** �t���X���b�h�_���v�擾�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^�� */
    String PARAM_THREAD_DUMP_STACKTRACE = "javelin.thread.dump.stackTrace";

    /** �N���X�q�X�g�O�����_���v�擾�̃C�x���g�� */
    String NAME_CLASSHISTOGRAM = "ClassHistogram";

    /** �N���X�q�X�g�O�����_���v�擾�̃C�x���g�� */
    String PARAM_CLASSHISTOGRAM = "ClassHistogram";

    /** �t��GC�擾�̃C�x���g�� */
    String NAME_FULLGC = "FullGCDetected";

    /** ���t�@�����X�_���v�擾�̃C�x���g�� */
    String NAME_REFERENCE_DUMP = "ReferenceDump";

    /** ���t�@�����X�_���v�擾�C�x���g�̃p�����[�^�� */
    String PARAM_REFERENCE_DUMP = "javelin.reference.dump";

    /** �t��GC�����񐔂̃p�����[�^�� */
    String PARAM_FULLGC_COUNT = "javelin.fullgc.count";

    /** �t��GC���s���Ԃ�\���p�����[�^�� */
    String PARAM_FULLGC_TIME = "javelin.fullgc.time";

    /** �t��GC�����o�����Ƃ��̃q�[�v�������̃p�����[�^�� */
    String PARAM_FULLGC_HEAPMEMORY = "javelin.fullgc.heapMemory";

    /** ��O���o�C�x���g�̃��\�b�h�� */
    String NAME_EXCEPTION_DETECTED = "ExceptionDetected";

    /** ��O���o�C�x���g�̎��ʎq�̃p�����[�^���B */
    String EXCEPTION_IDENTIFIER = "javelin.exception.identifier";

    /** ��O���o�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^���B */
    String EXCEPTION_STACKTRACE = "javelin.exception.stackTrace";

    /** CallTree���ő�l�ɒB�����Ƃ��̃C�x���g�� */
    String NAME_CALLTREE_FULL = "CallTreeFull";

    /** Invocation���ő�l�ɒB�����Ƃ��̃C�x���g�� */
    String NAME_INVOCATION_FULL = "InvocationFull";

    /** CallTree�̍ő�l */
    String PARAM_CALLTREE = "javelin.call.tree";

    /** CallTree���ő�l�ɒB�����Ƃ���<�N���X��>#<���\�b�h��> */
    String PARAM_CALLTREE_METHOD = "javelin.call.tree.method";

    /** CallTree���ő�l�ɒB�����Ƃ��̃X�^�b�N�g���[�X */
    String PARAM_CALLTREE_STACKTRACE = "javelin.call.tree.stackTrace";

    /** Invocation�̍ő�l */
    String PARAM_INVOCATION = "javelin.invocation";

    /** Invocation���ő�l�ɒB�����Ƃ���<�N���X��> */
    String PARAM_INVOCATION_CLASS = "javelin.invocation.class";

    /** Invocation���ő�l�ɒB�����Ƃ��ɒǉ�����<���\�b�h��> */
    String PARAM_INVOCATION_METHOD_ADD = "javelin.invocation.method.add";

    /** Invocation���ő�l�ɒB�����Ƃ��ɍ폜����<���\�b�h��> */
    String PARAM_INVOCATION_METHOD_REMOVE = "javelin.invocation.method.remove";

    /** Invocation���ő�l�ɒB�����Ƃ��̃X�^�b�N�g���[�X */
    String PARAM_INVOCATION_STACKTRACE = "javelin.invocation.stackTrace";

    /** �C�x���g�����̃N���X�� */
    String EVENT_CLASSNAME = "EVENT_JAVELIN_CLASS";

    /** �C�x���g�����̃��\�b�h�� */
    String EVENT_METHODNAME = "EVENT_JAVELIN_METHOD";

    /** �u���b�N�p�����o���̃C�x���g�� */
    String EVENT_THREAD_BLOCK_CONTINUE = "ThreadBlockContinue";

    /** �u���b�N�p�����o���A���b�N�ێ��X���b�h���̃p�����[�^�� */
    String PARAM_THREAD_MONITOR_OWNER = "thread.monitor.owner";

    /** �u���b�N�p�����o���A���b�N�҂��X���b�h���̃p�����[�^�� */
    String PARAM_THREAD_MONITOR_THREAD = "thread.monitor.thread";

    /** �u���b�N�p�����o���A�A���u���b�N���Ԃ̃p�����[�^�� */
    String PARAM_THREAD_BLOCK_DURATION = "thread.block.duration";

    /** �X���b�h���[�N���o���̃C�x���g�� */
    String EVENT_THREAD_LEAK_CONTINUE = "ThreadLeakContinue";

    /** �X���b�h���[�N���o�^�C�~���O�̃p�����[�^�� */
    String PARAM_THREAD_LEAK_TIMING = "thread.leak.timing";

    /** �X���b�h���[�N���o���̃X���b�hID�̃p�����[�^�� */
    String PARAM_THREAD_LEAK_ID = "thread.leak.id";

    /** �X���b�h���[�N���o�X���b�h�̃p�����[�^�� */
    String PARAM_THREAD_LEAK_NAME = "thread.leak.name";

    /** �X���b�h��Ԃ̃p�����[�^�� */
    String PARAM_THREAD_LEAK_STATE = "thread.leak.state";

    /** �X���b�h���[�N���o���̃X�^�b�N�g���[�X�̃p�����[�^�� */
    String PARAM_THREAD_LEAK_STACKTRACE = "thread.leak.stackTrace";

    /** ���`���������̃C�x���g�� */
    String NAME_LINEARSEARCH_DETECTED = "LinearSearchDetected";

    /** ���`�������o�������X�g�̃T�C�Y */
    String PARAM_LINEARSEARCH_SIZE = "linearsearch.size";

    /** ���`�������o�������X�g�̌����� */
    String PARAM_LINEARSEARCH_COUNT = "linearsearch.count";

    /** ���`�������o�������X�g�̃I�u�W�F�N�gID */
    String PARAM_LINEARSEARCH_OBJECTID = "linearsearch.objectID";

    /** ���`�������o���̃X�^�b�N�g���[�X */
    String PARAM_LINEARSEARCH_STACKTRACE = "linearsearch.stackTrace";

    /** Log4J�ɂ��G���[���O�o�͎��̃C�x���g�� */
    String NAME_LOG4JERROR_DETECTED = "Log4jErrorDetected";

    /** Log4J�ɂ��G���[���O�o�͎��̃��O���x�� */
    String PARAM_LOG4JERROR_LOGLEVEL = "log4jerror.logLevel";

    /** Log4J�ɂ��G���[���O�o�͎��̃��O���b�Z�[�W */
    String PARAM_LOG4JERROR_LOGMESSAGE = "log4jerror.logMessage";

    /** Log4J�ɂ��G���[���O�o�͎��̗�O���b�Z�[�W */
    String PARAM_LOG4JERROR_EXCLASS = "log4jerror.exClass";

    /** Log4J�ɂ��G���[���O�o�͎��̗�O���b�Z�[�W */
    String PARAM_LOG4JERROR_EXMESSAGE = "log4jerror.exMessage";

    /** Log4J�ɂ��G���[���O�o�͎��̗�O�X�^�b�N�g���[�X */
    String PARAM_LOG4JERROR_EXSTACKTRACE = "log4jerror.exStackTrace";

    /** Log4J�ɂ��G���[���O�o�͎��̃X�^�b�N�g���[�X */
    String PARAM_LOG4JERROR_STACKTRACE = "log4jerror.stackTrace";

    /** �f�b�h���b�N���o���̃C�x���g�� */
    String NAME_DEADLOCK_DETECTED = "DeadLockDetected";

    /** �f�b�h���b�N���o���̃X���b�h��� */
    String PARAM_DEADLOCK_INFO = "thread.deadlock.info.";

    /** CommonsPool���o�C�x���g�̃��\�b�h���B */
    String NAME_COMMONSPOOL_INIT = "CommonsPoolInitialize";

    /** CommonsPool�̃I�u�W�F�N�gID */
    String PARAM_COMMONSPOOL_OBJECTID = "javelin.commons.pool.objectID";

    /** CommonsPool�̃X�^�b�N�g���[�X */
    String PARAM_COMMONSPOOL_STACKTRACE = "javelin.commons.pool.stackTrace";

    /** �����ؒf���o���̃C�x���g�� */
    String NAME_FORCE_DISCONNECTED = "ForceDisconnected";

    /** HTTP�X�e�[�^�X�G���[�̃C�x���g�� */
    String NAME_HTTP_STATUS_ERROR = "HttpStatusError";

    /**  HTTP�X�e�[�^�X�G���[��URL */
    String PARAM_HTTP_URL = "javelin.commons.pool.stackTrace";

    /**  HTTP�X�e�[�^�X�G���[��URL */
    String PARAM_HTTP_STATUS = "http.status";

    /**  HTTP�X�e�[�^�X�G���[��URL */
    String PARAM_HTTP_THROWABLE_MESSAGE = "http.throwable.message";

    /**  HTTP�X�e�[�^�X�G���[��URL */
    String PARAM_HTTP_THROWABLE_STACKTRACE = "http.throwable.stacktrace";

    /** �X�g�[�����\�b�h���o���̃C�x���g�� */
    String EVENT_METHOD_STALL = "MethodStall";

    /** �X�g�[�����\�b�h���o�C�x���g��臒l�̃p�����[�^�� */
    String PARAM_METHOD_STALL_THRESHOLD = "javelin.method.stall.threshold";

    /** �X�g�[�����\�b�h���o�C�x���g�̃N���X���̃p�����[�^�� */
    String PARAM_METHOD_STALL_CLASS_NAME = "javelin.method.stall.className";

    /** �X�g�[�����\�b�h���o�C�x���g�̃��\�b�h���̃p�����[�^�� */
    String PARAM_METHOD_STALL_METHOD_NAME = "javelin.method.stall.methodName";

    /** �X�g�[�����\�b�h���o�C�x���g�̃X���b�hID�̃p�����[�^�� */
    String PARAM_METHOD_STALL_THREAD_ID = "javelin.method.stall.threadId";

    /** �X�g�[�����\�b�h���o�C�x���g�̃X���b�h���̃p�����[�^�� */
    String PARAM_METHOD_STALL_THREAD_NAME = "javelin.method.stall.threadName";

    /** �X�g�[�����\�b�h���o�C�x���g�̃X���b�h��Ԃ̃p�����[�^�� */
    String PARAM_METHOD_STALL_THREAD_STATE = "javelin.method.stall.threadState";

    /** �X�g�[�����\�b�h���o�C�x���g�̃X�^�b�N�g���[�X�̃p�����[�^�� */
    String PARAM_METHOD_STALL_STACKTRACE = "javelin.method.stall.stackTrace";
}
