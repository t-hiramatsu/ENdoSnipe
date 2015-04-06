/*
 * Copyright (c) 2004-2015 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.explorer.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.explorer.manager.DatabaseManager;

import org.springframework.stereotype.Service;

/**
 * JVNファイルダウンロード機能のクラス
 * @author kawasaki
 *
 */
@Service
public class JvnFileDownloadService
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(JvnFileDownloadService.class);

    /**JVNログ日付パターン */
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    /**
     * コンストラクタ
     */
    public JvnFileDownloadService()
    {

    }

    /**
     * 指定したファイルをJVNログファイルのzip圧縮形式にする。
     * @param zipFile ファイル
     * @param start 開始時刻
     * @param end 終了時刻
     * @return
     */
    public void writeFile(final OutputStream outputStream, final Timestamp start,
            final Timestamp end)
    {
        try
        {
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            List<JavelinLog> javelinLogList = this.getJavelinLog(start, end);
            for (JavelinLog javelinLog : javelinLogList)
            {
                String fileName = javelinLog.logFileName;
                InputStream inputStream = javelinLog.javelinLog;

                if (inputStream == null)
                {
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] dataBlock = new byte[1024];
                int count = inputStream.read(dataBlock, 0, 1024);
                while (count != -1)
                {
                    zipOutputStream.write(dataBlock, 0, count);
                    count = inputStream.read(dataBlock, 0, 1024);
                }
            }

            zipOutputStream.closeEntry();
            zipOutputStream.close();
        }
        catch (IOException ex)
        {
            LOGGER.log(LogMessageCodes.IO_ERROR, ex, ex.getMessage());
        }
    }

    /**
     * 指定した開始時刻～終了時刻に該当するJVNログを全て取得する。
     * @param start 開始時刻
     * @param end 終了時刻
     * @return JVNログファイルのリスト
     */
    private List<JavelinLog> getJavelinLog(final Timestamp start, final Timestamp end)
    {

        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        List<JavelinLog> javelinLogList = new ArrayList<JavelinLog>();
        try
        {
            javelinLogList = JavelinLogDao.selectByTermWithLog(dbName, start, end);
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, ex, ex.getMessage());
        }

        return javelinLogList;
    }
}
