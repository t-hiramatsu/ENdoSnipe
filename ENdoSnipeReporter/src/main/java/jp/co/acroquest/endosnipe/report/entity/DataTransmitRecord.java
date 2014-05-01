package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�f�[�^��o�́v�^�u�́u�l�b�g���[�N�o�R�ł̃f�[�^����M�ʁv�̃O���t�́A
 * �u�f�[�^���M�ʁv�̃f�[�^��1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class DataTransmitRecord
{
	/** �v������ */
    private Timestamp measurementTime_;

    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^���M��[�T�C�Y(Bytes)]�i��Ԋ�ԕ��ρj */
    private long dataTransmit_;
    
    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^���M��[�T�C�Y(Bytes)]�i��Ԋ�ԍő�j */
    private long dataTransmitMax_;
    
    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^���M��[�T�C�Y(Bytes)]�i��Ԋ�ԍŏ��j */
    private long dataTransmitMin_;
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
	 * @return the dataTransmit_
	 */
	public long getDataTransmit() {
		return dataTransmit_;
	}

	/**
	 * @param dataTransmit the dataTransmit_ to set
	 */
	public void setDataTransmit(long dataTransmit) {
		this.dataTransmit_ = dataTransmit;
	}

	/**
	 * @return the dataTransmitMax_
	 */
	public long getDataTransmitMax() {
		return dataTransmitMax_;
	}

	/**
	 * @param dataTransmitMax the dataTransmitMax_ to set
	 */
	public void setDataTransmitMax(long dataTransmitMax) {
		this.dataTransmitMax_ = dataTransmitMax;
	}

	/**
	 * @return the dataTransmitMin_
	 */
	public long getDataTransmitMin() {
		return dataTransmitMin_;
	}

	/**
	 * @param dataTransmitMin the dataTransmitMin_ to set
	 */
	public void setDataTransmitMin(long dataTransmitMin) {
		this.dataTransmitMin_ = dataTransmitMin;
	}
}
