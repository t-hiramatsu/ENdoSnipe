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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * ReentrantLock��p���āA�Ώۃ��\�b�h�̎��s��r������N���X�ł��B
 * 
 * @author eriguchi
 */
public class SynchronizeMonitor
{
    /** Lock�I�u�W�F�N�g�̃}�b�v�B */
    private static Map<Object, Lock> lockMap__ =
            Collections.synchronizedMap(new WeakHashMap<Object, Lock>());

    /**
     * �C���X�^���X����h�~���邽�߂̃R���X�g���N�^�ł��B
     */
    private SynchronizeMonitor()
    {
        // �������Ȃ��B
    }

    /**
     * Lock�I�u�W�F�N�g���擾���A���b�N����B
     * 
     * @param obj �Ώۂ̃I�u�W�F�N�g�B
     */
    public static void preProcess(Object obj)
    {
        if (obj == lockMap__)
        {
            return;
        }

        try
        {
            Lock lock = getLock(obj);
            lock.lock();
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * Lock�I�u�W�F�N�g���擾���A���b�N����������B
     * 
     * @param obj �Ώۂ̃I�u�W�F�N�g�B
     */
    public static void postProcess(Object obj)
    {
        if (obj == lockMap__)
        {
            return;
        }

        try
        {
            Lock lock = getLock(obj);
            lock.unlock();
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * Lock�I�u�W�F�N�g���擾����B
     * 
     * @param obj �ΏۃI�u�W�F�N�g�B
     * @return Lock�I�u�W�F�N�g�B
     */
    private static Lock getLock(Object obj)
    {
        Lock lock;
        synchronized (lockMap__)
        {
            lock = lockMap__.get(obj);
            if (lock == null)
            {
                lock = new ReentrantLock();
                lockMap__.put(obj, lock);
            }
        }
        return lock;
    }

}
