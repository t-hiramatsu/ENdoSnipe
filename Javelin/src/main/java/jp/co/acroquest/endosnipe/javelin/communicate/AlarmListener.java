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
package jp.co.acroquest.endosnipe.javelin.communicate;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;

/**
 * �A���[����ʒm���鏈������������C���^�t�F�[�X�B
 * 
 * @author unknown
 */
public interface AlarmListener
{
    /**
     * �������l���߂̃A���[����ʒm����ۂɎg�p����B
     * 
     * @param node �������l���߂����Ăяo���̏��B
     */
    void sendExceedThresholdAlarm(CallTreeNode node);

    /**
     * ���[�g�m�[�h(�R�[���c���[�̒��_)�݂̂�Alarm�̑ΏۂƂ��邩�ǂ������w�肷��B
     * 
     * @return true�Ȃ�΃��[�g�m�[�h�݂̂�ΏۂƂ���Bfalse�Ȃ�ΑS�Ă�Alarm��ΏۂƂ���B
     */
    boolean isSendingRootOnly();

    /**
     * ���������\�b�h
     * 
     * @throws Exception ���������ɔ���������O
     */
    void init() throws Exception;
}
