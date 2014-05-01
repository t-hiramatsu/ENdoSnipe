/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.util.HashMap;
import java.util.Map;

/**
 * Hadoop��HeartbeatResponse�Ɋ܂܂��
 * TaskTrackerAction����ێ�����N���X
 *
 * @author asazuma
 *
 */
public class HadoopAction
{
    /** �A�N�V������ʂ̗񋓑� */
    public static enum ActionType {LAUNCH_TASK, KILL_TASK, KILL_JOB,
                                   REINIT_TRACKER, COMMIT_TASK};

    /** Map�^�X�N���H */
    private boolean isMapTask_;

    /** �W���uID */
    private String jobID_;

    /** �^�X�N���sID */
    private String taskID_;

    /** �W���u�� */
    private String jobName_;

    /** �A�N�V������� */
    private ActionType actionType_;

    /** �����Ώۃf�[�^ */
    private String inputData_;

    /** �A�N�V������ʃ}�b�v */
    private static final Map<String, ActionType> ACTION_TYPE_MAP =
                                                    new HashMap<String, ActionType>()
    {
        {
            put("LAUNCH_TASK", ActionType.LAUNCH_TASK);
            put("KILL_TASK", ActionType.KILL_TASK);
            put("KILL_JOB", ActionType.KILL_JOB);
            put("REINIT_TRACKER", ActionType.REINIT_TRACKER);
            put("COMMIT_TASK", ActionType.COMMIT_TASK);
        }

    };

    /**
     * �R���X�g���N�^
     */
    public HadoopAction()
    {
        // �������Ȃ�
    }

    /**
     * Map�^�X�N���H
     *
     * @return {@code true}�FMap�^�X�N�^{@code false}�FReduce�^�X�N
     */
    public boolean isMapTask()
    {
        return isMapTask_;
    }

    /**
     * Map�^�X�N��Reduce�^�X�N����ݒ肷��B
     *
     * @param flag {@code true}�FMap�^�X�N�^{@code false}�FReduce�^�X�N
     */
    public void setMapTask(boolean flag)
    {
        this.isMapTask_  = flag;
    }

    /**
     * �W���uID���擾����B
     *
     * @return �W���uID
     */
    public String getJobID()
    {
        return jobID_;
    }

    /**
     * �W���uID��ݒ肷��B
     *
     * @param jobID �W���uID
     */
    public void setJobID(String jobID)
    {
        jobID_ = jobID;
    }

    /**
     * �^�X�N���sID���擾����B
     *
     * @return �^�X�N���sID
     */
    public String getTaskID()
    {
        return taskID_;
    }

    /**
     * �^�X�N���sID��ݒ肷��B
     *
     * @param taskID �^�X�N���sID
     */
    public void setTaskID(String taskID)
    {
        taskID_ = taskID;
    }

    /**
     * �W���u�����擾����B
     *
     * @return �W���u��
     */
    public String getJobName()
    {
        return jobName_;
    }

    /**
     * �W���u����ݒ肷��B
     * @param jobName �W���u��
     */
    public void setJobName(String jobName)
    {
        jobName_ = jobName;
    }

    /**
     * �A�N�V������ʂ��擾����B
     *
     * @return �A�N�V�������
     */
    public ActionType getActionType()
    {
        return actionType_;
    }

    /**
     * �A�N�V������ʂ�ݒ肷��B
     *
     * @param actionType �A�N�V�������
     */
    public void setActionType(ActionType actionType)
    {
        actionType_ = actionType;
    }

    /**
     * �A�N�V������ʂ�ݒ肷��B
     *
     * @param actionType �A�N�V�������
     */
    public void setActionType(String actionType)
    {
        actionType_ = ACTION_TYPE_MAP.get(actionType);
    }

    /**
     * �����Ώۃf�[�^��ݒ肷��
     * @param inputData �����Ώۃf�[�^
     */
    public void setInputData(String inputData)
    {
        inputData_ = inputData;
    }

    /**
     * �����Ώۃf�[�^���擾����
     * @return inputData �����Ώۃf�[�^
     */
    public String getInputData()
    {
        return inputData_;
    }
}
