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
package jp.co.acroquest.endosnipe.javelin.converter.exception.monitor;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.ExceptionDetectEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ��O���L���b�`���郂�j�^�N���X�ł��B
 * @author fujii
 *
 */
public class ExceptionMonitor
{
    /**
     * �C���X�^���X����h���v���C�x�[�g�R���X�g���N�^�ł��B
     */
    private ExceptionMonitor()
    {
        // Do Nothing.
    }

    /**
     * �C�x���g���O�ɗ�O���L���b�`�������Ƃ��o�͂��܂��B<br />
     * 
     * @param throwable {@link Throwable}�I�u�W�F�N�g
     * @param obj ��O���o�I�u�W�F�N�g
     */
    public static void postProcess(Throwable throwable, Object obj)
    {
        CommonEvent event = createExceptionEvent(throwable, obj);
        StatsJavelinRecorder.addEvent(event);
    }

    /**
     * ��O�L���b�`���o�̃C�x���g���쐬���܂��B<br />
     * 
     * @param throwable {@link Throwable}�I�u�W�F�N�g
     * @param obj ��O���o�I�u�W�F�N�g
     * @return {@link CommonEvent}�I�u�W�F�N�g
     */
    private static CommonEvent createExceptionEvent(Throwable throwable, Object obj)
    {
        CommonEvent event = new ExceptionDetectEvent();

        String identifier = StatsUtil.createIdentifier(obj);
        event.addParam(EventConstants.EXCEPTION_IDENTIFIER, identifier);

        String stackTrace = ThreadUtil.getStackTrace(throwable.getStackTrace());
        event.addParam(EventConstants.EXCEPTION_STACKTRACE, stackTrace);

        return event;
    }
}
