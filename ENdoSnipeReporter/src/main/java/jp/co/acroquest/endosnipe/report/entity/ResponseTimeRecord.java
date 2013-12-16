/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.entity;

/**
 * レスポンスタイムレポートに出力する１レコード分のデータを示すエンティティクラス。
 * 
 * @author M.Yoshida
 */
public class ResponseTimeRecord
{

	/** アクセス対象のURL/メソッド名 */
	private String accessTarget_;

	/** 対象URL/メソッドにアクセスした回数 */
	private long accessCount_;

	/** 出力期間内の最小レスポンスタイム[ms] */
	private long minResponseTime_;

	/** 出力期間内の平均レスポンスタイム[ms] */
	private long aveResponseTime_;

	/** 出力期間内の最大レスポンスタイム[ms] */
	private long maxResponseTime_;

	/**
	 * @return the accessTarget
	 */
	public String getAccessTarget()
	{
		return accessTarget_;
	}

	/**
	 * @return the accessCount
	 */
	public long getAccessCount()
	{
		return accessCount_;
	}

	/**
	 * @return the minResponseTime
	 */
	public long getMinResponseTime()
	{
		return minResponseTime_;
	}

	/**
	 * @return the aveResponseTime
	 */
	public long getAveResponseTime()
	{
		return aveResponseTime_;
	}

	/**
	 * @return the maxResponseTime
	 */
	public long getMaxResponseTime()
	{
		return maxResponseTime_;
	}

	/**
	 * @param accessTarget the accessTarget to set
	 */
	public void setAccessTarget(String accessTarget)
	{
		accessTarget_ = accessTarget;
	}

	/**
	 * @param accessCount the accessCount to set
	 */
	public void setAccessCount(long accessCount)
	{
		accessCount_ = accessCount;
	}

	/**
	 * @param minResponseTime the minResponseTime to set
	 */
	public void setMinResponseTime(long minResponseTime)
	{
		minResponseTime_ = minResponseTime;
	}

	/**
	 * @param aveResponseTime the aveResponseTime to set
	 */
	public void setAveResponseTime(long aveResponseTime)
	{
		aveResponseTime_ = aveResponseTime;
	}

	/**
	 * @param maxResponseTime the maxResponseTime to set
	 */
	public void setMaxResponseTime(long maxResponseTime)
	{
		maxResponseTime_ = maxResponseTime;
	}

}
