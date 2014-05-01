/*
 * Copyright (c) 2004-2010 SMG Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.report.dao.util;

import java.util.List;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.util.ResourceDataUtil;

/**
 * 割合を表すグラフのデータの変換を行う、ユーティリティクラスです。
 * 
 * @author iida
 */
public class PercentageDataUtil
{
	/**
	 * コンストラクタを隠蔽します。<br />
	 */
	private PercentageDataUtil()
	{
		// DO NOTHING
	}

	/**
	 * 指定された、割合を表すグラフのデータを、倍率を掛けられる前のものに戻します。<br />
	 * 
	 * @param percentageValues
	 *            倍率を掛けられた後のデータ
	 * @return 倍率を掛けられる前のデータ
	 */
	public static List<ReportItemValue> reconstitutePercentageData(
		List<ReportItemValue> percentageValues)
	{
		for (ReportItemValue value : percentageValues)
		{
			value.maxValue = value.maxValue.intValue()
				/ ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
			value.minValue = value.minValue.intValue()
				/ ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
			value.summaryValue = value.summaryValue.intValue()
				/ ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION;
		}
		return percentageValues;
	}
}
