package jp.co.acroquest.endosnipe.report.controller.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogInputStreamAccessor;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.javelin.parser.ParseException;
import jp.co.acroquest.endosnipe.perfdoctor.PerfDoctor;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.PerformanceDoctorFilter;
import jp.co.acroquest.endosnipe.perfdoctor.classfier.UnifiedFilter;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleCreateException;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleNotFoundException;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.entity.PerfDoctorRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * PerformanceDoctor�̐f�f���ʂ̃��|�[�g�𐶐�����A�v���Z�b�T�ł��B
 * 
 * @author iida
 */
public class PerfDoctorProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            PerfDoctorProcessor.class);

    /** ���|�[�g�ɏo�͂���錋�ʂ̐��̍ő�l */
    private static final int MAX_RECORD = 65534;
    
    /**
     * �R���X�g���N�^�ł��B
     * �w�肳�ꂽ�A�������郌�|�[�g�̎�ʂ��A�Z�b�g���܂��B
     * 
     * @param type �������郌�|�[�g�̎��
     */
    public PerfDoctorProcessor(ReportType type)
    {
        super(type);
    }

    /**
     * DB�Ȃǂ̃G���e�B�e�B����A���|�[�g�ɏo�͂���f�[�^���擾���܂��B
     * 
     * @param cond              �f�[�^�̎擾����
     * @param reportContainer   �Ԃ�l�Ȃǂ�n�����߂̃R���e�i
     * 
     * @return ���|�[�g�ɏo�͂���f�[�^
     */
    @Override
    protected Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        // ���|�[�g�ɏo�͂���f�[�^�i���̃��\�b�h�̖߂�l�j
        PerfDoctorRecord[] records = null;
        
        try
        {
            String databaseName = cond.getDatabases().get(0);   // �f�[�^�x�[�X��
            Timestamp start = cond.getStartDate();              // �J�n�����̃^�C���X�^���v
            Timestamp end = cond.getEndDate();                  // �I�������̃^�C���X�^���v
            
            // Step 1. DB����AJavelinLog�C���X�^���X�𐶐�����B
            List<JavelinLog> javelinLogList = JavelinLogDao.selectByTerm(databaseName, start, end);
            
            List<JavelinLogElement> javelinLogElementList = new ArrayList<JavelinLogElement>();
            
            // Step 2. JavelinLog�C���X�^���X����JavelinLogDatabaseAccessor�𐶐�����
            for (JavelinLog javelinLog : javelinLogList)    // ���������eJavelinLog�ɂ��āA�������J��Ԃ�
            {
            	InputStream javelinLogStream = null;
            	try
            	{
					javelinLogStream = JavelinLogDao
							.selectJavelinLogByLogId(databaseName, javelinLog.logId);
					JavelinLogInputStreamAccessor javelinLogMemoryAccessor = new JavelinLogInputStreamAccessor(
							javelinLog.logFileName, javelinLogStream);
	                
	                // Step 3. JavelinLogMemoryAccessor�C���X�^���X����JavelinParser�𐶐�����
	                JavelinParser javelinParser = new JavelinParser(javelinLogMemoryAccessor);
	                javelinParser.init();
	                
	                // Step 4. JavelinParser�C���X�^���X����JavelinLogElement�𐶐�����
	                JavelinLogElement javelinLogElement;
	                while ((javelinLogElement = javelinParser.nextElement()) != null)
	                {
	                    javelinLogElementList.add(javelinLogElement);
                    }
	                
	                JavelinParser.initDetailInfo(javelinLogElementList);
                }
                finally
                {
                    if (javelinLogStream != null)
                    {
                        javelinLogStream.close();
                    }
				}
            }
            
            // Step 5. ��������JavelinLogElement���APerformanceDoctor�ɐf�f������
            PerfDoctor perfDoctor = new PerfDoctor();
            perfDoctor.init();
            List<WarningUnit> warningUnitList =
                    perfDoctor.judgeJavelinLog(javelinLogElementList);

            if (MAX_RECORD <= warningUnitList.size())
            {
                warningUnitList = warningUnitList.subList(0, MAX_RECORD);
            }
            
            // �t�B���^���������s����B
            warningUnitList = this.doFilter(warningUnitList, cond);
            
            List<PerfDoctorRecord> recordList = new ArrayList<PerfDoctorRecord>();
            for (WarningUnit warningUnit : warningUnitList)
            {
                PerfDoctorRecord record = new PerfDoctorRecord(warningUnit);
                recordList.add(record);
            }
            
            // ���ʂ�z��ɒ���
            records = new PerfDoctorRecord[recordList.size()];
            recordList.toArray(records);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
        }
        catch (IOException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
        }
        catch (ParseException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
        }
        catch (RuleNotFoundException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
        }
        catch (RuleCreateException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
        }
        
        return records;
    }

    /**
     * �f�[�^�����|�[�g�ɏo�͂��܂��B
     * 
     * @param plotData          ���|�[�g�ɏo�͂���f�[�^
     * @param cond              �f�[�^�̏o�͏���
     * @param reportContainer   �Ԃ�l�Ȃǂ�n�����߂̃R���e�i
     */
    @Override
    protected void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        if (plotData instanceof PerfDoctorRecord[])
        {
            PerfDoctorRecord[] records = (PerfDoctorRecord[])plotData;
            
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
			
            RecordReporter<PerfDoctorRecord> reporter = 
                new RecordReporter<PerfDoctorRecord>(this.getReportType());
          
            String outputFilePath = getOutputFolderName() + File.separator;
            outputFilePath += ReporterConfigAccessor.getProperty(
                    this.getReportType().getId() + ".outputFile");
            
            reporter.outputReport(templateFilePath, outputFilePath, records,
                                  new Date(), new Date());  // ����2��Date�́A���Ɏg�p���Ȃ��B
        }
    }

    /**
     * ���X�g�Ƀt�B���^�[��������B
     * 
     * @param resultList    ���茋�ʂ̃��X�g
     * @param cond          �f�[�^�̏o�͏���
     * @return �t�B���^�[����������́A���茋�ʂ̃��X�g
     */
    private List<WarningUnit> doFilter(final List<WarningUnit> resultList,
            final ReportSearchCondition cond)
    {
        PerformanceDoctorFilter sameFilter = new PerformanceDoctorFilter();
        UnifiedFilter unifiedFilter = new UnifiedFilter();
        List<WarningUnit> filteredList = resultList;

        // ���v���t�B���^�Ɠ���x���t�B���^��ݒ肷��B
        if (cond.getLimitSameCause())
        {
            filteredList = unifiedFilter.doFilter(filteredList);
        }
        if (cond.getLimitBySameRule())
        {
            filteredList = sameFilter.doFilter(filteredList);
        }

        return filteredList;
    }

}
