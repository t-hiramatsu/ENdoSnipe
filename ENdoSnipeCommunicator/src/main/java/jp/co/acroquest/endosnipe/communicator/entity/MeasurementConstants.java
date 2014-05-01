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
 * �v���l��ʂ̒萔�C���^�[�t�F�[�X�ł��B<br />
 * �{�C���^�[�t�F�[�X�Œ�`�����v���l��ʁi<code>TYPE_</code>�Ŏn�܂�萔�j�́A
 * ��{�݌v�d�l���u�\5-1 �v���l���e�[�u���̏����l�v�ŋK�肳�ꂽ�l�ł��B<br />
 * �ǉ��E�ύX�̍ۂ́A��{�݌v�d�l���A measurementInfo.tsv ���C�����Ă��������B
 * 
 * @author fujii
 */
public interface MeasurementConstants
{
    /** �v���l���(���ݎ���) */
    int TYPE_TIME = 1;

    /** �v���l���(�擾����) */
    int TYPE_ACQUIREDTIME = 2;

    /** �v���l���(CPU����) */
    int TYPE_CPUTIME = 3;

    /** �v���l���(�q�[�v�������R�~�b�g��) */
    int TYPE_HEAPMEMORY_COMMITTED = 4;

    /** �v���l���(�q�[�v�������g�p��) */
    int TYPE_HEAPMEMORY_USED = 5;

    /** �v���l���(�q�[�v�������ő�) */
    int TYPE_HEAPMEMORY_MAX = 6;

    /** �v���l���(Java�ғ�����) */
    int TYPE_JAVAUPTIME = 7;

    /** �v���l���(�q�[�v�ȊO�̃������R�~�b�g��) */
    int TYPE_NONHEAPMEMORY_COMMITTED = 8;

    /** �v���l���(�q�[�v�ȊO�̃������g�p��) */
    int TYPE_NONHEAPMEMORY_USED = 9;

    /** �v���l���(�q�[�v�ȊO�̃������ő�) */
    int TYPE_NONHEAPMEMORY_MAX = 10;

    /** �v���l���(�����������e��) */
    int TYPE_PHYSICALMEMORY_CAPACITY = 11;

    /** �v���l���(�����������󂫗e��) */
    int TYPE_PHYSICALMEMORY_FREE = 12;

    /** �v���l���(�v���Z�b�T��) */
    int TYPE_PROCESSORCOUNT = 13;

    /** �v���l���(�X���b�v�̈�e��) */
    int TYPE_SWAPSPACE_CAPACITY = 14;

    /** �v���l���(�X���b�v�̈�󂫗e��) */
    int TYPE_SWAPSPACE_FREE = 15;

    /** �v���l���(���z�}�V���������e��) */
    int TYPE_VIRTUALMACHINEMEMORY_CAPACITY = 16;

    /** �v���l���(���z�}�V���������󂫗e��) */
    int TYPE_VIRTUALMACHINEMEMORY_FREE = 17;

    /** �v���l���(���z�������e��) */
    int TYPE_VIRTUALMEMORY_SIZE = 18;

    /** �v���l���(�l�b�g���[�N�f�[�^��M��) */
    int TYPE_NETWORK_INPUTSIZEOFPROCESS = 19;

    /** �v���l���(�l�b�g���[�N�f�[�^���M��) */
    int TYPE_NETWORK_OUTPUTSIZEOFPROCESS = 20;

    /** �v���l���(�t�@�C�����͗�) */
    int TYPE_FILE_INPUTSIZEOFPROCESS = 21;

    /** �v���l���(�t�@�C���o�͗�) */
    int TYPE_FILE_OUTPUTSIZEOFPROCESS = 22;

    /** �v���l���(�X���b�h��) */
    int TYPE_THREADCOUNT = 23;

    /** �v���l���(GC�g�[�^������) */
    int TYPE_GARBAGETOTALTIME = 24;

    /** �v���l���(���X�g�R���N�V�����̐�) */
    int TYPE_LISTCOUNT = 25;

    /** �v���l���(Queue�R���N�V�����̐�) */
    int TYPE_QUEUECOUNT = 26;

    /** �v���l���(Set�R���N�V�����̐�) */
    int TYPE_SETCOUNT = 27;

    /** �v���l���(Map�R���N�V�����̐�) */
    int TYPE_MAPCOUNT = 28;

    /** �v���l���(�N���X�q�X�g�O��������擾�����I�u�W�F�N�g�̃T�C�Y) */
    int TYPE_CLASSHISTOGRAM_SIZE = 29;

    /** �v���l���(�N���X�q�X�g�O��������擾�����I�u�W�F�N�g�̐�) */
    int TYPE_CLASSHISTOGRAM_COUNT = 30;

    /** �v���l���(Turn Around Time:���ϒl) */
    int TYPE_TURNAROUNDTIME = 31;

    /** �v���l���(Turn Around Time�Ăяo����) */
    int TYPE_TURNAROUNDTIMECOUNT = 32;

    /** �v���l���(�v�[���̍ő吔�A�ғ���) */
    int TYPE_POOLSIZE = 33;

    /** �v���l���(Finalize�ɓo�^����Ă���I�u�W�F�N�g��) */
    int TYPE_FINALIZATIONCOUNT = 34;

    /** �v���l���(HTTP�Z�b�V�����̃C���X�^���X��) */
    int TYPE_HTTPSESSION_NUMBER = 35;

    /** �v���l���(HTTP�Z�b�V�����̃I�u�W�F�N�g�o�^��) */
    int TYPE_HTTPSESSION_TOTALSIZE = 36;

    /** �v���l���(���[�J�X���b�h�̍ő吔�A�ғ���) */
    int TYPE_SERVERPOOL = 37;

    /** �v���l���(Turn Around Time:�ő�l) */
    int TYPE_TURNAROUNDTIMEMAX = 38;

    /** �v���l���(Turn Around Time:�ŏ��l) */
    int TYPE_TURNAROUNDTIMEMIN = 39;

    /** �v���l���(CallTreeNode��) */
    int TYPE_CALLTREENODE_COUNT = 40;

    /** �v���l���(JavelinConverter�ŕϊ��������\�b�h��) */
    int TYPE_CONVERTEDMOTHOD_COUNT = 41;

    /** �v���l���(JavelinConverter�ŕϊ��������\�b�h��) */
    int TYPE_EXCLUDEDMOTHOD_COUNT = 42;

    /** �v���l���(Java ���z�}�V�������s���J�n���Ă��烍�[�h���ꂽ�N���X�̍��v��) */
    int TYPE_TOTAL_LOADEDCLASSCOUNT = 43;

    /** �v���l���(Java ���z�}�V���Ɍ��݃��[�h����Ă���N���X�̐�) */
    int TYPE_LOADEDCLASSCOUNT = 44;

    /** �v���l���(JavelinConverter�ŕϊ����s�������\�b�h�̂����A�Ăяo���ꂽ���\�b�h��) */
    int TYPE_CALLEDMETHODCOUNT = 45;

    /** �v���l���(��O������) */
    int TYPE_THROWABLECOUNT = 46;

    /** �v���l���(HTTP�G���[������) */
    int TYPE_HTTP_ERRCOUNT = 47;

    /** �v���l���(�C�x���g��ʖ��̃C�x���g������) */
    int TYPE_EVENT_COUNT = 48;

    /** �v���l���(�V�X�e���S�̂̃������ő�l) */
    int TYPE_SYS_PHYSICALMEM_MAX = 49;

    /** �v���l���(�V�X�e���S�̂̋󂫃�����) */
    int TYPE_SYS_PHYSICALMEM_FREE = 50;

    /** �v���l���(�V�X�e���̃��[�U���[�h��CPU�g�p��) */
    int TYPE_SYS_CPUTIME_TOTAL = 51;

    /** �v���l���(�V�X�e���̃V�X�e�����[�h�ł�CPU�g�p��) */
    int TYPE_SYS_CPUTIME_SYS = 52;

    /** �v���l���(CPU���Ƃ̕���) */
    int TYPE_SYSTEM_CPUARRAY = 53;

    /** �v���l���(�V�X�e���S�̂̃y�[�W�C��) */
    int TYPE_SYS_PAGE_IN = 54;

    /** �v���l���(�V�X�e���S�̂̃y�[�W�A�E�g) */
    int TYPE_SYS_PAGE_OUT = 55;

    /** �v���l���(�v���Z�X����CpuTime) */
    int TYPE_PROC_CPUTIME_SYS = 56;

    /** �v���l���(�v���Z�X���̉��z�������g�p��) */
    int TYPE_PROC_VIRTUALMEM_USE = 57;

    /** �v���l���(�v���Z�X���̕����������g�p��) */
    int TYPE_PROC_PHYSICALMEM_USE = 58;

    /** �v���l���(�v���Z�X���̃X���b�h��) */
    int TYPE_PROC_THREAD_OS = 59;

    /** �v���l���(�v���Z�X���̃��W���[�t�H�[���g��) */
    int TYPE_PROC_MAJFLT = 60;

    /** �v���l���(�v���Z�X����fd/�n���h����) */
    int TYPE_SYS_FD_COUNT = 61;
    
    /** �v���l���(�v���Z�X����fd/�n���h����) */
    int TYPE_PROC_FD_COUNT = 62;
    
    /** CPU�g�p���i�V�X�e���j�̍��v */
    int TYPE_SYS_CPU_TOTAL_USAGE = 63;
    
    /** CPU�g�p���i�V�X�e���j�̒��̃V�X�e���̎g�p�� */
    int TYPE_SYS_CPU_SYS_USAGE = 64;
    
    /** CPU�g�p���i�v���Z�X�j�̍��v */
    int TYPE_PROC_CPU_TOTAL_USAGE = 65;
    
    /** �J�o���b�W */
    int TYPE_PROC_CPU_SYS_USAGE = 66;
    
    /** CPU�g�p���i�v���Z�X�j�̍��v */
    int TYPE_COVERAGE = 67;

    /** �v���l���(�V�X�e���S�̂̋󂫃�����) */
    int TYPE_SYS_PHYSICALMEM_USED = 68;
    
    /** �v���l���(���X�|���X��(sql������)) */
    int TYPE_PROC_RES_TOTAL_COUNT_EXCLUSION_SQL = 69;
    
    /** �v���l���(���X�|���X��(sql�̂�)) */
    int TYPE_PROC_RES_TOTAL_COUNT_ONLY_SQL = 70;
    
    /** �v���l���(�V�X�e���S�̂̋󂫃�����) */
    int TYPE_PROC_RES_TIME_AVERAGE_EXCLUSION_SQL = 71;
    
    /** �v���l���(�V�X�e���S�̂̋󂫃�����) */
    int TYPE_PROC_RES_TIME_AVERAGE_ONLY_SQL = 72;
    
    /** �v���l���(JMX�̌v���l) */
    int TYPE_JMX = 73;
    
    /** CPU�g�p���i�V�X�e���j�̒���IOWAIT�̎g�p�� */
    int TYPE_SYS_CPU_IOWAIT_TIME = 75;
    
    /** CPU�g�p���i�v���Z�X�j�̒���IOWAIT�̎g�p�� */
    int TYPE_PROC_CPU_IOWAIT_TIME = 76;
    
    /** CPU�g�p���i�V�X�e���j�̒���IOWAIT�̎g�p�� */
    int TYPE_SYS_CPU_IOWAIT_USAGE = 77;
    
    /** CPU�g�p���i�v���Z�X�j�̒���IOWAIT�̎g�p�� */
    int TYPE_PROC_CPU_IOWAIT_USAGE = 78;

    /** �v���l���(�t�@�C�����͗�) */
    int TYPE_FILE_INPUTSIZEOFSYSTEM = 79;

    /** �v���l���(�t�@�C���o�͗�) */
    int TYPE_FILE_OUTPUTSIZEOFSYSTEM = 80;
    
    /** �v���l���(HTTP�G���[������) */
    int TYPE_HTTP_ERRPR_RESPONSE = 81;
    
    /** �v���l���(�X�g�[�����o��) */
    int TYPE_METHODSTALLCOUNT = 82;

    /** �v���l���(NameNode) */
    int TYPE_HADOOP_NAMENODE = 83;
    
    /** �v���l���(DataNode) */
    int TYPE_HADOOP_DATANODE = 84;
    
    /** �v���l���(JobTracker) */
    int TYPE_HAOOP_JOBTRACKER = 85;
    
    /** �v���l���(TaskTracker) */
    int TYPE_HADOOP_TASKTRACKER = 86;
    
    /** �v���l���(HMaster) */
    int TYPE_HBASE_HMASTER = 87;

    /** �v���l���(HRegionServer) */
    int TYPE_HBASE_HREGIONSERVER = 88;
    
    /** �v���l���(MapReduce) */
    int TYPE_INFINISPAN_MAPREDUCE = 89;
}
