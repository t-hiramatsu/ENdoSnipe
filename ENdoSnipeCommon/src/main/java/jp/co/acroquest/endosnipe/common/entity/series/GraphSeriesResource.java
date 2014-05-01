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
package jp.co.acroquest.endosnipe.common.entity.series;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * �O���t�� 1 �n���\���N���X�B<br />
 *
 * @author sakamoto
 */
public class GraphSeriesResource implements Iterable<GraphResourceEntry>
{

    /** �n�� */
    private final String seriesName_;

    private final List<GraphResourceEntry> entries_;

    /**
     * �O���t�� 1 �n���\���I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param seriesName �n��
     */
    public GraphSeriesResource(final String seriesName)
    {
        this.seriesName_ = seriesName;
        this.entries_ = new ArrayList<GraphResourceEntry>();
    }

    /**
     * �O���t�� 1 �n���\���I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param seriesName �n��
     * @param entryList �n��f�[�^
     */
    public GraphSeriesResource(final String seriesName, final List<GraphResourceEntry> entryList)
    {
        this.seriesName_ = seriesName;
        this.entries_ = new ArrayList<GraphResourceEntry>(entryList);
    }

    /**
     * �n��ɒl�� 1 �ǉ����܂��B<br />
     *
     * @param entry �ǉ�����l
     */
    public void addGraphResourceEntry(final GraphResourceEntry entry)
    {
        this.entries_.add(entry);
    }

    /**
     * �n��ɒl�� 1 �ǉ����܂��B<br />
     *
     * @param time �ǉ�����l���擾���ꂽ�����i�~���b�j
     * @param value �ǉ�����l
     */
    public void addGraphResourceEntry(final long time, final Number value)
    {
        GraphResourceEntry entry = new GraphResourceEntry(time, value);
        addGraphResourceEntry(entry);
    }

    /**
     * �n�񖼂�Ԃ��܂��B<br />
     *
     * @return �n��
     */
    public String getSeriesName()
    {
        return this.seriesName_;
    }

    /**
     * �n��ɑ��݂���l�̐���Ԃ��܂��B<br />
     *
     * @return �l�̐�
     */
    public int getValueCount()
    {
        return this.entries_.size();
    }

    /**
     * �n����̃f�[�^�̃��X�g��Ԃ��܂��B<br />
     *
     * �Ԃ��ꂽ�f�[�^�͎Q�Ƃ̂݉\�ŁA�ǉ���폜�͍s���܂���B<br />
     *
     * @return �n����̃f�[�^�̃��X�g
     */
    public List<GraphResourceEntry> getEntryList()
    {
        return Collections.unmodifiableList(this.entries_);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<GraphResourceEntry> iterator()
    {
        return this.entries_.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{seriesName=");
        builder.append(getSeriesName());
        builder.append(",dataCount=");
        builder.append(this.entries_.size());
        builder.append("}");
        return builder.toString();
    }
}
