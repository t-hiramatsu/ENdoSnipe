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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.util.List;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.Messages;
import jp.co.acroquest.endosnipe.perfdoctor.PerfConstants;

/**
 * �P��� {@link JavelinLogElement} ���`�F�b�N���邽�߂̃V���v���ȃ��[���̂��߂̊��N���X�B
 *  
 * @author y-komori
 */
public abstract class SingleElementRule extends AbstractRule
{
    /**
     * @see jp.co.acroquest.endosnipe.perfdoctor.rule.AbstractRule#doJudge(java.util.List)
     * 
     * JavelinLogElementList�̗v�f��������肷��B
     * @param javelinLogElementList JavelinLogElement�̃��X�g
     */
    @Override
    public void doJudge(final List<JavelinLogElement> javelinLogElementList)
    {
        for (JavelinLogElement element : javelinLogElementList)
        {
            try
            {
                doJudgeElement(element);
            }
            catch (RuntimeException exception)
            {
                log(Messages.getMessage(PerfConstants.PERF_DOCTOR_RUNTIME_EXCEPTION), element,
                    exception);
            }

        }

        doJudgeEnd();
    }

    /**
     * �P��� {@link JavelinLogElement} �ɂ��Ĕ�����s���B
     * 
     * @param element {@link JavelinLogElement} �I�u�W�F�N�g
     */
    protected abstract void doJudgeElement(JavelinLogElement element);

    /**
     * �S�Ă̂� {@link JavelinLogElement} �ɂ��Ĕ�����s������ɌĂяo�����B
     * �Ō�ɍs�������������L�q����B
     */
    protected void doJudgeEnd()
    {
        // Do Nothing
    }
}
