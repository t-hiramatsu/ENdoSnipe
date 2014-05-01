/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
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
import jp.co.acroquest.endosnipe.report.dao.CpuAndMemoryRecordAccessor;
import jp.co.acroquest.endosnipe.report.entity.ProcessResourceRecord;
import jp.co.acroquest.endosnipe.report.output.ResourceReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * CPU/�������̎g�p�����|�[�g�𐶐����郌�|�[�g�v���Z�b�T
 * 
 * @author akiba
 */
public class CpuMemoryReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            CpuMemoryReportProcessor.class);

    /**
     * �v���Z�b�T�𐶐�����B
     * 
     * @param type ���̃v���Z�b�T���������|�[�g�̎�ށB
     */
    public CpuMemoryReportProcessor(ReportType type)
    {
        super(type);
    }

    /**
     * ���|�[�g�f�[�^���擾����B
     * 
     * @param cond ���������B
     * @param reportContainer ���|�[�g�o�͒��ɔ��������⑫�����i�[����ėp�R���e�i�B
     * @return ���|�[�g�f�[�^
     */
    @Override
    protected Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        // ���������̎擾
        String database = cond.getDatabases().get(0);
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // DB���猟��
        CpuAndMemoryRecordAccessor accessor = new CpuAndMemoryRecordAccessor();
        List<ProcessResourceRecord> data;
        try
        {
            data = accessor.findSystemResourceStaticsByTerm(database, startTime, endTime);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
        }

        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertPlotData(Object rawData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        List<ProcessResourceRecord> data = (List<ProcessResourceRecord>)rawData;
        return (ProcessResourceRecord[])data.toArray(new ProcessResourceRecord[data.size()]);
    }

    /**
     * ���|�[�g���o�͂���B
     * 
     * @param plotData ���|�[�g�f�[�^�B
     * @param cond ���|�[�g�f�[�^���擾�����ۂ̌��������B
     * @param reportContainer ���|�[�g�o�͒��ɔ��������⑫�����i�[����ėp�R���e�i�B
     */
    @Override
    protected void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        if ((plotData instanceof ProcessResourceRecord[]) == false)
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
        ProcessResourceRecord[] records = (ProcessResourceRecord[])plotData;
        String outputFilePath = getOutputFileName();
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // ���|�[�g�o�͂����s����
        ResourceReporter<ProcessResourceRecord> reporter =
                new ResourceReporter<ProcessResourceRecord>(this.getReportType());
        reporter.outputReport(templateFilePath, outputFilePath, records, startTime, endTime);
    }

}
