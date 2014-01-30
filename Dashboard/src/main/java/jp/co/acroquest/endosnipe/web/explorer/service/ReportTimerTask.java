/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

/**
 * Report timer task for time interval.
 * @author khinlay
 *
 */
@Service
public class ReportTimerTask extends QuartzJobBean
{
    /**
     * report schedule service
     */
    private ReportScheduleService runMeTask_;

    /**
     * default constructor
     */
    public ReportTimerTask()
    {

    }

    /**
     * run time.
     * @param runMeTask runMetask
     */
    public void setRunMeTask(final ReportScheduleService runMeTask)
    {
        this.runMeTask_ = runMeTask;
    }

    @Override
    protected void executeInternal(final JobExecutionContext context)
        throws JobExecutionException
    {

        runMeTask_.run();

    }
}
