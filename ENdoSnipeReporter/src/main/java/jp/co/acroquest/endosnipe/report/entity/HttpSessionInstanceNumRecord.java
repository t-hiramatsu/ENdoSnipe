package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�A�v���P�[�V�����v�^�u�́uHttpSession�̃C���X�^���X���v�̃��|�[�g�ɂ����āA
 * �o�͂������̒���1���R�[�h����ێ�����G���e�B�e�B�ł��B
 * 
 * @author T. Iida
 */
public class HttpSessionInstanceNumRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
	/** HttpSession�̃C���X�^���X��[��]�i��Ԋ��ԕ��ρj */
    private long      httpSessionInstanceNum_;
    
    /** HttpSession�̃C���X�^���X��[��]�i��Ԋ��ԍő�j */
    private long      httpSessionInstanceNumMax_;
    
    /** HttpSession�̃C���X�^���X��[��]�i��Ԋ��ԍŏ��j */
    private long      httpSessionInstanceNumMin_;

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
	 * @return the httpSessionInstanceNum_
	 */
	public long getHttpSessionInstanceNum() {
		return httpSessionInstanceNum_;
	}

	/**
	 * @param httpSessionInstanceNum the httpSessionInstanceNum_ to set
	 */
	public void setHttpSessionInstanceNum(long httpSessionInstanceNum) {
		this.httpSessionInstanceNum_ = httpSessionInstanceNum;
	}

	/**
	 * @return the httpSessionInstanceNumMax_
	 */
	public long getHttpSessionInstanceNumMax() {
		return httpSessionInstanceNumMax_;
	}

	/**
	 * @param httpSessionInstanceNumMax the httpSessionInstanceNumMax_ to set
	 */
	public void setHttpSessionInstanceNumMax(long httpSessionInstanceNumMax) {
		this.httpSessionInstanceNumMax_ = httpSessionInstanceNumMax;
	}

	/**
	 * @return the httpSessionInstanceNumMin_
	 */
	public long getHttpSessionInstanceNumMin() {
		return httpSessionInstanceNumMin_;
	}

	/**
	 * @param httpSessionInstanceNumMin the httpSessionInstanceNumMin_ to set
	 */
	public void setHttpSessionInstanceNumMin(long httpSessionInstanceNumMin) {
		this.httpSessionInstanceNumMin_ = httpSessionInstanceNumMin;
	}
}
