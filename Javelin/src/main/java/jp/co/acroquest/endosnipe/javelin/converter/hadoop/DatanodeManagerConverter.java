/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * DatanodeManager用のコンバータ
 * 
 * @author hiramatsu
 */
public class DatanodeManagerConverter extends AbstractConverter 
{
	/** コンバータ名 */
	private static final String CONVERTER_NAME = "DatanodeManagerConverter";

	/**
	 * {@inheritDoc}
	 */
	public void init() 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void convertImpl() throws CannotCompileException, NotFoundException,
			IOException 
			{
		CtClass ctClass = getCtClass();
		
		// インターフェースの追加
		CtClass accessorClass = getClassPool().get(
				DatanodeManagerAccessor.class.getName());
		ctClass.addInterface(accessorClass);

		CtMethod resolveMethod = CtMethod.make("" + //
				"public java.util.List resolve(java.util.List names){ " + //
				"        return this.dnsToSwitchMapping.resolve(names);" + //
				"}", ctClass);
		ctClass.addMethod(resolveMethod);

		setNewClassfileBuffer(ctClass.toBytecode());
		
		logModifiedMethod(CONVERTER_NAME, resolveMethod);
	}
}
