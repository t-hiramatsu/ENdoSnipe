package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.servlet.JvnDownloadServlet;
import jp.co.acroquest.endosnipe.web.dashboard.util.DaoUtil;

@Service
public class PerformanceDoctorService
{

	/** ロガー */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
			.getLogger(JvnDownloadServlet.class);

	/** バッファのサイズ */
	private static final int BUFFER_SIZE = 1024;

	// /**
	// * クライアントからのjvnファイル出力ダウンロードを受信するためのサーブレットです。
	// * @param request {@link HttpServletRequest}オブジェクト
	// * @param response {@link HttpServletResponse}オブジェクト
	// */
	// public void doRequest(HttpServletRequest request, HttpServletResponse response)
	// {
	// // パラメータ取得
	// String agentId = request.getParameter(EventConstants.AGENT_ID);
	// String logFileName = request.getParameter(EventConstants.LOG_FILE_NAME);
	//
	// try
	// {
	// // Javalinログを取得する
	// JavelinLog jvnLog = DaoUtil.getJavelinLog(agentId, logFileName);
	// if (jvnLog == null)
	// {
	// LOGGER.log(LogMessageCodes.FAIL_GET_JVNLOG);
	// return;
	// }
	// // Javalinログをクライアントに返す
	// printOutFile(request, response, logFileName, jvnLog.javelinLog);
	// }
	// catch (IOException ex)
	// {
	// // Do Nothing.
	// }
	// }

	/**
	 * クライアントからのjvnファイル出力ダウンロードを受信するためのサーブレットです。
	 * 
	 * @param request
	 *            {@link HttpServletRequest}オブジェクト
	 * @param response
	 *            {@link HttpServletResponse}オブジェクト
	 */
	public void doRequest(HttpServletResponse response, String fileName)
	{
		try
		{
			// Javalinログを取得する
			JavelinLog jvnLog = DaoUtil.getJavelinLog("1", fileName);
			if (jvnLog == null)
			{
				LOGGER.log(LogMessageCodes.FAIL_GET_JVNLOG);
				return;
			}
			// Javalinログをクライアントに返す
			printOutFile(null, response, fileName, jvnLog.javelinLog);
		}
		catch (IOException ex)
		{
			// Do Nothing.
		}
	}

	private void printOutFile(HttpServletRequest req, HttpServletResponse res, String logFileName,
			InputStream is) throws IOException
	{
		OutputStream os = res.getOutputStream();
		try
		{
			// レスポンス設定
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
		} finally
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

				} finally
				{
					os = null;
					is = null;
				}
			}
		}
	}

	/**
	 * ファイルが見つからない場合
	 * 
	 * @param res
	 *            {@link HttpServletResponse}
	 */
	private void printOutNotFound(HttpServletResponse res)
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
		} finally
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
				} finally
				{
					toClient = null;
				}
			}
		}
	}
}
