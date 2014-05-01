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

import java.util.List;

/**
 * 1��1��̃I�u�W�F�N�g�ւ̃A�N�Z�X�����L�^����B
 * @author eriguchi
 *
 */
public class ConcurrentMonitorItem
{
    /** �X���b�hID */
    private long threadId_;

    /** �X���b�h�� */
    private String threadName_;

    /** �X�^�b�N�g���[�X */
    private String stackTrace_;

    /** ���b�N���Ă���I�u�W�F�N�g�̃��X�g�B */
    private List<String> lockedObjectList_;

    private long time_;

    public long getThreadId()
    {
        return threadId_;
    }

    public void setThreadId(long threadId)
    {
        threadId_ = threadId;
    }

    public String getStackTrace()
    {
        return stackTrace_;
    }

    public void setStackTrace(String stackTrace)
    {
        stackTrace_ = stackTrace;
    }

    public List<String> getLockedObjectList()
    {
        return this.lockedObjectList_;
    }

    public void setLockedObjectList(List<String> prevList)
    {
        this.lockedObjectList_ = prevList;
    }

    public String getThreadName()
    {
        return threadName_;
    }

    public void setThreadName(String threadName)
    {
        threadName_ = threadName;
    }

    public long getTime()
    {
        return time_;
    }

    public void setTime(long time)
    {
        time_ = time;
    }

}
