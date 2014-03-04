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

@XmlType(name = "resource")
public class Resource
{
    public static final String OBJ_NAME_SHAPE = "ENS.ShapeElementView";

    public static final String OBJ_NAME_TEXT = "ENS.TextBoxElementView";

    public static final String OBJ_NAME_SIGNAL = "ENS.SignalElementView";

    public static final String OBJ_NAME_GRAPH = "ENS.MultipleResourceGraphElementView";

    public static final String OBJ_NAME_BACKGROUND = "ENS.BackgroundElementView";

    private String objectName;

    private Integer x;

    private Integer y;

    private Integer w;

    private Integer h;

    private Property property;

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(final String objectName)
    {
        this.objectName = objectName;
    }

    public Integer getX()
    {
        return x;
    }

    public void setX(final Integer x)
    {
        this.x = x;
    }

    public Integer getY()
    {
        return y;
    }

    public void setY(final Integer y)
    {
        this.y = y;
    }

    public Integer getW()
    {
        return w;
    }

    public void setW(final Integer w)
    {
        this.w = w;
    }

    public Integer getH()
    {
        return h;
    }

    public void setH(final Integer h)
    {
        this.h = h;
    }

    public Property getProperty()
    {
        return property;
    }

    public void setProperty(final Property property)
    {
        this.property = property;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((h == null) ? 0 : h.hashCode());
        result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        result = prime * result + ((w == null) ? 0 : w.hashCode());
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
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
        if (h == null)
        {
            if (other.h != null)
            {
                return false;
            }
        }
        else if (!h.equals(other.h))
        {
            return false;
        }
        if (objectName == null)
        {
            if (other.objectName != null)
            {
                return false;
            }
        }
        else if (!objectName.equals(other.objectName))
        {
            return false;
        }
        if (property == null)
        {
            if (other.property != null)
            {
                return false;
            }
        }
        else if (!property.equals(other.property))
        {
            return false;
        }
        if (w == null)
        {
            if (other.w != null)
            {
                return false;
            }
        }
        else if (!w.equals(other.w))
        {
            return false;
        }
        if (x == null)
        {
            if (other.x != null)
            {
                return false;
            }
        }
        else if (!x.equals(other.x))
        {
            return false;
        }
        if (y == null)
        {
            if (other.y != null)
            {
                return false;
            }
        }
        else if (!y.equals(other.y))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Resource [objectName=" + objectName + ", x=" + x + ", y=" + y + ", w=" + w + ", h="
                + h + ", property=" + property + "]";
    }

}
