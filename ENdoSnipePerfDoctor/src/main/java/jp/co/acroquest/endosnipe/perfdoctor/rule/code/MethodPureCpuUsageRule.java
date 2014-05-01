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
 * ���\�b�h�̊J�n����I���܂ł̊Ԃ̏���CPU���Ԃ��A臒l�𒴂������Ƃ����o����B</br> ���\�b�h�I��������JavelinLogElement����͂��A
 * ���\�b�h�̊J�n����I���܂łɎg�p����CPU���Ԃ�臒l�ɒB����臒l(�P��:msec)�ACPU����(�P��:msec)���o�͂���B<br>
 * 
 * ������e</br> <li>baseInfo[ID] ���uReturn�v�ł��邱�ƁB <li>detailInfo[JMXInfo] ��
 * thread.currentThreadCpuTime.delta �̒l��臒l�ȏ�ł��邱�ƁB
 * 
 * @author Sakamoto
 */
public class MethodPureCpuUsageRule extends SingleElementRule implements JavelinLogConstants
{
    /** �x���Ɣ��f����CPU���Ԃ̃f�t�H���g�l�B */
    private static final int DEFAULT_THRESHOLD = 3000;

    /** CPU���Ԃ�臒l(�P��:msec) */
    public long              threshold         = DEFAULT_THRESHOLD;

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

        // ����CPU���Ԃ��擾����
        Map<String, String> extraInfo =
                                        JavelinLogUtil.parseDetailInfo(element,
                                                                       JavelinParser.TAG_TYPE_EXTRAINFO);
        String cpuTimeStr = extraInfo.get(EXTRAPARAM_PURECPUTIME);

        if (cpuTimeStr == null)
        {
            // ��͏����擾�ł��Ȃ��ꍇ�́A������s��Ȃ�
            return;
        }

        double cpuTimeDouble = 0;
        long cpuTime = 0;
        cpuTimeDouble = Double.parseDouble(cpuTimeStr);
        cpuTime = (long)cpuTimeDouble;

        // 臒l�𒴂��Ă����ꍇ�A�G���[�Ƃ���
        if (cpuTime >= this.threshold)
        {
            String threadName = element.getThreadName();
            addError(element, this.threshold, cpuTime, threadName);
        }
    }
}
