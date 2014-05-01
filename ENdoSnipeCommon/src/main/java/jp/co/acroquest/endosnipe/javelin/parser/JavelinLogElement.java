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
package jp.co.acroquest.endosnipe.javelin.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;

/**
 * ���샍�O�t�@�C������؂�o������v�f���̃��O�B ��v�f���̃��O�́A���O�̏o�͎����A���\�b�h���A�N���X���A �I�u�W�F�N�gID�Ȃǂ�\���u��{���v��
 * �ϐ��̒l��X�^�b�N�g���[�X�Ȃǂ�\���u�ڍ׏��v����Ȃ�B
 * 
 * @author kameda
 */
public class JavelinLogElement
{
    private String logFileName_;

    private int startLogLine_;

    private int endLogLine_;

    /** Javelin��IP�A�h���X�B���̃f�[�^��DataCollector�݂̂��g�p����B */
    private String ipAddress_;

    /** Javelin�̃|�[�g�ԍ��B���̃f�[�^��DataCollector�݂̂��g�p����B */
    private int port_;

    /** Javelin�̃|�[�g�ԍ��B���̃f�[�^��DataCollector�݂̂��g�p����B */
    private String databaseName_;

    /** �A���[��臒l */
    private long alarmThreshold_;

    /** �A���[��CPU臒l */
    private long cpuAlarmThreshold_;

    private List<String> baseInfoList_;

    private Map<String, String> detailInfoMap_;

    private String[] args_;

    /**
     * JavalinLogElement�̏�����
     */
    public JavelinLogElement()
    {
        this.detailInfoMap_ = new HashMap<String, String>();
    }

    /**
     * �i�[���Ă����{����Ԃ��B
     * 
     * @return ��{���̃��X�g
     */
    public List<String> getBaseInfo()
    {
        return this.baseInfoList_;
    }

    /**
     * �i�[���Ă���v�f�̎��ʎq��Ԃ�
     * 
     * @return ���ʎq
     */
    public String getLogIDType()
    {
        if (this.baseInfoList_ == null)
        {
            return null;
        }

        return this.baseInfoList_.get(JavelinLogColumnNum.ID);
    }

    /**
     * �i�[���Ă���ڍ׏�񂩂�A �����Ŏw�肵���^�O�̎�ނɑΉ����郍�O�������Ԃ��B
     * 
     * @param tagType
     *            �ڍ׏��̎��
     * @return �ڍ׏��
     */
    public String getDetailInfo(final String tagType)
    {
        return this.detailInfoMap_.get(tagType);
    }

    /**
     * CSV�ŕ���������{�����Z�b�g����B
     * 
     * @param baseInfoList
     *            ��{���
     */
    public void setBaseInfo(final List<String> baseInfoList)
    {
        this.baseInfoList_ = baseInfoList;
    }

    /**
     * �ڍ׏��̃^�O�̎�ނƑΉ����郍�O��������Z�b�g����
     * 
     * @param tagType
     *            �ڍ׏��̃^�O�̎��
     * @param data
     *            �ڍ׏��̓��e
     */
    public void setDetailInfo(final String tagType, final String data)
    {
        this.detailInfoMap_.put(tagType, data);
    }

    /**
     * �ڍ׏��̃}�b�v���Z�b�g���܂��B<br />
     * 
     * @param detailMap
     *            �ڍ׏��̃}�b�v
     */
    public void setDetailInfo(final Map<String, String> detailMap)
    {
        this.detailInfoMap_ = detailMap;
    }

    /**
     * �ڍ׏��̃}�b�v��Ԃ��܂��B<br />
     * 
     * @return �ڍ׏��̃}�b�v
     */
    public Map<String, String> getDetailMap()
    {
        return this.detailInfoMap_;
    }

    /**
     * ���̃��O�̏I���s��Ԃ��܂��B<br />
     * 
     * @return �I���s
     */
    public int getEndLogLine()
    {
        return this.endLogLine_;
    }

    /**
     * ���̃��O�̏I���s���Z�b�g���܂��B<br />
     * 
     * @param endLogLine
     *            �I���s
     */
    public void setEndLogLine(final int endLogLine)
    {
        this.endLogLine_ = endLogLine;
    }

    /**
     * ���̃��O���o�͂���Javelin��IP�A�h���X��Ԃ��܂��B<br />
     * 
     * @return IP�A�h���X
     */
    public String getIpAddress()
    {
        return ipAddress_;
    }

    /**
     * ���̃��O���o�͂���Javelin��IP�A�h���X���Z�b�g���܂��B<br />
     * 
     * @param ipAddress IP�A�h���X
     */
    public void setIpAddress(final String ipAddress)
    {
        ipAddress_ = ipAddress;
    }

    /**
     * ���̃��O���o�͂���Javelin�̃|�[�g�ԍ���Ԃ��܂��B<br />
     * 
     * @return port �|�[�g�ԍ�
     */
    public int getPort()
    {
        return port_;
    }

    /**
     * ���̃��O���o�͂���Javelin�̃|�[�g�ԍ����Z�b�g���܂��B<br />
     * 
     * @param port �|�[�g�ԍ�
     */
    public void setPort(final int port)
    {
        port_ = port;
    }

    /**
     * ���̃��O���ۑ�����Ă���DB�̖��O��Ԃ��܂��B<br />
     * 
     * @return databaseName DB��
     */
    public String getDatabaseName()
    {
        return databaseName_;
    }

    /**
     * ���̃��O���ۑ�����Ă���DB�̖��O���Z�b�g���܂��B<br />
     * 
     * @param databaseName DB��
     */
    public void setDatabaseName(final String databaseName)
    {
        databaseName_ = databaseName;
    }

    /**
     * ���̃��O�̊J�n�s��Ԃ��܂��B<br />
     * 
     * @return �J�n�s
     */
    public int getStartLogLine()
    {
        return this.startLogLine_;
    }

    /**
     * ���̃��O�̊J�n�s���Z�b�g���܂��B<br />
     * 
     * @param startLogLine
     *            �J�n�s
     */
    public void setStartLogLine(final int startLogLine)
    {
        this.startLogLine_ = startLogLine;
    }

    /**
     * ���O�t�@�C������Ԃ��܂��B<br />
     * 
     * @return ���O�t�@�C����
     */
    public String getLogFileName()
    {
        return this.logFileName_;
    }

    /**
     * ���O�t�@�C�������Z�b�g���܂��B
     * 
     * @param logFileName
     *            ���O�t�@�C����
     */
    public void setLogFileName(final String logFileName)
    {
        this.logFileName_ = logFileName;
    }

    /**
     * �X���b�h���̂��擾���܂��B
     * 
     * @return �X���b�h����
     */
    public String getThreadName()
    {
        String ret;

        // ���O�̎�ނ��擾����B
        String id = this.baseInfoList_.get(JavelinLogColumnNum.ID);

        // ���O�̎�ނɉ����āA��{��񕔂���X���b�h���̂��擾���A�Ԃ��B
        if (JavelinConstants.MSG_CALL.equals(id))
        {
            ret = this.baseInfoList_.get(JavelinLogColumnNum.CALL_THREADID);
        }
        else if (JavelinConstants.MSG_RETURN.equals(id))
        {
            ret = this.baseInfoList_.get(JavelinLogColumnNum.RETURN_THREADID);
        }
        else if (JavelinConstants.MSG_CATCH.equals(id))
        {
            ret = this.baseInfoList_.get(JavelinLogColumnNum.CATCH_THREADID);
        }
        else if (JavelinConstants.MSG_THROW.equals(id))
        {
            ret = this.baseInfoList_.get(JavelinLogColumnNum.THROW_THREADID);
        }
        else
        {
            ret = this.baseInfoList_.get(JavelinLogColumnNum.READ_WRITE_THREADID);
        }

        return ret;
    }

    /**
     * �����̔z����擾���܂��B
     * @return �����̔z��
     */
    public String[] getArgs()
    {
        return args_;
    }

    /**
     * �����z���ݒ肵�܂��B
     * @param args �����̔z��
     */
    public void setArgs(final String[] args)
    {
        args_ = args;
    }

    /**
     * �A���[��臒l���擾���܂��B
     * @return �A���[��臒l
     */
    public long getAlarmThreshold()
    {
        return alarmThreshold_;
    }

    /**
     * �A���[��臒l��ݒ肵�܂��B
     * @param alarmThreshold �A���[��臒l
     */
    public void setAlarmThreshold(final long alarmThreshold)
    {
        alarmThreshold_ = alarmThreshold;
    }

    /**
     * �A���[��CPU臒l���擾���܂��B
     * @return �A���[��CPU臒l
     */
    public long getCpuAlarmThreshold()
    {
        return cpuAlarmThreshold_;
    }

    /**
     * �A���[��CPU臒l��ݒ肵�܂��B
     * @param cpuAlarmThreshold �A���[��CPU臒l
     */
    public void setCpuAlarmThreshold(final long cpuAlarmThreshold)
    {
        cpuAlarmThreshold_ = cpuAlarmThreshold;
    }

}
