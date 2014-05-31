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
 * リソースクラス
 * @author kamo
 *
 */
@XmlType(name = "resource")
public class Resource
{
    /** 図形のオブジェクト名 */
    public static final String OBJ_NAME_SHAPE = "ENS.ShapeElementView";

    /** テキストのオブジェクト名 */
    public static final String OBJ_NAME_TEXT = "ENS.TextBoxElementView";

    /** シグナルのオブジェクト名 */
    public static final String OBJ_NAME_SIGNAL = "ENS.SignalElementView";

    /** 複数リソースグラフのオブジェクト名 */
    public static final String OBJ_NAME_MULTIPLE_GRAPH = "ENS.MultipleResourceGraphElementView";

    /** リソースグラフのオブジェクト名 */
    public static final String OBJ_NAME_GRAPH = "ENS.ResourceGraphElementView";

    /** 背景のオブジェクト名 */
    public static final String OBJ_NAME_BACKGROUND = "ENS.BackgroundElementView";

    /** オブジェクト名 */
    private String objectName_;

    /** x座標 */
    private Integer x_;

    /** y座標 */
    private Integer y_;

    /** 横幅 */
    private Integer w_;

    /** 高さ */
    private Integer h_;

    /** プロパティ */
    private Property property_;

    /**
     * オブジェクト名を取得する
     * @return オブジェクト名
     */
    public String getObjectName()
    {
        return objectName_;
    }

    /**
     * オブジェクト名を設定する
     * @param objectName オブジェクト名
     */
    public void setObjectName(final String objectName)
    {
        this.objectName_ = objectName;
    }

    /**
     * x座標を取得する
     * @return x座標
     */
    public Integer getX()
    {
        return x_;
    }

    /**
     * x座標を設定する
     * @param x x座標
     */
    public void setX(final Integer x)
    {
        this.x_ = x;
    }

    /**
     * y座標を取得する
     * @return y座標
     */
    public Integer getY()
    {
        return y_;
    }

    /**
     * y座標を設定する
     * @param y y座標
     */
    public void setY(final Integer y)
    {
        this.y_ = y;
    }

    /**
     * 横幅を取得する
     * @return 横幅
     */
    public Integer getW()
    {
        return w_;
    }

    /**
     * 横幅を設定する
     * @param w 横幅
     */
    public void setW(final Integer w)
    {
        this.w_ = w;
    }

    /**
     * 高さを取得する
     * @return 高さ
     */
    public Integer getH()
    {
        return h_;
    }

    /**
     * 高さを設定する
     * @param h 高さ
     */
    public void setH(final Integer h)
    {
        this.h_ = h;
    }

    /**
     * プロパティを取得する
     * @return プロパティ
     */
    public Property getProperty()
    {
        return property_;
    }

    /**
     * プロパティを設定する
     * @param property プロパティ
     */
    public void setProperty(final Property property)
    {
        this.property_ = property;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((h_ == null) ? 0 : h_.hashCode());
        result = PRIME * result + ((objectName_ == null) ? 0 : objectName_.hashCode());
        result = PRIME * result + ((property_ == null) ? 0 : property_.hashCode());
        result = PRIME * result + ((w_ == null) ? 0 : w_.hashCode());
        result = PRIME * result + ((x_ == null) ? 0 : x_.hashCode());
        result = PRIME * result + ((y_ == null) ? 0 : y_.hashCode());
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
        Resource other = (Resource)obj;
        if (h_ == null)
        {
            if (other.h_ != null)
            {
                return false;
            }
        }
        else if (!h_.equals(other.h_))
        {
            return false;
        }
        if (objectName_ == null)
        {
            if (other.objectName_ != null)
            {
                return false;
            }
        }
        else if (!objectName_.equals(other.objectName_))
        {
            return false;
        }
        if (property_ == null)
        {
            if (other.property_ != null)
            {
                return false;
            }
        }
        else if (!property_.equals(other.property_))
        {
            return false;
        }
        if (w_ == null)
        {
            if (other.w_ != null)
            {
                return false;
            }
        }
        else if (!w_.equals(other.w_))
        {
            return false;
        }
        if (x_ == null)
        {
            if (other.x_ != null)
            {
                return false;
            }
        }
        else if (!x_.equals(other.x_))
        {
            return false;
        }
        if (y_ == null)
        {
            if (other.y_ != null)
            {
                return false;
            }
        }
        else if (!y_.equals(other.y_))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Resource [objectName=" + objectName_ + ", x=" + x_ + ", y=" + y_ + ", w=" + w_
                + ", h=" + h_ + ", property=" + property_ + "]";
    }

}
