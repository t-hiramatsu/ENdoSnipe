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

import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.Messages;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * CPU�̃A�C�h�����Ԃ��A臒l�𒴂������Ƃ����o���܂��B<br/>
 * JavelinLogElement����͂��A���s���Ԃ�CPU���Ԃ̍���臒l�ɒB�����ꍇ�A
 * 臒l(�P��:msec)�A�A�C�h������(�P��:msec)���o�͂��܂��B�B<br/><br/>
 * 
 * ������e<br/> 
 * <li>baseInfo[ID] ���uCall�v�ł��邱�ƁB 
 * <li>detailInfo[JMXInfo] �� duration �� thread.currentThreadCpuTime.delta �̍���臒l�ȏ�ł��邱�ƁB
 * 
 * @author fujii
 */
public class IdleTimeRule extends SingleElementRule implements JavelinLogConstants
{
    /** �A�C�h�����Ԃ�臒l(�P��:msec) */
    public long                 threshold;

    /** ��͏����擾�ł��Ȃ��ꍇ�̃��O���b�Z�[�W */
    private static final String MESSAGE_NO_JMXINFO =
                                                     Messages.getMessage("endosnipe.perfdoctor.rule.code.MethodPureCpuUsageRule.InfoNotGet",
                                                                         EXTRAPARAM_IDLETIME);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doJudgeElement(final JavelinLogElement element)
    {
        // ���\�b�h�̖߂�łȂ��ꍇ�́A������s���܂���B
        List<?> baseInfo = element.getBaseInfo();
        String id = (String)baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(id) == false)
        {
            return;
        }

        // �X���b�h��CPU���Ԃ��擾���܂��B
        Map<String, String> argsInfo =
                                       JavelinLogUtil.parseDetailInfo(element,
                                                                      JavelinParser.TAG_TYPE_JMXINFO);
        String cpuTimeStr = argsInfo.get(JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA);

        // ���s���Ԃ��擾���܂��B
        Map<String, String> extraInfo =
                                        JavelinLogUtil.parseDetailInfo(element,
                                                                       JavelinParser.TAG_TYPE_EXTRAINFO);
        String execTimeStr = extraInfo.get(EXTRAPARAM_DURATION);

        if (cpuTimeStr == null || execTimeStr == null)
        {
            // ��͏����擾�ł��Ȃ��ꍇ�́A������s���܂���B
            log(MESSAGE_NO_JMXINFO, element, null);
            return;
        }

        double cpuTimeDouble = 0;
        long cpuTime = 0;
        long execTime = 0;
        try
        {
            cpuTimeDouble = Double.parseDouble(cpuTimeStr);
            cpuTime = (long)cpuTimeDouble;
            execTime = Long.valueOf(execTimeStr);
        }
        catch (NumberFormatException ex)
        {
            return;
        }
        long idleTime = execTime - cpuTime;

        // 臒l�𒴂��Ă����ꍇ�A�G���[�Ƃ��܂��B
        if (idleTime >= this.threshold)
        {
            String threadName = element.getThreadName();
            addError(element, this.threshold, idleTime, threadName);
        }
    }
}
