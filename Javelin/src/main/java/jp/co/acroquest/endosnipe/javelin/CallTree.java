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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;

/**
 * Javelin���O�o�͗p�ɃR�[���X�^�b�N���L�^���邽�߂́A�c���[�N���X�B
 * 
 * @author yamasaki
 */
public class CallTree
{
    /** �f�t�H���g�̃R�[���o�b�N���X�g�̃T�C�Y */
    private static final int DEFAULT_CALLBACK_LIST_SIZE = 5;
    
    /** CallTree�m�[�h */
    private CallTreeNode rootNode_;

    /** ThreadID */
    private String threadID_;

    /** ���\�b�h�Ăяo���̃��[�g�m�[�h�ɂ��閼�O�B */
    private String rootCallerName_ = "unknown";

    /** ���\�b�h�Ăяo���̃G���h�m�[�h�̖��O������ł��Ȃ��ꍇ�ɂ��閼�O�B */
    private String endCalleeName_ = "unknown";

    /** ��O�̌��� */
    private Throwable cause_;

    /** CallBack�̃��X�g */
    private final List<Callback> callbackList_;

    /** �t���O�l��ۑ�����Map */
    private final Map<String, Object> flagMap_;

    /** ���O�l��ۑ�����Map */
    private final Map<String, Object> loggingValueMap_;

    /** �C�x���g�̃��X�g */
    private final List<CallTreeNode> eventList_;

    /** �L���Ȑ[���̃Z�b�g�B */
    private Set<Integer> depthSet_ = new HashSet<Integer>();

    /** �c���[�ɏ�������CallTreeNode�̐� */
    private int nodeCount_ = 0;

    /** �c���[�̃g�[�^���̃m�[�h�� */
    private int totalNodeCount_ = 0;

    /**
     * �g�����U�N�V�������̕ύX��h�~���邽�߂̃R�s�[�����{�������ǂ���
     */
    private boolean                           isConfigCopied_                    = false;
    
    /**
     * javelin.leak.collection.monitor�̃R�s�[�t�B�[���h
     * (�g�����U�N�V�������̕ύX��h�~����B)
     */
    private boolean isCollectionMonitorEnabled_ = CONFIG.isCollectionMonitor();
    
    /** javelin.concurrent.monitor�̃R�s�[�t�B�[���h */
    private boolean isConcurrentMonitorEnabled_ = CONFIG.isConcurrentAccessMonitored();
    
    /** javelin.call.tree.enabled�̃R�s�[�t�B�[���h */
    private boolean isCallTreeEnabled_ = false;
    
    /** javelin.jdbc.enable�̃R�s�[�t�B�[���h */
    private boolean isJdbcEnabled_ = false;
    
    /** javelin.jdbc.recordDuplJdbcCall�̃R�s�[�t�B�[���h */
    private boolean isRecordDuplJdbcCallEnabled_ = false;
    
    /** �c���[�ɋL�^����C�x���g�̍ő吔 */
    private static final int MAX_EVENT = 100;
    
    /** �m�[�h���̏W�v���@�𔻒肷�邽�߂̐ݒ�Q�Ɨp�B  */
    private static final JavelinConfig CONFIG = new JavelinConfig();

    /** �m�[�h���̏W�v���@�𔻒肷�邽�߂̐ݒ�Q�Ɨp�B  */
    private static final JdbcJavelinConfig JDBC_CONFIG = new JdbcJavelinConfig();    
    /**
     * StatsJavelinRecorder��臒l������s���ۂɁACallTreeNode�ŗL�̔�����s���N���X�B
     * (key, RecordStrategy)�̃}�b�v�Ƃ��ĕ��������Ƃ��ł���B
     * ����RecordStrategy�̔��菈����StatsJavelinRecorder���̂ɐݒ肵��
     * RecordStrategy���D�悵�Ď��s�����B
     */
    private final Map<String, RecordStrategy> highStrategyMap_;

    /**
     * StatsJavelinRecorder��臒l������s���ۂɁACallTreeNode�ŗL�̔�����s���N���X�B
     * (key, RecordStrategy)�̃}�b�v�Ƃ��ĕ��������Ƃ��ł���B
     * ����RecordStrategy�̔��菈����StatsJavelinRecorder���̂ɐݒ肵��
     * RecordStrategy�Ɠ��ꃌ�x���̗D��x�Ŏ��s�����B
     */
    private final Map<String, RecordStrategy> normalStrategyMap_;

    /**
     * �R���X�g���N�^�B �X���b�hID��ݒ肵�܂��B<br />
     */
    public CallTree()
    {
        this.threadID_ = StatsUtil.createThreadIDText();
        this.callbackList_ = new ArrayList<Callback>(DEFAULT_CALLBACK_LIST_SIZE);
        this.flagMap_ = new HashMap<String, Object>();
        this.loggingValueMap_ = new TreeMap<String, Object>();
        this.eventList_ = new ArrayList<CallTreeNode>();
        this.highStrategyMap_ = new LinkedHashMap<String, RecordStrategy>();
        this.normalStrategyMap_ = new LinkedHashMap<String, RecordStrategy>();
    }

    /**
     * �R�s�[�R���X�g���N�^�B <br />
     * loggingValueMap �̂݃f�B�[�v�R�s�[�A���̑��̃t�B�[���h�̓V�����[�R�s�[���܂��B<br />
     *
     * loggingValueMap �� Javelin �K�p�A�v���P�[�V�����̃X���b�h�Ƃ͕ʂ̃X���b�h�Ŏg�p����邽�߁A
     * �f�B�[�v�R�s�[���K�v�ł��B
     *
     * @param callTree �R�s�[�� CallTree
     */
    public CallTree(final CallTree callTree)
    {
        this.rootNode_ = callTree.rootNode_;
        this.threadID_ = callTree.threadID_;
        this.rootCallerName_ = callTree.rootCallerName_;
        this.endCalleeName_ = callTree.endCalleeName_;
        this.cause_ = callTree.cause_;
        this.callbackList_ = callTree.callbackList_;
        this.flagMap_ = callTree.flagMap_;
        this.loggingValueMap_ = new TreeMap<String, Object>(callTree.loggingValueMap_);
        this.eventList_ = callTree.eventList_;
        this.depthSet_ = callTree.depthSet_;
        this.nodeCount_ = callTree.nodeCount_;
        this.totalNodeCount_ = callTree.totalNodeCount_;
        this.isConfigCopied_ = callTree.isConfigCopied_;
        this.isCollectionMonitorEnabled_ = callTree.isCollectionMonitorEnabled_;
        this.isConcurrentMonitorEnabled_ = callTree.isConcurrentMonitorEnabled_;
        this.isCallTreeEnabled_ = callTree.isCallTreeEnabled_;
        this.isJdbcEnabled_ = callTree.isJdbcEnabled_;
        this.isRecordDuplJdbcCallEnabled_ = callTree.isRecordDuplJdbcCallEnabled_;
        this.highStrategyMap_ = callTree.highStrategyMap_;
        this.normalStrategyMap_ = callTree.normalStrategyMap_;
    }

    /**
     * ���������܂��B
     */
    public void init()
    {
        this.rootNode_ = null;
        this.cause_ = null;
        this.callbackList_.clear();
        this.flagMap_.clear();
        this.loggingValueMap_.clear();
        this.eventList_.clear();
        this.depthSet_.clear();
        this.nodeCount_ = 0;
        this.totalNodeCount_ = 0;
        this.highStrategyMap_ .clear();
        this.normalStrategyMap_.clear();
        
        this.isConfigCopied_ = false;
        this.isCollectionMonitorEnabled_  = CONFIG.isCollectionMonitor();
        this.isConcurrentMonitorEnabled_  = CONFIG.isConcurrentAccessMonitored();
     }
    
    /**
     * ���[�g�m�[�h���擾���܂��B<br />
     * 
     * @return ���[�g�m�[�h
     */
    public CallTreeNode getRootNode()
    {
        return this.rootNode_;
    }

    /**
     * ���[�g�m�[�h��ݒ肵�܂��B<br />
     * 
     * @param rootNode ���[�g�m�[�h
     */
    public void setRootNode(final CallTreeNode rootNode)
    {
        if (rootNode == null)
        {
            return;
        }

        this.rootNode_ = rootNode;
        this.rootNode_.setRoot(true);
        
        loadConfig();
        
        RootInvocationManager.addRootInvocation(this.rootNode_.getInvocation());
    }

    /**
     * �ݒ��CallTree�ɔ��f����B
     */
    public void loadConfig()
    {
        if (this.isConfigCopied_)
        {
            return;
        }
        this.isConfigCopied_ = true;
        this.isCollectionMonitorEnabled_  = CONFIG.isCollectionMonitor();
        this.isConcurrentMonitorEnabled_  = CONFIG.isConcurrentAccessMonitored();
        this.isCallTreeEnabled_           = CONFIG.isCallTreeEnabled();
        this.isJdbcEnabled_               = JDBC_CONFIG.isJdbcJavelinEnabled();
        this.isRecordDuplJdbcCallEnabled_ = JDBC_CONFIG.isRecordDuplJdbcCall();
    }

    /**
     * ThreadID���擾���܂��B<br />
     * 
     * @return ThreadID
     */
    public String getThreadID()
    {
        return this.threadID_;
    }

    /**
     * ThreadID��ݒ肵�܂��B<br />
     * 
     * @param threadID �X���b�hID
     */
    public void setThreadID(final String threadID)
    {
        this.threadID_ = threadID;
    }

    /**
     * �G���h�m�[�h���擾���܂��B<br />
     * 
     * @return �G���h�m�[�h
     */
    public String getEndCalleeName()
    {
        return this.endCalleeName_;
    }

    /**
     * �G���h�m�[�h��ݒ肵�܂��B<br />
     * 
     * @param endCalleeName �G���h�m�[�h
     */
    public void setEndCalleeName(final String endCalleeName)
    {
        if (endCalleeName == null)
        {
            return;
        }
        this.endCalleeName_ = endCalleeName;
    }

    /**
     * �Ăяo�����̃��[�g�m�[�h�����擾���܂��B<br />
     * 
     * @return �Ăяo�����̃��[�g�m�[�h��
     */
    public String getRootCallerName()
    {
        return this.rootCallerName_;
    }

    /**
     * �Ăяo�����̃��[�g�m�[�h����ݒ肵�܂��B<br />
     * 
     * @param rootCallerName �Ăяo�����̃��[�g�m�[�h���B
     */
    public void setRootCallerName(final String rootCallerName)
    {
        if (rootCallerName == null)
        {
            return;
        }
        this.rootCallerName_ = rootCallerName;
    }

    /**
     * CallBack��ǉ����܂��B<br />
     * 
     * @param callback CallBack
     */
    public void addCallback(final Callback callback)
    {
        this.callbackList_.add(callback);
    }

    /**
     * CallBack�����s���܂��B<br />
     */
    public void executeCallback()
    {
        int size = this.callbackList_.size();
        for (int index = 0; index < size; index++)
        {
            Callback callback = this.callbackList_.get(index);
            try
            {
                callback.execute();
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        if (size != 0)
        {
            this.callbackList_.clear();
        }
    }

    /**
     * �t���O��ݒ肵�܂��B<br />
     * 
     * @param flag �t���O
     * @param value �l
     * @return �t���O
     */
    public boolean setFlag(final String flag, final Object value)
    {
        if (SystemLogger.getInstance().isDebugEnabled())
        {
            SystemLogger.getInstance().debug(flag + " flag become valid.");
        }
        return (this.flagMap_.put(flag, value) != null);
    }

    /**
     * �t���O���擾���܂��B<br />
     * 
     * @param flag �t���O
     * @return �t���O
     */
    public Object getFlag(final String flag)
    {
        return this.flagMap_.get(flag);
    }

    /**
     * flag��Map�ɓo�^����Ă��邩�Ԃ��܂��B<br />
     * 
     * @param flag �t���O
     * @return true:�L�[��Map�ɓo�^����Ă���Afalse:�L�[��Map�ɓo�^����Ă��Ȃ��B
     */
    public boolean containsFlag(final String flag)
    {
        return this.flagMap_.containsKey(flag);
    }

    /**
     * �t���O�̒l��Map���珜�O���܂��B<br />
     * 
     * @param flag �t���O
     * @return true:���O�����Afalse:���O����Ȃ��B
     */
    public boolean removeFlag(final String flag)
    {
        return (this.flagMap_.remove(flag) != null);
    }

    /**
     * ���O�l��ݒ肵�܂��B<br />
     * 
     * @param key �L�[
     * @param value �l
     */
    public void setLoggingValue(final String key, final Object value)
    {
        this.loggingValueMap_.put(key, value);
    }

    /**
     * Map����L�[���擾���܂��B<br />
     * 
     * @return �L�[�z��
     */
    public String[] getLoggingKeys()
    {
        Set<String> keySet = this.loggingValueMap_.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        return keys;
    }

    /**
     * Map����L�[�ɑΉ�����l���擾���܂��B<br />
     * 
     * @param key �L�[
     * @return �L�[�̒l
     */
    public Object getLoggingValue(final String key)
    {
        return this.loggingValueMap_.get(key);
    }

    /**
     * ��O�̌������擾���܂��B<br />
     * 
     * @return ��O�̌���
     */
    public Throwable getCause()
    {
        return this.cause_;
    }

    /**
     * ��O�̌�����ݒ肵�܂��B<br />
     * 
     * @param cause ��O�̌���
     */
    public void setCause(final Throwable cause)
    {
        this.cause_ = cause;
    }

    /**
     * �C�x���g��������CallTreeNode��ǉ����܂��B<br />
     * 
     * @param node �C�x���g��������CallTreeNode
     */
    public void addEventNode(final CallTreeNode node)
    {
        if (eventList_.size() < MAX_EVENT)
        {
            this.eventList_.add(node);
        }
    }

    /**
     * �C�x���g��������CallTreeNode�̃��X�g���擾���܂��B<br />
     * 
     * @return �C�x���g��������CallTreeNode�̃��X�g
     */
    public List<CallTreeNode> getEventNodeList()
    {
        return this.eventList_;
    }

    /**
     * CallTreeNode�����P���₵�܂��B
     */
    public void incrementNodeCount()
    {
        this.nodeCount_++;
        if (isCallTreeEnabled_)
        {
            this.totalNodeCount_++;
        }
        else if (totalNodeCount_ < nodeCount_)
        {
            // javelin.call.tree.enable=false�̏ꍇ�A
            // nodeCount_���ō��l���������ꍇ�̂݁A
            // totalNodeCount_��nodeCount_��������B
            this.totalNodeCount_ = this.nodeCount_;
        }
    }

    /**
     * CallTreeNode�����P���炵�܂��B
     */
    public void decrementNodeCount()
    {
        this.nodeCount_--;
    }

    /**
     * CallTreeNode�����w�肳�ꂽ���������炵�܂��B
     *
     * @param count ���炷��
     */
    public void decrementNodeCount(int count)
    {
        this.nodeCount_ -= count;
    }
    
    /**
     * CallTreeNode�����擾���܂��B<br />
     * 
     * @return CallTreeNode��
     */
    public int getNodeCount()
    {
        return this.nodeCount_;
    }

    /**
     * �g�[�^���̃m�[�h�����擾����B
     * 
     * @return �g�[�^���̃m�[�h���B
     */
    public int getTotalNodeCount()
    {
        return totalNodeCount_;
    }
    
    /**
     * CallTree�̐[�������������܂��B<br />
     */
    public void clearDepth()
    {
        this.depthSet_ = new HashSet<Integer>();
    }

    /**
     * �v������CallTree�̐[����ۑ����܂��B<br />
     * 
     * @param depth CallTree�̐[��
     */
    public void addDepth(Integer depth)
    {
        this.depthSet_.add(depth);
    }

    /**
     * �����Ŏw�肵���[�����A�v���Ώۂł��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @param depth CallTree�̐[��
     * @return �w�肵���[�����v���Ώۂł���Ȃ�A<code>true</code>
     */
    public boolean containsDepth(Integer depth)
    {
        return this.depthSet_.contains(depth);
    }

    /**
     * �����Ŏw�肵���[�����v���Ώۂ��珜�O���܂��B<br />
     * 
     * @param depth CallTree�̐[��
     */
    public void removeDepth(Integer depth)
    {
        this.depthSet_.remove(depth);
    }

    /**
     * 臒l����p�N���X(����D��x�F��)��Ԃ��B
     * @param key 臒l����p�N���X�̃L�[
     * @return 臒l����p�N���X
     */
    public RecordStrategy getHighPriorityRecordStrategy(final String key)
    {
        return highStrategyMap_.get(key);
    }

    /**
     * �ݒ肳��Ă���臒l����p�N���X(����D��x�F��)�̃��X�g��Ԃ��B
     * @return 臒l����p�N���X�̃��X�g
     */
    public RecordStrategy[] getHighPriorityRecordStrategy()
    {
        return highStrategyMap_.values().toArray(new RecordStrategy[highStrategyMap_.size()]);
    }

    /**
     * 臒l����p�N���X(����D��x�F��)��ݒ肷��B���ɓ���̃L�[���o�^����Ă���ꍇ�́A�o�^���Ȃ��B
     * @param key 臒l����p�N���X�̃L�[
     * @param strategy 臒l����p�N���X
     */
    public void addHighPriorityRecordStrategy(final String key, final RecordStrategy strategy)
    {
        if (!highStrategyMap_.containsKey(key))
        {
            highStrategyMap_.put(key, strategy);
        }
    }

    /**
     * 臒l����p�N���X(����D��x�F�ʏ�)��ݒ肷��B���ɓ���̃L�[���o�^����Ă���ꍇ�́A�o�^���Ȃ��B
     * @param key 臒l����p�N���X�̃L�[
     * @param strategy 臒l����p�N���X
     */
    public void addRecordStrategy(final String key, final RecordStrategy strategy)
    {
        if (!normalStrategyMap_.containsKey(key))
        {
            normalStrategyMap_.put(key, strategy);
        }
    }

    /**
     * 臒l����p�N���X(����D��x�F�ʏ�)��Ԃ��B
     * @param key 臒l����p�N���X�̃L�[
     * @return 臒l����p�N���X
     */
    public RecordStrategy getRecordStrategy(final String key)
    {
        return normalStrategyMap_.get(key);
    }

    /**
     * �ݒ肳��Ă���臒l����p�N���X(����D��x�F�ʏ�)�̃��X�g��Ԃ��B
     * @return 臒l����p�N���X�̃��X�g
     */
    public RecordStrategy[] getRecordStrategy()
    {
        return normalStrategyMap_.values().toArray(new RecordStrategy[normalStrategyMap_.size()]);
    }

    /**
     * �{�c���[����CollectionMonitor���L�����ǂ����������t���O�B
     * @return true�Ȃ�΁ACollectionMonitor���L���B
     */
    public boolean isCollectionMonitorEnabled()
    {
        return isCollectionMonitorEnabled_;
    }

    /**
     * �{�c���[����ConcurrentMonitor���L�����ǂ����������t���O�B
     * @return true�Ȃ�΁AConcurrentMonitor���L���B
     */
    public boolean isConcurrentMonitorEnabled()
    {
        return isConcurrentMonitorEnabled_;
    }

    /**
     * �{�c���[���ŃR�[���c���[���L�����ǂ����������t���O�B
     * @return true�Ȃ�΁A�R�[���c���[���L���B
     */
    public boolean isCallTreeEnabled()
    {
        return isCallTreeEnabled_;
    }

    /**
     * �{�c���[����JDBC Javelin���L�����ǂ����������t���O�B
     * @return true�Ȃ�΁AJDBC Javelin���L���B
     */
    public boolean isJdbcEnabled()
    {
        return isJdbcEnabled_;
    }

    /**
     * �{�c���[����javelin.jdbc.recordDuplJdbcCall���L�����ǂ����������t���O�B
     * @return true�Ȃ�΁Ajavelin.jdbc.recordDuplJdbcCall���L���B
     */
    public boolean isRecordDuplJdbcCallEnabled()
    {
        return isRecordDuplJdbcCallEnabled_;
    }
    
    /**
     * �R�s�[�R���X�g���N�^�ɂ�莩���̃R�s�[�����B
     * @return �����̃R�s�[�B
     */
    public CallTree copy()
    {
        return new CallTree(this);
    }
}
