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
package jp.co.acroquest.endosnipe.web.explorer.entity;

/**
 * メソッド情報
 * 
 * @author hiramatsu
 */
public class InvocationInfo
{
    /**
     * コンストラクタ。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param target 計測対象であればtrue
     * @param transactionGraphOutput グラフにレスポンスの情報を記録するならtrue
     * @param alarmThreshold 処理時間が長いとしてjvnログを出力するかどうかの閾値
     * @param alarmCpuThreshold CPU使用時間が長いとしてjvnログを出力するかどうかの閾値
     */
    public InvocationInfo(final String className, final String methodName, final boolean target,
            final boolean transactionGraphOutput, final long alarmThreshold,
            final long alarmCpuThreshold)
    {
        super();
        className_ = className;
        methodName_ = methodName;
        target_ = target;
        transactionGraphOutput_ = transactionGraphOutput;
        alarmThreshold_ = alarmThreshold;
        alarmCpuThreshold_ = alarmCpuThreshold;
    }

    /** クラス名 */
    private String className_;

    /** メソッド名 */
    private String methodName_;

    /** ログ対象 */
    private boolean target_ = true;

    /** ルートノードの場合は <code>true</code> 、ルートノードでない場合は <code>false</code> */
    private boolean transactionGraphOutput_ = false;

    /** メソッドごとのアラーム閾値 */
    private long alarmThreshold_ = THRESHOLD_NOT_SPECIFIED;

    /** メソッドごとのCPUアラーム閾値 */
    private long alarmCpuThreshold_ = THRESHOLD_NOT_SPECIFIED;

    /** 閾値が指定されていないことを表す値 */
    public static final long THRESHOLD_NOT_SPECIFIED = -1;

    /**
     * クラス名を取得します。<br />
     * 
     * @return クラス名
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * クラス名を設定します。<br />
     * 
     * @param className クラス名
     */
    public void setClassName(final String className)
    {
        this.className_ = className;
    }

    /**
     * メソッド名を取得します。<br />
     * 
     * @return メソッド名
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * メソッド名を設定します。<br />
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(final String methodName)
    {
        this.methodName_ = methodName;
    }

    /**
     * メソッドの計測を行うかどうか。<br />
     * 
     * @return メソッドの計測を行う場合、<code>true</code>
     */
    public boolean isTarget()
    {
        return this.target_;
    }

    /**
     * メソッドの計測を行うかどうかを設定します。<br />
     * 
     * @param target メソッドの計測を行う場合、<code>true</code>
     */
    public void setTarget(final boolean target)
    {
        this.target_ = target;
    }

    /**
     * レスポンスのグラフに表示するかどうか。<br />
     * 
     * @return レスポンスのグラフに表示する場合、<code>true</code>
     */
    public boolean isResponseGraphOutput()
    {
        return this.transactionGraphOutput_;
    }

    /**
     * レスポンスのグラフに表示するかどうかを設定します。<br />
     * 
     * @param transactionGraphOutput レスポンスのグラフに表示する場合、<code>true</code>
     */
    public void setResponseGraphOutput(final boolean transactionGraphOutput)
    {
        this.transactionGraphOutput_ = transactionGraphOutput;
    }

    /**
     * アラーム発生の閾値を取得します。<br />
     * 
     * @return アラーム発生の閾値
     */
    public long getAlarmThreshold()
    {
        return this.alarmThreshold_;
    }

    /**
     * アラーム発生の閾値を設定します。<br />
     * 
     * @param alarmThreshold アラーム発生の閾値
     */
    public void setAlarmThreshold(final long alarmThreshold)
    {
        this.alarmThreshold_ = alarmThreshold;
    }

    /**
     * アラーム発生のCPU時間の閾値を取得します。<br />
     * 
     * @return アラーム発生のCPU時間の閾値
     */
    public long getAlarmCpuThreshold()
    {
        return this.alarmCpuThreshold_;
    }

    /**
     * アラーム発生のCPU時間の閾値を設定します。<br />
     * 
     * @param alarmCpuThreshold アラーム発生のCPU時間の閾値
     */
    public void setAlarmCpuThreshold(final long alarmCpuThreshold)
    {
        this.alarmCpuThreshold_ = alarmCpuThreshold;
    }
}
