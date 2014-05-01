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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.converter.hadoop.HadoopInfo;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;

/**
 * ���\�b�h�Ăяo�����
 * @author eriguchi
 *
 */
public class CallTreeNode
{
    /**
     * �R���X�g���N�^
     */
    public CallTreeNode()
    {
        // Do Nothing.
    }

    /**  */
    private Invocation          invocation_;

    /** �߂�l */
    private String              returnValue_;

    /** ��O */
    private Throwable           throwable_;

    /** ��O�������� */
    private long                throwTime_;

    /** �J�n���� */
    private long                startTime_;

    /** �I������ */
    private long                endTime_     = -1;

    /** �ݐώ��� */
    private long                accumulatedTime_;

    /** CPU���� */
    private long                cpuTime_;

    /** ���[�U���� */
    private long                userTime_;

    /** �J�n����VM�̃X�e�[�^�X */
    private VMStatus            startVmStatus_;

    /** �I������VM�̃X�e�[�^�X */
    private VMStatus            endVmStatus_;

    /** ���� */
    private String[]            args_;

    /** �X�^�b�N�g���[�X */
    private StackTraceElement[] stacktrace_;

    /** �������������Ă���CallTree */
    private CallTree            tree_;

    /** CallTreeNode�̐e�m�[�h */
    private CallTreeNode        parent_;

    /** CallTreeNode�̎q�m�[�h */
    private List<CallTreeNode>  children_    = new ArrayList<CallTreeNode>();

    /** �t�B�[���h�A�N�Z�X */
    private boolean             isFieldAccess_;

    /** �[�� */
    private int                 depth_;

    /** �q�m�[�h�ŏ���ꂽ�������� */
    private long                childrenTime_;

    /** �q�m�[�h�ŏ���ꂽCPU���� */
    private long                childrenCpuTime_;

    /** �q�m�[�h�ŏ���ꂽ���[�U���� */
    private long                childrenUserTime_;

    /** �P�̃m�[�h�ɋL�^����C�x���g�̍ő吔 */
    private static final int    MAX_EVENT    = 100;

    /** Invocation�ɋL�^�������ǂ����������t���O�B */
    private boolean             isRecoreded_ = false;

    /** �X�g�[�����\�b�h�Ƃ��Č��o���ꂽ���ǂ����������t���O */
    private boolean             isStalled_   = false;
    
    /** Hadoop��TaskTracker��� */
    private HadoopInfo hadoopInfo_ = null;

    /**
     * �[�����擾����B
     * @return �[��
     */
    public int getDepth()
    {
        return depth_;
    }

    /**
     * �[����ݒ肷��B
     * @param depth �[��
     */
    public void setDepth(int depth)
    {
        depth_ = depth;
    }

    /**  */
    private final Map<String, Object> loggingValueMap_ = new TreeMap<String, Object>();

    /** CallTreeNode��root���ǂ���(true:root�Afalse:not root�B�f�t�H���g�l��false) */
    private boolean                   isRoot_          = false;

    /** ���������C�x���g�B */
    private final List<CommonEvent>   eventList_       = new CopyOnWriteArrayList<CommonEvent>();

    /** �d���Ăяo���폜�p�̃J�E���^(ServletMonitor���g�p) */
    public int                        count_;

    /**
     * Javelin�̌v���ɂ�����������(���݂̎����ł�JDBC Javelin�̎��s�v��擾�̂�)
     */
    private long                      javelinTime_;

    /**
     * InvoCation
     * invocation���擾����B
     * @return Invocation
     */
    public Invocation getInvocation()
    {
        return this.invocation_;
    }

    /**
     * Invocation��ݒ肷��B
     * @param invocation Invocation
     */
    public void setInvocation(final Invocation invocation)
    {
        this.invocation_ = invocation;
    }

    /**
     * �߂�l���擾����B
     * @return �߂�l
     */
    public String getReturnValue()
    {
        return this.returnValue_;
    }

    /**
     * �߂�l��ݒ肷��B
     * @param returnValue �߂�l
     */
    public void setReturnValue(final String returnValue)
    {
        this.returnValue_ = returnValue;
    }

    /**
     * ���\�b�h�J�n�������擾����B
     * @return ���\�b�h�J�n����
     */
    public long getStartTime()
    {
        return this.startTime_;
    }

    /**
     * ���\�b�h�J�n�������擾����B
     * @param startTime ���\�b�h�J�n����
     */
    public void setStartTime(final long startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * ���\�b�h�̏I���������擾����B
     * @return ���\�b�h�̏I������
     */
    public long getEndTime()
    {
        return this.endTime_;
    }

    /**
     * ���\�b�h�̏I��������ݒ肷��B
     * @param endTime ���\�b�h�̏I������
     */
    public void setEndTime(final long endTime)
    {
        this.endTime_ = endTime;
        this.accumulatedTime_ = this.endTime_ - this.startTime_ - this.javelinTime_;
        this.invocation_.setAccumulatedTime(this.accumulatedTime_);
    }

    /**
     * �ݐώ��Ԃ��擾����B
     * @return �ݐώ���
     */
    public long getAccumulatedTime()
    {
        return this.accumulatedTime_;
    }

    /**
     * CPU���Ԃ��擾���B��
     * @param cpuTime CPU����
     */
    public void setCpuTime(final long cpuTime)
    {
        this.cpuTime_ = cpuTime;
    }

    /**
     * CPU���Ԃ��擾����B
     * @return CPU����
     */
    public long getCpuTime()
    {
        return this.cpuTime_;
    }

    /**
     * StackTrace���擾����B
     * @return StackTrace
     */
    public StackTraceElement[] getStacktrace()
    {
        return this.stacktrace_;
    }

    /**
     * StackTrace��ݒ肷��B
     * @param stacktrace StackTrace
     */
    public void setStacktrace(final StackTraceElement[] stacktrace)
    {
        this.stacktrace_ = stacktrace;
    }

    /**
     * ��������������CallTree���擾����B
     *
     * @return CallTree
     */
    public CallTree getTree()
    {
        return tree_;
    }

    /**
     * ��������������CallTree��ݒ肷��B
     * @param tree CallTree
     */
    public void setTree(CallTree tree)
    {
        tree_ = tree;
    }

    /**
     * CallTreeNode�̐e���擾����B
     * @return CallTreeNode�̐e
     */
    public CallTreeNode getParent()
    {
        return this.parent_;
    }

    /**
     * CallTreeNode�̐e��ݒ肷��B
     * @param parent CallTreeNode�̐e
     */
    public void setParent(final CallTreeNode parent)
    {
        this.parent_ = parent;
    }

    /**
     * CallTreeNOde�̎q��ݒ肷��B
     * @param children CallTreeNode�̎q
     */
    public void setChildren(List<CallTreeNode> children)
    {
        this.children_ = children;
    }

    /**
     * CallTreeNOde�̎q���擾����B
     * @return CallTreeNode�̎q
     */
    public List<CallTreeNode> getChildren()
    {
        return this.children_;
    }

    /**
     * CallTreeNOde�̎q��ǉ�����B
     * @param node CallTreeNode�̎q
     */
    public void addChild(final CallTreeNode node)
    {
        this.children_.add(node);
        node.setTree(tree_);
        node.setParent(this);

        if (tree_ != null)
        {
            tree_.incrementNodeCount();
        }
    }

    /**
     * �S�Ă�CallTreeNode�̎q���폜����B
     */
    public void clearChildren()
    {
        int size = children_.size();
        children_.clear();
        if (tree_ != null)
        {
            tree_.decrementNodeCount(size);
        }
    }

    /**
     * CallTreeNode�̎q���폜����B
     * @param node CallTreeNode�̎q
     */
    public void removeChild(CallTreeNode node)
    {
        if (children_.remove(node) && tree_ != null)
        {
            tree_.decrementNodeCount(node.countChildren() + 1);
        }
    }

    /**
     * �z���̃m�[�h�̐��𐔂���B
     *
     * @return�@�z���̃m�[�h�̐�
     */
    private int countChildren()
    {
        int count = children_.size();
        for (int index = 0; index < children_.size(); index++)
        {
            count = count + children_.get(index).countChildren();
        }

        return count;
    }

    /**
     * �������擾����B
     * @return ����
     */
    public String[] getArgs()
    {
        return this.args_;
    }

    /**
     * ������ݒ肷��B
     * @param args ����
     */
    public void setArgs(final String[] args)
    {
        this.args_ = args;
    }

    /**
     * �m�[�h���t�B�[���h�ւ̃A�N�Z�X���ǂ����������t���O���擾����B
     *
     * @return �t�B�[���h�A�N�Z�X�Ȃ�true�A�����łȂ����false��Ԃ��B
     */
    public boolean isFieldAccess()
    {
        return this.isFieldAccess_;
    }

    /**
     * �m�[�h���t�B�[���h�ւ̃A�N�Z�X���ǂ����������t���O���擾����B
     *
     * @param isFieldAccess �t�B�[���h�A�N�Z�X�Ȃ�true�A�����łȂ����false�B
     */
    public void setFieldAccess(final boolean isFieldAccess)
    {
        this.isFieldAccess_ = isFieldAccess;
    }

    /**
     * ��O���擾����B
     * @return ��O
     */
    public Throwable getThrowable()
    {
        return this.throwable_;
    }

    /**
     * ��O��ݒ肷��B
     * @param throwable ��O
     */
    public void setThrowable(final Throwable throwable)
    {
        this.throwable_ = throwable;
    }

    /**
     * ��O�����������擾����B
     * @return ��O��������
     */
    public long getThrowTime()
    {
        return this.throwTime_;
    }

    /**
     * ��O����������ݒ肷��B
     * @param throwTime ��O���������B
     */
    public void setThrowTime(final long throwTime)
    {
        this.throwTime_ = throwTime;
    }

    /**
     * VM�̃X�e�[�^�X���擾����B
     * @return VM�̃X�e�[�^�X
     */
    public VMStatus getEndVmStatus()
    {
        return this.endVmStatus_;
    }

    /**
     * VM�̃X�e�[�^�X��ݒ肷��B
     * @return VM�̃X�e�[�^�X
     */
    public VMStatus getStartVmStatus()
    {
        return this.startVmStatus_;
    }

    /**
     * �I������VM�̃X�e�[�^�X�ݒ肷��B
     * @param endVmStatus �I������VM�̃X�e�[�^�X
     */
    public void setEndVmStatus(final VMStatus endVmStatus)
    {
        this.endVmStatus_ = endVmStatus;
    }

    /**
     * �J�n����VM�̃X�e�[�^�X�ݒ肷��B
     * @param startVmStatus �J�n����VM�̃X�e�[�^�X
     */
    public void setStartVmStatus(final VMStatus startVmStatus)
    {
        this.startVmStatus_ = startVmStatus;
    }

    /**
     * ���[�U���Ԃ��擾����B
     * @return ���[�U����
     */
    public long getUserTime()
    {
        return this.userTime_;
    }

    /**
     * ���[�U���Ԃ�ݒ肷��B
     * @param userTime ���[�U����
     */
    public void setUserTime(final long userTime)
    {
        this.userTime_ = userTime;
    }

    /**
     * ���O�l��ݒ肷��B
     *
     * @param key �L�[
     * @param value �l
     */
    public void setLoggingValue(final String key, final Object value)
    {
        // Call Tree��disable�̏ꍇ����RootNode�����݂���ꍇ�A
        // RootNode�ɕۑ�����B
        if (isRoot_ == false && !tree_.isCallTreeEnabled())
        {
            CallTreeNode rootNode = tree_.getRootNode();
            if (rootNode != null)
            {
                rootNode.setLoggingValue(key, value);
            }
        }
        synchronized (this.loggingValueMap_)
        {
            this.loggingValueMap_.put(key, value);
        }
    }

    /**
     * Map����L�[���擾����B
     *
     * @return �L�[�z��
     */
    public String[] getLoggingKeys()
    {
        synchronized (this.loggingValueMap_)
        {
            Set<String> keySet = this.loggingValueMap_.keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);

            return keys;
        }
    }

    /**
     * Map����L�[�ɑΉ�����l���擾����B
     *
     * @param key �L�[
     * @return �L�[�̒l
     */
    public Object getLoggingValue(final String key)
    {
        synchronized (this.loggingValueMap_)
        {
            return this.loggingValueMap_.get(key);
        }
    }

    /**
     * CallTreeNode��root���ǂ�����\���l��Ԃ��B
     * @return true:root�Afalse:not root
     */
    public boolean isRoot()
    {
        return this.isRoot_;
    }

    /**
     * CallTreeNode��root���ǂ�����\���l��ݒ肷��B
     * @param isRoot CallTreeNode��root���ǂ�����\���l
     */
    public void setRoot(final boolean isRoot)
    {
        this.isRoot_ = isRoot;
    }

    /**
     * ����CallTreeNode��root�ɂ�����CallTreeNode��Ԃ��B
     * @return root�ɂ�����CallTreeNode
     */
    public CallTreeNode getRootNode()
    {
        CallTreeNode tmp = this;
        CallTreeNode parent = tmp.getParent();
        while (parent != null)
        {
            tmp = parent;
            parent = tmp.getParent();
        }
        return tmp;
    }

    /**
     * �C�x���g��ǉ�����B
     *
     * @param event �C�x���g�B
     */
    public void addEvent(CommonEvent event)
    {
        // Call Tree��disable�̏ꍇ��Tree�ɕۑ�����B
        if (tree_ != null && !tree_.isCallTreeEnabled())
        {
            CallTreeNode node = new CallTreeNode();
            node.setInvocation(getInvocation());
            node.addEventForce(event);
            tree_.addEventNode(node);
        }
        else
        {
            addEventForce(event);
        }
    }

    /**
     * �C�x���g��ǉ�����B
     *
     * @param event �C�x���g�B
     */
    private void addEventForce(CommonEvent event)
    {
        if (this.eventList_.size() < MAX_EVENT)
        {
            this.eventList_.add(event);
        }
    }

    /**
     * �C�x���g���X�g���擾����B
     *
     * @return �C�x���g�B
     */
    public CommonEvent[] getEventList()
    {
        return this.eventList_.toArray(new CommonEvent[this.eventList_.size()]);
    }

    /**
     * �w�肵���C�x���g���폜����B
     *
     * @param event �C�x���g�B
     */
    public void removeEvent(CommonEvent event)
    {
        this.eventList_.remove(event);
    }

    /**
     * Javelin�̌v���ɂ�����������(���݂̎����ł�JDBC Javelin�̎��s�v��擾�̂�)��
     * �l��ǉ�����B
     *
     * @param javelinTime Javelin�̌v���ɂ�����������
     */
    public void addJavelinTime(long javelinTime)
    {
        if (javelinTime == 0)
        {
            return;
        }

        this.javelinTime_ += javelinTime;
        CallTreeNode parent = this.parent_;
        if (parent != null)
        {
            parent.addJavelinTime(javelinTime);
        }
    }

    /**
     * Javelin�̌v���ɂ�����������(���݂̎����ł�JDBC Javelin�̎��s�v��擾�̂�)
     * ���擾����B
     *
     * @return Javelin�̌v���ɂ�����������
     */
    public long getJavelinTime()
    {
        return this.javelinTime_;
    }

    /**
     * Javelin�̌v���ɂ�����������(���݂̎����ł�JDBC Javelin�̎��s�v��擾�̂�)
     * ��ݒ肷��B
     *
     * @param javelinTime Javelin�̌v���ɂ�����������
     */
    public void setJavelinTime(long javelinTime)
    {
        javelinTime_ = javelinTime;
    }

    /**
     * �q�m�[�h�ŏ���ꂽ�������Ԃ��擾���܂��B
     * @return �q�m�[�h�ŏ���ꂽ��������
     */
    public long getChildrenTime()
    {
        return childrenTime_;
    }

    /**
     * �q�m�[�h�ŏ���ꂽCPU���Ԃ��擾���܂��B
     * @return �q�m�[�h�ŏ���ꂽCPU����
     */
    public long getChildrenCpuTime()
    {
        return childrenCpuTime_;
    }

    /**
     * �q�m�[�h�ŏ���ꂽ���[�U���Ԃ��擾���܂��B
     * @return �q�m�[�h�ŏ���ꂽ���[�U����
     */
    public long getChildrenUserTime()
    {
        return childrenUserTime_;
    }

    /**
     * �q�m�[�h�ŏ���ꂽ�������Ԃ�ǉ����܂��B
     * @param childrenTime �q�m�[�h�ŏ���ꂽ��������
     */
    public void addChildrenTime(long childrenTime)
    {
        childrenTime_ += childrenTime;
    }

    /**
     * �q�m�[�h�ŏ���ꂽCPU���Ԃ�ǉ����܂��B
     * @param childrenCpuTime �q�m�[�h�ŏ���ꂽCPU����
     */
    public void addChildrenCpuTime(long childrenCpuTime)
    {
        childrenCpuTime_ += childrenCpuTime;
    }

    /**
     * �q�m�[�h�ŏ���ꂽ���[�U���Ԃ�ǉ����܂��B
     * @param childrenUserTime �q�m�[�h�ŏ���ꂽ���[�U����
     */
    public void addChildrenUserTime(long childrenUserTime)
    {
        childrenUserTime_ += childrenUserTime;
    }

    /**
     * Invocation�ɋL�^�������ǂ����̃t���O���擾���܂��B
     * @return Invocation�ɋL�^�����Ƃ�true/�����łȂ��Ƃ�false
     */
    public boolean isRecoreded()
    {
        return isRecoreded_;
    }

    /**
     * Invocation�ɋL�^�������ǂ����̃t���O��ݒ肵�܂��B
     * @param isRecoreded Invocation�ɋL�^�������ǂ����̃t���O
     */
    public void setRecoreded(boolean isRecoreded)
    {
        isRecoreded_ = isRecoreded;
    }

    /**
     * �X�g�[�����\�b�h�Ƃ��Č��o���ꂽ���ǂ����������t���O���擾���܂��B
     * @return �X�g�[�����\�b�h�Ƃ��Č��o���ꂽ�Ƃ�true/�����łȂ��Ƃ�false
     */
    public boolean isStalled()
    {
        return isStalled_;
    }

    /**
     * �X�g�[�����\�b�h�Ƃ��Č��o���ꂽ���ǂ����������t���O��ݒ肵�܂��B
     * @param isStalled �X�g�[�����\�b�h�Ƃ��Č��o���ꂽ���ǂ����������t���O
     */
    public void setStalled(boolean isStalled)
    {
        isStalled_ = isStalled;
    }
    
    /**
     * TaskTracker�̃X�e�[�^�X���������Ă��邩��Ԃ��B
     *
     * @return {@code true}�F�����Ă���^{@code false}�F�����Ă��Ȃ�
     */
    public boolean hasHadoopInfo()
    {
        return hadoopInfo_ != null;
    }

    /**
     * TaskTracker�̃X�e�[�^�X�����擾����B
     *
     * @return TaskTracker�̃X�e�[�^�X���
     */
    public HadoopInfo getHadoopInfo()
    {
        return this.hadoopInfo_;
    }

    /**
     * TaskTracker�̃X�e�[�^�X����ݒ肷��B
     *
     * @param hadoopInfo TaskTracker�̃X�e�[�^�X���
     */
    public void setHadoopInfo(HadoopInfo hadoopInfo)
    {
        this.hadoopInfo_ = hadoopInfo;
    }
}
