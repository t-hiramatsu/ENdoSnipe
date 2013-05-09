package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�f�[�^��o�́v�^�u�́u�l�b�g���[�N�o�R�ł̃f�[�^����M�ʁv�̃O���t�́A
 * �u�f�[�^��M�ʁv�̃f�[�^��1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class DataReceiveRecord
{
	/** �v������ */
    private Timestamp measurementTime_;

    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^��M��[�T�C�Y(Bytes)]�i��Ԋ�ԕ��ρj */
    private long dataReceive_;
    
    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^��M��[�T�C�Y(Bytes)]�i��Ԋ�ԍő�j */
    private long dataReceiveMax_;
    
    /** �l�b�g���[�N�o�R�ł̃f�[�^����M�ʂ̃O���t�́A�f�[�^��M��[�T�C�Y(Bytes)]�i��Ԋ�ԍŏ��j */
    private long dataReceiveMin_;
    
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
	 * @return the dataReceive_
	 */
	public long getDataReceive() {
		return dataReceive_;
	}

	/**
	 * @param dataReceive the dataReceive_ to set
	 */
	public void setDataReceive(long dataReceive) {
		this.dataReceive_ = dataReceive;
	}

	/**
	 * @return the dataReceiveMax_
	 */
	public long getDataReceiveMax() {
		return dataReceiveMax_;
	}

	/**
	 * @param dataReceiveMax the dataReceiveMax_ to set
	 */
	public void setDataReceiveMax(long dataReceiveMax) {
		this.dataReceiveMax_ = dataReceiveMax;
	}

	/**
	 * @return the dataReceiveMin_
	 */
	public long getDataReceiveMin() {
		return dataReceiveMin_;
	}

	/**
	 * @param dataReceiveMin the dataReceiveMin_ to set
	 */
	public void setDataReceiveMin(long dataReceiveMin) {
		this.dataReceiveMin_ = dataReceiveMin;
	}
}
