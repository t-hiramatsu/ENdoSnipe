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

import java.util.ArrayList;

/**
 * Hadoop��TaskTrackerStatus����ێ�����N���X
 *
 * @author asazuma
 *
 */
public class HadoopInfo
{
    /** �z�X�g�� */
    private String host_ = null;

    /** �W���u�o�^���̃W���uID */
    private String submitJobID_ = null;

    /** ���������W���uID */
    private String completeJobID_ = null;

    /** ��~���ꂽ�W���uID */
    private String killedJobID_ = null;

    /** �^�X�N��Ԃ̃��X�g */
    private ArrayList<HadoopTaskStatus> taskStatuses_ = null;

    /** �A�N�V������� �̃��X�g*/
    private ArrayList<HadoopAction> taskTrackerActions_ = null;

    /**
     * �R���X�g���N�^
     */
    public HadoopInfo()
    {
        // �������Ȃ�
    }

    /**
     * �z�X�g�����擾����B
     *
     * @return �z�X�g��
     */
    public String getHost()
    {
        return this.host_;
    }

    /**
     * �z�X�g����ݒ肷��B
     *
     * @param host �z�X�g��
     */
    public void setHost(String host)
    {
        this.host_ = host;
    }

    /**
     * �^�X�N��Ԃ̃��X�g���擾����B
     *
     * @return �^�X�N��Ԃ̃��X�g
     */
    public ArrayList<HadoopTaskStatus> getTaskStatuses()
    {
        return this.taskStatuses_;
    }

    /**
     * �^�X�N��Ԃ̃��X�g��ݒ肷��B
     *
     * @param taskStatusList �^�X�N��Ԃ̃��X�g
     */
    public void setTaskStatuses(ArrayList<HadoopTaskStatus> taskStatusList)
    {
        this.taskStatuses_ = taskStatusList;
    }

    /**
     * �^�X�N��Ԃ����X�g�ɒǉ�����B
     *
     * @param taskStatus �^�X�N���
     */
    public void addTaskStatus(HadoopTaskStatus taskStatus)
    {
        if (taskStatus == null) return;

        if (this.taskStatuses_ == null)
        {
            this.taskStatuses_ = new ArrayList<HadoopTaskStatus>(1);
        }

        this.taskStatuses_.add(taskStatus);
    }

    /**
     * �A�N�V�������̃��X�g���擾����B
     *
     * @return �A�N�V�������̃��X�g
     */
    public ArrayList<HadoopAction> getTaskTrackerActions()
    {
        return this.taskTrackerActions_;
    }

    /**
     * �A�N�V�������̃��X�g��ݒ肷��B
     *
     * @param hadoopTaskTrackerActions �A�N�V�������̃��X�g
     */
    public void setTaskTrackerActions(ArrayList<HadoopAction> hadoopTaskTrackerActions)
    {
        this.taskTrackerActions_ = hadoopTaskTrackerActions;
    }

    /**
     * �A�N�V��������ǉ�����B
     *
     * @param hadoopTaskTrackerAction Hadoop��TaskTracker�̃A�N�V�������
     */
    public void addTaskTrackerAction(HadoopAction hadoopTaskTrackerAction)
    {
        if (this.taskTrackerActions_ == null)
        {
            this.taskTrackerActions_ = new ArrayList<HadoopAction>(1);
        }

        this.taskTrackerActions_.add(hadoopTaskTrackerAction);
    }

    /**
     * �W���u�o�^���������Ă��邩
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasSubmitInfo()
    {
        return this.submitJobID_ != null;
    }

    /**
     * �W���u�o�^���̃W���uID���擾
     *
     * @return �W���uID
     */
    public String getSubmitJobID()
    {
        return this.submitJobID_;
    }

    /**
     * �W���u�o�^���̃W���uID��ݒ�
     *
     * @param submitJobID �W���uID
     */
    public void setSubmitJobID(String submitJobID)
    {
        this.submitJobID_ = submitJobID;
    }

    /**
     * �����W���u���������Ă��邩
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasCompleteInfo()
    {
        return this.completeJobID_ != null;
    }

    /**
     * ���������W���uID���擾
     *
     * @return �W���uID
     */
    public String getCompleteJobID()
    {
        return this.completeJobID_;
    }

    /**
     * ���������W���uID��ݒ�
     *
     * @param completeJobID �W���uID
     */
    public void setCompleteJobID(String completeJobID)
    {
        this.completeJobID_ = completeJobID;
    }

    /**
     * �X�e�[�^�X���������Ă��邩���擾����B
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasStatuses()
    {
        if (this.taskStatuses_ == null)
            return false;
        else
            return !this.taskStatuses_.isEmpty();
    }

    /**
     * �A�N�V�������������Ă��邩���擾����B
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasActions()
    {
        if (this.taskTrackerActions_ == null)
            return false;
        else
            return !this.taskTrackerActions_.isEmpty();
    }

    /**
     * ��~���ꂽJobID���擾����B
     * 
     * @return ��~���ꂽ�W���u��ID
     */
    public String getKilledJobID()
    {
        return this.killedJobID_;
    }

    /**
     * ��~���ꂽJobID��ݒ肷��B
     * 
     * @param killedJobID ��~���ꂽ�W���u��ID
     */
    public void setKilledJobID(String killedJobID)
    {
        this.killedJobID_ = killedJobID;
    }

    /**
     * ��~���ꂽ�W���u���������Ă��邩���擾����B
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasKilledInfo()
    {
        return this.killedJobID_ != null;
    }
}
