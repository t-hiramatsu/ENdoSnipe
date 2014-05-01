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
package jp.co.acroquest.endosnipe.report.controller.dispatcher;

import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;

/**
 * レポート処理プロセッサのインタフェース。
 * レポート処理プロセッサは、本インタフェースをimplementsして作成する。
 * 
 * @author M.Yoshida
 */
public interface ReportPublishProcessor
{
	/**
	 * レポート出力処理を行う。
	 * 
	 * @param cond レポート出力の際の条件
	 * @throws InterruptedException 割り込み発生時
	 * @return レポート出力の結果
	 */
	ReportProcessReturnContainer publish(ReportSearchCondition cond) throws InterruptedException;
}
