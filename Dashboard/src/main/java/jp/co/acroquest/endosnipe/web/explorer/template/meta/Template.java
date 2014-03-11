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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * テンプレートクラス
 * @author kamo
 *
 */
@XmlType(name = "template")
public class Template
{
    /** 背景クラス */
    private Background background_;

    /** リソースクラスのリスト */
    private List<Resource> resources_;

    /** 横幅 */
    private Integer width_;

    /** 高さ */
    private Integer height_;

    /**
     * コンストラクタ
     */
    public Template()
    {
    }

    /**
     * 背景の取得
     * @return 背景
     */
    @XmlElement(name = "background")
    public Background getBackground()
    {
        return background_;
    }

    /**
     * 背景の設定
     * @param background 背景
     */
    public void setBackground(final Background background)
    {
        this.background_ = background;
    }

    /**
     * リソースリストの取得
     * @return リソースリスト
     */
    @XmlElement(name = "resource")
    @XmlElementWrapper(name = "resources")
    public List<Resource> getResources()
    {
        return resources_;
    }

    /**
     * リソースリストの設定
     * @param resources リソースリスト
     */
    public void setResources(final List<Resource> resources)
    {
        this.resources_ = resources;
    }

    /**
     * 横幅の取得
     * @return 横幅
     */
    @XmlElement(name = "width")
    public Integer getWidth()
    {
        return width_;
    }

    /**
     * 横幅の設定
     * @param width 横幅
     */
    public void setWidth(final Integer width)
    {
        this.width_ = width;
    }

    /**
     * 高さの取得
     * @return 高さ
     */
    @XmlElement(name = "height")
    public Integer getHeight()
    {
        return height_;
    }

    /**
     * 高さの設定
     * @param height 高さ
     */
    public void setHeight(final Integer height)
    {
        this.height_ = height;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((background_ == null) ? 0 : background_.hashCode());
        result = PRIME * result + ((height_ == null) ? 0 : height_.hashCode());
        result = PRIME * result + ((resources_ == null) ? 0 : resources_.hashCode());
        result = PRIME * result + ((width_ == null) ? 0 : width_.hashCode());
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
        Template other = (Template)obj;
        if (background_ == null)
        {
            if (other.background_ != null)
            {
                return false;
            }
        }
        else if (!background_.equals(other.background_))
        {
            return false;
        }
        if (height_ == null)
        {
            if (other.height_ != null)
            {
                return false;
            }
        }
        else if (!height_.equals(other.height_))
        {
            return false;
        }
        if (resources_ == null)
        {
            if (other.resources_ != null)
            {
                return false;
            }
        }
        else if (!resources_.equals(other.resources_))
        {
            return false;
        }
        if (width_ == null)
        {
            if (other.width_ != null)
            {
                return false;
            }
        }
        else if (!width_.equals(other.width_))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Template [background=" + background_ + ", resources=" + resources_ + ", width="
                + width_ + ", height=" + height_ + "]";
    }

}
