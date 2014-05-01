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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * �R�[���c���[���L�^���܂��B
 *
 * @author eriguchi
 */
public class CallTreeRecorder
{
    /** �X���b�hID���Z�b�g����Ă��Ȃ����Ƃ�\���l�B */
    private static final long THREAD_ID_NOT_SET = -1;

    /** �C���X�^���X */
    private static ThreadLocal<CallTreeRecorder> recorder__ = new ThreadLocal<CallTreeRecorder>() {
        @Override
        protected synchronized CallTreeRecorder initialValue()
        {
            return new CallTreeRecorder();
        }
    };

    /** CallTreeNode���X���b�h���Ɋi�[����}�b�v�B */
    private static Map<Long, WeakReference<CallTreeNode>> currentNodeMap__ =
            new ConcurrentHashMap<Long, WeakReference<CallTreeNode>>();

    /**
     * ���\�b�R�[���c���[�̋L�^�p�I�u�W�F�N�g�B<br />
     * ������ <code>new {@link CallTree}()</code> �Ƃ��Ȃ����ƁB
     * {@link CallTree} �̃R���X�g���N�^���ŁA {@link ThreadLocal} �̏������������s���Ă���A
     * ����������������Ȃ����Ƃ����邽�߁B
     */
    private CallTree callTree_ = null;

    /**
     * ���\�b�h�̌Ăяo�����I�u�W�F�N�g�B
     */
    private CallTreeNode callerNode_ =  null;

    /** ���\�b�h�Ăяo���̐[�� */
    private int depth_ = 0;

    /** ���R�[�h���\�b�h���Ăяo���ꂽ�� */
    boolean isRecordMethodCalled_;

    /** ��O������������ */
    boolean isExceptionOccured_;

    /**
     * �X���b�hID �B<br />
     * ������ <code>{@link ThreadUtil}.getThreadId()</code> �Ƃ��Ȃ����ƁB
     * {@link ThreadUtil}.getThreadId() ���ŁA {@link ThreadLocal} �̏������������s���Ă���A
     * ����������������Ȃ����Ƃ����邽�߁B
     */
    private long threadId_ = THREAD_ID_NOT_SET;

    /**
     * ����ȃC���X�^���X�����ւ��邽�߂̃R���X�g���N�^�ł��B
     */
    protected CallTreeRecorder()
    {
        // �C���X�^���X�����ւ���B
    }

    /**
     * �X���b�h���Ƃ�CallTreeReocrder�̃C���X�^���X���擾����B
     * @return CallTreeRecorder�@
     */
    public static CallTreeRecorder getInstance()
    {
        return recorder__.get();
    }


    /**
     * �ۑ����Ă���CallTreeNode���N���A���܂��B<br />
     */
    public static void clearNode()
    {
        List<Long> deleteList = new ArrayList<Long>();
        synchronized (currentNodeMap__)
        {
            for (Map.Entry<Long, WeakReference<CallTreeNode>> entry : currentNodeMap__.entrySet())
            {
                if (entry.getValue() == null)
                {
                    deleteList.add(entry.getKey());
                }
            }
        }

        for (int index = deleteList.size() - 1; index >= 0; index--)
        {
            deleteList.remove(index);
        }
    }

    /**
     * �����Ŏw�肳�ꂽ�X���b�hID�ɑΉ�����CallTreeNode ���擾���܂��B<br />
     *
     * @param id �X���b�hID
     * @return {@link CallTreeNode}�I�u�W�F�N�g
     */
    public static CallTreeNode getNode(final Long id)
    {
        WeakReference<CallTreeNode> weakReference = currentNodeMap__.get(id);
        if (weakReference == null)
        {
            return null;
        }

        CallTreeNode callTreeNode = weakReference.get();
        return callTreeNode;
    }

    /**
     * CallTree���擾���܂��B
     *
     * @return CallTree�B
     */
    public CallTree getCallTree()
    {
        if (this.callTree_ == null)
        {
            this.callTree_ = new CallTree();
        }
        return this.callTree_;
    }

    /**
     * CallTreeNode���擾���܂��B
     *
     * @return CallTree�B
     */
    public CallTreeNode getCallTreeNode()
    {
        return this.callerNode_;
    }

    /**
     * CallTree���N���A���A�V�����C���X�^���X��ݒ肵�܂��B
     */
    public void clearCallTree()
    {
        CallTree callTree = getCallTree();
        callTree.init();
        this.depth_ = 0;
        clearCallerNode();
    }

    /**
     * CallTreeNode���N���A���܂��B
     */
    public void clearCallerNode()
    {
        this.callerNode_ = null;
        long threadId = this.getThreadId();
        currentNodeMap__.remove(threadId);
    }

    /**
     * CallTreeNode��ݒ肵�܂��B
     *
     * @param node CallTreeNode�B
     */
    public void setCallerNode(final CallTreeNode node)
    {
        this.callerNode_ = node;

        long threadId = this.getThreadId();
        if (node == null)
        {
            currentNodeMap__.remove(threadId);
        }
        else
        {
            // Map�Ɋi�[����B
            currentNodeMap__.put(threadId, new WeakReference<CallTreeNode>(node));
        }
    }

    /**
     * ���\�b�h�Ăяo���c���[������������B
     *
     * @param callTree ���\�b�h�Ăяo���c���[�i <code>null</code> �͔񋖗e�j
     */
    public void setCallTree(final CallTree callTree)
    {
        this.callTree_ = callTree;
    }

    /**
     * ���݂̌Ăяo����CallTreeNode�̎q�m�[�h���폜����B
     */
    public void clearChildren()
    {
        CallTreeNode node = callerNode_;
        if (node != null)
        {
            node.clearChildren();
        }
    }

    /**
     * CallTree�̃T�C�Y���ő�l�ɒB���Ă��邩�ǂ����𔻒肵�܂��B<br />
     *
     * @param callTree �R�[���c���[
     * @param config Javelin.propeties�̐ݒ�l
     * @return CallTree�̃T�C�Y��Javelin.properties�ɐݒ肵�Ă����l�𒴂��Ă���Ȃ�A<code>true</code>
     */
    public static boolean isCallTreeFull(CallTree callTree, JavelinConfig config)
    {
        return callTree.getNodeCount() >= config.getCallTreeMax();
    }

    /**
     * CallTreeNode��ǉ����܂��B
     *
     * @param parent �e�m�[�h�B
     * @param tree �R�[���c���[�B
     * @param node �q�m�[�h�B
     * @param config �ݒ�B
     */
    public static void addCallTreeNode(CallTreeNode parent, 
            final CallTree tree, final CallTreeNode node, final JavelinConfig config)
    {
        if (parent != null)
        {
            parent.addChild(node);
            node.getInvocation().addCaller(parent.getInvocation());
        }
    }

    /**
     * CallTreeNode�𐶐����܂��B
     *
     * @param invocation Invocation�B
     * @param args �����B
     * @param stacktrace �X�^�b�N�g���[�X�B
     * @param config �ݒ�B
     * @return CallTreeNode�B
     */
    public static CallTreeNode createNode(
            Invocation invocation,
            final Object[] args,
            final StackTraceElement[] stacktrace,
            final JavelinConfig config)
    {
        CallTreeNode node = new CallTreeNode();

        node.setStacktrace(stacktrace);

        // �p�����[�^�ݒ肪�s���Ă���Ƃ��A�m�[�h�Ƀp�����[�^��ݒ肷��
        if (args != null)
        {
            CallTreeRecorder.addLogArgs(node, args, config);
        }

        node.setInvocation(invocation);

        return node;
    }

    /**
     * ���O�Ɉ�����ǉ����܂��B
     * @param node �m�[�h
     * @param args ����
     * @param config �R���t�B�O
     */
    public static void addLogArgs(
            final CallTreeNode node,
            final Object[] args,
            final JavelinConfig config)
    {
        String[] argStrings = new String[args.length];
        for (int index = 0; index < args.length; index++)
        {
            if (config.isArgsDetail())
            {
                int argsDetailDepth = config.getArgsDetailDepth();
                argStrings[index] =
                    StatsUtil.buildDetailString(args[index], argsDetailDepth);
            }
            else
            {
                argStrings[index] =
                    StatsUtil.toStr(args[index], config.getStringLimitLength());
            }
        }

        node.setArgs(argStrings);
    }

    /**
     * ���ɏI������CallTreeNode���擾����B
     *
     * @return ���ɏI������CallTreeNode�̃��X�g�B
     */
    public List<CallTreeNode> removeFinishedNode()
    {
        List<CallTreeNode> removedList = new ArrayList<CallTreeNode>();

        CallTree tree = getCallTree();
        CallTreeNode rootNode = tree.getRootNode();
        removeFinishedNodeInternal(removedList, rootNode);

        return removedList;
    }

    /**
     * �w�肵��CallTreeNode�̎q���I�����Ă���΍폜���A�폜����CallTreeNode�̓��X�g�Ɋi�[����B
     *
     * @param list �폜����CallTreeNode�̃��X�g�B
     * @param node ����Ώۂ�CallTreeNode�B
     */
    private static void removeFinishedNodeInternal(List<CallTreeNode> list, CallTreeNode node)
    {
        List<CallTreeNode> children = node.getChildren();
        for (int index = 0; index < children.size(); index++)
        {
            CallTreeNode child = children.get(index);
            if (child.getEndTime() != -1)
            {
                list.add(child);
                node.removeChild(child);
                index--;
            }
            else
            {
                removeFinishedNodeInternal(list, child);
            }
        }
    }

    /**
     * �[�����擾����B
     *
     * @return �[��
     */
    public int getDepth()
    {
        return this.depth_;
    }

    /**
     * �[����ݒ肷��B
     *
     * @param depth �[��
     */
    public void setDepth(int depth)
    {
        this.depth_ = depth;
    }

    /**
     * ���R�[�h���\�b�h���Ăяo���ꂽ�����擾����B
     * @return ���R�[�h���\�b�h���Ăяo���ꂽ��
     */
    public boolean isRecordMethodCalled()
    {
        return this.isRecordMethodCalled_;
    }

    /**
     * ���R�[�h���\�b�h���Ăяo���ꂽ�����擾����B
     * @param isRecordMethodCalled ���R�[�h���\�b�h���Ăяo���ꂽ��
     */
    public void setRecordMethodCalled(boolean isRecordMethodCalled)
    {
        this.isRecordMethodCalled_ = isRecordMethodCalled;
    }

    /**
     * ��O���������������擾����B
     *
     * @return ��O������������
     */
    public boolean isExceptionOccured()
    {
        return this.isExceptionOccured_;
    }

    /**
     * ��O���������������擾����B
     *
     * @param isExceptionOccured ��O������������
     */
    public void setExceptionOccured(boolean isExceptionOccured)
    {
        this.isExceptionOccured_ = isExceptionOccured;
    }

    /**
     * �X���b�hID���擾����B
     *
     * @return �X���b�hID
     */
    public long getThreadId()
    {
        if (this.threadId_ == THREAD_ID_NOT_SET)
        {
            this.threadId_ = ThreadUtil.getThreadId();
        }
        return this.threadId_;
    }

    /**
     * �q�m�[�h���폜����B
     *
     * @param parent �e�m�[�h
     * @param node �q�m�[�h
     */
    public void removeChildNode(CallTreeNode parent, CallTreeNode node)
    {
        parent.removeChild(node);
        setDepth(getDepth() - 1);
    }

}
