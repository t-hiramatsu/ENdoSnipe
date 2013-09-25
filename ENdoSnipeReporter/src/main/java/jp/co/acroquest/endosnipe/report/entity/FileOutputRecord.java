package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽo・ｽﾍ」・ｽ^・ｽu・ｽﾌ「・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ」・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、
 * ・ｽu・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽo・ｽﾍ量」・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class FileOutputRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
    private Timestamp measurementTime_;
    
	/** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽo・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
    private long fileOutput_;
    
    /** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽo・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
    private long fileOutputMax_;
    
    /** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽo・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
    private long fileOutputMin_;
	
	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime() {
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime) {
		this.measurementTime_ = measurementTime;
	}
	
	/**
	 * @return the fileOutput_
	 */
	public long getFileOutput() {
		return fileOutput_;
	}

	/**
	 * @param fileOutput the fileOutput_ to set
	 */
	public void setFileOutput(long fileOutput) {
		this.fileOutput_ = fileOutput;
	}

	/**
	 * @return the fileOutputMax_
	 */
	public long getFileOutputMax() {
		return fileOutputMax_;
	}

	/**
	 * @param fileOutputMax the fileOutputMax_ to set
	 */
	public void setFileOutputMax(long fileOutputMax) {
		this.fileOutputMax_ = fileOutputMax;
	}

	/**
	 * @return the fileOutputMin_
	 */
	public long getFileOutputMin() {
		return fileOutputMin_;
	}

	/**
	 * @param fileOutputMin the fileOutputMin_ to set
	 */
	public void setFileOutputMin(long fileOutputMin) {
		this.fileOutputMin_ = fileOutputMin;
	}
}
