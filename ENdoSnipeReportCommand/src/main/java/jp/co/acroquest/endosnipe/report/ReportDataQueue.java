/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package jp.co.acroquest.endosnipe.report;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * Queue for putting user inputted report data
 * @author khinewai
 * 
 */

public class ReportDataQueue implements LogMessageCodes, CommonLogMessageCodes
{
	/**
	 * QUEUE_SIZE for Queue
	 */
	private static final int QUEUE_SIZE = 100;

	/**
	 * OFFERING_TIMEOUT that put data to Queue
	 */
	private static final int OFFERING_TIMEOUT = 10000;

	/**
	 * TAKING_TIMEOUT that take data from Queue
	 */
	private static final int TAKING_TIMEOUT = 1000;

	/**
	 * queue to put data
	 */
	private BlockingQueue<ReportData> queue_ = new ArrayBlockingQueue<ReportData>(QUEUE_SIZE);

	/**
	 * instance object of ReportDataQueue
	 */
	private static ReportDataQueue instance = new ReportDataQueue();

	/**
	 * Logger 
	 */
	private static ENdoSnipeLogger logger_ = ENdoSnipeLogger.getLogger(ReportDataQueue.class);

	/**
	 * キューに {@link JavelinData} を追加します。<br />
	 * 
	 * @param data
	 *            {@link JavelinData} オブジェクト
	 */

	public void offer(final ReportData data)
	{
		if (data != null)
		{
			try
			{

				queue_.offer(data, OFFERING_TIMEOUT, TimeUnit.MILLISECONDS);
				if (logger_.isDebugEnabled())
				{
					logger_.log(QUEUE_OFFERED, data.toString());
				}
			}
			catch (InterruptedException ex)
			{
				logger_.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
			}
		}
	}

	/**
	 * キューから {@link JavelinData} を取り出します。<br />
	 * 
	 * キューにデータが存在しない場合、スレッドはブロックします。<br />
	 * 
	 * @return {@link JavelinData} オブジェクト
	 */
	public ReportData take()
	{
		try
		{
			ReportData data = queue_.poll(TAKING_TIMEOUT, TimeUnit.MILLISECONDS);
			if (logger_.isDebugEnabled() && data != null)
			{
				logger_.log(QUEUE_TAKEN, data.toString());
			}
			return data;
		}
		catch (InterruptedException ex)
		{
			logger_.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
			return null;
		}
	}

	/**
	 * キューに残っているデータの数を返します。<br />
	 * 
	 * @return データの数
	 */
	public int size()
	{
		return queue_.size();
	}

	/**
	 * Get Queue of Report
	 * @return reportQueue
	 */
	public ReportDataQueue getReportQueue()
	{
		return (ReportDataQueue) this.queue_;
	}

	/**
	 * get instance object of ReportDataQueue
	 * @return
	 */
	public static ReportDataQueue getInstance()
	{
		return instance;
	}

	/**
	 * set instance object to ReportDataQueue
	 * @param instance
	 */
	public static void setInstance(ReportDataQueue instance)
	{
		ReportDataQueue.instance = instance;
	}

}
