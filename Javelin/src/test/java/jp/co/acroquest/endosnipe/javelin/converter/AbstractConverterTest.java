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

import java.lang.reflect.Field;
import java.util.List;

import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import junit.framework.TestCase;

/**
 * AbstractConverter�̃e�X�g�R�[�h
 * AbstractConverter���p������TestExtendConverter�𗘗p���āA
 * �������s���B
 * @author fujii
 *
 */
public class AbstractConverterTest extends TestCase
{

    /** Javelin�ݒ�t�@�C���̃p�X */
    private static final String JAVELIN_CONFIG_PATH = "test/common/conf";

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp()
    {
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinConfig config = new JavelinConfig(JAVELIN_CONFIG_PATH);
        SystemLogger.initSystemLog(config);
    }

    /**
     * [����] 1-3-7 GetMatcheDeclaredBehavior�̃e�X�g�B <br />
     * �E���\�b�h�������Ȃ��N���X�ɑ΂��āAAbstractConverter#getMatchDeclaredBehavior��<br />
     *  ���s����B<br />
     * ����̃��X�g���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testGetMatcheDeclaredBehavior_EmptyMethod()
        throws Exception
    {

        // ����
        TestExtendConverter converter = new TestExtendConverter();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.converter.TestEmptyMethod");

        Field field = AbstractConverter.class.getDeclaredField("ctClass_");
        field.setAccessible(true);
        field.set(converter, ctClass);

        // ���s
        List<CtBehavior> list = converter.getMethodList();

        // ����
        assertEquals(0, list.size());
    }

    /**
     * [����] 1-3-8 GetMatcheDeclaredBehavior�̃e�X�g�B <br />
     * �EAbstract���\�b�h�݂̂����N���X�ɑ΂��āAAbstractConverter#getMatchDeclaredBehavior��<br />
     *  ���s����B<br />
     * ����̃��X�g���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testGetMatcheDeclaredBehavior_AbstractMethod()
        throws Exception
    {

        // ����
        TestExtendConverter converter = new TestExtendConverter();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.converter.TestAbstractMethod");

        Field field = AbstractConverter.class.getDeclaredField("ctClass_");
        field.setAccessible(true);
        field.set(converter, ctClass);

        // ���s
        List<CtBehavior> list = converter.getMethodList();

        // ����
        assertEquals(0, list.size());
    }

    /**
     * [����] 1-3-9 GetMatcheDeclaredBehavior�̃e�X�g�B <br />
     * �ENative���\�b�h�݂̂����N���X�ɑ΂��āAAbstractConverter#getMatchDeclaredBehavior��<br />
     *  ���s����B<br />
     * ����̃��X�g���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testGetMatcheDeclaredBehavior_NativeMethod()
        throws Exception
    {

        // ����
        TestExtendConverter converter = new TestExtendConverter();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.converter.TestNativeMethod");

        Field field = AbstractConverter.class.getDeclaredField("ctClass_");
        field.setAccessible(true);
        field.set(converter, ctClass);

        // ���s
        List<CtBehavior> list = converter.getMethodList();

        // ����
        assertEquals(0, list.size());
    }
}
