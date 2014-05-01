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
package jp.co.acroquest.endosnipe.javelin.converter.thread.monitor;

import java.lang.management.ThreadInfo;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;

/**
 * �X���b�h�Ď����s���ۂɎ��s����Task�B
 * 
 * @author eriguchi
 */
public interface ThreadMonitorTask
{
    /**
     * Task�����s���邩�ǂ����𔻒肷��B
     * 
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param threadInfo ���݂̃X���b�h���B
     * @return Task�����s���邩�ǂ����B
     */
    boolean isTarget(ThreadInfo prevThreadInfo, ThreadInfo threadInfo);

    /**
     * �w�肵��node�ɏ���ǉ�����B
     * 
     * @param node �Ώۂ�CallTreeNode�B
     * @param threadInfo ���݂̃X���b�h���B
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param maxDepth �擾����X�^�b�N�g���[�X�̐[���B
     */
    void updateNode(CallTreeNode node, ThreadInfo threadInfo, ThreadInfo prevThreadInfo,
            int maxDepth);

    /**
     * �����Ƃ��ė^����ꂽ������ɃC�x���g�𑗐M����B
     * 
     * @param threadId �X���b�hID
     * @param threadInfo ���݂̃X���b�h���B
     * @param prevThreadInfo �O��̃X���b�h���B
     * @param maxDepth �擾����X�^�b�N�g���[�X�̐[���B
     */
    void sendEvent(final Long threadId, final ThreadInfo threadInfo,
            final ThreadInfo prevThreadInfo, final int maxDepth);

    /**
     * �O��̃X���b�h����p���Ď���̔���܂łɕێ�����K�v����������X�V����B
     * 
     * @param threadId �X���b�hID
     * @param prevThreadInfo �O��̃X���b�h���B
     */
    void updateInfo(final Long threadId, final ThreadInfo prevThreadInfo);

    /**
     * �����Ƃ��Ďw�肳�ꂽ�X���b�hID�̕ێ������N���A����B
     * 
     * @param threadId �X���b�hID
     */
    void clearInfo(final Long threadId);
}
