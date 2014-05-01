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
package jp.co.acroquest.endosnipe.javelin.converter.leak.monitor;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * CollectionTraceManager�ɂ�����Collection�̃G���g���B
 * 
 * @author kimura
 *
 */
public class CollectionMonitorEntry
{
    /** �Ώۂ̎��ʎq */
    private String entryIdentifier_;

    /** ���o�� */
    private int detectCount_;

    /** �X�^�b�N�g���[�X���� */
    private final Set<Integer> traceSet_;

    /** �Ώۂ̃R���N�V���� */
    private WeakReference<Collection<?>> targetCollection_;

    /** �Ώۂ̃}�b�v */
    private WeakReference<Map<?, ?>> targetMap_;

    /** �Ώۂ̃T�C�Y */
    private int entryNumber_;
    
    /** ���o���̗v�f���B */
    private int detectedSize_;

    /**
     * �R���X�g���N�^
     * 
     * @param entryIdentifier �Ώۂ̎��ʎq
     * @param target �Ώ�
     */
    public CollectionMonitorEntry(final String entryIdentifier, final Collection<?> target)
    {
        this.entryIdentifier_ = entryIdentifier;
        this.detectCount_ = 0;
        this.traceSet_ = new LinkedHashSet<Integer>();
        this.targetCollection_ = new WeakReference<Collection<?>>(target);
        this.entryNumber_ = target.size();
    }

    /**
     * �R���X�g���N�^
     * 
     * @param entryIdentifier �Ώۂ̎��ʎq
     * @param target �Ώ�
     */
    public CollectionMonitorEntry(final String entryIdentifier, final Map<?, ?> target)
    {
        this.entryIdentifier_ = entryIdentifier;
        this.detectCount_ = 0;
        this.traceSet_ = new LinkedHashSet<Integer>();
        this.targetMap_ = new WeakReference<Map<?, ?>>(target);
        this.entryNumber_ = target.size();
    }

    /**
     * �Ώۂ̃T�C�Y���X�V����B
     */
    public void updateEntryNumber()
    {
        if (this.targetCollection_ != null)
        {
            Collection<?> collection = this.targetCollection_.get();
            if (collection != null)
            {
                this.entryNumber_ = collection.size();
            }
        }

        if (this.targetMap_ != null)
        {
            Map<?, ?> map = this.targetMap_.get();
            if (map != null)
            {
                this.entryNumber_ = map.size();
            }
        }
    }

    /**
     * �Ώۂ̎��ʎq���擾����
     * 
     * @return �Ώۂ̎��ʎq
     */
    public String getEntryIdentifier()
    {
        return this.entryIdentifier_;
    }

    /**
     * �Ώۂ̎��ʎq��ݒ肷��
     * 
     * @param entryIdentifier �Ώۂ̎��ʎq
     */
    public void setEntryIdentifier(final String entryIdentifier)
    {
        this.entryIdentifier_ = entryIdentifier;
    }

    /**
     * �Ώۂ̃T�C�Y���擾����
     * 
     * @return �Ώۂ̃T�C�Y
     */
    public int getEntryNumber()
    {
        return this.entryNumber_;
    }

    /**
     * ���[�N���o�񐔂��擾����B
     * @return ���[�N���o��
     */
    public int getDetectCount()
    {
        return this.detectCount_;
    }

    /**
     * ���[�N���o�񐔂�ݒ肷��B
     * @param detectCount ���[�N���o��
     */
    public void setDetectCount(final int detectCount)
    {
        this.detectCount_ = detectCount;
    }

    /**
     * �X�^�b�N�g���[�X�擾�񐔂��擾����B
     * @return �X�^�b�N�g���[�X�擾��
     */
    public int getTraceCount()
    {
        return this.traceSet_.size();
    }

    /**
     * �ۑ����Ă���X�^�b�N�g���[�X�̐擪�v�f���폜����B
     */
    public void removeTrace()
    {
        Integer traceHashCode = this.traceSet_.iterator().next();
        this.traceSet_.remove(traceHashCode);
    }

    /**
     * �X�^�b�N�g���[�X��hashCode��ۑ�����B
    * @param hashCode �X�^�b�N�g���[�X��hashCode�B
     */
    public void addTrace(final int hashCode)
    {
        this.traceSet_.add(hashCode);
    }

    /**
     * �ۑ����Ă���X�^�b�N�g���[�X�̂��폜����B
     * @param hashCode �X�^�b�N�g���[�X��hashCode�B
     */
    public void removeTrace(final int hashCode)
    {
        this.traceSet_.remove(Integer.valueOf(hashCode));
    }
    
    /**
     * �w�肵���X�^�b�N�g���[�X�����ɕۑ�����Ă���X�^�b�N�g���[�X��
     * ��v������̂����邩�ǂ����𔻒肷��B
     * 
     * @param hashCode �X�^�b�N�g���[�X��hashCode�B
     * @return ��v������̂����邩�ǂ����B
     */
    public boolean containsTrace(final int hashCode)
    {
        return this.traceSet_.contains(hashCode);
    }

    /**
     * �Ď��Ώۂ̃R���N�V�����I�u�W�F�N�g��Ԃ��܂��B<br />
     *
     * �R���N�V�������Ď����Ă��Ȃ��A�������͊Ď��ΏۃR���N�V���������ł� GC �ŉ������Ă���ꍇ��
     * <code>null</code> ��Ԃ��܂��B<br />
     *
     * @return �Ď��Ώۂ̃R���N�V����
     */
    public Collection<?> getCollection()
    {
        if (this.targetCollection_ != null)
        {
            return this.targetCollection_.get();
        }
        return null;
    }

    /**
     * �Ď��Ώۂ̃}�b�v�I�u�W�F�N�g��Ԃ��܂��B<br />
     *
     * �}�b�v���Ď����Ă��Ȃ��A�������͊Ď��Ώۃ}�b�v�����ł� GC �ŉ������Ă���ꍇ��
     * <code>null</code> ��Ԃ��܂��B<br />
     *
     * @return �Ď��Ώۂ̃}�b�v
     */
    public Map<?, ?> getMap()
    {
        if (this.targetMap_ != null)
        {
            return this.targetMap_.get();
        }
        return null;
    }

    /**
     * �Ώۂ̃R���N�V�����A�}�b�v���܂�GC�ɂ��������Ă��Ȃ������擾����B
     * 
     * @return �Ώۂ̃R���N�V�����A�}�b�v���܂�GC�ɂ��������Ă��Ȃ����B
     */
    public boolean exists()
    {
        return (this.targetCollection_ != null && this.targetCollection_.get() != null)
                || (this.targetMap_ != null && this.targetMap_.get() != null);
    }

    /**
     * ���o���̗v�f����ݒ肷��B
     * @param detectedSize ���o���̗v�f��
     */
    public void setDetectedSize(int detectedSize)
    {
        detectedSize_ = detectedSize;
    }

    /**
     * ���o���̗v�f����ݒ肷��B
     * @return ���o���̗v�f��
     */
    public int getDetectedSize()
    {
        return detectedSize_;
    }

    /**
     * �ۑ����Ă���X�^�b�N�g���[�X���폜����B
     */
    public void clearAllTrace()
    {
        this.traceSet_.clear();
    }
}
