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
package jp.co.acroquest.endosnipe.javelin.converter.thread.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �X���b�h�_���v���擾���邽�߂̃X���b�h�ł��B<br />
 * 
 * @author fujii
 *
 */
public class ThreadDumpMonitor implements Runnable
{
    /** Singleton�I�u�W�F�N�g */
    private static ThreadDumpMonitor instance__ = new ThreadDumpMonitor();

    /** Javelin�̐ݒ�B */
    private final JavelinConfig config_ = new JavelinConfig();

    /** �O���CPU���� */
    private long lastCpuTime_ = 0;

    /** �O���JavaUp���� */
    private long lastUpTime_ = 0;

    /** Java�A�b�v�^�C���̍��� */
    private long upTimeDif_ = 0;

    /** �v���Z�X�� */
    private int processorCount_;

    /** CPU�g�p�� */
    private double cpuUsage_;

    /** �X���b�h�� */
    private int threadNum_;

    /** �e�X���b�h����CPU�g�p�� */
    private Map<Long, Double> threadCpuRateMap_;

    /** �O��̃X���b�h����CPU�g�p���� */
    private Map<Long, Long> lastThreadCpuMap_;

    /** ���\�[�X���擾�p�I�u�W�F�N�g */
    private static ResourceCollector collector__;

    /** CPU�g�p���ɕϊ����邽�߂̒萔 */
    private static final int CONVERT_RATIO = 10000;

    static
    {
        collector__ = ResourceCollector.getInstance();
    }

    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�B
     */
    private ThreadDumpMonitor()
    {
        // Do Nothing.
    }

    /**
     * {@link ThreadDumpMonitor}�I�u�W�F�N�g���擾���܂��B<br />
     * 
     * @return {@link ThreadDumpMonitor}�I�u�W�F�N�g
     */
    public static ThreadDumpMonitor getInstance()
    {
        return instance__;
    }

    /**
     * "javelin.thread.dump.interval"�̊Ԋu���ƂɁA
     * �X���b�h�_���v���o�͂��邩�ǂ������肵�܂��B
     */
    public void run()
    {
        try
        {
            Thread.sleep(JavelinTransformer.WAIT_FOR_THREAD_START);
        }
        catch (Exception ex)
        {
            ;
        }
        
        while (true)
        {
            try
            {
                int sleepTime = this.config_.getThreadDumpInterval();
                Thread.sleep(sleepTime);
                // �X���b�h����臒l���z���Ă���Ƃ��ACPU�g�p����臒l���z���Ă���Ƃ��ɁA
                // �t���X���b�h�_���v���o�͂���B
                synchronized (this)
                {
                    if (isThreadDump())
                    {
                        CommonEvent event = createThreadDumpEvent();
                        StatsJavelinRecorder.addEvent(event);
                    }
                }
            }
            catch (Throwable ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /**
     * �X���b�h�_���v���o�͂��邩�ǂ�����Ԃ��܂��B<br />
     * �����͈ȉ���2��
     * <ol>
     * <li>�X���b�h�_���v�擾�t���O��<code>true</code></li>
     * <li>�X���b�h����臒l���z���Ă���A����CPU�g�p����臒l���z���Ă���</li>
     * </ol>
     * 
     * @return �X���b�h�_���v���o�͂���ꍇ�A<code>true</code>
     */
    private synchronized boolean isThreadDump()
    {
        // CPU�g�p���́A�����ɂ���Čv�Z���邽�߁A�ُ�l���o�Ȃ��悤�ɖ���v�Z����B
        this.cpuUsage_ = getCpuUssage();
        this.threadCpuRateMap_ = getThreadCpuRateMap(this.upTimeDif_);

        // �X���b�h�_���v�擾�t���O��OFF�̂Ƃ��ɂ́A�X���b�h�_���v���擾���Ȃ��B
        if (config_.isThreadDump() == false)
        {
            return false;
        }

        int threasholdCpu = config_.getThreadDumpCpu();
        if (this.cpuUsage_ > threasholdCpu)
        {
            return true;
        }

        // �X���b�h����臒l���z���Ă���Ƃ��ɁA�X���b�h�_���v���o�͂���B
        int threasholdThread = config_.getThreadDumpThreadNum();
        this.threadNum_ = getThreadNum();
        if (this.threadNum_ > threasholdThread)
        {
            return true;
        }
        return false;
    }

    /**
     * CPU�g�p�����擾���܂��B<br />
     * CPU�g�p��=(CPU���Ԃ̍���)/(Java��UP���� * �v���Z�b�T��)
     * 
     * @return CPU�g�p��
     */
    private synchronized double getCpuUssage()
    {
        Number cpuResource = collector__.getResource(TelegramConstants.ITEMNAME_PROCESS_CPU_TOTAL_TIME);
        Number uptimeResource = collector__.getResource(TelegramConstants.ITEMNAME_JAVAUPTIME);
        Number processorResource =
                collector__.getResource(TelegramConstants.ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT);

        if (cpuResource == null || uptimeResource == null || processorResource == null)
        {
            return 0;
        }

        long cpuTime = cpuResource.longValue();
        long upTime = uptimeResource.longValue();
        this.processorCount_ = processorResource.intValue();

        // CPU�g�p����臒l���z���Ă���Ƃ��ɁA�X���b�h�_���v���o�͂���B
        double cpuUsage = 0;
        if (this.lastUpTime_ != 0)
        {
            this.upTimeDif_ = upTime - this.lastUpTime_;
            cpuUsage =
                    (double)(cpuTime - this.lastCpuTime_)
                            / (this.upTimeDif_ * CONVERT_RATIO * this.processorCount_);
        }
        this.lastCpuTime_ = cpuTime;
        this.lastUpTime_ = upTime;

        return cpuUsage;
    }

    /**
     * �X���b�h�����擾���܂��B<br />
     * 
     * @return �X���b�h��
     */
    private synchronized int getThreadNum()
    {
        long[] threadIds = ThreadUtil.getAllThreadIds();
        return threadIds.length;
    }

    /**
     * �X���b�h����CPU���Ԃ�ۑ�����Map��Ԃ��܂��B<br />
     * 
     * @return �X���b�h����CPU�g�p���Ԃ�ۑ�����Map
     */
    private synchronized Map<Long, Long> getThreadCpuMap()
    {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] threadIds = ThreadUtil.getAllThreadIds();

        Map<Long, Long> threadCpuMap = new LinkedHashMap<Long, Long>();
        for (int num = 0; num < threadIds.length; num++)
        {
            long threadId = threadIds[num];
            threadCpuMap.put(threadId, bean.getThreadCpuTime(threadId));
        }

        return threadCpuMap;

    }

    /**
     * �t���X���b�h�_���v�o�̓C�x���g���쐬���܂��B<br />
     * [�C�x���g�`��]<br />
     * javelin.thread.dump.threadNum=&lt;�X���b�h��&gt;<br />
     * javelin.thread.dump.cpu.total=&lt;CPU�g�p���̍��v�l&gt;<br />
     * javelin.thread.dump.cpu.&lt;�X���b�hID1&gt;=&lt;�X���b�h1��CPU�g�p��&gt;<br />
     * javelin.thread.dump.cpu.&lt;�X���b�hID2&gt;=&lt;�X���b�h2��CPU�g�p��&gt;<br />
     * javelin.thread.dump.cpu.&lt;�X���b�hID3&gt;=&lt;�X���b�h3��CPU�g�p��&gt;<br />
     * �E�E�E<br />
     * javelin.thread.dump=&lt;�t���X���b�h�_���v&gt;<br />
     * 
     * @return �t���X���b�h�_���v�o�́B
     */
    private synchronized CommonEvent createThreadDumpEvent()
    {
        CommonEvent event = new CommonEvent();

        String threadDump = ThreadUtil.getFullThreadDump();

        event.setName(EventConstants.NAME_THREAD_DUMP);
        event.addParam(EventConstants.PARAM_THREAD_DUMP_THREADNUM, String.valueOf(this.threadNum_));
        event.addParam(EventConstants.PARAM_THREAD_DUMP_CPU_TOTAL, String.valueOf(this.cpuUsage_));

        // �X���b�h����CPU�g�p�����o�͂���B
        for (Map.Entry<Long, Double> entry : this.threadCpuRateMap_.entrySet())
        {
            Long threadId = entry.getKey();
            Double cpuRate = entry.getValue();
            if (cpuRate != 0.0)
            {
                event.addParam(EventConstants.PARAM_THREAD_DUMP_CPU + '.' + threadId,
                               String.valueOf(cpuRate));
            }
        }

        event.addParam(EventConstants.PARAM_THREAD_DUMP, threadDump);

        return event;
    }

    /**
     * �X���b�h����CPU�g�p�����擾���܂��B<br />
     * 
     * @return �X���b�h����CPU�g�p��
     */
    private synchronized Map<Long, Double> getThreadCpuRateMap(long upTimeDif)
    {
        Map<Long, Long> threadCpuMap = getThreadCpuMap();
        Map<Long, Double> threadCpuRateMap = new LinkedHashMap<Long, Double>();
        for (Map.Entry<Long, Long> entry : threadCpuMap.entrySet())
        {
            Long threadId = entry.getKey();
            if (this.lastThreadCpuMap_ == null)
            {
                threadCpuRateMap.put(threadId, 0.0);
                continue;
            }
            Long cpuTime = entry.getValue();
            Long lastCpuTime = this.lastThreadCpuMap_.get(threadId);
            if (upTimeDif == 0)
            {
                threadCpuRateMap.put(threadId, 0.0);
            }
            else if (lastCpuTime != null)
            {
                double threadCpuRate =
                        (double)(cpuTime - lastCpuTime)
                                / (upTimeDif * CONVERT_RATIO * this.processorCount_);
                threadCpuRateMap.put(threadId, threadCpuRate);
            }
            else
            {
                double threadCpuRate =
                        (double)(cpuTime) / (upTimeDif * CONVERT_RATIO * this.processorCount_);
                threadCpuRateMap.put(threadId, threadCpuRate);
            }
        }
        this.lastThreadCpuMap_ = threadCpuMap;
        return threadCpuRateMap;
    }

    /**
     * �X���b�h�_���v�擾�C�x���g�𑗐M���܂��B<br />
     *
     * @param telegramId �d�� ID
     */
    public synchronized void sendThreadDumpEvent(final long telegramId)
    {
        this.cpuUsage_ = getCpuUssage();
        this.threadNum_ = getThreadNum();
        this.threadCpuRateMap_ = getThreadCpuRateMap(this.upTimeDif_);
        CommonEvent event = createThreadDumpEvent();
        StatsJavelinRecorder.addEvent(event, telegramId);
    }
}
