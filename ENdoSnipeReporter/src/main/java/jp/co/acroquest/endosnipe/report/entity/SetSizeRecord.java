package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́uSet�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class SetSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** Set�̃T�C�Y[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      setSize_;
    
    /** Set�̃T�C�Y[�v�f��]�i��Ԋ�ԍő�j */
    private long      setSizeMax_;
    
    /** Set�̃T�C�Y[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      setSizeMin_;
    
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
	 * @return the setSize_
	 */
	public long getSetSize() {
		return setSize_;
	}

	/**
	 * @param setSize the setSize_ to set
	 */
	public void setSetSize(long setSize) {
		this.setSize_ = setSize;
	}

	/**
	 * @return the setSizeMax_
	 */
	public long getSetSizeMax() {
		return setSizeMax_;
	}

	/**
	 * @param setSizeMax the setSizeMax_ to set
	 */
	public void setSetSizeMax(long setSizeMax) {
		this.setSizeMax_ = setSizeMax;
	}

	/**
	 * @return the setSizeMin_
	 */
	public long getSetSizeMin() {
		return setSizeMin_;
	}

	/**
	 * @param setSizeMin the setSizeMin_ to set
	 */
	public void setSetSizeMin(long setSizeMin) {
		this.setSizeMin_ = setSizeMin;
	}
}
