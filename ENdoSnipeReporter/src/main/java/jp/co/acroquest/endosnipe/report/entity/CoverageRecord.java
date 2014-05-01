package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �uJavelin�v�^�u�́u�J�o���b�W�v�̃O���t�̃f�[�^��
 * 1�ێ�����N���X�ł��B
 * 
 * @author acroquest
 */
public class CoverageRecord
{
	/** �v������ */
    private Timestamp measurementTime_;

    /** �J�o���b�W[%]�i��Ԋ�ԕ��ρj */
    private double coverage_;
    
    /** �J�o���b�W[%]�i��Ԋ�ԍő�j */
    private double coverageMax_;
    
    /** �J�o���b�W[%]�i��Ԋ�ԍŏ��j */
    private double coverageMin_;

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
	 * @return the coverage_
	 */
	public double getCoverage() {
		return coverage_;
	}

	/**
	 * @param coverage the coverage_ to set
	 */
	public void setCoverage(double coverage) {
		this.coverage_ = coverage;
	}

	/**
	 * @return the coverageMax_
	 */
	public double getCoverageMax() {
		return coverageMax_;
	}

	/**
	 * @param coverageMax the coverageMax_ to set
	 */
	public void setCoverageMax(double coverageMax) {
		this.coverageMax_ = coverageMax;
	}

	/**
	 * @return the coverageMin_
	 */
	public double getCoverageMin() {
		return coverageMin_;
	}

	/**
	 * @param coverageMin the coverageMin_ to set
	 */
	public void setCoverageMin(double coverageMin) {
		this.coverageMin_ = coverageMin;
	}
}
