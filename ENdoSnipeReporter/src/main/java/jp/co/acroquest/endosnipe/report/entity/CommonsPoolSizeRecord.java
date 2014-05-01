package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�A�v���P�[�V�����v�^�u�́uCommons Pool�̃T�C�Y�v�̃��|�[�g�ɂ����āA
 * �o�͂������̒���1���R�[�h����ێ�����G���e�B�e�B�ł��B
 * 
 * @author T. Iida
 */
public class CommonsPoolSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** Commons Pool�̃T�C�Y�̍ő�l */
    private long      maxCommonsPoolSize_;

    /** Commons Pool�̃T�C�Y[��]�i��Ԋ��ԕ��ρj */
    private long      commonsPoolSize_;
    
    /** Commons Pool�̃T�C�Y[��]�i��Ԋ��ԍő�j */
    private long      commonsPoolSizeMax_;
    
    /** Commons Pool�̃T�C�Y[��]�i��Ԋ��ԍŏ��j */
    private long      commonsPoolSizeMin_;
    
    /**
     * �R���X�g���N�^
     */
    public CommonsPoolSizeRecord() {
        maxCommonsPoolSize_ = 0;
        commonsPoolSize_ = 0;
        commonsPoolSizeMax_ = 0;
        commonsPoolSizeMin_ = 0;
    }
    
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
	 * @return the maxCommonsPoolSize_
	 */
	public long getMaxCommonsPoolSize() {
		return maxCommonsPoolSize_;
	}

	/**
	 * @param maxCommonsPoolSize the maxCommonsPoolSize_ to set
	 */
	public void setMaxCommonsPoolSize(long maxCommonsPoolSize) {
		this.maxCommonsPoolSize_ = maxCommonsPoolSize;
	}

	/**
	 * @return the commonsPoolSize_
	 */
	public long getCommonsPoolSize() {
		return commonsPoolSize_;
	}

	/**
	 * @param commonsPoolSize the commonsPoolSize_ to set
	 */
	public void setCommonsPoolSize(long commonsPoolSize) {
		this.commonsPoolSize_ = commonsPoolSize;
	}

	/**
	 * @return the commonsPoolSizeMax_
	 */
	public long getCommonsPoolSizeMax() {
		return commonsPoolSizeMax_;
	}

	/**
	 * @param commonsPoolSizeMax the commonsPoolSizeMax_ to set
	 */
	public void setCommonsPoolSizeMax(long commonsPoolSizeMax) {
		this.commonsPoolSizeMax_ = commonsPoolSizeMax;
	}

	/**
	 * @return the commonsPoolSizeMin_
	 */
	public long getCommonsPoolSizeMin() {
		return commonsPoolSizeMin_;
	}

	/**
	 * @param commonsPoolSizeMin the commonsPoolSizeMin_ to set
	 */
	public void setCommonsPoolSizeMin(long commonsPoolSizeMin) {
		this.commonsPoolSizeMin_ = commonsPoolSizeMin;
	}
}
