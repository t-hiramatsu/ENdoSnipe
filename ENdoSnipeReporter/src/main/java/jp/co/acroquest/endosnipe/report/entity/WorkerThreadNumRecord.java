package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * 「アプリケーション」タブの「APサーバのワーカスレッド数」のレポートにおいて、
 * 出力される情報の中の1レコード分を保持するエンティティです。
 * 
 * @author T. Iida
 */
public class WorkerThreadNumRecord
{
    /** 計測時刻 */
    private Timestamp measurementTime_;

    /** APサーバのワーカスレッド数の最大値[スレッド数] */
    private long      maxWorkerThreadNum_;

    /** APサーバのワーカスレッド数[スレッド数]（補間期間平均） */
    private long      workerThreadNum_;

    /** APサーバのワーカスレッド数[スレッド数]（補間期間最大） */
    private long      workerThreadNumMax_;

    /** APサーバのワーカスレッド数[スレッド数]（補間期間最小） */
    private long      workerThreadNumMin_;

    /** APサーバのワーカスレッド数（wait）[スレッド数] */
    private long      waitWorkerThreadNum_;

    /**
     * コンストラクタ
     */
    public WorkerThreadNumRecord()
    {
        maxWorkerThreadNum_ = 0;
        workerThreadNum_ = 0;
        workerThreadNumMax_ = 0;
        workerThreadNumMin_ = 0;
        waitWorkerThreadNum_ = 0;
    }

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
     * @return the maxWorkerThreadNum_
     */
    public long getMaxWorkerThreadNum()
    {
        return maxWorkerThreadNum_;
    }

    /**
     * @param maxWorkerThreadNum the maxWorkerThreadNum_ to set
     */
    public void setMaxWorkerThreadNum(long maxWorkerThreadNum)
    {
        this.maxWorkerThreadNum_ = maxWorkerThreadNum;
    }

    /**
     * @return the workerThreadNum_
     */
    public long getWorkerThreadNum()
    {
        return workerThreadNum_;
    }

    /**
     * @param workerThreadNum_ the workerThreadNum_ to set
     */
    public void setWorkerThreadNum(long workerThreadNum_)
    {
        this.workerThreadNum_ = workerThreadNum_;
    }

    /**
     * @return the workerThreadNumMax_
     */
    public long getWorkerThreadNumMax()
    {
        return workerThreadNumMax_;
    }

    /**
     * @param workerThreadNumMax_ the workerThreadNumMax_ to set
     */
    public void setWorkerThreadNumMax(long workerThreadNumMax_)
    {
        this.workerThreadNumMax_ = workerThreadNumMax_;
    }

    /**
     * @return the workerThreadNumMin_
     */
    public long getWorkerThreadNumMin()
    {
        return workerThreadNumMin_;
    }

    /**
     * @param workerThreadNumMin_ the workerThreadNumMin_ to set
     */
    public void setWorkerThreadNumMin(long workerThreadNumMin_)
    {
        this.workerThreadNumMin_ = workerThreadNumMin_;
    }

    /**
     * @return the waitWorkerThreadNum_
     */
    public long getWaitWorkerThreadNum()
    {
        return waitWorkerThreadNum_;
    }

    /**
     * @param waitWorkerThreadNum the waitWorkerThreadNum_ to set
     */
    public void setWaitWorkerThreadNum(long waitWorkerThreadNum)
    {
        this.waitWorkerThreadNum_ = waitWorkerThreadNum;
    }
}
