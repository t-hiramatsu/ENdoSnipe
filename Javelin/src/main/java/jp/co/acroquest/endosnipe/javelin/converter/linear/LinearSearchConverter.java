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
package jp.co.acroquest.endosnipe.javelin.converter.linear;

import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.linear.monitor.LinearSearchMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.Modifier;
import jp.co.smg.endosnipe.javassist.NotFoundException;
import jp.co.smg.endosnipe.javassist.expr.ExprEditor;
import jp.co.smg.endosnipe.javassist.expr.MethodCall;

/**
 * 線形検索を検出するコンバータです。<br />
 * 
 * @author fujii
 *
 */
public class LinearSearchConverter extends AbstractConverter
{
    /** ファイル入力モニタのクラス名称 */
    private static final String LINEARSEARCH_MONITOR_NAME =
            LinearSearchMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException
    {
        CtClass ctClass = getCtClass();

        // Abstractオブジェクトに対しては、設定を行わない。
        int modifiers = ctClass.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers))
        {
            return;
        }

        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods)
        {
            String methodName = method.getName();
            String signature = method.getSignature();
            if ("get".equals(methodName))
            {
                if ("(I)Ljava/lang/Object;".equals(signature))
                {
                    modifyMethod(method);
                    logModifiedMethod("LinearSearchConverter", method);
                }
            }
            else if ("indexOf".equals(methodName))
            {
                JudgeIndexOfCall editor = new JudgeIndexOfCall();
                method.instrument(editor);
                
                if (editor.hasIndexOfCall_ == false)
                {
                    modifyMethodIndexOf(method);
                    logModifiedMethod("LinearSearchConverter", method);
                }
            }
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

    private void modifyMethod(CtMethod method)
        throws CannotCompileException
    {
        method.insertBefore(LINEARSEARCH_MONITOR_NAME + ".preProcess(this);");
        method.insertAfter(LINEARSEARCH_MONITOR_NAME + ".postProcess(this, $1);");
    }

    private void modifyMethodIndexOf(CtMethod method)
        throws CannotCompileException, NotFoundException
    {
        method.insertBefore(LINEARSEARCH_MONITOR_NAME + ".preProcess(this);");
        method.insertAfter(LINEARSEARCH_MONITOR_NAME
                + ".postProcessIndexOf(this, $1, $_);");
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // Do Nothing.

    }

    /**
     * indexOf呼び出しがあるかどうかをチェックするためのExprEditor
     * 
     * @author eriguchi
     */
    private static class JudgeIndexOfCall extends ExprEditor
    {
        public boolean hasIndexOfCall_ = false;

        public void edit(MethodCall methodCall)
            throws CannotCompileException
        {
            if ("indexOf".equals(methodCall.getMethodName()) == false)
            {
                return;
            }

            String methodClassName = methodCall.getClassName();
            String targetClassName = methodCall.getEnclosingClass().getName();
            if (methodClassName.equals(targetClassName))
            {
                hasIndexOfCall_ = true;
            }
        }
    }
}
