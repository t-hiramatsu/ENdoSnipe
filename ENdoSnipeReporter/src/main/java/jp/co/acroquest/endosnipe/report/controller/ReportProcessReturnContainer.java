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
package jp.co.acroquest.endosnipe.report.controller;

/**
 * レポート処理プロセッサが呼び出し元に値を返すためのコンテナクラス。
 * 各プロセッサは、このコンテナクラスを継承して独自フィールドを定義すること。
 * 
 * @author M.Yoshida
 */
public class ReportProcessReturnContainer
{
	/** プロセッサで発生した例外 */
	private Throwable happendedError_;

	/**
	 * @return the happendedError
	 */
	public Throwable getHappendedError()
	{
		return happendedError_;
	}

	/**
	 * @param happendedError the happendedError to set
	 */
	public void setHappendedError(Throwable happendedError)
	{
		happendedError_ = happendedError;
	}

}
