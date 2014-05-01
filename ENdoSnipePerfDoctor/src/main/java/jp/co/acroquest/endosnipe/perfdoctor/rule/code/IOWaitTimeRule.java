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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * I/O�҂��̒������������o���郋�[���ł��B<br />
 * I/O�҂����Ԃ͒��ڌ��o�ł��Ȃ����߁A���\�b�h�� TAT ���� CPU���ԁAWAIT���ԁA
 * �u���b�N���Ԃ�������I/O�҂����Ԃ����o���܂��B<br />
 * ���̂悤�ɂ��Č��o���� I/O �҂����Ԃ�臒l���������ꍇ�Ɍx�����܂��B<br />
 * 
 * @author akita
 */
public class IOWaitTimeRule extends SingleElementRule
{
    /** ���\�b�h��TAT��\�������� */
    private static final String DURATION                                      = "duration";

    /** ExtraInfo��\�������� */
    private static final String EXTRA_INFO                                    = "ExtraInfo";

    /**
     * �ڍ׏��擾�L�[:ThreadMXBean#getCurrentThreadCpuTime�p�����[�^�̍���
     * ���݂̃X���b�h�̍��v CPU ���Ԃ̍������i�m�b�P�ʂŕԂ��܂��B
     */
    private static final String JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA =
                                                                                "thread.currentThreadCpuTime.delta";

    /** 
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getWaitedTime�p�����[�^
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h���ʒm��ҋ@����
     * ���悻�̗ݐόo�ߎ��� (�~���b�P��) �̍�����Ԃ��܂��B
     */
    private static final String JMXPARAM_THREAD_THREADINFO_WAITED_TIME_DELTA  =
                                                                                "thread.threadInfo.waitedTime.delta";

    /**  
     * �ڍ׏��擾�L�[:ThreadMXBean#getThreadInfo#getBlockedTime�p�����[�^�̍���
     * �X���b�h�R���e���V�����Ď����L���ɂȂ��Ă���A���� ThreadInfo �Ɋ֘A����X���b�h�����j�^�[�ɓ��邩
     * �ē�����̂��u���b�N�������悻�̗ݐόo�ߎ��� (�~���b�P��) �̍�����Ԃ��܂��B
     */
    private static final String JMXPARAM_THREAD_THREADINFO_BLOCKED_TIME_DELTA =
                                                                                "thread.threadInfo.blockedTime.delta";

    /** �x���Ɣ��f���錟�o�l�̃f�t�H���g�l�B */
    private static final int    DEFAULT_THRESHOLD                             = 5000;

    /** ���o�l��臒l�B���̒l�𒴂����ۂɌx���𐶐�����B*/
    public long                 threshold                                     = DEFAULT_THRESHOLD;

    /**
     * CALL���O���̃��\�b�h��TAT�̒l�𒲍����A 臒l�𒴂��Ă����ۂɂ͌x������B
     * 
     * @param javelinLogElement
     *            ���O�̗v�f
     * 
     */
    @Override
    public void doJudgeElement(final JavelinLogElement javelinLogElement)
    {
        // ���ʎq��"Call"�łȂ��ꍇ�͔��肵�Ȃ��B
        String type = javelinLogElement.getBaseInfo().get(JavelinLogColumnNum.ID);
        boolean isCall = JavelinConstants.MSG_CALL.equals(type);
        if (isCall == false)
        {
            return;
        }

        if (JavelinLogUtil.isExistTag(javelinLogElement, JavelinParser.TAG_TYPE_JMXINFO) == false)
        {
            // �v������JMX�����擾���Ă��Ȃ��ꍇ�͔��肵�Ȃ�
            return;
        }

        // ExtraInfo�̓��e��\��Map���擾����B
        Map<String, String> extraInfo =
                                        JavelinLogUtil.parseDetailInfo(javelinLogElement,
                                                                       EXTRA_INFO);
        //JMX����ێ�����map���擾����B
        Map<String, String> jmxInfo =
                                      JavelinLogUtil.parseDetailInfo(javelinLogElement,
                                                                     JavelinParser.TAG_TYPE_JMXINFO);

        // ExtraInfo�̏���ێ�����map��胁�\�b�h��TAT�̒l�𓾂�B
        String durationString = extraInfo.get(DURATION);
        //JMX����map���AWait���Ԃ̒l�𓾂�B
        String waitTimeString = jmxInfo.get(JMXPARAM_THREAD_THREADINFO_WAITED_TIME_DELTA);
        //JMX����map���A���\�b�h��CPU���Ԃ̒l�𓾂�BCPU���Ԃ�msec�ɕϊ�����B
        String cpuTimeStr = jmxInfo.get(JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA);
        //JMX����map���A�u���b�N���Ԃ̒l�𓾂�B
        String blockTimeString = jmxInfo.get(JMXPARAM_THREAD_THREADINFO_BLOCKED_TIME_DELTA);

        //TAT,Wait����,CPU���ԁA�u���b�N���Ԃ����ꂼ��^�ϊ�����B�iCPU���Ԃ�nsec����msec�ɒP�ʂ�ύX����j

        long waitTime = 0;
        double cpuTimeDouble = 0;
        long cpuTime = 0;
        long blockTime = 0;
        long duration = 0;
        try
        {
            if (waitTimeString != null)
            {
                waitTime = Long.parseLong(waitTimeString);
            }
            if (cpuTimeStr != null)
            {
                cpuTimeDouble = Double.parseDouble(cpuTimeStr);
            }
            cpuTime = (long)cpuTimeDouble;
            if (blockTimeString != null)
            {
                blockTime = Long.parseLong(blockTimeString);
            }
            if (durationString != null)
            {
                duration = Long.parseLong(durationString);
            }
        }
        catch (NumberFormatException nfex)
        {
            return;
        }
        /*���\�b�h��TAT��CPU���ԁAWait���ԁA�u���b�N���Ԃ̘a�Ƃ̍������߂�B
        (���\�b�h��TAT)-((CPU����)+(Wait����)+(�u���b�N����))
        */
        long status = duration - (waitTime + cpuTime + blockTime);
        // �������o�l��臒l�ɒB����̂ł���΁A�x�����o���B
        if (status >= this.threshold)
        {
            addError(javelinLogElement, this.threshold, status);
        }
    }
}
