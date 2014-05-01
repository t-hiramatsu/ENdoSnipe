package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

public class ServerPoolRecord
{
    /** åvë™éûçè */
    private Timestamp measurementTime_;
    
    private long serverPoolMax_;
    
    private long serverPoolNum_;

    public Timestamp getMeasurementTime()
    {
        return measurementTime_;
    }

    public void setMeasurementTime(Timestamp measurementTime)
    {
        this.measurementTime_ = measurementTime;
    }

    public long getServerPoolMax()
    {
        return serverPoolMax_;
    }

    public void setServerPoolMax(long serverPoolMax)
    {
        this.serverPoolMax_ = serverPoolMax;
    }

    public long getServerPoolNum()
    {
        return serverPoolNum_;
    }

    public void setServerPoolNum(long serverPoolNum)
    {
        this.serverPoolNum_ = serverPoolNum;
    }
}
