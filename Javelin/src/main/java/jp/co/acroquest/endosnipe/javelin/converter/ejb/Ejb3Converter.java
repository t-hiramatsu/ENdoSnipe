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

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtMethod;

/**
 * EJB3.0�ȍ~�ɑΉ�����EJB�R���o�[�^�B
 * 
 * @author S.Kimura
 */
public class Ejb3Converter extends AbstractEjbConverter
{
    /** �ΏۃN���X�̕ϊ����s���A�Ɣ��肷��A�m�e�[�V�����N���X���� */
    private static final List<String> TARGET_ANNOTATIONS = new ArrayList<String>();

    static
    {
        TARGET_ANNOTATIONS.add("javax.ejb.Stateless");
        TARGET_ANNOTATIONS.add("javax.ejb.Stateful");
        TARGET_ANNOTATIONS.add("javax.ejb.MessageDriven");
    }

    /**
     * �ΏۃN���X�̕ϊ����s�����ǂ����𔻒肷��B���L�̏����̂����ꂩ�𖞂����ꍇ�ɕϊ����s���B
     * 
     * <ol>
     * <li>javax.ejb.Stateless�A�m�e�[�V�������܂�ł���B</li>
     * <li>javax.ejb.Stateful�A�m�e�[�V�������܂�ł���B</li>
     * <li>javax.ejb.MessageDriven�A�m�e�[�V�������܂�ł���B</li>
     * </ol>
     * 
     * @return �ϊ����s�����ǂ���
     * @throws CannotCompileException �R���p�C���Ɏ��s�����ꍇ
     */
    @Override
    protected boolean isConvert()
        throws CannotCompileException
    {
        boolean isConvert = false;

        Object[] annotations = null;
        try
        {
            annotations = getCtClass().getAnnotations();
        }
        catch (ClassNotFoundException cnex)
        {
            SystemLogger.getInstance().debug(cnex);
            return false;
        }

        for (Object targetAnnotation : annotations)
        {
            String annotationDesctiption = targetAnnotation.toString();

            if (containTargetAnnotation(annotationDesctiption) == true)
            {
                isConvert = true;
                break;
            }
        }
        return isConvert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void printModifiedLog(final CtMethod target)
    {
        logModifiedMethod("EJB3Converter", target);
    }

    /**
     * �Ώۂ̃A�m�e�[�V�������ϊ��ΏۃA�m�e�[�V�������܂�ł��邩�𔻒�
     * 
     * @return �ϊ��ΏۃA�m�e�[�V�������܂�ł���
     */
    private boolean containTargetAnnotation(final String annotationDesctiption)
    {
        for (String judgeTarget : TARGET_ANNOTATIONS)
        {
            int targetIndex = annotationDesctiption.indexOf(judgeTarget);
            if (0 <= targetIndex)
            {
                return true;
            }
        }

        return false;
    }
}
