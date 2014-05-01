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
package jp.co.acroquest.endosnipe.javelin.log;

import java.util.Date;

import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;

/**
 * Javelin���O�̃^�X�N�ł��B<br />
 * 
 * @author eriguchi
 * 
 */
class JavelinLogTask
{
    private final Date               date_;

    private final CallTree           tree_;

    private final CallTreeNode       node_;

    private final String             jvnFileName_;

    private final JavelinLogCallback jvelinLogCallback_;

    private final CallTreeNode       endNode_;

    private final long               telegramId_;

    private final String             itemName_;

    /**
     * �R���X�g���N�^�ł��B�����Ŏw�肵���p�����[�^��ݒ肵�܂��B<br />
     * 
     * @param date
     *            ����
     * @param jvnFileName
     *            Javelin�t�@�C����
     * @param tree
     *            CallTree
     * @param node
     *            CallTreeNode
     * @param jvelinLogCallback
     *            �R�[���o�b�N�I�u�W�F�N�g
     * @param telegramId
     *            �d�� ID
     * @param endNode
     *            �ŏI�m�[�h
     * @param itemName
     *            �A�C�e����
     */
    public JavelinLogTask(final Date date, final String jvnFileName, final CallTree tree,
            final CallTreeNode node, final JavelinLogCallback jvelinLogCallback,
            final CallTreeNode endNode, final long telegramId, final String itemName)
    {
        this.date_ = date;
        this.jvnFileName_ = jvnFileName;
        this.tree_ = tree.copy();
        this.node_ = node;
        this.jvelinLogCallback_ = jvelinLogCallback;
        this.endNode_ = endNode;
        this.telegramId_ = telegramId;
        this.itemName_ = itemName;
    }

    /**
     * �������擾���܂��B<br />
     * 
     * @return ����
     */
    public Date getDate()
    {
        return this.date_;
    }

    /**
     * Javelin�t�@�C�������擾���܂��B<br />
     * 
     * @return Javelin�t�@�C����
     */
    public String getJvnFileName()
    {
        return this.jvnFileName_;
    }

    /**
     * CallTree���擾���܂��B<br />
     * 
     * @return CallTree
     */
    public CallTree getTree()
    {
        return this.tree_;
    }

    /**
     * CallTreeNode���擾���܂��B<br />
     * 
     * @return CallTreeNode
     */
    public CallTreeNode getNode()
    {
        return this.node_;
    }

    /**
     * �R�[���o�b�N�I�u�W�F�N�g���擾���܂��B<br />
     * 
     * @return �R�[���o�b�N�I�u�W�F�N�g
     */
    public JavelinLogCallback getJavelinLogCallback()
    {
        return this.jvelinLogCallback_;
    }

    /**
     * �ŏI�m�[�h���擾���܂��B<br />
     * 
     * @return �ŏI�m�[�h
     */
    public CallTreeNode getEndNode()
    {
        return this.endNode_;
    }

    /**
     * �d�� ID ���擾���܂��B<br />
     * 
     * @return �d�� ID
     */
    public long getTelegramId()
    {
        return this.telegramId_;
    }

    /**
     * �A�C�e�������擾���܂��B<br />
     * 
     * @return �A�C�e����
     */
    public String getItemName()
    {
        return this.itemName_;
    }
}
