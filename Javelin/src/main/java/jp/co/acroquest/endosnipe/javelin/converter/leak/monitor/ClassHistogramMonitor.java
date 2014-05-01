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
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

public abstract class ClassHistogramMonitor
{

    /** �N���X�q�X�g�O�����̃��X�g�B */
    private List<ClassHistogramEntry> histgramList_;

    /** �O����擾���̎����B */
    private volatile long             prevTime_;

    /** �ݒ� */
    private JavelinConfig             javelinConfig_;

    public ClassHistogramMonitor()
    {
        histgramList_ = new ArrayList<ClassHistogramEntry>();
        prevTime_ = 0L;
        javelinConfig_ = new JavelinConfig();
    }

    /**
     * �N���X�q�X�g�O�������擾���A���ʂ�Ԃ��B<br>
     * ���ׂ����炷���߁A�O��擾������������ݒ肵�����Ԍo�߂��Ă���ꍇ�̂ݍĎ擾���s���B<br>
     * ����ȊO�̏ꍇ�͑O��擾�����������̂܂ܕԂ��B<br>
     * �ݒ�p�����[�^�͈ȉ��̒ʂ�B<br>
     * <ul>
     * <li>javelin.leak.class.histo �N���X�q�X�g�O�������擾���邩�ǂ����B
     * <li>javelin.leak.class.histo.interval �N���X�q�X�g�O�����擾�Ԋu(�~���b)�B
     * <li>javelin.leak.class.histo.max �N���X�q�X�g�O�����̏�ʉ������擾���邩�B
     * </ul>
     *
     * �q�[�v�q�X�g�O�����擾�� OFF �̏ꍇ�́A <code>null</code> ��Ԃ��B<br />
     *
     * @return �擾�����N���X�q�X�g�O�����̌��ʁB
     */
    public List<ClassHistogramEntry> getHistogramList()
    {
        if (javelinConfig_.getClassHisto() == false)
        {
            return new ArrayList<ClassHistogramEntry>();
        }

        synchronized (histgramList_)
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime - prevTime_ > javelinConfig_.getClassHistoInterval())
            {
                updateHistogram();
                prevTime_ = currentTime;
            }
        }

        return histgramList_;
    }

    private void updateHistogram()
    {
        if (javelinConfig_.getClassHisto() == false)
        {
            return;
        }

        synchronized (histgramList_)
        {
            histgramList_.clear();
            BufferedReader heapHistoReader = null;
            try
            {
                boolean classHistoGC = javelinConfig_.getClassHistoGC();
                heapHistoReader = newReader(classHistoGC);
                if (heapHistoReader == null)
                {
                    // �q�[�v�q�X�g�O�������擾�ł��Ȃ��ꍇ�́AGC�̂ݎ��{����B
                    if (javelinConfig_.getClassHistoGC())
                    {
                        System.gc();
                    }

                    return;
                }

                String line;
                while ((line = heapHistoReader.readLine()) != null)
                {
                    if (SystemLogger.getInstance().isDebugEnabled())
                    {
                        SystemLogger.getInstance().debug(line);
                    }

                    String[] splitLine = line.trim().split(" +");
                    try
                    {
                        ClassHistogramEntry entry = parseEntry(splitLine);
                        if (entry == null)
                        {
                            continue;
                        }
                        histgramList_.add(entry);

                        if (histgramList_.size() >= javelinConfig_.getClassHistoMax())
                        {
                            break;
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                        SystemLogger.getInstance().warn(nfe);
                    }
                }
            }
            catch (IOException ioe)
            {
                SystemLogger.getInstance().warn(ioe);
            }
            finally
            {
                if (heapHistoReader != null)
                {
                    try
                    {
                        heapHistoReader.close();
                    }
                    catch (IOException ioe)
                    {
                        SystemLogger.getInstance().warn(ioe);
                    }
                }
            }
        }
    }

    /**
     * �q�X�g�O�����̕������ǂݍ���Reader�𐶐�����B
     * 
     * @param classHistoGC �q�X�g�O�����擾����GC���邩�ǂ���
     * @return�@�q�X�g�O�����̕������ǂݍ���Reader�B
     * @throws IOException �q�X�g�O�����擾����IO�G���[������
     */
    public abstract BufferedReader newReader(boolean classHistoGC)
        throws IOException;

    /**
     * 1�s���p�[�X���āAClassHistogramEntry�𐶐�����B
     * @param splitLine 1�s
     * @return ClassHistogramEntry�A�p�[�X�Ɏ��s�����ꍇ��null
     */
    protected abstract ClassHistogramEntry parseEntry(final String[] splitLine);
}
