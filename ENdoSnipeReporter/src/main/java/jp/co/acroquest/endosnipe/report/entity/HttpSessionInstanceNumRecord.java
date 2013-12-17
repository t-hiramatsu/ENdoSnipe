package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * 「アプリケーション」タブの「HttpSessionのインスタンス数」のレポートにおいて、
 * 出力される情報の中の1レコード分を保持するエンティティです。
 * 
 * @author T. Iida
 */
public class HttpSessionInstanceNumRecord
{
	/** 計測時刻 */
	private Timestamp measurementTime_;

	/** HttpSessionのインスタンス数[数]（補間期間平均） */
	private long httpSessionInstanceNum_;

	/** HttpSessionのインスタンス数[数]（補間期間最大） */
	private long httpSessionInstanceNumMax_;

	/** HttpSessionのインスタンス数[数]（補間期間最小） */
	private long httpSessionInstanceNumMin_;

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
	 * @return the httpSessionInstanceNum_
	 */
	public long getHttpSessionInstanceNum()
	{
		return httpSessionInstanceNum_;
	}

	/**
	 * @param httpSessionInstanceNum the httpSessionInstanceNum_ to set
	 */
	public void setHttpSessionInstanceNum(long httpSessionInstanceNum)
	{
		this.httpSessionInstanceNum_ = httpSessionInstanceNum;
	}

	/**
	 * @return the httpSessionInstanceNumMax_
	 */
	public long getHttpSessionInstanceNumMax()
	{
		return httpSessionInstanceNumMax_;
	}

	/**
	 * @param httpSessionInstanceNumMax the httpSessionInstanceNumMax_ to set
	 */
	public void setHttpSessionInstanceNumMax(long httpSessionInstanceNumMax)
	{
		this.httpSessionInstanceNumMax_ = httpSessionInstanceNumMax;
	}

	/**
	 * @return the httpSessionInstanceNumMin_
	 */
	public long getHttpSessionInstanceNumMin()
	{
		return httpSessionInstanceNumMin_;
	}

	/**
	 * @param httpSessionInstanceNumMin the httpSessionInstanceNumMin_ to set
	 */
	public void setHttpSessionInstanceNumMin(long httpSessionInstanceNumMin)
	{
		this.httpSessionInstanceNumMin_ = httpSessionInstanceNumMin;
	}
}
