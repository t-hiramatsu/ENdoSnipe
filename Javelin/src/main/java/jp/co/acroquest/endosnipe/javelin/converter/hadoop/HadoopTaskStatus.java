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
 * �^�X�N�̏�Ԃ�ێ�����N���X
 *
 * @author y_asazuma
 *
 */
public class HadoopTaskStatus
{
    /** �^�X�N�t�F�[�Y�̗񋓑� */
    public static enum Phase{STARTING, MAP, SHUFFLE, SORT, REDUCE, CLEANUP}

    /** �^�X�N�X�e�[�^�X�̗񋓑� */
    public static enum State {RUNNING, SUCCEEDED, FAILED, UNASSIGNED, KILLED,
                              COMMIT_PENDING, FAILED_UNCLEAN, KILLED_UNCLEAN}

    /** �W���uID */
    private String jobID_;

    /** �^�X�N���sID */
    private String taskID_;

    /** �^�X�N�t�F�[�Y */
    private Phase phase_;

    /** �^�X�N�X�e�[�^�X */
    private State state_;

    /** �^�X�N�t�F�[�Y�}�b�v */
    private static final Map<String, Phase> PHASE_MAP = new HashMap<String, Phase>() {
        {
            put("STARTING", Phase.STARTING);
            put("MAP", Phase.MAP);
            put("SHUFFLE", Phase.SHUFFLE);
            put("SORT", Phase.SORT);
            put("REDUCE", Phase.REDUCE);
            put("CLEANUP", Phase.CLEANUP);
        }
    };

    /** �^�X�N�X�e�[�^�X�}�b�v */
    private static final Map<String, State> STATE_MAP = new HashMap<String, State>() {
        {
            put("RUNNING", State.RUNNING);
            put("SUCCEEDED", State.SUCCEEDED);
            put("FAILED", State.FAILED);
            put("UNASSIGNED", State.UNASSIGNED);
            put("KILLED", State.KILLED);
            put("COMMIT_PENDING", State.COMMIT_PENDING);
            put("FAILED_UNCLEAN", State.FAILED_UNCLEAN);
            put("KILLED_UNCLEAN", State.KILLED_UNCLEAN);
        }
    };

    /**
     * �R���X�g���N�^
     */
    public HadoopTaskStatus()
    {
        // �������Ȃ�
    }

    /**
     * �R���X�g���N�^
     *
     * @param jobID �W���uID
     * @param taskID �^�X�NID
     * @param phase �^�X�N�t�F�[�Y
     * @param state �^�X�N�X�e�[�^�X
     */
    public HadoopTaskStatus(String jobID,
                            String taskID,
                            String phase,
                            String state)
    {
        this.jobID_ = jobID;
        this.taskID_ = taskID;
        this.phase_ = PHASE_MAP.get(phase);
        this.state_ = STATE_MAP.get(state);
    }

    /**
     * �W���uID���擾����B
     *
     * @return �W���uID
     */
    public String getJobID()
    {
        return this.jobID_;
    }

    /**
     * �^�X�NID���擾����B
     *
     * @return �^�X�NID
     */
    public String getTaskID()
    {
        return this.taskID_;
    }

    /**
     * �^�X�N�̃t�F�[�Y���擾����B
     *
     * @return �^�X�N�t�F�[�Y
     */
    public Phase getPhase()
    {
        return this.phase_;
    }

    /**
     * �^�X�N�̃X�e�[�^�X���擾����B
     *
     * @return �^�X�N�X�e�[�^�X
     */
    public State getState()
    {
        return this.state_;
    }

    /**
     * �W���u���sID��ݒ肷��B
     *
     * @param taskID �W���uID
     */
    public void setJobID(String jobID)
    {
        this.jobID_ = jobID;
    }

    /**
     * �^�X�N���sID��ݒ肷��B
     *
     * @param taskID �^�X�NID
     */
    public void setTaskID(String taskID)
    {
        this.taskID_ = taskID;
    }

    /**
     * �^�X�N�̃t�F�[�Y��ݒ肷��B
     *
     * @param phase �^�X�N�t�F�[�Y
     */
    public void setPhase(Phase phase)
    {
        this.phase_ = phase;
    }

    /**
     * �^�X�N�̃t�F�[�Y��ݒ肷��B
     *
     * @param phase �^�X�N�t�F�[�Y
     */
    public void setPhase(String phase)
    {
        this.phase_ = PHASE_MAP.get(phase);
    }

    /**
     * �^�X�N�̃X�e�[�^�X��ݒ肷��B
     *
     * @param state �^�X�N�X�e�[�^�X
     */
    public void setState(State state)
    {
        this.state_ = state;
    }

    /**
     * �^�X�N�̃X�e�[�^�X��ݒ肷��B
     *
     * @param state �^�X�N�X�e�[�^�X
     */
    public void setState(String state)
    {
        this.state_ = STATE_MAP.get(state);
    }
}
