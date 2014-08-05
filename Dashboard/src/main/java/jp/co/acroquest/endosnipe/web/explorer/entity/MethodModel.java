/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.dto.MethodModelDto;
import jp.co.acroquest.endosnipe.web.explorer.util.ComponentModelUtil;

/**
 * クラス構造のメソッドを表すクラス。<br />
 *
 * @see StructureModel
 * @see ClassModel
 *
 * @author sakamoto
 */
public class MethodModel implements Comparable<MethodModel>
{
    /** パッケージ名 */
    private String packageName_;

    /** クラス名 */
    private String className_;

    /** メソッド名 */
    private String methodName_;

    /** メソッドの呼び出し回数 */
    private long callCount_;

    /** メソッドの最終呼び出しにおける処理時間（ミリ秒） */
    private long current_;

    /** メソッドの最短処理時間（ミリ秒） */
    private long minimum_;

    /** メソッドの最長処理時間（ミリ秒） */
    private long maximum_;

    /** メソッドの合計実行時間（ミリ秒） */
    private long total_;

    /** メソッドの最短 CPU 処理時間（ミリ秒） */
    private long cpuMinimum_;

    /** メソッドの最長 CPU 処理時間（ミリ秒） */
    private long cpuMaximum_;

    /** メソッドの合計 CPU 処理時間（ミリ秒） */
    private long cpuTotal_;

    /** メソッドの最短ユーザ処理時間（ミリ秒） */
    private long userMinimum_;

    /** メソッドの最長ユーザ時間（ミリ秒） */
    private long userMaximum_;

    /** メソッドの合計ユーザ時間（ミリ秒） */
    private long userTotal_;

    /** メソッドが計測対象かどうか */
    private boolean target_;

    private Date date_;

    /** メソッド内での例外発生回数 */
    private long throwableCount_;

    /** メソッド内で発生した例外の履歴 */
    private List<Throwable> throwableList_;

    /** メソッド内でのストール検出回数 */
    private long methodStallCount_;

    /** HTTPステータスエラーの発生回数 */
    private long httpStatusCount_;

    private long alarmThreshold_;

    private long alarmCpuThreshold_;

    private boolean transactionGraph_;

    /**
     * メソッドを表すオブジェクトを作成します。<br />
     *
     * @param methodName メソッド名
     */
    public MethodModel(final String methodName)
    {
        this.methodName_ = methodName;
        this.target_ = true;
        this.throwableList_ = new ArrayList<Throwable>();
    }

    /**
     * コピーコンストラクタ。
     *
     * @param methodModel コピー元
     */
    public MethodModel(final MethodModel methodModel)
    {
        this.callCount_ = methodModel.callCount_;
        this.className_ = methodModel.className_;
        this.cpuMaximum_ = methodModel.cpuMaximum_;
        this.cpuMinimum_ = methodModel.cpuMinimum_;
        this.cpuTotal_ = methodModel.cpuTotal_;
        this.current_ = methodModel.current_;
        this.date_ = new Date(methodModel.date_.getTime());
        this.maximum_ = methodModel.maximum_;
        this.methodName_ = methodModel.methodName_;
        this.minimum_ = methodModel.minimum_;
        this.packageName_ = methodModel.packageName_;
        this.target_ = methodModel.target_;
        this.throwableCount_ = methodModel.throwableCount_;
        this.throwableList_ = new ArrayList<Throwable>(methodModel.throwableList_);
        this.methodStallCount_ = methodModel.methodStallCount_;
        this.total_ = methodModel.total_;
    }

    /**
     * パッケージ名をセットします。<br />
     *
     * @param packageName クラス名
     */
    public void setPackageName(final String packageName)
    {
        this.packageName_ = packageName;
    }

    /**
     * パッケージ名を返します。<br />
     *
     * @return パッケージ名
     */
    public String getPackageName()
    {
        return this.packageName_;
    }

    /**
     * パッケージ名を含むクラス名をセットします。<br />
     *
     * @param fullClassName パッケージ名を含むクラス名
     */
    public void setFullClassName(final String fullClassName)
    {
        String[] packageAndClass = ComponentModelUtil.splitPackageAndClass(fullClassName);
        this.packageName_ = packageAndClass[0];
        this.className_ = packageAndClass[1];
    }

    /**
     * パッケージ名を含まないクラス名をセットします。<br />
     *
     * @param className パッケージ名を含まないクラス名
     */
    public void setClassName(final String className)
    {
        this.className_ = className;
    }

    /**
     * パッケージ名を含まないクラス名を返します。<br />
     *
     * @return パッケージ名を含まないクラス名
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * パッケージ名を含むクラス名を返します。<br />
     *
     * @return パッケージ名を含むクラス名
     */
    public String getFullClassName()
    {
        return ComponentModelUtil.getFullClassName(this.packageName_, this.className_);
    }

    public void setMethodName(final String methodName)
    {
        this.methodName_ = methodName;
    }

    /**
     * メソッド名を返します。<br />
     *
     * @return メソッド名
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * メソッドの呼び出し回数をセットします。<br />
     *
     * @param callCount 呼び出し回数
     */
    public void setCallCount(final long callCount)
    {
        this.callCount_ = callCount;
    }

    /**
     * メソッドの呼び出し回数を返します。<br />
     *
     * @return 呼び出し回数
     */
    public long getCallCount()
    {
        return this.callCount_;
    }

    /**
     * メソッドの最終呼び出しにおける処理時間をセットします。<br />
     *
     * @param current 最終呼び出しにおける処理時間（ミリ秒）
     */
    public void setCurrent(final long current)
    {
        this.current_ = current;
    }

    /**
     * メソッドの最終呼び出しにおける処理時間を返します。<br />
     *
     * @return 最終呼び出しにおける処理時間（ミリ秒）
     */
    public long getCurrent()
    {
        return this.current_;
    }

    /**
     * メソッドの最短処理時間をセットします。<br />
     * 
     * @param minimum 最短処理時間（ミリ秒） 
     */
    public void setMinimum(final long minimum)
    {
        this.minimum_ = minimum;
    }

    /**
     * メソッドの最短処理時間を返します。<br />
     *
     * @return 最短処理時間
     */
    public long getMinimum()
    {
        return this.minimum_;
    }

    /**
     * メソッドの最長処理時間をセットします。<br />
     * 
     * @param maximum 最長処理時間（ミリ秒） 
     */
    public void setMaximum(final long maximum)
    {
        this.maximum_ = maximum;
    }

    /**
     * メソッドの最長処理時間を返します。<br />
     *
     * @return 最長処理時間
     */
    public long getMaximum()
    {
        return this.maximum_;
    }

    /**
     * メソッドの合計実行時間をセットします。<br />
     *
     * @param total 合計実行時間（ミリ秒）
     */
    public void setTotal(final long total)
    {
        this.total_ = total;
    }

    /**
     * メソッドの合計実行時間を返します。<br />
     *
     * @return 合計実行時間（ミリ秒）
     */
    public long getTotal()
    {
        return this.total_;
    }

    /**
     * メソッドの平均実行時間を返します。<br />
     *
     * @return 平均実行時間（ミリ秒）
     */
    public double getAverage()
    {
        if (getCallCount() == 0)
        {
            return 0;
        }
        return (double)this.total_ / getCallCount();
    }

    /**
     * メソッドの最短 CPU 処理時間をセットします。<br />
     * 
     * @param cpuMinimum 最短 CPU 処理時間（ミリ秒） 
     */
    public void setCpuMinimum(final long cpuMinimum)
    {
        this.cpuMinimum_ = cpuMinimum;
    }

    /**
     * メソッドの最短 CPU 処理時間を返します。<br />
     *
     * @return 最短 CPU 処理時間
     */
    public long getCpuMinimum()
    {
        return this.cpuMinimum_;
    }

    /**
     * メソッドの最長 CPU 処理時間をセットします。<br />
     * 
     * @param cpuMaximum 最長 CPU 処理時間（ミリ秒） 
     */
    public void setCpuMaximum(final long cpuMaximum)
    {
        this.cpuMaximum_ = cpuMaximum;
    }

    /**
     * メソッドの最長 CPU 処理時間を返します。<br />
     *
     * @return 最長 CPU 処理時間
     */
    public long getCpuMaximum()
    {
        return this.cpuMaximum_;
    }

    /**
     * メソッドの合計 CPU 処理時間をセットします。<br />
     *
     * @param cpuTotal 合計 CPU 処理時間
     */
    public void setCpuTotal(final long cpuTotal)
    {
        this.cpuTotal_ = cpuTotal;
    }

    /**
     * メソッドの合計 CPU 処理時間を返します。<br />
     *
     * @return 合計 CPU 処理時間
     */
    public long getCpuTotal()
    {
        return this.cpuTotal_;
    }

    /**
     * メソッドの平均 CPU 処理時間を返します。<br />
     *
     * @return 平均 CPU 処理時間
     */
    public long getCpuAverage()
    {
        if (getCallCount() == 0)
        {
            return 0;
        }
        return this.cpuTotal_ / getCallCount();
    }

    /**
     * メソッドの最短ユーザ時間をセットします。<br />
     * 
     * @param userMinimum 最短ユーザ時間（ミリ秒） 
     */
    public void setUserMinimum(final long userMinimum)
    {
        this.userMinimum_ = userMinimum;
    }

    /**
     * メソッドの最短ユーザ時間を返します。<br />
     *
     * @return 最短ユーザ時間
     */
    public long getUserMinimum()
    {
        return this.userMinimum_;
    }

    /**
     * メソッドの最長ユーザ時間をセットします。<br />
     * 
     * @param userMaximum 最長ユーザ時間（ミリ秒） 
     */
    public void setUserMaximum(final long userMaximum)
    {
        this.userMaximum_ = userMaximum;
    }

    /**
     * メソッドの最長ユーザ時間を返します。<br />
     *
     * @return 最長ユーザ時間
     */
    public long getUserMaximum()
    {
        return this.userMaximum_;
    }

    /**
     * メソッドの合計ユーザ時間をセットします。<br />
     *
     * @param userTotal 合計ユーザ時間
     */
    public void setUserTotal(final long userTotal)
    {
        this.userTotal_ = userTotal;
    }

    /**
     * メソッドの合計ユーザ時間を返します。<br />
     *
     * @return 合計ユーザ時間
     */
    public long getUserTotal()
    {
        return this.userTotal_;
    }

    /**
     * メソッドの平均ユーザ時間を返します。<br />
     *
     * @return 平均ユーザ時間
     */
    public long getUserAverage()
    {
        if (getCallCount() == 0)
        {
            return 0;
        }
        return this.userTotal_ / getCallCount();
    }

    /**
     * 日時をセットします。<br />
     *
     * @param date 日時
     */
    public void setDate(final Date date)
    {
        this.date_ = date;
    }

    /**
     * 日時を返します。<br />
     *
     * @return 日時
     */
    public Date getDate()
    {
        return this.date_;
    }

    /**
     * メソッド内での例外発生回数をセットします。<br />
     *
     * @param throwableCount 例外発生回数
     */
    public void setThrowableCount(final long throwableCount)
    {
        this.throwableCount_ = throwableCount;
    }

    /**
     * メソッド内での例外発生回数を返します。<br />
     *
     * @return 例外発生回数
     */
    public long getThrowableCount()
    {
        return this.throwableCount_;
    }

    /**
     * @param throwableList
     */
    public void setThrowableList(final List<Throwable> throwableList)
    {
        throwableList_ = throwableList;
    }

    /**
     * メソッド内で発生した例外の履歴を返します。<br />
     *
     * @return 例外の履歴
     */
    public List<Throwable> getThrowableList()
    {
        return Collections.unmodifiableList(this.throwableList_);
    }

    /**
     * メソッド内でのストール検出回数をセットします。<br />
     *
     * @param methodStallCount ストール検出回数
     */
    public void setMethodStallCount(final long methodStallCount)
    {
        this.methodStallCount_ = methodStallCount;
    }

    /**
     * メソッド内でのストール検出回数を返します。<br />
     *
     * @return 例外発生回数
     */
    public long getMethodStallCount()
    {
        return this.methodStallCount_;
    }

    /**
     * メソッドが計測対象かどうかを返します。<br />
     * 
     * @return メソッドが計測対象である場合、<code>true</code>
     */
    public boolean isTarget()
    {
        return this.target_;
    }

    /**
     * メソッドが計測対象かどうかを設定します。<br />
     * 
     * @param target メソッドが計測対象かどうか
     */
    public void setTarget(final boolean target)
    {
        this.target_ = target;
    }

    /**
     * メソッド内でのHTTPステータスエラーの発生回数をセットします。<br />
     * 
     * @param httpStatusCount HTTPステータスエラー発生回数
     */
    public void setHttpStatusCount(final long httpStatusCount)
    {
        httpStatusCount_ = httpStatusCount;
    }

    /**
     * メソッド内でのHTTPステータスエラーの発生回数を返します。<br />
     * 
     * @return HTTPステータスエラーの発生回数
     */
    public long getHttpStatusCount()
    {
        return httpStatusCount_;
    }

    /**
     * {@inheritDoc}
     *
     * メソッド名のハッシュコードを返します。
     */
    @Override
    public int hashCode()
    {
        return this.methodName_.hashCode();
    }

    /**
     * {@inheritDoc}
     *
     * メソッド名のみ比較します。
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof MethodModel)
        {
            MethodModel methodModel = (MethodModel)obj;
            return this.methodName_.equals(methodModel.getMethodName());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * メソッド名のみ比較します。
     */
    public int compareTo(final MethodModel methodModel)
    {
        return this.methodName_.compareTo(methodModel.getMethodName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getMethodName());
        builder.append("{呼び出し回数=");
        builder.append(getCallCount());
        builder.append(", 合計処理時間=");
        builder.append(getTotal());
        builder.append(", 平均処理時間=");
        builder.append(getAverage());
        builder.append(", 最大処理時間=");
        builder.append(getMaximum());
        builder.append(", 最小処理時間=");
        builder.append(getMinimum());
        builder.append(", 例外発生回数=");
        builder.append(getThrowableCount());
        builder.append(", ストール検出回数=");
        builder.append(getMethodStallCount());
        builder.append(", HTTPエラー発生回数=");
        builder.append(getHttpStatusCount());
        builder.append(", 計測対象=");
        builder.append(isTarget());
        builder.append("}");

        return builder.toString();
    }

    public MethodModelDto convertToDto()
    {
        MethodModelDto dto = new MethodModelDto();
        dto.setPackageName(packageName_);
        dto.setClassName(className_);
        dto.setMethodName(methodName_);
        dto.setCallCount(callCount_);
        dto.setMinimum(minimum_);
        dto.setMaximum(cpuMaximum_);
        dto.setTotal(cpuTotal_);
        dto.setAverage(getAverage());
        dto.setCpuMinimum(cpuMinimum_);
        dto.setCpuMaximum(cpuMaximum_);
        dto.setCpuTotal(cpuTotal_);
        dto.setCpuAverage(getCpuAverage());
        dto.setUserMinimum(userMinimum_);
        dto.setUserMaximum(userMaximum_);
        dto.setUserTotal(userTotal_);
        dto.setUserAverage(getUserAverage());
        dto.setTarget(target_);
        dto.setThrowableCount(throwableCount_);
        dto.setAlarmThreshold(alarmThreshold_);
        dto.setAlarmCpuThreshold(alarmCpuThreshold_);
        dto.setTransactionGraph(transactionGraph_);
        dto.setProfileValueChanged("false");
        String classMethod = className_ + "____" + methodName_;
        classMethod = classMethod.replaceAll("\r\n", "");
        classMethod = classMethod.replaceAll("\n", "");
        dto.setClassMethodId(classMethod);
        return dto;
    }

    public void setAlarmThreshold(final long alarmThreshold)
    {
        alarmThreshold_ = alarmThreshold;

    }

    public long getAlarmThreshold()
    {
        return alarmThreshold_;
    }

    public void setAlarmCpuThreshold(final long alarmCpuThreshold)
    {
        alarmCpuThreshold_ = alarmCpuThreshold;
    }

    public long getAlarmCpuThreshold()
    {
        return alarmCpuThreshold_;
    }

    public void setTransactionGraph(final boolean transactionGraph)
    {
        transactionGraph_ = transactionGraph;
    }

    public boolean isTransactionGraph()
    {
        return transactionGraph_;
    }
}
