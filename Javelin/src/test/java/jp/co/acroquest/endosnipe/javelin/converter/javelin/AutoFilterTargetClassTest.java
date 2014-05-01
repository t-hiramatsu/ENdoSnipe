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

import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.test.util.JavelinTestUtil;

/**
 * �v���Ώێ����i���݋@�\�̃e�X�g�N���X
 * (S2JavelinBridgeConverter�̈�@�\�ƂȂ邪�A�ʂ̃N���X�ɐ؂�o���Ď������s��)
 * 
 * @author M.Yoshida
 *
 */
public class AutoFilterTargetClassTest extends DJUnitTestCase
{
    /** Javelin�ݒ�t�@�C���̃p�X */
    private static final String JAVELIN_CONFIG_PATH = "/ver4_1_test/conf/javelin.properties";

    protected void setUp()
        throws Exception
    {
        JavelinTestUtil.camouflageJavelinConfig(getClass(), JAVELIN_CONFIG_PATH);
        SystemLogger.initSystemLog(new JavelinConfig());
        super.setUp();
    }

    /**
     * [�������e]
     * �o�C�g�R�[�h��200�ȏ�A����R�[�h(goto)��3�ȏ�̃��\�b�h�̎��s
     * 
     * [�m�F���e]
     * �EpreProcess�ApostProcess���\�b�h���Ă΂�邱�ƁB(DJUnit�ɂ��m�F)
     * �EJavelin�g���[�X���O�Ń��\�b�h���ϊ�����Ă��邱�Ƃ��m�F����B(���O���ڎ��Ŋm�F)
     */
    public void testConvertFilter1()
    {
        try
        {
            JavelinTestUtil.applyMonitor(
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.S2JavelinBridgeConverter",
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.AutoFilterTarget");
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
        
        AutoFilterTarget.largeAndComplexMethod();
        
        assertCalled(StatsJavelinRecorder.class, "preProcess");
        assertCalled(StatsJavelinRecorder.class, "postProcess");
    }
    
    /**
     * [�������e]
     * �o�C�g�R�[�h��200�����A����R�[�h(goto)��3�ȏ�̃��\�b�h�̎��s
     * 
     * [�m�F���e]
     * �EpreProcess�ApostProcess���\�b�h���Ă΂�邱�ƁB(DJUnit�ɂ��m�F)
     * �EJavelin�g���[�X���O�Ń��\�b�h���ϊ�����Ă��邱�Ƃ��m�F����B(���O���ڎ��Ŋm�F)
     */
    public void testConvertFilter2()
    {
        try
        {
            JavelinTestUtil.applyMonitor(
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.S2JavelinBridgeConverter",
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.AutoFilterTarget");
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
        
        AutoFilterTarget.smallAndComplexMethod();
        
        assertCalled(StatsJavelinRecorder.class, "preProcess");
        assertCalled(StatsJavelinRecorder.class, "postProcess");
    }

    /**
     * [�������e]
     * �o�C�g�R�[�h��200�ȏ�A����R�[�h(goto)��3�����̃��\�b�h�̎��s
     * 
     * [�m�F���e]
     * �EpreProcess�ApostProcess���\�b�h���Ă΂�邱�ƁB(DJUnit�ɂ��m�F)
     * �EJavelin�g���[�X���O�Ń��\�b�h���ϊ�����Ă��邱�Ƃ��m�F����B(���O���ڎ��Ŋm�F)
     */
    public void testConvertFilter3()
    {
        try
        {
            JavelinTestUtil.applyMonitor(
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.S2JavelinBridgeConverter",
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.AutoFilterTarget");
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
        
        AutoFilterTarget.largeAndSimpleMethod();
        
        assertCalled(StatsJavelinRecorder.class, "preProcess");
        assertCalled(StatsJavelinRecorder.class, "postProcess");
    }

    /**
     * [�������e]
     * �o�C�g�R�[�h��200�����A����R�[�h(goto)��3�����̃��\�b�h�̎��s
     * 
     * [�m�F���e]
     * �EpreProcess�ApostProcess���\�b�h���Ă΂�Ȃ����ƁB(DJUnit�ɂ��m�F)
     * �EJavelin�g���[�X���O�Ń��\�b�h���ϊ�����Ă��邱�Ƃ��m�F����B(���O���ڎ��Ŋm�F)
     */
    public void testConvertFilter4()
    {
        try
        {
            JavelinTestUtil.applyMonitor(
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.S2JavelinBridgeConverter",
                "jp.co.acroquest.endosnipe.javelin.converter.s2javelin.AutoFilterTarget");
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
        
        assertNotCalled(StatsJavelinRecorder.class, "preProcess");
        assertNotCalled(StatsJavelinRecorder.class, "postProcess");
    }
    
}
