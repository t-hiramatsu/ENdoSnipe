package jp.co.acroquest.endosnipe.report.controller.processor;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishProcessor;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.RowRepeatParamParser;
import org.bbreak.excella.reports.tag.SingleParamParser;

/**
 * �T�}�����|�[�g���o�͂���A�v���Z�b�T�N���X�B<br>
 * 
 * @author iida
 */
public class SummaryReportProcessor implements ReportPublishProcessor
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            SummaryReportProcessor.class);

	/** �o�͂���郌�|�[�g�̎�ނ̃��X�g */
	private static final ThreadLocal<ReportType[]> OUTPUT_FILE_TYPE_LIST
	        = new ThreadLocal<ReportType[]>();

	/**
	 * �o�͂���郌�|�[�g�̎�ނ̃��X�g���Z�b�g���܂��B<br>
	 * 
	 * @param outputFileTypeList �o�͂���郌�|�[�g�̎�ނ̃��X�g
	 */
	public static void setOutputFileTypeList(ReportType[] outputFileTypeList)
	{
		OUTPUT_FILE_TYPE_LIST.set(outputFileTypeList);
	}

	/** ���|�[�g�v���Z�b�T�̍H���̐� */
	public static final int PROCESS_PHASE_NUM = 3;

	/** �f�[�^���擾����H�� */
	private static final String GET_DATA_PHASE_KEY
	        = "reporter.report.progress.detail.getData";

	/** �擾�����f�[�^��ϊ�����H�� */
	private static final String CONVERT_DATA_PHASE_KEY
	        = "reporter.report.progress.detail.convData";

	/** �t�@�C�����o�͂���H�� */
	private static final String OUTPUT_DATA_PHASE_KEY
	        = "reporter.report.progress.detail.output";
	
	/** ���|�[�g�o�͊��Ԃ������^�O */
	private static final String DATE_RANGE_TAG = "dataRange";
	
	/** �f�[�^�x�[�X���������^�O */
	private static final String DATABASE_NAME_TAG = "dataBaseName";
	
	/** �ԍ��̃��X�g�������^�O */
	private static final String NUMBERS_TAG = "numbers";
	
	/** �o�̓t�@�C�����̃��X�g�������^�O */
	private static final String FILE_NAMES_TAG = "fileNames";
	
	/** �o�̓t�@�C���̐����������^�O */
	private static final String FILE_EXPLANATIONS_TAG = "fileExplanations";
	
	/** ���|�[�g��� */
	private ReportType rType_;

	/** �o�͐�f�B���N�g���� */
	private String outputDir_;

	/**
	 * �R���X�g���N�^�B
	 * @param rType ���|�[�g���
	 */
	public SummaryReportProcessor(ReportType rType)
	{
		rType_ = rType;
	}

	/**
	 * �������������郌�|�[�g��ʂ��擾����B
	 * @return ���|�[�g���
	 */
	protected ReportType getReportType()
	{
		return rType_;
	}

	/**
	 * �o�͐�t�@�C���̃t�@�C�������擾����i�g���q�����j
	 * @return �o�͐�t�@�C���̃t�@�C����
	 */
	protected String getOutputFileName()
	{
		File outputFile = new File(outputDir_, ReporterConfigAccessor
				.getOutputFileName(rType_));
		return outputFile.getAbsolutePath();
	}
	
	/**
	 * �o�͐�t�H���_�̃p�X���擾����
	 * @return �o�͐�t�H���_�̃p�X
	 */
	protected String getOutputFolderName()
	{
		File outputFolder = new File(outputDir_);
		return outputFolder.getAbsolutePath();
	}

	/**
	 * ���|�[�g�o�͏������s���B
	 * @param cond ���|�[�g�o�͂̏���
	 * @return ReportProcessReturnContainer �R���e�i
	 * @throws InterruptedException ����X���b�h�����̃X���b�h�𒆒f���������ɔ�������B
	 */
	public ReportProcessReturnContainer publish(ReportSearchCondition cond)
			throws InterruptedException
	{
		// TODO ���̃v���Z�b�T�Ɠ��l��Phase���g�p���Ă���̂��C������B
		
		outputDir_ = cond.getOutputFilePath();

		ReportProcessReturnContainer retContainer = new ReportProcessReturnContainer();

		outputReport(null, cond, retContainer);

		return retContainer;
	}

	/**
	 * �f�[�^�擾�����͈̔͂�\�����镶����𐬌^����
	 * @param startDate �f�[�^�擾�J�n����
	 * @param endDate �f�[�^�擾�I������
	 * @return�@�\���p�̕�����
	 */
	private String getDataRangeString(Date startDate, Date endDate)
	{
		Calendar calendar = Calendar.getInstance();

		// �f�[�^�擾�J�n�����ƃf�[�^�擾�I�������𐬌^����
		calendar.setTime(startDate);
		String startDateString = String.format(
				"%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);
		calendar.setTime(endDate);
		String endDateString = String.format(
				"%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);

		// �\���p������𐬌`����
		StringBuilder builder = new StringBuilder();
		builder.append(startDateString);
		builder.append(" �` ");
		builder.append(endDateString);

		String returnValue = builder.toString();

		return returnValue;
	}

	/**
	 * ���|�[�g�o�͏������s���B
	 * @param plotData �g�p���Ȃ�
	 * @param cond ���|�[�g�o�͂̏���
	 * @param reportContainer �R���e�i
	 */
	private void outputReport(Object plotData, ReportSearchCondition cond,
			ReportProcessReturnContainer reportContainer)
	{
		// �_�C�A���O�Ń`�F�b�N���ꂽ�A���|�[�g��ނ̃��X�g���擾����B
		ReportType[] outputFileTypeList = OUTPUT_FILE_TYPE_LIST.get();
		// �u�ԍ��v�̗�ɏo�͂���f�[�^
		Integer[] numbers = new Integer[outputFileTypeList.length];
		// �u�t�@�C�����v�̗�ɏo�͂���f�[�^
		String[] fileNames = new String[outputFileTypeList.length];
		// �u�t�@�C�����v�Ɓu�����v�̗�ɏo�͂���f�[�^�̑Ή��֌W��\���}�b�v
		Map<String, String> explanationMap = new HashMap<String, String>();
		for (int index = 0; index < outputFileTypeList.length; index++)
		{
		    ReportType currentReportType = outputFileTypeList[index];
			String fileName = ReporterConfigAccessor
			        .getOutputFileName(currentReportType);
			String explanation = ReporterConfigAccessor
	                .getExplanation(currentReportType);
			numbers[index] = index + 1;
			// ApplicationReport��ObjectReport�AResponseTimeReport�AEventReport��
			// �f�B���N�g���Ƃ��ďo�͂����̂ŁA".xls"��t���Ȃ�
			if (currentReportType.equals(ReportType.SERVER_POOL)
			    || currentReportType.equals(ReportType.POOL_SIZE)
			    || currentReportType.equals(ReportType.OBJECT)
                || currentReportType.equals(ReportType.RESPONSE_LIST)
                || currentReportType.equals(ReportType.EVENT))
			{
				fileNames[index] = fileName;
			}
			else
			{
				fileNames[index] = fileName + ".xls";
			}
			explanationMap.put(fileNames[index], explanation);
		}
		Arrays.sort(fileNames);
		// �u�����v�̗�ɏo�͂���f�[�^
		String[] explanations = new String[outputFileTypeList.length];
		for (int index = 0; index < outputFileTypeList.length; index++)
		{
			String fileName = fileNames[index];
			explanations[index] = explanationMap.get(fileName);
		}

		// �o�͂��郌�|�[�g�̎�ނɂ��킹�ăe���v���[�g�̃t�@�C���p�X���擾����
		String templateFilePath;
		try
		{
			templateFilePath = TemplateFileManager.getInstance()
					.getTemplateFile(getReportType());
		}
		catch (IOException exception)
		{
			reportContainer.setHappendedError(exception);
			return;
		}

		// ���|�[�g�o�͂̈��������擾����
		String outputFilePath = this.getOutputFileName();
		Timestamp startDate = cond.getStartDate();
		Timestamp endDate = cond.getEndDate();
		List<String> databases = cond.getDatabases();

		// �o�͂���Excel�̃u�b�N�ƃV�[�g��\���I�u�W�F�N�g�𐶐�����
		String id = this.getReportType().getId();
		String templateSheetName = ReporterConfigAccessor.getProperty(id
				+ ".templateSheetName");
		ReportBook outputBook = new ReportBook(templateFilePath,
				outputFilePath, ExcelExporter.FORMAT_TYPE);
		ReportSheet outputDataSheet = new ReportSheet(templateSheetName);
		outputBook.addReportSheet(outputDataSheet);

		// �V�[�g�Ƀf�[�^�𗬂�����
		outputDataSheet.addParam(SingleParamParser.DEFAULT_TAG,
				DATE_RANGE_TAG, this.getDataRangeString(startDate, endDate));
		outputDataSheet.addParam(SingleParamParser.DEFAULT_TAG,
				DATABASE_NAME_TAG, databases.get(0));

		outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG,
				NUMBERS_TAG, numbers);
		outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG,
				FILE_NAMES_TAG, fileNames);
		outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG,
				FILE_EXPLANATIONS_TAG, explanations);

		// Excel�t�@�C�����o�͂���B
		ReportProcessor reportProcessor = new ReportProcessor();
		try
		{
			reportProcessor.process(outputBook);
		}
		catch (Exception ex)
		{
		    LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, ex,
		            ReporterConfigAccessor.getReportName(getReportType()));
		}
	}
}
