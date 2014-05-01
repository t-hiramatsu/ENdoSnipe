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

/**
 * �O���t�ɕ\�����郊�\�[�X�́@1�@�̓_��\���܂��B<br />
 *
 * �_�̏��́A�����ƒl����\������܂��B<br />
 *
 * @author eriguchi
 */
public class GraphResourceEntry
{
    /** ���̒l���擾���ꂽ�����i�~���b�j */
    private final long time_;

    /** �l */
    private final Number value_;

    /**
     * �O���t�ɕ\�����郊�\�[�X�� 1 �̓_�𐶐����܂��B<br />
     *
     * @param time ���̒l���擾���ꂽ�����i�~���b�j
     * @param value �l
     */
    public GraphResourceEntry(final long time, final Number value)
    {
        this.time_ = time;
        this.value_ = value;
    }

    /**
     * {@link GraphResourceEntry} �̃R�s�[�R���X�g���N�^�ł��B<br />
     *
     * @param entry �R�s�[����I�u�W�F�N�g
     */
    public GraphResourceEntry(final GraphResourceEntry entry)
    {
        this.time_ = entry.time_;
        this.value_ = entry.value_;
    }

    /**
     * ���̒l���擾���ꂽ�������~���b�ŕԂ��܂��B<br />
     *
     * @return ����
     */
    public long getTime()
    {
        return this.time_;
    }

    /**
     * �l���擾����B
     * 
     * @return �l�B
     */
    public Number getValue()
    {
        return this.value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{time=");
        builder.append(getTime());
        builder.append(",value=");
        builder.append(getValue());
        builder.append("}");
        return builder.toString();
    }

}
