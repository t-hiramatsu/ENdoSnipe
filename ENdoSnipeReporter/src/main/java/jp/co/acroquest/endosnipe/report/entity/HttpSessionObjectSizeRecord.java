package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�A�v���P�[�V�����v�^�u�́uHttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y�v�̃��|�[�g�ɂ����āA
 * �o�͂������̒���1���R�[�h����ێ�����G���e�B�e�B�ł��B
 * 
 * @author T. Iida
 */
public class HttpSessionObjectSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
	
	/** HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y[�T�C�Y(MB)]�i��Ԋ��ԕ��ρj */
    private long      httpSessionObjectSize_;
    
    /** HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y[�T�C�Y(MB)]�i��Ԋ��ԍő�j */
    private long      httpSessionObjectSizeMax_;
    
    /** HttpSession�ւ̓o�^�I�u�W�F�N�g���T�C�Y[�T�C�Y(MB)]�i��Ԋ��ԍŏ��j */
    private long      httpSessionObjectSizeMin_;

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
	 * @return the httpSessionObjectSize_
	 */
	public long getHttpSessionObjectSize() {
		return httpSessionObjectSize_;
	}

	/**
	 * @param httpSessionObjectSize the httpSessionObjectSize_ to set
	 */
	public void setHttpSessionObjectSize(long httpSessionObjectSize) {
		this.httpSessionObjectSize_ = httpSessionObjectSize;
	}

	/**
	 * @return the httpSessionObjectSizeMax_
	 */
	public long getHttpSessionObjectSizeMax() {
		return httpSessionObjectSizeMax_;
	}

	/**
	 * @param httpSessionObjectSizeMax the httpSessionObjectSizeMax_ to set
	 */
	public void setHttpSessionObjectSizeMax(long httpSessionObjectSizeMax) {
		this.httpSessionObjectSizeMax_ = httpSessionObjectSizeMax;
	}

	/**
	 * @return the httpSessionObjectSizeMin_
	 */
	public long getHttpSessionObjectSizeMin() {
		return httpSessionObjectSizeMin_;
	}

	/**
	 * @param httpSessionObjectSizeMin the httpSessionObjectSizeMin_ to set
	 */
	public void setHttpSessionObjectSizeMin(long httpSessionObjectSizeMin) {
		this.httpSessionObjectSizeMin_ = httpSessionObjectSizeMin;
	}
}
