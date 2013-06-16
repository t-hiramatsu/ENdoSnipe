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

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.file.monitor.FileOpenCloseMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

public class FileOpenConverter extends AbstractConverter
{
    /** ファイルopen/closeモニタのクラス名称 */
    private static final String OPENCLOSE_MONITOR_NAME =
                                                         FileOpenCloseMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // 何もしない。
    }

    /**
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

        for (CtBehavior b : getMatcheDeclaredBehavior())
        {
            CtClass[] parameterTypes;
            try
            {
                parameterTypes = b.getParameterTypes();
                if (parameterTypes.length > 0
                        && File.class.getCanonicalName().equals(parameterTypes[0].getName()))
                {
                    b.insertAfter(OPENCLOSE_MONITOR_NAME + ".open(this.getFD(), (java.io.File)$1);", true);
                }
            }
            catch (NotFoundException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

}
