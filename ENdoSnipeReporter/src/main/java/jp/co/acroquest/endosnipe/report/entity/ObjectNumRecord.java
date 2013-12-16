package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class ObjectNumRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long objectNum_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long objectNumMax_;

	/** ・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long objectNumMin_;

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
	 * @return the objectNum_
	 */
	public long getObjectNum()
	{
		return objectNum_;
	}

	/**
	 * @param objectNum the objectNum_ to set
	 */
	public void setObjectNum(long objectNum)
	{
		this.objectNum_ = objectNum;
	}

	/**
	 * @return the objectNumMax_
	 */
	public long getObjectNumMax()
	{
		return objectNumMax_;
	}

	/**
	 * @param objectNumMax the objectNumMax_ to set
	 */
	public void setObjectNumMax(long objectNumMax)
	{
		this.objectNumMax_ = objectNumMax;
	}

	/**
	 * @return the objectNumMin_
	 */
	public long getObjectNumMin()
	{
		return objectNumMin_;
	}

	/**
	 * @param objectNumMin the objectNumMin_ to set
	 */
	public void setObjectNumMin(long objectNumMin)
	{
		this.objectNumMin_ = objectNumMin;
	}
}
