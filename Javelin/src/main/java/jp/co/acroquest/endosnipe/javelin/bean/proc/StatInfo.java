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
package jp.co.acroquest.endosnipe.javelin.bean.proc;

/**
 *�@/proc/stat�̓��e��ێ�����Bean
 * 
 * @author eriguchi
 */
public class StatInfo
{
    /** CPU���[�U */
    private long   cpuUser_;

    /** CPU�V�X�e�� */
    private long   cpuSystem_;

    /** CPU�^�X�N */
    private long   cpuTask_;

    /** CPU�z�� */
    private long[] cpuArray_;

    /** �y�[�W�C������ */
    private long   pageIn_;

    /** �y�[�W�A�E�g����*/
    private long   pageOut_;

    /** FD�J�E���g */
    private long   fdCount_;

    /** CPU�@I/O�ҋ@���� */
    private long   cpuIoWait_;

    /**
     * CPU���[�U���擾���܂��B
     * @return CPU���[�U
     */
    public long getCpuUser()
    {
        return cpuUser_;
    }

    /**
     * CPU���[�U��ݒ肵�܂��B
     * @param cpuUser CPU���[�U
     */
    public void setCpuUser(long cpuUser)
    {
        cpuUser_ = cpuUser;
    }

    /**
     * CPU�V�X�e�����擾���܂��B
     * @return CPU�V�X�e��
     */
    public long getCpuSystem()
    {
        return cpuSystem_;
    }

    /**
     * CPU�V�X�e����ݒ肵�܂��B
     * @param cpuSystem CPU�V�X�e��
     */
    public void setCpuSystem(long cpuSystem)
    {
        cpuSystem_ = cpuSystem;
    }

    /**
     * CPU�^�X�N���擾���܂��B
     * @return CPU�^�X�N
     */
    public long getCpuTask()
    {
        return cpuTask_;
    }

    /**
     * CPU�^�X�N��ݒ肵�܂��B
     * @param cpuTask CPU�^�X�N
     */
    public void setCpuTask(long cpuTask)
    {
        cpuTask_ = cpuTask;
    }

    /**
     * CPU�z����擾���܂��B
     * @return CPU�z��
     */
    public long[] getCpuArray()
    {
        return cpuArray_;
    }

    /**
     * CPU�z���ݒ肵�܂��B
     * @param cpuArray CPU�z��
     */
    public void setCpuArray(long[] cpuArray)
    {
        cpuArray_ = cpuArray;
    }

    /**
     * �y�[�W�C�����Ԃ��擾���܂��B
     * @return �y�[�W�C������
     */
    public long getPageIn()
    {
        return pageIn_;
    }

    /**
     * �y�[�W�C�����Ԃ�ݒ肵�܂��B
     * @param pageIn �y�[�W�C��
     */
    public void setPageIn(long pageIn)
    {
        pageIn_ = pageIn;
    }

    /**
     * �y�[�W�A�E�g���Ԃ��擾���܂��B
     * @return �y�[�W�A�E�g����
     */
    public long getPageOut()
    {
        return pageOut_;
    }

    /**
     * �y�[�W�A�E�g���Ԃ�ݒ肵�܂��B
     * @param pageOut �y�[�W�A�E�g����
     */
    public void setPageOut(long pageOut)
    {
        pageOut_ = pageOut;
    }

    /**
     * FD�J�E���g���擾���܂��B
     * @return FD�J�E���g
     */
    public long getFdCount()
    {
        return fdCount_;
    }

    /**
     * FD�J�E���g��ݒ肵�܂��B
     * @param fdCount FD�J�E���g
     */
    public void setFdCount(long fdCount)
    {
        fdCount_ = fdCount;
    }

    /**
     * CPU�@I/O�ҋ@���Ԃ�ݒ肵�܂��B
     * @param cpuIoWait CPU I/O�ҋ@����
     */
    public void setCpuIoWait(long cpuIoWait)
    {
        this.cpuIoWait_ = cpuIoWait;
    }

    /**
     * CPU�@I/O�ҋ@���Ԃ��擾���܂��B
     * @return CPU�@I/O�ҋ@����
     */
    public long getCpuIoWait()
    {
        return cpuIoWait_;
    }
}
