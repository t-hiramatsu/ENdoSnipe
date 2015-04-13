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
import jp.co.acroquest.endosnipe.javassist.CannotCompileException;
import jp.co.acroquest.endosnipe.javassist.ClassPool;
import jp.co.acroquest.endosnipe.javassist.CtBehavior;
import jp.co.acroquest.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.javassist.NotFoundException;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.thread.monitor.ThreadLeakMonitor;

/**
 * ThreadLeakMonitorを呼び出すコードを埋め込むコンバータ。
 * 
 * @author eriguchi
 */
public class ThreadLeakMonitorConverter extends AbstractConverter
{
    /** モニタクラス名。 */
    private static final String MONITOR_CLASSNAME = ThreadLeakMonitor.class.getCanonicalName();

    /** ThrowableのCtClass。 */
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
            // 発生しない。
            SystemLogger.getInstance().warn(nfe);
        }
    }

    /**
     * startメソッドの最後にThreadLeakMonitor.postThreadStartを追加する。
     * runメソッドの最後にThreadLeakMonitor.postThreadRunを追加する。
     * 
     * @throws CannotCompileException コードの埋め込みに失敗した場合。
     * @throws IOException 変換後のCtClassのバイトコードへの変換に失敗した場合。 
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
