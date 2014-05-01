/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.report.controller.ReportPublishTask;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishDispatcher;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * BottleneckEye���N�������ɁA���|�[�g�쐬���s�����߂̃N���X�ł��B<br>
 * 
 * @author iida
 */
public class Reporter
{

    /** �J�n�^�I���������w�肷�镶����`���B */
    public static final String           TIME_FORMAT = "yyyyMMdd_HHmmss";

    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER      = ENdoSnipeLogger
                                                             .getLogger(
                                                                     ReportPublishDispatcher.class);

    /**
     * �R���X�g���N�^�B
     */
    public Reporter()
    {

    }

    /**
     * ���|�[�g�쐬���s���܂��B<br/>
     * 
     * @param config
     *            DataCollector�̐ݒ�/�萔��ێ�����I�u�W�F�N�g
     * @param fmTime
     *            �J�n����
     * @param toTime
     *            �I������
     * @param reportPath
     *            �o�͐�f�B���N�g��
     * @param targetItemName
     *            ���|�[�g�o�͑Ώۂ̐e�̍��ږ�
     * @param reportName
     *            ���|�[�g��
     */
    public void createReport(DataCollectorConfig config, Calendar fmTime,
            Calendar toTime, String reportPath, String targetItemName,
            String reportName)
    {

        // �J�n�������I��������薢�����w���Ă����ꍇ�̓G���[
        if (fmTime.compareTo(toTime) > 0)
        {
            System.err.println("�J�n�������I��������薢�����w���Ă��܂��B");
            return;
        }

        if (config == null)
        {
            return;
        }

        // DB�̏��ݒ���擾
        String dbName = config.getDatabaseName();
        String dbHost = config.getDatabaseHost();
        String dbPort = config.getDatabasePort();
        String dbUser = config.getDatabaseUserName();
        String dbPass = config.getDatabasePassword();

        // ���|�[�g�쐬�Ɏg�p����DB���w�肷��
        DBManager.updateSettings(false, "", dbHost, dbPort, dbName, dbUser,
                dbPass);

        // ���|�[�g�쐬���̊e�ݒ���s��
        ReportType[] outputReportTypes = new ReportType[]{ReportType.OBJECT};

        // TODO PerformanceDoctor �̃��[���ݒ��L���ɂ���B
        // // PerformanceDoctor�̃��[����ݒ肷��
        // int selectionIndex = 0;
        // RuleManager ruleManager = RuleManager.getInstance();
        // RuleSetConfig[] ruleSetConfigs = ruleManager.getRuleSetConfigs();
        // String id = ruleSetConfigs[selectionIndex].getId();
        // ruleManager.changeActiveRuleSetByID(id);

        Runnable callback = null;

        // ���|�[�g�o�̓f�B���N�g�������肵�A���݂��Ȃ���΍쐬����
        // ���|�[�g�o�͐�f�B���N�g���F
        // <current-dir>/reports/<db-name>/<from>-<to>/
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        String start = format.format(fmTime.getTime());
        String end = format.format(toTime.getTime());
        String leafDirectoryName = reportName + "_" + start + "-" + end;

        String outputFilePath = reportPath + File.separator + dbName
                + File.separator + leafDirectoryName;

        File outputDir = new File(outputFilePath);
        if (outputDir.exists() == false)
        {
            outputDir.mkdirs();
        }

        // TODO �i�荞�݂̃��[����ݒ肷��
        boolean limitSameCause = false;
        boolean limitBySameRule = false;

        // ���|�[�g�o�͊��Ԃ̏�����ݒ肷��
        ReportSearchCondition searchCondition = new ReportSearchCondition();
        searchCondition.setDatabases(Arrays.asList(dbName));
        searchCondition.setStartDate(new Timestamp(fmTime.getTimeInMillis()));
        searchCondition.setEndDate(new Timestamp(toTime.getTimeInMillis()));
        searchCondition.setOutputFilePath(outputFilePath);
        searchCondition.setLimitSameCause(limitSameCause);
        searchCondition.setLimitBySameRule(limitBySameRule);

        // ��΃p�X���擾
        File currentDirectory = new File(".");

        String outputDirFullPath = currentDirectory.getAbsolutePath()
                + File.separator + reportPath + File.separator + dbName
                + File.separator;

        // ���|�[�g�������O�ɏo�͂���
        LOGGER.log(LogIdConstants.OUTPUT_REPORT_INFO, outputDirFullPath,
                leafDirectoryName, targetItemName);

        // ReportPublishTask�����s���A���|�[�g�쐬���s��
        try
        {
            ReportPublishTask reportTask = new ReportPublishTask(
                    searchCondition, outputReportTypes, callback);

            // ���|�[�g���o�͂���
            reportTask.createReport(targetItemName);
        }
        catch (Exception e)
        {
            LOGGER.log(LogIdConstants.REPORT_PUBLISH_STOPPED_WARN,
                    outputDirFullPath + leafDirectoryName);
            return;
        }

        // zip���k����
        Project project = new Project();
        project.init();

        try
        {
            File baseDir = new File(outputFilePath);
            Zip zipper = new Zip();
            zipper.setProject(project);
            zipper.setTaskName("zip");
            zipper.setTaskType("zip");
            zipper.setDestFile(new File(outputFilePath + ".zip"));
            zipper.setBasedir(baseDir);
            zipper.execute();

            // zip���ɐ��������猳�̃f�B���N�g���͍폜����
            boolean deleted = deleteDir(baseDir);
            if (deleted == false)
            {
                LOGGER.log(LogIdConstants.FAIL_TO_DELETE_DIR, outputDirFullPath
                        + leafDirectoryName);
            }
        }
        catch (BuildException bex)
        {
            LOGGER.log(LogIdConstants.FAIL_TO_ZIP, outputDirFullPath
                    + leafDirectoryName);
        }
    }

    /**
     * �w�肵���f�B���N�g�����ƍ폜����B
     * 
     * @param dir
     *            �폜����f�B���N�g���B
     * @return �f�B���N�g���̍폜�Ɏ��s�����ꍇ�B
     */
    private static boolean deleteDir(File dir)
    {
        boolean result = true;
        File[] children = dir.listFiles();
        for (File child : children)
        {
            if (child.isDirectory() == true)
            {
                // �f�B���N�g���͍ċA���č폜���s��
                result = deleteDir(child);
                if (result == false)
                {
                    break;
                }
            }
            else
            {
                // �t�@�C���͒P�ɍ폜���s��
                result = child.delete();
                if (result == false)
                {
                    break;
                }
            }
        }

        // �S�Ă̍폜�ɐ������Ă���Β��g�͋�Ȃ̂ŁA�����̃f�B���N�g�����폜����
        if (result == true)
        {
            result = dir.delete();
        }

        return result;
    }

    public static void main(String[] args) throws Exception
    {
        Reporter reporter = new Reporter();
        DataCollectorConfig config = new DataCollectorConfig();
        config.setDatabaseHost("126.0.56.101");
        config.setDatabasePort("5432");
        config.setDatabaseName("endosnipedb");
        config.setDatabaseUserName("postgres");
        config.setDatabasePassword("postgres");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        
        Date fmDate = format.parse("2013/06/04 13:00");
        Calendar fmTime = Calendar.getInstance();
        fmTime.setTime(fmDate);
        
        Date toDate = format.parse("2013/06/04 14:00");
        Calendar toTime = Calendar.getInstance();
        toTime.setTime(toDate);

        
        String reportPath = "report";
        String targetItemName = "/";
        String reportName = "test";
        reporter.createReport(config, fmTime, toTime, reportPath,
                targetItemName, reportName);
    }
}
