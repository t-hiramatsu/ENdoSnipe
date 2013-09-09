package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

/**
 * MapReduceTaskをMapReduceTaskMonitorで扱うためのインタフェース
 * 
 * @author hiramatsu
 *
 */
public interface MapReduceTaskAccessor
{
    /**
     * ジョブIDをセットする。
     * 
     * @param jobId セットするジョブID
     */
    void setJobId(String jobId);

    /**
     * ジョブIDを取得する。
     * 
     * @return ジョブID
     */
    String getJobId();

    /**
     * mapperの名前を返す。
     * 
     * @return mapperの名前
     */
    String getMapperName();

    /**
     * タスクIDをマップに登録する。
     * 
     * @param address タスクの実行されるアドレス
     * @param taskId タスクID
     */
    void putTaskId(String address, String taskId);

    /**
     * 登録されたタスクIDの個数を返す。
     * 
     * @return 登録されたタスクIDの個数
     */
    int getSizeOfTaskIdMap();
}
