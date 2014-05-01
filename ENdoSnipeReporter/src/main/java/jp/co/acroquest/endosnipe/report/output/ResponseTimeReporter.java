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
package jp.co.acroquest.endosnipe.report.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.report.entity.ResponseTimeRecord;
import jp.co.acroquest.endosnipe.report.output.ResponseTimeReporter;

import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.BlockRowRepeatParamParser;
import org.bbreak.excella.reports.tag.RowRepeatParamParser;
import org.bbreak.excella.reports.tag.SingleParamParser;

/**
 * ���X�|���X�^�C���̃��|�[�g���o�͂���N���X
 * 
 * @author Y.Ochiai
 * 
 */
public class ResponseTimeReporter
{

    /** �Q�Ƃ���e���v���[�g�̃V�[�g�� */
    public static final String TEMPLATE_SHEET_NAME  = "���X�|���X�^�C�����|�[�g";

    /** �o�̓V�[�g�� */
    public static final String OUTPUT_SHEET_NAME    = "���X�|���X�^�C�����|�[�g";

    /** �u���p�����[�^�� */
    public static final String RESPONSE_TIME_RECORD = "responseTimeRecord";

    /** ���Ԃ�\���p�����[�^�� */
    public static final String NUMBERS              = "numbers";

    /** �J�n������\������p�����[�^�� */
    public static final String DATA_RANGE           = "dataRange";

    /**
     * ���|�[�g�̃G�N�Z���t�@�C�����o�͂���
     * 
     * @param templateFilePath
     *            �e���v���[�g�t�@�C���̃p�X
     * @param outputFilePath
     *            �o�͂���t�@�C���̃p�X
     * @param records
     *            �o�͂���f�[�^�̃��X�g
     * @param startDate
     *            �J�n����
     * @param endDate
     *            �I������
     */
    public void outputReport(String templateFilePath, String outputFilePath,
            ResponseTimeRecord[] records, Date startDate, Date endDate)
    {
        // ?�ǂݍ��ރe���v���[�g�t�@�C���̃p�X(�g���q��)
        // ?�o�͐�̃t�@�C���p�X(�g���q��Exporter�ɂ���Ď����I�ɕt�^����邽�߁A�s�v�B)
        // ?�t�@�C���t�H�[�}�b�g(ConvertConfiguration�̔z��)
        // ���w�肵�AReportBook�C���X�^���X�𐶐�����B
        ReportBook outputBook =
                                new ReportBook(templateFilePath, outputFilePath,
                                               ExcelExporter.FORMAT_TYPE);

        // �e���v���[�g�t�@�C�����̃V�[�g���Əo�̓V�[�g�����w�肵�A
        // ReportSheet�C���X�^���X�𐶐����āAReportBook�ɒǉ����܂��B
        ReportSheet outputDataSheet =
                                      new ReportSheet(ResponseTimeReporter.TEMPLATE_SHEET_NAME,
                                                      ResponseTimeReporter.OUTPUT_SHEET_NAME);
        outputBook.addReportSheet(outputDataSheet);

        // �u���p�����[�^��ReportSheet�I�u�W�F�N�g�ɒǉ�����B
        // (�����u���̃p�����[�^�ɂ͔z���n���B)
        List<Integer> numberList = new ArrayList<Integer>();
        for (int index = 0; index < records.length; index++)
        {
            numberList.add(index + 1);
        }
        outputDataSheet.addParam(BlockRowRepeatParamParser.DEFAULT_TAG,
                                 ResponseTimeReporter.RESPONSE_TIME_RECORD, records);

        //�\�̈�ԍ��[�̗�ɍ��ڔԍ���ǉ�
        outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG, ResponseTimeReporter.NUMBERS,
                                 numberList.toArray());

        //��������(��) ����:���� ����  ��������(��) ����:���� �܂ł̃f�[�^�擾���ʂł�
        //�Ƃ����������\��������
        String dataRange = this.getDataRangeString(startDate, endDate);
        outputDataSheet.addParam(SingleParamParser.DEFAULT_TAG, ResponseTimeReporter.DATA_RANGE,
                                 dataRange);

        // 
        // ReportProcessor�C���X�^���X�𐶐����A
        // ReportBook�����Ƀ��|�[�g���������s���܂��B
        // 
        ReportProcessor reportProcessor = new ReportProcessor();
        try
        {
            reportProcessor.process(outputBook);
        }
        catch (Exception e)
        {
            // �o�͎��s
            e.printStackTrace();
        }
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

        //�f�[�^�擾�J�n�����ƃf�[�^�擾�I�������𐬌^����
        calendar.setTime(startDate);
        String startDateString = String.format("%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);
        calendar.setTime(endDate);
        String endDateString = String.format("%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);

        //�\���p������𐬌^����
        StringBuilder builder = new StringBuilder();
        builder.append("Result of data acquisition from ");
        builder.append(startDateString);
        builder.append(" to ");
        builder.append(endDateString);

        String returnValue = builder.toString();

        return returnValue;
    }

}
