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

import java.math.BigDecimal;

import jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator;

import org.apache.commons.beanutils.converters.BigDecimalConverter;

/**
 * BigDecimal型に適合した計算処理を実行する計算機クラス
 * 
 * @author M.Yoshida
 */
public class BigDecimalCalculator implements Calculator 
{

	public Object add(Object obj1, Object obj2) 
	{
		BigDecimal decimalData1 = (BigDecimal)obj1;
		BigDecimal decimalData2 = (BigDecimal)obj2;
		
		return (Object)(decimalData1.add(decimalData2));
	}

	public Object div(Object obj1, Object obj2) 
	{
		BigDecimal decimalData1 = (BigDecimal)obj1;
		BigDecimal decimalData2 = (BigDecimal)obj2;
		
		return (Object)(decimalData1.divide(decimalData2, BigDecimal.ROUND_DOWN));
	}

	public Object immediate(String str) 
	{
		BigDecimalConverter converter = new BigDecimalConverter();
		return converter.convert(BigDecimal.class, str);
	}

	public Object mul(Object obj1, Object obj2) 
	{
		BigDecimal decimalData1 = (BigDecimal)obj1;
		BigDecimal decimalData2 = (BigDecimal)obj2;
		
		return (Object)(decimalData1.multiply(decimalData2));
	}

	public Object sub(Object obj1, Object obj2) 
	{
		BigDecimal decimalData1 = (BigDecimal)obj1;
		BigDecimal decimalData2 = (BigDecimal)obj2;
		
		return (Object)(decimalData1.subtract(decimalData2));
	}

}
