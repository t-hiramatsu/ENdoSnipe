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
package jp.co.acroquest.endosnipe.javelin.communicate;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.communicator.impl.CommunicationServerImpl;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;

/**
 * Javelin�̒ʐM�p�X���b�h
 * @author eriguchi
 *
 */
public class JavelinAcceptThread extends CommunicationServerImpl implements AlarmListener,
        TelegramConstants
{
    /** �ݒ� */
    static JavelinConfig config__ = new JavelinConfig();

    /** JavelinAcceptThread�̃C���X�^���X(�V���O���g��) */
    static JavelinAcceptThread instance__ = new JavelinAcceptThread();
    private JavelinAcceptThread()
    {
        super(config__.isAcceptPortIsRange(), config__.getAcceptPortRangeMax(),
                JavelinTransformer.WAIT_FOR_THREAD_START, 
                config__.getJavelinBindInterval(), 
                config__.getTelegramListeners().split(","));
        this.isJavelin_ = true;
        StatsJavelinRecorder.addListener(this);
    }

    /**
     * ���������\�b�h
     */
    public void init()
    {
        // Do Nothing.
    }

    /**
     * �X���b�h���J�n���܂��B
     */
    public void start()
    {
        int port = config__.getAcceptPort();
        this.start(port);
    }
        
    /**
     * JavelinAcceptThread�̃C���X�^���X���擾����B
     * @return JavelinAcceptThread
     */
    public static JavelinAcceptThread getInstance()
    {
        return instance__;
    }

    /**
     * {@inheritDoc}
     */
    public void sendExceedThresholdAlarm(final CallTreeNode node)
    {
        List<Invocation> invocationList = new ArrayList<Invocation>();
        List<Long> accumulateTimeList = new ArrayList<Long>();
        addInvocationList(invocationList, accumulateTimeList, node);

        Telegram objTelegram = JavelinTelegramCreator.create(invocationList,
                accumulateTimeList, BYTE_TELEGRAM_KIND_ALERT,
                BYTE_REQUEST_KIND_NOTIFY);

        // �A���[���𑗐M����B
        sendTelegram(objTelegram);
    }

    /**
     * CallTree�ɕۑ����ꂽ�S�Ă�Invocation�Ǝ��s���Ԃ��ċA�I��List�ɕۑ����܂��B<br />
     * 
     * @param invocationList Invocation��ۑ����郊�X�g�B
     * @param accumulateTimeList ���s���Ԃ�ۑ����郊�X�g�B
     * @param node CallTreeNode
     */
    private void addInvocationList(List<Invocation> invocationList, List<Long> accumulateTimeList,
            final CallTreeNode node)
    {
        invocationList.add(node.getInvocation());
        accumulateTimeList.add(node.getAccumulatedTime());
        List<CallTreeNode> children = node.getChildren();
        for (int index = 0; index < children.size(); index++)
        {
            CallTreeNode child = children.get(index);
            addInvocationList(invocationList, accumulateTimeList, child);
        }

    }

    /**
     * ����AlarmListener�����[�g�m�[�h�݂̂��������邩�ǂ�����Ԃ��B �����̃N���X�ł́A���false��Ԃ��B
     * 
     * @see jp.co.acroquest.endosnipe.javelin.communicate.AlarmListener#isSendingRootOnly()
     * @return false(���[�g�m�[�h�ȊO����������)
     */
    public boolean isSendingRootOnly()
    {
        return false;
    }

    /**
     * �N���C�A���g�������Ă��邩�ǂ����B
     * @return true:�N���C�A���g�������Ă���Afalse:�N���C�A���g�������Ă��Ȃ��B
     */
    public boolean hasClient()
    {
        return this.getActiveClient() > 0;
    }

}
