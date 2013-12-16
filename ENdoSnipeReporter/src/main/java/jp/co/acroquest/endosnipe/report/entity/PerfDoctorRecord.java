package jp.co.acroquest.endosnipe.report.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * PerformanceDoctorレポートに出力するデータの中の、 1レコード分の情報を保持するエンティティです。
 * 
 * @author T. Iida
 */
public class PerfDoctorRecord
{
	/** ID */
	private String id_ = "default id";

	/** Time */
	private String time_ = "default time";

	/** 概要 */
	private String description_ = "default description";

	/** 重要度 */
	private String level_ = "default level";

	/** クラス */
	private String className_ = "default className";

	/** メソッド */
	private String methodName_ = "default methodName";

	/** ファイル名 */
	private String logFileName_ = "default logFileName";

	/**
	 * コンストラクタです。
	 */
	public PerfDoctorRecord()
	{
		// 何もしない
	}

	/**
	 * コンストラクタです。 指定されたWarningUnitで、フィールドを初期化します。
	 * 
	 * @param warningUnit
	 *            指定されたWarningUnit
	 */
	public PerfDoctorRecord(WarningUnit warningUnit)
	{
		this.setId(warningUnit.getId());

		// calculating long to date format
		long time = warningUnit.getStartTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		this.setTime(formatter.format(calendar.getTime()));
		this.setDescription(warningUnit.getDescription());
		this.setLevel(warningUnit.getLevel());
		this.setClassName(warningUnit.getClassName());
		this.setMethodName(warningUnit.getMethodName());
		this.setLogFileName(warningUnit.getLogFileName());
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return this.id_;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id_ = id;
	}

	/**
	 * @return the time
	 */
	public String getTime()
	{
		return time_;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time)
	{
		this.time_ = time;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description_;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description_ = description;
	}

	/**
	 * @return the level
	 */
	public String getLevel()
	{
		return this.level_;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(String level)
	{
		this.level_ = level;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return this.className_;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className)
	{
		this.className_ = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName()
	{
		return this.methodName_;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName)
	{
		this.methodName_ = methodName;
	}

	/**
	 * @return the logFileName
	 */
	public String getLogFileName()
	{
		return this.logFileName_;
	}

	/**
	 * @param logFileName
	 *            the logFileName to set
	 */
	public void setLogFileName(String logFileName)
	{
		this.logFileName_ = logFileName;
	}
}
