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
package jp.co.acroquest.endosnipe.javelin.converter.jruby;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * JRuby用のコンバータクラス。
 * @author tanimoto
 *
 */
public class JrubyConverter extends AbstractConverter
{
    /** JavelinRecorder名 */
    private static final String JRUBY_RECORDER_NAME = JrubyRecorder.class.getName();

    /** 実行前処理として追加するpreProcessのコード(前)。 */
    private static final String PREPROCESS_CODE_BEFORE = JRUBY_RECORDER_NAME + ".preProcess(";

    /** 実行前処理として追加するpreProcessのコード(後)。 */
    private static final String PREPROCESS_CODE_AFTER = "\", $args);";

    /** 実行後処理として追加するpostProcessNGのコード(前)。 */
    private static final String POSTPROCESS_NG_CODE_BEFORE =
            JRUBY_RECORDER_NAME + ".postProcessNG(";

    /** 実行後処理として追加するpostProcessNGのコード(後)。 */
    private static final String POSTPROCESS_NG_CODE_AFTER = "\",$args,$e);throw $e;";

    /** 実行後処理として追加するpostProcessOKのコード(前)。 */
    private static final String POSTPROCESS_OK_CODE_BEFORE =
            JRUBY_RECORDER_NAME + ".postProcessOK(";

    /** 実行後処理として追加するpostProcessOKのコード(後)。 */
    private static final String POSTPROCESS_OK_CODE_AFTER = "\",$args,($w)$_);";

    /** 実行前処理として追加されるコードの固定部分の文字列長 */
    private static final int PREPROCESS_CODE_FIXEDLENGTH =
            PREPROCESS_CODE_BEFORE.length() + PREPROCESS_CODE_AFTER.length();

    /** 実行後処理として追加されるコードの固定部分の文字列長 */
    private static final int POSTPROCESS_CODE_FIXEDLENGTH =
            POSTPROCESS_OK_CODE_BEFORE.length() + POSTPROCESS_OK_CODE_AFTER.length();

    /** 実行後処理として追加されるNGコードの固定部分の文字列長 */
    private static final int NG_CODE_FIXEDLENGTH =
            POSTPROCESS_NG_CODE_BEFORE.length() + POSTPROCESS_NG_CODE_AFTER.length();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // S2StatsJavelinRecorderを初期化する
        synchronized (StatsJavelinRecorder.class)
        {
            if (StatsJavelinRecorder.isInitialized() == false)
            {
                StatsJavelinRecorder.javelinInit(new JavelinConfig());
            }
        }
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
    private void convertBehavior(final CtBehavior ctBehavior)
        throws CannotCompileException,
            NotFoundException
    {
        String className = getClassName();
        String methodName = ctBehavior.getName();
        String argClassMethod = "\"" + className + "\",\"" + methodName;
        int argLength = argClassMethod.length();

        // 実行前処理を追加する。
        int preProcessCodeLength = argLength + PREPROCESS_CODE_FIXEDLENGTH;
        StringBuilder preProcessCodeBuffer = new StringBuilder(preProcessCodeLength);
        preProcessCodeBuffer.append(PREPROCESS_CODE_BEFORE);
        preProcessCodeBuffer.append(argClassMethod);
        preProcessCodeBuffer.append(PREPROCESS_CODE_AFTER);
        String callPreProcessCode = preProcessCodeBuffer.toString();
        ctBehavior.insertBefore(callPreProcessCode);

        // 実行後処理を追加する。
        int postProcessCodeLength = argLength + POSTPROCESS_CODE_FIXEDLENGTH;
        StringBuilder postProcessCodeBuffer = new StringBuilder(postProcessCodeLength);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_BEFORE);
        postProcessCodeBuffer.append(argClassMethod);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_AFTER);
        String callPostProcessCode = postProcessCodeBuffer.toString();

        ctBehavior.insertAfter(callPostProcessCode);

        // 例外ハンドリングを追加する。
        CtClass throwable = getClassPool().get(Throwable.class.getName());
        // 実行前処理を追加する。
        int ngCodeLength = argLength + NG_CODE_FIXEDLENGTH;
        StringBuilder ngCodeBuffer = new StringBuilder(ngCodeLength);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_BEFORE);
        ngCodeBuffer.append(argClassMethod);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_AFTER);
        String ngCode = ngCodeBuffer.toString();
        ctBehavior.addCatch(ngCode, throwable);

        // 処理結果をログに出力する。
        logModifiedMethod(JrubyConverter.class.getSimpleName(), ctBehavior);
    }
}
