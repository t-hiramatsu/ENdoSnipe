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

import org.apache.commons.beanutils.converters.ByteConverter;

/**
 * Byte型に適合した計算処理を実行する計算機クラス
 * 
 * @author M.Yoshida
 */
public class ByteCalculator implements Calculator 
{

	/*
	 * (non-Javadoc)
	 * @see jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator#add(java.lang.Object, java.lang.Object)
	 */
	public Object add(Object obj1, Object obj2) 
	{
		Byte byteData1 = (Byte)obj1;
		Byte byteData2 = (Byte)obj2;
		
		return (Object)(new Byte((byte)(byteData1.byteValue() + byteData2.byteValue())));
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator#div(java.lang.Object, java.lang.Object)
	 */
	public Object div(Object obj1, Object obj2) 
	{
		Byte byteData1 = (Byte)obj1;
		Byte byteData2 = (Byte)obj2;

		return (Object)(new Byte((byte)(byteData1.byteValue() / byteData2.byteValue())));
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator#mul(java.lang.Object, java.lang.Object)
	 */
	public Object mul(Object obj1, Object obj2) 
	{
		Byte byteData1 = (Byte)obj1;
		Byte byteData2 = (Byte)obj2;

		return (Object)(new Byte((byte)(byteData1.byteValue() * byteData2.byteValue())));
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator#sub(java.lang.Object, java.lang.Object)
	 */
	public Object sub(Object obj1, Object obj2) 
	{
		Byte byteData1 = (Byte)obj1;
		Byte byteData2 = (Byte)obj2;

		return (Object)(new Byte((byte)(byteData1.byteValue() - byteData2.byteValue())));
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator#immediate(java.lang.String)
	 */
	public Object immediate(String str) 
	{
		ByteConverter converter = new ByteConverter();
		return converter.convert(Byte.class, str);
	}

}
