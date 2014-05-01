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
package jp.co.acroquest.endosnipe.javelin.converter.thread;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.thread.monitor.ThreadLeakMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * ThreadLeakMonitor���Ăяo���R�[�h�𖄂ߍ��ރR���o�[�^�B
 * 
 * @author eriguchi
 */
public class ThreadLeakMonitorConverter extends AbstractConverter
{
    /** ���j�^�N���X���B */
    private static final String MONITOR_CLASSNAME = ThreadLeakMonitor.class.getCanonicalName();

    /** Throwable��CtClass�B */
    private CtClass throwableClass_;

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        try
        {
            throwableClass_ = ClassPool.getDefault().get(Throwable.class.getCanonicalName());
        }
        catch (NotFoundException nfe)
        {
            // �������Ȃ��B
            SystemLogger.getInstance().warn(nfe);
        }
    }

    /**
     * start���\�b�h�̍Ō��ThreadLeakMonitor.postThreadStart��ǉ�����B
     * run���\�b�h�̍Ō��ThreadLeakMonitor.postThreadRun��ǉ�����B
     * 
     * @throws CannotCompileException �R�[�h�̖��ߍ��݂Ɏ��s�����ꍇ�B
     * @throws IOException �ϊ����CtClass�̃o�C�g�R�[�h�ւ̕ϊ��Ɏ��s�����ꍇ�B 
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            IOException
    {
        CtClass targetClass = getCtClass();

        List<CtBehavior> matcheDeclaredBehavior = getMatcheDeclaredBehavior();
        for (CtBehavior behavior : matcheDeclaredBehavior)
        {
            if ("start".equals(behavior.getName()))
            {
                behavior.insertAfter(MONITOR_CLASSNAME + ".postThreadStart(this);");
                logModifiedMethod("ThreadLeakMonitorConverter", behavior);
            }
            else if ("run".equals(behavior.getName()))
            {
                behavior.insertAfter(MONITOR_CLASSNAME + ".postThreadRun();");
                behavior.addCatch(MONITOR_CLASSNAME + ".postThreadRunNG($e);throw $e;",
                                  throwableClass_);
                logModifiedMethod("ThreadLeakMonitorConverter", behavior);
            }
        }

        setNewClassfileBuffer(targetClass.toBytecode());
    }
}
