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

/**
 * Objectが示すデータ型に適合した計算処理を実行するためのインタフェース
 * 
 * @author M.Yoshida
 */
public interface Calculator
{
	/**
	 * 2つのデータを「加算」した結果を返す。
	 * 
	 * @param obj1 データ１
	 * @param obj2 データ２
	 * @return 加算結果
	 */
	public Object add(Object obj1, Object obj2);

	/**
	 * 2つのデータを「減算」した結果を返す。
	 * 
	 * @param obj1 データ１
	 * @param obj2 データ２
	 * @return 減算結果
	 */
	public Object sub(Object obj1, Object obj2);

	/**
	 * 2つのデータを「乗算」した結果を返す。
	 * 
	 * @param obj1 データ１
	 * @param obj2 データ２
	 * @return 乗算結果
	 */
	public Object mul(Object obj1, Object obj2);

	/**
	 * 2つのデータを「除算」した結果を返す。
	 * 
	 * @param obj1 データ１
	 * @param obj2 データ２
	 * @return 除算結果
	 */
	public Object div(Object obj1, Object obj2);

	/**
	 * 文字列表現で示された値を適合する型のオブジェクトに変換して返す。
	 * 
	 * @param str 値の文字列表現
	 * @return 値
	 */
	public Object immediate(String str);
}
