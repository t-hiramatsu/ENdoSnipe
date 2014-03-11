/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.explorer.template.meta;

import javax.xml.bind.annotation.XmlType;

/**
 * 背景クラス
 * @author kamo
 *
 */
@XmlType(name = "background")
public class Background
{
    /** 背景色のオブジェクトタイプ */
    public static final String OBJECT_TYPE_POLYGON = "POLYGON";

    /** 背景画像のオブジェクトタイプ */
    public static final String OBJECT_TYPE_IMAGE = "IMAGE";

    /** オブジェクトタイプ */
    private String objectType_;

    /** 塗りつぶし色 */
    private String fill_;

    /** 背景画像のパス */
    private String src_;

    /**
     * コンストラクタ
     */
    public Background()
    {
    }

    /**
     * オブジェクトタイプを取得する
     * @return オブジェクトタイプ
     */
    public String getObjectType()
    {
        return objectType_;
    }

    /**
     * オブジェクトタイプを設定する
     * @param objectType オブジェクトタイプ
     */
    public void setObjectType(final String objectType)
    {
        this.objectType_ = objectType;
    }

    /**
     * 塗りつぶし色を取得する
     * @return 塗りつぶし色
     */
    public String getFill()
    {
        return fill_;
    }

    /**
     * 塗りつぶし色を設定する
     * @param fill 塗りつぶし色
     */
    public void setFill(final String fill)
    {
        this.fill_ = fill;
    }

    /**
     * 背景画像のパスを取得する
     * @return 背景画像のパス
     */
    public String getSrc()
    {
        return src_;
    }

    /**
     * 背景画像のパスを設定する
     * @param src 背景画像のパス
     */
    public void setSrc(final String src)
    {
        this.src_ = src;
    }

    /**
     * 空の要素であるか判定する
     * @return 空であるか
     */
    public boolean isEmpty()
    {
        if (this.fill_ != null)
        {
            return false;
        }
        if (this.objectType_ != null)
        {
            return false;
        }
        if (this.src_ != null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Background [objectType=" + objectType_ + ", fill=" + fill_ + ", src=" + src_ + "]";
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fill_ == null) ? 0 : fill_.hashCode());
        result = PRIME * result + ((objectType_ == null) ? 0 : objectType_.hashCode());
        result = PRIME * result + ((src_ == null) ? 0 : src_.hashCode());
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
        Background other = (Background)obj;
        if (fill_ == null)
        {
            if (other.fill_ != null)
            {
                return false;
            }
        }
        else if (!fill_.equals(other.fill_))
        {
            return false;
        }
        if (objectType_ == null)
        {
            if (other.objectType_ != null)
            {
                return false;
            }
        }
        else if (!objectType_.equals(other.objectType_))
        {
            return false;
        }
        if (src_ == null)
        {
            if (other.src_ != null)
            {
                return false;
            }
        }
        else if (!src_.equals(other.src_))
        {
            return false;
        }
        return true;
    }

}
