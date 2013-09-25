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
package jp.co.acroquest.endosnipe.javelin.converter.net;

import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.net.monitor.InputStreamMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.net.monitor.OutputStreamMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * NetWorkの入出力量を監視するためのコンバータ
 * 
 * @author yamasaki
 */
public class NetMonitorConverter extends AbstractConverter
{

    /** ネットワーク入力モニタのクラス名称 */
    private static final String INPUSTREAM_MONITOR_NAME =
            InputStreamMonitor.class.getCanonicalName();

    /** ネットワーク出力モニタのクラス名称 */
    private static final String OUTPUSTREAM_MONITOR_NAME =
            OutputStreamMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // Do Nothing.
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
        CtClass ctClass = getCtClass();

        CtMethod inputMethod = ctClass.getDeclaredMethod("getInputStream");
        convertGetInputStreamMethod(inputMethod);

        CtMethod outputMethod = ctClass.getDeclaredMethod("getOutputStream");
        convertGetOutputStreamMethod(outputMethod);

        setNewClassfileBuffer(ctClass.toBytecode());

    }

    /**
     * getInputStreamメソッドにコード埋め込みを行う。
     * 
     * @param ctMethod CtMethod
     * @throws CannotCompileException コンパイルが出来ない場合
     */
    private void convertGetInputStreamMethod(final CtMethod ctMethod)
        throws CannotCompileException
    {
        ctMethod.insertAfter("return new " + INPUSTREAM_MONITOR_NAME + "($_);");
        // 処理結果をログに出力する。
        logModifiedMethod("NetMonitorConverter", ctMethod);

    }

    /**
     * getOutputStreamメソッドにコード埋め込みを行う。
     * 
     * @param ctMethod CtMethod
     * @throws CannotCompileException コンパイルが出来ない場合
     */
    private void convertGetOutputStreamMethod(final CtMethod ctMethod)
        throws CannotCompileException
    {
        ctMethod.insertAfter("return new " + OUTPUSTREAM_MONITOR_NAME + "($_);");
        // 処理結果をログに出力する。
        logModifiedMethod("NetMonitorConverter", ctMethod);
    }
}
