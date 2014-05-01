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

import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.file.monitor.FileInputStreamMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.file.monitor.FileOutputStreamMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;

/**
 * �t�@�C���̓��o�̓f�[�^�𑗐M���邽�߂̃R���o�[�^
 * 
 * @author yamasaki
 */
public class FileMonitorConverter extends AbstractConverter
{
    /** �t�@�C�����̓��j�^�̃N���X���� */
    private static final String INPUSTREAM_MONITOR_NAME =
            FileInputStreamMonitor.class.getCanonicalName();

    /** �t�@�C���o�̓��j�^�̃N���X���� */
    private static final String OUTPUSTREAM_MONITOR_NAME =
            FileOutputStreamMonitor.class.getCanonicalName();

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // �������Ȃ��B
    }

    /**
     * java.io.FileInputStream#read �� java.io.FileOutputStream#write ��
     * �t�@�C�����o�͗ʎ擾�R�[�h�𖄂ߍ��ށB
     * 
     * @throws CannotCompileException �R���p�C������O
     * @throws IOException �t�@�C�����o�͎��̗�O
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            IOException
    {
        CtClass ctClass = getCtClass();

        if ("java.io.FileInputStream".equals(getClassName()))
        {
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods)
            {
                if (judgeModifyReadMethod(method))
                {
                    modifyReadMethod(method);
                    logModifiedMethod("FileMonitorConverter", method);

                }
            }
        }
        else if ("java.io.FileOutputStream".equals(getClassName()))
        {
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods)
            {
                judgeModifyWriteMethod(method);
            }
        }
        setNewClassfileBuffer(ctClass.toBytecode());
    }

    /**
     * java.io.FileInputStream �ɏC���������邩�ǂ������肷��B
     * 
     * @param method ���\�b�h
     * @return true:���\�b�h���C������Afalse:���\�b�h���C�����Ȃ�
     */
    private boolean judgeModifyReadMethod(final CtMethod method)
    {
        // read(byte[]), read(byte[], int, int) �̂Ƃ��ɁAmodifyWriteMethod()���Ăяo��
        String methodName = method.getName();
        boolean judge = false;
        if ("read".equals(methodName))
        {
            if ("([B)I".equals(method.getSignature()) || "([BII)I".equals(method.getSignature()))
            {
                judge = true;
            }
        }
        return judge;
    }

    /**
     * java.io.FileOutputStream �ɏC���������邩�ǂ������肷��B
     * 
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C�����̗�O
     */
    private void judgeModifyWriteMethod(final CtMethod method)
        throws CannotCompileException
    {
        // write(byte[]) �̂Ƃ��ɂ́AmodifyWriteMethod()���Ăяo���A
        // write(byte[], int, int) �̂Ƃ��ɂ́AmodifyWriteMethod2()���Ăяo��
        String methodName = method.getName();
        if ("write".equals(methodName))
        {
            if ("([B)V".equals(method.getSignature()))
            {
                modifyWriteMethod(method);
                logModifiedMethod("FileMonitorConverter", method);
            }
            else if ("([BII)V".equals(method.getSignature()))
            {
                modifyWriteMethod2(method);
                logModifiedMethod("FileMonitorConverter", method);
            }
        }
    }

    /**
     * read ���\�b�h���Ăяo���ꂽ��ɁA
     * FileInputStreamMonitor#postProcess ���Ăяo���悤�ɁA
     * �R�[�h�ɏC����������B
     * 
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C�����̗�O
     */
    private void modifyReadMethod(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(INPUSTREAM_MONITOR_NAME + ".postProcess($_);");
    }

    /**
     * read ���\�b�h���Ăяo���ꂽ��ɁA
     * FileOutputStreamMonitor#postProcess ���Ăяo���悤�ɁA
     * �R�[�h�ɏC����������B
     * 
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C�����̗�O
     * 
     */
    private void modifyWriteMethod(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(OUTPUSTREAM_MONITOR_NAME + ".postProcess($1.length);");
    }

    /**
     * FileOutputStreamMonitor �ɏC����������B
     * 
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C�����̗�O
     * 
     */
    private void modifyWriteMethod2(final CtMethod method)
        throws CannotCompileException
    {
        method.insertAfter(OUTPUSTREAM_MONITOR_NAME + ".postProcess($3);");
    }
}
