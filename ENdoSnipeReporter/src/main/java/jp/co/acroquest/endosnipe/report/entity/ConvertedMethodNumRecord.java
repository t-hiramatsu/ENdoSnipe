package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �uJavelin�v�^�u�́u�ϊ����\�b�h���v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class ConvertedMethodNumRecord
{
	/** �v������ */
    private Timestamp measurementTime_;
    
    /** �ϊ����\�b�h���̃O���t�́AJavelinConverter�ϊ����\�b�h��[��] */
    private long javelinConverterMethodNum_;
    
    /** �ϊ����\�b�h���̃O���t�́AJavelinConverter�ϊ����O���\�b�h��[��] */
    private long javelinConverterExcludedMethodNum_;
    
    /** �ϊ����\�b�h���̃O���t�́A���s���\�b�h��[��] */
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
