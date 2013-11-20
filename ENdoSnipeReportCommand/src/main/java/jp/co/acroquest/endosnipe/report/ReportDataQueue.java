package jp.co.acroquest.endosnipe.report;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;

/**
 * 
 * @author khinewai
 * 
 */

public class ReportDataQueue implements LogMessageCodes, CommonLogMessageCodes {

	private static final int QUEUE_SIZE = 100;
	private static final int OFFERING_TIMEOUT = 10000;
	private static final int TAKING_TIMEOUT = 1000;
	private BlockingQueue<ReportData> queue_ = new ArrayBlockingQueue<ReportData>(
			QUEUE_SIZE);

	private static ReportDataQueue instance = new ReportDataQueue();

	private static ENdoSnipeLogger logger_ = ENdoSnipeLogger
			.getLogger(ReportDataQueue.class);

	/**
	 * キューに {@link JavelinData} を追加します。<br />
	 * 
	 * @param data
	 *            {@link JavelinData} オブジェクト
	 */

	public void offer(final ReportData data) {
		if (data != null) {
			try {

				queue_.offer(data, OFFERING_TIMEOUT, TimeUnit.MILLISECONDS);
				if (logger_.isDebugEnabled()) {
					logger_.log(QUEUE_OFFERED, data.toString());
				}
			} catch (InterruptedException ex) {
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
	public ReportData take() {
		try {
			ReportData data = queue_
					.poll(TAKING_TIMEOUT, TimeUnit.MILLISECONDS);
			if (logger_.isDebugEnabled() && data != null) {
				logger_.log(QUEUE_TAKEN, data.toString());
			}
			return data;
		} catch (InterruptedException ex) {
			logger_.log(EXCEPTION_OCCURED_WITH_RESASON, ex, ex.getMessage());
			return null;
		}
	}

	/**
	 * キューに残っているデータの数を返します。<br />
	 * 
	 * @return データの数
	 */
	public int size() {
		return queue_.size();
	}

	public ReportDataQueue getReportQueue() {
		return (ReportDataQueue) this.queue_;
	}

	public static ReportDataQueue getInstance() {
		return instance;
	}

	public static void setInstance(ReportDataQueue instance) {
		ReportDataQueue.instance = instance;
	}

}
