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

import java.sql.Timestamp;

/**
 * レスポンスタイムの計測時刻時点でのサマリを示すエンティティ
 * 
 * @author M.Yoshida
 */
public class ResponseTimeSummaryRecord
{
	/** 計測時刻 */
	private Timestamp measurementTime_;

	/** 対象URLにアクセスした回数 */
	private long webAccessCount_;

	/** 対象URLにアクセスした回数 期間最大 */
	private long webAccessCountMax_;

	/** 対象URLにアクセスした回数 期間最小 */
	private long webAccessCountMin_;

	/** 対象メソッドにアクセスした回数 */
	private long javaAccessCount_;

	/** 対象メソッドにアクセスした回数 期間最大 */
	private long javaAccessCountMax_;

	/** 対象メソッドにアクセスした回数 期間最小 */
	private long javaAccessCountMin_;

	/** 対象SQLにアクセスした回数 */
	private long sqlAccessCount_;

	/** 対象SQLにアクセスした回数 期間最大 */
	private long sqlAccessCountMax_;

	/** 対象SQLにアクセスした回数 期間最小 */
	private long sqlAccessCountMin_;

	/** 出力期間内の最小レスポンスタイム(Web)[ms] */
	private long webMinResponseTime_;

	/** 出力期間内の最小レスポンスタイム(Web)[ms] 期間最大 */
	private long webMinResponseTimeMax_;

	/** 出力期間内の最小レスポンスタイム(Web)[ms] 期間最小 */
	private long webMinResponseTimeMin_;

	/** 出力期間内の平均レスポンスタイム(Web)[ms] */
	private long webAveResponseTime_;

	/** 出力期間内の平均レスポンスタイム(Web)[ms] 期間最大 */
	private long webAveResponseTimeMax_;

	/** 出力期間内の平均レスポンスタイム(Web)[ms] 期間最小 */
	private long webAveResponseTimeMin_;

	/** 出力期間内の最大レスポンスタイム(Web)[ms] */
	private long webMaxResponseTime_;

	/** 出力期間内の最大レスポンスタイム(Web)[ms] 期間最大 */
	private long webMaxResponseTimeMax_;

	/** 出力期間内の最大レスポンスタイム(Web)[ms] 期間最小 */
	private long webMaxResponseTimeMin_;

	/** 出力期間内の最小レスポンスタイム(Java)[ms] */
	private long javaMinResponseTime_;

	/** 出力期間内の最小レスポンスタイム(Java)[ms] 期間最大 */
	private long javaMinResponseTimeMax_;

	/** 出力期間内の最小レスポンスタイム(Java)[ms] 期間最小 */
	private long javaMinResponseTimeMin_;

	/** 出力期間内の平均レスポンスタイム(Java)[ms] */
	private long javaAveResponseTime_;

	/** 出力期間内の平均レスポンスタイム(Java)[ms] 期間最大 */
	private long javaAveResponseTimeMax_;

	/** 出力期間内の平均レスポンスタイム(Java)[ms] 期間最小 */
	private long javaAveResponseTimeMin_;

	/** 出力期間内の最大レスポンスタイム(Java)[ms] */
	private long javaMaxResponseTime_;

	/** 出力期間内の最大レスポンスタイム(Java)[ms] 期間最大 */
	private long javaMaxResponseTimeMax_;

	/** 出力期間内の最大レスポンスタイム(Java)[ms] 期間最小 */
	private long javaMaxResponseTimeMin_;

	/** 出力期間内の最小レスポンスタイム(SQL)[ms] */
	private long sqlMinResponseTime_;

	/** 出力期間内の最小レスポンスタイム(SQL)[ms] 期間最大 */
	private long sqlMinResponseTimeMax_;

	/** 出力期間内の最小レスポンスタイム(SQL)[ms] 期間最小 */
	private long sqlMinResponseTimeMin_;

	/** 出力期間内の平均レスポンスタイム(SQL)[ms] */
	private long sqlAveResponseTime_;

	/** 出力期間内の平均レスポンスタイム(SQL)[ms] 期間最大 */
	private long sqlAveResponseTimeMax_;

	/** 出力期間内の平均レスポンスタイム(SQL)[ms] 期間最小 */
	private long sqlAveResponseTimeMin_;

	/** 出力期間内の最大レスポンスタイム(SQL)[ms] */
	private long sqlMaxResponseTime_;

	/** 出力期間内の最大レスポンスタイム(SQL)[ms] 期間最大 */
	private long sqlMaxResponseTimeMax_;

	/** 出力期間内の最大レスポンスタイム(SQL)[ms] 期間最小 */
	private long sqlMaxResponseTimeMin_;

	/** 対象URLで例外が発生した回数 */
	private long webExceptionCount_;

	/** 対象URLで例外が発生した回数 期間最大 */
	private long webExceptionCountMax_;

	/** 対象URLで例外が発生した回数 期間最小 */
	private long webExceptionCountMin_;

	/** 対象メソッドで例外が発生した回数 */
	private long javaExceptionCount_;

	/** 対象メソッドで例外が発生した回数 期間最大 */
	private long javaExceptionCountMax_;

	/** 対象メソッドで例外が発生した回数 期間最小 */
	private long javaExceptionCountMin_;

	/** 対象SQLで例外が発生した回数 */
	private long sqlExceptionCount_;

	/** 対象SQLで例外が発生した回数 期間最大 */
	private long sqlExceptionCountMax_;

	/** 対象SQLで例外が発生した回数 期間最小 */
	private long sqlExceptionCountMin_;

	/** 対象URLでストールが発生した回数 */
	private long webStallCount_;

	/** 対象URLでストールが発生した回数 期間最大 */
	private long webStallCountMax_;

	/** 対象URLでストールが発生した回数 期間最小 */
	private long webStallCountMin_;

	/** 対象メソッドでストールが発生した回数 */
	private long javaStallCount_;

	/** 対象メソッドでストールが発生した回数 期間最大 */
	private long javaStallCountMax_;

	/** 対象メソッドでストールが発生した回数 期間最小 */
	private long javaStallCountMin_;

	/** 対象SQLでストールが発生した回数 */
	private long sqlStallCount_;

	/** 対象SQLでストールが発生した回数 期間最大 */
	private long sqlStallCountMax_;

	/** 対象SQLでストールが発生した回数 期間最小 */
	private long sqlStallCountMin_;

	/**
	 * @return the measurementTime
	 */
	public Timestamp getMeasurementTime()
	{
		return measurementTime_;
	}

	/**
	 * @return the webAccessCount
	 */
	public long getWebAccessCount()
	{
		return webAccessCount_;
	}

	/**
	 * @return the webAccessCountMax
	 */
	public long getWebAccessCountMax()
	{
		return webAccessCountMax_;
	}

	/**
	 * @return the webAccessCountMin
	 */
	public long getWebAccessCountMin()
	{
		return webAccessCountMin_;
	}

	/**
	 * @return the javaAccessCount
	 */
	public long getJavaAccessCount()
	{
		return javaAccessCount_;
	}

	/**
	 * @return the javaAccessCountMax
	 */
	public long getJavaAccessCountMax()
	{
		return javaAccessCountMax_;
	}

	/**
	 * @return the javaAccessCountMin
	 */
	public long getJavaAccessCountMin()
	{
		return javaAccessCountMin_;
	}

	/**
	 * @return the sqlAccessCount
	 */
	public long getSqlAccessCount()
	{
		return sqlAccessCount_;
	}

	/**
	 * @return the sqlAccessCountMax
	 */
	public long getSqlAccessCountMax()
	{
		return sqlAccessCountMax_;
	}

	/**
	 * @return the sqlAccessCountMin
	 */
	public long getSqlAccessCountMin()
	{
		return sqlAccessCountMin_;
	}

	/**
	 * @return the webMinResponseTime
	 */
	public long getWebMinResponseTime()
	{
		return webMinResponseTime_;
	}

	/**
	 * @return the webMinResponseTimeMax
	 */
	public long getWebMinResponseTimeMax()
	{
		return webMinResponseTimeMax_;
	}

	/**
	 * @return the webMinResponseTimeMin
	 */
	public long getWebMinResponseTimeMin()
	{
		return webMinResponseTimeMin_;
	}

	/**
	 * @return the webAveResponseTime
	 */
	public long getWebAveResponseTime()
	{
		return webAveResponseTime_;
	}

	/**
	 * @return the webAveResponseTimeMax
	 */
	public long getWebAveResponseTimeMax()
	{
		return webAveResponseTimeMax_;
	}

	/**
	 * @return the webAveResponseTimeMin
	 */
	public long getWebAveResponseTimeMin()
	{
		return webAveResponseTimeMin_;
	}

	/**
	 * @return the webMaxResponseTime
	 */
	public long getWebMaxResponseTime()
	{
		return webMaxResponseTime_;
	}

	/**
	 * @return the webMaxResponseTimeMax
	 */
	public long getWebMaxResponseTimeMax()
	{
		return webMaxResponseTimeMax_;
	}

	/**
	 * @return the webMaxResponseTimeMin
	 */
	public long getWebMaxResponseTimeMin()
	{
		return webMaxResponseTimeMin_;
	}

	/**
	 * @return the javaMinResponseTime
	 */
	public long getJavaMinResponseTime()
	{
		return javaMinResponseTime_;
	}

	/**
	 * @return the javaMinResponseTimeMax
	 */
	public long getJavaMinResponseTimeMax()
	{
		return javaMinResponseTimeMax_;
	}

	/**
	 * @return the javaMinResponseTimeMin
	 */
	public long getJavaMinResponseTimeMin()
	{
		return javaMinResponseTimeMin_;
	}

	/**
	 * @return the javaAveResponseTime
	 */
	public long getJavaAveResponseTime()
	{
		return javaAveResponseTime_;
	}

	/**
	 * @return the javaAveResponseTimeMax
	 */
	public long getJavaAveResponseTimeMax()
	{
		return javaAveResponseTimeMax_;
	}

	/**
	 * @return the javaAveResponseTimeMin
	 */
	public long getJavaAveResponseTimeMin()
	{
		return javaAveResponseTimeMin_;
	}

	/**
	 * @return the javaMaxResponseTime
	 */
	public long getJavaMaxResponseTime()
	{
		return javaMaxResponseTime_;
	}

	/**
	 * @return the javaMaxResponseTimeMax
	 */
	public long getJavaMaxResponseTimeMax()
	{
		return javaMaxResponseTimeMax_;
	}

	/**
	 * @return the javaMaxResponseTimeMin
	 */
	public long getJavaMaxResponseTimeMin()
	{
		return javaMaxResponseTimeMin_;
	}

	/**
	 * @return the sqlMinResponseTime
	 */
	public long getSqlMinResponseTime()
	{
		return sqlMinResponseTime_;
	}

	/**
	 * @return the sqlMinResponseTimeMax
	 */
	public long getSqlMinResponseTimeMax()
	{
		return sqlMinResponseTimeMax_;
	}

	/**
	 * @return the sqlMinResponseTimeMin
	 */
	public long getSqlMinResponseTimeMin()
	{
		return sqlMinResponseTimeMin_;
	}

	/**
	 * @return the sqlAveResponseTime
	 */
	public long getSqlAveResponseTime()
	{
		return sqlAveResponseTime_;
	}

	/**
	 * @return the sqlAveResponseTimeMax
	 */
	public long getSqlAveResponseTimeMax()
	{
		return sqlAveResponseTimeMax_;
	}

	/**
	 * @return the sqlAveResponseTimeMin
	 */
	public long getSqlAveResponseTimeMin()
	{
		return sqlAveResponseTimeMin_;
	}

	/**
	 * @return the sqlMaxResponseTime
	 */
	public long getSqlMaxResponseTime()
	{
		return sqlMaxResponseTime_;
	}

	/**
	 * @return the sqlMaxResponseTimeMax
	 */
	public long getSqlMaxResponseTimeMax()
	{
		return sqlMaxResponseTimeMax_;
	}

	/**
	 * @return the sqlMaxResponseTimeMin
	 */
	public long getSqlMaxResponseTimeMin()
	{
		return sqlMaxResponseTimeMin_;
	}

	/**
	 * @param measurementTime the measurementTime to set
	 */
	public void setMeasurementTime(Timestamp measurementTime)
	{
		measurementTime_ = measurementTime;
	}

	/**
	 * @param webAccessCount the webAccessCount to set
	 */
	public void setWebAccessCount(long webAccessCount)
	{
		webAccessCount_ = webAccessCount;
	}

	/**
	 * @param webAccessCountMax the webAccessCountMax to set
	 */
	public void setWebAccessCountMax(long webAccessCountMax)
	{
		webAccessCountMax_ = webAccessCountMax;
	}

	/**
	 * @param webAccessCountMin the webAccessCountMin to set
	 */
	public void setWebAccessCountMin(long webAccessCountMin)
	{
		webAccessCountMin_ = webAccessCountMin;
	}

	/**
	 * @param javaAccessCount the javaAccessCount to set
	 */
	public void setJavaAccessCount(long javaAccessCount)
	{
		javaAccessCount_ = javaAccessCount;
	}

	/**
	 * @param javaAccessCountMax the javaAccessCountMax to set
	 */
	public void setJavaAccessCountMax(long javaAccessCountMax)
	{
		javaAccessCountMax_ = javaAccessCountMax;
	}

	/**
	 * @param javaAccessCountMin the javaAccessCountMin to set
	 */
	public void setJavaAccessCountMin(long javaAccessCountMin)
	{
		javaAccessCountMin_ = javaAccessCountMin;
	}

	/**
	 * @param sqlAccessCount the sqlAccessCount to set
	 */
	public void setSqlAccessCount(long sqlAccessCount)
	{
		sqlAccessCount_ = sqlAccessCount;
	}

	/**
	 * @param sqlAccessCountMax the sqlAccessCountMax to set
	 */
	public void setSqlAccessCountMax(long sqlAccessCountMax)
	{
		sqlAccessCountMax_ = sqlAccessCountMax;
	}

	/**
	 * @param sqlAccessCountMin the sqlAccessCountMin to set
	 */
	public void setSqlAccessCountMin(long sqlAccessCountMin)
	{
		sqlAccessCountMin_ = sqlAccessCountMin;
	}

	/**
	 * @param webMinResponseTime the webMinResponseTime to set
	 */
	public void setWebMinResponseTime(long webMinResponseTime)
	{
		webMinResponseTime_ = webMinResponseTime;
	}

	/**
	 * @param webMinResponseTimeMax the webMinResponseTimeMax to set
	 */
	public void setWebMinResponseTimeMax(long webMinResponseTimeMax)
	{
		webMinResponseTimeMax_ = webMinResponseTimeMax;
	}

	/**
	 * @param webMinResponseTimeMin the webMinResponseTimeMin to set
	 */
	public void setWebMinResponseTimeMin(long webMinResponseTimeMin)
	{
		webMinResponseTimeMin_ = webMinResponseTimeMin;
	}

	/**
	 * @param webAveResponseTime the webAveResponseTime to set
	 */
	public void setWebAveResponseTime(long webAveResponseTime)
	{
		webAveResponseTime_ = webAveResponseTime;
	}

	/**
	 * @param webAveResponseTimeMax the webAveResponseTimeMax to set
	 */
	public void setWebAveResponseTimeMax(long webAveResponseTimeMax)
	{
		webAveResponseTimeMax_ = webAveResponseTimeMax;
	}

	/**
	 * @param webAveResponseTimeMin the webAveResponseTimeMin to set
	 */
	public void setWebAveResponseTimeMin(long webAveResponseTimeMin)
	{
		webAveResponseTimeMin_ = webAveResponseTimeMin;
	}

	/**
	 * @param webMaxResponseTime the webMaxResponseTime to set
	 */
	public void setWebMaxResponseTime(long webMaxResponseTime)
	{
		webMaxResponseTime_ = webMaxResponseTime;
	}

	/**
	 * @param webMaxResponseTimeMax the webMaxResponseTimeMax to set
	 */
	public void setWebMaxResponseTimeMax(long webMaxResponseTimeMax)
	{
		webMaxResponseTimeMax_ = webMaxResponseTimeMax;
	}

	/**
	 * @param webMaxResponseTimeMin the webMaxResponseTimeMin to set
	 */
	public void setWebMaxResponseTimeMin(long webMaxResponseTimeMin)
	{
		webMaxResponseTimeMin_ = webMaxResponseTimeMin;
	}

	/**
	 * @param javaMinResponseTime the javaMinResponseTime to set
	 */
	public void setJavaMinResponseTime(long javaMinResponseTime)
	{
		javaMinResponseTime_ = javaMinResponseTime;
	}

	/**
	 * @param javaMinResponseTimeMax the javaMinResponseTimeMax to set
	 */
	public void setJavaMinResponseTimeMax(long javaMinResponseTimeMax)
	{
		javaMinResponseTimeMax_ = javaMinResponseTimeMax;
	}

	/**
	 * @param javaMinResponseTimeMin the javaMinResponseTimeMin to set
	 */
	public void setJavaMinResponseTimeMin(long javaMinResponseTimeMin)
	{
		javaMinResponseTimeMin_ = javaMinResponseTimeMin;
	}

	/**
	 * @param javaAveResponseTime the javaAveResponseTime to set
	 */
	public void setJavaAveResponseTime(long javaAveResponseTime)
	{
		javaAveResponseTime_ = javaAveResponseTime;
	}

	/**
	 * @param javaAveResponseTimeMax the javaAveResponseTimeMax to set
	 */
	public void setJavaAveResponseTimeMax(long javaAveResponseTimeMax)
	{
		javaAveResponseTimeMax_ = javaAveResponseTimeMax;
	}

	/**
	 * @param javaAveResponseTimeMin the javaAveResponseTimeMin to set
	 */
	public void setJavaAveResponseTimeMin(long javaAveResponseTimeMin)
	{
		javaAveResponseTimeMin_ = javaAveResponseTimeMin;
	}

	/**
	 * @param javaMaxResponseTime the javaMaxResponseTime to set
	 */
	public void setJavaMaxResponseTime(long javaMaxResponseTime)
	{
		javaMaxResponseTime_ = javaMaxResponseTime;
	}

	/**
	 * @param javaMaxResponseTimeMax the javaMaxResponseTimeMax to set
	 */
	public void setJavaMaxResponseTimeMax(long javaMaxResponseTimeMax)
	{
		javaMaxResponseTimeMax_ = javaMaxResponseTimeMax;
	}

	/**
	 * @param javaMaxResponseTimeMin the javaMaxResponseTimeMin to set
	 */
	public void setJavaMaxResponseTimeMin(long javaMaxResponseTimeMin)
	{
		javaMaxResponseTimeMin_ = javaMaxResponseTimeMin;
	}

	/**
	 * @param sqlMinResponseTime the sqlMinResponseTime to set
	 */
	public void setSqlMinResponseTime(long sqlMinResponseTime)
	{
		sqlMinResponseTime_ = sqlMinResponseTime;
	}

	/**
	 * @param sqlMinResponseTimeMax the sqlMinResponseTimeMax to set
	 */
	public void setSqlMinResponseTimeMax(long sqlMinResponseTimeMax)
	{
		sqlMinResponseTimeMax_ = sqlMinResponseTimeMax;
	}

	/**
	 * @param sqlMinResponseTimeMin the sqlMinResponseTimeMin to set
	 */
	public void setSqlMinResponseTimeMin(long sqlMinResponseTimeMin)
	{
		sqlMinResponseTimeMin_ = sqlMinResponseTimeMin;
	}

	/**
	 * @param sqlAveResponseTime the sqlAveResponseTime to set
	 */
	public void setSqlAveResponseTime(long sqlAveResponseTime)
	{
		sqlAveResponseTime_ = sqlAveResponseTime;
	}

	/**
	 * @param sqlAveResponseTimeMax the sqlAveResponseTimeMax to set
	 */
	public void setSqlAveResponseTimeMax(long sqlAveResponseTimeMax)
	{
		sqlAveResponseTimeMax_ = sqlAveResponseTimeMax;
	}

	/**
	 * @param sqlAveResponseTimeMin the sqlAveResponseTimeMin to set
	 */
	public void setSqlAveResponseTimeMin(long sqlAveResponseTimeMin)
	{
		sqlAveResponseTimeMin_ = sqlAveResponseTimeMin;
	}

	/**
	 * @param sqlMaxResponseTime the sqlMaxResponseTime to set
	 */
	public void setSqlMaxResponseTime(long sqlMaxResponseTime)
	{
		sqlMaxResponseTime_ = sqlMaxResponseTime;
	}

	/**
	 * @param sqlMaxResponseTimeMax the sqlMaxResponseTimeMax to set
	 */
	public void setSqlMaxResponseTimeMax(long sqlMaxResponseTimeMax)
	{
		sqlMaxResponseTimeMax_ = sqlMaxResponseTimeMax;
	}

	/**
	 * @param sqlMaxResponseTimeMin the sqlMaxResponseTimeMin to set
	 */
	public void setSqlMaxResponseTimeMin(long sqlMaxResponseTimeMin)
	{
		sqlMaxResponseTimeMin_ = sqlMaxResponseTimeMin;
	}

	public long getWebExceptionCount()
	{
		return webExceptionCount_;
	}

	public void setWebExceptionCount(long webExceptionCount)
	{
		webExceptionCount_ = webExceptionCount;
	}

	public long getWebExceptionCountMax()
	{
		return webExceptionCountMax_;
	}

	public void setWebExceptionCountMax(long webExceptionCountMax)
	{
		webExceptionCountMax_ = webExceptionCountMax;
	}

	public long getWebExceptionCountMin()
	{
		return webExceptionCountMin_;
	}

	public void setWebExceptionCountMin(long webExceptionCountMin)
	{
		webExceptionCountMin_ = webExceptionCountMin;
	}

	public long getJavaExceptionCount()
	{
		return javaExceptionCount_;
	}

	public void setJavaExceptionCount(long javaExceptionCount)
	{
		javaExceptionCount_ = javaExceptionCount;
	}

	public long getJavaExceptionCountMax()
	{
		return javaExceptionCountMax_;
	}

	public void setJavaExceptionCountMax(long javaExceptionCountMax)
	{
		javaExceptionCountMax_ = javaExceptionCountMax;
	}

	public long getJavaExceptionCountMin()
	{
		return javaExceptionCountMin_;
	}

	public void setJavaExceptionCountMin(long javaExceptionCountMin)
	{
		javaExceptionCountMin_ = javaExceptionCountMin;
	}

	public long getSqlExceptionCount()
	{
		return sqlExceptionCount_;
	}

	public void setSqlExceptionCount(long sqlExceptionCount)
	{
		sqlExceptionCount_ = sqlExceptionCount;
	}

	public long getSqlExceptionCountMax()
	{
		return sqlExceptionCountMax_;
	}

	public void setSqlExceptionCountMax(long sqlExceptionCountMax)
	{
		sqlExceptionCountMax_ = sqlExceptionCountMax;
	}

	public long getSqlExceptionCountMin()
	{
		return sqlExceptionCountMin_;
	}

	public void setSqlExceptionCountMin(long sqlExceptionCountMin)
	{
		sqlExceptionCountMin_ = sqlExceptionCountMin;
	}

	/**
	 * @return the webStallCount
	 */
	public long getWebStallCount()
	{
		return webStallCount_;
	}

	/**
	 * @param webStallCount the webStallCount to set
	 */
	public void setWebStallCount(long webStallCount)
	{
		this.webStallCount_ = webStallCount;
	}

	/**
	 * @return the webStallCountMax
	 */
	public long getWebStallCountMax()
	{
		return webStallCountMax_;
	}

	/**
	 * @param webStallCountMax the webStallCountMax to set
	 */
	public void setWebStallCountMax(long webStallCountMax)
	{
		this.webStallCountMax_ = webStallCountMax;
	}

	/**
	 * @return the webStallCountMin
	 */
	public long getWebStallCountMin()
	{
		return webStallCountMin_;
	}

	/**
	 * @param webStallCountMin the webStallCountMin to set
	 */
	public void setWebStallCountMin(long webStallCountMin)
	{
		this.webStallCountMin_ = webStallCountMin;
	}

	/**
	 * @return the javaStallCount
	 */
	public long getJavaStallCount()
	{
		return javaStallCount_;
	}

	/**
	 * @param javaStallCount the javaStallCount to set
	 */
	public void setJavaStallCount(long javaStallCount)
	{
		this.javaStallCount_ = javaStallCount;
	}

	/**
	 * @return the javaStallCountMax
	 */
	public long getJavaStallCountMax()
	{
		return javaStallCountMax_;
	}

	/**
	 * @param javaStallCountMax the javaStallCountMax to set
	 */
	public void setJavaStallCountMax(long javaStallCountMax)
	{
		this.javaStallCountMax_ = javaStallCountMax;
	}

	/**
	 * @return the javaStallCountMin
	 */
	public long getJavaStallCountMin()
	{
		return javaStallCountMin_;
	}

	/**
	 * @param javaStallCountMin the javaStallCountMin to set
	 */
	public void setJavaStallCountMin(long javaStallCountMin)
	{
		this.javaStallCountMin_ = javaStallCountMin;
	}

	/**
	 * @return the sqlStallCount
	 */
	public long getSqlStallCount()
	{
		return sqlStallCount_;
	}

	/**
	 * @param sqlStallCount the sqlStallCount to set
	 */
	public void setSqlStallCount(long sqlStallCount)
	{
		this.sqlStallCount_ = sqlStallCount;
	}

	/**
	 * @return the sqlStallCountMax
	 */
	public long getSqlStallCountMax()
	{
		return sqlStallCountMax_;
	}

	/**
	 * @param sqlStallCountMax the sqlStallCountMax to set
	 */
	public void setSqlStallCountMax(long sqlStallCountMax)
	{
		this.sqlStallCountMax_ = sqlStallCountMax;
	}

	/**
	 * @return the sqlStallCountMin
	 */
	public long getSqlStallCountMin()
	{
		return sqlStallCountMin_;
	}

	/**
	 * @param sqlStallCountMin the sqlStallCountMin to set
	 */
	public void setSqlStallCountMin(long sqlStallCountMin)
	{
		this.sqlStallCountMin_ = sqlStallCountMin;
	}

}
