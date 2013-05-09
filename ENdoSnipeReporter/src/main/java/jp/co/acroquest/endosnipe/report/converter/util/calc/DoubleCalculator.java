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

import org.apache.commons.beanutils.converters.DoubleConverter;

/**
 * Double型に適合した計算処理を実行する計算機クラス
 * 
 * @author M.Yoshida
 */
public class DoubleCalculator implements Calculator 
{

	public Object add(Object obj1, Object obj2) 
	{
		Double doubleData1 = (Double)obj1;
		Double doubleData2 = (Double)obj2;
		
		return (Object)(new Double((double)(doubleData1.doubleValue() + doubleData2.doubleValue())));
	}

	public Object div(Object obj1, Object obj2) 
	{
		Double doubleData1 = (Double)obj1;
		Double doubleData2 = (Double)obj2;
		
		return (Object)(new Double((double)(doubleData1.doubleValue() / doubleData2.doubleValue())));
	}

	public Object immediate(String str) 
	{
		DoubleConverter converter = new DoubleConverter();
		return converter.convert(Double.class, str);
	}

	public Object mul(Object obj1, Object obj2) 
	{
		Double doubleData1 = (Double)obj1;
		Double doubleData2 = (Double)obj2;
		
		return (Object)(new Double((double)(doubleData1.doubleValue() * doubleData2.doubleValue())));
	}

	public Object sub(Object obj1, Object obj2) 
	{
		Double doubleData1 = (Double)obj1;
		Double doubleData2 = (Double)obj2;
		
		return (Object)(new Double((double)(doubleData1.doubleValue() - doubleData2.doubleValue())));
	}

}
