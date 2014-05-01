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
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * �K���o�͂���X�g���e�W�[�B
 * 
 * @author eriguchi
 */
public class AllRecordStrategy implements RecordStrategy
{
    /**
     * �K��Javelin���O�ʒm���s���܂��B
     * 
     * @param node �g�p���Ȃ��B
     * @return Javelin���O�ʒm�B
     */
    public JavelinLogCallback createCallback(CallTreeNode node)
    {
        return createCallback();
    }

    /**
     * �K��Javelin���O�ʒm���s���܂��B
     * 
     * @return Javelin���O�ʒm�B
     */
    public JavelinLogCallback createCallback()
    {
        return new JvnFileNotifyCallback();
    }

    /**
     * ���true��Ԃ��܂��B
     * 
     * @param node �g�p���Ȃ��B
     * @return true
     */
    public boolean judgeSendExceedThresholdAlarm(CallTreeNode node)
    {
        return true;
    }

    /**
     * �������܂���B
     */
    public void postJudge()
    {
        // Do Nothing
    }
}
