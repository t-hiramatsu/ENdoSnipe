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
import java.util.Map;

/**
 * HttpRequestValue�N���X
 * @author acroquest
 *
 */
public class HttpRequestValue
{
    /** �p�X��� */
    private String pathInfo_;
    
    /** �R���e�L�X�g�p�X */
    private String contextPath_;
    
    /** �T�[�u���b�g�p�X */
    private String servletPath_;

    /** �����[�g�z�X�g */
    private String remoteHost_;
    
    /** �����[�g�|�[�g */
    private int    remotePort_;
    
    /** ���\�b�h�� */
    private String method_;
    
    /** �N�G���X�g�����O */
    private String queryString_;
    
    /** �G���R�[�h������ */
    private String characterEncoding_;
    
    /** �p�����[�^��Map */
    @SuppressWarnings("rawtypes")
    private Map    parameterMap_;
    
    /**
     * �p�X�����擾���܂��B
     * @return �p�X���
     */
    public String getPathInfo()
    {
        return pathInfo_;
    }
    
    /**
     * �p�X����ݒ肵�܂��B
     * @param pathInfo �p�X���
     */
    public void setPathInfo(String pathInfo)
    {
        pathInfo_ = pathInfo;
    }
    
    /**
     * �R���e�L�X�g�p�X���擾���܂��B
     * @return �R���e�L�X�g�p�X
     */
    public String getContextPath()
    {
        return contextPath_;
    }
    
    /**
     * �R���e�L�X�g�p�X��ݒ肵�܂��B
     * @param contextPath �R���e�L�X�g�p�X
     */
    public void setContextPath(String contextPath)
    {
        contextPath_ = contextPath;
    }
    
    /**
     * �T�[�u���b�g�p�X���擾���܂��B
     * @return �T�[�u���b�g�p�X
     */
    public String getServletPath()
    {
        return servletPath_;
    }
    
    /**
     * �T�[�u���b�g�p�X��ݒ肵�܂��B
     * @param servletPath �T�[�u���b�g�p�X
     */
    public void setServletPath(String servletPath)
    {
        servletPath_ = servletPath;
    }

    /**
     * �����[�g�z�X�g�����擾���܂��B
     * @return �����[�g�z�X�g��
     */
    public String getRemoteHost()
    {
        return remoteHost_;
    }
    
    /**
     * �����[�g�z�X�g����ݒ肵�܂��B
     * @param remoteHost �����[�g�z�X�g��
     */
    public void setRemoteHost(String remoteHost)
    {
        remoteHost_ = remoteHost;
    }

    /**
     * �����[�g�|�[�g�ԍ����擾���܂��B
     * @return �����[�g�|�[�g�ԍ�
     */
    public int getRemotePort()
    {
        return remotePort_;
    }
    
    /**
     * �����[�g�|�[�g�ԍ���ݒ肵�܂��B
     * @param remotePort �����[�g�|�[�g�ԍ�
     */
    public void setRemotePort(int remotePort)
    {
        remotePort_ = remotePort;
    }
    
    /**
     * ���\�b�h�����擾���܂��B
     * @return ���\�b�h��
     */
    public String getMethod()
    {
        return method_;
    }
    
    /**
     * ���\�b�h����ݒ肵�܂��B
     * @param method ���\�b�h��
     */
    public void setMethod(String method)
    {
        method_ = method;
    }
    
    /**
     * �N�G���X�g�����O���擾���܂��B
     * @return �N�G���X�g�����O
     */
    public String getQueryString()
    {
        return queryString_;
    }
    
    /**
     * �N�G���X�g�����O��ݒ肵�܂��B
     * @param queryString �N�G���X�g�����O
     */
    public void setQueryString(String queryString)
    {
        queryString_ = queryString;
    }
    
    /**
     * �G���R�[�h��������擾���܂��B
     * @return �G���R�[�h������
     */
    public String getCharacterEncoding()
    {
        return characterEncoding_;
    }
    
    /**
     * �G���R�[�h�������ݒ肵�܂��B
     * @param characterEncoding �G���R�[�h������
     */
    public void setCharacterEncoding(String characterEncoding)
    {
        characterEncoding_ = characterEncoding;
    }
    
    /**
     * �p�����[�^��Map���擾���܂��B
     * @return �p�����[�^��Map
     */
    @SuppressWarnings("rawtypes")
    public Map getParameterMap()
    {
        return parameterMap_;
    }
    
    /**
     * �p�����[�^��Map��ݒ肵�܂��B
     * @param parameterMap �p�����[�^��Map
     */
    @SuppressWarnings("rawtypes")
    public void setParameterMap(Map parameterMap)
    {
        parameterMap_ = parameterMap;
    }
}
