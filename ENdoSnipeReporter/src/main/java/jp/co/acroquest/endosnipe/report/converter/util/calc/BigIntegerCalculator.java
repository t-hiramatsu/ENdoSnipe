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

import java.math.BigInteger;

import jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator;

import org.apache.commons.beanutils.converters.BigIntegerConverter;

/**
 * BigInteger�^�ɓK�������v�Z���������s����v�Z�@�N���X
 * 
 * @author M.Yoshida
 */
public class BigIntegerCalculator implements Calculator 
{

	public Object add(Object obj1, Object obj2) 
	{
		BigInteger integerData1 = (BigInteger)obj1;
		BigInteger integerData2 = (BigInteger)obj2;
		
		return (Object)(integerData1.add(integerData2));
	}

	public Object div(Object obj1, Object obj2) 
	{
		BigInteger integerData1 = (BigInteger)obj1;
		BigInteger integerData2 = (BigInteger)obj2;
		
		return (Object)(integerData1.divide(integerData2));
	}

	public Object immediate(String str) 
	{
		BigIntegerConverter converter = new BigIntegerConverter();
		return converter.convert(BigInteger.class, str);
	}

	public Object mul(Object obj1, Object obj2) 
	{
		BigInteger integerData1 = (BigInteger)obj1;
		BigInteger integerData2 = (BigInteger)obj2;
		
		return (Object)(integerData1.multiply(integerData2));
	}

	public Object sub(Object obj1, Object obj2) 
	{
		BigInteger integerData1 = (BigInteger)obj1;
		BigInteger integerData2 = (BigInteger)obj2;
		
		return (Object)(integerData1.subtract(integerData2));
	}

}
