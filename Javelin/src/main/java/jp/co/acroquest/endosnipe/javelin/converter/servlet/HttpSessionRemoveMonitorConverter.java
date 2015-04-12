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
package jp.co.acroquest.endosnipe.javelin.converter.servlet;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javassist.CannotCompileException;
import jp.co.acroquest.endosnipe.javassist.CtBehavior;
import jp.co.acroquest.endosnipe.javassist.Modifier;
import jp.co.acroquest.endosnipe.javassist.NotFoundException;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.reference.monitor.ReferenceMonitor;

/**
 * HttpSession継承クラスをモニタリングするコードを埋め込むコンバータ
 * 
 * @author S.Kimura
 */
public class HttpSessionRemoveMonitorConverter extends AbstractConverter
{
    /** 弱参照モニタークラス名称 */
    private static final String REFERENCE_MONITOR_NAME = ReferenceMonitor.class.getCanonicalName();

    /** モニタークラスに登録する際のキー */
    public static final String HTTPSESSION_MONITORKEY = "HttpSessionMonitorConverter";

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // 何もしない。
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
     * メソッドの振る舞いを修正する。
     * @param ctBehavior CtBehavior
     * @throws CannotCompileException コンパイルできない場合
     * @throws NotFoundException クラスが見つからない場合
     */
    private void convertBehavior(CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        if (Modifier.isStatic(ctBehavior.getModifiers()) == false)
        {
            ctBehavior.insertAfter(REFERENCE_MONITOR_NAME + ".remove(\"" + HTTPSESSION_MONITORKEY
                    + "\", this);");
        }
        else
        {
            ctBehavior.insertAfter(REFERENCE_MONITOR_NAME + ".remove(\"" + HTTPSESSION_MONITORKEY
                    + ".class\", $class);");
        }

        logModifiedMethod("HttpSessionRemoveMonitorConverter", ctBehavior);

    }
}
