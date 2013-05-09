/*
 * Copyright (c) 2004-2011 SMG Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
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
public class ReporterMain {
	/** このコマンドを動作させるのに必要なコマンドライン引数の数。 */
	private static final int REQUIRED_CMDLINE_PARAMS = 5;

	/** オプションを含めた場合の引数の数 */
	private static final int OPTIONAL_CMDLINE_PARAMS = 6;

	/** レポート出力ディレクトリ名のデフォルト。 */
	private static final String REPORT_OUTPUT_DIR = "../reports";

	/** 開始／終了時刻を指定する文字列形式。 */
	private static final String TIME_FORMAT = "yyyyMMdd_HHmmss";

	/** args[]: mode */
	private static final int ARGS_INDEX_MODE = 0;

	/** args[]: IndexNumber or DatabaseName */
	private static final int ARGS_INDEX_DB_IDENTITY = 1;

	/** args[]: fmTimeStr */
	private static final int ARGS_INDEX_FM_TIME_STR = 2;

	/** args[]: toTimeStr */
	private static final int ARGS_INDEX_TO_TIME_STR = 3;

	/** args[]: toOutoutReport */
	private static final int ARGS_INDEX_OUTPUT_REPORT = 4;

	/** args[]: full output */
	private static final int ARGS_INDEX_FULL_MODE = 5;

	/** インデックス番号指定のオプション */
	private static final String OPTION_INDEX_SPECIFY = "-i";

	/** データベース名指定のオプション */
	private static final String OPTION_NAME_SPECIFY = "-d";

	/** 全てのレポートを出力するかどうか */
	private static final String FULL_MODE = "full";

	/** システムプロパティにより、レポート出力先を指定する。 */
	private static final String REPORT_PATH = "report.path";

	/**
	 * レポート作成を行います。<br/>
	 * 
	 * @param args
	 *            コマンドライン引数。<br/>
	 *            [0]="-i"(インデックス指定)または"-d"(データベース名指定)<br/>
	 *            [1]=インデックス番号orデータベース名<br/>
	 *            [2]=レポート作成期間(開始)。 "yyyyMMdd_HHmmss"形式で指定する。<br/>
	 *            [3]=レポート作成期間(終了)。 "yyyyMMdd_HHmmss"形式で指定する。<br/>
	 *            [4]=詳細出力
	 */
	public static void main(String[] args) {
		// データベースをインデックス指定するか
		boolean use_index = true;
		// 詳細出力を行うか
		boolean full = false;

		// コマンドライン引数の数をチェック
		if (args.length != REQUIRED_CMDLINE_PARAMS
				&& args.length != OPTIONAL_CMDLINE_PARAMS) {
			usage();
			System.exit(1);
		}

		// 第１、２引数の判定
		if (OPTION_INDEX_SPECIFY.equals(args[ARGS_INDEX_MODE])) {
			// インデックス指定の場合は、第２引数が数値かチェック
			if (!checkIndexNo(args[ARGS_INDEX_DB_IDENTITY])) {
				usage();
				System.exit(1);
			}
		} else if (OPTION_NAME_SPECIFY.equals(args[ARGS_INDEX_MODE])) {
			use_index = false;
		} else {
			usage();
			System.exit(1);
		}

		// データ取得期間の設定から第３、４引数を判定する
		Calendar fmTime = Calendar.getInstance();
		Calendar toTime = Calendar.getInstance();

		// 引数で指定された文字列からDateオブジェクト生成
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
		try {
			// 開始時刻
			Date fmDate = dateFormat.parse(args[ARGS_INDEX_FM_TIME_STR]);
			fmTime.setTime(fmDate);

			// 終了時刻
			Date toDate = dateFormat.parse(args[ARGS_INDEX_TO_TIME_STR]);
			toTime.setTime(toDate);
		} catch (ParseException exception) {
			// 解析エラーとなった場合は終了
			System.err.println("時刻指定の形式が不正です。");
			System.exit(1);
		}

		// 開始時刻が終了時刻より未来を指していた場合はエラー
		if (fmTime.compareTo(toTime) > 0) {
			System.err.println("開始時刻が終了時刻より未来を指しています。");
			System.exit(1);
		}

		// 詳細出力を行うか
		if (args.length == OPTIONAL_CMDLINE_PARAMS) {
			if (FULL_MODE.equals(args[ARGS_INDEX_FULL_MODE])) {
				full = true;
			} else {
				usage();
				System.exit(1);
			}
		}

		// ログ出力インスタンスを生成する
		System.err.println("ENdoSnipeReportCommandを開始します。");

		// レポート出力対象のインデックス番号。(collector.propertiesの設定)
		int IndexNo = -1;

		// データベース接続設定
		String dbHost = null;
		String dbPort = null;
		String dbUser = null;
		String dbPass = null;
		String dbName = null;

		// レポート出力先ディレクトリ
		String reportPath = null;

		if (use_index) {
			try {
				IndexNo = Integer.parseInt(args[ARGS_INDEX_DB_IDENTITY]);
			} catch (NumberFormatException ex) {
				System.err.println("インデックス番号(" + args[ARGS_INDEX_DB_IDENTITY]
						+ ")が不正です。正しく指定してください。");
				System.exit(1);
			}
		}

		// collector.properties から各種設定値を読み込む
		DataCollectorConfig config = null;
		try {
			config = ConfigLoader.loadConfig();
		} catch (InitializeException ie) {
			System.err.println(ie);
			System.exit(1);
		}

		if (config == null) {
			System.exit(1);
		}

		// DBがH2の場合は終了
		DatabaseType type = config.getDatabaseType();
		if (type != DatabaseType.POSTGRES) {
			System.err
					.println("このプログラムはPostgreSQL専用です。collector.propertiesを修正してください。");
			System.exit(1);
		}

		// DBの諸設定を取得
		dbHost = config.getDatabaseHost();
		dbPort = config.getDatabasePort();
		dbUser = config.getDatabaseUserName();
		dbPass = config.getDatabasePassword();

		reportPath = args[ARGS_INDEX_OUTPUT_REPORT];

		// 各DB設定のリスト取得
		List<AgentSetting> agentSettings = config.getAgentSettingList();

		// インデックス番号を基にDB名を取得する場合
		if (use_index) {
			if (IndexNo > agentSettings.size()) {
				System.err.println("インデックス番号(" + IndexNo
						+ ")に該当するデータベースの設定がありません。終了します。");
				System.exit(1);
			}
			AgentSetting agentSetting = agentSettings.get(IndexNo);
			dbName = agentSetting.databaseName;
		}
		// DB名指定の場合
		else {
			dbName = args[ARGS_INDEX_DB_IDENTITY];
		}

		// レポート作成に使用するDBを指定する
		DBManager.updateSettings(false, "", dbHost, dbPort, dbName, dbUser,
				dbPass);

		// レポート作成時の各設定を行う
		ReportType[] outputReportTypes;

		// すべてのレポートを出力するかどうかを引数により指定する。
		if (full) {
			outputReportTypes = new ReportType[] { ReportType.SUMMARY,
					ReportType.SYSTEM, ReportType.PROCESS, ReportType.DATA_IO,
					ReportType.VM_STATUS, ReportType.OBJECT,
					ReportType.SERVER_POOL, ReportType.POOL_SIZE,
					ReportType.APPLICATION, ReportType.JAVELIN,
					ReportType.PERF_DOCTOR, ReportType.RESPONSE_LIST,
					ReportType.RESPONSE_SUMMARY };
		} else {
			outputReportTypes = new ReportType[] { ReportType.SUMMARY,
					ReportType.SYSTEM, ReportType.PROCESS, ReportType.DATA_IO,
					ReportType.VM_STATUS, ReportType.OBJECT,
					ReportType.SERVER_POOL, ReportType.POOL_SIZE,
					ReportType.APPLICATION, ReportType.JAVELIN,
					ReportType.PERF_DOCTOR, ReportType.RESPONSE_SUMMARY };
		}

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

		String outputFilePath;
		if (use_index) {
			// インデックス指定の場合のレポート出力先
			outputFilePath = reportPath + File.separator
					+ String.valueOf(IndexNo) + File.separator
					+ leafDirectoryName;
		} else {
			// データベース名指定の場合のレポート出力先
			outputFilePath = reportPath + File.separator + dbName
					+ File.separator + leafDirectoryName;
		}

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
		System.err.println("Parameters: Index DB = " + dbHost + ":" + dbPort
				+ "/" + dbName + ", Span = " + args[ARGS_INDEX_FM_TIME_STR]
				+ " - " + args[ARGS_INDEX_TO_TIME_STR]);
		System.err.println("レポート出力ディレクトリ: " + outputFilePath);

		// ReportPublishTaskを実行し、レポート作成を行う
		try {
			ReportPublishTask reportTask = new ReportPublishTask(
					searchCondition, outputReportTypes, callback);
			reportTask.setUser(true);
			// runメソッドを直接呼び出す
			reportTask.run(new MockIProgressMonitor());
		} catch (Exception e) {
			System.err.println("レポート生成中にエラーが発生しました。");
			System.exit(1);
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

	/**
	 * 指定された文字列が自然数を示すか判定する。<br/>
	 * (0から始まる場合は文字列とみなす)
	 * 
	 * @param text
	 *            判定対象
	 * 
	 * @return {@code true}数値である/{@code false}数値ではない
	 */
	private static boolean checkIndexNo(String text) {
		return text.matches("^[1-9][0-9]*$");
	}

	/**
	 * コマンドライン引数の使用方法を説明します。
	 */
	private static void usage() {
		System.err
				.println("USAGE: ReporterMain <-i IndexNo>|<-d DBName> <StartTime> <EndTime> <reportPath> [full]");
		System.err.println("         StartTime/EndTime: yyyyMMdd_HHmmss");
	}
}
