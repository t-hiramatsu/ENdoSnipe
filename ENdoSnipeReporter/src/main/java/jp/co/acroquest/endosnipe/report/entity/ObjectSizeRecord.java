package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́u�I�u�W�F�N�g�T�C�Y�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class ObjectSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      objectSize_;
    
    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ�ԍő�j */
    private long      objectSizeMax_;
    
    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      objectSizeMin_;
    
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
	 * @return the objectSize_
	 */
	public long getObjectSize() {
		return objectSize_;
	}

	/**
	 * @param objectSize the objectSize_ to set
	 */
	public void setObjectSize(long objectSize) {
		this.objectSize_ = objectSize;
	}

	/**
	 * @return the objectSizeMax_
	 */
	public long getObjectSizeMax() {
		return objectSizeMax_;
	}

	/**
	 * @param objectSizeMax the objectSizeMax_ to set
	 */
	public void setObjectSizeMax(long objectSizeMax) {
		this.objectSizeMax_ = objectSizeMax;
	}

	/**
	 * @return the objectSizeMin_
	 */
	public long getObjectSizeMin() {
		return objectSizeMin_;
	}

	/**
	 * @param objectSizeMin the objectSizeMin_ to set
	 */
	public void setObjectSizeMin(long objectSizeMin) {
		this.objectSizeMin_ = objectSizeMin;
	}
}
