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
package jp.co.acroquest.endosnipe.javelin.helper;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.VMStatus;

/**
 * JMX����VM�̏�Ԃ��擾���A�L�^���邽�߂Ɏg�p����B
 * 
 * �ȉ���MXBean����A�v���p�e�B���擾����B<br>
 * <br>
 * <table border="1" cellspacing="0">
 * <tr>
 *  <th>MXBean����</th>
 *  <th>�v���p�e�B</th>
 *  <th>���l</th>
 * </tr>
 * <tr>
 *  <td>ThreadMXBean</td>
 *  <td>
 *      currentThreadCpuTime<br>
 *      currentThreadUserTime<br>
 *      threadInfo.blockedCount<br>
 *      threadInfo.blockedTime<br>
 *      threadInfo.waitedCount<br>
 *      threadInfo.waitedTime<br>
 *  </td>
 *  <td>&nbsp;</td>
 * </tr>
 * <tr>
 *  <td>GarbageCollectorMXBean</td>
 *  <td>
 *      collectionCount<br>
 *      collectionTime<br>
 *  </td>
 *  <td>GarbageCollector����������ꍇ�́A�S�Ă�GarbageCollector�̒l�̍��v���g�p����B&nbsp;</td>
 * </tr>
 * <tr>
 *  <td>MemoryPoolMXBean</td>
 *  <td>
 *      peakUsage.usage<br>
 *  </td>
 *  <td>
 *      MemoryPool����������ꍇ�́A�S�Ă�MemoryPool�̒l�̍��v���g�p����B<br>
 *      �s�[�N�g�p�ʂ̃��Z�b�g�́ACallTree�̏��ThreadLocal�ɂȂ��ꍇ�Ɏ��{����B&nbsp;
 *  </td>
 * </tr>
 * </table>
 * 
 * @author eriguchi
 *
 */
public class VMStatusHelper
{
    private static RuntimeMXBean runtimeMBean__ = ManagementFactory.getRuntimeMXBean();

    private final ThreadMXBean threadMBean_ = ManagementFactory.getThreadMXBean();

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeanList_ =
            ManagementFactory.getGarbageCollectorMXBeans();

    /** �v���Z�X�� */
    private static String processName__;

    static
    {
        processName__ = runtimeMBean__.getName();
    }

    /**
     * ���������s���B
     */
    public void init()
    {
    }

    /**
     * JMX����VM�̏�Ԃ��擾���AVMStatus�I�u�W�F�N�g�𐶐�����B<br>
     * @param callTreeRecorder  callTreeRecorder
     * 
     * @return JMX����VM�̏�Ԃ��擾�����AVMStatus�I�u�W�F�N�g�B
     */
    public VMStatus createVMStatus(CallTreeRecorder callTreeRecorder)
    {
        long cpuTime  = this.threadMBean_.getCurrentThreadCpuTime();
        long userTime = this.threadMBean_.getCurrentThreadUserTime();
        long threadId = callTreeRecorder.getThreadId();

        long blockedCount;
        long blockedTime;
        long waitedCount;
        long waitedTime;
        
        if (threadId != 0)
        {
            ThreadInfo threadInfo = this.threadMBean_.getThreadInfo(threadId);
            if (threadInfo != null)
            {
                blockedCount = threadInfo.getBlockedCount();
                blockedTime  = threadInfo.getBlockedTime();
                waitedCount  = threadInfo.getWaitedCount();
                waitedTime   = threadInfo.getWaitedTime();
            }
            else
            {
                blockedCount = 0;
                blockedTime  = 0;
                waitedCount  = 0;
                waitedTime   = 0;
            }
        }
        else
        {
            blockedCount = 0;
            blockedTime  = 0;
            waitedCount  = 0;
            waitedTime   = 0;
        }

        long collectionCount = 0;
        long collectionTime  = 0;
        
        try
        {
            int size = this.garbageCollectorMXBeanList_.size();
            for (int index = 0; index < size; index++)
            {
                GarbageCollectorMXBean bean = garbageCollectorMXBeanList_.get(index);
                collectionCount += bean.getCollectionCount();
                collectionTime  += bean.getCollectionTime();
            }
        }
        catch(Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        VMStatus vmStatus = new VMStatus(cpuTime, 
                                         userTime, 
                                         blockedCount, 
                                         blockedTime, 
                                         waitedCount, 
                                         waitedTime, 
                                         collectionCount, 
                                         collectionTime);
        
        return vmStatus;
    }

    /**
     * �v���Z�X�����擾���܂��B<br />
     * 
     * @return �v���Z�X��
     */
    public static String getProcessName()
    {
        return processName__;
    }
}
