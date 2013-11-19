package jp.co.acroquest.endosnipe.report;

import java.util.Calendar;

/**
 * 
 * @author khinewai
 * 
 */
public class ReportData {
	public int reportId;
	public String reportName;
	public String targetMeasurementName;
	public Calendar reportTermFrom;
	public Calendar reportTermTo;
	public String status_;

	public String getStatus() {
		return status_;
	}

	public void setStatus_(String status) {
		this.status_ = status;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getTargetMeasurementName() {
		return targetMeasurementName;
	}

	public void setTargetMeasurementName(String targetMeasurementName) {
		this.targetMeasurementName = targetMeasurementName;
	}

	public Calendar getReportTermFrom() {
		return reportTermFrom;
	}

	public void setReportTermFrom(Calendar reportTermForm) {
		this.reportTermFrom = reportTermForm;
	}

	public Calendar getReportTermTo() {
		return reportTermTo;
	}

	public void setReportTermTo(Calendar reportTermTo) {
		this.reportTermTo = reportTermTo;
	}

}
