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
package jp.co.acroquest.endosnipe.javelin.converter.thread.monitor;

import java.lang.management.ThreadInfo;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;

/**
 * スレッド監視を行う際に実行するTask。
 * 
 * @author eriguchi
 */
public interface ThreadMonitorTask
{
    /**
     * Taskを実行するかどうかを判定する。
     * 
     * @param prevThreadInfo 前回のスレッド情報。
     * @param threadInfo 現在のスレッド情報。
     * @return Taskを実行するかどうか。
     */
    boolean isTarget(ThreadInfo prevThreadInfo, ThreadInfo threadInfo);

    /**
     * 指定したnodeに情報を追加する。
     * 
     * @param node 対象のCallTreeNode。
     * @param threadInfo 現在のスレッド情報。
     * @param prevThreadInfo 前回のスレッド情報。
     * @param maxDepth 取得するスタックトレースの深さ。
     */
    void updateNode(CallTreeNode node, ThreadInfo threadInfo, ThreadInfo prevThreadInfo,
            int maxDepth);

    /**
     * 引数として与えられた情報を基にイベントを送信する。
     * 
     * @param threadId スレッドID
     * @param threadInfo 現在のスレッド情報。
     * @param prevThreadInfo 前回のスレッド情報。
     * @param maxDepth 取得するスタックトレースの深さ。
     */
    void sendEvent(final Long threadId, final ThreadInfo threadInfo,
            final ThreadInfo prevThreadInfo, final int maxDepth);

    /**
     * 前回のスレッド情報を用いて次回の判定までに保持する必要がある情報を更新する。
     * 
     * @param threadId スレッドID
     * @param prevThreadInfo 前回のスレッド情報。
     */
    void updateInfo(final Long threadId, final ThreadInfo prevThreadInfo);

    /**
     * 引数として指定されたスレッドIDの保持情報をクリアする。
     * 
     * @param threadId スレッドID
     */
    void clearInfo(final Long threadId);
}
