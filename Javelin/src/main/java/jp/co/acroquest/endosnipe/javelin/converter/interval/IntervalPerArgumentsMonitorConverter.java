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
package jp.co.acroquest.endosnipe.javelin.converter.interval;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javassist.CannotCompileException;
import jp.co.acroquest.endosnipe.javassist.CtBehavior;
import jp.co.acroquest.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.javassist.NotFoundException;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.interval.monitor.IntervalPerArgumentsMonitor;

/**
 * 呼び出し間隔チェックコンバータ。<br />
 * IntervalMonitor とは異なり、同名メソッドの中でも、
 * メソッド呼び出し時の引数が等しい場合にカウントアップします。<br />
 * 
 * @author sakamoto
 */
public class IntervalPerArgumentsMonitorConverter extends AbstractConverter
{
    private static final String MONITOR_NAME = IntervalPerArgumentsMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException
    {
        List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : behaviorList)
        {
            convertBehavior(ctBehavior);
        }

        setNewClassfileBuffer(getCtClass().toBytecode());
    }

    /**
     * メソッドの振る舞いを修正する。
     * 呼び出し時間を記録し、前回との呼び出し時間が閾値以上であれば、
     * 強制的にアラームを出力し、問題を通知する。
     * 
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException コンパイルできない場合
     * @throws NotFoundException クラスが見つからない場合
     */
    private void convertBehavior(final CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        String className = getClassName();
        String methodName = ctBehavior.getName();

        ctBehavior.insertBefore(MONITOR_NAME + ".preProcess(\"" + className + "\",\"" + methodName
                + "\", $args);");
        ctBehavior.insertAfter(MONITOR_NAME + ".postProcess();");

        CtClass throwable = getClassPool().get(Throwable.class.getName());
        ctBehavior.addCatch(MONITOR_NAME + ".postProcessException();throw $e;", throwable);

        logModifiedMethod("IntervalPerArgumentsMonitorConverter", ctBehavior);
    }
}
