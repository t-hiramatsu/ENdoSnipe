package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�f�[�^��o�́v�^�u�́u�t�@�C����o�́v�̃O���t�́A
 * �u�t�@�C���o�͗ʁv�̃f�[�^��1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class FileOutputRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
	/** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C���o�͗�[�T�C�Y(Bytes)]�i��Ԋ�ԕ��ρj */
    private long fileOutput_;
    
    /** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C���o�͗�[�T�C�Y(Bytes)]�i��Ԋ�ԍő�j */
    private long fileOutputMax_;
    
    /** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C���o�͗�[�T�C�Y(Bytes)]�i��Ԋ�ԍŏ��j */
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
