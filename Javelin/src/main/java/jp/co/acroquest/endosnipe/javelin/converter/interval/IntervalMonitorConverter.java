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

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.interval.monitor.IntervalMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * �Ăяo���Ԋu�`�F�b�N�R���o�[�^�B<br />
 * IntervalMonitor��p�����Ăяo���Ԋu�̃`�F�b�N�������A�w�肳�ꂽ���\�b�h�ɖ��ߍ��ށB<br />
 * 
 * @author yamasaki
 */
public class IntervalMonitorConverter extends AbstractConverter
{
    private static final String MONITOR_NAME = IntervalMonitor.class.getCanonicalName();

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
     * ���\�b�h�̐U�镑�����C������B
     * �Ăяo�����Ԃ��L�^���A�O��Ƃ̌Ăяo�����Ԃ�臒l�ȏ�ł���΁A
     * �����I�ɃA���[�����o�͂��A����ʒm����B
     * 
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException �R���p�C���ł��Ȃ��ꍇ
     * @throws NotFoundException �N���X��������Ȃ��ꍇ
     */
    private void convertBehavior(final CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        String className = getClassName();
        String methodName = ctBehavior.getName();

        ctBehavior.insertBefore(MONITOR_NAME + ".preProcess(\"" + className + "\",\"" + methodName
                + "\");");
        ctBehavior.insertAfter(MONITOR_NAME + ".postProcess();");

        CtClass throwable = getClassPool().get(Throwable.class.getName());
        ctBehavior.addCatch(MONITOR_NAME + ".postProcessException();throw $e;", throwable);

        logModifiedMethod("IntervalMonitorConverter", ctBehavior);
    }
}
