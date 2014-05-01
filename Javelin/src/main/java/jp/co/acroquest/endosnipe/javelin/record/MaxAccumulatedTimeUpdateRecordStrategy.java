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

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.S2MaxAccumulatedTimeUpdateRecordStrategy;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * seasar��MaxAccumulatedTimeUpdateRecordStrategy�ɁA
 * Javelin���O�ʒm�d���̑��M������ǉ������X�g���e�W�[�B
 * 
 * @author eriguchi
 */
public class MaxAccumulatedTimeUpdateRecordStrategy extends
        S2MaxAccumulatedTimeUpdateRecordStrategy
{
    /** ���K��\������ */
    private static final int MEGA = 1000000;
    
    /**
     * �A���[���𔭐������邩�ǂ����𔻒肵�܂��B<br />
     * 
     * @param node {@link CallTreeNode}�I�u�W�F�N�g
     * @return �A���[���𔭐�������ꍇ�A<code>true</code>
     */
    public boolean judgeSendExceedThresholdAlarm(final CallTreeNode node)
    {
        boolean result = false;

        Invocation invocation = node.getInvocation();
        if (invocation.getCountFromStartup() > getIgnoreUpdateCount())
        {
            if (node.getAccumulatedTime() > invocation.getAlarmThreshold())
            {
                invocation.setAlarmThreshold(node.getAccumulatedTime());
                result = true;
            }
            
            long cpuTime = node.getCpuTime() / MEGA;
            if (cpuTime > invocation.getAlarmCpuThreshold())
            {
                invocation.setAlarmCpuThreshold(cpuTime);
                result = true;
            }
        }

        return result;
    }
    
    /**
     * Javelin���O�ʒm�d���𑗐M����R�[���o�b�N�I�u�W�F�N�g���쐬����B
     * 
     * @param node CallTreeNode
     * @return Javelin���O�ʒm�d���𑗐M����R�[���o�b�N�I�u�W�F�N�g
     */
    @Override
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
    @Override
    public JavelinLogCallback createCallback()
    {
        return new JvnFileNotifyCallback();
    }
}
