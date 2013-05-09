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

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.rule.RuleManager;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleSetConfig;
import jp.co.acroquest.endosnipe.report.controller.ReportPublishTask;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;

/**
 * データ取得、レポート出力を管理するクラス
 * 
 * @author M.Yoshida
 */
public class ReportPublisher {
	/**
	 * データベースから取得したデータをエクセルに出力する
	 * 
	 * @param dbName
	 *            データベース名称
	 * @param outputReportTypes
	 *            出力レポート種別
	 * @param outputFilePath
	 *            出力パス
	 * @param startDate
	 *            開始時刻
	 * @param endDate
	 *            終了時刻
	 * @param limitSameCause
	 *            PerformanceDoctorレポート出力で、同一原因を絞り込むかどうか
	 * @param limitBySameRule
	 *            レポート出力で、同一ルールを絞り込むかどうか
	 * @param selectionIndex
	 * @param callback 
	 */
	public void outputReport(String[] dbName, ReportType[] outputReportTypes,
            String outputFilePath, Date startDate, Date endDate, boolean limitSameCause,
            boolean limitBySameRule, int selectionIndex, Runnable callback)
    {
		
		RuleManager ruleManager = RuleManager.getInstance();
		RuleSetConfig[] ruleSetConfigs = ruleManager.getRuleSetConfigs();
		String id = ruleSetConfigs[selectionIndex].getId();

		ruleManager.changeActiveRuleSetByID(id);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String start = format.format(startDate);
		String end   = format.format(endDate);
		String leafDirectoryName = start + "-" + end;
		outputFilePath = outputFilePath + File.separator + leafDirectoryName;
		
		ReportSearchCondition searchCondition = new ReportSearchCondition();

		searchCondition.setDatabases(Arrays.asList(dbName));
		searchCondition.setStartDate(new Timestamp(startDate.getTime()));
		searchCondition.setEndDate(new Timestamp(endDate.getTime()));
		searchCondition.setOutputFilePath(outputFilePath);
		searchCondition.setLimitSameCause(limitSameCause);
		searchCondition.setLimitBySameRule(limitBySameRule);

		File outputDir = new File(outputFilePath);

		if (outputDir.exists() == false) {
			outputDir.mkdirs();
		}

		ReportPublishTask reportTask = new ReportPublishTask(searchCondition,
				outputReportTypes, callback);
		reportTask.setUser(true);
		reportTask.schedule();
	}

	public List<String> getRuleNameList() {
		RuleManager ruleManager = RuleManager.getInstance();
		RuleSetConfig[] ruleSetConfigs = ruleManager.getRuleSetConfigs();

		List<String> result = new ArrayList<String>(ruleSetConfigs.length);
		for (RuleSetConfig config : ruleSetConfigs) {
			result.add(config.getName());
		}

		return result;
	}
}
