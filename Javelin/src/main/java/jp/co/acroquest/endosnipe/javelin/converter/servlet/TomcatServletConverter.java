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
package jp.co.acroquest.endosnipe.javelin.converter.servlet;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpServletMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * ServletJavelin
 * 
 * @author yamasaki
 */
public class TomcatServletConverter extends AbstractConverter
{
    /** HttpServlet���j�^�̃N���X���� */
    private static final String SERVLET_MONITOR_NAME = HttpServletMonitor.class.getCanonicalName();

    private static final String SERVLET_RESPONSE_CLASS = "org.apache.catalina.connector.Response";

    private static final String SERVLET_REQUEST_CLASS = "org.apache.catalina.connector.Request";

    /** Throwable��CtClass�B */
    private CtClass             throwableClass_;

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        try
        {
            throwableClass_ = ClassPool.getDefault().get(Throwable.class.getCanonicalName());
        }
        catch (NotFoundException nfe)
        {
            // �������Ȃ��B
            SystemLogger.getInstance().warn(nfe);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertImpl()
        throws CannotCompileException,
            IOException
    {
        CtClass ctClass = getCtClass();

        // service���\�b�h���Ăяo���Aservice���\�b�h�����݂���ꍇ�̂݁A
        // ���\�b�h�Ƀ��O�o�̓R�[�h�𖄂ߍ��ށB
        List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : behaviorList)
        {
            convertMethod(ctBehavior);
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

    /**
     * �Ώۃ��\�b�h��ϊ�����B
     * 
     * @param ctMethod �ϊ��Ώۃ��\�b�h
     * @throws CannotCompileException �R���p�C�����s��
     */
    private void convertMethod(final CtBehavior ctMethod)
        throws CannotCompileException
    {

        try
        {
            CtClass[] parameterTypes = ctMethod.getParameterTypes();
            if (parameterTypes.length < 2)
            {
                return;
            }
            ctMethod.insertBefore(BEFORE);
            ctMethod.insertAfter(AFTER);
            ctMethod.addCatch(NG, throwableClass_);

            // �������ʂ����O�ɏo�͂���B
            logModifiedMethod("TomcatHttpServletConverter", ctMethod);
        }
        catch (NotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    private static final String BEFORE = "if ($1 instanceof "
     + SERVLET_REQUEST_CLASS
     + ") {"
     + SERVLET_REQUEST_CLASS
     + " httpRequest = ("
     + SERVLET_REQUEST_CLASS
     + ")$1;"
     + "jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue requestValue="
     + "    new jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue();"
     + "requestValue.setPathInfo(httpRequest.getPathInfo());"
     + "requestValue.setContextPath(httpRequest.getContextPath());"
     + "requestValue.setServletPath(httpRequest.getServletPath());"
     + "requestValue.setRemoteHost(httpRequest.getRemoteHost());"
     + "requestValue.setRemotePort(httpRequest.getRemotePort());"
     + "requestValue.setMethod(httpRequest.getMethod());"
     + "requestValue.setQueryString(httpRequest.getQueryString());"
     + "requestValue.setCharacterEncoding(httpRequest.getCharacterEncoding());"
     + "if (requestValue.getCharacterEncoding() != null) {"
     + "    requestValue.setParameterMap(httpRequest.getParameterMap()); "
     + "}" + SERVLET_MONITOR_NAME
     + ".preProcess(requestValue);" + "}";

    private static final String AFTER  = "if ($1 instanceof "
     + SERVLET_REQUEST_CLASS
     + ") {"
     + SERVLET_REQUEST_CLASS
     + " httpRequest = ("
     + SERVLET_REQUEST_CLASS
     + ")$1;"
     + "jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue requestValue="
     + "    new jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue();"
     + "requestValue.setPathInfo(httpRequest.getPathInfo());"
     + "requestValue.setContextPath(httpRequest.getContextPath());"
     + "requestValue.setServletPath(httpRequest.getServletPath());"
     + SERVLET_RESPONSE_CLASS
     + " httpResponse = ("
     + SERVLET_RESPONSE_CLASS
     + ")$2;"
     +"jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpResponseValue responseValue="
     + "    new jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpResponseValue();"
     + "responseValue.setContentType(httpResponse.getContentType());"
     + "responseValue.setStatus(httpResponse.getStatus());"
     + "responseValue.setThrowable((java.lang.Throwable)"
     + "httpRequest.getAttribute(\"javax.servlet.error.exception\"));"
     + SERVLET_MONITOR_NAME
     + ".postProcess(requestValue, responseValue);"
     + "}";

    private static final String NG = "if ($1 instanceof "
     + SERVLET_REQUEST_CLASS
     + ") {"
     + SERVLET_REQUEST_CLASS
     + " httpRequest = ("
     + SERVLET_REQUEST_CLASS
     + ")$1;"
     + "jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue requestValue="
     + "    new jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor.HttpRequestValue();"
     + "requestValue.setPathInfo(httpRequest.getPathInfo());"
     + "requestValue.setContextPath(httpRequest.getContextPath());"
     + "requestValue.setServletPath(httpRequest.getServletPath());"
     + SERVLET_MONITOR_NAME
     + ".postProcessNG(requestValue, $e);" + "}"
     + "throw $e;";
}
