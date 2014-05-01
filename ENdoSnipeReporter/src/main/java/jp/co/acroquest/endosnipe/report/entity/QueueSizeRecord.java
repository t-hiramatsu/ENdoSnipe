package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́uQueue�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class QueueSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      queueSize_;
    
    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ�ԍő�j */
    private long      queueSizeMax_;
    
    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      queueSizeMin_;
    
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
	 * @return the queueSize_
	 */
	public long getQueueSize() {
		return queueSize_;
	}

	/**
	 * @param queueSize the queueSize_ to set
	 */
	public void setQueueSize(long queueSize) {
		this.queueSize_ = queueSize;
	}

	/**
	 * @return the queueSizeMax_
	 */
	public long getQueueSizeMax() {
		return queueSizeMax_;
	}

	/**
	 * @param queueSizeMax the queueSizeMax_ to set
	 */
	public void setQueueSizeMax(long queueSizeMax) {
		this.queueSizeMax_ = queueSizeMax;
	}

	/**
	 * @return the queueSizeMin_
	 */
	public long getQueueSizeMin() {
		return queueSizeMin_;
	}

	/**
	 * @param queueSizeMin the queueSizeMin_ to set
	 */
	public void setQueueSizeMin(long queueSizeMin) {
		this.queueSizeMin_ = queueSizeMin;
	}
}
