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
 * JVMにアタッチするためのユーティリティ
 * 
 * @author eriguchi
 *
 */
public class AttachUtil
{

    /** JVMへの参照 */
    private static Object vm__ = null;

    /** heapHistoメソッド */
    private static Method heapHistoMethod__;

    /** detachMethodメソッド */
    private static Method detachMethod__;

    /** attachMethodメソッド */
    private static Method attachMethod__;

    /** ヒープヒストグラム取得時に、全てのオブジェクトを対象として指定するパラメータ。 */
    private static final String HISTO_ALL = "-all";

    /** ヒープヒストグラム取得時に、生存しているオブジェクトのみを対象として指定するパラメータ。 */
    private static final String HISTO_LIVE = "-live";

    /** ヒープ取得を行うクラス名*/
    private static final String GET_HEAP_CLASS_NAME = "sun.tools.attach.HotSpotVirtualMachine";

    /** ヒープ取得を行うメソッド名 */
    private static final String GET_HEAP_METHOD_NAME = "heapHisto";

    /** 初期化時のエラーメッセージキー */
    private static final String INIT_ERROR_MESSAGE_KEY =
        "javelin.common.AttachUtil.CannotFindMethod";
    static
    {
        try
        {
            Class<?> clazz = Class.forName(GET_HEAP_CLASS_NAME);
            heapHistoMethod__ = clazz.getMethod(GET_HEAP_METHOD_NAME, new Class[]{String.class});
            attachMethod__ = clazz.getMethod("attach", new Class[]{String.class});
            detachMethod__ = clazz.getMethod("detach", new Class[]{});
        }
        catch (ClassNotFoundException ex)
        {
            String message =
                JavelinMessages.getMessage(INIT_ERROR_MESSAGE_KEY, GET_HEAP_CLASS_NAME,
                                           GET_HEAP_METHOD_NAME, ex.getMessage());
            SystemLogger.getInstance().warn(message);
        }
        catch (NoSuchMethodException ex)
        {
            String message =
                JavelinMessages.getMessage(INIT_ERROR_MESSAGE_KEY, GET_HEAP_CLASS_NAME,
                                           GET_HEAP_METHOD_NAME, ex.getMessage());
            SystemLogger.getInstance().warn(message);
        }

    }

    /**
     * インスタンス化を避けるためのコンストラクタ。
     */
    private AttachUtil()
    {
    }

    /**
     * JVMにアタッチする。
     */
    public static void attach()
    {
        if (attachMethod__ == null)
        {
            return;
        }
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
     * ヒープのヒストグラムを取得する。
     * 
     * @param liveObjectOnly ヒストグラム取得の対象を、生存しているオブジェクトのみにするか(GCするかどうか)を指定する。
     * @return ヒープのヒストグラム。
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
     * ヒストグラムを取得するためのReaderを取得する。
     * 
     * @param liveObjectOnly ヒストグラム取得の対象を、生存しているオブジェクトのみにするか(GCするかどうか)を指定する。
     * 
     * @return ヒストグラムを取得するためのReader。
     * @throws IOException ヒープヒストグラム取得時の例外。
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
     * JVMからデタッチする。
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
