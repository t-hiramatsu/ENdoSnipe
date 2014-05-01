package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

/**
 * �W���u�̏��
 * 
 * @author hiramatsu
 *
 */
public class JobInfo
{
    /**�@�W���u�I����ԁ@*/
    private String              status_;

    /**�@�W���u�T�u�~�b�g�����@*/
    private long                submitTime_;

    /**�@�W���u�J�n�����@*/
    private long                startTime_;

    /**�@�W���u�I�������@*/
    private long                finishTime_;

    /**�@�W���u���@*/
    private String              jobName_;

    /**�@�W���uID�@*/
    private String              jobId_;

    /** jobID �� JSON�L�[  */
    private static final String JOB_ID      = "JobID";

    /** jobName �� JSON�L�[  */
    private static final String JOB_NAME    = "JobName";

    /** submitTime �� JSON�L�[  */
    private static final String SUBMIT_TIME = "SubmitTime";

    /** startTime �� JSON�L�[  */
    private static final String START_TIME  = "StartTime";

    /** finishTime �� JSON�L�[  */
    private static final String FINISH_TIME = "FinishTime";

    /** status �� JSON�L�[  */
    private static final String STATUS      = "Status";

    /**
     * �W���u���s���ʏ�Ԃ��擾����B
     * @return �W���u���s���ʏ��
     */
    public String getStatus()
    {
        return status_;
    }

    /**
     * �W���u���s���ʏ�Ԃ�ύX����B
     * @param status �W���u���s���ʏ��
     */
    public void setStatus(String status)
    {
        this.status_ = status;
    }

    /**
     * �W���u�T�u�~�b�g�������擾����B
     * @return �W���u�T�u�~�b�g����
     */
    public long getSubmitTime()
    {
        return submitTime_;
    }

    /**
     * �W���u�T�u�~�b�g������ύX����B
     * @param submitTime �W���u�T�u�~�b�g����
     */
    public void setSubmitTime(long submitTime)
    {
        this.submitTime_ = submitTime;
    }

    /**
     * �W���u�J�n�������擾����B
     * @return �W���u�J�n����
     */
    public long getStartTime()
    {
        return startTime_;
    }

    /**
     * �W���u�J�n������ύX����B
     * @param startTime �W���u�J�n����
     */
    public void setStartTime(long startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * �W���u�I���������擾����B
     * @return �W���u�I������
     */
    public long getFinishTime()
    {
        return finishTime_;
    }

    /**
     * �W���u�I��������ύX����B
     * @param finishTime �W���u�I������
     */
    public void setFinishTime(long finishTime)
    {
        this.finishTime_ = finishTime;
    }

    /**
     * �W���u�����擾����B
     * @return �W���u��
     */
    public String getJobName()
    {
        return jobName_;
    }

    /**
     * �W���u����ύX����B
     * @param jobName �W���u��
     */
    public void setJobName(String jobName)
    {
        this.jobName_ = jobName;
    }

    /**
     * �W���uID���擾����B
     * @return �W���uID
     */
    public String getJobId()
    {
        return jobId_;
    }

    /**
     * �W���uID��ύX����B
     * @param jobId �W���uID
     */
    public void setJobId(String jobId)
    {
        this.jobId_ = jobId;
    }

    @Override
    public String toString()
    {
        return "JobInfo [status_=" + status_ + ", submitTime_=" + submitTime_
                + ", startTime_=" + startTime_ + ", finishTime_=" + finishTime_
                + ", jobName_=" + jobName_ + ", jobId_=" + jobId_ + "]";
    }

    /**
     * @return json�`���̃W���u���
     */
    public String getJson()
    {
        Map<String, Object> jobStatusMap = new HashMap<String, Object>();
        jobStatusMap.put(JOB_ID, this.jobId_);
        jobStatusMap.put(JOB_NAME, this.jobName_);
        jobStatusMap.put(SUBMIT_TIME, this.submitTime_);
        jobStatusMap.put(START_TIME, this.startTime_);
        jobStatusMap.put(FINISH_TIME, this.finishTime_);

        jobStatusMap.put(STATUS, this.status_);

        String jsonString = JSON.encode(jobStatusMap);

        return jsonString;
    }

}
