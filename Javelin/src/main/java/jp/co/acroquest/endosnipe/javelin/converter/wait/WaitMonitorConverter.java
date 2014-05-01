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
package jp.co.acroquest.endosnipe.javelin.converter.wait;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.wait.monitor.WaitMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;
import jp.co.smg.endosnipe.javassist.expr.ExprEditor;
import jp.co.smg.endosnipe.javassist.expr.MethodCall;

/**
 * Object#wait�AThread.sleep�̎��s���Ԃ��Ď����A
 * <ul>
 * <li>�X���b�h�_���v�܂��͎��s�ӏ�</li>
 * <li>���s����</li>
 * </ul>
 * ���擾����B
 * 
 * @author eriguchi
 */
public class WaitMonitorConverter extends AbstractConverter
{
    private static final String OBJECT_CLASS_NAME = Object.class.getCanonicalName();

    private static final String THREAD_CLASS_NAME = Thread.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // �������Ȃ��B
    }

    /**
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
            behavior.instrument(new ExprEditor() {
                @Override
                public void edit(final MethodCall methodCall)
                    throws CannotCompileException
                {
                    String monitorCode =
                            WaitMonitor.class.getCanonicalName() + ".preProcess();"
                                    + "$_ = $proceed($$);" + WaitMonitor.class.getCanonicalName()
                                    + ".postProcess();";
                    try
                    {
                        CtMethod method = methodCall.getMethod();
                        String signature = method.getSignature();
                        CtClass declaringClass = method.getDeclaringClass();

                        if (THREAD_CLASS_NAME.equals(declaringClass.getName())
                                && "sleep".equals(method.getName()) && "(J)V".equals(signature))
                        {
                            methodCall.replace(monitorCode);
                        }

                        if (OBJECT_CLASS_NAME.equals(declaringClass.getName())
                                && "wait".equals(method.getName()) && "(J)V".equals(signature))
                        {
                            methodCall.replace(monitorCode);
                        }
                    }
                    catch (NullPointerException e)
                    {
                        SystemLogger.getInstance().debug(e);
                    }
                    catch (NotFoundException e)
                    {
                        SystemLogger.getInstance().debug(e);
                    }
                }
            });

            logModifiedMethod("WaitMonitorConverter", behavior);
        }

        setNewClassfileBuffer(targetClass.toBytecode());
    }
}
