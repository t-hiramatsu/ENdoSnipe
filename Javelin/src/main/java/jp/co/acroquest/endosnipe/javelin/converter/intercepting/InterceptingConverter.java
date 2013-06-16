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
package jp.co.acroquest.endosnipe.javelin.converter.intercepting;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * InterCeptor用コンバータ
 * @author yamasaki
 */
public abstract class InterceptingConverter extends AbstractConverter implements MethodInterceptor
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
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
     * メソッドの振る舞いを変更する。
     * @param ctBehavior メソッド
     */
    private void convertBehavior(final CtBehavior ctBehavior)
    {
        // Do Nothing.
    }

    /**
     * 未定
     * @param invocation Invocation
     * @throws Throwable 例外
     * @return Object
     */
    public abstract Object invoke(MethodInvocation invocation)
        throws Throwable;
}
