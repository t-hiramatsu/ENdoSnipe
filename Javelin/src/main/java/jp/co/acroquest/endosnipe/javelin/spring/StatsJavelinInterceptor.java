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
package jp.co.acroquest.endosnipe.javelin.spring;

import java.io.PrintStream;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Component�Ԃ̌Ăяo���֌W��MBean�Ƃ��Č��J���邽�߂�Interceptor�B
 * �ȉ��̏����AMBean�o�R�Ŏ擾���邱�Ƃ��\�B
 * <ol>
 * <li>���\�b�h�̌Ăяo����</li>
 * <li>���\�b�h�̕��Ϗ������ԁi�~���b�P�ʁj</li>
 * <li>���\�b�h�̍Œ��������ԁi�~���b�P�ʁj</li>
 * <li>���\�b�h�̍ŒZ�������ԁi�~���b�P�ʁj</li>
 * <li>���\�b�h�̌Ăяo����</li>
 * <li>��O�̔�����</li>
 * <li>��O�̔�������</li>
 * </ol>
 * �܂��A�ȉ��̏�����dicon�t�@�C���ł̃p�����[�^�Ŏw��\�B
 * <ol>
 * <li>intervalMax:���\�b�h�̏������Ԃ����񕪋L�^���邩�B
 *     �f�t�H���g�l��1000�B</li>
 * <li>throwableMax:��O�̔��������񕪋L�^���邩�B
 *     �f�t�H���g�l��1000�B</li>
 * <li>recordThreshold:�������ԋL�^�p��臒l�B
 *     ���̎��Ԃ��z�������\�b�h�Ăяo���̂݋L�^����B
 *     �f�t�H���g�l��0�B</li>
 * <li>alarmThreshold:�������ԋL�^�p��臒l�B
 *     ���̎��Ԃ��z�������\�b�h�Ăяo���̔�����Viwer�ɒʒm����B
 *     �f�t�H���g�l��1000�B</li>
 * <li>domain:MBean��o�^����ۂɎg�p����h���C���B
 *     ���ۂ̃h���C�����́A[domain�p�����[�^] + [Mbean�̎��]�ƂȂ�B
 *     MBean�̎�ނ͈ȉ��̂��̂�����B
 *     <ol>
 *       <li>container:�S�R���|�[�l���g��ObjectName���Ǘ�����B</li>
 *       <li>component:��̃R���|�[�l���g�Ɋւ���������J����MBean�B</li>
 *       <li>invocation:���\�b�h�Ăяo���Ɋւ���������J����MBean�B</li>
 *     </ol>
 *     </li>
 * </ol>
 * 
 * @version 0.1
 * @author Masanori Yamasaki
 */
public class StatsJavelinInterceptor implements MethodInterceptor
{
    private static final long serialVersionUID = 6661781313519708185L;

    private final JavelinConfig config_ = new JavelinConfig();

    /** �ݒ�l��W���o�͂ɏo�͂�����true */
    private boolean isPrintConfig_ = false;

    /**
     * �Ăяo�����擾�p��invoke���\�b�h�B
     * 
     * ���ۂ̃��\�b�h�Ăяo�������s����O��ŁA
     * ���s�񐔂���s���Ԃ�MBean�ɋL�^����B
     * 
     * ���s���ɗ�O�����������ꍇ�́A
     * ��O�̔����񐔂┭���������L�^����B
     * 
     * @param invocation �C���^�[�Z�v�^�ɂ���Ď擾���ꂽ�A�Ăяo�����\�b�h�̏��
     * @return invocation�����s�����Ƃ��̖߂�l
     * @throws Throwable invocation�����s�����Ƃ��ɔ���������O
     */
    public Object invoke(final MethodInvocation invocation)
        throws Throwable
    {
        // �ݒ�l���o�͂��Ă��Ȃ���Ώo�͂���
        synchronized (this)
        {
            if (this.isPrintConfig_ == false)
            {
                this.isPrintConfig_ = true;
                printConfigValue();
            }
        }

        String className = null;
        String methodName = null;
        try
        {
            // �Ăяo������擾�B
            className = invocation.getMethod().getDeclaringClass().getName();
            methodName = invocation.getMethod().getName();

            StackTraceElement[] stacktrace = null;
            if (this.config_.isLogStacktrace())
            {
                stacktrace = Thread.currentThread().getStackTrace();
            }

            StatsJavelinRecorder.preProcess(className, methodName, invocation.getArguments(),
                                              stacktrace, this.config_, false);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }

        Object ret = null;
        try
        {
            // ���\�b�h�Ăяo���B
            ret = invocation.proceed();
        }
        catch (Throwable cause)
        {
            StatsJavelinRecorder.postProcess(className, methodName, cause, this.config_, false);

            //��O���X���[���A�I������B
            throw cause;
        }

        try
        {
            StatsJavelinRecorder.postProcess(className, methodName, ret, this.config_, false);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }

        return ret;
    }

    /**
     * 
     * @param intervalMax TAT�̍ő�l
     */
    public void setIntervalMax(final int intervalMax)
    {
        if (this.config_.isSetIntervalMax() == false)
        {
            this.config_.setIntervalMax(intervalMax);
        }
    }

    /**
     * 
     * @param throwableMax �ۑ������O�̍ő吔
     */
    public void setThrowableMax(final int throwableMax)
    {
        if (this.config_.isSetThrowableMax() == false)
        {
            this.config_.setThrowableMax(throwableMax);
        }
    }

    /**
     * 
     * @param alarmThreshold �A���[���o�͂�臒l
     */
    public void setAlarmThreshold(final int alarmThreshold)
    {
        if (this.config_.isSetAlarmThreshold() == false)
        {
            this.config_.setAlarmThreshold(alarmThreshold);
        }
    }

    /**
     * 
     * @param javelinFileDir Javelin�̕ۑ��t�@�C��
     */
    public void setJavelinFileDir(final String javelinFileDir)
    {
        if (this.config_.isSetJavelinFileDir() == false)
        {
            this.config_.setJavelinFileDir(javelinFileDir);
        }
    }

    /**
     * 
     * @param isLogArgs �������o�͂��邩�ǂ���
     */
    public void setLogArgs(final boolean isLogArgs)
    {
        if (this.config_.isSetLogArgs() == false)
        {
            this.config_.setLogArgs(isLogArgs);
        }
    }

    /**
     * 
     * @param isLogReturn �߂�l���o�͂��邩�ǂ���
     */
    public void setLogReturn(final boolean isLogReturn)
    {
        if (this.config_.isSetLogReturn() == false)
        {
            this.config_.setLogReturn(isLogReturn);
        }
    }

    /**
     * 
     * @param value Javelin�t�@�C���ɃX�^�b�N�g���[�X���o�͂��邩�ǂ���
     */
    public void setLogStacktrace(final boolean value)
    {
        if (this.config_.isSetLogStacktrace() == false)
        {
            this.config_.setLogStacktrace(value);
        }
    }

    /**
     * 
     * @param endCalleeName �Ăяo����ɂ��閼��
     */
    public void setEndCalleeName(final String endCalleeName)
    {
        if (this.config_.isSetEndCalleeName() == false)
        {
            this.config_.setEndCalleeName(endCalleeName);
        }
    }

    /**
     * 
     * @param threadModel �X���b�h���f��
     */
    public void setThreadModel(final int threadModel)
    {
        if (this.config_.isSetThreadModel() == false)
        {
            this.config_.setThreadModel(threadModel);
        }
    }

    /**
     * �ݒ�l��W���o�͂ɏo�͂���B
     */
    private void printConfigValue()
    {
        PrintStream out = System.out;
        out.println(">>>> Properties related with SpringJavelin");
        out.println("\tjavelin.intervalMax     : " + this.config_.getIntervalMax());
        out.println("\tjavelin.throwableMax    : " + this.config_.getThrowableMax());
        out.println("\tjavelin.alarmThreshold  : " + this.config_.getAlarmThreshold());
        out.println("<<<<");
    }

}
