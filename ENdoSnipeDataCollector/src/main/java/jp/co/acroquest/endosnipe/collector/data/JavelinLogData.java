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
package jp.co.acroquest.endosnipe.collector.data;

import java.io.File;

/**
 * Javelin ���O��\�� {@link JavelinData} �ł��B<br />
 * Javelin ���O�� {@link String} �I�u�W�F�N�g�ŃI���������ɕێ����邩�A
 * �ۑ����ꂽ�ꎞ�t�@�C�� {@link File} �I�u�W�F�N�g�Ƃ��ĕێ����܂��B<br />
 * 
 * @author y-komori
 */
public class JavelinLogData extends AbstractJavelinData
{
    /** �f�t�H���g��StringBuilder�̃T�C�Y */
    private static final int DEFAULT_STRING_BUILDER_SIZE = 64;

    /** ���O�t�@�C���� */
    private String logFileName_;

    /** �t�@�C�� */
    private File   file_;

    /** ���e */
    private String contents_;
    
    private String agentName_;

    /** �A���[��臒l */
    private long   alarmThreshold_;

    /** �A���[��CPU臒l */
    private long   cpuAlarmThreshold_;

    /**
     * ���O���e��n���� {@link JavelinLogData} ���\�z���܂��B<br />
     * 
     * @param contents ���O���e
     */
    public JavelinLogData(final String contents)
    {
        if (contents == null)
        {
            throw new IllegalArgumentException("contents can't be null");
        }

        this.contents_ = contents;
    }

    /**
     * ���O�t�@�C���� {@link JavelinLogData} ���\�z���܂��B<br />
     * 
     * @param file Javelin ���O�t�@�C����\�� {@link File} �I�u�W�F�N�g
     */
    public JavelinLogData(final File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file can't be null");
        }

        this.file_ = file;
    }

    /**
     * ���O�ۑ���� {@link File} �I�u�W�F�N�g��Ԃ��܂��B<br />
     * 
     * @return ���O�ۑ���� {@link File} �I�u�W�F�N�g
     */
    public File getFile()
    {
        return file_;
    }

    /**
     * �ێ����Ă���ꎞ�t�@�C�����폜���܂��B<br />
     * 
     * @return �폜�ɐ��������ꍇ�� <code>true</code>
     */
    public boolean deleteFile()
    {
        if (file_ != null)
        {
            boolean result = file_.delete();
            return result;
        }
        else
        {
            return true;
        }
    }

    /**
     * ���O���e��Ԃ��܂��B<br />
     * 
     * @return ���O���e
     */
    public String getContents()
    {
        return contents_;
    }

    /**
     * ���O�t�@�C������Ԃ��܂��B<br />
     * 
     * @return ���O�t�@�C����
     */
    public String getLogFileName()
    {
        return logFileName_;
    }

    /**
     * ���O�t�@�C������ݒ肵�܂��B<br />
     * 
     * @param logFileName ���O�t�@�C����
     */
    public void setLogFileName(final String logFileName)
    {
        logFileName_ = logFileName;
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

    /* (non-Javadoc)
     * @see jp.co.acroquest.endosnipe.collector.data.AbstractJavelinData#getAdditionalString()
     */
    @Override
    protected String getAdditionalString()
    {
        StringBuilder builder = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE);
        if (logFileName_ != null)
        {
            builder.append(logFileName_);
        }
        if (file_ != null)
        {
            builder.append(" ");
            builder.append(file_.getAbsolutePath());
        }
        return builder.toString();
    }

    /**
     * �G�[�W�F���g�����擾���܂��B
     * @return �G�[�W�F���g�� 
     */
	public String getAgentName() 
	{
		return agentName_;
	}

	/**
	 * �G�[�W�F���g����ݒ肵�܂��B
	 * @param agentName �G�[�W�F���g��
	 */
	public void setAgentName(String agentName) 
	{
		this.agentName_ = agentName;
	}

}
