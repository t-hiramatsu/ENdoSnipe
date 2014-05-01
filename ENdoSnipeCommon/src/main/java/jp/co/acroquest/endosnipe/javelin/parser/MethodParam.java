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
package jp.co.acroquest.endosnipe.javelin.parser;

import java.util.Map;

/**
 * �����l��ێ����郁�\�b�h���
 *
 * @author Sakamoto
 */
public class MethodParam
{
    /** ���\�b�h��� */
    private JavelinLogElement javelinLogElement_;

    /** ���\�b�h���s���� */
    private long duration_;

    /** ���O�ɏo�͂���Ă���l */
    private Map<String, Double> originalDataMap_;

    /** ���\�b�h�����l */
    private Map<String, Double> pureDataMap_;

    /** ���\�b�h�J�n���� */
    private long startTime_;

    /** ���\�b�h�I������ */
    private long endTime_;

    /**
     * ���\�b�h�P�̏���\���I�u�W�F�N�g���쐬����B
     */
    public MethodParam()
    {
        //doNothing
    }

    /**
     * JavelinLogElement���擾����B
     *
     * @return JavelinLogElement
     */
    public JavelinLogElement getJavelinLogElement()
    {
        return this.javelinLogElement_;
    }

    /**
     * ���\�b�h���s���Ԃ��擾����B
     *
     * @return ���\�b�h���s����
     */
    public long getDuration()
    {
        return this.duration_;
    }

    /**
     * ���\�b�h�J�n�������擾����B
     *
     * @return ���\�b�h�J�n����
     */
    public long getStartTime()
    {
        return this.startTime_;
    }

    /**
     * ���\�b�h�I���������擾����B
     *
     * @return ���\�b�h�I������
     */
    public long getEndTime()
    {
        return this.endTime_;
    }

    /**
     * ���O�ɏo�͂���Ă���l���擾����B
     *
     * @return ���O�ɏo�͂���Ă���l�̃}�b�v
     */
    public Map<String, Double> getOriginalDataMap()
    {
        return this.originalDataMap_;
    }

    /**
     * ���\�b�h�̏����l���擾����B
     *
     * @return ���\�b�h�̏����l�̃}�b�v
     */
    public Map<String, Double> getPureDataMap()
    {
        return this.pureDataMap_;
    }

    /**
     * JavelinLogElement��ݒ肵�܂��B<br />
     * 
     * @param javelinLogElement {@link JavelinLogElement}�I�u�W�F�N�g
     */
    public void setJavelinLogElement(final JavelinLogElement javelinLogElement)
    {
        this.javelinLogElement_ = javelinLogElement;
    }

    /**
     * Duration��ݒ肵�܂��B<br />
     * 
     * @param duration Duration
     */
    public void setDuration(final long duration)
    {
        this.duration_ = duration;
    }

    /**
     * Javelin���O�ɏo�͂��ꂽ�l��Map��ݒ肵�܂��B<br />
     * 
     * @param originalDataMap Javelin���O�ɏo�͂��ꂽ�l��Map
     */
    public void setOriginalDataMap(final Map<String, Double> originalDataMap)
    {
        this.originalDataMap_ = originalDataMap;
    }

    /**
     * �����l��ۑ�����Map��ݒ肵�܂��B<br />
     * 
     * @param pureDataMap �����l��ۑ�����Map
     */
    public void setPureDataMap(final Map<String, Double> pureDataMap)
    {
        this.pureDataMap_ = pureDataMap;
    }

    /**
     * �J�n������ݒ肵�܂��B<br />
     * 
     * @param startTime �J�n����
     */
    public void setStartTime(final long startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * �I��������ݒ肵�܂��B<br />
     * 
     * @param endTime �I������
     */
    public void setEndTime(final long endTime)
    {
        this.endTime_ = endTime;
    }

    /**
     * �����l����q���\�b�h�̒l�������B
     *
     * @param childMethod �q���\�b�h
     */
    public void subtractData(final MethodParam childMethod)
    {
        for (Map.Entry<String, Double> entrySet : childMethod.getOriginalDataMap().entrySet())
        {
            String key = entrySet.getKey();
            if (this.pureDataMap_.containsKey(key) == true)
            {
                double parentValue = this.pureDataMap_.get(key);
                double childValue = entrySet.getValue();
                this.pureDataMap_.put(key, parentValue - childValue);
            }
        }
    }
}
