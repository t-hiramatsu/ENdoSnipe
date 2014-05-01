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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.bean.proc.DiskStats;
import jp.co.acroquest.endosnipe.javelin.bean.proc.MemInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.ProcInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.SelfStatInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.StatInfo;

/**
 * Linux��/proc��ǂݍ���ProcParser�B
 * 
 * @author eriguchi
 * @author iida
 * @author akita
 * 
 */
public class LinuxProcParser implements ProcParser
{
    /** /proc/self/stat�̃p�X�B */
    private static final String PROC_SELF_STAT_PATH = "/proc/self/stat";

    /** /proc/self/fd�̃p�X�B */
    private static final String PROC_SELF_FD_PATH = "/proc/self/fd";
    
    /** /proc/sys/fs/file-nr�̃p�X�B */
    private static final String PROC_SYS_FS_FILENR = "/proc/sys/fs/file-nr";

    /** /proc/meminfo�̃p�X�B */
    private static final String PROC_MEMINFO_PATH   = "/proc/meminfo";

    /** /proc/stat�t�@�C���̃p�X */
    private static final String PROC_STAT_PATH      = "/proc/stat";

    /** /proc/vmstat�t�@�C���̃p�X */
    private static final String PROC_VMSTAT_PATH    = "/proc/vmstat";

    /** /proc/diskstats�̃p�X�B */
    private static final String PROC_DISKSTATS_PATH   = "/proc/diskstats";

    // parseStatInfo�ŗp����萔�B
    // �e�p�����[�^���\�������s�̐擪�̕�����������B
    //CPU���Ƃ̕��ׂ̃L�[���[�h��"cpu"�𗘗p���A���̌�ɂO����̘A�Ԃ�����B(cpu0,cpu1,�E�E�E)

    /** stat�t�@�C������cpu�̃L�[���[�h */
    private static final String CPU_VALUE_KEY       = "cpu";

    /** vmstat�t�@�C�����̃y�[�W�C���̃L�[���[�h */
    private static final String PAGEIN_VALUE_KEY    = "pgpgin";

    /** vmstat�t�@�C�����̃y�[�W�A�E�g�̃L�[���[�h */
    private static final String PAGEOUT_VALUE_KEY   = "pgpgout";

    // parseSelfStatInfo�ŗp����萔�B
    // �e�p�����[�^�l�����Ԗڂɕ\������Ă���̂��������B�i�������A�ԍ���0����n�܂�B�j
    // �����̔ԍ��́A�ȉ��̃y�[�W�Ɍf�ڂ���Ă���\�[�X�R�[�h���璲�ׂ����̂ł���B
    // http://lxr.linux.no/linux+v2.6.18/fs/proc/array.c

    /** /proc/self/stat��utime���o�͂���鏇�ԁB */
    private static final int    UTIME_INDEX         = 13;

    /** /proc/self/stat��stime���o�͂���鏇�ԁB */
    private static final int    STIME_INDEX         = 14;

    /** /proc/self/stat��vsize���o�͂���鏇�ԁB�i"0"�����ɂ���B�j */
    private static final int    VSIZE_INDEX         = 22;

    /** /proc/self/stat��rss���o�͂���鏇�ԁB�i"0"�����ɂ���B�j */
    private static final int    RSS_INDEX           = 23;

    /** /proc/self/stat��numThread���o�͂���鏇�� */
    private static final int    NUM_THREADS_INDEX   = 19;

    /** /proc/self/stat��majflt���o�͂���鏇�� */
    private static final int    MAJFLT_INDEX        = 11;

    /** cpu�̒P�ʕϊ��i1/100sec��nsec�j�ɗp����B */
    private static final int    JIFFY_TO_NANO       = 10000000;

    /** rss�̒l�ϊ��ɗp����B */
    private static final int    CONVERT_RSS         = 4096;

    /** ������,�X���b�v��kilobyte��byte�ɒP�ʕϊ��ikilobyte��byte�j����B */
    private static final int    KILOBYTE_TO_BYTE    = 1024;

    // parseMemInfo�ŗp����萔�B
    // �e�p�����[�^���\�������s�̐擪�̕�����������B

    /** /proc/meminfo��MemTotal������������B */
    private static final String MEM_TOTAL           = "MemTotal:";

    /** /proc/meminfo��MemFree������������B */
    private static final String MEM_FREE            = "MemFree:";

    /** /proc/meminfo��Buffers������������B */
    private static final String BUFFERS             = "Buffers:";

    /** /proc/meminfo��Cached������������B */
    private static final String CACHED              = "Cached:";

    /** /proc/meminfo��SwapTotal������������B */
    private static final String SWAP_TOTAL          = "SwapTotal:";

    /** /proc/meminfo��SwapFree������������B */
    private static final String SWAP_FREE           = "SwapFree:";

    private static final int    BLOCK_TO_BYTE       = 512;

    /**proc/diskstats�œǂݏo���ׂ���̃g�[�N���� */
    private static final int DISKSTATS_TOKEN_MAX = 10;


    /** �擾�������\�[�X�l */
    private ProcInfo procInfo_;

    /**
     * ���������s���B
     * 
     * @return ���������ꍇ�ɂ̂�true
     */
    public boolean init()
    {
        return true;
    }

    /**
     * /proc/meminfo�A/proc/stat�A/proc/self/stat����ǂݍ��݁A
     * ProcInfo�Ɋi�[����B
     *
     * @return ProcInfo
     */
    public ProcInfo load()
    {
        MemInfo memInfo = parseMemInfo();
        DiskStats diskStats = parseDiskStats();
        StatInfo statInfo = parseStatInfo();
        SelfStatInfo selfStatInfo = parseSelfStatInfo();

        ProcInfo procInfo = new ProcInfo();
        procInfo.setMemInfo(memInfo);
        procInfo.setDiskStats(diskStats);
        procInfo.setStatInfo(statInfo);
        procInfo.setSelfStatInfo(selfStatInfo);
        
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
        List<String> paramValueStrings = new ArrayList<String>();
        File file = new File(PROC_SELF_STAT_PATH);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(line);
                while (tokenizer.hasMoreElements())
                {
                    paramValueStrings.add(tokenizer.nextToken());
                }
            }

            long paramValue;

            paramValue = getLongFromParamValueString(paramValueStrings, UTIME_INDEX, JIFFY_TO_NANO);
            selfStatInfo.setUtime(paramValue);

            paramValue = getLongFromParamValueString(paramValueStrings, STIME_INDEX, JIFFY_TO_NANO);
            selfStatInfo.setStime(paramValue);

            paramValue = getLongFromParamValueString(paramValueStrings, VSIZE_INDEX, 1);
            selfStatInfo.setVsize(paramValue);

            paramValue = getLongFromParamValueString(paramValueStrings, RSS_INDEX, 1);
            selfStatInfo.setRss(paramValue * CONVERT_RSS);

            paramValue = getLongFromParamValueString(paramValueStrings, NUM_THREADS_INDEX, 1);
            selfStatInfo.setNumThreads(paramValue);

            paramValue = getLongFromParamValueString(paramValueStrings, MAJFLT_INDEX, 1);
            selfStatInfo.setMajflt(paramValue);
        }
        catch (FileNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (NullPointerException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (NumberFormatException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        File selfFdDir = new File(PROC_SELF_FD_PATH);
        String[] list = selfFdDir.list();
        if (list != null)
        {
            int length = list.length;
            selfStatInfo.setFdCount(length);
        }
        
        return selfStatInfo;
    }

    private long getLongFromParamValueString(final List<String> paramValueStrings, final int index,
            final int unit)
    {
        String paramValueString = paramValueStrings.get(index);
        long paramValue = Long.parseLong(paramValueString) * unit;
        return paramValue;
    }

    /**
     * /proc/stat�̈ȉ��̏���StatInfo�ɃZ�b�g���A�Ԃ��B<br>
     * <ul>
     *   <li>cpu(nano�b)</li>
     *   <li>cpu0,cpu1,cpu2,�E�E�E(nano�b)</li>
     *   <li>pgpgin(byte)</li>
     *   <li>pgpgout(byte)</li>
     * </ul>
     * @return StatInfo /proc/stat,/proc/vmstat,/proc/sys/fs/file-nr�̏��
     */
    private StatInfo parseStatInfo()
    {
        StatInfo statInfo = new StatInfo();

        loadProcStat(statInfo, PROC_STAT_PATH);
        loadProcVmstat(statInfo, PROC_VMSTAT_PATH);
        loadProcSysFs(statInfo, PROC_SYS_FS_FILENR);
        
        return statInfo;
    }

    private void loadProcSysFs(StatInfo statInfo, String procSysFsFilenr)
    {
        File filenrFile = new File(procSysFsFilenr);
        if (filenrFile.exists() == false)
        {
            return;
        }

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(filenrFile));
            String line = reader.readLine();
            if(line == null)
            {
                return;
            }
                
            StringTokenizer tokenizer = new StringTokenizer(line);
            if (tokenizer.hasMoreTokens() == false)
            {
                return;
            }
            String totalFdCountStr = tokenizer.nextToken();

            if (tokenizer.hasMoreTokens() == false)
            {
                return;
            }
            String totalFreeFdCountStr = tokenizer.nextToken();
            
            long totalFdCount = Long.parseLong(totalFdCountStr);
            long totalFreeFdCount = Long.parseLong(totalFreeFdCountStr);
            
            statInfo.setFdCount(totalFdCount - totalFreeFdCount);
        }
        catch (NumberFormatException nfe)
        {
            SystemLogger.getInstance().warn(nfe);
            return;
        }
        catch (FileNotFoundException fnfe)
        {
            SystemLogger.getInstance().warn(fnfe);
            return;
        }
        catch (IOException ioe)
        {
            SystemLogger.getInstance().warn(ioe);
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioe)
                {
                    SystemLogger.getInstance().warn(ioe);
                }
            }
        }
    }

    private void loadProcStat(StatInfo sid, String statFilePath)
    {
        BufferedReader br = null;
        File statFile = new File(statFilePath);
        if (statFile.exists() == false)
        {
            return;
        }
        
        try
        {
            br = new BufferedReader(new FileReader(statFile));
        }
        catch (FileNotFoundException fnfex)
        {
            SystemLogger.getInstance().warn(fnfex);
            return;
        }
        catch (Exception ioex)
        {
            SystemLogger.getInstance().warn(ioex);
            return;
        }

        String str;
        int cpuNo = 0;
        String cpuLoadParamKey = CPU_VALUE_KEY + cpuNo;
        ArrayList<Long> cpuXXXList = new ArrayList<Long>();

        // �s��1��ڂ̃L�[���[�h���炻�ꂼ��̒l�𔻕ʂ���B
        // cpu: ���[�U���[�h��CPU�g�p�ʁA�V�X�e�����[�h�ł�CPU�g�p�ʁA
        // �^�X�N�҂��ł�CPU�g�p��
        // cpu0-x: cpu����CPU�g�p��
        try
        {
            while ((str = br.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(str);
                String token = st.nextToken();

                if (token.equals(CPU_VALUE_KEY))
                {
                    token = st.nextToken();
                    long cpuUser = Long.parseLong(token) * JIFFY_TO_NANO;
                    token = st.nextToken();
                    token = st.nextToken();
                    long cpuSystem = Long.parseLong(token) * JIFFY_TO_NANO;
                    token = st.nextToken();
                    sid.setCpuTask(Long.parseLong(token) * JIFFY_TO_NANO);
                    if(st.hasMoreTokens())
                    {
                        token = st.nextToken();
                        sid.setCpuIoWait(Long.parseLong(token) * JIFFY_TO_NANO);
                    }
                    while (st.hasMoreTokens())
                    {
                        token = st.nextToken();
                        cpuUser += Long.parseLong(token) * JIFFY_TO_NANO;
                    }
                    sid.setCpuSystem(cpuSystem);
                    sid.setCpuUser(cpuUser);
                }
                else if (token.equals(cpuLoadParamKey))
                {
                    token = st.nextToken();
                    cpuXXXList.add(Long.parseLong(token));
                    cpuNo = cpuNo + 1;
                    cpuLoadParamKey = CPU_VALUE_KEY + cpuNo;
                }
            }

            long[] cpuArray = new long[cpuXXXList.size()];
            Long temp = new Long("0");
            for (int index = 0; index < cpuXXXList.size(); index++)
            {
                temp = (Long)cpuXXXList.get(index);
                cpuArray[index] = (temp * JIFFY_TO_NANO);
            }
            sid.setCpuArray(cpuArray);
        }
        catch (NumberFormatException nfex)
        {
            SystemLogger.getInstance().warn(nfex);
        }
        catch (NoSuchElementException nseex)
        {
            SystemLogger.getInstance().warn(nseex);
        }
        catch (IOException ioex)
        {
            SystemLogger.getInstance().warn(ioex);
        }
        finally
        {
            try
            {
                br.close();
            }
            catch (IOException ioex)
            {
                SystemLogger.getInstance().warn(ioex);
            }
        }
    }

    private void loadProcVmstat(StatInfo sid, String vmstatFilePath)
    {
        FileReader fr;
        File vmstatFile = new File(vmstatFilePath);

        if (vmstatFile.exists() == false)
        {
            return;
        }

        BufferedReader br2 = null;
        try
        {
            fr = new FileReader(vmstatFile);
            br2 = new BufferedReader(fr);
        }
        catch (FileNotFoundException fnfex)
        {
            SystemLogger.getInstance().warn(fnfex);
            return;
        }
        catch (Exception ioex)
        {
            SystemLogger.getInstance().warn(ioex);
            return;
        }

        // vmstat�t�@�C������ǂݍ��񂾃f�[�^�̉��
        // page: page�̓��o�͗�
        try
        {
            String str2;
            while ((str2 = br2.readLine()) != null)
            {

                StringTokenizer st = new StringTokenizer(str2);
                String token = st.nextToken();
                if (token.equals(PAGEIN_VALUE_KEY))
                {
                    token = st.nextToken();
                    sid.setPageIn(Long.parseLong(token) * BLOCK_TO_BYTE);
                }
                else if (token.equals(PAGEOUT_VALUE_KEY))
                {
                    token = st.nextToken();
                    sid.setPageOut(Long.parseLong(token) * BLOCK_TO_BYTE);
                }
            }
        }
        catch (NumberFormatException nfex)
        {
            SystemLogger.getInstance().warn(nfex);
        }
        catch (NoSuchElementException nseex)
        {
            SystemLogger.getInstance().warn(nseex);
        }
        catch (IOException ioex)
        {
            SystemLogger.getInstance().warn(ioex);
        }
        finally
        {
            try
            {
                if (br2 != null)
                {
                    br2.close();
                }
            }
            catch (IOException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
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
        File file = new File(PROC_MEMINFO_PATH);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(line);
                line = reader.readLine();

                String paramName = tokenizer.nextToken();
                String paramValueString = tokenizer.nextToken();
                if (paramValueString == null)
                {
                    continue;
                }
                long paramValue = Long.parseLong(paramValueString);

                if (paramName.equals(MEM_TOTAL))
                {
                    memInfo.setMemTotal(paramValue * KILOBYTE_TO_BYTE);
                }
                else if (paramName.equals(MEM_FREE))
                {
                    memInfo.setMemFree(paramValue * KILOBYTE_TO_BYTE);
                }
                else if (paramName.equals(BUFFERS))
                {
                    memInfo.setBufferes(paramValue * KILOBYTE_TO_BYTE);
                }
                else if (paramName.equals(CACHED))
                {
                    memInfo.setCached(paramValue * KILOBYTE_TO_BYTE);
                }
                else if (paramName.equals(SWAP_TOTAL))
                {
                    memInfo.setSwapTotal(paramValue * KILOBYTE_TO_BYTE);
                }
                else if (paramName.equals(SWAP_FREE))
                {
                    memInfo.setSwapFree(paramValue * KILOBYTE_TO_BYTE);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (NumberFormatException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        return memInfo;
    }

    /**
     * /proc/diskstats�̈ȉ��̏���DiskStats�ɃZ�b�g���A�Ԃ��B<br>
     * <ul>
     *   <li>�t�@�C�����͗ʁF</li>
     *   <li>�t�@�C���o�͗ʁF</li>
     * </ul>
     * @return MemInfo /proc/meminfo�̏��
     */
    private DiskStats parseDiskStats()
    {
        DiskStats diskStats = new DiskStats();
        File file = new File(PROC_DISKSTATS_PATH);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            long readSector = 0;
            long writeSector = 0;
            while (line != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(line);
                line = reader.readLine();

                try
                {
                    if (tokenizer.countTokens() >= DISKSTATS_TOKEN_MAX)
                    {
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        readSector += Long.parseLong(tokenizer.nextToken());
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        writeSector += Long.parseLong(tokenizer.nextToken());
                    }
                }
                catch (NumberFormatException nfe)
                {
                    SystemLogger.getInstance().warn(nfe);
                }
            }
            diskStats.setDiskReadSector(readSector);
            diskStats.setDiskWriteSector(writeSector);
        }
        catch (FileNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (NumberFormatException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        return diskStats;
    }
    
    /** 
     * ���\�[�X�g�p�󋵂̃f�[�^ procInfo ��Ԃ�
     * @return procInfo
     */
    public ProcInfo getProcInfo()
    {
        return this.procInfo_;
    }

}
