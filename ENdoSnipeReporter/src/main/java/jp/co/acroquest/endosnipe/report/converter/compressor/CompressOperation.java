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
package jp.co.acroquest.endosnipe.report.converter.compressor;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;

/**
 * サンプリングの圧縮を行う際に、Compressorに圧縮処理の方法を
 * 指定するためのエンティティオブジェクト
 * 
 * @author M.Yoshida
 */
public class CompressOperation
{
	/** 圧縮時の計測値演算対象フィールド */
	private String compressField_;

	/** 圧縮時の演算処理 */
	private CompressOperator operation_;

	/**
	 * コンストラクタ。
	 * @param compressField 計測値演算対象フィールド
	 * @param operation     演算処理
	 */
	public CompressOperation(String compressField, CompressOperator operation)
	{
		this.compressField_ = compressField;
		this.operation_ = operation;
	}

	public String getCompressField()
	{
		return compressField_;
	}

	public void setCompressField(String compressField)
	{
		this.compressField_ = compressField;
	}

	public CompressOperator getOperation()
	{
		return operation_;
	}

	public void setOperation(CompressOperator operation)
	{
		this.operation_ = operation;
	}
}
