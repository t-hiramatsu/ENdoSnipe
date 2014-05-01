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
package jp.co.acroquest.endosnipe.javelin.converter.javelin;

import java.util.ArrayList;

import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.conf.ExcludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.IncludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.converter.Converter;
import jp.co.acroquest.endosnipe.javelin.converter.javelin.JavelinBridgeConverter;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.test.util.JavelinTestUtil;

/**
 * S2JaelinBridge�R���o�[�^�̃e�X�g�N���X
 *
 * @author fujii
 */
public class S2JavelinBridgeConverterTest extends DJUnitTestCase
{

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        MockObjectManager.initialize();

        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinTestUtil.camouflageJavelinConfig(getClass(), "/ver4_1_test/conf/javelin.properties");
        JavelinTestUtil.camouflageJavelinConfig("javelin.bytecode.exclude.policy", 0);
        JavelinTestUtil.camouflageJavelinConfig("javelin.call.tree.enable", true);

        JavelinConfig config = new JavelinConfig();
        SystemLogger.initSystemLog(config);
    }

    /**
     * [����] 2-1-1 convert�̃e�X�g�B <br />
     * �Epublic���\�b�h����������JavelinTestPublic�N���X�ɑ΂��āA<br />
     *  S2JavelinBridge�R���o�[�^��K�p����B<br />
     * 
     * ���V�X�e�����O�Ƀ��\�b�h�ϊ���񂪏o�͂���邱�Ƃ�ڎ�����B 
     * 
     * @throws Exception ��O
     */
    public void testConvertImpl_convert_Public()
        throws Exception
    {
        // ����
        // �R���o�[�^�̍쐬
        Converter converter = createConverter();

        // ���O�R�[�h���ߍ��ݑΏۃN���X�̌Ăяo��
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass =
                pool.get("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestPublic");
        String className = "JavelinTestPublic";

        // �����̏����ݒ�
        IncludeConversionConfig includeConfig = new IncludeConversionConfig();
        includeConfig.readConfig("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestPublic,S2Converter");
        java.util.List<ExcludeConversionConfig> excludeConfig =
                new ArrayList<ExcludeConversionConfig>();
        ExcludeConversionConfig exclude = new ExcludeConversionConfig();
        exclude.readConfig("test#test");
        excludeConfig.add(exclude);

        // ���s
        converter.convert(className, null, pool, ctClass, includeConfig, excludeConfig);
        // ����
        // �ڎ�
    }

    /**
     * [����] 2-1-2 convert�̃e�X�g�B <br />
     * �Eprotected���\�b�h����������JavelinTestProtected�N���X�ɑ΂��āA<br />
     *  S2JavelinBridge�R���o�[�^��K�p����B<br />
     * 
     * ���V�X�e�����O�Ƀ��\�b�h�ϊ���񂪏o�͂���邱�Ƃ�ڎ�����B 
     * 
     * @throws Exception ��O
     */
    public void testConvertImpl_convert_Protected()
        throws Exception
    {
        // ����
        // �R���o�[�^�̍쐬
        Converter converter = createConverter();

        // ���O�R�[�h���ߍ��ݑΏۃN���X�̌Ăяo��
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass =
                pool.get("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestProtected");
        String className = "JavelinTestProtected";

        // �����̏����ݒ�
        IncludeConversionConfig includeConfig = new IncludeConversionConfig();
        includeConfig.readConfig("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestProtected,S2Converter");
        java.util.List<ExcludeConversionConfig> excludeConfig =
                new ArrayList<ExcludeConversionConfig>();
        ExcludeConversionConfig exclude = new ExcludeConversionConfig();
        exclude.readConfig("test#test");
        excludeConfig.add(exclude);

        // ���s
        converter.convert(className, null, pool, ctClass, includeConfig, excludeConfig);
        // ����
        // �ڎ�
    }

    /**
     * [����] 2-1-3convert�̃e�X�g�B <br />
     * �Eprivate���\�b�h����������JavelinTestPrivate�N���X�ɑ΂��āA<br />
     *  S2JavelinBridge�R���o�[�^��K�p����B<br />
     * 
     * ���V�X�e�����O�Ƀ��\�b�h�ϊ���񂪏o�͂���Ȃ����Ƃ�ڎ�����B 
     * 
     * @throws Exception ��O
     */
    public void testConvertImpl_convert_Private()
        throws Exception
    {
        // ����
        // �R���o�[�^�̍쐬
        Converter converter = createConverter();

        // ���O�R�[�h���ߍ��ݑΏۃN���X�̌Ăяo��
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass =
                pool.get("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestPrivate");
        String className = "JavelinTestPrivate";

        // �����̏����ݒ�
        IncludeConversionConfig includeConfig = new IncludeConversionConfig();
        includeConfig.readConfig("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestPrivate,S2Converter");
        java.util.List<ExcludeConversionConfig> excludeConfig =
                new ArrayList<ExcludeConversionConfig>();
        ExcludeConversionConfig exclude = new ExcludeConversionConfig();
        exclude.readConfig("test#test");
        excludeConfig.add(exclude);

        // ���s
        converter.convert(className, null, pool, ctClass, includeConfig, excludeConfig);
        // ����
        // �ڎ�
    }

    /**
     * [����] 2-1-4convert�̃e�X�g�B <br />
     * �Estatic���\�b�h�݂̂���������JavelinTestStatic�N���X�ɑ΂��āA<br />
     *  S2JavelinBridge�R���o�[�^��K�p����B<br />
     * 
     * ���V�X�e�����O�Ƀ��\�b�h�ϊ���񂪏o�͂���Ă��邱�Ƃ�ڎ�����B 
     * 
     * @throws Exception ��O
     */
    public void testConvertImpl_convert_Static()
        throws Exception
    {
        // ����
        // �R���o�[�^�̍쐬
        Converter converter = createConverter();

        // ���O�R�[�h���ߍ��ݑΏۃN���X�̌Ăяo��
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass =
                pool.get("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestStatic");
        String className = "JavelinTestStatic";

        // �����̏����ݒ�
        IncludeConversionConfig includeConfig = new IncludeConversionConfig();
        includeConfig.readConfig("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestStatic,S2Converter");
        java.util.List<ExcludeConversionConfig> excludeConfig =
                new ArrayList<ExcludeConversionConfig>();
        ExcludeConversionConfig exclude = new ExcludeConversionConfig();
        exclude.readConfig("test#test");
        excludeConfig.add(exclude);

        // ���s
        converter.convert(className, null, pool, ctClass, includeConfig, excludeConfig);
        // ����
        // �ڎ�
    }

    /**
     * [����] 2-1-5 convert�̃e�X�g�B <br />
     * �Estatic���\�b�h�݂̂���������JavelinTestStatic�N���X�ɑ΂��āA<br />
     *  S2JavelinBridge�R���o�[�^��K�p����B<br />
     * 
     * ���V�X�e�����O�Ƀ��\�b�h�ϊ���񂪏o�͂���Ȃ����Ƃ�ڎ�����B 
     * 
     * @throws Exception ��O
     */
    public void testConvertImpl_convert_Constructor()
        throws Exception
    {
        // ����
        // �R���o�[�^�̍쐬
        Converter converter = createConverter();

        // ���O�R�[�h���ߍ��ݑΏۃN���X�̌Ăяo��
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass =
                pool.get("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestConstructor");
        String className = "JavelinTestConstructor";

        // �����̏����ݒ�
        IncludeConversionConfig includeConfig = new IncludeConversionConfig();
        includeConfig.readConfig("jp.co.acroquest.endosnipe.javelin.converter.s2javelin.JavelinTestConstructor,S2Converter");
        java.util.List<ExcludeConversionConfig> excludeConfig =
                new ArrayList<ExcludeConversionConfig>();
        ExcludeConversionConfig exclude = new ExcludeConversionConfig();
        exclude.readConfig("test#test");
        excludeConfig.add(exclude);

        // ���s
        converter.convert(className, null, pool, ctClass, includeConfig, excludeConfig);
        // ����
        // �ڎ�
    }

    /**
     * CallTreeFull�C�x���g���������邱�Ƃ��m�F����B
     */
    public void testCallTreeFull_臒l����()
    {
        // ����
        CallTreeFullTestSample sample = null; 
        try
        {
            // �T���v���R�[�h�̕ϊ����s���B
            // ver4_1_test/conf/include.conf�ɁA�T���v���R�[�h��include�ݒ���L�q���Ă���B
            sample = (CallTreeFullTestSample)JavelinTestUtil.createMonitoredObject(
                                         "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.S2JavelinBridgeConverter", 
                                         "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.CallTreeFullTestSample");
            JavelinTestUtil.camouflageJavelinConfig("javelin.call.tree.enable", true);
            JavelinTestUtil.camouflageJavelinConfig("javelin.call.tree.max", 500);
            JavelinTestUtil.camouflageJavelinConfig("javelin.autoExcludeThreshold.time", 0);
                }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

        // ���s
        sample.entry(500);

        // ����
        assertCalled(StatsJavelinRecorder.class, "postProcess");
        assertCalled(StatsJavelinRecorder.class, "sendCallTreeEvent");
        assertCalled(StatsJavelinRecorder.class, "addEvent");
        CommonEvent event = (CommonEvent)getArgument(StatsJavelinRecorder.class, "addEvent", 0);
        assertEquals("CallTreeFull", event.getName());
    }

    /**
     * �R���o�[�^���쐬����B
     * 
     * @return S2JavelinBridgeConverter
     */
    private Converter createConverter()
    {
        return new JavelinBridgeConverter();
    }
}
