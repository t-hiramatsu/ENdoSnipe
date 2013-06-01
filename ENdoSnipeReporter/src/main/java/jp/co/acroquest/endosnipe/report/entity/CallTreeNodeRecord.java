package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �uJavelin�v�^�u�́uCallTreeNode�������v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class CallTreeNodeRecord
{
	/** �v������ */
    private Timestamp measurementTime_;

    /** CallTreeNode�������̃O���t�́A�ő�[��] */
    private long callTreeNodeNumMax_;
    
    /** CallTreeNode�������̃O���t�́A����[��] */
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
