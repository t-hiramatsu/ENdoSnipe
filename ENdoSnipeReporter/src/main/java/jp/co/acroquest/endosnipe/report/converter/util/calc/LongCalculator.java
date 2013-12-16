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
package jp.co.acroquest.endosnipe.report.converter.util.calc;

import jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator;

import org.apache.commons.beanutils.converters.LongConverter;

/**
 * Long型に適合した計算処理を実行する計算機クラス
 * 
 * @author M.Yoshida
 */
public class LongCalculator implements Calculator
{

	public Object add(Object obj1, Object obj2)
	{
		Long longData1 = (Long) obj1;
		Long longData2 = (Long) obj2;

		return (Object) (new Long((long) (longData1.longValue() + longData2.longValue())));
	}

	public Object div(Object obj1, Object obj2)
	{
		Long longData1 = (Long) obj1;
		Long longData2 = (Long) obj2;

		return (Object) (new Long((long) (longData1.longValue() / longData2.longValue())));
	}

	public Object immediate(String str)
	{
		LongConverter converter = new LongConverter();
		return converter.convert(Long.class, str);
	}

	public Object mul(Object obj1, Object obj2)
	{
		Long longData1 = (Long) obj1;
		Long longData2 = (Long) obj2;

		return (Object) (new Long((long) (longData1.longValue() * longData2.longValue())));
	}

	public Object sub(Object obj1, Object obj2)
	{
		Long longData1 = (Long) obj1;
		Long longData2 = (Long) obj2;

		return (Object) (new Long((long) (longData1.longValue() - longData2.longValue())));
	}

}
