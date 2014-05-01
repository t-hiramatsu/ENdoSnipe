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
package jp.co.acroquest.endosnipe.javelin.converter.leak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.common.JavassistUtil;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinMessages;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CollectionMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * �R���N�V�����T�C�Y�ǐ՗p�̃R���o�[�^
 * �R���N�V�����N���X�A�}�b�v�N���X�̃R���X�g���N�^�ɑ΂��A
 * �Ď��}�l�[�W���Ɏ����o�^����R�[�h�𖄂ߍ��ށB
 * 
 * @author kimura
 */
public class CollectionMonitorConverter extends AbstractConverter
{
    /** �Ď��N���X�̖��� */
    public static final String COLLECTIONTRACER_NAME = CollectionMonitor.class.getCanonicalName();

    /**
     * Map����������N���X�ł���΁Aput,putAll���\�b�h�ɏ��擾�R�[�h�𖄂ߍ��ށB
     * Queue����������N���X�ł���΁Aoffer���\�b�h�ɏ��擾�R�[�h�𖄂ߍ��ށB
     * Collection����������N���X�ł���΁Aadd,addAll���\�b�h�ɏ��擾�R�[�h�𖄂ߍ��ށB
     * ����ȊO�̏ꍇ�͉������Ȃ��B
     * 
     * @throws CannotCompileException �R�[�h�̖��ߍ��݂Ɏ��s�����ꍇ�B
     * @throws IOException �ϊ����CtClass�̃o�C�g�R�[�h�ւ̕ϊ��Ɏ��s�����ꍇ�B 
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            IOException
    {
        CtClass targetClass = getCtClass();
        boolean excludeTarget = isExcludeTarget(targetClass);
        if (excludeTarget)
        {
            return;
        }

        boolean isInheritedMap =
                JavassistUtil.isInherited(targetClass, targetClass.getClassPool(),
                                          Map.class.getCanonicalName());

        boolean isInheritedCollection =
                JavassistUtil.isInherited(targetClass, targetClass.getClassPool(),
                                          Collection.class.getCanonicalName());
        boolean isInheritedQueue =
                JavassistUtil.isInherited(targetClass, targetClass.getClassPool(),
                                          Queue.class.getCanonicalName());

        if (isInheritedMap)
        {
            // put���\�b�h�Ɋւ��ẮA������2�ł�����݂̂̂��擾����B
            List<CtMethod> targetMethods = getDeclaredPutMethods(targetClass);
            for (CtMethod targetMethod : targetMethods)
            {
                modifyMapAdd(targetMethod);
            }
            targetMethods = getDeclaredMethods(targetClass, "putAll");
            for (CtMethod targetMethod : targetMethods)
            {
                modifyMapAdd(targetMethod);
            }
        }
        else if (isInheritedQueue)
        {
            // Collection�p����������AQueue�p������̕����Ɏ��s����B
            // ���R�́AArrayBlockingQueue�ȂǁACollection��Queue�������������Ă���N���X�ɑ΂��ẮA
            // add()��addAll()��offer()���ϊ�����K�v�����邽�߁B
            List<CtMethod> targetMethods = getDeclaredMethods(targetClass,"add|addAll" +
            		"|addFirst|addLast|offer|offerFirst|offerLast|put|putFirst|putLast");
            for (CtMethod targetMethod : targetMethods)
            {
                modifyCollectionAdd(targetMethod);
            }
        }
        else if (isInheritedCollection)
        {
            List<CtMethod> targetMethods = getDeclaredMethods(targetClass, "add|addAll|addElement");
            for (CtMethod targetMethod : targetMethods)
            {
                modifyCollectionAdd(targetMethod);
            }
        }
        else
        {
            return;
        }

        setNewClassfileBuffer(targetClass.toBytecode());
    }

    /**
     * �w�肵���N���X�Ő錾����Ă��郁�\�b�h�̃��X�g���擾����B
     * 
     * @param ctClass �N���X�B
     * @param methodName ���\�b�h���B
     * @return �w�肵���N���X�Ő錾����Ă��郁�\�b�h�̃��X�g�B
     */
    public List<CtMethod> getDeclaredMethods(final CtClass ctClass, final String methodName)
    {
        List<CtMethod> result = new ArrayList<CtMethod>();

        for (CtMethod ctMethod : ctClass.getDeclaredMethods())
        {
            if (ctMethod.getName().matches(methodName) && ctMethod.isEmpty() == false)
            {
                result.add(ctMethod);
            }
        }

        return result;
    }
    
    /**
     * �w�肵���AMap�����������N���X�Ő錾����Ă���Aput���\�b�h�̃��X�g���擾����B<br />
     * �������A�擾����put���\�b�h�́A������2�ikey��value�j�ł�����݂̂̂�ΏۂƂ���B<br />
     * 
     * @param ctClass Map�����������N���X�B
     * @return �w�肵���N���X�Ő錾����Ă���A�w�肳�ꂽ���O�������\�b�h�̃��X�g�B
     */
    public List<CtMethod> getDeclaredPutMethods(final CtClass ctClass)
    {
        List<CtMethod> result = new ArrayList<CtMethod>();

        for (CtMethod ctMethod : ctClass.getDeclaredMethods())
        {
            if ("put".equals(ctMethod.getName()) && ctMethod.isEmpty() == false)
            {
                CtClass[] parameterTypes = new CtClass[0];
                try
                {
                    parameterTypes = ctMethod.getParameterTypes();
                }
                catch (NotFoundException ex)
                {
                    String key = "javelin.converter.CollectionMonitorConverter.ArgumentTypeCannotReferred";
                    String message = JavelinMessages.getMessage(key);
                    SystemLogger.getInstance().warn(message, ex);
                }
                if (parameterTypes.length == 2)
                {
                    result.add(ctMethod);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // Do Nothing.
    }

    /**
     * �Ď��}�l�[�W���ւ̓o�^�R�[�h�𖄂ߍ���
     * 
     * @param target
     * @throws CannotCompileException 
     */
    private void modifyMapAdd(final CtMethod target)
        throws CannotCompileException
    {
        // �Ώۂ̃��\�b�h�̈����̐��𒲂ׁA�ǉ����ꂽ�I�u�W�F�N�g�̃C���f�b�N�X���擾����B
        String element = "$";
        int argsLength = 0;
        try
        {
            argsLength = target.getParameterTypes().length;
            element += argsLength;
        }
        catch (NotFoundException ex)
        {
            SystemLogger.getInstance().warn("Parameter Types not Found.", ex);
            element = "null";
        }
        
        target.insertAfter(COLLECTIONTRACER_NAME
                            + ".preProcessMapPut(this, "+ element + ");", true);
        logModifiedMethod("CollectionMonitorConverter", target);
    }

    /**
     * �Ď��}�l�[�W���ւ̓o�^�R�[�h�𖄂ߍ���
     * 
     * @param target
     * @throws CannotCompileException 
     */
    private void modifyCollectionAdd(final CtMethod target)
        throws CannotCompileException
    {
        // �Ώۂ̃��\�b�h�̈����̐��𒲂ׁA�ǉ����ꂽ�I�u�W�F�N�g�̃C���f�b�N�X���擾����B
        String element = "$";
        int argsLength = 0;
        try
        {
            argsLength = target.getParameterTypes().length;
            element += argsLength;
        }
        catch (NotFoundException ex)
        {
            SystemLogger.getInstance().warn("Parameter Types not Found.", ex);
            element = "null";
        }
        
        target.insertAfter(COLLECTIONTRACER_NAME
                            + ".preProcessCollectionAdd(this, " + element + ");", true);
        logModifiedMethod("CollectionMonitorConverter", target);
    }

    /**
     * ���O�ΏۂƂȂ�N���X�ł��邩�ǂ����𔻒肵�܂��B(WAS7.0 �Ή�)<br />
     * <br />
     * ���O�Ώۏ���:<br />
     * <li>�x���_�[��IBM�ł���B</li>
     * <li>���z�}�V����Java1.6�ɏ������Ă���B</li>
     * <li>java.util.Hashtable �� java.util.AbstractList �ł���B</li>
     *
     * @return
     */
    private boolean isExcludeTarget(CtClass targetClass)
    {
        String vendor = System.getProperty("java.vendor");
        if (vendor.contains("IBM"))
        {
            String version = System.getProperty("java.specification.version");
            if ("1.6".equals(version))
            {
                String className = targetClass.getName();
                if ("java.util.Hashtable".equals(className)
                        || "java.util.AbstractList".equals(className))
                {
                    SystemLogger.getInstance().info("exclude class:" + className);
                    return true;
                }
            }
        }
        return false;
    }
}
