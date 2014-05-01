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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;

/**
 * �t��GC���Ď����郂�j�^�N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
public class FullGCMonitor implements Runnable
{
    /** GC�����񐔂�ۑ�����Map */
    private Map<String, Long> gcCountMap_ = new HashMap<String, Long>();

    /** GC���s���Ԃ�ۑ�����Map */
    private Map<String, Long> gcTimeMap_ = new HashMap<String, Long>();

    /** �t��GC�𔭐�������GarbageCollector�̖��O�̃��X�g */
    private static final List<String> FULLGC_NAME_LIST;

    /** Javelin�̐ݒ�B */
    private static JavelinConfig config__ = new JavelinConfig();

    /** ���\�[�X���擾�p�I�u�W�F�N�g */
    private static ResourceCollector collector__;

    /** Singleton�C���X�^���X */
    private static FullGCMonitor instance__ = new FullGCMonitor();

    /** �X���[�v���� */
    private static final int SLEEP_TIME = 5000;

    static
    {
        String fullGCNameList = config__.getFullGCList();
        FULLGC_NAME_LIST = Arrays.asList(fullGCNameList.split(","));
        collector__ = ResourceCollector.getInstance();
    }

    /**
     * �I�u�W�F�N�g�̃C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^
     */
    private FullGCMonitor()
    {
        // Do Nothing.
    }

    /**
     * �t��GC�擾���j�^���擾���܂��B<br />
     * 
     * @return {@link FullGCMonitor}�I�u�W�F�N�g
     */
    public static FullGCMonitor getInstance()
    {
        return instance__;
    }

    /**
     * �t��GC���������Ă��邩�A5�b���ƂɃ`�F�b�N���܂��B<br />
     * �t��GC���������Ă���ꍇ�A�C�x���g���쐬���A���M���܂��B
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
            if (config__.isFullGCMonitor())
            {
                List<GarbageCollectorMXBean> gcBeanList =
                        ManagementFactory.getGarbageCollectorMXBeans();
                for (GarbageCollectorMXBean gcBean : gcBeanList)
                {
                    String gcName = gcBean.getName();
                    if (FULLGC_NAME_LIST.contains(gcName))
                    {
                        detect(gcBean, gcName);
                    }
                }
            }
            try
            {
                Thread.sleep(SLEEP_TIME);
            }
            catch (InterruptedException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /**
     * �t��GC�̌��o���s���܂��B<br />
     * [����]<br />
     *�@�EGC�̎��s�񐔂��O�񎞂��������Ă���Ƃ��B<br />
     * �EGC�̎��s���Ԃ�臒l���z���Ă���Ƃ��B
     * 
     * @param gcBean {@link GarbageCollectorMXBean}�I�u�W�F�N�g
     * @param gcName GarbageCollector��
     */
    private void detect(GarbageCollectorMXBean gcBean, String gcName)
    {
        Long lastGCCount = this.gcCountMap_.get(gcName);
        long gcCount = gcBean.getCollectionCount();

        // �P��ڂ́AGC�񐔂�1��ȏ�̂Ƃ��̂݁A�t��GC�C�x���g�𔭐�������B
        // �Q��ڈȍ~�́AGC�����񐔂������Ă����Ƃ��̂݁A�t��GC�C�x���g�𔭐�������B
        if (lastGCCount == null)
        {
            this.gcCountMap_.put(gcName, gcCount);
            if (gcCount > 0)
            {
                long gcTime = gcBean.getCollectionTime();
                CommonEvent event = createFullGCEvent(gcCount, gcTime);
                StatsJavelinRecorder.addEvent(event);
                this.gcTimeMap_.put(gcName, gcTime);
            }
        }
        else
        {
            this.gcCountMap_.put(gcName, gcCount);
            if (lastGCCount.longValue() < gcCount)
            {
                Long lastGCTime = this.gcTimeMap_.get(gcName);
                if (lastGCTime == null)
                {
                    lastGCTime = Long.valueOf(0);
                }
                long gcTime = gcBean.getCollectionTime() - lastGCTime.longValue();
                if (gcTime >= config__.getFullGCThreshold())
                {
                    CommonEvent event = createFullGCEvent(gcCount, gcTime);
                    StatsJavelinRecorder.addEvent(event);
                }
                this.gcTimeMap_.put(gcName, gcTime);
            }
        }
    }

    /**
     * FullGC�����C�x���g���쐬���܂��B<br />
     * [�C�x���g�`��]<br />
     * ���C�x���g��<br />
     *  FullGCDetected<br />
     * ���p�����[�^<br />
     *  javelin.fullgc.count=&lt;�t��GC������&gt;<br />
     *  javelin.fullgc.time=&lt;�t��GC���s����(5�b�ȓ��Ŕ�������GC���Ԃ̍��v�l)&gt;<br />
     *  javelin.fullgc.heapMemory=&lt;���݂̃q�[�v������&gt;<br />
     * 
     * @param count �t��GC�̔�����
     * @param time �t��GC�̎��s����
     * @return FullGC�����C�x���g
     */
    public CommonEvent createFullGCEvent(final long count, final long time)
    {
        CommonEvent event = new CommonEvent();
        event.setName(EventConstants.NAME_FULLGC);
        event.setLevel(CommonEvent.LEVEL_INFO);
        event.addParam(EventConstants.PARAM_FULLGC_COUNT, String.valueOf(count));
        event.addParam(EventConstants.PARAM_FULLGC_TIME, String.valueOf(time));

        Number memoryResource =
                collector__.getResource(TelegramConstants.ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED);
        if (memoryResource != null && memoryResource instanceof Long)
        {
            event.addParam(EventConstants.PARAM_FULLGC_HEAPMEMORY, String.valueOf(memoryResource));
        }

        return event;

    }
}
