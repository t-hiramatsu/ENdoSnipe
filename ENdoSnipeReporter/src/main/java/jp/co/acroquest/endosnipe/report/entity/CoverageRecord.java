package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽuJavelin・ｽv・ｽ^・ｽu・ｽﾌ「・ｽJ・ｽo・ｽ・ｽ・ｽb・ｽW・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class CoverageRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
	private Timestamp measurementTime_;

	/** ・ｽJ・ｽo・ｽ・ｽ・ｽb・ｽW[%]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ包ｿｽ・ｽﾏ） */
	private double coverage_;

	/** ・ｽJ・ｽo・ｽ・ｽ・ｽb・ｽW[%]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最托ｿｽj */
	private double coverageMax_;

	/** ・ｽJ・ｽo・ｽ・ｽ・ｽb・ｽW[%]・ｽi・ｽ・ｽﾔ奇ｿｽﾔ最擾ｿｽ・ｽj */
	private double coverageMin_;

	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime()
	{
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime)
	{
		this.measurementTime_ = measurementTime;
	}

	/**
	 * @return the coverage_
	 */
	public double getCoverage()
	{
		return coverage_;
	}

	/**
	 * @param coverage the coverage_ to set
	 */
	public void setCoverage(double coverage)
	{
		this.coverage_ = coverage;
	}

	/**
	 * @return the coverageMax_
	 */
	public double getCoverageMax()
	{
		return coverageMax_;
	}

	/**
	 * @param coverageMax the coverageMax_ to set
	 */
	public void setCoverageMax(double coverageMax)
	{
		this.coverageMax_ = coverageMax;
	}

	/**
	 * @return the coverageMin_
	 */
	public double getCoverageMin()
	{
		return coverageMin_;
	}

	/**
	 * @param coverageMin the coverageMin_ to set
	 */
	public void setCoverageMin(double coverageMin)
	{
		this.coverageMin_ = coverageMin;
	}
}
