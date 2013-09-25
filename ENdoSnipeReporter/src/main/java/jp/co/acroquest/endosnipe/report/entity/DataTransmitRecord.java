package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽo・ｽﾍ」・ｽ^・ｽu・ｽﾌ「・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊ」・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽ・ｽM・ｽﾊ」・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class DataTransmitRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
    private Timestamp measurementTime_;

    /** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
    private long dataTransmit_;
    
    /** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
    private long dataTransmitMax_;
    
    /** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
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
