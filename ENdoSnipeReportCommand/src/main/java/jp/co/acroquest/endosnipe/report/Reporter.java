/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.util.IOUtil;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.db.DatabaseType;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.report.controller.ReportPublishTask;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.mock.MockIProgressMonitor;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * BottleneckEyeを起動せずに、レポート作成を行うためのクラスです。<br>
 * 
 * @author iida
 */
public class Reporter {

	/** 開始／終了時刻を指定する文字列形式。 */
	private static final String TIME_FORMAT = "yyyyMMdd_HHmmss";
	
	/**
	 * コンストラクタ。
	 */
	public Reporter() {
		
	}
	
	/**
	 * レポート作成を行います。<br/>
	 * 
	 * @param dbName DB名
	 * @param fmTime 開始時刻
	 * @param toTime 終了時刻
	 * @param reportPath 出力先ディレクトリ
	 * @param targetItemName レポート出力対象の親の項目名
	 */
	public void createReport(String dbName, Calendar fmTime, Calendar toTime,
			String reportPath, String targetItemName) {

		// 開始時刻が終了時刻より未来を指していた場合はエラー
		if (fmTime.compareTo(toTime) > 0) {
			System.err.println("開始時刻が終了時刻より未来を指しています。");
			return;
		}

		// ログ出力インスタンスを生成する
		System.err.println("ENdoSnipeReportCommandを開始します。");

		// データベース接続設定
		String dbHost = null;
		String dbPort = null;
		String dbUser = null;
		String dbPass = null;

		// collector.properties から各種設定値を読み込む
		DataCollectorConfig config = null;
		try {
			config = ConfigLoader.loadConfig();
		} catch (InitializeException ie) {
			System.err.println(ie);
			return;
		}

		if (config == null) {
			return;
		}

		// DBがH2の場合は終了
		DatabaseType type = config.getDatabaseType();
		if (type != DatabaseType.POSTGRES) {
			System.err
					.println("このプログラムはPostgreSQL専用です。collector.propertiesを修正してください。");
			return;
		}

		// DBの諸設定を取得
		dbHost = config.getDatabaseHost();
		dbPort = config.getDatabasePort();
		dbUser = config.getDatabaseUserName();
		dbPass = config.getDatabasePassword();

		// レポート作成に使用するDBを指定する
		DBManager.updateSettings(false, "", dbHost, dbPort, dbName, dbUser,
				dbPass);

		// レポート作成時の各設定を行う
		ReportType[] outputReportTypes = new ReportType[] { ReportType.OBJECT };

		// TODO PerformanceDoctor のルール設定を有効にする。
		// // PerformanceDoctorのルールを設定する
		// int selectionIndex = 0;
		// RuleManager ruleManager = RuleManager.getInstance();
		// RuleSetConfig[] ruleSetConfigs = ruleManager.getRuleSetConfigs();
		// String id = ruleSetConfigs[selectionIndex].getId();
		// ruleManager.changeActiveRuleSetByID(id);

		Runnable callback = null;

		// レポート出力ディレクトリを決定し、存在しなければ作成する
		// レポート出力先ディレクトリ：
		// <current-dir>/reports/<db-name>/<from>-<to>/
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		String start = format.format(fmTime.getTime());
		String end = format.format(toTime.getTime());
		String leafDirectoryName = start + "-" + end;

		String outputFilePath = reportPath + File.separator + dbName
				+ File.separator + leafDirectoryName;

		File outputDir = new File(outputFilePath);
		if (outputDir.exists() == false) {
			outputDir.mkdirs();
		}

		// TODO 絞り込みのルールを設定する
		boolean limitSameCause = false;
		boolean limitBySameRule = false;

		// レポート出力期間の条件を設定する
		ReportSearchCondition searchCondition = new ReportSearchCondition();
		searchCondition.setDatabases(Arrays.asList(dbName));
		searchCondition.setStartDate(new Timestamp(fmTime.getTimeInMillis()));
		searchCondition.setEndDate(new Timestamp(toTime.getTimeInMillis()));
		searchCondition.setOutputFilePath(outputFilePath);
		searchCondition.setLimitSameCause(limitSameCause);
		searchCondition.setLimitBySameRule(limitBySameRule);

		System.err.println("レポートの生成を開始します。");
		System.err.println("レポート出力ディレクトリ: " + outputFilePath);

		// ReportPublishTaskを実行し、レポート作成を行う
		try {
			ReportPublishTask reportTask = new ReportPublishTask(
					searchCondition, outputReportTypes, callback);
			reportTask.setUser(true);

			MockIProgressMonitor mockIProgressMonitor = new MockIProgressMonitor();
			
			// runメソッドを直接呼び出す
			reportTask.createReport(mockIProgressMonitor, targetItemName);
		} catch (Exception e) {
			System.err.println("レポート生成中にエラーが発生しました。");
			return;
		}

		System.err.println("レポートの生成が終了しました。");

		System.err.println("jvnログの出力を開始します。");
		if (!outputJvnLog(dbName, new Timestamp(fmTime.getTimeInMillis()),
				new Timestamp(toTime.getTimeInMillis()), outputFilePath
						+ File.separator + "jvn_logs"))
			System.exit(1);
		System.err.println("jvnログの出力が終了しました。");

		// zip圧縮する
		Project project = new Project();
		project.init();

		try {
			File baseDir = new File(outputFilePath);
			Zip zipper = new Zip();
			zipper.setProject(project);
			zipper.setTaskName("zip");
			zipper.setTaskType("zip");
			zipper.setDestFile(new File(outputFilePath + ".zip"));
			zipper.setBasedir(baseDir);
			zipper.execute();
			System.err.println("レポートのzip化が終了しました。");

			// zip化に成功したら元のディレクトリは削除する
			boolean deleted = deleteDir(baseDir);
			if (deleted == false) {
				System.err.println("レポート作成元のディレクトリ削除に失敗しました。");
			}
		} catch (BuildException bex) {
			System.err.println("レポートのzip化に失敗しました。");
		}
	}

	/**
	 * Javelinログをデータベースから読み込み、ファイル出力する。
	 * 
	 * @param database
	 *            データベース名
	 * @param start
	 *            開始日時
	 * @param end
	 *            開始日時
	 * @param outputDir
	 *            出力先ディレクトリ
	 * 
	 * @return {@code true}成功/{@code false}失敗
	 */
	private static boolean outputJvnLog(String database, Timestamp start,
			Timestamp end, String outputDir) {
		File outputDirFile = new File(outputDir);
		boolean isSuccess = outputDirFile.mkdirs();
		if (isSuccess == false) {
			System.err.println("jvnログ出力ディレクトリの作成に失敗しました。");
			return false;
		}

		try {
			List<JavelinLog> jvnLogList = JavelinLogDao.selectByTermWithLog(
					database, start, end);
			for (JavelinLog log : jvnLogList) {
				String fileName = log.logFileName;
				OutputStream output = null;
				try {
					output = new BufferedOutputStream(new FileOutputStream(
							outputDir + File.separator + fileName));
					IOUtil.copy(log.javelinLog, output);
				} catch (FileNotFoundException fnfe) {
					System.err.println("jvnログ出力ディレクトリが見つかりません。");
				} catch (IOException ioe) {
					System.err.println("jvnログ出力中に例外が発生しました。");
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException ioe) {
							System.err.println("jvnログクローズ中に例外が発生しました。");
						}
					}
				}
			}

		} catch (SQLException sqle) {
			System.err.println("DBからのjvnログ読み込み中に例外が発生しました。");
			return false;
		}

		return true;
	}

	/**
	 * 指定したディレクトリごと削除する。
	 * 
	 * @param dir
	 *            削除するディレクトリ。
	 * @return ディレクトリの削除に失敗した場合。
	 */
	private static boolean deleteDir(File dir) {
		boolean result = true;
		File[] children = dir.listFiles();
		for (File child : children) {
			if (child.isDirectory() == true) {
				// ディレクトリは再帰して削除を行う
				result = deleteDir(child);
				if (result == false) {
					break;
				}
			} else {
				// ファイルは単に削除を行う
				result = child.delete();
				if (result == false) {
					break;
				}
			}
		}

		// 全ての削除に成功していれば中身は空なので、自分のディレクトリを削除する
		if (result == true) {
			result = dir.delete();
		}

		return result;
	}
}
