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
package jp.co.acroquest.endosnipe.javelin.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.converter.util.CalledMethodCounter;

/**
 * ���\�b�h�Ăяo���̓��v�����L�^����B
 * @author eriguchi
 */
public class Invocation implements InvocationMBean, Serializable
{
    /** �o�b�t�@�T�C�Y�̃f�t�H���g�l */
    private static final int DEF_BUFFER_SIZE = 256;

    /** �V���A��ID */
    private static final long serialVersionUID = -6941143619225037990L;    

    /** �����l */
    private static final long INITIAL = -1;

    /** ���X�|���X�񐔂� 0 �ȊO�̂Ƃ��� tatCallZeroValueStartTime_ �̒l */
    public static final long TAT_ZERO_KEEP_TIME_NULL_VALUE = 0;

    /** �A���[��臒l���w�肳��Ă��Ȃ��Ƃ��� alarmThreshold �̒l */
    public static final long THRESHOLD_NOT_SPECIFIED = -1;

    /** HashMap�̃f�t�H���g�T�C�Y */
    private static final int HASH_MAP_DEFALT_SIZE = 5;
    
    /** �N���X�� */
    private final String className_;

    /** ���\�b�h�� */
    private final String methodName_;

    /** CallTree���̌v���f�[�^��ۑ����邽�߂Ɏg�p����A�N���X���A���\�b�h�������������L�[���B */
    private final String key_;

    /** RootInvocationManager���g�p����L�[���B  */
    private final String rootInvocationManagerKey_;
    
    /** ���\�b�h�Ăяo���� */
    private long count_;

    /** �v���Z�X�N���ォ��̌Ăяo���� */
    private transient long countFromStartup_;

    private static final int SUM = 0;
    private static final int MAX = 1;
    private static final int MIN = 2;
    
    private long[] intervals_                = new long[]{0, INITIAL, INITIAL};
    private long[] cpuIntervals_             = new long[]{0, INITIAL, INITIAL};
    private long[] userIntervals_            = new long[]{0, INITIAL, INITIAL};
    private long[] accumulatedIntervals_     = new long[]{0, INITIAL, INITIAL};
    private long[] accumulatedCpuIntervals_  = new long[]{0, INITIAL, INITIAL};
    private long[] accumulatedUserIntervals_ = new long[]{0, INITIAL, INITIAL};
    
    /** ����������O�̌� */
    private int throwableCount_ = 0;

    /** ���o�����X�g�[���̌� */
    private int methodStallCount_ = 0;

    /** HTTP�X�e�[�^�X�̌� */
    private int httpStatusCount_ = 0;
    
    /** �Ăяo������Set */
    private final Map<Invocation, Invocation> callerSet_ =
            new ConcurrentHashMap<Invocation, Invocation>();

    /** ���\�b�h�̍ŏI���s���� */
    private long accumulatedTime_;

    /** accumulatedTime_�̍ő�l�B {@link #setAccumulatedTime}�̒���accumulatedTime_�Ƌ��ɍX�V������s���B�@*/
    private long maxAccumulatedTime_;

    /** �Ăяo������Ԃ��u�����N����ۂ�臒l�B �l�i�~���b�j������鏈�����Ԃ̌Ăяo�����͐Ԃ��u�����N���Ȃ��B */
    private long alarmThreshold_;

    /** �x���𔭐�������CPU���Ԃ�臒l */
    private long alarmCpuThreshold_ = THRESHOLD_NOT_SPECIFIED;

    /** �v���Z�X�� */
    private final String processName_;

    /** �n�b�V���R�[�h�B */
    private int code_ = 0;

    /** �ŏI�X�V���� */
    private long lastUpdatedTime_;

    /** �ŏI�A���[���������� */
    private long lastAlarmTime_;

    /** �ǉ��Œl��ۑ�����ꍇ�ɗ��p����B */
    private Map<String, Object> optValueMap_;

    /** Invocation��root���ǂ���(true:root�Afalse:not root�B�f�t�H���g�l��false) */
    private boolean isRoot_ = false;

    /** �v���Ώۂ��ۂ� */
    private TripleState measurementTarget_ = TripleState.NOT_SPECIFIED;

    /** ���X�|���X�O���t���o�͂��邩�ǂ��� */
    private TripleState responseGraphOutput_ = TripleState.NOT_SPECIFIED;

    /** AccumulatedTime���Z�b�g�̍ŏI���� */
    private long lastResetAccumulatedTime_;

    /** ���\�b�h�Ăяo���񐔂� 0 �ł����Ԃ��X�^�[�g���������B */
    private long tatCallZeroValueStartTime_;

    /** TAT�̑��a�Broot��Invocation�̂ݗL���l���ݒ肳��� */
    private long accumulatedTimeSum_;

    /** TAT�̍ő�l�Broot��Invocation�̂ݗL���l���ݒ肳��� */
    private long accumulatedMax_;

    /** TAT�̍ŏ��l�Broot��Invocation�̂ݗL���l���ݒ肳��� */
    private long accumulatedMin_;

    /** ���[�g�Ƃ��ČĂяo���ꂽ�񐔁Broot��Invocation�̂ݗL���l���ݒ肳��� */
    private int accumulatedTimeCount_;

    /** Turn Around Time���v�����邩�ǂ���true:�v������Afalse:�v�����Ȃ��B�f�t�H���g�l��true) */
    private boolean isTatEnabled_ = true;

    /** Turn Around Time�̕ێ����� */
    private long tatKeepTime_;

    /** Invocation��Java�N���X�ɑ΂��Đݒ肳�ꂽ���̂��ǂ��� */
    private boolean isJavaClass_ = false;

    /** �N����A���\�b�h���Ăяo���ꂽ�� */
    private transient boolean isCalledAfterStarted_ = false;

    /** ���O�D��ł��邩�̃t���O */
    private boolean isExcludePreffered_;

    /** �ΏۗD��ł��邩 */
    private boolean isTargetPreferred_;

    /** ���O�ł��邩*/
    private boolean isExclude_;

    /** �^�[�Q�b�g�ł��邩 */
    private boolean isTarget_;

    /** ��O�̃J�E���g��Map */
    private Map<String, Integer> throwableCountMap_;
    
    /** httpStatus�̃J�E���g��Map */
    private Map<String, Integer> httpStatusCountMap_;
    
    /**
     * �R���X�g���N�^
     * @param processName �v���Z�X��
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param alarmThreshold �A���[�������̂��߂̃��\�b�h���s���Ԃ�臒l
     */
    public Invocation(
            final String processName, 
            final String className, 
            final String methodName, 
            final long   alarmThreshold)
    {
        this.processName_    = processName;
        this.className_      = className;
        this.methodName_     = methodName;
        this.alarmThreshold_ = alarmThreshold;
        this.isJavaClass_    = judgeIsJavaClass(className, methodName);

        this.key_  = className + "#" + methodName;
        this.code_ = key_.hashCode();

        if (methodName.startsWith("/"))
        {
            if ("/".equals(className))
            {
                // �N���X���� "/" �݂̂̏ꍇ�A�N���X���ƃ��\�b�h�����Ȃ���� "//path" �Ƃ����悤��
                // "/" ���A�����邽�߁A�N���X�������������̂����ʕ�����Ƃ���
                this.rootInvocationManagerKey_ = methodName;
            }
            else
            {
                this.rootInvocationManagerKey_ = className + methodName;
            }
        }
        else
        {
            this.rootInvocationManagerKey_ = this.key_;
        }
        
        this.lastAlarmTime_   = 0;
        this.lastUpdatedTime_ = System.currentTimeMillis();
        this.tatCallZeroValueStartTime_ = TAT_ZERO_KEEP_TIME_NULL_VALUE;
        this.isTarget_ = true;
        this.isExclude_ = false;
        this.isTargetPreferred_ = false;
        this.isExcludePreffered_ = false;
        this.throwableCountMap_ = new HashMap<String, Integer>(HASH_MAP_DEFALT_SIZE);
        this.httpStatusCountMap_ = new HashMap<String, Integer>(HASH_MAP_DEFALT_SIZE);
    }

    /**
     * �N���X�����擾����B
     * @return �N���X��
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * ���\�b�h�����擾����B
     * @return ���\�b�h��
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * �L�[�����擾����B
     * @return �L�[���
     */
    public String getKey()
    {
        return this.key_;
    }

    /**
     * RootInvocationManager�Ŏg�p����L�[�����擾����B
     * @return �L�[���
     */
    public String getRootInvocationManagerKey()
    {
        return rootInvocationManagerKey_;
    }

    /**
     * ���\�b�h�Ăяo���񐔂��擾����B
     * @return ���\�b�h�Ăяo����
     */
    public long getCount()
    {
        return this.count_;
    }

    /**
     * InvocationInterval�̊e�v�f�̍ŏ��l��ۑ����Ă���{@link InvocationInterval}
     * @return InvocationInterval
     */
    public long getMinimum()
    {
        return this.intervals_[MIN];
    }

    /**
     * InvocationInterval�̊e�v�f�̍ő�l��ۑ����Ă���{@link InvocationInterval}
     * @return InvocationInterval
     */
    public long getMaximum()
    {
        return this.intervals_[MAX];
    }

    /**
     * ���\�b�h���s���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h���s���Ԃ̕��ϒl
     */
    public long getAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }

        return this.intervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h���s���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h���s���Ԃ̍��v�l
     */
    public long getTotal()
    {
        return this.intervals_[SUM];
    }

    /**
     * ���\�b�h��CPU���Ԃ̍ŏ��l���擾����B
     * @return ���\�b�h��CPU���Ԃ̍ŏ��l
     */
    public long getCpuMinimum()
    {
        return this.cpuIntervals_[MIN];
    }

    /**
     * ���\�b�h��CPU���Ԃ̍ő�l���擾����B
     * @return ���\�b�h��CPU���Ԃ̍ő�l
     */
    public long getCpuMaximum()
    {
        return this.cpuIntervals_[MAX];
    }

    /**
     * ���\�b�h��CPU���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h��CPU���Ԃ̕��ϒl
     */
    public long getCpuAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }
        
        return this.cpuIntervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h�� CPU ���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h�� CPU ���Ԃ̍��v�l
     */
    public long getCpuTotal()
    {
        return this.cpuIntervals_[SUM];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍ŏ��l���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̍ŏ��l
     */
    public long getUserMinimum()
    {
        return this.userIntervals_[MIN];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍ő�l���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̍ő�l
     */
    public long getUserMaximum()
    {
        return this.userIntervals_[MAX];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̕��ϒl
     */
    public long getUserAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }

        return this.userIntervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h�̃��[�U���Ԃ̍��v�l
     */
    public long getUserTotal()
    {
        return this.userIntervals_[SUM];
    }

    /**
     * InvocationInterval�̊e�v�f�̍ŏ��l��ۑ����Ă���{@link InvocationInterval}
     * @return InvocationInterval
     */
    public long getAccumulatedMinimum()
    {
        return this.accumulatedIntervals_[MIN];
    }

    /**
     * InvocationInterval�̊e�v�f�̍ő�l��ۑ����Ă���{@link InvocationInterval}
     * @return InvocationInterval
     */
    public long getAccumulatedMaximum()
    {
        return this.accumulatedIntervals_[MAX];
    }

    /**
     * ���\�b�h���s���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h���s���Ԃ̕��ϒl
     */
    public long getAccumulatedAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }

        return this.accumulatedIntervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h���s���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h���s���Ԃ̍��v�l
     */
    public long getAccumulatedTotal()
    {
        return this.accumulatedIntervals_[SUM];
    }

    /**
     * ���\�b�h��CPU���Ԃ̍ŏ��l���擾����B
     * @return ���\�b�h��CPU���Ԃ̍ŏ��l
     */
    public long getAccumulatedCpuMinimum()
    {
        return this.accumulatedCpuIntervals_[MIN];
    }

    /**
     * ���\�b�h��CPU���Ԃ̍ő�l���擾����B
     * @return ���\�b�h��CPU���Ԃ̍ő�l
     */
    public long getAccumulatedCpuMaximum()
    {
        return this.accumulatedCpuIntervals_[MAX];
    }

    /**
     * ���\�b�h��CPU���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h��CPU���Ԃ̕��ϒl
     */
    public long getAccumulatedCpuAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }

        return this.accumulatedCpuIntervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h�� CPU ���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h�� CPU ���Ԃ̍��v�l
     */
    public long getAccumulatedCpuTotal()
    {
        return this.accumulatedCpuIntervals_[SUM];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍ŏ��l���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̍ŏ��l
     */
    public long getAccumulatedUserMinimum()
    {
        return this.accumulatedUserIntervals_[MIN];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍ő�l���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̍ő�l
     */
    public long getAccumulatedUserMaximum()
    {
        return this.accumulatedUserIntervals_[MAX];
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̕��ϒl���擾����B
     * @return ���\�b�h�̃��[�U���Ԃ̕��ϒl
     */
    public long getAccumulatedUserAverage()
    {
        // 0���Z�������B
        if (this.count_ == 0)
        {
            return 0;
        }
        
        return this.accumulatedUserIntervals_[SUM] / this.count_;
    }

    /**
     * ���\�b�h�̃��[�U���Ԃ̍��v�l���擾���܂��B<br />
     *
     * @return ���\�b�h�̃��[�U���Ԃ̍��v�l
     */
    public long getAccumulatedUserTotal()
    {
        return this.accumulatedUserIntervals_[SUM];
    }
    
    /**
     * ��O�̔����񐔂�Ԃ��B
     * @return ��O�̔�����
     */
    public long getThrowableCount()
    {
        return this.throwableCount_;
    }

    /**
     * �X�g�[���̔����񐔂�Ԃ��B
     * @return �X�g�[���̔�����
     */
    public long getMethodStallCount()
    {
        return this.methodStallCount_;
    }
    
    /**
     * �X�g�[���̔����񐔂����Z����B
     * @param count �X�g�[���̔�����
     */
    public synchronized void addMethodStallCount(long count)
    {
        this.methodStallCount_ += count; 
    }

    /**
     * ���\�b�h�̌Ăяo������z��Ƃ��Ď擾����B<br />
     * �`���F �N���X��#���\�b�h��
     * @return ���\�b�h�̌Ăяo�����̔z��
     */
    public synchronized String[] getAllCallerName()
    {
        Invocation[] invocations = 
            callerSet_.keySet().toArray(new Invocation[callerSet_.size()]);
        String[] objNames = new String[invocations.length];

        for (int index = 0; index < invocations.length; index++)
        {
            objNames[index] =
                    invocations[index].getClassName() 
                    + "#" + invocations[index].getMethodName();
        }
        return objNames;
    }

    /**
     * Invocation���ۑ����Ă���S�Ẵ��\�b�h�̌Ăяo�����ɑΉ�����Invocation��Ԃ��B
     * @return �S�Ẵ��\�b�h�̌Ăяo�����ɑΉ�����Invocation
     */
    public synchronized Invocation[] getAllCallerInvocation()
    {
        Invocation[] invocations = 
            callerSet_.keySet().toArray(new Invocation[callerSet_.size()]);
        return invocations;
    }

    /**
     * �ۑ�����InvocationInterval�̏����X�V����B<br />
     * �����āA�N���㏉�߂ČĂяo����A����Java�N���X�������ꍇ�͌Ăяo���ꂽ���Ƃ��J�E���^�ɔ��f����B<br />
     * �X�V�Ώ�:
     * <ul>
     * �@�@<li>count_:���\�b�h�Ăяo����</li>
     * �@�@<li>intervalSum_:InvocationInterval�̊e�v�f�̍��v</li>
     * �@�@<li>intervalList_:Invocation���ۑ�����Invocation�̃��X�g</li>
     * �@�@<li>intervalMax_:InvocationInteral�̊e�v�f�̍ő�l</li>
     * �@�@<li>minimumInterval_:InvocationInterval�̊e�v�f�̍ŏ��l</li>
     * </ul>
     * @param node �m�[�h
     * @param interval {@link InvocationInterval}�I�u�W�F�N�g
     * @param cpuInterval CPU�̃C���^�[�o������
     * @param userInterval USER�̃C���^�[�o������
     */
    public synchronized void addInterval(
            final CallTreeNode node, 
            final long interval,
            final long cpuInterval,
            final long userInterval)
    {
        if (this.isCalledAfterStarted_ == false && this.isJavaClass_ == true)
        {
            this.isCalledAfterStarted_ = true;
            CalledMethodCounter.incrementCounter();
        }

        intervals_[SUM]     += interval;
        cpuIntervals_[SUM]  += cpuInterval;
        userIntervals_[SUM] += userInterval;

        if (intervals_[MAX] < interval)
        {
            intervals_[MAX] = interval;
        }
        if (cpuIntervals_[MAX] < cpuInterval)
        {
            cpuIntervals_[MAX] = cpuInterval;
        }
        if (userIntervals_[MAX] < userInterval)
        {
            userIntervals_[MAX] = userInterval;
        }
        
        if (intervals_[MIN] == INITIAL || intervals_[MIN] > interval)
        {
            intervals_[MIN] = interval;
        }
        if (cpuIntervals_[MIN] == INITIAL || cpuIntervals_[MIN] > cpuInterval)
        {
            cpuIntervals_[MIN] = cpuInterval;
        }
        if (userIntervals_[MIN] == INITIAL || userIntervals_[MIN] > userInterval)
        {
            userIntervals_[MIN] = userInterval;
        }
        
        long accumulatedTime = node.getAccumulatedTime();
        long cpuTime         = node.getCpuTime();
        long userTime        = node.getUserTime();
        
        accumulatedIntervals_[SUM]     += accumulatedTime;
        accumulatedCpuIntervals_[SUM]  += cpuTime;
        accumulatedUserIntervals_[SUM] += userTime;
        
        if (accumulatedIntervals_[MAX] < accumulatedTime)
        {
            accumulatedIntervals_[MAX] = accumulatedTime;
        }
        if (accumulatedCpuIntervals_[MAX] < cpuTime)
        {
            accumulatedCpuIntervals_[MAX] = cpuTime;
        }
        if (accumulatedUserIntervals_[MAX] < userTime)
        {
            accumulatedUserIntervals_[MAX] = userTime;
        }
        
        if (accumulatedIntervals_[MIN] == INITIAL || accumulatedIntervals_[MIN] > accumulatedTime)
        {
            accumulatedIntervals_[MIN] = accumulatedTime;
        }
        if (accumulatedCpuIntervals_[MIN] == INITIAL || accumulatedCpuIntervals_[MIN] > cpuTime)
        {
            accumulatedCpuIntervals_[MIN] = cpuTime;
        }
        if (accumulatedUserIntervals_[MIN] == INITIAL || accumulatedUserIntervals_[MIN] > userTime)
        {
            accumulatedUserIntervals_[MIN] = userTime;
        }
        
        this.count_++;
        this.countFromStartup_++;
        
        updateLastUpdatedTime();
    }

    /**
     * Invocation���ۑ����郁�\�b�h�̌Ăяo�����̍X�V���s���B
     * @param caller ���\�b�h�̌Ăяo������{@link Invocation}�I�u�W�F�N�g
     */
    public void addCaller(final Invocation caller)
    {
        if (caller != null)
        {
            this.callerSet_.put(caller, caller);
        }
        updateLastUpdatedTime();
    }

    /**
     * Invocation���ۑ������O�̍X�V���s���B
     * @param throwable �V�K�Ŕ�������{@link Throwable}�I�u�W�F�N�g
     */
    public synchronized void addThrowable(final Throwable throwable)
    {
        String name = throwable.getClass().getName();
        Integer count = this.throwableCountMap_.get(name);
        if(count == null)
        {
            count = Integer.valueOf(1);
        }
        else
        {
            count = Integer.valueOf(count.intValue() + 1);
        }
        this.throwableCountMap_.put(name, count);
        
        this.throwableCount_++;
        updateLastUpdatedTime();
    }

    /**
     * �A���[�����������TAT��臒l��Ԃ��B
     * @return �A���[�����������TAT��臒l�i�ʎw�肳��Ă��Ȃ��ꍇ�� -1 �j
     */
    public long getAlarmThreshold()
    {
        return this.alarmThreshold_;
    }

    /**
     * �A���[�����������TAT��臒l��ݒ肷��B
     * @param alarmThreshold �A���[�����������TAT��臒l
     */
    public void setAlarmThreshold(final long alarmThreshold)
    {
        this.alarmThreshold_ = alarmThreshold;
        updateLastUpdatedTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer(DEF_BUFFER_SIZE);
        buffer.append(processName_);
        buffer.append(":");
        buffer.append(className_);
        buffer.append("#");
        buffer.append(methodName_);
        buffer.append(",");
        buffer.append(getCount());
        buffer.append(",");
        buffer.append(getMinimum());
        buffer.append(",");
        buffer.append(getMaximum());
        buffer.append(",");
        buffer.append(getAverage());
        buffer.append(",");
        buffer.append(getThrowableCount());
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object target)
    {
        if (!(target instanceof Invocation))
        {
            return false;
        }

        Invocation invocation = (Invocation)target;
        
//        if (!this.className_.equals(invocation.getClassName())
//                || !this.methodName_.equals(invocation.getMethodName()))
        if (!this.key_.equals(invocation.getKey()))
        {
            return false;
        }
        
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.code_;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void reset()
    {
        count_ = 0;
        this.throwableCount_ = 0;
        this.methodStallCount_ = 0;
        this.httpStatusCount_ = 0;
        this.isCalledAfterStarted_ = false;

        intervals_[SUM] = 0;
        intervals_[MAX] = INITIAL;
        intervals_[MIN] = INITIAL;
        cpuIntervals_[SUM] = 0;
        cpuIntervals_[MAX] = INITIAL;
        cpuIntervals_[MIN] = INITIAL;
        userIntervals_[SUM] = 0;
        userIntervals_[MAX] = INITIAL;
        userIntervals_[MIN] = INITIAL;
        accumulatedIntervals_[SUM] = 0;
        accumulatedIntervals_[MAX] = INITIAL;
        accumulatedIntervals_[MIN] = INITIAL;
        accumulatedCpuIntervals_[SUM] = 0;
        accumulatedCpuIntervals_[MAX] = INITIAL;
        accumulatedCpuIntervals_[MIN] = INITIAL;
        accumulatedUserIntervals_[SUM] = 0;
        accumulatedUserIntervals_[MAX] = INITIAL;
        accumulatedUserIntervals_[MIN] = INITIAL;

        updateLastUpdatedTime();
    }

    /**
     * �v���Z�X�����擾����B
     * @return �v���Z�X��
     */
    public String getProcessName()
    {
        return processName_;
    }

    /**
     * ���\�b�h�̍ŏI���s���Ԃ��擾����B
     * @return ���\�b�h�̍ŏI���s����
     */
    public long getAccumulatedTime()
    {
        return accumulatedTime_;
    }

    /**
     * ���\�b�h�̍ŏI���s���Ԃ�ݒ肵�A���܂ł̃��\�b�h���s���Ԃ̍ő�l�𒴂��Ă���ꍇ�́A�ő�l���X�V����B
     * @param accumulatedTime ���\�b�h�̍ŏI���s����
     */
    public void setAccumulatedTime(final long accumulatedTime)
    {
        this.accumulatedTime_ = accumulatedTime;
        if (this.accumulatedTime_ > this.maxAccumulatedTime_)
        {
            this.maxAccumulatedTime_ = this.accumulatedTime_;
        }
        
        updateLastUpdatedTime();
        // TAT�v���Ώۂł���A���ATAT���v������ݒ�̏ꍇ�A
        // AccumulatedTime�̑��a�Ɛݒ�񐔂��X�V����
        if (isResponseGraphOutputTarget() && this.isTatEnabled_ == true)
        {
            if (this.lastResetAccumulatedTime_ != 0
                    && this.lastUpdatedTime_ - this.lastResetAccumulatedTime_ > this.tatKeepTime_)
            {
                // TAT�ێ����Ԃ��Â��f�[�^�͏�������
                this.accumulatedTimeSum_ = accumulatedTime;
                this.accumulatedMin_ = accumulatedTime;
                this.accumulatedMax_ = accumulatedTime;
                this.accumulatedTimeCount_ = 1;
            }
            else
            {
                // AccumulatedTime�̑��a�Ɛݒ�񐔂��X�V����
                if (accumulatedTime < this.accumulatedMin_ || this.accumulatedTimeCount_ == 0)
                {
                    this.accumulatedMin_ = accumulatedTime;
                }
                if (accumulatedTime > this.accumulatedMax_)
                {
                    this.accumulatedMax_ = accumulatedTime;
                }
                this.accumulatedTimeSum_ += accumulatedTime;
                this.accumulatedTimeCount_++;
            }
            this.lastResetAccumulatedTime_ = this.lastUpdatedTime_;
        }
    }

    /**
     * ���\�b�h�̎��s���Ԃ̍ő�l���擾����B
     * @return ���\�b�h�̎��s���Ԃ̍ő�l
     */
    public long getMaxAccumulatedTime()
    {
        return this.maxAccumulatedTime_;
    }

    /**
     * ���\�b�h���s���Ԃ̍ő�l���X�V�����񐔂��擾����B
     * @return ���\�b�h���s���Ԃ̍ő�l���X�V������
     */
    public long getCountFromStartup()
    {
        return this.countFromStartup_;
    }

    /**
     * �x���𔭐�������CPU���Ԃ�臒l���擾����
     * @return CPU���Ԃ�臒l
     */
    public long getAlarmCpuThreshold()
    {
        return this.alarmCpuThreshold_;
    }

    /**
     * �x���𔭐�������CPU���Ԃ�臒l��ݒ肷��
     * @param alarmCpuThreshold CPU���Ԃ�臒l
     */
    public void setAlarmCpuThreshold(final long alarmCpuThreshold)
    {
        this.alarmCpuThreshold_ = alarmCpuThreshold;
        updateLastUpdatedTime();
    }

    /**
     * �ŏI�X�V�������擾����B
     * @return �ŏI�X�V����
     */
    public long getLastUpdatedTime()
    {
        return this.lastUpdatedTime_;
    }

    /**
     * �ŏI�X�V�������X�V����B
     */
    private void updateLastUpdatedTime()
    {
        this.lastUpdatedTime_ = System.currentTimeMillis();
    }

    /**
     * �ŏI�A���[�������������擾����B
     * @return �ŏI�A���[����������
     */
    public long getLastAlarmTime()
    {
        return this.lastAlarmTime_;
    }

    /**
     * �ŏI�A���[�������������X�V����B
     * @param lastAlarmTime �ŏI�A���[����������
     */
    public void setLastAlarmTime(final long lastAlarmTime)
    {
        this.lastAlarmTime_ = lastAlarmTime;
    }

    /**
     * �ǉ��ŕۑ�����l���X�V����B
     * @param key �X�V����l�ɑΉ�����L�[
     * @param value �X�V����l
     */
    public synchronized void putOptValue(String key, Object value)
    {
        if(this.optValueMap_ == null)
        {
            this.optValueMap_ = new ConcurrentHashMap<String, Object>();
        }
        
        this.optValueMap_.put(key, value);
    }

    /**
     * key�ɑΉ�����l��Invocation����擾����B
     * @param key �L�[
     * @return Invocation���ۑ�����key�ɑΉ�����l
     */
    public synchronized Object getOptValue(String key)
    {
        if(this.optValueMap_ == null)
        {
            this.optValueMap_ = new ConcurrentHashMap<String, Object>();
        }
        
        return this.optValueMap_.get(key);
    }

    /**
     * Invocation��root���ǂ�����\���l��Ԃ��B
     * @return true:root�Afalse:not root
     */
    public boolean isRoot()
    {
        return this.isRoot_;
    }

    /**
     * Invocation��root���ǂ�����\���l��ݒ肷��B
     * @param isRoot Invocation��root���ǂ�����\���l
     */
    public void setRoot(final boolean isRoot)
    {
        this.isRoot_ = isRoot;
    }

    /**
     * �g�����U�N�V�����O���t�o�͑Ώېݒ��Ԃ��܂��B<br />
     *
     * @return {@link TripleState} �̒l
     */
    public TripleState getTransactionGraphOutput()
    {
        return this.responseGraphOutput_;
    }

    /**
     * �v���Ώۂ��ۂ����Z�b�g���܂��B<br />
     *
     * @param state �v���Ώۂ̏ꍇ�� <code>ON</code> �A�v���ΏۂłȂ��ꍇ�� <code>OFF</code> �A
     *              �w�肳��Ă��Ȃ��ꍇ�� <code>NOT_SPECIFIED</code>
     */
    public void setMeasurementTarget(TripleState state)
    {
        this.measurementTarget_ = state;
    }

    /**
     * �v���Ώۂ��ۂ���Ԃ��܂��B<br />
     *
     * @return �v���Ώۂ̏ꍇ�� <code>ON</code> �A�v���ΏۂłȂ��ꍇ�� <code>OFF</code> �A
     *         �w�肳��Ă��Ȃ��ꍇ�� <code>NOT_SPECIFIED</code>
     */
    public TripleState getMeasurementTarget()
    {
        return this.measurementTarget_;
    }

    /**
     * �g�����U�N�V�����O���t�o�͑Ώېݒ���Z�b�g���܂��B<br />
     *
     * @param output {@link TripleState} �̒l
     */
    public void setResponseGraphOutput(TripleState output)
    {
        this.responseGraphOutput_ = output;
    }

    /**
     * �g�����U�N�V�����O���t�o�͑Ώۂ��ۂ���Ԃ��܂��B<br />
     *
     * @return �g�����U�N�V�����O���t�o�͑Ώۂ̏ꍇ�� <code>true</code>
     */
    public boolean isResponseGraphOutputTarget()
    {
        TripleState output = getTransactionGraphOutput();
        return (output == TripleState.ON || (output == TripleState.NOT_SPECIFIED && isRoot()));
    }

    /**
     * �{�N���X�����AAccumulatedTime�̑��a�Ɛݒ�񐔂����Z�b�g���A
     * �O�񃊃Z�b�g����́AAccumulatedTime�̕��ϒl��Ԃ��B<br />
     * �O�񃊃Z�b�g���� {@link #setAccumulatedTime} ���Ăяo����Ă��Ȃ��ꍇ��0��Ԃ��B
     *
     * @return �O�񃊃Z�b�g���獡��܂ł� TAT ���
     */
    public synchronized TurnAroundTimeInfo resetAccumulatedTimeCount()
    {
        this.lastResetAccumulatedTime_ = System.currentTimeMillis();

        if (this.accumulatedTimeCount_ == 0)
        {
            if (this.tatCallZeroValueStartTime_ == TAT_ZERO_KEEP_TIME_NULL_VALUE)
            {
                // �Ăяo���񐔂̒l�� 0 �ł����Ԃ��J�n�����������Z�b�g����
                this.tatCallZeroValueStartTime_ = this.lastResetAccumulatedTime_;
            }
            this.accumulatedTimeSum_ = 0;
            this.accumulatedMin_ = 0;
            this.resetThrowableCountMap(false);
            this.resetHttpStatusCountMap(false);
            TurnAroundTimeInfo retVal = 
                    new TurnAroundTimeInfo(this.accumulatedTimeSum_, this.accumulatedMax_,
                                          this.accumulatedMin_, this.accumulatedTimeCount_,
                                          this.throwableCountMap_, this.httpStatusCountMap_,
                                          this.methodStallCount_);
            this.throwableCountMap_ = new HashMap<String, Integer>(HASH_MAP_DEFALT_SIZE);
            this.httpStatusCountMap_ = new HashMap<String, Integer>(HASH_MAP_DEFALT_SIZE);
            this.methodStallCount_ = 0;
            return retVal;
        }

        this.tatCallZeroValueStartTime_ = TAT_ZERO_KEEP_TIME_NULL_VALUE;

        TurnAroundTimeInfo retVal =
                new TurnAroundTimeInfo(accumulatedTimeSum_, this.accumulatedMax_,
                                       this.accumulatedMin_, accumulatedTimeCount_,
                                       this.throwableCountMap_, this.httpStatusCountMap_,
                                       this.methodStallCount_);
        this.accumulatedTimeSum_ = 0;
        this.accumulatedTimeCount_ = 0;
        this.accumulatedMax_ = 0;
        this.accumulatedMin_ = Long.MAX_VALUE;
        this.resetThrowableCountMap(true);
        this.resetHttpStatusCountMap(true);
        this.methodStallCount_ = 0;
        return retVal;
    }
    
    /**
     * �{�N���X������ThrowableCountMap�����Z�b�g���܂��B<br />
     * �l��1�ȏ�̗v�f�͒l��0�ɂ��A�l��0�̗v�f�͂��̂܂܎c����Map�����菜���܂��B<br />
     * 
     * @param removeZeroCountData count��0�̃f�[�^��Map�����菜�����ǂ���
     */
    private synchronized void resetThrowableCountMap(boolean removeZeroCountData)
    {
        Map<String, Integer> newMap = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> orgEntry : this.throwableCountMap_.entrySet())
        {
            String name = orgEntry.getKey();
            Integer count = orgEntry.getValue();
            if (removeZeroCountData == false || 0 < count)
            {
                newMap.put(name, 0);
            }
        }
        this.throwableCountMap_ = newMap;
    }

    /**
     * Turn Around Time���v�����邩�ǂ�����ݒ肷��B
     * @param tatEnabled Turn Around Time���v������Ȃ�true
     */
    public void setTatEnabled(final boolean tatEnabled)
    {
        this.isTatEnabled_ = tatEnabled;
    }

    /**
     * Turn Around Time�̕ێ����Ԃ��Z�b�g����B
     * @param tatKeepTime Turn Around Time�̕ێ�����
     */
    public void setTatKeepTime(final long tatKeepTime)
    {
        this.tatKeepTime_ = tatKeepTime;
    }

    /**
     * ���\�b�h�Ăяo���񐔂� 0 �ł����Ԃ��X�^�[�g����������Ԃ��܂��B<br />
     *
     * @return ���\�b�h�Ăяo���񐔂� 0 �ł����Ԃ��X�^�[�g��������
     */
    public long getTatCallZeroValueStartTime()
    {
        return this.tatCallZeroValueStartTime_;
    }

    /**
     * Turn Around Time�̍ő�l��Ԃ��܂��B<br />
     * 
     * @return Turn Around Time�̍ő�l
     */
    public long getAccumulatedMax()
    {
        return this.accumulatedMax_;
    }

    /**
     * Turn Around Time�̍ŏ��l��Ԃ��܂��B<br />
     * 
     * @return Turn Around Time�̍ŏ��l
     */
    public long getAccumulatedMin()
    {
        return this.accumulatedMin_;
    }

    /**
     * Invocation�N���X�̃N���X���́A���\�b�h���̂����Java�N���X���ǂ����𔻒肷��B
     * @param className �N���X����
     * @param methodName ���\�b�h����
     * @return true Java�N���X�������ꍇ
     *         false Java�N���X�ł͂Ȃ��ꍇ
     */
    private static boolean judgeIsJavaClass(final String className, final String methodName)
    {
        //�C�x���g����p�ɐ������ꂽInvocation��Ώۂ��珜�O
        if (className == null || className.length() == 0 || methodName == null
                || methodName.length() == 0)
        {
            return false;
        }

        // S2JavelinFilter/HttpServletMonitor�AJrubyConverter�A
        // Event�o�͎��ɐ������ꂽInvocation��Ώۂ��珜�O
        if (className.startsWith("/") == true
                || EventConstants.EVENT_CLASSNAME.equals(className) == true)
        {
            return false;
        }

        // JDBCJavelin�ɂ��ϊ������O
        if (className.startsWith("jdbc:"))
        {
            return false;
        }

        //Javelin���O�v�f�쐬�pInvocation�ƁAEvent���M�pInvocation�A
        //S2JavelinFilter/HttpServletMonitor�ɍ쐬���ꂽInvocation�����O
        if (JavelinLogConstants.DEFAULT_LOGMETHOD.equals(methodName) == true
                || methodName.startsWith("/") == true
                || EventConstants.EVENT_METHODNAME.equals(className) == true)
        {
            return false;
        }

        return true;
    }

    /**
     * ���O�D��ł��邩�̃t���O��ݒ肵�܂��B
     * @param isExcludePreffered ���O�D��ł��邩�̃t���O
     */
    public void setExcludePreffered(boolean isExcludePreffered)
    {
        this.isExcludePreffered_ = isExcludePreffered;
    }

    /**
     * ���O�D��ł��邩�̃t���O���擾���܂��B
     * @return ���O�D��ł��邩�̃t���O
     */
    public boolean isExcludePreffered()
    {
        return isExcludePreffered_;
    }

    /**
     * �ΏۗD��ł��邩�̃t���O���擾���܂��B
     * @return �ΏۗD��ł��邩�̃t���O
     */
    public boolean isTargetPreferred()
    {
        return this.isTargetPreferred_;
    }

    /**
     * �ΏۗD��ł��邩�̃t���O��ݒ肵�܂��B
     * @param isTargetPreferred �ΏۗD��ł��邩�̃t���O
     */
    public void setTargetPreferred(boolean isTargetPreferred)
    {
        isTargetPreferred_ = isTargetPreferred;
    }

    /**
     * ���O�ł��邩�̃t���O��ݒ肵�܂��B
     * @param isExclude ���O�ł��邩�̃t���O
     */
    public void setExclude(boolean isExclude)
    {
        this.isExclude_ = isExclude;
    }

    /**
     * ���O�ł��邩�̃t���O���擾���܂��B
     * @return ���O�ł��邩�̃t���O
     */
    public boolean isExclude()
    {
        return isExclude_;
    }

    /**
     * �^�[�Q�b�g�ł��邩�̃t���O��ݒ肵�܂��B
     * @param isTarget �^�[�Q�b�g�ł��邩�̃t���O
     */
    public void setTarget(boolean isTarget)
    {
        this.isTarget_ = isTarget;
    }

    /**
     * �^�[�Q�b�g�ł��邩�̃t���O���擾���܂��B
     * @return �^�[�Q�b�g�ł��邩�̃t���O
     */
    public boolean isTarget()
    {
        return isTarget_;
    }

    /**
     * �����Ŏw�肵��HTTP�X�e�[�^�X�ɑ΂��ăJ�E���g�����܂��B
     * @param httpStatus HTTP�X�e�[�^�X
     */
    public synchronized void addHttpStatusCount(final String httpStatus)
    {
        Integer count = this.throwableCountMap_.get(httpStatus);
        if(count == null)
        {
            count = Integer.valueOf(1);
        }
        else
        {
            count = Integer.valueOf(count.intValue() + 1);
        }
        this.throwableCountMap_.put(httpStatus, count);
        
        this.throwableCount_++;
        updateLastUpdatedTime();
/*        
        
        Integer count = this.httpStatusCountMap_.get(httpStatus);
        if(count == null)
        {
            count = Integer.valueOf(1);
        }
        else
        {
            count = Integer.valueOf(count.intValue() + 1);
        }
        this.httpStatusCountMap_.put(httpStatus, count);
        
        updateLastUpdatedTime();
        */
    }

    
    /**
     * �{�N���X������HttpStatusCountMap�����Z�b�g���܂��B<br />
     * �l��1�ȏ�̗v�f�͒l��0�ɂ��A�l��0�̗v�f�͂��̂܂܎c����Map�����菜���܂��B<br />
     * 
     * @param removeZeroCountData count��0�̃f�[�^��Map�����菜�����ǂ���
     */
    private synchronized void resetHttpStatusCountMap(boolean removeZeroCountData)
    {
        Map<String, Integer> newMap = new HashMap<String, Integer>();
        if (this.httpStatusCountMap_ != null)
        {
            for (Map.Entry<String, Integer> orgEntry : this.httpStatusCountMap_.entrySet())
            {
                String name = orgEntry.getKey();
                Integer count = orgEntry.getValue();
                if (removeZeroCountData == false || 0 < count)
                {
                    newMap.put(name, 0);
                }
            }
        }
        this.httpStatusCountMap_ = newMap;
    }
}
