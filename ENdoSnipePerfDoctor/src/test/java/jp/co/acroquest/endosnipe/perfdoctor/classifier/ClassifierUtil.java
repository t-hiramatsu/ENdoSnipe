package jp.co.acroquest.endosnipe.perfdoctor.classifier;

import static junit.framework.Assert.assertEquals;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnitGetter;

/**
 * ���ފ��Util�N���X
 * @author fujii
 *
 */
public class ClassifierUtil
{
    /**�@�x����ID�@*/
    public static final String UNIT_ID = "testWarningId";

    /** ���[����ID */
    private static String id = "testRuleId";

    /**
	 * @return the iD
	 */
	public static String getID()
	{
		return id;
	}

	/**
	 * @param iD the iD to set
	 */
	public static void setID(String iD)
	{
		id = iD;
	}

	/** �x���̐��� */
    public static final String DESCRIPTION = "This is a testWarningUtnit";

    /** �N���X�� */
    public static final String CLASS_NAME = "testRuleId";

    /** ���\�b�h�� */
    public static final String METHOD_NAME = "testRuleId";

    /** �d�v�x */
    public static final String LEVEL = "ERROR";

    /** ���O�t�@�C���� */
    public static final String LOG_FILENAME = "file1";

    /** �s�ԍ� */
    public static final int LOG_FILELINENUMBER = 1;

    /** �J�n���� */
    public static final int STARTTIME = 0;

    /** �I������ */
    public static final int ENDTIME = 0;

    /**
     * @param  �B
     * @param  �B
     * @param args 臒l�A���o�l�Ȃǂ̈����B
     * @return�@WarnignUnit WarningUnit
     */

    /**
     * 2��WarnigUnit����v���邩���؂���B
     * @param expect ���҂���WarningUnit
     * @param result ���ʂ�WarningUnit
     */
    public static void assertWarningUnitList(WarningUnit expect, WarningUnit result)
    {
        assertEquals(expect.getId(), result.getId());
        assertEquals(expect.getClassName(), result.getClassName());
        assertEquals(expect.getMethodName(), result.getMethodName());
        assertEquals(expect.getLevel(), result.getLevel());
        assertEquals(expect.getUnitId(), result.getUnitId());
        assertEquals(expect.getDescription(), result.getDescription());
        assertEquals(expect.getLogFileName(), result.getLogFileName());
        assertEquals(expect.getLogFileLineNumber(), result.getLogFileLineNumber());
        assertEquals(expect.getStartTime(), result.getStartTime());
        assertEquals(expect.getEndTime(), result.getEndTime());

        Object[] expectArgs = expect.getArgs();
        Object[] resultArgs = result.getArgs();
        assertEquals(expectArgs.length, resultArgs.length);
        for (int cnt = 0; cnt < expectArgs.length; cnt++)
        {
            assertEquals(expectArgs[cnt], resultArgs[cnt]);
        }
    }

    /**
     * �����ŋ��ʓI�ɗ��p����WarningUnit���쐬����B
     * @param args WarningUnit��Args
     * @return WarningUnit
     */
    public static WarningUnit createDefaultWarningUnit(Object[] args)
    {
        return WarningUnitGetter.createWarningUnit(UNIT_ID, id, DESCRIPTION, CLASS_NAME,
                                                   METHOD_NAME, LEVEL, LOG_FILENAME,
                                                   LOG_FILELINENUMBER, STARTTIME, ENDTIME, args);
    }

    /**
     * �����ŋ��ʓI�ɗ��p����WarningUnit���쐬����(�C�x���g�p)�B
     * @param args WarningUnit��Args
     * @return WarningUnit
     */
    public static WarningUnit createDefaultEventWarningUnit(String stackTrace, Object[] args)
    {
        return WarningUnitGetter.createWarningUnit(UNIT_ID, id, DESCRIPTION, CLASS_NAME,
                                                   METHOD_NAME, LEVEL, LOG_FILENAME,
                                                   LOG_FILELINENUMBER, STARTTIME, ENDTIME, true,
                                                   stackTrace, args);
    }

    /**
     * �����ŋ��ʓI�ɗ��p����WarningUnit���쐬����B
     * @param ruleId �x���̃��[��ID
     * @param fileName �t�@�C����
     * @param startTime �J�n����
     * @param endTime �I������
     * @param args WarningUnit��Args
     * @return WarningUnit
     */
    public static WarningUnit createWarningUnit(String ruleId, String fileName, long startTime,
            long endTime, Object[] args)
    {
        return WarningUnitGetter.createWarningUnit(UNIT_ID, ruleId, DESCRIPTION, CLASS_NAME,
                                                   METHOD_NAME, LEVEL, fileName,
                                                   LOG_FILELINENUMBER, startTime, endTime, args);
    }

}
