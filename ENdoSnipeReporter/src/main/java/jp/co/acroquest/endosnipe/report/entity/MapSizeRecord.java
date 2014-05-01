package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「Map・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class MapSizeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** Map・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long mapSize_;

	/** Map・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long mapSizeMax_;

	/** Map・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long mapSizeMin_;

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
	 * @return the mapSize_
	 */
	public long getMapSize()
	{
		return mapSize_;
	}

	/**
	 * @param mapSize the mapSize_ to set
	 */
	public void setMapSize(long mapSize)
	{
		this.mapSize_ = mapSize;
	}

	/**
	 * @return the mapSizeMax_
	 */
	public long getMapSizeMax()
	{
		return mapSizeMax_;
	}

	/**
	 * @param mapSizeMax the mapSizeMax_ to set
	 */
	public void setMapSizeMax(long mapSizeMax)
	{
		this.mapSizeMax_ = mapSizeMax;
	}

	/**
	 * @return the mapSizeMin_
	 */
	public long getMapSizeMin()
	{
		return mapSizeMin_;
	}

	/**
	 * @param mapSizeMin the mapSizeMin_ to set
	 */
	public void setMapSizeMin(long mapSizeMin)
	{
		this.mapSizeMin_ = mapSizeMin;
	}
}
