package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽT・ｽC・ｽY・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class ObjectSizeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽT・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long objectSize_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽT・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long objectSizeMax_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽT・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long objectSizeMin_;

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
	 * @return the objectSize_
	 */
	public long getObjectSize()
	{
		return objectSize_;
	}

	/**
	 * @param objectSize the objectSize_ to set
	 */
	public void setObjectSize(long objectSize)
	{
		this.objectSize_ = objectSize;
	}

	/**
	 * @return the objectSizeMax_
	 */
	public long getObjectSizeMax()
	{
		return objectSizeMax_;
	}

	/**
	 * @param objectSizeMax the objectSizeMax_ to set
	 */
	public void setObjectSizeMax(long objectSizeMax)
	{
		this.objectSizeMax_ = objectSizeMax;
	}

	/**
	 * @return the objectSizeMin_
	 */
	public long getObjectSizeMin()
	{
		return objectSizeMin_;
	}

	/**
	 * @param objectSizeMin the objectSizeMin_ to set
	 */
	public void setObjectSizeMin(long objectSizeMin)
	{
		this.objectSizeMin_ = objectSizeMin;
	}
}
