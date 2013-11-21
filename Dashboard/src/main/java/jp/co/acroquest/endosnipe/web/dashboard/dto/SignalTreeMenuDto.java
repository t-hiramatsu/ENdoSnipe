package jp.co.acroquest.endosnipe.web.dashboard.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * シグナルのツリーメニューのDTOクラス。
 *
 * @author miyasaka
 *
 */
public class SignalTreeMenuDto extends TreeMenuDto
{
    /** シグナル定義テーブルのID。 */
    private long signalId_;

    /** シグナル名。 */
    private String signalName_;

    /** エスカレーション期間。 */
    private double escalationPeriod_;

    /** 設定できる閾値の上限レベル。 */
    private int level_;

    /** マッチングパターン。 */
    private String matchingPattern_;

    /** 閾値の判定結果の値。 */
    private Integer signalValue_;

    /** 閾値のマップ */
    private Map<Integer, Double> signalMap_ = new HashMap<Integer, Double>();

    /**
     * コンストラクタ。
     */
    public SignalTreeMenuDto()
    {

    }

    public long getSignalId()
    {
        return signalId_;
    }

    public void setSignalId(final long signalId)
    {
        signalId_ = signalId;
    }

    public String getSignalName()
    {
        return signalName_;
    }

    public void setSignalName(final String signalName)
    {
        signalName_ = signalName;
    }

    public double getEscalationPeriod()
    {
        return escalationPeriod_;
    }

    public void setEscalationPeriod(final double escalationPeriod)
    {
        escalationPeriod_ = escalationPeriod;
    }

    public int getLevel()
    {
        return level_;
    }

    public void setLevel(final int level)
    {
        level_ = level;
    }

    public String getMatchingPattern()
    {
        return matchingPattern_;
    }

    public void setMatchingPattern(final String matchingPattern)
    {
        matchingPattern_ = matchingPattern;
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

    /**
     * 閾値のマップを取得する。
     * 
     * @return 閾値のマップ
     */
    public Map<Integer, Double> getSignalMap()
    {
        return signalMap_;
    }

    /**
     * 閾値のマップを設定する。
     * 
     * @param signalMap 閾値のマップ
     */
    public void setSignalMap(final Map<Integer, Double> signalMap)
    {
        signalMap_ = signalMap;
    }

    @Override
    public String toString()
    {
        return "SignalTreeMenuDto [signalId_=" + signalId_ + ", signalName_=" + signalName_
                + ", escalationPeriod_=" + escalationPeriod_ + ", level_=" + level_
                + ", matchingPattern_=" + matchingPattern_ + ", signalValue_=" + signalValue_
                + ", signalMap_=" + signalMap_ + "]";
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(escalationPeriod_);
        result = PRIME * result + (int)(temp ^ (temp >>> 32));
        result = PRIME * result + level_;
        result = PRIME * result + ((matchingPattern_ == null) ? 0 : matchingPattern_.hashCode());
        result = PRIME * result + (int)(signalId_ ^ (signalId_ >>> 32));
        result = PRIME * result + ((signalMap_ == null) ? 0 : signalMap_.hashCode());
        result = PRIME * result + ((signalName_ == null) ? 0 : signalName_.hashCode());
        result = PRIME * result + ((signalValue_ == null) ? 0 : signalValue_.hashCode());
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
        if (Double.doubleToLongBits(escalationPeriod_) != Double.doubleToLongBits(other.escalationPeriod_))
        {
            return false;
        }
        if (level_ != other.level_)
        {
            return false;
        }
        if (matchingPattern_ == null)
        {
            if (other.matchingPattern_ != null)
            {
                return false;
            }
        }
        else if (!matchingPattern_.equals(other.matchingPattern_))
        {
            return false;
        }
        if (signalId_ != other.signalId_)
        {
            return false;
        }
        if (signalMap_ == null)
        {
            if (other.signalMap_ != null)
            {
                return false;
            }
        }
        else if (!signalMap_.equals(other.signalMap_))
        {
            return false;
        }
        if (signalName_ == null)
        {
            if (other.signalName_ != null)
            {
                return false;
            }
        }
        else if (!signalName_.equals(other.signalName_))
        {
            return false;
        }
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
