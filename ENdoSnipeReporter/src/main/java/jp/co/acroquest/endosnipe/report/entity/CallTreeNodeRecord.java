package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * ・ｽuJavelin・ｽv・ｽ^・ｽu・ｽﾌ「CallTreeNode・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽv・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌデ・ｽ[・ｽ^・ｽ・ｽ
 * 1・ｽﾂ保趣ｿｽ・ｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 * 
 * @author acroquest
 */
public class CallTreeNodeRecord
{
	/** ・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ */
    private Timestamp measurementTime_;

    /** CallTreeNode・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽﾅ托ｿｽ[・ｽ・ｽ] */
    private long callTreeNodeNumMax_;
    
    /** CallTreeNode・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌグ・ｽ・ｽ・ｽt・ｽﾌ、・ｽ・ｽ・ｽ・ｽ[・ｽ・ｽ] */
    private long callTreeNodeNumAverage_;
    
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
	 * @return the callTreeNodeNumMax_
	 */
	public long getCallTreeNodeNumMax() {
		return callTreeNodeNumMax_;
	}

	/**
	 * @param callTreeNodeNumMax the callTreeNodeNumMax_ to set
	 */
	public void setCallTreeNodeNumMax(long callTreeNodeNumMax) {
		this.callTreeNodeNumMax_ = callTreeNodeNumMax;
	}

	/**
	 * @return the callTreeNodeNumAverage_
	 */
	public long getCallTreeNodeNumAverage() {
		return callTreeNodeNumAverage_;
	}

	/**
	 * @param callTreeNodeNumAverage the callTreeNodeNumAverage_ to set
	 */
	public void setCallTreeNodeNumAverage(long callTreeNodeNumAverage) {
		this.callTreeNodeNumAverage_ = callTreeNodeNumAverage;
	}
}
