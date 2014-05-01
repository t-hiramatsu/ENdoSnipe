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
package jp.co.acroquest.endosnipe.perfdoctor.rule.def;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * �v���p�e�B��`�N���X�B
 * @author tanimoto
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class PropertyDef implements Serializable
{
    /** �V���A��ID */
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private String            name_;

    @XmlValue
    private String            value_;

    /**
     * �f�t�H���g�R���X�g���N�^�B<br />
     */
    public PropertyDef()
    {
        // Do nothing.
    }

    /**
     * �R�s�[�R���X�g���N�^�B<br />
     *
     * @param propertyDef �R�s�[��
     */
    public PropertyDef(final PropertyDef propertyDef)
    {
        this.name_ = propertyDef.name_;
        this.value_ = propertyDef.value_;
    }

    /**
     * �v���p�e�B�����擾����B
     * @return �v���p�e�B��
     */
    public String getName()
    {
        return this.name_;
    }

    /**
     * �v���p�e�B����ݒ肷��B
     * @param name �v���p�e�B��
     */
    public void setName(final String name)
    {
        this.name_ = name;
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
     * �l��ݒ肷��B
     * @param value �l
     */
    public void setValue(final String value)
    {
        this.value_ = value;
    }
}
