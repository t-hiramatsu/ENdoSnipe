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
 * 使用できるデータベースと、それらのデータ保存期間を<br />
 * 一覧表示するためのコマンドです。
 *
 * @author y_asazuma
 */
public class ShowDBList
{
    /**
     * @param args コマンドライン引数は使用しない。
     */
    public static void main(String[] args)
    {
        // 引数が指定された場合はUSAGEを表示して終了
        if (args.length != 0)
        {
            usage();
            System.exit(1);
        }

        // 設定ファイルの読み込み
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

        // 接続するDB情報の初期化
        DBManager.updateSettings(false, "",
                                 config.getDatabaseHost(),
                                 config.getDatabasePort(),
                                 config.getDatabaseName(),
                                 config.getDatabaseUserName(),
                                 config.getDatabasePassword());

        // Index毎にDB名とデータ蓄積期間を表示する
        List<AgentSetting> settingList = config.getAgentSettingList();
        for (AgentSetting setting : settingList)
        {
            // DBからデータ蓄積期間を取得
            Timestamp[] term = null;
            try
            {
                term = MeasurementValueDao.getTerm(setting.databaseName);
            }
            catch (SQLException e)
            {
                System.err.println("データベース接続で問題が発生しました。");
                System.exit(1);
            }

            // インデックスとデータ名の表示
            System.out.println("Index : " + String.valueOf(setting.agentId) +
                               "\tDatabaseName : " + setting.databaseName);
            // データ蓄積期間の表示
            System.out.println("Accumulation period :\n\t" +
                               term[0].toString() + " - " + term[1].toString() + "\n");
        }
    }

    /**
     * コマンドライン引数の使用方法を説明します。
     */
    private static void usage()
    {
        System.err.println("USAGE: ShowDBList");
    }
}
