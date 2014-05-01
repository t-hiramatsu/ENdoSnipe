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
package jp.co.acroquest.endosnipe.javelin;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * TAT�̒l���ݒ肵��臒l���z���Ă���ꍇ�A���O�t�@�C���o�́A�A���[���ʒm���s���B
 * @author eriguchi
 *
 */
public class S2DefaultRecordStrategy implements RecordStrategy
{
    private final JavelinConfig config_ = new JavelinConfig();
    
    /** �ݒ肳��Ă����ہA�펞�o�͂��Ȃ��A�Ɣ��肷��Threshold */
    private static final long ALWAYS_FALSE_THRESHOLD = -1;

    /**
     * �A���[����ʒm���邩�ǂ������肷��</br>
     * AccumulatedTime��javelin.alarmThreshold�ɐݒ肵���l�ȏ�̂Ƃ��ɏo�͂���B
     * @param node CallTreeNode
     * @return true:�A���[���ʒm���s���Afalse�F�A���[���ʒm���s��Ȃ��B
     */
    public boolean judgeSendExceedThresholdAlarm(final CallTreeNode node)
    {
        long alarmThreshold = node.getInvocation().getAlarmThreshold();
        if (alarmThreshold == Invocation.THRESHOLD_NOT_SPECIFIED)
        {
            //JavelinConfig��臒l��-1�Ɛݒ肳��Ă����ꍇ�A�{����ɂ�郍�O�o�͍͂s��Ȃ�
            if(this.config_.getAlarmCpuThreashold() == ALWAYS_FALSE_THRESHOLD)
            {
                return false;
            }
            alarmThreshold = this.config_.getAlarmThreshold();
        }
        if (node.getAccumulatedTime() >= alarmThreshold)
        {
            return true;
        }
        return false;
    }

    /**
     * �������Ȃ��B
     */
    public void postJudge()
    {
        // Do Nothing
    }

    /**
     * �������Ȃ��B
     * @param node CallTreeNode
     * @return null
     */
    public JavelinLogCallback createCallback(final CallTreeNode node)
    {
        // Do Nothing
        return null;
    }

    /**
     * �������Ȃ��B
     * @return null
     */
    public JavelinLogCallback createCallback()
    {
        // Do Nothing
        return null;
    }
}
