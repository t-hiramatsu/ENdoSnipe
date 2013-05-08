package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * シグナルのツリーメニューのDTOクラス。
 *
 * @author miyasaka
 *
 */
public class SignalTreeMenuDto extends TreeMenuDto
{

    /** 閾値の判定結果の値。 */
    private Integer signalValue_;

    /**
     * コンストラクタ。
     */
    public SignalTreeMenuDto()
    {

    }

    /**
     * 閾値の判定結果の値を取得する。
     * 
     * @return 閾値の判定結果の値
     */
    public Integer getSignalValue()
    {
        return signalValue_;
    }

    /**
     * 閾値の判定結果の値を設定する。
     * 
     * @param signalValue 閾値の判定結果の値
     */
    public void setSignalValue(final Integer signalValue)
    {
        this.signalValue_ = signalValue;
    }

    @Override
    public String toString()
    {
        return "SignalTreeMenuDto [signalValue=" + signalValue_ + ", getSignalValue()="
                + getSignalValue() + ", getData()=" + getData() + ", getTreeId()=" + getTreeId()
                + ", getParentTreeId()=" + getParentTreeId() + ", getId()=" + getId()
                + ", getMeasurementUnit()=" + getMeasurementUnit() + ", getType()=" + getType()
                + ", toString()=" + super.toString() + ", hashCode()=" + hashCode()
                + ", getClass()=" + getClass() + "]";
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        if (signalValue_ != null)
        {
            result = PRIME * result + super.hashCode();
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
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        SignalTreeMenuDto other = (SignalTreeMenuDto)obj;
        if (signalValue_ == null)
        {
            if (other.signalValue_ != null)
            {
                return false;
            }
        }
        else if (!signalValue_.equals(other.signalValue_))
        {
            return false;
        }
        return true;
    }

}
