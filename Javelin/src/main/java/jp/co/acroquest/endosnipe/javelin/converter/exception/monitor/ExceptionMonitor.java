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
 * 例外をキャッチするモニタクラスです。
 * @author fujii
 *
 */
public class ExceptionMonitor
{
    /**
     * インスタンス化を防ぐプライベートコンストラクタです。
     */
    private ExceptionMonitor()
    {
        // Do Nothing.
    }

    /**
     * イベントログに例外をキャッチしたことを出力します。<br />
     * 
     * @param throwable {@link Throwable}オブジェクト
     * @param obj 例外検出オブジェクト
     */
    public static void postProcess(Throwable throwable, Object obj)
    {
        CommonEvent event = createExceptionEvent(throwable, obj);
        StatsJavelinRecorder.addEvent(event);
    }

    /**
     * 例外キャッチ検出のイベントを作成します。<br />
     * 
     * @param throwable {@link Throwable}オブジェクト
     * @param obj 例外検出オブジェクト
     * @return {@link CommonEvent}オブジェクト
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
