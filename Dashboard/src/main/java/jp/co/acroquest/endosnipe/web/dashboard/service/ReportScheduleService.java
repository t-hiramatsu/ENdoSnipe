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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 
 * @author khinlay
 *
 */
@Service
public class ReportScheduleService
{

    /** connect with database */
    String[] springConfig_ = { "spring/batch/jobs/job-extract-users.xml" };

    /** create class path */
    ApplicationContext context_ = new ClassPathXmlApplicationContext(springConfig_);

    /**
     * デフォルトコンストラクタ。
     */
    public ReportScheduleService()
    {
    }

    /**
     * Thread excuting.
     */
    public void run()
    {
        Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());

        JobLauncher jobLauncher = (JobLauncher)context_.getBean("jobLauncher");
        Job job = (Job)context_.getBean("testJob");

        try
        {

            JobParameters param =
                    new JobParametersBuilder().addString("currentTime", currentTime.toString()).toJobParameters();
            jobLauncher.run(job, param);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }
    }

}
