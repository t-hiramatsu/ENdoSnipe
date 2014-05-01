/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.javelin.converter.method.monitor;

import java.lang.management.ThreadInfo;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.JavelinTransformer;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.event.MethodStallEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ストールメソッドを監視するためのスレッドです。<br />
 * 
 * @author matsuoka
 *
 */
public class MethodStallMonitor implements Runnable
{
    /** Singletonオブジェクト */
    private static MethodStallMonitor instance__ = new MethodStallMonitor();

    /** Javelinの設定。 */
    private final JavelinConfig config_ = new JavelinConfig();
    
    /**
     * インスタンス化を阻止するプライベートコンストラクタ。
     */
    private MethodStallMonitor()
    {
        // Do Nothing.
    }

    /**
     * {@link MethodStallMonitor}オブジェクトを取得します。<br />
     * 
     * @return {@link MethodStallMonitor}オブジェクト
     */
    public static MethodStallMonitor getInstance()
    {
        return instance__;
    }

    /**
     * "javelin.method.stall.interval"の間隔ごとに、
     * メソッドがストールしているかどうか判定します。
     */
    public void run()
    {
        try
        {
            Thread.sleep(JavelinTransformer.WAIT_FOR_THREAD_START);
        }
        catch (Exception ex)
        {
            // CHECKSTYLE:OFF
            ;
            // CHECKSTYLE:ON
        }
        
        while (true)
        {
            try
            {
                int sleepTime = this.config_.getMethodStallInterval();
                Thread.sleep(sleepTime);
                
                if (this.config_.isMethodStallMonitor()) 
                {
                    int threshold = this.config_.getMethodStallThreshold();
                    checkMethodStall(threshold);
                }
            }
            catch (Throwable ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }
    
    /**
     * ストールメソッドの存在をチェックする。
     * @param threshold 閾値
     */
    private void checkMethodStall(int threshold)
    {
        long currentTime = System.currentTimeMillis();

        long []threadIds = ThreadUtil.getAllThreadIds();
        for (long id : threadIds)
        {
            CallTreeNode node = CallTreeRecorder.getNode(id);
            if (isStall(node, currentTime, threshold))
            {
                int depth = this.config_.getMethodStallTraceDepth();
                MethodStallEvent event =  createEvent(id, node, depth);
                StatsJavelinRecorder.addEvent(event);
                node.setStalled(true);
            }
        }
    }

    /**
     * 対象のCallTreeNodeがストールしているかを判定します。
     * @param node 判定対象のCallTreeNode
     * @param currentTime 現在時刻
     * @param threshold 閾値
     * @return ストールしていたら<code>true</code>を返します。
     */
    private boolean isStall(CallTreeNode node, long currentTime, int threshold)
    {
        if (node != null && !node.isStalled() && currentTime - node.getStartTime() > threshold)
        {
            return true;
        }
        return false;
    }
    
    /**
     * ストールメソッド検出イベントを作成します。
     * @param threadId メソッドがストールしているスレッドのID
     * @param node CallTreeNode
     * @param maxDepth 取得するスタックトレースの深さ
     * @return 作成したイベント
     */
    private MethodStallEvent createEvent(long threadId, CallTreeNode node, int maxDepth)
    {
        MethodStallEvent event = new MethodStallEvent();

        Invocation invocation = node.getInvocation();
        event.addParam(EventConstants.PARAM_METHOD_STALL_CLASS_NAME, invocation.getClassName());
        event.addParam(EventConstants.PARAM_METHOD_STALL_METHOD_NAME, invocation.getMethodName());
        
        ThreadInfo info = ThreadUtil.getThreadInfo(threadId, maxDepth);
        String stackTrace = ThreadUtil.getStackTrace(info.getStackTrace());
        event.addParam(EventConstants.PARAM_METHOD_STALL_STACKTRACE, stackTrace);
        
        return event;
    }
}
