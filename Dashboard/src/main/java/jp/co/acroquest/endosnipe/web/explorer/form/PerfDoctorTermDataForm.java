package jp.co.acroquest.endosnipe.web.explorer.form;

import java.util.List;

/**
 * PerformanceDoctorの期間データ取得用クラス。
 * 
 * @author hiramatsu
 *
 */
public class PerfDoctorTermDataForm
{
    /**　開始時刻。　*/
    private String startTime_;

    /** 終了時刻。 */
    private String endTime_;

    /**　データのIDのリスト。　*/
    private List<String> dataGroupIdList_;

    /**　最大描画行数。　*/
    private int maxLineNum_;

    /**
     * コンストラクタ。
     */
    public PerfDoctorTermDataForm()
    {

    }

    /**
     * 開始時刻を取得する。
     * 
     * @return 開始時刻
     */
    public String getStartTime()
    {
        return startTime_;
    }

    /**
     * 開始時刻を設定する。
     * 
     * @param startTime 設定する開始時刻
     */
    public void setStartTime(final String startTime)
    {
        this.startTime_ = startTime;
    }

    /**
     * 終了時刻を取得する。
     * 
     * @return 終了時刻
     */
    public String getEndTime()
    {
        return endTime_;
    }

    /**
     * 終了時刻を設定する。
     * 
     * @param endTime 設定する終了時刻
     */
    public void setEndTime(final String endTime)
    {
        this.endTime_ = endTime;
    }

    /**
     * データのIDのリストを取得する。
     * 
     * @return データのIDのリスト
     */
    public List<String> getDataGroupIdList()
    {
        return dataGroupIdList_;
    }

    /**
     * データのIDのリストを設定する。
     * 
     * @param dataGroupIdList 設定するデータのIDのリスト
     */
    public void setDataGroupIdList(final List<String> dataGroupIdList)
    {
        this.dataGroupIdList_ = dataGroupIdList;
    }

    /**
     * 最大描画行数を取得する。
     * 
     * @return 最大描画行数
     */
    public int getMaxLineNum()
    {
        return maxLineNum_;
    }

    /**
     * 最大描画行数を設定する。
     * 
     * @param maxLineNum 設定する最大描画行数
     */
    public void setMaxLineNum(final int maxLineNum)
    {
        this.maxLineNum_ = maxLineNum;
    }

}
