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
package jp.co.acroquest.endosnipe.javelin.converter.leak.monitor;

import java.io.BufferedReader;
import java.io.IOException;

import jp.co.acroquest.endosnipe.javelin.common.AttachUtil;

/**
 * Attach APIを用いてクラスヒストグラムを取得する。
 * 取得したヒストグラムは以下の形式で取得できるため、パース処理を行い、
 * 
 * <pre>
 * num   #instances    #bytes  class name
 * --------------------------------------
 *   1:     68319     4985448  [C
 *   2:     39246     4661584  <constMethodKlass>
 *   3:     19963     3308768  [B
 *   4:     39246     3144680  <methodKlass>
 *   5:     55071     2329384  <symbolKlass>
 *   6:      3798     1927880  <constantPoolKlass>
 *   7:     70549     1693176  java.lang.String
 *   8:      3787     1658056  <instanceKlassKlass>
 *   9:     53196     1276704  java.util.HashMap$Entry
 *  10:      3277     1129696  <constantPoolCacheKlass>
 * </pre>
 * 
 * @author eriguchi
 */
public class SunClassHistogramMonitor extends ClassHistogramMonitor
{
    /** クラスヒストグラム中の、インスタンス数のインデックス。 */
    private static final int INDEX_INSTANCES = 1;

    /** クラスヒストグラム中の、サイズ(byte)のインデックス。 */
    private static final int INDEX_BYTES = 2;

    /** クラスヒストグラム中の、クラス名のインデックス。 */
    private static final int INDEX_CLASSNAME = 3;

    /** クラスヒストグラムのカラム数。 */
    private static final int CLASS_HISTOGRAM_COLUMNS = 4;

    /**
     * ヒストグラムの文字列を読み込むReaderを生成する。
     * 
     * @param classHistoGC ヒストグラム取得時にGCするかどうか
     * @return　ヒストグラムの文字列を読み込むReader。
     * @throws IOException ヒストグラム取得時にIOエラーが発生
     */
    public BufferedReader newReader(boolean classHistoGC)
        throws IOException
    {
        return AttachUtil.getHeapHistoReader(classHistoGC);
    }

    /**
     * 1行をパースして、ClassHistogramEntryを生成する。
     * @param splitLine 1行
     * @return ClassHistogramEntry、パースに失敗した場合はnull
     */
    public ClassHistogramEntry parseEntry(final String[] splitLine)
    {
        if (splitLine.length != CLASS_HISTOGRAM_COLUMNS)
        {
            return null;
        }
        
        ClassHistogramEntry entry = new ClassHistogramEntry();
        entry.setInstances(Integer.parseInt(splitLine[INDEX_INSTANCES]));
        entry.setBytes(Integer.parseInt(splitLine[INDEX_BYTES]));
        entry.setClassName(splitLine[INDEX_CLASSNAME]);
        return entry;
    }
}
