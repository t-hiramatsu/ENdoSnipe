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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats.oracle;

import static jp.co.acroquest.endosnipe.javelin.jdbc.stats.SqlTraceStatus.KEY_SESSION_CLOSING;
import static jp.co.acroquest.endosnipe.javelin.jdbc.stats.SqlTraceStatus.KEY_SESSION_FINISHED;
import static jp.co.acroquest.endosnipe.javelin.jdbc.stats.SqlTraceStatus.KEY_SESSION_INITIALIZING;
import static jp.co.acroquest.endosnipe.javelin.jdbc.stats.SqlTraceStatus.KEY_SESSION_STARTED;

import java.sql.Connection;

import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.Callback;

/**
 * Oracle�̃Z�b�V�������I�����邽�߂̃R�[���o�b�N
 * @author akiba
 *
 */
public class OracleSessionStopCallback implements Callback
{
    /** �R�l�N�V���� */
    private final Connection connection_;

    /**
     * �R���X�g���N�^
     * @param connection �R�l�N�V����
     */
    public OracleSessionStopCallback(final Connection connection)
    {
        this.connection_ = connection;
    }

    /**
     * SQL�g���[�X���I������B
     */
    public void execute()
    {
        CallTree tree = CallTreeRecorder.getInstance().getCallTree();

        // �uSQL�g���[�X�I�����v�ɑJ�ڂ���B
        tree.removeFlag(KEY_SESSION_INITIALIZING);
        tree.removeFlag(KEY_SESSION_STARTED);
        tree.setFlag(KEY_SESSION_CLOSING, KEY_SESSION_CLOSING);

        OracleProcessor.stopSqlTrace(this.connection_);

        // �uSQL�g���[�X��~���v�ɑJ�ڂ���B
        tree.removeFlag(KEY_SESSION_CLOSING);
        tree.setFlag(KEY_SESSION_FINISHED, KEY_SESSION_FINISHED);
    }
}
