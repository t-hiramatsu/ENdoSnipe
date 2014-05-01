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

import java.util.Arrays;

import jp.co.acroquest.endosnipe.common.entity.ItemType;

/**
 * �d���{�̂̂��߂̃G���e�B�e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class Body
{
    /** �I�u�W�F�N�g�� */
    private String strObjName_ = "";

    /** �I�u�W�F�N�g�̕\���� */
    private String strObjDispName_ = "";

    /** ���ږ� */
    private String strItemName_ = "";

    /** ���ڌ^ */
    private ItemType byteItemType_ = ItemType.ITEMTYPE_BYTE;

    /** �J��Ԃ��� */
    private int intLoopCount_ = 0;

    /** ���� */
    private Object[] objItemValueArr_ = null;

    /**
     * �I�u�W�F�N�g�����擾���܂��B<br />
     * 
     * @return �I�u�W�F�N�g��
     */
    public String getStrObjName()
    {
        return this.strObjName_;
    }

    /**
     * �I�u�W�F�N�g����ݒ肵�܂��B<br />
     * 
     * @param strObjName �I�u�W�F�N�g��
     */
    public void setStrObjName(final String strObjName)
    {
        this.strObjName_ = strObjName;
    }

    /**
     * ���ږ����擾���܂��B<br />
     * 
     * @return ���ږ�
     */
    public String getStrItemName()
    {
        return this.strItemName_;
    }

    /**
     * ���ږ���ݒ肵�܂��B<br />
     * 
     * @param strItemName ���ږ�
     */
    public void setStrItemName(final String strItemName)
    {
        this.strItemName_ = strItemName;
    }

    /**
     * ���ڌ^���擾���܂��B<br />
     * 
     * @return ���ڌ^
     */
    public ItemType getByteItemMode()
    {
        return this.byteItemType_;
    }

    /**
     * ���ڌ^��ݒ肵�܂��B<br />
     * 
     * @param byteItemMode ���ڌ^
     */
    public void setByteItemMode(final ItemType byteItemMode)
    {
        this.byteItemType_ = byteItemMode;
    }

    /**
     * �J��Ԃ��񐔂��擾���܂��B<br />
     * 
     * @return �J��Ԃ���
     */
    public int getIntLoopCount()
    {
        return this.intLoopCount_;
    }

    /**
     * �J��Ԃ��񐔂�ݒ肵�܂��B<br />
     * 
     * @param intLoopCount �J��Ԃ���
     */
    public void setIntLoopCount(final int intLoopCount)
    {
        this.intLoopCount_ = intLoopCount;
    }

    /**
     * �������擾���܂��B<br />
     * 
     * @return ����
     */
    public Object[] getObjItemValueArr()
    {
        return this.objItemValueArr_;
    }

    /**
     * ������ݒ肵�܂��B<br />
     * 
     * @param objItemValueArr ����
     */
    public void setObjItemValueArr(final Object[] objItemValueArr)
    {
        this.objItemValueArr_ = objItemValueArr;
    }

    /**
     * �I�u�W�F�N�g�̕\�������擾���܂��B
     * 
     * @return �I�u�W�F�N�g�̕\����
     */
    public String getStrObjDispName()
    {
        return strObjDispName_;
    }

    /**
     * �I�u�W�F�N�g�̕\������ݒ肵�܂��B
     * 
     * @param strObjDispName �I�u�W�F�N�g�̕\����
     */
    public void setStrObjDispName(String strObjDispName)
    {
        strObjDispName_ = strObjDispName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemName:" + this.strItemName_);
        builder.append(", ObjectName:" + this.strObjName_);
        builder.append(", ObjectDisplayName:" + this.strObjDispName_);
        builder.append(Arrays.toString(getObjItemValueArr()));
        return builder.toString();
    }
}
