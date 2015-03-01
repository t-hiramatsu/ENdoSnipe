/*******************************************************************************
 * ENdoSnipe 5.3 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.util.StringUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

import org.apache.commons.lang.StringUtils;

/**
 * クライアントで保存するメソッド情報です。<br />
 * 
 * @author hitramatsu
 *
 */
public class InvocationModel implements Comparable<InvocationModel>, NotificationListener,
        TelegramConstants
{
    /** 列：インデックス名 */
    private static final int INDEX_METHODNAME = 1;

    /** 列：クラス名 */
    private static final int INDEX_CLASSNAME = 0;

    /** 平均処理/CPU/USER時間表示用のフォーマット */
    private static final DecimalFormat AVERAGE_FORMAT = new DecimalFormat("0.000");

    /** 閾値が指定されていないことを表す値 */
    public static final long THRESHOLD_NOT_SPECIFIED = -1;

    /** コンポーネント */
    private ComponentModel component_;

    /** Javelin接続先 */
    private String targetAddress_;

    /** クラス名 */
    private String className_;

    /** メソッド名 */
    private String methodName_;

    /** 日付 */
    private Date date_;

    /** メソッドの呼び出し回数。 */
    private long count_;

    /** メソッドの積算最短処理時間（単位:ミリ秒）。 */
    private long accumulatedMinimum_;

    /** メソッドの積算最長処理時間（単位:ミリ秒）。 */
    private long accumulatedMaximum_;

    /** メソッドの積算合計処理時間（単位：ミリ秒）。 */
    private long accumulatedTotal_;

    /** メソッドの積算最短CPU処理時間（単位:ミリ秒）。 */
    private long accumulatedCpuMinimum_;

    /** メソッドの積算最長CPU処理時間（単位:ミリ秒）。 */
    private long accumulatedCpuMaximum_;

    /** メソッドの積算合計CPU処理時間（単位：ミリ秒）。 */
    private long accumulatedCpuTotal_;

    /** メソッドの積算最短CPU処理時間（単位:ミリ秒）。 */
    private long accumulatedUserMinimum_;

    /** メソッドの積算最長CPU処理時間（単位:ミリ秒）。 */
    private long accumulatedUserMaximum_;

    /** メソッドの積算合計CPU処理時間（単位：ミリ秒）。 */
    private long accumulatedUserTotal_;

    /** メソッドの最短処理時間（単位:ミリ秒）。 */
    private long minimum_;

    /** メソッドの最長処理時間（単位:ミリ秒）。 */
    private long maximum_;

    /** メソッドの合計処理時間（単位：ミリ秒）。 */
    private long total_;

    /** メソッドの最短CPU処理時間（単位:ミリ秒）。 */
    private long cpuMinimum_;

    /** メソッドの最長CPU処理時間（単位:ミリ秒）。 */
    private long cpuMaximum_;

    /** メソッドの合計CPU処理時間（単位：ミリ秒）。 */
    private long cpuTotal_;

    /** メソッドの最短USER処理時間（単位:ミリ秒）。 */
    private long userMinimum_;

    /** メソッドの最長USER処理時間（単位:ミリ秒）。 */
    private long userMaximum_;

    /** メソッドの合計USER処理時間（単位：ミリ秒）。 */
    private long userTotal_;

    /** メソッド内での例外発生回数。 */
    private long throwableCount_;

    /** メソッド内でのストール検出回数。 */
    private long methodStallCount_;

    /** HTTPステータスエラーの発生回数 */
    private long httpStatusCount_;

    /** ログ対象 */
    private boolean target_ = true;

    /** ルートノードの場合は <code>true</code> 、ルートノードでない場合は <code>false</code> */
    private boolean transactionGraphOutput_ = false;

    /** メソッド内で発生した例外の履歴。 */
    private List<Throwable> throwableList_;

    /** メソッドごとのアラーム閾値 */
    private long alarmThreshold_ = THRESHOLD_NOT_SPECIFIED;

    /** メソッドごとのCPUアラーム閾値 */
    private long alarmCpuThreshold_ = THRESHOLD_NOT_SPECIFIED;

    /** ユーザがこのオブジェクトのフィールド値を変更した場合は <code>true</code> */
    private boolean modifiedByUser_ = false;

    /** 閾値判定用の定数文字列 */
    private static final String EXCEED_THRESHOLD_ALARM = "Alarm:EXCEED_THRESHOLD";

    /** */
    private static final int MILLIS = 1000;

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
     * 対象URLを取得します。<br />
     * 
     * @return 対象URL
     */
    public String getTargetAddress()
    {
        return this.targetAddress_;
    }

    /**
     * 対象URLを設定します。<br />
     * 
     * @param targetAddress 対象URL
     */
    public void setTargetAddress(final String targetAddress)
    {
        this.targetAddress_ = targetAddress;
    }

    /**
     * メソッドの実行回数を取得します。<br />
     * 
     * @return メソッドの実行回数
     */
    public long getCount()
    {
        return this.count_;
    }

    /**
     * メソッドの実行回数を設定します。<br />
     * 
     * @param count メソッドの実行回数
     */
    public void setCount(final long count)
    {
        this.count_ = count;
    }

    /**
     * メソッドの平均実行時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均実行時間
     */
    public String getAccumulatedAverage()
    {
        return AVERAGE_FORMAT.format(getAccumulatedAverageDouble());
    }

    /**
     * メソッド平均実行時間をdouble型で返します。<br />
     * 
     * @return メソッド平均実行時間
     */
    public double getAccumulatedAverageDouble()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getAccumulatedTotal() / getCount();
        }
        return result;
    }

    /**
     * メソッドの総実行時間を取得します。<br />
     * 
     * @return メソッドの総実行時間
     */
    public long getAccumulatedTotal()
    {
        return this.accumulatedTotal_;
    }

    /**
     * メソッドの総実行時間を設定します。<br />
     * 
     * @param accumulatedTotal メソッドの総実行時間
     */
    public void setAccumulatedTotal(final long accumulatedTotal)
    {
        this.accumulatedTotal_ = accumulatedTotal;
    }

    /**
     * メソッドの実行時間の最大値を取得します。<br />
     * 
     * @return メソッドの実行時間の最大値
     */
    public long getAccumulatedMaximum()
    {
        return this.accumulatedMaximum_;
    }

    /**
     * メソッドの実行時間の最大値を設定します。<br />
     * 
     * @param accumulatedMaximum メソッドの実行時間の最大値
     */
    public void setAccumulatedMaximum(final long accumulatedMaximum)
    {
        this.accumulatedMaximum_ = accumulatedMaximum;
    }

    /**
     * メソッドの実行時間の最小値を取得します。<br />
     * 
     * @return メソッドの実行時間の最小値
     */
    public long getAccumulatedMinimum()
    {
        return this.accumulatedMinimum_;
    }

    /**
     * メソッドの実行時間の最小値を設定します。<br />
     * 
     * @param accumulatedMinimum メソッドの実行時間の最小値
     */
    public void setAccumulatedMinimum(final long accumulatedMinimum)
    {
        this.accumulatedMinimum_ = accumulatedMinimum;
    }

    /**
     * メソッドの平均実行時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均実行時間
     */
    public String getAccumulatedCpuAverage()
    {
        return AVERAGE_FORMAT.format(getAccumulatedCpuAverageDouble());
    }

    /**
     * メソッド平均実行時間をdouble型で返します。<br />
     * 
     * @return メソッド平均実行時間
     */
    public double getAccumulatedCpuAverageDouble()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getAccumulatedCpuTotal() / getCount();
        }
        return result;
    }

    /**
     * メソッドのCPU総実行時間を取得します。<br />
     * 
     * @return メソッドのCPU総実行時間
     */
    public long getAccumulatedCpuTotal()
    {
        return this.accumulatedCpuTotal_;
    }

    /**
     * メソッドのCPU総実行時間を設定します。<br />
     * 
     * @param accumulatedCpuTotal メソッドのCPU総実行時間
     */
    public void setAccumulatedCpuTotal(final long accumulatedCpuTotal)
    {
        this.accumulatedCpuTotal_ = accumulatedCpuTotal;
    }

    /**
     * メソッドのCPU実行時間の最小値を取得します。<br />
     * 
     * @return メソッドのCPU実行時間の最小値
     */
    public long getAccumulatedCpuMinimum()
    {
        return this.accumulatedCpuMinimum_;
    }

    /**
     * メソッドのCPU実行時間の最小値を設定します。<br />
     * 
     * @param accumulatedCpuMinimum メソッドのCPU実行時間の最小値
     */
    public void setAccumulatedCpuMinimum(final long accumulatedCpuMinimum)
    {
        this.accumulatedCpuMinimum_ = accumulatedCpuMinimum;
    }

    /**
     * メソッドのCPU実行時間の最大値を取得します。<br />
     * 
     * @return メソッドのCPU実行時間の最大値
     */
    public long getAccumulatedCpuMaximum()
    {
        return this.accumulatedCpuMaximum_;
    }

    /**
     * メソッドのCPU実行時間の最大値を設定します。<br />
     * 
     * @param accumulatedCpuMaximum メソッドのCPU実行時間の最大値
     */
    public void setAccumulatedCpuMaximum(final long accumulatedCpuMaximum)
    {
        this.accumulatedCpuMaximum_ = accumulatedCpuMaximum;
    }

    /**
     * メソッドの平均実行時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均実行時間
     */
    public String getAccumulatedUserAverage()
    {
        return AVERAGE_FORMAT.format(getAccumulatedUserAverageDouble());
    }

    /**
     * メソッド平均実行時間をdouble型で返します。<br />
     * 
     * @return メソッド平均実行時間
     */
    public double getAccumulatedUserAverageDouble()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getAccumulatedUserTotal() / getCount();
        }
        return result;
    }

    /**
     * メソッドのユーザ総実行時間を取得します。<br />
     * 
     * @return メソッドのユーザ総実行時間
     */
    public long getAccumulatedUserTotal()
    {
        return this.accumulatedUserTotal_;
    }

    /**
     * メソッドのユーザ総実行時間を設定します。<br />
     * 
     * @param accumulatedUserTotal メソッドのユーザ総実行時間
     */
    public void setAccumulatedUserTotal(final long accumulatedUserTotal)
    {
        this.accumulatedUserTotal_ = accumulatedUserTotal;
    }

    /**
     * メソッドのユーザ実行時間の最小値を取得します。<br />
     * 
     * @return メソッドのユーザ実行時間の最小値
     */
    public long getAccumulatedUserMinimum()
    {
        return this.accumulatedUserMinimum_;
    }

    /**
     * メソッドのユーザ実行時間の最小値を設定します。<br />
     * 
     * @param accumulatedUserMinimum メソッドのユーザ実行時間の最小値
     */
    public void setAccumulatedUserMinimum(final long accumulatedUserMinimum)
    {
        this.accumulatedUserMinimum_ = accumulatedUserMinimum;
    }

    /**
     * メソッドのユーザ実行時間の最大値を取得します。<br />
     * 
     * @return メソッドのユーザ実行時間の最大値
     */
    public long getAccumulatedUserMaximum()
    {
        return this.accumulatedUserMaximum_;
    }

    /**
     * メソッドのユーザ実行時間の最大値を設定します。<br />
     * 
     * @param accumulatedUserMaximum メソッドのユーザ実行時間の最大値
     */
    public void setAccumulatedUserMaximum(final long accumulatedUserMaximum)
    {
        this.accumulatedUserMaximum_ = accumulatedUserMaximum;
    }

    /**
     * メソッドの平均実行時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均実行時間
     */
    public String getAverage()
    {
        return AVERAGE_FORMAT.format(getAverageDouble());
    }

    /**
     * メソッド平均実行時間をdouble型で返します。<br />
     * 
     * @return メソッド平均実行時間
     */
    public double getAverageDouble()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getTotal() / getCount();
        }
        return result;
    }

    /**
     * メソッドの総実行時間を取得します。<br />
     * 
     * @return メソッドの総実行時間
     */
    public long getTotal()
    {
        return this.total_;
    }

    /**
     * メソッドの総実行時間を設定します。<br />
     * 
     * @param total メソッドの総実行時間
     */
    public void setTotal(final long total)
    {
        this.total_ = total;
    }

    /**
     * メソッドの実行時間の最大値を取得します。<br />
     * 
     * @return メソッドの実行時間の最大値
     */
    public long getMaximum()
    {
        return this.maximum_;
    }

    /**
     * メソッドの実行時間の最大値を設定します。<br />
     * 
     * @param maximum メソッドの実行時間の最大値
     */
    public void setMaximum(final long maximum)
    {
        this.maximum_ = maximum;
    }

    /**
     * メソッドの実行時間の最小値を取得します。<br />
     * 
     * @return メソッドの実行時間の最小値
     */
    public long getMinimum()
    {
        return this.minimum_;
    }

    /**
     * メソッドの実行時間の最小値を設定します。<br />
     * 
     * @param minimum メソッドの実行時間の最小値
     */
    public void setMinimum(final long minimum)
    {
        this.minimum_ = minimum;
    }

    /**
     * メソッドのCPU総実行時間を取得します。<br />
     * 
     * @return メソッドのCPU総実行時間
     */
    public long getCpuTotal()
    {
        return this.cpuTotal_;
    }

    /**
     * メソッドのCPU総実行時間を設定します。<br />
     * 
     * @param cpuTotal メソッドのCPU総実行時間
     */
    public void setCpuTotal(final long cpuTotal)
    {
        this.cpuTotal_ = cpuTotal;
    }

    /**
     * メソッドのCPU実行時間の最小値を取得します。<br />
     * 
     * @return メソッドのCPU実行時間の最小値
     */
    public long getCpuMinimum()
    {
        return this.cpuMinimum_;
    }

    /**
     * メソッドのCPU実行時間の最小値を設定します。<br />
     * 
     * @param cpuMinimum メソッドのCPU実行時間の最小値
     */
    public void setCpuMinimum(final long cpuMinimum)
    {
        this.cpuMinimum_ = cpuMinimum;
    }

    /**
     * メソッドのCPU実行時間の最大値を取得します。<br />
     * 
     * @return メソッドのCPU実行時間の最大値
     */
    public long getCpuMaximum()
    {
        return this.cpuMaximum_;
    }

    /**
     * メソッドのCPU実行時間の最大値を設定します。<br />
     * 
     * @param cpuMaximum メソッドのCPU実行時間の最大値
     */
    public void setCpuMaximum(final long cpuMaximum)
    {
        this.cpuMaximum_ = cpuMaximum;
    }

    /**
     * メソッドの平均 CPU 時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     *
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均 CPU 時間
     */
    public String getCpuAverage()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getCpuTotal() / getCount();
        }
        return AVERAGE_FORMAT.format(result);
    }

    /**
     * ユーザ時間の最小値を取得します。<br />
     * 
     * @return ユーザ時間の最小値
     */
    public long getUserMinimum()
    {
        return this.userMinimum_;
    }

    /**
     * ユーザ時間の最小値を設定します。<br />
     * 
     * @param userMinimum ユーザ時間の最小値
     */
    public void setUserMinimum(final long userMinimum)
    {
        this.userMinimum_ = userMinimum;
    }

    /**
     * ユーザ時間の最大値を取得します。<br />
     * 
     * @return ユーザ時間の最大値
     */
    public long getUserMaximum()
    {
        return this.userMaximum_;
    }

    /**
     * ユーザ時間の最小値を設定します。<br />
     * 
     * @param userMaximum ユーザ時間の最大値
     */
    public void setUserMaximum(final long userMaximum)
    {
        this.userMaximum_ = userMaximum;
    }

    /**
     * 総ユーザ時間を取得します。<br />
     * 
     * @return 総ユーザ時間
     */
    public long getUserTotal()
    {
        return this.userTotal_;
    }

    /**
     * 総ユーザ時間を設定します。<br />
     * 
     * @param userTotal 総ユーザ時間
     */
    public void setUserTotal(final long userTotal)
    {
        this.userTotal_ = userTotal;
    }

    /**
     * メソッドの平均 USER 時間を小数点以下３桁固定表示形式で文字列として返します。<br />
     *
     * まだメソッドが 1 回も実行されていない場合は、 <code>0</code> を返します。<br />
     *
     * @return メソッドの平均 USER 時間
     */
    public String getUserAverage()
    {
        double result;

        if (getCount() == 0)
        {
            result = 0;
        }
        else
        {
            result = (double)getUserTotal() / getCount();
        }
        return AVERAGE_FORMAT.format(result);
    }

    /**
     * 例外発生回数を取得します。<br />
     * 
     * @return 例外発生回数
     */
    public long getThrowableCount()
    {
        return this.throwableCount_;
    }

    /**
     * 例外発生回数を設定します。<br />
     * 
     * @param throwableCount 例外発生回数
     */
    public void setThrowableCount(final long throwableCount)
    {
        this.throwableCount_ = throwableCount;
    }

    /**
     * メソッド内で発生した例外の履歴を取得します。<br />
     * 
     * @return メソッド内で発生した例外の履歴
     */
    public List<Throwable> getThrowableList()
    {
        return this.throwableList_;
    }

    /**
     * メソッド内で発生した例外の履歴を設定します。<br />
     * 
     * @param throwableList メソッド内で発生した例外の履歴
     */
    public void setThrowableList(final List<Throwable> throwableList)
    {
        this.throwableList_ = throwableList;
    }

    /**
     * ストール検出回数を取得します。<br />
     * 
     * @return ストール検出回数
     */
    public long getMethodStallCount()
    {
        return this.methodStallCount_;
    }

    /**
     * ストール検出回数を設定します。<br />
     * 
     * @param methodStall ストール検出回数
     */
    public void setMethodStallCount(final long methodStall)
    {
        this.methodStallCount_ = methodStall;
    }

    /**
     * コンポーネントを取得します。<br />
     * 
     * @return コンポーネント
     */
    public ComponentModel getComponent()
    {
        return this.component_;
    }

    /**
     * コンポーネントを設定します。<br />
     * 
     * @param component コンポーネント
     */
    public void setComponent(final ComponentModel component)
    {
        this.component_ = component;
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

    /**
     * 日時を取得します。<br />
     * 
     * @return 日時
     */
    public Date getDate()
    {
        return this.date_;
    }

    /**
     * 日時を設定します。<br />
     * 
     * @param date 日時
     */
    public void setDate(final Date date)
    {
        this.date_ = date;
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
     * ユーザがこのオブジェクトのフィールド値を変更したかどうかを返します。<br />
     *
     * @return ユーザがこのオブジェクトのフィールド値を変更した場合は <code>true</code>
     */
    public boolean isModifiedByUser()
    {
        return this.modifiedByUser_;
    }

    /**
     * ユーザがこのオブジェクトのフィールド値を変更したかどうかをセットします。<br />
     *
     * @param modified ユーザがこのオブジェクトのフィールド値を変更した場合は <code>true</code>
     */
    public void setModifiedByUser(final boolean modified)
    {
        this.modifiedByUser_ = modified;
    }

    public void setHttpStatusCount(final long httpStatusCount)
    {
        httpStatusCount_ = httpStatusCount;
    }

    public long getHttpStatusCount()
    {
        return httpStatusCount_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("呼び出し回数=");
        builder.append(getCount());
        builder.append(", 平均処理時間=");
        builder.append(getAverage());
        builder.append(", 最大処理時間=");
        builder.append(getMaximum());
        builder.append(", 最小処理時間=");
        builder.append(getMinimum());
        builder.append(", 例外発生回数=");
        builder.append(getThrowableCount());
        builder.append(", 計測対象=");
        builder.append(isTarget());
        builder.append(", レスポンスグラフ出力対象=");
        builder.append(isResponseGraphOutput());

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(final InvocationModel obj)
    {
        return this.getMethodName().compareTo(obj.getMethodName());
    }

    /**
     * {@inheritDoc}
     *
     * View タブ上に表示される項目（メソッド名、最大時間、平均時間）で比較します。<br />
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof InvocationModel)
        {
            InvocationModel invocation = (InvocationModel)obj;
            String className = invocation.getClassName();
            String methodName = invocation.getMethodName();
            String average = invocation.getAverage();
            long maximum = invocation.getMaximum();
            boolean equal =
                    (getClassName().equals(className) && getMethodName().equals(methodName)
                            && getAverage().equals(average) && getMaximum() == maximum);
            return equal;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return getMethodName().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public void handleNotification(final Notification notification, final Object handback)
    {
        if (notification instanceof AttributeChangeNotification)
        {
            String alarmMsg = ((AttributeChangeNotification)notification).getMessage();
            if (EXCEED_THRESHOLD_ALARM.equals(alarmMsg) == true)
            {
                handleExceedThresholdAlarm((AttributeChangeNotification)notification);
            }
        }
        throw new RuntimeException("aaaa");

    }

    private void handleExceedThresholdAlarm(final AttributeChangeNotification notification)
    {
        //setAverage((Long)notification.getOldValue());
        setMaximum((Long)notification.getNewValue());
        this.component_.setExceededThresholdAlarm(this.methodName_);
    }

    /**
     * 電文から {@link InvocationModel} のリストを生成します。<br />
     *
     * @param telegram 電文
     * @param alarmThreshold アラームの閾値
     * @param warningThreshold 警告の閾値
     * @return {@link InvocationModel} のリスト
     */
    public static InvocationModel[] createFromTelegram(final Telegram telegram,
            final long alarmThreshold, final long warningThreshold)
    {
        Map<String, InvocationModel> invocationMap = new HashMap<String, InvocationModel>();

        Body[] objBody = telegram.getObjBody();
        String strClassMethodName = null;
        for (int index = 0; index < objBody.length; index++)
        {
            ResponseBody responseBody = (ResponseBody)objBody[index];

            if (responseBody.getStrObjName() == null)
            {
                continue;
            }

            // 全て呼び出す元を設定用
            // データをInvocationModelに設定する
            String strItemName = ((ResponseBody)objBody[index]).getStrItemName();
            Object[] objTempArr = ((ResponseBody)objBody[index]).getObjItemValueArr();

            // 説明の配列長が0であればスキップする
            if (objTempArr.length == 0)
            {
                continue;
            }

            if (StringUtils.isEmpty(responseBody.getStrObjName()) == false)
            {
                strClassMethodName = responseBody.getStrObjName();
            }
            InvocationModel invocation = invocationMap.get(strClassMethodName);
            if (invocation == null)
            {
                // 取得した後、一つInvocationModelを作って、データを設定する
                invocation = new InvocationModel();
                invocation.setDate(new Date());
                // 対象名より、クラス名、メソッド名を取得する
                if (strClassMethodName == null)
                {
                    continue;
                }
                List<String> strClassMethodNameArr =
                        StringUtil.split(strClassMethodName, CLASSMETHOD_SEPARATOR);
                String strClassName = "unknown";
                String strMethodName = "unknown";
                if (strClassMethodNameArr.size() > INDEX_METHODNAME)
                {
                    strClassName = strClassMethodNameArr.get(INDEX_CLASSNAME);
                    strMethodName = strClassMethodNameArr.get(INDEX_METHODNAME);
                }
                invocation.setClassName(strClassName);
                invocation.setMethodName(strMethodName);

                invocationMap.put(strClassMethodName, invocation);
            }

            Long value = 0L;
            String valid = "false";
            if (responseBody.getByteItemMode() == ItemType.ITEMTYPE_LONG)
            {
                value = (Long)objTempArr[0];
            }
            else if (responseBody.getByteItemMode() == ItemType.ITEMTYPE_STRING)
            {
                valid = (String)objTempArr[0];
            }

            // 呼び出し回数
            if (ITEMNAME_CALL_COUNT.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setCount(value);
            }
            // 最大処理時間
            else if (ITEMNAME_ACCUMULATED_MAXIMUM_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setAccumulatedMaximum(value);
            }
            // 最小処理時間
            else if (ITEMNAME_ACCUMULATED_MINIMUM_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setAccumulatedMinimum(value);
            }
            // 合計処理時間
            else if (ITEMNAME_ACCUMULATED_TOTAL_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setAccumulatedTotal(value);
            }
            // 最大CPU時間
            else if (ITEMNAME_ACCUMULATED_MAXIMUM_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedCpuMaximum(value);
            }
            // 最小CPU時間
            else if (ITEMNAME_ACCUMULATED_MINIMUM_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedCpuMinimum(value);
            }
            // 合計CPU時間
            else if (ITEMNAME_ACCUMULATED_TOTAL_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedCpuTotal(value);
            }
            // 最大ユーザ時間
            else if (ITEMNAME_ACCUMULATED_MAXIMUM_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedUserMaximum(value);
            }
            // 最小ユーザ時間
            else if (ITEMNAME_ACCUMULATED_MINIMUM_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedUserMinimum(value);
            }
            // 合計ユーザ時間
            else if (ITEMNAME_ACCUMULATED_TOTAL_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setAccumulatedUserTotal(value);
            }
            // 最大処理時間
            else if (ITEMNAME_MAXIMUM_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setMaximum(value);
            }
            // 最小処理時間
            else if (ITEMNAME_MINIMUM_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setMinimum(value);
            }
            // 合計処理時間
            else if (ITEMNAME_TOTAL_INTERVAL.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setTotal(value);
            }
            // 最大CPU時間
            else if (ITEMNAME_MAXIMUM_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setCpuMaximum(value);
            }
            // 最小CPU時間
            else if (ITEMNAME_MINIMUM_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setCpuMinimum(value);
            }
            // 合計CPU時間
            else if (ITEMNAME_TOTAL_CPU_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setCpuTotal(value);
            }
            // 最大USER時間
            else if (ITEMNAME_MAXIMUM_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setUserMaximum(value);
            }
            // 最小USER時間
            else if (ITEMNAME_MINIMUM_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setUserMinimum(value);
            }
            // 合計USER時間
            else if (ITEMNAME_TOTAL_USER_INTERVAL.equals(strItemName))
            {
                value = getValidMegaValue(value);
                invocation.setUserTotal(value);
            }
            // 例外発生回数
            else if (ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setThrowableCount(value);
            }
            // ストール検出回数
            else if (ITEMNAME_JAVAPROCESS_METHOD_STALL_COUNT.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setMethodStallCount(value);
            }
            // メソッドの呼び出し元 クラス名については未実装
            // CHECKSTYLE:OFF
            else if (ITEMNAME_ALL_CALLER_NAMES.equals(strItemName))
            {
                // 未実装
            }
            // CHECKSTYLE:ON
            else if (ITEMNAME_TARGET.equals(strItemName))
            {
                invocation.setTarget(Boolean.valueOf(valid));
            }
            // このメソッドがトランザクショングラフ出力対象かどうか
            else if (ITEMNAME_TRANSACTION_GRAPH.equals(strItemName))
            {
                invocation.setResponseGraphOutput(Boolean.valueOf(valid));
            }
            // TATアラーム閾値
            else if (ITEMNAME_ALARM_THRESHOLD.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setAlarmThreshold(value);
            }
            // CPUアラーム閾値
            else if (ITEMNAME_ALARM_CPU_THRESHOLD.equals(strItemName))
            {
                value = getValidValue(value);
                invocation.setAlarmCpuThreshold(value);
            }
            /*            else if (ITEMNAME_JAVAPROCESS_HTTP_EXCEPTION.equals(strItemName))
                        {
                            value = getValidValue(value);
                            invocation.setHttpStatusCount(value);
                        }
                        */
        }

        Collection<InvocationModel> invocationList = invocationMap.values();
        return invocationList.toArray(new InvocationModel[invocationList.size()]);
    }

    private static Long getValidMegaValue(Long value)
    {
        if (value < 0)
        {
            value = Long.valueOf(-1);
        }
        else
        {
            value /= (MILLIS * MILLIS);
        }
        return value;
    }

    private static Long getValidValue(Long value)
    {
        if (value < 0)
        {
            value = Long.valueOf(-1);
        }
        return value;
    }
}
