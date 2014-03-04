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

@XmlType(name = "background")
public class Background
{
    public static final String OBJECT_TYPE_POLYGON = "POLYGON";

    public static final String OBJECT_TYPE_IMAGE = "IMAGE";

    private String objectType;

    private String fill;

    private String src;

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType(final String objectType)
    {
        this.objectType = objectType;
    }

    public String getFill()
    {
        return fill;
    }

    public void setFill(final String fill)
    {
        this.fill = fill;
    }

    public String getSrc()
    {
        return src;
    }

    public void setSrc(final String src)
    {
        this.src = src;
    }

    public boolean isEmpty()
    {
        if (this.fill != null)
        {
            return false;
        }
        if (this.objectType != null)
        {
            return false;
        }
        if (this.src != null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Background [objectType=" + objectType + ", fill=" + fill + ", src=" + src + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fill == null) ? 0 : fill.hashCode());
        result = prime * result + ((objectType == null) ? 0 : objectType.hashCode());
        result = prime * result + ((src == null) ? 0 : src.hashCode());
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
        if (fill == null)
        {
            if (other.fill != null)
            {
                return false;
            }
        }
        else if (!fill.equals(other.fill))
        {
            return false;
        }
        if (objectType == null)
        {
            if (other.objectType != null)
            {
                return false;
            }
        }
        else if (!objectType.equals(other.objectType))
        {
            return false;
        }
        if (src == null)
        {
            if (other.src != null)
            {
                return false;
            }
        }
        else if (!src.equals(other.src))
        {
            return false;
        }
        return true;
    }

}
