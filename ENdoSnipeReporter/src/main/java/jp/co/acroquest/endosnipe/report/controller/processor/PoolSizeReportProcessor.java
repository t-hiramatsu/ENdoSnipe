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
import java.util.ArrayList;
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
import jp.co.acroquest.endosnipe.report.entity.ItemData;
import jp.co.acroquest.endosnipe.report.entity.ItemRecord;
import jp.co.acroquest.endosnipe.report.entity.PoolSizeRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

import org.apache.commons.lang.StringUtils;

/**
 * AP�T�[�o�̃��[�J�X���b�h���̃��|�[�g�𐶐����郌�|�[�g�v���Z�b�T�B
 * 
 * @author iida
 */
public class PoolSizeReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            PoolSizeReportProcessor.class);

    /**
     * ReportProcessor�𐶐�����B<br>
     * 
     * @param type
     *            ���|�[�g���
     */
    public PoolSizeReportProcessor(ReportType type)
    {
        super(type);
    }

    /**
     * {@inheritDoc}
     * 
     * @param reportContainer
     */
    @Override
    protected Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        // �����������擾����B
        String database = cond.getDatabases().get(0);
        Timestamp startTime = cond.getStartDate();
        Timestamp endTime = cond.getEndDate();

        // DB����f�[�^����������B
        List<ItemData> workerThreadNumData = null;
        try
        {
            workerThreadNumData = GraphItemAccessUtil.findItemData(
                    database, Constants.ITEMNAME_SERVER_POOL,
                    CompressOperator.SIMPLE_AVERAGE, startTime, endTime);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
        }

        return workerThreadNumData;
    }

    /**
     * {@inheritDoc}
     * 
     * @param reportContainer
     */
    @SuppressWarnings("unchecked")
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
     * @param cond
     * @param reportContainer
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        List<ItemData> workerThreadNumData = (List<ItemData>) plotData;
        
        // ���ږ���"http-8080_max","http-8080_current","http-8080_wait"�ƂȂ��Ă���̂ŁA
        // "_"�̑O�����������̂��܂Ƃ߁A����̃O���t�ɏo�͂���B
        Map<String, List<PoolSizeRecord>> poolSizeRecordMap = new HashMap<String, List<PoolSizeRecord>>();
        for (ItemData itemData : workerThreadNumData) {
            String[] itemName = StringUtils.split(itemData.getItemName(), "_");
            if (!poolSizeRecordMap.containsKey(itemName[0])) {
                List<PoolSizeRecord> poolSizeRecords = new ArrayList<PoolSizeRecord>(200);
                for (ItemRecord itemRecord : itemData.getRecords()) {
                    PoolSizeRecord poolSizeRecord = new PoolSizeRecord();
                    poolSizeRecord.setMeasurementTime(itemRecord.getMeasurementTime());
                    poolSizeRecords.add(poolSizeRecord);
                }
                poolSizeRecordMap.put(itemName[0], poolSizeRecords);
            }
            for (int index = 0; index < itemData.getRecords().size(); index++) {
                ItemRecord itemRecord = itemData.getRecords().get(index);
                PoolSizeRecord poolSizeRecord = poolSizeRecordMap.get(itemName[0]).get(index);
                if ("max".equals(itemName[1])) {
                    poolSizeRecord.setPoolSizeMax(itemRecord.getValue());
                } else if ("current".equals(itemName[1])) {
                    poolSizeRecord.setPoolSizeCurrent(itemRecord.getValue());
                } else if ("wait".equals(itemName[1])) {
                    poolSizeRecord.setPoolSizeWait(itemRecord.getValue());
                }
            }
        }

        // �o�͂��郌�|�[�g�̎�ނɉ����āA�e���v���[�g�̃t�@�C���p�X���擾����B
        String templateFilePath;
        try
        {
            templateFilePath = TemplateFileManager.getInstance()
                    .getTemplateFile(ReportType.POOL_SIZE);
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
        RecordReporter<PoolSizeRecord> reporter = new RecordReporter<PoolSizeRecord>(
                getReportType());
        
        for (Map.Entry<String, List<PoolSizeRecord>> poolSizeRecordEntry : poolSizeRecordMap.entrySet())
        {
            PoolSizeRecord[] records = poolSizeRecordEntry.getValue().toArray(
                    new PoolSizeRecord[] {});
            String itemName = poolSizeRecordEntry.getKey();
            String[] graphTitles =
            { "AP�T�[�o�̃��[�J�X���b�h���i" + itemName + "�j" };
            reporter.outputReport(templateFilePath, outputFolderPath, itemName,
                    graphTitles, records, startTime, endTime);
        }
    }
}
