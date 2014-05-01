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
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * ���\�b�h�̊J�n����I���܂ł̊Ԃ�CPU���Ԃ��A臒l�𒴂������Ƃ����o����B</br> ���\�b�h�I��������JavelinLogElement����͂��A
 * ���\�b�h�̊J�n����I���܂łɎg�p����CPU���Ԃ�臒l�ɒB����臒l(�P��:msec)�ACPU����(�P��:msec)���o�͂���B<br>
 * 
 * ������e</br> <li>baseInfo[ID] ���uReturn�v�ł��邱�ƁB <li>detailInfo[JMXInfo] ��
 * thread.currentThreadCpuTime.delta �̒l��臒l�ȏ�ł��邱�ƁB
 * 
 * @author tsukano
 */
public class MethodCpuUsageRule extends SingleElementRule implements JavelinLogConstants
{
    /** CPU���Ԃ�臒l(�P��:msec) */
    public long threshold;

    /** CPU���Ԃ�臒l�̍ŏ��l(�P��:msec) */
    // private static final long MIN_THRESHOLD = 0;
    /** CPU���Ԃ�臒l�̍ő�l(�P��:msec) */
    // private static final long MAX_THRESHOLD = Long.MAX_VALUE;
    // ���o�[�W�����ł�validation�͈���Ȃ��B
    // /** validate�G���[�̃��O���b�Z�[�W */
    // private static final String MESSAGE_VALIDATE_ERROR
    // = "threshold(%1d)���ݒ�\�͈�(%2d - %3d)�ł��B";
    // �����_�ł͎g�p���Ȃ��Bvalidate����������ۂɂ͂����p����B
    /**
     * {@inheritDoc}</br> threshold�ɐݒ肵���l��validate���s���B
     */
    /*
     * @Override public void validate() { if (this.threshold < MIN_THRESHOLD ||
     * MAX_THRESHOLD < this.threshold) { String message =
     * String.format(MESSAGE_VALIDATE_ERROR, this.threshold, MIN_THRESHOLD,
     * MAX_THRESHOLD); log(message, null, null);
     * 
     * // TODO:validate�����������ۂɏC�����邱�ƁB addValidationError(MESSAGE_VALIDATE_ERROR,
     * this.threshold, MIN_THRESHOLD, MAX_THRESHOLD); } }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void doJudgeElement(final JavelinLogElement element)
    {
        // ���\�b�h�̖߂�łȂ��ꍇ�́A������s��Ȃ�
        List<String> baseInfo = element.getBaseInfo();
        String id = baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(id) == false)
        {
            return;
        }

        // SQL���s�͏��O����
        String className = baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_CLASS);
        if (isSqlExec(className) == true)
        {
            return;
        }

        // JMX��񂩂�CPU���Ԃ��擾����
        Map<String, String> map =
                                  JavelinLogUtil.parseDetailInfo(element,
                                                                 JavelinParser.TAG_TYPE_JMXINFO);
        String cpuTimeStr = map.get(JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA);

        //CPU���Ԃ��擾�ł��Ȃ��ꍇ�A������s��Ȃ��B
        if (cpuTimeStr == null)
        {
            return;
        }

        double cpuTimeDouble = 0;
        long cpuTime = 0;
        try
        {
            cpuTimeDouble = Double.parseDouble(cpuTimeStr);
            cpuTime = (long)cpuTimeDouble;
        }
        catch (NumberFormatException ex)
        {
            return;
        }

        // 臒l�𒴂��Ă����ꍇ�A�G���[�Ƃ���
        if (cpuTime >= this.threshold)
        {
            String threadName = element.getThreadName();
            addError(element, this.threshold, cpuTime, threadName);
        }
    }

}
