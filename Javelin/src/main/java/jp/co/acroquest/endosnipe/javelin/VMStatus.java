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

/**
 * VM�̏�Ԃ�\���N���X
 * @author eriguchi
 */
public class VMStatus
{
    /** ��̃X�e�[�^�X */
    public static final VMStatus EMPTY_STATUS = new VMStatus();
    
    /** CPU���� */
    private long cpuTime_;

    /** ���[�U���� */
    private long userTime_;

    /** �u���b�N���� */
    private long blockedTime_;

    /** wait���� */
    private long waitedTime_;

    /** GC���s���� */
    private long collectionTime_;

    /** �u���b�N�� */
    private long blockedCount_;

    /** wait�� */
    private long waitedCount_;

    /** GC�� */
    private long collectionCount_;

    /**
     * EMPTY_STATUS�����p�R���X�g���N�^�B
     */
    public VMStatus()
    {
        
    }
    
    /**
     * �R���X�g���N�^�B
     * 
     * @param cpuTime CPU����
     * @param userTime ���[�U����
     * @param blockedCount �u���b�N��
     * @param blockedTime �u���b�N����
     * @param waitedCount �E�F�C�g��
     * @param waitedTime �E�F�C�g����
     * @param collectionCount GC���s��
     * @param collectionTime GC���s����
     */
    public VMStatus(
            long cpuTime, 
            long userTime, 
            long blockedCount, 
            long blockedTime, 
            long waitedCount, 
            long waitedTime, 
            long collectionCount,
            long collectionTime)
    {
        this.cpuTime_         = cpuTime;
        this.userTime_        = userTime;
        this.blockedCount_    = blockedCount;
        this.blockedTime_     = blockedTime;
        this.waitedCount_     = waitedCount;
        this.waitedTime_      = waitedTime;
        this.collectionCount_ = collectionCount;
        this.collectionTime_  = collectionTime;
    }
    
    /**
     * �u���b�N���Ԃ��擾����B
     * @return �u���b�N����
     */
    public long getBlockedTime()
    {
        return this.blockedTime_;
    }

    /**
     * �u���b�N���Ԃ�ݒ肷��B
     * @param blockedTime �u���b�N����
     */
//    public void setBlockedTime(final long blockedTime)
//    {
//        this.blockedTime_ = blockedTime;
//    }

    /**
     * CPU���Ԃ��擾����B
     * @return CPU����
     */
    public long getCpuTime()
    {
        return this.cpuTime_;
    }

    /**
     * CPU���Ԃ�ݒ肷��B
     * @param cpuTime CPU����
     */
//    public void setCpuTime(final long cpuTime)
//    {
//        this.cpuTime_ = cpuTime;
//    }

    /**
     * ���[�U���Ԃ��擾����B
     * @return ���[�U����
     */
    public long getUserTime()
    {
        return this.userTime_;
    }

    /**
     * ���[�U���Ԃ�ݒ肷��B
     * @param userTime ���[�U����
     */
//    public void setUserTime(final long userTime)
//    {
//        this.userTime_ = userTime;
//    }

    /**
     * wait���Ԃ��擾����B
     * @return wait����
     */
    public long getWaitedTime()
    {
        return this.waitedTime_;
    }

    /**
     * wait���Ԃ�ݒ肷��B
     * @param waitedTime wait����
     */
//    public void setWaitedTime(final long waitedTime)
//    {
//        this.waitedTime_ = waitedTime;
//    }

    /**
     * GC���s���Ԃ��擾����B
     * @return GC���s����
     */
    public long getCollectionTime()
    {
        return this.collectionTime_;
    }

    /**
     * GC���s���Ԃ�ݒ肷��B
     * @param collectionTime GC���s����
     */
//    public void setCollectionTime(final long collectionTime)
//    {
//        this.collectionTime_ = collectionTime;
//    }

    /**
     * �u���b�N�񐔂��擾����B
     * @return �u���b�N��
     */
    public long getBlockedCount()
    {
        return this.blockedCount_;
    }

    /**
     * �u���b�N�񐔂�ݒ肷��B
     * @param blockedCount �u���b�N��
     */
//    public void setBlockedCount(final long blockedCount)
//    {
//        this.blockedCount_ = blockedCount;
//    }

    /**
     * wait�񐔂��擾����B
     * @return wait��
     */
    public long getWaitedCount()
    {
        return this.waitedCount_;
    }

    /**
     * wait�񐔂�ݒ肷��B
     * @param waitedCount wait��
     */
//    public void setWaitedCount(final long waitedCount)
//    {
//        this.waitedCount_ = waitedCount;
//    }

    /**
     * GC�񐔂��擾����B
     * @return GC��
     */
    public long getCollectionCount()
    {
        return this.collectionCount_;
    }

    /**
     * GC�񐔂�ݒ肷��B
     * @param collectionCount GC��
     */
//    public void setCollectionCount(final long collectionCount)
//    {
//        this.collectionCount_ = collectionCount;
//    }
}
