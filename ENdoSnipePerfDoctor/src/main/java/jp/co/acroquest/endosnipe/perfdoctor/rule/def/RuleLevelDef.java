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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * ���x�����Ƃ̃v���p�e�B��`�N���X�B
 * @author tanimoto
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RuleLevelDef implements Serializable
{
    /** �V���A��ID */
    private static final long serialVersionUID = 1L;

    /** ���[���̖�背�x�� */
    @XmlAttribute
    private String            level_;

    /** ���x�����L�����ǂ��� */
    @XmlAttribute
    private String            enabled_;

    /** duration��臒l */
    @XmlAttribute
    private String            durationThreshold_;

    /** �v���p�e�B��`�ꗗ */
    @XmlElement(name = "property")
    private List<PropertyDef> propertyDefs_;

    /**
     * �f�t�H���g�R���X�g���N�^�B<br />
     */
    public RuleLevelDef()
    {
        // Do nothing.
    }

    /**
     * �R�s�[�R���X�g���N�^�B<br />
     *
     * @param ruleLevelDef �R�s�[��
     */
    public RuleLevelDef(final RuleLevelDef ruleLevelDef)
    {
        this.level_ = ruleLevelDef.level_;
        this.enabled_ = ruleLevelDef.enabled_;
        this.durationThreshold_ = ruleLevelDef.durationThreshold_;
        if (ruleLevelDef.propertyDefs_ != null)
        {
            this.propertyDefs_ = new ArrayList<PropertyDef>();
            for (PropertyDef propertyDef : ruleLevelDef.propertyDefs_)
            {
                this.propertyDefs_.add(new PropertyDef(propertyDef));
            }
        }
    }

    /**
     * duration��臒l���擾����B
     * @return duration��臒l
     */
    public String getDurationThreshold()
    {
        return this.durationThreshold_;
    }

    /**
     * duration��臒l��ݒ肷��B
     * @param durationThreshold duration��臒l
     */
    public void setDurationThreshold(final String durationThreshold)
    {
        this.durationThreshold_ = durationThreshold;
    }

    /**
     * ���x�����L�����ǂ������擾����B
     * @return ���x�����L�����ǂ���
     */
    public String getEnabled()
    {
        return this.enabled_;
    }

    /**
     * ���x�����L�����ǂ�����ݒ肷��B
     * @param enabled ���x�����L�����ǂ���
     */
    public void setEnabled(final String enabled)
    {
        this.enabled_ = enabled;
    }

    /**
     * ���[���̖�背�x�����擾����B
     * @return ���[���̖�背�x��
     */
    public String getLevel()
    {
        return this.level_;
    }

    /**
     * ���[���̖�背�x����ݒ肷��B
     * @param level ���[���̖�背�x��
     */
    public void setLevel(final String level)
    {
        this.level_ = level;
    }

    /**
     * �v���p�e�B��`�ꗗ���擾����B
     * @return propertyDefs
     */
    public List<PropertyDef> getPropertyDefs()
    {
        return this.propertyDefs_;
    }

    /**
     * �v���p�e�B��`�ꗗ��ݒ肷��B
     * @param propertyDefs �v���p�e�B��`�ꗗ
     */
    public void setPropertyDefs(final List<PropertyDef> propertyDefs)
    {
        this.propertyDefs_ = propertyDefs;
    }
}
