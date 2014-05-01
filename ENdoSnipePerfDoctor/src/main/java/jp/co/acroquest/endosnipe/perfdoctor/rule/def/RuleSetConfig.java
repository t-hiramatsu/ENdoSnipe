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

/**
 * ���[���Z�b�g�ݒ�N���X�B<br>
 * @author tanimoto
 *
 */
public class RuleSetConfig implements Serializable
{
    /** �V���A��ID */
    private static final long serialVersionUID = 1L;

    /** ���[���Z�b�g��ID�B */
    private String            id_;

    /** ���[���Z�b�g���B */
    private String            name_;

    /** ���[���Z�b�g��`�̃t�@�C�����B */
    private String            fileName_;

    /**
     * fileName_���擾����B
     * @return fileName
     */
    public String getFileName()
    {
        return this.fileName_;
    }

    /**
     * fileName��ݒ肷��B
     * @param fileName fileName
     */
    public void setFileName(final String fileName)
    {
        this.fileName_ = fileName;
    }

    /**
     * id_���擾����B
     * @return id
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * id��ݒ肷��B
     * @param id id
     */
    public void setId(final String id)
    {
        this.id_ = id;
    }

    /**
     * name_���擾����B
     * @return name
     */
    public String getName()
    {
        return this.name_;
    }

    /**
     * name��ݒ肷��B
     * @param name name
     */
    public void setName(final String name)
    {
        this.name_ = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.fileName_ == null) ? 0 : this.fileName_.hashCode());
        result = PRIME * result + ((this.id_ == null) ? 0 : this.id_.hashCode());
        result = PRIME * result + ((this.name_ == null) ? 0 : this.name_.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        RuleSetConfig other = (RuleSetConfig)obj;
        if (this.fileName_ == null)
        {
            if (other.fileName_ != null)
            {
                return false;
            }
        }
        else if (!this.fileName_.equals(other.fileName_))
        {
            return false;
        }
        if (this.id_ == null)
        {
            if (other.id_ != null)
            {
                return false;
            }
        }
        else if (!this.id_.equals(other.id_))
        {
            return false;
        }
        if (this.name_ == null)
        {
            if (other.name_ != null)
            {
                return false;
            }
        }
        else if (!this.name_.equals(other.name_))
        {
            return false;
        }
        return true;
    }

}
