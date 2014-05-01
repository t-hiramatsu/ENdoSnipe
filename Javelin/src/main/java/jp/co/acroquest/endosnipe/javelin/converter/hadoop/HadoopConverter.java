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
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.util.ConvertedMethodCounter;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * Hadoop�p�R���o�[�^
 *
 * @author asazuma
 */
public class HadoopConverter extends AbstractConverter
{
    /** �R���o�[�^�� */
    private static final String CONVERTER_NAME = "HadoopConverter";

    /** JavelinRecorder�� */
    private static final String JAVELIN_RECORDER_NAME = HadoopRecorder.class.getName();

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(�O) */
    private static final String PREPROCESS_CODE_BEFORE = JAVELIN_RECORDER_NAME + ".preProcess(";

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(��) */
    private static final String PREPROCESS_CODE_AFTER = "\", $args);";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(�O) */
    private static final String POSTPROCESS_NG_CODE_BEFORE =
            JAVELIN_RECORDER_NAME + ".postProcessNG(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(��) */
    private static final String POSTPROCESS_NG_CODE_AFTER = "\",$e, $0);throw $e;";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(�O) */
    private static final String POSTPROCESS_OK_CODE_BEFORE =
            JAVELIN_RECORDER_NAME + ".postProcessOK(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(��) */
    private static final String POSTPROCESS_OK_CODE_AFTER = "\",($w)$_, $0);";

    /** ���s�O�����Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int PREPROCESS_CODE_FIXEDLENGTH =
            PREPROCESS_CODE_BEFORE.length() + PREPROCESS_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int POSTPROCESS_CODE_FIXEDLENGTH =
            POSTPROCESS_OK_CODE_BEFORE.length() + POSTPROCESS_OK_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������NG�R�[�h�̌Œ蕔���̕����� */
    private static final int NG_CODE_FIXEDLENGTH =
            POSTPROCESS_NG_CODE_BEFORE.length() + POSTPROCESS_NG_CODE_AFTER.length();

    /** �������̃��\�b�h�̃o�C�g�R�[�h���B */
    private BytecodeInfo info_;

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // HadoopRecorder������������
        synchronized (HadoopRecorder.class)
        {
            if (HadoopRecorder.isInitialized() == false)
            {
                HadoopRecorder.javelinInit(new JavelinConfig());
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
            // �w�肳�ꂽHadoop�̃��\�b�h�͏�ɊĎ��ΏۂƂ���B
            convertBehavior(ctBehavior);
            // JavelinConverter�ŕϊ����s�������\�b�h�����L�^
            ConvertedMethodCounter.incrementConvertedCount();
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
        logModifiedMethod(CONVERTER_NAME, ctBehavior, createByteCodeInfo());
    }

    private String createByteCodeInfo()
    {
        return this.info_ != null ? ":" + this.info_ : "";
    }

    private static class BytecodeInfo
    {
        int length_ = -1;

        int controlCount_ = -1;

        @Override
        public String toString()
        {
            String result;
            if (controlCount_ == -1)
            {
                result = "codeLength=" + length_;
            }
            else
            {
                result = "codeLength=" + length_ + ", controlCount=" + controlCount_;
            }

            return result;
        }
    }
}
