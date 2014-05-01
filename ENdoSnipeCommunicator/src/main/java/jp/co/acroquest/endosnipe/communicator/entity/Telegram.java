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
 * �d���f�[�^�̂��߂̃G���e�B�e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class Telegram implements Cloneable
{
    /** �d���w�b�_ */
    private Header objHeader_ = null;

    /** �d���{�� */
    private Body[] objBody_ = null;

    /**
     * �d���w�b�_���擾���܂��B<br />
     * 
     * @return �d���w�b�_
     */
    public Header getObjHeader()
    {
        return objHeader_;
    }

    /**
     * �d���w�b�_��ݒ肵�܂��B<br />
     * 
     * @param objHeader �d���w�b�_
     */
    public void setObjHeader(final Header objHeader)
    {
        this.objHeader_ = objHeader;
    }

    /**
     * �d���{�̂��擾���܂��B<br />
     * 
     * @return �d���{��
     */
    public Body[] getObjBody()
    {
        return objBody_;
    }

    /**
     * �d���{�̂�ݒ肵�܂��B<br />
     * 
     * @param objBody �d���{��
     */
    public void setObjBody(final Body[] objBody)
    {
        this.objBody_ = objBody;
    }

    /**
     * �d���{�̂�ǉ����܂��B<br />
     * 
     * @param objBody �ǉ�����d���{��
     */
    public void addObjBody(final Body objBody)
    {
        int oldLength = this.objBody_.length;
        Body[] newBody = new Body[oldLength + 1];
        System.arraycopy(this.objBody_, 0, newBody, 0, oldLength);
        newBody[oldLength] = objBody;
        this.objBody_ = newBody;
    }

    /**
     * �I�u�W�F�N�g���R�s�[���郁�\�b�h�ł��B<br />
     * 
     * @return �R�s�[���{@link Telegram}�I�u�W�F�N�g
     * @throws CloneNotSupportedException �N���[�����T�|�[�g����Ă��Ȃ��ꍇ
     */
    public Telegram clone()
        throws CloneNotSupportedException
    {
        return (Telegram)super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.objHeader_.toString());
        builder.append('\n');
        for (Body body : this.objBody_)
        {
            builder.append(body);
            builder.append(", ");
        }

        return builder.toString();

    }
}
