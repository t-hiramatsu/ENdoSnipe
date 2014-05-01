package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́u�I�u�W�F�N�g���v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class ObjectNumRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      objectNum_;
    
    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ�ԍő�j */
    private long      objectNumMax_;
    
    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      objectNumMin_;
    
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
	 * @return the objectNum_
	 */
	public long getObjectNum() {
		return objectNum_;
	}

	/**
	 * @param objectNum the objectNum_ to set
	 */
	public void setObjectNum(long objectNum) {
		this.objectNum_ = objectNum;
	}

	/**
	 * @return the objectNumMax_
	 */
	public long getObjectNumMax() {
		return objectNumMax_;
	}

	/**
	 * @param objectNumMax the objectNumMax_ to set
	 */
	public void setObjectNumMax(long objectNumMax) {
		this.objectNumMax_ = objectNumMax;
	}

	/**
	 * @return the objectNumMin_
	 */
	public long getObjectNumMin() {
		return objectNumMin_;
	}

	/**
	 * @param objectNumMin the objectNumMin_ to set
	 */
	public void setObjectNumMin(long objectNumMin) {
		this.objectNumMin_ = objectNumMin;
	}
}
