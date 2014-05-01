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
 * JRuby�p�̃R���o�[�^�N���X�B
 * @author tanimoto
 *
 */
public class JrubyConverter extends AbstractConverter
{
    /** JavelinRecorder�� */
    private static final String JRUBY_RECORDER_NAME = JrubyRecorder.class.getName();

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(�O)�B */
    private static final String PREPROCESS_CODE_BEFORE = JRUBY_RECORDER_NAME + ".preProcess(";

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(��)�B */
    private static final String PREPROCESS_CODE_AFTER = "\", $args);";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(�O)�B */
    private static final String POSTPROCESS_NG_CODE_BEFORE =
            JRUBY_RECORDER_NAME + ".postProcessNG(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(��)�B */
    private static final String POSTPROCESS_NG_CODE_AFTER = "\",$args,$e);throw $e;";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(�O)�B */
    private static final String POSTPROCESS_OK_CODE_BEFORE =
            JRUBY_RECORDER_NAME + ".postProcessOK(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(��)�B */
    private static final String POSTPROCESS_OK_CODE_AFTER = "\",$args,($w)$_);";

    /** ���s�O�����Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int PREPROCESS_CODE_FIXEDLENGTH =
            PREPROCESS_CODE_BEFORE.length() + PREPROCESS_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int POSTPROCESS_CODE_FIXEDLENGTH =
            POSTPROCESS_OK_CODE_BEFORE.length() + POSTPROCESS_OK_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������NG�R�[�h�̌Œ蕔���̕����� */
    private static final int NG_CODE_FIXEDLENGTH =
            POSTPROCESS_NG_CODE_BEFORE.length() + POSTPROCESS_NG_CODE_AFTER.length();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // S2StatsJavelinRecorder������������
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
     * ���\�b�h�̐U�镑�����C������B
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
        String argClassMethod = "\"" + className + "\",\"" + methodName;
        int argLength = argClassMethod.length();

        // ���s�O������ǉ�����B
        int preProcessCodeLength = argLength + PREPROCESS_CODE_FIXEDLENGTH;
        StringBuilder preProcessCodeBuffer = new StringBuilder(preProcessCodeLength);
        preProcessCodeBuffer.append(PREPROCESS_CODE_BEFORE);
        preProcessCodeBuffer.append(argClassMethod);
        preProcessCodeBuffer.append(PREPROCESS_CODE_AFTER);
        String callPreProcessCode = preProcessCodeBuffer.toString();
        ctBehavior.insertBefore(callPreProcessCode);

        // ���s�㏈����ǉ�����B
        int postProcessCodeLength = argLength + POSTPROCESS_CODE_FIXEDLENGTH;
        StringBuilder postProcessCodeBuffer = new StringBuilder(postProcessCodeLength);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_BEFORE);
        postProcessCodeBuffer.append(argClassMethod);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_AFTER);
        String callPostProcessCode = postProcessCodeBuffer.toString();

        ctBehavior.insertAfter(callPostProcessCode);

        // ��O�n���h�����O��ǉ�����B
        CtClass throwable = getClassPool().get(Throwable.class.getName());
        // ���s�O������ǉ�����B
        int ngCodeLength = argLength + NG_CODE_FIXEDLENGTH;
        StringBuilder ngCodeBuffer = new StringBuilder(ngCodeLength);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_BEFORE);
        ngCodeBuffer.append(argClassMethod);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_AFTER);
        String ngCode = ngCodeBuffer.toString();
        ctBehavior.addCatch(ngCode, throwable);

        // �������ʂ����O�ɏo�͂���B
        logModifiedMethod(JrubyConverter.class.getSimpleName(), ctBehavior);
    }
}
