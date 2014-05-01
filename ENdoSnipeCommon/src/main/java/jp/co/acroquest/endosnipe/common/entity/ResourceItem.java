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
package jp.co.acroquest.endosnipe.common.entity;

/**
 * ���\�[�X���ƒl��ۑ�����N���X
 *
 * @author eriguchi
 */
public class ResourceItem
{
    /** ���\�[�X��(JMX�v���l�̏ꍇ��Attribute��) */
    private String      name_              = "";

    /** JMX�v���l���擾�����ꍇ�̃I�u�W�F�N�g�� */
    private String      objectName_        = "";

    /** �v���l�̌^ */
    private ItemType    itemType_;

    /** �v���l */
    private String      value_             = null;

    /** �I�u�W�F�N�g�̕\���� */
    private String      objectDisplayNeme_ = "";

    /** attribute�̕\���� */
    private String      displayName_       = "";

    /** �v���l�̕\���^ */
    private DisplayType displayType_;

    /**
     * ���\�[�X����ݒ肷��B
     * @param name ���\�[�X��
     */
    public void setName(final String name)
    {
        this.name_ = name;
    }

    /**
     * ���\�[�X�����擾����B
     * @return ���\�[�X��
     */
    public String getName()
    {
        return this.name_;
    }

    /**
     * MBean�����擾���܂��B
     *
     * @return MBean��
     */
    public String getObjectName()
    {
        return objectName_;
    }

    /**
     * MBean����ݒ肵�܂��B
     *
     * @param objectName MBean��
     */
    public void setObjectName(final String objectName)
    {
        objectName_ = objectName;
    }

    /**
     * �l��ݒ肷��B
     * @param value �l
     */
    public void setValue(final String value)
    {
        this.value_ = value;
    }

    /**
     * �l���擾����B
     * @return �l
     */
    public String getValue()
    {
        return this.value_;
    }

    /**
     * ���\�[�X�l�̌^��Ԃ��B
     *
     * @return �^
     */
    public ItemType getItemType()
    {
        return this.itemType_;
    }

    /**
     * ���\�[�X�l�̌^��ݒ肷��B
     *
     * @param itemType �^
     */
    public void setItemType(final ItemType itemType)
    {
        itemType_ = itemType;
    }

    /**
     * �I�u�W�F�N�g�̕\�������擾����B
     *
     * @return �I�u�W�F�N�g�̕\����
     */
    public String getObjectDisplayNeme()
    {
        return objectDisplayNeme_;
    }

    /**
     * �I�u�W�F�N�g�̕\������ݒ肷��B
     *
     * @param objectDisplayNeme �I�u�W�F�N�g�̕\����
     */
    public void setObjectDisplayNeme(final String objectDisplayNeme)
    {
        objectDisplayNeme_ = objectDisplayNeme;
    }

    /**
     * �\�������擾����B
     *
     * @return �\����
     */
    public String getDisplayName()
    {
        return displayName_;
    }

    /**
     * �\������ݒ肷��B
     *
     * @param displayName �\����
     */
    public void setDisplayName(final String displayName)
    {
        displayName_ = displayName;
    }

    /**
     * �\���^���擾����B
     * @return �\���^
     */
    public DisplayType getDisplayType()
    {
        return displayType_;
    }

    /**
     * �\���^���擾����B
     * @param displayType �\���^
     */
    public void setDisplayType(final DisplayType displayType)
    {
        displayType_ = displayType;
    }

    /**
     * �������Ԃ��B
     *
     * @return ������
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(this.name_);
        builder.append("=(");
        builder.append(this.value_);
        builder.append(")");

        return builder.toString();
    }
}
