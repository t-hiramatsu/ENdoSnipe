package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * 「アプリケーション」タブの「HttpSessionへの登録オブジェクト総サイズ」のレポートにおいて、
 * 出力される情報の中の1レコード分を保持するエンティティです。
 * 
 * @author T. Iida
 */
public class HttpSessionObjectSizeRecord
{
	/** 計測時刻 */
	private Timestamp measurementTime_;

	/** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間平均） */
	private long httpSessionObjectSize_;

	/** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間最大） */
	private long httpSessionObjectSizeMax_;

	/** HttpSessionへの登録オブジェクト総サイズ[サイズ(MB)]（補間期間最小） */
	private long httpSessionObjectSizeMin_;

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
	 * @return the httpSessionObjectSize_
	 */
	public long getHttpSessionObjectSize()
	{
		return httpSessionObjectSize_;
	}

	/**
	 * @param httpSessionObjectSize the httpSessionObjectSize_ to set
	 */
	public void setHttpSessionObjectSize(long httpSessionObjectSize)
	{
		this.httpSessionObjectSize_ = httpSessionObjectSize;
	}

	/**
	 * @return the httpSessionObjectSizeMax_
	 */
	public long getHttpSessionObjectSizeMax()
	{
		return httpSessionObjectSizeMax_;
	}

	/**
	 * @param httpSessionObjectSizeMax the httpSessionObjectSizeMax_ to set
	 */
	public void setHttpSessionObjectSizeMax(long httpSessionObjectSizeMax)
	{
		this.httpSessionObjectSizeMax_ = httpSessionObjectSizeMax;
	}

	/**
	 * @return the httpSessionObjectSizeMin_
	 */
	public long getHttpSessionObjectSizeMin()
	{
		return httpSessionObjectSizeMin_;
	}

	/**
	 * @param httpSessionObjectSizeMin the httpSessionObjectSizeMin_ to set
	 */
	public void setHttpSessionObjectSizeMin(long httpSessionObjectSizeMin)
	{
		this.httpSessionObjectSizeMin_ = httpSessionObjectSizeMin;
	}
}
