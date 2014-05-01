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
import jp.co.acroquest.endosnipe.report.entity.ServerPoolRecord;
import jp.co.acroquest.endosnipe.report.output.RecordReporter;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

import org.apache.commons.lang.StringUtils;

/**
 * Commons Pool�̃T�C�Y�̃��|�[�g�𐶐����郌�|�[�g�v���Z�b�T�B
 * 
 * @author iida
 */
public class ServerPoolReportProcessor extends ReportPublishProcessorBase
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
            ServerPoolReportProcessor.class);

    /**
     * ReportProcessor�𐶐�����B<br>
     * 
     * @param type
     *            ���|�[�g���
     */
    public ServerPoolReportProcessor(ReportType type)
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
        List<ItemData> commonsPoolSizeData = null;
        try
        {
            commonsPoolSizeData = GraphItemAccessUtil.findItemData(
                    database, Constants.ITEMNAME_POOL_SIZE,
                    CompressOperator.SIMPLE_AVERAGE, startTime, endTime);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogIdConstants.EXCEPTION_IN_READING, ex,
                    ReporterConfigAccessor.getReportName(getReportType()));
            return null;
        }

        return commonsPoolSizeData;
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
        List<ItemData> commonsPoolSizeData = (List<ItemData>) plotData;
        
        // ���ږ���"Max:ConnectionPoolImpl@56ff3d","Num:ConnectionPoolImpl@56ff3d"�ƂȂ��Ă���̂ŁA
        // ":"�̌オ���������̂��܂Ƃ߁A����̃O���t�ɏo�͂���B
        Map<String, List<ServerPoolRecord>> serverPoolRecordMap = new HashMap<String, List<ServerPoolRecord>>();
        for (ItemData itemData : commonsPoolSizeData) {
            String[] itemName = StringUtils.split(itemData.getItemName(), ":");
            if (!serverPoolRecordMap.containsKey(itemName[1])) {
                List<ServerPoolRecord> serverPoolRecords = new ArrayList<ServerPoolRecord>(200);
                for (ItemRecord itemRecord : itemData.getRecords()) {
                    ServerPoolRecord serverPoolRecord = new ServerPoolRecord();
                    serverPoolRecord.setMeasurementTime(itemRecord.getMeasurementTime());
                    serverPoolRecords.add(serverPoolRecord);
                }
                serverPoolRecordMap.put(itemName[1], serverPoolRecords);
            }
            for (int index = 0; index < itemData.getRecords().size(); index++) {
                ItemRecord itemRecord = itemData.getRecords().get(index);
                ServerPoolRecord serverPoolRecord = serverPoolRecordMap.get(itemName[1]).get(index);
                if ("Max".equals(itemName[0])) {
                    serverPoolRecord.setServerPoolMax(itemRecord.getValue());
                } else if ("Num".equals(itemName[0])) {
                    serverPoolRecord.setServerPoolNum(itemRecord.getValue());
                }
            }
        }

        // �o�͂��郌�|�[�g�̎�ނɉ����āA�e���v���[�g�̃t�@�C���p�X���擾����B
        String templateFilePath;
        try
        {
            templateFilePath = TemplateFileManager.getInstance()
                    .getTemplateFile(ReportType.SERVER_POOL);
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
        RecordReporter<ServerPoolRecord> reporter = new RecordReporter<ServerPoolRecord>(
                getReportType());
        
        for (Map.Entry<String, List<ServerPoolRecord>> serverPoolRecordEntry : serverPoolRecordMap.entrySet())
        {
            ServerPoolRecord[] records = serverPoolRecordEntry.getValue().toArray(
                    new ServerPoolRecord[] {});
            String itemName = serverPoolRecordEntry.getKey();
            String[] graphTitles =
            { "Commons Pool�̃T�C�Y�i" + itemName + "�j" };
            reporter.outputReport(templateFilePath, outputFolderPath, itemName,
                    graphTitles, records, startTime, endTime);
        }
    }
}
