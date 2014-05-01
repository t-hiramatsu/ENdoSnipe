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
import junit.framework.TestCase;

/**
 * TAT���Ԃ�臒l���肷��N���X�̃e�X�g�B
 * @author fujii
 *
 */
public class DefaultRecordStrategyTest extends TestCase
{
    /** Javelin�ݒ�t�@�C���̃p�X */
    private static final String JAVELIN_CONFIG_PATH = "test/strategy/conf";

    /**
     * ���������\�b�h<br />
     * �V�X�e�����O�̏��������s���B
     */
    @Override
    public void setUp()
        throws Exception
    {
        super.setUp();
        // �I�v�V�����t�@�C������A�I�v�V�����ݒ��ǂݍ��ށB
        JavelinConfig config = new JavelinConfig(JAVELIN_CONFIG_PATH);
        SystemLogger.initSystemLog(config);
    }

    /**
    /**
     * [����] 4-2-9 createCallback�̃e�X�g�B <br />
     * �ETAT���ԁF1000(�~���b)�ATAT�̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  createCallback���ĂԁB<br />
     * ��null���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testCreateCallback_Under()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B
        node.getInvocation().setAlarmThreshold(2000);

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
     * [����] 4-2-10 createCallback�̃e�X�g�B <br />
     * �ETAT���ԁF5000(�~���b)�ATAT�̃A���[��臒l�F2000(�~���b)�ŁA<br />
     *  createCallback���ĂԁB<br />
     * ��JavelinLogCallback�I�u�W�F�N�g���Ԃ�B
     * 
     * @throws Exception ��O
     */
    public void testCreateCallback_Over()
        throws Exception
    {
        // ����
        CallTreeNode node = createCallTreeNode();

        // TAT�̃A���[��臒l��ݒ肷��B
        node.getInvocation().setAlarmThreshold(2000);

        // TAT���Ԃ�ݒ�ɂ���B
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
