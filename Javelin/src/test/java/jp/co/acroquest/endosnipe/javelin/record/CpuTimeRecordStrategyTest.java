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
package jp.co.acroquest.endosnipe.javelin.record;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;
import jp.co.acroquest.test.util.JavelinTestUtil;
import junit.framework.TestCase;

/**
 * CPU���ԁATAT���Ԃ�臒l���肷��N���X�̃e�X�g�B
 * @author fujii
 *
 */
public class CpuTimeRecordStrategyTest extends TestCase
{
    private static final int MILLI_TO_NANO = 1000000;

    /** Javelin�ݒ�t�@�C���̃p�X */
    private static final String JAVELIN_CONFIG_PATH = "/strategy/conf/javelin.properties";

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinTestUtil.camouflageJavelinConfig(getClass(), JAVELIN_CONFIG_PATH);
        JavelinConfig config = new JavelinConfig();
        SystemLogger.initSystemLog(config);
    }

    /**
     * [����] 4-2-5 judgeSendExceedThresholdAlarm�̃e�X�g�B <br />
     * �ECPU����:1000(�~���b) TAT���ԁF5000(�~���b)�A
     *  CPU���Ԃ̃A���[��臒l:500(�~���b)�ATAT���Ԃ̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  judgeSendExceedThresholdAlarm���ĂԁB<br />
     * ��true���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testJudgeSendExceedThresholdAlarm_CpuOver_TATOver()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(1000 * MILLI_TO_NANO);

        // TAT���Ԃ�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(5000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        boolean isAlarm = strategy.judgeSendExceedThresholdAlarm(node);

        // ����
        assertTrue(isAlarm);
    }

    /**
     * [����] 4-2-6 judgeSendExceedThresholdAlarm�̃e�X�g�B <br />
     * �ECPU����:100(�~���b) TAT���ԁF5000(�~���b)�A
     *  CPU���Ԃ̃A���[��臒l:500(�~���b)�ATAT���Ԃ̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  judgeSendExceedThresholdAlarm���ĂԁB<br />
     * ��true���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testJudgeSendExceedThresholdAlarm_CpuUnder_TATOver()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(100 * MILLI_TO_NANO);

        // TAT���Ԃ�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(5000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        boolean isAlarm = strategy.judgeSendExceedThresholdAlarm(node);

        // ����
        assertTrue(isAlarm);
    }

    /**
     * [����] 4-2-7 judgeSendExceedThresholdAlarm�̃e�X�g�B <br />
     * �ECPU����:1000(�~���b) TAT���ԁF1000(�~���b)�A
     *  CPU���Ԃ̃A���[��臒l:500(�~���b)�ATAT���Ԃ̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  judgeSendExceedThresholdAlarm���ĂԁB<br />
     * ��true���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testJudgeSendExceedThresholdAlarm_CpuOver_TATUnder()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);
        //node.getInvocation().setAlarmCpuThreshold(500);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(1000 * MILLI_TO_NANO);

        // TAT���Ԃ�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(1000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        boolean isAlarm = strategy.judgeSendExceedThresholdAlarm(node);

        // ����
        assertTrue(isAlarm);
    }

    /**
     * [����] 4-2-8 judgeSendExceedThresholdAlarm�̃e�X�g�B <br />
     * �ECPU����:100(�~���b) TAT���ԁF1000(�~���b)�A
     *  CPU���Ԃ̃A���[��臒l:500(�~���b)�ATAT���Ԃ̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  judgeSendExceedThresholdAlarm���ĂԁB<br />
     * ��false���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testJudgeSendExceedThresholdAlarm_CpuUnder_TATUnder()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ�ɂ���B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(100 * MILLI_TO_NANO);

        // TAT���Ԃ�1�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(1000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        boolean isAlarm = strategy.judgeSendExceedThresholdAlarm(node);

        // ����
        assertFalse(isAlarm);
    }

    /**
    /**
     * [����] 4-2-9 judgeGenerateJaveinFile�̃e�X�g�B <br />
     * �ECPU����:100(�~���b) TAT���ԁF1000(�~���b)�A
     *  CPU���Ԃ̋L�^臒l:500(�~���b)�ATAT���Ԃ̋L�^臒l�F2000(�~���b)�ŁA<br />
     *  judgeGenerateJaveinFile���ĂԁB<br />
     * ��JvnCallBack�I�u�W�F�N�g���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testCreateCallback_Under()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(100 * MILLI_TO_NANO);

        // TAT���Ԃ�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(1000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        JavelinLogCallback callback = strategy.createCallback(node);

        // ����
        assertNull(callback);
    }

    /**
    /**
     * [����] 4-2-10 judgeGenerateJaveinFile�̃e�X�g�B <br />
     * �ECPU����:1000(�~���b) TAT���ԁF5000(�~���b)�A
     *  CPU���Ԃ̋L�^臒l:500(�~���b)�ATAT���Ԃ̋L�^臒l�F2000(�~���b)�ŁA<br />
     *  judgeGenerateJaveinFile���ĂԁB<br />
     * ��JvnCallBack�I�u�W�F�N�g���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testCreateCallback_Over()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B(CPU�̃A���[��臒l��javelin.properties�ɂĐݒ�)
        node.getInvocation().setAlarmThreshold(2000);

        // CPU���Ԃ�ݒ肷��B
        node.setCpuTime(100 * MILLI_TO_NANO);

        // TAT���Ԃ�ݒ肷��B
        node.setStartTime(0);
        node.setEndTime(5000);

        CpuTimeRecordStrategy strategy = new CpuTimeRecordStrategy();

        // ���s
        JavelinLogCallback callback = strategy.createCallback(node);

        // ����
        assertNotNull(callback);
    }

    /**
     * �f�t�H���g��CallTreeNode���쐬����B
     * @return CallTreeNode
     * @throws Exception�@��O
     */
    private CallTreeNode createCallTreeNode()
        throws Exception
    {
        // Invocation�ݒ�
        Invocation invocation =
                new Invocation("pid@host", "RootCallerName", "callerMethod", 0);
        CallTreeNode node = new CallTreeNode();
        node.setInvocation(invocation);
        return node;
    }

}
