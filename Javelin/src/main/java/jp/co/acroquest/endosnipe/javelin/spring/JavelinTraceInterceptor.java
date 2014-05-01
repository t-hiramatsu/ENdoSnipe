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
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * Javelin���O���o�͂��邽�߂�TraceInterceptor�B
 * 
 * @version 0.2
 * @author On Eriguchi (Acroquest), Tetsu Hayakawa (Acroquest)
 */
public class JavelinTraceInterceptor implements MethodInterceptor
{
    private static final long serialVersionUID = 6661781313519708185L;

    /**
     * ���\�b�h�Ăяo������Javelin���O�̐擪�ɏo�͂��鎯�ʎq�B
     */
    private static final String CALL = "Call  ";

    /**
     * ���\�b�h�߂莞��Javelin���O�̐擪�ɏo�͂��鎯�ʎq�B
     */
    private static final String RETURN = "Return";

    /**
     * ��Othrow����Javelin���O�̐擪�ɏo�͂��鎯�ʎq�B
     */
    private static final String THROW = "Throw ";

    /**
     * ��Ocatch����Javelin���O�̐擪�ɏo�͂��鎯�ʎq�B
     */
    private static final String CATCH = "Catch ";

    private static final int CALLER_STACKTRACE_INDEX = 3;

    /**
     * ���s�����B
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * ���O�̋�؂蕶���B
     */
    private static final String DELIM = ",";

    /**
     * �X���b�h���̋�؂蕶��
     */
    private static final String THREAD_DELIM = "@";

    /**
     * ���O�o�͂��鎞���̃t�H�[�}�b�g�B
     */
    private static final String TIME_FORMAT_STR = "yyyy/MM/dd HH:mm:ss.SSS";

    /**
     * Javelin���O�ɏo�͂��鎞���f�[�^���`�p�t�H�[�}�b�^�B
     */
    private final ThreadLocal<SimpleDateFormat> timeFormat_ = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat(TIME_FORMAT_STR);
        }
    };

    /**
     * ���\�b�h�̌Ăяo�����I�u�W�F�N�g��\��������B
     */
    private final ThreadLocal<String> callerLog_ = new ThreadLocal<String>() {
        @Override
        protected synchronized String initialValue()
        {
            return "<unknown>,<unknown>,0";
        }
    };

    /**
     * Javelin���O�o�͗p���K�[�B
     */
    private static Logger logger__ = Logger.getLogger(JavelinTraceInterceptor.class);

    /**
     * �ݒ�
     */
    private final JavelinConfig config_ = new JavelinConfig();

    /**
     * ��O�������L�^����e�[�u���B �L�[�ɃX���b�h�̎��ʎq�A�l�ɗ�O�I�u�W�F�N�g������B ��O�����������炱�̃}�b�v�ɂ��̗�O��o�^���A
     * �L���b�`���ꂽ���_�ō폜����B
     */
    private static Map<String, Throwable> exceptionMap__ =
            Collections.synchronizedMap(new HashMap<String, Throwable>());

    /** �ݒ�l��W���o�͂ɏo�͂�����true */
    private boolean isPrintConfig_ = false;

    /**
     * Javelin���O�o�͗p��invoke���\�b�h�B
     * 
     * ���ۂ̃��\�b�h�Ăяo�������s����O��ŁA �Ăяo���ƕԋp�̏ڍ׃��O���AJavelin�`���ŏo�͂���B
     * 
     * ���s���ɗ�O�����������ꍇ�́A���̏ڍׂ����O�o�͂���B
     * 
     * @param invocation
     *            �C���^�[�Z�v�^�ɂ���Ď擾���ꂽ�A�Ăяo�����\�b�h�̏��
     * @return invocation�����s�����Ƃ��̖߂�l
     * @throws Throwable
     *             invocation�����s�����Ƃ��ɔ���������O
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

        StringBuffer methodCallBuff = new StringBuffer(256);

        // �Ăяo������擾�B
        String calleeClassName = invocation.getMethod().getDeclaringClass().getName();
        String calleeMethodName = invocation.getMethod().getName();

        String objectID = Integer.toHexString(System.identityHashCode(invocation.getThis()));
        String modifiers = Modifier.toString(invocation.getMethod().getModifiers());

        // �Ăяo�������擾�B
        String currentCallerLog = this.callerLog_.get();

        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        String threadClassName = currentThread.getClass().getName();
        String threadID = Integer.toHexString(System.identityHashCode(currentThread));
        String threadInfo = threadName + THREAD_DELIM + threadClassName + THREAD_DELIM + threadID;

        // ���\�b�h�Ăяo�����ʕ��������B
        methodCallBuff.append(calleeMethodName);
        methodCallBuff.append(DELIM);
        methodCallBuff.append(calleeClassName);
        methodCallBuff.append(DELIM);
        methodCallBuff.append(objectID);

        // �Ăяo����̃��O���A
        // ���񃍃O�o�͎��̌Ăяo�����Ƃ��Ďg�p���邽�߂ɕۑ�����B
        this.callerLog_.set(methodCallBuff.toString());

        methodCallBuff.append(DELIM);
        methodCallBuff.append(currentCallerLog);
        methodCallBuff.append(DELIM);
        methodCallBuff.append(modifiers);
        methodCallBuff.append(DELIM);
        methodCallBuff.append(threadInfo);
        //		methodCallBuff.append(NEW_LINE);

        // Call �ڍ׃��O�����B
        StringBuffer callDetailBuff = createCallDetail(methodCallBuff, invocation);

        // Call ���O�o�́B
        logger__.debug(callDetailBuff);

        // ���ۂ̃��\�b�h�Ăяo�������s����B
        // ���s���ɗ�O������������A���̏ڍ׃��O���o�͂���B
        Object ret = null;
        try
        {
            // ���\�b�h�Ăяo���B
            ret = invocation.proceed();
        }
        catch (Throwable cause)
        {
            // Throw�ڍ׃��O�����B
            StringBuffer throwBuff =
                    createThrowCatchDetail(THROW, calleeMethodName, calleeClassName, objectID,
                                           threadInfo, cause);

            // Throw ���O�o�́B
            logger__.debug(throwBuff);

            // ��O�������L�^����B
            exceptionMap__.put(threadInfo, cause);

            // ��O���X���[���A�I������B
            throw cause;
        }

        // ���̃X���b�h�ŁA���O�ɗ�O���������Ă��������m�F����B
        // �������Ă��Ă����ɂ��ǂ蒅�����̂ł���΁A���̎��_�ŗ�O��
        // catch���ꂽ�Ƃ������ƂɂȂ�̂ŁACatch���O���o�͂���B
        boolean isExceptionThrowd = exceptionMap__.containsKey(threadInfo);
        if (isExceptionThrowd == true)
        {
            // �������Ă�����O�I�u�W�F�N�g���}�b�v������o���B�i���폜����j
            Throwable exception = exceptionMap__.remove(threadInfo);

            // Catch�ڍ׃��O�����B
            StringBuffer throwBuff =
                    createThrowCatchDetail(CATCH, calleeMethodName, calleeClassName, objectID,
                                           threadInfo, exception);

            // Catch ���O�o�́B
            logger__.debug(throwBuff);
        }

        // Return�ڍ׃��O�����B
        StringBuffer returnDetailBuff = createReturnDetail(methodCallBuff, ret);

        // Return���O�o�́B
        logger__.debug(returnDetailBuff);

        this.callerLog_.set(currentCallerLog);

        // invocation�����s�����ۂ̖߂�l��Ԃ��B
        return ret;
    }

    /**
     * ���\�b�h�Ăяo���i���ʎqCall�j�̏ڍׂȃ��O�𐶐�����B
     * 
     * @param methodCallBuff
     *            ���\�b�h�Ăяo���̕�����
     * @param invocation
     *            ���\�b�h�Ăяo���̏��
     * @return ���\�b�h�Ăяo���̏ڍׂȃ��O
     */
    private StringBuffer createCallDetail(final StringBuffer methodCallBuff,
            final MethodInvocation invocation)
    {
        String timeStr = (this.timeFormat_.get()).format(new Date());
        StringBuffer callDetailBuff = new StringBuffer(512);

        callDetailBuff.append(CALL);
        callDetailBuff.append(DELIM);
        callDetailBuff.append(timeStr);
        callDetailBuff.append(DELIM);
        callDetailBuff.append(methodCallBuff);
        callDetailBuff.append(NEW_LINE);

        // �����̃��O�𐶐�����B
        if (this.config_.isLogArgs() == true)
        {
            callDetailBuff.append("<<javelin.Args_START>>" + NEW_LINE);
            Object[] args = invocation.getArguments();
            if (args != null && args.length > 0)
            {
                for (int i = 0; i < args.length; ++i)
                {
                    callDetailBuff.append("    args[");
                    callDetailBuff.append(i);
                    callDetailBuff.append("] = ");
                    callDetailBuff.append("[");
                    callDetailBuff.append(args[i].getClass().getName());
                    callDetailBuff.append("] ");
                    callDetailBuff.append(args[i]);
                    callDetailBuff.append(NEW_LINE);
                }
            }
            callDetailBuff.append("<<javelin.Args_END>>" + NEW_LINE);
        }

        // �X�^�b�N�g���[�X�̃��O�𐶐�����B
        if (this.config_.isLogStacktrace() == true)
        {
            callDetailBuff.append("<<javelin.StackTrace_START>>" + NEW_LINE);
            Throwable th = new Throwable();
            StackTraceElement[] stackTraceElements = th.getStackTrace();
            for (int index = CALLER_STACKTRACE_INDEX; index < stackTraceElements.length; index++)
            {
                callDetailBuff.append("    at ");
                callDetailBuff.append(stackTraceElements[index]);
                callDetailBuff.append(NEW_LINE);
            }

            callDetailBuff.append("<<javelin.StackTrace_END>>" + NEW_LINE);
        }

        if (invocation.getThis() instanceof Throwable)
        {
            callDetailBuff.append("<<javelin.Exception>>" + NEW_LINE);
        }
        return callDetailBuff;
    }

    /**
     * ���\�b�h�̖߂�i���ʎqReturn�j�̏ڍׂȃ��O�𐶐�����B
     * 
     * @param methodCallBuff
     *            ���\�b�h�Ăяo���̕�����
     * @param ret
     *            ���\�b�h�̖߂�l
     * @return ���\�b�h�̖߂�̏ڍׂȃ��O
     */
    private StringBuffer createReturnDetail(final StringBuffer methodCallBuff, final Object ret)
    {
        StringBuffer returnDetailBuff = new StringBuffer(512);
        String returnTimeStr = (this.timeFormat_.get()).format(new Date());
        returnDetailBuff.append(RETURN);
        returnDetailBuff.append(DELIM);
        returnDetailBuff.append(returnTimeStr);
        returnDetailBuff.append(DELIM);
        returnDetailBuff.append(methodCallBuff);

        // �߂�l�̃��O�𐶐�����B
        if (this.config_.isLogReturn())
        {
            returnDetailBuff.append(NEW_LINE);
            returnDetailBuff.append("<<javelin.Return_START>>" + NEW_LINE);
            returnDetailBuff.append("    ");
            returnDetailBuff.append(ret);
            returnDetailBuff.append(NEW_LINE);
            returnDetailBuff.append("<<javelin.Return_END>>" + NEW_LINE);
        }
        return returnDetailBuff;
    }

    /**
     * ��O�����i���ʎqThrow�j�A�܂��͗�O�L���b�`�iCatch�j�̏ڍ׃��O�𐶐�����B
     * 
     * @param id
     *            ���ʎq�BThrow�܂���Catch
     * @param calleeMethodName
     *            �Ăяo���惁�\�b�h��
     * @param calleeClassName
     *            �Ăяo����N���X��
     * @param objectID
     *            �Ăяo����N���X�̃I�u�W�F�N�gID
     * @param threadInfo
     *            �X���b�h���
     * @param cause
     *            ����������O
     * @return ��O�����̏ڍׂȃ��O
     */
    private StringBuffer createThrowCatchDetail(final String id, final String calleeMethodName,
            final String calleeClassName, final String objectID, final String threadInfo,
            final Throwable cause)
    {
        String throwTimeStr = (this.timeFormat_.get()).format(new Date());
        String throwableID = Integer.toHexString(System.identityHashCode(cause));
        StringBuffer throwBuff = new StringBuffer(512);
        throwBuff.append(id);
        throwBuff.append(DELIM);
        throwBuff.append(throwTimeStr);
        throwBuff.append(DELIM);
        throwBuff.append(cause.getClass().getName());
        throwBuff.append(DELIM);
        throwBuff.append(throwableID);
        throwBuff.append(DELIM);
        throwBuff.append(calleeMethodName);
        throwBuff.append(DELIM);
        throwBuff.append(calleeClassName);
        throwBuff.append(DELIM);
        throwBuff.append(objectID);
        throwBuff.append(DELIM);
        throwBuff.append(threadInfo);
        return throwBuff;
    }

    /**
     * �Ăяo�������L�^����ő匏�����Z�b�g����B
     *
     * @param intervalMax ����
     */
    public void setIntervalMax(final int intervalMax)
    {
        if (this.config_.isSetIntervalMax() == false)
        {
            this.config_.setIntervalMax(intervalMax);
        }
    }

    /**
     * ��O�̔����������L�^����ő匏�����Z�b�g����B
     *
     * @param throwableMax ����
     */
    public void setThrowableMax(final int throwableMax)
    {
        if (this.config_.isSetThrowableMax() == false)
        {
            this.config_.setThrowableMax(throwableMax);
        }
    }

    /**
     * �Ăяo�����𔭕񂷂�ۂ�臒l���Z�b�g����B
     *
     * @param alarmThreshold 臒l�i�~���b�j
     */
    public void setAlarmThreshold(final int alarmThreshold)
    {
        if (this.config_.isSetAlarmThreshold() == false)
        {
            this.config_.setAlarmThreshold(alarmThreshold);
        }
    }

    /**
     * �����̓��e�����O�o�͂��邩�ǂ����ݒ肷��B
     * 
     * @param isLogArgs
     *            ���������O�o�͂���Ȃ�true
     */
    public void setLogArgs(final boolean isLogArgs)
    {
        if (this.config_.isSetLogArgs() == false)
        {
            this.config_.setLogArgs(isLogArgs);
        }
    }

    /**
     * �߂�l�̓��e�����O�o�͂��邩�ǂ����ݒ肷��B
     * 
     * @param isLogReturn
     *            �߂�l�����O�o�͂���Ȃ�true
     */
    public void setLogReturn(final boolean isLogReturn)
    {
        if (this.config_.isSetLogReturn() == false)
        {
            this.config_.setLogReturn(isLogReturn);
        }
    }

    /**
     * ���\�b�h�Ăяo���܂ł̃X�^�b�N�g���[�X�����O�o�͂��邩�ݒ肷��B
     * 
     * @param isLogStackTrace
     *            �X�^�b�N�g���[�X�����O�o�͂���Ȃ�true
     */
    public void setLogStackTrace(final boolean isLogStackTrace)
    {
        if (this.config_.isSetLogStacktrace() == false)
        {
            this.config_.setLogStacktrace(isLogStackTrace);
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
