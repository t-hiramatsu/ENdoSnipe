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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinMessages;

/**
 * JVM�ɃA�^�b�`���邽�߂̃��[�e�B���e�B
 * 
 * @author eriguchi
 *
 */
public class AttachUtil
{
    /** JVM�ւ̎Q�� */
    private static Object       vm__       = null;

    /** heapHisto���\�b�h */
    private static Method       heapHistoMethod__;

    /** detachMethod���\�b�h */
    private static Method       detachMethod__;

    /** attachMethod���\�b�h */
    private static Method attachMethod__;

    /** �q�[�v�q�X�g�O�����擾���ɁA�S�ẴI�u�W�F�N�g��ΏۂƂ��Ďw�肷��p�����[�^�B */
    private static final String HISTO_ALL  = "-all";

    /** �q�[�v�q�X�g�O�����擾���ɁA�������Ă���I�u�W�F�N�g�݂̂�ΏۂƂ��Ďw�肷��p�����[�^�B */
    private static final String HISTO_LIVE = "-live";

    static
    {
        try
        {
            Class<?> clazz = Class.forName("sun.tools.attach.HotSpotVirtualMachine");
            heapHistoMethod__ = clazz.getMethod("heapHisto", new Class[]{String.class});
            attachMethod__ = clazz.getMethod("attach", new Class[]{String.class});
            detachMethod__ = clazz.getMethod("detach", new Class[]{});
        }
        catch (ClassNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (NoSuchMethodException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

    }
    
    /**
     * �C���X�^���X��������邽�߂̃R���X�g���N�^�B
     */
    private AttachUtil()
    {
    }

    /**
     * JVM�ɃA�^�b�`����B
     */
    public static void attach()
    {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String vmId = runtimeMXBean.getName();

        try
        {
            int index = vmId.indexOf('@');
            if (index < 0)
            {
                String messageId = "javelin.common.AttachUtil.FailGetProcessID";
                String message = JavelinMessages.getMessage(messageId);
                SystemLogger.getInstance().warn(message);
                return;
            }
            String processId = vmId.substring(0, index);
            vm__ = attachMethod__.invoke(null, processId);

        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * �q�[�v�̃q�X�g�O�������擾����B
     * 
     * @param liveObjectOnly �q�X�g�O�����擾�̑Ώۂ��A�������Ă���I�u�W�F�N�g�݂̂ɂ��邩(GC���邩�ǂ���)���w�肷��B
     * @return �q�[�v�̃q�X�g�O�����B
     */
    public static String getHeapHisto(final boolean liveObjectOnly)
    {
        StringBuffer buf = new StringBuffer();
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = getHeapHistoReader(liveObjectOnly);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                buf.append(line);
                buf.append("\n");
            }
        }
        catch (IOException ioe)
        {
            SystemLogger.getInstance().warn(ioe);
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (IOException ioe)
                {
                    SystemLogger.getInstance().warn(ioe);
                }
            }
        }

        String heapHisto = buf.toString();
        return heapHisto;
    }

    /**
     * �q�X�g�O�������擾���邽�߂�Reader���擾����B
     * 
     * @param liveObjectOnly �q�X�g�O�����擾�̑Ώۂ��A�������Ă���I�u�W�F�N�g�݂̂ɂ��邩(GC���邩�ǂ���)���w�肷��B
     * 
     * @return �q�X�g�O�������擾���邽�߂�Reader�B
     * @throws IOException �q�[�v�q�X�g�O�����擾���̗�O�B
     */
    public static BufferedReader getHeapHistoReader(final boolean liveObjectOnly)
        throws IOException
    {
        if (vm__ == null)
        {
            return null;
        }

        String histoParam;
        if (liveObjectOnly == true)
        {
            histoParam = HISTO_LIVE;
        }
        else
        {
            histoParam = HISTO_ALL;
        }
        try
        {
            InputStream inputStream = (InputStream)heapHistoMethod__.invoke(vm__, histoParam);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader;
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
            vm__ = null;
        }
        catch (InvocationTargetException ex)
        {
            if (ex.getCause() instanceof IOException)
            {
                throw (IOException)ex.getCause();
            }
        }
        return null;
    }

    /**
     * JVM����f�^�b�`����B
     */
    public static void detach()
    {
        if (vm__ == null)
        {
            return;
        }

        try
        {
            detachMethod__.invoke(vm__);
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
            vm__ = null;
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }
}
