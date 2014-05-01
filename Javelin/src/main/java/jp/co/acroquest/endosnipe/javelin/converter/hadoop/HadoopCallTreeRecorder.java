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
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;

/**
 * Hadoop�p�̃R�[���c���[���L�^����B
 *
 * @author matsuoka
 */
public class HadoopCallTreeRecorder
{
    /** {@link HadoopCallTreeRecorder}�̗B��̃C���X�^���X */
    private static HadoopCallTreeRecorder instance__ = new HadoopCallTreeRecorder();

    /** �W���uID���L�[�Ƃ���{@link HadoopCallTree}�̃��X�g */
    private Map<String, HadoopCallTree> hadoopCallTreeMap_ = new HashMap<String, HadoopCallTree>();

    /** �W���uID���L�[�Ƃ����X���b�h����{@link CallTree}�̈ꗗ */
    private HashMap<String, HashMap<String, CallTree>> callTreeMap_ = new HashMap<String, HashMap<String, CallTree>>(1);

    /** �W���uID���L�[�Ƃ����X���b�h����{@link CallTreeNode}�̈ꗗ */
    private HashMap<String, HashMap<String, CallTreeNode>> callTreeNodeMap_ = new HashMap<String, HashMap<String, CallTreeNode>>(1);

    /** �X���b�h����{@link Invocation} */
    private HashMap<String, Invocation> invocationMap_ = new HashMap<String, Invocation>(1);

    /** �X���b�h���̃z�X�g�� */
    private HashMap<String, String> hostNameMap_ = new HashMap<String, String>(1);

    /** �X���b�h���̃��\�b�h�J�n����*/
    private HashMap<String, Long> startTimeMap_ = new HashMap<String, Long>(1);

    /**
     * �R���X�g���N�^���B������B
     */
    private HadoopCallTreeRecorder()
    {
        // Do Nothing.
    }

    /**
     * {@link HadoopCallTreeRecorder}�̃C���X�^���X���擾����B
     * @return {@link HadoopCallTreeRecorder}�̃C���X�^���X��Ԃ��B
     */
    public static HadoopCallTreeRecorder getInstance()
    {
        return instance__;
    }

    /**
     * �R�[���c���[��ǉ�����B
     * @param jobId Hadoop�̃W���uID
     * @param tree �ǉ�����R�[���c���[
     */
    public void addCallTree(String jobId, CallTree tree)
    {
        synchronized(hadoopCallTreeMap_)
        {
            HadoopCallTree hadoopCallTree = hadoopCallTreeMap_.get(jobId);
            if (hadoopCallTree == null)
            {
                hadoopCallTree = new HadoopCallTree();
                hadoopCallTreeMap_.put(jobId, hadoopCallTree);
            }

            hadoopCallTree.addChild(tree.copy());
        }
    }

    /**
     * Hadoop�̃W���uID�ɑΉ�����R�[���c���[���擾����B
     * @param jobId Hadoop�̃W���uID
     * @return �w�肳�ꂽ�W���uID�̃R�[���c���[
     */
    public HadoopCallTree getCallTree(String jobId)
    {
        return hadoopCallTreeMap_.get(jobId);
    }

    /**
     * Hadoop�̃W���uID�ɑΉ�����R�[���c���[���폜����B
     * @param jobId Hadoop�̃W���uID
     */
    public void removeCallTree(String jobId)
    {
        synchronized(hadoopCallTreeMap_)
        {
            hadoopCallTreeMap_.remove(jobId);
        }
    }

    /**
     * JobID�ꗗ��Ԃ�
     *
     * @return JobID�ꗗ
     */
    public ArrayList<String> getJobIds()
    {
        ArrayList<String> jobs = new ArrayList<String>();
        for(String jobID : hadoopCallTreeMap_.keySet())
        {
            jobs.add(jobID);
        }
        return jobs;
    }

    /**
     * CallTree�ƃW���uID��R�t�Ċi�[����B
     * 
     * @param jobID �W���uID
     * @param tree {@link CallTree}
     */
    public void putCallTree(String jobID, CallTree tree)
    {
        String threadID = String.valueOf(Thread.currentThread().getId());
        synchronized(callTreeMap_)
        {
            if(!callTreeMap_.containsKey(threadID))
            {
                callTreeMap_.put(threadID, new HashMap<String, CallTree>(1));
            }
            callTreeMap_.get(threadID).put(jobID, tree);
        }
        return;
    }

    /**
     * �w�肳�ꂽ�W���uID�ɕR�t����CallTree���擾����B<br />
     * ���o����CallTree��CallTreeRecorder����폜�����B
     * 
     * @param jobID �W���uID
     * 
     * @return {@link CallTree}
     */
    public CallTree takeCallTree(String jobID)
    {
        CallTree tree = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(callTreeMap_)
        {
            if(!callTreeMap_.containsKey(threadID))
                return tree;

            tree = callTreeMap_.get(threadID).get(jobID);
            callTreeMap_.get(threadID).remove(jobID);
        }

        return tree;
    }

    /**
     * �W���uID���L�[�Ƃ���CallTree�̈ꗗ���擾����B<br />
     * ���o����CallTree�̈ꗗ��CallTreeRecorder����폜�����B
     * 
     * @return �W���uID���L�[�Ƃ���{@link CallTree}�̈ꗗ
     */
    public HashMap<String, CallTree> takeAllCallTree()
    {
        HashMap<String, CallTree> callTreeMap = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(callTreeMap_)
        {
            if(!callTreeMap_.containsKey(threadID))
                return callTreeMap;

            callTreeMap = callTreeMap_.get(threadID);
            callTreeMap_.remove(threadID);
        }

        return callTreeMap;
    }

    /**
     * CallTreeNode�ƃW���uID��R�t�Ċi�[����B
     * 
     * @param jobID �W���uID
     * @param node {@link CallTreeNode}
     */
    public void putCallTreeNode(String jobID, CallTreeNode node)
    {
        String threadID = String.valueOf(Thread.currentThread().getId());
        synchronized(callTreeNodeMap_)
        {
            if(!callTreeNodeMap_.containsKey(threadID))
            {
                callTreeNodeMap_.put(threadID, new HashMap<String, CallTreeNode>(1));
            }
            callTreeNodeMap_.get(threadID).put(jobID, node);
        }
        return;
    }

    /**
     * �w�肳�ꂽ�W���uID�ɕR�t����CallTreeNode���擾����B<br />
     * ���o����CallTreeNode��CallTreeRecorder����폜�����B
     * 
     * @param jobID �W���uID
     * 
     * @return {@link CallTreeNode}
     */
    public CallTreeNode takeCallTreeNode(String jobID)
    {
        CallTreeNode node = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(callTreeNodeMap_)
        {
            if(!callTreeNodeMap_.containsKey(threadID))
                return node;

            node = callTreeNodeMap_.get(threadID).get(jobID);
            callTreeNodeMap_.get(threadID).remove(jobID);
        }

        return node;
    }

    /**
     * �W���uID���L�[�Ƃ���CallTreeNode�̈ꗗ���擾����B<br />
     * ���o����CallTreeNode�̈ꗗ��CallTreeRecorder����폜�����B
     * 
     * @return �W���uID���L�[�Ƃ���{@link CallTreeNode}�̈ꗗ
     */
    public HashMap<String, CallTreeNode> takeAllCallTreeNode()
    {
        HashMap<String, CallTreeNode> callTreeNodeMap = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(callTreeNodeMap_)
        {
            if(!callTreeNodeMap_.containsKey(threadID))
                return callTreeNodeMap;

            callTreeNodeMap = callTreeNodeMap_.get(threadID);
            callTreeNodeMap_.remove(threadID);
        }

        return callTreeNodeMap;
    }

    /**
     * Invocation���擾����B<br />
     * ���o����Invocation��CallTreeRecorder����폜�����B
     * 
     * @return {@link Invocation}
     */
    public Invocation getInvocation()
    {
        Invocation ret = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(invocationMap_)
        {
            if(!invocationMap_.containsKey(threadID))
                return ret;

            ret = invocationMap_.get(threadID);
            invocationMap_.remove(threadID);
        }

        return ret;
    }

    /**
     * Invocation��ݒ肷��B
     * 
     * @param invocation {@link Invocation} 
     */
    public void setInvocation(Invocation invocation)
    {
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(invocationMap_)
        {
            if(!invocationMap_.containsKey(threadID))
                invocationMap_.put(threadID, invocation);
        }
    }

    /**
     * �z�X�g�����擾����B<br />
     * ���o�����z�X�g����CallTreeRecorder����폜�����B
     * 
     * @return �z�X�g��
     */
    public String takeHostname()
    {
        String ret = null;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(hostNameMap_)
        {
            if(!hostNameMap_.containsKey(threadID))
                return ret;

            ret = hostNameMap_.get(threadID);
            hostNameMap_.remove(threadID);
        }

        return ret;
    }

    /**
     * �z�X�g����ݒ肷��B
     * 
     * @param hostName �z�X�g�� 
     */
    public void putHostName(String hostName)
    {
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(hostNameMap_)
        {
            if(!hostNameMap_.containsKey(threadID))
                hostNameMap_.put(threadID, hostName);
        }
    }

    /**
     * ���\�b�h�J�n�������擾����B<br />
     * ���o�������\�b�h�J�n������CallTreeRecorder����폜�����B
     * 
     * @return ���\�b�h�J�n����
     */
    public long takeStartTime()
    {
        long ret = 0L;
        String threadID = String.valueOf(Thread.currentThread().getId());

        synchronized(startTimeMap_)
        {
            if(!startTimeMap_.containsKey(threadID))
                return ret;

            ret = startTimeMap_.get(threadID).longValue();
            startTimeMap_.remove(threadID);
        }

        return ret;
    }

    /**
     * ���\�b�h�J�n������ݒ肷��B
     * 
     * @param startTime ���\�b�h�J�n���� 
     */
    public void putStartTime(long startTime)
    {
        String threadID = String.valueOf(Thread.currentThread().getId());
        Long start = Long.valueOf(startTime);

        synchronized(startTimeMap_)
        {
            if(!startTimeMap_.containsKey(threadID))
                startTimeMap_.put(threadID, start);
        }
    }
}
