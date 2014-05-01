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
package jp.co.acroquest.endosnipe.report.controller.dispatcher;

import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishDispatcher;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishProcessor;

/**
 * �e���|�[�g�̏����N���X�̎��s�̐U�蕪�����s���f�B�X�p�b�`��
 * 
 * @author M.Yoshida
 */
public class ReportPublishDispatcher
{
    /** ���K�[ */
    private static final ENdoSnipeLogger   LOGGER     =
                                                        ENdoSnipeLogger.getLogger(
                                                                                  ReportPublishDispatcher.class);

    /** �C���X�^���X�ێ��p�t�B�[���h */
    private static ReportPublishDispatcher instance__ = null;

    /** �C���X�^���X����h�~���邽�߂̃R���X�g���N�^ */
    private ReportPublishDispatcher()
    {
    }

    /**
     * �f�B�X�p�b�`���̃C���X�^���X���擾����B
     * 
     * @return �C���X�^���X
     */
    public static ReportPublishDispatcher getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new ReportPublishDispatcher();
        }
        return instance__;
    }

    /**
     * �w�肵�����|�[�g�^�C�v�̃��|�[�g���o�͂���v���Z�b�T���Ăяo���B
     * 
     * @param rType ���|�[�g�^�C�v
     * @param cond  �v���Z�b�T�ɓn���i���ݏ���
     * @return ���|�[�g�o�͏����̌���
     */
    public ReportProcessReturnContainer dispatch(ReportType rType, ReportSearchCondition cond)
    {
        String reportProcessorName = ReporterConfigAccessor.getReportProcessorName(rType);
        ReportProcessReturnContainer returnContainer = null;

        if (reportProcessorName == null)
        {
            return null;
        }

        ReportPublishProcessor processor = null;

        try
        {
            Class<?> reportProcessorClass = Class.forName(reportProcessorName);
            processor =
                        (ReportPublishProcessor)reportProcessorClass.getConstructor(
                                                                                    ReportType.class).newInstance(
                                                                                                                  rType);
        }
        catch (Exception e1)
        {
            returnContainer = new ReportProcessReturnContainer();
            returnContainer.setHappendedError(e1);
            LOGGER.log(LogIdConstants.EXCEPTION_HAPPENED, e1, new Object[0]);

            return returnContainer;
        }

        try
        {
            returnContainer = processor.publish(cond);
        }
        catch (Throwable e)
        {
            returnContainer = new ReportProcessReturnContainer();
            returnContainer.setHappendedError(e);
        }

        return returnContainer;
    }
}
