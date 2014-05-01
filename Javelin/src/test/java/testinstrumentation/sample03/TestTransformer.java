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
package testinstrumentation.sample03;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;

public class TestTransformer implements ClassFileTransformer
{

    public byte[] transform(final ClassLoader loader, final String className,
            final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
        throws IllegalClassFormatException
    {
        // �N���X�v�[�������
        ClassPool objClassPool = ClassPool.getDefault();
        // Javassist�Ŏg����悤�ɁA�ϊ�����
        String toJavassistClassName = className.replace("/", ".");

        // �ϊ��p�I�u�W�F�N�g
        CtClass objCtClass = null;
        try
        {
            objCtClass = objClassPool.get(toJavassistClassName);
        }
        catch (NotFoundException objNotFoundException)
        {
            objNotFoundException.printStackTrace();
        }

        // �S�ă��\�b�h���擾���āA�ϊ�����
        CtMethod[] objCtMethodArr = objCtClass.getDeclaredMethods();
        for (int index = 0; index < objCtMethodArr.length; index++)
        {
            try
            {
                // �O�����R�[�h�����
                StringBuffer beforeCode = new StringBuffer();
                beforeCode.append("{ System.out.println(");
                beforeCode.append("\"���������\�b�h�w");
                beforeCode.append(toJavassistClassName);
                beforeCode.append(".\"");
                beforeCode.append("+");
                beforeCode.append("\"");
                beforeCode.append(objCtMethodArr[index].getName());
                beforeCode.append("�x���Ăяo���ꂽ�B\"");
                beforeCode.append("); }");

                // �O�����R�[�h�𖄂ߍ���
                objCtMethodArr[index].insertBefore(beforeCode.toString());

                // �㏈���R�[�h�����
                StringBuffer afterCode = new StringBuffer();
                afterCode.append("{ System.out.println(");
                afterCode.append("\"���������\�b�h�w");
                afterCode.append(toJavassistClassName);
                afterCode.append(".\"");
                afterCode.append("+");
                afterCode.append("\"");
                afterCode.append(objCtMethodArr[index].getName());
                afterCode.append("�x���I��܂����B\"");
                afterCode.append("); }");
                afterCode.append("{ System.out.println(");
                afterCode.append("); }");

                // �㏈���R�[�h�𖄂ߍ���
                objCtMethodArr[index].insertAfter(afterCode.toString());
            }
            catch (CannotCompileException objCannotCompileException)
            {
                objCannotCompileException.printStackTrace();
            }
        }

        // �ϊ������N���X��ԋp����
        byte[] byteNewClassArr = null;
        try
        {
            byteNewClassArr = objCtClass.toBytecode();
        }
        catch (IOException objIOException)
        {
            objIOException.printStackTrace();
        }
        catch (CannotCompileException objCannotCompileException)
        {
            objCannotCompileException.printStackTrace();
        }
        return byteNewClassArr;
    }
}
