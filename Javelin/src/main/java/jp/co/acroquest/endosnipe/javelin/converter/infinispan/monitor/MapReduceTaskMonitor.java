package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

import java.util.Map;

import org.eclipse.ui.internal.progress.JobInfo;

public class MapReduceTaskMonitor
{
    private Map<String, JobInfo> jobInfoMap_;

    public void preProcess(JobInfo jobInfo)
    {
        jobInfo.setJobId(jobId);
    }

    public void saveJobInfo(JobInfo jobInfo)
    {
        jobInfoMap_.put(jobId, jobInfo);
    }
}
