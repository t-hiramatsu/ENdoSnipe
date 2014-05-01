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
package jp.co.acroquest.endosnipe.javelin.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.conf.ExcludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.IncludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinMessages;
import jp.co.acroquest.endosnipe.javelin.converter.util.ConverterUtil;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtConstructor;
import jp.co.smg.endosnipe.javassist.CtMember;
import jp.co.smg.endosnipe.javassist.Modifier;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * �R���o�[�^�̒��ۃN���X
 * 
 * @author eriguchi
 * 
 */
public abstract class AbstractConverter implements Converter
{
    /** �R���X�g���N�^�p�̃��\�b�h���ʎq */
    private static final String           CONSTRUCTOR_IDENTIFIER = "<CONSTRUCTOR>";

    /** �N���X�t�@�C���̃o�b�t�@ */
    private byte[]                        classfileBuffer_;

    /** �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@ */
    private byte[]                        newClassfileBuffer_;

    /** �N���X�� */
    private String                        className_;

    /** Include�̐ݒ� */
    private IncludeConversionConfig       includeConfig_;

    /** Exclude�̐ݒ胊�X�g */
    private List<ExcludeConversionConfig> excludeConfigList_;

    /** CtClass */
    private CtClass                       ctClass_;

    /** ClassPool */
    private ClassPool                     pool_;

    /**
     * {@inheritDoc}
     */
    public byte[] convert(final String className, final byte[] classfileBuffer,
            final ClassPool pool, final CtClass ctClass,
            final IncludeConversionConfig includeConfig,
            final List<ExcludeConversionConfig> excludeConfigList)
    {
        if (classfileBuffer != null)
        {
            this.classfileBuffer_ = classfileBuffer.clone();
        }
        else
        {
            this.classfileBuffer_ = null;
        }

        this.className_ = className;
        this.includeConfig_ = includeConfig;
        this.excludeConfigList_ = excludeConfigList;
        this.pool_ = pool;
        this.ctClass_ = ctClass;
        this.newClassfileBuffer_ = null;

        prepare();

        try
        {
            convertImpl();
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        return getResult();
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getResult()
    {
        if (this.newClassfileBuffer_ != null)
        {
            return this.newClassfileBuffer_.clone();
        }

        return null;
    }

    /**
     * �O����
     */
    protected void prepare()
    {
        // �������Ȃ�
    }

    /**
     * �R�[�h���ߍ��ݑΏۃ��\�b�h�̃��X�g���쐬����B
     * @return �R�[�h���ߍ��ݑΏۃ��\�b�h�̃��X�g
     */
    protected List<CtBehavior> getMatcheDeclaredBehavior()
    {
        List<CtBehavior> list = new ArrayList<CtBehavior>();

        for (CtBehavior ctBehavior : this.ctClass_.getDeclaredBehaviors())
        {
            // ��̃��\�b�h�͏��O����B
            if (ctBehavior.isEmpty())
            {
                continue;
            }

            // �C���q��Abstract����Native�̃��\�b�h�͏��O����B
            int modifiers = ctBehavior.getModifiers();
            if (Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers))
            {
                continue;
            }

            // Include�ɐݒ肳��Ă��āAExclude�ɐݒ肳��Ă��Ȃ����\�b�h�����X�g�ɒǉ�����B
            String methodName = ctBehavior.getName();

            //�R���X�g���N�^�̏ꍇ�A�p�^�[����<Constructor>�̏ꍇ���ǉ��Ŕ�����s��
            boolean isConstructor = ctBehavior instanceof CtConstructor;
            boolean isConstInclude = false;
            boolean isConstExclude = false;

            IncludeConversionConfig includeConfig = this.includeConfig_;
            String methodNamePattern = includeConfig.getMethodNamePattern();
            if (isConstructor)
            {
                CtConstructor constructor = (CtConstructor)ctBehavior;
                if (constructor.isClassInitializer() == false)
                {
                    isConstInclude = CONSTRUCTOR_IDENTIFIER.matches(methodNamePattern);
                    isConstExclude = isExcludeTarget(CONSTRUCTOR_IDENTIFIER);
                }
            }

            //���\�b�h���ł̔��茋�ʎ擾
            boolean isNameInclude = methodName.matches(methodNamePattern);
            boolean isNameExclude = isExcludeTarget(methodName);

            //�R���X�g���N�^�A���\�b�h���̔���̂ǂ��炩�Łuinclude�v�ƂȂ�A
            //���R���X�g���N�^�A���\�b�h���̔���Łuexclude�v�ƂȂ��Ă��Ȃ��ꍇ�A�ϊ��Ώۂɒǉ�
            if ((isConstInclude || isNameInclude) && (!isConstExclude && !isNameExclude))
            {
                list.add(ctBehavior);
            }
        }

        return list;
    }

    /**
     * �����Ŏw�肵�����\�b�h���R�[�h���ߍ��ݑΏۂ��珜�O����邩���肷��B
     * @param methodName ���\�b�h��
     * @return true:�R�[�h���ߍ��ݑΏۂ��珜�O����Afalse:�R�[�h���ߍ��ݑΏۂ��珜�O���Ȃ�
     */
    private boolean isExcludeTarget(final String methodName)
    {
        for (ExcludeConversionConfig excludeConfig : this.excludeConfigList_)
        {
            if (methodName.matches(excludeConfig.getMethodNamePattern()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * �R���o�[�^�̎���
     * @throws CannotCompileException �R���p�C�����ł��Ȃ��ꍇ
     * @throws NotFoundException �N���X��������Ȃ��ꍇ
     * @throws IOException ���o�͗�O
     */
    public abstract void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException;

    /**
     * �N���X�t�@�C���̃o�b�t�@���擾����B
     * @return �N���X�t�@�C���̃o�b�t�@
     */
    public byte[] getClassfileBuffer()
    {
        if (this.classfileBuffer_ != null)
        {
            return this.classfileBuffer_.clone();
        }

        return null;
    }

    /**
     * �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@���擾����B
     * @return �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@
     */
    public byte[] getNewClassfileBuffer()
    {
        if (this.newClassfileBuffer_ != null)
        {
            return this.newClassfileBuffer_.clone();
        }

        return null;
    }

    /**
     * �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@��ݒ肷��B
     * @param newClassfileBuffer �R�[�h���ߍ��݌�̃N���X�t�@�C���̃o�b�t�@
     */
    public void setNewClassfileBuffer(final byte[] newClassfileBuffer)
    {
        if(newClassfileBuffer != null)
        {
            this.newClassfileBuffer_ = newClassfileBuffer.clone();
        }
    }

    /**
     * �N���X�����擾����.
     * 
     * @return �N���X��
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * Config���擾����.
     * 
     * @return Config
     */
    public IncludeConversionConfig getConfig()
    {
        return this.includeConfig_;
    }

    /**
     * C��Class���擾����.
     * 
     * @return C��Class
     */
    public CtClass getCtClass()
    {
        return this.ctClass_;
    }

    /**
     * ClassPool���擾����.
     * 
     * @return ClassPool
     */
    public ClassPool getClassPool()
    {
        return this.pool_;
    }

    /**
     * �ϊ��N���X���̎擾
     * 
     * @return �ϊ��N���X��
     */
    protected String simpleName()
    {
        String className = getClassName();
        className = ConverterUtil.toSimpleName(className);
        return className;
    }

    /**
     * �ϊ������o�͂���
     * 
     * @param converterName �R���o�[�^���B
     * @param ctMember �ϊ��Ώ�
     */
    protected void logModifiedMethod(final String converterName, final CtMember ctMember)
    {
        this.logModifiedMethod(converterName, ctMember, null);
    }

    /**
     * �ϊ������o�͂���
     * 
     * @param converterName �R���o�[�^���B
     * @param ctMember �ϊ��Ώ�
     * @param message ���b�Z�[�W�B
     */
    protected void logModifiedMethod(final String converterName, final CtMember ctMember,
            final String message)
    {
        // �������ʂ����O�ɏo�͂���B
        String methodName = ctMember.getName();

        String key = "javelin.converter.AbstractConverter.ModifiedMethodLabel";
        String modifiedMethodTag = JavelinMessages.getMessage(key, converterName);
        StringBuilder messageBuilder = new StringBuilder(modifiedMethodTag);
        messageBuilder.append(simpleName());
        messageBuilder.append("#");
        messageBuilder.append(methodName);

        if (message != null)
        {
            messageBuilder.append(message);
        }

        SystemLogger.getInstance().info(messageBuilder.toString());
    }
}
