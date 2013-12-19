package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽu・ｽI・ｽu・ｽW・ｽF・ｽN・ｽg・ｽ・ｽ・ｽv・ｽ^・ｽu・ｽﾌ「Queue・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class QueueSizeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** Queue・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private long queueSize_;

	/** Queue・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private long queueSizeMax_;

	/** Queue・ｽﾌサ・ｽC・ｽY[・ｽv・ｽf・ｽ・ｽ]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private long queueSizeMin_;

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
	 * @return the queueSize_
	 */
	public long getQueueSize()
	{
		return queueSize_;
	}

	/**
	 * @param queueSize the queueSize_ to set
	 */
	public void setQueueSize(long queueSize)
	{
		this.queueSize_ = queueSize;
	}

	/**
	 * @return the queueSizeMax_
	 */
	public long getQueueSizeMax()
	{
		return queueSizeMax_;
	}

	/**
	 * @param queueSizeMax the queueSizeMax_ to set
	 */
	public void setQueueSizeMax(long queueSizeMax)
	{
		this.queueSizeMax_ = queueSizeMax;
	}

	/**
	 * @return the queueSizeMin_
	 */
	public long getQueueSizeMin()
	{
		return queueSizeMin_;
	}

	/**
	 * @param queueSizeMin the queueSizeMin_ to set
	 */
	public void setQueueSizeMin(long queueSizeMin)
	{
		this.queueSizeMin_ = queueSizeMin;
	}
}
