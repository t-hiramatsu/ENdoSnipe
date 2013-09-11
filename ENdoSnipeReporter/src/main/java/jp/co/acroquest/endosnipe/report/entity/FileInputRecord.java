package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽo・ｽﾍ」・ｽ^・ｽu・ｽﾌ「・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ」・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、
 * ・ｽu・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽﾍ量」・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class FileInputRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
    private Timestamp measurementTime_;
    
	/** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
    private long fileInput_;
    
    /** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
    private long fileInputMax_;
    
    /** ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽo・ｽﾍ量のグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽﾍ暦ｿｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
    private long fileInputMin_;
    
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
	 * @return the fileInput_
	 */
	public long getFileInput() {
		return fileInput_;
	}

	/**
	 * @param fileInput・ｽ@the fileInput_ to set
	 */
	public void setFileInput(long fileInput) {
		this.fileInput_ = fileInput;
	}

	/**
	 * @return the fileInputMax_
	 */
	public long getFileInputMax() {
		return fileInputMax_;
	}

	/**
	 * @param fileInputMax the fileInputMax_ to set
	 */
	public void setFileInputMax(long fileInputMax) {
		this.fileInputMax_ = fileInputMax;
	}

	/**
	 * @return the fileInputMin_
	 */
	public long getFileInputMin() {
		return fileInputMin_;
	}

	/**
	 * @param fileInputMin the fileInputMin_ to set
	 */
	public void setFileInputMin(long fileInputMin) {
		this.fileInputMin_ = fileInputMin;
	}
}
