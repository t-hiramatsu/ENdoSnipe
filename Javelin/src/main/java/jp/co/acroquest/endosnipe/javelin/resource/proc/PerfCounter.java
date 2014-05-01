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
package jp.co.acroquest.endosnipe.javelin.resource.proc;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * @author ochiai
 *
 */
public class PerfCounter
{
    /** CPU�g�p���擾�̂��߂̕����� */
    public static final String PROCESSOR_TOTAL_PROCESSOR_TIME =
        "\\\\Processor(_Total)\\\\% Processor Time";
    
    /** CPU�g�p���i�V�X�e���j�擾�̂��߂̕����� */
    public static final String PROCESSOR_TOTAL_PRIVILEGED_TIME =
        "\\\\Processor(_Total)\\\\% Privileged Time";
    
    /** CPU�g�p���i���[�U�j�擾�̂��߂̕����� */
    public static final String PROCESSOR_TOTAL_USER_TIME =
        "\\\\Processor(_Total)\\\\% User Time";
    
    /** �����������i�ő�j�擾�̂��߂̕����� */
    public static final String MEMORY_TOTAL =
        "Memory Total";
    
    /** �����������i�󂫁j�擾�̂��߂̕����� */
    public static final String MEMORY_AVAILABLE_BYTES =
        "\\\\Memory\\\\Available Bytes";
    
    /** �y�[�W�t�@�C���g�p���擾�̂��߂̕�����i�j */
    public static final String PAGING_FILE_USAGE =
        "\\\\PAGING FILE\\\\% USAGE";
    
    /** �y�[�W�t�@�C���g�p�ʎ擾�̂��߂̕����� */
    public static final String PROCESS_TOTAL_PAGE_FILE_BYTES =
        "\\\\Processor(_Total)\\\\Page File Bytes";
    
    /** ���z�������g�p�ʎ擾�̂��߂̕����� */
    public static final String PROCESS_TOTAL_VIRTUAL_BYTES =
        "\\\\Process(_Total)\\\\Virtual Bytes";
    
    /** �V�X�e���S�̂̃n���h���g�p���擾�̂��߂̕����� */
    public static final String PROCESS_TOTAL_NUMBER_FDS =
        "\\\\Process(_Total)\\\\Handle Count";
    
    /** �y�[�W�C���擾�̂��߂̕����� */
    public static final String MEMORY_PAGES_INPUT_SEC =
        "\\\\Memory\\\\Pages Input/sec";
    
    /** �y�[�W�A�E�g�擾�̂��߂̕����� */
    public static final String MEMORY_PAGES_OUTPUT_SEC =
        "\\\\Memory\\\\Pages Output/sec";
    
    /** % User Time�擾�̂��߂̕����� */
    public static final String PROCESS_USER_TIME =
        "\\\\Process(xxx)\\\\% User Time";
    
    /** % Privileged Time�擾�̂��߂̕����� */
    public static final String PROCESS_PRIVILEGED_TIME =
        "\\\\Processor(xxx)\\\\% Privileged Time";
    
    /** ���W���[�t�H�[���g�擾�̂��߂̕����� */
    public static final String PROCESS_PAGE_FAULTS_SEC =
        "\\\\Process(xxx)\\\\Page Faults/sec";
    
    /** vsize�擾�̂��߂̕����� */
    public static final String PROCESS_VIRTUAL_BYTES =
        "\\\\Process(xxx)\\\\Virtual Bytes";
    
    /** rss�擾�̂��߂̕����� */
    public static final String PROCESS_WORKING_SET =
        "\\\\Process(xxx)\\\\Working Set";
    
    /** �X���b�h���擾�̂��߂̕����� */
    public static final String PROCESS_THREAD_COUNT =
        "\\\\Process(xxx)\\\\Thread Count";
    
    /** �v���Z�X�̃n���h���g�p���擾�̂��߂̕����� */
    public static final String PROCESS_NUMBER_FDS =
        "\\\\Process(xxx)\\\\Handle Count";

    /** �v���Ԋu�i���ۂ̒l�j */
    public static final String INTERVAL = "Interval";

    /** �~���b����b�ւ̕ϊ� */
    private static final double MILLI_SECONDS_TO_SECONDS = 1000.0;
    
    /** �O��̌v�������ilong�l �~���b�j */
    private long lastMeasuredTime_;

    private Map<String, Double> prevResourceMap_;

    // dll �t�@�C�������[�h����
    static {
        SystemLogger logger = SystemLogger.getInstance();

        // ���C�u���������[�h���܂�
        JavelinConfigUtil javelinConfigUtil = JavelinConfigUtil.getInstance();
        
        // CPU bit��
        String bit = System.getProperty("sun.arch.data.model");
        if (bit == null || bit.length() == 0)
        {
            logger.warn("you have to set \"sun.arch.data.model\" system properties.");
            bit = "";
        }
        
        String libPath = "./PerfCounter_" +  bit + ".dll";
        
        libPath = javelinConfigUtil.convertRelPathFromJartoAbsPath(libPath);

        if (logger.isDebugEnabled())
        {
            logger.debug("loading dll for read performance counter : " + libPath);
        }
        
        try 
        {
            System.load(libPath);
        }
        catch (SecurityException se)
        {
            logger.error("Can't load dll library : " + libPath, se);
        }
        catch (UnsatisfiedLinkError ule)
        {
            logger.error("Can't load dll library : " + libPath, ule);
        }
    }
    
    /**
     * �V�K�N�G���[���쐬
     * @return �쐬�ɐ��������� true
     */
    private native boolean openQuery();

    /**
     * �N�G���[��ǉ�
     * @return �ǉ��ɐ��������� true
     */
    private native boolean addCounter(String counterPath);

    /**
     * �n���h�����X�V���܂��B
     *
     * @return �n���h�����X�V�����ꍇ�� <code>true</code> �A�X�V���Ȃ������ꍇ�� <code>false</code>
     */
    private native boolean updateHandles();

    /**
     * �v��
     * @return �v���ɐ���������true
     */
    private native boolean collectQueryData();

    /**
     * �V�X�e����CPU�g�p���iSystem�j���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueSysCPUSys();

    /**
     * �V�X�e����CPU�g�p���iUser�j���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueSysCPUUser();

    /**
     * �����������i�ő�j���擾
     * @return �擾�����l
     */
    private native double getMemoryTotal();

    /**
     * �����������i�󂫁j���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueMemAvailable();

    /**
     * �y�[�W�t�@�C���g�p�����擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValuePageFileUsage();

    /**
     * �y�[�W�t�@�C���g�p�ʂ��擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValuePageFileBytes();

    /**
     * �y�[�W�t�@�C���g�p�ʂ��擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueVirtualBytes();

    /**
     * �V�X�e���S�̂�FD�����擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueSystemFDs();

    /**
     * �y�[�W�C�����擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValuePageIn();

    /**
     * �y�[�W�A�E�g���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValuePageOut();

    /**
     * % User Time���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueProcessUserTime();

    /**
     * % Privileged Time���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueProcessPrivilegedTime();

    /**
     * ���W���[�t�H�[���g���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueMajFlt();

    /**
     * vsize���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueVSize();

    /**
     * rss���擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueRSS();

    /**
     * �X���b�h�����擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueNumThreads();

    /**
     * �v���Z�X��FD�����擾
     * @return �擾�����l
     */
    private native double getFormattedCounterValueProcFDs();
    
    /**
     * �N�G���[�̎g�p���I��
     * @return �I���ɐ��������� true
     */
    private native boolean closeQuery();

    /**
     * �R���X�g���N�^
     */
    public PerfCounter()
    {
        // do nothing
    }
    
    /**
     * �V�X�e�����\�[�X�擾�����̏��������\�b�h
     * @return �������ɐ���������true
     */
    public boolean init()
    {
        this.lastMeasuredTime_ = Calendar.getInstance().getTimeInMillis();
        boolean result = openQuery();
        if (result)
        {
            result = addCounter(PROCESSOR_TOTAL_PRIVILEGED_TIME);
        }
        if (result)
        {
            result = collectQueryData();
        }
        return result;
    }
    
    /**
     * �V�X�e�����\�[�X�擾�����̏I�����\�b�h
     * @return �I���ɐ���������true
     */
    public boolean destroy()
    {
        closeQuery();
        return true;
    }
    
    /**
     * Windows �̃V�X�e�����\�[�X��Map��Ԃ�
     * 
     * @return �V�X�e�����\�[�X��Map
     */
    public Map<String, Double> getPerfData()
    {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        // �v���Ԋu��b�P�ʂɕϊ�����
        double measurementInterval =
            (currentTime - this.lastMeasuredTime_) / MILLI_SECONDS_TO_SECONDS;
        this.lastMeasuredTime_ = currentTime;

        boolean updated = updateHandles();

        // �o�^����Ă���J�E���^�l���v������
        collectQueryData();

        // ���ꂼ��̃J�E���^�l�̌v�����ʂ𓾂�
        double sysCPUSys = getFormattedCounterValueSysCPUSys();
        double sysCPUUser = getFormattedCounterValueSysCPUUser();
        double memTotal = getMemoryTotal();
        double memAvailable = getFormattedCounterValueMemAvailable();
        double pageFileUsage = getFormattedCounterValuePageFileUsage();
        double pageFileBytes = getFormattedCounterValuePageFileBytes();
        double virtualBytes = getFormattedCounterValueVirtualBytes();
        double systemFDs = getFormattedCounterValueSystemFDs();
        double pageIn = getFormattedCounterValuePageIn();
        double pageOut = getFormattedCounterValuePageOut();
        double procUserTime = getFormattedCounterValueProcessUserTime();
        double procSysTime = getFormattedCounterValueProcessPrivilegedTime();
        double majFlt = getFormattedCounterValueMajFlt();
        double vsize = getFormattedCounterValueVSize();
        double rss = getFormattedCounterValueRSS();
        double numThreads = getFormattedCounterValueNumThreads();
        double procFDs = getFormattedCounterValueProcFDs();

        if (updated && this.prevResourceMap_ != null)
        {
            procUserTime = this.prevResourceMap_.get(PROCESS_USER_TIME);
            procSysTime = this.prevResourceMap_.get(PROCESS_PRIVILEGED_TIME);
            majFlt = this.prevResourceMap_.get(PROCESS_PAGE_FAULTS_SEC);
            vsize = this.prevResourceMap_.get(PROCESS_VIRTUAL_BYTES);
            rss = this.prevResourceMap_.get(PROCESS_WORKING_SET);
            numThreads = this.prevResourceMap_.get(PROCESS_THREAD_COUNT);
            procFDs = this.prevResourceMap_.get(PROCESS_NUMBER_FDS);
        }

        Map<String, Double> systemResourceMap = new HashMap<String, Double>();
        systemResourceMap.put(PROCESSOR_TOTAL_PRIVILEGED_TIME, sysCPUSys);
        systemResourceMap.put(PROCESSOR_TOTAL_USER_TIME, sysCPUUser);
        systemResourceMap.put(MEMORY_TOTAL, memTotal);
        systemResourceMap.put(MEMORY_AVAILABLE_BYTES, memAvailable);
        systemResourceMap.put(PAGING_FILE_USAGE, pageFileUsage);
        systemResourceMap.put(MEMORY_PAGES_INPUT_SEC, pageIn);
        systemResourceMap.put(MEMORY_PAGES_OUTPUT_SEC, pageOut);
        systemResourceMap.put(PROCESS_TOTAL_NUMBER_FDS, systemFDs);
        systemResourceMap.put(PROCESS_TOTAL_PAGE_FILE_BYTES, pageFileBytes);
        systemResourceMap.put(PROCESS_TOTAL_VIRTUAL_BYTES, virtualBytes);
        systemResourceMap.put(PROCESS_USER_TIME, procUserTime);
        systemResourceMap.put(PROCESS_PRIVILEGED_TIME, procSysTime);
        systemResourceMap.put(PROCESS_PAGE_FAULTS_SEC, majFlt);
        systemResourceMap.put(PROCESS_VIRTUAL_BYTES, vsize);
        systemResourceMap.put(PROCESS_WORKING_SET, rss);
        systemResourceMap.put(PROCESS_THREAD_COUNT, numThreads);
        systemResourceMap.put(PROCESS_NUMBER_FDS, procFDs);

        systemResourceMap.put(INTERVAL, measurementInterval);

        this.prevResourceMap_ = systemResourceMap;
        return systemResourceMap;
    }
}
