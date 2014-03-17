/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogInputStreamAccessor;
import jp.co.acroquest.endosnipe.data.LogMessageCodes;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.javelin.parser.ParseException;

/**
 * JavelinLog変換用のユーティリティクラス
 * 
 * @author hiramatsu
 *
 */
public class JavelinLogConvertUtil
{
    /** ロガー */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(JavelinLogConvertUtil.class);

    /**
     * インスタンス化を避けるためのprivateコンストラクタです。<br />
     */
    private JavelinLogConvertUtil()
    {
    }

    /**
     * jvnLogListからJavelinLogElementのリストを生成します。
     * @param jvnLogList {@link JavelinLog}の配列
     * @param javelinLogElementList {@link JavelinLogElement}のリスト
     */
    public static void createJvnLogElementList(final List<JavelinLog> jvnLogList,
        final List<JavelinLogElement> javelinLogElementList)
    {
        System.out.println("#JvnLog: " + jvnLogList.size());
        System.out.println("JvnLog.JvnLog: " + jvnLogList.get(0).javelinLog);
        for (JavelinLog jvnLog : jvnLogList)
        {
            InputStream javelinLogStream = jvnLog.javelinLog;
            if (javelinLogStream == null)
            {
                continue;
            }

            JavelinLogInputStreamAccessor javelinLogMemoryAccessor =
                new JavelinLogInputStreamAccessor(jvnLog.logFileName, javelinLogStream);

            String measurementItemName = jvnLog.measurementItemName;
            parseJvnLogStream(javelinLogElementList, javelinLogStream, javelinLogMemoryAccessor,
                              measurementItemName);
        }
    }

    /**
     * Javelinログのストリームを解析し、JavelinLogElementオブジェクトを作成します。
     * @param javelinLogElementList {@link JavelinLogElement}オブジェクトのリスト
     * @param javelinLogStream Javelinログのストリーム
     * @param javelinLogMemoryAccessor {@link JavelinLogInputStreamAccessor}オブジェクト
     * @param measurementItemName 計測項目名
     */
    private static void parseJvnLogStream(final List<JavelinLogElement> javelinLogElementList,
        final InputStream javelinLogStream,
        final JavelinLogInputStreamAccessor javelinLogMemoryAccessor,
        final String measurementItemName)
    {
        JavelinParser javelinParser = new JavelinParser(javelinLogMemoryAccessor);

        try
        {
            javelinParser.init();
            JavelinLogElement javelinLogElement;
            while ((javelinLogElement = javelinParser.nextElement()) != null)
            {
                javelinLogElement.setMeasurementItemName(measurementItemName);
                javelinLogElementList.add(javelinLogElement);
            }
        }
        catch (ParseException ex)
        {
            LOGGER.log(LogMessageCodes.FAIL_PARSE_JVNLOG, ex);
        }
        catch (IOException ex)
        {
            LOGGER.log(LogMessageCodes.IO_ERROR, ex);
        }
        finally
        {
            if (javelinLogStream != null)
            {
                try
                {
                    javelinLogStream.close();
                }
                catch (IOException ex)
                {
                    LOGGER.log(LogMessageCodes.IO_ERROR, ex);
                }
            }
        }
    }
}
