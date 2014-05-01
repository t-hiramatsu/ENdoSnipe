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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.SqlCountOverEvent;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ����g�����U�N�V�������ŁA����SQL�̌Ăяo���񐔂����񐔂𒴂����ꍇ�ɁA�L�^�E�ʒm���s��RecordStrategy�B
 * 臒l��javelin.jdbc.sqlcount�Ŏw�肷��B
 * 
 * @author tsukano
 */
public class SqlCountStrategy implements RecordStrategy
{
    /** ����SQL�̌Ăяo���񐔂��A���[���ɂ���臒l */
    private final long threshold_;

    /**
     * SQL�̌Ăяo���񐔂�ێ�����}�b�v�B
     * key=SQL���Avalue=�Ăяo����
     */
    private final Map<String, Integer> sqlCountMap_ = new HashMap<String, Integer>();

    /**
     * �v���p�e�B����sqlcount��ǂݍ��ށB
     */
    public SqlCountStrategy()
    {
        JdbcJavelinConfig config = new JdbcJavelinConfig();
        threshold_ = config.getSqlcount();
    }

    /**
     * SQL�̌Ăяo���񐔂̃J�E���g�𑝂₷�B
     * @param sql �Ăяo���񐔂𑝂₷SQL
     */
    public void incrementSQLCount(final String sql)
    {
        synchronized (sqlCountMap_)
        {
            int newValue;
            if (sqlCountMap_.containsKey(sql))
            {
                // ���ɌĂяo���o��������SQL�͌Ăяo���񐔂�1���₷
                int old = sqlCountMap_.get(sql);
                newValue = old + 1;
            }
            else
            {
                // ���ɌĂяo���o�����Ȃ�SQL�͌Ăяo���񐔂�1�ɂ���
                newValue = 1;
            }

            sqlCountMap_.put(sql, newValue);
            if (newValue >= threshold_)
            {
                CommonEvent event = new SqlCountOverEvent();
                event.addParam(EventConstants.PARAM_SQLCOUNT_THRESHOLD, String.valueOf(threshold_));
                event.addParam(EventConstants.PARAM_SQLCOUNT_ACTUAL, String.valueOf(newValue));
                event.addParam(EventConstants.PARAM_SQLCOUNT_SQL, sql);
                JavelinConfig config = new JavelinConfig();
                StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
                String stackTrace = ThreadUtil.getStackTrace(stacktraces, config.getTraceDepth());
                event.addParam(EventConstants.PARAM_SQLCOUNT_STACKTRACE, stackTrace);
                StatsJavelinRecorder.addEvent(event);
            }
        }
    }

    /**
     * ���false��Ԃ��܂��B
     * 
     * @param node �g�p���܂���B
     * @return ���false�B 
     */
    public boolean judgeGenerateJaveinFile(final CallTreeNode node)
    {
        return false;
    }

    /**
     * ���false��Ԃ��܂��B
     * 
     * @param node �g�p���܂���B
     * @return ���false�B 
     */
    public boolean judgeSendExceedThresholdAlarm(final CallTreeNode node)
    {
        return judgeGenerateJaveinFile(node);
    }

    /**
     * SQL�̌Ăяo���񐔂̏����N���A����B
     */
    public void postJudge()
    {
        synchronized (sqlCountMap_)
        {
            sqlCountMap_.clear();
        }
    }

    /**
     * �������Ȃ��B
     * @param node CallTreeNode
     * @return null
     */
    public JavelinLogCallback createCallback(final CallTreeNode node)
    {
        // Do Nothing
        return null;
    }

    /**
     * �������Ȃ��B
     * @return null
     */
    public JavelinLogCallback createCallback()
    {
        // Do Nothing
        return null;
    }
}
