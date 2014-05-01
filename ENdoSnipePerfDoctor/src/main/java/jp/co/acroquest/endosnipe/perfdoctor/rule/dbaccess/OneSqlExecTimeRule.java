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

import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;

/**
 * ����SQL�̑����s���Ԃ��J�E���g���郋�[���B
 * 
 * @author tooru
 */
public class OneSqlExecTimeRule extends AbstractDbAccessRule
{
    /** SQL�̊J�n�^�O */
    private static final String                SQL_TAG = "[SQL]";

    /** �x���Ɣ��f���� SQL ���s�񐔂�臒l */
    public long                                threshold;

    /** ����SQL���̑����s���Ԃ��L�^����Map */
    private final Map<SqlEntry, SqlCountEntry> sqlCounts_;

    /**
     * �R���X�g���N�^�B
     */
    public OneSqlExecTimeRule()
    {
        super();
        this.sqlCounts_ = new HashMap<SqlEntry, SqlCountEntry>();
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

        String threadName = element.getThreadName();

        String sql = content;
        SqlEntry key = new SqlEntry(threadName, sql);
        SqlCountEntry countEntry = this.sqlCounts_.get(key);
        String[] args = JavelinLogUtil.getArgs(element);
        String timeStr = JavelinLogUtil.getArgContent(args[0], "[Time]");

        long count;
        if (countEntry != null)
        {
            count = countEntry.getCount() + Long.parseLong(timeStr);

        }
        else
        {
            countEntry = new SqlCountEntry();
            count = Long.parseLong(timeStr);
            this.sqlCounts_.put(key, countEntry);
        }
        countEntry.setCount(count);
        countEntry.addBindValCount(bindVal);

        if (countEntry.getErrorElement() == null && count >= this.threshold)
        {
            countEntry.setErrorElement(element);
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
        for (Map.Entry<SqlEntry, SqlCountEntry> entry : this.sqlCounts_.entrySet())
        {
            SqlEntry sqlEntry = entry.getKey();
            SqlCountEntry countEntry = entry.getValue();
            Long count = countEntry.getCount();
            JavelinLogElement errorElement = countEntry.getErrorElement();
            if (errorElement == null)
            {
                continue;
            }
            String threadName = errorElement.getThreadName();
            String sql = sqlEntry.getSql();
            addError(sql + "@" + threadName, errorElement, this.threshold, count,
                     countEntry.getBindValCount(), threadName);
        }
    }

    /**
     * �X���b�h���Ƃ�SQL
     */
    private static class SqlEntry
    {
        /** �X���b�h���� */
        private final String threadName_;

        /** SQL �̓��e */
        private final String sql_;

        /**
         * �R���X�g���N�^�B
         * @param threadName �X���b�h��
         * @param sql SQL
         */
        public SqlEntry(final String threadName, final String sql)
        {
            this.threadName_ = threadName;
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

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((this.sql_ == null) ? 0 : this.sql_.hashCode());
            result =
                     PRIME * result
                             + ((this.threadName_ == null) ? 0 : this.threadName_.hashCode());
            return result;
        }

        /**
         * {@inheritDoc}
         */
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
            final SqlEntry OTHER = (SqlEntry)obj;
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
            if (this.threadName_ == null)
            {
                if (OTHER.threadName_ != null)
                {
                    return false;
                }
            }
            else if (this.threadName_.equals(OTHER.threadName_) == false)
            {
                return false;
            }
            return true;
        }
    }
}
