package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * mapreduce�W���u�A�^�X�N�̏����擾����B
 * 
 * @author hiramatsu
 *
 */
public class MapReduceTaskMonitor
{
    /**�@�W���u����ێ�����}�b�v�@*/
    private static Map<String, JobInfo>  jobInfoMap__  = new ConcurrentHashMap<String, JobInfo>();

    /**�@�^�X�N����ێ�����}�b�v�@*/
    private static Map<String, TaskInfo> taskInfoMap__ = new ConcurrentHashMap<String, TaskInfo>();

    /**�@�Ō�̃W���u�����s���ꂽ�����@*/
    private static String                previousDate__;

    /**�@���݂̎����iyyyyMMddHHmm�j���Ɏ��s���ꂽ�W���u�̌��@*/
    private static int                   num__;

    private MapReduceTaskMonitor()
    {}

    /**
     * �W���u�J�n�O�̏������s���B
     * 
     * @param accessor �Ώۂ̃W���u
     */
    public static void preProcess(MapReduceTaskAccessor accessor)
    {
        long submitTime = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String dateString = sdf.format(date);
        if (previousDate__ == null || !previousDate__.equals(sdf.format(date)))
        {
            previousDate__ = dateString;
            num__ = 1;
        }
        else
        {
            num__++;
        }
        DecimalFormat df = new DecimalFormat("00000000");
        String jobId = "job_" + dateString + "_" + df.format(num__);
        JobInfo jobInfo = new JobInfo();
        jobInfo.setSubmitTime(submitTime);
        jobInfo.setStartTime(submitTime);
        String tmpName = accessor.getMapperName();
        String[] tmpNames = tmpName.split("\\.");
        tmpName = tmpNames[tmpNames.length - 1];
        String mapper = "Mapper";
        jobInfo.setJobName(tmpName.endsWith(mapper) ? tmpName.substring(0,
                tmpName.length() - mapper.length()) : tmpName);
        jobInfo.setJobId(jobId);
        accessor.setJobId(jobId);
        saveJobInfo(jobInfo);
    }

    /**
     * �W���u�I�����̏������s���B
     * 
     * @param accessor �Ώۂ̃W���u
     */
    public static void postProcess(MapReduceTaskAccessor accessor)
    {
        long finishTime = System.currentTimeMillis();
        String jobId = accessor.getJobId();
        JobInfo jobInfo = jobInfoMap__.get(jobId);
        if (jobInfo == null)
        {
            return;
        }
        jobInfo.setFinishTime(finishTime);
        jobInfo.setStatus("SUCCEEDED");
        SystemLogger.getInstance().warn("postProcess:" + jobInfo);
    }

    /**
     * �W���u���s���̏������s���B
     * 
     * @param accessor �Ώۂ̃W���u
     * @param throwable �^�X�N���s�����̗�O
     */
    public static void postProcessNG(MapReduceTaskAccessor accessor,
            Throwable throwable)
    {
        long finishTime = System.currentTimeMillis();
        String jobId = accessor.getJobId();
        JobInfo jobInfo = jobInfoMap__.get(jobId);
        if (jobInfo == null)
        {
            return;
        }
        jobInfo.setFinishTime(finishTime);
        jobInfo.setStatus("FAILED");
        SystemLogger.getInstance().warn("postProcessNG:" + jobInfo);
    }

    /**
     * �^�X�N�J�n�O�̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     * @param address �^�X�N�̎��s�����A�h���X
     */
    public static void preProcessTask(MapReduceTaskAccessor accessor,
            String address)
    {
        long startTime = System.currentTimeMillis();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStartTime(startTime);
        taskInfo.setJobID(accessor.getJobId());
        DecimalFormat df = new DecimalFormat("00000000");
        String taskId = accessor.getJobId() + "_"
                + (df.format(accessor.getSizeOfTaskIdMap() + 1));
        taskInfo.setTaskAttemptID(taskId);
        accessor.putTaskId(address, taskId);
        taskInfo.setHostName(address);
        saveTaskInfo(taskInfo);
        SystemLogger.getInstance().warn("preProcessTask:" + taskInfo);
    }

    /**
     * org.infinispan.remoting.rpc.RpcManager.invokeRemotely�J�n�O�̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     * @param address �^�X�N�̎��s�����A�h���X
     */
    public static void preProcessTaskForInvokeRemotely(
            MapReduceTaskAccessor accessor, String address)
    {
        long startTime = System.currentTimeMillis();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStartTime(startTime);
        taskInfo.setJobID(accessor.getJobId());
        taskInfo.setTaskAttemptID(accessor.getJobId());
        accessor.putTaskId(address, accessor.getJobId());
        taskInfo.setHostName(address);
        saveTaskInfo(taskInfo);
        SystemLogger.getInstance().warn(
                "preProcessTaskForInvokeRemotely:" + taskInfo);
    }

    /**
     * org.infinispan.remoting.rpc.RpcManager.invokeRemotelyInFuture�J�n�O�̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     * @param address �^�X�N�̎��s�����A�h���X
     */
    public static void preProcessTaskForInvokeRemotelyInFuture(
            MapReduceTaskAccessor accessor, String address)
    {
        long startTime = System.currentTimeMillis();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStartTime(startTime);
        taskInfo.setJobID(accessor.getJobId());
        DecimalFormat df = new DecimalFormat("00000000");
        String taskId = accessor.getJobId() + "_"
                + (df.format(accessor.getSizeOfTaskIdMap() + 1));
        taskInfo.setTaskAttemptID(taskId);
        accessor.putTaskId(address, taskId);
        taskInfo.setHostName(address);
        saveTaskInfo(taskInfo);
        SystemLogger.getInstance().warn(
                "preProcessTaskForInvokeRemotelyInFuture:" + taskInfo);
    }

    /**
     * �^�X�N�I�����̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     */
    public static void postProcessTask(MapReduceTaskAccessor accessor)
    {
        long finishTime = System.currentTimeMillis();
        TaskInfo taskInfo = taskInfoMap__.get(accessor.getJobId());
        if (taskInfo != null)
        {
            taskInfo.setFinishTime(finishTime);
            taskInfo.setStatus("SUCCEEDED");
        }
        SystemLogger.getInstance().warn("postProcessTask:" + taskInfo);
    }

    /**
     * org.infinispan.remoting.rpc.RpcManager.invokeRemotely�I����̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     * @param map �^�X�N�̎��s���ꂽ�A�h���X�ƏI����Ԃ̃}�b�v
     */
    public static void postProcessTaskForInvokeRemotely(
            MapReduceTaskAccessor accessor, Map<String, Boolean> map)
    {
        long finishTime = System.currentTimeMillis();
        TaskInfo taskInfoOrg = taskInfoMap__.remove(accessor.getJobId());
        DecimalFormat df = new DecimalFormat("00000000");
        int index = 0;
        Set<Entry<String, Boolean>> entrySet = map.entrySet();
        for (Entry<?, ?> entry : entrySet)
        {
            String address = (String)entry.getKey();
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setStartTime(taskInfoOrg.getStartTime());
            taskInfo.setFinishTime(finishTime);
            taskInfo.setJobID(taskInfoOrg.getJobID());
            taskInfo.setTaskAttemptID(taskInfoOrg.getTaskAttemptID() + "_"
                    + df.format(index + 1));
            taskInfo.setHostName(address);
            taskInfo.setStatus(map.get(address).booleanValue() ? "SUCCEEDED" : "FAILED");
            saveTaskInfo(taskInfo);
            if (index > 0)
            {
                accessor.putTaskId(taskInfo.getHostName(),
                        taskInfo.getTaskAttemptID());
            }
            index++;
            SystemLogger.getInstance().warn(
                    "postProcessTaskForInvokeRemotely:" + taskInfo);
        }
    }

    /**
     * �^�X�N���s���̏������s���B
     * 
     * @param accessor �Ώۂ̃^�X�N
     */
    public static void postProcessNGTask(MapReduceTaskAccessor accessor)
    {
        long finishTime = System.currentTimeMillis();
        TaskInfo taskInfo = taskInfoMap__.get(accessor.getJobId());
        if (taskInfo != null)
        {
            taskInfo.setFinishTime(finishTime);
            taskInfo.setStatus("FAILED");
        }
        SystemLogger.getInstance().warn("postProcessNGTask:" + taskInfo);
    }

    /**
     * �W���u����ۑ�����B
     * 
     * @param jobInfo �o�^����W���u�̏��
     */
    public static void saveJobInfo(JobInfo jobInfo)
    {
        jobInfoMap__.put(jobInfo.getJobId(), jobInfo);
        SystemLogger.getInstance().warn("saveJobInfo:" + jobInfo);
    }

    /**
     * �^�X�N����ۑ�����B
     * 
     * @param taskInfo �o�^����^�X�N�̏��
     */
    public static void saveTaskInfo(TaskInfo taskInfo)
    {
        taskInfoMap__.put(taskInfo.getTaskAttemptID(), taskInfo);
        SystemLogger.getInstance().warn("saveTaskInfo:" + taskInfo);
    }

    /**
     * �I�������W���u���}�b�v�����菜���A�R���N�V�����Ƃ��ĕԂ��B
     * 
     * @return �I�������W���u�̃R���N�V����
     */
    public static Collection<JobInfo> cloneJobInfoMapAsCollection()
    {
        Collection<JobInfo> removedCollection = new ArrayList<JobInfo>();
        Map<String, JobInfo> newJobInfoMap = new ConcurrentHashMap<String, JobInfo>();

        for (JobInfo jobInfo : jobInfoMap__.values())
        {
            if (jobInfo.getStatus() == null)
            {
                newJobInfoMap.put(jobInfo.getJobId(), jobInfo);
            }
            else
            {
                removedCollection.add(jobInfo);
            }
        }
        jobInfoMap__ = newJobInfoMap;
        return removedCollection;
    }

    /**
     * �I�������^�X�N���}�b�v�����菜���A�R���N�V�����Ƃ��ĕԂ��B
     * 
     * @return �I�������^�X�N�̃R���N�V����
     */
    public static Collection<TaskInfo> cloneTaskInfoMapAsCollection()
    {
        Collection<TaskInfo> removedCollection = new ArrayList<TaskInfo>();
        Map<String, TaskInfo> newTaskInfoMap = new ConcurrentHashMap<String, TaskInfo>();

        for (TaskInfo taskInfo : taskInfoMap__.values())
        {
            if (taskInfo.getStatus() == null)
            {
                newTaskInfoMap.put(taskInfo.getJobID(), taskInfo);
            }
            else
            {
                removedCollection.add(taskInfo);
            }
        }
        taskInfoMap__ = newTaskInfoMap;
        return removedCollection;
    }
}
