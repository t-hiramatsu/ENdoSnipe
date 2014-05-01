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
 * Attach API��p���ăN���X�q�X�g�O�������擾����B
 * �擾�����q�X�g�O�����͈ȉ��̌`���Ŏ擾�ł��邽�߁A�p�[�X�������s���A
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
    /** �N���X�q�X�g�O�������́A�C���X�^���X���̃C���f�b�N�X�B */
    private static final int INDEX_INSTANCES = 1;

    /** �N���X�q�X�g�O�������́A�T�C�Y(byte)�̃C���f�b�N�X�B */
    private static final int INDEX_BYTES = 2;

    /** �N���X�q�X�g�O�������́A�N���X���̃C���f�b�N�X�B */
    private static final int INDEX_CLASSNAME = 3;

    /** �N���X�q�X�g�O�����̃J�������B */
    private static final int CLASS_HISTOGRAM_COLUMNS = 4;

    /**
     * �q�X�g�O�����̕������ǂݍ���Reader�𐶐�����B
     * 
     * @param classHistoGC �q�X�g�O�����擾����GC���邩�ǂ���
     * @return�@�q�X�g�O�����̕������ǂݍ���Reader�B
     * @throws IOException �q�X�g�O�����擾����IO�G���[������
     */
    public BufferedReader newReader(boolean classHistoGC)
        throws IOException
    {
        return AttachUtil.getHeapHistoReader(classHistoGC);
    }

    /**
     * 1�s���p�[�X���āAClassHistogramEntry�𐶐�����B
     * @param splitLine 1�s
     * @return ClassHistogramEntry�A�p�[�X�Ɏ��s�����ꍇ��null
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
