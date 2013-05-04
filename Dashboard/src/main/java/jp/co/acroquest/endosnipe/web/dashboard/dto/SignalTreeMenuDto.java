package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * シグナルのツリーメニューのDTOクラス。
 *
 * @author miyasaka
 *
 */
public class SignalTreeMenuDto extends TreeMenuDto
{

    /** 閾値の判定結果の値 */
    private Integer signalValue;

    public Integer getSignalValue()
    {
        return signalValue;
    }

    public void setSignalValue(final Integer signalValue)
    {
        this.signalValue = signalValue;
    }

    @Override
    public String toString()
    {
        return "SignalTreeMenuDto [signalValue=" + signalValue + ", getSignalValue()="
                + getSignalValue() + ", getData()=" + getData() + ", getTreeId()=" + getTreeId()
                + ", getParentTreeId()=" + getParentTreeId() + ", getId()=" + getId()
                + ", getMeasurementUnit()=" + getMeasurementUnit() + ", getType()=" + getType()
                + ", toString()=" + super.toString() + ", hashCode()=" + hashCode()
                + ", getClass()=" + getClass() + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((signalValue == null) ? 0 : signalValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        SignalTreeMenuDto other = (SignalTreeMenuDto)obj;
        if (signalValue == null)
        {
            if (other.signalValue != null)
            {
                return false;
            }
        }
        else if (!signalValue.equals(other.signalValue))
        {
            return false;
        }
        return true;
    }

}
