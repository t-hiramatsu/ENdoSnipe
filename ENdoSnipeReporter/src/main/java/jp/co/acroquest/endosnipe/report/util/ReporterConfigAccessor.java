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
package jp.co.acroquest.endosnipe.report.util;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.report.LogIdConstants;
import jp.co.acroquest.endosnipe.report.controller.ReportType;

/**
 * Reporter�R���|�[�l���g�̃R���t�B�O�t�@�C���ireporter.properties�j��
 * �����擾���邽�߂̃A�N�Z�T�N���X�B
 * 
 * @author M.Yoshida
 */
public class ReporterConfigAccessor
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER                  =
                                                                   ENdoSnipeLogger.getLogger(
                                                                                             ReporterConfigAccessor.class);
    
    /** �R���t�B�O�t�@�C���p�X */
    private static final String          PROPERTY_RESOURCE_PATH  = "/reporter.properties";
    
    /** �e���v���[�g�t�@�C�� */
    private static final String          REPORT_TEMPLATE_SUFFIX  = ".template";
    
    /** �v���Z�b�T */
    private static final String          REPORT_PROCESSOR_SUFFIX = ".processor";
    
    /** ���|�[�g���� */
    private static final String          REPORT_NAME_SUFFIX      = ".reportName";
    
    /** �o�̓t�@�C�� */
    private static final String          REPORT_OUTPUT_SUFFIX    = ".outputFile";
    
    /** ���� */
    private static final String          REPORT_EXPLANATION_SUFFIX    = ".explanation";
    
    /** �ݒ�ێ��t�B�[���h */
    private static Properties            configProperties__        = null;

    /**
     * �C���X�^���X���h�~�̂��߂̃R���X�g���N�^
     */
    private ReporterConfigAccessor()
    {
    }

    private static Properties getReporterConfigProperties()
    {
        if (configProperties__ != null)
        {
            return configProperties__;
        }

        URL templateSetting = ReporterConfigAccessor.class.getResource(PROPERTY_RESOURCE_PATH);
        configProperties__ = new Properties();

        try
        {
            configProperties__.load(templateSetting.openStream());
        }
        catch (IOException e)
        {
            LOGGER.log(LogIdConstants.READ_FAULT_CONFIG, e, new Object[0]);
        }

        return configProperties__;
    }

    /**
     * �w�肳�ꂽ�L�[�Ɋ֘A����v���p�e�B�l���擾����B
     * @param key �L�[
     * @return �L�[�Ɋ֘A����l
     */
    public static String getProperty(String key)
    {
        Properties configProp = getReporterConfigProperties();

        return configProp.getProperty(key);
    }

    /**
     * �w�肳�ꂽ�L�[�Ɋ֘A����v���p�e�B�ɁA�u���p�����[�^��K�p���Ď擾����B
     * 
     * @param key   �L�[
     * @param param �L�[�Ɋ֘A����l�ɓK�p����u���p�����[�^
     * @return �L�[�Ɋ֘A����l�i�u���ς݁j
     */
    public static String getPropertyWithParam(String key, Object... param)
    {
        String messagePattern = getProperty(key);
        return MessageFormat.format(messagePattern, param);
    }

    /**
     * �w�肵�����|�[�g�^�C�v�̃e���v���[�g�ւ̃p�X���v���p�e�B����擾����B
     * 
     * @param type ���|�[�g�^�C�v
     * @return �e���v���[�g�ւ̃p�X
     */
    public static String getReportTemplateResourcePath(ReportType type)
    {
        return getProperty(type.getId() + REPORT_TEMPLATE_SUFFIX);
    }

    /**
     * �w�肵�����|�[�g�^�C�v�̃��|�[�g�v���Z�b�T�̊��S���薼���v���p�e�B����擾����B
     * 
     * @param type ���|�[�g�^�C�v
     * @return ���|�[�g�v���Z�b�T�̊��S���薼
     */
    public static String getReportProcessorName(ReportType type)
    {
        return getProperty(type.getId() + REPORT_PROCESSOR_SUFFIX);
    }

    /**
     * �w�肵�����|�[�g�^�C�v�̃��|�[�g�����v���p�e�B����擾����B
     * 
     * @param type ���|�[�g�^�C�v
     * @return ���|�[�g��
     */
    public static String getReportName(ReportType type)
    {
        return getProperty(type.getId() + REPORT_NAME_SUFFIX);
    }

    /**
     * �w�肵�����|�[�g�^�C�v�̃��|�[�g�o�̓t�@�C�������擾����B
     * 
     * @param type ���|�[�g�^�C�v
     * @return ���|�[�g�o�̓t�@�C����
     */
    public static String getOutputFileName(ReportType type)
    {
        return getProperty(type.getId() + REPORT_OUTPUT_SUFFIX);
    }
    
    /**
     * �w�肵�����|�[�g�^�C�v�̐������擾����B
     * 
     * @param type ���|�[�g�^�C�v
     * @return ���|�[�g�̐���
     */
    public static String getExplanation(ReportType type)
    {
        return getProperty(type.getId() + REPORT_EXPLANATION_SUFFIX);
    }
}
