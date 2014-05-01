/*
 * Copyright (c) 2004-2010 SMG Co., Ltd. All Rights Reserved.
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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.TemplateFileManager;
import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.dao.util.GraphItemAccessUtil;
import jp.co.acroquest.endosnipe.report.entity.ApplicationRecord;
import jp.co.acroquest.endosnipe.report.entity.ItemData;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * Javelin�̃C�x���g���̃��|�[�g�𐶐����郌�|�[�g�v���Z�b�T�ł��B
 * 
 * @author iida
 */
public class EventReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            EventReportProcessor.class);

    /**
     * ReportProcessor�𐶐����܂��B<br>
     * 
     * @param type ���|�[�g���
     */
    public EventReportProcessor(ReportType type)
    {
        super(type);
    }

    /**
     * {@inheritDoc}
     * 
     * @param cond ���|�[�g�o�͐ݒ�
     * @param reportContainer ���|�[�g�o�͏����̃R���e�i
     */
    @Override
    protected Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        // �����������擾���܂��B
        String database = cond.getDatabases().get(0);
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // DB����f�[�^���������܂��B
        List<ItemData> eventCountData;
        try
        {
            eventCountData = GraphItemAccessUtil.findItemData(
                    database, Constants.ITEMNAME_EVENT_COUNT,
                    CompressOperator.SIMPLE_AVERAGE, startTime, endTime);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
        }

        // �擾�����f�[�^��Map�ɂ܂Ƃ߂ă��^�[�����܂��B
        Map<String, List<?>> data = new HashMap<String, List<?>>();
        data.put(Constants.ITEMNAME_EVENT_COUNT, eventCountData);

        return data;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param reportContainer
     */
    @Override
    protected Object convertPlotData(Object rawData,
            ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        // �f�[�^�ϊ��͓��ɍs���܂���B
        return rawData;
    }

    /**
     * {@inheritDoc}
     * 
     * @param plotData �O���t�o�̓f�[�^
     * @param cond ���|�[�g�o�͐ݒ�
     * @param reportContainer ���|�[�g�o�͏����̃R���e�i
     */
    @Override
    protected void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        if (plotData instanceof Map == false)
        {
            return;
        }
        
        // Map����O���t�̃f�[�^���擾���܂��B
        Map<String, List<ItemData>> data = (Map<String, List<ItemData>>) plotData;

        List<ItemData> eventCountDataList = data.get(Constants.ITEMNAME_EVENT_COUNT);

        // �o�͂��郌�|�[�g�̎�ނɉ����āA�e���v���[�g�̃t�@�C���p�X���擾����B
        String templateFilePath;
        try
        {
            templateFilePath = TemplateFileManager.getInstance()
                    .getTemplateFile(ReportType.EVENT);
        }
        catch (IOException exception)
        {
            reportContainer.setHappendedError(exception);
            return;
        }

        // ���|�[�g�o�͂̈��������擾����B
        String outputFolderPath = getOutputFolderName()
                + File.separator
                + ReporterConfigAccessor.getProperty(super.getReportType()
                        .getId()
                        + ".outputFile");
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // ���|�[�g�o�͂����s����B
        RecordReporter<ApplicationRecord> reporter = new RecordReporter<ApplicationRecord>(
                getReportType());
        reporter.outputReports(templateFilePath, outputFolderPath,
                eventCountDataList, startTime, endTime);
    }
}
