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

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.ReporterPluginProvider;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishDispatcher;
import jp.co.acroquest.endosnipe.report.controller.processor.SummaryReportProcessor;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.controller.ProgressController;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportPublishTask;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * レポート出力処理を実行するタスクオブジェクト
 * 進捗率の通知を伴う。
 * 
 * @author M.Yoshida
 */
public class ReportPublishTask extends Job
{
    /** 検索条件 */
    private ReportSearchCondition        searchCondition_;

    /** レポート種別 */
    private ReportType[]                 publishTypes_;

    /** 終了時にコールバックする */
    private Runnable callback_;

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(
                                                                            ReportPublishTask.class,
                                                                            ReporterPluginProvider.INSTANCE);

    /**
     * コンストラクタ
     * 
     * @param cond        検索条件
     * @param publishType レポート種別
     * @param callback コールバック
     */
    public ReportPublishTask(ReportSearchCondition cond, ReportType[] publishType, Runnable callback)
    {
        super("ENdoSnipeReportPublish");
        List<ReportType> additionalTypes = new ArrayList<ReportType>();

        for (ReportType type : publishType)
        {
            additionalTypes.add(type);
        }

        searchCondition_ = cond;
        publishTypes_ = (ReportType[])additionalTypes.toArray(new ReportType[0]);
        callback_ = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IStatus run(IProgressMonitor monitor)
    {
        ProgressController progressCtrl = new ProgressController(monitor, publishTypes_.length);
        searchCondition_.setProgressController(progressCtrl);

        ReportPublishDispatcher dispatcher = ReportPublishDispatcher.getInstance();

        progressCtrl.beginTask();
        
        SummaryReportProcessor.setOutputFileTypeList(publishTypes_);

        for (ReportType type : publishTypes_)
        {
            ReportProcessReturnContainer retCont;
            progressCtrl.startProcessor(type);
            retCont = dispatcher.dispatch(type, searchCondition_);

            if (retCont.getHappendedError() != null)
            {
                if (retCont.getHappendedError() instanceof InterruptedException)
                {
                    return Status.CANCEL_STATUS;
                }

                LOGGER.log(LogIdConstants.REPORT_PUBLISH_STOPPED_WARN, retCont.getHappendedError(),
                           ReporterConfigAccessor.getReportName(type));
                continue;
            }
            if (monitor.isCanceled())
            {
                return Status.CANCEL_STATUS;
            }
        }

        if (this.callback_ != null)
        {
            this.callback_.run();
        }
        
        progressCtrl.endTask();

        return Status.OK_STATUS;
    }
}
