package jp.co.acroquest.endosnipe.javelin.converter.infinispan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.co.acroquest.endosnipe.common.entity.DisplayType;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.JobInfo;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.MapReduceTaskMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.TaskInfo;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;

/**
 * Infinispan のジョブ情報を取得する
 * 
 * @author hiramatsu
 *
 */
public class InfinispanJobGetter implements MultiResourceGetter
{
    /** Jobの情報を表す項目名 */
    private static final String ITEM_NAME_JOB  = "/infinispan/mapreduce/job";

    /** Taskの情報を表す項目名 */
    private static final String ITEM_NAME_TASK = "/infinispan/mapreduce/task";

    /**
     * {@inheritDoc}
     */
    public List<ResourceItem> getValues()
    {
        SystemLogger.getInstance().info(
                "InfinispanJobGetter.getValues() : start");
        List<ResourceItem> returnList = new ArrayList<ResourceItem>();

        Collection<JobInfo> jobInfoList = MapReduceTaskMonitor.cloneJobInfoMapAsCollection();
        Collection<TaskInfo> taskInfoList = MapReduceTaskMonitor.cloneTaskInfoMapAsCollection();

        SystemLogger.getInstance().info(
                "InfinispanJobGetter.getValues() : jobInfoList "
                        + jobInfoList.size());
        SystemLogger.getInstance().info(
                "InfinispanJobGetter.getValues() : taskInfoList "
                        + taskInfoList.size());

        // Jobの情報を取得する
        StringBuilder jobInfoBuilder = new StringBuilder();
        jobInfoBuilder.append("[");

        for (JobInfo jobInfo : jobInfoList)
        {
            if (jobInfoBuilder.length() > 1)
            {
                jobInfoBuilder.append(",");
            }
            String jobInfoJSON = jobInfo.getJson();
            jobInfoBuilder.append(jobInfoJSON);
            SystemLogger.getInstance().info(
                    "InfinispanJobGetter.getValues() : jobInfoJSON "
                            + jobInfoJSON);
        }
        jobInfoBuilder.append("]");
        String jobInfoListJSON = jobInfoBuilder.toString();

        if (jobInfoListJSON.length() > 2)
        {
            ResourceItem jobInfoItem = createResourceItem(ITEM_NAME_JOB,
                    jobInfoListJSON, ItemType.ITEMTYPE_STRING);
            returnList.add(jobInfoItem);
            SystemLogger.getInstance().info(
                    "InfinispanJobGetter.getValues() : jobInfoListJSON "
                            + jobInfoListJSON);
        }

        SystemLogger.getInstance().info(
                "InfinispanJobGetter.getValues() : jobInfoList "
                        + jobInfoList.size());

        // taskの情報を取得する
        StringBuilder taskInfoBuilder = new StringBuilder();
        taskInfoBuilder.append("[");

        for (TaskInfo taskInfo : taskInfoList)
        {
            if (taskInfoBuilder.length() > 1)
            {
                taskInfoBuilder.append(",");
            }
            String taskInfoJSON = taskInfo.getJson();
            taskInfoBuilder.append(taskInfoJSON);
            SystemLogger.getInstance().info(
                    "InfinispanJobGetter.getValues() : taskInfoJSON "
                            + taskInfoJSON);
        }
        taskInfoBuilder.append("]");
        String taskInfoListJSON = taskInfoBuilder.toString();

        if (taskInfoListJSON.length() > 2)
        {
            ResourceItem taskInfoItem = createResourceItem(ITEM_NAME_TASK,
                    taskInfoListJSON, ItemType.ITEMTYPE_STRING);
            returnList.add(taskInfoItem);
            SystemLogger.getInstance().info(
                    "InfinispanJobGetter.getValues() : taskInfoListJSON "
                            + taskInfoListJSON);
        }

        SystemLogger.getInstance().info(
                "InfinispanJobGetter.getValues() : taskInfoList "
                        + taskInfoList.size());

        return returnList;
    }

    private ResourceItem createResourceItem(String name, String value,
            ItemType itemType)
    {
        ResourceItem retValue = new ResourceItem();

        retValue.setValue(value);
        retValue.setItemType(itemType);
        retValue.setObjectName(name);
        retValue.setName(name);
        retValue.setObjectDisplayNeme(name);
        retValue.setDisplayName(name);
        retValue.setDisplayType(DisplayType.DISPLAYTYPE_NORMAL);

        return retValue;
    }

    /**
     * {@inheritDoc}
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_STRING;
    }
}
