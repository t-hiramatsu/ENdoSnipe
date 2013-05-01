package jp.co.acroquest.endosnipe.web.dashboard.form;

import java.util.List;

/**
 * PerformanceDoctorの期間データ取得用クラス。
 * 
 * @author hiramatsu
 *
 */
public class PerfDoctorTermDataForm {
	/**　開始時刻。　*/
	private String startTime;

	/** 終了時刻。 */
	private String endTime;

	/**　データのIDのリスト。　*/
	private List<String> dataGroupIdList;

	/**　最大描画行数。　*/
	private int maxLineNum;
	
	/**
	 * 開始時刻を取得する。
	 * 
	 * @return 開始時刻
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * 開始時刻を設定する。
	 * 
	 * @param startTime 設定する開始時刻
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * 終了時刻を取得する。
	 * 
	 * @return 終了時刻
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * 終了時刻を設定する。
	 * 
	 * @param endTime 設定する終了時刻
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * データのIDのリストを取得する。
	 * 
	 * @return データのIDのリスト
	 */
	public List<String> getDataGroupIdList() {
		return dataGroupIdList;
	}

	/**
	 * データのIDのリストを設定する。
	 * 
	 * @param dataGroupIdList 設定するデータのIDのリスト
	 */
	public void setDataGroupIdList(List<String> dataGroupIdList) {
		this.dataGroupIdList = dataGroupIdList;
	}

	/**
	 * 最大描画行数を取得する。
	 * 
	 * @return 最大描画行数
	 */
	public int getMaxLineNum() {
		return maxLineNum;
	}

	/**
	 * 最大描画行数を設定する。
	 * 
	 * @param maxLineNum 設定する最大描画行数
	 */
	public void setMaxLineNum(int maxLineNum) {
		this.maxLineNum = maxLineNum;
	}

}
