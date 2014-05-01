package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�I�u�W�F�N�g���v�^�u�́uMap�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class MapSizeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** Map�̃T�C�Y[�v�f��]�i��Ԋ�ԕ��ρj */
    private long      mapSize_;
    
    /** Map�̃T�C�Y[�v�f��]�i��Ԋ�ԍő�j */
    private long      mapSizeMax_;
    
    /** Map�̃T�C�Y[�v�f��]�i��Ԋ�ԍŏ��j */
    private long      mapSizeMin_;
    
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
	 * @return the mapSize_
	 */
	public long getMapSize() {
		return mapSize_;
	}

	/**
	 * @param mapSize the mapSize_ to set
	 */
	public void setMapSize(long mapSize) {
		this.mapSize_ = mapSize;
	}

	/**
	 * @return the mapSizeMax_
	 */
	public long getMapSizeMax() {
		return mapSizeMax_;
	}

	/**
	 * @param mapSizeMax the mapSizeMax_ to set
	 */
	public void setMapSizeMax(long mapSizeMax) {
		this.mapSizeMax_ = mapSizeMax;
	}

	/**
	 * @return the mapSizeMin_
	 */
	public long getMapSizeMin() {
		return mapSizeMin_;
	}

	/**
	 * @param mapSizeMin the mapSizeMin_ to set
	 */
	public void setMapSizeMin(long mapSizeMin) {
		this.mapSizeMin_ = mapSizeMin;
	}
}
