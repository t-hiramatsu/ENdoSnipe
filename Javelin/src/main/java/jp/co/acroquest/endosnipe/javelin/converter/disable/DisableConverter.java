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
package jp.co.acroquest.endosnipe.javelin.converter.disable;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.disable.monitor.DisableMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * Monitor��1���I�Ȗ��������s��Monitor��g�ݍ��ރR���o�[�^
 * 
 * @author eriguchi
 *
 */
public class DisableConverter extends AbstractConverter
{
    /** ���j�^�N���X���B */
    private static final String MONITOR_CLASSNAME =
        DisableMonitor.class.getCanonicalName();

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

        List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : behaviorList)
        {
            convertMethod(ctBehavior);
            // �������ʂ����O�ɏo�͂���B
            logModifiedMethod("DisableConverter", ctBehavior);
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

    /**
     * ���\�b�h��ύX���A�����X���b�h�ɂ�铯���A�N�Z�X�𔻒肷��R�[�h�𖄂ߍ��ށB
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C���ł��Ȃ��ꍇ
     */
    private void convertMethod(CtBehavior method)
        throws CannotCompileException
    {
        // �����������𖄂ߍ��ށB
        method.insertBefore(MONITOR_CLASSNAME + ".preProcess();");
        method.insertAfter(MONITOR_CLASSNAME + ".postProcess();", true);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // Do Nothing.
    }
}
