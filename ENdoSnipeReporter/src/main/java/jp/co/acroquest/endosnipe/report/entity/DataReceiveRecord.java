package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽo・ｽﾍ」・ｽ^・ｽu・ｽﾌ「・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊ」・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、
 * ・ｽu・ｽf・ｽ[・ｽ^・ｽ・ｽM・ｽﾊ」・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class DataReceiveRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long dataReceive_;

	/** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long dataReceiveMax_;

	/** ・ｽl・ｽb・ｽg・ｽ・ｽ・ｽ[・ｽN・ｽo・ｽR・ｽﾅのデ・ｽ[・ｽ^・ｽ・ｽ・ｽ・ｽM・ｽﾊのグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽf・ｽ[・ｽ^・ｽ・ｽM・ｽ・ｽ[・ｽT・ｽC・ｽY(Bytes)]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long dataReceiveMin_;

	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime()
	{
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime)
	{
		this.measurementTime_ = measurementTime;
	}

	/**
	 * @return the dataReceive_
	 */
	public long getDataReceive()
	{
		return dataReceive_;
	}

	/**
	 * @param dataReceive the dataReceive_ to set
	 */
	public void setDataReceive(long dataReceive)
	{
		this.dataReceive_ = dataReceive;
	}

	/**
	 * @return the dataReceiveMax_
	 */
	public long getDataReceiveMax()
	{
		return dataReceiveMax_;
	}

	/**
	 * @param dataReceiveMax the dataReceiveMax_ to set
	 */
	public void setDataReceiveMax(long dataReceiveMax)
	{
		this.dataReceiveMax_ = dataReceiveMax;
	}

	/**
	 * @return the dataReceiveMin_
	 */
	public long getDataReceiveMin()
	{
		return dataReceiveMin_;
	}

	/**
	 * @param dataReceiveMin the dataReceiveMin_ to set
	 */
	public void setDataReceiveMin(long dataReceiveMin)
	{
		this.dataReceiveMin_ = dataReceiveMin;
	}
}
