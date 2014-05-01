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
package jp.co.acroquest.endosnipe.data.entity;

/**
 * �v���l���e�[�u���ɑ΂���C�~���[�^�u���ȃG���e�B�e�B�N���X�ł��B<br />
 *
 * @author y-sakamoto
 */
public class MeasurementInfo
{

    /**
     * �v���l��ʂ�\�� ID �B<br />
     *
     * JAVELIN_MESUREMENT �e�[�u���� MESUREMENT_TYPE �J�����ɗ��p�����l�B
     */
    private final long measurementType_;

    /**
     * ���\�[�X�ʒm�d���̍��ږ��B<br />
     */
    private final String itemName_;

    /**
     * �v���l�̕\�����́B<br />
     *
     * BottleneckEye �Ȃǂ̉�̓A�v���P�[�V�����ŕ\������Ƃ��ɗ��p���܂��B
     */
    private final String displayName_;

    /**
     * �v���l�Ɋւ�������B<br />
     *
     * ��̓A�v���P�[�V�����Ōv���l�̏ڍא�����\������Ƃ��ɗ��p���܂��B
     */
    private final String description_;

    /**
     * �v���l���e�[�u���ɑ΂���G���e�B�e�B�I�u�W�F�N�g�𐶐����܂��B<br />
     *
     * @param measurementType �v���l���
     * @param itemName ���\�[�X�ʒm�d���̍��ږ�
     * @param displayName �v���l�̕\������
     * @param description �v���l�Ɋւ������
     */
    public MeasurementInfo(final long measurementType, final String itemName,
            final String displayName, final String description)
    {
        this.measurementType_ = measurementType;
        this.itemName_ = itemName;
        this.displayName_ = displayName;
        this.description_ = description;
    }

    /**
     * �v���l��ʂ�Ԃ��܂��B<br />
     *
     * @return �v���l���
     */
    public long getMeasurementType()
    {
        return this.measurementType_;
    }

    /**
     * ���\�[�X�ʒm�d���̍��ږ���Ԃ��܂��B<br />
     *
     * @return ���\�[�X�ʒm�d���̍��ږ�
     */
    public String getItemName()
    {
        return this.itemName_;
    }

    /**
     * �v���l�̕\�����̂�Ԃ��܂��B<br />
     *
     * @return �v���l�̕\������
     */
    public String getDisplayName()
    {
        return this.displayName_;
    }

    /**
     * �v���l�Ɋւ��������Ԃ��܂��B<br />
     *
     * @return �v���l�Ɋւ������
     */
    public String getDescription()
    {
        return this.description_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{measurementType=");
        builder.append(this.measurementType_);
        builder.append(",itemName=");
        builder.append(this.itemName_);
        builder.append("}");
        return builder.toString();
    }
}
