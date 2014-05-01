package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

/**
 * �^�X�N���
 * @author hiramatsu
 */
public class TaskInfo
{
    /**�@�^�X�N�I����ԁ@*/
    private String              status_;

    /**�@�^�X�N�J�n�����@*/
    private long                startTime_;

    /**�@�^�X�N�I�������@*/
    private long                finishTime_;

    /**�@�W���uID�@*/
    private String              jobID_;

    /**�@�^�X�NID�@*/
    private String              taskAttemptID_;

    /**�@�z�X�g�T�[�o���@*/
    private String              hostName_;

    /** jobID �� JSON�L�[  */
    private static final String JOB_ID          = "JobID";

    /** startTime �� JSON�L�[  */
    private static final String START_TIME      = "StartTime";

    /** finishTime �� JSON�L�[  */
    private static final String FINISH_TIME     = "FinishTime";

    /** status �� JSON�L�[  */
    private static final String STATUS          = "Status";

    /** taskAttemptId �� JSON�L�[  */
    private static final String TASK_ATTEMPT_ID = "TaskAttemptID";

    /** status �� JSON�L�[  */
    private static final String HOST_NAME       = "Hostname";

    /**
     * �^�X�N�I������Ԃ��擾����B
     * 
     * @return �I�������
     */
    public String getStatus()
    {
        return status_;
    }

    /**
     * �^�X�N�I������Ԃ�ݒ肷��B
     * 
     * @param status �ݒ肷��I�����
     */
    public void setStatus(String status)
    {
        this.status_ = status;
    }

    /**
     * �^�X�N�J�n�������擾����B
     * 
     * @return �^�X�N�J�n����
     */
    public long getStartTime()
    {
        return startTime_;
    }

    /**
     * �^�X�N�J�n������ݒ肷��B
     * 
     * @param startTime �ݒ肷��^�X�N�J�n����
     */
    public void setStartTime(long startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * �^�X�N�I���������擾����B
     * 
     * @return �^�X�N�I������
     */
    public long getFinishTime()
    {
        return finishTime_;
    }

    /**
     * �^�X�N�I��������ݒ肷��B
     * 
     * @param finishTime �ݒ肷��^�X�N�I������
     */
    public void setFinishTime(long finishTime)
    {
        this.finishTime_ = finishTime;
    }

    /**
     * �Ή�����W���u��ID���擾����B
     * 
     * @return �Ή�����W���u��ID
     */
    public String getJobID()
    {
        return jobID_;
    }

    /**
     * �Ή�����W���u��ID��ݒ肷��B
     * 
     * @param jobID �ݒ肷��Ή�����W���u��ID
     */
    public void setJobID(String jobID)
    {
        this.jobID_ = jobID;
    }

    /**
     * �^�X�NID���擾����B
     * 
     * @return �^�X�NID
     */
    public String getTaskAttemptID()
    {
        return taskAttemptID_;
    }

    /**
     * �^�X�NID��ݒ肷��B
     * 
     * @param taskAttemptID �ݒ肷��^�X�NID
     */
    public void setTaskAttemptID(String taskAttemptID)
    {
        this.taskAttemptID_ = taskAttemptID;
    }

    /**
     * �z�X�g�T�[�o�����擾����B
     * 
     * @return �z�X�g�T�[�o��
     */
    public String getHostName()
    {
        return hostName_;
    }

    /**
     * �z�X�g�T�[�o����ݒ肷��B
     * 
     * @param hostName �ݒ肷��z�X�g�T�[�o��
     */
    public void setHostName(String hostName)
    {
        this.hostName_ = hostName;
    }

    @Override
    public String toString()
    {
        return "TaskInfo [status_=" + status_ + ", startTime_=" + startTime_
                + ", finishTime_=" + finishTime_ + ", jobID_=" + jobID_
                + ", taskAttemptID_=" + taskAttemptID_ + ", hostName_="
                + hostName_ + "]";
    }

    /**
     * json�`���̃^�X�N����Ԃ��B
     * 
     * @return json�`���̃^�X�N���
     */
    public String getJson()
    {
        Map<String, Object> taskStatusMap = new HashMap<String, Object>();
        taskStatusMap.put(JOB_ID, this.jobID_);
        taskStatusMap.put(START_TIME, this.startTime_);
        taskStatusMap.put(FINISH_TIME, this.finishTime_);
        taskStatusMap.put(TASK_ATTEMPT_ID, this.taskAttemptID_);
        taskStatusMap.put(HOST_NAME, this.hostName_);
        taskStatusMap.put(STATUS, this.status_);

        String jsonString = JSON.encode(taskStatusMap);

        return jsonString;
    }

}
