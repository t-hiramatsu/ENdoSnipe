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
package jp.co.acroquest.endosnipe.web.explorer.dto;

/**
 * ツリーメニューのDTOクラス。
 * 
 * @author miyasaka
 *
 */
public class TreeMenuDto
{
    /** 表示名 */
    private String data_;

    /** ツリーID */
    private String treeId_;

    /** 親ツリーID */
    private String parentTreeId_;

    /** ID */
    private String id_;

    /** タイプ */
    private String type_;

    /** アイコン */
    private String icon_;

    /** 計測単位 */
    private String measurementUnit_;

    private String message_;

    /**
     * コンストラクタ。
     */
    public TreeMenuDto()
    {

    }

    /**
     * アイコンを取得する。
     * 
     * @return アイコン
     */
    public String getIcon()
    {
        return icon_;
    }

    /**
     * アイコンを設定する。
     * 
     * @param icon アイコン
     */
    public void setIcon(final String icon)
    {
        this.icon_ = icon;
    }

    /**
     * 表示名を取得する。
     * 
     * @return 表示名
     */
    public String getData()
    {
        return data_;
    }

    /**
     * 表示名を設定する。
     * 
     * @param data 表示名
     */
    public void setData(final String data)
    {
        this.data_ = data;
    }

    /**
     * ツリーIDを取得する。
     * 
     * @return ツリーID
     */
    public String getTreeId()
    {
        return treeId_;
    }

    /**
     * ツリーIDを設定する。
     * 
     * @param treeId ツリーID
     */
    public void setTreeId(final String treeId)
    {
        this.treeId_ = treeId;
    }

    /**
     * 親ツリーIDを取得する。
     * 
     * @return 親ツリーID
     */
    public String getParentTreeId()
    {
        return parentTreeId_;
    }

    /**
     * 親ツリーIDを設定する。
     * 
     * @param parentTreeId 親ツリーID
     */
    public void setParentTreeId(final String parentTreeId)
    {
        this.parentTreeId_ = parentTreeId;
    }

    /**
     * IDを取得する。
     * 
     * @return ID
     */
    public String getId()
    {
        return id_;
    }

    /**
     * IDを設定する。
     *
     * @param id ID
     * 
     */
    public void setId(final String id)
    {
        this.id_ = id;
    }

    /**
     * 計測単位を取得する。
     * 
     * @return 計測単位
     */
    public String getMeasurementUnit()
    {
        return measurementUnit_;
    }

    /**
     * 計測単位を設定する。
     * 
     * @param measurementUnit 計測単位
     */
    public void setMeasurementUnit(final String measurementUnit)
    {
        this.measurementUnit_ = measurementUnit;
    }

    /**
     * message for Summary Signal.
     * 
     * @return message
     */
    public String getMessage()
    {
        return message_;
    }

    /**
     * message for Summary Signal
     * 
     * @param message to alarm error or not
     */
    public void setMessage(final String message)
    {
        message_ = message;
    }

    /**
     * タイプを取得する。
     * 
     * @return タイプ
     */
    public String getType()
    {
        return type_;
    }

    /**
     * タイプを設定する。
     * 
     * @param type タイプ
     */
    public void setType(final String type)
    {
        this.type_ = type;
    }

    @Override
    public String toString()
    {
        return "TreeMenuDto [data=" + data_ + ", treeId=" + treeId_ + ", parentTreeId="
                + parentTreeId_ + ", id=" + id_ + ", type=" + type_ + ", icon=" + icon_
                + ", measurementUnit=" + measurementUnit_ + ", message=" + message_ + "]";
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;

        if (data_ != null)
        {
            result = PRIME * result + data_.hashCode();
        }

        if (icon_ != null)
        {
            result = PRIME * result + icon_.hashCode();
        }

        if (id_ != null)
        {
            result = PRIME * result + id_.hashCode();
        }

        if (measurementUnit_ != null)
        {
            result = PRIME * result + measurementUnit_.hashCode();
        }

        if (parentTreeId_ != null)
        {
            result = PRIME * result + parentTreeId_.hashCode();
        }

        if (treeId_ != null)
        {
            result = PRIME * result + treeId_.hashCode();
        }

        if (type_ != null)
        {
            result = PRIME * result + type_.hashCode();
        }

        if (message_ != null)
        {
            result = PRIME * result + message_.hashCode();
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
        TreeMenuDto other = (TreeMenuDto)obj;
        if (data_ == null)
        {
            if (other.data_ != null)
            {
                return false;
            }
        }
        else if (!data_.equals(other.data_))
        {
            return false;
        }
        if (icon_ == null)
        {
            if (other.icon_ != null)
            {
                return false;
            }
        }
        else if (!icon_.equals(other.icon_))
        {
            return false;
        }
        if (id_ == null)
        {
            if (other.id_ != null)
            {
                return false;
            }
        }
        else if (!id_.equals(other.id_))
        {
            return false;
        }
        if (measurementUnit_ == null)
        {
            if (other.measurementUnit_ != null)
            {
                return false;
            }
        }
        else if (!measurementUnit_.equals(other.measurementUnit_))
        {
            return false;
        }
        if (parentTreeId_ == null)
        {
            if (other.parentTreeId_ != null)
            {
                return false;
            }
        }
        else if (!parentTreeId_.equals(other.parentTreeId_))
        {
            return false;
        }
        if (treeId_ == null)
        {
            if (other.treeId_ != null)
            {
                return false;
            }
        }
        else if (!treeId_.equals(other.treeId_))
        {
            return false;
        }
        if (type_ == null)
        {
            if (other.type_ != null)
            {
                return false;
            }
        }
        else if (!type_.equals(other.type_))
        {
            return false;
        }
        if (message_ == null)
        {
            if (other.message_ != null)
            {
                return false;
            }
        }
        else if (!message_.equals(other.message_))
        {
            return false;
        }
        return true;
    }

}
