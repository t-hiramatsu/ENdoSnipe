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

import java.util.concurrent.atomic.AtomicLong;

/**
 * Javelin���ŕێ�����Ă���CallTreeNode���̍ő�l�A���ϒl���Z�o����N���X
 * CallTree�̗��p���I������ۂ�CallTree�̒l��ێ����A
 * ���\�[�X�擾�v���d����M���ƂɎ擾�����l�𑗐M����B
 * 
 * @author S.Kimura
 * @author sakamoto
 */
public class CallTreeNodeMonitor
{

    private static AtomicLong callTreeCount__ = new AtomicLong();

    private static AtomicLong maxNodeCount__ = new AtomicLong();

    private static AtomicLong allNodeCount__ = new AtomicLong();

    /**
     * �C���X�^���X���h�~�̂��߂̃R���X�g���N�^
     */
    private CallTreeNodeMonitor()
    {
        // Do nothing.
    }

    /**
     * �I������CallTree���ێ����Ă���nodeCount��ǉ�
     * 
     * @param nodeCount �ǉ�����nodeCount
     */
    public static void add(long nodeCount)
    {
        synchronized (maxNodeCount__)
        {
            if (nodeCount > maxNodeCount__.get())
            {
                maxNodeCount__.set(nodeCount);
            }
        }
        allNodeCount__.addAndGet(nodeCount);
        callTreeCount__.incrementAndGet();
    }

    /**
     * CallTree�ێ������擾����
     * 
     * @return CallTree�ێ���
     */
    public static long getCallTreeCount()
    {
        return callTreeCount__.get();
    }

    /**
     * CallTree�̍ő�Node�ێ������擾����
     * 
     * @return �ő�Node�ێ���
     */
    public static long getMaxNodeCount()
    {
        return maxNodeCount__.get();
    }

    /**
     * CallTree�̑�Node�ێ������擾����
     * 
     * @return ��Node�ێ���
     */
    public static long getAllNodeCount()
    {
        return allNodeCount__.get();
    }

    /**
     * �ێ����Ă���nodeCount��������
     */
    public static void clear()
    {
        callTreeCount__.set(0);
        maxNodeCount__.set(0);
        allNodeCount__.set(0);
    }
}
