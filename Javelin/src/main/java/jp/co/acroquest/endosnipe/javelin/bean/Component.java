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
package jp.co.acroquest.endosnipe.javelin.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.javelin.RootInvocationManager;

/**
 * Component�N���X
 * @author acroquest
 *
 */
public class Component implements ComponentMBean, Serializable
{
    /** �V���A���o�[�W����ID */
    private static final long             serialVersionUID = 934662584633636762L;

    /** �N���X�� */
    private final String                  className_;

    /** invocationMap */
    private final Map<String, Invocation> invocationMap_   =
                                                     new ConcurrentHashMap<String, Invocation>();

    /** ���\�b�h���̃��X�g */
    private final List<String>            methodNameList_  =
                                             Collections.synchronizedList(new LinkedList<String>());

    /**
     * �R���X�g���N�^
     * @param className �N���X��
     */
    public Component(final String className)
    {
        className_ = className;
    }

    /**
     * {@inheritDoc}
     */
    public String getClassName()
    {
        return className_;
    }

    /**
     * {@inheritDoc}
     */
    public Invocation[] getAllInvocation()
    {
        int size = invocationMap_.values().size();
        Invocation[] invocations = invocationMap_.values().toArray(new Invocation[size]);
        return invocations;
    }

    /**
     * invovation��ǉ����܂�
     * @param invocation �ǉ�����invocation
     */
    public synchronized void addInvocation(final Invocation invocation)
    {
        String methodName = invocation.getMethodName();
        methodNameList_.add(methodName);
        invocationMap_.put(methodName, invocation);
    }

    /**
     * �Â����\�b�h�����폜���A�V�������\�b�h����ǉ����܂��B
     *
     * @param invocation �ǉ����郁�\�b�h���
     * @return �폜���ꂽ���\�b�h���
     */
    public synchronized Invocation addAndDeleteOldestInvocation(final Invocation invocation)
    {
        Invocation removedInvoction = null;
        if (this.methodNameList_.size() > 0 && this.invocationMap_.size() > 0)
        {
            long averageDuration = getTotalDuration() / this.invocationMap_.size();
            Iterator<String> methodIterator = this.methodNameList_.iterator();
            while (methodIterator.hasNext())
            {
                String deleteCandidateKey = methodIterator.next();
                Invocation deleteCandidateInvocation = this.invocationMap_.get(deleteCandidateKey);
                if (deleteCandidateInvocation != null
                        && deleteCandidateInvocation.getTotal() <= averageDuration)
                {
                    methodIterator.remove();
                    removedInvoction = this.invocationMap_.remove(deleteCandidateKey);
                    RootInvocationManager.removeInvocation(deleteCandidateInvocation);
                    break;
                }
            }
        }

        addInvocation(invocation);
        return removedInvoction;
    }

    /**
     * invocation���擾���܂��B 
     * @param methodName ���\�b�h��
     * @return invocation
     */
    public Invocation getInvocation(final String methodName)
    {
        return invocationMap_.get(methodName);
    }

    /**
     * invocation��Map�̃T�C�Y���擾����B
     * @return invocation��Map�̃T�C�Y
     */
    public int getRecordedInvocationNum()
    {
        return invocationMap_.size();
    }

    /**
     * {@inheritDoc}
     */
    public void reset()
    {
        for (Invocation invocation : invocationMap_.values())
        {
            invocation.reset();
        }
    }

    /**
     * ���̃N���X���ɂ��邷�ׂẴ��\�b�h�̍��v���Ԃ̍��v���v�Z���܂��B
     *
     * @return ���s���Ԃ̍��v�i�~���b�j
     */
    private long getTotalDuration()
    {
        long totalDuration = 0;
        for (Map.Entry<String, Invocation> entry : this.invocationMap_.entrySet())
        {
            totalDuration += entry.getValue().getTotal();
        }
        return totalDuration;
    }
}
