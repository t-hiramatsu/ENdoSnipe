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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.RecordStrategy;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * ����SQL�̎��s�v��o�͂����莞�Ԓ������ꍇ�ɁA���s�v��̋L�^�E�ʒm���s��RecordStrategy�B
 * 臒l��javelin.jdbc.planInterval�Ŏw�肷��B
 * 
 * @author tsukano
 */
public class SqlPlanStrategy implements RecordStrategy
{
    /** ���ss�v���ۑ�����ۂ̃L�[ */
    private static final String JDBC_PLAN_KEY = "jdbc.plan";

    /** ����SQL�̎��s�v��o�͂��A���[���ɂ���臒l */
    private long threshold_;

    /** 臒l��\���v���p�e�B�� */
    private static final String PLAN_INTERVAL_KEY =
            JavelinConfig.JAVELIN_PREFIX + "jdbc.planInterval";

    /** 臒l�̃f�t�H���g(1��) */
    private static final int DEFAULT_PLAN_INTERVAL = 60000 * 60 * 24;

    /** CallTreeNode�ɓo�^����ۂ̃L�[ */
    public static final String KEY = "SqlPlanStrategy";

    /**
     * ���s�v��̍ŏI�o�͓�����ێ�����}�b�v�B
     * �S�g�����U�N�V�����ɂ܂������ĕێ����Ă���B
     * key=SQL����hashCode�Avalue=���s�v��̍ŏI�o�͓���
     */
    private static Map<Integer, Long> sqlPlanMap__ = new ConcurrentHashMap<Integer, Long>();

    /**
     * �v���p�e�B����planInterval��ǂݍ��ށB
     */
    public SqlPlanStrategy()
    {
        JavelinConfigUtil configUtil = JavelinConfigUtil.getInstance();
        threshold_ = configUtil.getLong(PLAN_INTERVAL_KEY, DEFAULT_PLAN_INTERVAL);
    }

    /**
     * SQL�̎��s�v��o�͗p��SQL�����邩���肵�܂��B<br />
     * 
     * @param sql ���s�v��o�͎��Ԃ��L�^����SQL
     * 
     * @return ���s�v��o�͗p��SQL���o�^����Ă���A���������؂�Ă��Ȃ��ꍇ�� <code>true</code>
     */
    public boolean existPlanOutputSql(String sql)
    {
        boolean result = false;
        if (sql == null)
        {
            return result;
        }

        long now = System.currentTimeMillis();
        Integer hashCode = Integer.valueOf(sql.hashCode());

        Long lastOutputTimeLong = sqlPlanMap__.get(hashCode);
        if (lastOutputTimeLong != null)
        {
            long lastOutputTime = lastOutputTimeLong.longValue();
            if (now - lastOutputTime <= threshold_)
            {
                result = true;
            }
        }

        return result;
    }

    /**
     * SQL�̎��s�v��o�͗p��SQL��o�^���܂��B<br />
     * 
     * @param sql ���s�v��o�͗p��SQL
     */
    public void recordPlanOutputSql(String sql)
    {
        if (sql == null)
        {
            return;
        }
        long now = System.currentTimeMillis();
        Integer hashCode = Integer.valueOf(sql.hashCode());

        sqlPlanMap__.put(hashCode, now);
    }

    /**
     * Javeliln���O�͏o�͂��Ȃ��B
     * �ۑ����Ă���SQL�������Ȃ����ꍇ�ɍ폜����B
     * 
     * @param node �m�[�h�B
     * @return Javelin���O�t�@�C�����o�͂��邩�ǂ����B
     */
    public boolean judgeGenerateJaveinFile(CallTreeNode node)
    {
        if (sqlPlanMap__.size() > new JavelinConfig().getRecordInvocationMax())
        {
            sqlPlanMap__.clear();
        }

        return false;
    }

    /**
     * ���s�v���ݒ肷��B
     * @param node �m�[�h
     * @param execPlan ���s�v��
     */
    public void setExecPlan(CallTreeNode node, String[] execPlan)
    {
        node.getInvocation().putOptValue(JDBC_PLAN_KEY, execPlan);
    }

    /**
     * ���s�v����擾����B
     * @param node �m�[�h
     * @return ���s�v��
     */
    public String[] getExecPlan(CallTreeNode node)
    {
        return (String[])node.getInvocation().getOptValue(JDBC_PLAN_KEY);
    }

    /**
     * {@inheritDoc}
     */
    public boolean judgeSendExceedThresholdAlarm(CallTreeNode node)
    {
        return judgeGenerateJaveinFile(node);
    }

    /**
     * �������Ȃ��B
     */
    public void postJudge()
    {
        // Do Nothing
    }

    /**
     * �������Ȃ��B
     * @param node CallTreeNode
     * @return null
     */
    public JavelinLogCallback createCallback(CallTreeNode node)
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
