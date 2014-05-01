package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

public class JavelinRecord
{
	/** �v������ */
    private Timestamp measurementTime_;

    /** CallTreeNode�������̃O���t�́A�ő�[��] */
    private long callTreeNodeNumMax_;
    
    /** CallTreeNode�������̃O���t�́A����[��] */
    private double callTreeNodeNumAverage_;
    
    /** �ϊ����\�b�h���̃O���t�́AJavelinConverter�ϊ����\�b�h��[��] */
    private long javelinConverterMethodNum_;
    
    /** �ϊ����\�b�h���̃O���t�́AJavelinConverter�ϊ����O���\�b�h��[��] */
    private long javelinConverterExcludedMethodNum_;
    
    /** �ϊ����\�b�h���̃O���t�́A���s���\�b�h��[��] */
    private long executedMethodNum_;
    
    /** �J�o���b�W[%]�i��Ԋ��ԕ��ρj */
    private double coverage_;
    
    /** �J�o���b�W[%]�i��Ԋ��ԍő�j */
    private double coverageMax_;
    
    /** �J�o���b�W[%]�i��Ԋ��ԍŏ��j */
    private double coverageMin_;
    
    /** �C�x���g���i�ő�j */
    private long eventMax_;
    
    /** �C�x���g��(�ŏ�) */
    private long eventMin_;
    
    /** �C�x���g�� */
    private long event_;

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
	public double getCallTreeNodeNumAverage() {
		return callTreeNodeNumAverage_;
	}

	/**
	 * @param callTreeNodeNumAverage the callTreeNodeNumAverage_ to set
	 */
	public void setCallTreeNodeNumAverage(double callTreeNodeNumAverage) {
		this.callTreeNodeNumAverage_ = callTreeNodeNumAverage;
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

    /**
     * @param eventMax_ the eventMax_ to set
     */
    public void setEventMax(long eventMax_)
    {
        this.eventMax_ = eventMax_;
    }

    /**
     * @return the eventMax_
     */
    public long getEventMax()
    {
        return eventMax_;
    }

    /**
     * @param eventMin_ the eventMin_ to set
     */
    public void setEventMin(long eventMin_)
    {
        this.eventMin_ = eventMin_;
    }

    /**
     * @return the eventMin_
     */
    public long getEventMin()
    {
        return eventMin_;
    }

    /**
     * @param event_ the event_ to set
     */
    public void setEvent(long event_)
    {
        this.event_ = event_;
    }

    /**
     * @return the event_
     */
    public long getEvent()
    {
        return event_;
    }
}
