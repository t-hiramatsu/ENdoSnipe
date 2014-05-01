package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

/**
 * �u�A�v���P�[�V�����v�^�u�́uAP�T�[�o�̃��[�J�X���b�h���v�̃��|�[�g�ɂ����āA
 * �o�͂������̒���1���R�[�h����ێ�����G���e�B�e�B�ł��B
 * 
 * @author T. Iida
 */
public class WorkerThreadNumRecord
{
    /** �v������ */
    private Timestamp measurementTime_;

    /** AP�T�[�o�̃��[�J�X���b�h���̍ő�l[�X���b�h��] */
    private long      maxWorkerThreadNum_;

    /** AP�T�[�o�̃��[�J�X���b�h��[�X���b�h��]�i��Ԋ��ԕ��ρj */
    private long      workerThreadNum_;

    /** AP�T�[�o�̃��[�J�X���b�h��[�X���b�h��]�i��Ԋ��ԍő�j */
    private long      workerThreadNumMax_;

    /** AP�T�[�o�̃��[�J�X���b�h��[�X���b�h��]�i��Ԋ��ԍŏ��j */
    private long      workerThreadNumMin_;

    /** AP�T�[�o�̃��[�J�X���b�h���iwait�j[�X���b�h��] */
    private long      waitWorkerThreadNum_;

    /**
     * �R���X�g���N�^
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
