package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「Set・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class SetSizeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** Set・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long setSize_;

	/** Set・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long setSizeMax_;

	/** Set・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long setSizeMin_;

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
	 * @return the setSize_
	 */
	public long getSetSize()
	{
		return setSize_;
	}

	/**
	 * @param setSize the setSize_ to set
	 */
	public void setSetSize(long setSize)
	{
		this.setSize_ = setSize;
	}

	/**
	 * @return the setSizeMax_
	 */
	public long getSetSizeMax()
	{
		return setSizeMax_;
	}

	/**
	 * @param setSizeMax the setSizeMax_ to set
	 */
	public void setSetSizeMax(long setSizeMax)
	{
		this.setSizeMax_ = setSizeMax;
	}

	/**
	 * @return the setSizeMin_
	 */
	public long getSetSizeMin()
	{
		return setSizeMin_;
	}

	/**
	 * @param setSizeMin the setSizeMin_ to set
	 */
	public void setSetSizeMin(long setSizeMin)
	{
		this.setSizeMin_ = setSizeMin;
	}
}
