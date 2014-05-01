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
package jp.co.acroquest.endosnipe.communicator.entity;

/**
 * �d���w�b�_�̂��߂̃G���e�B�e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class Header
{
    /** �w�b�_�� */
    public static final int HEADER_LENGTH = 18;

    /** �d���� */
    private int intSize_ = 0;

    /** �d��ID */
    private long id_ = 0;

    /** �I���t���O(0:�I���łȂ� 1:�I��) */
    private byte lastTelegram_ = 0;

    /** �d����� */
    private byte byteTelegramKind_ = 0;

    /** �v��������� */
    private byte byteRequestKind_ = 0;

    /**
     * �d�������擾���܂��B<br />
     * 
     * @return �d����
     */
    public int getIntSize()
    {
        return intSize_;
    }

    /**
     * �d������ݒ肵�܂��B<br />
     * 
     * @param intSize �d����
     */
    public void setIntSize(final int intSize)
    {
        this.intSize_ = intSize;
    }

    /**
     * �d�� ID ���擾���܂��B
     *
     * @return �d�� ID
     */
    public long getId()
    {
        return this.id_;
    }

    /**
     * �d�� ID ��ݒ肵�܂��B
     *
     * @param id �d�� ID
     */
    public void setId(final long id)
    {
        this.id_ = id;
    }

    
    /**
     * �I���t���O���擾���܂��B
     *
     * @return �I���t���O
     */
    public byte getLastTelegram()
    {
        return lastTelegram_;
    }

    /**
     * �I���t���O��ݒ肵�܂��B
     *
     * @param lastTelegram �I���t���O
     */
    public void setLastTelegram(byte lastTelegram)
    {
        lastTelegram_ = lastTelegram;
    }

    /**
     * �d����ʂ��擾���܂��B<br />
     * 
     * @return �d�����
     */
    public byte getByteTelegramKind()
    {
        return byteTelegramKind_;
    }

    /**
     * �d����ʂ�ݒ肵�܂��B<br />
     * 
     * @param byteTelegramKind �d�����
     */
    public void setByteTelegramKind(final byte byteTelegramKind)
    {
        this.byteTelegramKind_ = byteTelegramKind;
    }

    /**
     * �v��������ʂ��擾���܂��B<br />
     * 
     * @return �v���������
     */
    public byte getByteRequestKind()
    {
        return byteRequestKind_;
    }

    /**
     * �v��������ʂ�ݒ肵�܂��B<br />
     * 
     * @param byteRequestKind �v���������
     */
    public void setByteRequestKind(final byte byteRequestKind)
    {
        this.byteRequestKind_ = byteRequestKind;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Id=");
        builder.append(getId());
        builder.append(",RequestKind=");
        builder.append(getByteRequestKind());
        builder.append(",TelegramKind=");
        builder.append(getByteTelegramKind());
        builder.append(",IntSize=");
        builder.append(getIntSize());
        return builder.toString();
    }

}
