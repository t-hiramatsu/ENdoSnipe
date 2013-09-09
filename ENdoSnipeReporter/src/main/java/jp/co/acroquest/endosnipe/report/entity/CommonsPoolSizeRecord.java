package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * 「アプリケーション」タブの「Commons Poolのサイズ」のレポートにおいて、
 * 出力される情報の中の1レコード分を保持するエンティティです。
 * 
 * @author T. Iida
 */
public class CommonsPoolSizeRecord
{
	/** 計測時刻 */
    private Timestamp measurementTime_;
    
    /** Commons Poolのサイズの最大値 */
    private long      maxCommonsPoolSize_;

    /** Commons Poolのサイズ[数]（補間期間平均） */
    private long      commonsPoolSize_;
    
    /** Commons Poolのサイズ[数]（補間期間最大） */
    private long      commonsPoolSizeMax_;
    
    /** Commons Poolのサイズ[数]（補間期間最小） */
    private long      commonsPoolSizeMin_;
    
    /**
     * コンストラクタ
     */
    public CommonsPoolSizeRecord() {
        maxCommonsPoolSize_ = 0;
        commonsPoolSize_ = 0;
        commonsPoolSizeMax_ = 0;
        commonsPoolSizeMin_ = 0;
    }
    
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
	 * @return the maxCommonsPoolSize_
	 */
	public long getMaxCommonsPoolSize() {
		return maxCommonsPoolSize_;
	}

	/**
	 * @param maxCommonsPoolSize the maxCommonsPoolSize_ to set
	 */
	public void setMaxCommonsPoolSize(long maxCommonsPoolSize) {
		this.maxCommonsPoolSize_ = maxCommonsPoolSize;
	}

	/**
	 * @return the commonsPoolSize_
	 */
	public long getCommonsPoolSize() {
		return commonsPoolSize_;
	}

	/**
	 * @param commonsPoolSize the commonsPoolSize_ to set
	 */
	public void setCommonsPoolSize(long commonsPoolSize) {
		this.commonsPoolSize_ = commonsPoolSize;
	}

	/**
	 * @return the commonsPoolSizeMax_
	 */
	public long getCommonsPoolSizeMax() {
		return commonsPoolSizeMax_;
	}

	/**
	 * @param commonsPoolSizeMax the commonsPoolSizeMax_ to set
	 */
	public void setCommonsPoolSizeMax(long commonsPoolSizeMax) {
		this.commonsPoolSizeMax_ = commonsPoolSizeMax;
	}

	/**
	 * @return the commonsPoolSizeMin_
	 */
	public long getCommonsPoolSizeMin() {
		return commonsPoolSizeMin_;
	}

	/**
	 * @param commonsPoolSizeMin the commonsPoolSizeMin_ to set
	 */
	public void setCommonsPoolSizeMin(long commonsPoolSizeMin) {
		this.commonsPoolSizeMin_ = commonsPoolSizeMin;
	}
}
