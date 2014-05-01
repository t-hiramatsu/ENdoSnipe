/*
 * Copyright (c) 2004-2008 SMG Co., Ltd. All Rights Reserved.
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
 * レポート出力用のエンティティ<br /> 
 *
 * @author eriguchi
 */
public class ReportItemValue
{
	/**
	 * 計測時刻。<br />
	 */
	public Timestamp measurementTime;

	/**
	 * 系列名<br />
	 */
	public String itemName;

	/**
	 * 計測値(上限値)。
	 */
	public Number limitValue;

	/**
	 * 計測値(サマリ)。
	 */
	public Number summaryValue;

	/**
	 * 計測値(最大)。
	 */
	public Number maxValue;

	/**
	 * 計測値(最小)。
	 */
	public Number minValue;

	/**
	 * 計測値のインデックス
	 */
	public int index;

	/**
	 * コンストラクタ
	 */
	public ReportItemValue()
	{
		itemName = "";
		limitValue = 0;
		summaryValue = 0;
		maxValue = 0;
		minValue = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[itemName=");
		builder.append(this.itemName);
		builder.append(", summaryValue=");
		builder.append(this.summaryValue);
		builder.append(", maxValue=");
		builder.append(this.maxValue);
		builder.append(", minValue=");
		builder.append(this.minValue);
		builder.append(", measurementTime=");
		builder.append(this.measurementTime);
		builder.append("]");
		return builder.toString();
	}
}
