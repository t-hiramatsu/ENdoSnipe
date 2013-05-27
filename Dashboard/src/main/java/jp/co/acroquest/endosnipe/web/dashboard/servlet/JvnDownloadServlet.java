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
package jp.co.acroquest.endosnipe.web.dashboard.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.dashboard.constants.EventConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.util.DaoUtil;

/**
 * jvnファイル出力用のサーブレットです。
 * @author tsukano
 *
 */
public class JvnDownloadServlet extends HttpServlet
{

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER           =
            ENdoSnipeLogger.getLogger(JvnDownloadServlet.class);

    /** バッファのサイズ */
    private static final int             BUFFER_SIZE      = 1024;

    /** シリアルID */
    private static final long            serialVersionUID = 2070325848334763894L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
            throws ServletException
            {
        // Do Nothing.
            }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
    {
        doRequest(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
    {
        doRequest(request, response);
    }

    /**
     * クライアントからのjvnファイル出力ダウンロードを受信するためのサーブレットです。
     * @param request {@link HttpServletRequest}オブジェクト
     * @param response {@link HttpServletResponse}オブジェクト
     */
    public void doRequest(final HttpServletRequest request, final HttpServletResponse response)
    {
        // パラメータ取得
        String agentId = request.getParameter(EventConstants.AGENT_ID);
        String logFileName = request.getParameter(EventConstants.LOG_FILE_NAME);

        try
        {
            // Javalinログを取得する
            JavelinLog jvnLog = DaoUtil.getJavelinLog(agentId, logFileName);
            if (jvnLog == null)
            {
                LOGGER.log(LogMessageCodes.FAIL_GET_JVNLOG);
                return;
            }
            // Javalinログをクライアントに返す
            printOutFile(request, response, logFileName, jvnLog.javelinLog);
        }
        catch (IOException ex)
        {
            // Do Nothing.
        }
    }

    private void printOutFile(final HttpServletRequest req, final HttpServletResponse res, final String logFileName,
            InputStream is)
                    throws IOException
                    {
        OutputStream os = res.getOutputStream();
        try
        {
            //レスポンス設定
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "filename=\"" + logFileName + "\"");

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = is.read(buffer)) >= 0)
            {
                os.write(buffer, 0, len);
            }

            is.close();
        }
        catch (IOException ex)
        {
            printOutNotFound(res);
        }
        finally
        {

            if (os != null)
            {
                try
                {
                    os.close();
                    is.close();
                }
                catch (IOException ex)
                {
                    LOGGER.log(LogMessageCodes.IO_ERROR);

                }
                finally
                {
                    os = null;
                    is = null;
                }
            }
        }
                    }

    /**
     * ファイルが見つからない場合
     * @param res {@link HttpServletResponse}
     */
    private void printOutNotFound(final HttpServletResponse res)
    {
        OutputStream toClient = null;
        try
        {
            toClient = res.getOutputStream();
            res.setContentType("text/html;charset=Shift_JIS");
            toClient.write("File not found".getBytes());
            toClient.close();
        }
        catch (IOException ex)
        {
            LOGGER.log(LogMessageCodes.IO_ERROR);
        }
        finally
        {
            if (toClient != null)
            {
                try
                {
                    toClient.close();
                }
                catch (IOException ex)
                {
                    LOGGER.log(LogMessageCodes.IO_ERROR);
                }
                finally
                {
                    toClient = null;
                }
            }
        }
    }
}