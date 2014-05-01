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

import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.bean.proc.DiskStats;
import jp.co.acroquest.endosnipe.javelin.bean.proc.MemInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.ProcInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.SelfStatInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.StatInfo;
import jp.co.acroquest.endosnipe.javelin.resource.ProcessorCountGetter;

/**
 * Windows�̃��\�[�X����ǂݍ���ProcParser�B
 * 
 * @author ochiai
 */
public class WindowsProcParser implements ProcParser
{

    /** �b���i�m�b�ɒ������߂̒萔�F1000 * 1000 * 1000 */
    private static final int SECONDS_TO_NANO_SECONDS = 1000 * 1000 * 1000;
    
    /** �p�[�Z���g�l�������ɒ������߂̒萔�F100 */
    private static final double PERCENT_TO_DECIMAL = 100;
    
    /** CPU�V�X�e�����ԁ@*/
    private static long cpuTimeSystem__ = 0;

    /** CPU���[�U���� */
    private static long cpuTimeUser__ = 0;
    
    /** CPU�������� */
    private static long processUserTime__ = 0;
    
    /** CPU�V�X�e������ */
    private static long processSTime__ = 0;

    /** �擾�������\�[�X�l */
    private ProcInfo procInfo_;
    
    /** ���\�[�X�l�̎擾 */
    private PerfCounter perfCounter_ = null;

    /**
     * ���������s���B���������ꍇ�ɂ̂�true
     * 
     * @return ���������ꍇ�ɂ̂�true
     */
    public boolean init()
    {
        // ����
        this.perfCounter_ = new PerfCounter();
        return this.perfCounter_.init();        
    }
    
/**
     *      /proc/meminfo�A/proc/stat�A/proc/self/stat����ǂݍ��݁A
     *    ProcInfo�Ɋi�[����B
     *    
     *    @return PocInfo
     */
    public ProcInfo load()
    {
        ProcInfo procInfo = parseStatInfo();
        this.procInfo_ = procInfo;
        return procInfo;
    }

    /**
     * /proc/stat�̈ȉ��̏���StatInfo�ɃZ�b�g���A�Ԃ��B<br>
     * <ul>
     *   <li>cpu(nano�b)</li>
     *   <li>cpu0,cpu1,cpu2,�E�E�E(nano�b)</li>
     *   <li>pgpgin(byte)</li>
     *   <li>pgpgout(byte)</li>
     * </ul>
     * @return SelfStatInfo /proc/stat,/proc/vmstat�̏��
     */
    private ProcInfo parseStatInfo()
    {
        ProcInfo procInfo = new ProcInfo();
        
        MemInfo memInfo = new MemInfo();
        StatInfo statInfo = new StatInfo();
        SelfStatInfo selfStatInfo = new SelfStatInfo();
        DiskStats diskStats = new DiskStats();

        if (this.perfCounter_ == null)
        {
            // ����
            this.perfCounter_ = new PerfCounter();
            this.perfCounter_.init();
        }
        
        Map<String, Double> perfData = this.perfCounter_.getPerfData();
        Double userobj = perfData.get(PerfCounter.PROCESSOR_TOTAL_USER_TIME);
        Double sysobj = perfData.get(PerfCounter.PROCESSOR_TOTAL_PRIVILEGED_TIME);
        Double memTotal = perfData.get(PerfCounter.MEMORY_TOTAL);
        Double memAvailable = perfData.get(PerfCounter.MEMORY_AVAILABLE_BYTES);
        Double pageUsage = perfData.get(PerfCounter.PAGING_FILE_USAGE);
        Double pageBytes = perfData.get(PerfCounter.PROCESS_TOTAL_PAGE_FILE_BYTES);
        Double pageIn = perfData.get(PerfCounter.MEMORY_PAGES_INPUT_SEC);
        Double pageOut = perfData.get(PerfCounter.MEMORY_PAGES_OUTPUT_SEC);
        Double userTime = perfData.get(PerfCounter.PROCESS_USER_TIME);
        Double privilegedTime = perfData.get(PerfCounter.PROCESS_PRIVILEGED_TIME);
        Double majFlt = perfData.get(PerfCounter.PROCESS_PAGE_FAULTS_SEC);
        Double vsize = perfData.get(PerfCounter.PROCESS_VIRTUAL_BYTES);
        Double rss = perfData.get(PerfCounter.PROCESS_WORKING_SET);
        Double numThreads = perfData.get(PerfCounter.PROCESS_THREAD_COUNT);
        Double procFDCount = perfData.get(PerfCounter.PROCESS_NUMBER_FDS);
        Double systemFDCount = perfData.get(PerfCounter.PROCESS_TOTAL_NUMBER_FDS);
        
        // �ώZ�l��n�����߂ɕϊ�����
        ProcessorCountGetter procCountGetter = new ProcessorCountGetter();
        int procCount = procCountGetter.getValue().intValue();
        double interval = perfData.get(PerfCounter.INTERVAL);
        cpuTimeUser__ +=
            procCount * userobj
            / PERCENT_TO_DECIMAL * interval * SECONDS_TO_NANO_SECONDS;
        cpuTimeSystem__ +=
            procCount * sysobj
            / PERCENT_TO_DECIMAL * interval * SECONDS_TO_NANO_SECONDS;
        processUserTime__ += userTime / PERCENT_TO_DECIMAL * interval * SECONDS_TO_NANO_SECONDS;
        processSTime__ += privilegedTime / PERCENT_TO_DECIMAL * interval * SECONDS_TO_NANO_SECONDS;
        
        Double pageInTotal = pageIn * interval;
        Double pageOutTotal = pageOut * interval;
        Double majFltTotal = majFlt * interval;
        
        Double swapTotal = pageBytes / pageUsage * PERCENT_TO_DECIMAL;
        Double swapFree = swapTotal - pageBytes;
        
        statInfo.setCpuSystem(cpuTimeSystem__);
        statInfo.setCpuUser(cpuTimeUser__);
        //statInfo.setCpuTask(cpuTask);
        statInfo.setPageIn(pageInTotal.longValue());
        statInfo.setPageOut(pageOutTotal.longValue());
        statInfo.setFdCount(systemFDCount.intValue());
        
        selfStatInfo.setMajflt(majFltTotal.longValue());
        selfStatInfo.setVsize(vsize.longValue());
        selfStatInfo.setRss(rss.longValue());
        selfStatInfo.setNumThreads(numThreads.longValue());
        selfStatInfo.setUtime(processUserTime__);
        selfStatInfo.setStime(processSTime__);
        selfStatInfo.setFdCount(procFDCount.intValue());

        memInfo.setMemTotal(memTotal.longValue());
        memInfo.setMemFree(memAvailable.longValue());
        memInfo.setBufferes(0);
        memInfo.setCached(0);
        memInfo.setSwapTotal(swapTotal.longValue());
        memInfo.setSwapFree(swapFree.longValue());
        //memInfo.setVmallocTotal(0);
        
        
        procInfo.setMemInfo(memInfo);
        procInfo.setStatInfo(statInfo);
        procInfo.setSelfStatInfo(selfStatInfo);
        procInfo.setDiskStats(diskStats);

        return procInfo;
    }
    
    /**
     * ���\�[�X�g�p�󋵂̃f�[�^ procInfo ��Ԃ�
     * @return ProcInfo
     */
    public ProcInfo getProcInfo()
    {
        return this.procInfo_;
    }
}
