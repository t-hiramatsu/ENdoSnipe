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
package jp.co.acroquest.endosnipe.data.dto;

/**
 * �?��ーメニューのDTOクラス�?
 * 
 * @author pin
 *
 */
public class GraphTypeDto
{
    /**  */
    private String itemName_;

    /** */
    private String itemType_;

    /**
     * コンストラクタ�?
     */
    public GraphTypeDto()
    {

    }

    public GraphTypeDto(final String itemName, final String itemType)
    {
        super();
        itemName_ = itemName;
        itemType_ = itemType;
    }

    /**
     * 表示名を取得する�?
     * 
     * @return 表示�?
     */
    public String getItemName()
    {
        return itemName_;
    }

    /**
     * 表示名を設定する�?
     * 
     * @param itemName 表示�?
     */
    public void setItemName(final String itemName)
    {
        this.itemName_ = itemName;
    }

    /**
     * �?��ーIDを取得する�?
     * 
     * @return �?��ーID
     */
    public String getItemType()
    {
        return itemType_;
    }

    /**
     * �?��ーIDを設定する�?
     * 
     * @param itemType �?��ーID
     */
    public void setItemType(final String itemType)
    {
        this.itemType_ = itemType;
    }

    @Override
    public String toString()
    {
        return "GraphTypeDto [ItemName=" + itemName_ + ", ItemType=" + itemType_ + "]";
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;

        if (itemName_ != null)
        {
            result = PRIME * result + itemName_.hashCode();
        }

        if (itemType_ != null)
        {
            result = PRIME * result + itemType_.hashCode();
        }

        return result;
    }

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
        GraphTypeDto other = (GraphTypeDto)obj;
        if (itemName_ == null)
        {
            if (other.itemName_ != null)
            {
                return false;
            }
        }
        else if (!itemName_.equals(other.itemName_))
        {
            return false;
        }

        return true;
    }
}