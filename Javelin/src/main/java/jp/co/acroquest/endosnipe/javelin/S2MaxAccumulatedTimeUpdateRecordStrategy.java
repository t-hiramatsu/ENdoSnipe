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
package jp.co.acroquest.endosnipe.javelin;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.javelin.log.JavelinLogCallback;

/**
 * �Ăяo���ώZ���Ԃ̍ő�l���X�V���ꂽ�ꍇ�ɁA�L�^�E�ʒm���s��RecordStrategy�B �������A�s�K�v�ȋL�^�E�ʒm��h�����߁A�X�V�񐔂�
 * javelin.maxAccumulatedTimeUpdate.ignoreUpdateCount�Ŏw�肳�ꂽ �񐔈ȉ��̏ꍇ�͋L�^�E�ʒm���s��Ȃ��B
 * 
 * @author tsukano
 */
public class S2MaxAccumulatedTimeUpdateRecordStrategy implements RecordStrategy
{
    /** �X�V�񐔂𖳎�����臒l */
    private final int ignoreUpdateCount_;

    /** �X�V�񐔂𖳎�����臒l��\���v���p�e�B�� */
    private static final String IGNOREUPDATECOUNT_KEY =
            JavelinConfig.JAVELIN_PREFIX + "maxAccumulatedTimeUpdate.ignoreUpdateCount";

    /** �X�V�񐔂𖳎�����臒l�̃f�t�H���g */
    private static final int DEFAULT_IGNOREUPDATECOUNT = 3;

    /**
     * �v���p�e�B����ignoreUpdateCount��ǂݍ��ށB
     */
    public S2MaxAccumulatedTimeUpdateRecordStrategy()
    {
        JavelinConfigUtil configUtil = JavelinConfigUtil.getInstance();
        this.ignoreUpdateCount_ =
                configUtil.getInteger(IGNOREUPDATECOUNT_KEY, DEFAULT_IGNOREUPDATECOUNT);
    }

    /**
     * �A���[���𔭐������邩�ǂ����𔻒肵�܂��B<br />
     * 
     * @param node {@link CallTreeNode}�I�u�W�F�N�g
     * @return �A���[���𔭐�������ꍇ�A<code>true</code>
     */
    public boolean judgeSendExceedThresholdAlarm(final CallTreeNode node)
    {
        if (node.getAccumulatedTime() >= node.getInvocation().getMaxAccumulatedTime()
                && node.getInvocation().getCountFromStartup() > this.ignoreUpdateCount_)
        {
            return true;
        }

        return false;
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

    /**
     * �X�V�񐔂𖳎�����臒l���擾����B
     * @return �X�V�񐔂𖳎�����臒l
     */
    public int getIgnoreUpdateCount()
    {
        return ignoreUpdateCount_;
    }
}
