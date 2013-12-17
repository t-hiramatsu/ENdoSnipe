package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「List・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class ListSizeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** List・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long listSize_;

	/** List・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long listSizeMax_;

	/** List・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long listSizeMin_;

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
	 * @return the listSize_
	 */
	public long getListSize()
	{
		return listSize_;
	}

	/**
	 * @param listSize the listSize_ to set
	 */
	public void setListSize(long listSize)
	{
		this.listSize_ = listSize;
	}

	/**
	 * @return the listSizeMax_
	 */
	public long getListSizeMax()
	{
		return listSizeMax_;
	}

	/**
	 * @param listSizeMax the listSizeMax_ to set
	 */
	public void setListSizeMax(long listSizeMax)
	{
		this.listSizeMax_ = listSizeMax;
	}

	/**
	 * @return the listSizeMin_
	 */
	public long getListSizeMin()
	{
		return listSizeMin_;
	}

	/**
	 * @param listSizeMin the listSizeMin_ to set
	 */
	public void setListSizeMin(long listSizeMin)
	{
		this.listSizeMin_ = listSizeMin;
	}
}
