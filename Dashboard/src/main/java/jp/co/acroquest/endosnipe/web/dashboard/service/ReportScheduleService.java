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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

/**
 * 
 * @author khinlay
 *
 */
@Service
public class ReportScheduleService
{
    private final boolean verifyCursorPosition = true;

    private ResultSet rs;

    /**
     * デフォルトコンストラクタ。
     */
    public ReportScheduleService()
    {
    }

    public void run()
    {
        java.util.Date date = new java.util.Date();
        Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());

        String[] springConfig = { "spring/batch/jobs/job-extract-users.xml" };

        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        JobLauncher jobLauncher = (JobLauncher)context.getBean("jobLauncher");
        Job job = (Job)context.getBean("testJob");

        try
        {

            JobParameters param =
                    new JobParametersBuilder().addString("currentTime", currentTime.toString()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, param);
            System.out.println("\nExit Status : " + execution.getStatus());
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        System.out.println("Get data for every 5 seconds");
    }

    /**
     * Check the result set is in synch with the currentRow attribute. This is
     * important to ensure that the user hasn't modified the current row.
     */
    protected void verifyCursorPosition(final long expectedCurrentRow)
        throws SQLException
    {
        if (verifyCursorPosition)
        {
            if (expectedCurrentRow != this.rs.getRow())
            {
                throw new InvalidDataAccessResourceUsageException(
                                                                  "Unexpected cursor position change.");
            }
        }
    }
}
