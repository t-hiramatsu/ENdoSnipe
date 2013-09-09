package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽuJavelin・ｽv・ｽ^・ｽu・ｽﾌ「・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class ConvertedMethodNumRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
    private Timestamp measurementTime_;
    
    /** ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、JavelinConverter・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ[・ｽ・ｽ] */
    private long javelinConverterMethodNum_;
    
    /** ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、JavelinConverter・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽO・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ[・ｽ・ｽ] */
    private long javelinConverterExcludedMethodNum_;
    
    /** ・ｽﾏ奇ｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽ・ｽ・ｽs・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽ[・ｽ・ｽ] */
    private long executedMethodNum_;

	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime() {
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime) {
		this.measurementTime_ = measurementTime;
	}

	/**
	 * @return the javelinConverterMethodNum_
	 */
	public long getJavelinConverterMethodNum() {
		return javelinConverterMethodNum_;
	}

	/**
	 * @param javelinConverterMethodNum the javelinConverterMethodNum_ to set
	 */
	public void setJavelinConverterMethodNum(long javelinConverterMethodNum) {
		this.javelinConverterMethodNum_ = javelinConverterMethodNum;
	}

	/**
	 * @return the javelinConverterExcludedMethodNum_
	 */
	public long getJavelinConverterExcludedMethodNum() {
		return javelinConverterExcludedMethodNum_;
	}

	/**
	 * @param javelinConverterExcludedMethodNum the javelinConverterExcludedMethodNum_ to set
	 */
	public void setJavelinConverterExcludedMethodNum(
			long javelinConverterExcludedMethodNum) {
		this.javelinConverterExcludedMethodNum_ = javelinConverterExcludedMethodNum;
	}

	/**
	 * @return the executedMethodNum_
	 */
	public long getExecutedMethodNum() {
		return executedMethodNum_;
	}

	/**
	 * @param executedMethodNum the executedMethodNum_ to set
	 */
	public void setExecutedMethodNum(long executedMethodNum) {
		this.executedMethodNum_ = executedMethodNum;
	}
}
