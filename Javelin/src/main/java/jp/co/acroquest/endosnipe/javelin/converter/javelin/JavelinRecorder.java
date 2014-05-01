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

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.converter.util.ConverterUtil;

/**
 * Javelin���O���L�^����
 * 
 * @author eriguchi
 * 
 */
public class JavelinRecorder
{
    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private JavelinRecorder()
    {
        // Do Nothing.
    }

    /** Javelin�̐ݒ�t�@�C�� */
    private static JavelinConfig config__ = new JavelinConfig();

    /** �N���X���̏ȗ����̃t���O */
    private static boolean isSimplification__ = false;

    static
    {
        isSimplification__ = config__.isClassNameSimplify();
    }

    /**
     * �O�����B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     */
    public static void preProcess(String className, String methodName, final Object[] args)
    {
        try
        {
            if (isSimplification__)
            {
                className = ConverterUtil.toSimpleName(className);
                methodName = ConverterUtil.toSimpleName(methodName);
            }

            Object[] argsToRecord = null;
            if (config__.isLogArgs())
            {
                argsToRecord = args;
            }

            StatsJavelinRecorder.preProcess(className, methodName, argsToRecord, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���i�{�����������j�B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param retValue �߂�l�B
     */
    public static void postProcessOK(String className, String methodName, final Object retValue)
    {
        try
        {
            if (isSimplification__)
            {
                className = ConverterUtil.toSimpleName(className);
                methodName = ConverterUtil.toSimpleName(methodName);
            }

            Object retValueToRecord = null;
            if (config__.isLogReturn())
            {
                retValueToRecord = retValue;
            }

            StatsJavelinRecorder.postProcess(className, methodName, retValueToRecord, config__);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���i�{�������s���j�B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param cause ��O�̌���
     */
    public static void postProcessNG(String className, String methodName, final Throwable cause)
    {
        try
        {
            if (isSimplification__)
            {
                className = ConverterUtil.toSimpleName(className);
                methodName = ConverterUtil.toSimpleName(methodName);
            }
            StatsJavelinRecorder.postProcess(className, methodName, cause, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �ݒ�N���X��ǂݍ���
     * 
     * @param config Javelin�̐ݒ�t�@�C��
     */
    public static void setJavelinConfig(final JavelinConfig config)
    {
        JavelinRecorder.config__ = config;
    }

    /**
     * �X���b�h��ID��ݒ肷��
     * 
     * @param threadId �X���b�hID
     */
    public static void setThreadId(final String threadId)
    {
        StatsJavelinRecorder.setThreadId(threadId);
    }

}
