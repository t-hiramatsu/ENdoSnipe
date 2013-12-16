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
 * レポートの「種類」を示す列挙型
 * 種類毎にプロパティキーを導くための「ID」を持つ。
 * 
 * @author M.Yoshida
 *
 */
public enum ReportType
{
	/** システムリソース使用推移レポート */
	SYSTEM("reporter.report.type.system"),
	/** プロセスリソース使用推移レポート */
	PROCESS("reporter.report.type.process"),
	/** データ入出力レポート */
	DATA_IO("reporter.report.type.dataIO"),
	/** VM状態レポート */
	VM_STATUS("reporter.report.type.vmStatus"),
	/** オブジェクト数レポート */
	OBJECT("reporter.report.type.object"),
	/** レスポンスタイムサマリレポート */
	RESPONSE_SUMMARY("reporter.report.type.responseSummary"),
	/** アクセス対象別レスポンスタイムレポート */
	RESPONSE_LIST("reporter.report.type.responseList"),
	/** アプリケーションレポート */
	APPLICATION("reporter.report.type.application"),
	/** アプリケーションレポートのCommons Poolのサイズのレポート */
	SERVER_POOL("reporter.report.type.serverPool"),
	/** アプリケーションレポートのAPサーバのワーカスレッド数のレポート */
	POOL_SIZE("reporter.report.type.poolSize"),
	/** Javelinレポート */
	JAVELIN("reporter.report.type.javelin"),
	/** イベント種別毎のイベント発生回数レポート */
	EVENT("reporter.report.type.javelin.event"),
	/** PerformanceDoctorレポート */
	PERF_DOCTOR("reporter.report.type.perfDoctor"),
	/** レスポンスタイムサマリレポート */
	ITEM("reporter.report.type.item"),
	/** 平均サマリのレポート */
	OBJECT_AVERAGE("reporter.report.type.object.average"),
	/** 積算サマリグラフのレポート */
	OBJECT_TOTAL("reporter.report.type.object.total"),
	/** サマリレポート */
	SUMMARY("reporter.report.type.summary");

	/** ID */
	private String id_;

	/**
	 * コンストラクタ
	 * @param id
	 */
	private ReportType(String id)
	{
		this.id_ = id;
	}

	/**
	 * レポートの種類に対応する「ID」を取得する。
	 * 
	 * @return ID
	 */
	public String getId()
	{
		return this.id_;
	}
}
