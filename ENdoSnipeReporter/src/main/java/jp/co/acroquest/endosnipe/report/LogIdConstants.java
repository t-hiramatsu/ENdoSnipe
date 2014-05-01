package jp.co.acroquest.endosnipe.report;

/**
 * ���O�o�͗p���b�Z�[�W���`����萔�N���X
 * 
 * @author kimura
 */
public interface LogIdConstants {
	/** ��O�������̃��OID */
	String EXCEPTION_HAPPENED = "EERT0001";

	/** ���|�[�g�o�͂���~�����ۂ̂̃��OID */
	String REPORT_PUBLISH_STOPPED_WARN = "WERT0002";

	/** ���|�[�g�������L�����Z�����ꂽ�ۂ̃��OID */
	String REPORT_CANCEL_INFO = "IERT0003";

	/** �R���t�B�O�ǂݍ��ݎ��s���̃��OID */
	String READ_FAULT_CONFIG = "EERT0004";

	/** �f�[�^�ǂݍ��ݎ��s���̃��OID */
	String EXCEPTION_IN_READING = "WERT0005";

	/** ���|�[�g�o�͏��̃��OID */
	String OUTPUT_REPORT_INFO = "DERT0006";
	
	/** ���|�[�g�쐬���̃f�B���N�g���폜�Ɏ��s�����ۂ̃��OID */
	String FAIL_TO_DELETE_DIR = "WERT0007";
	
	/** ���|�[�g�t�@�C����ZIP���Ɏ��s�����ۂ̃��OID */
	String FAIL_TO_ZIP = "WERT0008";
}
