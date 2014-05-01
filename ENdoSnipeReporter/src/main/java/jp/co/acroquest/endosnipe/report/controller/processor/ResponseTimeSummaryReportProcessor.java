package jp.co.acroquest.endosnipe.report.controller.processor;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.dao.ResponseTimeSummaryRecordAccessor;
import jp.co.acroquest.endosnipe.report.entity.ResponseTimeSummaryRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * ResponseTime��Summary���쐬����v���Z�b�T
 * 
 * @author kimura
 *
 */
public class ResponseTimeSummaryReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            ResponseTimeSummaryReportProcessor.class);

    /**
     * �R���X�g���N�^
     * @param type ���|�[�g���
     */
    public ResponseTimeSummaryReportProcessor(ReportType type)
    {
        super(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        ResponseTimeSummaryRecordAccessor accessor = new ResponseTimeSummaryRecordAccessor();

        List<ResponseTimeSummaryRecord> rawData;
        try
        {
            rawData = accessor.findResponseStatisticsByTerm(cond.getDatabases().get(0),
                    cond.getStartDate(), cond.getEndDate());
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
        }

        return rawData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertPlotData(Object rawData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        List<ResponseTimeSummaryRecord> dataList = (List<ResponseTimeSummaryRecord>) rawData;

		return (ResponseTimeSummaryRecord[]) dataList
				.toArray(new ResponseTimeSummaryRecord[dataList.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        if ((plotData instanceof ResponseTimeSummaryRecord[]) == false)
        {
            return;
        }

        // �o�͂��郌�|�[�g�̎�ނɂ��킹�ăe���v���[�g�̃t�@�C���p�X���擾����
        String templateFilePath;
        try
        {
            templateFilePath = TemplateFileManager.getInstance().getTemplateFile(getReportType());
        }
        catch (IOException exception)
        {
            reportContainer.setHappendedError(exception);
            return;
        }

        // ���|�[�g�o�͂̈��������擾����
        ResponseTimeSummaryRecord[] records = (ResponseTimeSummaryRecord[])plotData;
        String outputFilePath = getOutputFileName();
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // ���|�[�g�o�͂����s����
        RecordReporter<ResponseTimeSummaryRecord> reporter =
                                                             new RecordReporter<ResponseTimeSummaryRecord>(
                                                                                                           getReportType());
        reporter.outputReport(templateFilePath, outputFilePath, records, startTime, endTime);
    }

}
