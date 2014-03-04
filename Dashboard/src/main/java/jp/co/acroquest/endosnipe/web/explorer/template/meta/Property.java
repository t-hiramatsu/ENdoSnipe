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

public class Property
{
    private String strokeDasharray;

    private String stroke;

    private Integer strokeWidth;

    private String fill;

    private String shapeName;

    private Integer fontSize;

    private String textAnchor;

    private String fontFamily;

    private String text;

    private String resourceId;

    private String target;

    private String name;

    private Integer level;

    private String threshold;

    private Integer period;

    private Property border;

    private Property label;

    private Property signal;

    public String getStrokeDasharray()
    {
        return strokeDasharray;
    }

    public void setStrokeDasharray(final String strokeDasharray)
    {
        this.strokeDasharray = strokeDasharray;
    }

    public String getStroke()
    {
        return stroke;
    }

    public void setStroke(final String stroke)
    {
        this.stroke = stroke;
    }

    public Integer getStrokeWidth()
    {
        return strokeWidth;
    }

    public void setStrokeWidth(final Integer strokeWidth)
    {
        this.strokeWidth = strokeWidth;
    }

    public String getFill()
    {
        return fill;
    }

    public void setFill(final String fill)
    {
        this.fill = fill;
    }

    public String getShapeName()
    {
        return shapeName;
    }

    public void setShapeName(final String shapeName)
    {
        this.shapeName = shapeName;
    }

    public Integer getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(final Integer fontSize)
    {
        this.fontSize = fontSize;
    }

    public String getTextAnchor()
    {
        return textAnchor;
    }

    public void setTextAnchor(final String textAnchor)
    {
        this.textAnchor = textAnchor;
    }

    public String getFontFamily()
    {
        return fontFamily;
    }

    public void setFontFamily(final String fontFamily)
    {
        this.fontFamily = fontFamily;
    }

    public String getText()
    {
        return text;
    }

    public void setText(final String text)
    {
        this.text = text;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(final String resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(final String target)
    {
        this.target = target;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(final Integer level)
    {
        this.level = level;
    }

    public String getThreshold()
    {
        return threshold;
    }

    public void setThreshold(final String threshold)
    {
        this.threshold = threshold;
    }

    public Integer getPeriod()
    {
        return period;
    }

    public void setPeriod(final Integer period)
    {
        this.period = period;
    }

    public Property getBorder()
    {
        return border;
    }

    public void setBorder(final Property border)
    {
        this.border = border;
    }

    public Property getLabel()
    {
        return label;
    }

    public void setLabel(final Property label)
    {
        this.label = label;
    }

    public Property getSignal()
    {
        return signal;
    }

    public void setSignal(final Property signal)
    {
        this.signal = signal;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((border == null) ? 0 : border.hashCode());
        result = prime * result + ((fill == null) ? 0 : fill.hashCode());
        result = prime * result + ((fontFamily == null) ? 0 : fontFamily.hashCode());
        result = prime * result + ((fontSize == null) ? 0 : fontSize.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
        result = prime * result + ((shapeName == null) ? 0 : shapeName.hashCode());
        result = prime * result + ((signal == null) ? 0 : signal.hashCode());
        result = prime * result + ((stroke == null) ? 0 : stroke.hashCode());
        result = prime * result + ((strokeDasharray == null) ? 0 : strokeDasharray.hashCode());
        result = prime * result + ((strokeWidth == null) ? 0 : strokeWidth.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((textAnchor == null) ? 0 : textAnchor.hashCode());
        result = prime * result + ((threshold == null) ? 0 : threshold.hashCode());
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
        Property other = (Property)obj;
        if (border == null)
        {
            if (other.border != null)
            {
                return false;
            }
        }
        else if (!border.equals(other.border))
        {
            return false;
        }
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
        if (fontFamily == null)
        {
            if (other.fontFamily != null)
            {
                return false;
            }
        }
        else if (!fontFamily.equals(other.fontFamily))
        {
            return false;
        }
        if (fontSize == null)
        {
            if (other.fontSize != null)
            {
                return false;
            }
        }
        else if (!fontSize.equals(other.fontSize))
        {
            return false;
        }
        if (label == null)
        {
            if (other.label != null)
            {
                return false;
            }
        }
        else if (!label.equals(other.label))
        {
            return false;
        }
        if (level == null)
        {
            if (other.level != null)
            {
                return false;
            }
        }
        else if (!level.equals(other.level))
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (period == null)
        {
            if (other.period != null)
            {
                return false;
            }
        }
        else if (!period.equals(other.period))
        {
            return false;
        }
        if (resourceId == null)
        {
            if (other.resourceId != null)
            {
                return false;
            }
        }
        else if (!resourceId.equals(other.resourceId))
        {
            return false;
        }
        if (shapeName == null)
        {
            if (other.shapeName != null)
            {
                return false;
            }
        }
        else if (!shapeName.equals(other.shapeName))
        {
            return false;
        }
        if (signal == null)
        {
            if (other.signal != null)
            {
                return false;
            }
        }
        else if (!signal.equals(other.signal))
        {
            return false;
        }
        if (stroke == null)
        {
            if (other.stroke != null)
            {
                return false;
            }
        }
        else if (!stroke.equals(other.stroke))
        {
            return false;
        }
        if (strokeDasharray == null)
        {
            if (other.strokeDasharray != null)
            {
                return false;
            }
        }
        else if (!strokeDasharray.equals(other.strokeDasharray))
        {
            return false;
        }
        if (strokeWidth == null)
        {
            if (other.strokeWidth != null)
            {
                return false;
            }
        }
        else if (!strokeWidth.equals(other.strokeWidth))
        {
            return false;
        }
        if (target == null)
        {
            if (other.target != null)
            {
                return false;
            }
        }
        else if (!target.equals(other.target))
        {
            return false;
        }
        if (text == null)
        {
            if (other.text != null)
            {
                return false;
            }
        }
        else if (!text.equals(other.text))
        {
            return false;
        }
        if (textAnchor == null)
        {
            if (other.textAnchor != null)
            {
                return false;
            }
        }
        else if (!textAnchor.equals(other.textAnchor))
        {
            return false;
        }
        if (threshold == null)
        {
            if (other.threshold != null)
            {
                return false;
            }
        }
        else if (!threshold.equals(other.threshold))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Property [strokeDasharray=" + strokeDasharray + ", stroke=" + stroke
                + ", strokeWidth=" + strokeWidth + ", fill=" + fill + ", shapeName=" + shapeName
                + ", fontSize=" + fontSize + ", textAnchor=" + textAnchor + ", fontFamily="
                + fontFamily + ", text=" + text + ", resourceId=" + resourceId + ", target="
                + target + ", name=" + name + ", level=" + level + ", threshold=" + threshold
                + ", period=" + period + ", border=" + border + ", label=" + label + ", signal="
                + signal + "]";
    }

}
