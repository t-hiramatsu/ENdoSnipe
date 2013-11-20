package jp.co.acroquest.endosnipe.report;

import java.io.File;
import java.util.Calendar;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;

public class ReporterThread implements Runnable, LogMessageCodes {

	private ReportDataQueue reportQueue = ReportDataQueue.getInstance();
	public boolean isRunningThread = true;
	private static final String REPORT_PATH = "report";

	DataCollectorConfig config;

	public ReporterThread(DataCollectorConfig dataCollectorConfig) {

		this.config = dataCollectorConfig;
	}

	public ReporterThread() {
	}

	public void run() {
		if(this.config==null)
		{
			isRunningThread = false;
			return;
		}
		Reporter reporter = new Reporter();
		while (isRunningThread) {
			if (this.reportQueue == null || this.reportQueue.size() == 0) {
				continue;
			}

			// キューからデータを取り出す
			ReportData data = this.reportQueue.take();

			if (data != null) {
				Calendar fmTime = data.getReportTermFrom();
				Calendar toTime = data.getReportTermTo();
				String targetItemName = data.getTargetMeasurementName();
				String reportNamePath = data.getReportName();
				String[] reportNameSplitList = reportNamePath.split("/");
				int reportNameSplitLength = reportNameSplitList.length;

				String reportName = reportNameSplitList[reportNameSplitLength - 1];
				String status = data.getStatus();
				String tempDirectory = System.getProperty("java.io.tmpdir");
				this.deleteTempFile(tempDirectory);
				reporter.createReport(this.config, fmTime, toTime, REPORT_PATH,
						targetItemName, reportName, status);
				this.deleteTempFile(tempDirectory);
			}
		}

	}

	/**
	 * This function is delete temp file in the temp directory
	 * 
	 * @param tempDirectory
	 *            get temp file
	 */
	private void deleteTempFile(final String tempDirectory) {
		File directory = new File(tempDirectory);
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getAbsolutePath();
				if (fileName.endsWith(".xls")) {
					file.delete();
				}
			}
		}

	}

	public void stopThread() {
		this.isRunningThread = false;

	}

}
