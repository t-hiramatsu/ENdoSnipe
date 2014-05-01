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
 * ���[����`�N���X�B
 * @author tanimoto
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RuleDef implements Serializable
{
    /** �V���A��ID */
    private static final long  serialVersionUID = 1L;

    /** ���[��ID */
    @XmlAttribute
    private String             id_;

    /** ���[���̃N���X�� */
    @XmlAttribute
    private String             className_;

    /** ���[�����L�����ǂ��� */
    @XmlAttribute
    private String             enabled_;

    /** ���x���ʒ�` */
    @XmlElement(name = "ruleLevel")
    private List<RuleLevelDef> ruleLevelDefs_;

    /**
     * �f�t�H���g�R���X�g���N�^�B<br />
     */
    public RuleDef()
    {
        // Do nothing.
    }

    /**
     * �R�s�[�R���X�g���N�^�B<br />
     *
     * @param ruleDef �R�s�[��
     */
    public RuleDef(final RuleDef ruleDef)
    {
        this.id_ = ruleDef.id_;
        this.className_ = ruleDef.className_;
        this.enabled_ = ruleDef.enabled_;
        if (ruleDef.ruleLevelDefs_ != null)
        {
            this.ruleLevelDefs_ = new ArrayList<RuleLevelDef>();
            for (RuleLevelDef ruleLevelDef : ruleDef.ruleLevelDefs_)
            {
                this.ruleLevelDefs_.add(new RuleLevelDef(ruleLevelDef));
            }
        }
    }

    /**
     * ���[���̃N���X�����擾����B
     * @return ���[���̃N���X��
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * ���[���̃N���X����ݒ肷��B
     * @param className ���[���̃N���X��
     */
    public void setClassName(final String className)
    {
        this.className_ = className;
    }

    /**
     * ���[�����L�����ǂ������擾����B
     * @return ���[�����L�����ǂ���
     */
    public String getEnabled()
    {
        return this.enabled_;
    }

    /**
     * ���[�����L�����ǂ�����ݒ肷��B
     * @param enabled ���[�����L�����ǂ���
     */
    public void setEnabled(final String enabled)
    {
        this.enabled_ = enabled;
    }

    /**
     * ���[��ID���擾����B
     * @return ���[��ID
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * ���[��ID��ݒ肷��B
     * @param id ���[��ID
     */
    public void setId(final String id)
    {
        this.id_ = id;
    }

    /**
     * ���x���ʒ�`���擾����B
     * @return ruleLevelDefs ���x���ʒ�`
     */
    public List<RuleLevelDef> getRuleLevelDefs()
    {
        return this.ruleLevelDefs_;
    }

    /**
     * ���x���ʒ�`��ݒ肷��B
     * @param ruleLevelDefs ���x���ʒ�`
     */
    public void setRuleLevelDefs(final List<RuleLevelDef> ruleLevelDefs)
    {
        this.ruleLevelDefs_ = ruleLevelDefs;
    }
}
