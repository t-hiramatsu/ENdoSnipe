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
package jp.co.acroquest.endosnipe.common.parser;

/**
 * Javelin ���O�̂��߂̒萔�C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author y-komori
 */
public interface JavelinLogConstants
{
    /**
     * �ڍ׏��擾�L�[:ThreadMXBean#getCurrentThreadCpuTime�p�����[�^
     * ���݂̃X���b�h�̍��v CPU ���Ԃ��i�m�b�P�ʂŕԂ��܂��B
     */
    String JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME = "thread.currentThreadCpuTime";

    /**
     * �ڍ׏��擾�L�[:ThreadMXBean#getCurrentThreadCpuTime�p�����[�^�̍���
     * ���݂̃X���b�h�̍��v CPU ���Ԃ̍������i�m�b�P�ʂŕԂ��܂��B
     */
    String JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA = "thread.currentThreadCpuTime.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getCurrentThreadUserTime�p�����[�^
     * ���݂̃X���b�h�����[�U���[�h�Ŏ��s���� CPU ���� (�i�m�b�P��) ��Ԃ��܂��B
     */
    String JMXPARAM_THREAD_CURRENT_THREAD_USER_TIME = "thread.currentThreadUserTime";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getCurrentThreadUserTime�p�����[�^�̍���
     * ���݂̃X���b�h�����[�U���[�h�Ŏ��s���� CPU ���� (�i�m�b�P��) �̍�����Ԃ��܂��B
     */
    String JMXPARAM_THREAD_CURRENT_THREAD_USER_TIME_DELTA = "thread.currentThreadUserTime.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getBlockedCount�p�����[�^
     * ���� ThreadInfo �Ɋ֘A����X���b�h���A���j�^�[�ɓ��邩�A�ē�����̂��u���b�N�������v�񐔂�Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_BLOCKED_COUNT = "thread.threadInfo.blockedCount";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getBlockedCount�p�����[�^�̍���
     * ���� ThreadInfo �Ɋ֘A����X���b�h���A���j�^�[�ɓ��邩�A�ē�����̂��u���b�N�������v�񐔂̍�����Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_BLOCKED_COUNT_DELTA = "thread.threadInfo.blockedCount.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getBlockedTime�p�����[�^
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h�����j�^�[�ɓ��邩
     * �ē�����̂��u���b�N�������悻�̗ݐόo�ߎ��� (�~���b�P��) ��Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_BLOCKED_TIME = "thread.threadInfo.blockedTime";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getBlockedTime�p�����[�^�̍���
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h�����j�^�[�ɓ��邩
     * �ē�����̂��u���b�N�������悻�̗ݐόo�ߎ��� (�~���b�P��) �̍�����Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_BLOCKED_TIME_DELTA = "thread.threadInfo.blockedTime.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getWaitedCount�p�����[�^
     * ���� ThreadInfo �Ɋ֘A����X���b�h���ʒm��ҋ@�������v�񐔂�Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_WAITED_COUNT = "thread.threadInfo.waitedCount";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getWaitedCount�p�����[�^�̍���
     * ���� ThreadInfo �Ɋ֘A����X���b�h���ʒm��ҋ@�������v�񐔂̍�����Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_WAITED_COUNT_DELTA = "thread.threadInfo.waitedCount.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getWaitedTime�p�����[�^
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h���ʒm��ҋ@����
     * ���悻�̗ݐόo�ߎ��� (�~���b�P��) ��Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_WAITED_TIME = "thread.threadInfo.waitedTime";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getWaitedTime�p�����[�^�̍���
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h���ʒm��ҋ@����
     * ���悻�̗ݐόo�ߎ��� (�~���b�P��) �̍�����Ԃ��܂��B
     */
    String JMXPARAM_THREAD_THREADINFO_WAITED_TIME_DELTA = "thread.threadInfo.waitedTime.delta";

    /** 
     * �ڍ׏��擾�L�[:GarbageCollectorMXBean#getCollectionCount�p�����[�^
     *���������R���N�V�����̍��v����Ԃ��܂��B
     */
    String JMXPARAM_GARBAGECOLLECTOR_COLLECTION_COUNT = "garbageCollector.collectionCount";

    /** 
     * �ڍ׏��擾�L�[:GarbageCollectorMXBean#getCollectionCount�p�����[�^�̍���
     * ���������R���N�V�����̍��v����Ԃ��܂��B
     */
    String JMXPARAM_GARBAGECOLLECTOR_COLLECTION_COUNT_DELTA =
            "garbageCollector.collectionCount.delta";

    /** 
     * �ڍ׏��擾�L�[:GarbageCollectorMXBean#getCollectionTime�p�����[�^
     * �R���N�V�����̂��悻�̗ݐόo�ߎ��� (�~���b�P��) ��Ԃ��܂��B
     */
    String JMXPARAM_GARBAGECOLLECTOR_COLLECTION_TIME = "garbageCollector.collectionTime";

    /** 
     * �ڍ׏��擾�L�[:GarbageCollectorMXBean#getCollectionTime�p�����[�^�̍���
     * �R���N�V�����̂��悻�̗ݐόo�ߎ��� (�~���b�P��) ��Ԃ��܂��B
     */
    String JMXPARAM_GARBAGECOLLECTOR_COLLECTION_TIME_DELTA =
            "garbageCollector.collectionTime.delta";

    /** 
     * �ڍ׏��擾�L�[:MemoryPoolMXBean#getPeakUsage#getUsage�p�����[�^
     * Java ���z�}�V�����N������Ă���A�܂��̓s�[�N�����Z�b�g����Ă���́A���̃������v�[���̃s�[�N�������g�p�ʂ�Ԃ��܂�
     */
    String JMXPARAM_MEMORYPOOL_PEAKUSAGE_USAGE = "memoryPool.peakUsage.usage";

    /** 
     * �ڍ׏��擾�L�[:MemoryPoolMXBean#getPeakUsage#getUsage�p�����[�^�̍���
     * Java ���z�}�V�����N������Ă���A�܂��̓s�[�N�����Z�b�g����Ă���́A���̃������v�[���̃s�[�N�������g�p�ʂ�Ԃ��܂�
     */
    String JMXPARAM_MEMORYPOOL_PEAKUSAGE_USAGE_DELTA = "memoryPool.peakUsage.usage.delta";

    /** 
     * �ڍ׏��擾�L�[:MemoryMXBean#getHeapMemoryUsage�p�����[�^
     * ���\�b�h�Ăяo�����̃q�[�v�������g�p�ʂ��擾���܂��B
     */
    String JMXPARAM_MEMORY_HEAPMEMORYUSAGE_START = "memory.heapMemoryUsage.start";

    /** 
     * �ڍ׏��擾�L�[:MemoryMXBean#getHeapMemoryUsage�p�����[�^
     * ���\�b�h�I�����̃q�[�v�������g�p�ʂ��擾���܂��B
     */
    String JMXPARAM_MEMORY_HEAPMEMORYUSAGE_END = "memory.heapMemoryUsage.end";

    /** 
     * �ڍ׏��擾�L�[:���\�b�h��TAT
     */
    String EXTRAPARAM_DURATION = "duration";

    /**
     * �ڍ׏��擾�L�[�F���\�b�h�̏���ԁi�q���\�b�h�Ăяo�����Ԃ��������������\�b�h���s���ԁj
     */
    String EXTRAPARAM_ELAPSEDTIME = "elapsedTime";

    /** 
     * �ڍ׏��擾�L�[:���\�b�h��CPU���ԁi�q���\�b�h�Ăяo��CPU���Ԃ��������������\�b�hCPU���ԁj
     */
    String EXTRAPARAM_PURECPUTIME = "pureCpuTimeDelta";

    /** 
     * �ڍ׏��擾�L�[:���\�b�h��Wait���ԁi�q���\�b�h�Ăяo��Wait���Ԃ��������������\�b�hCPU���ԁj
     */
    String EXTRAPARAM_PUREWAITEDTIME = "pureWaitedTimeDelta";

    /** 
     * �ڍ׏��擾�L�[:���\�b�h��USER���ԁi�q���\�b�h�Ăяo��USER���Ԃ��������������\�b�hUSER���ԁj
     */
    String EXTRAPARAM_PUREUSERTIME = "pureUserTimeDelta";

    /** 
     * �ڍ׏��擾�L�[:CPU�̃A�C�h�����ԁi���s���Ԃ���CPU���Ԃ����������ԁj
     */
    String EXTRAPARAM_IDLETIME = "IdleTime";

    /** 
     * �ڍ׏��擾�L�[:�������{���̃t�@�C�����͗�
     */
    String IOPARAM_DISK_INPUT = "file.currentFileReadLength";

    /** 
     * �ڍ׏��擾�L�[:�������{���̃t�@�C���o�͗�
     */
    String IOPARAM_DISK_OUTPUT = "file.currentFileWriteLength";

    /** 
     * �ڍ׏��擾�L�[:�������{���̃l�b�g���[�N���͗�
     */
    String IOPARAM_NETWORK_INPUT = "net.currentThreadReadLength";

    /** 
     * �ڍ׏��擾�L�[:�������{���̃l�b�g���[�N�o�͗�
     */
    String IOPARAM_NETWORK_OUTPUT = "net.currentThreadWriteLength";

    /** ��O�����^�O�B*/
    String JAVELIN_EXCEPTION = "<<javelin.Exception>>";

    /** �X�^�b�N�g���[�X�o�͂̊J�n�^�O�B*/
    String JAVELIN_STACKTRACE_START = "<<javelin.StackTrace_START>>";

    /** �X�^�b�N�g���[�X�o�͂̏I���^�O�B*/
    String JAVELIN_STACKTRACE_END = "<<javelin.StackTrace_END>>";

    /** �t�B�[���h�l�o�͂̊J�n�^�O�B*/
    String JAVELIN_FIELDVALUE_START = "<<javelin.FieldValue_START>>";

    /** �t�B�[���h�l�o�͂̏I���^�O�B*/
    String JAVELIN_FIELDVALUE_END = "<<javelin.FieldValue_END>>";

    /** �߂�l�o�͂̊J�n�^�O�B*/
    String JAVELIN_RETURN_START = "<<javelin.Return_START>>";

    /** �߂�l�o�͂̏I���^�O�B*/
    String JAVELIN_RETURN_END = "<<javelin.Return_END>>";

    /** �����o�͂̊J�n�^�O�B*/
    String JAVELIN_ARGS_START = "<<javelin.Args_START>>";

    /** �����o�͂̏I���^�O�B*/
    String JAVELIN_ARGS_END = "<<javelin.Args_END>>";

    /** JMX�ɂ��擾����VM�̏�ԏo�͂̊J�n�^�O�B*/
    String JAVELIN_JMXINFO_START = "<<javelin.JMXInfo_START>>";

    /** JMX�ɂ��擾����VM�̏�ԏo�͂̏I���^�O�B*/
    String JAVELIN_JMXINFO_END = "<<javelin.JMXInfo_END>>";

    /** �ǉ����o�͂̊J�n�^�O�B*/
    String JAVELIN_EXTRAINFO_START = "<<javelin.ExtraInfo_START>>";

    /** �ǉ����o�͂̏I���^�O�B*/
    String JAVELIN_EXTRAINFO_END = "<<javelin.ExtraInfo_END>>";

    /** �C�x���g�p�����[�^�o�͂̊J�n�^�O�B*/
    String JAVELIN_EVENTINFO_START = "<<javelin.EventInfo_START>>";

    /** �C�x���g�p�����[�^�o�͂̏I���^�O�B*/
    String JAVELIN_EVENTINFO_END = "<<javelin.EventInfo_END>>";

    /** ���샍�O�o�͓����̃t�H�[�}�b�g�B*/
    String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";

    /** �C�x���g�̌x�����x���FInfo */
    String EVENT_INFO = "INFO";

    /** �C�x���g�̌x�����x���FWarn */
    String EVENT_WARN = "WARN";

    /** �C�x���g�̌x�����x���FWarn */
    String EVENT_ERROR = "ERROR";

    /** ���O�o�͎��AparentNode�����݂��Ȃ��ꍇ�̃f�t�H���g���\�b�h���� */
    String DEFAULT_LOGMETHOD = "unknown";

}
