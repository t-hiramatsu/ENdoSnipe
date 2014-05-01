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

import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;
import jp.co.acroquest.endosnipe.common.util.CSVTokenizer;
import jp.co.acroquest.endosnipe.report.output.ResourceReporter;

import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.BlockRowRepeatParamParser;
import org.bbreak.excella.reports.tag.RowRepeatParamParser;
import org.bbreak.excella.reports.tag.SingleParamParser;

/**
 * ���\�[�X�̃��|�[�g���o�͂���N���X
 * 
 * @author eriguchi
 * @param <T> ���\�[�X��\���G���e�B�e�B
 * 
 */
public class ResourceReporter<T>
{

    /** �Q�Ƃ���e���v���[�g�̃V�[�g�� */
    public static final String TEMPLATE_SHEET_NAME  = "�V�X�e�����\�[�X���|�[�g";

    /** �o�̓V�[�g�� */
    public static final String OUTPUT_SHEET_NAME    = "�V�X�e�����\�[�X���|�[�g";

    /** �u���p�����[�^�� */
    public static final String RESPONSE_TIME_RECORD = "systemResourceRecord";

    /** ���Ԃ�\���p�����[�^�� */
    public static final String NUMBERS              = "numbers";

    /** �J�n������\������p�����[�^�� */
    public static final String DATA_RANGE           = "dataRange";
    
    /** �Q�Ƃ���e���v���[�g�̃V�[�g���̃��X�g */
    private String[] templateSheetNames_;
    
    /**
     * �R���X�g���N�^�B<br />
     * �v���p�e�B�t�@�C������A�e���v���[�g�t�@�C���̃V�[�g���ꗗ���擾���܂��B<br />
     * 
     * @param type ���|�[�g�̎��
     */
    public ResourceReporter(ReportType type)
    {
        String id = type.getId();
        
        // �e���v���[�g�t�@�C���̃V�[�g���̈ꗗ���擾����B
        String templateSheetNames = ReporterConfigAccessor.getProperty(id + ".templateSheetNames");
        CSVTokenizer tokenizer = new CSVTokenizer(templateSheetNames);
        int tokenCount = tokenizer.countTokens();
        this.templateSheetNames_ = new String[tokenCount];
        int index = 0;
        while (tokenizer.hasMoreTokens())
        {
        	this.templateSheetNames_[index] = tokenizer.nextToken();
        	index++;
        }
    }

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
            T[] records, Date startDate, Date endDate)
    {
        // �@�ǂݍ��ރe���v���[�g�t�@�C���̃p�X(�g���q��)
        // �A�o�͐�̃t�@�C���p�X(�g���q��Exporter�ɂ���Ď����I�ɕt�^����邽�߁A�s�v�B)
        // �B�t�@�C���t�H�[�}�b�g(ConvertConfiguration�̔z��)
        // ���w�肵�AReportBook�C���X�^���X�𐶐�����B
        ReportBook outputBook =
                                new ReportBook(templateFilePath, outputFilePath,
                                               ExcelExporter.FORMAT_TYPE);

        for (String templateSheetName : this.templateSheetNames_)
        {
        	// �e���v���[�g�t�@�C�����̃V�[�g���Əo�̓V�[�g�����w�肵�A
            // ReportSheet�C���X�^���X�𐶐����āAReportBook�ɒǉ�����B
            ReportSheet outputDataSheet = new ReportSheet(templateSheetName, templateSheetName);
            outputBook.addReportSheet(outputDataSheet);

            // �u���p�����[�^��ReportSheet�I�u�W�F�N�g�ɒǉ�����B
            // (�����u���̃p�����[�^�ɂ͔z���n���B)
            List<Integer> numberList = new ArrayList<Integer>();
            for (int index = 0; index < records.length; index++)
            {
                numberList.add(index + 1);
            }
            outputDataSheet.addParam(BlockRowRepeatParamParser.DEFAULT_TAG,
                                     ResourceReporter.RESPONSE_TIME_RECORD, records);

            //�\�̈�ԍ��[�̗�ɍ��ڔԍ���ǉ�
            outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG, ResourceReporter.NUMBERS,
                                     numberList.toArray());

            //��������(��) ����:���� ����  ��������(��) ����:���� �܂ł̃f�[�^�擾���ʂł�
            //�Ƃ����������\��������
            String dataRange = this.getDataRangeString(startDate, endDate);
            outputDataSheet.addParam(SingleParamParser.DEFAULT_TAG, ResourceReporter.DATA_RANGE,
                                     dataRange);
        }

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
        builder.append(startDateString);
        builder.append(" ���� ");
        builder.append(endDateString);
        builder.append(" �܂ł̃f�[�^�擾���ʂł�");

        String returnValue = builder.toString();

        return returnValue;
    }
}
