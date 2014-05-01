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
package jp.co.acroquest.endosnipe.javelin.converter.leak;

import java.io.IOException;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.InstanceMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtConstructor;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;
import jp.co.smg.endosnipe.javassist.expr.ConstructorCall;
import jp.co.smg.endosnipe.javassist.expr.ExprEditor;

/**
 * �C���X�^���X�����Ď����邽�߂̃R���o�[�^
 * 
 * @author eriguchi
 */
public class InstanceMonitorConverter extends AbstractConverter
{
    private static final String INSTANCE_MONITOR_NAME = InstanceMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // �������Ȃ��B
    }

    /**
     * {@inheritDoc}
     */
    public void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException
    {
        CtClass ctClass = getCtClass();

        CtConstructor[] behaviorList = ctClass.getConstructors();
        for (CtConstructor ctBehavior : behaviorList)
        {
            convertConstructor(ctBehavior);
        }

        try
        {
            CtMethod finalizeMethod = ctClass.getDeclaredMethod("finalize");
            convertFinalizeMethod(finalizeMethod);
        }
        catch (NotFoundException nfe)
        {
            SystemLogger.getInstance().debug(nfe);
            String callRemoveSrc = createCallRemoveSrc();
            ctClass.addMethod(CtMethod.make("protected void finalize(){" + callRemoveSrc + "}",
                                            ctClass));
        }

        setNewClassfileBuffer(getCtClass().toBytecode());
    }

    private String createCallRemoveSrc()
    {
        String className = getClassName();
        String callRemoveSrc = INSTANCE_MONITOR_NAME + ".remove(\"" + className + "\");";
        return callRemoveSrc;
    }

    /**
     * ���\�b�h�̐U�镑�����C������B
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException �R���p�C���ł��Ȃ��ꍇ
     * @throws NotFoundException �N���X��������Ȃ��ꍇ
     */
    private void convertConstructor(CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        JudgeConstructorCall editor = new JudgeConstructorCall();
        ctBehavior.instrument(editor);

        if (editor.hasConstructorCall_ == true)
        {
            return;
        }

        String className = getClassName();
        ctBehavior.insertAfter(INSTANCE_MONITOR_NAME + ".add(\"" + className + "\");");
        logModifiedMethod(InstanceMonitorConverter.class.getCanonicalName(), ctBehavior);
    }
    
    /**
     * �R���X�g���N�^�Ăяo�������邩�ǂ������`�F�b�N���邽�߂�ExprEditor
     * 
     * @author eriguchi
     */
    private static class JudgeConstructorCall extends ExprEditor
    {
        public boolean hasConstructorCall_ = false;

        public void edit(ConstructorCall c)
            throws CannotCompileException
        {
            String constructorClassName = c.getClassName();
            String targetClassName = c.getEnclosingClass().getName();
            if(constructorClassName.equals(targetClassName))
            {
                hasConstructorCall_ = true;
            }
        }
    }

    /**
     * ���\�b�h�̐U�镑�����C������B
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException �R���p�C���ł��Ȃ��ꍇ
     * @throws NotFoundException �N���X��������Ȃ��ꍇ
     */
    private void convertFinalizeMethod(CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        String callRemoveSrc = createCallRemoveSrc();
        ctBehavior.insertAfter(callRemoveSrc);
        logModifiedMethod(InstanceMonitorConverter.class.getCanonicalName(), ctBehavior);
    }
}
