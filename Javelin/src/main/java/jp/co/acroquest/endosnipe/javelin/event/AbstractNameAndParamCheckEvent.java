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
package jp.co.acroquest.endosnipe.javelin.event;

import java.util.Map;

/**
 * �C�x���g���y�уp�����[�^�ɂ���āA�}�����s���C�x���g�N���X�ł��B<br />
 * 
 * @author kajiwara
 *
 */
public class AbstractNameAndParamCheckEvent extends CommonEvent
{
    /**
     * ���O�Ńn�b�V���R�[�h���v�Z����B
     * 
     * @return �n�b�V���R�[�h�B
     */
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.name_ == null) ? 0 : this.name_.hashCode());
        return result;
    }

    /**
     * ���O�y�уp�����[�^��r����B
     * 
     * @param obj ��r�ΏہB
     * @return ��r���ʁB
     */
    public boolean equals(Object obj)
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
        AbstractNameAndParamCheckEvent other = (AbstractNameAndParamCheckEvent)obj;
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
        
        if (!concatEventDescription(this.paramMap_).equals(concatEventDescription(other.paramMap_)))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * �C�x���g�̃p�����[�^���P�Ɍq���ĕԂ�
     * @param eventDescription �ϊ��Ώۂ̃C�x���g
     * @return �C�x���g�p�����[�^
     */
    private String concatEventDescription(Map<String, String> eventDescription)
    {
        StringBuffer eventParam = new StringBuffer("");
        for (Map.Entry<String, String> entry : eventDescription.entrySet())
        {
            eventParam.append(entry.getValue());
        }
        
        return eventParam.toString();
    }
}
