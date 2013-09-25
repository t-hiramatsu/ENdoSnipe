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
package jp.co.acroquest.endosnipe.javelin.converter.file;

import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.file.monitor.FileInputStreamMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.file.monitor.FileOutputStreamMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;

/**
 * ファイルの入出力データを送信するためのコンバータ
 * 
 * @author yamasaki
 */
public class FileMonitorConverter extends AbstractConverter
{
    /** ファイル入力モニタのクラス名称 */
    private static final String INPUSTREAM_MONITOR_NAME =
            FileInputStreamMonitor.class.getCanonicalName();

    /** ファイル出力モニタのクラス名称 */
    private static final String OUTPUSTREAM_MONITOR_NAME =
            FileOutputStreamMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // 何もしない。
    }

    /**
     * java.io.FileInputStream#read と java.io.FileOutputStream#write に
     * ファイル入出力量取得コードを埋め込む。
     * 
     * @throws CannotCompileException コンパイル時例外
     * @throws IOException ファイル入出力時の例外
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            IOException
    {
        CtClass ctClass = getCtClass();

        if ("java.io.FileInputStream".equals(getClassName()))
        {
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods)
            {
                if (judgeModifyReadMethod(method))
                {
                    modifyReadMethod(method);
                    logModifiedMethod("FileMonitorConverter", method);

                }
            }
        }
        else if ("java.io.FileOutputStream".equals(getClassName()))
        {
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods)
            {
                judgeModifyWriteMethod(method);
            }
        }
        setNewClassfileBuffer(ctClass.toBytecode());
    }

    /**
     * java.io.FileInputStream に修正を加えるかどうか判定する。
     * 
     * @param method メソッド
     * @return true:メソッドを修正する、false:メソッドを修正しない
     */
    private boolean judgeModifyReadMethod(final CtMethod method)
    {
        // read(byte[]), read(byte[], int, int) のときに、modifyWriteMethod()を呼び出す
        String methodName = method.getName();
        boolean judge = false;
        if ("read".equals(methodName))
        {
            if ("([B)I".equals(method.getSignature()) || "([BII)I".equals(method.getSignature()))
            {
                judge = true;
            }
        }
        return judge;
    }

    /**
     * java.io.FileOutputStream に修正を加えるかどうか判定する。
     * 
     * @param method メソッド
     * @throws CannotCompileException コンパイル時の例外
     */
    private void judgeModifyWriteMethod(final CtMethod method)
        throws CannotCompileException
    {
        // write(byte[]) のときには、modifyWriteMethod()を呼び出し、
        // write(byte[], int, int) のときには、modifyWriteMethod2()を呼び出す
        String methodName = method.getName();
        if ("write".equals(methodName))
        {
            if ("([B)V".equals(method.getSignature()))
            {
                modifyWriteMethod(method);
                logModifiedMethod("FileMonitorConverter", method);
            }
            else if ("([BII)V".equals(method.getSignature()))
            {
                modifyWriteMethod2(method);
                logModifiedMethod("FileMonitorConverter", method);
            }
        }
    }

    /**
     * read メソッドが呼び出された後に、
     * FileInputStreamMonitor#postProcess を呼び出すように、
     * コードに修正を加える。
     * 
     * @param method メソッド
     * @throws CannotCompileException コンパイル時の例外
     */
    private void modifyReadMethod(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(INPUSTREAM_MONITOR_NAME + ".postProcess($_);");
    }

    /**
     * read メソッドが呼び出された後に、
     * FileOutputStreamMonitor#postProcess を呼び出すように、
     * コードに修正を加える。
     * 
     * @param method メソッド
     * @throws CannotCompileException コンパイル時の例外
     * 
     */
    private void modifyWriteMethod(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(OUTPUSTREAM_MONITOR_NAME + ".postProcess($1.length);");
    }

    /**
     * FileOutputStreamMonitor に修正を加える。
     * 
     * @param method メソッド
     * @throws CannotCompileException コンパイル時の例外
     * 
     */
    private void modifyWriteMethod2(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(OUTPUSTREAM_MONITOR_NAME + ".postProcess($3);");
    }
}
