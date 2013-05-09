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
package jp.co.acroquest.endosnipe.report.controller;

import jp.co.acroquest.endosnipe.report.controller.processor.ReportPublishProcessorBase;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;
import jp.co.acroquest.endosnipe.report.controller.ReportType;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 進捗状況（プログレスバー）のコントロールを行う。
 * 
 * @author M.Yoshida
 */
public class ProgressController
{
    /** プログレスバーに表示するタイトル */
    private static final String REPORT_TASK_TITLE       = "reporter.report.progress.title";

    /** プログレスバーに表示するメッセージテンプレートのキー */
    private static final String REPORT_SUBTASK_TEMPLATE = "reporter.report.progress.detail";

    /** プログレスバーに状況を通知するためのモニタ */
    private IProgressMonitor    progressMonitor_;

    /** 全体で実行するレポートプロセッサの数 */
    private int                 runnableProcessorNum_;
    
    /** 現在動作している工程のインデックス*/
    private int                 nowPhase_;

    /** 現在動作しているプロセッサが処理するレポート種別 */
    private ReportType          nowReportType_;

    /**
     * コンストラクタ
     * @param monitor    プログレスモニター
     * @param processNum 処理するレポートプロセッサの数
     */
    public ProgressController(IProgressMonitor monitor, int processNum)
    {
        progressMonitor_ = monitor;
        runnableProcessorNum_ = processNum;
        nowPhase_ = 0;
    }

    /**
     * 処理を開始する。
     */
    public void beginTask()
    {
        String taskName = ReporterConfigAccessor.getProperty(REPORT_TASK_TITLE);

        // 総タスク数は、（出力するレポートの数）×（レポートプロセッサの工程の数）である。
        // ただし、進捗率が100%にならないよう、1多く設定しておく。
        int totalWork = runnableProcessorNum_ * ReportPublishProcessorBase.PROCESS_PHASE_NUM;
        progressMonitor_.beginTask(taskName, totalWork + 1);
        nowPhase_ = ReportPublishProcessorBase.PROCESS_PHASE_NUM;
    }

    /**
     * 次のレポートプロセッサを開始する。
     * 
     * @param type     レポートプロセッサが処理するレポートの種別
     */
    public void startProcessor(ReportType type)
    {
        nowReportType_ = type;

        String reportName = ReporterConfigAccessor.getReportName(nowReportType_);
        String subTaskName =
                             ReporterConfigAccessor.getPropertyWithParam(REPORT_SUBTASK_TEMPLATE,
                                                                         reportName, "");
        
        // 前のレポートプロセッサでnextPhaseメソッドが工程数回呼ばれていない場合、その分だけ進捗を補正する。
        progressMonitor_.subTask(subTaskName);
        if (nowPhase_ < ReportPublishProcessorBase.PROCESS_PHASE_NUM)
        {
        	progressMonitor_.worked(ReportPublishProcessorBase.PROCESS_PHASE_NUM - nowPhase_);
        }
        
        nowPhase_ = 0;
    }

    /**
     * 次工程に進む
     * @param detailKey 工程の内容を示すプロパティのキー
     */
    public void nextPhase(String detailKey)
    {
        String reportName = ReporterConfigAccessor.getReportName(nowReportType_);
        String detailStatus = ReporterConfigAccessor.getProperty(detailKey);
        String subTaskName = ReporterConfigAccessor.getPropertyWithParam(REPORT_SUBTASK_TEMPLATE,
                                                                         reportName, detailStatus);

        // 総タスク数に対する進捗を1増やす。
        progressMonitor_.worked(1);
        nowPhase_++;
        progressMonitor_.subTask(subTaskName);
    }

    /**
     * 処理を終了する。
     */
    public void endTask()
    {
    	// この進捗の増加で、進捗率が100%になる。
    	progressMonitor_.worked(1);
        progressMonitor_.done();
    }

    /**
     * キャンセルが行われているかチェックする。
     * 
     * @return キャンセルされていればtrue。そうでなければfalse。
     */
    public boolean isCanceled()
    {
        return progressMonitor_.isCanceled();
    }
}
