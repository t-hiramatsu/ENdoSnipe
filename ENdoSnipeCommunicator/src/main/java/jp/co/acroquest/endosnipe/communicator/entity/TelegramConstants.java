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
package jp.co.acroquest.endosnipe.communicator.entity;

/**
 * �d���Ɋւ���萔�C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author y-komori
 */
public interface TelegramConstants
{
	/** �d����ʁi��Ԓʒm�j */
	byte BYTE_TELEGRAM_KIND_ALERT = 0;

	/** �d����ʁi��Ԏ擾�j */
	byte BYTE_TELEGRAM_KIND_GET = 1;

	/** �d����ʁi���Z�b�g�j */
	byte BYTE_TELEGRAM_KIND_RESET = 2;

	/** �d����ʁi���\�[�X�ʒm�j */
	byte BYTE_TELEGRAM_KIND_RESOURCENOTIFY = 3;

	/** �d����ʁi�ݒ�ύX�j */
	byte BYTE_TELEGRAM_KIND_CONFIGCHANGE = 4;

	/** �d����ʁi�@�\�Ăяo���j */
	byte BYTE_TELEGRAM_KIND_FUNCTIONCALL = 5;

	/** �d�����(JVN���O�_�E�����[�h) */
	byte BYTE_TELEGRAM_KIND_JVN_FILE = 6;

	/** �d�����(�T�[�o�v���p�e�B�ݒ�擾) */
	byte BYTE_TELEGRAM_KIND_GET_PROPERTY = 7;

	/** �d�����(�T�[�o�v���p�e�B�ݒ�X�V) */
	byte BYTE_TELEGRAM_KIND_UPDATE_PROPERTY = 8;

	/** �d�����(JVN���O�ꗗ�擾) */
	byte BYTE_TELEGRAM_KIND_JVN_FILE_LIST = 9;

	/** �d�����(�v���ΏۍX�V) */
	byte BYTE_TELEGRAM_KIND_UPDATE_TARGET = 10;

	/** �d�����(�_���v�擾) */
	byte BYTE_TELEGRAM_KIND_GET_DUMP = 11;

	/** �d�����(�N���X�폜) */
	byte BYTE_TELEGRAM_KIND_REMOVE_CLASS = 12;

	/** �d�����(臒l���߃A���[��/�����A���[���ʒm) */
	byte BYTE_TELEGRAM_SIGNAL_STATE_CHANGE = 13;

	/** �d�����(�ڑ��ʒm) */
	byte BYTE_TELEGRAM_KIND_CONNECT_NOTIFY = 14;

	/** �d�����(JMX���ڒʒm) */
	byte BYTE_TELEGRAM_KIND_JMX = 15;

	/** �d�����(DB�������ʒm) */
	byte BYTE_TELEGRAM_KIND_ADD_DATABASE_NAME = 16;

	/** �d�����(DB�������ʒm) */
	byte BYTE_TELEGRAM_KIND_DEL_DATABASE_NAME = 17;

    /** �d�����(�V�O�i����Ԏ擾) */
    byte BYTE_TELEGRAM_KIND_SIGNAL_STATE = 21;

    /** �d�����(�V�O�i����`�X�V) */
    byte BYTE_TELEGRAM_KIND_SIGNAL_DEFINITION = 22;
    
    /** �d�����(�c���[��`�X�V) */
    byte BYTE_TELEGRAM_KIND_TREE_DEFINITION = 23;

	/** �Ō�̓d���ȊO */
	byte HALFWAY_TELEGRAM = 0;

	/** �Ō�̓d�� */
	byte FINAL_TELEGRAM = 1;

	/** �v��������ʁi�ʒm�j */
	byte BYTE_REQUEST_KIND_NOTIFY = 0;

	/** �v��������ʁi�v���j */
	byte BYTE_REQUEST_KIND_REQUEST = 1;

	/** �v��������ʁi�����j */
	byte BYTE_REQUEST_KIND_RESPONSE = 2;

	/** �I�u�W�F�N�g���i���\�[�X�ʒm�ł̃��\�[�X�l�j */
	String OBJECTNAME_RESOURCE = "resources";

	/** �I�u�W�F�N�g���i���\�[�X�ʒm�ł̎����j */
	String TIME_RESOURCE = "resource-time";

	/** �I�u�W�F�N�g���i�@�\�Ăяo���ł̐ڑ��Ǘ��j */
	String OBJECTNAME_CONNECTIONMANAGER = "connectionManager";

	/** �I�u�W�F�N�g���i�@�\�Ăяo���ł̕\������j */
	String OBJECTNAME_VIEWOPERATION = "viewOperation";

	/** �I�u�W�F�N�g���i�@�\�Ăяo���ł̋��� Full GC ���s�j */
	String OBJECTNAME_FORCEFULLGC = "forceFullGC";

	/** �I�u�W�F�N�g���iJVN�t�@�C���j */
	String OBJECTNAME_JVN_FILE = "jvnFile";

	/** �I�u�W�F�N�g��(�_���v�擾) */
	String OBJECTNAME_DUMP = "dump";

    /** �I�u�W�F�N�g��(�v���A���[��) */
    String OBJECTNAME_RESOURCEALARM = "resourceAlarm";

    /** �I�u�W�F�N�g��(�V�O�i����`�ύX) */
    String OBJECTNAME_SIGNAL_CHANGE = "signalDefinitionChange";
    
    /** �I�u�W�F�N�g��(�c���[��`�ύX) */
    String OBJECTNAME_TREE_CHANGE = "treeDefinitionChange";

	/** �I�u�W�F�N�g���i�ڑ����ʒm�ł̐ڑ����j */
	String OBJECTNAME_CONNECTINFO = "connectInformation";

	/** �I�u�W�F�N�g��(JMX�I�u�W�F�N�g��) */
	String OBJECTNAME_JMX = "jmx";

	/** �I�u�W�F�N�g��(DB��) */
	String OBJECTnAME_DATABASE_NAME = "databaseName";

	// ////////////////
	// ����p���ږ� //
	// ////////////////

	/** ���ږ��i�����������j */
	String ITEMNAME_INITIALIZE = "/initialize";

	/** ���ږ�(�q�[�v�_���v�擾) */
	String ITEMNAME_HEAPDUMP = "/heapDump";

	/** ���ږ�(�X���b�h�_���v�擾) */
	String ITEMNAME_THREADDUMP = "/threadDump";

	/** ���ږ�(�N���X�q�X�g�O�����擾) */
	String ITEMNAME_CLASSHISTOGRAM = "classHistogramDump";

	/** ���ږ�(�Z�b�V�����_���v�擾) */
	String ITEMNAME_SESSIONDUMP = "sessionDump";

	/** ���ږ�(�@�\�Ăяo���ł̋��� Full GC) */
	String ITEMNAME_FORCEFULLGC = "forceFullGC";

	// ////////////////
	// �擾�p���ږ� //
	// ////////////////
	// ���ږ�(DB����擾����n����w�肷��L�[������)

	// -----------------------------------------------------
	// ��b����

	/** ���ږ��i�����j */
	// String ITEMNAME_TIME = "/time";
	String ITEMNAME_TIME = "/common/fundamental/time/current";

	/** ���ږ��i���\�[�X�l�ł̎擾�����j */
	// String ITEMNAME_ACQUIREDTIME = "/acquiredTime";
	String ITEMNAME_ACQUIREDTIME = "/common/fundamental/time/acquired";

	/** ���ږ�(�Ăяo����) */
	// String ITEMNAME_CALL_COUNT = "/callCount";
	String ITEMNAME_CALL_COUNT = "/common/fundamental/callCount";

	/** ���ږ�(����̏�������) */
	// String ITEMNAME_CURRENT_INTERVAL = "/currentInterval";
	String ITEMNAME_CURRENT_INTERVAL = "/common/fundamental/interval/current";

	/** ���ږ�(���v��������) */
	// String ITEMNAME_TOTAL_INTERVAL = "/totalInterval";
	String ITEMNAME_TOTAL_INTERVAL = "/common/fundamental/interval/total";

	/** ���ږ�(�ő又������) */
	// String ITEMNAME_MAXIMUM_INTERVAL = "/maximumInterval";
	String ITEMNAME_MAXIMUM_INTERVAL = "/common/fundamental/interval/max";

	/** ���ږ�(�ŏ���������) */
	// String ITEMNAME_MINIMUM_INTERVAL = "/minimumInterval";
	String ITEMNAME_MINIMUM_INTERVAL = "/common/fundamental/interval/min";

	/** ���ږ�(���vCPU����) */
	// String ITEMNAME_TOTAL_CPU_INTERVAL = "/totalCpuInterval";
	String ITEMNAME_TOTAL_CPU_INTERVAL = "/common/fundamental/cpuInterval/total";

	/** ���ږ�(�ő�CPU����) */
	// String ITEMNAME_MAXIMUM_CPU_INTERVAL = "/maximumCpuInterval";
	String ITEMNAME_MAXIMUM_CPU_INTERVAL = "/common/fundamental/cpuInterval/max";

	/** ���ږ�(�ŏ�CPU����) */
	// String ITEMNAME_MINIMUM_CPU_INTERVAL = "/minimumCpuInterval";
	String ITEMNAME_MINIMUM_CPU_INTERVAL = "/common/fundamental/cpuInterval/min";

	/** ���ږ�(���v���[�U����) */
	// String ITEMNAME_TOTAL_USER_INTERVAL = "/totalUserInterval";
	String ITEMNAME_TOTAL_USER_INTERVAL = "/common/fundamental/userInterval/total";

	/** ���ږ�(�ő僆�[�U����) */
	// String ITEMNAME_MAXIMUM_USER_INTERVAL = "/maximumUserInterval";
	String ITEMNAME_MAXIMUM_USER_INTERVAL = "/common/fundamental/userInterval/max";

	/** ���ږ�(�ŏ����[�U����) */
	// String ITEMNAME_MINIMUM_USER_INTERVAL = "/minimumUserInterval";
	String ITEMNAME_MINIMUM_USER_INTERVAL = "/common/fundamental/userInterval/min";

	// -----------------------------------------------------
	// �ݐϒl

	/** ���ږ�(���v��������) */
	// String ITEMNAME_ACCUMULATED_TOTAL_INTERVAL = "totalAccumulatedInterval";
	String ITEMNAME_ACCUMULATED_TOTAL_INTERVAL = "/common/accumulated/interval/total";

	/** ���ږ�(�ő又������) */
	// String ITEMNAME_ACCUMULATED_MAXIMUM_INTERVAL =
	// "maximumAccumulatedInterval";
	String ITEMNAME_ACCUMULATED_MAXIMUM_INTERVAL = "/common/accumulated/interval/max";

	/** ���ږ�(�ŏ���������) */
	// String ITEMNAME_ACCUMULATED_MINIMUM_INTERVAL =
	// "minimumAccumulatedInterval";
	String ITEMNAME_ACCUMULATED_MINIMUM_INTERVAL = "/common/accumulated/interval/min";

	/** ���ږ�(���vCPU����) */
	// String ITEMNAME_ACCUMULATED_TOTAL_CPU_INTERVAL =
	// "totalAccumulatedCpuInterval";
	String ITEMNAME_ACCUMULATED_TOTAL_CPU_INTERVAL = "/common/accumulated/cpuInterval/total";

	/** ���ږ�(�ő�CPU����) */
	// String ITEMNAME_ACCUMULATED_MAXIMUM_CPU_INTERVAL =
	// "maximumAccumulatedCpuInterval";
	String ITEMNAME_ACCUMULATED_MAXIMUM_CPU_INTERVAL = "/common/accumulated/cpuInterval/max";

	/** ���ږ�(�ŏ�CPU����) */
	// String ITEMNAME_ACCUMULATED_MINIMUM_CPU_INTERVAL =
	// "minimumAccumulatedCpuInterval";
	String ITEMNAME_ACCUMULATED_MINIMUM_CPU_INTERVAL = "/common/accumulated/cpuInterval/min";

	/** ���ږ�(���vCPU����) */
	// String ITEMNAME_ACCUMULATED_TOTAL_USER_INTERVAL =
	// "totalAccumulatedUserInterval";
	String ITEMNAME_ACCUMULATED_TOTAL_USER_INTERVAL = "/common/accumulated/userInterval/total";

	/** ���ږ�(�ő�CPU����) */
	// String ITEMNAME_ACCUMULATED_MAXIMUM_USER_INTERVAL =
	// "maximumAccumulatedUserInterval";
	String ITEMNAME_ACCUMULATED_MAXIMUM_USER_INTERVAL = "/common/accumulated/userInterval/max";

	/** ���ږ�(�ŏ�CPU����) */
	// String ITEMNAME_ACCUMULATED_MINIMUM_USER_INTERVAL =
	// "minimumAccumulatedUserInterval";
	String ITEMNAME_ACCUMULATED_MINIMUM_USER_INTERVAL = "/common/accumulated/userInterval/min";

	// -----------------------------------------------------
	// �V�X�e������: ��b�l

	/** ���ږ��i���\�[�X�l�ł̃v���Z�b�T���j */
	// String ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT =
	// "/system.cpu.processor.count";
	String ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT = "/system/cpu/processor/number";

	/** ���ږ��i���[�U���[�h�ł�CPU�g�p�ʁj */
	// String ITEMNAME_SYSTEM_CPU_USERMODE_TIME = "/system.cpu.usermode.time";
	String ITEMNAME_SYSTEM_CPU_USERMODE_TIME = "/system/cpu/time/user(d)";

	/** ���ږ��i�V�X�e�����[�h�ł�CPU�g�p�ʁj */
	// String ITEMNAME_SYSTEM_CPU_SYSTEM_TIME = "/system.cpu.system.time";
	String ITEMNAME_SYSTEM_CPU_SYSTEM_TIME = "/system/cpu/time/system(d)";

	/** ���ږ��iiowait�ł�CPU�g�p�ʁj */
	// String ITEMNAME_SYSTEM_CPU_IOWAIT_TIME = "/system.cpu.iowait.time";
	String ITEMNAME_SYSTEM_CPU_IOWAIT_TIME = "/system/cpu/time/iowait(d)";

	// -----------------------------------------------------
	// �V�X�e������: CPU(�Z�o�l)

	/** ���ږ��iCPU�g�p���i�V�X�e���j�̍��v�j */
	// String ITEMNAME_SYSTEM_CPU_TOTAL_USAGE = "/system.cpu.total.usage";
	String ITEMNAME_SYSTEM_CPU_TOTAL_USAGE = "/system/cpu/usage/total:%";

	/** ���ږ��iCPU�g�p���i�V�X�e���j�̂����̃V�X�e���̎g�p���j */
	// String ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE = "/system.cpu.system.usage";
	String ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE = "/system/cpu/usage/system:%";

	/** ���ږ��iCPU�g�p���i�V�X�e���j�̂�����I/O Wait�̎g�p���j */
	// String ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE = "/system.cpu.iowait.usage";
	String ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE = "/system/cpu/usage/iowait:%";

	/** ���ږ��iCPU���Ƃ̕��ׁF����CPU�R�A�ԍ�(1�`N)��t����j */
	// String ITEMNAME_CPU_ARRAY = "/sys_cputime_1";
	String ITEMNAME_CPU_ARRAY = "/system/cpu/load(d)";

	// -----------------------------------------------------
	// �V�X�e������: ������(�Z�o�l)

	/** ���ږ��i�V�X�e���S�̂̃������ő�l�j */
	// String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX =
	// "/system.memory.physical.max";
	String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX = "/system/memory/physical/max:bytes";

	/** ���ږ��i�V�X�e���S�̂̋󂫃������j */
	// String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE =
	// "/system.memory.physical.free";
	String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE = "/system/memory/physical/free:bytes";

	/** ���ږ��i�V�X�e���S�̂̋󂫃������j */
	// String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_USED =
	// "/system.memory.physical.used";
	String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_USED = "/system/memory/physical/used:bytes";

	/** ���ږ��i���\�[�X�l�ł̉��z�������e�ʁj */
	// String ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED =
	// "/system.memory.virtual.used";
	String ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED = "/system/memory/virtual/used:bytes";

	/** ���ږ��i���\�[�X�l�ł̃X���b�v�̈�e�ʁj */
	// String ITEMNAME_SYSTEM_MEMORY_SWAP_MAX = "/system.memory.swap.max";
	String ITEMNAME_SYSTEM_MEMORY_SWAP_MAX = "/system/memory/swap/max:bytes";

	/** ���ږ��i���\�[�X�l�ł̃X���b�v�̈�󂫗e�ʁj */
	// String ITEMNAME_SYSTEM_MEMORY_SWAP_FREE = "/system.memory.swap.free";
	String ITEMNAME_SYSTEM_MEMORY_SWAP_FREE = "/system/memory/swap/free:bytes";

	/** ���ږ��ipage in�j */
	// String ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT =
	// "/system.memory.pagein.count";
	String ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT = "/system/memory/pagein(d)";

	/** ���ږ��ipage out�j */
	// String ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT =
	// "/system.memory.pageout.count";
	String ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT = "/system/memory/pageout(d)";

	// -----------------------------------------------------
	// �V�X�e������: �t�@�C��

	/** ���ږ��i���\�[�X�l�ł́A�t�@�C�����͗ʁj */
	// String ITEMNAME_FILEINPUTSIZEOFSYSTEM = "/system.file.input";
	String ITEMNAME_FILEINPUTSIZEOFSYSTEM = "/system/file/read:bytes(d)";

	/** ���ږ��i���\�[�X�l�ł́A�t�@�C���o�͗ʁj */
	// String ITEMNAME_FILEOUTPUTSIZEOFSYSTEM = "/system.file.output";
	String ITEMNAME_FILEOUTPUTSIZEOFSYSTEM = "/system/file/write:bytes(d)";

	/** ���ږ��iFD/�n���h�����j */
	// String ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER =
	// "/system.handle.total.number";
	String ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER = "/system/file/handle/number";

	// -----------------------------------------------------
	// �v���Z�X����: CPU(��b�l)

	/** ���ږ��iCPU�g�p����:total�j */
	// String ITEMNAME_PROCESS_CPU_TOTAL_TIME = "/process.cpu.total.time";
	String ITEMNAME_PROCESS_CPU_TOTAL_TIME = "/process/cpu/time/total(d)";

	/** ���ږ��iCPU�g�p����:system�j */
	// String ITEMNAME_PROC_CPUTIME_SYS = "/process.cpu.system.time";
	String ITEMNAME_PROCESS_CPU_SYSTEM_TIME = "/process/cpu/time/system(d)";

	/** ���ږ��iCPU�g�p����:iowait�j */
	// String ITEMNAME_PROCESS_CPU_IOWAIT_TIME = "/process.cpu.iowait.time";
	String ITEMNAME_PROCESS_CPU_IOWAIT_TIME = "/process/cpu/time/iowait(d)";

	/** ���ږ��i���\�[�X�l�ł�Java�ғ����ԁj */
	// String ITEMNAME_JAVAUPTIME = "/javaUpTime";
	String ITEMNAME_JAVAUPTIME = "/process/fundamental/uptime";

	// -----------------------------------------------------
	// �v���Z�X����: CPU(�Z�o�l)

	/** ���ږ��iCPU�g�p���i�v���Z�X�j�̍��v�j */
	// String ITEMNAME_PROCESS_CPU_TOTAL_USAGE = "/process.cpu.total.usage";
	String ITEMNAME_PROCESS_CPU_TOTAL_USAGE = "/process/cpu/usage/total:%";

	/** ���ږ��iCPU�g�p���i�v���Z�X�j�̂����̃V�X�e���̎g�p���j */
	// String ITEMNAME_PROCESS_CPU_SYSTEM_USAGE = "/process.cpu.system.usage";
	String ITEMNAME_PROCESS_CPU_SYSTEM_USAGE = "/process/cpu/usage/system:%";

	/** ���ږ��iCPU�g�p���i�v���Z�X�j�̂�����IOWAIT�̎g�p���j */
	// String ITEMNAME_PROCESS_CPU_IOWAIT_USAGE = "/process.cpu.iowait.usage";
	String ITEMNAME_PROCESS_CPU_IOWAIT_USAGE = "/process/cpu/usage/iowait:%";

	// -----------------------------------------------------
	// �v���Z�X����: ������(�Z�o�l)

	/** ���ږ��i���\�[�X�l�ł̕����������e�ʁj */
	// String ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX =
	// "/process.memory.physical.max";
	String ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX = "/process/memory/physical/max:bytes";

	/** ���ږ��i�����������g�p�ʁi�v���Z�X�j�j */
	// String ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED =
	// "/process.memory.physical.used";
	String ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED = "/process/memory/physical/used:bytes";

	/** ���ږ��i���\�[�X�l�ł̕����������󂫗e�ʁj */
	// String ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE =
	// "/process.memory.physical.free";
	String ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE = "/process/memory/physical/free:bytes";

	/** ���ږ��i���\�[�X�l�ł̉��z�}�V���������e�ʁj */
	// String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX =
	// "/process.memory.virtualmachine.max";
	String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX = "/process/memory/virtual/max:bytes";

	/** ���ږ��i���z�������g�p�ʁi�v���Z�X�j�j */
	// String ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED =
	// "/process.memory.virtual.used";
	String ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED = "/process/memory/virtual/used:bytes";

	/** ���ږ��i���\�[�X�l�ł̉��z�}�V���������󂫗e�ʁj */
	// String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE =
	// "/process.memory.virtualmachine.free";
	String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE = "/process/memory/virtual/free:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�������R�~�b�g�e�ʁj */
	// String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT =
	// "/javaprocess.memory.heap.commit";
	String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT = "/process/heap/commit:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�������ő�j */
	// String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX =
	// "/javaprocess.memory.heap.max";
	String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX = "/process/heap/max:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�������g�p�ʁj */
	// String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED =
	// "/javaprocess.memory.heap.used";
	String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED = "/process/heap/used:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�ȊO�̃������R�~�b�g�e�ʁj */
	// String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT =
	// "/javaprocess.memory.nonheap.commit";
	String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT = "/process/nonheap/commit:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�ȊO�̃������ő�j */
	// String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX =
	// "/javaprocess.memory.nonheap.max";
	String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX = "/process/nonheap/max:bytes";

	/** ���ږ��i���\�[�X�l�ł̃q�[�v�ȊO�̃������g�p�ʁj */
	// String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED =
	// "/javaprocess.memory.nonheap.used";
	String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED = "/process/nonheap/used:bytes";

	/** ���ږ��i���W���[�t�H�[���g���j */
	// String ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT =
	// "/process.memory.majorfault.count";
	String ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT = "/process/memory/majorfault/count(d)";

	// -----------------------------------------------------
	// �v���Z�X����: �l�b�g���[�N

	/** ���ږ��i���\�[�X�l�ł́A�v���Z�X�S�̂̃l�b�g���[�N�f�[�^��M�ʁj */
	// String ITEMNAME_NETWORKINPUTSIZEOFPROCESS = "/proc.network.input";
	String ITEMNAME_NETWORKINPUTSIZEOFPROCESS = "/process/network/read:bytes(d)";

	/** ���ږ��i���\�[�X�l�ł́A�v���Z�X�S�̂̃l�b�g���[�N�f�[�^���M�ʁj */
	// String ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS = "/proc.network.output";
	String ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS = "/process/network/write:bytes(d)";

	// -----------------------------------------------------
	// �v���Z�X����: �t�@�C��

	/** ���ږ��i���\�[�X�l�ł́A�t�@�C�����͗ʁj */
	// String ITEMNAME_FILEINPUTSIZEOFPROCESS = "/proc.file.input";
	String ITEMNAME_FILEINPUTSIZEOFPROCESS = "/process/file/read:bytes(d)";

	/** ���ږ��i���\�[�X�l�ł́A�t�@�C���o�͗ʁj */
	// String ITEMNAME_FILEOUTPUTSIZEOFPROCESS = "/proc.file.output";
	String ITEMNAME_FILEOUTPUTSIZEOFPROCESS = "/process/file/write:bytes(d)";

	// -----------------------------------------------------
	// �v���Z�X����: �X���b�h

	/** ���ږ��i�X���b�h���j */
	// String ITEMNAME_PROCESS_THREAD_TOTAL_COUNT =
	// "/process.thread.total.count";
	String ITEMNAME_PROCESS_THREAD_TOTAL_COUNT = "/process/thread/native";

	/** ���ږ��iFD/�n���h�����j */
	// String ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER =
	// "/process.handle.total.number";
	String ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER = "/process/file/handle/number";

	/** ���ږ��i���\�[�X�l�ł́A�X���b�h���j */
	// String ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT =
	// "/javaprocess.thread.total.count";
	String ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT = "/process/thread/java";

	// -----------------------------------------------------
	// �v���Z�X���: �R���N�V�������

	/** ���ږ��i���\�[�X�l�ł́A�R���N�V�����̐��j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT =
	// "/javaprocess.collection.list.count";
	String ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT = "/process/collection/list";

	/** ���ږ��i���\�[�X�l�ł́A�R���N�V�����̐��j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_QUEUE_COUNT =
	// "/javaprocess.collection.queue.count";
	String ITEMNAME_JAVAPROCESS_COLLECTION_QUEUE_COUNT = "/process/collection/queue";

	/** ���ږ��i���\�[�X�l�ł́A�R���N�V�����̐��j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_SET_COUNT =
	// "/javaprocess.collection.set.count";
	String ITEMNAME_JAVAPROCESS_COLLECTION_SET_COUNT = "/process/collection/set";

	/** ���ږ��i���\�[�X�l�ł́A�R���N�V�����̐��j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_MAP_COUNT =
	// "/javaprocess.collection.map.count";
	String ITEMNAME_JAVAPROCESS_COLLECTION_MAP_COUNT = "/process/collection/map";

	/** ���ږ��i���\�[�X�l�ł́A�N���X�q�X�g�O��������擾�����I�u�W�F�N�g�̃T�C�Y�j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_SIZE =
	// "/javaprocess.collection.histogram.size";
	String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_SIZE = "/process/histogram/object/size:bytes";

	/** ���ږ��i���\�[�X�l�ł́A�N���X�q�X�g�O��������擾�����I�u�W�F�N�g�̐��j */
	// String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_COUNT =
	// "/javaprocess.collection.histogram.count";
	String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_COUNT = "/process/histogram/object/number";

	// -----------------------------------------------------
	// �v���Z�X���: ���X�|���X���

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̕��ϒl�j */
	// String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE =
	// "/process.response.time.average";
	String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE = "/process/response/total/average";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ő�l�j */
	// String ITEMNAME_PROCESS_RESPONSE_TIME_MAX = "/process.response.time.max";
	String ITEMNAME_PROCESS_RESPONSE_TIME_MAX = "/process/response/total/max";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ŏ��l�j */
	// String ITEMNAME_PROCESS_RESPONSE_TIME_MIN = "/process.response.time.min";
	String ITEMNAME_PROCESS_RESPONSE_TIME_MIN = "/process/response/total/min";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̌Ăяo���񐔁j */
	// String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT =
	// "/process.response.total.count";
	String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT = "/process/response/total/count";
	
	/** �ړ����i���\�[�X�l�ł́Aevent�̔����񐔁j */
	String PREFIX_PROCESS_RESPONSE_EVENT = "/process/response/page/event";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̕��ϒl(SQL�ȊO)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE_EXCL_SQL = "/process/response/nosql/average";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ő�l(SQL�ȊO)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_MAX_EXCL_SQL = "/process/response/nosql/max";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ŏ��l(SQL�ȊO)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_MIN_EXCL_SQL = "/process/response/nosql/min";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̌Ăяo����(SQL�ȊO)�j */
	String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT_EXCL_SQL = "/process/response/nosql/count";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̕��ϒl(SQL�̂�)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE_ONLY_SQL = "/process/response/sql/average";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ő�l(SQL�̂�)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_MAX_ONLY_SQL = "/process/response/sql/max";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̍ŏ��l(SQL�̂�)�j */
	String ITEMNAME_PROCESS_RESPONSE_TIME_MIN_ONLY_SQL = "/process/response/sql/min";

	/** ���ږ��i���\�[�X�l�ł́ATurn Around Time�̌Ăяo����(SQL�̂�)�j */
	String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT_ONLY_SQL = "/process/response/sql/count";

	/** ���ږ�(HTTP��O) */
	// String ITEMNAME_JAVAPROCESS_HTTP_EXCEPTION =
	// "/javaprocess.http.exception.occurence.count";
	String ITEMNAME_JAVAPROCESS_HTTP_EXCEPTION = "/process/response/http/thrown";

	// -----------------------------------------------------
	// �v���Z�X���: VM

	/** ���ږ�(Java ���z�}�V�������s���J�n���Ă��烍�[�h���ꂽ�N���X�̍��v��) */
	// String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL =
	// "/javaprocess.classloader.class.total";
	String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL = "/process/classloader/class/total";

	/** ���ږ�(Java ���z�}�V���Ɍ��݃��[�h����Ă���N���X�̐�) */
	// String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT =
	// "/javaprocess.classloader.class.current";
	String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT = "/process/classloader/class/current";

	/** ���ږ��i���\�[�X�l�ł́A�g�[�^���̃K�x�[�W�R���N�V�����̎��ԁj */
	// String ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL = "/javaprocess.gc.time.total";
	String ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL = "/process/gc/time/total(d)";

	/** ���ږ��i���\�[�X�l�ł́A�t�@�C�i���C�Y�҂��I�u�W�F�N�g���j */
	// String ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT =
	// "/javaprocess.gc.finalizequeue.count";
	String ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT = "/process/gc/finalizequeue/number";

	/** ���ږ�(��O������) */
	// String ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT =
	// "/javaprocess.exception.occurence.count";
	String ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT = "/process/response/java/error";

	/** ���ږ�(�X�g�[��������) */
	// String ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT =
	// "/javaprocess.method.stall.count";
	String ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT = "/process/response/java/stalled";

	// -----------------------------------------------------
	// �v���Z�X���: AP�T�[�o���

	/** ���ږ��iAP�T�[�o�̃��[�J�[�X���b�h�v�[��(�ő吔,�ғ���)�j */
	// String ITEMNAME_SERVER_POOL = "/serverPool";
	String ITEMNAME_SERVER_POOL = "/process/apserver/worker/number";

	/** ���ږ��i�v�[��(�ő吔,�ғ���)�j */
	// String ITEMNAME_POOL_SIZE = "/poolSize";
	String ITEMNAME_POOL_SIZE = "/process/commons/poolsize";

	/** ���ږ��i���\�[�X�l�ł́AHttpSession���j */
	// String ITEMNAME_HTTPSESSION_NUM = "/httpSessionNumber";
	String ITEMNAME_HTTPSESSION_NUM = "/process/httpsession/instance/number";

	/** ���ږ��i���\�[�X�l�ł́AHttpSession���T�C�Y�j */
	// String ITEMNAME_HTTPSESSION_TOTALSIZE = "/httpSessionTotalSize";
	String ITEMNAME_HTTPSESSION_TOTALSIZE = "/process/httpsession/size/total:bytes";

	// -----------------------------------------------------
	// Agent���

	/** ���ږ��i�J�o���b�W�j */
	// String ITEMNAME_COVERAGE = "/coverage";
	String ITEMNAME_COVERAGE = "/javelin/converter/coverage:%";

	/** ���ږ��i�C�x���g��ʖ��̃C�x���g�����񐔁j */
	// String ITEMNAME_EVENT_COUNT = "/eventCount";
	String ITEMNAME_EVENT_COUNT = "/javelin/event/occured/count";

	/** ���ږ�(CallNodeTree��) */
	// String ITEMNAME_NODECOUNT = "/callTreeNodeCount";
	String ITEMNAME_NODECOUNT = "/javelin/calltreenode/generated/current";

	/** ���ږ�(�ő�CallTreeNode��) */
	// String ITEMNAME_MAX_NODECOUNT = "/maxCallTreeNodeCount";
	String ITEMNAME_MAX_NODECOUNT = "/javelin/calltreenode/generated/max";

	/** ���ږ�(���vCallTreeNode��) */
	// String ITEMNAME_ALL_NODECOUNT = "/allCallTreeNodeCount";
	String ITEMNAME_ALL_NODECOUNT = "/javelin/calltreenode/generated/all";

	/** ���ږ�(CallTree��) */
	// String ITEMNAME_CALLTREECOUNT = "/callTreeCount";
	String ITEMNAME_CALLTREECOUNT = "/javelin/calltree/generated/current";

	/** ���ږ�(JavelinConverter�ŕϊ��������\�b�h��) */
	// String ITEMNAME_CONVERTEDMETHOD = "/convertedMethodCount";
	String ITEMNAME_CONVERTEDMETHOD = "/javelin/converter/methods/converted";

	/** ���ږ�(JavelinConverter�ŕϊ��Ώۂ��珜�O�������\�b�h��) */
	// String ITEMNAME_EXCLUDEDMETHOD = "/excludedMethodCount";
	String ITEMNAME_EXCLUDEDMETHOD = "/javelin/converter/methods/excluded";

	/** ���ږ�(JavelinConverter�ŕϊ����s�������\�b�h�̂����A�Ăяo���ꂽ���\�b�h��) */
	// String ITEMNAME_CALLEDMETHODCOUNT = "/calledMethodCount";
	String ITEMNAME_CALLEDMETHODCOUNT = "/javelin/converter/methods/executed";

	/** ���ږ��iJVN�t�@�C�� JVN�t�@�C�����j */
	// String ITEMNAME_JVN_FILE_NAME = "/jvnFileName";
	String ITEMNAME_JVN_FILE_NAME = "/javelin/jvnfile/name";

	/** ���ږ��iJVN�t�@�C�� JVN�t�@�C�����e�j */
	// String ITEMNAME_JVN_FILE_CONTENT = "/jvnFileContent";
	String ITEMNAME_JVN_FILE_CONTENT = "/javelin/jvnfile/contents";

	/** ���ږ��i�A�C�e�����j */
	String ITEMNAME_JVN_ITEM_NAME = "/javelin/jvnfile/itemname";

	// -----------------------------------------------------
	// ���̑�

	/** ���ږ��iJMX�v���l�j */
	// String ITEMNAME_JMX = "jmx";
	String ITEMNAME_JMX = "/jmx";

	// -----------------------------------------------------
	// �Ď����

	/** ���ږ��i�ڑ��ʒm�F�ڑ�����ʁj */
	String ITEMNAME_CONNECTNOTIFY_KIND = "kind";

	/** ���ږ��i�ڑ��ʒm�FDB���j */
	String ITEMNAME_CONNECTNOTIFY_DBNAME = "dbName";

	/** ���ږ�(�ڑ��ʒm�F�ڑ��ړI) */
	String ITEMNAME_CONNECTNOTIFY_PURPOSE = "purpose";

	/** ���ږ�(�S�Ă̌Ăяo�����̖��O) */
	String ITEMNAME_ALL_CALLER_NAMES = "/allCallerNames";

	/** ���ږ�(���[�g) */
	String ITEMNAME_ROOT_NODE = "/rootNode";

	/** ���ږ�(�v���Ώ�) */
	String ITEMNAME_TARGET = "/target";

	/** ���ږ�(�g�����U�N�V�����O���t�o�͑Ώ�) */
	String ITEMNAME_TRANSACTION_GRAPH = "/transactionGraph";

	/** ���ږ�(TAT�A���[��臒l) */
	String ITEMNAME_ALARM_THRESHOLD = "/alarmThreshold";

	/** ���ږ�(CPU�A���[��臒l) */
	String ITEMNAME_ALARM_CPU_THRESHOLD = "/alarmCpuThreshold";

	/** ���ږ�(�N���X��) */
	String ITEMNAME_CLASSTOREMOVE = "/classToRemove";

	/** ���ږ�(HTTP�G���[������) */
	String ITEMNAME_HTTP_ERRCOUNT = "httpErrorCount";

	/** ���ږ�(Hadoop:NameNode) */
	String ITEMNAME_HADOOP_NAMENODE = "/hadoop/NameNode";

	/** ���ږ�(Hadoop:JobTracker) */
	String ITEMNAME_HADOOP_JOBTRACKER = "/hadoop/JobTracker";

	/** ���ږ�(Infinispan:mapreduce) */
	String ITEMNAME_INFINISPAN_MAPREDUCE = "/infinispan/MapReduce";

	/** ���ږ�(Hadoop:DataNode) */
	String ITEMNAME_HADOOP_DATANODE = "/hadoop/DataNode";

	/** ���ږ�(Hadoop:TaskTracker) */
	String ITEMNAME_HADOOP_TASKTRACKER = "/hadoop/TaskTracker";

	/** ���ږ�(HBase:HMaster) */
	String ITEMNAME_HBASE_HMASTER = "/hbase/HMaster";

	/** ���ږ�(HBase:HRegionServer) */
	String ITEMNAME_HBASE_HREGIONSERVER = "/hbase/HRegionServer";

	/** ���ږ��i�v��ID�j */
	String ITEMNAME_MEASUREMENT_TYPE = "measurementType";

    /** ���ږ��i�A���[���̎�ށj */
    String ITEMNAME_ALARM_ID = "alarmId";

    /** ���ږ��i臒l�����`����ID�j */
    String ITEMNAME_SIGNAL_ID = "signalId";

	/** ���ږ��i�A���[���̎�ށj */
	String ITEMNAME_ALARM_TYPE = "alarmType";

	/** ���ږ��i�A���[���������̏�Q��ԁj */
	String ITEMNAME_ALARM_STATE = "alarmState";

    /** ���ږ��i臒l�����`���̃��x���j */
    String ITEMNAME_SIGNAL_LEVEL = "signalLevel";
    
    /** ���ږ�(�V�O�i����`�ǉ�) */
    String ITEMNAME_SIGNAL_ADD = "signalAdd";

    /** ���ږ�(�V�O�i����`�X�V) */
    String ITEMNAME_SIGNAL_UPDATE= "signalUpdate";

    /** ���ږ�(�V�O�i����`�폜) */
    String ITEMNAME_SIGNAL_DELETE = "signalDelete";
    
    /** ���ږ�(�c���[��`�ǉ�) */
    String ITEMNAME_TREE_ADD = "treeAdd";
    
    /** ���ږ��i�c���[�\�����j */
    String ITEMNAME_TREE_DATA = "data";
    
    /** ���ږ��i�c���[ID�j */
    String ITEMNAME_TREE_TREEID = "treeId";
    
    /** ���ږ��i�e�c���[ID�j */
    String ITEMNAME_TREE_PARENTTREEID = "parentTreeId";
    
    /** ���ږ��iID�j */
    String ITEMNAME_TREE_ID = "id";
    
    /** ���ږ��i�c���[�̃^�C�v�j */
    String ITEMNAME_TREE_TYPE = "type";
  
    /** ���ږ��i�A�C�R���j */
    String ITEMNAME_TREE_ICON = "icon";
    
    /** ���ږ��i�v���P�ʁj */
    String ITEMNAME_TREE_MEASUREMENTUNIT = "measurementUnit";

	/** int �� byte[] �ϊ����ɑΉ�����o�C�g�� */
	int INT_BYTE_SWITCH_LENGTH = 4;

	/** long �� byte[] �ϊ����ɑΉ�����o�C�g�� */
	int LONG_BYTE_SWITCH_LENGTH = 8;

	/** ���ڌ^�i�P�o�C�g�����t�����j */
	byte BYTE_ITEMMODE_KIND_BYTE = 0;

	/** ���ڌ^�i�S�o�C�g�����t�����j */
	byte BYTE_ITEMMODE_KIND_4BYTE_INT = 2;

	/** ���ڌ^�i�W�o�C�g�����t�����j */
	byte BYTE_ITEMMODE_KIND_8BYTE_INT = 3;

	/** ���ڌ^�i������j */
	byte BYTE_ITEMMODE_KIND_STRING = 6;

	/** ���[�v�񐔁i1��j */
	int INT_LOOP_COUNT_SINGLE = 1;

	/** �N���X���A���\�b�h���̃Z�p���[�^ */
	String CLASSMETHOD_SEPARATOR = "###CLASSMETHOD_SEPARATOR###";
}
