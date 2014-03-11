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

@XmlType(name = "template")
public class Template
{
    private static final int PRIME = 31;

    private Background background;

    private List<Resource> resources;

    private Integer width;

    private Integer height;

    @XmlElement(name = "background")
    public Background getBackground()
    {
        return background;
    }

    public void setBackground(final Background background)
    {
        this.background = background;
    }

    @XmlElement(name = "resource")
    @XmlElementWrapper(name = "resources")
    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources(final List<Resource> resources)
    {
        this.resources = resources;
    }

    @XmlElement(name = "width")
    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(final Integer width)
    {
        this.width = width;
    }

    @XmlElement(name = "height")
    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(final Integer height)
    {
        this.height = height;
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        result = PRIME * result + ((background == null) ? 0 : background.hashCode());
        result = PRIME * result + ((height == null) ? 0 : height.hashCode());
        result = PRIME * result + ((resources == null) ? 0 : resources.hashCode());
        result = PRIME * result + ((width == null) ? 0 : width.hashCode());
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
        if (background == null)
        {
            if (other.background != null)
            {
                return false;
            }
        }
        else if (!background.equals(other.background))
        {
            return false;
        }
        if (height == null)
        {
            if (other.height != null)
            {
                return false;
            }
        }
        else if (!height.equals(other.height))
        {
            return false;
        }
        if (resources == null)
        {
            if (other.resources != null)
            {
                return false;
            }
        }
        else if (!resources.equals(other.resources))
        {
            return false;
        }
        if (width == null)
        {
            if (other.width != null)
            {
                return false;
            }
        }
        else if (!width.equals(other.width))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Template [background=" + background + ", resources=" + resources + ", width="
                + width + ", height=" + height + "]";
    }

}
