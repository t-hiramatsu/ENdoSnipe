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

import java.io.File;

import jp.co.acroquest.endosnipe.javelin.bean.proc.DiskStats;
import jp.co.acroquest.endosnipe.javelin.bean.proc.MemInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.ProcInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.SelfStatInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.StatInfo;
import jp.co.acroquest.endosnipe.javelin.resource.ProcessorCountGetter;

/**
 * Solaris��/proc��ǂݍ���ProcParser�B
 * 
 * @author eriguchi
 * @author iida
 * @author akita
 */
public class SolarisProcParser implements ProcParser
{
    /** /proc/self/fd�̃p�X�B */
    private static final String PROC_SELF_FD_PATH = "/proc/self/fd";

	/** �擾�������\�[�X�l */
    private ProcInfo procInfo_;

    /** ���\�[�X�l�̎擾 */
    private SolarisResourceReader resourceReader_ = null;

    /** CPU�R�A�� */
    private int processors_ = new ProcessorCountGetter().getValue().intValue();

    /**
     * ���������s���B
     * 
     * @return ���������ꍇ�ɂ̂�true
     */
    public boolean init()
    {
        // ����
        this.resourceReader_ = new SolarisResourceReader();
        return this.resourceReader_.init();
    }
    
    
    /**
     * /proc/meminfo�A/proc/stat�A/proc/self/stat����ǂݍ��݁A
     * ProcInfo�Ɋi�[����B
     *
     * @return ProcInfo
     */
    public ProcInfo load()
    {
        if (this.resourceReader_ == null)
        {
            // ����
            this.resourceReader_ = new SolarisResourceReader();
            this.resourceReader_.init();
        }

        this.resourceReader_.refresh();
        
        MemInfo memInfo = parseMemInfo();
        StatInfo statInfo = parseStatInfo();
        SelfStatInfo selfStatInfo = parseSelfStatInfo();

        ProcInfo procInfo = new ProcInfo();
        procInfo.setMemInfo(memInfo);
        procInfo.setStatInfo(statInfo);
        procInfo.setSelfStatInfo(selfStatInfo);
        
        DiskStats diskStats = new DiskStats();
        procInfo.setDiskStats(diskStats);
        
        this.procInfo_ = procInfo;

        return procInfo;
    }

    /**
     * /proc/self/stat�̈ȉ��̏���SelfStatInfo�ɃZ�b�g���A�Ԃ��B<br>
     * <ul>
     *   <li>utime</li>
     *   <li>stime</li>
     *   <li>vsize</li>
     *   <li>rss</li>
     *   <li>numThreads</li>
     *   <li>majflt</li>
     * </ul>
     * @return SelfStatInfo /proc/self/stat�̏��
     */
    private SelfStatInfo parseSelfStatInfo()
    {
        SelfStatInfo selfStatInfo = new SelfStatInfo();

        selfStatInfo.setStime(this.resourceReader_.getProcessCPUSys() * this.processors_);
        selfStatInfo.setUtime(this.resourceReader_.getProcessCPUUser() * this.processors_);
        selfStatInfo.setMajflt(this.resourceReader_.getProcessMajFlt());
        selfStatInfo.setNumThreads(this.resourceReader_.getNumThreads());
        selfStatInfo.setRss(this.resourceReader_.getProcessMemoryPhysical());
        selfStatInfo.setVsize(this.resourceReader_.getProcessMemoryVirtual());

        File selfFdDir = new File(PROC_SELF_FD_PATH);
        String[] list = selfFdDir.list();
        if(list != null)
        {
            int length = list.length;
            selfStatInfo.setFdCount(length);
        }

    	return selfStatInfo;
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
    private StatInfo parseStatInfo()
    {
        StatInfo statInfo = new StatInfo();
        
        statInfo.setCpuSystem(this.resourceReader_.getSystemCPUSys() * this.processors_);
        statInfo.setCpuTask(0);
        statInfo.setCpuUser(this.resourceReader_.getSystemCPUUser() * this.processors_);
        
        statInfo.setPageIn(this.resourceReader_.getSystemPageIn());
        statInfo.setPageOut(this.resourceReader_.getSystemPageOut());
        
        return statInfo;
    }

    /**
     * /proc/meminfo�̈ȉ��̏���MemInfo�ɃZ�b�g���A�Ԃ��B<br>
     * <ul>
     *   <li>�V�X�e���S�̂̃������ő�l�F MemTotal�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̋󂫃������F MemFree�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̃o�b�t�@�F Buffers�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̃L���b�V���F Cached�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̃X���b�v�ő�ʁF SwapTotal�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̃X���b�v�󂫗e�ʁF SwapFree�̒l(byte)</li>
     *   <li>�V�X�e���S�̂̉��z�������g�p�ʁF VmallocTotal�̒l(byte)</li>
     * </ul>
     * @return MemInfo /proc/meminfo�̏��
     */
    private MemInfo parseMemInfo()
    {
        MemInfo memInfo = new MemInfo();
        
        memInfo.setBufferes(0);
        memInfo.setCached(0);
        memInfo.setMemTotal(this.resourceReader_.getSystemMemoryTotal());
        memInfo.setMemFree(this.resourceReader_.getSystemMemoryFree());
        
        memInfo.setSwapFree(this.resourceReader_.getSystemSwapFree());
        memInfo.setSwapTotal(this.resourceReader_.getSystemSwapTotal());
        
        return memInfo;
    }

    /**
     * ���\�[�X�g�p�󋵂̃f�[�^ procInfo ��Ԃ��B
     *
     * @return procInfo
     */
    public ProcInfo getProcInfo()
    {
        return this.procInfo_;
    }
}
