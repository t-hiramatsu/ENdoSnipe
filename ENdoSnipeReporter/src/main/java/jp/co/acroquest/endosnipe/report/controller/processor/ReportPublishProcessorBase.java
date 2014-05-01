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

import java.io.File;

import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishProcessor;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * ���|�[�g�o�̓v���Z�b�T�̃x�[�X�N���X�B
 * �e���|�[�g�^�C�v�ɑΉ����郌�|�[�g�o�̓v���Z�b�T�́A�{�N���X���p�����č쐬���邱�ƁB
 * 
 * 
 * @author M.Yoshida
 *
 */
public abstract class ReportPublishProcessorBase implements ReportPublishProcessor
{
    /** ���|�[�g�v���Z�b�T�̍H���̐� */
    public static final int     PROCESS_PHASE_NUM      = 3;

    /**  */
    private static final String GET_DATA_PHASE_KEY     = "reporter.report.progress.detail.getData";

    /**  */
    private static final String CONVERT_DATA_PHASE_KEY = "reporter.report.progress.detail.convData";

    /**  */
    private static final String OUTPUT_DATA_PHASE_KEY  = "reporter.report.progress.detail.output";

    /** ���|�[�g��� */
    private ReportType          rType_;

    /** �o�͐�f�B���N�g���� */
    private String              outputDir_;

    /**
     * �R���X�g���N�^�B
     * 
     * @param type ���|�[�g���
     */
    public ReportPublishProcessorBase(ReportType type)
    {
        rType_ = type;
    }

    /**
     * �������������郌�|�[�g��ʂ��擾����B
     * 
     * @return ���|�[�g���
     */
    protected ReportType getReportType()
    {
        return rType_;
    }

    /**
     * �o�͐�t�@�C���̃t�@�C�������擾����i�g���q�����j
     * 
     * @return �o�͐�t�@�C���̃t�@�C����
     */
    protected String getOutputFileName()
    {
        File outputFile = new File(outputDir_, ReporterConfigAccessor.getOutputFileName(rType_));
        return outputFile.getAbsolutePath();
    }

    /**
     * �o�͐�t�H���_�̃p�X���擾����
     * 
     * @return �o�͐�t�H���_�̃p�X
     */
    protected String getOutputFolderName()
    {
        File outputFolder = new File(outputDir_);
        return outputFolder.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    public ReportProcessReturnContainer publish(ReportSearchCondition cond)
        throws InterruptedException
    {
        outputDir_ = cond.getOutputFilePath();

        ReportProcessReturnContainer retContainer = new ReportProcessReturnContainer();

        Object rawData = getReportPlotData(cond, retContainer);


        if (rawData != null)
        {
            Object convertedPlotData = convertPlotData(rawData, cond, retContainer);

            outputReport(convertedPlotData, cond, retContainer);
        }

        return retContainer;
    }

    /**
     * DB�Ȃǂ̃G���e�B�e�B����A���|�[�g�ɏo�͂���f�[�^���擾����B
     * 
     * @param cond            �f�[�^�擾����
     * @param reportContainer �Ԃ�l�Ȃǂ�n�����߂̃R���e�i
     * @return �擾�������|�[�g�̃f�[�^
     */
    protected abstract Object getReportPlotData(ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer);

    /**
     * �G���e�B�e�B����擾�����f�[�^���A���ۂ̕\���̌^�ɕϊ�����B
     * �{���\�b�h�́A�ϊ����s��Ȃ��ꍇ�́A�I�[�o�[���C�h�s�v�B
     * 
     * @param rawData         �G���e�B�e�B����擾�������f�[�^
     * @param cond            �f�[�^�擾�^�ϊ�����
     * @param reportContainer �Ԃ�l�Ȃǂ�n�����߂̃R���e�i
     * @return �ϊ���̃f�[�^
     */
    protected Object convertPlotData(Object rawData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer)
    {
        return rawData;
    }

    /**
     * �f�[�^�����|�[�g�ɏo�͂���B
     * 
     * @param plotData        ���|�[�g�ɏo�͂���f�[�^
     * @param cond            �f�[�^�o�͏���
     * @param reportContainer �Ԃ�l�Ȃǂ�n�����߂̃R���e�i
     */
    protected abstract void outputReport(Object plotData, ReportSearchCondition cond,
            ReportProcessReturnContainer reportContainer);
}
