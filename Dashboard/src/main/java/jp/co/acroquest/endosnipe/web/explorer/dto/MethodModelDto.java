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
package jp.co.acroquest.endosnipe.web.explorer.dto;

/**
 * MethodModelのDto
 * 
 * @author hiramatsu
 */
public class MethodModelDto
{
    /** パッケージ名 */
    private String packageName_;

    /** クラス名 */
    private String className_;

    /** メソッド名 */
    private String methodName_;

    /** メソッドの呼び出し回数 */
    private long callCount_;

    /** メソッドの最短処理時間（ミリ秒） */
    private long minimum_;

    /** メソッドの最長処理時間（ミリ秒） */
    private long maximum_;

    /** メソッドの合計実行時間（ミリ秒） */
    private long total_;

    /** メソッドの平均実行時間（ミリ秒） */
    private double average_;

    /** メソッドの最短 CPU 処理時間（ミリ秒） */
    private long cpuMinimum_;

    /** メソッドの最長 CPU 処理時間（ミリ秒） */
    private long cpuMaximum_;

    /** メソッドの合計 CPU 処理時間（ミリ秒） */
    private long cpuTotal_;

    /** メソッドの平均 CPU 処理時間（ミリ秒） */
    private double cpuAverage_;

    /** メソッドの最短ユーザ処理時間（ミリ秒） */
    private long userMinimum_;

    /** メソッドの最長ユーザ時間（ミリ秒） */
    private long userMaximum_;

    /** メソッドの合計ユーザ時間（ミリ秒） */
    private long userTotal_;

    /** メソッドの平均ユーザ時間（ミリ秒） */
    private double userAverage_;

    /** メソッドが計測対象かどうか */
    private boolean target_;

    /** メソッド内での例外発生回数 */
    private long throwableCount_;

    private long alarmThreshold_;

    private long alarmCpuThreshold_;

    private boolean transactionGraph_;

    private String profileValueChanged_;

    private String classMethodId_;

    public String getPackageName()
    {
        return packageName_;
    }

    public void setPackageName(final String packageName)
    {
        packageName_ = packageName;
    }

    public String getClassName()
    {
        return className_;
    }

    public void setClassName(final String className)
    {
        className_ = className;
    }

    public String getMethodName()
    {
        return methodName_;
    }

    public void setMethodName(final String methodName)
    {
        methodName_ = methodName;
    }

    public long getCallCount()
    {
        return callCount_;
    }

    public void setCallCount(final long callCount)
    {
        callCount_ = callCount;
    }

    public long getMinimum()
    {
        return minimum_;
    }

    public void setMinimum(final long minimum)
    {
        minimum_ = minimum;
    }

    public long getMaximum()
    {
        return maximum_;
    }

    public void setMaximum(final long maximum)
    {
        maximum_ = maximum;
    }

    public long getTotal()
    {
        return total_;
    }

    public void setTotal(final long total)
    {
        total_ = total;
    }

    public long getCpuMinimum()
    {
        return cpuMinimum_;
    }

    public void setCpuMinimum(final long cpuMinimum)
    {
        cpuMinimum_ = cpuMinimum;
    }

    public long getCpuMaximum()
    {
        return cpuMaximum_;
    }

    public void setCpuMaximum(final long cpuMaximum)
    {
        cpuMaximum_ = cpuMaximum;
    }

    public long getCpuTotal()
    {
        return cpuTotal_;
    }

    public void setCpuTotal(final long cpuTotal)
    {
        cpuTotal_ = cpuTotal;
    }

    public long getUserMinimum()
    {
        return userMinimum_;
    }

    public void setUserMinimum(final long userMinimum)
    {
        userMinimum_ = userMinimum;
    }

    public long getUserMaximum()
    {
        return userMaximum_;
    }

    public void setUserMaximum(final long userMaximum)
    {
        userMaximum_ = userMaximum;
    }

    public long getUserTotal()
    {
        return userTotal_;
    }

    public void setUserTotal(final long userTotal)
    {
        userTotal_ = userTotal;
    }

    public String getTarget()
    {
        return Boolean.toString(target_);
    }

    public void setTarget(final boolean target)
    {
        target_ = target;
    }

    public long getThrowableCount()
    {
        return throwableCount_;
    }

    public void setThrowableCount(final long throwableCount)
    {
        throwableCount_ = throwableCount;
    }

    public double getAverage()
    {
        return average_;
    }

    public void setAverage(final double average)
    {
        average_ = average;
    }

    public double getCpuAverage()
    {
        return cpuAverage_;
    }

    public void setCpuAverage(final double cpuAverage)
    {
        cpuAverage_ = cpuAverage;
    }

    public double getUserAverage()
    {
        return userAverage_;
    }

    public void setUserAverage(final double userAverage)
    {
        userAverage_ = userAverage;
    }

    public long getAlarmThreshold()
    {
        return alarmThreshold_;
    }

    public void setAlarmThreshold(final long alarmThreshold)
    {
        alarmThreshold_ = alarmThreshold;
    }

    public long getAlarmCpuThreshold()
    {
        return alarmCpuThreshold_;
    }

    public void setAlarmCpuThreshold(final long alarmCpuThreshold)
    {
        alarmCpuThreshold_ = alarmCpuThreshold;
    }

    public String getTransactionGraph()
    {
        return Boolean.toString(transactionGraph_);
    }

    public void setTransactionGraph(final boolean transactionGraph)
    {
        transactionGraph_ = transactionGraph;
    }

    public String getProfileValueChanged()
    {
        return profileValueChanged_;
    }

    public void setProfileValueChanged(final String changed)
    {
        profileValueChanged_ = changed;
    }

    public String getClassMethodId()
    {
        return classMethodId_;
    }

    public void setClassMethodId(final String id)
    {
        classMethodId_ = id;
    }
}
