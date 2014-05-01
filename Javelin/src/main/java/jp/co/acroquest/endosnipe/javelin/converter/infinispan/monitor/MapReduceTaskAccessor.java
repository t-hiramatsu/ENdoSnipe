package jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor;

/**
 * MapReduceTask��MapReduceTaskMonitor�ň������߂̃C���^�t�F�[�X
 * 
 * @author hiramatsu
 *
 */
public interface MapReduceTaskAccessor
{
    /**
     * �W���uID���Z�b�g����B
     * 
     * @param jobId �Z�b�g����W���uID
     */
    void setJobId(String jobId);

    /**
     * �W���uID���擾����B
     * 
     * @return �W���uID
     */
    String getJobId();

    /**
     * mapper�̖��O��Ԃ��B
     * 
     * @return mapper�̖��O
     */
    String getMapperName();

    /**
     * �^�X�NID���}�b�v�ɓo�^����B
     * 
     * @param address �^�X�N�̎��s�����A�h���X
     * @param taskId �^�X�NID
     */
    void putTaskId(String address, String taskId);

    /**
     * �o�^���ꂽ�^�X�NID�̌���Ԃ��B
     * 
     * @return �o�^���ꂽ�^�X�NID�̌�
     */
    int getSizeOfTaskIdMap();
}
