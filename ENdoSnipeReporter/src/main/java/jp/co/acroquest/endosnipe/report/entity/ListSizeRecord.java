package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́uList�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class ListSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      listSize_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ�ԍő�j */
    private long      listSizeMax_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      listSizeMin_;
    
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
	 * @return the listSize_
	 */
	public long getListSize() {
		return listSize_;
	}

	/**
	 * @param listSize the listSize_ to set
	 */
	public void setListSize(long listSize) {
		this.listSize_ = listSize;
	}

	/**
	 * @return the listSizeMax_
	 */
	public long getListSizeMax() {
		return listSizeMax_;
	}

	/**
	 * @param listSizeMax the listSizeMax_ to set
	 */
	public void setListSizeMax(long listSizeMax) {
		this.listSizeMax_ = listSizeMax;
	}

	/**
	 * @return the listSizeMin_
	 */
	public long getListSizeMin() {
		return listSizeMin_;
	}

	/**
	 * @param listSizeMin the listSizeMin_ to set
	 */
	public void setListSizeMin(long listSizeMin) {
		this.listSizeMin_ = listSizeMin;
	}
}
