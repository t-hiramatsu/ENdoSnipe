/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.javelin.converter.file.monitor;

import static jp.co.acroquest.endosnipe.javelin.converter.file.FileMonitorConstants.KEY_FILE_READ_LENGTH;
import static jp.co.acroquest.endosnipe.javelin.converter.file.FileMonitorConstants.KEY_FILE_THREAD_READ_LENGTH;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.converter.util.StreamMonitorUtil;


/**
 * FileInputStreamのデータ量を監視するクラス
 * 
 * @author yamasaki
 * 
 */
public class FileInputStreamMonitor
{
    /**
     * デフォルトコンストラクタ
     */
    private FileInputStreamMonitor()
    {
        // Do Nothing.
    }

    /**
     * コード埋め込み処理により、メソッド実行後に呼ばれるメソッド
     * 
     * @param size ファイル入力量
     */
    public static void postProcess(final int size)
    {
        // ファイル入力量が負の値のときは、そのままメソッドを終了する。
        if (size < 0)
        {
            return;
        }

        JavelinConfig config = new JavelinConfig();
        if (config.isFileInputMonitor())
        {
            StreamMonitorUtil.recordStreamAmount(size, KEY_FILE_THREAD_READ_LENGTH,
                                                 KEY_FILE_READ_LENGTH);
        }
    }
}
