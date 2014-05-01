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

import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * StatsJavelinRecorder�ňȉ��̏������s�����ǂ������肷��Strategy�C���^�t�F�[�X�B</br>
 * <li>Javelin���O</li>
 * <li>�A���[���ʒm</li>
 * 
 * @author tsukano
 */
public interface RecordStrategy
{
    /**
     * �A���[����ʒm���邩�ǂ������肷��B
     * @param node {@link CallTreeNode}�I�u�W�F�N�g
     * @return true:�ʒm����Afalse:�ʒm���Ȃ�
     */
    boolean judgeSendExceedThresholdAlarm(CallTreeNode node);

    /**
     * �����Ɍ㏈�����s���B
     */
    void postJudge();

    /**
     * �R�[���o�b�N�I�u�W�F�N�g��Ԃ��B
     * 
     * @param node {@link CallTreeNode}�I�u�W�F�N�g
     * @return �R�[���o�b�N�I�u�W�F�N�g�B
     */
    JavelinLogCallback createCallback(CallTreeNode node);

    /**
     * �R�[���o�b�N�I�u�W�F�N�g��Ԃ��B
     * 
     * @return �R�[���o�b�N�I�u�W�F�N�g�B
     */
    JavelinLogCallback createCallback();
}
