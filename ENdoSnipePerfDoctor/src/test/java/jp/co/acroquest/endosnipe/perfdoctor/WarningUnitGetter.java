package jp.co.acroquest.endosnipe.perfdoctor;

/**
 * WarningUnit���쐬���邽�߂̃N���X�B
 * @author fujii
 *
 */
public class WarningUnitGetter
{
    /**
     * WarningUnit���쐬����B
     * @param unitId �x����ID
     * @param id ���[����ID
     * @param description �x���̐����B
     * @param className �N���X���B
     * @param methodName ���\�b�h���B
     * @param level �d�v�x
     * @param logFileName ���O�t�@�C�����B
     * @param logFileLineNumber �s�ԍ��B
     * @param args 臒l�A���o�l�Ȃǂ̈����B
     * @return WarnignUnit WarningUnit
     */
    public static WarningUnit createWarningUnit(String unitId, String id, String description,
            String className, String methodName, String level, String logFileName,
            int logFileLineNumber, long startTime, long endTime, Object[] args)
    {
        return new WarningUnit(unitId, id, description, className, methodName, level, logFileName,
                               logFileLineNumber, startTime, endTime, true, args);
    }

    /**
     * WarningUnit���쐬����B
     * @param unitId �x����ID
     * @param id ���[����ID
     * @param description �x���̐����B
     * @param className �N���X���B
     * @param methodName ���\�b�h���B
     * @param level �d�v�x
     * @param logFileName ���O�t�@�C�����B
     * @param logFileLineNumber �s�ԍ��B
     * @param isEvent �C�x���g�ł��邩�ǂ����B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param args 臒l�A���o�l�Ȃǂ̈����B
     * @return WarnignUnit WarningUnit
     */
    public static WarningUnit createWarningUnit(String unitId, String id, String description,
            String className, String methodName, String level, String logFileName,
            int logFileLineNumber, long startTime, long endTime, boolean isEvent,
            String stackTrace, Object[] args)
    {
        return new WarningUnit(unitId, id, description, className, methodName, level, logFileName,
                               logFileLineNumber, startTime, endTime, true, isEvent, stackTrace,
                               args);
    }

}
