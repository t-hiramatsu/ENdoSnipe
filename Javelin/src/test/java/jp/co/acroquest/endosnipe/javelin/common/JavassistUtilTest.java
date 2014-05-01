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
package jp.co.acroquest.endosnipe.javelin.common;

import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.testutil.PrivateAccessor;
import jp.co.acroquest.test.util.JavelinTestUtil;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;
import junit.framework.TestCase;

/**
 * JavassistUtil�̃e�X�g�N���X�B
 * @author fujii
 *
 */
public class JavassistUtilTest extends TestCase
{
    /** Javelin�ݒ�t�@�C���̃p�X */
    private static final String JAVELIN_CONFIG_PATH = "/common/conf/javelin.properties";

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinTestUtil.camouflageJavelinConfig(getClass(), JAVELIN_CONFIG_PATH);
        JavelinConfig config = new JavelinConfig();
        SystemLogger.initSystemLog(config);

        // �p���֌W�̃L���b�V�����N���A����
        PrivateAccessor.setField(JavassistUtil.class, "inheritedMap__",
                                 new ConcurrentHashMap<String, Boolean>());
        PrivateAccessor.setField(JavassistUtil.class, "maximumDepth__",
                                 config.getInheritanceDepth());
    }

    /**
     * [����] 1-3-1 isInherited�̃e�X�g�B <br />
     * �EinheritedClassName���p�����Ă���N���X�ɑ΂��āA<br />
     *  isInherited���\�b�h�����s����B<br />
     * ��true���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testIsInherited_ineritate()
        throws Exception
    {

        // ����
        // �N���X���Ăяo���B
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.common.TestInherit1");

        // ���s
        boolean isInherited =
                JavassistUtil.isInherited(ctClass, pool,
                                          "jp.co.acroquest.endosnipe.javelin.common.TestRootClass");

        // ����
        assertTrue(isInherited);

    }

    /**
     * [����] 1-3-2 isInherited�̃e�X�g�B <br />
     * �E���݂��Ȃ��N���X�̖��O��inheritedClassName�ɂ��āA<br />
     *  isInherited���\�b�h�����s����B<br />
     * ��false���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testIsInherited_NotExistClass()
        throws Exception
    {
        // ����
        // �N���X���Ăяo���B
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.common.TestInherit1");

        // ���s
        boolean isInherited =
                JavassistUtil.isInherited(ctClass, pool,
                                          "jp.co.acroquest.endosnipe.javelin.common.NotExistClass");

        // ����
        assertFalse(isInherited);

    }

    /**
     * [����] 1-3-5 isInherited�̃e�X�g�B <br />
     * �EinheritedClassName��3�K�w���Ōp�����Ă���N���X�ɑ΂��āA<br />
     *  isInherited���\�b�h�����s����B<br />
     * ��true���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testIsInherited_MaxDepth()
        throws Exception
    {

        // ����
        // �N���X���Ăяo���B
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.common.TestInherit3");

        // ���s
        boolean isInherited =
                JavassistUtil.isInherited(ctClass, pool,
                                          "jp.co.acroquest.endosnipe.javelin.common.TestRootClass");
        System.out.println(new JavelinConfig().getInheritanceDepth());

        // ����
        assertTrue(isInherited);
    }

    /**
     * [����] 1-3-6 isInherited�̃e�X�g�B <br />
     * �EinheritedClassName��4�K�w���Ōp�����Ă���N���X�ɑ΂��āA<br />
     *  isInherited���\�b�h�����s����B<br />
     * ��false���Ԃ�B<br />
     * 
     * @throws Exception ��O
     */
    public void testIsInherited_OverDepth()
        throws Exception
    {

        // ����
        // �N���X���Ăяo���B
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("jp.co.acroquest.endosnipe.javelin.common.TestInherit4");

        // ���s
        boolean isInherited =
                JavassistUtil.isInherited(ctClass, pool,
                                          "jp.co.acroquest.endosnipe.javelin.common.TestRootClass");

        // ����
        assertFalse(isInherited);
    }

}
