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
package jp.co.acroquest.endosnipe.javelin.converter.ejb;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;

/**
 * EJBコンバータで使用するモニタクラス
 * 
 * @author S.Kimura
 */
public class EjbSessionMonitor
{
    /**
     * インスタンス化防止のためのコンストラクタ
     */
    private EjbSessionMonitor()
    {
        // Do Nothing.
    }

    /** Javelinの設定ファイル */
    private static JavelinConfig config__ = new JavelinConfig();

    /**
     * 前処理。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param args 引数
     */
    public static void preProcess(String className, String methodName, final Object[] args)
    {
        try
        {
            StatsJavelinRecorder.preProcess(className, methodName, args, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * 後処理（本処理成功時）。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param retValue 戻り値。
     */
    public static void postProcessOK(String className, String methodName, final Object retValue)
    {
        try
        {
            StatsJavelinRecorder.postProcess(className, methodName, retValue, config__);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * 後処理（本処理失敗時）。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param cause 例外の原因
     */
    public static void postProcessNG(String className, String methodName, final Throwable cause)
    {
        try
        {
            StatsJavelinRecorder.postProcess(className, methodName, cause, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }
}
