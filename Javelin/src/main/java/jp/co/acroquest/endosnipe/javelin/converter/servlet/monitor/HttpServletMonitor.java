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
package jp.co.acroquest.endosnipe.javelin.converter.servlet.monitor;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.event.HttpStatusEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ServletJavelin�����s����
 * 
 * @author yamasaki
 * 
 */
public class HttpServletMonitor
{
    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private HttpServletMonitor()
    {
        // Do Nothing.
    }

    /** �z�X�g������͂�������ԍ� */
    private static final int ARGS_HOST_NUM = 0;

    /** �|�[�g�ԍ�����͂�������ԍ� */
    private static final int ARGS_PORT_NUM = 1;

    /** �R���e�N�X�g�p�X����͂�������ԍ� */
    private static final int ARGS_CONTEXTPATH_NUM = 2;

    /** �T�[�u���b�g�p�X����͂�������ԍ� */
    private static final int ARGS_SERVLETPATH_NUM = 3;

    /** ���\�b�h������͂�������ԍ� */
    private static final int ARGS_METHOD_NUM = 4;

    /** �N�G�����������͂�������ԍ� */
    private static final int ARGS_QUERY_STRING_NUM = 5;

    /** �p�����[�^�̃}�b�v����͂�������ԍ� */
    private static final int ARGS_PARAMETER_MAP_NUM = 6;

    /** �Z�b�V��������͂�������ԍ� */
    private static final int ARGS_SESSION_NUM = 7;
    
    /** �G���[�X�e�[�^�X400�ԑ��\������ */
    private static final int ERROR_STATUS_FOUR_HUNDRED = 400;

    /** �����̐�. */
    private static final int ARGS_NUM = 8;

    /** Javelin�̐ݒ� */
    private static JavelinConfig config__ = new JavelinConfig();

    /**
     * �O����
     * 
     * @param request ���N�G�X�g
     */
    public static void preProcess(HttpRequestValue request)
    {
        try
        {
            CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();
            
            // ThreadLocal����N���X���A���\�b�h�����Ăяo���B �N���X���A���\�b�h�������݂��Ȃ��ꍇ�A 
            // HttpRequest�̃R���e�L�X�g�p�X�A�T�[�u���b�g�p�X���Ăяo���B
            String[] paths = getPaths(request);
            String contextPath = paths[0];
            String servletPath = paths[1];
            
            // �e�m�[�h�Ɠ���̃p�X�Ȃ�΁A��d�ɋL�^���Ȃ��B
            CallTreeNode parent = callTreeRecorder.getCallTreeNode();
            if (parent != null)
            {
                Invocation invocation = parent.getInvocation();
                if (invocation != null 
                    && contextPath.equals(invocation.getClassName())
                    && servletPath.equals(invocation.getMethodName()))
                {
                    parent.count_++;
                    return;
                }
            }
            
            Object[] args = null;

            // log.args��true�̂Ƃ��A��������\������B
            if (config__.isLogArgs())
            {
                args = new Object[ARGS_NUM];
                args[ARGS_HOST_NUM] = request.getRemoteHost();
                args[ARGS_PORT_NUM] = request.getRemotePort();
                args[ARGS_CONTEXTPATH_NUM] = contextPath;
                args[ARGS_SERVLETPATH_NUM] = servletPath;
                args[ARGS_METHOD_NUM] = request.getMethod();
                if (request.getQueryString() == null)
                {
                    args[ARGS_QUERY_STRING_NUM] = "";
                }
                else
                {
                    args[ARGS_QUERY_STRING_NUM] = request.getQueryString();
                }
                if (request.getCharacterEncoding() == null)
                {
                    // Character Encoding���w�肳��Ă��Ȃ��ꍇ�A
                    // �����������������Ă��܂����߁A�p�����[�^�}�b�v�͎擾���Ȃ��B
                    args[ARGS_PARAMETER_MAP_NUM] = "Parameter map is unavailable.";
                }
                else
                {
                    args[ARGS_PARAMETER_MAP_NUM] = request.getParameterMap();
                }
            }
            

            StackTraceElement[] stacktrace = null;
            if (config__.isLogStacktrace())
            {
                stacktrace = ThreadUtil.getCurrentStackTrace();
            }
            
            StatsJavelinRecorder.preProcess(contextPath, 
                                              servletPath, 
                                              args, 
                                              stacktrace, 
                                              config__,
                                              true, 
                                              true);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    /**
     * �㏈���B
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public static void postProcess(HttpRequestValue request, HttpResponseValue response)
    {
        String[] paths = getPaths(request);
        String contextPath = paths[0];
        String servletPath = paths[1];
        
        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();
        CallTreeNode node = callTreeRecorder.getCallTreeNode();
        
        if (node != null)
        {
            if (node.count_ > 0)
            {
                node.count_--;
                return;
            }
        }
        
        try
        {
            int status = response.getStatus();
            if (status >= ERROR_STATUS_FOUR_HUNDRED && config__.isHttpStatusError())
            {
                HttpStatusEvent event =
                                        new HttpStatusEvent(contextPath, servletPath, status,
                                                            response.getThrowable(),
                                                            config__.getTraceDepth());
                StatsJavelinRecorder.addEvent(event);
                
                Invocation invocation = node.getInvocation();
                invocation.addHttpStatusCount(String.valueOf(status));
            }
            
            Object returnValue = null;
            if (config__.isLogReturn())
            {
                returnValue = status;
            }

              StatsJavelinRecorder.postProcess(contextPath, 
                                                 servletPath, 
                                                 returnValue, 
                                                 config__,
                                                 true);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    /**
     * �㏈��(��O)�B
     * @param request ���N�G�X�g
     * @param throwable ��O�B
     */
    public static void postProcessNG(
            HttpRequestValue request,
            final Throwable  throwable)
    {
        String[] paths = getPaths(request);
        String contextPath = paths[0];
        String servletPath = paths[1];

        // Invocation�ɗ�O���L�^����Ȃ��ꍇ�ł����������A�Ƃ����L�^�͍s��
        CallTreeRecorder callTreeRecorder = CallTreeRecorder.getInstance();
        CallTreeNode node = callTreeRecorder.getCallTreeNode();
        
        if (node != null)
        {
            if (node.count_ > 0)
            {
                node.count_--;
                return;
            }
        }
        
        CallTree callTree = callTreeRecorder.getCallTree();
        if (node != null && callTree != null)
        {
            if (throwable != null && callTree.getCause() == throwable)
            {
                Invocation invocation = node.getInvocation();
                invocation.addThrowable(throwable);
            }
        }

        try
        {
            StatsJavelinRecorder.postProcess(contextPath, 
                                               servletPath, 
                                               throwable, 
                                               config__, 
                                               true);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    /**
     * HTTP���N�G�X�g����N���X���A���\�b�h���ƂȂ�p�X�����擾����B
     * 
     * @param request
     * @return
     */
    private static String[] getPaths(final HttpRequestValue request)
    {
        String contextPath;
        String servletPath;
        String pathInfo = request.getPathInfo();
        if (pathInfo == null)
        {

            contextPath = request.getContextPath();
            servletPath = request.getServletPath();
        }
        else
        {
            contextPath = request.getContextPath() + request.getServletPath();
            servletPath = pathInfo;
        }

        // �R���e�L�X�g�p�X����̏ꍇ��"/"�𗘗p����B
        if ("".equals(contextPath))
        {
            contextPath = "/";
        }
        
        String[] paths = {contextPath, servletPath};
        return paths;
    }
}
