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
package jp.co.acroquest.endosnipe.javelin.converter.concurrent.monitor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * �I�u�W�F�N�g�̃n�b�V���R�[�h�ƃX���b�hID�̊֌W��ێ�����G���g��
 * @author fujii
 */
public class ConcurrentMonitorObject
{
    /** �Ď��Ώۂ̃I�u�W�F�N�g�B */
    private WeakReference<Object> ref_;

    /** �n�b�V���R�[�h */
    private int hashCode_;

    /** ���ʎq�B */
    private String identifier_;

    /** �O��̃X���b�hID */
    private long prevThreadId_;

    /** �e���ځB */
    private List<ConcurrentMonitorItem> itemList_;

    /** ���b�N�I�u�W�F�N�g�̃��X�g�B */
    private List<String> lockedObjectList_;

    /** �X���b�h�ԍ����̃R���p���[�^�B */
    private Comparator<? super ConcurrentMonitorItem> comparator_ =
            new Comparator<ConcurrentMonitorItem>() {
                public int compare(ConcurrentMonitorItem o1, ConcurrentMonitorItem o2)
                {
                    if (o1 == o2)
                    {
                        return 0;
                    }
                    if (o1 == null || o2 == null)
                    {
                        return 0;
                    }

                    if (o1.getThreadId() == o2.getThreadId())
                    {
                        return 0;
                    }
                    if (o1.getThreadId() < o2.getThreadId())
                    {
                        return -1;
                    }
                    
                    return 1;
                }
            };

    /** �ۑ����郁�b�Z�[�W�̍ő吔 */
    private static final int MAX_MESSAGE_NUM = 50;

    /**
     * �R���X�g���N�^�B
     * 
     * @param obj �Ώۂ̃I�u�W�F�N�g�B
     * @param identifier ���ʎq
     */
    public ConcurrentMonitorObject(final Object obj, final String identifier)
    {
        this.ref_ = new WeakReference<Object>(obj);
        this.hashCode_ = identifier.hashCode();
        this.identifier_ = identifier;
        this.itemList_ = new ArrayList<ConcurrentMonitorItem>();
    }

    /**
     * �n�b�V���R�[�h���擾����B
     * @return �n�b�V���R�[�h
     */
    public int getHashCode()
    {
        return this.hashCode_;
    }

    /**
     * �X���b�hID���܂܂�Ă��邩�ǂ������擾����B
     * 
     * @param threadId �X���b�h�h�c�B
     * @return �X���b�hID���܂܂�Ă��邩�ǂ����B
     */
    public boolean containsThreadId(long threadId)
    {
        int result = search(threadId);
        return result >= 0;
    }

    private int search(long threadId)
    {
        synchronized (this.itemList_)
        {
            ConcurrentMonitorItem key = new ConcurrentMonitorItem();
            key.setThreadId(threadId);
            int result = Collections.binarySearch(this.itemList_, key, comparator_);
            return result;
        }
    }

    /**
     * �X���b�h�A�N�Z�X����ۑ�����B
     * 
     * @param threadId �X���b�hID�B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param lockeObjectList ���b�N���Ă���I�u�W�F�N�g�̃��X�g�B
     * 
     */
    public void addTrace(final long threadId, final String stackTrace,
            final List<String> lockeObjectList)
    {
        // �ۑ����郁�b�Z�[�W�̐����ő�l�ɒB���Ă���ꍇ�́A��������B
        if (this.itemList_.size() >= MAX_MESSAGE_NUM)
        {
            return;
        }

        String threadName = Thread.currentThread().getName();
        ConcurrentMonitorItem item = new ConcurrentMonitorItem();
        item.setThreadId(threadId);
        item.setTime(System.currentTimeMillis());
        item.setStackTrace(stackTrace);
        item.setLockedObjectList(lockeObjectList);
        item.setThreadName(threadName);

        synchronized (this.itemList_)
        {
            int threadIndex = search(threadId);
            if (threadIndex < 0)
            {
                this.itemList_.add(-1 - threadIndex, item);
            }
        }

    }

    public List<String> getLockedObjectList()
    {
        return this.lockedObjectList_;
    }

    public void setLockedObjectList(List<String> prevList)
    {
        this.lockedObjectList_ = prevList;
    }

    public String getIdentifier()
    {
        return identifier_;
    }

    public void setIdentifier(String identifier)
    {
        identifier_ = identifier;
    }

    public List<ConcurrentMonitorItem> getItemMap()
    {
        return Collections.unmodifiableList(itemList_);
    }

    public Object getRef()
    {
        return ref_.get();
    }

    public long getPrevThreadId()
    {
        return prevThreadId_;
    }

    public void setPrevThreadId(long prevThreadId)
    {
        prevThreadId_ = prevThreadId;
    }
}
