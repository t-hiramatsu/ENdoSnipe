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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.db.DBManager;

/**
 * �g�p�ł���f�[�^�x�[�X�ƁA�����̃f�[�^�ۑ����Ԃ�<br />
 * �ꗗ�\�����邽�߂̃R�}���h�ł��B
 *
 * @author y_asazuma
 */
public class ShowDBList
{
    /**
     * @param args �R�}���h���C�������͎g�p���Ȃ��B
     */
    public static void main(String[] args)
    {
        // �������w�肳�ꂽ�ꍇ��USAGE��\�����ďI��
        if (args.length != 0)
        {
            usage();
            System.exit(1);
        }

        // �ݒ�t�@�C���̓ǂݍ���
        DataCollectorConfig config = null;
        try
        {
            config = ConfigLoader.loadConfig();
        }
        catch (Exception e)
        {
            System.err.println(e);
            System.exit(1);
        }

        // �ڑ�����DB���̏�����
        DBManager.updateSettings(false, "",
                                 config.getDatabaseHost(),
                                 config.getDatabasePort(),
                                 config.getDatabaseName(),
                                 config.getDatabaseUserName(),
                                 config.getDatabasePassword());

        // Index����DB���ƃf�[�^�~�ϊ��Ԃ�\������
        List<AgentSetting> settingList = config.getAgentSettingList();
        for (AgentSetting setting : settingList)
        {
            // DB����f�[�^�~�ϊ��Ԃ��擾
            Timestamp[] term = null;
            try
            {
                term = MeasurementValueDao.getTerm(setting.databaseName);
            }
            catch (SQLException e)
            {
                System.err.println("�f�[�^�x�[�X�ڑ��Ŗ�肪�������܂����B");
                System.exit(1);
            }

            // �C���f�b�N�X�ƃf�[�^���̕\��
            System.out.println("Index : " + String.valueOf(setting.agentId) +
                               "\tDatabaseName : " + setting.databaseName);
            // �f�[�^�~�ϊ��Ԃ̕\��
            System.out.println("Accumulation period :\n\t" +
                               term[0].toString() + " - " + term[1].toString() + "\n");
        }
    }

    /**
     * �R�}���h���C�������̎g�p���@��������܂��B
     */
    private static void usage()
    {
        System.err.println("USAGE: ShowDBList");
    }
}
