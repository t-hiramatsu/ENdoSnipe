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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.util.ConvertedMethodCounter;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * EJB�̃Z�b�V����Bean���Ď����邽�߂̃R���o�[�^�BEJB2�ȑO��EJB3�ȍ~�̋��ʏ�����{�N���X�ɋL�q����B
 * 
 * @author S.Kimura
 */
public abstract class AbstractEjbConverter extends AbstractConverter
{
    /** �ϊ��Ώۂ��珜�O���郁�\�b�h */
    private static final Set<String> EXCLUDE_METHODS = new HashSet<String>();

    /** JavelinRecorder�� */
    private static final String EJB_MONITOR_NAME = EjbSessionMonitor.class.getCanonicalName();

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(�O)�B */
    private static final String PREPROCESS_CODE_BEFORE = EJB_MONITOR_NAME + ".preProcess(";

    /** ���s�O�����Ƃ��Ēǉ�����preProcess�̃R�[�h(��)�B */
    private static final String PREPROCESS_CODE_AFTER = "\", $args);";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(�O)�B */
    private static final String POSTPROCESS_NG_CODE_BEFORE = EJB_MONITOR_NAME + ".postProcessNG(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessNG�̃R�[�h(��)�B */
    private static final String POSTPROCESS_NG_CODE_AFTER = "\",$e);throw $e;";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(�O)�B */
    private static final String POSTPROCESS_OK_CODE_BEFORE = EJB_MONITOR_NAME + ".postProcessOK(";

    /** ���s�㏈���Ƃ��Ēǉ�����postProcessOK�̃R�[�h(��)�B */
    private static final String POSTPROCESS_OK_CODE_AFTER = "\",($w)$_);";

    /** ���s�O�����Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int PREPROCESS_CODE_FIXEDLENGTH =
            PREPROCESS_CODE_BEFORE.length() + PREPROCESS_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������R�[�h�̌Œ蕔���̕����� */
    private static final int POSTPROCESS_CODE_FIXEDLENGTH =
            POSTPROCESS_OK_CODE_BEFORE.length() + POSTPROCESS_OK_CODE_AFTER.length();

    /** ���s�㏈���Ƃ��Ēǉ������NG�R�[�h�̌Œ蕔���̕����� */
    private static final int NG_CODE_FIXEDLENGTH =
            POSTPROCESS_NG_CODE_BEFORE.length() + POSTPROCESS_NG_CODE_AFTER.length();

    static
    {
        EXCLUDE_METHODS.add("ejbCreate");
        EXCLUDE_METHODS.add("ejbActivate");
        EXCLUDE_METHODS.add("ejbPassivate");
        EXCLUDE_METHODS.add("ejbRemove");
        EXCLUDE_METHODS.add("setSessionContext");
        EXCLUDE_METHODS.add("setMessageDrivenContext");
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
        JavelinConfig config = new JavelinConfig();
        boolean isConverted = false;

        if (config.isEjbSessionMonitor() == false)
        {
            return;
        }

        if (isConvert() == false)
        {
            return;
        }

        List<CtBehavior> declaredBehaviorLIst = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : declaredBehaviorLIst)
        {
            if (EXCLUDE_METHODS.contains(ctBehavior.getName()) == false)
            {
                if (ctBehavior.isEmpty() == true)
                {
                    continue;
                }
                
                if (ctBehavior instanceof CtMethod != true)
                {
                    continue;
                }

                CtMethod ctMethod = (CtMethod)ctBehavior;
                convertMethod(ctMethod );
                //�J�o���b�W���擾���邽�߁A�J���A�v���P�[�V�������ŕϊ����ꂽ���\�b�h���Ƃ��ăJ�E���g
                ConvertedMethodCounter.incrementConvertedCount();
                isConverted = true;
            }
        }

        if (isConverted == true)
        {
            setNewClassfileBuffer(getCtClass().toBytecode());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // Do Nothing.
    }

    /**
     * �ϊ��Ώۃ��\�b�h�̃N���X�ϊ����s��
     * 
     * @param targetMethod �ϊ��Ώۃ��\�b�h
     * @throws CannotCompileException �R���p�C���Ɏ��s�����ꍇ 
     * @throws NotFoundException �N���X���擾�ł��Ȃ������ꍇ
     */
    private void convertMethod(final CtMethod targetMethod)
        throws CannotCompileException,
            NotFoundException
    {
        String className = getClassName();
        String methodName = targetMethod.getName();
        String argClassMethod = "\"" + className + "\",\"" + methodName;
        int argLength = argClassMethod.length();

        // ���s�O������ǉ�����B
        int preProcessCodeLength = argLength + PREPROCESS_CODE_FIXEDLENGTH;
        StringBuilder preProcessCodeBuffer = new StringBuilder(preProcessCodeLength);
        preProcessCodeBuffer.append(PREPROCESS_CODE_BEFORE);
        preProcessCodeBuffer.append(argClassMethod);
        preProcessCodeBuffer.append(PREPROCESS_CODE_AFTER);
        String callPreProcessCode = preProcessCodeBuffer.toString();
        targetMethod.insertBefore(callPreProcessCode);

        // ���s�㏈����ǉ�����B
        int postProcessCodeLength = argLength + POSTPROCESS_CODE_FIXEDLENGTH;
        StringBuilder postProcessCodeBuffer = new StringBuilder(postProcessCodeLength);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_BEFORE);
        postProcessCodeBuffer.append(argClassMethod);
        postProcessCodeBuffer.append(POSTPROCESS_OK_CODE_AFTER);
        String callPostProcessCode = postProcessCodeBuffer.toString();

        targetMethod.insertAfter(callPostProcessCode);

        // ��O�n���h�����O��ǉ�����B
        CtClass throwable = getClassPool().get(Throwable.class.getName());
        // ���s�O������ǉ�����B
        int ngCodeLength = argLength + NG_CODE_FIXEDLENGTH;
        StringBuilder ngCodeBuffer = new StringBuilder(ngCodeLength);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_BEFORE);
        ngCodeBuffer.append(argClassMethod);
        ngCodeBuffer.append(POSTPROCESS_NG_CODE_AFTER);
        String ngCode = ngCodeBuffer.toString();
        targetMethod.addCatch(ngCode, throwable);
        printModifiedLog(targetMethod);
    }

    /**
     * �ΏۃN���X�̃R���o�[�g���s�����ǂ���������s��
     * 
     * @return �R���o�[�g���s�����ǂ���
     * @throws CannotCompileException �R���o�[�g�̌��ʃR���p�C���Ɏ��s�����ꍇ
     */
    protected abstract boolean isConvert()
        throws CannotCompileException;

    /**
     * �ϊ������o�͂���
     * 
     * @param target �ϊ����{���\�b�h
     */
    protected abstract void printModifiedLog(final CtMethod target);
}
