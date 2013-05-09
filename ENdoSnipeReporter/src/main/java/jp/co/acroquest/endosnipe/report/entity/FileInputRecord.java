package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�f�[�^��o�́v�^�u�́u�t�@�C����o�́v�̃O���t�́A
 * �u�t�@�C����͗ʁv�̃f�[�^��1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class FileInputRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
	/** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C����͗�[�T�C�Y(Bytes)]�i��Ԋ�ԕ��ρj */
    private long fileInput_;
    
    /** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C����͗�[�T�C�Y(Bytes)]�i��Ԋ�ԍő�j */
    private long fileInputMax_;
    
    /** �t�@�C����o�͗ʂ̃O���t�́A�t�@�C����͗�[�T�C�Y(Bytes)]�i��Ԋ�ԍŏ��j */
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
	 * @param fileInput�@the fileInput_ to set
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
