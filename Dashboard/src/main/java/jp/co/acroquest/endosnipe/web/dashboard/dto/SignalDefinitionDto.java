package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * シグナル定義のDTOクラス。
 * 
 * @author miyasaka
 * 
 */
public class SignalDefinitionDto
{
    /** シグナル定義テーブルのID。 */
    private long signalId_;

    /** シグナル名。 */
    private String signalName_;

    /** マッチングパターン。 */
    private String matchingPattern_;

    /** 設定できる閾値の上限レベル。 */
    private int level_;

    /** 各レベルの閾値。 */
    private String patternValue_;

    /** エスカレーション期間。 */
    private double escalationPeriod_;

    /** 閾値判定の結果。 */
    private Integer signalValue_;

    /**
     * コンストラクタ。
     */
    public SignalDefinitionDto()
    {

    }

    /**
     * シグナル名を取得する。
     * 
     * @return シグナル名
     */
    public String getSignalName()
    {
        return signalName_;
    }

    /**
     * シグナルIDを取得する。
     * 
     * @return シグナルID
     */
    public long getSignalId()
    {
        return signalId_;
    }

    /**
     * シグナルIDを設定する。
     * 
     * @param signalId
     *            シグナルID
     */
    public void setSignalId(final long signalId)
    {
        this.signalId_ = signalId;
    }

    /**
     * シグナル名を設定する。
     * 
     * @param signalName
     *            シグナル名
     */
    public void setSignalName(final String signalName)
    {
        this.signalName_ = signalName;
    }

    /**
     * マッチングパターンを取得する。
     * 
     * @return マッチングパターン
     */
    public String getMatchingPattern()
    {
        return matchingPattern_;
    }

    /**
     * マッチングパターンを設定する。
     * 
     * @param matchingPattern
     *            マッチングパターン
     */
    public void setMatchingPattern(final String matchingPattern)
    {
        this.matchingPattern_ = matchingPattern;
    }

    /**
     * 設定できる閾値の上限レベルを取得する。
     * 
     * @return 設定できる閾値の上限レベル
     */
    public int getLevel()
    {
        return level_;
    }

    /**
     * 設定できる閾値の上限レベルを設定する。
     * 
     * @param level
     *            設定できる閾値の上限レベル
     */
    public void setLevel(final int level)
    {
        this.level_ = level;
    }

    /**
     * 各レベルの閾値を取得する。
     * 
     * @return 各レベルの閾値
     */
    public String getPatternValue()
    {
        return patternValue_;
    }

    /**
     * 各レベルの閾値を設定する。
     * 
     * @param patternValue
     *            各レベルの閾値
     */
    public void setPatternValue(final String patternValue)
    {
        this.patternValue_ = patternValue;
    }

    /**
     * エスカレーション期間を取得する。
     * 
     * @return エスカレーション期間
     */
    public double getEscalationPeriod()
    {
        return escalationPeriod_;
    }

    /**
     * エスカレーション期間を設定する。
     * 
     * @param escalationPeriod
     *            エスカレーション期間
     */
    public void setEscalationPeriod(final double escalationPeriod)
    {
        this.escalationPeriod_ = escalationPeriod;
    }

    /**
     * 閾値判定の結果を取得する。
     * 
     * @return 閾値判定の結果
     */
    public Integer getSignalValue()
    {
        return signalValue_;
    }

    /**
     * 閾値判定の結果を設定する。
     * 
     * @param signalValue 閾値判定の結果
     */
    public void setSignalValue(final Integer signalValue)
    {
        this.signalValue_ = signalValue;
    }
}
