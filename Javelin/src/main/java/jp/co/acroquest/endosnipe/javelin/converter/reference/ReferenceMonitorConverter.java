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
package jp.co.acroquest.endosnipe.javelin.converter.reference;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.reference.monitor.ReferenceMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.Modifier;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * �Q�Ƃ�ێ����邽�߂̃R���o�[�^
 * 
 * @author eriguchi
 */
public class ReferenceMonitorConverter extends AbstractConverter
{
    private static final String REFERENCE_MONITOR_NAME = ReferenceMonitor.class.getCanonicalName();

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
     * ���\�b�h�̐U�镑�����C������B
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException �R���p�C���ł��Ȃ��ꍇ
     * @throws NotFoundException �N���X��������Ȃ��ꍇ
     */
    private void convertBehavior(CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        String className = getClassName();

        if (Modifier.isStatic(ctBehavior.getModifiers()) == false)
        {
            ctBehavior.insertAfter(REFERENCE_MONITOR_NAME + ".add(\"" + className + "\", this);");
        }
        else
        {
            ctBehavior.insertAfter(REFERENCE_MONITOR_NAME + ".add(\"" + className
                    + ".class\", $class);");
        }

        logModifiedMethod(ReferenceMonitorConverter.class.getCanonicalName(), ctBehavior);

    }
}
