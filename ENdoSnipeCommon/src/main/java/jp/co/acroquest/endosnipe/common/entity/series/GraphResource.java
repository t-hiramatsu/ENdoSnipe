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
import java.util.Iterator;
import java.util.List;

/**
 * ����̃O���t�̃��\�[�X�l��\���N���X�B<br />
 *
 * �O���t���̌n��̒l�����ׂĎ����܂��B<br />
 *
 * @author sakamoto
 */
public class GraphResource implements Iterable<GraphSeriesResource>
{
    private final String graphName_;

    private final List<GraphSeriesResource> seriesList_;

    private Number maxValue_;

    /**
     * ����̃O���t���̂��ׂĂ̒l�����I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param graphName �O���t��
     */
    public GraphResource(final String graphName)
    {
        this.graphName_ = graphName;
        this.maxValue_ = null;
        this.seriesList_ = new ArrayList<GraphSeriesResource>();
    }

    /**
     * �O���t�̖��O��Ԃ��܂��B<br />
     *
     * @return �O���t�̖��O
     */
    public String getGraphName()
    {
        return this.graphName_;
    }

    /**
     * �c���̍ő�l��Ԃ��܂��B<br />
     *
     * @return �c���̍ő�l
     */
    public Number getMaxValue()
    {
        return this.maxValue_;
    }

    /**
     * �c���̍ő�l���Z�b�g���܂��B<br />
     *
     * @param maxValue �c���̍ő�l
     */
    public void setMaxValue(final Number maxValue)
    {
        this.maxValue_ = maxValue;
    }

    /**
     * �n�񐔂�Ԃ��܂��B<br />
     *
     * @return �n��
     */
    public int getSeriesCount()
    {
        return this.seriesList_.size();
    }

    /**
     * �O���t�ɒl��ǉ����܂��B<br />
     *
     * @param seriesName �n��
     * @param entry �ǉ�����l
     */
    public void addGraphResourceEntry(final String seriesName, final GraphResourceEntry entry)
    {
        GraphSeriesResource series = getSeries(seriesName);
        if (series == null)
        {
            series = new GraphSeriesResource(seriesName);
            this.seriesList_.add(series);
        }
        series.addGraphResourceEntry(entry);
    }

    /**
     * �w�肳�ꂽ�n��̃O���t�ɒl��ǉ����܂��B<br />
     *
     * @param seriesIndex �n��ԍ��i�n��ԍ����n�񐔈ȏ�̏ꍇ�A�Ԃɋ�̌n���}������j
     * @param entry �ǉ�����l
     */
    public void addGraphResourceEntry(final int seriesIndex, final GraphResourceEntry entry)
    {
        while (this.seriesList_.size() <= seriesIndex)
        {
            this.seriesList_.add(new GraphSeriesResource(null));
        }
        GraphSeriesResource series = this.seriesList_.get(seriesIndex);
        series.addGraphResourceEntry(entry);
    }

    /**
     * �n���ǉ����܂��B<br />
     *
     * @param series �n��
     */
    public void addSeries(final GraphSeriesResource series)
    {
        this.seriesList_.add(series);
    }

    /**
     * �n��̒l��Ԃ��܂��B<br />
     *
     * @param seriesName �n��
     * @return �n��̒l�i�w�肳�ꂽ�n�񂪑��݂��Ȃ��ꍇ�� <code>null</code> �j
     */
    public GraphSeriesResource getSeries(final String seriesName)
    {
        for (GraphSeriesResource series : this.seriesList_)
        {
            if (seriesName == null)
            {
                if (series.getSeriesName() == null)
                {
                    return series;
                }
            }
            else if (seriesName.equals(series.getSeriesName()))
            {
                return series;
            }
        }
        return null;
    }

    /**
     * �C���f�b�N�X���w�肵�Čn��̒l��Ԃ��܂��B<br />
     *
     * @param index �C���f�b�N�X
     * @return �ŐV�̃f�[�^�̎���
     */
    public GraphSeriesResource getSeries(final int index)
    {
        return this.seriesList_.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<GraphSeriesResource> iterator()
    {
        return this.seriesList_.iterator();
    }

}
