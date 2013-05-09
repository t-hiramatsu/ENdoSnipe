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
import java.math.BigInteger;

import jp.co.acroquest.endosnipe.report.converter.util.calc.BigDecimalCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.BigIntegerCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.ByteCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.DoubleCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.FloatCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.IntegerCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.LongCalculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.ShortCalculator;

/**
 * 型に対応する計算機クラスを生成するFactoryクラス
 * 
 * @author M.Yoshida
 *
 */
public class CalculatorFactory 
{
	public static Calculator createCalculator(Class<?> clazz)
	{
		if(byte.class.equals(clazz) || Byte.class.equals(clazz))
		{
			return new ByteCalculator();
		}
		
		if(short.class.equals(clazz) || Short.class.equals(clazz))
		{
			return new ShortCalculator();
		}
		
		if(int.class.equals(clazz) || Integer.class.equals(clazz))
		{
			return new IntegerCalculator();
		}
		
		if(long.class.equals(clazz) || Long.class.equals(clazz))
		{
			return new LongCalculator();
		}
		
		if(float.class.equals(clazz) || Float.class.equals(clazz))
		{
			return new FloatCalculator();
		}
		
		if(double.class.equals(clazz) || Double.class.equals(clazz))
		{
			return new DoubleCalculator();
		}
		
		if(BigDecimal.class.equals(clazz))
		{
			return new BigDecimalCalculator();
		}
		
		if(BigInteger.class.equals(clazz))
		{
			return new BigIntegerCalculator();
		}
		
		return null;
	}
}
