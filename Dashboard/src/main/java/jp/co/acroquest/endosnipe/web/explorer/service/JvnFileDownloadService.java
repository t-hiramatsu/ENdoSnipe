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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.web.explorer.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.explorer.dto.JvnFileSearchResultDto;
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

    /** 日付のフォーマット。 */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**JVNログ日付パターン */
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    /**
     * コンストラクタ
     */
    public JvnFileDownloadService()
    {

    }

    /**
     * 指定したログIDに該当するJVNログファイルを取得してzip圧縮形式にする。
     * @param outputStream 出力ストリーム
     * @param logIdList ログIDのリスト
     */
    public void writeFile(final OutputStream outputStream, final List<Long> logIdList)
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        try
        {
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            List<JavelinLog> javelinLogList =
                    JavelinLogDao.selectJavelinLogByLogIdList(dbName, logIdList, true);
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
        catch (SQLException ex)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, ex, ex.getMessage());
        }
        catch (IOException ex)
        {
            LOGGER.log(LogMessageCodes.IO_ERROR, ex, ex.getMessage());
        }
    }

    /**
     * 指定した開始時刻～終了時刻、アイテム名に該当するJVNログを全て取得する。
     * @param start 開始時刻
     * @param end 終了時刻
     * @param name アイテム名
     * @return JVNログファイルのリスト
     */
    public List<JvnFileSearchResultDto> getJavelinLog(final Timestamp start, final Timestamp end,
            final String name)
    {
        List<JvnFileSearchResultDto> resultList = new ArrayList<JvnFileSearchResultDto>();
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        try
        {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            List<JavelinLog> javelinLogList =
                    JavelinLogDao.selectByTermAndName(dbName, start, end, name, true);

            for (JavelinLog jevelinLog : javelinLogList)
            {
                JvnFileSearchResultDto resultDto = new JvnFileSearchResultDto();
                resultDto.setLogId(jevelinLog.logId);
                resultDto.setLogFileName(jevelinLog.logFileName);
                resultDto.setStartTime(dateFormat.format(jevelinLog.startTime));
                resultDto.setEndTime(dateFormat.format(jevelinLog.endTime));
                resultDto.setCalleeClass(jevelinLog.callerClass);
                resultDto.setCalleeName(jevelinLog.calleeName);
                resultDto.setElapsedTime(jevelinLog.elapsedTime);
                resultDto.setThreadName(jevelinLog.threadName);
                resultList.add(resultDto);
            }
        }
        catch (SQLException ex)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, ex, ex.getMessage());
        }

        return resultList;
    }
}
