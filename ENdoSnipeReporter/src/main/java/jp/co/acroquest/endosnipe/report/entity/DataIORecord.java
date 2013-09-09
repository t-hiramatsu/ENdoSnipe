package jp.co.acroquest.endosnipe.report.entity;

import java.sql.Timestamp;

public class DataIORecord
{
	/** 計測時刻 */
    private Timestamp measurementTime_;

    /** ネットワーク経由でのデータ送受信量のグラフの、データ受信量[サイズ(Bytes)]（補間期間平均） */
    private long dataReceive_;
    
    /** ネットワーク経由でのデータ送受信量のグラフの、データ受信量[サイズ(Bytes)]（補間期間最大） */
    private long dataReceiveMax_;
    
    /** ネットワーク経由でのデータ送受信量のグラフの、データ受信量[サイズ(Bytes)]（補間期間最小） */
    private long dataReceiveMin_;

    /** ネットワーク経由でのデータ送受信量のグラフの、データ送信量[サイズ(Bytes)]（補間期間平均） */
    private long dataTransmit_;
    
    /** ネットワーク経由でのデータ送受信量のグラフの、データ送信量[サイズ(Bytes)]（補間期間最大） */
    private long dataTransmitMax_;
    
    /** ネットワーク経由でのデータ送受信量のグラフの、データ送信量[サイズ(Bytes)]（補間期間最小） */
    private long dataTransmitMin_;

    /** ファイル入出力量のグラフの、ファイル入力量[サイズ(Bytes)]（補間期間平均） */
    private long fileInput_;
    
    /** ファイル入出力量のグラフの、ファイル入力量[サイズ(Bytes)]（補間期間最大） */
    private long fileInputMax_;
    
    /** ファイル入出力量のグラフの、ファイル入力量[サイズ(Bytes)]（補間期間最小） */
    private long fileInputMin_;

    /** ファイル入出力量のグラフの、ファイル出力量[サイズ(Bytes)]（補間期間平均） */
    private long fileOutput_;
    
    /** ファイル入出力量のグラフの、ファイル出力量[サイズ(Bytes)]（補間期間最大） */
    private long fileOutputMax_;
    
    /** ファイル入出力量のグラフの、ファイル出力量[サイズ(Bytes)]（補間期間最小） */
    private long fileOutputMin_;

	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime() {
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime) {
		this.measurementTime_ = measurementTime;
	}

	/**
	 * @return the dataReceive_
	 */
	public long getDataReceive() {
		return dataReceive_;
	}

	/**
	 * @param dataReceive the dataReceive_ to set
	 */
	public void setDataReceive(long dataReceive) {
		this.dataReceive_ = dataReceive;
	}

	/**
	 * @return the dataReceiveMax_
	 */
	public long getDataReceiveMax() {
		return dataReceiveMax_;
	}

	/**
	 * @param dataReceiveMax the dataReceiveMax_ to set
	 */
	public void setDataReceiveMax(long dataReceiveMax) {
		this.dataReceiveMax_ = dataReceiveMax;
	}

	/**
	 * @return the dataReceiveMin_
	 */
	public long getDataReceiveMin() {
		return dataReceiveMin_;
	}

	/**
	 * @param dataReceiveMin the dataReceiveMin_ to set
	 */
	public void setDataReceiveMin(long dataReceiveMin) {
		this.dataReceiveMin_ = dataReceiveMin;
	}

	/**
	 * @return the dataTransmit_
	 */
	public long getDataTransmit() {
		return dataTransmit_;
	}

	/**
	 * @param dataTransmit the dataTransmit_ to set
	 */
	public void setDataTransmit(long dataTransmit) {
		this.dataTransmit_ = dataTransmit;
	}

	/**
	 * @return the dataTransmitMax_
	 */
	public long getDataTransmitMax() {
		return dataTransmitMax_;
	}

	/**
	 * @param dataTransmitMax the dataTransmitMax_ to set
	 */
	public void setDataTransmitMax(long dataTransmitMax) {
		this.dataTransmitMax_ = dataTransmitMax;
	}

	/**
	 * @return the dataTransmitMin_
	 */
	public long getDataTransmitMin() {
		return dataTransmitMin_;
	}

	/**
	 * @param dataTransmitMin the dataTransmitMin_ to set
	 */
	public void setDataTransmitMin(long dataTransmitMin) {
		this.dataTransmitMin_ = dataTransmitMin;
	}

	/**
	 * @return the fileInput_
	 */
	public long getFileInput() {
		return fileInput_;
	}

	/**
	 * @param fileInput　the fileInput_ to set
	 */
	public void setFileInput(long fileInput) {
		this.fileInput_ = fileInput;
	}

	/**
	 * @return the fileInputMax_
	 */
	public long getFileInputMax() {
		return fileInputMax_;
	}

	/**
	 * @param fileInputMax the fileInputMax_ to set
	 */
	public void setFileInputMax(long fileInputMax) {
		this.fileInputMax_ = fileInputMax;
	}

	/**
	 * @return the fileInputMin_
	 */
	public long getFileInputMin() {
		return fileInputMin_;
	}

	/**
	 * @param fileInputMin the fileInputMin_ to set
	 */
	public void setFileInputMin(long fileInputMin) {
		this.fileInputMin_ = fileInputMin;
	}

	/**
	 * @return the fileOutput_
	 */
	public long getFileOutput() {
		return fileOutput_;
	}

	/**
	 * @param fileOutput the fileOutput_ to set
	 */
	public void setFileOutput(long fileOutput) {
		this.fileOutput_ = fileOutput;
	}

	/**
	 * @return the fileOutputMax_
	 */
	public long getFileOutputMax() {
		return fileOutputMax_;
	}

	/**
	 * @param fileOutputMax the fileOutputMax_ to set
	 */
	public void setFileOutputMax(long fileOutputMax) {
		this.fileOutputMax_ = fileOutputMax;
	}

	/**
	 * @return the fileOutputMin_
	 */
	public long getFileOutputMin() {
		return fileOutputMin_;
	}

	/**
	 * @param fileOutputMin the fileOutputMin_ to set
	 */
	public void setFileOutputMin(long fileOutputMin) {
		this.fileOutputMin_ = fileOutputMin;
	}
}
