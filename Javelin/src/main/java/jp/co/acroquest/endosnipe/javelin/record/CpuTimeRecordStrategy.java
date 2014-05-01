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
package jp.co.acroquest.endosnipe.javelin.record;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * CPU���ԁA��������TAT��臒l�ŏo�͂��邩�ǂ����𔻒肷��N���X
 * 
 * @author fujii
 * 
 */
public class CpuTimeRecordStrategy implements RecordStrategy
{
    /** �P�ʂ��}�C�N���b����b�ɕς��邽�߂̒l */
    private static final int NANO_TO_MILLI = 1000000;

    /** Javelin�̐ݒ�l */
    private final JavelinConfig config_ = new JavelinConfig();
    
    /** �ݒ肳��Ă����ہA�펞�o�͂��Ȃ��A�Ɣ��肷��Threshold */
    private static final long ALWAYS_FALSE_THRESHOLD = -1;

    /**
     * �A���[����ʒm���邩�ǂ������肷��</br>
     * CpuTime��javelin.alarmCpuThreshold�ɐݒ肵���l�ȏ�̂Ƃ��ɏo�͂���B
     * 
     * @param node CallTree�m�[�h
     * @return true:���O�ʒm����Afalse:���O�ʒm���Ȃ��B
     */
    public boolean judgeSendExceedThresholdAlarm(final CallTreeNode node)
    {
        boolean exceedAlarmThreshold = isAlarmThreshold(node);
        boolean exceedAlarmCpuThreshold = isAlarmCpuThresold(node);

        if (exceedAlarmThreshold || exceedAlarmCpuThreshold)
        {
            return true;
        }
        return false;
    }

    /**
     * �ݒ肵���x��������CPU���Ԃ�臒l�𒴂��Ă��邩���肷��B
     * 
     * @param node CallTree�m�[�h
     * @return �ݒ肵��CPU���Ԃ𒴂��Ă���B
     */
    private boolean isAlarmCpuThresold(final CallTreeNode node)
    {
        long alarmCpuThreshold = node.getInvocation().getAlarmCpuThreshold();
        if (alarmCpuThreshold == Invocation.THRESHOLD_NOT_SPECIFIED)
        {
            //JavelinConfig��臒l��-1�Ɛݒ肳��Ă����ꍇ�A�{����ɂ�郍�O�o�͍͂s��Ȃ�
            if(this.config_.getAlarmCpuThreashold() == ALWAYS_FALSE_THRESHOLD)
            {
                return false;
            }
            alarmCpuThreshold = this.config_.getAlarmCpuThreashold();
        }
        if (node.getCpuTime() / NANO_TO_MILLI >= alarmCpuThreshold)
        {
            return true;
        }
        return false;
    }

    /**
     * �ݒ肵���x�������̎��Ԃ�臒l�𒴂��Ă��邩���肷��B
     * 
     * @param node CallTree�m�[�h
     * @return �ݒ肵���x������臒l�𒴂��Ă���B
     */
    private boolean isAlarmThreshold(final CallTreeNode node)
    {
        long alarmThreshold = node.getInvocation().getAlarmThreshold();
        if (alarmThreshold == Invocation.THRESHOLD_NOT_SPECIFIED)
        {
            //JavelinConfig��臒l��-1�Ɛݒ肳��Ă����ꍇ�A�{����ɂ�郍�O�o�͍͂s��Ȃ�
            if(this.config_.getAlarmThreshold() == ALWAYS_FALSE_THRESHOLD)
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
     * Javelin���O�ʒm�d���𑗐M����R�[���o�b�N�I�u�W�F�N�g���쐬����B
     * 
     * @param node CallTree�m�[�h
     * @return �R�[���o�b�N
     */
    public JavelinLogCallback createCallback(final CallTreeNode node)
    {
        // �A���[��臒l�𒴂��Ă����ꍇ�̂�Javelin���O�ʒm�d���𑗐M����B
        if (this.judgeSendExceedThresholdAlarm(node) == false)
        {
            return null;
        }

        return createCallback();
    }

    /**
     * Javelin���O�ʒm�d���𑗐M����R�[���o�b�N�I�u�W�F�N�g���쐬����B
     * 
     * @return �R�[���o�b�N
     */
    public JavelinLogCallback createCallback()
    {
        return new JvnFileNotifyCallback();
    }
}
