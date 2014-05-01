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
package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;

/**
 * ����SQL�̔��s�񐔂��J�E���g���郋�[���B
 * 
 * @author y-komori
 */
public class OneSqlCountRule extends AbstractDbAccessRule
{
    /** SQL�̊J�n�^�O */
    private static final String                   SQL_TAG = "[SQL]";

    /** �x���Ɣ��f���� SQL ���s�񐔂�臒l */
    public int                                    threshold;

    /** ����SQL���̔��s�񐔂��L�^����Map */
    private final Map<SqlKeyEntry, SqlCountEntry> sqlCounts_;

    /**
     * �R���X�g���N�^�B
     */
    public OneSqlCountRule()
    {
        super();
        this.sqlCounts_ = new HashMap<SqlKeyEntry, SqlCountEntry>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doJudgeContent(final JavelinLogElement element, final String content,
            final String bindVal)
    {
        // TODO validation����������ۂɂ͂�����Ń`�F�b�N����悤�ɂ���
        if (this.threshold < 1)
        {
            this.threshold = 1;
        }

        String logFileName = element.getLogFileName();
        String sql = content;

        SqlKeyEntry entry = new SqlKeyEntry(logFileName, sql);
        SqlCountEntry sqlCounter = this.sqlCounts_.get(entry);
        if (sqlCounter != null)
        {
            sqlCounter.setCount(sqlCounter.getCount() + 1);
        }
        else
        {
            sqlCounter = new SqlCountEntry();
            this.sqlCounts_.put(entry, sqlCounter);
        }

        sqlCounter.addBindValCount(bindVal);

        if (sqlCounter.getCount() == this.threshold)
        {
            sqlCounter.setErrorElement(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTagName()
    {
        return SQL_TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doJudgeEnd()
    {
        for (Map.Entry<SqlKeyEntry, SqlCountEntry> entry : this.sqlCounts_.entrySet())
        {
            SqlKeyEntry sqlEntry = entry.getKey();
            SqlCountEntry counter = entry.getValue();
            long count = counter.getCount();
            JavelinLogElement errorElement = counter.getErrorElement();
            if (errorElement == null)
            {
                continue;
            }
            String threadName = errorElement.getThreadName();
            String logFileName = errorElement.getLogFileName();
            String sql = sqlEntry.getSql();
            String durationString = getThresholdStrategy().extractDurationThreshold(errorElement);
            addError(sql + "@" + logFileName, errorElement, this.threshold, count, durationString,
                     counter.getBindValCount(), threadName);
        }
    }

    /**
     * ���O�t�@�C�����Ƃ�SQL
     */
    private static class SqlKeyEntry
    {
        /** ���O�t�@�C������ */
        private final String logFileName_;

        /** SQL �̓��e */
        private final String sql_;

        /**
         * �R���X�g���N�^�B
         * @param logFileName ���O�t�@�C����
         * @param sql SQL
         */
        public SqlKeyEntry(final String logFileName, final String sql)
        {
            this.logFileName_ = logFileName;
            this.sql_ = sql;
        }

        /**
         * SQL�̓��e��Ԃ��B
         * @return SQL��
         */
        public String getSql()
        {
            return this.sql_;
        }

        @Override
        public int hashCode()
        {
            final int PRIME = 31;
            int result = 1;
            int sqlHashCode = 0;
            if (this.sql_ != null)
            {
                sqlHashCode = this.sql_.hashCode();
            }
            result = PRIME * result + sqlHashCode;

            int logFileHashCode = 0;
            if (this.logFileName_ != null)
            {
                logFileHashCode = this.logFileName_.hashCode();
            }
            result = PRIME * result + logFileHashCode;

            return result;
        }

        @Override
        public boolean equals(final Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final SqlKeyEntry OTHER = (SqlKeyEntry)obj;
            if (this.sql_ == null)
            {
                if (OTHER.sql_ != null)
                {
                    return false;
                }
            }
            else if (this.sql_.equals(OTHER.sql_) == false)
            {
                return false;
            }
            if (this.logFileName_ == null)
            {
                if (OTHER.logFileName_ != null)
                {
                    return false;
                }
            }
            else if (this.logFileName_.equals(OTHER.logFileName_) == false)
            {
                return false;
            }
            return true;
        }
    }
}
