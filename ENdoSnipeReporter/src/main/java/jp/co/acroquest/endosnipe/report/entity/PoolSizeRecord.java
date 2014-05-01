package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

public class PoolSizeRecord
{
    /** åvë™éûçè */
    private Timestamp measurementTime_;
    
    private long poolSizeMax_;
    
    private long poolSizeCurrent_;
    
    private long poolSizeWait_;

    public Timestamp getMeasurementTime()
    {
        return measurementTime_;
    }

    public void setMeasurementTime(Timestamp measurementTime)
    {
        this.measurementTime_ = measurementTime;
    }

    public long getPoolSizeMax()
    {
        return poolSizeMax_;
    }

    public void setPoolSizeMax(long poolSizeMax)
    {
        this.poolSizeMax_ = poolSizeMax;
    }

    public long getPoolSizeCurrent()
    {
        return poolSizeCurrent_;
    }

    public void setPoolSizeCurrent(long poolSizeCurrent)
    {
        this.poolSizeCurrent_ = poolSizeCurrent;
    }

    public long getPoolSizeWait()
    {
        return poolSizeWait_;
    }

    public void setPoolSizeWait(long poolSizeWait)
    {
        this.poolSizeWait_ = poolSizeWait;
    }
}
